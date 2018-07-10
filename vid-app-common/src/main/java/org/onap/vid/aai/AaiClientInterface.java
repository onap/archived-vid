package org.onap.vid.aai;

import org.codehaus.jackson.JsonNode;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.AaiNodeQueryResponse;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.model.probes.ExternalComponentStatus;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Oren on 7/4/17.
 */
public interface AaiClientInterface {

    AaiResponse<AaiNodeQueryResponse> searchNodeTypeByName(String name, ResourceType type);

    AaiResponse<SubscriberList> getAllSubscribers();

    AaiResponse getSubscriberData(String subscriberId);

    AaiResponse getServices();

    AaiResponse getServicesByOwningEntityId(List<String> owningEntityIds);

    AaiResponse<GetTenantsResponse[]> getTenants(String globalCustomerId, String serviceType);

    AaiResponse<OperationalEnvironmentList> getOperationalEnvironments(String operationalEnvironmentType, String operationalEnvironmentStatus);

    AaiResponse getAllAicZones();

    AaiResponse getAicZoneForPnf(String globalCustomerId , String serviceType , String serviceId);

	AaiResponse getVNFData();

    AaiResponse getNetworkCollectionDetails(String serviceInstanceId);

    AaiResponse getInstanceGroupsByCloudRegion(String cloudOwner, String cloudRegionId, String networkFunction);

    Response getVNFData(String globalSubscriberId, String serviceType);

    AaiResponse getVNFData(String globalSubscriberId, String serviceType, String serviceInstanceId);

    AaiResponse getNodeTemplateInstances(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion);

    Response getVersionByInvariantId(List<String> modelInvariantId);

    AaiResponse getServicesByProjectNames(List<String> projectNames);

    AaiResponse getServiceModelsByDistributionStatus();
	
    AaiResponse getPNFData(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion, String equipVendor, String equipModel);

    AaiResponse<Pnf> getSpecificPnf(String pnfId);

    AaiResponse getServiceInstance(String globalCustomerId, String serviceType, String serviceInstanceId);

    AaiResponse getLogicalLink(String link);

    AaiResponse<JsonNode> getCloudRegionAndSourceByPortMirroringConfigurationId(String configurationId);

    List<PortDetailsTranslator.PortDetails> getPortMirroringSourcePorts(String configurationID);

    AaiResponse getInstanceGroupsByVnfInstanceId(String vnfInstanceId);

    ExternalComponentStatus probeAaiGetAllSubscribers();
}
