package org.onap.vid.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.onap.vid.aai.*;
import org.onap.vid.aai.ServiceInstance;
import org.onap.vid.aai.ServiceSubscription;
import org.onap.vid.aai.Services;
import org.onap.vid.aai.model.AaiGetAicZone.AicZones;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.AaiGetNetworkCollectionDetails;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.AaiGetRelatedInstanceGroupsByVnfId;
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
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Oren on 7/4/17.
 */
public class AaiServiceImpl implements AaiService {
    private static final String SERVICE_INSTANCE_ID = "service-instance.service-instance-id";
    private static final String SERVICE_TYPE = "service-subscription.service-type";
    private static final String CUSTOMER_ID = "customer.global-customer-id";
    private static final String SERVICE_INSTANCE_NAME = "service-instance.service-instance-name";
    private int indexOfSubscriberName = 6;

    @Autowired
    private AaiOverTLSClientInterface aaiOverTLSClientInterface;

    @Autowired
    private AaiResponseTranslator aaiResponseTranslator;

    @Autowired
    private PortDetailsTranslator portDetailsTranslator;

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(AaiServiceImpl.class);

    private List<Service> convertModelToService(Model model) {
        List<Service> services = new ArrayList<>();
        String category = "";

        if(validateModel(model)){
            if(model.getModelType() != null) {
                category = model.getModelType();
            }

            for (ModelVer modelVer: model.getModelVers().getModelVer()) {
                Service service = new Service(
                        modelVer.getModelVersionId(),
                        model.getModelInvariantId(),
                        category, modelVer.getModelVersion(), modelVer.getModelName(),
                        modelVer.getDistributionStatus(),
                        null, null, null, null
                );

                services.add(service);
            }
        } else {
            return Collections.emptyList();
        }

        return services;
    }

    private boolean validateModel(Model model){
        if (model == null) {
            return false;
        } else {
            return model.getModelVers() != null && model.getModelVers().getModelVer() != null && model.getModelVers().getModelVer().get(0).getModelVersionId() != null;
        }
    }

    private List<ServiceInstanceSearchResult> getServicesByOwningEntityId(List<String> owningEntities, RoleValidator roleValidator) {
        HttpResponse<OwningEntityResponse> servicesByOwningEntityId = aaiOverTLSClientInterface
            .getServicesByOwningEntityId(owningEntities);
        List<ServiceInstanceSearchResult> serviceInstanceSearchResultList = new ArrayList<>();
        if (servicesByOwningEntityId.getBody() != null) {
            for (OwningEntity owningEntity : servicesByOwningEntityId.getBody().getOwningEntity()) {
                if (owningEntity.getRelationshipList() != null) {
                    serviceInstanceSearchResultList = convertRelationshipToSearchResult(owningEntity, serviceInstanceSearchResultList, roleValidator);
                }
            }
        }
        return serviceInstanceSearchResultList;
    }

