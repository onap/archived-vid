package org.onap.vid.mso;

import org.onap.vid.changeManagement.RequestDetailsWrapper;
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
    MsoResponseWrapper createSvcInstance(RequestDetails requestDetails, String endpoint) throws Exception;
    
    //For VoLTE E2E services
    MsoResponseWrapper createE2eSvcInstance(Object requestDetails, String endpoint) throws Exception;
    MsoResponseWrapper deleteE2eSvcInstance(Object requestDetails, String endpoint) throws Exception;

    /**
     * will create a virtual network function using MSO service.
     * @param requestDetails - information about the vnf to create
     * @return - the response body recived from MSO
     * @throws Exception
     */
    MsoResponseWrapper createVnf(RequestDetails requestDetails, String endpoint) throws Exception;

    MsoResponseWrapper createNwInstance(RequestDetails requestDetails, String endpoint) throws Exception;
    /**
     *
     * @param requestDetails
     * @param path
     * @return
     * @throws Exception
     */
    MsoResponseWrapper createVolumeGroupInstance(RequestDetails requestDetails, String path) throws Exception;

    /**
     *
     * @param requestDetails
     * @return
     * @throws Exception
     */
    MsoResponseWrapper createVfModuleInstance(RequestDetails requestDetails, String endpoint) throws Exception;

    MsoResponseWrapper createConfigurationInstance(RequestDetails requestDetails, String endpoint) throws Exception;

    MsoResponseWrapper deleteSvcInstance(RequestDetails requestDetails, String endpoint) throws Exception;

    MsoResponseWrapper deleteVnf(RequestDetails requestDetails, String endpoint) throws Exception;

    MsoResponseWrapper deleteVfModule(RequestDetails requestDetails, String endpoint) throws Exception;

    MsoResponseWrapper deleteVolumeGroupInstance(RequestDetails requestDetails, String endpoint) throws Exception;

    MsoResponseWrapper deleteNwInstance(RequestDetails requestDetails, String endpoint) throws Exception;

    void getOrchestrationRequest(String t, String sourceId, String endpoint, RestObject restObject) throws Exception;

    MsoResponseWrapper getOrchestrationRequestsForDashboard(String t , String sourceId , String endpoint , RestObject restObject) throws Exception;

    MsoResponseWrapper getManualTasksByRequestId(String t , String sourceId , String endpoint , RestObject restObject) throws Exception;

    MsoResponseWrapper completeManualTask(RequestDetails requestDetails, String t, String sourceId, String endpoint, RestObject restObject) throws Exception;

	MsoResponseWrapper updateVnf(org.onap.vid.changeManagement.RequestDetails requestDetails, String vnf_endpoint)  throws Exception;

	MsoResponseWrapper replaceVnf(org.onap.vid.changeManagement.RequestDetails requestDetails, String vnf_endpoint)  throws Exception;

    MsoResponseWrapper deleteConfiguration(RequestDetails requestDetails, String pmc_endpoint) throws Exception;

    MsoResponseWrapper setConfigurationActiveStatus(RequestDetails requestDetails, String endpoint) throws Exception;

    MsoResponseWrapper setPortOnConfigurationStatus(RequestDetails requestDetails, String endpoint) throws Exception;

    void setServiceInstanceStatus(RequestDetails requestDetails, String t, String sourceId, String endpoint, RestObject<String> restObject) throws Exception;

    MsoResponseWrapperInterface changeManagementUpdate(RequestDetailsWrapper requestDetails, String endpoint) throws Exception;

    MsoResponseWrapper removeRelationshipFromServiceInstance(RequestDetails requestDetails, String endpoint) throws Exception;

    MsoResponseWrapper addRelationshipToServiceInstance(RequestDetails requestDetails, String addRelationshipsPath) throws Exception;
}

