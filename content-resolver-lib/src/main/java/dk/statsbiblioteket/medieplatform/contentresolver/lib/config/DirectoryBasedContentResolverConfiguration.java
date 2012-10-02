package dk.statsbiblioteket.medieplatform.contentresolver.lib.config;

import dk.statsbiblioteket.medieplatform.contentresolver.model.Resource;

import java.io.File;
import java.util.regex.Pattern;

/** ConfigurableContentResolverConfiguration for a directory based content resolver. */
public class DirectoryBasedContentResolverConfiguration {
    /** The type of content resolved. See {@link Resource#getType()}. */
    private String type;
    /** Base directory for the content resolved by this resolver. */
    private File baseDirectory;
    /**
     * Number of characters to use for splitting content into directories. Example: if the is 2, the file "hello.txt"
     * will be in the path "h/e/hello.txt".
     */
    private int characterDirs;
    /**
     * Pattern used for turning the pid into a file name regex. The pattern uses the format of
     * {@link java.util.Formatter}, where the pid string is inserted as first parameter. The result should be a
     * regex {@link Pattern} that files should match to be included. Example: "%s-[0-9]*.mpg"
     */
    private String filenameRegexPattern;
    /**
     * The URI where the content may be resolved.  The pattern uses the format of {@link java.util.Formatter},
     * where the relative full file path is inserted as first parameter, and the file name is inserted as the second
     * parameter. E.g. http://example.com/resolve/%s may turn into
     * http://example.com/resolve/8/8/1/4/88144228-38ce-4f84-9ea4-115caab84297.mpg whereas
     * http://example.com/resolve/%2$s may turn into
     * http://example.com/resolve/88144228-38ce-4f84-9ea4-115caab84297.mpg.
     */
    private String uriPattern;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File getBaseDirectory() {
        return baseDirectory;
    }

    public void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public int getCharacterDirs() {
        return characterDirs;
    }

    public void setCharacterDirs(int characterDirs) {
        this.characterDirs = characterDirs;
    }

    public String getFilenameRegexPattern() {
        return filenameRegexPattern;
    }

    public void setFilenameRegexPattern(String filenameRegexPattern) {
        this.filenameRegexPattern = filenameRegexPattern;
    }

    public String getUriPattern() {
        return uriPattern;
    }

    public void setUriPattern(String uriPattern) {
        this.uriPattern = uriPattern;
    }
}
