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

package org.onap.vid.controllers;

import com.mashape.unirest.http.HttpResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.jackson.map.ObjectMapper;
import org.onap.vid.aai.AaiGetVnfResponse;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.AaiResponseTranslator.PortMirroringConfigData;
import org.onap.vid.aai.ResponseWithRequestInfo;
import org.onap.vid.aai.ServiceInstancesSearchResults;
import org.onap.vid.aai.Services;
import org.onap.vid.aai.model.AaiGetAicZone.AicZones;
import org.onap.vid.aai.model.AaiGetInstanceGroupsByCloudRegion;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.AaiGetNetworkCollectionDetails;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfResponse;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetServicesRequestModel.GetServicesAAIRespone;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.model.VersionByInvariantIdsRequest;
import org.onap.vid.roles.Role;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.roles.RoleValidator;
import org.onap.vid.services.AaiService;
import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.onap.vid.utils.Logging.getMethodName;

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
     * The logger.
     */
    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(AaiController.class);
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

    @Autowired
    private AAIRestInterface aaiRestInterface;

    /**
     * Welcome method.
     *
     * @param request the request
     * @return ModelAndView The view
     */
    @RequestMapping(value = {"/subscriberSearch"}, method = RequestMethod.GET)
    public ModelAndView welcome(HttpServletRequest request) {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== AaiController welcome start");
        return new ModelAndView(getViewName());
    }

    @RequestMapping(value = {"/aai_get_aic_zones"}, method = RequestMethod.GET)
    public ResponseEntity<String> getAicZones(HttpServletRequest request) throws IOException {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== getAicZones controller start");
        HttpResponse<AicZones> response = aaiService.getAaiZones();
        return httpResponseToResponseEntity(response);
    }

    @RequestMapping(value = {"/aai_get_aic_zone_for_pnf/{globalCustomerId}/{serviceType}/{serviceId}"}, method = RequestMethod.GET)
    public ResponseEntity<String> getAicZoneForPnf(@PathVariable("globalCustomerId") String globalCustomerId ,@PathVariable("serviceType") String serviceType , @PathVariable("serviceId") String serviceId ,HttpServletRequest request) throws IOException {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== getAicZoneForPnf controller start");
        AaiResponse response = aaiService.getAicZoneForPnf(globalCustomerId , serviceType , serviceId);
        return aaiResponseToResponseEntity(response);
    }

    @RequestMapping(value = {"/aai_get_instance_groups_by_vnf_instance_id/{vnfInstanceId}"}, method = RequestMethod.GET)
    public ResponseEntity<String> getInstanceGroupsByVnfInstanceId(@PathVariable("vnfInstanceId") String vnfInstanceId ,HttpServletRequest request) throws IOException {
        AaiResponse response = aaiService.getInstanceGroupsByVnfInstanceId(vnfInstanceId);
        return aaiResponseToResponseEntity(response);
    }
    /**
     * Get services from a&ai.
     *
     * @return ResponseEntity<String> The response entity with the logged in user uuid.
     * @throws IOException          Signals that an I/O exception has occurred.
     */
    @RequestMapping(value = {"/getuserID"}, method = RequestMethod.GET)
    public ResponseEntity<String> getUserID(HttpServletRequest request) {

        String userId = ControllersUtils.extractUserId(request);

        return new ResponseEntity<String>(userId, HttpStatus.OK);
    }

    /**
     * Get services from a&ai.
     *
     * @return ResponseEntity<String> The response entity
     * @throws IOException          Signals that an I/O exception has occurred.
     */
    @RequestMapping(value = "/aai_get_services", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doGetServices(HttpServletRequest request) throws IOException {
        RoleValidator roleValidator = new RoleValidator(roleProvider.getUserRoles(request));

        HttpResponse<GetServicesAAIRespone> subscriberList = aaiService.getServices(roleValidator);
        ResponseEntity<String> responseEntity = httpResponseToResponseEntity(subscriberList);

        return responseEntity;
    }


    @RequestMapping(value = {"/aai_get_version_by_invariant_id"}, method = RequestMethod.POST)
    public ResponseEntity<String> getVersionByInvariantId(HttpServletRequest request, @RequestBody VersionByInvariantIdsRequest versions) {
        ResponseEntity<String> responseEntity;
        ObjectMapper objectMapper = new ObjectMapper();
        HttpResponse<ResponseWithRequestInfo> result = aaiService.getVersionByInvariantId(versions.versions);
        return new ResponseEntity<String>(result.getBody().getResponse().readEntity(String.class), HttpStatus.OK);
    }


    private ResponseEntity<String> httpResponseToResponseEntity(HttpResponse responseData)
            throws IOException {
        ResponseEntity<String> responseEntity;
        ObjectMapper objectMapper = new ObjectMapper();
        if (responseData.getStatus() == 200) {
            responseEntity = new ResponseEntity<>(objectMapper.writeValueAsString(responseData.getBody()), HttpStatus.OK);
        } else {
            String message =IOUtils.toString(responseData.getRawBody(), "UTF-8");
            responseEntity = new ResponseEntity<>(message, HttpStatus.valueOf(responseData.getStatus()));
        }
        return responseEntity;
    }

    private ResponseEntity<String> aaiResponseToResponseEntity(AaiResponse aaiResponseData)
        throws IOException {
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
     */
    @RequestMapping(value = "/aai_get_service_instance/{service-instance-id}/{service-instance-type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doGetServiceInstance(@PathVariable("service-instance-id") String serviceInstanceId, @PathVariable("service-instance-type") String serviceInstanceType) {
        Response resp = null;

        if (serviceInstanceType.equalsIgnoreCase("Service Instance Id")) {
            resp = doAaiGet(
                    "search/nodes-query?search-node-type=service-instance&filter=service-instance-id:EQUALS:"
                            + serviceInstanceId, false);
        } else {
            resp = doAaiGet(
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
     */
    @RequestMapping(value = "/aai_get_service_subscription/{global-customer-id}/{service-subscription-id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doGetServices(@PathVariable("global-customer-id") String globalCustomerId,
                                                @PathVariable("service-subscription-id") String serviceSubscriptionId) {
        Response resp = doAaiGet("business/customers/customer/" + globalCustomerId
                + "/service-subscriptions/service-subscription/" + serviceSubscriptionId + "?depth=0", false);
        return convertResponseToResponseEntity(resp);
    }

    /**
     * Obtain the subscriber list from a&ai.
     *
     * @param fullSet the full set
     * @return ResponseEntity The response entity
     * @throws IOException          Signals that an I/O exception has occurred.
     */
    @RequestMapping(value = "/aai_get_subscribers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doGetSubscriberList(HttpServletRequest request, @DefaultValue("n") @QueryParam("fullSet") String fullSet) throws IOException {
        return getFullSubscriberList(request);
    }

    /**
     * Obtain the Target Prov Status from the System.Properties file.
     *
     * @return ResponseEntity The response entity
     * @throws IOException          Signals that an I/O exception has occurred.
     */
    @RequestMapping(value = "/get_system_prop_vnf_prov_status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTargetProvStatus() {
        String p = SystemProperties.getProperty("aai.vnf.provstatus");
        return new ResponseEntity<String>(p, HttpStatus.OK);
    }


    /**
     * Obtain the Target Prov Status from the System.Properties file.
     *
     * @return ResponseEntity The response entity
     * @throws IOException          Signals that an I/O exception has occurred.
     */
    @RequestMapping(value = "/get_operational_environments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public AaiResponse<OperationalEnvironmentList> getOperationalEnvironments(@RequestParam(value="operationalEnvironmentType", required = false) String operationalEnvironmentType,
                                                           @RequestParam(value="operationalEnvironmentStatus", required = false) String operationalEnvironmentStatus) {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "start {}({}, {})", getMethodName(), operationalEnvironmentType, operationalEnvironmentStatus);
        HttpResponse<OperationalEnvironmentList> operationalEnvironments = aaiService
            .getOperationalEnvironments(operationalEnvironmentType, operationalEnvironmentStatus);
        AaiResponse<OperationalEnvironmentList> response = null;
        String errorMessage = null;
        if (operationalEnvironments.getStatus() != 200) {
            try {
                errorMessage = IOUtils.toString(operationalEnvironments.getRawBody(), "UTF-8");
            } catch (IOException e) {
                errorMessage = operationalEnvironments.getStatusText();
            }
        }
        response = new AaiResponse<>(operationalEnvironments.getBody(), errorMessage, operationalEnvironments.getStatus());
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "end {}() => {}", getMethodName(), response);
        return response;
    }

    /**
     * Obtain the full subscriber list from a&ai.
     * <p>
     * g @return ResponseEntity The response entity
     *
     * @throws IOException          Signals that an I/O exception has occurred.
     */
    @RequestMapping(value = "/aai_get_full_subscribers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFullSubscriberList(HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<String> responseEntity;
        RoleValidator roleValidator = new RoleValidator(roleProvider.getUserRoles(request));
        HttpResponse<SubscriberList> subscriberList = aaiService.getFullSubscriberList(roleValidator);
        if (subscriberList.getStatus() == 200) {
            responseEntity = new ResponseEntity<String>(objectMapper.writeValueAsString(subscriberList.getBody()), HttpStatus.OK);
        } else {
            String error = IOUtils.toString(subscriberList.getRawBody(), "UTF-8");
            responseEntity = new ResponseEntity<String>(error, HttpStatus.valueOf(subscriberList.getStatus()));
        }
        return responseEntity;
    }


    @RequestMapping(value = "/get_vnf_data_by_globalid_and_service_type/{globalCustomerId}/{serviceType}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getVnfDataByGlobalIdAndServiceType(HttpServletRequest request,
                                                                     @PathVariable("globalCustomerId") String globalCustomerId,
                                                                     @PathVariable("serviceType") String serviceType) {

        return convertResponseToResponseEntity(aaiService.getVNFData(globalCustomerId, serviceType));
    }


    /**
     * Refresh the subscriber list from a&ai.
     *
     * @return ResponseEntity The response entity
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @RequestMapping(value = "/aai_refresh_subscribers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doRefreshSubscriberList() {
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
    public ResponseEntity<String> doRefreshFullSubscriberList() {
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
        List<Role> roles = roleProvider.getUserRoles(request);
        RoleValidator roleValidator = new RoleValidator(roles);
        HttpResponse<Services> subscriberData = aaiService.getSubscriberData(subscriberId, roleValidator);
        String httpMessage = subscriberData.getBody() != null ?
                objectMapper.writeValueAsString(subscriberData.getBody()) :
                IOUtils.toString(subscriberData.getRawBody(), "UTF-8");
        return new ResponseEntity<String>(httpMessage, HttpStatus.valueOf(subscriberData.getStatus()));
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

        Response resp = doAaiPost("search/named-query", componentListPayload, false);
        return convertResponseToResponseEntity(resp);
    }

    @RequestMapping(value = "/aai_get_vnf_data/{globalCustomerId}/{serviceType}/{serviceInstanceId}", method = RequestMethod.GET)
    public AaiResponse<String> getVnfData(
            @PathVariable("globalCustomerId") String globalCustomerId,
            @PathVariable("serviceType") String serviceType,
            @PathVariable("serviceInstanceId") String serviceInstanceId) {
        HttpResponse<AaiGetVnfResponse> vnfData = aaiService.getVNFData(globalCustomerId, serviceType, serviceInstanceId);
        String errorMessage = null;
        if (vnfData.getStatus() != 200) {
            try {
                errorMessage = IOUtils.toString(vnfData.getRawBody(), "UTF-8");
            } catch (IOException e) {
                errorMessage = vnfData.getStatusText();
            }
        }
        return new AaiResponse<>(vnfData.getBody().toString(), errorMessage, vnfData.getStatus());
    }


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

        Response resp = doAaiPost("search/named-query", componentListPayload, false);
        return convertResponseToResponseEntity(resp);
    }

    @RequestMapping(value = "/aai_get_vnf_instances/{globalCustomerId}/{serviceType}/{modelVersionId}/{modelInvariantId}/{cloudRegion}", method = RequestMethod.GET)
    public ResponseEntity<String> getNodeTemplateInstances(
            @PathVariable("globalCustomerId") String globalCustomerId,
            @PathVariable("serviceType") String serviceType,
            @PathVariable("modelVersionId") String modelVersionId,
            @PathVariable("modelInvariantId") String modelInvariantId,
            @PathVariable("cloudRegion") String cloudRegion) {

        HttpResponse<AaiGetVnfResponse> resp = aaiService.getNodeTemplateInstances(globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegion);
        return new ResponseEntity<String>(resp.getBody().toString(), HttpStatus.valueOf(resp.getStatus()));
    }

    @RequestMapping(value = "/aai_get_network_collection_details/{serviceInstanceId}", method = RequestMethod.GET)
    public ResponseEntity<String> getNetworkCollectionDetails(@PathVariable("serviceInstanceId") String serviceInstanceId) throws IOException {
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        HttpResponse<AaiGetNetworkCollectionDetails> resp = aaiService.getNetworkCollectionDetails(serviceInstanceId);

        String httpMessage = resp.getBody() != null ?
                objectMapper.writeValueAsString(resp.getBody()) :
                IOUtils.toString(resp.getRawBody(), "UTF-8");
        return new ResponseEntity<>(httpMessage, HttpStatus.valueOf(resp.getStatus()));
    }

    @RequestMapping(value = "/aai_get_instance_groups_by_cloudregion/{cloudOwner}/{cloudRegionId}/{networkFunction}", method = RequestMethod.GET)
    public ResponseEntity<String> getInstanceGroupsByCloudRegion(@PathVariable("cloudOwner") String cloudOwner,
                                                                 @PathVariable("cloudRegionId") String cloudRegionId,
                                                                 @PathVariable("networkFunction") String networkFunction) throws IOException {
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        HttpResponse<AaiGetInstanceGroupsByCloudRegion> resp = aaiService.getInstanceGroupsByCloudRegion(cloudOwner, cloudRegionId, networkFunction);

        String httpMessage = resp.getBody() != null ?
                objectMapper.writeValueAsString(resp.getBody()) :
                IOUtils.toString(resp.getRawBody(), "UTF-8");
        return new ResponseEntity<>(httpMessage, HttpStatus.valueOf(resp.getStatus()));
    }

    @RequestMapping(value = "/aai_get_by_uri/**", method = RequestMethod.GET)
    public ResponseEntity<String> getByUri(HttpServletRequest request) {

        String restOfTheUrl = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String formattedUri = restOfTheUrl.replaceFirst("/aai_get_by_uri/", "").replaceFirst("^aai/v[\\d]+/", "");

        Response resp = doAaiGet(formattedUri, false);

        return convertResponseToResponseEntity(resp);
    }



    @RequestMapping(value = "/aai_get_configuration/{configuration_id}", method = RequestMethod.GET)
    public ResponseEntity<String> getSpecificConfiguration(@PathVariable("configuration_id") String configurationId) {

        Response resp = doAaiGet("network/configurations/configuration/"+configurationId, false);

        return convertResponseToResponseEntity(resp);
    }

    @RequestMapping(value = "/aai_get_service_instance_pnfs/{globalCustomerId}/{serviceType}/{serviceInstanceId}", method = RequestMethod.GET)
    public List<String> getServiceInstanceAssociatedPnfs(
            @PathVariable("globalCustomerId") String globalCustomerId,
            @PathVariable("serviceType") String serviceType,
            @PathVariable("serviceInstanceId") String serviceInstanceId) {

        return aaiService.getServiceInstanceAssociatedPnfs(globalCustomerId, serviceType, serviceInstanceId);
    }

    /**
     * PNF section
     */
    @RequestMapping(value = "/aai_get_pnfs/pnf/{pnf_id}", method = RequestMethod.GET)
    public ResponseEntity getSpecificPnf(@PathVariable("pnf_id") String pnfId) {
        HttpResponse<Pnf> resp;
        ResponseEntity<Pnf> re;
        try {
            resp = aaiService.getSpecificPnf(pnfId);
            re = new ResponseEntity<>(resp.getBody(), HttpStatus.valueOf(resp.getStatus()));
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return re;
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
            HttpResponse<GetTenantsResponse[]> tenants = aaiService
                .getTenants(globalCustomerId, serviceType, roleValidator);
            if (tenants.getStatus()== 200) {
                responseEntity = new ResponseEntity<>(objectMapper.writeValueAsString(tenants.getBody()), HttpStatus.OK);
            } else {
                String message = IOUtils.toString(tenants.getRawBody(), "UTF-8");
                responseEntity = new ResponseEntity<String>(message, HttpStatus.valueOf(tenants.getStatus()));
            }
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>("Unable to proccess getTenants reponse", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/aai_get_pnf_instances/{globalCustomerId}/{serviceType}/{modelVersionId}/{modelInvariantId}/{cloudRegion}/{equipVendor}/{equipModel}", method = RequestMethod.GET)
    public ResponseEntity<String> getPnfInstances(
            @PathVariable("globalCustomerId") String globalCustomerId,
            @PathVariable("serviceType") String serviceType,
            @PathVariable("modelVersionId") String modelVersionId,
            @PathVariable("modelInvariantId") String modelInvariantId,
            @PathVariable("cloudRegion") String cloudRegion,
            @PathVariable("equipVendor") String equipVendor,
            @PathVariable("equipModel") String equipModel) {

        HttpResponse<AaiGetPnfResponse> resp = aaiService.getPNFData(globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegion, equipVendor, equipModel);
        return new ResponseEntity<>(resp.getBody().toString(), HttpStatus.valueOf(resp.getStatus()));
    }

    @RequestMapping(value = "/aai_getPortMirroringConfigsData", method = RequestMethod.GET)
    public Map<String, PortMirroringConfigData> getPortMirroringConfigsData(
            @RequestParam ("configurationIds") List<String> configurationIds) {

        return configurationIds.stream()
                .map(id -> ImmutablePair.of(id, aaiService.getPortMirroringConfigData(id)))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    @RequestMapping(value = "/aai_getPortMirroringSourcePorts", method = RequestMethod.GET)
    public Map<String, Object> getPortMirroringSourcePorts(
            @RequestParam ("configurationIds") List<String> configurationIds) {

        return configurationIds.stream()
                .map(id -> ImmutablePair.of(id, aaiService.getPortMirroringSourcePorts(id)))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
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

    private ResponseEntity<String> convertResponseToResponseEntity(HttpResponse<String> resp) {
        ResponseEntity<String> respEnt;
        if (resp == null) {
            respEnt = new ResponseEntity<>("Failed to fetch data from A&AI, check server logs for details.", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            respEnt = new ResponseEntity<>(resp.getBody(), HttpStatus.valueOf(resp.getStatus()));
        }
        return respEnt;
    }

    /**
     * Gets the subscribers.
     *
     * @param isFullSet the is full set
     * @return the subscribers
     */
    private Response getSubscribers(boolean isFullSet) {

        String depth = "0";

        Response resp = doAaiGet("business/customers?subscriber-type=INFRA&depth=" + depth, false);
        if (resp != null) {
            LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "getSubscribers() resp=" + resp.getStatusInfo().toString());
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
        Response resp = doAaiGet("business/customers/customer/" + subscriberId + "?depth=2", false);
        //String resp = doAaiGet(certiPath.getAbsolutePath(), "business/customers/customer/" + subscriberId, false);
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "getSubscriberDetails() resp=" + resp.getStatusInfo().toString());
        return resp;
    }

    /**
     * Send a GET request to a&ai.
     *
     * @param uri       the uri
     * @param xml       the xml
     * @return String The response
     */
    protected Response doAaiGet(String uri, boolean xml) {
        String methodName = "getSubscriberList";
        String transId = UUID.randomUUID().toString();
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        Response resp = null;
        try {


            resp = aaiRestInterface.RestGet(fromAppId, transId, uri, xml).getResponse();

        } catch (WebApplicationException e) {
            final String message = e.getResponse().readEntity(String.class);
            LOGGER.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + message);
            LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + message);
        } catch (Exception e) {
            LOGGER.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
        }

        return resp;
    }

    /**
     * Send a POST request to a&ai.
     *
     * @param uri       the uri
     * @param payload   the payload
     * @param xml       the xml
     * @return String The response
     */
    protected Response doAaiPost(String uri, String payload, boolean xml) {
        String methodName = "getSubscriberList";
        String transId = UUID.randomUUID().toString();
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        Response resp = null;
        try {

            resp = aaiRestInterface.RestPost(fromAppId, uri, payload, xml);

        } catch (Exception e) {
            LOGGER.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
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

    private String getAaiErrorMessage(String message) {
        try {
            org.json.JSONObject json = new org.json.JSONObject(message);
            json = json.getJSONObject("requestError").getJSONObject("serviceException");

            return json.getString("messageId") + ": " + json.getString("text");

        } catch (Exception e) {
            return null;
        }
    }
}