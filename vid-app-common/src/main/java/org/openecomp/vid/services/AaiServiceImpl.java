package org.openecomp.vid.services;

import org.ecomp.aai.model.AaiAICZones.AicZones;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.vid.aai.*;
import org.openecomp.vid.aai.model.AaiGetServicesRequestModel.*;
import org.openecomp.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.openecomp.vid.model.*;
import org.openecomp.vid.roles.RoleValidator;
import org.openecomp.vid.scheduler.SchedulerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.ws.rs.core.Response;

import java.util.List;

/**
 * Created by Oren on 7/4/17.
 */
public class AaiServiceImpl implements AaiService {


    @Autowired
    private AaiClientInterface aaiClient;


    @Override
    public SubscriberFilteredResults getFullSubscriberList(RoleValidator roleValidator) {
        AaiResponse<SubscriberList> subscriberResponse = aaiClient.getAllSubscribers();
        SubscriberFilteredResults subscriberFilteredResults =
                new SubscriberFilteredResults(roleValidator,subscriberResponse.getT(),
                        subscriberResponse.getErrorMessage(),
                        subscriberResponse.getHttpCode());

        return subscriberFilteredResults;
    }

    @Override
    public AaiResponse getSubscriberData(String subscriberId, RoleValidator roleProvider) {
        AaiResponse<Services> subscriberResponse = aaiClient.getSubscriberData(subscriberId);
        String subscriberGlobalId = subscriberResponse.getT().globalCustomerId;
        for (ServiceSubscription serviceSubscription : subscriberResponse.getT().serviceSubscriptions.serviceSubscription) {
            String serviceType = serviceSubscription.serviceType;
            serviceSubscription.isPermitted = roleProvider.isServicePermitted(subscriberGlobalId,serviceType);
        }
        return subscriberResponse;

    }

    @Override
    public Response getVersionByInvariantId(List<String> modelInvariantId) {
        try {
            return aaiClient.getVersionByInvariantId(modelInvariantId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AaiResponse getServices(RoleValidator roleValidator) {
        AaiResponse<GetServicesAAIRespone> subscriberResponse = aaiClient.getServices();
        for (org.openecomp.vid.aai.model.AaiGetServicesRequestModel.Service service :subscriberResponse.getT().service){
            service.isPermitted = true;
        }
        return subscriberResponse;
    }

    @Override
    public AaiResponse<GetTenantsResponse[]> getTenants(String globalCustomerId, String serviceType, RoleValidator roleValidator) {
        AaiResponse<GetTenantsResponse[]> aaiGetTenantsResponse = aaiClient.getTenants(globalCustomerId,serviceType);
        GetTenantsResponse[] tenants = aaiGetTenantsResponse.getT();
        for (int i=0;i<tenants.length;i++){
            tenants[i].isPermitted = roleValidator.isTenantPermitted(globalCustomerId,serviceType, tenants[i].tenantID);
        }
        return aaiGetTenantsResponse;
    }

    @Override
    public AaiResponse getVNFData(String globalSubscriberId, String serviceType, String serviceInstanceId) {
        return aaiClient.getVNFData(globalSubscriberId,serviceType,serviceInstanceId);
    }

    @Override
    public Response getVNFData(String globalSubscriberId, String serviceType) {
        return aaiClient.getVNFData(globalSubscriberId,serviceType);
    }

    @Override
	public AaiResponse getAaiZones() {
		AaiResponse<AicZones> response = aaiClient.getAllAicZones();
		return response;
	}

	@Override
	public AaiResponse getAicZoneForPnf(String globalCustomerId , String serviceType , String serviceId) {
		AaiResponse<AicZones> response = aaiClient.getAicZoneForPnf(globalCustomerId , serviceType , serviceId);
		return response;
	}
}
