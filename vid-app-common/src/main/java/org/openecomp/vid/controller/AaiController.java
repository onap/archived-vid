/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

package org.openecomp.vid.controller;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openecomp.aai.util.AAIRestInterface;
import org.openecomp.portalsdk.core.controller.RestrictedBaseController;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.vid.aai.AaiResponse;
import org.openecomp.vid.aai.ServiceInstancesSearchResults;
import org.openecomp.vid.aai.SubscriberData;
import org.openecomp.vid.aai.SubscriberFilteredResults;
import org.openecomp.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.openecomp.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.openecomp.vid.model.VersionByInvariantIdsRequest;
import org.openecomp.vid.roles.Role;
import org.openecomp.vid.roles.RoleProvider;
import org.openecomp.vid.roles.RoleValidator;
import org.openecomp.vid.services.AaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.openecomp.vid.utils.Logging.getMethodName;

/**
 * Controller to handle a&ai requests.
 */

@RestController
public class AaiController extends RestrictedBaseController {
    /**
     * The Constant dateFormat.
     */
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
    /**
     * The from app id.
     */
    protected String fromAppId = "VidAaiController";
    /**
     * The view name.
     */
    String viewName;
    /**
     * The logger.
     */
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AaiController.class);
    /**
     * The model.
     */
    private Map<String, Object> model = new HashMap<String, Object>();
    /**
     * The servlet context.
     */
    @Autowired
    private ServletContext servletContext;
    /**
     * aai service
     */
    @Autowired
    private AaiService aaiService;
    @Autowired
    private RoleProvider roleProvider;

    public AaiController() {

    }

    public AaiController(ServletContext servletContext) {
        this.servletContext = servletContext;

    }

    /**
     * Return tenant details.
     *
     * @param jsonObject the json object
     * @return String The parsing results
     */
    public static String parseCustomerObjectForTenants(JSONObject jsonObject) {

        JSONArray tenantArray = new JSONArray();
        boolean bconvert = false;

        try {

            JSONObject serviceSubsObj = (JSONObject) jsonObject.get("service-subscriptions");

            if (serviceSubsObj != null) {
                JSONArray srvcSubArray = (JSONArray) serviceSubsObj.get("service-subscription");

                if (srvcSubArray != null) {
                    Iterator i = srvcSubArray.iterator();

                    while (i.hasNext()) {

                        JSONObject innerObj = (JSONObject) i.next();

                        if (innerObj == null)
                            continue;

                        JSONObject relationShipListsObj = (JSONObject) innerObj.get("relationship-list");
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


    /**
     * Retrieve the service subscription from the jsonObject.
     *
     * @param jsonObject the json object
     * @return String
     */
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

    /**
     * Check for null.
     *
     * @param local the local
     * @return the string
     */
    private static String checkForNull(String local) {
        if (local != null)
            return local;
        else
            return "";

    }

    /**
     * Welcome method.
     *
     * @param request the request
     * @return ModelAndView The view
     */
    @RequestMapping(value = {"/subscriberSearch"}, method = RequestMethod.GET)
    public ModelAndView welcome(HttpServletRequest request) {
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== AaiController welcome start");
        return new ModelAndView(getViewName());
    }

    @RequestMapping(value = {"/aai_get_aic_zones"}, method = RequestMethod.GET)
    public ResponseEntity<String> getAicZones(HttpServletRequest request) throws JsonGenerationException, JsonMappingException, IOException {
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== getAicZones controller start");
        AaiResponse response = aaiService.getAaiZones();
        return aaiResponseToResponseEntity(response);
    }

    @RequestMapping(value = {"/aai_get_aic_zone_for_pnf/{globalCustomerId}/{serviceType}/{serviceId}"}, method = RequestMethod.GET)
    public ResponseEntity<String> getAicZoneForPnf(@PathVariable("globalCustomerId") String globalCustomerId ,@PathVariable("serviceType") String serviceType , @PathVariable("serviceId") String serviceId ,HttpServletRequest request) throws JsonGenerationException, JsonMappingException, IOException {
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== getAicZoneForPnf controller start");
        AaiResponse response = aaiService.getAicZoneForPnf(globalCustomerId , serviceType , serviceId);
        return aaiResponseToResponseEntity(response);
    }

    /* (non-Javadoc)
     * @see org.openecomp.portalsdk.core.controller.RestrictedBaseController#getViewName()
     */
    public String getViewName() {
        return viewName;
    }

    /* (non-Javadoc)
     * @see org.openecomp.portalsdk.core.controller.RestrictedBaseController#setViewName(java.lang.String)
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    /**
     * Get services from a&ai.
     *
     * @return ResponseEntity<String> The response entity with the logged in user uuid.
     * @throws IOException          Signals that an I/O exception has occurred.
     * @throws InterruptedException the interrupted exception
     */
    @RequestMapping(value = {"/getuserID"}, method = RequestMethod.GET)
    public ResponseEntity<String> getUserID(HttpServletRequest request) throws IOException, InterruptedException {

        String userId = ControllersUtils.extractUserId(request);

        return new ResponseEntity<String>(userId, HttpStatus.OK);
    }

    /**
     * Get services from a&ai.
     *
     * @return ResponseEntity<String> The response entity
     * @throws IOException          Signals that an I/O exception has occurred.
     * @throws InterruptedException the interrupted exception
     */
    @RequestMapping(value = "/aai_get_services", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doGetServices(HttpServletRequest request) throws IOException, InterruptedException {
        RoleValidator roleValidator = new RoleValidator(roleProvider.getUserRoles(request));

        AaiResponse subscriberList = aaiService.getServices(roleValidator);
        ResponseEntity<String> responseEntity = aaiResponseToResponseEntity(subscriberList);

        return responseEntity;
    }


    @RequestMapping(value = {"/aai_get_version_by_invariant_id"}, method = RequestMethod.POST)
    public ResponseEntity<String> getVersionByInvariantId(HttpServletRequest request, @RequestBody VersionByInvariantIdsRequest versions) throws IOException {
        ResponseEntity<String> responseEntity;
        ObjectMapper objectMapper = new ObjectMapper();

        Response result = aaiService.getVersionByInvariantId(versions.versions);

        return new ResponseEntity<String>(result.readEntity(String.class), HttpStatus.OK);
    }


    private ResponseEntity<String> aaiResponseToResponseEntity(AaiResponse aaiResponseData)
            throws IOException, JsonGenerationException, JsonMappingException {
        ResponseEntity<String> responseEntity;
        ObjectMapper objectMapper = new ObjectMapper();
        if (aaiResponseData.getHttpCode() == 200) {
            responseEntity = new ResponseEntity<String>(objectMapper.writeValueAsString(aaiResponseData.getT()), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<String>(aaiResponseData.getErrorMessage(), HttpStatus.valueOf(aaiResponseData.getHttpCode()));
        }
        return responseEntity;
    }

    /**
     * Lookup single service instance in a&ai.  Get the service-subscription and customer, too, i guess?
     *
     * @param serviceInstanceId the service instance Id
     * @return ResponseEntity The response entity
     * @throws IOException          Signals that an I/O exception has occurred.
     * @throws InterruptedException the interrupted exception
     */
    @RequestMapping(value = "/aai_get_service_instance/{service-instance-id}/{service-instance-type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doGetServiceInstance(@PathVariable("service-instance-id") String serviceInstanceId, @PathVariable("service-instance-type") String serviceInstanceType) throws IOException, InterruptedException {
        File certiPath = GetCertificatesPath();
        Response resp = null;

        if (serviceInstanceType.equalsIgnoreCase("Service Instance Id")) {
            resp = doAaiGet(certiPath.getAbsolutePath(),
                    "search/nodes-query?search-node-type=service-instance&filter=service-instance-id:EQUALS:"
                            + serviceInstanceId, false);
        } else {
            resp = doAaiGet(certiPath.getAbsolutePath(),
                    "search/nodes-query?search-node-type=service-instance&filter=service-instance-name:EQUALS:"
                            + serviceInstanceId, false);
        }
        return convertResponseToResponseEntity(resp);
    }

    /**
     * Get services from a&ai.
     *
     * @param globalCustomerId      the global customer id
     * @param serviceSubscriptionId the service subscription id
     * @return ResponseEntity The response entity
     * @throws IOException          Signals that an I/O exception has occurred.
     * @throws InterruptedException the interrupted exception
     */
    @RequestMapping(value = "/aai_get_service_subscription/{global-customer-id}/{service-subscription-id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doGetServices(@PathVariable("global-customer-id") String globalCustomerId,
                                                @PathVariable("service-subscription-id") String serviceSubscriptionId) throws IOException, InterruptedException {
        File certiPath = GetCertificatesPath();
        Response resp = doAaiGet(certiPath.getAbsolutePath(), "business/customers/customer/" + globalCustomerId
                + "/service-subscriptions/service-subscription/" + serviceSubscriptionId + "?depth=0", false);
        return convertResponseToResponseEntity(resp);
    }

    /**
     * Obtain the subscriber list from a&ai.
     *
     * @param fullSet the full set
     * @return ResponseEntity The response entity
     * @throws IOException          Signals that an I/O exception has occurred.
     * @throws InterruptedException the interrupted exception
     */
    @RequestMapping(value = "/aai_get_subscribers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doGetSubscriberList(HttpServletRequest request, @DefaultValue("n") @QueryParam("fullSet") String fullSet) throws IOException, InterruptedException {
        return getFullSubscriberList(request);
    }

    /**
     * Obtain the Target Prov Status from the System.Properties file.
     *
     * @return ResponseEntity The response entity
     * @throws IOException          Signals that an I/O exception has occurred.
     * @throws InterruptedException the interrupted exception
     */
    @RequestMapping(value = "/get_system_prop_vnf_prov_status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTargetProvStatus() throws IOException, InterruptedException {
        String p = SystemProperties.getProperty("aai.vnf.provstatus");
        return new ResponseEntity<String>(p, HttpStatus.OK);
    }


    /**
     * Obtain the Target Prov Status from the System.Properties file.
     *
     * @return ResponseEntity The response entity
     * @throws IOException          Signals that an I/O exception has occurred.
     * @throws InterruptedException the interrupted exception
     */
    @RequestMapping(value = "/get_operational_environments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public AaiResponse<OperationalEnvironmentList> getOperationalEnvironments(@RequestParam(value="operationalEnvironmentType", required = false) String operationalEnvironmentType,
                                                           @RequestParam(value="operationalEnvironmentStatus", required = false) String operationalEnvironmentStatus) throws IOException, InterruptedException {
       logger.debug(EELFLoggerDelegate.debugLogger, "start {}({}, {})", getMethodName(), operationalEnvironmentType, operationalEnvironmentStatus);
       AaiResponse<OperationalEnvironmentList> response = aaiService.getOperationalEnvironments(operationalEnvironmentType,operationalEnvironmentStatus);
       logger.debug(EELFLoggerDelegate.debugLogger, "end {}() => {}", getMethodName(), response);
       return response;
    }

    /**
     * Obtain the full subscriber list from a&ai.
     * <p>
     * g @return ResponseEntity The response entity
     *
     * @throws IOException          Signals that an I/O exception has occurred.
     * @throws InterruptedException the interrupted exception
     */
    @RequestMapping(value = "/aai_get_full_subscribers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFullSubscriberList(HttpServletRequest request) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<String> responseEntity;
        RoleValidator roleValidator = new RoleValidator(roleProvider.getUserRoles(request));
        SubscriberFilteredResults subscriberList = aaiService.getFullSubscriberList(roleValidator);
        if (subscriberList.getHttpCode() == 200) {
            responseEntity = new ResponseEntity<String>(objectMapper.writeValueAsString(subscriberList.getSubscriberList()), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<String>(subscriberList.getErrorMessage(), HttpStatus.valueOf(subscriberList.getHttpCode()));
        }


        return responseEntity;
    }


    @RequestMapping(value = "/get_vnf_data_by_globalid_and_service_type/{globalCustomerId}/{serviceType}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getVnfDataByGlobalIdAndServiceType(HttpServletRequest request,
                                                                     @PathVariable("globalCustomerId") String globalCustomerId,
                                                                     @PathVariable("serviceType") String serviceType) throws IOException {

        Response resp = aaiService.getVNFData(globalCustomerId, serviceType);
        return convertResponseToResponseEntity(resp);
    }


    /**
     * Refresh the subscriber list from a&ai.
     *
     * @return ResponseEntity The response entity
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @RequestMapping(value = "/aai_refresh_subscribers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doRefreshSubscriberList() throws IOException {
        Response resp = getSubscribers(false);
        return convertResponseToResponseEntity(resp);
    }

    /**
     * Refresh the full subscriber list from a&ai.
     *
     * @return ResponseEntity The response entity
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @RequestMapping(value = "/aai_refresh_full_subscribers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doRefreshFullSubscriberList() throws IOException {
        Response resp = getSubscribers(false);
        return convertResponseToResponseEntity(resp);
    }

    /**
     * Get subscriber details from a&ai.
     *
     * @param subscriberId the subscriber id
     * @return ResponseEntity The response entity
     */
    @RequestMapping(value = "/aai_sub_details/{subscriberId}", method = RequestMethod.GET)
    public ResponseEntity<String> GetSubscriberDetails(HttpServletRequest request, @PathVariable("subscriberId") String subscriberId) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity responseEntity;
        List<Role> roles = roleProvider.getUserRoles(request);
        RoleValidator roleValidator = new RoleValidator(roles);
        AaiResponse<SubscriberData> subscriberData = aaiService.getSubscriberData(subscriberId, roleValidator);
        String httpMessage = subscriberData.getT() != null ?
                objectMapper.writeValueAsString(subscriberData.getT()) :
                subscriberData.getErrorMessage();

        responseEntity = new ResponseEntity<String>(httpMessage, HttpStatus.valueOf(subscriberData.getHttpCode()));
        return responseEntity;
    }

    /**
     * Get service instances that match the query from a&ai.
     *
     * @param subscriberId the subscriber id
     * @param instanceIdentifier the service instance name or id.
     * @param projects the projects that are related to the instance
     * @param owningEntities the owningEntities that are related to the instance
     * @return ResponseEntity The response entity
     */
    @RequestMapping(value = "/search_service_instances", method = RequestMethod.GET)
    public ResponseEntity<String> SearchServiceInstances(HttpServletRequest request,
                                                         @RequestParam(value="subscriberId", required = false) String subscriberId,
                                                         @RequestParam(value="serviceInstanceIdentifier", required = false) String instanceIdentifier,
                                                         @RequestParam(value="project", required = false) List<String> projects,
                                                         @RequestParam(value="owningEntity", required = false) List<String> owningEntities) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity responseEntity;

        List<Role> roles = roleProvider.getUserRoles(request);
        RoleValidator roleValidator = new RoleValidator(roles);

        AaiResponse<ServiceInstancesSearchResults> searchResult = aaiService.getServiceInstanceSearchResults(subscriberId, instanceIdentifier, roleValidator, owningEntities, projects);

        String httpMessage = searchResult.getT() != null ?
                objectMapper.writeValueAsString(searchResult.getT()) :
                searchResult.getErrorMessage();


        if(searchResult.getT().serviceInstances.size() == 0){
            responseEntity = new ResponseEntity<String>(httpMessage, HttpStatus.NOT_FOUND);

        } else {
            responseEntity = new ResponseEntity<String>(httpMessage, HttpStatus.valueOf(searchResult.getHttpCode()));

        }
        return responseEntity;
    }



    /**
     * Issue a named query to a&ai.
     *
     * @param namedQueryId     the named query id
     * @param globalCustomerId the global customer id
     * @param serviceType      the service type
     * @param serviceInstance  the service instance
     * @return ResponseEntity The response entity
     */
    @RequestMapping(value = "/aai_sub_viewedit/{namedQueryId}/{globalCustomerId}/{serviceType}/{serviceInstance}", method = RequestMethod.GET)
    public ResponseEntity<String> viewEditGetComponentList(
            @PathVariable("namedQueryId") String namedQueryId,
            @PathVariable("globalCustomerId") String globalCustomerId,
            @PathVariable("serviceType") String serviceType,
            @PathVariable("serviceInstance") String serviceInstance) {

        String componentListPayload = getComponentListPutPayload(namedQueryId, globalCustomerId, serviceType, serviceInstance);
        File certiPath = GetCertificatesPath();

        Response resp = doAaiPost(certiPath.getAbsolutePath(), "search/named-query", componentListPayload, false);
//        //mock adds configuration to view/edit response
//        String res = "";
//        try {
//            res = resp.readEntity(String.class);
//            InputStream aaiConfigInstanceFile = AaiController.class.getClassLoader().getResourceAsStream("aai_config_instance_view_edit.json");
//            res = res.substring(0, res.length() - 5) + "," + convertStreamToString(aaiConfigInstanceFile) + "]}}]}";
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new ResponseEntity<>(res, HttpStatus.OK);
        return convertResponseToResponseEntity(resp);
    }

//    public String convertStreamToString(InputStream is) throws IOException {
//        if (is != null) {
//            Writer writer = new StringWriter();
//
//            char[] buffer = new char[1024];
//            try {
//                Reader reader = new BufferedReader(
//                        new InputStreamReader(is, "UTF-8"));
//                int n;
//                while ((n = reader.read(buffer)) != -1) {
//                    writer.write(buffer, 0, n);
//                }
//            } finally {
//                is.close();
//            }
//            return writer.toString();
//        }
//        return "";
//    }

    @RequestMapping(value = "/aai_get_vnf_data/{globalCustomerId}/{serviceType}/{serviceInstanceId}", method = RequestMethod.GET)
    public AaiResponse<String> getVnfData(
            @PathVariable("globalCustomerId") String globalCustomerId,
            @PathVariable("serviceType") String serviceType,
            @PathVariable("serviceInstanceId") String serviceInstanceId) {

        return aaiService.getVNFData(globalCustomerId, serviceType, serviceInstanceId);

    }


    //	@RequestMapping(value="/aai_get_tenants/{global-customer-id}", method = RequestMethod.GET)
    //	public ResponseEntity<String> viewEditGetComponentList(
    //			@PathVariable("global-customer-id") String globalCustomerId) {
    //		return new ResponseEntity<String>(getTenants(globalCustomerId), HttpStatus.OK);
    //	}

    /**
     * Issue a named query to a&ai.
     *
     * @param namedQueryId     the named query id
     * @param globalCustomerId the global customer id
     * @param serviceType      the service type
     * @return ResponseEntity The response entity
     */
    @RequestMapping(value = "/aai_get_models_by_service_type/{namedQueryId}/{globalCustomerId}/{serviceType}", method = RequestMethod.GET)
    public ResponseEntity<String> viewEditGetComponentList(
            @PathVariable("namedQueryId") String namedQueryId,
            @PathVariable("globalCustomerId") String globalCustomerId,
            @PathVariable("serviceType") String serviceType) {

        String componentListPayload = getModelsByServiceTypePayload(namedQueryId, globalCustomerId, serviceType);
        File certiPath = GetCertificatesPath();

        Response resp = doAaiPost(certiPath.getAbsolutePath(), "search/named-query", componentListPayload, false);
        return convertResponseToResponseEntity(resp);
    }

    @RequestMapping(value = "/aai_get_vnf_instances/{globalCustomerId}/{serviceType}/{modelVersionId}/{modelInvariantId}/{cloudRegion}", method = RequestMethod.GET)
    public ResponseEntity<String> getNodeTemplateInstances(
            @PathVariable("globalCustomerId") String globalCustomerId,
            @PathVariable("serviceType") String serviceType,
            @PathVariable("modelVersionId") String modelVersionId,
            @PathVariable("modelInvariantId") String modelInvariantId,
            @PathVariable("cloudRegion") String cloudRegion) {

        AaiResponse<String> resp = aaiService.getNodeTemplateInstances(globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegion);
        return new ResponseEntity<String>(resp.getT(), HttpStatus.valueOf(resp.getHttpCode()));
    }

    @RequestMapping(value = "/aai_get_by_uri/**", method = RequestMethod.GET)
    public ResponseEntity<String> getByUri(HttpServletRequest request) {
        File certiPath = GetCertificatesPath();

        String restOfTheUrl = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String formattedUri = restOfTheUrl.replaceFirst("/aai_get_by_uri/", "").replaceFirst("^aai/v[\\d]+/", "");

        Response resp = doAaiGet(certiPath.getAbsolutePath(), formattedUri, false);

        return convertResponseToResponseEntity(resp);
    }

    @RequestMapping(value = "/aai_get_configuration/{configuration_id}", method = RequestMethod.GET)
    public ResponseEntity<String> getSpecificConfiguration(@PathVariable("configuration_id") String configurationId) {
        File certiPath = GetCertificatesPath();

        Response resp = doAaiGet(certiPath.getAbsolutePath(), "network/configurations/configuration/"+configurationId, false);

        return convertResponseToResponseEntity(resp);
    }

    /**
     * Parses the for tenants.
     *
     * @param resp the resp
     * @return the string
     */
    private String parseForTenants(String resp) {
        String tenantList = "";

        try {
            JSONParser jsonParser = new JSONParser();

            JSONObject jsonObject = (JSONObject) jsonParser.parse(resp);

            return parseCustomerObjectForTenants(jsonObject);
        } catch (Exception ex) {

        }

        return tenantList;
    }

    /**
     * Parses the for tenants by service subscription.
     *
     * @param resp the resp
     * @return the string
     */
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

    /**
     * Obtain tenants for a given service type.
     *
     * @param globalCustomerId the global customer id
     * @param serviceType      the service type
     * @return ResponseEntity The response entity
     */
    @RequestMapping(value = "/aai_get_tenants/{global-customer-id}/{service-type}", method = RequestMethod.GET)
    public ResponseEntity<String> viewEditGetTenantsFromServiceType(HttpServletRequest request,
                                                                    @PathVariable("global-customer-id") String globalCustomerId, @PathVariable("service-type") String serviceType) {

        ResponseEntity responseEntity;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Role> roles = roleProvider.getUserRoles(request);
            RoleValidator roleValidator = new RoleValidator(roles);
            AaiResponse<GetTenantsResponse[]> response = aaiService.getTenants(globalCustomerId, serviceType, roleValidator);
            if (response.getHttpCode() == 200) {
                responseEntity = new ResponseEntity<String>(objectMapper.writeValueAsString(response.getT()), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<String>(response.getErrorMessage(), HttpStatus.valueOf(response.getHttpCode()));
            }
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>("Unable to proccess getTenants reponse", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    private ResponseEntity<String> convertResponseToResponseEntity(Response resp) {
        ResponseEntity<String> respEnt;
        ObjectMapper objectMapper = new ObjectMapper();
        if (resp == null) {
            respEnt = new ResponseEntity<String>("Failed to fetch data from A&AI, check server logs for details.", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            respEnt = new ResponseEntity<String>(resp.readEntity(String.class), HttpStatus.valueOf(resp.getStatus()));
        }
        return respEnt;
    }

    /**
     * Gets the tenants.
     *
     * @param globalCustomerId the global customer id
     * @return the tenants
     */
    private ResponseEntity<String> getTenants(String globalCustomerId) {
        File certiPath = GetCertificatesPath();
        Response resp = doAaiGet(certiPath.getAbsolutePath(), "business/customers/customer/" + globalCustomerId, false);

        ResponseEntity<String> respEnt;
        if (resp.getStatus() >= 200 && resp.getStatus() <= 299) {
            respEnt = new ResponseEntity<String>(parseForTenants((String) resp.readEntity(String.class)), HttpStatus.OK);
        } else {
            respEnt = new ResponseEntity<String>((String) resp.readEntity(String.class), HttpStatus.valueOf(resp.getStatus()));
        }
        return respEnt;

    }

    /**
     * Gets the tenants from service type.
     *
     * @param globalCustomerId the global customer id
     * @param serviceType      the service type
     * @return the tenants from service type
     */
    private ResponseEntity<String> getTenantsFromServiceType(String globalCustomerId, String serviceType) {


        File certiPath = GetCertificatesPath();
        String url = "business/customers/customer/" + globalCustomerId + "/service-subscriptions/service-subscription/" + serviceType;

        Response resp = doAaiGet(certiPath.getAbsolutePath(), url, false);

        ResponseEntity<String> respEnt;
        if (resp.getStatus() >= 200 && resp.getStatus() <= 299) {
            respEnt = new ResponseEntity<String>(parseForTenantsByServiceSubscription((String) resp.readEntity(String.class)), HttpStatus.OK);
        } else {
            respEnt = new ResponseEntity<String>((String) resp.readEntity(String.class), HttpStatus.valueOf(resp.getStatus()));
        }
        return respEnt;

    }

    /**
     * Gets the services.
     *
     * @return the services
     */
    private Response getServices() {
        File certiPath = GetCertificatesPath();
        Response resp = doAaiGet(certiPath.getAbsolutePath(), "service-design-and-creation/services", false);
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "getServices() resp=" + resp.getStatusInfo());

        //model.put("aai_get_services", resp);
        return resp;
    }

    /**
     * Gets the subscribers.
     *
     * @param isFullSet the is full set
     * @return the subscribers
     */
    private Response getSubscribers(boolean isFullSet) {

        File certiPath = GetCertificatesPath();
        String depth = "0";

        Response resp = doAaiGet(certiPath.getAbsolutePath(), "business/customers?subscriber-type=INFRA&depth=" + depth, false);
        if (resp != null) {
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "getSubscribers() resp=" + resp.getStatusInfo().toString());
        }
        return resp;
    }

    /**
     * Gets the subscriber details.
     *
     * @param subscriberId the subscriber id
     * @return the subscriber details
     */
    private Response getSubscriberDetails(String subscriberId) {
        File certiPath = GetCertificatesPath();
        Response resp = doAaiGet(certiPath.getAbsolutePath(), "business/customers/customer/" + subscriberId + "?depth=2", false);
        //String resp = doAaiGet(certiPath.getAbsolutePath(), "business/customers/customer/" + subscriberId, false);
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "getSubscriberDetails() resp=" + resp.getStatusInfo().toString());
        return resp;
    }

    /**
     * Gets the certificates path.
     *
     * @return the file
     */
    private File GetCertificatesPath() {
        if (servletContext != null)
            return new File(servletContext.getRealPath("/WEB-INF/cert/"));
        return null;
    }

    /**
     * Send a GET request to a&ai.
     *
     * @param certiPath the certi path
     * @param uri       the uri
     * @param xml       the xml
     * @return String The response
     */
    protected Response doAaiGet(String certiPath, String uri, boolean xml) {
        String methodName = "getSubscriberList";
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

    /**
     * Send a POST request to a&ai.
     *
     * @param certiPath the certi path
     * @param uri       the uri
     * @param payload   the payload
     * @param xml       the xml
     * @return String The response
     */
    protected Response doAaiPost(String certiPath, String uri, String payload, boolean xml) {
        String methodName = "getSubscriberList";
        String transId = UUID.randomUUID().toString();
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        Response resp = null;
        try {

            AAIRestInterface restContrller = new AAIRestInterface(certiPath);
            resp = restContrller.RestPost(fromAppId, transId, uri, payload, xml);

        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
        }

        return resp;
    }

    /**
     * Gets the component list put payload.
     *
     * @param namedQueryId     the named query id
     * @param globalCustomerId the global customer id
     * @param serviceType      the service type
     * @param serviceInstance  the service instance
     * @return the component list put payload
     */
    private String getComponentListPutPayload(String namedQueryId, String globalCustomerId, String serviceType, String serviceInstance) {
        return
                "		{" +
                        "    \"instance-filters\": {" +
                        "        \"instance-filter\": [" +
                        "            {" +
                        "                \"customer\": {" +
                        "                    \"global-customer-id\": \"" + globalCustomerId + "\"" +
                        "                }," +
                        "                \"service-instance\": {" +
                        "                    \"service-instance-id\": \"" + serviceInstance + "\"" +
                        "                }," +
                        "                \"service-subscription\": {" +
                        "                    \"service-type\": \"" + serviceType + "\"" +
                        "                }" +
                        "            }" +
                        "        ]" +
                        "    }," +
                        "    \"query-parameters\": {" +
                        "        \"named-query\": {" +
                        "            \"named-query-uuid\": \"" + namedQueryId + "\"" +
                        "        }" +
                        "    }" +
                        "}";

    }

    private String getModelsByServiceTypePayload(String namedQueryId, String globalCustomerId, String serviceType) {
        // TODO Auto-generated method stub
        return "		{" +
                "    \"instance-filters\": {" +
                "        \"instance-filter\": [" +
                "            {" +
                "                \"customer\": {" +
                "                    \"global-customer-id\": \"" + globalCustomerId + "\"" +
                "                }," +
                "                \"service-subscription\": {" +
                "                    \"service-type\": \"" + serviceType + "\"" +
                "                }" +
                "            }" +
                "        ]" +
                "    }," +
                "    \"query-parameters\": {" +
                "        \"named-query\": {" +
                "            \"named-query-uuid\": \"" + namedQueryId + "\"" +
                "        }" +
                "    }" +
                "}";

    }
}

