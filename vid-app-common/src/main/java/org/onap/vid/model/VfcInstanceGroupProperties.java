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

public class VfcInstanceGroupProperties {

    private String vfcParentPortRole;
    private String networkCollectionFunction;
    private String vfcInstanceGroupFunction;
    private String subinterfaceRole;

    public String getVfcParentPortRole() {
        return vfcParentPortRole;
    }

    public void setVfcParentPortRole(String vfcParentPortRole) {
        this.vfcParentPortRole = vfcParentPortRole;
    }

    public String getNetworkCollectionFunction() {
        return networkCollectionFunction;
    }

    public void setNetworkCollectionFunction(String networkCollectionFunction) {
        this.networkCollectionFunction = networkCollectionFunction;
    }

    public String getVfcInstanceGroupFunction() {
        return vfcInstanceGroupFunction;
    }

    public void setVfcInstanceGroupFunction(String vfcInstanceGroupFunction) {
        this.vfcInstanceGroupFunction = vfcInstanceGroupFunction;
    }

    public String getSubinterfaceRole() {
        return subinterfaceRole;
    }

    public void setSubinterfaceRole(String subinterfaceRole) {
        this.subinterfaceRole = subinterfaceRole;
    }



}
