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

import static java.util.Collections.emptyMap;

import java.util.Map;
import java.util.Objects;
import org.onap.vid.model.aaiTree.ExistingElementsCounterMaps;

public class ServiceInstantiationTemplate extends ServiceInstantiation implements ExistingElementsCounterMaps {

    private final Map<String, Long> existingVNFCounterMap;
    private final Map<String, Long> existingPNFCounterMap;
    private final Map<String, Long> existingNetworksCounterMap;
    private final Map<String, Long> existingVnfGroupCounterMap;
    private final Map<String, Long> existingVRFCounterMap;
    private final Map<String, String> existingNames = emptyMap();

    public ServiceInstantiationTemplate(
        ServiceInstantiation baseService,
        Map<String, Long> vnfCounterMap,
        Map<String, Long> pnfCounterMap,
        Map<String, Long> networksCounterMap,
        Map<String, Long> vnfGroupCounterMap,
        Map<String, Long> VRFCounterMap
    ) {
        super(
            baseService.getModelInfo(), baseService.getOwningEntityId(), baseService.getOwningEntityName(),
            baseService.getProjectName(), baseService.getGlobalSubscriberId(), baseService.getSubscriberName(),
            baseService.getProductFamilyId(), baseService.getInstanceName(), baseService.getSubscriptionServiceType(),
            baseService.getLcpCloudRegionId(), baseService.getLcpCloudRegionId(), baseService.getTenantId(),
            baseService.getTenantName(), baseService.getAicZoneId(), baseService.getAicZoneName(),
            baseService.getVnfs(), baseService.getPnfs(), baseService.getNetworks(), baseService.getVnfGroups(), baseService.getVrfs(),
            baseService.getInstanceParams(), baseService.isPause(), baseService.getBulkSize(),
            baseService.isRollbackOnFailure(), baseService.isALaCarte(), baseService.getTestApi(),
            baseService.getInstanceId(), Objects.toString(baseService.getAction(), null),
            baseService.getTrackById(), baseService.getIsFailed(), baseService.getStatusMessage(),
            baseService.getVidNotions(),
            baseService.getOriginalName()
        );

        this.existingVNFCounterMap = vnfCounterMap;
        this.existingPNFCounterMap = pnfCounterMap;
        this.existingNetworksCounterMap = networksCounterMap;
        this.existingVnfGroupCounterMap = vnfGroupCounterMap;
        this.existingVRFCounterMap = VRFCounterMap;
    }

    @Override
    public Map<String, Long> getExistingVNFCounterMap() {
        return existingVNFCounterMap;
    }

    @Override
    public Map<String, Long> getExistingPNFCounterMap() {
        return existingPNFCounterMap;
    }

    @Override
    public Map<String, Long> getExistingNetworksCounterMap() {
        return existingNetworksCounterMap;
    }

    @Override
    public Map<String, Long> getExistingVnfGroupCounterMap() {
        return existingVnfGroupCounterMap;
    }

    @Override
    public Map<String, Long> getExistingVRFCounterMap() {
        return existingVRFCounterMap;
    }

    public Map<String, String> getExistingNames() {
        return existingNames;
    }
}
