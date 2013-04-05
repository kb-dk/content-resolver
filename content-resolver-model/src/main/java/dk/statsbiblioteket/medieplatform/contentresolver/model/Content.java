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
import java.util.ArrayList;
import java.util.List;

/**
 * List of available content.
 */
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

    /**
     * Add to available content.
     *
     * @param resources Resources to add.
     */
    public void addResources(List<Resource> resources) {
        for (Resource resource : resources) {
            this.resources.add(resource);
        }
    }
}
