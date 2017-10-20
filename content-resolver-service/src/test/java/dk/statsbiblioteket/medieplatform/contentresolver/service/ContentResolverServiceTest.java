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

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.net.httpserver.HttpServer;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameAlreadyBoundException;
import javax.ws.rs.core.MediaType;
import java.util.Collections;

/**
 * Test webservice behaves as expected.
 */
public class ContentResolverServiceTest {
    private static final String EXPECTED_JSON_SINGLE_UUID
            = "{\"00001ecd-f3d8-4aac-a486-093e45b079a0\":{\"resource\":[{\"url\":[\"rtsp://example.com/bart/preview/0/0/0/0/00001ecd-f3d8-4aac-a486-093e45b079a0.preview.flv\"],\"type\":\"preview\"},{\"url\":[\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.0.jpeg\",\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.1.jpeg\",\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.2.jpeg\",\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.3.jpeg\",\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.preview.0.jpeg\"],\"type\":\"thumbnails\"}]}}";
    private static final String EXPECTED_JSON_SINGLE_UUID_WITH_PREFIX
            = "{\"uuid:00001ecd-f3d8-4aac-a486-093e45b079a0\":{\"resource\":[{\"url\":[\"rtsp://example.com/bart/preview/0/0/0/0/00001ecd-f3d8-4aac-a486-093e45b079a0.preview.flv\"],\"type\":\"preview\"},{\"url\":[\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.0.jpeg\",\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.1.jpeg\",\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.2.jpeg\",\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.3.jpeg\",\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.preview.0.jpeg\"],\"type\":\"thumbnails\"}]}}";
    private static final String EXPECTED_JSON_MULTIPLE_UUIDS
            = "{\"00001ecd-f3d8-4aac-a486-093e45b079a0\":{\"resource\":[{\"url\":[\"rtsp://example.com/bart/preview/0/0/0/0/00001ecd-f3d8-4aac-a486-093e45b079a0.preview.flv\"],\"type\":\"preview\"},{\"url\":[\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.0.jpeg\",\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.1.jpeg\",\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.2.jpeg\",\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.3.jpeg\",\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.preview.0.jpeg\"],\"type\":\"thumbnails\"}]},\"00004513-07c0-41f7-9a4e-ce7b2ef69954\":{\"resource\":[{\"url\":[\"rtsp://example.com/bart/preview/0/0/0/0/00004513-07c0-41f7-9a4e-ce7b2ef69954.preview.flv\"],\"type\":\"preview\"},{\"type\":\"stream\",\"url\":[\"http://example.com/bart/streams/00/00/00004513-07c0-41f7-9a4e-ce7b2ef69954\"]},{\"url\":[\"http://example.com/bart/thumbnail/00004513-07c0-41f7-9a4e-ce7b2ef69954.snapshot.0.jpeg\",\"http://example.com/bart/thumbnail/00004513-07c0-41f7-9a4e-ce7b2ef69954.snapshot.1.jpeg\",\"http://example.com/bart/thumbnail/00004513-07c0-41f7-9a4e-ce7b2ef69954.snapshot.2.jpeg\",\"http://example.com/bart/thumbnail/00004513-07c0-41f7-9a4e-ce7b2ef69954.snapshot.preview.0.jpeg\",\"http://example.com/bart/thumbnail/00004513-07c0-41f7-9a4e-ce7b2ef69954.snapshot.preview.1.jpeg\"],\"type\":\"thumbnails\"}]}}";
    private WebResource wr;
    private HttpServer httpServer;

    // JSONAssert strict checking (i.e. don't consider array order important) is disabled by passing false at 3rd arg.
    private final boolean IGNORE_ORDER = false; 

    @Before
    public void setUp() throws Exception {
        // Setup JNDI context
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
        InitialContext ic = new InitialContext();

        try {
            ic.createSubcontext("java:");
            ic.createSubcontext("java:comp");
            ic.createSubcontext("java:comp/env");
            ic.createSubcontext("java:comp/env/contentresolver");
            ic.bind("java:comp/env/contentresolver/configfile",
                    getClass().getClassLoader().getResource("testconfiguration.xml").getFile());
        } catch (NameAlreadyBoundException e) {
            // Ignore
        }

        // Setup webservice
        ClassNamesResourceConfig rc = new ClassNamesResourceConfig(ContentResolverService.class);
        rc.setPropertiesAndFeatures(Collections.singletonMap(
                "com.sun.jersey.api.json.POJOMappingFeature", (Object) Boolean.TRUE));
        httpServer = HttpServerFactory
                .create("http://localhost:12345/", rc);
        httpServer.start();
        Client c = Client.create();
        wr = c.resource("http://localhost:12345/");
    }

    @After
    public void shutDown() throws Exception {
        httpServer.stop(0);
    }

    @Test
    public void testWebservice() throws Exception {
        // Call webservice
        String result = wr.path("content/").queryParam("id", "00001ecd-f3d8-4aac-a486-093e45b079a0").accept(MediaType.APPLICATION_JSON)
                .get(String.class);

        // Check result
        JSONObject expectedJson = new JSONObject(EXPECTED_JSON_SINGLE_UUID);
        JSONObject jsonFromService = new JSONObject(result);
        JSONAssert.assertEquals(expectedJson, jsonFromService, IGNORE_ORDER);
    }

    @Test
    public void testWebserviceWithUuidPrefix() throws Exception {
        // Call webservice
        String result = wr.path("content").queryParam("id", "uuid:00001ecd-f3d8-4aac-a486-093e45b079a0").accept(MediaType.APPLICATION_JSON)
                .get(String.class);

        // Check result
        JSONObject expectedJson = new JSONObject(EXPECTED_JSON_SINGLE_UUID_WITH_PREFIX);
        JSONObject jsonFromService = new JSONObject(result);
        JSONAssert.assertEquals(expectedJson, jsonFromService, IGNORE_ORDER);
    }

    @Test
    public void testWebserviceWithMultipleUuids() throws Exception {
        // Call webservice
        String result = wr.path("content").queryParam("id", "00001ecd-f3d8-4aac-a486-093e45b079a0")
                .queryParam("id", "00004513-07c0-41f7-9a4e-ce7b2ef69954").accept(MediaType.APPLICATION_JSON)
                .get(String.class);

        // Check result
        JSONObject expectedJson = new JSONObject(EXPECTED_JSON_MULTIPLE_UUIDS);
        JSONObject jsonFromService = new JSONObject(result);
        JSONAssert.assertEquals(expectedJson, jsonFromService, IGNORE_ORDER);
    }
}
