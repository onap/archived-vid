/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia. All rights reserved.
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

package org.onap.vid.aai;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import java.util.List;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.model.AaiGetAicZone.AicZones;
import org.onap.vid.aai.model.AaiGetInstanceGroupsByCloudRegion;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.AaiGetNetworkCollectionDetails;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.AaiGetRelatedInstanceGroupsByVnfId;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfResponse;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetPortMirroringSourcePorts;
import org.onap.vid.aai.model.AaiGetServicesRequestModel.GetServicesAAIRespone;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.AaiNodeQueryResponse;
import org.onap.vid.aai.model.GetServiceModelsByDistributionStatusResponse;
import org.onap.vid.aai.model.LogicalLinkResponse;
import org.onap.vid.aai.model.OwningEntityResponse;
import org.onap.vid.aai.model.ProjectResponse;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.aai.model.ServiceRelationships;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.model.probes.ExternalComponentStatus;

public interface AaiOverTLSClientInterface {

    class URIS {

        static final String NODE_TYPE_BY_NAME = "search/nodes-query?search-node-type=%s&filter=%s:EQUALS:%s";
        static final String SUBSCRIBERS = "business/customers?subscriber-type=INFRA&depth=%s";
        static final String SUBSCRIBER_DATA = "business/customers/customer/%s?depth=%s";
        static final String SERVICES = "service-design-and-creation/services";
        static final String SERVICES_BY_OWNING_ENTITY_ID = "business/owning-entities?owning-entity-id=%s";
        static final String TENANTS = "business/customers/customer/%s/service-subscriptions/service-subscription/%s";
        static final String OPERATIONAL_ENVIRONMENTS = "cloud-infrastructure/operational-environments";
        static final String AIC_ZONES = "network/zones";
        static final String QUERY_FORMAT_SIMPLE = "query?format=simple";
        static final String QUERY_FORMAT_RESOURCE = "query?format=resource";
        static final String VERSION_BY_INVARIANT_ID = "service-design-and-creation/models?depth=2";
        static final String SERVICES_BY_PROJECT_NAMES = "business/projects?";
        static final String SPECIFIC_PNF = "network/pnfs/pnf/%s";
        static final String SERVICE_INSTANCE = "business/customers/customer/%s/service-subscriptions/service-subscription/%s/service-instances/service-instance/%s";
        static final String LOGICAL_LINK = "network/logical-links/logical-link/%s";
        static final String CLOUD_REGION_AND_SOURCE = "query?format=simple&nodesOnly=true";
        static final String INSTANCE_GROUPS_BY_VNF_ID = "network/generic-vnfs/generic-vnf/%s?depth=0";
    }

    class HEADERS {

        static final String TRANSACTION_ID_HEADER = "X-TransactionId";
        static final String FROM_APP_ID_HEADER = "X-FromAppId";
        static final String CONTENT_TYPE = "Content-Type";
        static final String REQUEST_ID = SystemProperties.ECOMP_REQUEST_ID;
        static final String ACCEPT = "Accept";
    }

    void setUseClientCert(boolean useClientCert);

    HttpResponse<AaiNodeQueryResponse> searchNodeTypeByName(String name, ResourceType type);

    HttpResponse<SubscriberList> getAllSubscribers();

    HttpResponse<Services> getSubscriberData(String subscriberId);

    HttpResponse<GetServicesAAIRespone> getServices();

    HttpResponse<OwningEntityResponse> getServicesByOwningEntityId(List<String> owningEntityIds);

    HttpResponse<GetTenantsResponse[]> getTenants(String globalCustomerId, String serviceType);

    HttpResponse<OperationalEnvironmentList> getOperationalEnvironments(String operationalEnvironmentType,
        String operationalEnvironmentStatus);

    HttpResponse<AicZones> getAllAicZones();

    HttpResponse<String> getAicZoneForPnf(String globalCustomerId, String serviceType, String serviceId);

    HttpResponse<AaiGetVnfResponse> getVNFData();

    HttpResponse<AaiGetNetworkCollectionDetails> getNetworkCollectionDetails(String serviceInstanceId);

    HttpResponse<AaiGetInstanceGroupsByCloudRegion> getInstanceGroupsByCloudRegion(String cloudOwner,
        String cloudRegionId, String networkFunction);

    HttpResponse<String> getVNFData(String globalSubscriberId, String serviceType);

    HttpResponse<AaiGetVnfResponse> getVNFData(String globalSubscriberId, String serviceType, String serviceInstanceId);

    HttpResponse<AaiGetVnfResponse> getNodeTemplateInstances(String globalCustomerId, String serviceType,
        String modelVersionId, String modelInvariantId, String cloudRegion);

    HttpResponse<ResponseWithRequestInfo> getVersionByInvariantId(List<String> modelInvariantId);

    HttpResponse<ProjectResponse> getServicesByProjectNames(List<String> projectNames);

    HttpResponse<GetServiceModelsByDistributionStatusResponse> getServiceModelsByDistributionStatus();

    HttpResponse<AaiGetPnfResponse> getPNFData(String globalCustomerId, String serviceType, String modelVersionId,
        String modelInvariantId, String cloudRegion, String equipVendor, String equipModel);

    HttpResponse<Pnf> getSpecificPnf(String pnfId);

    HttpResponse<ServiceRelationships> getServiceInstance(String globalCustomerId, String serviceType,
        String serviceInstanceId);

    HttpResponse<LogicalLinkResponse> getLogicalLink(String link);

    HttpResponse<JsonNode> getCloudRegionAndSourceByPortMirroringConfigurationId(String configurationId);

    HttpResponse<AaiGetPortMirroringSourcePorts> getPortMirroringSourcePorts(String configurationID);

    HttpResponse<AaiGetRelatedInstanceGroupsByVnfId> getInstanceGroupsByVnfInstanceId(String vnfInstanceId);

    ExternalComponentStatus probeAaiGetAllSubscribers();

}