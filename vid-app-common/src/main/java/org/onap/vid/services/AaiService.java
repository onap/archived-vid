package org.onap.vid.services;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.SubscriberFilteredResults;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.model.ServiceInstanceSearchResult;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.roles.RoleValidator;

import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;
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

}
