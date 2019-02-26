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

package org.onap.vid.model.serviceInstantiation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.mso.model.ModelInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ServiceInstantiation extends BaseResource implements JobAdapter.AsyncJobRequest {

    private final String owningEntityId;

    private final String owningEntityName;

    private final String projectName;

    private final String subscriberName;

    private final String globalSubscriberId;

    private final String productFamilyId;

    private final String subscriptionServiceType;

    private final String tenantName;

    private final String aicZoneId;

    private final String aicZoneName;

    private final Map<String, Vnf> vnfs;

    private final Map<String, Network> networks;

    private final Map<String, InstanceGroup> vnfGroups;

    private final boolean isPause;

    private final int bulkSize;

    private final String testApi;

    private final boolean isALaCarte;

    public ServiceInstantiation(@JsonProperty("modelInfo") ModelInfo modelInfo,
                                @JsonProperty("owningEntityId") String owningEntityId,
                                @JsonProperty("owningEntityName") String owningEntityName,
                                @JsonProperty("projectName") String projectName,
                                @JsonProperty("globalSubscriberId") String globalSubscriberId,
                                @JsonProperty("subscriberName") String subscriberName,
                                @JsonProperty("productFamilyId") String productFamilyId,
                                @JsonProperty("instanceName") String instanceName,
                                @JsonProperty("subscriptionServiceType") String subscriptionServiceType,
                                @JsonProperty("lcpCloudRegionId") String lcpCloudRegionId,
                                @JsonProperty("legacyRegion") String legacyRegion,
                                @JsonProperty("tenantId") String tenantId,
                                @JsonProperty("tenantName") String tenantName,
                                @JsonProperty("aicZoneId") String aicZoneId,
                                @JsonProperty("aicZoneName") String aicZoneName,
                                @JsonProperty("vnfs") Map<String, Vnf> vnfs,
                                @JsonProperty("networks") Map<String, Network> networks,
                                @JsonProperty("vnfGroups") Map<String, InstanceGroup> vnfGroups,
                                @JsonProperty("instanceParams") List<Map<String, String>> instanceParams,
                                @JsonProperty("pause") boolean isPause,
                                @JsonProperty("bulkSize") int bulkSize,
                                @JsonProperty("rollbackOnFailure") boolean rollbackOnFailure,
                                @JsonProperty("isALaCarte") boolean isALaCarte,
                                @JsonProperty("testApi") String testApi,
                                @JsonProperty("instanceId") String instanceId,
                                @JsonProperty("action") String action
                               ) {
        super(modelInfo, instanceName, action, lcpCloudRegionId, legacyRegion, tenantId, instanceParams, rollbackOnFailure, instanceId);
        this.owningEntityId = owningEntityId;
        this.owningEntityName = owningEntityName;
        this.projectName = projectName;
        this.globalSubscriberId = globalSubscriberId;
        this.subscriberName = subscriberName;
        this.productFamilyId = productFamilyId;
        this.subscriptionServiceType = subscriptionServiceType;
        this.tenantName = tenantName;
        this.aicZoneId = aicZoneId;
        this.aicZoneName = aicZoneName;
        this.vnfs = vnfs;
        this.networks = networks;
        this.vnfGroups = vnfGroups;
        this.isPause = isPause;
        this.bulkSize = bulkSize;
        this.isALaCarte = isALaCarte;
        this.testApi = isALaCarte ? testApi : null;
    }

    public String getOwningEntityId() {
        return owningEntityId;
    }

    public String getOwningEntityName() {
        return owningEntityName;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getGlobalSubscriberId() {
        return globalSubscriberId;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public String getProductFamilyId() {
        return productFamilyId;
    }

    public String getSubscriptionServiceType() {
        return subscriptionServiceType;
    }

    public String getTenantName() {
        return tenantName;
    }

    public String getAicZoneId() {
        return aicZoneId;
    }

    public String getAicZoneName() {
        return aicZoneName;
    }

    public Map<String, Vnf> getVnfs() {
        return vnfs == null ? Collections.emptyMap() : vnfs;
    }

    public Map<String, Network> getNetworks() {
        return networks == null ? Collections.emptyMap() : networks;
    }

    public Map<String, InstanceGroup> getVnfGroups() {
        return vnfGroups == null ? Collections.emptyMap() : vnfGroups;
    }

    public boolean isPause() {
        return isPause;
    }

    public int getBulkSize() { return bulkSize; }

    @Override
    protected String getModelType() {
        return "service";
    }

    @JsonProperty("isALaCarte")
    public boolean isALaCarte() {
        return isALaCarte;
    }

    public String getTestApi() {
        return this.testApi;
    }

}
