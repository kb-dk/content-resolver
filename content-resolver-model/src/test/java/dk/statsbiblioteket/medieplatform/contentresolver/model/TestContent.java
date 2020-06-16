package dk.statsbiblioteket.medieplatform.contentresolver.model;

/*
 * #%L
 * content-resolver-model
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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringWriter;
import java.net.URI;
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
            + "       [\"http://example.com/3\"]\n"
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
        assertTrue(xmlDiff.identical(), xmlDiff.toString());
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
        ObjectMapper om = new ObjectMapper();
        om.writeValue(writer, content);
        String result = writer.toString();

        // Test for equality
        JSONAssert.assertEquals(new JSONObject(EXPECTED_JSON), new JSONObject(result), false);
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
        resource.setUris(Arrays.asList(new URI("http://example.com/1"), new URI("http://example.com/2")));
        content.addResource(resource);
        Resource resource2 = new Resource();
        resource2.setType("test2");
        resource2.setUris(Arrays.asList(new URI("http://example.com/3")));
        content.addResource(resource2);
        return content;
    }
}
