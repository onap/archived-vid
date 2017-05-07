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

import org.openecomp.vid.asdc.beans.tosca.NodeTemplate;

/**
 * The Class Network.
 */
public class Network extends Node {
	
	/** The model customization name. */
	private String modelCustomizationName;
	
	/**
	 * Instantiates a new network.
	 */
	public Network() {
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
	 * Sets the model customization name.
	 *
	 * @param modelCustomizationName the new model customization name
	 */
	private void setModelCustomizationName(String modelCustomizationName) {
		this.modelCustomizationName = modelCustomizationName;
	}
	
	public void extractNetwork(String modelCustomizationName, NodeTemplate nodeTemplate) {
		
		super.extractNode(nodeTemplate);	
		setModelCustomizationName(modelCustomizationName);
		
	}
}
