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

package org.onap.vid.services;

import org.mockito.*;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.vid.changeManagement.ChangeManagementRequest;
import org.onap.vid.changeManagement.RequestDetails;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoResponseWrapperInterface;
import org.onap.vid.scheduler.SchedulerRestInterfaceIfc;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class ChangeManagementServiceImplTest {

    @Mock
    DataAccessService dataAccessServiceMock;

    @Mock
    MsoBusinessLogic msoBusinessLogicMock;

    @Mock
    SchedulerRestInterfaceIfc schedulerRestInterface;

    @Mock
    CloudOwnerService cloudOwnerService;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void doChangeManagement_requestIsNull_returnsNull() throws Exception {
        ChangeManagementServiceImpl changeManagementService = new ChangeManagementServiceImpl(dataAccessServiceMock, msoBusinessLogicMock, schedulerRestInterface, cloudOwnerService);
        ResponseEntity<String> result = changeManagementService.doChangeManagement(null,"anyString");
        assertNull(result);
    }

    @Test
    public void doChangeManagement_currentRequestDetailsIsNull_returnsNull() throws Exception {
        ChangeManagementServiceImpl changeManagementService = new ChangeManagementServiceImpl(dataAccessServiceMock, msoBusinessLogicMock, schedulerRestInterface, cloudOwnerService);

        ChangeManagementServiceImpl changeManagementServiceSpied = Mockito.spy(changeManagementService);
        Mockito.doReturn(null).when(changeManagementServiceSpied).findRequestByVnfName(Matchers.anyList(),Mockito.anyString());

        ResponseEntity<String> result = changeManagementServiceSpied.doChangeManagement(null,"anyString");

        assertNull(result);
    }


    @Test
    public void  doChangeManagement_requestTypeIsUpdate_MsoUpdateVnfIsCalled() throws Exception {
        Mockito.doReturn(Mockito.mock(MsoResponseWrapperInterface.class)).when(msoBusinessLogicMock).updateVnf(Mockito.any(),Mockito.any(),Mockito.any());
        RequestDetails requestDetails = callChangeManagement(ChangeManagementRequest.UPDATE);

        ArgumentCaptor<RequestDetails> argumentCaptor = ArgumentCaptor.forClass(RequestDetails.class);
        verify(msoBusinessLogicMock).updateVnf(argumentCaptor.capture(),Mockito.any(),Mockito.any());
        assertEquals(argumentCaptor.getValue().getVnfInstanceId(), requestDetails.getVnfInstanceId());
    }

    @Test
    public void  doChangeManagement_requestTypeIsReplace_MsoUpdateVnfIsCalled() throws Exception {
        Mockito.doReturn(Mockito.mock(MsoResponseWrapperInterface.class)).when(msoBusinessLogicMock).replaceVnf(Mockito.any(),Mockito.any(),Mockito.any());
        RequestDetails requestDetails = callChangeManagement(ChangeManagementRequest.REPLACE);


        ArgumentCaptor<RequestDetails> argumentCaptor = ArgumentCaptor.forClass(RequestDetails.class);

        verify(msoBusinessLogicMock).replaceVnf(argumentCaptor.capture(),Mockito.any(),Mockito.any());
        assertEquals(argumentCaptor.getValue().getVnfInstanceId(), requestDetails.getVnfInstanceId());
    }

    @Test
    public void  doChangeManagement_requestTypeIsInPlaceSoftwareUpdate_MsoUpdateVnfIsCalled() throws Exception {
        Mockito.doReturn(Mockito.mock(MsoResponseWrapperInterface.class)).when(msoBusinessLogicMock).updateVnfSoftware(Mockito.any(),Mockito.any(),Mockito.any());
        RequestDetails requestDetails = callChangeManagement(ChangeManagementRequest.VNF_IN_PLACE_SOFTWARE_UPDATE);


        ArgumentCaptor<RequestDetails> argumentCaptor = ArgumentCaptor.forClass(RequestDetails.class);

        verify(msoBusinessLogicMock).updateVnfSoftware(argumentCaptor.capture(),Mockito.any(),Mockito.any());
        assertEquals(argumentCaptor.getValue().getVnfInstanceId(), requestDetails.getVnfInstanceId());
    }

    @Test
    public void  doChangeManagement_requestTypeIsConfigUpdate_MsoUpdateVnfIsCalled() throws Exception {
        Mockito.doReturn(Mockito.mock(MsoResponseWrapperInterface.class)).when(msoBusinessLogicMock).updateVnfConfig(Mockito.any(),Mockito.any(),Mockito.any());
        RequestDetails requestDetails = callChangeManagement(ChangeManagementRequest.CONFIG_UPDATE);


        ArgumentCaptor<RequestDetails> argumentCaptor = ArgumentCaptor.forClass(RequestDetails.class);

        verify(msoBusinessLogicMock).updateVnfConfig(argumentCaptor.capture(),Mockito.any(),Mockito.any());
        assertEquals(argumentCaptor.getValue().getVnfInstanceId(), requestDetails.getVnfInstanceId());
    }

    private RequestDetails callChangeManagement(String requestType) throws Exception {
        ChangeManagementServiceImpl changeManagementService = new ChangeManagementServiceImpl(dataAccessServiceMock, msoBusinessLogicMock, schedulerRestInterface, cloudOwnerService);
        ChangeManagementServiceImpl changeManagementServiceSpied = Mockito.spy(changeManagementService);
        ChangeManagementRequest updateRequest = new ChangeManagementRequest();

        updateRequest.setRequestType(requestType);
        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setVnfInstanceId("vnfFakeId");
        Mockito.doReturn("fakeId").when(changeManagementServiceSpied).extractServiceInstanceId(Mockito.any(),Mockito.any());
        Mockito.doReturn(requestDetails).when(changeManagementServiceSpied).findRequestByVnfName(Matchers.any(),Mockito.any());

        changeManagementServiceSpied.doChangeManagement(updateRequest, "anyVnfName");

        return requestDetails;
    }
}
