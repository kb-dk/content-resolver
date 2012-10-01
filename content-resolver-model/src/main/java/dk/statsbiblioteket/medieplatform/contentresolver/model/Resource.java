package dk.statsbiblioteket.medieplatform.contentresolver.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A single content resource.
 */
public class Resource {
    /** The type of the resource. E.g. thumbnail, preview, presentationcopy, or original. */
    private String type;
    /** The urls where the resource is available (usually one, but may be more for e.g. thumbnails). */
    private List<URL> urls;

    /**
     * Get the type of the resource.
     *
     * @return The type of the resource. E.g. thumbnail, preview, presentationcopy, or original.
     * */
    @XmlElement(namespace = "http://medieplatform.statsbiblioteket.dk/contentresolver/")
    public String getType() {
        return type;
    }

    /**
     * Set the type of the resource.
     *
     * @param type The type of the resource. E.g. thumbnail, preview, presentationcopy, or original.
     * */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the URLs of the resource.
     *
     * @return The urls where the resource is available (usually one, but may be more for e.g. thumbnails).
     */
    @XmlElement(name = "url", namespace = "http://medieplatform.statsbiblioteket.dk/contentresolver/")
    public List<URL> getUrls() {
        return new ArrayList<URL>(urls);
    }

    /**
     * Set the URLs of the resource.
     *
     * @param urls The urls where the resource is available (usually one, but may be more for e.g. thumbnails).
     */
    public void setUrls(List<URL> urls) {
        this.urls = new ArrayList<URL>(urls);
    }
}
