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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

/** Test directory based lookup. */
public class DirectoryBasedContentResolverTest {
    /**
     * Test looking up files in directory structure.
     *
     * @throws Exception
     */
    @Test
    public void testGetContent() throws Exception {
        // Find directories with test resources.
        File previewsDirectory = new File(
                getClass().getClassLoader().getResource("previews/README_previews.txt").getPath()).getParentFile();
        File thumbnailsDirectory = new File(
                getClass().getClassLoader().getResource("thumbnails/README_thumbnails.txt").getPath()).getParentFile();
        File streamsDirectory = new File(
                getClass().getClassLoader().getResource("streams/README_streams.txt").getPath()).getParentFile();

        // Lookup a resource in the previews directory
        Content content = new DirectoryBasedContentResolver("preview", previewsDirectory, 4, 1,
                                                            "%s\\.preview\\.(flv)|(mp3)",
                                                            "rtsp://example.com/bart/preview/%s")
                .getContent("00001ecd-f3d8-4aac-a486-093e45b079a0");

        // Check result: Exactly one uri of type preview.
        assertEquals(1, content.getResources().size());
        assertEquals("preview", content.getResources().get(0).getType());
        assertEquals(1, content.getResources().get(0).getUris().size());
        assertEquals(
                new URI("rtsp://example.com/bart/preview/0/0/0/0/00001ecd-f3d8-4aac-a486-093e45b079a0.preview.flv"),
                content.getResources().get(0).getUris().get(0));

        // Lookup a resource in the thumbnails directory.
        content = new DirectoryBasedContentResolver("thumbnails", thumbnailsDirectory, 4, 1, "%s\\.snapshot\\..*\\.jpeg",
                                                    "http://example.com/bart/thumbnail/%2$s")
                .getContent("00001ecd-f3d8-4aac-a486-093e45b079a0");

        // Check result: 5 uris in one resource of type thumbnail.
        assertEquals(1, content.getResources().size());
        assertEquals("thumbnails", content.getResources().get(0).getType());
        assertEquals(5, content.getResources().get(0).getUris().size());
        assertEquals(new URI("http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.0.jpeg"),
                     content.getResources().get(0).getUris().get(0));
        assertEquals(new URI("http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.1.jpeg"),
                     content.getResources().get(0).getUris().get(1));
        assertEquals(new URI("http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.2.jpeg"),
                     content.getResources().get(0).getUris().get(2));
        assertEquals(new URI("http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.3.jpeg"),
                     content.getResources().get(0).getUris().get(3));
        assertEquals(
                new URI("http://example.com/bart/thumbnail/00001ecd-f3d8-4aac-a486-093e45b079a0.snapshot.preview.0.jpeg"),
                content.getResources().get(0).getUris().get(4));
        
        content = new DirectoryBasedContentResolver("streams", streamsDirectory, 2, 2, "%s",
                "http://example.com/bart/streams/%s")
                .getContent("00001234-07c0-41f7-9a4e-ce7b2ef69954");

        assertEquals(1, content.getResources().size());
        assertEquals("streams", content.getResources().get(0).getType()); 
        assertEquals(1, content.getResources().get(0).getUris().size());
        assertEquals(new URI("http://example.com/bart/streams/00/00/00001234-07c0-41f7-9a4e-ce7b2ef69954"),
                content.getResources().get(0).getUris().get(0));
    }
    
    @Test
    public void testOldConstructor() throws URISyntaxException {
        File previewsDirectory = new File(
                getClass().getClassLoader().getResource("previews/README_previews.txt").getPath()).getParentFile();
     // Lookup a resource in the previews directory
        Content content = new DirectoryBasedContentResolver("preview", previewsDirectory, 4,
                                                            "%s\\.preview\\.(flv)|(mp3)",
                                                            "rtsp://example.com/bart/preview/%s")
                .getContent("00001ecd-f3d8-4aac-a486-093e45b079a0");

        // Check result: Exactly one uri of type preview.
        assertEquals(1, content.getResources().size());
        assertEquals("preview", content.getResources().get(0).getType());
        assertEquals(1, content.getResources().get(0).getUris().size());
        assertEquals(
                new URI("rtsp://example.com/bart/preview/0/0/0/0/00001ecd-f3d8-4aac-a486-093e45b079a0.preview.flv"),
                content.getResources().get(0).getUris().get(0));
    }
    
    @Test
    public void testShortFilename() throws URISyntaxException {
        File streamsDirectory = new File(
                getClass().getClassLoader().getResource("streams/README_streams.txt").getPath()).getParentFile();

        Content content = new DirectoryBasedContentResolver("streams", streamsDirectory, 3, 2, "%s",
                "http://example.com/bart/streams/%s")
                .getContent("hello");

        assertEquals(1, content.getResources().size());
        assertEquals("streams", content.getResources().get(0).getType()); 
        assertEquals(1, content.getResources().get(0).getUris().size());
        assertEquals(new URI("http://example.com/bart/streams/he/ll/o/hello"),
                content.getResources().get(0).getUris().get(0));
    }
}
