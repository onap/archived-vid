package org.openecomp.vid.aai;

import org.openecomp.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.openecomp.vid.model.SubscriberList;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Oren on 7/4/17.
 */
public interface AaiClientInterface {

    AaiResponse<SubscriberList> getAllSubscribers();

    AaiResponse getSubscriberData(String subscriberId);

    AaiResponse getServices();

    AaiResponse<GetTenantsResponse[]> getTenants(String globalCustomerId, String serviceType);
    
    AaiResponse getAllAicZones();
    
    AaiResponse getAicZoneForPnf(String globalCustomerId , String serviceType , String serviceId);

	AaiResponse getVNFData();

    Response getVNFData(String globalSubscriberId, String serviceType);

    AaiResponse getVNFData(String globalSubscriberId, String serviceType, String serviceInstanceId);

    Response getVersionByInvariantId(List<String> modelInvariantId);
}
