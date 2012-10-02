package dk.statsbiblioteket.medieplatform.contentresolver.lib.config;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * ConfigurableContentResolverConfiguration of configurable content resolver.
 */
@XmlRootElement
public class ConfigurableContentResolverConfiguration {
    private List<DirectoryBasedContentResolverConfiguration> directoryBasedContentResolverConfigurations;
}
