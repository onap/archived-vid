/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 Nokia. All rights reserved.
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
package org.onap.vid.mso.rest;

import com.xebialabs.restito.server.StubServer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.controllers.MsoController;
import org.onap.vid.mso.MsoInterface;
import org.onap.vid.mso.MsoProperties;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.mso.MsoResponseWrapperInterface;
import org.onap.vid.mso.RestObject;
import org.onap.vid.properties.BaseUrlProvider;

import static org.mockito.Mockito.doReturn;

public class MsoRestClientNewTest {

    private static StubServer server;
    private static StubServer securedServer;
    private static Properties props = new Properties();
    private static String msoCreateServiceInstanceJson;
    private final static String CREATE_INSTANCE_RESPONSE_STR =
        "{\"requestReferences\":{\"instanceId\":\"baa13544-0e95-4644-9565-9a198a29a294\","
            + "\"requestId\":\"a42a1a35-3d63-4629-bbe0-4989fa7414cb\"}}";
    private final static String SERVICE_INSTANCE_ID = "12345";
    private static final String SAMPLE_VNF_INSTANCE_ID = "111";
    private static final String SAMPLE_VNF_MODULE_ID = "987";
    private static final String SAMPLE_NETWORK_INSTANCE_ID = "666";
    private static final String SAMPLE_CONFIGURATION_ID = "997";
    private static final String SAMPLE_REQUEST_ID = "7777";


    private static BaseUrlProvider baseUrlProvider =Mockito.mock(BaseUrlProvider.class);

    @BeforeClass
    public static void start() throws IOException {
        server = new StubServer().run();
        securedServer = new StubServer().secured().run();

        Path resourceDirectory =
            Paths.get("src", "test", "resources", "WEB-INF", "conf", "system.properties");
        try(InputStream is = Files.newInputStream(resourceDirectory)) {
            props.load(is);
        }

        Path msoServiceInstantiationJsonFilePath =
            Paths.get("src", "test", "resources", "payload_jsons", "mso_service_instantiation.json");
        msoCreateServiceInstanceJson =
            String.join("\n", Files.readAllLines(msoServiceInstantiationJsonFilePath));
        doReturn("http://localhost:"+ server.getPort()).when(baseUrlProvider).getBaseUrl();
    }

    @AfterClass
    public static void stop() {
        server.stop();
        securedServer.stop();
    }


