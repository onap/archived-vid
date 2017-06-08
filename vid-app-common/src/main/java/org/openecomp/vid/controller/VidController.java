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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Client;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.openecomp.vid.exceptions.VidServiceUnavailableException;
import org.openecomp.vid.model.ModelUtil;
import org.openecomp.vid.model.ModelConstants;
import org.openecomp.vid.model.Network;
import org.openecomp.vid.model.ServiceModel;
import org.openecomp.vid.model.VNF;
import org.openecomp.vid.model.VfModule;
import org.openecomp.vid.model.VolumeGroup;
//import org.openecomp.vid.model.Service;
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
import org.openecomp.vid.properties.VidProperties;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * The Class VidController.
 */
@RestController
public class VidController extends RestrictedBaseController {
	
	/** The Constant LOG. */
	private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(VidController.class);
	
	/** The Constant dateFormat. */
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

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
			Client cl = null;
			if ( protocol.equalsIgnoreCase("https") ) {
				try {
					SSLContext ctx = SSLContext.getInstance("TLSv1.2");
					ctx.init(null, null, null);
					cl = ClientBuilder.newBuilder().sslContext(ctx).build();
				}
				catch ( NoSuchAlgorithmException n ) {
					throw new RuntimeException("SDC Client could not be instantiated due to unsupported protocol TLSv1.2", n);
				}
				catch ( KeyManagementException k ) {
					throw new RuntimeException("SDC Client could not be instantiated due to a key management exception", k);
				}
			}
			else {
				cl = ClientBuilder.newBuilder().build();
			}
			
			try {
				final URI uri = new URI(protocol + "://" + host + ":" + port + "/");
				return new RestfulAsdcClient.Builder(cl, uri)
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
	public Collection<org.openecomp.vid.asdc.beans.Service> getServices(HttpServletRequest request) throws VidServiceUnavailableException {
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
		String methodName = "getServices";
        LOG.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName + " start");
        boolean isNewFlow = false;
        
        String asdcModelNamespaces[] = VidProperties.getAsdcModelNamespace();
        String[] vnfTags = ModelUtil.getTags(asdcModelNamespaces, ModelConstants.VNF);
        String[] networkTags = ModelUtil.getTags(asdcModelNamespaces, ModelConstants.NETWORK);
        String[] vfModuleTags = ModelUtil.getTags(asdcModelNamespaces, ModelConstants.VF_MODULE);
        
		try {
			final ServiceModel serviceModel = new ServiceModel();
			final Map<String, VNF> vnfs = new HashMap<String, VNF> ();
			final Map<String, Network> networks = new HashMap<String, Network> ();
			
			final ToscaCsar serviceCsar = getAsdcClient().getServiceToscaModel(UUID.fromString(uuid));
			final Service asdcServiceMetadata = getAsdcClient().getService(UUID.fromString(uuid));
			final ToscaModel asdcServiceToscaModel = serviceCsar.getParent();
			
			serviceModel.setService(ServiceModel.extractService(asdcServiceToscaModel, asdcServiceMetadata));
			
			for (Entry<String, NodeTemplate> component: asdcServiceToscaModel.gettopology_template().getnode_templates().entrySet()) {
				final String modelCustomizationName = component.getKey();
				LOG.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName + " model customization name: " + modelCustomizationName);
				final NodeTemplate nodeTemplate = component.getValue();
				final String type = nodeTemplate.getType();
				
				// is it a VNF?
				if ( ModelUtil.isType (type, vnfTags) ) {
                    LOG.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName + " found node template type: " + type);

					final UUID vnfUuid = UUID.fromString(nodeTemplate.getMetadata().getUUID());
					final VNF vnf = new VNF();
					vnf.extractVnf(modelCustomizationName, nodeTemplate);
					
					if (vnf.getVersion() == null) {
						// vnf version should always be populated. The call below may not return the correct metadata since
						// uuid is not unique
						final Resource vnfMetadata = getAsdcClient().getResource(UUID.fromString(nodeTemplate.getMetadata().getUUID()));
						vnf.setVersion(vnfMetadata.getVersion());
					}
              
                    LOG.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName + " VNF commands: " + vnf.getCommands());
					vnfs.put(modelCustomizationName, vnf);
					if ( (vnf.getCustomizationUuid() != null) && (vnf.getCustomizationUuid().length() > 0 ) ) {
						isNewFlow = true;
					}
				}
				
