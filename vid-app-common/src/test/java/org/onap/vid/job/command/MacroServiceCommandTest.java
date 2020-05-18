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

package org.onap.vid.job.command;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.vid.model.VidNotions.ModelCategory.INFRASTRUCTURE_VPN;
import static org.onap.vid.model.VidNotions.ModelCategory.IS_5G_FABRIC_CONFIGURATION_MODEL;
import static org.onap.vid.model.VidNotions.ModelCategory.IS_5G_PROVIDER_NETWORK_MODEL;
import static org.onap.vid.model.VidNotions.ModelCategory.OTHER;
import static org.onap.vid.model.VidNotions.ModelCategory.SERVICE_WITH_COLLECTION_RESOURCE;
import static org.onap.vid.model.VidNotions.ModelCategory.Transport;
import static org.testng.AssertJUnit.assertEquals;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.Action;
import org.onap.vid.model.VidNotions;
import org.onap.vid.model.serviceInstantiation.BaseResource;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AuditService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

public class MacroServiceCommandTest {

    @Mock
    private InProgressStatusService inProgressStatusService;

    @Mock
    private WatchChildrenJobsBL watchChildrenJobsB;

    @Mock
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Mock
    private JobsBrokerService jobsBrokerService;

    @Mock
    private MsoRequestBuilder msoRequestBuilder;

    @Mock
    private MsoResultHandlerService msoResultHandlerService;

    @Mock
    private JobAdapter jobAdapter;

    @Mock
    private RestMsoImplementation restMso;

    @Mock
    private AuditService auditService;

    @Mock
    FeatureManager featureManager;

    @InjectMocks
    private MacroServiceCommand macroServiceCommand;

    @DataProvider
    public static Object[][] modelCategoryPre1806DataProvider() {
        return new Object[][]{
                {IS_5G_PROVIDER_NETWORK_MODEL, false},
                {IS_5G_FABRIC_CONFIGURATION_MODEL, false},
                {Transport, true},
                {SERVICE_WITH_COLLECTION_RESOURCE, true},
                {INFRASTRUCTURE_VPN, true},
                {OTHER, false},
        };
    }

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(dataProvider = "modelCategoryPre1806DataProvider")
    public void testShouldUsePre1806Request(VidNotions.ModelCategory modelCategory, boolean expectedResult) {
        ServiceInstantiation serviceInstantiation = mock(ServiceInstantiation.class);
        VidNotions vidNotions = mock(VidNotions.class);
        when(serviceInstantiation.getVidNotions()).thenReturn(vidNotions);
        when(vidNotions.getModelCategory()).thenReturn(modelCategory);
        assertEquals(macroServiceCommand.shouldUsePre1806Request(serviceInstantiation), expectedResult);
    }

    @DataProvider
    public static Object[][] MsoFilteredRequestsDataProvider() {
        return new Object[][]{
                {Collections.EMPTY_LIST},
                {ImmutableList.of(new AsyncRequestStatus.Request())}
        };
    }

    @Test(dataProvider = "MsoFilteredRequestsDataProvider")
    public void givenResumeAction_whenCantRetrieveRequestIdFromMSO_thenJobIsFailed(List<AsyncRequestStatus.Request> requests) {
        String instanceId = UUID.randomUUID().toString();
        BaseResource baseResource = mock(BaseResource.class);
        when(baseResource.getInstanceId()).thenReturn(instanceId);
        when(baseResource.getAction()).thenReturn(Action.Resume);
        macroServiceCommand.init(new JobSharedData(null, null, baseResource, null));
        when(auditService.retrieveRequestsFromMsoByServiceIdAndRequestTypeAndScope(eq(instanceId), any(), any()))
                .thenReturn(requests);
        assertEquals(macroServiceCommand.resumeMyself(), Job.JobStatus.FAILED);
    }

}
