package dk.statsbiblioteket.medieplatform.contentresolver.lib;

/*
 * #%L
 * content-resolver-lib
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
