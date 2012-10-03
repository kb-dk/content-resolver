package dk.statsbiblioteket.medieplatform.contentresolver.lib.config;

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
