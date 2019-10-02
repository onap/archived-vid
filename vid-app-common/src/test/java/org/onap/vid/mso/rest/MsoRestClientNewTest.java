/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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

import static org.apache.commons.io.IOUtils.toInputStream;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.vid.controller.MsoController.SVC_INSTANCE_ID;
import static org.onap.vid.controller.MsoController.VNF_INSTANCE_ID;
import static org.onap.vid.utils.KotlinUtilsKt.JOSHWORKS_JACKSON_OBJECT_MAPPER;

import com.xebialabs.restito.server.StubServer;
import io.joshworks.restclient.http.HttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.controller.MsoController;
import org.onap.vid.mso.MsoProperties;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.mso.MsoResponseWrapperInterface;
import org.onap.vid.mso.RestObject;
import org.onap.vid.utils.Logging;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {SystemProperties.class})
public class MsoRestClientNewTest {

    private static StubServer server;
    private static StubServer securedServer;
    private static PropertiesConfiguration props = new PropertiesConfiguration();
    private static String msoCreateServiceInstanceJson;
    private static String msoScaleOutVfModule;
    private final static String CREATE_INSTANCE_RESPONSE_STR =
            "{\"requestReferences\":{\"instanceId\":\"baa13544-0e95-4644-9565-9a198a29a294\","
                    + "\"requestId\":\"a42a1a35-3d63-4629-bbe0-4989fa7414cb\"}}";
    private final static String SERVICE_INSTANCE_ID = "12345";
    private static final String SAMPLE_VNF_INSTANCE_ID = "111";
    private static final String SAMPLE_VNF_MODULE_ID = "987";
    private static final String SAMPLE_NETWORK_INSTANCE_ID = "666";
    private static final String SAMPLE_CONFIGURATION_ID = "997";
    private static final String SAMPLE_REQUEST_ID = "7777";


    @BeforeClass
    public static void start() throws Exception {
        server = new StubServer().run();
        securedServer = new StubServer().secured().run();

        Path resourceDirectory =
                Paths.get("src", "test", "resources", "WEB-INF", "conf", "system.properties");
        try (InputStream is = Files.newInputStream(resourceDirectory)) {
            props.load(is);
        }

        Path msoServiceInstantiationJsonFilePath =
                Paths.get("src", "test", "resources", "payload_jsons", "mso_service_instantiation.json");

        Path scaleOutJsonFilePath = Paths.get("src", "test", "resources", "payload_jsons", "scaleOutVfModulePayloadToMso.json");
        msoCreateServiceInstanceJson =
                String.join("\n", Files.readAllLines(msoServiceInstantiationJsonFilePath));
        msoScaleOutVfModule = String.join("\n", Files.readAllLines(scaleOutJsonFilePath));

    }

    @AfterClass
    public static void stop() {
        server.stop();
        securedServer.stop();
    }


    private String baseUrl() {
        return String.format("http://localhost:%d", server.getPort());
    }

