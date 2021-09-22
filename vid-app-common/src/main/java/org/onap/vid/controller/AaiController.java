/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia.
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

package org.onap.vid.controller;

import static org.onap.vid.utils.Logging.getMethodName;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.AaiResponseTranslator.PortMirroringConfigData;
import org.onap.vid.aai.ServiceInstancesSearchResults;
import org.onap.vid.aai.SubscriberFilteredResults;
import org.onap.vid.aai.model.AaiGetInstanceGroupsByCloudRegion;
import org.onap.vid.aai.model.AaiGetOperationalEnvironments.OperationalEnvironmentList;
import org.onap.vid.aai.model.AaiGetPnfs.Pnf;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.model.VersionByInvariantIdsRequest;
import org.onap.vid.properties.Features;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.roles.RoleValidator;
import org.onap.vid.services.AaiService;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.onap.vid.utils.Unchecked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.togglz.core.manager.FeatureManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.onap.vid.aai.CustomerSpecificServiceInstance;
import org.onap.vid.aai.DSLQuerySimpleResponse;
import org.onap.vid.aai.model.ServiceRelationships;
import org.onap.vid.aai.model.ViewEditSIResult;


@RestController
public class AaiController extends RestrictedBaseController {

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(AaiController.class);
    private static final String FROM_APP_ID = "VidAaiController";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private AaiService aaiService;
    private AAIRestInterface aaiRestInterface;
    private RoleProvider roleProvider;
    private SystemPropertiesWrapper systemPropertiesWrapper;
    private FeatureManager featureManager;


    @Autowired
    public AaiController(AaiService aaiService,
        AAIRestInterface aaiRestInterface,
        RoleProvider roleProvider,
        SystemPropertiesWrapper systemPropertiesWrapper,
        FeatureManager featureManager
    ) {

        this.aaiService = aaiService;
        this.aaiRestInterface = aaiRestInterface;
        this.roleProvider = roleProvider;
        this.systemPropertiesWrapper = systemPropertiesWrapper;
        this.featureManager = featureManager;
    }

    @RequestMapping(value = {"/subscriberSearch"}, method = RequestMethod.GET)
    public ModelAndView welcome() {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== AaiController welcome start");
        return new ModelAndView(getViewName());
    }

    @RequestMapping(value = {"/aai_get_aic_zones"}, method = RequestMethod.GET)
    public ResponseEntity<String> getAicZones() throws IOException {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== getAicZones controller start");
        AaiResponse response = aaiService.getAaiZones();
        return aaiResponseToResponseEntity(response);
    }

    @RequestMapping(value = {
        "/aai_get_aic_zone_for_pnf/{globalCustomerId}/{serviceType}/{serviceId}"}, method = RequestMethod.GET)
    public ResponseEntity<String> getAicZoneForPnf(@PathVariable("globalCustomerId") String globalCustomerId,
        @PathVariable("serviceType") String serviceType, @PathVariable("serviceId") String serviceId) throws IOException {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== getAicZoneForPnf controller start");
        AaiResponse response = aaiService.getAicZoneForPnf(globalCustomerId, serviceType, serviceId);
        return aaiResponseToResponseEntity(response);
    }

    @RequestMapping(value = {"/aai_get_instance_groups_by_vnf_instance_id/{vnfInstanceId}"}, method = RequestMethod.GET)
    public ResponseEntity<String> getInstanceGroupsByVnfInstanceId(@PathVariable("vnfInstanceId") String vnfInstanceId) throws IOException {
        AaiResponse response = aaiService.getInstanceGroupsByVnfInstanceId(vnfInstanceId);
        return aaiResponseToResponseEntity(response);
    }

