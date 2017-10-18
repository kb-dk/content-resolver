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
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/** Test configurable content resolver. */
public class ConfigurableContentResolverTest {
    @Test
    public void testConfiguredContentResolver() throws Exception {
        ContentResolver contentResolver = new ConfigurableContentResolver(getClass().getClassLoader().getResource("testconfiguration.xml").getPath());
        // Lookup a resource in the previews directory
        Content content = contentResolver.getContent("00001ecd-f3d8-4aac-a486-093e45b079a0");

        // Check result: Expecting two types, one 'preview' one 'thumbnails'.
        assertEquals(2, content.getResources().size());
        Set<String> resourceTypes = new HashSet<>();
        for(Resource r : content.getResources()) {
            resourceTypes.add(r.getType());
        }
        assertTrue(resourceTypes.contains("preview"));
        assertTrue(resourceTypes.contains("thumbnails"));
        
        // Check result: verify that the 'preview' and 'thumbnails' content types contain the expected urls.
        
        for(Resource r : content.getResources()) {
            if(r.getType().equals("preview")) {
                assertEquals(1, r.getUris().size());
                assertEquals(
                        new URI("rtsp://example.com/bart/preview/0/0/0/0/00001ecd-f3d8-4aac-a486-093e45b079a0.preview.flv"),
                        r.getUris().get(0));
            } else if(r.getType().equals("thumbnails")) {
                assertEquals(5, r.getUris().size());
                assertEquals(new URI("http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.0.jpeg"),
                             r.getUris().get(0));
                assertEquals(new URI("http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.1.jpeg"),
                             r.getUris().get(1));
                assertEquals(new URI("http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.2.jpeg"),
                             r.getUris().get(2));
                assertEquals(new URI("http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.3.jpeg"),
                             r.getUris().get(3));
                assertEquals(
                        new URI("http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.preview.0.jpeg"),
                        r.getUris().get(4));               
            } else {
                assertFalse("Got an unexpected type: '" + r.getType() + "'", 
                        r.getType().equals("preview") || r.getType().equals("thumbnails"));
            }
        }
    }
}
