package org.openecomp.vid.aai;

import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.ecomp.aai.model.AaiAICZones.AicZones;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openecomp.aai.util.AAIRestInterface;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.openecomp.vid.aai.model.Relationship;
import org.openecomp.vid.aai.model.RelationshipData;
import org.openecomp.vid.aai.model.RelationshipList;

import org.openecomp.vid.aai.model.OwningEntityResponse;
import org.openecomp.vid.aai.model.Project;
import org.openecomp.vid.aai.model.ProjectResponse;
import org.openecomp.vid.aai.model.Relationship;
import org.openecomp.vid.aai.model.RelationshipData;
import org.openecomp.vid.aai.model.RelationshipList;
import org.openecomp.vid.aai.model.ServiceRelationships;
import org.openecomp.vid.aai.model.AaiGetServicesRequestModel.GetServicesAAIRespone;
import org.openecomp.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.openecomp.vid.model.SubscriberList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriUtils;


import javax.servlet.ServletContext;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by Oren on 7/4/17.
 */
public class AaiClient implements AaiClientInterface {

    /**
     * The Constant dateFormat.
     */
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
    protected String fromAppId = "VidAaiController";
    @Autowired
    ServletContext servletContext;
    /**
     * The logger
     */

    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AaiClient.class);


    public AaiClient() {
        //        certiPath = getCertificatesFile().getAbsolutePath();
        //        depth = "0";
    }

    public AaiClient(ServletContext context) {
        servletContext = context;
    }


    private static String checkForNull(String local) {
        if (local != null)
            return local;
        else
            return "";

    }

    @Override
    public AaiResponse getServicesByOwningEntityId(List<String> owningEntityIds){
        File certiPath = getCertificatesFile();
        Response resp = doAaiGet(certiPath.getAbsolutePath(), getUrlFromLIst("business/owning-entities?", "owning-entity-id=", owningEntityIds), false);
        AaiResponse aaiResponse = proccessAaiResponse(resp, OwningEntityResponse.class, null);

        return aaiResponse;
    }

    @Override
    public AaiResponse getServicesByProjectNames(List<String> projectNames){
        File certiPath = getCertificatesFile();
        Response resp = doAaiGet(certiPath.getAbsolutePath(), getUrlFromLIst("business/projects?", "project-name=", projectNames), false);
        AaiResponse aaiResponse = proccessAaiResponse(resp, ProjectResponse.class, null);

        return aaiResponse;
    }

    private String getUrlFromLIst(String url, String paramKey, List<String> params){
        url.concat(paramKey);
        int i = 0;
        for(String param: params){
            i ++;
            url = url.concat(paramKey);
            url = url.concat(param);
            if(i != params.size()){
                url = url.concat("&");
            }
        }
        return url;
    }


    @Override
    public AaiResponse<SubscriberList> getAllSubscribers() {
        String certiPath = getCertificatesFile().getAbsolutePath();
        String depth = "0";
        Response resp = doAaiGet(certiPath, "business/customers?subscriber-type=INFRA&depth=" + depth, false);
        return proccessAaiResponse(resp, SubscriberList.class, null);
    }


    @Override
    public AaiResponse getAllAicZones() {
        String certiPath = getCertificatesFile().getAbsolutePath();
        Response resp = doAaiGet(certiPath, "network/zones", false);
        AaiResponse aaiAicZones = proccessAaiResponse(resp, AicZones.class, null);
        return aaiAicZones;
    }


	@Override
	public AaiResponse<String> getAicZoneForPnf(String globalCustomerId , String serviceType , String serviceId) {
		String certiPath = getCertificatesFile().getAbsolutePath();
		String aicZonePath = "business/customers/customer/" + globalCustomerId + "/service-subscriptions/service-subscription/" + serviceType + "/service-instances/service-instance/" + serviceId;
		Response resp = doAaiGet(certiPath , aicZonePath , false);
		AaiResponse<ServiceRelationships> aaiResponse = proccessAaiResponse(resp , ServiceRelationships.class , null);
		ServiceRelationships serviceRelationships = (ServiceRelationships)aaiResponse.getT();
        RelationshipList relationshipList = serviceRelationships.getRelationshipList();
        Relationship relationship = relationshipList.getRelationship().get(0);
		RelationshipData relationshipData=  relationship.getRelationDataList().get(0);
        String aicZone = relationshipData.getRelationshipValue();
	    AaiResponse<String> aaiAicZonaForPnfResponse = new AaiResponse(aicZone , null ,HttpStatus.SC_OK);
		return  aaiAicZonaForPnfResponse;
	}


    @Override
    public AaiResponse getVNFData() {
        String certiPath = getCertificatesFile().getAbsolutePath();
        String payload = "{\"start\": [\"/business/customers/customer/e433710f-9217-458d-a79d-1c7aff376d89/service-subscriptions/service-subscription/VIRTUAL%20USP/service-instances/service-instance/3f93c7cb-2fd0-4557-9514-e189b7b04f9d\"],	\"query\": \"query/vnf-topology-fromServiceInstance\"}";
        Response resp = doAaiPut(certiPath, "query?format=simple", payload, false);
        return proccessAaiResponse(resp, AaiGetVnfResponse.class, null);

    }

    @Override
    public Response getVNFData(String globalSubscriberId, String serviceType) {
        String certiPath = getCertificatesFile().getAbsolutePath();
        String payload = "{\"start\": [\"business/customers/customer/" + globalSubscriberId + "/service-subscriptions/service-subscription/"+ serviceType +"/service-instances\"]," +
                "\"query\": \"query/vnf-topology-fromServiceInstance\"}";
        return doAaiPut(certiPath, "query?format=simple", payload, false);

    }

    @Override
    public AaiResponse getVNFData(String globalSubscriberId, String serviceType, String serviceInstanceId) {
        String certiPath = getCertificatesFile().getAbsolutePath();
        String payload = "{\"start\": [\"/business/customers/customer/" + globalSubscriberId + "/service-subscriptions/service-subscription/" + encodePathSegment(serviceType) + "/service-instances/service-instance/" + serviceInstanceId + "\"],	\"query\": \"query/vnf-topology-fromServiceInstance\"}";
        Response resp = doAaiPut(certiPath, "query?format=simple", payload, false);
        return proccessAaiResponse(resp, AaiGetVnfResponse.class, null);
    }

    @Override
    public Response getVersionByInvariantId(List<String> modelInvariantId) {
        File certiPath = getCertificatesFile();
        StringBuilder sb = new StringBuilder();
        for (String id : modelInvariantId){
            sb.append("&model-invariant-id=");
            sb.append(id);

        }
        Response resp = doAaiGet(certiPath.getAbsolutePath(), "service-design-and-creation/models?depth=2"+ sb.toString(), false);
        return resp;
    }

    @Override
    public AaiResponse getSubscriberData(String subscriberId) {
        File certiPath = getCertificatesFile();
        String depth = "2";
        AaiResponse subscriberDataResponse;
        Response resp = doAaiGet(certiPath.getAbsolutePath(), "business/customers/customer/" + subscriberId + "?depth=" + depth, false);
        subscriberDataResponse = proccessAaiResponse(resp, Services.class, null);
        return subscriberDataResponse;
    }

    @Override
    public AaiResponse getServices() {
        File certiPath = getCertificatesFile();
        Response resp = doAaiGet(certiPath.getAbsolutePath(), "service-design-and-creation/services", false);
        AaiResponse<GetServicesAAIRespone> getServicesResponse = proccessAaiResponse(resp, GetServicesAAIRespone.class, null);

        return getServicesResponse;
    }

    @Override
    public AaiResponse getOperationalEnvironments(String operationalEnvironmentType, String operationalEnvironmentStatus) {
        File certiPath = getCertificatesFile();
        String url = "aai/cloud-infrastructure/operational-environments";
        URIBuilder urlBuilder  = new URIBuilder();
        if (operationalEnvironmentType != null)
            urlBuilder.addParameter("operational-environment-type", operationalEnvironmentType);
        if (operationalEnvironmentStatus != null)
            urlBuilder.addParameter("operational-environment-status", operationalEnvironmentStatus);
        url += urlBuilder.toString();
        Response resp = doAaiGet(certiPath.getAbsolutePath(), url, false);
        AaiResponse<OperationalEnvironmentList> getOperationalEnvironmentsResponse = proccessAaiResponse(resp, OperationalEnvironmentList.class, null);
            return getOperationalEnvironmentsResponse;

    }

    @Override
    public AaiResponse getTenants(String globalCustomerId, String serviceType) {
        File certiPath = getCertificatesFile();
        String url = "business/customers/customer/" + globalCustomerId + "/service-subscriptions/service-subscription/" + serviceType;

        Response resp = doAaiGet(certiPath.getAbsolutePath(), url, false);
        String responseAsString = parseForTenantsByServiceSubscription(resp.readEntity(String.class));
        if (responseAsString.equals("")){
            AaiResponse aaiResponse = new AaiResponse<>(null, String.format("{\"statusText\":\" A&AI has no LCP Region & Tenants associated to subscriber '%s' and service type '%s'\"}", globalCustomerId, serviceType), HttpStatus.SC_INTERNAL_SERVER_ERROR);
            return  aaiResponse;
        }
        else {
                AaiResponse<GetTenantsResponse[]> getTenantsResponse = proccessAaiResponse(resp, GetTenantsResponse[].class, responseAsString);
                return getTenantsResponse;
        }

    }

    @Override
    public AaiResponse getNodeTemplateInstances(String globalCustomerId, String serviceType, String modelVersionId, String modelInvariantId, String cloudRegion) {

        String certiPath = getCertificatesFile().getAbsolutePath();

        String payload1 = "{\"start\": [\"/business/customers/customer/" + globalCustomerId +
                "/service-subscriptions/service-subscription/" + serviceType +
                "/service-instances?model-version-id=" + modelVersionId +
                "&model-invariant-id="+modelInvariantId + "\"],	" +
                "\"query\": \"query/vnfFromModelbyRegion?cloudRegionId="+cloudRegion+"\"}";

        Response resp1 = doAaiPut(certiPath, "query?format=simple", payload1, false);
        AaiResponse aaiResponse1 = proccessAaiResponse(resp1, AaiGetVnfResponse.class, null);

        if (aaiResponse1.getHttpCode() != HttpStatus.SC_OK) {
            // return error
            return aaiResponse1;
        }

        AaiGetVnfResponse response1T = (AaiGetVnfResponse) aaiResponse1.getT();
        if (response1T.results.size() > 0) {

            String payload2 = "{\"start\": [\"" +
                    "/network/generic-vnfs\"],	" +
                    "\"query\": \"query/vnfFromModelbyRegion?cloudRegionId=" + cloudRegion + "\"}";

            Response resp2 = doAaiPut(certiPath, "query?format=simple", payload2, false);
            AaiResponse aaiResponse2 = proccessAaiResponse(resp2, AaiGetVnfResponse.class, null);

            if (aaiResponse2.getHttpCode() != HttpStatus.SC_OK) {
                // return error
                return aaiResponse2;
            }

            AaiGetVnfResponse response2T = (AaiGetVnfResponse) aaiResponse2.getT();
            Map<String, VnfResult> genericVnfResultMap = response2T.results.stream().collect(Collectors.toMap(vnf -> vnf.id, vnf -> vnf));

            List<VnfResult> filteredGenericVnfResultMap = response1T.results.stream()
                    .flatMap(vnf -> vnf.relatedTo.stream())          // check all the 'related-to'
                    .filter(o -> "generic-vnf".equals(o.nodeType))
                    .map(o -> o.id)                                  // take only the 'id' of any 'generic vnf'
                    .filter(genericVnfResultMap::containsKey)        // lookup for the id in the whole list of vnfs
                    .map(genericVnfResultMap::get)
                    .collect(Collectors.toList());

            return new AaiResponse<>(ImmutableMap.of("results", filteredGenericVnfResultMap), null, HttpStatus.SC_OK);
        } else {
            // empty list as result
            return new AaiResponse<>(ImmutableMap.of("results", new VnfResult[]{}), null, HttpStatus.SC_OK);
        }
    }

    private AaiResponse proccessAaiResponse(Response resp, Class classType, String responseBody) {
        AaiResponse subscriberDataResponse = null;
        if (resp == null) {
            subscriberDataResponse = new AaiResponse<>(null, null, HttpStatus.SC_INTERNAL_SERVER_ERROR);
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "Invalid response from AAI");
        } else {
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "getSubscribers() resp=" + resp.getStatusInfo().toString());
            if (resp.getStatus() != HttpStatus.SC_OK) {
                logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "Invalid response from AAI");
                subscriberDataResponse = new AaiResponse<>(null, resp.readEntity(String.class), resp.getStatus());
            } else {
                String finalResponse;
                try {
                    if (responseBody != null) {
                        finalResponse = responseBody;
                    } else {
                        finalResponse = resp.readEntity(String.class);
                    }

                    subscriberDataResponse = new AaiResponse<>((new ObjectMapper().readValue(finalResponse, classType)), null, HttpStatus.SC_OK);

                } catch (IOException e) {
                    subscriberDataResponse = new AaiResponse<>(null, null, HttpStatus.SC_INTERNAL_SERVER_ERROR);
                }
                catch(Exception e){
                	e.printStackTrace();
                }

            }
        }
        return subscriberDataResponse;
    }

    private File getCertificatesFile() {
        if (servletContext != null)
            return new File(servletContext.getRealPath("/WEB-INF/cert/"));
        return null;
    }

    @SuppressWarnings("all")
    protected Response doAaiGet(String certiPath, String uri, boolean xml) {
        String methodName = "doAaiGet";
        String transId = UUID.randomUUID().toString();
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        Response resp = null;
        try {

            AAIRestInterface restContrller = new AAIRestInterface(certiPath);
            resp = restContrller.RestGet(fromAppId, transId, uri, xml);

        } catch (WebApplicationException e) {
            final String message = ((BadRequestException) e).getResponse().readEntity(String.class);
            logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + message);
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + message);
        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
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

        }

        return tenantList;
    }

    protected Response doAaiPut(String certiPath, String uri, String payload, boolean xml) {
        String methodName = "doAaiPut";
        String transId = UUID.randomUUID().toString();
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        Response resp = null;
        try {

            AAIRestInterface restContrller = new AAIRestInterface(certiPath);
            resp = restContrller.RestPut(fromAppId, transId, uri, payload, xml);

        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
        }

        return resp;
    }


    public static String parseServiceSubscriptionObjectForTenants(JSONObject jsonObject) {

        JSONArray tenantArray = new JSONArray();
        boolean bconvert = false;

        try {
            JSONObject relationShipListsObj = (JSONObject) jsonObject.get("relationship-list");
            if (relationShipListsObj != null) {
                JSONArray rShipArray = (JSONArray) relationShipListsObj.get("relationship");
                if (rShipArray != null) {
                    Iterator i1 = rShipArray.iterator();

                    while (i1.hasNext()) {

                        JSONObject inner1Obj = (JSONObject) i1.next();

                        if (inner1Obj == null)
                            continue;

                        String relatedTo = checkForNull((String) inner1Obj.get("related-to"));
                        if (relatedTo.equalsIgnoreCase("tenant")) {
                            JSONObject tenantNewObj = new JSONObject();

                            String relatedLink = checkForNull((String) inner1Obj.get("related-link"));
                            tenantNewObj.put("link", relatedLink);

                            JSONArray rDataArray = (JSONArray) inner1Obj.get("relationship-data");
                            if (rDataArray != null) {
                                Iterator i2 = rDataArray.iterator();

                                while (i2.hasNext()) {
                                    JSONObject inner2Obj = (JSONObject) i2.next();

                                    if (inner2Obj == null)
                                        continue;

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
                            }

                            JSONArray relatedTPropArray = (JSONArray) inner1Obj.get("related-to-property");
                            if (relatedTPropArray != null) {
                                Iterator i3 = relatedTPropArray.iterator();

                                while (i3.hasNext()) {
                                    JSONObject inner3Obj = (JSONObject) i3.next();

                                    if (inner3Obj == null)
                                        continue;

                                    String propKey = checkForNull((String) inner3Obj.get("property-key"));
                                    String propVal = checkForNull((String) inner3Obj.get("property-value"));
                                    if (propKey.equalsIgnoreCase("tenant.tenant-name")) {
                                        tenantNewObj.put("tenantName", propVal);
                                    }
                                }
                            }
                            bconvert = true;
                            tenantArray.add(tenantNewObj);
                        }
                    }

                }
            }
        } catch (NullPointerException ex) {


        }

        if (bconvert)
            return tenantArray.toJSONString();
        else
            return "";

    }

    private static String encodePathSegment(String serviceType) {
        try {
            return UriUtils.encodePathSegment(serviceType, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URI encoding failed unexpectedly", e);
        }
    }

}