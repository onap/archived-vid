package org.openecomp.vid.services;

import org.openecomp.vid.aai.AaiResponse;
import org.openecomp.vid.aai.SubscriberFilteredResults;
import org.openecomp.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.openecomp.vid.roles.RoleValidator;

import java.util.List;

/**
 * Created by Oren on 7/4/17.
 */
public interface AaiService {


    SubscriberFilteredResults getFullSubscriberList(RoleValidator roleValidator);

    AaiResponse getSubscriberData(String subscriberId, RoleValidator roleValidator);

    AaiResponse getServices(RoleValidator roleValidator);
    
    AaiResponse getAaiZones();

    AaiResponse<GetTenantsResponse[]> getTenants(String globalCustomerId, String serviceType, RoleValidator roleValidator);
}
