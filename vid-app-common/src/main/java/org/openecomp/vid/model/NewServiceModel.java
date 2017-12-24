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

package org.openecomp.vid.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openecomp.vid.asdc.beans.tosca.Group;
import org.openecomp.vid.asdc.beans.tosca.ToscaModel;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.vid.properties.VidProperties;
/**
 * The Class ServiceModel.
 */

public class NewServiceModel {

	/** The Constant LOG. */
	private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(NewServiceModel.class);
	
	/** The Constant dateFormat. */
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	/** The service. */
	private Service service;
	
	/** The vnfs. */
	private Map<String, VNF> vnfs;
	
	/** The networks. */
	private Map<String, Network> networks;

	/** 
	 * The vf modules. The VNF also has vfmodules but the vfmodules at the service level may have additional info
	 * that is not present in the VNF, like the vf module customization String 
	 */
	private Map<String, VfModule> vfModules;
	/**
	 * The volume groups. The VNF also has volume groups but the volume groups will be populated at the service level
	 * for newer models
	 */
	private Map<String, VolumeGroup> volumeGroups;

	private Map<String, PortMirroringConfig> configurations;

	private Map<String, ServiceProxy> serviceProxies;

	private Map<String, Node> pnfs;

	/**
	 * Instantiates a new service model.
	 */
	public NewServiceModel() {}
	
	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	public Service getService() {
		return service;
	}

	/**
	 * Gets the vnfs.
	 *
	 * @return the vnfs
	 */
	public Map<String, VNF> getVnfs() {
		return vnfs;
	}

	/**
	 * Gets the networks.
	 *
	 * @return the networks
	 */
	public Map<String, Network> getNetworks() {
		return networks;
	}

	/**
	 * Sets the service.
	 *
	 * @param service the new service
	 */
	public void setService(Service service) {
		this.service = service;
	}

	/**
	 * Sets the vnfs.
	 *
	 * @param vnfs the vnfs
	 */
	public void setVnfs(Map<String, VNF> vnfs) {
		this.vnfs = vnfs;
	}

	/**
	 * Sets the networks.
	 *
	 * @param networks the networks
	 */
	public void setNetworks(Map<String, Network> networks) {
		this.networks = networks;
	}
	/**
	 * Gets the vf modules.
	 *
	 * @return the vf modules
	 */
	public Map<String, VfModule> getVfModules() {
		return vfModules;
	}
	/**
	 * Gets the volume groups.
	 *
	 * @return the volume groups
	 */
	public Map<String, VolumeGroup> getVolumeGroups() {
		return volumeGroups;
	}

	public Map<String, PortMirroringConfig> getConfigurations() {
		return configurations;
	}

	public Map<String, ServiceProxy> getServiceProxies() {
		return serviceProxies;
	}

	/**
	 * Sets the vf modules.
	 *
	 * @param vfModules the vf modules
	 */


	public void setVfModules(Map<String, VfModule> vfModules) {
		this.vfModules = vfModules;
	}
	/**
	 * Sets the volume groups.
	 *
	 * @param volumeGroups the volume groups
	 */
	public void setVolumeGroups(Map<String, VolumeGroup> volumeGroups) {
		this.volumeGroups = volumeGroups;
	}


	public Map<String, Node> getPnfs() {
		return pnfs;
	}

	public void setPnfs(Map<String, Node> pnfs) {
		this.pnfs = pnfs;
	}

