package org.onap.vid.services;

import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.AaiResponseTranslator;
import org.onap.vid.aai.SubscriberFilteredResults;
import org.onap.vid.aai.model.AaiGetInstanceGroupsByCloudRegion;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.roles.RoleValidator;

import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;

/**
 * Created by Oren on 7/4/17.
 */
public interface AaiService {


    SubscriberFilteredResults getFullSubscriberList(RoleValidator roleValidator);

    AaiResponse getSubscriberData(String subscriberId, RoleValidator roleValidator);

    AaiResponse getServiceInstanceSearchResults(String subscriberId, String instanceIdentifier, RoleValidator roleProvider, List<String> owningEntities, List<String> projects);

    AaiResponse<SubscriberList> getFullSubscriberList();

    AaiResponse getServices(RoleValidator roleValidator);
    
    AaiResponse getAaiZones();

    AaiResponse getNetworkCollectionDetails(String serviceInstanceId);

    AaiResponse<AaiGetInstanceGroupsByCloudRegion> getInstanceGroupsByCloudRegion(String cloudOwner, String cloudRegionId, String networkFunction);

    AaiResponse<OperationalEnvironmentList> getOperationalEnvironments(String operationalEnvironmentType, String operationalEnvironmentStatus);

    AaiResponse getAicZoneForPnf(String globalCustomerId , String serviceType , String serviceId);

    Response getVNFData(String globalSubscriberId, String serviceType);

    AaiResponse<GetTenantsResponse[]> getTenants(String globalCustomerId, String serviceType, RoleValidator roleValidator);

    AaiResponse getVNFData(String globalSubscriberId, String serviceType, String serviceInstanceId);

    AaiResponse getNodeTemplateInstances(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion);

    Response getVersionByInvariantId(List<String> modelInvariantId);

    Collection<Service> getServicesByDistributionStatus();

    AaiResponse<Pnf> getSpecificPnf(String pnfId);

    List<String> getServiceInstanceAssociatedPnfs(String globalCustomerId, String serviceType, String serviceInstanceId);

    AaiResponse getPNFData(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion, String equipVendor, String equipModel);

    AaiResponseTranslator.PortMirroringConfigData getPortMirroringConfigData(String configurationId);

    List<PortDetailsTranslator.PortDetails> getPortMirroringSourcePorts(String configurationId);

    AaiResponse getInstanceGroupsByVnfInstanceId(String vnfInstanceId);
}
