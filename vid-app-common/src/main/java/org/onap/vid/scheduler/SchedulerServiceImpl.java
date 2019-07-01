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

package org.onap.vid.scheduler;

import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.model.probes.ErrorMetadata;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.model.probes.HttpRequestMetadata;
import org.onap.vid.mso.RestObjectWithRequestInfo;
import org.onap.vid.services.ChangeManagementService;
import org.onap.vid.utils.Logging;
import org.springframework.beans.factory.annotation.Autowired;

public class SchedulerServiceImpl implements SchedulerService{

    private final ChangeManagementService changeManagementService;


    @Autowired
    public SchedulerServiceImpl(ChangeManagementService changeManagementService) {
        this.changeManagementService = changeManagementService;
    }

    @Override
    public ExternalComponentStatus probeComponent() {
        long startTime = System.currentTimeMillis();
        try {
            RestObjectWithRequestInfo response = this.changeManagementService.getSchedulerChangeManagementsWithRequestInfo();
            return new ExternalComponentStatus(
                    ExternalComponentStatus.Component.SCHEDULER,
                    true,
                    new HttpRequestMetadata(response, "OK", startTime)
            );
        } catch (ExceptionWithRequestInfo e) {
            long duration = System.currentTimeMillis() - startTime;
            return new ExternalComponentStatus(ExternalComponentStatus.Component.SCHEDULER,
                    false,
                    new HttpRequestMetadata(e, duration));
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            return new ExternalComponentStatus(ExternalComponentStatus.Component.SCHEDULER, false,
                    new ErrorMetadata(Logging.exceptionToDescription(e), duration));
        }
    }
}