	/**
	 * Extract service.
	 *
	 * @param serviceToscaModel the service tosca model
	 * @param asdcServiceMetadata the asdc service metadata
	 * @return the service
	 */
	public static Service extractService(ToscaModel serviceToscaModel, org.openecomp.vid.asdc.beans.Service asdcServiceMetadata) {
		
		final Service service = new Service();
		
		service.setCategory(serviceToscaModel.getMetadata().getCategory());
		service.setInvariantUuid(serviceToscaModel.getMetadata().getInvariantUUID());
		service.setName(serviceToscaModel.getMetadata().getName());
		service.setUuid(serviceToscaModel.getMetadata().getUUID());
		service.setDescription(serviceToscaModel.getMetadata().getDescription());
		service.setServiceEcompNaming(serviceToscaModel.getMetadata().getServiceEcompNaming());
		service.setInputs(serviceToscaModel.gettopology_template().getInputs());
		//FIXME: SDC is not sending the Version with the Tosca Model for 1610 - they should send it in 1702
		//THIS IS A TEMPORARY FIX, AT SOME POINT UNCOMMENT ME
		//service.setVersion(serviceToscaModel.getMetadata().getVersion());
		service.setVersion(asdcServiceMetadata.getVersion());

		return service;
	}
	public static void extractGroups (ToscaModel serviceToscaModel,NewServiceModel serviceModel) {
		// Get the groups. The groups may duplicate the groups that are in the VNF model and have
		// additional data like the VF module customization String>
		
		final Map<String, VfModule> vfModules = new HashMap<String, VfModule> ();
		final Map<String, VolumeGroup> volumeGroups = new HashMap<String, VolumeGroup> ();
		
		String asdcModelNamespace = VidProperties.getAsdcModelNamespace();
    	String vfModuleTag = asdcModelNamespace + ModelConstants.VF_MODULE;
    	
		for (Entry<String, Group> component : serviceToscaModel.gettopology_template().getGroups().entrySet()) {
			final Group group = component.getValue();
			final String type = group.getType();
			final String customizationName = component.getKey();
			
			if (type.startsWith(vfModuleTag)) {
				VfModule vfMod = VfModule.extractVfModule(customizationName, group);
				vfModules.put(customizationName, vfMod);
				if ( vfMod.isVolumeGroupAllowed() ) {
					//volume groups have the same customization name as the vf module
					volumeGroups.put(customizationName, VolumeGroup.extractVolumeGroup(customizationName,group));
				}
			}
		}
		// add this point vfModules and volume groups are disconnected from VNF
		serviceModel.setVfModules (vfModules);
		serviceModel.setVolumeGroups (volumeGroups);
		
	}
	/**
	 * Populate the vf modules and volume groups that we may have under the service level under each VNF.
	 */
//	public void associateGroups() {
//		String methodName = "associateGroups()";
//        LOG.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName + " start");
//		// go through the vnfs, get the vnf normalized name and look for a vf module with a customization name that starts
//		// with vnf + ".."
//		String vnfCustomizationName = null;
//		String normalizedVnfCustomizationName = null;
//		String vfModuleCustomizationName = null;
//		NewVNF tmpVnf = null;
//
//		if ( ( getVnfs() != null ) && (!(getVnfs().isEmpty())) ) {
//			for (Entry<String, NewVNF> vnfComponent : getVnfs().entrySet()) {
//				vnfCustomizationName = vnfComponent.getValue().getModelCustomizationName();
//				normalizedVnfCustomizationName = VNF.normalizeName(vnfCustomizationName);
//
//				LOG.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName +
//						" VNF customizationName=" + vnfCustomizationName + "normalized customization name=" + normalizedVnfCustomizationName);
//
//				// now check to see if there is a vf module with customization name that starts with normalizedVnfCustomizationName
//
//				if (( getVfModules() != null ) && (!(getVfModules().isEmpty()))) {
//					for (Entry<String, VfModule> vfModuleComponent : getVfModules().entrySet()) {
//						vfModuleCustomizationName = vfModuleComponent.getValue().getModelCustomizationName();
//
//						LOG.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName +
//								" VF Module customizationName=" + vfModuleCustomizationName );
//						if ( vfModuleCustomizationName.startsWith(normalizedVnfCustomizationName + ".." )) {
//
//							// this vf module belongs to the VNF
//							tmpVnf = vnfComponent.getValue();
//							(tmpVnf.getVfModules()).put(vfModuleComponent.getKey(), vfModuleComponent.getValue());
//
//							LOG.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName +
//									" Associated VF Module customizationName=" + vfModuleComponent.getKey() + " with VNF customization name=" + vnfCustomizationName);
//
//							// now find if this vf module has volume groups, if so, find the volume group with the same customization name and put it under the VNF
//							if ( vfModuleComponent.getValue().isVolumeGroupAllowed() ) {
//								if (( getVolumeGroups() != null ) && (!(getVolumeGroups().isEmpty()))) {
//									if (getVolumeGroups().containsKey((vfModuleCustomizationName))) {
//										(vnfComponent.getValue().getVolumeGroups()).put(vfModuleCustomizationName, (getVolumeGroups()).get(vfModuleCustomizationName));
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
		
//	}
}
