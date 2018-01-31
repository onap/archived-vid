package org.onap.vid.aai;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetServicesRequestModel.GetServicesAAIRespone;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.model.SubscriberList;

import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.List;

/**
 * Created by Oren on 7/4/17.
 */
public interface AaiClientInterface {

    AaiResponse<SubscriberList> getAllSubscribers();

    AaiResponse getSubscriberData(String subscriberId);

    AaiResponse getServices();

    AaiResponse getServicesByOwningEntityId(List<String> owningEntityIds);

    AaiResponse<GetTenantsResponse[]> getTenants(String globalCustomerId, String serviceType);

    AaiResponse<OperationalEnvironmentList> getOperationalEnvironments(String operationalEnvironmentType, String operationalEnvironmentStatus);

    AaiResponse getAllAicZones();

    AaiResponse getAicZoneForPnf(String globalCustomerId , String serviceType , String serviceId);

	AaiResponse getVNFData();

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
}
