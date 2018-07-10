package org.onap.vid.asdc.local;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.asdc.beans.tosca.ToscaCsar;
import org.onap.vid.asdc.beans.tosca.ToscaMeta;
import org.onap.vid.asdc.beans.tosca.ToscaModel;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipFile;

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
    public Path getServiceToscaModel(UUID serviceUuid) throws AsdcCatalogException {

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
        File file = new File(classLoader.getResource(toscaModelURL).getFile());

        try {
            //using URLDecoder.decode to convert special characters from %XX to real character
            //see https://stackoverflow.com/questions/32251251/java-classloader-getresource-with-special-characters-in-path
            return Paths.get(URLDecoder.decode(file.getPath(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new GenericUncheckedException(e);
        }
    }

    /**
     * Gets the tosca model.
     *
     * @param csarInputStream the csar input stream
     * @return the tosca model
     * @throws AsdcCatalogException the asdc catalog exception
     */
    private ToscaCsar getToscaModel(InputStream csarInputStream) throws AsdcCatalogException {
        final Path csarFile;

        try {
            csarFile = Files.createTempFile("csar", ".zip");
            Files.copy(csarInputStream, csarFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new AsdcCatalogException("Caught IOException while creating CSAR", e);
        }

        try (final ZipFile csar = new ZipFile(csarFile.toFile())) {

            final InputStream toscaMetaStream = csar.getInputStream(csar.getEntry("TOSCA-Metadata/TOSCA.meta"));
            final ToscaMeta toscaMeta = new ToscaMeta.Builder(toscaMetaStream).build();
            final String entryDefinitions = toscaMeta.get("Entry-Definitions");
            final InputStream toscaParentEntryYamlStream = csar.getInputStream(csar.getEntry(entryDefinitions));

            final Yaml yaml = new Yaml();
            final ToscaModel parentModel = yaml.loadAs(toscaParentEntryYamlStream, ToscaModel.class);

            final ToscaCsar.Builder csarBuilder = new ToscaCsar.Builder(parentModel);

            for (Map<String, Map<String, String>> imports : parentModel.getImports()) {
                for (Map.Entry<String, Map<String, String>> entry : imports.entrySet()) {
                    final InputStream toscaChildEntryYamlStream = csar.getInputStream(csar.getEntry("Definitions/" + entry.getValue().get("file")));
                    final ToscaModel childModel = yaml.loadAs(toscaChildEntryYamlStream, ToscaModel.class);
                    csarBuilder.addVnf(childModel);
                }
            }

            return csarBuilder.build();
        } catch (IOException e) {
            throw new AsdcCatalogException("Caught IOException while processing CSAR", e);
        }
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
