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

/**
 * A single map from id to content.
 */
public class IdMap {
    /** The id (uuid) mapped to content */
    private String id;
    /** The content for the given id */
    private Content content;

    /**
     * Get the id (uuid) mapped to content
     *
     * @return The id (uuid) mapped to content
     * */
    @XmlElement(namespace = "http://medieplatform.statsbiblioteket.dk/contentresolver/")
    public String getId() {
        return id;
    }

    /**
     * Set the id (uuid) mapped to content
     *
     * @param id The id (uuid) mapped to content
     * */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the content for the given id
     *
     * @return The content for the given id
     */
    @XmlElement(namespace = "http://medieplatform.statsbiblioteket.dk/contentresolver/")
    public Content getContent() {
        return content;
    }

    /**
     * Set the content for the given id
     *
     * @param content The content for the given id
     */
    public void setContent(Content content) {
        this.content = content;
    }

}
