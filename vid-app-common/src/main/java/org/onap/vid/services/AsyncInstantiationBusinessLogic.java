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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.job.Job;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.rest.AsyncRequestStatus;

public interface AsyncInstantiationBusinessLogic {

    List<String> PARAMS_TO_IGNORE = Arrays.asList("vnf_name", "vf_module_name");

    List<ServiceInfo> getAllServicesInfo();

    List<UUID> pushBulkJob(ServiceInstantiation request, String userId);

    boolean isPartOfBulk(UUID jobId);

    String getServiceInstantiationPath(ServiceInstantiation serviceInstantiationRequest);

    String getServiceDeletionPath(String serviceInstanceId);

    String getVnfInstantiationPath(String serviceInstanceId);

    String getVnfDeletionPath(String serviceInstanceId, String vnfInstanceId);

    String getNetworkInstantiationPath(String serviceInstanceId);

    String getVfmoduleInstantiationPath(String serviceInstanceId, String vnfInstanceId);

    String getVfModuleReplacePath(String serviceInstanceId, String vnfInstanceId, String vfModuleInstanceId);

    String getVfModuleDeletePath(String serviceInstanceId, String vnfInstanceId, String vfModuleInstanceId);

    String getVolumeGroupInstantiationPath(String serviceInstanceId, String vnfInstanceId);

    String getInstanceGroupInstantiationPath();

    String getInstanceGroupMemberInstantiationPath(String vnfGroupInstanceId);

    String getInstanceGroupDeletePath(String instanceGroupId);

    String getInstanceGroupMemberDeletePath(String vnfGroupInstanceId);

    String getNetworkDeletePath(String serviceInstanceId, String networkInstanceId);

    String getOrchestrationRequestsPath();

    ServiceInfo updateServiceInfo(UUID jobUUID, Consumer<ServiceInfo> serviceUpdater);

    ServiceInfo updateServiceInfoAndAuditStatus(UUID jobUuid, Job.JobStatus jobStatus);

    Job.JobStatus calcStatus(AsyncRequestStatus asyncRequestStatus);

    void handleFailedInstantiation(UUID jobUUID);

    void deleteJob(UUID jobId);

    void hideServiceInfo(UUID jobUUID);

    int getCounterForName(String name);

    int getMaxRetriesGettingFreeNameFromAai();

    void setMaxRetriesGettingFreeNameFromAai(int maxRetriesGettingFreeNameFromAai);

    String getUniqueName(String name, ResourceType resourceType);

    ServiceInstantiation prepareServiceToBeUnique(ServiceInstantiation serviceInstantiation);

    ServiceInstantiation enrichBulkForRetry(ServiceInstantiation serviceInstantiation, UUID jobId);

    List<UUID> retryJob(UUID jobId, String userId);

    List<UUID> retryJob(ServiceInstantiation request, UUID oldJobId, String userId);

    void addResourceInfo(JobSharedData sharedData, Job.JobStatus jobStatus, String instanceId);

    void addFailedResourceInfo(JobSharedData sharedData, RestObject msoResponse);

    void updateResourceInfo(JobSharedData sharedData, Job.JobStatus jobStatus, AsyncRequestStatus message);

    ServiceInstantiation getBulkForRetry(UUID jobId);

    String getResumeRequestPath(String requestId);
}
