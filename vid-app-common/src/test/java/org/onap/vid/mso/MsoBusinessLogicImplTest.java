package org.onap.vid.mso;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.controllers.MsoController;
import org.onap.vid.mso.rest.RequestDetails;
import org.onap.vid.properties.Features;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.onap.vid.controllers.MsoController.SVC_INSTANCE_ID;
import static org.onap.vid.mso.MsoBusinessLogicImpl.validateEndpointPath;

@ContextConfiguration(classes = {SystemProperties.class})
@WebAppConfiguration
public class MsoBusinessLogicImplTest extends AbstractTestNGSpringContextTests {

    private static final String SERVICE_INSTANCE_ID = "1";
    private static final String VNF_INSTANCE_ID = "1";
    private static final String EXPECTED_SCALE_OUT_PATH = "/serviceInstantiation/v7/serviceInstances/1/vnfs/1/vfModules/scaleOut";
    private static final Path PATH_TO_NOT_PROCESSED_SCALE_OUT_REQUEST = Paths.get("src", "test", "resources", "payload_jsons", "scaleOutVfModulePayload.json");
    private static final Path PATH_TO_FINAL_SCALE_OUT_REQUEST = Paths.get("src", "test", "resources", "payload_jsons", "scaleOutVfModulePayloadToMso.json");
    private static final ObjectMapper OBJECT_MAPPER=new ObjectMapper();

    @InjectMocks
    private MsoBusinessLogicImpl msoBusinessLogic;

    @Mock
    private FeatureManager featureManagerMock;

    @Mock
    private MsoInterface msoInterfaceMock;


    @BeforeTest
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateEndpointPath_endPointIsNotEmptyAndVaild_returnProperty() {
        System.setProperty("TestEnv", "123");
        String foundEndPoint = validateEndpointPath("TestEnv");
        Assert.assertEquals("123", foundEndPoint);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void validateEndpointPath_endPointIsNull_throwRuntimeException() {
        validateEndpointPath("NotExists");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void validateEndpointPath_endPointIsNotEmptyButDoesntExists_throwRuntimeException() {
        System.setProperty("EmptyEndPoint", "");
        validateEndpointPath("EmptyEndPoint");
    }


    //@Test(dataProvider = "unAssignOrDeleteParams")
    public void deleteSvcInstance_verifyEndPointPathConstructing_unAssignFeatureOffOrUnAssignFlagIsFalse(boolean isAssignFlag, String status) {
        Mockito.reset(msoInterfaceMock);
        String endpoint = validateEndpointPath(isAssignFlag ? MsoProperties.MSO_DELETE_OR_UNASSIGN_REST_API_SVC_INSTANCE : MsoProperties.MSO_REST_API_SVC_INSTANCE);
        RequestDetails requestDetails = new RequestDetails();

        when(featureManagerMock.isActive(Features.FLAG_UNASSIGN_SERVICE)).thenReturn(isAssignFlag);

        msoBusinessLogic.deleteSvcInstance(requestDetails, "tempId", status);

        verify(msoInterfaceMock).deleteSvcInstance(requestDetails, endpoint + "/tempId");
    }

    @DataProvider
    public Object[][] unAssignOrDeleteParams() {
        return new Object[][]{
                {Boolean.FALSE, "active"},
                {Boolean.FALSE, "created"},
                {Boolean.TRUE, "Active"},
                {Boolean.TRUE, "unexpected-status"},
        };
    }

    //@Test(dataProvider = "unAssignStatus")
    public void deleteSvcInstance_verifyEndPointPathConstructing_unAssignFeatureOnAndUnAssignFlagIsTrue(String status) {
        Mockito.reset(msoInterfaceMock);
        // in the test Features.FLAG_UNASSIGN_SERVICE is active so the endpoint should be MsoProperties.MSO_DELETE_OR_UNASSIGN_REST_API_SVC_INSTANCE
        String endpoint = validateEndpointPath(MsoProperties.MSO_DELETE_OR_UNASSIGN_REST_API_SVC_INSTANCE);
        RequestDetails requestDetails = new RequestDetails();

        when(featureManagerMock.isActive(Features.FLAG_UNASSIGN_SERVICE)).thenReturn(true);

        msoBusinessLogic.deleteSvcInstance(requestDetails, "tempId", status);

        verify(msoInterfaceMock).unassignSvcInstance(requestDetails, endpoint + "/tempId/unassign");
    }

    @DataProvider
    public Object[][] unAssignStatus() {
        return new Object[][]{
                {"Created"},
                {"Pendingdelete"},
                {"pending-Delete"},
                {"Assigned"}
        };
    }

    @Test
    public void deleteVnf_verifyEndPointPathConstructing() {
        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VNF_INSTANCE);
        RequestDetails requestDetails = new RequestDetails();

        String vnf_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, "serviceInstanceTempId");

        msoBusinessLogic.deleteVnf(requestDetails, "serviceInstanceTempId", "vnfInstanceTempId");
        verify(msoInterfaceMock).deleteVnf(requestDetails, vnf_endpoint + "/vnfInstanceTempId");
    }

    @Test
    public void deleteVfModule_verifyEndPointPathConstructing() {
        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VF_MODULE_INSTANCE);
        RequestDetails requestDetails = new RequestDetails();

        String vf__modules_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, "serviceInstanceTempId").replaceFirst(MsoController.VNF_INSTANCE_ID, "vnfInstanceTempId");

        msoBusinessLogic.deleteVfModule(requestDetails, "serviceInstanceTempId", "vnfInstanceTempId", "vfModuleTempId");
        verify(msoInterfaceMock).deleteVfModule(requestDetails, vf__modules_endpoint + "/vfModuleTempId");
    }


    @Test
    public void shouldSendProperScaleOutRequest() throws IOException {
        ArgumentCaptor<RequestDetailsWrapper> requestDetailsWrapperArgumentCaptor = ArgumentCaptor.forClass(RequestDetailsWrapper.class);
        org.onap.vid.changeManagement.RequestDetails requestDetails = getScaleOutRequest();
        RequestDetailsWrapper expectedRequestWrapper = getExpectedRequestWrapper();

        msoBusinessLogic.scaleOutVfModuleInstance(requestDetails, SERVICE_INSTANCE_ID, VNF_INSTANCE_ID);

        verify(msoInterfaceMock).scaleOutVFModuleInstance(requestDetailsWrapperArgumentCaptor.capture(), eq(EXPECTED_SCALE_OUT_PATH));
        RequestDetailsWrapper actual = requestDetailsWrapperArgumentCaptor.getAllValues().get(0);

        assertThat(expectedRequestWrapper.requestDetails).isEqualTo(actual.requestDetails);
    }

    private org.onap.vid.changeManagement.RequestDetails getScaleOutRequest() throws IOException {
        return OBJECT_MAPPER.readValue(PATH_TO_NOT_PROCESSED_SCALE_OUT_REQUEST.toFile(), org.onap.vid.changeManagement.RequestDetails.class);
    }

    private RequestDetailsWrapper getExpectedRequestWrapper() throws IOException {
        return OBJECT_MAPPER.readValue(PATH_TO_FINAL_SCALE_OUT_REQUEST.toFile(), new TypeReference<RequestDetailsWrapper<org.onap.vid.changeManagement.RequestDetails>>() {
        });
    }
}

