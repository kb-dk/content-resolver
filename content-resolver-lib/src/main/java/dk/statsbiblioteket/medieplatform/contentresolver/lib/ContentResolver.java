package dk.statsbiblioteket.medieplatform.contentresolver.lib;

import dk.statsbiblioteket.medieplatform.contentresolver.model.Content;

/**
 * Given a PID, return a list of content disseminations.
 */
public interface ContentResolver {
    /**
     * Given a PID, return a list of content disseminations.
     * @param pid The pid of the content to lookup.
     * @return Dissemination of the content.
     */
    Content getContent(String pid);
}
