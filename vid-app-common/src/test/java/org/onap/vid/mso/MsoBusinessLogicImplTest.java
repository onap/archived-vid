package org.onap.vid.mso;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.mso.rest.RequestDetails;
import org.onap.vid.properties.Features;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.onap.vid.controllers.MsoController.SVC_INSTANCE_ID;
import static org.onap.vid.controllers.MsoController.VNF_INSTANCE_ID;
import static org.onap.vid.mso.MsoBusinessLogicImpl.validateEndpointPath;

@ContextConfiguration(classes = {SystemProperties.class})
@WebAppConfiguration
public class MsoBusinessLogicImplTest extends AbstractTestNGSpringContextTests {

    @InjectMocks
    private MsoBusinessLogicImpl msoBusinessLogic;

    @Mock
    private FeatureManager featureManagerMock;

    @Mock
    private MsoInterface msoInterfaceMock;


    @BeforeTest
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateEndpointPath_endPointIsNotEmptyAndVaild_returnProperty(){
        System.setProperty("TestEnv","123");
        String foundEndPoint = validateEndpointPath("TestEnv");
        Assert.assertEquals("123",foundEndPoint);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void validateEndpointPath_endPointIsNull_throwRuntimeException(){
        validateEndpointPath("NotExists");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void validateEndpointPath_endPointIsNotEmptyButDoesntExists_throwRuntimeException(){
        System.setProperty("EmptyEndPoint","");
        validateEndpointPath("EmptyEndPoint");
    }


    //@Test(dataProvider = "unAssignOrDeleteParams")
    public void deleteSvcInstance_verifyEndPointPathConstructing_unAssignFeatureOffOrUnAssignFlagIsFalse(boolean isAssignFlag,String status) {
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

        msoBusinessLogic.deleteVnf(requestDetails, "serviceInstanceTempId","vnfInstanceTempId");
        verify(msoInterfaceMock).deleteVnf(requestDetails, vnf_endpoint + "/vnfInstanceTempId");
    }

    @Test
    public void deleteVfModule_verifyEndPointPathConstructing() {
        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VF_MODULE_INSTANCE);
        RequestDetails requestDetails = new RequestDetails();

        String vf__modules_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, "serviceInstanceTempId").replaceFirst(VNF_INSTANCE_ID, "vnfInstanceTempId");

        msoBusinessLogic.deleteVfModule(requestDetails, "serviceInstanceTempId","vnfInstanceTempId", "vfModuleTempId");
        verify(msoInterfaceMock).deleteVfModule(requestDetails, vf__modules_endpoint + "/vfModuleTempId" );
    }

    @Test
    public void insertServiceInstantiationToDB_StartJob() {

//        broker = new JobsBrokerServiceInDatabaseImpl(dataAccessServiceMock, sessionFactory);
//        ((JobsBrokerServiceInDatabaseImpl)broker).deleteAll();
//
////        msoBusinessLogic.setDataAccessService(dataAccessServiceMock);
////        msoBusinessLogic.setJobsBrokerService(broker);
////        msoBusinessLogic.setJobAdapter(jobAdapter);
//
//        ServiceInstantiation serviceInstantiation = new ServiceInstantiation();
//        serviceInstantiation.setCount(2);
//        serviceInstantiation.setInstanceName("TestName");
//
//        msoBusinessLogic.pushBulkJob(serviceInstantiation, "testUserId");
//
//        List<ServiceInfo> serviceInfoList = dataAccessServiceMock.getList(ServiceInfo.class, null);
//        int k = 9;
//        Assert.assertEquals(serviceInstantiation, containsInAnyOrder(serviceInfoList.toArray()));
    }
}

