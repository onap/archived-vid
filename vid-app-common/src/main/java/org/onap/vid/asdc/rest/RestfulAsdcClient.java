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

package org.onap.vid.asdc.rest;

import com.att.eelf.configuration.EELFLogger;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.model.ModelConstants;
import org.onap.vid.properties.VidProperties;
import org.onap.vid.utils.Logging;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.http.HttpMethod;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.UUID;

import static org.onap.vid.utils.Logging.REQUEST_ID_HEADER_KEY;
/**
 * The Class RestfulAsdcClient.
 */
@SuppressWarnings("Duplicates")
public class RestfulAsdcClient implements AsdcClient {


    /**
     * The Class Builder.
     */
    public static class Builder {

        /**
         * The client.
         */
        private final Client client;

        /**
         * The uri.
         */
        private final URI uri;

        /**
         * The auth.
         */
        private String auth = null;

        /**
         * Instantiates a new builder.
         *
         * @param client the client
         * @param uri    the uri
         */
        public Builder(Client client, URI uri) {
            this.client = client;
            this.client.register(JacksonJsonProvider.class);
            this.uri = uri;
        }

        /**
         * Auth.
         *
         * @param auth the auth
         * @return the builder
         */
        public Builder auth(String auth) {
            this.auth = auth;
            return this;
        }

        /**
         * Builds the.
         *
         * @return the restful asdc client
         */
        public RestfulAsdcClient build() {
            return new RestfulAsdcClient(this);
        }
    }

    /**
     * The Constant LOG.
     */
    static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(RestfulAsdcClient.class);

    final private static EELFLogger outgoingRequestsLogger = Logging.getRequestsLogger("asdc");

    /**
     * The client.
     */
    private final Client client;

    /**
     * The uri.
     */
    private final URI uri;

    /**
     * The common headers.
     */
    private final MultivaluedHashMap<String, Object> commonHeaders;

    /**
     * The auth.
     */
    private final String auth;

    /**
     * Instantiates a new restful asdc client.
     *
     * @param builder the builder
     */
    RestfulAsdcClient(Builder builder) {
        client = builder.client;
        uri = builder.uri;
        auth = builder.auth;

        commonHeaders = new MultivaluedHashMap<String, Object>();
        commonHeaders.put("X-ECOMP-InstanceID", Collections.singletonList((Object) (SystemProperties.getProperty(SystemProperties.APP_DISPLAY_NAME))));
        commonHeaders.put("Authorization", Collections.singletonList((Object) (auth)));
    }

    private Path createTmpFile(InputStream csarInputStream) throws AsdcCatalogException {
        final Path csarFile;
        try {
            csarFile = Files.createTempFile("csar", ".zip");
            Files.copy(csarInputStream, csarFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new AsdcCatalogException("Caught IOException while creating CSAR", e);
        }
        return csarFile;
    }

    /**
     * Gets the client.
     *
     * @return the client
     */
    private Client getClient() {
        return client;
    }

    /* (non-Javadoc)
     * @see org.onap.vid.asdc.AsdcClient#getService(java.util.UUID)
     */
    public Service getService(UUID uuid) throws AsdcCatalogException {

        String path = VidProperties.getPropertyWithDefault(
                ModelConstants.ASDC_SVC_API_PATH,
                ModelConstants.DEFAULT_ASDC_SVC_API_PATH);

        String url = uri+path + "/" + uuid.toString() + "/metadata";
        Logging.logRequest(outgoingRequestsLogger, HttpMethod.GET, url);
        try {
            Response response = getClient()
                    .target(uri)
                    .path(path + "/" + uuid.toString() + "/metadata")
                    .request(MediaType.APPLICATION_JSON)
                    .headers(commonHeaders)
                    .header(REQUEST_ID_HEADER_KEY, Logging.extractOrGenerateRequestId())
                    .get();
            Logging.logResponse(outgoingRequestsLogger, HttpMethod.GET, url, response);
            return response.readEntity(Service.class);
        } catch (ResponseProcessingException e) {
            //Couldn't convert response to Java type
            throw new AsdcCatalogException("SDC response could not be processed", e);
        } catch (ProcessingException e) {
            //IO problems during request
            throw new AsdcCatalogException("Failed to get a response from SDC service", e);
        } catch (WebApplicationException e) {
            //Web service returned data, but the response status wasn't a good one (i.e. non 2xx)
            throw new AsdcCatalogException(e);
        }
    }


    /* (non-Javadoc)
     * @see org.onap.vid.asdc.AsdcClient#getServiceToscaModel(java.util.UUID)
     */
    public Path getServiceToscaModel(UUID serviceUuid) throws AsdcCatalogException {
        String path = VidProperties.getPropertyWithDefault(ModelConstants.ASDC_SVC_API_PATH,
                ModelConstants.DEFAULT_ASDC_SVC_API_PATH);

        String url = uri+path + "/" + serviceUuid + "/toscaModel";
        Logging.logRequest(outgoingRequestsLogger, HttpMethod.GET, url);
        try {
            final InputStream csarInputStream = getClient()
                    .target(uri)
                    .path(path + "/" + serviceUuid + "/toscaModel")
                    .request(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                    .headers(commonHeaders)
                    .header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
                    .header(REQUEST_ID_HEADER_KEY, Logging.extractOrGenerateRequestId())
                    .get(InputStream.class);
            Path toscaFilePath = createTmpFile(csarInputStream);
            outgoingRequestsLogger.debug("Received {} {} . Tosca file was saved at: {}", HttpMethod.GET.name(), url, toscaFilePath.toAbsolutePath());
            return toscaFilePath;
        } catch (ResponseProcessingException e) {
            //Couldn't convert response to Java type
            throw new AsdcCatalogException("SDC response could not be processed", e);
        } catch (ProcessingException e) {
            //IO problems during request
            throw new AsdcCatalogException("Failed to get a response from SDC service. Cause: "+e.getMessage(), e);
        } catch (WebApplicationException e) {
            //Web service returned data, but the response status wasn't a good one (i.e. non 2xx)
            throw new AsdcCatalogException(e);
        }
    }

}

