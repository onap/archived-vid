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

package org.openecomp.vid.asdc;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.openecomp.vid.asdc.beans.Artifact;
import org.openecomp.vid.asdc.beans.Resource;
import org.openecomp.vid.asdc.beans.Service;
import org.openecomp.vid.asdc.beans.tosca.ToscaCsar;

/**
 * The Interface AsdcClient.
 */
public interface AsdcClient {
	
	/**
	 * Gets the resource.
	 *
	 * @param uuid the uuid
	 * @return the resource
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	public Resource getResource(UUID uuid) throws AsdcCatalogException;
	
	/**
	 * Gets the resources.
	 *
	 * @return the resources
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	public Collection<Resource> getResources() throws AsdcCatalogException;
	
	/**
	 * Gets the resources.
	 *
	 * @param filter the filter
	 * @return the resources
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	public Collection<Resource> getResources(Map<String, String[]> filter) throws AsdcCatalogException;
	
	/**
	 * Gets the resource artifact.
	 *
	 * @param resourceUuid the resource uuid
	 * @param artifactUuid the artifact uuid
	 * @return the resource artifact
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	public Artifact getResourceArtifact(UUID resourceUuid, UUID artifactUuid) throws AsdcCatalogException;
	
	/**
	 * Gets the resource tosca model.
	 *
	 * @param uuid the uuid
	 * @return the resource tosca model
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	public ToscaCsar getResourceToscaModel(UUID uuid) throws AsdcCatalogException;
	
	/**
	 * Gets the service.
	 *
	 * @param uuid the uuid
	 * @return the service
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	public Service getService(UUID uuid) throws AsdcCatalogException;
	
	/**
	 * Gets the services.
	 *
	 * @return the services
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	public Collection<Service> getServices() throws AsdcCatalogException;
	
	/**
	 * Gets the services.
	 *
	 * @param filter the filter
	 * @return the services
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	public Collection<Service> getServices(Map<String, String[]> filter) throws AsdcCatalogException;
	
	/**
	 * Gets the service artifact.
	 *
	 * @param serviceUuid the service uuid
	 * @param artifactUuid the artifact uuid
	 * @return the service artifact
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	public Artifact getServiceArtifact(UUID serviceUuid, UUID artifactUuid) throws AsdcCatalogException;
	
	/**
	 * Gets the service tosca model.
	 *
	 * @param uuid the uuid
	 * @return the service tosca model
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	public ToscaCsar getServiceToscaModel(UUID uuid) throws AsdcCatalogException;
	
	//TODO: Collect TOSCA information from CSAR
}