    @Test
    public void testCreateSvcInstance() throws Exception {
        String endpoint = props.getString(MsoProperties.MSO_REST_API_CONFIGURATIONS);
        endpoint = endpoint.replace(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        try (MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
                server,
                endpoint,
                HttpStatus.ACCEPTED_202,
                CREATE_INSTANCE_RESPONSE_STR, CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executePost(msoCreateServiceInstanceJson, msoRestClient()::createSvcInstance);
        }
    }

    @Test
    public void testCreateVnf() throws Exception {
        String endpoint = props.getString(MsoProperties.MSO_REST_API_VNF_INSTANCE);
        endpoint = endpoint.replace(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        try (MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
                server,
                endpoint,
                HttpStatus.ACCEPTED_202,
                CREATE_INSTANCE_RESPONSE_STR, CREATE_INSTANCE_RESPONSE_STR)) {

            closure.executePost(msoCreateServiceInstanceJson, msoRestClient()::createVnf);
        }
    }

    @Test
    public void testCreateNwInstance() throws Exception {
        String endpoint = props.getString(MsoProperties.MSO_REST_API_NETWORK_INSTANCE);
        String nw_endpoint = endpoint.replaceFirst(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        try (MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
                server,
                nw_endpoint,
                HttpStatus.ACCEPTED_202,
                CREATE_INSTANCE_RESPONSE_STR, CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executePost(msoCreateServiceInstanceJson, msoRestClient()::createNwInstance);
        }
    }

    @Test
    public void testCreateVolumeGroupInstance() throws Exception {
        String endpoint = props.getString(MsoProperties.MSO_REST_API_VOLUME_GROUP_INSTANCE);
        String vnf_endpoint = endpoint.replaceFirst(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        vnf_endpoint = vnf_endpoint.replaceFirst(MsoController.VNF_INSTANCE_ID, SAMPLE_VNF_INSTANCE_ID);
        try (MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
                server,
                vnf_endpoint,
                HttpStatus.ACCEPTED_202,
                CREATE_INSTANCE_RESPONSE_STR, CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executePost(msoCreateServiceInstanceJson, msoRestClient()::createVolumeGroupInstance);
        }
    }

    @Test
    public void testCreateVfModuleInstance() throws Exception {
        String endpoint = props.getString(MsoProperties.MSO_REST_API_VF_MODULE_INSTANCE);
        String partial_endpoint = endpoint.replaceFirst(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        String vf_module_endpoint =
                partial_endpoint.replaceFirst(MsoController.VNF_INSTANCE_ID, SAMPLE_VNF_INSTANCE_ID);


        try (MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
                server,
                vf_module_endpoint,
                HttpStatus.ACCEPTED_202,
                CREATE_INSTANCE_RESPONSE_STR, CREATE_INSTANCE_RESPONSE_STR)) {
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
    @Ignore
    @Test
    public void testDeleteSvcInstance() throws Exception {
        String endpoint = props.getString(MsoProperties.MSO_RESTAPI_SERVICE_INSTANCE);
        endpoint = endpoint.replaceFirst(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);


        try (MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
                server,
                endpoint,
                HttpStatus.NO_CONTENT_204,
                CREATE_INSTANCE_RESPONSE_STR, CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executeDelete(msoCreateServiceInstanceJson, msoRestClient()::deleteSvcInstance);
        }
    }

    @Ignore
    @Test
    public void testDeleteVnf() throws Exception {
        String endpoint = props.getString(MsoProperties.MSO_REST_API_VNF_INSTANCE);
        endpoint = endpoint.replaceFirst(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);

        try (MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
                server,
                endpoint,
                HttpStatus.NO_CONTENT_204,
                CREATE_INSTANCE_RESPONSE_STR, CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executeDelete(msoCreateServiceInstanceJson, msoRestClient()::deleteVnf);
        }
    }

    @Ignore
    @Test
    public void testDeleteVfModule() throws Exception {
        String endpoint = props.getString(MsoProperties.MSO_REST_API_VF_MODULE_INSTANCE);
        String part_endpoint = endpoint.replaceFirst(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        String vf_modules_endpoint = part_endpoint.replaceFirst(MsoController.VNF_INSTANCE_ID, SAMPLE_VNF_INSTANCE_ID);
        String delete_vf_endpoint = vf_modules_endpoint + '/' + SAMPLE_VNF_MODULE_ID;

        try (MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
                server,
                delete_vf_endpoint,
                HttpStatus.NO_CONTENT_204,
                CREATE_INSTANCE_RESPONSE_STR, CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executeDelete(msoCreateServiceInstanceJson, msoRestClient()::deleteVfModule);
        }
    }

    @Ignore
    @Test
    public void testDeleteVolumeGroupInstance() throws Exception {
        String endpoint = props.getString(MsoProperties.MSO_REST_API_VOLUME_GROUP_INSTANCE);
        String svc_endpoint = endpoint.replaceFirst(MsoController.SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        String vnf_endpoint = svc_endpoint.replaceFirst(MsoController.VNF_INSTANCE_ID, SAMPLE_VNF_INSTANCE_ID);
        String delete_volume_group_endpoint = vnf_endpoint + "/" + SAMPLE_VNF_MODULE_ID;

        try (MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
                server,
                delete_volume_group_endpoint,
                HttpStatus.NO_CONTENT_204,
                CREATE_INSTANCE_RESPONSE_STR, CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executeDelete(msoCreateServiceInstanceJson, msoRestClient()::deleteVolumeGroupInstance);
        }
    }

    @Ignore
    @Test
    public void testDeleteNwInstance() throws Exception {
        String endpoint = props.getString(MsoProperties.MSO_REST_API_NETWORK_INSTANCE);
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
        String p = props.getString(MsoProperties.MSO_REST_API_GET_ORC_REQ);
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
    public void testGetManualTasksByRequestId() {
        String p = props.getString(MsoProperties.MSO_REST_API_GET_ORC_REQ);
        String path = p + "/" + UUID.randomUUID();
        String validResponse = "" 
            + "{ "
            + "  \"taskList\": [ "
            + "    { "
            + "      \"taskId\": \"daf4dd84-b77a-42da-a051-3239b7a9392c\", "
            + "      \"type\": \"fallout\", "
            + "      \"nfRole\": \"vEsmeralda\", "
            + "      \"subscriptionServiceType\": \"Emanuel\", "
            + "      \"originalRequestId\": \"d352c70d-5ef8-4977-9ea8-4c8cbe860422\", "
            + "      \"originalRequestorId\": \"ss835w\", "
            + "      \"errorSource\": \"A&AI\", "
            + "      \"errorCode\": \"404\", "
            + "      \"errorMessage\": \"Failed in A&AI 404\", "
            + "      \"validResponses\": [ "
            + "        \"rollback\", "
            + "        \"abort\", "
            + "        \"skip\", "
            + "        \"resume\", "
            + "        \"retry\" "
            + "      ] "
            + "    } "
            + "  ] "
            + "}";

        try(MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
            server,
            path,
            HttpStatus.OK_200,
            validResponse,validResponse)) {
            closure.executeGet(endpoint -> msoRestClient().getManualTasksByRequestId(null, null, endpoint, null));
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
        String endpoint = "/serviceInstantiation/v7/serviceInstances/<service_instance_id>/configurations/<configuration_id>";
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
    public void testSetServiceInstanceStatus_givenValidResponse_responseIsPopulatedAccordingly() {
        RequestDetails requestDetails = new RequestDetails();
        String t = "";
        String sourceId = "";
        String endpoint = "";
        final SyncRestClient client = mock(SyncRestClient.class);
        MsoRestClientNew testSubject = new MsoRestClientNew(client, "", new SystemPropertiesWrapper());

        // setup
        final HttpResponse<String> response = mock(HttpResponse.class);
        final int expectedStatus = 202;
        final String expectedResponse = "expected response";

        when(client.post(eq(endpoint), anyMap(), eq(requestDetails), eq(String.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(expectedStatus);
        when(response.getBody()).thenReturn(expectedResponse);
        when(response.getRawBody()).thenReturn(toInputStream(expectedResponse));

        // test
        MsoResponseWrapper responseWrapper = testSubject.setServiceInstanceStatus(requestDetails, endpoint);

        assertThat(responseWrapper.getStatus(), is(expectedStatus));
        assertThat(responseWrapper.getEntity(), is(expectedResponse));
    }

    @Test
    public void testRemoveRelationshipFromServiceInstance() throws Exception {
        String serviceEndpoint = props.getString(MsoProperties.MSO_RESTAPI_SERVICE_INSTANCE);
        String removeRelationshipsPath = serviceEndpoint + "/" + SERVICE_INSTANCE_ID + "/removeRelationships";

        try (MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
                server,
                removeRelationshipsPath,
                HttpStatus.ACCEPTED_202,
                CREATE_INSTANCE_RESPONSE_STR, CREATE_INSTANCE_RESPONSE_STR)) {
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
    @Test
    public void testScaleOutVfModule() throws IOException {
        String serviceEndpoint = props.getString(MsoProperties.MSO_REST_API_VF_MODULE_SCALE_OUT);
        String partial_endpoint = serviceEndpoint.replaceFirst(SVC_INSTANCE_ID, SERVICE_INSTANCE_ID);
        String vf_module_endpoint = partial_endpoint.replaceFirst(VNF_INSTANCE_ID, SAMPLE_VNF_INSTANCE_ID);
        try (MsoRestClientTestUtil closure = new MsoRestClientTestUtil(
                server,
                vf_module_endpoint,
                HttpStatus.ACCEPTED_202,
                CREATE_INSTANCE_RESPONSE_STR, CREATE_INSTANCE_RESPONSE_STR)) {
            closure.executePostCall(msoScaleOutVfModule, msoRestClient()::scaleOutVFModuleInstance);
        }

    }

    private MsoRestClientNew msoRestClient() {
        return new MsoRestClientNew(new SyncRestClient(JOSHWORKS_JACKSON_OBJECT_MAPPER, mock(Logging.class)),
            baseUrl(), new SystemPropertiesWrapper());
    }

    private MsoRestClientNew createTestSubject() {
        return new MsoRestClientNew(null, "", new SystemPropertiesWrapper());
    }
}
