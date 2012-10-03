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

import org.junit.Test;

import dk.statsbiblioteket.medieplatform.contentresolver.model.Content;
import dk.statsbiblioteket.medieplatform.contentresolver.model.Resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Test combining content resolver.
 */
public class CombiningContentResolverTest {
    @Test
    public void testGetContent() throws Exception {
        // Create two content resolvers
        ContentResolver contentResolver1 = new ContentResolver() {
            public Content getContent(String pid) {
                return getTestContent(pid + "1", "http://example.com/1a", "http://example.com/1b");
            }
        };
        ContentResolver contentResolver2 = new ContentResolver() {
            public Content getContent(String pid) {
                return getTestContent(pid + "2", "http://example.com/2a");
            }
        };

        // Combine them
        ContentResolver combiningContentResolver
                = new CombiningContentResolver(Arrays.asList(contentResolver1, contentResolver2));

        // Get result
        Content result = combiningContentResolver.getContent("testpid");

        //Check result
        assertEquals("Should have the two combined results.", 2, result.getResources().size());
        Resource resultResource1 = result.getResources().get(0);
        Resource resultResource2 = result.getResources().get(1);
        // Check types
        assertTrue("Should have one result with first type",
                   resultResource1.getType().equals("testpid1") || resultResource2.getType().equals("testpid1"));
        assertTrue("Should have one result with second type",
                   resultResource1.getType().equals("testpid2") || resultResource2.getType().equals("testpid2"));
        // Swap them for easier checking if swapped
        if (!resultResource1.getType().equals("testpid1")) {
            Resource temp = resultResource1;
            resultResource1 = resultResource2;
            resultResource2 = temp;
        }
        // Check uris
        assertEquals("Result from first resolver should have two uris", 2, resultResource1.getUris().size());
        assertTrue(resultResource1.getUris().contains(new URI("http://example.com/1a")));
        assertTrue(resultResource1.getUris().contains(new URI("http://example.com/1b")));
        assertEquals("Result from second resolver should have one uri", 1, resultResource2.getUris().size());
        assertTrue(resultResource2.getUris().contains(new URI("http://example.com/2a")));

    }

    private Content getTestContent(String type, String... uriStrings) {
        Content content = new Content();
        ArrayList<Resource> resources = new ArrayList<Resource>();
        Resource resource1a = new Resource();
        resource1a.setType(type);
        List<URI> uris = new ArrayList<URI>();
        try {
            for (String uriString : uriStrings) {
                uris.add(new URI(uriString));
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.toString(), e);
        }
        resource1a.setUris(uris);
        resources.add(resource1a);
        content.setResources(resources);
        return content;
    }
}