				// is it a Network?
				if ( ModelUtil.isType (type, networkTags) ) {
					LOG.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName + " found node template type: " + type);
					final UUID networkUuid = UUID.fromString(nodeTemplate.getMetadata().getUUID());
					final Network network = new Network();
					network.extractNetwork(modelCustomizationName, nodeTemplate);
					
					if (network.getVersion() == null) {
						// network version should always be populated. The call below may not return the correct metadata since
						// uuid is not unique
						final Resource networkMetadata = getAsdcClient().getResource(UUID.fromString(nodeTemplate.getMetadata().getUUID()));
						network.setVersion(networkMetadata.getVersion());
					}
					if ( (network.getCustomizationUuid() != null) && (network.getCustomizationUuid().length() > 0 ) ) {
						isNewFlow = true;
					}
					networks.put(modelCustomizationName, network);
					
				}		
			}
			serviceModel.setVnfs(vnfs);
			serviceModel.setNetworks(networks);
			// If we see customization uuid under vnf or network, follow 1702 flow
			if ( isNewFlow ) {
				return ( getCustomizedServices(asdcServiceToscaModel, serviceModel) );
			}
			VNF vnf	= null;
			for (ToscaModel vnfModel : serviceCsar.getChildren()) {
				
				// using uuid to match should only be valid for 1610 models
				
				final String vnfUuid = (vnfModel.getMetadata().getUUID());
				// find the VNF with that uuid, uuid is not the key anymore
				for ( Entry<String, VNF> vnfComp : vnfs.entrySet() ) {
					if ( ( ( vnfComp.getValue().getUuid() ).equalsIgnoreCase(vnfUuid) ) ) {
						// found the vnf
						vnf = vnfComp.getValue();
					}
				}
				final Map<String, VfModule> vfModules = new HashMap<String, VfModule> ();
				final Map<String, VolumeGroup> volumeGroups = new HashMap<String, VolumeGroup> ();
				
				if (vnf == null) {
					LOG.warn("Couldn't find VNF object " + vnfUuid + ". Problem with Tosca model?");
					continue;
				}

				vnf.setInputs(vnfModel.gettopology_template().getInputs());

				for (Entry<String, Group> component1 : vnfModel.gettopology_template().getGroups().entrySet()) {
					final Group group = component1.getValue();
					final String type = group.getType();
					final String modelCustomizationName = component1.getKey();
					
					// VF Module Customization UUID: We may have the complete set of all VF Modules for all VNFs under service and VF Modules under each VNF.
					// Keep using the VF Modules under VNFs but we need to get the customization uuid from the service level and put them
					// under each VF module at the VNF level
					if ( ModelUtil.isType (type, vfModuleTags) ) {
					
						VfModule vfMod = VfModule.extractVfModule(modelCustomizationName, group);
						
						// Add the vf module customization uuid from the service model
						// The key of the VF Module in the service level will be the VF instance name appended to the VF Module name: 
						// <VF instance name>..<VF Module name>
						/* String normalizedVnfCustomizationName = VNF.normalizeName (vnf.getModelCustomizationName());
						org.openecomp.vid.model.Service.extractVfModuleCustomizationUUID (serviceModel.getService(), normalizedVnfCustomizationName, vfMod);*/
								
						vfModules.put(modelCustomizationName, vfMod);
						
						if ( vfMod.isVolumeGroupAllowed() ) {
								volumeGroups.put(modelCustomizationName, VolumeGroup.extractVolumeGroup(modelCustomizationName, group));
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
		catch (Exception e) {
			LOG.error("Failed to retrieve service definitions from SDC", e);
			throw new VidServiceUnavailableException("Failed to retrieve service definitions from SDC", e);
		}
	}

	public ServiceModel getCustomizedServices(ToscaModel asdcServiceToscaModel, ServiceModel serviceModel) {
		String methodName = "asdcServiceToscaModel";
        LOG.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName + " start");
        
        // asdcServiceToscaModel should have vf modules and vol groups populated at this point but 
        // they are not associated with the VNFs
		serviceModel.extractGroups(asdcServiceToscaModel);
		// Now put the vf modules and volume groups under the VNF they belong too
		serviceModel.associateGroups();
		return (serviceModel);
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
