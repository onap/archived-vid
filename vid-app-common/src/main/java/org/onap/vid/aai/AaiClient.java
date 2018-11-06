package org.onap.vid.aai;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.onap.vid.aai.model.AaiGetAicZone.AicZones;
import org.onap.vid.aai.model.*;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.*;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetServicesRequestModel.GetServicesAAIRespone;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.Relationship;
import org.onap.vid.aai.model.RelationshipData;
import org.onap.vid.aai.model.RelationshipList;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.aai.util.VidObjectMapperType;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.model.probes.ErrorMetadata;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.model.probes.HttpRequestMetadata;
import org.onap.vid.utils.Logging;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.web.util.UriUtils;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**

 * Created by Oren on 7/4/17.
 */
public class AaiClient implements AaiClientInterface {


    public static final String QUERY_FORMAT_RESOURCE = "query?format=resource";
    public static final String SERVICE_SUBSCRIPTIONS_PATH = "/service-subscriptions/service-subscription/";
    public static final String MODEL_INVARIANT_ID = "&model-invariant-id=";
    public static final String QUERY_FORMAT_SIMPLE = "query?format=simple";
    public static final String BUSINESS_CUSTOMER = "/business/customers/customer/";
    public static final String SERVICE_INSTANCE = "/service-instances/service-instance/";
    public static final String BUSINESS_CUSTOMERS_CUSTOMER = "business/customers/customer/";

    protected String fromAppId = "VidAaiController";

    private PortDetailsTranslator portDetailsTranslator;

    private final AAIRestInterface restController;

