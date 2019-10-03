/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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
package org.onap.vid.mso;

import io.joshworks.restclient.http.HttpResponse;
import java.util.Map;
import org.onap.vid.aai.HttpResponseWithRequestInfo;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.changeManagement.WorkflowRequestDetail;
import org.onap.vid.model.SOWorkflowList;
import org.onap.vid.mso.rest.RequestDetails;

/**
 * Created by pickjonathan on 21/06/2017.
 */
public interface MsoInterface {

    /**
     * This function will post MSO service with information about how to instantiate the requested service
     * @param requestDetails The details about the service as they come from the web.
     * @return MsoResponseWrapper containing information about the service instantiation
     * --> success : see JSON at resources folder mso_create_instance_response.
     * --> failure : would return 200 with failure data.
     * @throws Exception
     */
    MsoResponseWrapper createSvcInstance(RequestDetails requestDetails, String endpoint);

    //For VoLTE E2E services
    MsoResponseWrapper createE2eSvcInstance(Object requestDetails, String endpoint);
    MsoResponseWrapper deleteE2eSvcInstance(Object requestDetails, String endpoint);

    /**
     * will create a virtual network function using MSO service.
     * @param requestDetails - information about the vnf to create
     * @return - the response body recived from MSO
     * @throws Exception
     */
    MsoResponseWrapper createVnf(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper createNwInstance(RequestDetails requestDetails, String endpoint);
    /**
     *
     * @param requestDetails
     * @param path
     * @return
     * @throws Exception
     */
    MsoResponseWrapper createVolumeGroupInstance(RequestDetails requestDetails, String path);

    /**
     *
     * @param requestDetails
     * @return
     * @throws Exception
     */
    MsoResponseWrapper createVfModuleInstance(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper createConfigurationInstance(org.onap.vid.mso.rest.RequestDetailsWrapper requestDetailsWrapper, String endpoint);

    MsoResponseWrapper scaleOutVFModuleInstance(RequestDetailsWrapper requestDetailsWrapper, String endpoint);

    MsoResponseWrapper deleteSvcInstance(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper unassignSvcInstance(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper deleteVnf(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper deleteVfModule(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper deleteVolumeGroupInstance(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper deleteNwInstance(RequestDetails requestDetails, String endpoint);

    HttpResponseWithRequestInfo<String> getOrchestrationRequest(String endpoint, boolean warpException);

    MsoResponseWrapper getOrchestrationRequest(String endpoint);

    MsoResponseWrapper getManualTasksByRequestId(String t , String sourceId , String endpoint , RestObject restObject);

    MsoResponseWrapper completeManualTask(RequestDetails requestDetails, String t, String sourceId, String endpoint, RestObject restObject);

	MsoResponseWrapper updateVnf(org.onap.vid.changeManagement.RequestDetails requestDetails, String vnfEndpoint);

	MsoResponseWrapper replaceVnf(org.onap.vid.changeManagement.RequestDetails requestDetails, String vnfEndpoint);

    MsoResponseWrapper deleteConfiguration(org.onap.vid.mso.rest.RequestDetailsWrapper requestDetailsWrapper, String pmcEndpoint);

    MsoResponseWrapper setConfigurationActiveStatus(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper setPortOnConfigurationStatus(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper setServiceInstanceStatus(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapperInterface changeManagementUpdate(RequestDetailsWrapper requestDetails, String endpoint);

    MsoResponseWrapper removeRelationshipFromServiceInstance(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper addRelationshipToServiceInstance(RequestDetails requestDetails, String addRelationshipsPath);

    HttpResponse<SOWorkflowList> getWorkflowListByModelId(String endpoint);

    MsoResponseWrapper invokeWorkflow(WorkflowRequestDetail requestDetails,String invokeWorkflowsPath,  Map<String, String> extraHeaders);

    <T> HttpResponse<T> get(String path, Class<T> responseClass);

    <T> HttpResponse<T> post(String path, RequestDetailsWrapper<?> requestDetailsWrapper, Class<T> responseClass);

}

