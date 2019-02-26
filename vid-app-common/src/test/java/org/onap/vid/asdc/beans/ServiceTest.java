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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Test;

public class ServiceTest {

    private Service service;
    private Service.LifecycleState lifecycleState;
    private Collection<Artifact> artifacts;
    private Collection<SubResource> resources;


    @org.junit.Before
    public void setup() {
        service = new Service();
    }

    @Test
    public void testGetName_shouldNotNull() {
        service.setName("service1");
        String result = service.getName();
        assertNotNull(result);

    }

    @Test
    public void testGetVersion_shouldNotNull() {

        service.setVersion("version");
        String result = service.getVersion();
        assertNotNull(result);
    }

    @Test
    public void testLifeCycleState_shouldNotNull() {
        service.setLifecycleState(Service.LifecycleState.CERTIFICATION_IN_PROGRESS);
        Service.LifecycleState result = service.getLifecycleState();
        assertNotNull(result);
    }

    @Test
    public void getToscaModelURL_shouldNotNull() {
        service.setToscaModelURL("URL");
        String result = service.getToscaModelURL();
        assertNotNull(result);
    }


    @Test
    public void getCategory_shouldNotNull() {
        service.setCategory("category");
        String result = service.getCategory();
        assertNotNull(result);
    }

    @Test
    public void getLastUpdaterUserId_shouldNotNull() {
        service.set("lastUpdaterUserId");
        String result = service.getLastUpdaterUserId();
        assertNotNull(result);
    }

    @Test
    public void getLastUpdaterFullName_shouldNotNull() {
        service.setLastUpdaterFullName("lastUpdaterFullName");
        String result = service.getLastUpdaterFullName();
        assertNotNull(result);
    }


    @Test
    public void getDistributionStatus_shouldNotNull() {
        service.setDistributionStatus("distributionStatus");
        String result = service.getDistributionStatus();
        assertNotNull(result);
    }

    @Test
    public void getArtifacts_shouldNotNull() {
        artifacts = new ArrayList<>();
        Artifact artifact = new Artifact();
        artifact.setArtifactDescription("artifactDescription");
        artifacts.add(artifact);
        service.setArtifacts(artifacts);
        Collection<Artifact> result = service.getArtifacts();
        assertNotNull(result);
    }

    @Test
    public void getResources_shouldNotNull() {
        artifacts = new ArrayList<>();
        Artifact artifact = new Artifact();
        artifact.setArtifactDescription("artifactDescription");
        resources = new ArrayList<>();
        SubResource subResource = new SubResource();
        subResource.setArtifacts(artifacts);
        service.setResources(resources);
        assertNotNull(service.getResources());
    }


    @After
    public void tearDown() {
        service = null;
        lifecycleState = null;
    }
}

