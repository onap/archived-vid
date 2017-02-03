/*-
 * ============LICENSE_START=======================================================
 * VID ASDC Client
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

package org.openecomp.vid.asdc.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.zip.ZipFile;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;

import org.openecomp.vid.asdc.AsdcCatalogException;
import org.openecomp.vid.asdc.AsdcClient;
import org.openecomp.vid.asdc.beans.Artifact;
import org.openecomp.vid.asdc.beans.Resource;
import org.openecomp.vid.asdc.beans.Service;
import org.openecomp.vid.asdc.beans.tosca.ToscaCsar;
import org.openecomp.vid.asdc.beans.tosca.ToscaMeta;
import org.openecomp.vid.asdc.beans.tosca.ToscaModel;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * The Class RestfulAsdcClient.
 */
public class RestfulAsdcClient implements AsdcClient {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(RestfulAsdcClient.class.getName());
	
	/** The client. */
	private final Client client;
	
	/** The uri. */
	private final URI uri;
	
	/** The common headers. */
	private final MultivaluedHashMap<String, Object> commonHeaders;
	
	/** The auth. */
	private final String auth;
	
	/**
	 * The Class Builder.
	 */
	public static class Builder {
	
		/** The client. */
		private final Client client;
		
		/** The uri. */
		private final URI uri;
		
		/** The auth. */
		private String auth = null;
		
		/**
		 * Instantiates a new builder.
		 *
		 * @param client the client
		 * @param uri the uri
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
	 * Instantiates a new restful asdc client.
	 *
	 * @param builder the builder
	 */
	private RestfulAsdcClient(Builder builder) {
		client = builder.client;
		uri = builder.uri;
		auth = builder.auth;
		
		commonHeaders = new MultivaluedHashMap<String, Object> ();
		commonHeaders.put("X-ECOMP-InstanceID", Collections.singletonList((Object) "VID"));
		commonHeaders.put("Authorization",  Collections.singletonList((Object) (auth)));
	}
	
	/**
	 * Gets the client.
	 *
	 * @return the client
	 */
	private Client getClient() { return client; }
	
