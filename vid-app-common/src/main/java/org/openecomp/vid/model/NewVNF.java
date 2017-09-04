package org.openecomp.vid.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.vid.asdc.beans.tosca.NodeTemplate;

public class NewVNF extends NewNode {
	
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
