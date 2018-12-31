/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 Nokia. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.joshworks.restclient.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.*;
import org.onap.vid.aai.model.*;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.AaiGetRelatedInstanceGroupsByVnfId;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetServicesRequestModel.GetServicesAAIRespone;
import org.onap.vid.aai.model.Properties;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.ServiceInstanceSearchResult;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.model.aaiTree.RelatedVnf;
import org.onap.vid.roles.RoleValidator;
import org.onap.vid.utils.Intersection;
import org.onap.vid.utils.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Oren on 7/4/17.
 */
public class AaiServiceImpl implements AaiService {
    private static final String SERVICE_INSTANCE_ID = "service-instance.service-instance-id";
    private static final String SERVICE_TYPE = "service-subscription.service-type";
    private static final String CUSTOMER_ID = "customer.global-customer-id";
    private static final String SERVICE_INSTANCE_NAME = "service-instance.service-instance-name";
    private static final String TENANT_NODE_TYPE = "tenant";
    private static final String CLOUD_REGION_NODE_TYPE = "cloud-region";
    private int indexOfSubscriberName = 6;

    @Autowired
    private AaiClientInterface aaiClient;

    @Autowired
    @Qualifier("aaiClientForCodehausMapping")
    private AaiOverTLSClientInterface aaiOverTLSClient;

    @Autowired
    private AaiResponseTranslator aaiResponseTranslator;

    @Autowired
    private AAITreeNodeBuilder aaiTreeNode;

    @Autowired
    private AAIServiceTree aaiServiceTree;

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
    public SubscriberFilteredResults getFullSubscriberList(RoleValidator roleValidator) {
        HttpResponse<SubscriberList> allSubscribers = aaiOverTLSClient.getAllSubscribers();
        return new SubscriberFilteredResults(
            roleValidator,
            allSubscribers.getBody(),
            allSubscribers.getStatusText(),
            allSubscribers.getStatus()
        );
    }

    @Override
    public AaiResponse<OperationalEnvironmentList> getOperationalEnvironments(String operationalEnvironmentType, String operationalEnvironmentStatus) {
        return aaiClient.getOperationalEnvironments(operationalEnvironmentType, operationalEnvironmentStatus);
    }