	/* (non-Javadoc)
	 * @see org.openecomp.vid.asdc.AsdcClient#getResource(java.util.UUID)
	 */
	public Resource getResource(UUID uuid) throws AsdcCatalogException {
		try {
			return getClient()
					.target(uri)
					.path("asdc/v1/catalog/resources/" + uuid.toString() + "/metadata")
					.request(MediaType.APPLICATION_JSON_TYPE)
					.headers(commonHeaders)
					.header("Content-Type", MediaType.APPLICATION_JSON)
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
	 * @see org.openecomp.vid.asdc.AsdcClient#getResources()
	 */
	public Collection<Resource> getResources() throws AsdcCatalogException {

		try {
			return getClient()
					.target(uri)
					.path("asdc/v1/catalog/resources")
					.request(MediaType.APPLICATION_JSON_TYPE)
					.headers(commonHeaders)
					.header("Content-Type", MediaType.APPLICATION_JSON)
					.get(new GenericType<Collection<Resource>> () {});
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
	 * @see org.openecomp.vid.asdc.AsdcClient#getResources(java.util.Map)
	 */
	public Collection<Resource> getResources(Map<String, String[]> filter) throws AsdcCatalogException {
		WebTarget target = getClient()
				.target(uri)
				.path("asdc/v1/catalog/resources");
		
		for (Entry<String, String[]> filterEntry : filter.entrySet()) {
			target = target.queryParam(filterEntry.getKey(), (Object []) filterEntry.getValue());
		}
		
		try {
			return target.request()
					.accept(MediaType.APPLICATION_JSON_TYPE)
					.headers(commonHeaders)
					.header("Content-Type", MediaType.APPLICATION_JSON)
					.get(new GenericType<Collection<Resource>> () {});
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
	 * @see org.openecomp.vid.asdc.AsdcClient#getResourceArtifact(java.util.UUID, java.util.UUID)
	 */
	public Artifact getResourceArtifact(UUID resourceUuid, UUID artifactUuid) throws AsdcCatalogException {
	
		try {
			return getClient()
					.target(uri)
					.path("/asdc/v1/catalog/resources/" + resourceUuid + "/artifacts/" + artifactUuid)
					.request(MediaType.APPLICATION_JSON_TYPE)
					.headers(commonHeaders)
					.header("Content-Type", MediaType.APPLICATION_JSON)
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
	 * @see org.openecomp.vid.asdc.AsdcClient#getService(java.util.UUID)
	 */
	public Service getService(UUID uuid) throws AsdcCatalogException {
		try {
			return getClient()
					.target(uri)
					.path("asdc/v1/catalog/services/" + uuid.toString() + "/metadata")
					.request(MediaType.APPLICATION_JSON)
					.headers(commonHeaders)
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
	 * @see org.openecomp.vid.asdc.AsdcClient#getServices()
	 */
	public Collection<Service> getServices() throws AsdcCatalogException {
		try {
			return getClient()
					.target(uri)
					.path("asdc/v1/catalog/services")
					.request()
					.accept(MediaType.APPLICATION_JSON_TYPE)
					.headers(commonHeaders)
					.header("Content-Type", MediaType.APPLICATION_JSON)
					.get(new GenericType<Collection<Service>> () {});
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
	 * @see org.openecomp.vid.asdc.AsdcClient#getServices(java.util.Map)
	 */
	public Collection<Service> getServices(Map<String, String[]> filter) throws AsdcCatalogException {
		WebTarget target = getClient()
				.target(uri)
				.path("asdc/v1/catalog/services");
		
			
		for (Entry<String, String[]> filterEntry : filter.entrySet()) {
			target = target.queryParam(filterEntry.getKey(), (Object []) filterEntry.getValue());
		}
		
		try {
			return target.request()
					.accept(MediaType.APPLICATION_JSON_TYPE)
					.headers(commonHeaders)
					.header("Content-Type", MediaType.APPLICATION_JSON)
					.get(new GenericType<Collection<Service>> () {});
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
	 * @see org.openecomp.vid.asdc.AsdcClient#getServiceArtifact(java.util.UUID, java.util.UUID)
	 */
	public Artifact getServiceArtifact(UUID serviceUuid, UUID artifactUuid) throws AsdcCatalogException {
		try {
			return getClient()
					.target(uri)
					.path("/asdc/v1/catalog/services/" + serviceUuid + "/artifacts/" + artifactUuid)
					.request(MediaType.APPLICATION_JSON_TYPE)
					.headers(commonHeaders)
					.header("Content-Type", MediaType.APPLICATION_JSON)
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
	 * @see org.openecomp.vid.asdc.AsdcClient#getResourceToscaModel(java.util.UUID)
	 */
	public ToscaCsar getResourceToscaModel(UUID resourceUuid) throws AsdcCatalogException {
		try (final InputStream csarInputStream = (InputStream) getClient()
				.target(uri)
				.path("/asdc/v1/catalog/resources/" + resourceUuid + "/toscaModel")
				.request(MediaType.APPLICATION_OCTET_STREAM_TYPE)
				.headers(commonHeaders)
				.header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
				.get(InputStream.class)) {

			return getToscaModel(csarInputStream);
		} catch (IOException e) {
			throw new AsdcCatalogException("Failed to retrieve resource TOSCA model from ASDC", e);
		}
	}

	/* (non-Javadoc)
	 * @see org.openecomp.vid.asdc.AsdcClient#getServiceToscaModel(java.util.UUID)
	 */
	public ToscaCsar getServiceToscaModel(UUID serviceUuid) throws AsdcCatalogException {
		try {
			final InputStream csarInputStream = (InputStream) getClient()
					.target(uri)
					.path("/asdc/v1/catalog/services/" + serviceUuid + "/toscaModel")
					.request(MediaType.APPLICATION_OCTET_STREAM_TYPE)
					.headers(commonHeaders)
					.header("Content-Type", MediaType.APPLICATION_OCTET_STREAM)
					.get(InputStream.class);
					
			return getToscaModel(csarInputStream);
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
			
			try {
				final Yaml yaml = new Yaml();
				final ToscaModel parentModel = yaml.loadAs(toscaParentEntryYamlStream, ToscaModel.class);
	
				final ToscaCsar.Builder csarBuilder = new ToscaCsar.Builder(parentModel);
				
				for (Map<String, Map<String, String>> imports : parentModel.getImports()) {
					for (Entry<String, Map<String, String>> entry : imports.entrySet()) {
						final InputStream toscaChildEntryYamlStream = csar.getInputStream(csar.getEntry("Definitions/" + entry.getValue().get("file")));
						final ToscaModel childModel = yaml.loadAs(toscaChildEntryYamlStream, ToscaModel.class);
						csarBuilder.addVnf(childModel);
					}
				}
				
				return csarBuilder.build();
			} catch (YAMLException e) {
				throw new AsdcCatalogException("Caught exception while processing TOSCA YAML", e);
			}
		} catch (IOException e) {
			throw new AsdcCatalogException("Caught IOException while processing CSAR", e);
		}
	}
}
