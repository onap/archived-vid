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


import java.util.Collection;

public class Workflow {
    //Private members:
    private int id;
    private String workflowName;
    private Collection<String> vnfNames;


    //Constructors:
    public Workflow() {}

    public Workflow(int id, String workflowName, Collection<String> vnfNames) {
        this.id = id;
        this.workflowName = workflowName;
        this.vnfNames = vnfNames;
    }


    //Setters and getters:
    public int getId() {
        return id;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public Collection<String> getVnfNames() {
        return this.vnfNames;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public void setVnfNames(Collection<String> vnfNames) {
        this.vnfNames = vnfNames;
    }
}
