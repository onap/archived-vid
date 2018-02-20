package org.onap.vid.services;

import org.apache.http.HttpStatus;
import org.onap.vid.aai.model.AaiGetAicZone.AicZones;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.*;
import org.onap.vid.aai.ServiceInstance;
import org.onap.vid.aai.ServiceSubscription;
import org.onap.vid.aai.Services;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetServicesRequestModel.GetServicesAAIRespone;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.*;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.model.ServiceInstanceSearchResult;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.roles.RoleValidator;
import org.onap.vid.utils.Intersection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Oren on 7/4/17.
 */
public class AaiServiceImpl implements AaiService {
    private String serviceInstanceId = "service-instance.service-instance-id";
    private String serviceType = "service-subscription.service-type";
    private String customerId = "customer.global-customer-id";
    private String serviceInstanceName = "service-instance.service-instance-name";
    private int indexOfSubscriberName = 6;

    @Autowired
    private AaiClientInterface aaiClient;

    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AaiServiceImpl.class);

    private List<Service> convertModelToService(Model model) {
        List<Service> services = new ArrayList<>();
        String category = "";

        if(validateModel(model)){
            if(model.getModelType() != null)
                category = model.getModelType();

            for (ModelVer modelVer: model.getModelVers().getModelVer()) {
                Service service = new Service();
                if (modelVer.getModelVersionId() != null)
                    service.setUuid(modelVer.getModelVersionId());
                if(model.getModelInvariantId() != null)
                    service.setInvariantUUID(model.getModelInvariantId());
                if(modelVer.getModelVersion() != null)
                    service.setVersion(modelVer.getModelVersion());
                if(modelVer.getModelName() != null)
                    service.setName(modelVer.getModelName());
                if(modelVer.getDistributionStatus() != null)
                    service.setDistributionStatus(Service.DistributionStatus.valueOf(modelVer.getDistributionStatus()));
                service.setCategory(category);

                services.add(service);
            }
        } else {
            return null;
        }

        return services;
    }

    private boolean validateModel(Model model){
        if(model != null){
            if(model.getModelVers() != null && model.getModelVers().getModelVer() != null && model.getModelVers().getModelVer().get(0).getModelVersionId() != null){
                return true;
            }
        }
        return false;
    }

    private List<ServiceInstanceSearchResult> getServicesByOwningEntityId(List<String> owningEntities, RoleValidator roleValidator) {
        AaiResponse<OwningEntityResponse> owningEntityResponse = aaiClient.getServicesByOwningEntityId(owningEntities);
        List<ServiceInstanceSearchResult> serviceInstanceSearchResultList = new ArrayList<>();
        if (owningEntityResponse.getT() != null) {
            for (OwningEntity owningEntity : owningEntityResponse.getT().getOwningEntity()) {
                if (owningEntity.getRelationshipList() != null) {
                    serviceInstanceSearchResultList = convertRelationshipToSearchResult(owningEntity, serviceInstanceSearchResultList, roleValidator);
                }
            }
        }
        return serviceInstanceSearchResultList;
    }

    private List<ServiceInstanceSearchResult> getServicesByProjectNames(List<String> projectNames, RoleValidator roleValidator) {
        AaiResponse<ProjectResponse> projectByIdResponse = aaiClient.getServicesByProjectNames(projectNames);
        List<ServiceInstanceSearchResult> serviceInstanceSearchResultList = new ArrayList<>();
        if (projectByIdResponse.getT() != null) {
            for (Project project : projectByIdResponse.getT().getProject()) {
                if (project.getRelationshipList() != null)
                    serviceInstanceSearchResultList = convertRelationshipToSearchResult(project, serviceInstanceSearchResultList, roleValidator);
            }
        }
        return serviceInstanceSearchResultList;
    }

    private List<ServiceInstanceSearchResult> convertRelationshipToSearchResult(AaiRelationResponse owningEntityResponse, List<ServiceInstanceSearchResult> serviceInstanceSearchResultList, RoleValidator roleValidator) {
        if (owningEntityResponse.getRelationshipList().getRelationship() != null) {
            List<Relationship> relationshipList = owningEntityResponse.getRelationshipList().getRelationship();
            for (Relationship relationship : relationshipList) {
                ServiceInstanceSearchResult serviceInstanceSearchResult = new ServiceInstanceSearchResult();
                extractRelationshipData(relationship, serviceInstanceSearchResult, roleValidator);
                extractRelatedToProperty(relationship, serviceInstanceSearchResult);
                serviceInstanceSearchResultList.add(serviceInstanceSearchResult);
            }
        }
        return serviceInstanceSearchResultList;
    }

    private void extractRelationshipData(Relationship relationship, ServiceInstanceSearchResult serviceInstanceSearchResult, RoleValidator roleValidator) {
        List<RelationshipData> relationshipDataList = relationship.getRelationDataList();
        if (relationshipDataList != null) {
            setSubscriberName(relationship, serviceInstanceSearchResult);
            for (RelationshipData relationshipData : relationshipDataList) {
                String key = relationshipData.getRelationshipKey();
                if (key.equals(serviceInstanceId)) {
                    serviceInstanceSearchResult.setServiceInstanceId(relationshipData.getRelationshipValue());
                } else if (key.equals(serviceType)) {
                    serviceInstanceSearchResult.setServiceType(relationshipData.getRelationshipValue());
                } else if (key.equals(customerId)) {
                    serviceInstanceSearchResult.setGlobalCustomerId(relationshipData.getRelationshipValue());
                }
            }

            boolean isPermitted = roleValidator.isServicePermitted(serviceInstanceSearchResult.getSubscriberName(), serviceInstanceSearchResult.getServiceType());
            serviceInstanceSearchResult.setIsPermitted(isPermitted);
        }
    }

    private void setSubscriberName(Relationship relationship, ServiceInstanceSearchResult serviceInstanceSearchResult) {
        String relatedLink = relationship.getRelatedLink();
        String[] subsciber = relatedLink.split("/");
        serviceInstanceSearchResult.setSubscriberName(subsciber[indexOfSubscriberName]);
    }

    private void extractRelatedToProperty(Relationship relationship, ServiceInstanceSearchResult serviceInstanceSearchResult) {
        List<RelatedToProperty> relatedToPropertyList = relationship.getRelatedToPropertyList();
        if (relatedToPropertyList != null) {
            for (RelatedToProperty relatedToProperty : relatedToPropertyList) {
                if (relatedToProperty.getPropertyKey().equals(serviceInstanceName)) {
                    serviceInstanceSearchResult.setServiceInstanceName(relatedToProperty.getPropertyValue());
                }
            }
        }
    }

    @Override
    public SubscriberFilteredResults getFullSubscriberList(RoleValidator roleValidator) {
        AaiResponse<SubscriberList> subscriberResponse = aaiClient.getAllSubscribers();
        SubscriberFilteredResults subscriberFilteredResults =
                new SubscriberFilteredResults(roleValidator, subscriberResponse.getT(),
                        subscriberResponse.getErrorMessage(),
                        subscriberResponse.getHttpCode());

        return subscriberFilteredResults;
    }

    @Override
    public AaiResponse<OperationalEnvironmentList> getOperationalEnvironments(String operationalEnvironmentType, String operationalEnvironmentStatus) {
        AaiResponse<OperationalEnvironmentList> subscriberResponse = aaiClient.getOperationalEnvironments(operationalEnvironmentType, operationalEnvironmentStatus);
        return subscriberResponse;
    }

    @Override
    public AaiResponse<SubscriberList> getFullSubscriberList() {
        AaiResponse<SubscriberList> subscriberResponse = aaiClient.getAllSubscribers();
        return subscriberResponse;
    }

    @Override
    public AaiResponse getSubscriberData(String subscriberId, RoleValidator roleValidator) {
        AaiResponse<Services> subscriberResponse = aaiClient.getSubscriberData(subscriberId);
        String subscriberGlobalId = subscriberResponse.getT().globalCustomerId;
        for (ServiceSubscription serviceSubscription : subscriberResponse.getT().serviceSubscriptions.serviceSubscription) {
            String serviceType = serviceSubscription.serviceType;
            serviceSubscription.isPermitted = roleValidator.isServicePermitted(subscriberGlobalId, serviceType);
        }
        return subscriberResponse;

    }

    @Override
    public AaiResponse getServiceInstanceSearchResults(String subscriberId, String instanceIdentifier, RoleValidator roleValidator, List<String> owningEntities, List<String> projects) {
        List<List<ServiceInstanceSearchResult>> resultList = new ArrayList<>();
        ServiceInstancesSearchResults serviceInstancesSearchResults = new ServiceInstancesSearchResults();

        if (subscriberId != null || instanceIdentifier != null) {
            resultList.add(getServicesBySubscriber(subscriberId, instanceIdentifier, roleValidator));
        }
        if (owningEntities != null) {
            resultList.add(getServicesByOwningEntityId(owningEntities, roleValidator));
        }
        if (projects != null) {
            resultList.add(getServicesByProjectNames(projects, roleValidator));
        }
        if (resultList.size() > 0) {
            Intersection<ServiceInstanceSearchResult> intersection = new Intersection<>();
            serviceInstancesSearchResults.serviceInstances = intersection.intersectMultipileArray(resultList);
        }

        return new AaiResponse<>(serviceInstancesSearchResults, null, HttpStatus.SC_OK);
    }


    private List<ServiceInstanceSearchResult> getServicesBySubscriber(String subscriberId, String instanceIdentifier, RoleValidator roleValidator) {
        AaiResponse<Services> subscriberResponse = aaiClient.getSubscriberData(subscriberId);
        String subscriberGlobalId = subscriberResponse.getT().globalCustomerId;
        String subscriberName = subscriberResponse.getT().subscriberName;
        ServiceSubscriptions serviceSubscriptions = subscriberResponse.getT().serviceSubscriptions;

        return getSearchResultsForSubscriptions(serviceSubscriptions, subscriberId, instanceIdentifier, roleValidator, subscriberGlobalId, subscriberName);

    }


    private ArrayList<ServiceInstanceSearchResult> getSearchResultsForSubscriptions(ServiceSubscriptions serviceSubscriptions, String subscriberId, String instanceIdentifier, RoleValidator roleValidator, String subscriberGlobalId, String subscriberName) {
        ArrayList<ServiceInstanceSearchResult> results = new ArrayList<>();

        if (serviceSubscriptions != null) {
            for (ServiceSubscription serviceSubscription : serviceSubscriptions.serviceSubscription) {
                String serviceType = serviceSubscription.serviceType;
                serviceSubscription.isPermitted = roleValidator.isServicePermitted(subscriberGlobalId, serviceType);
                ArrayList<ServiceInstanceSearchResult> resultsForSubscription = getSearchResultsForSingleSubscription(serviceSubscription, subscriberId, instanceIdentifier, subscriberName, serviceType);
                results.addAll(resultsForSubscription);
            }
        }

        return results;
    }

    private ArrayList<ServiceInstanceSearchResult> getSearchResultsForSingleSubscription(ServiceSubscription serviceSubscription, String subscriberId, String instanceIdentifier, String subscriberName, String serviceType) {
        ArrayList<ServiceInstanceSearchResult> results = new ArrayList<>();

        if (serviceSubscription.serviceInstances != null) {
            for (ServiceInstance serviceInstance : serviceSubscription.serviceInstances.serviceInstance) {
                ServiceInstanceSearchResult serviceInstanceSearchResult =
                        new ServiceInstanceSearchResult(serviceInstance.serviceInstanceId, subscriberId, serviceType, serviceInstance.serviceInstanceName,
                                subscriberName, serviceInstance.modelInvariantId, serviceInstance.modelVersionId, serviceSubscription.isPermitted);

                if (instanceIdentifier == null) {
                    results.add(serviceInstanceSearchResult);
                } else if (serviceInstanceMatchesIdentifier(instanceIdentifier, serviceInstance)) {
                    results.add(serviceInstanceSearchResult);
                }
            }
        }

        return results;
    }

    private boolean serviceInstanceMatchesIdentifier(String instanceIdentifier, ServiceInstance serviceInstance) {
        return instanceIdentifier.equals(serviceInstance.serviceInstanceId) || instanceIdentifier.equals(serviceInstance.serviceInstanceName);
    }

    @Override
    public Response getVersionByInvariantId(List<String> modelInvariantId) {
        try {
            return aaiClient.getVersionByInvariantId(modelInvariantId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AaiResponse<Pnf> getSpecificPnf(String pnfId) {
        return aaiClient.getSpecificPnf(pnfId);
    }

    @Override
    public AaiResponse getPNFData(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion, String equipVendor, String equipModel) {
        return aaiClient.getPNFData(globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegion, equipVendor, equipModel);
    }



    @Override
    public AaiResponse getServices(RoleValidator roleValidator) {
        AaiResponse<GetServicesAAIRespone> subscriberResponse = aaiClient.getServices();
        if (subscriberResponse.getT() != null)
            for (org.onap.vid.aai.model.AaiGetServicesRequestModel.Service service : subscriberResponse.getT().service) {
                service.isPermitted = true;
            }
        return subscriberResponse;
    }

    @Override
    public AaiResponse<GetTenantsResponse[]> getTenants(String globalCustomerId, String serviceType, RoleValidator roleValidator) {
        AaiResponse<GetTenantsResponse[]> aaiGetTenantsResponse = aaiClient.getTenants(globalCustomerId, serviceType);
        GetTenantsResponse[] tenants = aaiGetTenantsResponse.getT();
        if (tenants != null) {
            for (int i = 0; i < tenants.length; i++) {
                tenants[i].isPermitted = roleValidator.isTenantPermitted(globalCustomerId, serviceType, tenants[i].tenantName);
            }
        }
        return aaiGetTenantsResponse;


    }

    @Override
    public AaiResponse getVNFData(String globalSubscriberId, String serviceType, String serviceInstanceId) {
        return aaiClient.getVNFData(globalSubscriberId, serviceType, serviceInstanceId);
    }

    @Override
    public Response getVNFData(String globalSubscriberId, String serviceType) {
        return aaiClient.getVNFData(globalSubscriberId, serviceType);
    }

    @Override
    public AaiResponse getAaiZones() {
        AaiResponse<AicZones> response = aaiClient.getAllAicZones();
        return response;
    }

    @Override
    public AaiResponse getAicZoneForPnf(String globalCustomerId, String serviceType, String serviceId) {
        String aicZone = "";

        AaiResponse<ServiceRelationships> serviceInstanceResp = aaiClient.getServiceInstance(globalCustomerId, serviceType, serviceId);
        if (serviceInstanceResp.getT() != null) {
            List<String> aicZoneList = getRelationshipDataByType(serviceInstanceResp.getT().getRelationshipList(), "zone", "zone.zone-id");
            if (aicZoneList.size() > 0) {
                aicZone = aicZoneList.get(0);
            } else {
                logger.warn("aic zone not found for service instance " + serviceId);
            }
        } else {
            if (serviceInstanceResp.getErrorMessage() != null) {
                logger.error("get service instance " + serviceId + " return error", serviceInstanceResp.getErrorMessage());
                return new AaiResponse(aicZone , serviceInstanceResp.getErrorMessage() ,serviceInstanceResp.getHttpCode());
            } else {
                logger.warn("get service instance " + serviceId + " return empty body");
                return new AaiResponse(aicZone , "get service instance " + serviceId + " return empty body" ,serviceInstanceResp.getHttpCode());
            }
        }

        return new AaiResponse(aicZone , null ,HttpStatus.SC_OK);
    }

    @Override
    public AaiResponse getNodeTemplateInstances(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion) {
        return aaiClient.getNodeTemplateInstances(globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegion);
    }

    @Override
    public Collection<Service> getServicesByDistributionStatus() {
        AaiResponse<GetServiceModelsByDistributionStatusResponse> serviceModelsByDistributionStatusResponse = aaiClient.getServiceModelsByDistributionStatus();
        Collection<Service> services = new ArrayList<>();
        if (serviceModelsByDistributionStatusResponse.getT() != null) {
            List<Result> results = serviceModelsByDistributionStatusResponse.getT().getResults();
            for (Result result : results) {
                if(result.getModel() != null) {
                    List<Service> service = convertModelToService(result.getModel());
                    if (service != null) {
                        services.addAll(service);
                    }
                }
            }
        }
        return services;
    }

    @Override
    public List<String> getServiceInstanceAssociatedPnfs(String globalCustomerId, String serviceType, String serviceInstanceId) {
        List<String> pnfs = new ArrayList<>();

        AaiResponse<ServiceRelationships> serviceInstanceResp = aaiClient.getServiceInstance(globalCustomerId, serviceType, serviceInstanceId);
        if (serviceInstanceResp.getT() != null) {
            List<String> logicalLinks = getRelationshipDataByType(serviceInstanceResp.getT().getRelationshipList(), "logical-link", "logical-link.link-name");
            for (String logicalLink : logicalLinks) {
                String link = "";
                try {
                    link = URLEncoder.encode(logicalLink, "UTF-8");
                    AaiResponse<LogicalLinkResponse> logicalLinkResp = aaiClient.getLogicalLink(link);
                    if (logicalLinkResp.getT() != null) {
                        //lag-interface is the key for pnf - approved by Bracha
                        List<String> linkPnfs = getRelationshipDataByType(logicalLinkResp.getT().getRelationshipList(), "lag-interface", "pnf.pnf-name");
                        if (linkPnfs.size() > 0) {
                            pnfs.addAll(linkPnfs);
                        } else {
                            logger.warn("no pnf found for logical link " + logicalLink);
                        }
                    } else {
                        if (logicalLinkResp.getErrorMessage() != null) {
                            logger.error("get logical link " + logicalLink + " return error", logicalLinkResp.getErrorMessage());
                        } else {
                            logger.warn("get logical link " + logicalLink + " return empty body");
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    logger.error("Failed to encode logical link: " + logicalLink, e.getMessage());
                }
            }
        } else {
            if (serviceInstanceResp.getErrorMessage() != null) {
                logger.error("get service instance " + serviceInstanceId + " return error", serviceInstanceResp.getErrorMessage());
            } else {
                logger.warn("get service instance " + serviceInstanceId + " return empty body");
            }
        }

        return pnfs.stream().distinct().collect(Collectors.toList());
    }

    private List<String> getRelationshipDataByType(RelationshipList relationshipList, String relationshipType, String relationshipDataKey) {
        List<String> relationshipValues = new ArrayList<>();
        for (Relationship relationship : relationshipList.getRelationship()) {
            if (relationship.getRelatedTo().equals(relationshipType)) {
                relationshipValues.addAll( relationship.getRelationDataList().stream()
                        .filter(rel -> rel.getRelationshipKey().equals(relationshipDataKey))
                        .map(RelationshipData::getRelationshipValue)
                        .collect(Collectors.toList())
                );
            }
        }


        return relationshipValues;
    }
}
