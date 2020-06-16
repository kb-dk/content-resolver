package dk.statsbiblioteket.medieplatform.contentresolver.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;


public class ContentResolverApplication extends Application {
    public Set<Class<?>> getClasses() {
        return new HashSet<>(Arrays.asList(JacksonJsonProvider.class, ContentResolverService.class));
    }
}

