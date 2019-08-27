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

package org.onap.vid.job.command;

import org.apache.commons.lang3.StringUtils;
import org.onap.vid.aai.model.ModelVer;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.services.AaiService;
import org.onap.vid.services.VidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandUtils {

    private final VidService vidService;
    private final AaiService aaiService;

    @Autowired
    public CommandUtils(VidService vidService, AaiService aaiService) {
        this.vidService = vidService;
        this.aaiService = aaiService;
    }

    public boolean isVfModuleBaseModule(String serviceModelUuid, String vfModuleModelUUID) throws AsdcCatalogException{
        ServiceModel serviceModel =  getServiceModel(serviceModelUuid);

        if (serviceModel.getVfModules() == null) {
            throw createAsdcCatalogVfModuleModelUUIDNotFoundException(serviceModelUuid, vfModuleModelUUID);
        }

        return serviceModel.getVfModules()
                .values()
                .stream()
                .filter(vfModule -> StringUtils.equals(vfModule.getUuid(), vfModuleModelUUID))
                .findFirst()
                .orElseThrow(() -> createAsdcCatalogVfModuleModelUUIDNotFoundException(serviceModelUuid, vfModuleModelUUID))
                .getProperties()
                .getBaseModule();
    }

    public ServiceModel getServiceModel(String serviceModelUuid) throws AsdcCatalogException{
        ServiceModel serviceModel =  vidService.getService(serviceModelUuid);

        if (serviceModel==null) {
            throw new AsdcCatalogException("Failed to retrieve model with uuid "+serviceModelUuid +" from SDC");
        }

        return serviceModel;
    }

    public String getNewestModelUuid(String serviceModelInvariantId)
    {
        ModelVer serviceModelLatestVersion = aaiService.getNewestModelVersionByInvariantId(serviceModelInvariantId);

        return serviceModelLatestVersion.getModelVersionId();
    }

    private AsdcCatalogException createAsdcCatalogVfModuleModelUUIDNotFoundException(String serviceModelUuid, String vfModuleModelUUID) {
        return new AsdcCatalogException("Failed to find vfModuleModelUUID: " + vfModuleModelUUID +
                "in model with uuid: " + serviceModelUuid);
    }

}
