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
package org.onap.vid.mso.rest;

import static org.onap.vid.utils.Logging.ONAP_REQUEST_ID_HEADER_KEY;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonNode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.util.security.Password;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.HttpResponseWithRequestInfo;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.changeManagement.MsoRequestDetails;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.changeManagement.WorkflowRequestDetail;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.model.SOWorkflowList;
import org.onap.vid.mso.MsoInterface;
import org.onap.vid.mso.MsoProperties;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.mso.MsoResponseWrapperInterface;
import org.onap.vid.mso.MsoUtil;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.RestObject;
import org.onap.vid.utils.Logging;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.http.HttpMethod;


/**
 * Created by pickjonathan on 21/06/2017.
 */
public class MsoRestClientNew extends RestMsoImplementation implements MsoInterface {

    /**
     * The Constant dateFormat.
     */
    public static final String X_FROM_APP_ID = "X-FromAppId";
    public static final String X_ONAP_PARTNER_NAME = "X-ONAP-PartnerName";
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
    private static final String START = " start";
    private final SyncRestClient client;
    private final String baseUrl;
    private final Map<String, String> commonHeaders;
    /**
     * The logger.
     */
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MsoRestClientNew.class);

    public MsoRestClientNew(SyncRestClient client, String baseUrl, HttpsAuthClient authClient, SystemPropertiesWrapper systemPropertiesWrapper) {
        super(authClient,systemPropertiesWrapper);
        this.client = client;
        this.baseUrl = baseUrl;
        this.commonHeaders = initCommonHeaders();
    }

    @Override
    public MsoResponseWrapper createSvcInstance(RequestDetails requestDetails, String endpoint) {
        String methodName = "createSvcInstance ";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;

        return createInstance(requestDetails, path);
    }
    
    @Override
    public MsoResponseWrapper createE2eSvcInstance(Object requestDetails, String endpoint) {
        String methodName = "createE2eSvcInstance ";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;

        return createInstance(requestDetails, path);
    }

    @Override
    public MsoResponseWrapper createVnf(RequestDetails requestDetails, String endpoint) {

        String methodName = "createVnf";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;

        return createInstance(requestDetails, path);
    }

    @Override
    public MsoResponseWrapper createNwInstance(RequestDetails requestDetails, String endpoint) {

        String methodName = "createNwInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;

        return createInstance(requestDetails, path);
    }

    @Override
    public MsoResponseWrapper createVolumeGroupInstance(RequestDetails requestDetails, String endpoint) {
        String methodName = "createVolumeGroupInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;

        return createInstance(requestDetails, path);
    }

    @Override
    public MsoResponseWrapper createVfModuleInstance(RequestDetails requestDetails, String endpoint) {
        String methodName = "createVfModuleInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;

        return createInstance(requestDetails, path);
    }

    @Override
    public MsoResponseWrapper scaleOutVFModuleInstance(RequestDetailsWrapper requestDetailsWrapper, String endpoint) {
        String methodName = "scaleOutVFModuleInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;
        return createInstance(requestDetailsWrapper, path);
    }

    @Override
    public MsoResponseWrapper createConfigurationInstance(org.onap.vid.mso.rest.RequestDetailsWrapper requestDetailsWrapper, String endpoint) {
        String methodName = "createConfigurationInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;

        return createInstance(requestDetailsWrapper, path);
    }

    @Override
    public MsoResponseWrapper deleteE2eSvcInstance(Object requestDetails, String endpoint) {
        String methodName = "deleteE2eSvcInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;
        return deleteInstance(requestDetails, path);
    }

    @Override
    public MsoResponseWrapper deleteSvcInstance(RequestDetails requestDetails, String endpoint) {
        String methodName = "deleteSvcInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;
        return deleteInstance(requestDetails, path);
    }

    @Override
    public MsoResponseWrapper unassignSvcInstance(RequestDetails requestDetails, String endpoint) {
        String methodName = "unassignSvcInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;

        HttpResponse<String> response = client.post(path, commonHeaders, requestDetails, String.class);
        return MsoUtil.wrapResponse(response);
    }

    @Override
    public MsoResponseWrapper deleteVnf(RequestDetails requestDetails, String endpoint) {
        String methodName = "deleteVnf";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;

        return deleteInstance(requestDetails, path);
    }

    @Override
    public MsoResponseWrapper deleteVfModule(RequestDetails requestDetails, String endpoint) {
        String methodName = "deleteVfModule";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;

        return deleteInstance(requestDetails, path);
    }

    @Override
    public MsoResponseWrapper deleteVolumeGroupInstance(RequestDetails requestDetails, String endpoint) {
        String methodName = "deleteVolumeGroupInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;

        return deleteInstance(requestDetails, path);
    }

    @Override
    public MsoResponseWrapper deleteNwInstance(RequestDetails requestDetails, String endpoint) {
        String methodName = "deleteNwInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;

        return deleteInstance(requestDetails, path);
    }

    @Override
    public HttpResponseWithRequestInfo<String> getOrchestrationRequest(String endpoint, boolean warpException) {
        String path = baseUrl + endpoint;

        HttpResponse<String> response = client.get(path, commonHeaders, new HashMap<>(), String.class);
        return new HttpResponseWithRequestInfo<>(response, path, HttpMethod.GET);
    }

    @Override
    public MsoResponseWrapper getOrchestrationRequest(String endpoint) {
        String path = baseUrl + endpoint;

        HttpResponse<String> response = client.get(path, commonHeaders, new HashMap<>(), String.class);
        return MsoUtil.wrapResponse(response);
    }

    @Override
    public MsoResponseWrapper getManualTasksByRequestId(String t, String sourceId, String endpoint, RestObject restObject) {
        String methodName = "getManualTasksByRequestId";
        logger.debug(methodName + START);

        try {
            String path = baseUrl + endpoint;

            HttpResponse<String> response = client.get(path, commonHeaders, new HashMap<>(), String.class);
            MsoResponseWrapper w = MsoUtil.wrapResponse(response);

            logger.debug(EELFLoggerDelegate.debugLogger, methodName + " w=" + w.getResponse());
            return w;

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, "." + methodName + e.toString());
            throw e;
        }
    }

    @Override
    public MsoResponseWrapper completeManualTask(RequestDetails requestDetails, String t, String sourceId, String endpoint, RestObject restObject) {
        String methodName = "completeManualTask";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + " calling Complete ");
        try {
            String path = baseUrl + endpoint;

            HttpResponse<String> response = client.post(path, commonHeaders, requestDetails, String.class);
            MsoResponseWrapper w = MsoUtil.wrapResponse(response);

            logger.debug(EELFLoggerDelegate.debugLogger, methodName + " w=" + w.getResponse());
            return w;

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, "." + methodName + e.toString());
            throw e;
        }
    }

    @Override
    public MsoResponseWrapper replaceVnf(org.onap.vid.changeManagement.RequestDetails requestDetails, String endpoint) {
        String methodName = "replaceVnf";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;
        return replaceInstance(requestDetails, path);
    }

    @Override
    public MsoResponseWrapper deleteConfiguration(org.onap.vid.mso.rest.RequestDetailsWrapper requestDetailsWrapper, String pmc_endpoint) {
        String methodName = "deleteConfiguration";
        logger.debug(EELFLoggerDelegate.debugLogger,
                methodName + START);
        String path = baseUrl + pmc_endpoint;

        return deleteInstance(requestDetailsWrapper, path);
    }

    @Override
    public MsoResponseWrapper setConfigurationActiveStatus(RequestDetails request, String endpoint) {
        String methodName = "setConfigurationActiveStatus";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);

        try {
            String path = baseUrl + endpoint;

            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== "
                  + methodName + " calling change configuration active status, path =[" + path + "]");
            HttpResponse<String> response = client.post(path, commonHeaders, request, String.class);
            return MsoUtil.wrapResponse(response);
        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, "." + methodName + e.toString());
            throw e;
        }
    }

    @Override
    public MsoResponseWrapper setPortOnConfigurationStatus(RequestDetails request, String endpoint) {
        String methodName = "setPortOnConfigurationStatus";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);

        try {
            String path = baseUrl + endpoint;
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== "
                + methodName + " calling change port configuration status, path =[" + path + "]");
            HttpResponse<String> response = client.post(path, commonHeaders, request, String.class);
            return MsoUtil.wrapResponse(response);
        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, "." + methodName + e.toString());
            throw e;
        }
    }

    @Override
    public MsoResponseWrapperInterface changeManagementUpdate(RequestDetailsWrapper requestDetails, String endpoint) {
        String path = baseUrl + endpoint;
        HttpResponse<String> response = client.post(path, commonHeaders, requestDetails, String.class);
        return MsoUtil.wrapResponse2(response, RequestReferencesContainer.class);
    }

    public MsoResponseWrapper replaceInstance(org.onap.vid.changeManagement.RequestDetails request, String path) {
        String methodName = "replaceInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);

        try {
            logger.debug(EELFLoggerDelegate.debugLogger, methodName + " calling Replace VNF, path =[" + path + "]");
            RequestDetailsWrapper requestDetailsWrapper = new RequestDetailsWrapper();
            requestDetailsWrapper.requestDetails = new MsoRequestDetails(request);

            HttpResponse<String> response = client.post(path, commonHeaders, requestDetailsWrapper, String.class);
            MsoResponseWrapper msoResponseWrapperObject = MsoUtil.wrapResponse(response);
            int status = msoResponseWrapperObject.getStatus();
            if (status == 202) {
                logger.debug(EELFLoggerDelegate.debugLogger, methodName +
                        ",post succeeded, msoResponseWrapperObject response:" + msoResponseWrapperObject.getResponse());
            } else {
                logger.error(EELFLoggerDelegate.debugLogger, methodName +
                        ": post failed, msoResponseWrapperObject status" + status + ", response:" + msoResponseWrapperObject.getResponse());

            }
            return msoResponseWrapperObject;

        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, "." + methodName + e.toString());
            throw e;
        }

    }

    @Override
    public MsoResponseWrapper updateVnf(org.onap.vid.changeManagement.RequestDetails requestDetails, String endpoint) {
        String methodName = "updateVnf";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);
        String path = baseUrl + endpoint;

        RequestDetailsWrapper wrapper = new RequestDetailsWrapper();
        wrapper.requestDetails = new MsoRequestDetails(requestDetails);
        return updateInstance(requestDetails, path);
    }

    public MsoResponseWrapper updateInstance(org.onap.vid.changeManagement.RequestDetails request, String path) {
        String methodName = "updateInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);

        try {
            logger.debug(EELFLoggerDelegate.debugLogger, methodName + " calling Delete, path =[" + path + "]");

            RequestDetailsWrapper requestDetailsWrapper = new RequestDetailsWrapper();
            requestDetailsWrapper.requestDetails = new MsoRequestDetails(request);
            HttpResponse<String> response = client.put(path, commonHeaders, requestDetailsWrapper, String.class);
            MsoResponseWrapper w = MsoUtil.wrapResponse(response);

            logger.debug(EELFLoggerDelegate.debugLogger, methodName + " w=" + w.getResponse());
            return w;

        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, "." + methodName + e.toString());
            throw e;
        }

    }

    public MsoResponseWrapper setServiceInstanceStatus(RequestDetails requestDetails,
        String endpoint) {
        String methodName = "activateServiceInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + " start ");
        try {
            String path = baseUrl + endpoint;
            HttpResponse<String> response = client.post(path, commonHeaders, requestDetails, String.class);
            MsoResponseWrapper w = MsoUtil.wrapResponse(response);
            logger.debug(EELFLoggerDelegate.debugLogger, methodName + " w =" + w.getResponse());
            return w;

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, "." + methodName + e.toString());
            throw e;
        }
    }

    @Override
    public MsoResponseWrapper removeRelationshipFromServiceInstance(RequestDetails requestDetails, String endpoint) {
        String methodName = "removeRelationshipFromServiceInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);

        try {
            logger.debug(EELFLoggerDelegate.debugLogger, methodName + " calling Remove relationship from service instance, path =[" + endpoint + "]");
            String path = baseUrl + endpoint;
            HttpResponse<String> response = client.post(path, commonHeaders, requestDetails, String.class);
            return MsoUtil.wrapResponse(response);
        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, "." + methodName + e.toString());
            throw e;
        }
    }

    @Override
    public MsoResponseWrapper addRelationshipToServiceInstance(RequestDetails requestDetails, String addRelationshipsPath) {
        String methodName = "addRelationshipToServiceInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);

        try {
            logger.debug(EELFLoggerDelegate.debugLogger, methodName + " calling Add relationship to service instance, path =[" + addRelationshipsPath + "]");
            String path = baseUrl + addRelationshipsPath;

            HttpResponse<String> response = client.post(path, commonHeaders, requestDetails, String.class);
            return MsoUtil.wrapResponse(response);
        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, "." + methodName + e.toString());
            throw e;
        }
    }

    @Override
    public MsoResponseWrapper invokeWorkflow(WorkflowRequestDetail workflowRequestDetail, String invokeWorkflowsPath, Map<String, String> extraHeaders) {
        String path = baseUrl + invokeWorkflowsPath;
        Map<String, String> finalHeader = new HashMap<>();

        finalHeader.putAll(commonHeaders);
        finalHeader.putAll(extraHeaders);

        RequestDetailsWrapper<WorkflowRequestDetail> requestDetailsWrapper = new RequestDetailsWrapper<>(workflowRequestDetail);

        HttpResponse<JsonNode> response = client.post(path, finalHeader, requestDetailsWrapper);
        return MsoUtil.wrapResponse(response);
    }

    @Override
    public <T> HttpResponse<T> get(String endpoint, Class<T> responseClass) {
        String path = baseUrl + endpoint;
        return client.get(path, commonHeaders, new HashMap<>(), responseClass);
    }

    @Override
    public <T> HttpResponse<T> post(String endpoint, RequestDetailsWrapper<?> requestDetailsWrapper, Class<T> responseClass) {
        String path = baseUrl + endpoint;

        return client.post(path, commonHeaders, requestDetailsWrapper, responseClass);
    }

    @Override
    public <T> HttpResponse<T> post(String endpoint, RequestDetails requestDetails, Class<T> responseClass) {
        String path = baseUrl + endpoint;

        return client.post(path, commonHeaders, requestDetails, responseClass);
    }


    public HttpResponse<SOWorkflowList> getWorkflowListByModelId(String endpoint){
        String path = baseUrl + endpoint;

        return client.get(path, commonHeaders, Maps.newHashMap(), SOWorkflowList.class);
    }

    private MsoResponseWrapper createInstance(Object request, String path) {
        String methodName = "createInstance";
        logger.debug(methodName + START);

        try {
            HttpResponse<String> response = client.post(path, commonHeaders, request, String.class);
            return MsoUtil.wrapResponse(response);
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, "." + methodName + e.toString());
            throw e;
        }
    }

    /**
     * Delete instance.
     *
     * @param request the request
     * @param path    the path
     * @return the mso response wrapper
     * @throws Exception the exception
     */
    private MsoResponseWrapper deleteInstance(Object request, String path) {
        String methodName = "deleteInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + START);

        try {
            logger.debug(EELFLoggerDelegate.debugLogger, methodName + " calling Delete, path =[" + path + "]");

            HttpResponse<String> response = client.delete(path, commonHeaders, request, String.class);
            MsoResponseWrapper w = MsoUtil.wrapResponse(response);

            logger.debug(EELFLoggerDelegate.debugLogger, methodName + " w=" + w.getResponse());
            return w;

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, "." + methodName + e.toString());
            throw e;
        }

    }

    private Map<String, String> initCommonHeaders() {
        String username = systemProperties.getProperty(MsoProperties.MSO_USER_NAME);
        String password = systemProperties.getProperty(MsoProperties.MSO_PASSWORD);
        String decrypted_password = Password.deobfuscate(password);

        String authString = username + ":" + decrypted_password;

        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);

        Map<String, String> map = new HashMap<>();
        map.put(HttpHeaders.AUTHORIZATION,  "Basic " + authStringEnc);
        map.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
        map.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        map.put(X_FROM_APP_ID, systemProperties.getProperty(SystemProperties.APP_DISPLAY_NAME));
        map.put(X_ONAP_PARTNER_NAME, "VID");
        map.put(SystemProperties.ECOMP_REQUEST_ID, Logging.extractOrGenerateRequestId());
        map.put(ONAP_REQUEST_ID_HEADER_KEY, Logging.extractOrGenerateRequestId());
        return ImmutableMap.copyOf(map);
    }

}
