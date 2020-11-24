/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

import static com.fasterxml.jackson.module.kotlin.ExtensionsKt.jacksonObjectMapper;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.exceptions.InvalidAAIResponseException;
import org.onap.vid.aai.model.AaiGetAicZone.AicZones;
import org.onap.vid.aai.model.AaiGetInstanceGroupsByCloudRegion;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.AaiGetNetworkCollectionDetails;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.AaiGetNetworkCollectionDetailsHelper;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.AaiGetRelatedInstanceGroupsByVnfId;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.CloudRegion;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.InstanceGroup;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Network;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfResponse;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetServicesRequestModel.GetServicesAAIRespone;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.CustomQuerySimpleResult;
import org.onap.vid.aai.model.GetServiceModelsByDistributionStatusResponse;
import org.onap.vid.aai.model.LogicalLinkResponse;
import org.onap.vid.aai.model.ModelVer;
import org.onap.vid.aai.model.ModelVersions;
import org.onap.vid.aai.model.OwningEntityResponse;
import org.onap.vid.aai.model.PortDetailsTranslator;
import org.onap.vid.aai.model.ProjectResponse;
import org.onap.vid.aai.model.Properties;
import org.onap.vid.aai.model.ResourceType;
import org.onap.vid.aai.model.ServiceRelationships;
import org.onap.vid.aai.model.SimpleResult;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.aai.util.CacheProvider;
import org.onap.vid.aai.util.VidObjectMapperType;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.model.probes.ErrorMetadata;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.model.probes.HttpRequestMetadata;
import org.onap.vid.utils.Logging;
import org.onap.vid.utils.Unchecked;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriUtils;

public class AaiClient implements AaiClientInterface {


    public static final String QUERY_FORMAT_RESOURCE = "query?format=resource";
    private static final String SERVICE_SUBSCRIPTIONS_PATH = "/service-subscriptions/service-subscription/";
    private static final String MODEL_INVARIANT_ID = "&model-invariant-id=";
    private static final String QUERY_FORMAT_SIMPLE = "query?format=simple";
    private static final String BUSINESS_CUSTOMER = "/business/customers/customer/";
    private static final String SERVICE_INSTANCE = "/service-instances/service-instance/";
    private static final String BUSINESS_CUSTOMERS_CUSTOMER = "business/customers/customer/";

    protected String fromAppId = "VidAaiController";

    private PortDetailsTranslator portDetailsTranslator;

    private final AAIRestInterface restController;

    private final CacheProvider cacheProvider;

    ObjectMapper objectMapper = jacksonObjectMapper();

