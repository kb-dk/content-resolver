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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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
    public DirectoryBasedContentResolver(String type, File baseDirectory, int characterDirs, String filenameRegexPattern, 
                                         String uriPattern) {
        this(type, baseDirectory, characterDirs, 1, filenameRegexPattern, uriPattern);
    }
    
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
        int startIdx = 0, stopIdx = 0;
        int iterations = getIterations(pid);
        for (int i = 0; i < iterations; i++) {
            startIdx = (i * characterDirsWidth);
            stopIdx = Math.min((startIdx + characterDirsWidth), pid.length());
            String pathChar = pid.substring(startIdx, stopIdx);
            directory = new File(directory, pathChar);
            uriPath += pathChar + "/";
        }
        final String filenameRegex = String.format(Locale.ROOT, filenameRegexPattern, pid);


        List<URI> uris = new ArrayList<>();
        /* Files.listFiles returns File objects, which require system stats-calls (to get their size).
           Files.newDirectoryStream returns Paths, which does not require these calls.
           Unless the size is needed for most of the accessed files, the newDirectoryStream is much preferred,
           especially for network attached file systems.
           See also https://www.slideshare.net/GregBanks1/java-hates-linux-deal-with-it page 30-44 */
        try (DirectoryStream<Path> candidatePaths = Files.newDirectoryStream(directory.toPath())) {
            for (Path candidatePath : candidatePaths) {
                final String filename = candidatePath.getFileName().toString();
                if (filename.matches(filenameRegex)) {
                    try {
                        uris.add(new URI(String.format(Locale.ROOT, uriPattern, uriPath + filename, filename)));
                    } catch (URISyntaxException e) {
                        // URI is not added
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Exception while listing content of " + directory.toPath(), e);
        }

        if (!uris.isEmpty()) {
            Collections.sort(uris);
            Resource resource = new Resource();
            resource.setType(type);
            resource.setUris(uris);
            result.addResource(resource);
        }
        return result;
    }
    
    /**
     * Get the number of iterations (or levels of directories) needed. 
     * The number of iterations is the lesser of {@link #characterDirs} and 
     * pid.length/{@link #characterDirsWidth} rounded up. 
     * @param pid The PID that is being resolved. 
     * @return The number of iterations needed.  
     */
    private int getIterations(String pid) {
        int pidLengthLimitation = (int) Math.ceil(((double) pid.length()) / characterDirsWidth);
        int iterations = Math.min(characterDirs, pidLengthLimitation);
        return iterations;
    }
    
}
