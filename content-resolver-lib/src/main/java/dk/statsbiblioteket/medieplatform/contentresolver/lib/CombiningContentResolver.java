package dk.statsbiblioteket.medieplatform.contentresolver.lib;

import dk.statsbiblioteket.medieplatform.contentresolver.model.Content;
import dk.statsbiblioteket.medieplatform.contentresolver.model.Resource;

import java.util.List;

/**
 * Given a list of content resolvers, combine the results into one.
 */
public class CombiningContentResolver implements ContentResolver {
    private final List<ContentResolver> contentResolvers;

    public CombiningContentResolver(List<ContentResolver> contentResolvers) {
        this.contentResolvers = contentResolvers;
    }

    /**
     * Given a PID, return the list of content disseminations defined by combining a set of other content resolvers.
     *
     * @param pid The pid of the content to lookup.
     * @return Dissemination of the content.
     */
    public Content getContent(String pid) {
        Content content = new Content();
        for (ContentResolver contentResolver : contentResolvers) {
            List<Resource> resources = contentResolver.getContent(pid).getResources();
            for (Resource resource : resources) {
                content.addResource(resource);
            }
        }
        return content;
    }
}
