package dk.statsbiblioteket.medieplatform.contentresolver.lib;

/*
 * #%L
 * content-resolver-lib
 * %%
 * Copyright (C) 2012 The State and University Library, Denmark
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import dk.statsbiblioteket.medieplatform.contentresolver.model.Content;
import dk.statsbiblioteket.medieplatform.contentresolver.model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Given a list of content resolvers, combine the results into one.
 */
public class CombiningContentResolver implements ContentResolver {
    private final List<ContentResolver> contentResolvers;
    private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    public CombiningContentResolver(List<ContentResolver> contentResolvers) {
        this.contentResolvers = contentResolvers;
    }

    /**
     * Given a PID, return the list of content disseminations defined by combining a set of other content resolvers.
     *
     * @param pid The pid of the content to lookup.
     * @return Dissemination of the content.
     */
    public Content getContent(final String pid) {
        final Content content = new Content();

        List<Callable<Void>> callables = new ArrayList<Callable<Void>>(contentResolvers.size());
        for (final ContentResolver contentResolver : contentResolvers) {
            callables.add(new Callable<Void>() {
                @Override
                public Void call() {
                    List<Resource> resources = contentResolver.getContent(pid).getResources();
                    for (Resource resource : resources) {
                        content.addResource(resource);
                    }
                    return null;
                }
            });
        }
        try {
            THREAD_POOL.invokeAll(callables, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            //Ignore and return what we have
        }
        return content;
    }
}
