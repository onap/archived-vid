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

package org.onap.vid.changeManagement;

import org.onap.vid.model.VNFDao;
import org.onap.vid.model.VidWorkflow;

import java.util.List;
import java.util.stream.Collectors;

public class VnfDetailsWithWorkflows extends VnfDetails {

    private List<String> workflows;

    public VnfDetailsWithWorkflows() {
    }

    @SuppressWarnings("WeakerAccess")
    public VnfDetailsWithWorkflows(String UUID, String invariantUUID, List<String> workflows) {
        super(UUID, invariantUUID);
        this.workflows = workflows;
    }

    public VnfDetailsWithWorkflows(VNFDao vnfDao) {
        this(vnfDao.getVnfUUID(),
             vnfDao.getVnfInvariantUUID(),
             vnfDao.getWorkflows().stream().map(VidWorkflow::getWokflowName).collect(Collectors.toList()));
    }

    public List<String> getWorkflows() {
        return workflows;
    }

    public void setWorkflows(List<String> workflows) {
        this.workflows = workflows;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