    @RequestMapping(value = {"/getuserID"}, method = RequestMethod.GET)
    public ResponseEntity<String> getUserID(HttpServletRequest request) {

        String userId = new ControllersUtils(systemPropertiesWrapper).extractUserId(request);

        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    @RequestMapping(value = "/aai_get_services", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doGetServices(HttpServletRequest request) throws IOException {
        RoleValidator roleValidator = roleProvider.getUserRolesValidator(request);

        AaiResponse subscriberList = aaiService.getServices(roleValidator);
        return aaiResponseToResponseEntity(subscriberList);
    }


    @RequestMapping(value = {"/aai_get_version_by_invariant_id"}, method = RequestMethod.POST)
    public ResponseEntity<String> getVersionByInvariantId(@RequestBody VersionByInvariantIdsRequest versions) {
        Response result = aaiService.getVersionByInvariantId(versions.versions);

        return new ResponseEntity<>(result.readEntity(String.class), HttpStatus.OK);
    }


    private ResponseEntity<String> aaiResponseToResponseEntity(AaiResponse aaiResponseData)
        throws IOException {
        ResponseEntity<String> responseEntity;
        if (aaiResponseData.getHttpCode() == 200) {
            responseEntity = new ResponseEntity<>(objectMapper.writeValueAsString(aaiResponseData.getT()),
                HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(aaiResponseData.getErrorMessage(),
                HttpStatus.valueOf(aaiResponseData.getHttpCode()));
        }

        return responseEntity;
    }

    @RequestMapping(value = "/aai_get_service_instance/{service-instance-id}/{service-instance-type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doGetServiceInstance(@PathVariable("service-instance-id") String serviceInstanceId,
        @PathVariable("service-instance-type") String serviceInstanceType) {
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

    @RequestMapping(value = "/aai_get_service_subscription/{global-customer-id}/{service-subscription-id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doGetServices(@PathVariable("global-customer-id") String globalCustomerId,
        @PathVariable("service-subscription-id") String serviceSubscriptionId) {
        Response resp = doAaiGet("business/customers/customer/" + globalCustomerId
            + "/service-subscriptions/service-subscription/" + serviceSubscriptionId + "?depth=0", false);
        return convertResponseToResponseEntity(resp);
    }

    @RequestMapping(value = "/aai_get_subscribers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doGetSubscriberList(HttpServletRequest request,
        @DefaultValue("n") @QueryParam("fullSet") String fullSet) throws IOException {
        return getFullSubscriberList(request);
    }

    @RequestMapping(value = "/get_system_prop_vnf_prov_status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTargetProvStatus() {
        String p = systemPropertiesWrapper.getProperty("aai.vnf.provstatus");
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @RequestMapping(value = "/get_operational_environments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public AaiResponse<OperationalEnvironmentList> getOperationalEnvironments(
        @RequestParam(value = "operationalEnvironmentType", required = false) String operationalEnvironmentType,
        @RequestParam(value = "operationalEnvironmentStatus", required = false) String operationalEnvironmentStatus) {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "start {}({}, {})", getMethodName(), operationalEnvironmentType,
            operationalEnvironmentStatus);
        AaiResponse<OperationalEnvironmentList> response = aaiService
            .getOperationalEnvironments(operationalEnvironmentType, operationalEnvironmentStatus);
        if (response.getHttpCode() != 200) {
            String errorMessage = getAaiErrorMessage(response.getErrorMessage());
            if (errorMessage != null) {
                response = new AaiResponse<>(response.getT(), errorMessage, response.getHttpCode());
            }
        }

        LOGGER.debug(EELFLoggerDelegate.debugLogger, "end {}() => {}", getMethodName(), response);
        return response;
    }

    @RequestMapping(value = "/aai_get_full_subscribers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFullSubscriberList(HttpServletRequest request) throws IOException {
        ResponseEntity<String> responseEntity;
        RoleValidator roleValidator = roleProvider.getUserRolesValidator(request);
        SubscriberFilteredResults subscriberList = aaiService.getFullSubscriberList(roleValidator);
        if (subscriberList.getHttpCode() == 200) {
            responseEntity = new ResponseEntity<>(objectMapper.writeValueAsString(subscriberList.getSubscriberList()),
                HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(subscriberList.getErrorMessage(),
                HttpStatus.valueOf(subscriberList.getHttpCode()));
        }

        return responseEntity;
    }

    @RequestMapping(value = "/aai_refresh_subscribers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doRefreshSubscriberList() {
        return refreshSubscriberList();
    }

    @RequestMapping(value = "/aai_refresh_full_subscribers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doRefreshFullSubscriberList() {
        return refreshSubscriberList();
    }

    protected ResponseEntity<String> refreshSubscriberList() {
        Response resp = getSubscribers();
        return convertResponseToResponseEntity(resp);
    }

    @RequestMapping(value = "/aai_sub_details/{subscriberId}", method = RequestMethod.GET)
    public ResponseEntity<String> getSubscriberDetails(HttpServletRequest request, @PathVariable("subscriberId") String subscriberId,
                                                       @RequestParam(value="omitServiceInstances", required = false, defaultValue = "false") boolean omitServiceInstances) throws IOException {
        RoleValidator roleValidator = roleProvider.getUserRolesValidator(request);
        AaiResponse subscriberData = aaiService.getSubscriberData(subscriberId, roleValidator,
            featureManager.isActive(Features.FLAG_1906_AAI_SUB_DETAILS_REDUCE_DEPTH) && omitServiceInstances);
        String httpMessage = subscriberData.getT() != null ? objectMapper.writeValueAsString(subscriberData.getT()) : subscriberData.getErrorMessage();

        return new ResponseEntity<>(httpMessage,
            HttpStatus.valueOf(subscriberData.getHttpCode()));
    }

    @RequestMapping(value = "/search_service_instances", method = RequestMethod.GET)
    public ResponseEntity<String> SearchServiceInstances(HttpServletRequest request,
        @RequestParam(value = "subscriberId", required = false) String subscriberId,
        @RequestParam(value = "serviceInstanceIdentifier", required = false) String instanceIdentifier,
        @RequestParam(value = "serviceInstanceIdentifierType", required = false) String instanceIdentifierType,
        @RequestParam(value = "project", required = false) List<String> projects,
        @RequestParam(value = "owningEntity", required = false) List<String> owningEntities) throws IOException {
        ResponseEntity responseEntity;

        RoleValidator roleValidator = roleProvider.getUserRolesValidator(request);

        AaiResponse<ServiceInstancesSearchResults> searchResult = null;

        if( instanceIdentifier != null && isValidInstanceIdentifierType(instanceIdentifierType)) {
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== search_service_instances search by subscriberId "
                + " instanceIdentifier and instanceIdentifierType start");
            searchResult = aaiService
                .getServiceInstanceSearchResultsByIdentifierType(subscriberId, instanceIdentifier,
                    instanceIdentifierType, roleValidator, owningEntities, projects);
        } else {
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== search_service_instances search by subscriberId "
                + "instanceIdentifier instanceIdentifier and instanceIdentifierType start");
            searchResult = aaiService
                .getServiceInstanceSearchResults(subscriberId, instanceIdentifier, roleValidator, owningEntities, projects);
        }

        String httpMessage = searchResult.getT() != null ?
            objectMapper.writeValueAsString(searchResult.getT()) :
            searchResult.getErrorMessage();

        if (searchResult.getT().serviceInstances.isEmpty()) {
            responseEntity = new ResponseEntity<>(httpMessage, HttpStatus.NOT_FOUND);

        } else {
            responseEntity = new ResponseEntity<>(httpMessage, HttpStatus.valueOf(searchResult.getHttpCode()));

        }
        return responseEntity;
    }

    @RequestMapping(value = {
        "/aai_get_service_instance_by_id_and_type/{globalCustomerId}/{serviceInstanceIdentifier}/{serviceIdentifierType}/{subscriberName}",
        "/aai_get_service_instance_by_id_and_type/{globalCustomerId}/{serviceInstanceIdentifier}/{serviceIdentifierType}"},
        method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> doGetServiceInstanceByIdAndType(
        @PathVariable("globalCustomerId") String globalCustomerId,
        @PathVariable("serviceInstanceIdentifier") String serviceInstanceIdentifier,
        @PathVariable("serviceIdentifierType") String serviceIdentifierType,
        @PathVariable("subscriberName") java.util.Optional<String> subscriberName) throws IOException {

        AaiResponse aaiResponse = null;
        String orchStatus = null;
        String siid, siName, modelVerId, modelInvId = null;
        String errorMessage = null;
        int statusCode = -1;
        ViewEditSIResult viewEditSIResult = new ViewEditSIResult();
        if(!subscriberName.equals(Optional.empty()) && serviceInstanceIdentifier != null) {
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + ".aai_get_service_instance_by_id_and_type. "
                + "Search node query to get Service Type: "+serviceInstanceIdentifier);
            ResponseEntity entity = convertResponseToResponseEntity(doAaiGet(
                "search/nodes-query?search-node-type=service-instance&filter=service-instance-id:EQUALS:"
                    + serviceInstanceIdentifier, false));
            JSONParser jsonParser = new JSONParser();
            try {
                if(entity != null) {
                    org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) jsonParser.parse(
                                                                                            entity.getBody().toString());
                    JSONArray jSONArray = (JSONArray)jsonObject.get("result-data");
                    org.json.simple.JSONObject jSONObject = (org.json.simple.JSONObject)jSONArray.get(0);
                    String resourceLink = jSONObject.get("resource-link").toString();
                    String serviceType = resourceLink.split("/")[9];
                    aaiResponse = aaiService.getServiceInstanceBySubscriberIdAndSIID(globalCustomerId,serviceType,
                                                                                            serviceInstanceIdentifier);
                    if(aaiResponse != null && aaiResponse.getT() != null) {
                        viewEditSIResult.setOrchestrationStatus(((ServiceRelationships) aaiResponse.getT()).orchestrationStatus);
                        viewEditSIResult.setServiceInstanceId(((ServiceRelationships) aaiResponse.getT()).serviceInstanceId);
                        viewEditSIResult.setServiceInstanceName(((ServiceRelationships) aaiResponse.getT()).serviceInstanceName);
                        viewEditSIResult.setModelVersionId(((ServiceRelationships) aaiResponse.getT()).modelVersionId);
                        viewEditSIResult.setModelInvariantId(((ServiceRelationships) aaiResponse.getT()).modelInvariantId);
                        viewEditSIResult.setSubscriberName(subscriberName.get());
                    } else {
                        LOGGER.info(EELFLoggerDelegate.errorLogger, "<== " + ".aai_get_service_instance_by_id_and_type. No response for getServiceInstanceBySubscriberIdAndSIID: "+serviceInstanceIdentifier);
                        errorMessage = aaiResponse.getErrorMessage();
                    }
                    statusCode = aaiResponse.getHttpCode();
                } else {
                    LOGGER.info(EELFLoggerDelegate.errorLogger, "<== " + ".aai_get_service_instance_by_id_and_type. No response for nodes-query for siid: "+serviceInstanceIdentifier);
                    statusCode = entity.getStatusCode().value();
                    errorMessage = aaiResponse.getErrorMessage();
                }
            } catch (Exception e) {
                LOGGER.info(EELFLoggerDelegate.errorLogger, "<== " + ".aai_get_service_instance_by_id_and_type" + e.toString());
                LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + ".aai_get_service_instance_by_id_and_type" + e.toString());
            }
        } else {
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + ".aai_get_service_instance_by_id_and_type. Use DSL query to get SI details."+serviceInstanceIdentifier);
            aaiResponse = aaiService
                .getServiceInstanceBySubscriberIdAndInstanceIdentifier(globalCustomerId, serviceIdentifierType, serviceInstanceIdentifier);
            if(aaiResponse != null && aaiResponse.getT() != null) {
                CustomerSpecificServiceInstance siData = ((DSLQuerySimpleResponse)aaiResponse.getT()).getResults().get(0).getCustomer().
                    customerRelatedNodes.get(0).getCustomerServiceSubscription().
                    getServiceSubscriptionRelatedNodes().get(0).getServiceInstance();
                viewEditSIResult.setOrchestrationStatus(siData.getOrchestrationStatus());
                viewEditSIResult.setServiceInstanceId(siData.serviceInstanceId);
                viewEditSIResult.setServiceInstanceName(siData.serviceInstanceName);
                viewEditSIResult.setModelVersionId(siData.modelVersionId);
                viewEditSIResult.setModelInvariantId(siData.modelInvariantId);
                viewEditSIResult.setSubscriberName(((DSLQuerySimpleResponse)aaiResponse.getT()).getResults().get(0).getCustomer().subscriberName);
            } else {
                LOGGER.info(EELFLoggerDelegate.errorLogger, "<== " + ".aai_get_service_instance_by_id_and_type. No result for DSL query :"+serviceInstanceIdentifier);
                errorMessage = aaiResponse.getErrorMessage();
            }
            statusCode = aaiResponse.getHttpCode();
        }
        String httpMessage = viewEditSIResult != null ? objectMapper.writeValueAsString(viewEditSIResult) : errorMessage;
        return new ResponseEntity<>(httpMessage,HttpStatus.valueOf(statusCode));
    }


