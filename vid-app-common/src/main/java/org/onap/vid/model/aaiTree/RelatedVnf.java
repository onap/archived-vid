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

package org.onap.vid.model.aaiTree;

import org.onap.vid.aai.util.AAITreeConverter;

import static org.onap.vid.aai.util.AAITreeConverter.VNF_TYPE;

public class RelatedVnf extends Node {

    private String serviceInstanceId;
    private String serviceInstanceName;
    private String tenantName;

    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    public void setServiceInstanceId(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
    }

    public String getServiceInstanceName() {
        return serviceInstanceName;
    }

    public void setServiceInstanceName(String serviceInstanceName) {
        this.serviceInstanceName = serviceInstanceName;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public RelatedVnf(AAITreeNode node) {
        super(node, AAITreeConverter.ModelType.vnf);
    }

    public static RelatedVnf from(AAITreeNode node) {
        RelatedVnf vnf = new RelatedVnf(node);
        vnf.setServiceInstanceId(node.getParent().getId());
        vnf.setServiceInstanceName(node.getParent().getName());

        if (node.getAdditionalProperties().get(VNF_TYPE) != null) {
            vnf.setInstanceType(node.getAdditionalProperties().get(VNF_TYPE).toString());
        }

        return vnf;
    }
}
