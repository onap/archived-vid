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

package org.onap.vid.model;

import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.onap.vid.asdc.beans.tosca.NodeTemplate;
import org.onap.vid.controllers.VidController;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.asdc.beans.tosca.Group;
import org.onap.vid.asdc.beans.tosca.Input;

/**
 * The Class VNF.
 */
public class VNF extends Node {
	
	/** The Constant LOG. */
	private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(VNF.class);
	
	/** The Constant dateFormat. */
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

	/** The pattern used to normalize VNF names */
	final static Pattern COMPONENT_INSTANCE_NAME_DELIMETER_PATTERN = Pattern.compile("[\\.\\-]+");
	
	/** The model customization name. */
	private String modelCustomizationName;
	
	/** The vf modules. */
	private Map<String, VfModule> vfModules = new HashMap<String, VfModule>();
	
	/** The volume groups. */
	private Map<String, VolumeGroup> volumeGroups = new HashMap<String, VolumeGroup>();

	private Map<String, VfcInstanceGroup> vfcInstanceGroups = new HashMap<>();


	/**
	 * Instantiates a new vnf.
	 */
	public VNF() {
		super();
	}

	/**
	 * Gets the model customization name.
	 *
	 * @return the model customization name
	 */
	public String getModelCustomizationName() {
		return modelCustomizationName;
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
	 * Sets the vf modules.
	 *
	 * @param vfModules the vf modules
	 */
	public void setVfModules(Map<String, VfModule> vfModules) {
		this.vfModules = vfModules;
	}

	/**
	 * Gets the volume groups.
	 *
	 * @return the volume groups
	 */
	public Map<String, VolumeGroup> getVolumeGroups() {
		return volumeGroups;
	}

	/**
	 * Sets the volume groups.
	 *
	 * @param volumeGroups the volume groups
	 */
	public void setVolumeGroups(Map<String, VolumeGroup> volumeGroups) {
		this.volumeGroups = volumeGroups;
	}


	public Map<String, VfcInstanceGroup> getVfcInstanceGroups() {
		return vfcInstanceGroups;
	}

	public void setVfcInstanceGroups(Map<String, VfcInstanceGroup> vfcInstanceGroups) {
		this.vfcInstanceGroups = vfcInstanceGroups;
	}

	/**
	 * Extract vnf.
	 *
	 * @param modelCustomizationName the model customization name
	 * @param nodeTemplate the node template
	 * @return the vnf
	 */
	public void extractVnf(String modelCustomizationName, NodeTemplate nodeTemplate) {
		
		super.extractNode(nodeTemplate);	
		setModelCustomizationName(modelCustomizationName);
		
	}

	/**
	 * Sets the model customization name.
	 *
	 * @param modelCustomizationName the new model customization name
	 */
	public void setModelCustomizationName(String modelCustomizationName) {
		this.modelCustomizationName = modelCustomizationName;
	}
	/**
	 * Normalize the VNF name
	 * @param originalName
	 * @return the normalized name
	 */
	public static String normalizeName (String originalName) {

		String normalizedName = originalName.toLowerCase();
		normalizedName = COMPONENT_INSTANCE_NAME_DELIMETER_PATTERN.matcher(normalizedName).replaceAll(" ");
		String[] splitArr = null;
		
		try {
			splitArr = normalizedName.split(" ");
		}
		catch (Exception ex ) {
			return (normalizedName);
		}
		StringBuffer sb = new StringBuffer();
		if ( splitArr != null ) {
			for (String splitElement : splitArr) {
				sb.append(splitElement);
			}
			return (sb.toString());
		}
		else {
			return (normalizedName);
		}
		
	}
}
