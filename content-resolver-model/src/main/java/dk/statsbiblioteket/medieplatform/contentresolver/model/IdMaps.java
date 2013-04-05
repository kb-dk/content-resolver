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
import java.util.List;

/**
 * List of available maps.
 */
@XmlRootElement(namespace = "http://medieplatform.statsbiblioteket.dk/contentresolver/")
public class IdMaps {
    /** Available maps. */
    private List<IdMap> idMaps = new ArrayList<IdMap>();

    /**
     * Get available id maps.
     *
     * @return Available id maps.
     */
    @XmlElement(name = "idmap", namespace = "http://medieplatform.statsbiblioteket.dk/contentresolver/")
    public List<IdMap> getIdMaps() {
        return idMaps;
    }

    /**
     * Set available maps.
     *
     * @param idMaps Overwrite all available maps with this.
     */
    public void setIdMaps(List<IdMap> idMaps) {
        this.idMaps = new ArrayList<IdMap>(idMaps);
    }

    /**
     * Add to available maps.
     *
     * @param idMap Map to add.
     *
     */
    public void addIdMap(IdMap idMap) {
        this.idMaps.add(idMap);
    }
}
