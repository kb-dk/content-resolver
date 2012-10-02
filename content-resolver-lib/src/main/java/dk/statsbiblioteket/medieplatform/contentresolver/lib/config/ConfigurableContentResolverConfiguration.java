package dk.statsbiblioteket.medieplatform.contentresolver.lib.config;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/** ConfigurableContentResolverConfiguration of configurable content resolver. */
@XmlRootElement
public class ConfigurableContentResolverConfiguration {
    /** List of configurations for directory based content resolvers. */
    private List<DirectoryBasedContentResolverConfiguration> directoryBasedContentResolverConfigurations;

    /**
     * Get list of configurations for directory based content resolvers.
     *
     * @return List of configurations for directory based content resolvers.
     */
    public List<DirectoryBasedContentResolverConfiguration> getDirectoryBasedContentResolverConfigurations() {
        return directoryBasedContentResolverConfigurations;
    }

    /**
     * Set list of configurations for directory based content resolvers.
     *
     * @param directoryBasedContentResolverConfigurations
     *         List of configurations for directory based content resolvers.
     */
    public void setDirectoryBasedContentResolverConfigurations(
            List<DirectoryBasedContentResolverConfiguration> directoryBasedContentResolverConfigurations) {
        this.directoryBasedContentResolverConfigurations = directoryBasedContentResolverConfigurations;
    }
}