    /**
     * The logger
     */
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AaiClient.class);

    /**
     * The Constant dateFormat.
     */
    static final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

    static final String GET_SERVICE_MODELS_RESPONSE_BODY = "{\"start\" : \"service-design-and-creation/models/\", \"query\" : \"query/serviceModels-byDistributionStatus?distributionStatus=DISTRIBUTION_COMPLETE_OK\"}";

    @Inject
    public AaiClient(AAIRestInterface restController, PortDetailsTranslator portDetailsTranslator) {
        this.restController = restController;
        this.portDetailsTranslator = portDetailsTranslator;
    }


    private static String checkForNull(String local) {
        if (local != null)
            return local;
        else
            return "";

    }

    @Override
    public AaiResponse getServicesByOwningEntityId(List<String> owningEntityIds){
        Response resp = doAaiGet(getUrlFromLIst("business/owning-entities?", "owning-entity-id=", owningEntityIds), false);
        return processAaiResponse(resp, OwningEntityResponse.class, null);
    }

    @Override
    public AaiResponse getServicesByProjectNames(List<String> projectNames){
        Response resp = doAaiGet(getUrlFromLIst("business/projects?", "project-name=",  projectNames), false);
        return processAaiResponse(resp, ProjectResponse.class, null);
    }

    @Override
    public AaiResponse getServiceModelsByDistributionStatus() {
        Response resp = doAaiPut(QUERY_FORMAT_RESOURCE, GET_SERVICE_MODELS_RESPONSE_BODY, false);
        return processAaiResponse(resp, GetServiceModelsByDistributionStatusResponse.class, null);
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
                "{\"start\": [\"cloud-infrastructure/cloud-regions/cloud-region/" + cloudOwner + "/" + cloudRegionId + "\"]," +
                        "\"query\": \"query/instance-group-byCloudRegion?type=L3-NETWORK&role=SUB-INTERFACE&function=" + networkFunction + "\"}\n", false);
        return processAaiResponse(resp, AaiGetInstanceGroupsByCloudRegion.class, null, VidObjectMapperType.FASTERXML);
    }

    private AaiResponse getNetworkCollectionDetailsResponse(AaiResponse<AaiGetNetworkCollectionDetailsHelper> aaiResponse){
        if(aaiResponse.getHttpCode() == 200) {
            com.fasterxml.jackson.databind.ObjectMapper om = new com.fasterxml.jackson.databind.ObjectMapper();
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
                return new AaiResponse(e.getCause(), "AAI response parsing Error" , aaiResponse.getHttpCode());
            }
            catch (Exception e) {
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
        AaiResponse<AaiGetPortMirroringSourcePorts> aaiResponse = processAaiResponse(resp, AaiGetPortMirroringSourcePorts.class, rawPayload);
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
    public AaiResponse<AaiNodeQueryResponse> searchNodeTypeByName(String name, ResourceType type) {
        String path = String.format(
                "search/nodes-query?search-node-type=%s&filter=%s:EQUALS:%s",
                type.getAaiFormat(),
                type.getNameFilter(),
                name
        );
        return typedAaiGet(path, AaiNodeQueryResponse.class);
    }

    private <T> AaiResponse<T> typedAaiGet(String path, Class<T> clz) {
        Response resp = doAaiGet(path , false);
        return processAaiResponse(resp, clz, null, VidObjectMapperType.FASTERXML);
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
                logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
                logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
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
        return getAllSubscribers(false).getAaiResponse();
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
    public AaiResponse<String> getAicZoneForPnf(String globalCustomerId , String serviceType , String serviceId) {
        String aicZonePath = BUSINESS_CUSTOMERS_CUSTOMER + globalCustomerId + SERVICE_SUBSCRIPTIONS_PATH + serviceType + SERVICE_INSTANCE + serviceId;
        Response resp = doAaiGet(aicZonePath , false);
        AaiResponse<ServiceRelationships> aaiResponse = processAaiResponse(resp , ServiceRelationships.class , null);
        ServiceRelationships serviceRelationships = aaiResponse.getT();
        RelationshipList relationshipList = serviceRelationships.getRelationshipList();
        Relationship relationship = relationshipList.getRelationship().get(0);
        RelationshipData relationshipData=  relationship.getRelationDataList().get(0);
        String aicZone = relationshipData.getRelationshipValue();
        return new AaiResponse(aicZone , null ,HttpStatus.SC_OK);
    }


    @Override
    public AaiResponse getVNFData() {
        String payload = "{\"start\": [\"/business/customers/customer/e433710f-9217-458d-a79d-1c7aff376d89/service-subscriptions/service-subscription/VIRTUAL%20USP/service-instances/service-instance/3f93c7cb-2fd0-4557-9514-e189b7b04f9d\"],	\"query\": \"query/vnf-topology-fromServiceInstance\"}";
        Response resp = doAaiPut(QUERY_FORMAT_SIMPLE, payload, false);
        return processAaiResponse(resp, AaiGetVnfResponse.class, null);
    }

    @Override
    public Response getVNFData(String globalSubscriberId, String serviceType) {
        String payload = "{\"start\": [\"business/customers/customer/" + globalSubscriberId + SERVICE_SUBSCRIPTIONS_PATH + encodePathSegment(serviceType) +"/service-instances\"]," +
                "\"query\": \"query/vnf-topology-fromServiceInstance\"}";
        return doAaiPut(QUERY_FORMAT_SIMPLE, payload, false);
    }

    @Override
    public AaiResponse getVNFData(String globalSubscriberId, String serviceType, String serviceInstanceId) {
        String payload = "{\"start\": [\"/business/customers/customer/" + globalSubscriberId + SERVICE_SUBSCRIPTIONS_PATH + encodePathSegment(serviceType) + SERVICE_INSTANCE + serviceInstanceId + "\"],	\"query\": \"query/vnf-topology-fromServiceInstance\"}";
        Response resp = doAaiPut(QUERY_FORMAT_SIMPLE, payload, false);
        return processAaiResponse(resp, AaiGetVnfResponse.class, null);
    }

    @Override
    public Response getVersionByInvariantId(List<String> modelInvariantId) {
        StringBuilder sb = new StringBuilder();
        for (String id : modelInvariantId){
            sb.append(MODEL_INVARIANT_ID);
            sb.append(id);

        }
        return doAaiGet("service-design-and-creation/models?depth=2"+ sb.toString(), false);
    }

    @Override
    public AaiResponse getSubscriberData(String subscriberId) {
        String depth = "2";
        AaiResponse subscriberDataResponse;
        Response resp = doAaiGet(BUSINESS_CUSTOMERS_CUSTOMER + subscriberId + "?depth=" + depth, false);
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
        AaiResponse aaiResponse;

        if ((globalCustomerId == null || globalCustomerId.isEmpty()) || ((serviceType == null) || (serviceType.isEmpty())) ){
            aaiResponse = new AaiResponse<>(null, "{\"statusText\":\" Failed to retrieve LCP Region & Tenants from A&AI, Subscriber ID or Service Type is missing.\"}", HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return  aaiResponse;
        }

        String url = BUSINESS_CUSTOMERS_CUSTOMER + globalCustomerId + SERVICE_SUBSCRIPTIONS_PATH + serviceType;

        Response resp = doAaiGet(url, false);
        String responseAsString = parseForTenantsByServiceSubscription(resp.readEntity(String.class));
        if (responseAsString.equals("")){
            return new AaiResponse<>(null, String.format("{\"statusText\":\" A&AI has no LCP Region & Tenants associated to subscriber '%s' and service type '%s'\"}", globalCustomerId, serviceType), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
        else {
            return processAaiResponse(resp, GetTenantsResponse[].class, responseAsString);
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
        String responseBody = null;
        Integer responseHttpCode = null;
        try {
            Response response = responseWithRequestInfo.getResponse();
            responseHttpCode = (response != null) ? response.getStatus() : null;
            responseBody = (response != null) ? response.readEntity(String.class) : null;
            AaiResponse<T> processedAaiResponse = processAaiResponse(response, classType, responseBody, VidObjectMapperType.CODEHAUS, propagateExceptions);
            return new AaiResponseWithRequestInfo<>(responseWithRequestInfo.getRequestHttpMethod(), responseWithRequestInfo.getRequestUrl(), processedAaiResponse,
                    responseBody);
        } catch (Exception e) {
            throw new ExceptionWithRequestInfo(responseWithRequestInfo.getRequestHttpMethod(),
                    responseWithRequestInfo.getRequestUrl(), responseBody, responseHttpCode, e);
        }
    }

    private AaiResponse processAaiResponse(Response resp, Class classType, String responseBody) {
        return processAaiResponse(resp, classType, responseBody, VidObjectMapperType.CODEHAUS);
    }

    private AaiResponse processAaiResponse(Response resp, Class classType, String responseBody, VidObjectMapperType omType) {
        return processAaiResponse(resp, classType, responseBody, omType, false);
    }

    private AaiResponse processAaiResponse(Response resp, Class classType, String responseBody, VidObjectMapperType omType, boolean propagateExceptions) {
        AaiResponse subscriberDataResponse;
        if (resp == null) {
            subscriberDataResponse = new AaiResponse<>(null, null, HttpStatus.SC_INTERNAL_SERVER_ERROR);
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "Invalid response from AAI");
        } else {
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "getSubscribers() resp=" + resp.getStatusInfo().toString());
            if (resp.getStatus() != HttpStatus.SC_OK) {
                subscriberDataResponse = processFailureResponse(resp,responseBody);
            } else {
                subscriberDataResponse = processOkResponse(resp, classType, responseBody, omType, propagateExceptions);
            }
        }
        return subscriberDataResponse;
    }

    private AaiResponse processFailureResponse(Response resp, String responseBody) {
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "Invalid response from AAI");
        String rawData;
        if (responseBody != null) {
            rawData = responseBody;
        } else {
            rawData = resp.readEntity(String.class);
        }
        return new AaiResponse<>(null, rawData, resp.getStatus());
    }

    private AaiResponse processOkResponse(Response resp, Class classType, String responseBody, VidObjectMapperType omType, boolean propagateExceptions) {
        AaiResponse subscriberDataResponse;
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

    private AaiResponse parseFasterXmlObject(Class classType, String finalResponse) throws IOException {
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        return new AaiResponse<>((objectMapper.readValue(finalResponse, classType)), null, HttpStatus.SC_OK);
    }

    private AaiResponse parseCodeHausObject(Class classType, String finalResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return new AaiResponse<>((objectMapper.readValue(finalResponse, classType)), null, HttpStatus.SC_OK);
    }

    public Response doAaiGet(String uri, boolean xml) {
        return doAaiGet(uri, xml, false).getResponse();
    }


    public ResponseWithRequestInfo doAaiGet(String uri, boolean xml, boolean propagateExceptions) {
        String methodName = "doAaiGet";
        String transId = UUID.randomUUID().toString();
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        ResponseWithRequestInfo resp;
        try {
            resp = restController.RestGet(fromAppId, transId, uri, xml, propagateExceptions);

        } catch (Exception e) {
            if (propagateExceptions) {
                throw (e instanceof RuntimeException) ? (RuntimeException)e : new GenericUncheckedException(e);
            } else {
                final Exception actual =
                        e instanceof ExceptionWithRequestInfo ? (Exception) e.getCause() : e;

                final String message =
                        actual instanceof WebApplicationException ? ((WebApplicationException) actual).getResponse().readEntity(String.class) : e.toString();

                //ToDo: change parameter of requestUrl to real url from RestGet function
                resp = new ResponseWithRequestInfo(null, null, org.springframework.http.HttpMethod.GET);
                logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + message);
                logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + message);
            }
        }
        return resp;
    }

    private String parseForTenantsByServiceSubscription(String resp) {
        String tenantList = "";

        try {
            JSONParser jsonParser = new JSONParser();

            JSONObject jsonObject = (JSONObject) jsonParser.parse(resp);

            return parseServiceSubscriptionObjectForTenants(jsonObject);
        } catch (Exception ex) {
            logger.debug(EELFLoggerDelegate.debugLogger, "parseForTenantsByServiceSubscription error while parsing tenants by service subscription", ex);
        }
        return tenantList;
    }

    protected Response doAaiPut(String uri, String payload, boolean xml) {
        String methodName = "doAaiPut";
        String transId = UUID.randomUUID().toString();
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        Response resp = null;
        try {

            resp = restController.RestPut(fromAppId, uri, payload, xml);

        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
        }
        return resp;
    }


    private String parseServiceSubscriptionObjectForTenants(JSONObject jsonObject) {
        JSONArray tenantArray = new JSONArray();
        boolean bconvert = false;
        try {
            JSONObject relationShipListsObj = (JSONObject) jsonObject.get("relationship-list");
            if (relationShipListsObj != null) {
                JSONArray rShipArray = (JSONArray) relationShipListsObj.get("relationship");
                for (Object innerObj : defaultIfNull(rShipArray, emptyList())) {
                    if (innerObj != null) {
                        bconvert = parseTenant(tenantArray, bconvert, (JSONObject) innerObj);
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

    private static boolean parseTenant(JSONArray tenantArray, boolean bconvert, JSONObject inner1Obj) {
        String relatedTo = checkForNull((String) inner1Obj.get("related-to"));
        if (relatedTo.equalsIgnoreCase("tenant")) {
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
        if (propKey.equalsIgnoreCase("tenant.tenant-name")) {
            tenantNewObj.put("tenantName", propVal);
        }
    }

    private static void parseRelationShip(JSONObject tenantNewObj, JSONObject inner2Obj) {
        if (inner2Obj == null)
            return;

        String rShipKey = checkForNull((String) inner2Obj.get("relationship-key"));
        String rShipVal = checkForNull((String) inner2Obj.get("relationship-value"));
        if (rShipKey.equalsIgnoreCase("cloud-region.cloud-owner")) {
            tenantNewObj.put("cloudOwner", rShipVal);
        } else if (rShipKey.equalsIgnoreCase("cloud-region.cloud-region-id")) {
            tenantNewObj.put("cloudRegionID", rShipVal);
        }

        if (rShipKey.equalsIgnoreCase("tenant.tenant-id")) {
            tenantNewObj.put("tenantID", rShipVal);
        }
    }

    private static String encodePathSegment(String segmentToEncode) {
        try {
            return UriUtils.encodePathSegment(segmentToEncode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new GenericUncheckedException("URI encoding failed unexpectedly", e);
        }
    }

    @Override
    public ExternalComponentStatus probeAaiGetAllSubscribers(){
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
                    StringUtils.substring(responseWithRequestInfo.getRawData(), 0, 500),
                    isAvailable ? "OK" : "No subscriber received",
                    duration
            );
            return new ExternalComponentStatus(ExternalComponentStatus.Component.AAI, isAvailable, metadata);

        } catch (ExceptionWithRequestInfo e) {
            long duration = System.currentTimeMillis() - startTime;
            return new ExternalComponentStatus(ExternalComponentStatus.Component.AAI, false,
                    new HttpRequestMetadata(
                            e.getHttpMethod(),
                            defaultIfNull(e.getHttpCode(), 0),
                            e.getRequestedUrl(),
                            e.getRawData(),
                            Logging.exceptionToDescription(e.getCause()), duration));
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            return new ExternalComponentStatus(ExternalComponentStatus.Component.AAI, false,
                    new ErrorMetadata(Logging.exceptionToDescription(e), duration));
        }
    }
}