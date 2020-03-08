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

import static org.apache.commons.collections4.MapUtils.emptyIfNull;

import org.apache.commons.lang3.StringUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.model.ModelVer;
import org.onap.vid.model.Group;
import org.onap.vid.model.GroupProperties;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.VfModule;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.services.AaiService;
import org.onap.vid.services.VidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandUtils {

    private static final EELFLoggerDelegate Logger = EELFLoggerDelegate.getLogger(CommandUtils.class);

    private final VidService vidService;
    private final AaiService aaiService;

    @Autowired
    public CommandUtils(VidService vidService, AaiService aaiService) {
        this.vidService = vidService;
        this.aaiService = aaiService;
    }

    public boolean isVfModuleBaseModule(String serviceModelUuid, ModelInfo vfModuleModelInfo) {
        ServiceModel serviceModel = getServiceModel(serviceModelUuid);

        return emptyIfNull(serviceModel.getVfModules())
            .values().stream()
            .filter(toscaModelInfo -> modelsMatch(vfModuleModelInfo, toscaModelInfo))
            .map(Group::getProperties)
            .map(GroupProperties::getBaseModule)
            .findAny().orElseGet(() -> {
                Logger.debug(EELFLoggerDelegate.debugLogger,
                    "Could not find vfModule in model with uuid {}, assuming not base module ({})",
                    serviceModelUuid, vfModuleModelInfo);
                return false;
            });
    }

    private boolean modelsMatch(ModelInfo instanceModelInfo, VfModule toscaModelInfo) {
        return StringUtils.equals(toscaModelInfo.getCustomizationUuid(), instanceModelInfo.getModelCustomizationId())
            || StringUtils.equals(toscaModelInfo.getModelCustomizationName(), instanceModelInfo.getModelCustomizationName());
    }

    public ServiceModel getServiceModel(String serviceModelUuid) {
        return vidService.getServiceModelOrThrow(serviceModelUuid);
    }

    public String getNewestModelUuid(String serviceModelInvariantId) {
        ModelVer serviceModelLatestVersion = aaiService.getNewestModelVersionByInvariantId(serviceModelInvariantId);
        return serviceModelLatestVersion.getModelVersionId();
    }

}