    @RequestMapping(value = "/aai_sub_viewedit/{namedQueryId}/{globalCustomerId}/{serviceType}/{serviceInstance}", method = RequestMethod.GET)
    public ResponseEntity<String> viewEditGetComponentList(
        @PathVariable("namedQueryId") String namedQueryId,
        @PathVariable("globalCustomerId") String globalCustomerId,
        @PathVariable("serviceType") String serviceType,
        @PathVariable("serviceInstance") String serviceInstance) {

        String componentListPayload = getComponentListPutPayload(namedQueryId, globalCustomerId, serviceType,
            serviceInstance);

        Response resp = doAaiPost("search/named-query", componentListPayload, false);
        return convertResponseToResponseEntity(resp);
    }

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

        AaiResponse<String> resp = aaiService
            .getNodeTemplateInstances(globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegion);
        return new ResponseEntity<>(resp.getT(), HttpStatus.valueOf(resp.getHttpCode()));
    }

    @RequestMapping(value = "/aai_get_network_collection_details/{serviceInstanceId}", method = RequestMethod.GET)
    public ResponseEntity<String> getNetworkCollectionDetails(
        @PathVariable("serviceInstanceId") String serviceInstanceId) throws IOException {
        AaiResponse<String> resp = aaiService.getNetworkCollectionDetails(serviceInstanceId);

        String httpMessage = resp.getT() != null ?
            objectMapper.writeValueAsString(resp.getT()) :
            resp.getErrorMessage();
        return new ResponseEntity<>(httpMessage, HttpStatus.valueOf(resp.getHttpCode()));
    }

    @RequestMapping(value = "/aai_get_instance_groups_by_cloudregion/{cloudOwner}/{cloudRegionId}/{networkFunction}", method = RequestMethod.GET)
    public ResponseEntity<String> getInstanceGroupsByCloudRegion(@PathVariable("cloudOwner") String cloudOwner,
        @PathVariable("cloudRegionId") String cloudRegionId,
        @PathVariable("networkFunction") String networkFunction) throws IOException {
        AaiResponse<AaiGetInstanceGroupsByCloudRegion> resp = aaiService
            .getInstanceGroupsByCloudRegion(cloudOwner, cloudRegionId, networkFunction);

        String httpMessage = resp.getT() != null ?
            objectMapper.writeValueAsString(resp.getT()) :
            resp.getErrorMessage();
        return new ResponseEntity<>(httpMessage, HttpStatus.valueOf(resp.getHttpCode()));
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

        Response resp = doAaiGet("network/configurations/configuration/" + configurationId, false);

        return convertResponseToResponseEntity(resp);
    }

    @RequestMapping(value = "/aai_get_service_instance_pnfs/{globalCustomerId}/{serviceType}/{serviceInstanceId}", method = RequestMethod.GET)
    public List<String> getServiceInstanceAssociatedPnfs(
        @PathVariable("globalCustomerId") String globalCustomerId,
        @PathVariable("serviceType") String serviceType,
        @PathVariable("serviceInstanceId") String serviceInstanceId) {

        return aaiService.getServiceInstanceAssociatedPnfs(globalCustomerId, serviceType, serviceInstanceId);
    }

    @RequestMapping(value = "/aai_get_pnfs/pnf/{pnf_id}", method = RequestMethod.GET)
    public ResponseEntity getSpecificPnf(@PathVariable("pnf_id") String pnfId) {
        ResponseEntity<Pnf> re;
        try {
            AaiResponse<Pnf> resp = aaiService.getSpecificPnf(pnfId);
            re = new ResponseEntity<>(resp.getT(), HttpStatus.valueOf(resp.getHttpCode()));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return re;
    }

    @RequestMapping(value = "/aai_get_tenants/{global-customer-id}/{service-type}", method = RequestMethod.GET)
    public ResponseEntity<String> viewEditGetTenantsFromServiceType(HttpServletRequest request,
        @PathVariable("global-customer-id") String globalCustomerId, @PathVariable("service-type") String serviceType) {

        ResponseEntity responseEntity;
        try {
            RoleValidator roleValidator = roleProvider.getUserRolesValidator(request);
            AaiResponse<GetTenantsResponse[]> response = aaiService
                .getTenants(globalCustomerId, serviceType, roleValidator);
            if (response.getHttpCode() == 200) {
                responseEntity = new ResponseEntity<>(objectMapper.writeValueAsString(response.getT()),
                    HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>(response.getErrorMessage(),
                    HttpStatus.valueOf(response.getHttpCode()));
            }
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>("Unable to proccess getTenants reponse",
                HttpStatus.INTERNAL_SERVER_ERROR);
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

        AaiResponse<String> resp = aaiService
            .getPNFData(globalCustomerId, serviceType, modelVersionId, modelInvariantId, cloudRegion, equipVendor,
                equipModel);
        return new ResponseEntity<>(resp.getT(), HttpStatus.valueOf(resp.getHttpCode()));
    }

    @RequestMapping(value = "/aai_getPortMirroringConfigsData", method = RequestMethod.GET)
    public Map<String, PortMirroringConfigData> getPortMirroringConfigsData(
        @RequestParam("configurationIds") List<String> configurationIds) {

        return configurationIds.stream()
            .map(id -> ImmutablePair.of(id, aaiService.getPortMirroringConfigData(id)))
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    @RequestMapping(value = "/aai_getPortMirroringSourcePorts", method = RequestMethod.GET)
    public Map<String, Object> getPortMirroringSourcePorts(
        @RequestParam("configurationIds") List<String> configurationIds) {

        return configurationIds.stream()
            .map(id -> ImmutablePair.of(id, aaiService.getPortMirroringSourcePorts(id)))
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private ResponseEntity<String> convertResponseToResponseEntity(Response resp) {
        ResponseEntity<String> respEnt;
        if (resp == null) {
            respEnt = new ResponseEntity<>("Failed to fetch data from A&AI, check server logs for details.",
                HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            respEnt = new ResponseEntity<>(resp.readEntity(String.class), HttpStatus.valueOf(resp.getStatus()));
        }
        return respEnt;
    }

    private Response getSubscribers() {

        String depth = "0";

        Response resp = doAaiGet("business/customers?subscriber-type=INFRA&depth=" + depth, false);
        if (resp != null) {
            LOGGER
                .debug(EELFLoggerDelegate.debugLogger, "<== getSubscribers() resp=" + resp.getStatusInfo().toString());
        }
        return resp;
    }

    protected Response doAaiGet(String uri, boolean xml) {
        String methodName = "getSubscriberList";
        String transId = UUID.randomUUID().toString();
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + " start");

        Response resp = null;
        try {

            resp = aaiRestInterface.RestGet(FROM_APP_ID, transId, Unchecked.toURI(uri), xml).getResponse();

        } catch (WebApplicationException e) {
            final String message = e.getResponse().readEntity(String.class);
            LOGGER.info(EELFLoggerDelegate.errorLogger, "<== " + "." + methodName + message);
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + "." + methodName + message);
        } catch (Exception e) {
            LOGGER.info(EELFLoggerDelegate.errorLogger, "<== " + "." + methodName + e.toString());
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + "." + methodName + e.toString());
        }

        return resp;
    }

    protected Response doAaiPost(String uri, String payload, boolean xml) {
        String methodName = "getSubscriberList";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + " start");

        Response resp = null;
        try {

            resp = aaiRestInterface.RestPost(FROM_APP_ID, uri, payload, xml);

        } catch (Exception e) {
            LOGGER.info(EELFLoggerDelegate.errorLogger, "<== " + "." + methodName + e.toString());
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + "." + methodName + e.toString());
        }

        return resp;
    }

    private String getComponentListPutPayload(String namedQueryId, String globalCustomerId, String serviceType,
        String serviceInstance) {
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
    private boolean isValidInstanceIdentifierType(String instanceIdentifierType) {
        return instanceIdentifierType != null
            && (    instanceIdentifierType.equalsIgnoreCase("Service Instance Id") ||
            instanceIdentifierType.equalsIgnoreCase("Service Instance Name"));
    }
}
