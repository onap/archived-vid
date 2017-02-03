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
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.NotFoundException;

import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import org.openecomp.vid.asdc.beans.Artifact;
import org.openecomp.vid.asdc.beans.Resource;
import org.openecomp.vid.asdc.beans.Service;
import org.openecomp.vid.asdc.beans.Service.DistributionStatus;
import org.openecomp.vid.asdc.beans.tosca.Group;
import org.openecomp.vid.asdc.beans.tosca.Input;
import org.openecomp.vid.asdc.beans.tosca.NodeTemplate;
import org.openecomp.vid.asdc.beans.tosca.ToscaCsar;
import org.openecomp.vid.asdc.beans.tosca.ToscaModel;

/**
 * The Class BaseClientTest.
 */
public class BaseClientTest {

    /** The collector. */
    @Rule
    public ErrorCollector collector = new ErrorCollector();
    
	/**
	 * Run resource tests.
	 *
	 * @param client the client
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	protected void runResourceTests(AsdcClient client) throws AsdcCatalogException {
		final Collection<Resource> resources = client.getResources();
		
		collector.checkThat("getResources() returned nothing", resources.isEmpty(), IsEqual.equalTo(false));
		
		final Resource resource = resources.iterator().next();
		
		testResource(resource);
		
		final Resource thisResource = client.getResource(UUID.fromString(resource.getUuid()));
		
		collector.checkThat(thisResource, IsEqual.equalTo(resource));
		
		for (Resource aResource : resources) {
			if (aResource.getArtifacts() != null && !aResource.getArtifacts().isEmpty()) {
			
				final Artifact artifact = aResource.getArtifacts().iterator().next();
				
				testArtifact(artifact);
				
				final UUID resourceUuid = UUID.fromString(aResource.getUuid());
				final UUID artifactUuid = UUID.fromString(artifact.getArtifactUUID());
				final Artifact thisArtifact = client.getResourceArtifact(resourceUuid, artifactUuid);
				
				collector.checkThat(artifact, IsEqual.equalTo(thisArtifact));
			}
		}
		
		try {
			final Collection<Resource> badResources = client.getResources(Collections.singletonMap("category", new String[] {"Bad Resources"}));
			
			for (Resource badResource : badResources) {
				collector.checkThat(badResource.getCategory(), IsEqual.equalTo("Bad Resources"));
			}
		} catch (NotFoundException e) {
			//No resources of this category were found
		}
		
		try {
			final Collection<Resource> reallyBadResources = client.getResources(Collections.singletonMap("subCategory", new String[] {"Really Bad Resources"}));
			
			for (Resource reallyBadResource : reallyBadResources) {
				collector.checkThat(reallyBadResource.getSubCategory(), IsEqual.equalTo("Really Bad Resources"));
			}
		} catch (NotFoundException e) {
			//No resources of this subcategory were found
		}
		
		final ToscaCsar toscaCsar = client.getResourceToscaModel(UUID.fromString(resource.getUuid()));
		
		testToscaCsar(toscaCsar);
	}
	
	/**
	 * Run service tests.
	 *
	 * @param client the client
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	protected void runServiceTests(AsdcClient client) throws AsdcCatalogException {
		final Collection<Service> services = client.getServices();
		
		collector.checkThat("getServices() returned nothing", services.isEmpty(), IsEqual.equalTo(false));
		
		final Service service = services.iterator().next();
		
		testService(service);
		
		final Service thisService = client.getService(UUID.fromString(service.getUuid()));
		
		collector.checkThat(thisService, IsEqual.equalTo(service));
		
		for (Service aService : services) {
			if (aService.getArtifacts() != null && ! aService.getArtifacts().isEmpty()) {
				final Artifact artifact = aService.getArtifacts().iterator().next();
				
				testArtifact(artifact);
				
				final UUID serviceUuid = UUID.fromString(aService.getUuid());
				final UUID artifactUuid = UUID.fromString(artifact.getArtifactUUID());
				final Artifact thisArtifact = client.getServiceArtifact(serviceUuid, artifactUuid);
				
				collector.checkThat(artifact, IsEqual.equalTo(thisArtifact));
				break;
			}
		}

		try {
			final Collection<Service> distributedServices = client.getServices(Collections.singletonMap("distributionStatus", new String[] {"DISTRIBUTED"}));
			
			for (Service distributedService : distributedServices) {
				collector.checkThat(distributedService.getDistributionStatus(), IsEqual.equalTo(DistributionStatus.DISTRIBUTED));
			}
		} catch (NotFoundException e) {
			//No services of this distributionStatus were found
		}

		try {
			final Collection<Service> badServices = client.getServices(Collections.singletonMap("category", new String[] {"Bad Services"}));

			for (Service badService : badServices) {
				collector.checkThat(badService.getCategory(), IsEqual.equalTo("Bad Services"));
			}
		} catch (NotFoundException e) {
			//No services of this category were found
		}
		
		final ToscaCsar toscaCsar = client.getServiceToscaModel(UUID.fromString(service.getUuid()));
		
		testToscaCsar(toscaCsar);
	}
	
	/**
	 * Test service.
	 *
	 * @param service the service
	 */
	private void testService(Service service) {
		service.getArtifacts();
		service.getCategory();
		service.getDistributionStatus();
		service.getInvariantUUID();
		service.getLastUpdaterUserId();
		service.getLastUpdaterFullName();
		service.getLifecycleState();
		service.getName();
		service.getResources();
		service.getToscaModelURL();
		service.getUuid();
		service.getVersion();
	}
	
