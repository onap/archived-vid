/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.asdc.beans;

import java.util.UUID;

/**
 * The Class Artifact.
 */
public class Artifact {

/*
 * SDC has widened this to a String type for 1610.
	public enum Type {
		HEAT,
		HEAT_ENV,
		HEAT_VOL,
		HEAT_NET,
		HEAT_NESTED,
		HEAT_ARTIFACT,
		YANG_XML,
		VNF_CATALOG,
		VF_LICENSE,
		VENDOR_LICENSE,
		ASSET_INVENTORY_PROFILE,
		ASSET_QUERY_SPEC,
		APPC_CONFIG,
		VF_MODULES_METADATA,
		DCAE_TOSCA,
		DCAE_JSON,
		DCAE_EMF,
		DCAE_DOC,
		DCAE_BLUEPRINT,
		DCAE_EVENT,
		DCAE_INVENTORY_TOSCA,
		DCAE_INVENTORY_JSON,
		DCAE_INVENTORY_EMF,
		DCAE_INVENTORY_DOC,
		DCAE_INVENTORY_BLUEPRINT,
		DCAE_INVENTORY_EVENT,
		OTHER,
		AAI_SERVICE_MODEL //HEY! READ ME! YES, YOU!  I AM A TEMPORARY FIX, PLEASE REMOVE ME BECAUSE I AM A FRAUD.  I DON'T BELONG HERE.
						  //Warm Regards,
						  //	*The* Artifact.Type.AAI_SERVICE_MODEL Constant
	}
	*/
	
	/** The artifact name. */
	private String artifactName;
	
	/** The artifact label. */
	private String artifactLabel;
	
	/** The artifact group type. */
	private String artifactGroupType;
	
	/** The artifact type. */
	private String artifactType;
	
	/** The artifact URL. */
	private String artifactURL;
	
	/** The artifact description. */
	private String artifactDescription;
	
	/** The artifact timeout. */
	private int artifactTimeout;
	
	/** The artifact checksum. */
	private String artifactChecksum;
	
	/** The artifact UUID. */
	private String artifactUUID;
	
	/** The artifact version. */
	private String artifactVersion;
	
	/** The generated from UUID. */
	private String generatedFromUUID;
	
	/**
	 * Gets the artifact name.
	 *
	 * @return the artifact name
	 */
	public String getArtifactName() {
		return artifactName;
	}
	
	/**
	 * Gets the artifact type.
	 *
	 * @return the artifact type
	 */
	public String getArtifactType() {
		return artifactType;
	}
	/**
	 * Gets the artifact group type.
	 *
	 * @return the artifact group type
	 */
	public String getArtifactGroupType() {
		return artifactGroupType;
	}
	
	/**
	 * Gets the artifact label.
	 *
	 * @return the artifact label
	 */
	public String getArtifactLabel() {
		return artifactLabel;
	}
	/**
	 * Gets the artifact URL.
	 *
	 * @return the artifact URL
	 */
	public String getArtifactURL() {
		return artifactURL;
	}
	
	/**
	 * Gets the artifact description.
	 *
	 * @return the artifact description
	 */
	public String getArtifactDescription() {
		return artifactDescription;
	}
	
	/**
	 * Gets the artifact timeout.
	 *
	 * @return the artifact timeout
	 */
	public int getArtifactTimeout() {
		return artifactTimeout;
	}
	
	/**
	 * Gets the artifact checksum.
	 *
	 * @return the artifact checksum
	 */
	public String getArtifactChecksum() {
		return artifactChecksum;
	}
	
	/**
	 * Gets the artifact UUID.
	 *
	 * @return the artifact UUID
	 */
	public String getArtifactUUID() {
		return artifactUUID;
	}
	
	/**
	 * Gets the artifact version.
	 *
	 * @return the artifact version
	 */
	public String getArtifactVersion() {
		return artifactVersion;
	}
	
	/**
	 * Gets the generated from UUID.
	 *
	 * @return the generated from UUID
	 */
	public String getGeneratedFromUUID() {
		return generatedFromUUID;
	}
	
	/**
	 * Sets the artifact name.
	 *
	 * @param artifactName the new artifact name
	 */
	public void setArtifactName(String artifactName) {
		this.artifactName = artifactName;
	}
	
	/**
	 * Sets the artifact type.
	 *
	 * @param artifactType the new artifact type
	 */
	public void setArtifactType(String artifactType) {
		this.artifactType = artifactType;
	}
	/**
	 * Sets the artifact group type.
	 *
	 * @param artifactGroupType the new artifact group type
	 */
	public void setArtifactGroupType(String artifactGroupType) {
		this.artifactGroupType = artifactGroupType;
	}
	/**
	 * Sets the artifact label.
	 *
	 * @param artifactGroupType the new artifact label
	 */
	public void setArtifactLabel(String artifactLabel) {
		this.artifactLabel = artifactLabel;
	}
	/**
	 * Sets the artifact URL.
	 *
	 * @param artifactURL the new artifact URL
	 */
	public void setArtifactURL(String artifactURL) {
		this.artifactURL = artifactURL;
	}
	
	/**
	 * Sets the artifact description.
	 *
	 * @param artifactDescription the new artifact description
	 */
	public void setArtifactDescription(String artifactDescription) {
		this.artifactDescription = artifactDescription;
	}
	
	/**
	 * Sets the artifact timeout.
	 *
	 * @param artifactTimeout the new artifact timeout
	 */
	public void setArtifactTimeout(int artifactTimeout) {
		this.artifactTimeout = artifactTimeout;
	}
	
	/**
	 * Sets the artifact checksum.
	 *
	 * @param artifactChecksum the new artifact checksum
	 */
	public void setArtifactChecksum(String artifactChecksum) {
		this.artifactChecksum = artifactChecksum;
	}
	
	/**
	 * Sets the artifact UUID.
	 *
	 * @param artifactUUID the new artifact UUID
	 */
	public void setArtifactUUID(String artifactUUID) {
		this.artifactUUID = artifactUUID;
	}
	
	/**
	 * Sets the artifact version.
	 *
	 * @param artifactVersion the new artifact version
	 */
	public void setArtifactVersion(String artifactVersion) {
		this.artifactVersion = artifactVersion;
	}
	
	/**
	 * Sets the generated from UUID.
	 *
	 * @param generatedFromUUID the new generated from UUID
	 */
	public void setGeneratedFromUUID(String generatedFromUUID) {
		this.generatedFromUUID = generatedFromUUID;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final UUID uuid = UUID.fromString(getArtifactUUID());
		
		return uuid.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this)
            return true;
		if (!(o instanceof Artifact))
            return false;
		
		final Artifact artifact = (Artifact) o;
		
		return (artifact.getArtifactUUID().equals(getArtifactUUID()));
	}
}
