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

import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;

import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.dal.AsyncInstantiationRepository;
import org.onap.vid.exceptions.AccessDeniedException;
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.MsoResponseWrapper2;
import org.onap.vid.properties.Features;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AuditService;
import org.onap.vid.services.InstantiationTemplatesService;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.togglz.core.manager.FeatureManager;


@RestController
@RequestMapping(AsyncInstantiationController.ASYNC_INSTANTIATION)
public class AsyncInstantiationController extends VidRestrictedBaseController {

    public static final String ASYNC_INSTANTIATION = "asyncInstantiation";

    protected final AsyncInstantiationBusinessLogic asyncInstantiationBL;
    protected final InstantiationTemplatesService instantiationTemplates;
    protected final AsyncInstantiationRepository asyncInstantiationRepository;
    private final SystemPropertiesWrapper systemPropertiesWrapper;

    private final RoleProvider roleProvider;

    private final FeatureManager featureManager;

    protected final AuditService auditService;

    @Autowired
    public AsyncInstantiationController(AsyncInstantiationBusinessLogic asyncInstantiationBL,
        InstantiationTemplatesService instantiationTemplates,
        AsyncInstantiationRepository asyncInstantiationRepository, RoleProvider roleProvider,
        FeatureManager featureManager, SystemPropertiesWrapper systemPropertiesWrapper,
        AuditService auditService) {
        this.asyncInstantiationBL = asyncInstantiationBL;
        this.instantiationTemplates = instantiationTemplates;
        this.asyncInstantiationRepository = asyncInstantiationRepository;
        this.roleProvider = roleProvider;
        this.featureManager = featureManager;
        this.systemPropertiesWrapper = systemPropertiesWrapper;
        this.auditService = auditService;
    }

    /**
     * Gets the new services status.
     * @param request the request
     * @return the services list
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<ServiceInfo> getServicesInfo(HttpServletRequest request,
        @RequestParam(value = "serviceModelId", required = false) UUID serviceModelId) {
        if (serviceModelId == null) {
            return asyncInstantiationBL.getAllServicesInfo();
        } else {
            return  asyncInstantiationRepository.listInstantiatedServicesByServiceModelId(serviceModelId);
        }
    }

    @RequestMapping(value = "bulk", method = RequestMethod.POST)
    public MsoResponseWrapper2<List<String>> createBulkOfServices(@RequestBody ServiceInstantiation request, HttpServletRequest httpServletRequest) {
        //Push to DB according the model
        try {
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "incoming ServiceInstantiation request: "+ JACKSON_OBJECT_MAPPER.writeValueAsString(request));
        }
        catch (Exception e) {
            LOGGER.error(EELFLoggerDelegate.errorLogger, "failed to log incoming ServiceInstantiation request ", e);
        }
        String userId = new ControllersUtils(systemPropertiesWrapper).extractUserId(httpServletRequest);

        throwExceptionIfAccessDenied(request, httpServletRequest, userId);
        List<UUID> uuids = asyncInstantiationBL.pushBulkJob(request, userId);
        return new MsoResponseWrapper2(200, uuids);
    }



    @RequestMapping(value = "retryJobWithChangedData/{jobId}", method = RequestMethod.POST)
    public MsoResponseWrapper2<List<String>> retryJobWithChangedData(@RequestBody ServiceInstantiation request, @PathVariable(value="jobId") UUID jobId, HttpServletRequest httpServletRequest) {

        String userId = new ControllersUtils(systemPropertiesWrapper).extractUserId(httpServletRequest);
        List<UUID> uuids =  asyncInstantiationBL.retryJob(request, jobId, userId);
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
        return auditService.getAuditStatuses(jobId, source);
    }

    @RequestMapping(value = "auditStatus/{jobId}/mso", method = RequestMethod.GET)
    public List<JobAuditStatus> getJobMsoAuditStatusForAlaCarte(HttpServletRequest request,
                                                                @PathVariable(value="jobId") UUID jobId,
                                                                @RequestParam(value="requestId", required = false) UUID requestId,
                                                                @RequestParam(value="serviceInstanceId", required = false) UUID serviceInstanceId){
        if (serviceInstanceId != null) {
            return auditService.getAuditStatusFromMsoByInstanceId(JobAuditStatus.ResourceTypeFilter.SERVICE, serviceInstanceId, jobId);
        }
        if (requestId != null){
            return auditService.getAuditStatusFromMsoByRequestId(jobId, requestId);
        }
        return auditService.getAuditStatusFromMsoByJobId(jobId);

    }

    @RequestMapping(value = "auditStatus/{type}/{instanceId}/mso", method = RequestMethod.GET)
    public List<JobAuditStatus> getAuditStatusFromMsoByInstanceId(HttpServletRequest request,
                                                                  @PathVariable(value="type") JobAuditStatus.ResourceTypeFilter resourceTypeFilter,
                                                                  @PathVariable(value="instanceId") UUID instanceId) {
        return auditService.getAuditStatusFromMsoByInstanceId(resourceTypeFilter, instanceId, null);
    }

    @RequestMapping(value = "/bulkForRetry/{jobId}", method = RequestMethod.GET)
    public ServiceInstantiation getBulkForRetry(HttpServletRequest request, @PathVariable(value="jobId") UUID jobId) {
        return asyncInstantiationBL.getBulkForRetry(jobId);
    }

    @RequestMapping(value = "retry/{jobId}", method = RequestMethod.POST)
    public MsoResponseWrapper2<List<UUID>> retryJobRequest(HttpServletRequest httpServletRequest,
                                                           @PathVariable(value="jobId") UUID jobId) {

        String userId = new ControllersUtils(systemPropertiesWrapper).extractUserId(httpServletRequest);
        List<UUID> uuids =  asyncInstantiationBL.retryJob(jobId, userId);

        return new MsoResponseWrapper2(200, uuids);
    }

    @GetMapping("templateTopology/{jobId}")
    public ServiceInstantiation getTemplateTopology(HttpServletRequest request, @PathVariable(value="jobId") UUID jobId) {
        return instantiationTemplates.getJobRequestAsTemplate(jobId);
    }

    @RequestMapping(value = "/auditStatusForRetry/{trackById}", method = RequestMethod.GET)
    public JobAuditStatus getResourceAuditStatus(HttpServletRequest request, @PathVariable(value="trackById") String trackById) {
        return auditService.getResourceAuditStatus(trackById);
    }

    private void throwExceptionIfAccessDenied(ServiceInstantiation request, HttpServletRequest httpServletRequest, String userId) {
        if (featureManager.isActive(Features.FLAG_1906_INSTANTIATION_API_USER_VALIDATION) && !roleProvider.getUserRolesValidator(httpServletRequest).isServicePermitted(request.getGlobalSubscriberId(), request.getSubscriptionServiceType())) {
            throw new AccessDeniedException(String.format("User %s is not allowed to make this request", userId));
        }
    }
}
