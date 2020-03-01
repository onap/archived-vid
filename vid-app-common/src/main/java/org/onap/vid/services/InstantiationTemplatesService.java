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
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.dal.AsyncInstantiationRepository;
import org.onap.vid.model.ModelUtil;
import org.onap.vid.model.serviceInstantiation.BaseResource;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiationTemplate;
import org.onap.vid.properties.Features;
import org.springframework.stereotype.Component;
import org.togglz.core.manager.FeatureManager;

@Component
public class InstantiationTemplatesService {

    private final ModelUtil modelUtil;
    private final AsyncInstantiationRepository asyncInstantiationRepository;
    private FeatureManager featureManager;


    @Inject
    public InstantiationTemplatesService(ModelUtil modelUtil,
        AsyncInstantiationRepository asyncInstantiationRepository,
        FeatureManager featureManager) {
        this.modelUtil = modelUtil;
        this.asyncInstantiationRepository = asyncInstantiationRepository;
        this.featureManager = featureManager;
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

    public Collection<Service> setOnEachServiceIsTemplateExists(Collection<Service> services){
        if (!featureManager.isActive(Features.FLAG_2004_INSTANTIATION_TEMPLATES_POPUP)){
            return unsetTemplateExistsToAllServices(services);

        }

        Set<String> serviceModelIdsFromDB  = asyncInstantiationRepository.getAllTemplatesServiceModelIds();

        return services.stream().map(it -> setTemplateExistForService(it, serviceModelIdsFromDB)).collect(toList());
    }

    @NotNull
    protected Collection<Service> unsetTemplateExistsToAllServices(Collection<Service> services) {
        services.forEach(it -> it.setIsInstantiationTemplateExists(false));
        return services;
    }

    protected Service setTemplateExistForService(Service service, Set<String> serviceModelIdsFromDb) {
        service.setIsInstantiationTemplateExists(serviceModelIdsFromDb.contains(service.getUuid()));
        return service;
    }
}