    /**
     * The logger
     */
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AaiClient.class);

    private static final String GET_SERVICE_MODELS_REQUEST_BODY = "{\"start\" : \"service-design-and-creation/models/\", \"query\" : \"query/serviceModels-byDistributionStatus?distributionStatus=DISTRIBUTION_COMPLETE_OK\"}";

    @Inject
    public AaiClient(AAIRestInterface restController, PortDetailsTranslator portDetailsTranslator, CacheProvider cacheProvider) {
        this.restController = restController;
        this.portDetailsTranslator = portDetailsTranslator;
        this.cacheProvider = cacheProvider;
    }


    private static String checkForNull(String local) {
        if (local != null)
            return local;
        else
            return "";

    }

    @Override
    public AaiResponse<OwningEntityResponse> getServicesByOwningEntityId(List<String> owningEntityIds){
        Response resp = doAaiGet(getUrlFromLIst("business/owning-entities?", "owning-entity-id=", owningEntityIds), false);
        return processAaiResponse(resp, OwningEntityResponse.class, null);
    }

    @Override
    public AaiResponse<ProjectResponse> getServicesByProjectNames(List<String> projectNames){
        Response resp = doAaiGet(getUrlFromLIst("business/projects?", "project-name=",  projectNames), false);
        return processAaiResponse(resp, ProjectResponse.class, null);
    }

    @Override
    public AaiResponse getServiceModelsByDistributionStatus() {
        return getFromCache("getServiceModelsByDistributionStatus", this::getServiceModelsByDistributionStatusNonCached,
                true, "Failed to get service models by distribution status");
    }

    private AaiResponse getServiceModelsByDistributionStatusNonCached(boolean propagateExceptions) {
        GetServiceModelsByDistributionStatusResponse response = typedAaiRest(QUERY_FORMAT_RESOURCE, GetServiceModelsByDistributionStatusResponse.class,
                GET_SERVICE_MODELS_REQUEST_BODY, HttpMethod.PUT, propagateExceptions);
        return new AaiResponse(response, "", HttpStatus.SC_OK);
    }

    @Override
    public AaiResponse getNetworkCollectionDetails(String serviceInstanceId) {
        Response resp = doAaiPut(QUERY_FORMAT_RESOURCE, "{\"start\": [\"nodes/service-instances/service-instance/" + serviceInstanceId + "\"],\"query\": \"query/network-collection-ByServiceInstance\"}\n", false);
        AaiResponse<AaiGetNetworkCollectionDetailsHelper> aaiResponse = processAaiResponse(resp, AaiGetNetworkCollectionDetailsHelper.class, null, VidObjectMapperType.FASTERXML);
        return getNetworkCollectionDetailsResponse(aaiResponse);
    }

    @Override
    public AaiResponse getInstanceGroupsByCloudRegion(String cloudOwner, String cloudRegionId, String networkFunction) {
        Response resp = doAaiPut(QUERY_FORMAT_RESOURCE,
                "{\"start\": [\"cloud-infrastructure/cloud-regions/cloud-region/" + encodePathSegment(cloudOwner) + "/" + encodePathSegment(cloudRegionId) + "\"]," +
                        "\"query\": \"query/instance-groups-byCloudRegion?type=L3-NETWORK&role=SUB-INTERFACE&function=" + encodePathSegment(networkFunction) + "\"}\n", false);
        return processAaiResponse(resp, AaiGetInstanceGroupsByCloudRegion.class, null, VidObjectMapperType.FASTERXML);
    }

    private AaiResponse getNetworkCollectionDetailsResponse(AaiResponse<AaiGetNetworkCollectionDetailsHelper> aaiResponse){
        if(aaiResponse.getHttpCode() == 200) {
            ObjectMapper om = objectMapper;
            AaiGetNetworkCollectionDetails aaiGetNetworkCollectionDetails = new AaiGetNetworkCollectionDetails();
            try {
                for (int i = 0; i < aaiResponse.getT().getResults().size(); i++) {
                    LinkedHashMap<String, Object> temp = ((LinkedHashMap) aaiResponse.getT().getResults().get(i));
                    if (temp.get("service-instance") != null)
                        aaiGetNetworkCollectionDetails.getResults().setServiceInstance(om.readValue(om.writeValueAsString(temp.get("service-instance")), org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.ServiceInstance.class));
                    else if (temp.get("collection") != null)
                        aaiGetNetworkCollectionDetails.getResults().setCollection(om.readValue(om.writeValueAsString(temp.get("collection")), org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Collection.class));
                    else if (temp.get("instance-group") != null)
                        aaiGetNetworkCollectionDetails.getResults().setInstanceGroup(om.readValue(om.writeValueAsString(temp.get("instance-group")), InstanceGroup.class));
                    else if (temp.get("l3-network") != null)
                        aaiGetNetworkCollectionDetails.getResults().getNetworks().add(om.readValue(om.writeValueAsString(temp.get("l3-network")), Network.class));
                }
                return new AaiResponse(aaiGetNetworkCollectionDetails, null, HttpStatus.SC_OK);
            }
            catch (com.fasterxml.jackson.databind.JsonMappingException e) {
                logger.error(EELFLoggerDelegate.errorLogger, "AAI response parsing Error",  e);
                return new AaiResponse(e.getCause(), "AAI response parsing Error" , HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
            catch (Exception e) {
                logger.error(EELFLoggerDelegate.errorLogger,"Exception in aai response parsing", e);
                return new AaiResponse(e.getCause(), "Got " + aaiResponse.getHttpCode() + " from a&ai" , aaiResponse.getHttpCode());
            }
        }
        return aaiResponse;
    }

    @Override
    public AaiResponse getPNFData(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion, String equipVendor, String equipModel) {
        String siQuery = BUSINESS_CUSTOMER + globalCustomerId + SERVICE_SUBSCRIPTIONS_PATH + encodePathSegment(serviceType) + "/service-instances?model-version-id=" + modelVersionId + MODEL_INVARIANT_ID + modelInvariantId;
        String pnfQuery = "query/pnf-fromModel-byRegion?cloudRegionId=" + encodePathSegment(cloudRegion) + "&equipVendor=" + encodePathSegment(equipVendor) + "&equipModel=" + encodePathSegment(equipModel);
        String payload = "{\"start\":\"" + siQuery + "\",\"query\":\"" + pnfQuery + "\"}";
        Response resp = doAaiPut(QUERY_FORMAT_SIMPLE, payload, false);
        return processAaiResponse(resp, AaiGetPnfResponse.class, null);
    }


    @Override
    public AaiResponse<Pnf> getSpecificPnf(String pnfId) {
        Response resp = doAaiGet("network/pnfs/pnf/"+pnfId, false);
        return processAaiResponse(resp, Pnf.class, null);
    }


    public AaiResponse getInstanceGroupsByVnfInstanceId(String vnfInstanceId){
        Response resp = doAaiGet("network/generic-vnfs/generic-vnf/" + vnfInstanceId + "?depth=0", false);
        return processAaiResponse(resp, AaiGetRelatedInstanceGroupsByVnfId.class , null, null);
    }


    @Override
    public List<PortDetailsTranslator.PortDetails> getPortMirroringSourcePorts(String configurationID) {
        String payload = "{\"start\":\"/network/configurations/configuration/" + configurationID + "\",\"query\":\"query/pserver-fromConfiguration\"}";
        Response resp = doAaiPut(QUERY_FORMAT_SIMPLE, payload, false);
        resp.bufferEntity(); // avoid later "Entity input stream has already been closed" problems
        String rawPayload = resp.readEntity(String.class);
        AaiResponse<CustomQuerySimpleResult> aaiResponse = processAaiResponse(resp, CustomQuerySimpleResult.class, rawPayload);
        return portDetailsTranslator.extractPortDetails(aaiResponse, rawPayload);
    }



    public AaiResponse getServiceInstance(String globalCustomerId, String serviceType, String serviceInstanceId) {
        String getServiceInstancePath = BUSINESS_CUSTOMERS_CUSTOMER + globalCustomerId+ SERVICE_SUBSCRIPTIONS_PATH +serviceType+ SERVICE_INSTANCE +serviceInstanceId;
        Response resp = doAaiGet(getServiceInstancePath , false);
        return processAaiResponse(resp, ServiceRelationships.class, null);
    }

    @Override
    public AaiResponse getLogicalLink(String link) {
        Response resp = doAaiGet("network/logical-links/logical-link/" + link , false);
        return processAaiResponse(resp, LogicalLinkResponse.class, null);
    }

    @Override
    public boolean isNodeTypeExistsByName(String name, ResourceType type) {
        if (isEmpty(name)) {
            throw new GenericUncheckedException("Empty resource-name provided to searchNodeTypeByName; request is rejected as this will cause full resources listing");
        }

        URI path = Unchecked.toURI(String.format( // e.g. GET /aai/v$/nodes/vf-modules?vf-module-name={vf-module-name}
                "nodes/%s?%s=%s",
                type.getAaiFormat(),
                type.getNameFilter(),
                encodePathSegment(name)
        ));
        final ResponseWithRequestInfo responseWithRequestInfo = restController.RestGet(fromAppId, UUID.randomUUID().toString(), path, false, true);

        return isResourceExistByStatusCode(responseWithRequestInfo);
    }

    public Map<String, Properties> getCloudRegionAndTenantByVnfId(String vnfId) {
        String start = "/network/generic-vnfs/generic-vnf/" + vnfId;
        String query = "/query/cloud-region-fromVnf";

        String payload = "{\"start\":[\"" + start + "\"],\"query\":\"" + query + "\"}";
        CustomQuerySimpleResult result = typedAaiRest(QUERY_FORMAT_SIMPLE, CustomQuerySimpleResult.class, payload, HttpMethod.PUT, false);

        return result.getResults().stream()
                .filter(res -> StringUtils.equals(res.getNodeType(), "tenant") ||
                        StringUtils.equals(res.getNodeType(), "cloud-region"))
                .collect(toMap(SimpleResult::getNodeType, SimpleResult::getProperties));
    }

    @Override
    public AaiResponse<AaiGetVnfResponse> getVnfsByParamsForChangeManagement(String subscriberId, String serviceType, @Nullable String nfRole,
        @Nullable String cloudRegion) {
        String payloadAsString = "";
        ResponseWithRequestInfo response;
        ImmutableMap<String, Serializable> payload = getMapForAAIQueryByParams(subscriberId, serviceType,
            nfRole, cloudRegion);
        try {
            payloadAsString = JACKSON_OBJECT_MAPPER.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            ExceptionUtils.rethrow(e);
        }
        response = doAaiPut(QUERY_FORMAT_SIMPLE, payloadAsString, false, false);
        AaiResponseWithRequestInfo<AaiGetVnfResponse> aaiResponse = processAaiResponse(response, AaiGetVnfResponse.class, false);
        verifyAaiResponseValidityOrThrowExc(aaiResponse, aaiResponse.getAaiResponse().getHttpCode());
        return aaiResponse.getAaiResponse();
    }

    private ImmutableMap<String, Serializable> getMapForAAIQueryByParams(String subscriberId,
        String serviceType, @Nullable String nfRole, @Nullable String cloudRegion) {
        // in a case cloudRegion is null using query/vnfs-fromServiceInstance-filter,
        // otherwise using query/vnfs-fromServiceInstance-filterByCloudRegion
        if (nfRole != null){
            if (cloudRegion != null){
                return ImmutableMap.of(
                    "start", ImmutableList
                        .of("/business/customers/customer/" + encodePathSegment(subscriberId) + "/service-subscriptions/service-subscription/" + encodePathSegment(serviceType) + "/service-instances"),
                    "query",  "query/vnfs-fromServiceInstance-filterByCloudRegion?nfRole=" + nfRole + "&cloudRegionID=" + cloudRegion + ""
                );
            }else {
                return ImmutableMap.of(
                    "start", ImmutableList
                        .of("/business/customers/customer/" + encodePathSegment(subscriberId) + "/service-subscriptions/service-subscription/" + encodePathSegment(serviceType) + "/service-instances"),
                    "query",  "query/vnfs-fromServiceInstance-filter?nfRole=" + nfRole + ""
                );
            }
        }

        if (cloudRegion != null){
            return ImmutableMap.of(
                "start", ImmutableList
                    .of("/business/customers/customer/" + encodePathSegment(subscriberId) + "/service-subscriptions/service-subscription/" + encodePathSegment(serviceType) + "/service-instances"),
                "query",  "query/vnfs-fromServiceInstance-filterByCloudRegion?cloudRegionID=" + cloudRegion + ""
            );
        }

        return ImmutableMap.of(
            "start", ImmutableList
                .of("/business/customers/customer/" + encodePathSegment(subscriberId) + "/service-subscriptions/service-subscription/" + encodePathSegment(serviceType) + "/service-instances"),
            "query", "query/vnfs-fromServiceInstance-filter"
        );
    }

    private boolean isResourceExistByStatusCode(ResponseWithRequestInfo responseWithRequestInfo) {
        // 200 - is found
        // 404 - resource not found
        Response.Status statusInfo = responseWithRequestInfo.getResponse().getStatusInfo().toEnum();
        switch (statusInfo) {
            case OK:
                return true;
            case NOT_FOUND:
                return false;
            default:
                throw new GenericUncheckedException("Unexpected response-code (only OK and NOT_FOUND are expected): " +
                        responseWithRequestInfo.getResponse().getStatusInfo());
        }
    }

    @Override
    public <T> T typedAaiGet(URI uri, Class<T> clz) {
        return typedAaiRest(uri, clz, null, HttpMethod.GET, false);
    }

    public <T> T typedAaiRest(String path, Class<T> clz, String payload, HttpMethod method, boolean propagateExceptions) {
        return typedAaiRest(Unchecked.toURI(path), clz, payload, method, propagateExceptions);
    }


    public <T> T typedAaiRest(URI path, Class<T> clz, String payload, HttpMethod method, boolean propagateExceptions) {
        ResponseWithRequestInfo responseWithRequestInfo;
        try {
            responseWithRequestInfo = restController.doRest(fromAppId, UUID.randomUUID().toString(), path, payload, method, false, propagateExceptions);
        } catch (Exception e) {
            responseWithRequestInfo = handleExceptionFromRestCall(propagateExceptions, "doAai"+method.name(), e);
        }

        final AaiResponseWithRequestInfo<T> aaiResponse = processAaiResponse(responseWithRequestInfo, clz, VidObjectMapperType.FASTERXML, true);
        verifyAaiResponseValidityOrThrowExc(aaiResponse, responseWithRequestInfo.getResponse().getStatus());
        return aaiResponse.getAaiResponse().getT();
    }

    private void verifyAaiResponseValidityOrThrowExc(AaiResponseWithRequestInfo aaiResponse, int httpCode) {
        if (aaiResponse.getAaiResponse().getHttpCode() > 399 || aaiResponse.getAaiResponse().getT() == null) {
            throw new ExceptionWithRequestInfo(aaiResponse.getHttpMethod(),
                aaiResponse.getRequestedUrl(),
                aaiResponse.getRawData(),
                httpCode,
                new InvalidAAIResponseException(aaiResponse.getAaiResponse()));
        }
    }

    private String getUrlFromLIst(String url, String paramKey, List<String> params){
        int i = 0;
        for(String param: params){
            i ++;
            url = url.concat(paramKey);
            String encodedParam= param;
            try {
                encodedParam= URLEncoder.encode(param, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                String methodName = "getUrlFromList";
                logger.error(EELFLoggerDelegate.errorLogger, methodName, e);
                logger.debug(EELFLoggerDelegate.debugLogger, methodName, e);
            }
            url = url.concat(encodedParam);
            if(i != params.size()){
                url = url.concat("&");
            }
        }
        return url;
    }



    @Override
    public AaiResponse<SubscriberList> getAllSubscribers() {
        return getFromCache("getAllSubscribers", this::getAllSubscribersNonCached, true, "Failed to get all subscribers");
    }

    private <K> AaiResponse getFromCache(String cacheName, Function<K, AaiResponse> function, K argument, String errorMessage) {
        try {
            return cacheProvider
                    .aaiClientCacheFor(cacheName, function)
                    .get(argument);
        } catch (ExceptionWithRequestInfo exception) {
            logger.error(errorMessage, exception);
            return new AaiResponse(null, exception.getRawData(), exception.getHttpCode());
        }
        catch (Exception exception) {
            logger.error(errorMessage, exception);
            return new AaiResponse(null, exception.getMessage(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private AaiResponse<SubscriberList> getAllSubscribersNonCached(boolean propagateExceptions) {
        AaiResponse<SubscriberList> aaiResponse = getAllSubscribers(propagateExceptions).getAaiResponse();
        if (propagateExceptions && (aaiResponse.getT() == null || aaiResponse.getT().customer == null || aaiResponse.getT().customer.isEmpty())) {
            throw new GenericUncheckedException("Failed to get Subscribers data. The data is null or empty.");
        } else {
            return aaiResponse;
        }
    }

    AaiResponseWithRequestInfo<SubscriberList> getAllSubscribers(boolean propagateExceptions){
        String depth = "0";
        ResponseWithRequestInfo aaiGetResult = doAaiGet("business/customers?subscriber-type=INFRA&depth=" + depth, false, propagateExceptions);
        AaiResponseWithRequestInfo<SubscriberList> responseWithRequestInfo = processAaiResponse(aaiGetResult, SubscriberList.class, propagateExceptions);
        responseWithRequestInfo.setRequestedUrl(aaiGetResult.getRequestUrl());
        responseWithRequestInfo.setHttpMethod(aaiGetResult.getRequestHttpMethod());
        return responseWithRequestInfo;
    }


    @Override
    public AaiResponse getAllAicZones() {
        Response resp = doAaiGet("network/zones", false);
        return processAaiResponse(resp, AicZones.class, null);
    }

    @Override
    public AaiResponse<AaiGetVnfResponse> getVNFData(String globalSubscriberId, String serviceType) {
        String payload = "{\"start\": [\"business/customers/customer/" + globalSubscriberId + SERVICE_SUBSCRIPTIONS_PATH + encodePathSegment(serviceType) +"/service-instances\"]," +
                "\"query\": \"query/vnf-topology-fromServiceInstance\"}";
        Response resp = doAaiPut(QUERY_FORMAT_SIMPLE, payload, false);
        return processAaiResponse(resp, AaiGetVnfResponse.class, null);
    }

    @Override
    public AaiResponse getVNFData(String globalSubscriberId, String serviceType, String serviceInstanceId) {
        String payload = "{\"start\": [\"/business/customers/customer/" + globalSubscriberId + SERVICE_SUBSCRIPTIONS_PATH + encodePathSegment(serviceType) + SERVICE_INSTANCE + serviceInstanceId + "\"],	\"query\": \"query/vnf-topology-fromServiceInstance\"}";
        Response resp = doAaiPut(QUERY_FORMAT_SIMPLE, payload, false);
        return processAaiResponse(resp, AaiGetVnfResponse.class, null);
    }

    @Override
    public Response getVersionByInvariantId(List<String> modelInvariantId) {
        if (modelInvariantId.isEmpty()) {
            throw new GenericUncheckedException("Zero invariant-ids provided to getVersionByInvariantId; request is rejected as this will cause full models listing");
        }

        StringBuilder sb = new StringBuilder();
        for (String id : modelInvariantId){
            sb.append(MODEL_INVARIANT_ID);
            sb.append(id);

        }
        return doAaiGet("service-design-and-creation/models?depth=2" + sb.toString(), false);
    }

    @Override
    public ModelVer getLatestVersionByInvariantId(String modelInvariantId) {
        return maxModelVer(getAllVersionsByInvariantId(modelInvariantId));
    }

    @Override
    public List<ModelVer> getSortedVersionsByInvariantId(String modelInvariantId) {
        return sortedModelVer(getAllVersionsByInvariantId(modelInvariantId));
    }

    private Stream<ModelVer> getAllVersionsByInvariantId(String modelInvariantId) {
        if (modelInvariantId.isEmpty()) {
            throw new GenericUncheckedException("no invariant-id provided to getLatestVersionByInvariantId; request is rejected");
        }

        Response response = doAaiPut("query?format=resource&depth=0",  "{\"start\": [\"service-design-and-creation/models/model/" + modelInvariantId + "\"],\"query\": \"query/serviceModels-byDistributionStatus?distributionStatus=DISTRIBUTION_COMPLETE_OK\"}",false);
        AaiResponse<ModelVersions> aaiResponse = processAaiResponse(response, ModelVersions.class, null, VidObjectMapperType.FASTERXML);

        return toModelVerStream(aaiResponse.getT());
    }

    protected Stream<ModelVer> toModelVerStream(ModelVersions modelVersions) {
        if (modelVersions == null)
            return null;

        return Stream.of(modelVersions)
                .map(ModelVersions::getResults)
                .flatMap(java.util.Collection::stream)
                .flatMap(map -> map.entrySet().stream())
                .filter(kv -> StringUtils.equals(kv.getKey(), "model-ver"))
                .map(Map.Entry::getValue);

    }

    protected ModelVer maxModelVer(Stream<ModelVer> modelVerStream) {
        if (modelVerStream == null)
            return null;

        return modelVerStream
                .filter(modelVer -> StringUtils.isNotEmpty(modelVer.getModelVersion()))
                .max(comparing(ModelVer::getModelVersion, comparing(DefaultArtifactVersion::new)))
                .orElseThrow(() -> new GenericUncheckedException("Could not find any version"));
    }

    protected List<ModelVer> sortedModelVer(Stream<ModelVer> modelVerStream) {
        if (modelVerStream == null)
            return emptyList();

        return modelVerStream
            .sorted(comparing(ModelVer::getModelVersion, comparing(DefaultArtifactVersion::new)).reversed())
            .collect(Collectors.toList());
    }

    @Override
    public AaiResponse<Services> getSubscriberData(String subscriberId, boolean omitServiceInstances) {
        String depth = omitServiceInstances ? "1" : "2";
        AaiResponse<Services> subscriberDataResponse;
		String query = depth.equals("1") ?
                BUSINESS_CUSTOMERS_CUSTOMER + subscriberId + "?depth=" + depth :
                BUSINESS_CUSTOMERS_CUSTOMER + subscriberId + "?depth=" + depth +"&nodes-only";
				
        Response resp = doAaiGet(query, false);
        subscriberDataResponse = processAaiResponse(resp, Services.class, null);
        return subscriberDataResponse;
    }

    @Override
    public AaiResponse getServices() {
        Response resp = doAaiGet("service-design-and-creation/services", false);
        return processAaiResponse(resp, GetServicesAAIRespone.class, null);
    }

    @Override
    public AaiResponse getOperationalEnvironments(String operationalEnvironmentType, String operationalEnvironmentStatus) {
        String url = "cloud-infrastructure/operational-environments";
        URIBuilder urlBuilder  = new URIBuilder();
        if (operationalEnvironmentType != null)
            urlBuilder.addParameter("operational-environment-type", operationalEnvironmentType);
        if (operationalEnvironmentStatus != null)
            urlBuilder.addParameter("operational-environment-status", operationalEnvironmentStatus);
        url += urlBuilder.toString();
        Response resp = doAaiGet(url, false);
        return processAaiResponse(resp, OperationalEnvironmentList.class, null);
    }

    @Override
    public AaiResponse getTenants(String globalCustomerId, String serviceType) {
        if ((globalCustomerId == null || globalCustomerId.isEmpty()) || ((serviceType == null) || (serviceType.isEmpty()))){
            return buildAaiResponseForGetTenantsFailure(" Failed to retrieve LCP Region & Tenants from A&AI, Subscriber ID or Service Type is missing.");
        }
        try {
            return cacheProvider
                    .aaiClientCacheFor("getTenants", this::getTenantsByKey)
                    .get(CacheProvider.compileKey(globalCustomerId, serviceType));
        }
        catch (ParsingGetTenantsResponseFailure exception) {
            logger.error("Failed to get tenants ", exception);
            return buildAaiResponseForGetTenantsFailure(exception.getMessage());
        }
    }

    @Override
    public AaiResponse getNodeTemplateInstances(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion) {

        String siQuery = BUSINESS_CUSTOMER + globalCustomerId + SERVICE_SUBSCRIPTIONS_PATH + encodePathSegment(serviceType) + "/service-instances?model-version-id=" + modelVersionId + MODEL_INVARIANT_ID + modelInvariantId;
        String vnfQuery = "query/queryvnfFromModelbyRegion?cloudRegionId=" + encodePathSegment(cloudRegion);
        String payload1 = "{\"start\":\"" + siQuery + "\",\"query\":\"" + vnfQuery + "\"}";

        Response resp1 = doAaiPut(QUERY_FORMAT_SIMPLE, payload1, false);
        AaiResponse aaiResponse1 = processAaiResponse(resp1, AaiGetVnfResponse.class, null);
        logger.debug(EELFLoggerDelegate.debugLogger, "getNodeTemplateInstances AAI's response: {}", aaiResponse1);
        return aaiResponse1;
    }

    @Override
    public AaiResponse<JsonNode> getCloudRegionAndSourceByPortMirroringConfigurationId(String configurationId) {
        final String start = "[\"network/configurations/configuration/" + configurationId + "\"]";
        final String query = "\"query/cloud-region-and-source-FromConfiguration\"";
        String payload = "{\"start\":" + start + ",\"query\":" + query + "}";

        Response response = doAaiPut("query?format=simple&nodesOnly=true", payload, false);
        AaiResponse<JsonNode> aaiResponse = processAaiResponse(response, JsonNode.class, null);

        logger.debug(EELFLoggerDelegate.debugLogger, "getNodeTemplateInstances AAI's response: {}", aaiResponse);
        return aaiResponse;
    }

    private <T> AaiResponseWithRequestInfo<T> processAaiResponse(ResponseWithRequestInfo responseWithRequestInfo, Class<? extends T> classType, boolean propagateExceptions) {
        return processAaiResponse(responseWithRequestInfo, classType, VidObjectMapperType.CODEHAUS, propagateExceptions);
    }

    private <T> AaiResponseWithRequestInfo<T> processAaiResponse(ResponseWithRequestInfo responseWithRequestInfo, Class<? extends T> classType, VidObjectMapperType omType, boolean propagateExceptions) {
        String responseBody = null;
        Integer responseHttpCode = null;
        try {
            Response response = responseWithRequestInfo.getResponse();
            responseHttpCode = (response != null) ? response.getStatus() : null;
            responseBody = (response != null) ? response.readEntity(String.class) : null;
            AaiResponse<T> processedAaiResponse = processAaiResponse(response, classType, responseBody, omType, propagateExceptions);
            return new AaiResponseWithRequestInfo<>(responseWithRequestInfo.getRequestHttpMethod(), responseWithRequestInfo.getRequestUrl(), processedAaiResponse,
                    responseBody);
        } catch (Exception e) {
            throw new ExceptionWithRequestInfo(responseWithRequestInfo.getRequestHttpMethod(),
                    responseWithRequestInfo.getRequestUrl(), responseBody, responseHttpCode, e);
        }
    }

    private <T> AaiResponse<T> processAaiResponse(Response resp, Class<? extends T> classType, String responseBody) {
        return processAaiResponse(resp, classType, responseBody, VidObjectMapperType.CODEHAUS);
    }

    private <T> AaiResponse<T> processAaiResponse(Response resp, Class<? extends T> classType, String responseBody, VidObjectMapperType omType) {
        return processAaiResponse(resp, classType, responseBody, omType, false);
    }

    private  <T> AaiResponse<T> processAaiResponse(Response resp, Class<? extends T> classType, String responseBody, VidObjectMapperType omType, boolean propagateExceptions) {
        AaiResponse<T> subscriberDataResponse;
        if (resp == null) {
            subscriberDataResponse = new AaiResponse<>(null, null, HttpStatus.SC_INTERNAL_SERVER_ERROR);
            logger.debug(EELFLoggerDelegate.debugLogger, "Invalid response from AAI");
        } else {
            logger.debug(EELFLoggerDelegate.debugLogger, "getSubscribers() resp=" + resp.getStatusInfo().toString());
            if (resp.getStatus() != HttpStatus.SC_OK) {
                subscriberDataResponse = processFailureResponse(resp,responseBody);
            } else {
                subscriberDataResponse = processOkResponse(resp, classType, responseBody, omType, propagateExceptions);
            }
        }
        return subscriberDataResponse;
    }

    private AaiResponse processFailureResponse(Response resp, String responseBody) {
        logger.debug(EELFLoggerDelegate.debugLogger, "Invalid response from AAI");
        String rawData;
        if (responseBody != null) {
            rawData = responseBody;
        } else {
            rawData = resp.readEntity(String.class);
        }
        return new AaiResponse<>(null, rawData, resp.getStatus());
    }

    private <T> AaiResponse<T> processOkResponse(Response resp, Class<? extends T> classType, String responseBody, VidObjectMapperType omType, boolean propagateExceptions) {
        AaiResponse<T> subscriberDataResponse;
        String finalResponse = null;
        try {
            if (responseBody != null) {
                finalResponse = responseBody;
            } else {
                finalResponse = resp.readEntity(String.class);
            }

            if(omType == VidObjectMapperType.CODEHAUS)
                subscriberDataResponse = parseCodeHausObject(classType, finalResponse);
            else
                subscriberDataResponse = parseFasterXmlObject(classType, finalResponse);

        } catch(Exception e){
            if (propagateExceptions) {
                throw new GenericUncheckedException(e);
            } else {
                subscriberDataResponse = new AaiResponse<>(null, null, HttpStatus.SC_INTERNAL_SERVER_ERROR);
                logger.error("Failed to parse aai response: \"{}\" to class {}", finalResponse, classType, e);
            }
        }
        return subscriberDataResponse;
    }

    private <T> AaiResponse<T> parseFasterXmlObject(Class<? extends T> classType, String finalResponse) throws IOException {
        return new AaiResponse<>((objectMapper.readValue(finalResponse, classType)), null, HttpStatus.SC_OK);
    }

    private <T> AaiResponse<T> parseCodeHausObject(Class<? extends T> classType, String finalResponse) throws IOException {
        return new AaiResponse<>((objectMapper.readValue(finalResponse, classType)), null, HttpStatus.SC_OK);
    }

    @Override
    public Response doAaiGet(String uri, boolean xml) {
        return doAaiGet(uri, xml, false).getResponse();
    }


    public ResponseWithRequestInfo doAaiGet(String uri, boolean xml, boolean propagateExceptions) {
        return doAaiGet(Unchecked.toURI(uri), xml, propagateExceptions);
    }

    public ResponseWithRequestInfo doAaiGet(URI uri, boolean xml, boolean propagateExceptions) {
        String methodName = "doAaiGet";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + " start");

        ResponseWithRequestInfo resp;
        try {
            resp = restController.RestGet(fromAppId, UUID.randomUUID().toString(), uri, xml, propagateExceptions);

        } catch (Exception e) {
            resp = handleExceptionFromRestCall(propagateExceptions, methodName, e);
        }
        return resp;
    }

    @NotNull
    protected ResponseWithRequestInfo handleExceptionFromRestCall(boolean propagateExceptions, String methodName, Exception e) {
        ResponseWithRequestInfo resp;
        if (propagateExceptions) {
            throw (e instanceof RuntimeException) ? (RuntimeException)e : new GenericUncheckedException(e);
        } else {
            final Exception actual =
                    e instanceof ExceptionWithRequestInfo ? (Exception) e.getCause() : e;

            final String message =
                    actual instanceof WebApplicationException ? ((WebApplicationException) actual).getResponse().readEntity(String.class) : e.toString();

            //ToDo: change parameter of requestUrl to real url from doRest function
            resp = new ResponseWithRequestInfo(null, null, org.springframework.http.HttpMethod.GET);
            logger.info(EELFLoggerDelegate.errorLogger, methodName + message);
            logger.debug(EELFLoggerDelegate.debugLogger, methodName + message);
        }
        return resp;
    }

    private String parseForTenantsByServiceSubscription(String relatedToKey, String resp) {
        String tenantList = "";

        try {
            JSONParser jsonParser = new JSONParser();

            JSONObject jsonObject = (JSONObject) jsonParser.parse(resp);

            return parseServiceSubscriptionObjectForTenants(relatedToKey, jsonObject);
        } catch (Exception ex) {
            logger.debug(EELFLoggerDelegate.debugLogger, "parseForTenantsByServiceSubscription error while parsing tenants by service subscription", ex);
        }
        return tenantList;
    }

    protected Response doAaiPut(String uri, String payload, boolean xml) {
        return doAaiPut(uri, payload, xml, false).getResponse();
    }

    protected ResponseWithRequestInfo doAaiPut(String uri, String payload, boolean xml, boolean propagateExceptions) {
        String methodName = "doAaiPut";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + " start");

        ResponseWithRequestInfo resp;
        try {

            resp = restController.RestPut(fromAppId, uri, payload, xml, propagateExceptions);

        } catch (Exception e) {
            resp = handleExceptionFromRestCall(propagateExceptions, methodName, e);
        }
        return resp;
    }


    private String parseServiceSubscriptionObjectForTenants(String relatedToKey, JSONObject jsonObject) {
        JSONArray tenantArray = new JSONArray();
        boolean bconvert = false;
        try {
            JSONObject relationShipListsObj = (JSONObject) jsonObject.get("relationship-list");
            if (relationShipListsObj != null) {
                JSONArray rShipArray = (JSONArray) relationShipListsObj.get("relationship");
                for (Object innerObj : defaultIfNull(rShipArray, emptyList())) {
                    if (innerObj != null) {
                        bconvert = parseTenant(relatedToKey, tenantArray, bconvert, (JSONObject) innerObj);
                    }
                }
            }
        } catch (NullPointerException ex) {
            logger.debug(EELFLoggerDelegate.debugLogger, "parseServiceSubscriptionObjectForTenants. error while parsing service subscription object for tenants", ex);
        }

        if (bconvert)
            return tenantArray.toJSONString();
        else
            return "";

    }

    private static boolean parseTenant(String relatedToKey, JSONArray tenantArray, boolean bconvert, JSONObject inner1Obj) {
        String relatedTo = checkForNull((String) inner1Obj.get("related-to"));
        if (relatedTo.equalsIgnoreCase(relatedToKey)) {
            JSONObject tenantNewObj = new JSONObject();

            String relatedLink = checkForNull((String) inner1Obj.get("related-link"));
            tenantNewObj.put("link", relatedLink);

            JSONArray rDataArray = (JSONArray) inner1Obj.get("relationship-data");
            for (Object innerObj : defaultIfNull(rDataArray, emptyList())) {
                parseRelationShip(tenantNewObj, (JSONObject) innerObj);
            }

            JSONArray relatedTPropArray = (JSONArray) inner1Obj.get("related-to-property");
            for (Object innerObj : defaultIfNull(relatedTPropArray, emptyList())) {
                parseRelatedTProp(tenantNewObj, (JSONObject) innerObj);
            }
            bconvert = true;
            tenantArray.add(tenantNewObj);
        }
        return bconvert;
    }

    private static void parseRelatedTProp(JSONObject tenantNewObj, JSONObject innerObj) {
        if (innerObj == null)
            return;

        String propKey = checkForNull((String) innerObj.get("property-key"));
        String propVal = checkForNull((String) innerObj.get("property-value"));
        if (equalsIgnoreCase(propKey, "tenant.tenant-name")) {
            tenantNewObj.put("tenantName", propVal);
        }
    }

    private static void parseRelationShip(JSONObject tenantNewObj, JSONObject inner2Obj) {
        if (inner2Obj == null)
            return;

        String rShipKey = checkForNull((String) inner2Obj.get("relationship-key"));
        String rShipVal = checkForNull((String) inner2Obj.get("relationship-value"));
        if (equalsIgnoreCase(rShipKey, "cloud-region.cloud-owner")) {
            tenantNewObj.put("cloudOwner", rShipVal);
        } else if (equalsIgnoreCase(rShipKey, "cloud-region.cloud-region-id")) {
            tenantNewObj.put("cloudRegionID", rShipVal);
        } else if (equalsIgnoreCase(rShipKey, "tenant.tenant-id")) {
            tenantNewObj.put("tenantID", rShipVal);
        }
    }

    private static String encodePathSegment(String segmentToEncode) {
        return UriUtils.encodePathSegment(segmentToEncode, "UTF-8");
    }

    @Override
    public ExternalComponentStatus probeComponent(){
        long startTime = System.currentTimeMillis();
        try {
            AaiResponseWithRequestInfo<SubscriberList> responseWithRequestInfo = getAllSubscribers(true);
            AaiResponse<SubscriberList> aaiResponse = responseWithRequestInfo.getAaiResponse();
            long duration = System.currentTimeMillis() - startTime;

            SubscriberList subscribersList = (aaiResponse != null) ? aaiResponse.getT() : null;
            boolean isAvailable = subscribersList != null && subscribersList.customer != null && !subscribersList.customer.isEmpty();

            HttpRequestMetadata metadata = new HttpRequestMetadata(
                    responseWithRequestInfo.getHttpMethod(),
                    (aaiResponse != null) ? aaiResponse.getHttpCode() : 0,
                    responseWithRequestInfo.getRequestedUrl(),
                    responseWithRequestInfo.getRawData(),
                    isAvailable ? "OK" : "No subscriber received",
                    duration
            );
            return new ExternalComponentStatus(ExternalComponentStatus.Component.AAI, isAvailable, metadata);

        } catch (ExceptionWithRequestInfo e) {
            long duration = System.currentTimeMillis() - startTime;
            return new ExternalComponentStatus(ExternalComponentStatus.Component.AAI, false,
                    new HttpRequestMetadata(e, duration));
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            return new ExternalComponentStatus(ExternalComponentStatus.Component.AAI, false,
                    new ErrorMetadata(Logging.exceptionToDescription(e), duration));
        }
    }

    @Override
    public String getCloudOwnerByCloudRegionId(String cloudRegionId) {
        return cacheProvider
                .aaiClientCacheFor("getCloudOwnerByCloudRegionId", this::getCloudOwnerByCloudRegionIdNonCached)
                .get(cloudRegionId);
    }


    @Override
    public GetTenantsResponse getHomingDataByVfModule(String vnfInstanceId, String vfModuleId) {

        if (isEmpty(vnfInstanceId)|| isEmpty(vfModuleId)){
            throw new GenericUncheckedException("Failed to retrieve homing data associated to vfModule from A&AI, VNF InstanceId or VF Module Id is missing.");
        }
        Response resp = doAaiGet("network/generic-vnfs/generic-vnf/" + vnfInstanceId +"/vf-modules/vf-module/"+ vfModuleId, false);
        String responseAsString = parseForTenantsByServiceSubscription("vserver",resp.readEntity(String.class));
        if (isEmpty(responseAsString)){
            throw new GenericUncheckedException( String.format("A&AI has no homing data associated to vfModule '%s' of vnf '%s'", vfModuleId, vnfInstanceId));
        }
        else {
            AaiResponse aaiResponse = processAaiResponse(resp, GetTenantsResponse[].class, responseAsString);
            return ((GetTenantsResponse[])aaiResponse.getT())[0];
        }
    }

    @Override
    public void resetCache(String cacheName) {
        cacheProvider.resetCache(cacheName);
    }

    String getCloudOwnerByCloudRegionIdNonCached(String cloudRegionId) {
        String uri = "cloud-infrastructure/cloud-regions?cloud-region-id=" + encodePathSegment(cloudRegionId);

        final CloudRegion.Collection cloudRegionCollection =
                typedAaiGet(Unchecked.toURI(uri), CloudRegion.Collection.class);

        return cloudRegionCollection
                .getCloudRegions().stream()
                .map(CloudRegion::getCloudOwner)
                // from here we assure that the cloud owner is given, and not null
                // and non-empty, and that if more than one cloud-owner is given -
                // it is only a single value.
                // exception is thrown if none or more than a single values are
                // given.
                .filter(StringUtils::isNotEmpty)
                .distinct()
                .reduce((a, b) -> {
                    // will be invoked only if distinct() leaves more than a single element
                    throw new GenericUncheckedException("Conflicting cloud-owner found for " + cloudRegionId + ": '" + a + "' / '" + b + "'");
                })
                .orElseThrow(() -> new GenericUncheckedException("No cloud-owner found for " + cloudRegionId));
    }

    private AaiResponse getTenantsByKey(String key) {
        String[] args = CacheProvider.decompileKey(key);
        String globalCustomerId = safeGetFromArray(args, 0);
        String serviceType = safeGetFromArray(args, 1);
        return getTenantsNonCached(globalCustomerId, serviceType);
    }

    AaiResponse getTenantsNonCached(String globalCustomerId, String serviceType) {
        String url = BUSINESS_CUSTOMERS_CUSTOMER + globalCustomerId + SERVICE_SUBSCRIPTIONS_PATH + serviceType;

        Response resp = doAaiGet(url, false);
        String responseAsString = parseForTenantsByServiceSubscription("tenant",resp.readEntity(String.class));
        if (isEmpty(responseAsString)){
           throw new ParsingGetTenantsResponseFailure(String.format("A&AI has no LCP Region & Tenants associated to subscriber '%s' and service type '%s'", globalCustomerId, serviceType));
        }
        else {
            return processAaiResponse(resp, GetTenantsResponse[].class, responseAsString);
        }
    }

    public static class ParsingGetTenantsResponseFailure extends GenericUncheckedException {

        public ParsingGetTenantsResponseFailure(String message) {
            super(message);
        }
    }

    @NotNull
    private AaiResponse<String> buildAaiResponseForGetTenantsFailure(String errorText) {
        return new AaiResponse<>(null, String.format("{\"statusText\":\"%s\"}", errorText), HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    private static String safeGetFromArray(String[] array, int i) {
        if (i < 0 || i >= array.length) {
            return null;
        } else {
            return array[i];
        }
    }
}
