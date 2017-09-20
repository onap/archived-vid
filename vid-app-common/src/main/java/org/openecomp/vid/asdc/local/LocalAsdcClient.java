package org.openecomp.vid.asdc.local;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openecomp.vid.asdc.AsdcCatalogException;
import org.openecomp.vid.asdc.AsdcClient;
import org.openecomp.vid.asdc.beans.Artifact;
import org.openecomp.vid.asdc.beans.Resource;
import org.openecomp.vid.asdc.beans.Service;
import org.openecomp.vid.asdc.beans.tosca.ToscaCsar;
import org.openecomp.vid.asdc.beans.tosca.ToscaMeta;
import org.openecomp.vid.asdc.beans.tosca.ToscaModel;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.zip.ZipFile;

/**
 * The Class LocalAsdcClient.
 */
public class LocalAsdcClient implements AsdcClient {


    /**
     * The catalog.
     */
    private final JSONObject catalog;

    /**
     * The mapper.
     */
    private final ObjectMapper mapper;

    /**
     * The Class Builder.
     */
    public static class Builder {

        /**
         * The catalog.
         */
        private JSONObject catalog = new JSONObject()
                .put("resources", new JSONObject())
                .put("services", new JSONObject());

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
        public org.openecomp.vid.asdc.local.LocalAsdcClient.Builder catalog(JSONObject catalog) {
            this.catalog = catalog;
            return this;
        }

        /**
         * Mapper.
         *
         * @param mapper the mapper
         * @return the builder
         */
        public org.openecomp.vid.asdc.local.LocalAsdcClient.Builder mapper(ObjectMapper mapper) {
            this.mapper = mapper;
            return this;
        }

        /**
         * Builds the.
         *
         * @return the in local sdc client
         */
        public org.openecomp.vid.asdc.local.LocalAsdcClient build() {
            return new org.openecomp.vid.asdc.local.LocalAsdcClient(this);
        }
    }

