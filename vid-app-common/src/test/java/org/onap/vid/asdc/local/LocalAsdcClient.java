/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia. All rights reserved.
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

package org.onap.vid.asdc.local;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.joshworks.restclient.http.HttpResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;
import org.onap.vid.aai.HttpResponseWithRequestInfo;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.exceptions.GenericUncheckedException;

/**
 * The Class LocalAsdcClient.
 */
public class LocalAsdcClient implements AsdcClient {


    public static final String SERVICES = "services";
    /**
     * The catalog.
     */
    private final JSONObject catalog;

    /**
     * The mapper.
     */
    private final ObjectMapper mapper;

    /**
     * Instantiates a new in local sdc client.
     *
     * @param builder the builder
     */
    public LocalAsdcClient(org.onap.vid.asdc.local.LocalAsdcClient.Builder builder) {
        catalog = builder.catalog;
        mapper = builder.mapper;
    }

    /**
     * Gets the catalog.
     *
     * @return the catalog
     */
    private JSONObject getCatalog() {
        return catalog;
    }

    /**
     * Gets the mapper.
     *
     * @return the mapper
     */
    private ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * Convert.
     *
     * @param <T>   the generic type
     * @param json  the json
     * @param clazz the clazz
     * @return the t
     * @throws AsdcCatalogException the sdc catalog exception
     */
    private <T> T convert(JSONObject json, Class<T> clazz) throws AsdcCatalogException {
        try {
            return getMapper().readValue(json.toString(), clazz);
        } catch (JsonParseException e) {
            throw new AsdcCatalogException("Failed to parse SDC response (bad data)", e);
        } catch (JsonMappingException e) {
            throw new AsdcCatalogException("Failed to map SDC response to internal VID data structure(s)", e);
        } catch (IOException e) {
            throw new AsdcCatalogException("Failed to get a response from SDC", e);
        }
    }

    /* (non-Javadoc)
     * @see org.onap.vid.asdc.AsdcClient#getService(java.util.UUID)
     */
    public Service getService(UUID uuid) throws AsdcCatalogException {

        JSONObject serviceJsonObject = null;
        final JSONArray categoryJsonArray = getCatalog().getJSONArray(SERVICES);

        for (int i = 0; i < categoryJsonArray.length(); i++) {
            JSONObject jsonServiceObject = categoryJsonArray.getJSONObject(i);
            if (jsonServiceObject.get("uuid").equals(uuid.toString())) {
                serviceJsonObject = jsonServiceObject;
                break;
            }
        }

        if (serviceJsonObject != null)
            return convert(serviceJsonObject, Service.class);
        else return null;
    }

    /* (non-Javadoc)
     * @see org.onap.vid.asdc.AsdcClient#getServiceToscaModel(java.util.UUID)
     */
    public Path getServiceToscaModel(UUID serviceUuid) {

        String toscaModelURL = null;

        final JSONArray categoryJsonArray = getCatalog().getJSONArray(SERVICES);

        for (int i = 0; i < categoryJsonArray.length(); i++) {

            JSONObject jsonServiceObject = categoryJsonArray.getJSONObject(i);
            if (jsonServiceObject.get("uuid").equals(serviceUuid.toString())) {
                toscaModelURL = jsonServiceObject.getString("toscaModelURL");
            }
        }
        if (toscaModelURL == null) {
            return null;
        }
        ClassLoader classLoader = getClass().getClassLoader();

        try {
            File file = new File(classLoader.getResource(toscaModelURL).getFile());
            //using URLDecoder.decode to convert special characters from %XX to real character
            //see https://stackoverflow.com/questions/32251251/java-classloader-getresource-with-special-characters-in-path
            return Paths.get(URLDecoder.decode(file.getPath(), "UTF-8"));
        } catch (RuntimeException | UnsupportedEncodingException e) {
            throw new GenericUncheckedException("Can't find " + toscaModelURL, e);
        }
    }

    @Override
    public HttpResponse<String> checkSDCConnectivity() {
        return HttpResponse.fallback("");
    }

    @Override
    public HttpResponseWithRequestInfo<InputStream> getServiceInputStream(UUID serviceUuid, boolean warpException) {
        return null;
    }

    @Override
    public String getBaseUrl(){
        return "";
    }

    /**
     * The Class Builder.
     */
    public static class Builder {

        /**
         * The catalog.
         */
        private JSONObject catalog = new JSONObject()
                .put("resources", new JSONObject())
                .put(SERVICES, new JSONObject());

        /**
         * The mapper.
         */
        private ObjectMapper mapper = new ObjectMapper();

        /**
         * Instantiates a new builder.
         */
        public Builder() {
        }

        /**
         * Catalog.
         *
         * @param catalog the catalog
         * @return the builder
         */
        public org.onap.vid.asdc.local.LocalAsdcClient.Builder catalog(JSONObject catalog) {
            this.catalog = catalog;
            return this;
        }

        /**
         * Mapper.
         *
         * @param mapper the mapper
         * @return the builder
         */
        public org.onap.vid.asdc.local.LocalAsdcClient.Builder mapper(ObjectMapper mapper) {
            this.mapper = mapper;
            return this;
        }

        /**
         * Builds the.
         *
         * @return the in local sdc client
         */
        public org.onap.vid.asdc.local.LocalAsdcClient build() {
            return new org.onap.vid.asdc.local.LocalAsdcClient(this);
        }
    }

}
