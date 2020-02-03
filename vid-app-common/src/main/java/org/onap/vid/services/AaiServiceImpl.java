/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;
import static org.onap.vid.aai.AaiClient.QUERY_FORMAT_RESOURCE;
import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiGetVnfResponse;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.AaiResponseTranslator;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.ServiceInstance;
import org.onap.vid.aai.ServiceInstancesSearchResults;
import org.onap.vid.aai.ServiceSubscription;
import org.onap.vid.aai.ServiceSubscriptions;
import org.onap.vid.aai.Services;
import org.onap.vid.aai.SubscriberFilteredResults;
import org.onap.vid.aai.model.AaiGetInstanceGroupsByCloudRegion;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.AaiGetRelatedInstanceGroupsByVnfId;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetServicesRequestModel.GetServicesAAIRespone;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.AaiRelationResponse;
import org.onap.vid.aai.model.GetServiceModelsByDistributionStatusResponse;
import org.onap.vid.aai.model.InstanceGroupInfo;
import org.onap.vid.aai.model.LogicalLinkResponse;
import org.onap.vid.aai.model.Model;
import org.onap.vid.aai.model.ModelVer;
import org.onap.vid.aai.model.OwningEntity;
import org.onap.vid.aai.model.OwningEntityResponse;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.aai.model.Project;
import org.onap.vid.aai.model.ProjectResponse;
import org.onap.vid.aai.model.Properties;
import org.onap.vid.aai.model.RelatedToProperty;
import org.onap.vid.aai.model.Relationship;
import org.onap.vid.aai.model.RelationshipData;
import org.onap.vid.aai.model.RelationshipList;
import org.onap.vid.aai.model.Result;
import org.onap.vid.aai.model.ServiceRelationships;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.ServiceInstanceSearchResult;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.model.aaiTree.Network;
import org.onap.vid.model.aaiTree.NodeType;
import org.onap.vid.model.aaiTree.RelatedVnf;
import org.onap.vid.model.aaiTree.VpnBinding;
import org.onap.vid.model.aaiTree.VpnBindingKt;
import org.onap.vid.roles.PermissionPropertiesSubscriberAndServiceType;
import org.onap.vid.roles.RoleValidator;
import org.onap.vid.utils.Intersection;
import org.onap.vid.utils.Logging;
import org.onap.vid.utils.Tree;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

public class AaiServiceImpl implements AaiService {
    private static final String SERVICE_INSTANCE_ID = "service-instance.service-instance-id";
    private static final String SERVICE_TYPE = "service-subscription.service-type";
    private static final String CUSTOMER_ID = "customer.global-customer-id";
    private static final String OWNING_ENTITY_ID = "owning-entity.owning-entity-id";
    private static final String SERVICE_INSTANCE_NAME = "service-instance.service-instance-name";
    private static final String TENANT_NODE_TYPE = "tenant";
    private static final String CLOUD_REGION_NODE_TYPE = "cloud-region";
    private int indexOfSubscriberName = 6;

    private AaiClientInterface aaiClient;
    private AaiResponseTranslator aaiResponseTranslator;
    private AAIServiceTree aaiServiceTree;
    private ExecutorService executorService;
    private final Logging logging;


    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(AaiServiceImpl.class);

    @Autowired
    public AaiServiceImpl(
        AaiClientInterface aaiClient,
        AaiResponseTranslator aaiResponseTranslator,
        AAIServiceTree aaiServiceTree,
        ExecutorService executorService, Logging logging)
    {
        this.aaiClient = aaiClient;
        this.aaiResponseTranslator = aaiResponseTranslator;
        this.aaiServiceTree = aaiServiceTree;
        this.executorService = executorService;
        this.logging = logging;
    }

