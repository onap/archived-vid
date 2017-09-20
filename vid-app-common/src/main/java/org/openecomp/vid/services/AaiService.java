package org.openecomp.vid.services;

import org.openecomp.vid.aai.AaiResponse;
import org.openecomp.vid.aai.SubscriberFilteredResults;
import org.openecomp.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.openecomp.vid.roles.RoleValidator;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Oren on 7/4/17.
 */
public interface AaiService {


    SubscriberFilteredResults getFullSubscriberList(RoleValidator roleValidator);

    AaiResponse getSubscriberData(String subscriberId, RoleValidator roleValidator);

    AaiResponse getServices(RoleValidator roleValidator);
    
    AaiResponse getAaiZones();

    AaiResponse getAicZoneForPnf(String globalCustomerId , String serviceType , String serviceId);

    Response getVNFData(String globalSubscriberId, String serviceType);

    AaiResponse<GetTenantsResponse[]> getTenants(String globalCustomerId, String serviceType, RoleValidator roleValidator);

    AaiResponse getVNFData(String globalSubscriberId, String serviceType, String serviceInstanceId);

    Response getVersionByInvariantId(List<String> modelInvariantId);
}
