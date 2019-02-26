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

import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.job.Job;
import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.serviceInstantiation.*;
import org.onap.vid.mso.model.*;
import org.onap.vid.mso.rest.AsyncRequestStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public interface AsyncInstantiationBusinessLogic {

    List<String> PARAMS_TO_IGNORE = Arrays.asList("vnf_name", "vf_module_name");

    List<ServiceInfo> getAllServicesInfo();

    List<UUID> pushBulkJob(ServiceInstantiation request, String userId);

    RequestDetailsWrapper<ServiceInstantiationRequestDetails> generateMacroServiceInstantiationRequest(UUID uuid, ServiceInstantiation details, String optimisticUniqueServiceInstanceName, String userId);

    RequestDetailsWrapper<ServiceInstantiationRequestDetails> generateALaCarteServiceInstantiationRequest(UUID uuid, ServiceInstantiation details, String optimisticUniqueServiceInstanceName, String userId);

    RequestDetailsWrapper<ServiceDeletionRequestDetails> generateALaCarteServiceDeletionRequest(UUID uuid, ServiceInstantiation details, String userId);

    RequestDetailsWrapper<VnfInstantiationRequestDetails> generateVnfInstantiationRequest(Vnf vnfDetails, ModelInfo serviceModelInfo, String serviceInstanceId, String userId);

    RequestDetailsWrapper<VfModuleInstantiationRequestDetails> generateVfModuleInstantiationRequest(VfModule vfModuleDetails, ModelInfo serviceModelInfo, String serviceInstanceId, ModelInfo vnfModelInfo, String vnfInstanceId, String vgInstanceId, String userId);

    RequestDetailsWrapper<VolumeGroupRequestDetails> generateVolumeGroupInstantiationRequest(VfModule vfModuleDetails, ModelInfo serviceModelInfo, String serviceInstanceId, ModelInfo vnfModelInfo, String vnfInstanceId, String userId);

    RequestDetailsWrapper<NetworkInstantiationRequestDetails> generateNetworkInstantiationRequest(Network networkDetails, ModelInfo serviceModelInfo, String serviceInstanceId, String userId);

    RequestDetailsWrapper<InstanceGroupInstantiationRequestDetails> generateInstanceGroupInstantiationRequest(InstanceGroup request, ModelInfo serviceModelInfo, String serviceInstanceId, String userId);

    List<Map<String,String>> buildVnfInstanceParams(List<Map<String, String>> currentVnfInstanceParams, List<VfModuleMacro> vfModules);

    String getServiceInstantiationPath(ServiceInstantiation serviceInstantiationRequest);

    String getServiceDeletionPath(String serviceInstanceId);

    String getVnfInstantiationPath(String serviceInstanceId);

    String getNetworkInstantiationPath(String serviceInstanceId);

    String getVfmoduleInstantiationPath(String serviceInstanceId, String vnfInstanceId);

    String getVolumeGroupInstantiationPath(String serviceInstanceId, String vnfInstanceId);

    String getInstanceGroupInstantiationPath();

    String getInstanceGroupDeletePath(String instanceGroupId);

    String getOrchestrationRequestsPath();

    ServiceInfo getServiceInfoByJobId(UUID jobUUID);

    List<JobAuditStatus> getAuditStatuses(UUID jobUUID, JobAuditStatus.SourceStatus source);

    ServiceInfo updateServiceInfo(UUID jobUUID, Consumer<ServiceInfo> serviceUpdater);

    ServiceInfo updateServiceInfoAndAuditStatus(UUID jobUuid, Job.JobStatus jobStatus);

    void auditVidStatus(UUID jobUUID, Job.JobStatus jobStatus);

    void auditMsoStatus(UUID jobUUID, AsyncRequestStatus.Request msoRequestStatus);

    void auditMsoStatus(UUID jobUUID, String jobStatus, String requestId, String additionalInfo);

    Job.JobStatus calcStatus(AsyncRequestStatus asyncRequestStatus);

    void handleFailedInstantiation(UUID jobUUID);

    void deleteJob(UUID jobId);

    void hideServiceInfo(UUID jobUUID);

    int getCounterForName(String name);

    int getMaxRetriesGettingFreeNameFromAai();

    void setMaxRetriesGettingFreeNameFromAai(int maxRetriesGettingFreeNameFromAai);

    String getUniqueName(String name, ResourceType resourceType);


}
