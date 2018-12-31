package org.onap.vid.model;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class NewVNF extends NewNode {

	/** The pattern used to normalize VNF names */
	static final Pattern COMPONENT_INSTANCE_NAME_DELIMETER_PATTERN = Pattern.compile("[\\.\\-]+");
	
	/** The model customization name. */
	private String modelCustomizationName;
	
	/** The vf modules. */
	private Map<String, VfModule> vfModules = new HashMap<>();
	
	/** The volume groups. */
	private Map<String, VolumeGroup> volumeGroups = new HashMap<>();
	
	/**
	 * Instantiates a newvnf.
	 */
	public NewVNF() {
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
			return VNF.normalizeName(originalName);
	}
}
