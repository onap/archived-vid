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

package org.openecomp.vid.controller;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.ClientBuilder;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.openecomp.vid.exceptions.VidServiceUnavailableException;
import org.openecomp.vid.model.Network;
import org.openecomp.vid.model.ServiceModel;
import org.openecomp.vid.model.VNF;
import org.openecomp.vid.model.VfModule;
import org.openecomp.vid.model.VolumeGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import org.openecomp.portalsdk.core.controller.RestrictedBaseController;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.vid.asdc.AsdcCatalogException;
import org.openecomp.vid.asdc.AsdcClient;
import org.openecomp.vid.asdc.beans.Resource;
import org.openecomp.vid.asdc.beans.Service;
import org.openecomp.vid.asdc.beans.tosca.Group;
import org.openecomp.vid.asdc.beans.tosca.NodeTemplate;
import org.openecomp.vid.asdc.beans.tosca.ToscaCsar;
import org.openecomp.vid.asdc.beans.tosca.ToscaModel;
import org.openecomp.vid.asdc.memory.InMemoryAsdcClient;
import org.openecomp.vid.asdc.rest.RestfulAsdcClient;
import org.openecomp.vid.properties.AsdcClientConfiguration;
import org.openecomp.vid.properties.AsdcClientConfiguration.AsdcClientType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class VidController.
 */
@RestController
public class VidController extends RestrictedBaseController {
	
	/** The Constant LOG. */
	private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(VidController.class);
	
	/** The app context. */
	@Autowired
	private ApplicationContext appContext;
	
