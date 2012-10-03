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
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * A single content resource.
 */
public class Resource {
    /** The type of the resource. E.g. thumbnail, preview, presentationcopy, or original. */
    private String type;
    /** The urls where the resource is available (usually one, but may be more for e.g. thumbnails). */
    private List<URI> uris;

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
    public List<URI> getUris() {
        return new ArrayList<URI>(uris);
    }

    /**
     * Set the URLs of the resource.
     *
     * @param uris The urls where the resource is available (usually one, but may be more for e.g. thumbnails).
     */
    public void setUris(List<URI> uris) {
        this.uris = new ArrayList<URI>(uris);
    }
}
