package dk.statsbiblioteket.medieplatform.contentresolver.model;

/*
 * #%L
 * content-resolver-model
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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * List of available content.
 */
@XmlRootElement(namespace = "http://medieplatform.statsbiblioteket.dk/contentresolver/")
public class Content {

    /**
     * Whether or not this Content accepts new resources.
     * When {@link #close()} is called, this will be set to false and attempts to set or add new resources
     * will be quetly ignored.
     */
    private boolean acceptsResources = true;

    /** Available contents. */
    private List<Resource> resources = Collections.synchronizedList(new ArrayList<Resource>());

    /**
     * Get available content.
     *
     * @return Available content.
     */
    @XmlElement(name = "resource", namespace = "http://medieplatform.statsbiblioteket.dk/contentresolver/")
    public List<Resource> getResources() {
        return new ArrayList<Resource>(resources);
    }

    /**
     * Set available content.
     *
     * @param resources Overwrite all available contents with this.
     */
    public void setResources(List<Resource> resources) {
        if (acceptsResources) {
            this.resources = Collections.synchronizedList(new ArrayList<Resource>(resources));
        }
    }

    /**
     * Add to available content.
     *
     * @param resource Resource to add.
     *
     */
    public void addResource(Resource resource) {
        if (acceptsResources) {
            this.resources.add(resource);
        }
    }

    /**
     * If called, further calls to {@link #setResources(List)} and {@link #addResource(Resource)} will have no effect.
     * This is used to guard against ConcurrentModificationExceptions when the Content is being delivered by jackson.
     */
    public void close() {
        acceptsResources = false;
    }
}
