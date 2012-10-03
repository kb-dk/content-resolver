package dk.statsbiblioteket.medieplatform.contentresolver.service;

/*
 * #%L
 * content-resolver-service
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

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.ClassNamesResourceConfig;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * A stand-alone server disseminating content from the current directory where it is started, as (invalid?) file urls.
 */
public class Server {
    /**
     * Start the server. Arguments are ignored.
     * @param args Ignored.
     * @throws Exception on any trouble.
     */
    public static void main(String[] args) throws Exception {
        // Setup JNDI context
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
        InitialContext ic = new InitialContext();

        ic.createSubcontext("java:");
        ic.createSubcontext("java:comp");
        ic.createSubcontext("java:comp/env");
        ic.createSubcontext("java:comp/env/contentresolver");
        ic.bind("java:comp/env/contentresolver/configfile",
                Server.class.getClassLoader().getResource("content-resolver-default.xml").getFile());

        // Start the server
        HttpServerFactory.create("http://localhost:9998/content-resolver", new ClassNamesResourceConfig(ContentResolverService.class))
                .start();
    }
}
