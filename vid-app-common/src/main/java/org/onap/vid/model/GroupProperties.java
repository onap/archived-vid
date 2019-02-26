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

public class GroupProperties {
    private Integer minCountInstances;
    private Integer maxCountInstances;
    private Integer initialCount;
    private String vfModuleLabel;
    private boolean baseModule = false;

    public String getVfModuleLabel() {
        return vfModuleLabel;
    }

    public void setVfModuleLabel(String vfModuleLabel) {
        this.vfModuleLabel = vfModuleLabel;
    }

    public Integer getMinCountInstances() {
        return minCountInstances;
    }

    public void setMinCountInstances(Integer minCountInstances) {
        this.minCountInstances = minCountInstances;
    }

    public Integer getMaxCountInstances() {
        return maxCountInstances;
    }

    public void setMaxCountInstances(Integer maxCountInstances) {
        this.maxCountInstances = maxCountInstances;
    }

    public boolean getBaseModule() {
        return baseModule;
    }

    public void setBaseModule(boolean baseModule) {
        this.baseModule = baseModule;
    }

    public Integer getInitialCount() {
        return initialCount;
    }

    public void setInitialCount(Integer initialCount) {
        this.initialCount = initialCount;
    }
}
