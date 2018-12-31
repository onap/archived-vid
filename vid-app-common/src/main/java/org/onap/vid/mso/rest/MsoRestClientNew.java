/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 Nokia. All rights reserved.
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

import com.google.common.collect.ImmutableMap;
import io.joshworks.restclient.http.HttpResponse;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.util.security.Password;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.changeManagement.MsoRequestDetails;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.mso.*;
import org.onap.vid.utils.Logging;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by pickjonathan on 21/06/2017.
 */
public class MsoRestClientNew implements MsoInterface {

    /**
     * The Constant dateFormat.
     */
    public static final String X_FROM_APP_ID = "X-FromAppId";
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
    private static final String START = " start";
    private final SyncRestClient client;
    private final String baseUrl;
    private final Map<String, String> commonHeaders;
    /**
     * The logger.
     */
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MsoRestClientNew.class);

    public MsoRestClientNew(SyncRestClient client, String baseUrl) {
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
        HttpResponse<String> response = client.post(endpoint, commonHeaders, requestDetails, String.class);
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
    public MsoResponseWrapper getOrchestrationRequest(String t, String sourceId, String endpoint, RestObject restObject, boolean warpException) {
        String path = baseUrl + endpoint;

        HttpResponse<String> response = client.get(path, commonHeaders, new HashMap<>(), String.class);
        return MsoUtil.wrapResponse(response);
    }

    @Override
    public MsoResponseWrapper getOrchestrationRequest(String endpoint) {
        String path = baseUrl + endpoint;

        HttpResponse<String> response = client.get(path, commonHeaders, new HashMap<>(), String.class);
        return MsoUtil.wrapResponse(response);
    }

    public MsoResponseWrapper getManualTasks(String endpoint) {
        String path = baseUrl + endpoint;

        HttpResponse<String> response = client.get(path, commonHeaders, new HashMap<>(), String.class);
        return MsoUtil.wrapResponse(response);
    }

    public MsoResponseWrapper getManualTasksByRequestId(String t, String sourceId, String endpoint, RestObject restObject) {
        String methodName = "getManualTasksByRequestId";
        logger.debug(methodName + START);

        try {
            String path = baseUrl + endpoint;

            MsoResponseWrapper w =getManualTasks(path);
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
        HttpResponse<RequestReferencesContainer> response = client.post(path, commonHeaders, requestDetails, RequestReferencesContainer.class);
        return MsoUtil.wrapResponse(response);
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

    public void setServiceInstanceStatus(RequestDetails requestDetails, String t, String sourceId, String endpoint, RestObject<String> restObject) {
        String methodName = "activateServiceInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + " start ");
        try {
            String path = baseUrl + endpoint;
            HttpResponse<String> response = client.post(path, commonHeaders, requestDetails, String.class);
            MsoResponseWrapper w = MsoUtil.wrapResponse(response);
            logger.debug(EELFLoggerDelegate.debugLogger, methodName + " w =" + w.getResponse());

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
    public <T> HttpResponse<T> get(String path, Class<T> responseClass) {
        return client.get(path, commonHeaders, new HashMap<>(), responseClass);
    }

    @Override
    public <T> HttpResponse<T> post(String path, RequestDetailsWrapper<?> requestDetailsWrapper,
        Class<T> responseClass) {
        return client.post(path, commonHeaders, requestDetailsWrapper, responseClass);
    }


    private MsoResponseWrapper createInstance(Object request, String endpoint) {
        String methodName = "createInstance";
        logger.debug(methodName + START);

        try {
            HttpResponse<String> response = client.post(endpoint, commonHeaders, request, String.class);
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
        String username = SystemProperties.getProperty(MsoProperties.MSO_USER_NAME);
        String password = SystemProperties.getProperty(MsoProperties.MSO_PASSWORD);
        String decrypted_password = Password.deobfuscate(password);

        String authString = username + ":" + decrypted_password;

        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);

        Map<String, String> map = new HashMap<>();
        map.put(HttpHeaders.AUTHORIZATION,  "Basic " + authStringEnc);
        map.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
        map.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        map.put(X_FROM_APP_ID, SystemProperties.getProperty(SystemProperties.APP_DISPLAY_NAME));
        map.put(SystemProperties.ECOMP_REQUEST_ID, Logging.extractOrGenerateRequestId());
        return ImmutableMap.copyOf(map);
    }

}