    @Test
    public void testCreateSvcInstance() throws Exception {
        String endpoint = props.getProperty(MsoProperties.MSO_REST_API_CONFIGURATIONS);
        endpoint = endpoint.replace(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        try(MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
            server,
            endpoint,
            HttpStatus.ACCEPTED_202,
            CREATE_INSTANCE_RESPONSE_STR,CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executePost(msoCreateServiceInstanceJson, msoRestClient()::createSvcInstance);
        }
    }

    @Test
    public void testCreateVnf() throws Exception {
        String endpoint = props.getProperty(MsoProperties.MSO_REST_API_VNF_INSTANCE);
        endpoint = endpoint.replace(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        try(MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
            server,
            endpoint,
            HttpStatus.ACCEPTED_202,
            CREATE_INSTANCE_RESPONSE_STR,CREATE_INSTANCE_RESPONSE_STR)) {
            
            closure.executePost(msoCreateServiceInstanceJson, msoRestClient()::createVnf);
        }
    }

    @Test
    public void testCreateNwInstance() throws Exception {
        String endpoint = props.getProperty(MsoProperties.MSO_REST_API_NETWORK_INSTANCE);
        String nw_endpoint = endpoint.replaceFirst(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        try(MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
            server,
            nw_endpoint,
            HttpStatus.ACCEPTED_202,
            CREATE_INSTANCE_RESPONSE_STR,CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executePost(msoCreateServiceInstanceJson, msoRestClient()::createNwInstance);
        }
    }

    @Test
    public void testCreateVolumeGroupInstance() throws Exception {
        String endpoint = props.getProperty(MsoProperties.MSO_REST_API_VOLUME_GROUP_INSTANCE);
        String vnf_endpoint = endpoint.replaceFirst(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        vnf_endpoint = vnf_endpoint.replaceFirst(MsoController.VNF_INSTANCE_ID, SAMPLE_VNF_INSTANCE_ID);
        try(MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
            server,
            vnf_endpoint,
            HttpStatus.ACCEPTED_202,
            CREATE_INSTANCE_RESPONSE_STR,CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executePost(msoCreateServiceInstanceJson, msoRestClient()::createVolumeGroupInstance);
        }
    }

    @Test
    public void testCreateVfModuleInstance() throws Exception {
        String endpoint = props.getProperty(MsoProperties.MSO_REST_API_VF_MODULE_INSTANCE);
        String partial_endpoint = endpoint.replaceFirst(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        String vf_module_endpoint =
            partial_endpoint.replaceFirst(MsoController.VNF_INSTANCE_ID, SAMPLE_VNF_INSTANCE_ID);


        try(MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
            server,
            vf_module_endpoint,
            HttpStatus.ACCEPTED_202,
            CREATE_INSTANCE_RESPONSE_STR,CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executePost(msoCreateServiceInstanceJson, msoRestClient()::createVfModuleInstance);
        }
    }

    @Test
    public void testCreateConfigurationInstance() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetailsWrapper requestDetails = null;
        String endpoint = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.createConfigurationInstance(requestDetails, endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeleteSvcInstance() throws Exception {
        String endpoint = props.getProperty(MsoProperties.MSO_REST_API_SVC_INSTANCE);
        endpoint = endpoint.replaceFirst(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);


        try(MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
            server,
            endpoint,
            HttpStatus.NO_CONTENT_204,
            CREATE_INSTANCE_RESPONSE_STR,CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executeDelete(msoCreateServiceInstanceJson, msoRestClient()::deleteSvcInstance);
        }
    }

    @Test
    public void testDeleteVnf() throws Exception {
        String endpoint = props.getProperty(MsoProperties.MSO_REST_API_VNF_INSTANCE);
        endpoint = endpoint.replaceFirst(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);

        try(MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
            server,
            endpoint,
            HttpStatus.NO_CONTENT_204,
            CREATE_INSTANCE_RESPONSE_STR,CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executeDelete(msoCreateServiceInstanceJson, msoRestClient()::deleteVnf);
        }
    }

    @Test
    public void testDeleteVfModule() throws Exception {
        String endpoint = props.getProperty(MsoProperties.MSO_REST_API_VF_MODULE_INSTANCE);
        String part_endpoint = endpoint.replaceFirst(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        String vf_modules_endpoint = part_endpoint.replaceFirst(MsoController.VNF_INSTANCE_ID, SAMPLE_VNF_INSTANCE_ID);
        String delete_vf_endpoint = vf_modules_endpoint + '/' + SAMPLE_VNF_MODULE_ID;

        try(MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
            server,
            delete_vf_endpoint,
            HttpStatus.NO_CONTENT_204,
            CREATE_INSTANCE_RESPONSE_STR,CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executeDelete(msoCreateServiceInstanceJson, msoRestClient()::deleteVfModule);
        }
    }

    @Test
    public void testDeleteVolumeGroupInstance() throws Exception {
        String endpoint = props.getProperty(MsoProperties.MSO_REST_API_VOLUME_GROUP_INSTANCE);
        String svc_endpoint = endpoint.replaceFirst(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        String vnf_endpoint = svc_endpoint.replaceFirst(MsoController.VNF_INSTANCE_ID, SAMPLE_VNF_INSTANCE_ID);
        String delete_volume_group_endpoint = vnf_endpoint + "/" + SAMPLE_VNF_MODULE_ID;

        try(MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
            server,
            delete_volume_group_endpoint,
            HttpStatus.NO_CONTENT_204,
            CREATE_INSTANCE_RESPONSE_STR,CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executeDelete(msoCreateServiceInstanceJson, msoRestClient()::deleteVolumeGroupInstance);
        }
    }

    @Test
    public void testDeleteNwInstance() throws Exception {
        String endpoint = props.getProperty(MsoProperties.MSO_REST_API_NETWORK_INSTANCE);
        String svc_endpoint = endpoint.replaceFirst(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        String delete_nw_endpoint = svc_endpoint + "/" + SAMPLE_NETWORK_INSTANCE_ID;

        try(MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
            server,
            delete_nw_endpoint,
            HttpStatus.NO_CONTENT_204,
            CREATE_INSTANCE_RESPONSE_STR,CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executeDelete(msoCreateServiceInstanceJson, msoRestClient()::deleteNwInstance);
        }
    }

    @Test
    public void testGetOrchestrationRequest() {
        String p = props.getProperty(MsoProperties.MSO_REST_API_GET_ORC_REQ);
        String path = p + "/" + SAMPLE_REQUEST_ID;

        try(MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
            server,
            path,
            HttpStatus.OK_200,
            CREATE_INSTANCE_RESPONSE_STR,CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executeGet(msoRestClient()::getOrchestrationRequest);
        }
    }

    @Test
    public void testGetManualTasks() {
        String p = props.getProperty(MsoProperties.MSO_REST_API_GET_ORC_REQ);
        String path = p + "/" + UUID.randomUUID();

        try(MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
            server,
            path,
            HttpStatus.OK_200,
            CREATE_INSTANCE_RESPONSE_STR,CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executeGet(msoRestClient()::getManualTasks);
        }
    }

    @Test
    public void testGetOrchestrationRequestsForDashboard() throws Exception {
        MsoRestClientNew testSubject;
        String t = "";
        String sourceId = "";
        String endpoint = "";
        RestObject restObject = null;
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getOrchestrationRequestsForDashboard(t, sourceId, endpoint, restObject);
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetManualTasksByRequestId() throws Exception {
        MsoRestClientNew testSubject;
        String t = "";
        String sourceId = "";
        String endpoint = "";
        RestObject restObject = null;
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.getManualTasksByRequestId(t, sourceId, endpoint, restObject);
        } catch (Exception e) {
        }
    }

    @Test
    public void testCompleteManualTask() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String t = "";
        String sourceId = "";
        String endpoint = "";
        RestObject restObject = null;
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.completeManualTask(requestDetails, t, sourceId, endpoint, restObject);
        } catch (Exception e) {
        }
    }

    @Test
    public void testDeleteConfiguration() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetailsWrapper requestDetails = null;
        String pmc_endpoint = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.deleteConfiguration(requestDetails, pmc_endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testSetConfigurationActiveStatus() throws Exception {
        String endpoint = "/serviceInstances/v5/<service_instance_id>/configurations/<configuration_id>";
        endpoint = endpoint.replace(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        endpoint = endpoint.replace(MsoController.CONFIGURATION_ID, SAMPLE_CONFIGURATION_ID);
        endpoint = endpoint + "/activate";

        try(MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
            server,
            endpoint,
            HttpStatus.ACCEPTED_202,
            CREATE_INSTANCE_RESPONSE_STR,CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executePost(msoCreateServiceInstanceJson, msoRestClient()::setConfigurationActiveStatus);
        }
    }

    @Test
    public void testSetPortOnConfigurationStatus() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails request = null;
        String path = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.setPortOnConfigurationStatus(request, path);
        } catch (Exception e) {
        }
    }

    @Test
    public void testChangeManagementUpdate() throws Exception {
        MsoRestClientNew testSubject;
        org.onap.vid.changeManagement.RequestDetailsWrapper requestDetails = null;
        String endpoint = "";
        MsoResponseWrapperInterface result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.changeManagementUpdate(requestDetails, endpoint);
        } catch (Exception e) {
        }
    }

    @Test
    public void testSetServiceInstanceStatus() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String t = "";
        String sourceId = "";
        String endpoint = "";
        RestObject<String> restObject = null;

        // default test
        try {
            testSubject = createTestSubject();
            testSubject.setServiceInstanceStatus(requestDetails, t, sourceId, endpoint, restObject);
        } catch (Exception e) {
        }
    }

    @Test
    public void testRemoveRelationshipFromServiceInstance() throws Exception {
        String serviceEndpoint = props.getProperty(MsoProperties.MSO_REST_API_SVC_INSTANCE);
        String removeRelationshipsPath = serviceEndpoint + "/" + SERVICE_INSTANCE_ID + "/removeRelationships";

        try(MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
            server,
            removeRelationshipsPath,
            HttpStatus.ACCEPTED_202,
            CREATE_INSTANCE_RESPONSE_STR,CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executePost(msoCreateServiceInstanceJson, msoRestClient()::removeRelationshipFromServiceInstance);
        }
    }

    @Test
    public void testAddRelationshipToServiceInstance() throws Exception {
        MsoRestClientNew testSubject;
        RequestDetails requestDetails = null;
        String addRelationshipsPath = "";
        MsoResponseWrapper result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.addRelationshipToServiceInstance(requestDetails, addRelationshipsPath);
        } catch (Exception e) {
        }
    }

    private MsoRestClientNew msoRestClient() {
        return new MsoRestClientNew(new SyncRestClient(MsoInterface.objectMapper()), baseUrlProvider);
    }

    private MsoRestClientNew createTestSubject() {
        return new MsoRestClientNew(null, baseUrlProvider);
    }
}