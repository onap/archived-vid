package org.onap.vid.aai;

import com.fasterxml.jackson.databind.JsonNode;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.CustomQuerySimpleResult;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.aai.model.Properties;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.model.probes.ExternalComponentStatus;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Created by Oren on 7/4/17.
 */
public interface AaiClientInterface {

    boolean isNodeTypeExistsByName(String name, ResourceType type);

    <T> T typedAaiGet(URI path, Class<T> clz);

    AaiResponse<SubscriberList> getAllSubscribers();

    AaiResponse getSubscriberData(String subscriberId);

    AaiResponse getServices();

    AaiResponse getServicesByOwningEntityId(List<String> owningEntityIds);

    AaiResponse<GetTenantsResponse[]> getTenants(String globalCustomerId, String serviceType);

    AaiResponse<OperationalEnvironmentList> getOperationalEnvironments(String operationalEnvironmentType, String operationalEnvironmentStatus);

    AaiResponse getAllAicZones();

    AaiResponse getNetworkCollectionDetails(String serviceInstanceId);

    AaiResponse getInstanceGroupsByCloudRegion(String cloudOwner, String cloudRegionId, String networkFunction);

    AaiResponse getVNFData(String globalSubscriberId, String serviceType);

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

    Response doAaiGet(String uri, boolean xml);

    String getCloudOwnerByCloudRegionId(String cloudRegionId);

    GetTenantsResponse getHomingDataByVfModule(String vnfInstanceId, String vfModuleId);

    void resetCache(String cacheName);

    Map<String, Properties> getCloudRegionAndTenantByVnfId(String vnfId);
}
