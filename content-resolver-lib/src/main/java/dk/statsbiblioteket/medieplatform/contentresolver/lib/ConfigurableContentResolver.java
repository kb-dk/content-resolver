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

import dk.statsbiblioteket.medieplatform.contentresolver.lib.config.ConfigurableContentResolverConfiguration;
import dk.statsbiblioteket.medieplatform.contentresolver.lib.config.DirectoryBasedContentResolverConfiguration;
import dk.statsbiblioteket.medieplatform.contentresolver.model.Content;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Read configuration from a file, and initialise a combination of directory based configurations. */
public class ConfigurableContentResolver implements ContentResolver {

    private final ContentResolver contentResolver;

    /** Initialise with configuration file found in JNDI context java:comp/env key contentresolver/configfile */
    public ConfigurableContentResolver() {
        this(getConfigFileFromJNDI());
    }

    /**
     * Initialise with configuration file given.
     *
     * @param configFile The configuration file.
     */
    public ConfigurableContentResolver(String configFile) {
        ConfigurableContentResolverConfiguration configuration = loadFile(new File(configFile));

        List<ContentResolver> directoryBasedContentResolvers = new ArrayList<ContentResolver>();
        for (DirectoryBasedContentResolverConfiguration directoryBasedContentResolverConfiguration : configuration
                .getDirectoryBasedContentResolverConfigurations()) {
            directoryBasedContentResolvers
                    .add(new DirectoryBasedContentResolver(directoryBasedContentResolverConfiguration.getType(),
                                                           directoryBasedContentResolverConfiguration
                                                                   .getBaseDirectory(),
                                                           directoryBasedContentResolverConfiguration
                                                                   .getCharacterDirs(),
                                                           directoryBasedContentResolverConfiguration
                                                                   .getCharacterDirsWidth(),
                                                           directoryBasedContentResolverConfiguration
                                                                   .getFilenameRegexPattern(),
                                                           directoryBasedContentResolverConfiguration.getUriPattern()));
        }
        contentResolver = new CombiningContentResolver(directoryBasedContentResolvers);
    }

    /**
     * Lookup content from directory based content resolvers found in configuration.
     *
     * @param pid The pid of the content to lookup.
     * @return The content.
     */
    @Override
    public Content getContent(String pid) {
        return contentResolver.getContent(pid);
    }

    /**
     * Lookup config file with JNDI.
     *
     * @return Filename found in JNDI context java:comp/env key contentresolver/configfile
     */
    private static String getConfigFileFromJNDI() {
        String configFile;
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");

            configFile = (String) envCtx.lookup("contentresolver/configfile");
        } catch (NamingException e) {
            throw new ConfigurationException("Failed loading configuration: " + e.getMessage(), e);
        }
        return configFile;
    }

    /**
     * Read configuration from file with JAXB.
     *
     * @param file The file with configuration.
     * @return Configuration.
     */
    private static ConfigurableContentResolverConfiguration loadFile(File file) {
        try {
            return (ConfigurableContentResolverConfiguration) JAXBContext
                    .newInstance(ConfigurableContentResolverConfiguration.class).createUnmarshaller().unmarshal(file);
        } catch (JAXBException e) {
            throw new ConfigurationException("Unable to load configuration: " + e.getMessage(), e);
        }
    }
}
