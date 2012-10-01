package dk.statsbiblioteket.medieplatform.contentresolver.model;

import static org.junit.Assert.*;

import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONMarshaller;
import net.sf.json.JSONObject;
import net.sf.json.test.JSONAssert;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.net.URL;
import java.util.Arrays;

/**
 * Test Content class.
 */
public class TestContent {
    private static final String EXPECTED_XML
            = "<content xmlns=\"http://medieplatform.statsbiblioteket.dk/contentresolver/\">\n"
            + "  <resource>"
            + "    <type>test</type>"
            + "    <url>http://example.com/1</url>"
            + "    <url>http://example.com/2</url>"
            + "  </resource>"
            + "  <resource>"
            + "    <type>test2</type>"
            + "    <url>http://example.com/3</url>"
            + "  </resource>"
            + "</content>";
    private static final String EXPECTED_JSON
            = "{\"resource\":\n"
            + "  [\n"
            + "    {\"type\":\n"
            + "      \"test\",\n"
            + "     \"url\":\n"
            + "      [\"http://example.com/1\",\n"
            + "       \"http://example.com/2\"\n"
            + "      ]\n"
            + "    },\n"
            + "    {\"type\":\n"
            + "      \"test2\",\n"
            + "     \"url\":\n"
            + "       \"http://example.com/3\"\n"
            + "    }\n"
            + "  ]\n"
            + "}\n";

    /**
     * Test that a test content object serializes to XML as expected.
     *
     * @throws Exception On any error.
     */
    @Test
    public void testXmlSerialization() throws Exception {
        Content content = getTestContent();

        // Serialize to XML
        StringWriter writer = new StringWriter();
        Marshaller marshaller = JAXBContext.newInstance(Content.class).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(content, writer);
        String result = writer.toString();

        // Test for equality
        XMLUnit.setIgnoreWhitespace(true);
        Diff xmlDiff = XMLUnit.compareXML(EXPECTED_XML, result);
        assertTrue(xmlDiff.toString(), xmlDiff.identical());
    }

    /**
     * Test that a test content object serializes to JSON as expected.
     *
     * @throws Exception On any error.
     */
    @Test
    public void testJaxbSerialization() throws Exception {
        Content content = getTestContent();

        // Serialize to JSON
        StringWriter writer = new StringWriter();
        JSONMarshaller marshaller = new JSONJAXBContext(Content.class).createJSONMarshaller();
        marshaller.setProperty(JSONMarshaller.FORMATTED, true);
        marshaller.marshallToJSON(content, writer);
        String result = writer.toString();

        // Test for equality
        JSONAssert.assertEquals(JSONObject.fromObject(EXPECTED_JSON), JSONObject.fromObject(result));
    }

    /**
     * Get a test content object.
     *
     * @return A test content object.
     *
     * @throws Exception on any test errors.
     */
    private Content getTestContent() throws Exception {
        Content content = new Content();
        Resource resource = new Resource();
        resource.setType("test");
        resource.setUrls(Arrays.asList(new URL("http://example.com/1"), new URL("http://example.com/2")));
        content.addResource(resource);
        Resource resource2 = new Resource();
        resource2.setType("test2");
        resource2.setUrls(Arrays.asList(new URL("http://example.com/3")));
        content.addResource(resource2);
        return content;
    }
}