	/**
	 * Test resource.
	 *
	 * @param resource the resource
	 */
	private void testResource(Resource resource) {
		resource.getArtifacts();
		resource.getCategory();
		resource.getInvariantUUID();
		resource.getLastUpdaterUserId();
		resource.getLastUpdaterFullName();
		resource.getLifecycleState();
		resource.getName();
		resource.getResources();
		resource.getResourceType();
		resource.getSubCategory();
		resource.getToscaModel();
		resource.getToscaModelURL();
		resource.getToscaResourceName();
		resource.getUuid();
		resource.getVersion();
	}
	
	/**
	 * Test artifact.
	 *
	 * @param artifact the artifact
	 */
	private void testArtifact(Artifact artifact) {
		artifact.getArtifactChecksum();
		artifact.getArtifactDescription();
		artifact.getArtifactName();
		artifact.getArtifactTimeout();
		artifact.getArtifactType();
		artifact.getArtifactURL();
		artifact.getArtifactUUID();
		artifact.getArtifactVersion();
		artifact.getGeneratedFromUUID();
	}
	
	/**
	 * Test tosca csar.
	 *
	 * @param toscaCsar the tosca csar
	 */
	private void testToscaCsar(ToscaCsar toscaCsar) {
		testToscaModel(toscaCsar.getParent());
		
		for (ToscaModel childModel : toscaCsar.getChildren()) {
			testToscaModel(childModel);
		}
	}
	
	/**
	 * Test tosca model.
	 *
	 * @param toscaModel the tosca model
	 */
	private void testToscaModel(ToscaModel toscaModel) {
		
		toscaModel.getDescription();
		toscaModel.getMetadata().getCategory();
		toscaModel.getMetadata().getDescription();
		toscaModel.getMetadata().getInvariantUUID();
		toscaModel.getMetadata().getName();
		toscaModel.getMetadata().getType();
		toscaModel.getMetadata().gettemplate_name();
		toscaModel.getMetadata().getUUID();
		toscaModel.getMetadata().getVersion();
		toscaModel.getMetadata().isServiceEcompNaming();
		toscaModel.getMetadata().isServiceHoming();
		
		if (!toscaModel.gettopology_template().getInputs().isEmpty()) {
			final Input input = toscaModel.gettopology_template().getInputs().values().iterator().next();
			input.getDefault();
			input.getDescription();
			input.getType();
			input.toString();
		}
		
		if (!toscaModel.gettopology_template().getnode_templates().isEmpty()) {
			final NodeTemplate nodeTemplate = toscaModel.gettopology_template().getnode_templates().values().iterator().next();
			nodeTemplate.getMetadata();
			nodeTemplate.getProperties();
			nodeTemplate.getRequirements();
			nodeTemplate.getType();
		}
		
		if (!toscaModel.gettopology_template().getGroups().isEmpty()) {
			final Group group = toscaModel.gettopology_template().getGroups().values().iterator().next();
			group.getMembers();
			group.getMetadata();
			group.getType();
		}
		
		if (!toscaModel.getImports().isEmpty()) {
			for (Map<String, Map<String, String>> imports : toscaModel.getImports()) {
				imports.values().iterator().next().get("file");
			}
		}
		
		toscaModel.gettopology_template().getsubstitution_mappings().getnode_type();
		
		if (!toscaModel.gettopology_template().getsubstitution_mappings().getCapabilities().isEmpty()) {
			toscaModel.gettopology_template().getsubstitution_mappings().getCapabilities();
		}
		
		toscaModel.gettosca_definitions_version();
	}
	
	/**
	 * Test try catch asdc catalog exception.
	 */
	@Test
	public void testTryCatchAsdcCatalogException() {
		try {
			throw new AsdcCatalogException("testing");
		} catch (AsdcCatalogException e) {
			Assert.assertEquals("testing", e.getMessage());
		}
		
		final Exception cause = new Exception();
		
		try {
			throw new AsdcCatalogException("testing", cause);
		} catch (AsdcCatalogException e) {
			Assert.assertEquals("testing", e.getMessage());
			Assert.assertEquals(cause, e.getCause());
		}
	}
}
