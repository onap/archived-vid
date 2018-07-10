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

    MsoResponseWrapper deleteSvcInstance(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper unassignSvcInstance(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper deleteVnf(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper deleteVfModule(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper deleteVolumeGroupInstance(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper deleteNwInstance(RequestDetails requestDetails, String endpoint);

    void getOrchestrationRequest(String t, String sourceId, String endpoint, RestObject restObject);

    MsoResponseWrapper getOrchestrationRequestsForDashboard(String t , String sourceId , String endpoint , RestObject restObject);

    MsoResponseWrapper getManualTasksByRequestId(String t , String sourceId , String endpoint , RestObject restObject);

    MsoResponseWrapper completeManualTask(RequestDetails requestDetails, String t, String sourceId, String endpoint, RestObject restObject);

	MsoResponseWrapper updateVnf(org.onap.vid.changeManagement.RequestDetails requestDetails, String vnf_endpoint);

	MsoResponseWrapper replaceVnf(org.onap.vid.changeManagement.RequestDetails requestDetails, String vnf_endpoint);

    MsoResponseWrapper deleteConfiguration(org.onap.vid.mso.rest.RequestDetailsWrapper requestDetailsWrapper, String pmc_endpoint);

    MsoResponseWrapper setConfigurationActiveStatus(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper setPortOnConfigurationStatus(RequestDetails requestDetails, String endpoint);

    void setServiceInstanceStatus(RequestDetails requestDetails, String t, String sourceId, String endpoint, RestObject<String> restObject);

    MsoResponseWrapperInterface changeManagementUpdate(RequestDetailsWrapper requestDetails, String endpoint);

    MsoResponseWrapper removeRelationshipFromServiceInstance(RequestDetails requestDetails, String endpoint);

    MsoResponseWrapper addRelationshipToServiceInstance(RequestDetails requestDetails, String addRelationshipsPath);
}

