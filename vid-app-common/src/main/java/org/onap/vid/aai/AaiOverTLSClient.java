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

import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.ACCEPT;
import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.CONTENT_TYPE;
import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.FROM_APP_ID_HEADER;
import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.REQUEST_ID;
import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.TRANSACTION_ID_HEADER;
import static org.onap.vid.aai.AaiOverTLSClientInterface.URIS.AIC_ZONES;
import static org.onap.vid.aai.AaiOverTLSClientInterface.URIS.CLOUD_REGION_AND_SOURCE;
import static org.onap.vid.aai.AaiOverTLSClientInterface.URIS.LOGICAL_LINK;
import static org.onap.vid.aai.AaiOverTLSClientInterface.URIS.OPERATIONAL_ENVIRONMENTS;
import static org.onap.vid.aai.AaiOverTLSClientInterface.URIS.INSTANCE_GROUPS_BY_VNF_ID;
import static org.onap.vid.aai.AaiOverTLSClientInterface.URIS.QUERY_FORMAT_RESOURCE;
import static org.onap.vid.aai.AaiOverTLSClientInterface.URIS.QUERY_FORMAT_SIMPLE;
import static org.onap.vid.aai.AaiOverTLSClientInterface.URIS.SERVICES;
import static org.onap.vid.aai.AaiOverTLSClientInterface.URIS.SERVICES_BY_PROJECT_NAMES;
import static org.onap.vid.aai.AaiOverTLSClientInterface.URIS.SERVICE_INSTANCE;
import static org.onap.vid.aai.AaiOverTLSClientInterface.URIS.SPECIFIC_PNF;
import static org.onap.vid.aai.AaiOverTLSClientInterface.URIS.VERSION_BY_INVARIANT_ID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import io.vavr.collection.HashMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import lombok.val;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.eclipse.jetty.util.security.Password;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.model.AaiGetAicZone.AicZones;
import org.onap.vid.aai.model.AaiGetInstanceGroupsByCloudRegion;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.AaiGetNetworkCollectionDetails;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.AaiGetNetworkCollectionDetailsHelper;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.AaiGetRelatedInstanceGroupsByVnfId;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.InstanceGroup;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Network;
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
import org.onap.vid.aai.util.AAIProperties;
import org.onap.vid.client.SyncRestClientInterface;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.model.probes.HttpRequestMetadata;
import org.onap.vid.utils.Logging;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriUtils;

public class AaiOverTLSClient implements AaiOverTLSClientInterface {