    private List<ServiceInstanceSearchResult> getServicesByProjectNames(List<String> projectNames, RoleValidator roleValidator) {
        HttpResponse<ProjectResponse> servicesByProjectNames = aaiOverTLSClientInterface
            .getServicesByProjectNames(projectNames);
        List<ServiceInstanceSearchResult> serviceInstanceSearchResultList = new ArrayList<>();
        if (servicesByProjectNames.getBody() != null) {
            for (Project project : servicesByProjectNames.getBody().getProject()) {
                if (project.getRelationshipList() != null) {
                    serviceInstanceSearchResultList = convertRelationshipToSearchResult(project, serviceInstanceSearchResultList, roleValidator);
                }
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
                if (key.equals(SERVICE_INSTANCE_ID)) {
                    serviceInstanceSearchResult.setServiceInstanceId(relationshipData.getRelationshipValue());
                } else if (key.equals(SERVICE_TYPE)) {
                    serviceInstanceSearchResult.setServiceType(relationshipData.getRelationshipValue());
                } else if (key.equals(CUSTOMER_ID)) {
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
                if (relatedToProperty.getPropertyKey().equals(SERVICE_INSTANCE_NAME)) {
                    serviceInstanceSearchResult.setServiceInstanceName(relatedToProperty.getPropertyValue());
                }
            }
        }
    }

    @Override
    public HttpResponse<SubscriberList> getFullSubscriberList(RoleValidator roleValidator) {
        return aaiOverTLSClientInterface.getAllSubscribers();
    }

    @Override
    public HttpResponse<OperationalEnvironmentList> getOperationalEnvironments(String operationalEnvironmentType, String operationalEnvironmentStatus) {
        return aaiOverTLSClientInterface.getOperationalEnvironments(operationalEnvironmentType, operationalEnvironmentStatus);
    }

    @Override
    public HttpResponse<SubscriberList> getFullSubscriberList() {
        return aaiOverTLSClientInterface.getAllSubscribers();
    }

    @Override
    public HttpResponse<Services> getSubscriberData(String subscriberId, RoleValidator roleValidator) {
        HttpResponse<Services> subscriberData = aaiOverTLSClientInterface.getSubscriberData(subscriberId);
        Services services = subscriberData.getBody();
        String subscriberGlobalId = services.globalCustomerId;
        for (ServiceSubscription serviceSubscription : services.serviceSubscriptions.serviceSubscription) {
            String serviceType = serviceSubscription.serviceType;
            serviceSubscription.isPermitted = roleValidator.isServicePermitted(subscriberGlobalId, serviceType);
        }
        return subscriberData;
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
        if (!resultList.isEmpty()) {
            Intersection<ServiceInstanceSearchResult> intersection = new Intersection<>();
            serviceInstancesSearchResults.serviceInstances = intersection.intersectMultipileArray(resultList);
        }

        return new AaiResponse<>(serviceInstancesSearchResults, null, HttpStatus.SC_OK);
    }


    private List<ServiceInstanceSearchResult> getServicesBySubscriber(String subscriberId, String instanceIdentifier, RoleValidator roleValidator) {
        HttpResponse<Services> subscriberResponse = aaiOverTLSClientInterface.getSubscriberData(subscriberId);
        String subscriberGlobalId = subscriberResponse.getBody().globalCustomerId;
        String subscriberName = subscriberResponse.getBody().subscriberName;
        ServiceSubscriptions serviceSubscriptions = subscriberResponse.getBody().serviceSubscriptions;

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
    public HttpResponse<ResponseWithRequestInfo> getVersionByInvariantId(List<String> modelInvariantId) {
        try {
            return aaiOverTLSClientInterface.getVersionByInvariantId(modelInvariantId);
        } catch (Exception e) {
            LOGGER.error(EELFLoggerDelegate.errorLogger, "Failed to getVersionByInvariantId from A&AI", e);
        }
        return null;
    }

    @Override
    public HttpResponse<Pnf> getSpecificPnf(String pnfId) {
        return aaiOverTLSClientInterface.getSpecificPnf(pnfId);
    }

    @Override
    public HttpResponse<AaiGetPnfResponse> getPNFData(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion, String equipVendor, String equipModel) {
        return aaiOverTLSClientInterface.getPNFData(globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegion, equipVendor, equipModel);
    }

    @Override
    public HttpResponse<GetServicesAAIRespone> getServices(RoleValidator roleValidator) {
        HttpResponse<GetServicesAAIRespone> subscriberResponse = aaiOverTLSClientInterface.getServices();
        if (subscriberResponse.getBody() != null) {
            for (org.onap.vid.aai.model.AaiGetServicesRequestModel.Service service : subscriberResponse.getBody().service) {
                service.isPermitted = true;
            }
        }
        return subscriberResponse;
    }

    @Override
    public HttpResponse<GetTenantsResponse[]> getTenants(String globalCustomerId, String serviceType, RoleValidator roleValidator) {
        HttpResponse<GetTenantsResponse[]> tenantsResponse = aaiOverTLSClientInterface.getTenants(globalCustomerId, serviceType);
        GetTenantsResponse[] tenants = tenantsResponse.getBody();
        if (tenants != null) {
            for (int i = 0; i < tenants.length; i++) {
                tenants[i].isPermitted = roleValidator.isTenantPermitted(globalCustomerId, serviceType, tenants[i].tenantName);
            }
        }
        return tenantsResponse;
    }

    @Override
    public HttpResponse<AaiGetVnfResponse> getVNFData(String globalSubscriberId, String serviceType, String serviceInstanceId) {
        return aaiOverTLSClientInterface.getVNFData(globalSubscriberId, serviceType, serviceInstanceId);
    }

    @Override
    public HttpResponse<String> getVNFData(String globalSubscriberId, String serviceType) {
        return aaiOverTLSClientInterface.getVNFData(globalSubscriberId, serviceType);
    }

    @Override
    public HttpResponse<AicZones> getAaiZones() {
        return aaiOverTLSClientInterface.getAllAicZones();
    }

    @Override
    public AaiResponse getAicZoneForPnf(String globalCustomerId, String serviceType, String serviceId) {
        String aicZone = "";

        HttpResponse<ServiceRelationships> serviceInstanceResp = aaiOverTLSClientInterface.getServiceInstance(globalCustomerId, serviceType, serviceId);
        if (serviceInstanceResp.getBody() != null) {
            List<String> aicZoneList = getRelationshipDataByType(serviceInstanceResp.getBody().getRelationshipList(), "zone", "zone.zone-id");
            if (!aicZoneList.isEmpty()) {
                aicZone = aicZoneList.get(0);
            } else {
                LOGGER.warn("aic zone not found for service instance " + serviceId);
            }
        } else {
            try {
                String message = IOUtils.toString(serviceInstanceResp.getRawBody(), "UTF-8");
                LOGGER.error("get service instance {} return error {}", serviceId, message);
                return new AaiResponse(aicZone , message, serviceInstanceResp.getStatus());
            } catch (IOException e) {
                LOGGER.warn("get service instance {} return empty body", serviceId);
                return new AaiResponse(aicZone , "get service instance " + serviceId + " return empty body", serviceInstanceResp.getStatus());
            }
        }
        return new AaiResponse(aicZone , null ,HttpStatus.SC_OK);
    }

    @Override
    public HttpResponse<AaiGetVnfResponse> getNodeTemplateInstances(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion) {
        return aaiOverTLSClientInterface.getNodeTemplateInstances(globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegion);
    }

    @Override
    public HttpResponse<AaiGetNetworkCollectionDetails> getNetworkCollectionDetails(String serviceInstanceId){
        HttpResponse<AaiGetNetworkCollectionDetails> getNetworkCollectionDetailsAaiResponse = aaiOverTLSClientInterface.getNetworkCollectionDetails(serviceInstanceId);
        return getNetworkCollectionDetailsAaiResponse;
    }

    @Override
    public HttpResponse<AaiGetInstanceGroupsByCloudRegion> getInstanceGroupsByCloudRegion(String cloudOwner, String cloudRegionId, String networkFunction){
        HttpResponse<AaiGetInstanceGroupsByCloudRegion> getInstanceGroupsByCloudRegionResponse = aaiOverTLSClientInterface.getInstanceGroupsByCloudRegion(cloudOwner, cloudRegionId, networkFunction);
        return getInstanceGroupsByCloudRegionResponse;
    }

    @Override
    public Collection<Service> getServicesByDistributionStatus() {
        HttpResponse<GetServiceModelsByDistributionStatusResponse> serviceModelsByDistributionStatusResponse = aaiOverTLSClientInterface.getServiceModelsByDistributionStatus();
        Collection<Service> services = new ArrayList<>();
        if (serviceModelsByDistributionStatusResponse.getBody() != null) {
            List<Result> results = serviceModelsByDistributionStatusResponse.getBody().getResults();
            for (Result result : results) {
                if(result.getModel() != null) {
                    List<Service> service = convertModelToService(result.getModel());
                    services.addAll(service);
                }
            }
        }
        return services;
    }

    @Override
    public List<String> getServiceInstanceAssociatedPnfs(String globalCustomerId, String serviceType, String serviceInstanceId) {
        List<String> pnfs = new ArrayList<>();

        HttpResponse<ServiceRelationships> serviceInstanceResp = aaiOverTLSClientInterface.getServiceInstance(globalCustomerId, serviceType, serviceInstanceId);
        if (serviceInstanceResp.getBody() != null) {

            addPnfsToListViaLogicalLinks(pnfs, serviceInstanceResp);
            addPnfsToListViaDirectRelations(pnfs, serviceInstanceResp);

            if (pnfs.isEmpty()) {
                LOGGER.warn("no pnf direct relation found for service id:" + serviceInstanceId+
                        " name: "+serviceInstanceResp.getBody().getServiceInstanceName());
            }
        } else {
            try {
                String error = IOUtils.toString(serviceInstanceResp.getRawBody(), "UTF-8");
                LOGGER.error("get service instance {} return error {}", serviceInstanceId, error);
            } catch (IOException e) {
                LOGGER.warn("get service instance {} return empty body", serviceInstanceId);
            }
        }

        return pnfs.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public AaiResponseTranslator.PortMirroringConfigData getPortMirroringConfigData(String configurationId) {
        HttpResponse<JsonNode> aaiResponse = aaiOverTLSClientInterface.getCloudRegionAndSourceByPortMirroringConfigurationId(configurationId);
        return aaiResponseTranslator.extractPortMirroringConfigData(aaiResponse);
    }

    @Override
    public AaiResponse getInstanceGroupsByVnfInstanceId(String vnfInstanceId) throws IOException {
        HttpResponse<AaiGetRelatedInstanceGroupsByVnfId> aaiResponse = aaiOverTLSClientInterface.getInstanceGroupsByVnfInstanceId(vnfInstanceId);
        String error = null;
        if(aaiResponse.getStatus() != HttpStatus.SC_OK){
            error = IOUtils.toString(aaiResponse.getRawBody(), "UTF-8");
        }
        return new AaiResponse(convertGetInstanceGroupsResponseToSimpleResponse(aaiResponse.getBody()), error, aaiResponse.getStatus());
    }

    private List<InstanceGroupInfo> convertGetInstanceGroupsResponseToSimpleResponse(AaiGetRelatedInstanceGroupsByVnfId response) {
        List<InstanceGroupInfo> instanceGroupInfoList = new ArrayList<>();
        for(org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Relationship relationship: response.getRelationshipList().getRelationship()){
            getInstanceGroupInfoFromRelationship(relationship, instanceGroupInfoList);
        }
        return instanceGroupInfoList;
    }

    private void getInstanceGroupInfoFromRelationship(org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Relationship relationship, List<InstanceGroupInfo> instanceGroupInfoList) {
        if(relationship.getRelatedTo().equals("instance-group")){
            for(org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.RelatedToProperty relatedToProperty: relationship.getRelatedToPropertyList()){
                if(relatedToProperty.getPropertyKey().equals("instance-group.instance-group-name")){
                    instanceGroupInfoList.add(new InstanceGroupInfo(relatedToProperty.getPropertyValue()));
                }
            }
        }
    }

    @Override
    public  List<PortDetailsTranslator.PortDetails> getPortMirroringSourcePorts(String configurationId) {
        HttpResponse<AaiGetPortMirroringSourcePorts> portMirroringSourcePorts = aaiOverTLSClientInterface
            .getPortMirroringSourcePorts(configurationId);
        return portDetailsTranslator.extractPortDetails(portMirroringSourcePorts);
    }

    private void addPnfsToListViaDirectRelations(List<String> pnfs, HttpResponse<ServiceRelationships> serviceInstanceResp) {
        pnfs.addAll(getRelationshipDataByType(serviceInstanceResp.getBody().getRelationshipList(), "pnf", "pnf.pnf-name"));
    }

    private void addPnfsToListViaLogicalLinks(List<String> pnfs, HttpResponse<ServiceRelationships> serviceInstanceResp) {
        List<String> logicalLinks = getRelationshipDataByType(serviceInstanceResp.getBody().getRelationshipList(), "logical-link", "logical-link.link-name");
        for (String logicalLink : logicalLinks) {
            String link;
            try {
                link = URLEncoder.encode(logicalLink, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("Failed to encode logical link: " + logicalLink, e);
                continue;
            }
            HttpResponse<LogicalLinkResponse> logicalLinkResp = aaiOverTLSClientInterface.getLogicalLink(link);
            if (logicalLinkResp.getBody() != null) {
                List<String> linkPnfs = getRelationshipDataByType(logicalLinkResp.getBody().getRelationshipList(), "lag-interface", "pnf.pnf-name");
                if (!linkPnfs.isEmpty()) {
                    pnfs.addAll(linkPnfs);
                } else {
                    LOGGER.warn("no pnf found for logical link " + logicalLink);
                }
            } else {
                try {
                    String error = IOUtils.toString(logicalLinkResp.getRawBody(), "UTF-8");
                    LOGGER.error("get logical link " + logicalLink + " return error", error);
                } catch (IOException e) {
                    LOGGER.warn("get logical link " + logicalLink + " return empty body");
                }
            }
        }
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