    private List<Service> convertModelToService(Model model) {
        List<Service> services = new ArrayList<>();

        if(validateModel(model)){
            for (ModelVer modelVer: model.getModelVers().getModelVer()) {
                Service service = new Service.ServiceBuilder()
                        .setUuid(modelVer.getModelVersionId())
                        .setInvariantUUID(model.getModelInvariantId())
                        .setCategory(model.getModelType() != null ? model.getModelType() : "")
                        .setVersion(modelVer.getModelVersion())
                        .setName( modelVer.getModelName())
                        .setDistributionStatus(modelVer.getDistributionStatus())
                        .setToscaModelURL(null)
                        .setLifecycleState(null)
                        .setArtifacts(null)
                        .setResources(null)
                        .setOrchestrationType(modelVer.getOrchestrationType())
                        .build();



                services.add(service);
            }
        } else {
            return Collections.emptyList();
        }

        return services;
    }

    private boolean hasData(AaiResponse<?> aaiResponse) {
        return aaiResponse != null && aaiResponse.getT() != null;
    }

    private boolean validateModel(Model model){
        if (model == null) {
            return false;
        } else {
            return model.getModelVers() != null && model.getModelVers().getModelVer() != null && model.getModelVers().getModelVer().get(0).getModelVersionId() != null;
        }
    }

