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

import java.util.Map;
import java.util.UUID;

import org.openecomp.vid.asdc.beans.tosca.ToscaModel;

/**
 * The Class ServiceModel.
 */
public class ServiceModel {

	/** The service. */
	private Service service;
	
	/** The vnfs. */
	private Map<UUID, VNF> vnfs;
	
	/** The networks. */
	private Map<UUID, Network> networks;

	/**
	 * Instantiates a new service model.
	 */
	public ServiceModel() {}
	
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
	public Map<UUID, VNF> getVnfs() {
		return vnfs;
	}

	/**
	 * Gets the networks.
	 *
	 * @return the networks
	 */
	public Map<UUID, Network> getNetworks() {
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
	public void setVnfs(Map<UUID, VNF> vnfs) {
		this.vnfs = vnfs;
	}

	/**
	 * Sets the networks.
	 *
	 * @param networks the networks
	 */
	public void setNetworks(Map<UUID, Network> networks) {
		this.networks = networks;
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
		service.setInputs(serviceToscaModel.gettopology_template().getInputs());
		
		//FIXME: ASDC is not sending the Version with the Tosca Model for 1610 - they should send it in 1702
		//THIS IS A TEMPORARY FIX, AT SOME POINT UNCOMMENT ME
		//service.setVersion(serviceToscaModel.getMetadata().getVersion());
		service.setVersion(asdcServiceMetadata.getVersion());

		return service;
	}
}
