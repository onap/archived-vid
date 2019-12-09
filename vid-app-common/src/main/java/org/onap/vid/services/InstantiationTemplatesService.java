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

package org.onap.vid.services;

import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import org.onap.vid.dal.AsyncInstantiationRepository;
import org.onap.vid.model.ModelUtil;
import org.onap.vid.model.serviceInstantiation.BaseResource;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiationTemplate;
import org.springframework.stereotype.Component;

@Component
public class InstantiationTemplatesService {

    private final ModelUtil modelUtil;
    private final AsyncInstantiationRepository asyncInstantiationRepository;

    @Inject
    public InstantiationTemplatesService(ModelUtil modelUtil,
        AsyncInstantiationRepository asyncInstantiationRepository) {
        this.modelUtil = modelUtil;
        this.asyncInstantiationRepository = asyncInstantiationRepository;
    }

    public ServiceInstantiationTemplate getJobRequestAsTemplate(UUID jobId) {
        ServiceInstantiation jobRequest = requireNonNull(asyncInstantiationRepository.getJobRequest(jobId));

        return new ServiceInstantiationTemplate(
            jobRequest,
            counterMap(jobRequest.getVnfs()),
            counterMap(jobRequest.getNetworks()),
            counterMap(jobRequest.getVnfGroups()),
            emptyMap() // model info for VRF is not stored
        );
    }

    private <T extends BaseResource> Map<String, Long> counterMap(Map<String, T> nodesToCount) {
        return modelUtil.getExistingCounterMap(
            nodesToCount, BaseResource::getModelInfo
        );
    }

}