    List<ServiceInstanceSearchResult> getServicesByOwningEntityId(List<String> owningEntities, RoleValidator roleValidator) {
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
                    serviceInstanceSearchResult.setSubscriberId(relationshipData.getRelationshipValue());
                }
            }

            boolean isPermitted = roleValidator.isServicePermitted(serviceInstanceSearchResult);
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
        AaiResponse<SubscriberList> subscriberResponse = aaiClient.getAllSubscribers();

        return new SubscriberFilteredResults(roleValidator, subscriberResponse.getT(),
                subscriberResponse.getErrorMessage(),
                subscriberResponse.getHttpCode());
    }

    @Override
    public AaiResponse<OperationalEnvironmentList> getOperationalEnvironments(String operationalEnvironmentType, String operationalEnvironmentStatus) {
        return aaiClient.getOperationalEnvironments(operationalEnvironmentType, operationalEnvironmentStatus);
    }

    @Override
    public AaiResponse<SubscriberList> getFullSubscriberList() {
        return aaiClient.getAllSubscribers();
    }

    @Override
    public AaiResponse getSubscriberData(String subscriberId, RoleValidator roleValidator, boolean omitServiceInstances) {
        AaiResponse<Services> subscriberResponse = aaiClient.getSubscriberData(subscriberId, omitServiceInstances);
        for (ServiceSubscription serviceSubscription : subscriberResponse.getT().serviceSubscriptions.serviceSubscription) {
            serviceSubscription.isPermitted = roleValidator.isServicePermitted(
                new PermissionPropertiesSubscriberAndServiceType(serviceSubscription, subscriberResponse.getT().globalCustomerId));
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
        AaiResponse<Services> subscriberResponse = aaiClient.getSubscriberData(subscriberId, false);
        String subscriberName = subscriberResponse.getT().subscriberName;
        ServiceSubscriptions serviceSubscriptions = subscriberResponse.getT().serviceSubscriptions;

        return getSearchResultsForSubscriptions(serviceSubscriptions, subscriberId, instanceIdentifier, roleValidator, subscriberName);
    }


    private ArrayList<ServiceInstanceSearchResult> getSearchResultsForSubscriptions(
        ServiceSubscriptions serviceSubscriptions, String subscriberId, String instanceIdentifier,
        RoleValidator roleValidator, String subscriberName) {
        ArrayList<ServiceInstanceSearchResult> results = new ArrayList<>();

        if (serviceSubscriptions != null) {
            for (ServiceSubscription serviceSubscription : serviceSubscriptions.serviceSubscription) {
                serviceSubscription.isPermitted = roleValidator.isServicePermitted(new PermissionPropertiesSubscriberAndServiceType(serviceSubscription, subscriberId));
                results.addAll(getSearchResultsForSingleSubscription(
                    serviceSubscription, subscriberId, instanceIdentifier, subscriberName,
                    serviceSubscription.serviceType, roleValidator)
                );
            }
        }

        return results;
    }

    private ArrayList<ServiceInstanceSearchResult> getSearchResultsForSingleSubscription(
        ServiceSubscription serviceSubscription, String subscriberId, String instanceIdentifier, String subscriberName,
        String serviceType, RoleValidator roleValidator) {
        ArrayList<ServiceInstanceSearchResult> results = new ArrayList<>();

        if (serviceSubscription.serviceInstances != null) {
            for (ServiceInstance serviceInstance : serviceSubscription.serviceInstances.serviceInstance) {

                ServiceInstanceSearchResult serviceInstanceSearchResult =
                        new ServiceInstanceSearchResult(serviceInstance.serviceInstanceId, subscriberId, serviceType,
                            serviceInstance.serviceInstanceName, subscriberName, serviceInstance.modelInvariantId,
                            serviceInstance.modelVersionId, relatedOwningEntityId(serviceInstance), false);

                serviceInstanceSearchResult.setIsPermitted(roleValidator.isServicePermitted(serviceInstanceSearchResult));

                if ((instanceIdentifier == null) || (serviceInstanceMatchesIdentifier(instanceIdentifier, serviceInstance))){
                    results.add(serviceInstanceSearchResult);
                }
            }
        }

        return results;
    }

    protected String relatedOwningEntityId(ServiceInstance serviceInstance) {
        /*
        For reference, consider the service-instance structure below. Method will null-safely extract the
        `relationship-value` where `relationship-key` == `owning-entity.owning-entity-id`.

        {
          "service-instance-id": "5d521981-33be-4bb5-bb20-5616a9c52a5a",
          ...
          "relationship-list": {
            "relationship": [
              {
                "related-to": "owning-entity",
                "related-link": "/aai/v11/business/owning-entities/owning-entity/4d4ecf59-41f1-40d4-818d-885234680a42",
                "relationship-data": [
                  {
                    "relationship-key": "owning-entity.owning-entity-id",
                    "relationship-value": "4d4ecf59-41f1-40d4-818d-885234680a42"
                  }
                ]
              }
            ]
          }
        }
        */

        Stream<RelationshipData> allRelationships =
            Optional.ofNullable(serviceInstance.relationshipList)
                .map(it -> it.getRelationship())
                .map(it -> it.stream().flatMap(r -> emptyIfNull(r.getRelationDataList()).stream()))
                .orElse(Stream.empty());

        return allRelationships
            .filter(r -> StringUtils.equals(r.getRelationshipKey(), OWNING_ENTITY_ID))
            .map(it -> it.getRelationshipValue())
            .findAny().orElse(null);
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
            for (GetTenantsResponse tenant : tenants) {
                tenant.isPermitted = roleValidator.isTenantPermitted(globalCustomerId, serviceType, tenant.tenantName);
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
        AaiResponse<AaiGetVnfResponse> response = aaiClient.getVNFData(globalSubscriberId, serviceType);
        return hasData(response) ? response : new AaiResponse<>();
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
            return new AaiResponse<>(convertGetInstanceGroupsResponseToSimpleResponse(aaiResponse.getT()), aaiResponse.getErrorMessage(), aaiResponse.getHttpCode());
        }
        return aaiResponse;
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

        Tree<AAIServiceTree.AaiRelationship> pathsToSearch = new Tree<>(new AAIServiceTree.AaiRelationship(NodeType.SERVICE_INSTANCE));
        pathsToSearch.addPath(AAIServiceTree.toAaiRelationshipList(NodeType.GENERIC_VNF, NodeType.INSTANCE_GROUP));

        //get all vnfs related to service-instances from the model-invariant-id
        List<AAITreeNode> aaiTree = aaiServiceTree.buildAAITree(getURL, null, HttpMethod.GET, pathsToSearch, true);

        //filter by instance-group-role & instance-group-type properties (from getAdditionalProperties)
        //only vnfs has related instance-group with the same groupType & groupRole - are filtered out.
        List<AAITreeNode> filteredVnfs = filterByInstanceGroupRoleAndType(aaiTree, groupRole, groupType);

        //convert vnfs to expected result
        List<RelatedVnf> convertedVnfs = filteredVnfs.stream()
                .map(RelatedVnf::from)
                .collect(Collectors.toList());

        final Map<String, String> copyOfParentMDC = MDC.getCopyOfContextMap();

        try {
            return executorService.submit(() ->
                    convertedVnfs.parallelStream()
                            .map(logging.withMDC(copyOfParentMDC, this::enrichRelatedVnfWithCloudRegionAndTenant))
                            .collect(Collectors.toList())
            ).get();
        } catch (Exception e) {
            LOGGER.error(EELFLoggerDelegate.errorLogger, "Search group Members - Failed to enrich vnf with cloud region", e);
            return convertedVnfs;
        }
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
        if(StringUtils.equals(relationship.getRelatedTo(),"instance-group")){
            for(org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.RelatedToProperty relatedToProperty: relationship.getRelatedToPropertyList()){
                if(StringUtils.equals(relatedToProperty.getKey(),"instance-group.instance-group-name")){
                    instanceGroupInfoList.add(new InstanceGroupInfo(relatedToProperty.getValue()));
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
        String result;
        try {
            org.onap.vid.model.aaiTree.ServiceInstance tree = aaiServiceTree.getServiceInstanceTopology(globalCustomerId, serviceType, serviceInstanceId);
            result = JACKSON_OBJECT_MAPPER.writeValueAsString(tree);
        } catch (Exception e) {
            throw new GenericUncheckedException(e);
        }
        return result;
    }

    @Override
    public List<VpnBinding> getVpnListByVpnType(String vpnType) {
        String path = "network/vpn-bindings?vpn-type=" + vpnType;

        try {
            List<AAITreeNode> aaiTree = aaiServiceTree.buildAAITreeForUniqueResource(path, NodeType.VPN_BINDING);
            return aaiTree.stream().map(VpnBindingKt::from).collect(Collectors.toList());
        } catch (ExceptionWithRequestInfo exception) {
            if (Objects.equals(404, exception.getHttpCode())) {
                return emptyList();
            }
            throw exception;
        }

    }

    @Override
    public List<Network> getL3NetworksByCloudRegion(String cloudRegionId, String tenantId, String networkRole) {
        String payload = buildPayloadForL3NetworksByCloudRegion(cloudRegionId, tenantId, networkRole);

        try {
            List<AAITreeNode> aaiTree = aaiServiceTree.buildAAITreeForUniqueResourceFromCustomQuery(QUERY_FORMAT_RESOURCE, payload, HttpMethod.PUT, NodeType.NETWORK);
            return aaiTree.stream().map(Network::from).collect(Collectors.toList());
        } catch (ExceptionWithRequestInfo exception) {
            if (Objects.equals(404, exception.getHttpCode())) {
                return emptyList();
            }
            throw exception;
        }
    }

    @NotNull
    protected String buildPayloadForL3NetworksByCloudRegion(String cloudRegionId, String tenantId, String networkRole) {
        String networkRolePart = StringUtils.isEmpty(networkRole) ? "" : "&networkRole=" + networkRole;
        String cloudOwner = aaiClient.getCloudOwnerByCloudRegionId(cloudRegionId);
        return "{\"start\":\"/cloud-infrastructure/cloud-regions/cloud-region/" + cloudOwner + "/" + cloudRegionId + "\"," +
                "\"query\":\"query/l3-networks-by-cloud-region?tenantId=" + tenantId + networkRolePart + "\"}";
    }

    @Override
    public ModelVer getNewestModelVersionByInvariantId(String modelInvariantId){
        return aaiClient.getLatestVersionByInvariantId(modelInvariantId);
    }
}