	/**
	 * Gets the object mapper.
	 *
	 * @return the object mapper
	 */
	@Bean
	public ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}
	
	/**
	 * Gets the asdc client.
	 *
	 * @return the asdc client
	 */
	@Bean
	public AsdcClient getAsdcClient() {
		
		final AsdcClientConfiguration asdcClientConfig = appContext.getBean(AsdcClientConfiguration.class);

		switch (asdcClientConfig.getAsdcClientType()) {
		case IN_MEMORY:
			final InputStream asdcCatalogFile = VidController.class.getClassLoader().getResourceAsStream("catalog.json");
			final JSONTokener tokener = new JSONTokener(asdcCatalogFile);
			final JSONObject catalog = new JSONObject(tokener);

			return new InMemoryAsdcClient.Builder().catalog(catalog).build();
		case REST:

			final String protocol = asdcClientConfig.getAsdcClientProtocol();
			final String host = asdcClientConfig.getAsdcClientHost();
			final int port = asdcClientConfig.getAsdcClientPort();
			final String auth = asdcClientConfig.getAsdcClientAuth();

			try {
				final URI uri = new URI(protocol + "://" + host + ":" + port + "/");
				return new RestfulAsdcClient.Builder(ClientBuilder.newClient(), uri)
								.auth(auth)
								.build();
			} catch (URISyntaxException e) {
				throw new RuntimeException("SDC Client could not be instantiated due to a syntax error in the URI", e);
			}
			
		default:
			throw new RuntimeException(asdcClientConfig.getAsdcClientType() + " is invalid; must be one of " + Arrays.toString(AsdcClientType.values()));
		}
	}
	
	/**
	 * Gets the services.
	 *
	 * @param request the request
	 * @return the services
	 * @throws VidServiceUnavailableException the vid service unavailable exception
	 */
	@RequestMapping(value={"/rest/models/services"}, method = RequestMethod.GET)
	public Collection<Service> getServices(HttpServletRequest request) throws VidServiceUnavailableException {
		try {
			return getAsdcClient().getServices(request.getParameterMap());
		} catch (AsdcCatalogException e) {
			LOG.error("Failed to retrieve service definitions from SDC", e);
			throw new VidServiceUnavailableException("Failed to retrieve service definitions from SDC", e);
		} catch (Throwable t) {
			LOG.debug("Unexpected error while retrieving service definitions from SDC: " + t.getMessage() + ":", t);
			t.printStackTrace();
			throw new VidServiceUnavailableException("Unexpected error while retrieving service definitions from SDC: " + t.getMessage(), t);
		}
	}
	
	/**
	 * Gets the services.
	 *
	 * @param uuid the uuid
	 * @return the services
	 * @throws VidServiceUnavailableException the vid service unavailable exception
	 */
	@RequestMapping(value={"/rest/models/services/{uuid}"}, method = RequestMethod.GET)
	public ServiceModel getServices(@PathVariable("uuid") String uuid) throws VidServiceUnavailableException {
		try {
			final ServiceModel serviceModel = new ServiceModel();
			final Map<UUID, VNF> vnfs = new HashMap<UUID, VNF> ();
			final Map<UUID, Network> networks = new HashMap<UUID, Network> ();
			
			final ToscaCsar serviceCsar = getAsdcClient().getServiceToscaModel(UUID.fromString(uuid));
			final Service asdcServiceMetadata = getAsdcClient().getService(UUID.fromString(uuid));
			final ToscaModel asdcService = serviceCsar.getParent();
			
			serviceModel.setService(ServiceModel.extractService(asdcService, asdcServiceMetadata));
			
			for (Entry<String, NodeTemplate> component: asdcService.gettopology_template().getnode_templates().entrySet()) {
				final String modelCustomizationName = component.getKey();
				final NodeTemplate nodeTemplate = component.getValue();
				final String type = nodeTemplate.getType();
				
				if (type.startsWith("org.openecomp.resource.vf")) {
					final UUID vnfUuid = UUID.fromString(nodeTemplate.getMetadata().getUUID());
					final VNF vnf = VNF.extractVnf(modelCustomizationName, nodeTemplate);
					
					if (vnf.getVersion() == null) {
						final Resource vnfMetadata = getAsdcClient().getResource(UUID.fromString(nodeTemplate.getMetadata().getUUID()));
						vnf.setVersion(vnfMetadata.getVersion());
					}
					
					vnfs.put(vnfUuid, vnf);
				}
			}
			
			for (ToscaModel vnfModel : serviceCsar.getChildren()) {
				final UUID vnfUuid = UUID.fromString(vnfModel.getMetadata().getUUID());
				final VNF vnf = vnfs.get(vnfUuid);
				final Map<UUID, VfModule> vfModules = new HashMap<UUID, VfModule> ();
				final Map<UUID, VolumeGroup> volumeGroups = new HashMap<UUID, VolumeGroup> ();
				
				if (vnf == null) {
					LOG.warn("Couldn't find VNF object " + vnfUuid + ". Problem with Tosca model?");
					continue;
				}

				vnf.setInputs(vnfModel.gettopology_template().getInputs());

				for (Entry<String, Group> component : vnfModel.gettopology_template().getGroups().entrySet()) {
					final Group group = component.getValue();
					final String type = group.getType();
					
					if (type.startsWith("org.openecomp.groups.VfModule")) {
						final UUID vfModuleUuid = UUID.fromString(group.getMetadata().getVfModuleModelUUID());
						
						vfModules.put(vfModuleUuid, VfModule.extractVfModule(group));
						
						if (Boolean.valueOf(group.getProperties().get("volume_group"))) {
							volumeGroups.put(vfModuleUuid, VolumeGroup.extractVolumeGroup(group));
						}
					}
				}
				
				vnf.setVfModules(vfModules);
				vnf.setVolumeGroups(volumeGroups);
			}
			
			serviceModel.setVnfs(vnfs);
			serviceModel.setNetworks(networks);
			
			return serviceModel;
		} catch (AsdcCatalogException e) {
			LOG.error("Failed to retrieve service definitions from SDC", e);
			throw new VidServiceUnavailableException("Failed to retrieve service definitions from SDC", e);
		}
	}

	/**
	 * Gets the services view.
	 *
	 * @param request the request
	 * @return the services view
	 * @throws VidServiceUnavailableException the vid service unavailable exception
	 */
	@RequestMapping(value={"/serviceModels"}, method=RequestMethod.GET)
	public ModelAndView getServicesView(HttpServletRequest request) throws VidServiceUnavailableException {
		return new ModelAndView("serviceModels");
	}
}
