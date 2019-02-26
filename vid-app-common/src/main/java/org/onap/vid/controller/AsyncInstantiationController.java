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

package org.onap.vid.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.exceptions.OperationNotAllowedException;
import org.onap.vid.model.ExceptionResponse;
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.MsoResponseWrapper2;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;


@RestController
@RequestMapping(AsyncInstantiationController.ASYNC_INSTANTIATION)
public class AsyncInstantiationController extends VidRestrictedBaseController {

    public static final String ASYNC_INSTANTIATION = "asyncInstantiation";

    protected final AsyncInstantiationBusinessLogic asyncInstantiationBL;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    protected AuditService auditService;

    @Autowired
    public AsyncInstantiationController(AsyncInstantiationBusinessLogic asyncInstantiationBL) {
        this.asyncInstantiationBL = asyncInstantiationBL;
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    @ResponseStatus(value=METHOD_NOT_ALLOWED)
    public ExceptionResponse illegalStateExceptionHandler(Exception e) {
        return ControllersUtils.handleException(e, LOGGER);
    }

    /**
     * Gets the new services status.
     * @param request the request
     * @return the services list
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<ServiceInfo> getServicesInfo(HttpServletRequest request) {
        return asyncInstantiationBL.getAllServicesInfo();
    }

    @RequestMapping(value = "bulk", method = RequestMethod.POST)
    public MsoResponseWrapper2<List<String>> createBulkOfServices(@RequestBody ServiceInstantiation request, HttpServletRequest httpServletRequest) {
        //Push to DB according the model
        try {
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "incoming ServiceInstantiation request: "+ objectMapper.writeValueAsString(request));
        }
        catch (Exception e) {
            LOGGER.error(EELFLoggerDelegate.errorLogger, "failed to log incoming ServiceInstantiation request ", e);
        }
        String userId = ControllersUtils.extractUserId(httpServletRequest);
        List<UUID> uuids =  asyncInstantiationBL.pushBulkJob(request, userId);

        return new MsoResponseWrapper2(200, uuids);
    }

    @RequestMapping(value = "job/{jobId}", method = RequestMethod.DELETE)
    public void deleteServiceInfo(@PathVariable("jobId") UUID jobId) {
        asyncInstantiationBL.deleteJob(jobId);
    }

    @RequestMapping(value = "hide/{jobId}", method = RequestMethod.POST)
    public void hideServiceInfo(@PathVariable("jobId") UUID jobId) {
        asyncInstantiationBL.hideServiceInfo(jobId);
    }

    @RequestMapping(value = "auditStatus/{jobId}", method = RequestMethod.GET)
    public List<JobAuditStatus> getJobAuditStatus(HttpServletRequest request, @PathVariable(value="jobId") UUID jobId, @RequestParam(value="source") JobAuditStatus.SourceStatus source){
        return asyncInstantiationBL.getAuditStatuses(jobId, source);
    }

    @RequestMapping(value = "auditStatus/{jobId}/mso", method = RequestMethod.GET)
    public List<JobAuditStatus> getJobMsoAuditStatusForAlaCarte(HttpServletRequest request,
                                                                @PathVariable(value="jobId") UUID jobId,
                                                                @RequestParam(value="requestId", required = false) UUID requestId,
                                                                @RequestParam(value="serviceInstanceId", required = false) UUID serviceInstanceId){
        if (serviceInstanceId != null) {
            return auditService.getAuditStatusFromMsoByServiceInstanceId(jobId, serviceInstanceId);
        }
        if (requestId != null){
            return auditService.getAuditStatusFromMsoByRequestId(jobId, requestId);
        }
        return auditService.getAuditStatusFromMsoByJobId(jobId);

    }


}