    /**
     * Instantiates a new in local sdc client.
     *
     * @param builder the builder
     */
    private LocalAsdcClient(org.openecomp.vid.asdc.local.LocalAsdcClient.Builder builder) {
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
     * @see org.openecomp.vid.asdc.AsdcClient#getResource(java.util.UUID)
     */
    public Resource getResource(UUID uuid) throws AsdcCatalogException {
        final JSONObject resource = getCatalog().getJSONObject("resources")
                .getJSONObject(uuid.toString());
        return convert(resource, Resource.class);
    }

    /* (non-Javadoc)
     * @see org.openecomp.vid.asdc.AsdcClient#getResources()
     */
    public Collection<Resource> getResources() throws AsdcCatalogException {
        final Collection<Resource> resources = new LinkedList<Resource>();

        for (String key : getCatalog().getJSONObject("resources").keySet()) {
            final JSONObject json = getCatalog().getJSONObject("resources").getJSONObject(key);
            final Resource resource = convert(json, Resource.class);
            resources.add(resource);
        }

        return resources;
    }

    /* (non-Javadoc)
     * @see org.openecomp.vid.asdc.AsdcClient#getResources(java.util.Map)
     */
    public Collection<Resource> getResources(Map<String, String[]> filter) throws AsdcCatalogException {
        final Collection<Resource> resources = new LinkedList<Resource>();

        for (String key : getCatalog().getJSONObject("resources").keySet()) {
            final JSONObject json = getCatalog().getJSONObject("resources").getJSONObject(key);

            boolean filterMatch = true;

            for (Map.Entry<String, String[]> entry : filter.entrySet()) {
                for (int i = 0; i < entry.getValue().length; i++) {
                    if (!json.getString(entry.getKey()).equals(entry.getValue()[i])) {
                        filterMatch = false;
                        break;
                    }
                }
            }

            if (filterMatch) resources.add(convert(json, Resource.class));
        }

        return resources;
    }

    /* (non-Javadoc)
     * @see org.openecomp.vid.asdc.AsdcClient#getService(java.util.UUID)
     */
    public Service getService(UUID uuid) throws AsdcCatalogException {

        JSONObject serviceJsonObject = null;
        final JSONArray categoryJsonArray = getCatalog().getJSONArray("services");

        for (int i = 0; i < categoryJsonArray.length() ; i++) {
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
     * @see org.openecomp.vid.asdc.AsdcClient#getServices()
     */
    public Collection<Service> getServices() throws AsdcCatalogException {
        final Collection<Service> services = new LinkedList<Service>();

        JSONArray servicesArr = getCatalog().getJSONArray("services");

        for (Object objService : servicesArr) {
            JSONObject jsonServiceItem = (JSONObject) objService;
            final Service service = convert(jsonServiceItem, Service.class);
            services.add(service);
        }

        return services;
    }

    /* (non-Javadoc)
     * @see org.openecompt.vid.asdc.AsdcClient#getServices(java.util.Map)
     */
    public Collection<Service> getServices(Map<String, String[]> filter) throws AsdcCatalogException {
        final Collection<Service> services = new LinkedList<Service>();

        JSONArray catalogServices = catalog.getJSONArray("services");

        for (int i = 0; i < catalogServices.length(); i++) {

            JSONObject serviceJson = catalogServices.getJSONObject(i);

            boolean filterMatch = true;

            for (Map.Entry<String, String[]> entry : filter.entrySet()) {
                for (int j = 0; j < entry.getValue().length; j++) {
                    if (!serviceJson.getString(entry.getKey()).equals(entry.getValue()[j])) {
                        filterMatch = false;
                        break;
                    }
                }
            }
            if (filterMatch) services.add(convert(serviceJson, Service.class));
        }
        return services;
    }

    /* (non-Javadoc)
     * @see org.openecomp.vid.asdc.AsdcClient#getResourceArtifact(java.util.UUID, java.util.UUID)
     */
    public Artifact getResourceArtifact(UUID resourceUuid, UUID artifactUuid) throws AsdcCatalogException {
        final  JSONArray artifacts = getCatalog().getJSONObject("resources")
                .getJSONObject(resourceUuid.toString())
                .getJSONArray("artifacts");

        for (int i = 0; i < artifacts.length(); i++) {
            final JSONObject artifact = artifacts.getJSONObject(i);

            if (artifact.getString("artifactUUID").equals(artifactUuid.toString())) {
                return convert(artifact, Artifact.class);
            }
        }

        return null;
    }

    /* (non-Javadoc)
     * @see org.openecomp.vid.asdc.AsdcClient#getServiceArtifact(java.util.UUID, java.util.UUID)
     */
    public Artifact getServiceArtifact(UUID serviceUuid, UUID artifactUuid) throws AsdcCatalogException {
        final JSONArray artifacts = getCatalog().getJSONObject("services")
                .getJSONObject(serviceUuid.toString())
                .getJSONArray("artifacts");

        for (int i = 0; i < artifacts.length(); i++) {
            final JSONObject artifact = artifacts.getJSONObject(i);

            if (artifact.getString("artifactUUID").equals(artifactUuid.toString())) {
                return convert(artifact, Artifact.class);
            }
        }

        return null;
    }

    /* (non-Javadoc)
     * @see org.openecomp.vid.asdc.AsdcClient#getResourceToscaModel(java.util.UUID)
     */
    public Path getResourceToscaModel(UUID resourceUuid) throws AsdcCatalogException {
        final String toscaModelURL = getCatalog().getJSONObject("resources")
                .getJSONObject(resourceUuid.toString())
                .getString("toscaModelURL");


        final InputStream toscaModelStream = getClass().getClassLoader().getResourceAsStream(toscaModelURL);

        if (toscaModelStream == null) return null;

        return null;//getToscaModel(toscaModelStream);
    }

    /* (non-Javadoc)
     * @see org.openecomp.vid.asdc.AsdcClient#getServiceToscaModel(java.util.UUID)
     */
    public Path getServiceToscaModel(UUID serviceUuid) throws AsdcCatalogException {

        String toscaModelURL = null;

        final JSONArray categoryJsonArray = getCatalog().getJSONArray("services");

        for (int i = 0; i < categoryJsonArray.length() ; i++) {

            JSONObject jsonServiceObject = categoryJsonArray.getJSONObject(i);
            if (jsonServiceObject.get("uuid").equals(serviceUuid.toString())) {
                toscaModelURL = jsonServiceObject.getString("toscaModelURL");
            }
        }

        final InputStream toscaModelStream = getClass().getClassLoader().getResourceAsStream(toscaModelURL);

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(toscaModelURL).getFile());
        Path path = Paths.get(file.getPath());

        if (toscaModelStream == null) return null;

        return path;
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

}
