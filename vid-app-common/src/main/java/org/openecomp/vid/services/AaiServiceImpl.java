package org.openecomp.vid.services;

import org.ecomp.aai.model.AaiAICZones.AicZones;
import org.openecomp.vid.aai.*;
import org.openecomp.vid.aai.model.AaiGetServicesRequestModel.*;
import org.openecomp.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.openecomp.vid.model.*;
import org.openecomp.vid.roles.RoleValidator;
import org.springframework.beans.factory.annotation.Autowired;

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
            serviceSubscription.isPermitted = roleProvider.isServicePermitted(subscriberGlobalId,serviceType);;
        }
        return subscriberResponse;

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
	public AaiResponse getAaiZones() {
		AaiResponse<AicZones> response = aaiClient.getAllAicZones();
		return response;
	}
}
