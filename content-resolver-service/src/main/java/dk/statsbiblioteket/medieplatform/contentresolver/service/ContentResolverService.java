package dk.statsbiblioteket.medieplatform.contentresolver.service;

import dk.statsbiblioteket.medieplatform.contentresolver.lib.ConfigurableContentResolver;
import dk.statsbiblioteket.medieplatform.contentresolver.lib.ContentResolver;
import dk.statsbiblioteket.medieplatform.contentresolver.model.Content;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/")
public class ContentResolverService implements ContentResolver {
    private final ContentResolver contentResolver;

    public ContentResolverService() {

        //TODO initialise content resolver from configuration.
        this.contentResolver = new ConfigurableContentResolver();
    }

    /**
     * Given a PID, return a list of content disseminations.
     *
     * @param pid The pid of the content to lookup.
     * @return Dissemination of the content.
     */
    @GET
    @Path("content/{pid}")
    @Produces({"text/xml", "application/json"})
    public Content getContent(@PathParam("pid") String pid) {
        return contentResolver.getContent(pid);
    }
}
