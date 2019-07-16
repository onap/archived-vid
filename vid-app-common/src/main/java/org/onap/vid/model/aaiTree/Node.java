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

import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.mso.model.ModelInfo;

import java.util.Objects;
import java.util.UUID;

public class Node extends AbstractNode {
    private String instanceType;

    private String provStatus;
    private Boolean inMaint;

    private String uuid;
    private String originalName;

    private String legacyRegion;
    private String lineOfBusiness;
    private String platformName;

    private final String trackById;

    public Node() {
        trackById = UUID.randomUUID().toString();
    }

    public Node(AAITreeNode aaiNode) {
        super();
        this.instanceId = aaiNode.getId();
        this.instanceName = aaiNode.getName();
        this.orchStatus = aaiNode.getOrchestrationStatus();
        this.provStatus = aaiNode.getProvStatus();
        this.inMaint = aaiNode.getInMaint();
        this.uuid = aaiNode.getModelVersionId();
        this.originalName = aaiNode.getKeyInModel();
        this.trackById = aaiNode.getUniqueNodeKey();

        ModelInfo nodeModelInfo = new ModelInfo();
        if (aaiNode.getType() != null) {
            nodeModelInfo.setModelType(aaiNode.getType().getModelType());
        }
        nodeModelInfo.setModelName(aaiNode.getModelName());
        nodeModelInfo.setModelVersion(aaiNode.getModelVersion());
        nodeModelInfo.setModelVersionId(aaiNode.getModelVersionId());
        nodeModelInfo.setModelInvariantId(aaiNode.getModelInvariantId());
        nodeModelInfo.setModelCustomizationId(aaiNode.getModelCustomizationId());
        nodeModelInfo.setModelCustomizationName(aaiNode.getModelCustomizationName());

        this.modelInfo = nodeModelInfo;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public void setOrchStatus(String orchStatus) {
        this.orchStatus = orchStatus;
    }

    public String getProvStatus() {
        return provStatus;
    }

    public void setProvStatus(String provStatus) {
        this.provStatus = provStatus;
    }

    public Boolean getInMaint() {
        return inMaint;
    }

    public void setInMaint(Boolean inMaint) {
        this.inMaint = inMaint;
    }

    public void setModelInfo(ModelInfo modelInfo) {
        this.modelInfo = modelInfo;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public void setProductFamilyId(String productFamilyId) {
        this.productFamilyId = productFamilyId;
    }

    public void setLcpCloudRegionId(String lcpCloudRegionId) {
        this.lcpCloudRegionId = lcpCloudRegionId;
    }

    public String getLegacyRegion() {
        return legacyRegion;
    }

    public void setLegacyRegion(String legacyRegion) {
        this.legacyRegion = legacyRegion;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getLineOfBusiness() {
        return lineOfBusiness;
    }

    public void setLineOfBusiness(String lineOfBusiness) {
        this.lineOfBusiness = lineOfBusiness;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getTrackById() {
        return trackById;
    }

    public static void fillCloudConfigurationProperties(AbstractNode that, CloudConfiguration cloudConfiguration) {
        if (cloudConfiguration !=null) {
            that.lcpCloudRegionId = cloudConfiguration.getLcpCloudRegionId();
            that.tenantId = cloudConfiguration.getTenantId();
            that.cloudOwner = cloudConfiguration.getCloudOwner();
        }
    }

    public static String readValueAsStringFromAdditionalProperties(AAITreeNode node, String key) {
        return Objects.toString(node.getAdditionalProperties().get(key), null);
    }
}
