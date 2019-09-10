/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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

package org.onap.vid.asdc.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static org.onap.portalsdk.core.util.SystemProperties.APP_DISPLAY_NAME;
import static org.onap.vid.asdc.AsdcClient.URIS.METADATA_URL_TEMPLATE;
import static org.onap.vid.asdc.AsdcClient.URIS.TOSCA_MODEL_URL_TEMPLATE;
import static org.onap.vid.client.SyncRestClientInterface.HEADERS.AUTHORIZATION;
import static org.onap.vid.client.SyncRestClientInterface.HEADERS.CONTENT_TYPE;
import static org.onap.vid.client.SyncRestClientInterface.HEADERS.X_ECOMP_INSTANCE_ID;
import static org.onap.vid.client.UnirestPatchKt.extractRawAsString;
import static org.onap.vid.utils.Logging.REQUEST_ID_HEADER_KEY;
import static org.onap.vid.utils.Logging.logRequest;

import com.att.eelf.configuration.EELFLogger;
import com.google.common.collect.ImmutableMap;
import io.joshworks.restclient.http.HttpResponse;
import io.vavr.control.Try;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ResponseProcessingException;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.HttpResponseWithRequestInfo;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.client.SyncRestClientInterface;
import org.onap.vid.model.ModelConstants;
import org.onap.vid.properties.VidProperties;
import org.onap.vid.utils.Logging;
import org.springframework.http.HttpMethod;

public class SdcRestClient implements AsdcClient {

    private String baseUrl;
    private String path;
    private String auth;
    private static final EELFLogger LOGGER = Logging.getRequestsLogger("sdc");

    private SyncRestClientInterface syncRestClient;


    public SdcRestClient(String baseUrl, String auth, SyncRestClientInterface client) {
        this.syncRestClient = client;
        this.auth = auth;
        this.baseUrl = baseUrl;
        this.path = VidProperties.getPropertyWithDefault(ModelConstants.ASDC_SVC_API_PATH, ModelConstants.DEFAULT_ASDC_SVC_API_PATH);
    }


    @Override
    public Service getService(UUID uuid) throws AsdcCatalogException {
        String finalUrl = String.format(METADATA_URL_TEMPLATE, baseUrl, path, uuid);
        logRequest(LOGGER, HttpMethod.GET, finalUrl);

        return Try
                .of(() -> syncRestClient.get(finalUrl, prepareHeaders(auth, APPLICATION_JSON), Collections.emptyMap(), Service.class))
                .getOrElseThrow(AsdcCatalogException::new)
                .getBody();

    }

    @Override
    public Path getServiceToscaModel(UUID uuid) throws AsdcCatalogException {
        try {
            HttpResponseWithRequestInfo<InputStream> responseWithRequestInfo = getServiceInputStream(uuid, false);

            if (responseWithRequestInfo.getResponse().getStatus()>399) {
                Logging.logResponse(LOGGER, HttpMethod.GET,
                    responseWithRequestInfo.getRequestUrl(), responseWithRequestInfo.getResponse());

                String body = extractRawAsString(responseWithRequestInfo.getResponse());
                throw new AsdcCatalogException(String.format("Http bad status code: %s, body: %s",
                    responseWithRequestInfo.getResponse().getStatus(),
                    body));
            }

            final InputStream csarInputStream = responseWithRequestInfo.getResponse().getBody();
            Path toscaFilePath = createTmpFile(csarInputStream);
            LOGGER.debug("Received {} {} . Tosca file was saved at: {}",
                responseWithRequestInfo.getRequestHttpMethod().name(),
                responseWithRequestInfo.getRequestUrl(),
                toscaFilePath.toAbsolutePath());
            return toscaFilePath;
        } catch (ResponseProcessingException e) {
            //Couldn't convert response to Java type
            throw new AsdcCatalogException("ASDC response could not be processed", e);
        } catch (ProcessingException e) {
            //IO problems during request
            throw new AsdcCatalogException("Failed to get a response from ASDC service. Cause: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new AsdcCatalogException(e);
        }
    }

    @Override
    public HttpResponseWithRequestInfo<InputStream> getServiceInputStream(UUID serviceUuid, boolean warpException) {
        String finalUrl = String.format(TOSCA_MODEL_URL_TEMPLATE, baseUrl, path, serviceUuid);
        logRequest(LOGGER, HttpMethod.GET, finalUrl);
        try {
            HttpResponse<InputStream> httpResponse = syncRestClient.getStream(finalUrl, prepareHeaders(auth, APPLICATION_OCTET_STREAM), Collections.emptyMap());
            return new HttpResponseWithRequestInfo<>(httpResponse, finalUrl, HttpMethod.GET);
        }
        catch (RuntimeException exception) {
            throw warpException ? new ExceptionWithRequestInfo(HttpMethod.GET, finalUrl, exception) : exception;
        }
    }


    @Override
    public HttpResponse<String> checkSDCConnectivity() {
        String finalUrl = baseUrl + URIS.HEALTH_CHECK_ENDPOINT;

        return syncRestClient
                .get(finalUrl, prepareHeaders(auth, APPLICATION_JSON), Collections.emptyMap(), String.class);
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    private Map<String, String> prepareHeaders(String auth, String contentType) {
        return ImmutableMap.of(
                X_ECOMP_INSTANCE_ID, SystemProperties.getProperty(APP_DISPLAY_NAME),
                AUTHORIZATION, auth,
                REQUEST_ID_HEADER_KEY, Logging.extractOrGenerateRequestId(),
                CONTENT_TYPE, contentType
        );
    }

    private Path createTmpFile(InputStream csarInputStream) throws AsdcCatalogException {
        return Try
                .of(() -> tryToCreateTmpFile(csarInputStream))
                .getOrElseThrow(throwable -> new AsdcCatalogException("Caught IOException while creating CSAR", throwable));
    }

    private Path tryToCreateTmpFile(InputStream csarInputStream) throws IOException {
        Path csarFile = Files.createTempFile("csar", ".zip");
        Files.copy(csarInputStream, csarFile, StandardCopyOption.REPLACE_EXISTING);

        LOGGER.debug("Tosca file was saved at: {} ", csarFile.toAbsolutePath());

        return csarFile;
    }
}
