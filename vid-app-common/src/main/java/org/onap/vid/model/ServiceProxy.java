/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
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
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.model;

public class ServiceProxy extends Node {

    private String sourceModelUuid;

    private String sourceModelInvariant;

    private String sourceModelName;

    public String getSourceModelUuid() {
        return sourceModelUuid;
    }

    public void setSourceModelUuid(String sourceModelUuid) {
        this.sourceModelUuid = sourceModelUuid;
    }

    public String getSourceModelInvariant() {
        return sourceModelInvariant;
    }

    public void setSourceModelInvariant(String sourceModelInvariant) {
        this.sourceModelInvariant = sourceModelInvariant;
    }

    public String getSourceModelName() {
        return sourceModelName;
    }

    public void setSourceModelName(String sourceModelName) {
        this.sourceModelName = sourceModelName;
    }

}
