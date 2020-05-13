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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.ObjectUtils;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobType;
import org.onap.vid.model.VidNotions;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.utils.jackson.BooleanAsStringSerializer;

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

    private int bulkSize;

    private final String testApi;

    private final boolean isALaCarte;

    private final VidNotions vidNotions;
    private Map<String, VrfEntry> vrfs;

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
                                @JsonProperty("vrfs") Map<String, VrfEntry> vrfs,
                                @JsonProperty("instanceParams") List<Map<String, String>> instanceParams,
                                @JsonProperty("pause") boolean isPause,
                                @JsonProperty("bulkSize") int bulkSize,
                                @JsonProperty("rollbackOnFailure") boolean rollbackOnFailure,
                                @JsonProperty("isALaCarte") boolean isALaCarte,
                                @JsonProperty("testApi") String testApi,
                                @JsonProperty("instanceId") String instanceId,
                                @JsonProperty("action") String action,
                                @JsonProperty("trackById") String trackById,
                                @JsonProperty("isFailed") Boolean isFailed,
                                @JsonProperty("statusMessage") String statusMessage,
                                @JsonProperty("vidNotions") VidNotions vidNotions,
                                @JsonProperty("originalName") String originalName) {
        super(modelInfo, instanceName, action, lcpCloudRegionId, legacyRegion, tenantId, instanceParams, rollbackOnFailure, instanceId, trackById, isFailed, statusMessage,
            null, null, originalName);
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
        this.vrfs = vrfs;
        this.isPause = isPause;
        this.bulkSize = bulkSize;
        this.isALaCarte = isALaCarte;
        this.testApi = isALaCarte ? testApi : null;
        this.vidNotions = vidNotions;
    }

    @Override
    @JsonSerialize(using=BooleanAsStringSerializer.class)
    public boolean isRollbackOnFailure() {
        // this override is for the BooleanAsStringSerializer annotation,
        // but for Service-Instance level only
        return super.isRollbackOnFailure();
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
        return emptyMapIfNull(vnfs);
    }

    public Map<String, Network> getNetworks() {
        return emptyMapIfNull(networks);
    }

    public Map<String, InstanceGroup> getVnfGroups() {
        return emptyMapIfNull(vnfGroups);
    }

    public Map<String, VrfEntry> getVrfs() {
        return emptyMapIfNull(vrfs);
    }

    public boolean isPause() {
        return isPause;
    }

    public int getBulkSize() { return bulkSize; }

    public void setBulkSize(int bulkSize) {
        this.bulkSize = bulkSize;
    }

    public VidNotions getVidNotions() {
        return vidNotions;
    }

    @Override
    protected String getModelType() {
        return "service";
    }

    @Override
    public Collection<BaseResource> getChildren() {
        return Stream.of(getNetworks().values(), getVnfs().values(), getVnfGroups().values()).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @JsonProperty("isALaCarte")
    public boolean isALaCarte() {
        return isALaCarte;
    }

    public String getTestApi() {
        return this.testApi;
    }

    @Override
    public JobType getJobType() {
        return JobType.ALaCarteService;
    }

    private <T> Map<String, T> emptyMapIfNull(Map<String, T> map) {
        return ObjectUtils.defaultIfNull(map, Collections.emptyMap());
    }

}