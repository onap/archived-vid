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

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.beans.Artifact;
import org.onap.vid.asdc.beans.Resource;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.asdc.parser.ToscaParserImpl;
import org.onap.vid.model.ModelConstants;
import org.onap.vid.properties.VidProperties;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import static org.onap.vid.utils.Logging.getHttpServletRequest;
import static org.onap.vid.utils.Logging.requestIdHeaderKey;
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

    /**
     * The Constant dateFormat.
     */
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

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

    ToscaParserImpl p = new ToscaParserImpl();

    /**
     * Instantiates a new restful asdc client.
     *
     * @param builder the builder
     */
    private RestfulAsdcClient(Builder builder) {
        client = builder.client;
        uri = builder.uri;
        auth = builder.auth;

        commonHeaders = new MultivaluedHashMap<String, Object>();
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
     * @see org.onap.vid.asdc.AsdcClient#getResource(java.util.UUID)
     */
    public Resource getResource(UUID uuid) throws AsdcCatalogException {

        String path = VidProperties.getPropertyWithDefault(ModelConstants.ASDC_RESOURCE_API_PATH, ModelConstants.DEFAULT_ASDC_RESOURCE_API_PATH);
        try {
            return getClient()
                    .target(uri)
                    .path(path + "/" + uuid.toString() + "/metadata")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .headers(commonHeaders)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .header(requestIdHeaderKey, getHttpServletRequest().getHeader(requestIdHeaderKey))
                    .get(Resource.class);
        } catch (ResponseProcessingException e) {
            //Couldn't convert response to Java type
            throw new AsdcCatalogException("ASDC response could not be processed", e);
        } catch (ProcessingException e) {
            //IO problems during request
            throw new AsdcCatalogException("Failed to get a response from ASDC service", e);
        } catch (WebApplicationException e) {
            //Web service returned data, but the response status wasn't a good one (i.e. non 2xx)
            throw new AsdcCatalogException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.onap.vid.asdc.AsdcClient#getResourceArtifact(java.util.UUID, java.util.UUID)
     */
    public Artifact getResourceArtifact(UUID resourceUuid, UUID artifactUuid) throws AsdcCatalogException {
        String path = VidProperties.getPropertyWithDefault(ModelConstants.ASDC_RESOURCE_API_PATH, ModelConstants.DEFAULT_ASDC_RESOURCE_API_PATH);
        try {
            return getClient()
                    .target(uri)
                    .path(path + "/" + resourceUuid + "/artifacts/" + artifactUuid)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .headers(commonHeaders)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .header(requestIdHeaderKey, getHttpServletRequest().getHeader(requestIdHeaderKey))
                    .get(Artifact.class);
        } catch (ResponseProcessingException e) {
            //Couldn't convert response to Java type
            throw new AsdcCatalogException("ASDC response could not be processed", e);
        } catch (ProcessingException e) {
            //IO problems during request
            throw new AsdcCatalogException("Failed to get a response from ASDC service", e);
        } catch (WebApplicationException e) {
            //Web service returned data, but the response status wasn't a good one (i.e. non 2xx)
            throw new AsdcCatalogException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.onap.vid.asdc.AsdcClient#getResources()
     */
    public Collection<Resource> getResources() throws AsdcCatalogException {
        String path = VidProperties.getPropertyWithDefault(ModelConstants.ASDC_RESOURCE_API_PATH, ModelConstants.DEFAULT_ASDC_RESOURCE_API_PATH);
        try {
            return getClient()
                    .target(uri)
                    .path(path)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .headers(commonHeaders)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .header(requestIdHeaderKey, getHttpServletRequest().getHeader(requestIdHeaderKey))
                    .get(new GenericType<Collection<Resource>>() {
                    });
        } catch (ResponseProcessingException e) {
            //Couldn't convert response to Java type
            throw new AsdcCatalogException("ASDC response could not be processed", e);
        } catch (ProcessingException e) {
            //IO problems during request
            throw new AsdcCatalogException("Failed to get a response from ASDC service", e);
        } catch (WebApplicationException e) {
            //Web service returned data, but the response status wasn't a good one (i.e. non 2xx)
            throw new AsdcCatalogException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.onap.vid.asdc.AsdcClient#getResources(java.util.Map)
     */
    public Collection<Resource> getResources(Map<String, String[]> filter) throws AsdcCatalogException {
        String path = VidProperties.getPropertyWithDefault(ModelConstants.ASDC_RESOURCE_API_PATH, ModelConstants.DEFAULT_ASDC_RESOURCE_API_PATH);
        WebTarget target = getClient()
                .target(uri)
                .path(path);

        for (Entry<String, String[]> filterEntry : filter.entrySet()) {
            target = target.queryParam(filterEntry.getKey(), (Object[]) filterEntry.getValue());
        }

        try {
            return target.request()
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .headers(commonHeaders)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .header(requestIdHeaderKey, getHttpServletRequest().getHeader(requestIdHeaderKey))
                    .get(new GenericType<Collection<Resource>>() {
                    });
        } catch (ResponseProcessingException e) {
            //Couldn't convert response to Java type
            throw new AsdcCatalogException("ASDC response could not be processed", e);
        } catch (ProcessingException e) {
            //IO problems during request
            throw new AsdcCatalogException("Failed to get a response from ASDC service", e);
        } catch (NotFoundException e) {
            throw e;
        } catch (WebApplicationException e) {
            //Web service returned data, but the response status wasn't a good one (i.e. non 2xx)
            throw new AsdcCatalogException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.onap.vid.asdc.AsdcClient#getResourceToscaModel(java.util.UUID)
     */
    public Path getResourceToscaModel(UUID resourceUuid) throws AsdcCatalogException {
        String path = VidProperties.getPropertyWithDefault(ModelConstants.ASDC_RESOURCE_API_PATH, ModelConstants.DEFAULT_ASDC_RESOURCE_API_PATH);
        try (final InputStream csarInputStream = (InputStream) getClient()
                .target(uri)
                .path(path + "/" + resourceUuid + "/toscaModel")
                .request(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                .headers(commonHeaders)
                .header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
                .header(requestIdHeaderKey, getHttpServletRequest().getHeader(requestIdHeaderKey))
                .get(InputStream.class)) {

            return getToscaCsar(csarInputStream);
        } catch (IOException e) {
            throw new AsdcCatalogException("Failed to retrieve resource TOSCA model from ASDC", e);
        }
    }

    /* (non-Javadoc)
     * @see org.onap.vid.asdc.AsdcClient#getService(java.util.UUID)
     */
    public Service getService(UUID uuid) throws AsdcCatalogException {

        String path = VidProperties.getPropertyWithDefault(ModelConstants.ASDC_SVC_API_PATH, ModelConstants.DEFAULT_ASDC_SVC_API_PATH);
        try {
            return getClient()
                    .target(uri)
                    .path(path + "/" + uuid.toString() + "/metadata")
                    .request(MediaType.APPLICATION_JSON)
                    .headers(commonHeaders)
                    .header(requestIdHeaderKey, getHttpServletRequest().getHeader(requestIdHeaderKey))
                    .get(Service.class);
        } catch (ResponseProcessingException e) {
            //Couldn't convert response to Java type
            throw new AsdcCatalogException("ASDC response could not be processed", e);
        } catch (ProcessingException e) {
            //IO problems during request
            throw new AsdcCatalogException("Failed to get a response from ASDC service", e);
        } catch (WebApplicationException e) {
            //Web service returned data, but the response status wasn't a good one (i.e. non 2xx)
            throw new AsdcCatalogException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.onap.vid.asdc.AsdcClient#getServiceArtifact(java.util.UUID, java.util.UUID)
     */
    public Artifact getServiceArtifact(UUID serviceUuid, UUID artifactUuid) throws AsdcCatalogException {
        String path = VidProperties.getPropertyWithDefault(ModelConstants.ASDC_SVC_API_PATH, ModelConstants.DEFAULT_ASDC_SVC_API_PATH);

        try {
            return getClient()
                    .target(uri)
                    .path(path + "/" + serviceUuid + "/artifacts/" + artifactUuid)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .headers(commonHeaders)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .header(requestIdHeaderKey, getHttpServletRequest().getHeader(requestIdHeaderKey))
                    .get(Artifact.class);
        } catch (ResponseProcessingException e) {
            //Couldn't convert response to Java type
            throw new AsdcCatalogException("ASDC response could not be processed", e);
        } catch (ProcessingException e) {
            //IO problems during request
            throw new AsdcCatalogException("Failed to get a response from ASDC service", e);
        } catch (WebApplicationException e) {
            //Web service returned data, but the response status wasn't a good one (i.e. non 2xx)
            throw new AsdcCatalogException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.onap.vid.asdc.AsdcClient#getServices()
     */
    public Collection<Service> getServices() throws AsdcCatalogException {
        String path = VidProperties.getPropertyWithDefault(ModelConstants.ASDC_SVC_API_PATH, ModelConstants.DEFAULT_ASDC_SVC_API_PATH);
        try {
            return getClient()
                    .target(uri)
                    .path(path)
                    .request()
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .headers(commonHeaders)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .header(requestIdHeaderKey, getHttpServletRequest().getHeader(requestIdHeaderKey))
                    .get(new GenericType<Collection<Service>>() {
                    });
        } catch (ResponseProcessingException e) {
            //Couldn't convert response to Java type
            throw new AsdcCatalogException("ASDC response could not be processed", e);
        } catch (ProcessingException e) {
            //IO problems during request
            throw new AsdcCatalogException("Failed to get a response from ASDC service", e);
        } catch (WebApplicationException e) {
            //Web service returned data, but the response status wasn't a good one (i.e. non 2xx)
            throw new AsdcCatalogException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.onap.vid.asdc.AsdcClient#getServices(java.util.Map)
     */
    public Collection<Service> getServices(Map<String, String[]> filter) throws AsdcCatalogException {

        String path = VidProperties.getPropertyWithDefault(ModelConstants.ASDC_SVC_API_PATH, ModelConstants.DEFAULT_ASDC_SVC_API_PATH);
        WebTarget target = getClient()
                .target(uri)
                .path(path);


        for (Entry<String, String[]> filterEntry : filter.entrySet()) {
            target = target.queryParam(filterEntry.getKey(), (Object[]) filterEntry.getValue());
        }

        try {
            return target.request()
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .headers(commonHeaders)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .header(requestIdHeaderKey, getHttpServletRequest().getHeader(requestIdHeaderKey))
                    .get(new GenericType<Collection<Service>>() {
                    });
        } catch (ResponseProcessingException e) {
            //Couldn't convert response to Java type
            throw new AsdcCatalogException("ASDC response could not be processed", e);
        } catch (ProcessingException e) {
            //IO problems during request
            throw new AsdcCatalogException("Failed to get a response from ASDC service", e);
        } catch (NotFoundException e) {
            throw e;
        } catch (WebApplicationException e) {
            //Web service returned data, but the response status wasn't a good one (i.e. non 2xx)
            throw new AsdcCatalogException(e);
        }
    }


    /* (non-Javadoc)
     * @see org.onap.vid.asdc.AsdcClient#getServiceToscaModel(java.util.UUID)
     */
    public Path getServiceToscaModel(UUID serviceUuid) throws AsdcCatalogException {
        String path = VidProperties.getPropertyWithDefault(ModelConstants.ASDC_SVC_API_PATH, ModelConstants.DEFAULT_ASDC_SVC_API_PATH);
        try {
            final InputStream csarInputStream = (InputStream) getClient()
                    .target(uri)
                    .path(path + "/" + serviceUuid + "/toscaModel")
                    .request(MediaType.APPLICATION_OCTET_STREAM_TYPE)
                    .headers(commonHeaders)
                    .header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
                    .header(requestIdHeaderKey, getHttpServletRequest().getHeader(requestIdHeaderKey))
                    .get(InputStream.class);


            return getToscaCsar(csarInputStream);
        } catch (ResponseProcessingException e) {
            //Couldn't convert response to Java type
            throw new AsdcCatalogException("ASDC response could not be processed", e);
        } catch (ProcessingException e) {
            //IO problems during request
            throw new AsdcCatalogException("Failed to get a response from ASDC service", e);
        } catch (WebApplicationException e) {
            //Web service returned data, but the response status wasn't a good one (i.e. non 2xx)
            throw new AsdcCatalogException(e);
        }
    }


    /**
     * Gets the tosca model.
     *
     * @param csarInputStream the csar input stream
     * @return the tosca model
     * @throws AsdcCatalogException the asdc catalog exception
     */
    private Path getToscaCsar(InputStream csarInputStream) throws AsdcCatalogException {
        return createTmpFile(csarInputStream);
    }
}

