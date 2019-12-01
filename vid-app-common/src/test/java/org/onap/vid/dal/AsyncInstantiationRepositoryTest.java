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

package org.onap.vid.dal;

import static java.util.stream.Collectors.*;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.onap.vid.job.Job.JobStatus.COMPLETED;

import com.google.common.collect.ImmutableList;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.config.DataSourceConfig;
import org.onap.vid.config.MockedAaiClientAndFeatureManagerConfig;
import org.onap.vid.job.Job;
import org.onap.vid.model.ResourceInfo;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.mso.rest.RequestStatus;
import org.onap.vid.services.AsyncInstantiationBaseTest;
import org.onap.vid.utils.TimeUtils;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration(classes = {DataSourceConfig.class, SystemProperties.class, MockedAaiClientAndFeatureManagerConfig.class})
public class AsyncInstantiationRepositoryTest extends AsyncInstantiationBaseTest {

    @Inject
    private DataAccessService dataAccessService;

    @BeforeClass
    void initServicesInfoService() {
        createInstanceParamsMaps();
    }

    private void createNewTestServicesInfoWithServiceModelID() {

        LocalDateTime NOW = LocalDateTime.now();

        addNewServiceInfo(UUID.randomUUID(), "abc", "1", NOW.minusDays(1L), NOW, COMPLETED, false, false,
            MODEL_UUID);
        addNewServiceInfo(UUID.randomUUID(), "abc", "2", NOW, NOW, COMPLETED, false, false,
            MODEL_UUID_2);
        addNewServiceInfo(UUID.randomUUID(), "abc", "3", NOW, NOW, COMPLETED, false, false,
            MODEL_UUID);
        addNewServiceInfo(UUID.randomUUID(), "abc", "hidden", NOW, NOW, COMPLETED, true, false,
            MODEL_UUID);
    }

    @Test
    public void filterServicesByMoServiceModelID() {
        AsyncInstantiationRepository underTest = new AsyncInstantiationRepository(dataAccessService);
        createNewTestServicesInfoWithServiceModelID();
        List<ServiceInfo> serviceInfoListResult = underTest.listServicesByServiceModelId(UUID.fromString(MODEL_UUID));

        assertThat(serviceInfoListResult.stream().map(ServiceInfo::getServiceInstanceName).collect(toList()),
            contains("3", "1"));
    }

    @Test
    public void whenSaveNewRequest_thenRequestIsRetrieved() {
        AsyncInstantiationRepository underTest = new AsyncInstantiationRepository(dataAccessService);
        ServiceInstantiation serviceInstantiation = generateALaCarteWithVnfsServiceInstantiationPayload();
        UUID jobUuid = UUID.randomUUID();
        underTest.addJobRequest(jobUuid, serviceInstantiation);
        ServiceInstantiation stored = underTest.getJobRequest(jobUuid);
        assertThat(stored, jsonEquals(serviceInstantiation).when(IGNORING_ARRAY_ORDER));
    }

    private AsyncRequestStatus createAsyncRequestStatus(String message, String requestState){
        RequestStatus requestStatus = new RequestStatus(requestState, message, TimeUtils.zonedDateTimeToString(ZonedDateTime.now()));
        AsyncRequestStatus.Request request = new AsyncRequestStatus.Request(requestStatus);
        return new AsyncRequestStatus(request);
    }

    @Test
    public void getResourceInfoByRootJobId_returnsMapOfjobIdResources(){
        AsyncInstantiationRepository underTest = new AsyncInstantiationRepository(dataAccessService);
        UUID jobId1= UUID.randomUUID();
        UUID jobId2= UUID.randomUUID();
        AsyncRequestStatus errorMessage= createAsyncRequestStatus("MSO failed resource", "FAILED");
        List<ResourceInfo> requestInfoList= ImmutableList.of(
                new ResourceInfo("aaaaaa",jobId1, "64f3123a-f9a8-4591-b481-d662134bcb52", Job.JobStatus.COMPLETED, null),
                new ResourceInfo("bbbbbb",jobId1, "65f3123a-f9a8-4591-b481-kodj9ig87gdu", Job.JobStatus.COMPLETED_WITH_ERRORS, null),
                new ResourceInfo("dddddd",jobId1, null, Job.JobStatus.FAILED, null),
                new ResourceInfo("cccccc",jobId1, null, Job.JobStatus.FAILED, errorMessage),
                new ResourceInfo("eeeeee",jobId2, null, Job.JobStatus.FAILED, null),
                new ResourceInfo("ffffff",jobId2, "66f3123a-f9a8-4591-b481-ghfgh6767567", Job.JobStatus.COMPLETED, null)
        );
        for(ResourceInfo info: requestInfoList){
            underTest.saveResourceInfo(info);
        }
        Map<String, ResourceInfo> storedByTrackId = underTest.getResourceInfoByRootJobId(jobId1);
        assertThat(storedByTrackId.values(), hasSize(4));
        assertThat(storedByTrackId.get("aaaaaa").getInstanceId(), equalTo("64f3123a-f9a8-4591-b481-d662134bcb52"));
        assertThat(storedByTrackId.get("cccccc").getErrorMessage().request.requestStatus.getStatusMessage(), equalTo("MSO failed resource"));
        assertThat(storedByTrackId.get("cccccc").getErrorMessage().request.requestStatus.getRequestState(), equalTo("FAILED"));
        assertThat(storedByTrackId.get("dddddd").getErrorMessage(), equalTo(null));
        assertThat(storedByTrackId.values(),
            jsonEquals(requestInfoList.stream().filter(i -> i.getRootJobId().equals(jobId1)).collect(
                toList())).when(IGNORING_ARRAY_ORDER));
    }
}
