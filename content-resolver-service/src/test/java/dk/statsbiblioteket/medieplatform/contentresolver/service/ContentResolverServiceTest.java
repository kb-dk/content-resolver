package dk.statsbiblioteket.medieplatform.contentresolver.service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import net.sf.json.JSONObject;
import net.sf.json.test.JSONAssert;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * Test webservice behaves as expected.
 */
public class ContentResolverServiceTest {
    private static final String EXPECTED_JSON
            = "{\"resource\":[{\"type\":\"preview\",\"url\":\"rtsp://example.com/bart/preview/0/0/0/0/00001ecd-f3d8-4aac-a486-093e45b079a0.preview.flv\"},{\"type\":\"thumbnails\",\"url\":[\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.0.jpeg\",\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.1.jpeg\",\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.2.jpeg\",\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.3.jpeg\",\"http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.preview.0.jpeg\"]}]}\n";

    @Test
    public void testWebservice() throws Exception {
        // Setup webservice
        HttpServerFactory.create("http://localhost:12345/", new ClassNamesResourceConfig(ContentResolverService.class))
                .start();

        // Setup JNDI context
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
        InitialContext ic = new InitialContext();

        ic.createSubcontext("java:");
        ic.createSubcontext("java:comp");
        ic.createSubcontext("java:comp/env");
        ic.createSubcontext("java:comp/env/contentresolver");
        ic.bind("java:comp/env/contentresolver/configfile",
                getClass().getClassLoader().getResource("testconfiguration.xml").getFile());

        // Call webservice
        Client c = Client.create();
        WebResource wr = c.resource("http://localhost:12345/");
        String result = wr.path("content").path("00001ecd-f3d8-4aac-a486-093e45b079a0").accept("application/json")
                .get(String.class);

        // Check result
        JSONAssert.assertEquals("Should get expected json", JSONObject.fromObject(EXPECTED_JSON),
                                JSONObject.fromObject(result));
    }
}