    private SyncRestClientInterface syncRestClient;
    private boolean useClientCert;
    private static String CALLER_APP_ID = "VidAaiController";
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AaiOverTLSClient.class);

    @Inject
    public AaiOverTLSClient(SyncRestClientInterface syncRestClient) {
        this.syncRestClient = syncRestClient;
    }

    @Override
    public void setUseClientCert(boolean useClientCert) {
        this.useClientCert = useClientCert;
    }

    @Override
    public HttpResponse<AaiNodeQueryResponse> searchNodeTypeByName(String name, ResourceType type) {
        val uri = getServer() + String.format(URIS.NODE_TYPE_BY_NAME, type.getAaiFormat(), type.getNameFilter(), name);
        return syncRestClient.get(uri, getRequestHeaders(), Collections.emptyMap(), AaiNodeQueryResponse.class);
    }

    @Override
    public HttpResponse<SubscriberList> getAllSubscribers() {
        val uri = getServer() + String.format(URIS.SUBSCRIBERS, 0);
        return syncRestClient.get(uri, getRequestHeaders(), Collections.emptyMap(), SubscriberList.class);
    }

    @Override
    public HttpResponse<Services> getSubscriberData(String subscriberId) {
        val uri = getServer() + String.format(URIS.SUBSCRIBER_DATA, subscriberId, 2);
        return syncRestClient.get(uri, getRequestHeaders(), Collections.emptyMap(), Services.class);
    }

    @Override
    public HttpResponse<GetServicesAAIRespone> getServices() {
        val uri = getServer() + SERVICES;
        return syncRestClient.get(uri, getRequestHeaders(), Collections.emptyMap(), GetServicesAAIRespone.class);
    }

    @Override
    public HttpResponse<OwningEntityResponse> getServicesByOwningEntityId(List<String> owningEntityIds) {
        val uri = getServer() + String.format(URIS.SERVICES_BY_OWNING_ENTITY_ID, String.join(", ", owningEntityIds));
        return syncRestClient.get(uri, getRequestHeaders(), Collections.emptyMap(), OwningEntityResponse.class);
    }

    @Override
    public HttpResponse<GetTenantsResponse[]> getTenants(String globalCustomerId, String serviceType) {
        if ((globalCustomerId == null || globalCustomerId.isEmpty()) || ((serviceType == null) || (serviceType
            .isEmpty()))) {
            BasicHttpResponse failureResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), HttpStatus.SC_INTERNAL_SERVER_ERROR,
                    "{\"statusText\":\" Failed to retrieve LCP Region & Tenants from A&AI, Subscriber ID or Service Type is missing.\"}"));
            return new HttpResponse<>(failureResponse, GetTenantsResponse[].class);
        }
        val uri = getServer() + String.format(URIS.TENANTS, globalCustomerId, serviceType);
        return syncRestClient.get(uri, getRequestHeaders(), Collections.emptyMap(), GetTenantsResponse[].class);
    }

    @Override
    public HttpResponse<OperationalEnvironmentList> getOperationalEnvironments(String operationalEnvironmentType,
        String operationalEnvironmentStatus) {
        val uri = getServer() + OPERATIONAL_ENVIRONMENTS;
        val routeParams = HashMap.<String, String>empty();
        if (operationalEnvironmentType != null) {
            routeParams.put("operational-environment-type", operationalEnvironmentType);
        }
        if (operationalEnvironmentStatus != null) {
            routeParams.put("operational-environment-status", operationalEnvironmentStatus);
        }
        return syncRestClient.get(uri, getRequestHeaders(), routeParams.toJavaMap(), OperationalEnvironmentList.class);
    }

    @Override
    public HttpResponse<AicZones> getAllAicZones() {
        val uri = getServer() + AIC_ZONES;
        return syncRestClient.get(uri, getRequestHeaders(), Collections.emptyMap(), AicZones.class);
    }

    private String getServer() {
        return SystemProperties.getProperty(AAIProperties.AAI_SERVER_URL);
    }

    @Override
    public HttpResponse<String> getAicZoneForPnf(String globalCustomerId, String serviceType, String serviceId) {
        val aicZoneUri = getServer() + String.format(SERVICE_INSTANCE, globalCustomerId, serviceType, serviceId);
        HttpResponse<ServiceRelationships> serviceRelationshipsHttpResponse = syncRestClient
            .get(aicZoneUri, getRequestHeaders(), Collections.emptyMap(), ServiceRelationships.class);
        val relationships = serviceRelationshipsHttpResponse.getBody().getRelationshipList().getRelationship();
        val aicZone = relationships.get(0).getRelationDataList().get(0).getRelationshipValue();
        return new HttpResponse<>(repackHttpResponse(aicZone, HttpStatus.SC_OK, "OK"), String.class);
    }


    @Override
    public HttpResponse<AaiGetVnfResponse> getVNFData() {
        val uri = getServer() + QUERY_FORMAT_SIMPLE;
        val payload = "{\"start\": [\"/business/customers/customer/e433710f-9217-458d-a79d-1c7aff376d89/service-subscriptions/service-subscription/VIRTUAL%20USP/service-instances/service-instance/3f93c7cb-2fd0-4557-9514-e189b7b04f9d\"],	\"query\": \"query/vnf-topology-fromServiceInstance\"}";
        return syncRestClient.put(uri, getRequestHeaders(), payload, AaiGetVnfResponse.class);
    }

    @Override
    public HttpResponse<AaiGetNetworkCollectionDetails> getNetworkCollectionDetails(String serviceInstanceId) {
        val uri = getServer() + QUERY_FORMAT_RESOURCE;
        val payload = "{\"start\": [\"nodes/service-instances/service-instance/" + serviceInstanceId
            + "\"],\"query\": \"query/network-collection-ByServiceInstance\"}\n";
        HttpResponse<AaiGetNetworkCollectionDetailsHelper> networkCollectionDetailsHelperHttpResponse = syncRestClient
            .put(uri, getRequestHeaders(), payload, AaiGetNetworkCollectionDetailsHelper.class);
        return getNetworkCollectionDetailsResponse(networkCollectionDetailsHelperHttpResponse);
    }

    @Override
    public HttpResponse<AaiGetInstanceGroupsByCloudRegion> getInstanceGroupsByCloudRegion(String cloudOwner,
        String cloudRegionId, String networkFunction) {
        val uri = getServer() + QUERY_FORMAT_RESOURCE;
        val payload =
            "{\"start\": [\"cloud-infrastructure/cloud-regions/cloud-region/" + cloudOwner + "/" + cloudRegionId
                + "\"]," +
                "\"query\": \"query/instance-group-byCloudRegion?type=L3-NETWORK&role=SUB-INTERFACE&function="
                + networkFunction + "\"}\n";
        return syncRestClient.put(uri, getRequestHeaders(), payload, AaiGetInstanceGroupsByCloudRegion.class);
    }

    @Override
    public HttpResponse<String> getVNFData(String globalSubscriberId, String serviceType) {
        try {
            val payload = "{\"start\": [\"business/customers/customer/" + globalSubscriberId
                + "/service-subscriptions/service-subscription/" + UriUtils
                .encodePathSegment(serviceType, "UTF-8") + "/service-instances\"],"
                + "\"query\": \"query/vnf-topology-fromServiceInstance\"}";
            return syncRestClient.put(QUERY_FORMAT_SIMPLE, getRequestHeaders(), payload, String.class);
        } catch (UnsupportedEncodingException e) {
            throw new GenericUncheckedException("URI encoding failed unexpectedly", e);
        }
    }

    @Override
    public HttpResponse<AaiGetVnfResponse> getVNFData(String globalSubscriberId, String serviceType,
        String serviceInstanceId) {
        val uri = getServer() + QUERY_FORMAT_SIMPLE;
        try {
            val payload = "{\"start\": [\"/business/customers/customer/" + globalSubscriberId
                + "/service-subscriptions/service-subscription/" +
                UriUtils.encodePathSegment(serviceType, "UTF-8") + "/service-instances/service-instance/"
                + serviceInstanceId
                + "\"],	\"query\": \"query/vnf-topology-fromServiceInstance\"}";
            return syncRestClient.put(uri, getRequestHeaders(), payload, AaiGetVnfResponse.class);
        } catch (UnsupportedEncodingException e) {
            throw new GenericUncheckedException("URI encoding failed unexpectedly", e);
        }
    }

    @Override
    public HttpResponse<AaiGetVnfResponse> getNodeTemplateInstances(String globalCustomerId, String serviceType,
        String modelVersionId, String modelInvariantId, String cloudRegion) {
        try {
            val uri = getServer() + QUERY_FORMAT_SIMPLE;
            val siQuery =
                "/business/customers/customer/" + globalCustomerId + "/service-subscriptions/service-subscription/"
                    + UriUtils.encodePathSegment(serviceType, "UTF-8") + "/service-instances?model-version-id="
                    + modelVersionId
                    + "&model-invariant-id=" + modelInvariantId;
            val vnfQuery =
                "query/queryvnfFromModelbyRegion?cloudRegionId=" + UriUtils.encodePathSegment(cloudRegion, "UTF-8");
            val payload = "{\"start\":\"" + siQuery + "\",\"query\":\"" + vnfQuery + "\"}";
            return syncRestClient.put(uri, getRequestHeaders(), payload, AaiGetVnfResponse.class);
        } catch (UnsupportedEncodingException e) {
            throw new GenericUncheckedException("URI encoding failed unexpectedly", e);
        }
    }

    @Override
    public HttpResponse<ResponseWithRequestInfo> getVersionByInvariantId(List<String> modelInvariantId) {
        val uri = getServer() + VERSION_BY_INVARIANT_ID;
        val sb = new StringBuilder();
        for (val id : modelInvariantId) {
            sb.append("&model-invariant-id=");
            sb.append(id);
        }
        return syncRestClient
            .get(uri + sb.toString(), getRequestHeaders(), Collections.emptyMap(), ResponseWithRequestInfo.class);
    }

    @Override
    public HttpResponse<ProjectResponse> getServicesByProjectNames(List<String> projectNames) {
        val uri = getServer() + SERVICES_BY_PROJECT_NAMES;
        val sb = new StringBuilder();
        projectNames.forEach(name -> sb.append("&project-name=").append(name));
        return syncRestClient
            .get(uri + sb.toString(), getRequestHeaders(), Collections.emptyMap(), ProjectResponse.class);
    }

    @Override
    public HttpResponse<GetServiceModelsByDistributionStatusResponse> getServiceModelsByDistributionStatus() {
        val uri = getServer() + QUERY_FORMAT_RESOURCE;
        val payload = "{\"start\" : \"service-design-and-creation/models/\", \"query\" : \"query/serviceModels-byDistributionStatus?distributionStatus=DISTRIBUTION_COMPLETE_OK\"}";
        return syncRestClient
            .put(uri, getRequestHeaders(), payload, GetServiceModelsByDistributionStatusResponse.class);
    }

    @Override
    public HttpResponse<AaiGetPnfResponse> getPNFData(String globalCustomerId, String serviceType,
        String modelVersionId, String modelInvariantId, String cloudRegion, String equipVendor, String equipModel) {
        try {
            val uri = getServer() + QUERY_FORMAT_SIMPLE;
            val siQuery =
                "/business/customers/customer/" + globalCustomerId + "/service-subscriptions/service-subscription/"
                    + UriUtils.encodePathSegment(serviceType, "UTF-8") + "/service-instances?model-version-id="
                    + modelVersionId + "&model-invariant-id=" + modelInvariantId;
            val pnfQuery =
                "query/pnf-fromModel-byRegion?cloudRegionId=" + UriUtils.encodePathSegment(cloudRegion, "UTF-8")
                    + "&equipVendor="
                    + UriUtils.encodePathSegment(equipVendor, "UTF-8") + "&equipModel=" + UriUtils
                    .encodePathSegment(equipModel, "UTF-8");
            val payload = "{\"start\":\"" + siQuery + "\",\"query\":\"" + pnfQuery + "\"}";
            return syncRestClient.put(uri, getRequestHeaders(), payload, AaiGetPnfResponse.class);
        } catch (UnsupportedEncodingException e) {
            throw new GenericUncheckedException("URI encoding failed unexpectedly", e);
        }
    }

    @Override
    public HttpResponse<Pnf> getSpecificPnf(String pnfId) {
        val uri = getServer() + String.format(SPECIFIC_PNF, pnfId);
        return syncRestClient.get(uri, getRequestHeaders(), Collections.emptyMap(), Pnf.class);
    }

    @Override
    public HttpResponse<ServiceRelationships> getServiceInstance(String globalCustomerId, String serviceType,
        String serviceInstanceId) {
        val uri = getServer() + String.format(SERVICE_INSTANCE, globalCustomerId, serviceType, serviceInstanceId);
        return syncRestClient.get(uri, getRequestHeaders(), Collections.emptyMap(), ServiceRelationships.class);
    }

    @Override
    public HttpResponse<LogicalLinkResponse> getLogicalLink(String link) {
        val uri = getServer() + String.format(LOGICAL_LINK, link);
        return syncRestClient.get(uri, getRequestHeaders(), Collections.emptyMap(), LogicalLinkResponse.class);
    }

    @Override
    public HttpResponse<JsonNode> getCloudRegionAndSourceByPortMirroringConfigurationId(String configurationId) {
        val uri = getServer() + CLOUD_REGION_AND_SOURCE;
        val start = "[\"network/configurations/configuration/" + configurationId + "\"]";
        val query = "\"query/cloud-region-and-source-FromConfiguration\"";
        val payload = "{\"start\":" + start + ",\"query\":" + query + "}";
        return syncRestClient.put(uri, getRequestHeaders(), payload);
    }

    @Override
    public HttpResponse<AaiGetPortMirroringSourcePorts> getPortMirroringSourcePorts(String configurationID) {
        val payload = "{\"start\":\"/network/configurations/configuration/" + configurationID
            + "\",\"query\":\"query/pserver-fromConfiguration\"}";
        return syncRestClient
            .put(QUERY_FORMAT_SIMPLE, getRequestHeaders(), payload, AaiGetPortMirroringSourcePorts.class);
    }

    @Override
    public HttpResponse<AaiGetRelatedInstanceGroupsByVnfId> getInstanceGroupsByVnfInstanceId(String vnfInstanceId) {
        val uri = getServer() + String.format(INSTANCE_GROUPS_BY_VNF_ID, vnfInstanceId);
        return syncRestClient
            .get(uri, getRequestHeaders(), Collections.emptyMap(), AaiGetRelatedInstanceGroupsByVnfId.class);
    }

    @Override
    public ExternalComponentStatus probeAaiGetAllSubscribers() {
        val startTime = System.currentTimeMillis();
        val uri = getServer() + String.format(URIS.SUBSCRIBERS, 0);
        try {
            val allSubscribers = getAllSubscribers();
            val duration = System.currentTimeMillis() - startTime;
            val isAvailable =
                allSubscribers.getBody() != null && allSubscribers.getBody().customer != null && !allSubscribers
                    .getBody().customer.isEmpty();
            HttpRequestMetadata metadata = new HttpRequestMetadata(
                HttpMethod.GET,
                allSubscribers.getStatus(),
                uri,
                new ObjectMapper().writeValueAsString(allSubscribers.getBody()),
                isAvailable ? "OK" : "No subscriber received",
                duration
            );
            return new ExternalComponentStatus(ExternalComponentStatus.Component.AAI, isAvailable, metadata);
        } catch (JsonProcessingException e) {
            HttpResponse<String> stringHttpResponse = syncRestClient
                .get(uri, getRequestHeaders(), Collections.emptyMap(), String.class);
            long duration = System.currentTimeMillis() - startTime;
            return new ExternalComponentStatus(ExternalComponentStatus.Component.AAI, false,
                new HttpRequestMetadata(
                    HttpMethod.GET,
                    getAllSubscribers().getStatus(),
                    uri,
                    stringHttpResponse.getBody(),
                    Logging.exceptionToDescription(e.getCause()), duration));
        }
    }

    private Map<String, String> getRequestHeaders() {
        val result = HashMap.of(
            TRANSACTION_ID_HEADER, UUID.randomUUID().toString(),
            FROM_APP_ID_HEADER, CALLER_APP_ID,
            CONTENT_TYPE, MediaType.APPLICATION_JSON,
            REQUEST_ID, Logging.extractOrGenerateRequestId(),
            ACCEPT, MediaType.APPLICATION_JSON)
            .toJavaMap();
        result.putAll(getAuthorizationHeader());
        return result;
    }

    private Map<String, String> getAuthorizationHeader() {
        if (!useClientCert) {
            val vidUsername = SystemProperties.getProperty(AAIProperties.AAI_VID_USERNAME);
            val vidPassword = Password.deobfuscate(SystemProperties.getProperty(AAIProperties.AAI_VID_PASSWD_X));
            val encoded = Base64.getEncoder()
                .encodeToString((vidUsername + ":" + vidPassword).getBytes(StandardCharsets.UTF_8));
            return HashMap.of("Authorization", "Basic " + encoded).toJavaMap();
        }
        return HashMap.<String, String>empty().toJavaMap();
    }

    private BasicHttpResponse repackHttpResponse(Object entity, int status, String msg) {
        val body = new BasicHttpEntity();
        try {
            val baos = new ByteArrayOutputStream();
            val oos = new ObjectOutputStream(baos);
            oos.writeObject(entity);
            oos.flush();
            oos.close();
            body.setContent(new ByteArrayInputStream(baos.toByteArray()));
            val response = new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), status, msg));
            response.setEntity(body);
            return response;
        } catch (IOException e) {
            logger.error("Failed to repack aai response.", e);
            return new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), status, msg));
        }
    }

    private HttpResponse<AaiGetNetworkCollectionDetails> getNetworkCollectionDetailsResponse(
        HttpResponse<AaiGetNetworkCollectionDetailsHelper> aaiResponse) {
        if (aaiResponse.getStatus() == 200) {
            val om = new ObjectMapper();
            val aaiGetNetworkCollectionDetails = new AaiGetNetworkCollectionDetails();
            try {
                for (int i = 0; i < aaiResponse.getBody().getResults().size(); i++) {
                    val temp = ((LinkedHashMap) aaiResponse.getBody().getResults().get(i));
                    if (temp.get("service-instance") != null) {
                        aaiGetNetworkCollectionDetails.getResults().setServiceInstance(
                            om.readValue(om.writeValueAsString(temp.get("service-instance")),
                                org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.ServiceInstance.class));
                    } else if (temp.get("collection") != null) {
                        aaiGetNetworkCollectionDetails.getResults().setCollection(
                            om.readValue(om.writeValueAsString(temp.get("collection")),
                                org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Collection.class));
                    } else if (temp.get("instance-group") != null) {
                        aaiGetNetworkCollectionDetails.getResults().setInstanceGroup(
                            om.readValue(om.writeValueAsString(temp.get("instance-group")), InstanceGroup.class));
                    } else if (temp.get("l3-network") != null) {
                        aaiGetNetworkCollectionDetails.getResults().getNetworks()
                            .add(om.readValue(om.writeValueAsString(temp.get("l3-network")), Network.class));
                    }
                }
                return new HttpResponse<>(repackHttpResponse(aaiGetNetworkCollectionDetails, HttpStatus.SC_OK, "OK"),
                    AaiGetNetworkCollectionDetails.class);
            } catch (JsonMappingException e) {
                return new HttpResponse<>(
                    repackHttpResponse(null, aaiResponse.getStatus(), "AAI response parsing Error: " + e.getCause()),
                    AaiGetNetworkCollectionDetails.class);
            } catch (Exception e) {
                return new HttpResponse<>(repackHttpResponse(null, aaiResponse.getStatus(),
                    "Got " + aaiResponse.getStatus() + " from a&ai: " + e.getCause()),
                    AaiGetNetworkCollectionDetails.class);
            }
        } else {
            return new HttpResponse<>(repackHttpResponse(null, aaiResponse.getStatus(), aaiResponse.getStatusText()),
                AaiGetNetworkCollectionDetails.class);
        }
    }
}
