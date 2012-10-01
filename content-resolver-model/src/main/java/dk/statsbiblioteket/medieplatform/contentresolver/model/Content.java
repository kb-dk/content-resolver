package dk.statsbiblioteket.medieplatform.contentresolver.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * List of available content.
 */
@XmlRootElement(namespace = "http://medieplatform.statsbiblioteket.dk/contentresolver/")
public class Content {
    /** Available contents. */
    private List<Resource> resources = new ArrayList<Resource>();

    /**
     * Get available content.
     *
     * @return Available content.
     */
    @XmlElement(name = "resource", namespace = "http://medieplatform.statsbiblioteket.dk/contentresolver/")
    public List<Resource> getResources() {
        return resources;
    }

    /**
     * Set available content.
     *
     * @param resources Overwrite all available contents with this.
     */
    public void setResources(List<Resource> resources) {
        this.resources = new ArrayList<Resource>(resources);
    }

    /**
     * Add to available content.
     *
     * @param resource Resource to add.
     *
     */
    public void addResource(Resource resource) {
        this.resources.add(resource);
    }
}
