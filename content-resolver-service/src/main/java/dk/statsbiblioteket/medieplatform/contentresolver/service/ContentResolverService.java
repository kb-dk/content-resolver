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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/")
public class ContentResolverService {
    private final ContentResolver contentResolver;

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
        Map<String,Content> idContentPairs = new HashMap<String,Content>();

        for (String pid : ids) {
            Content content = new Content();
            // Remove prefixed "uuid:" if it is there
            if (pid.contains(":")) {
                pid = pid.substring(pid.indexOf(':') + 1);
            }

            content.addResources(contentResolver.getContent(pid)
                    .getResources());

            idContentPairs.put(pid, content);
        }

        return idContentPairs;
    }
}
