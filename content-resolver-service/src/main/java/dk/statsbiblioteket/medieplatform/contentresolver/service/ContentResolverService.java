package dk.statsbiblioteket.medieplatform.contentresolver.service;

/*
 * #%L
 * content-resolver-service
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

import dk.statsbiblioteket.medieplatform.contentresolver.lib.ConfigurableContentResolver;
import dk.statsbiblioteket.medieplatform.contentresolver.lib.ContentResolver;
import dk.statsbiblioteket.medieplatform.contentresolver.model.Content;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Path("/")
public class ContentResolverService {
    private final ContentResolver contentResolver;
    private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    public ContentResolverService() {
        this.contentResolver = new ConfigurableContentResolver();
    }

    /**
     * Given a PID, return a list of content disseminations.
     *
     * @param ids The ids of the content to lookup.
     * @return Dissemination of the content.
     */
    @GET
    @Path("content/")
    //@Produces({"application/json"})
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,Content> getContent(@QueryParam("id") List<String> ids) {
        final Map<String,Content> idContentPairs = Collections.synchronizedMap(new HashMap<String,Content>());

        List<Callable<Void>> callables = new ArrayList<Callable<Void>>(ids.size());
        for (final String id : ids) {
            callables.add(new Callable<Void>() {
                @Override
                public Void call() {
                    String cleanid = id;
                    Content content = new Content();
                    // Remove prefixed "uuid:" if it is there
                    if (cleanid.contains(":")) {
                        cleanid = cleanid.substring(cleanid.lastIndexOf(':') + 1);
                    }

                    content.setResources(contentResolver.getContent(cleanid).getResources());

                    idContentPairs.put(id, content);
                    return null;
                }
            });
        }
        try {
            THREAD_POOL.invokeAll(callables, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            //Ignore and return what we have
        }
        return idContentPairs;
    }
}
