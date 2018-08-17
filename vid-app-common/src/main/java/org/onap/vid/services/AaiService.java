package org.onap.vid.services;

import com.mashape.unirest.http.HttpResponse;
import java.io.IOException;
import org.onap.vid.aai.AaiGetVnfResponse;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.AaiResponseTranslator;
import org.onap.vid.aai.ResponseWithRequestInfo;
import org.onap.vid.aai.Services;
import org.onap.vid.aai.model.AaiGetAicZone.AicZones;
import org.onap.vid.aai.model.AaiGetInstanceGroupsByCloudRegion;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.AaiGetNetworkCollectionDetails;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfResponse;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetServicesRequestModel.GetServicesAAIRespone;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.roles.RoleValidator;

import java.util.Collection;
import java.util.List;

/**
 * Created by Oren on 7/4/17.
 */
public interface AaiService {


    HttpResponse<SubscriberList> getFullSubscriberList(RoleValidator roleValidator);

    HttpResponse<Services> getSubscriberData(String subscriberId, RoleValidator roleValidator);

    AaiResponse getServiceInstanceSearchResults(String subscriberId, String instanceIdentifier, RoleValidator roleProvider, List<String> owningEntities, List<String> projects);

    HttpResponse<SubscriberList> getFullSubscriberList();

    HttpResponse<GetServicesAAIRespone> getServices(RoleValidator roleValidator);
    
    HttpResponse<AicZones> getAaiZones();

    HttpResponse<AaiGetNetworkCollectionDetails> getNetworkCollectionDetails(String serviceInstanceId);

    HttpResponse<AaiGetInstanceGroupsByCloudRegion> getInstanceGroupsByCloudRegion(String cloudOwner, String cloudRegionId, String networkFunction);

    HttpResponse<OperationalEnvironmentList> getOperationalEnvironments(String operationalEnvironmentType, String operationalEnvironmentStatus);

    AaiResponse getAicZoneForPnf(String globalCustomerId , String serviceType , String serviceId);

    HttpResponse<String> getVNFData(String globalSubscriberId, String serviceType);

    HttpResponse<GetTenantsResponse[]> getTenants(String globalCustomerId, String serviceType, RoleValidator roleValidator);

    HttpResponse<AaiGetVnfResponse> getVNFData(String globalSubscriberId, String serviceType, String serviceInstanceId);

    HttpResponse<AaiGetVnfResponse> getNodeTemplateInstances(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion);

    HttpResponse<ResponseWithRequestInfo> getVersionByInvariantId(List<String> modelInvariantId);

    Collection<Service> getServicesByDistributionStatus();

    HttpResponse<Pnf> getSpecificPnf(String pnfId);

    List<String> getServiceInstanceAssociatedPnfs(String globalCustomerId, String serviceType, String serviceInstanceId);

    HttpResponse<AaiGetPnfResponse> getPNFData(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion, String equipVendor, String equipModel);

    AaiResponseTranslator.PortMirroringConfigData getPortMirroringConfigData(String configurationId);

    List<PortDetailsTranslator.PortDetails> getPortMirroringSourcePorts(String configurationId);

    AaiResponse getInstanceGroupsByVnfInstanceId(String vnfInstanceId) throws IOException;
}
