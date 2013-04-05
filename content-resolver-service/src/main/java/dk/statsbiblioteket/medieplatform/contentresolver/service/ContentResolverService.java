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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/")
public class ContentResolverService implements ContentResolver {
    private final ContentResolver contentResolver;

    public ContentResolverService() {
        this.contentResolver = new ConfigurableContentResolver();
    }

    /**
     * Given a PID, return a list of content disseminations.
     *
     * @param pid The pid of the content to lookup.
     * @return Dissemination of the content.
     */
    @GET
    @Path("content/?{queryString}")
    @Produces({"text/xml", "application/json"})
    public Content getContent(@PathParam("queryString") String queryString) {
        String[] queryParts = queryString.split("&");
        Content gatheredContent = new Content();

        for (String pidDefinition : queryParts) {
            String pid;

            if (pidDefinition.substring(0, 3).equals("id=")) {
                pid = pidDefinition.substring(3);
            } else {
                return new Content();
            }

            // Remove prefixed "uuid:" if it is there
            if (pid.contains(":")) {
                pid = pid.substring(pid.indexOf(':') + 1);
            }

            gatheredContent.addResources(contentResolver.getContent(pid)
                    .getResources());
        }

        return gatheredContent;
    }
}