    @Override
    public HttpResponse<SubscriberList> getFullSubscriberList() {
        return aaiOverTLSClient.getAllSubscribers();
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
        if (!resultList.isEmpty()) {
            serviceInstancesSearchResults.serviceInstances = Intersection.of(resultList);
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

                if ((instanceIdentifier == null) || (serviceInstanceMatchesIdentifier(instanceIdentifier, serviceInstance))){
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
            LOGGER.error(EELFLoggerDelegate.errorLogger, "Failed to getVersionByInvariantId from A&AI", e);
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
        if (subscriberResponse.getT() != null) {
            for (org.onap.vid.aai.model.AaiGetServicesRequestModel.Service service : subscriberResponse.getT().service) {
                service.isPermitted = true;
            }
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
    public AaiResponse<AaiGetVnfResponse> getVNFData(String globalSubscriberId, String serviceType) {
        AaiResponse response = aaiClient.getVNFData(globalSubscriberId, serviceType);
        return filterChangeManagementVNFCandidatesResponse(response);
    }

    private AaiResponse<AaiGetVnfResponse> filterChangeManagementVNFCandidatesResponse(AaiResponse<AaiGetVnfResponse> response) {


        if (response != null && response.getT() != null) {
            response.getT().results =
                    response.getT().results.stream()
                            .filter(result -> (
                                    result.nodeType.equalsIgnoreCase("generic-vnf") ||
                                            result.nodeType.equalsIgnoreCase("service-instance")))
                            .collect(Collectors.toList());

            return response;
        }

        return new AaiResponse();
    }

    @Override
    public AaiResponse getAaiZones() {
        return aaiClient.getAllAicZones();
    }

    @Override
    public AaiResponse getAicZoneForPnf(String globalCustomerId, String serviceType, String serviceId) {
        String aicZone = "";

        AaiResponse<ServiceRelationships> serviceInstanceResp = aaiClient.getServiceInstance(globalCustomerId, serviceType, serviceId);
        if (serviceInstanceResp.getT() != null) {
            List<String> aicZoneList = getRelationshipDataByType(serviceInstanceResp.getT().getRelationshipList(), "zone", "zone.zone-id");
            if (!aicZoneList.isEmpty()) {
                aicZone = aicZoneList.get(0);
            } else {
                LOGGER.warn("aic zone not found for service instance " + serviceId);
            }
        } else {
            if (serviceInstanceResp.getErrorMessage() != null) {
                LOGGER.error("get service instance {} return error {}", serviceId, serviceInstanceResp.getErrorMessage());
                return new AaiResponse(aicZone , serviceInstanceResp.getErrorMessage() ,serviceInstanceResp.getHttpCode());
            } else {
                LOGGER.warn("get service instance {} return empty body", serviceId);
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
    public AaiResponse getNetworkCollectionDetails(String serviceInstanceId){
        return aaiClient.getNetworkCollectionDetails(serviceInstanceId);
    }

    @Override
    public AaiResponse<AaiGetInstanceGroupsByCloudRegion> getInstanceGroupsByCloudRegion(String cloudOwner, String cloudRegionId, String networkFunction){
        return aaiClient.getInstanceGroupsByCloudRegion(cloudOwner, cloudRegionId, networkFunction);
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
                    services.addAll(service);
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

            addPnfsToListViaLogicalLinks(pnfs, serviceInstanceResp);
            addPnfsToListViaDirectRelations(pnfs, serviceInstanceResp);

            if (pnfs.isEmpty()) {
                LOGGER.warn("no pnf direct relation found for service id:" + serviceInstanceId+
                        " name: "+serviceInstanceResp.getT().getServiceInstanceName());
            }
        } else {
            if (serviceInstanceResp.getErrorMessage() != null) {
                LOGGER.error("get service instance {} return error {}", serviceInstanceId, serviceInstanceResp.getErrorMessage());
            } else {
                LOGGER.warn("get service instance {} return empty body", serviceInstanceId);
            }
        }

        return pnfs.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public AaiResponseTranslator.PortMirroringConfigData getPortMirroringConfigData(String configurationId) {
        AaiResponse<JsonNode> aaiResponse = aaiClient.getCloudRegionAndSourceByPortMirroringConfigurationId(configurationId);
        return aaiResponseTranslator.extractPortMirroringConfigData(aaiResponse);
    }

    @Override
    public AaiResponse getInstanceGroupsByVnfInstanceId(String vnfInstanceId){
        AaiResponse<AaiGetRelatedInstanceGroupsByVnfId> aaiResponse = aaiClient.getInstanceGroupsByVnfInstanceId(vnfInstanceId);
        if(aaiResponse.getHttpCode() == HttpStatus.SC_OK){
            return new AaiResponse(convertGetInstanceGroupsResponseToSimpleResponse(aaiResponse.getT()), aaiResponse.getErrorMessage(), aaiResponse.getHttpCode());
        }
        return aaiClient.getInstanceGroupsByVnfInstanceId(vnfInstanceId);
    }

    @Override
    public GetTenantsResponse getHomingDataByVfModule(String vnfInstanceId, String vfModuleId) {
        return aaiClient.getHomingDataByVfModule(vnfInstanceId,vfModuleId);
    }

    @Override
    public List<RelatedVnf> searchGroupMembers(String globalCustomerId, String serviceType, String invariantId, String groupType, String groupRole) {
        String getURL = "business/customers/customer/" +
                    globalCustomerId + "/service-subscriptions/service-subscription/" +
                    serviceType + "/service-instances?model-invariant-id=" + invariantId;

        Tree<AAIServiceTree.AaiRelationship> pathsToSearch = new Tree<>(new AAIServiceTree.AaiRelationship(AAITreeNodeBuilder.SERVICE_INSTANCE));
        pathsToSearch.addPath(AAITreeNodeBuilder.toAaiRelationshipList(AAITreeNodeBuilder.GENERIC_VNF, AAITreeNodeBuilder.INSTANCE_GROUP));

        //get all vnfs related to service-instances from the model-invariant-id
        List<AAITreeNode> aaiTree = aaiServiceTree.buildAAITree(getURL, pathsToSearch);

        //filter by instance-group-role & instance-group-type properties (from getAdditionalProperties)
        //only vnfs has related instance-group with the same groupType & groupRole - are filtered out.
        List<AAITreeNode> filteredVnfs = filterByInstanceGroupRoleAndType(aaiTree, groupRole, groupType);

        //convert vnfs to expected result
        return filteredVnfs.stream()
                .map(RelatedVnf::from)
                .map(this::enrichRelatedVnfWithCloudRegionAndTenant)
                .collect(Collectors.toList());
    }

    private List<AAITreeNode> filterByInstanceGroupRoleAndType(List<AAITreeNode> aaiTree, String groupRole, String groupType) {

        return aaiTree.stream()
                .map(AAITreeNode::getChildren)
                .flatMap(Collection::stream)
                .filter(vnf -> isInstanceGroupsNotMatchRoleAndType(vnf.getChildren(), groupRole, groupType))
                .collect(Collectors.toList());
    }

    public boolean isInstanceGroupsNotMatchRoleAndType(List<AAITreeNode> instanceGroups, String groupRole, String groupType) {
        return instanceGroups.stream()
                .map(AAITreeNode::getAdditionalProperties)
                .allMatch(props ->
                        (!(groupRole.equals(props.get("instance-group-role")) &&
                                groupType.equals(props.get("instance-group-type"))))
                );
    }

    public RelatedVnf enrichRelatedVnfWithCloudRegionAndTenant(RelatedVnf vnf) {
        Map<String, Properties> cloudRegionAndTenant = aaiClient.getCloudRegionAndTenantByVnfId(vnf.getInstanceId());

        if (cloudRegionAndTenant.containsKey(TENANT_NODE_TYPE)) {
            vnf.setTenantId(cloudRegionAndTenant.get(TENANT_NODE_TYPE).getTenantId());
            vnf.setTenantName(cloudRegionAndTenant.get(TENANT_NODE_TYPE).getTenantName());
        }

        if (cloudRegionAndTenant.containsKey(CLOUD_REGION_NODE_TYPE)) {
            vnf.setLcpCloudRegionId(cloudRegionAndTenant.get(CLOUD_REGION_NODE_TYPE).getCloudRegionId());
        }

        return vnf;
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
    public  List<PortDetailsTranslator.PortDetails> getPortMirroringSourcePorts(String configurationId){
        return aaiClient.getPortMirroringSourcePorts(configurationId);
    }

    private void addPnfsToListViaDirectRelations(List<String> pnfs, AaiResponse<ServiceRelationships> serviceInstanceResp) {
        pnfs.addAll(getRelationshipDataByType(serviceInstanceResp.getT().getRelationshipList(), "pnf", "pnf.pnf-name"));
    }

    private void addPnfsToListViaLogicalLinks(List<String> pnfs, AaiResponse<ServiceRelationships> serviceInstanceResp) {
        List<String> logicalLinks = getRelationshipDataByType(serviceInstanceResp.getT().getRelationshipList(), "logical-link", "logical-link.link-name");
        for (String logicalLink : logicalLinks) {
            String link;
            try {
                link = URLEncoder.encode(logicalLink, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("Failed to encode logical link: " + logicalLink, e);
                continue;
            }

            AaiResponse<LogicalLinkResponse> logicalLinkResp = aaiClient.getLogicalLink(link);
            if (logicalLinkResp.getT() != null) {
                //lag-interface is the key for pnf - approved by Bracha
                List<String> linkPnfs = getRelationshipDataByType(logicalLinkResp.getT().getRelationshipList(), "lag-interface", "pnf.pnf-name");
                if (!linkPnfs.isEmpty()) {
                    pnfs.addAll(linkPnfs);
                } else {
                    LOGGER.warn("no pnf found for logical link " + logicalLink);
                }
            } else {
                if (logicalLinkResp.getErrorMessage() != null) {
                    LOGGER.error("get logical link " + logicalLink + " return error", logicalLinkResp.getErrorMessage());
                } else {
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

    public String getAAIServiceTree(String globalCustomerId, String serviceType, String serviceInstanceId) {
        ObjectMapper om = new ObjectMapper();
        String result;
        try {
            org.onap.vid.model.aaiTree.ServiceInstance tree = aaiServiceTree.getServiceInstanceTopology(globalCustomerId, serviceType, serviceInstanceId);
            result = om.writeValueAsString(tree);
        } catch (Exception e) {
            throw new GenericUncheckedException(e);
        }
        return result;
    }
}
