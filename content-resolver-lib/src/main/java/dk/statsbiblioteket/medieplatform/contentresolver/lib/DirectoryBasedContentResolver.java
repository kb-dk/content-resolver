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

import dk.statsbiblioteket.medieplatform.contentresolver.model.Content;
import dk.statsbiblioteket.medieplatform.contentresolver.model.Resource;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Look up content in a filesystem, based on directories.
 */
public class DirectoryBasedContentResolver implements ContentResolver {
    /**
     * The type of content resolved. See {@link Resource#getType()}.
     */
    private final String type;

    /**
     * Base directory for the content resolved by this resolver.
     */
    private final File baseDirectory;

    /**
     * Number of characters to use for splitting content into directories. Example: if the is 2, the file "hello.txt"
     * will be in the path "h/e/hello.txt".
     */
    private final int characterDirs;
    
    /**
     * Width of each characterDirs split. Example: if it is 2, and characterDirs = 2, the file "hello.txt" 
     * will be in the path "he/ll/hello.txt"
     */
    private final int characterDirsWidth;

    /**
     * Pattern used for turning the pid into a file name regex. The pattern uses the format of
     * {@link java.util.Formatter}, where the pid string is inserted as first parameter. The result should be a
     * regex {@link Pattern} that files should match to be included. Example: "%s-[0-9]*.mpg"
     */
    private final String filenameRegexPattern;

    /**
     * The URI where the content may be resolved.  The pattern uses the format of {@link java.util.Formatter},
     * where the relative full file path is inserted as first parameter, and the file name is inserted as the second
     * parameter. E.g. http://example.com/resolve/%s may turn into
     * http://example.com/resolve/8/8/1/4/88144228-38ce-4f84-9ea4-115caab84297.mpg whereas
     * http://example.com/resolve/%2$s may turn into
     * http://example.com/resolve/88144228-38ce-4f84-9ea4-115caab84297.mpg.
     */
    private final String uriPattern;

    /**
     * Initialise directorybased content resolver.
     *
     * @param type The type of content resolved. See {@link Resource#getType()}.
     * @param baseDirectory Base directory for the content resolved by this resolver.
     * @param characterDirs Number of characters to use for splitting content into directories. Example: if the is 2,
     *                      the file "hello.txt" will be in the path "h/e/hello.txt".
     * @param characterDirsWidth Width of each characterDirs split. Example: if it is 2, and characterDirs = 2, the file "hello.txt" 
     *                        will be in the path "he/ll/hello.txt"
     * @param filenameRegexPattern Pattern used for turning the pid into a file name. The pattern uses the format of
     *                        {@link java.util.Formatter}, where the pid string is inserted as first parameter.
     *                        Example: "%s.mpg"
     * @param uriPattern The URI where the content may be resolved. The pattern uses the format of
     *                   {@link java.util.Formatter}, where the relative full file path is inserted as first parameter,
     *                   and the file name is inserted as the second parameter. E.g.
     *                   http://example.com/resolve/%s may turn into
     *                   http://example.com/resolve/8/8/1/4/88144228-38ce-4f84-9ea4-115caab84297.mpg
     *                   whereas
     *                   http://example.com/resolve/%2$s may turn into
     *                   http://example.com/resolve/88144228-38ce-4f84-9ea4-115caab84297.mpg
     */
    public DirectoryBasedContentResolver(String type, File baseDirectory, int characterDirs, int characterDirsWidth, 
                                         String filenameRegexPattern, String uriPattern) {
        this.type = type;
        this.baseDirectory = baseDirectory;
        this.characterDirs = characterDirs;
        this.characterDirsWidth = characterDirsWidth;
        this.filenameRegexPattern = filenameRegexPattern;
        this.uriPattern = uriPattern;
    }

    /**
     * Given a PID, return a list of content disseminations present in the configured directory of the configured form.
     *
     * @param pid The pid of the content to lookup.
     * @return Dissemination of the content.
     */
    public Content getContent(String pid) {
        final Content result = new Content();
        if (pid == null || pid.isEmpty()) {
            return result;
        }
        File directory = baseDirectory;
        String uriPath = "";
        int iterations = Math.min(characterDirs, (pid.length() / characterDirsWidth));
        for (int i = 0; i < iterations; i++) {
            int startIdx = i * characterDirsWidth;
            int stopIdx = startIdx + (characterDirsWidth); 
            String pathChar = pid.substring(startIdx, stopIdx);
            directory = new File(directory, pathChar);
            uriPath += pathChar + "/";
        }
        final String filenameRegex = String.format(filenameRegexPattern, pid);

        File[] files = directory.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.matches(filenameRegex);
            }
        });
        if (files != null && files.length > 0) {
            List<URI> uris = new ArrayList<URI>();
            for (File file : files) {
                try {
                    uris.add(new URI(String.format(uriPattern, uriPath + file.getName(), file.getName())));
                } catch (URISyntaxException e) {
                    // URI is not added
                }
            }
            Collections.sort(uris);
            Resource resource = new Resource();
            resource.setType(type);
            resource.setUris(uris);
            result.addResource(resource);
        }
        return result;
    }
}
