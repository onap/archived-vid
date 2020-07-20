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

import static java.util.stream.Collectors.toList;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.onap.vid.job.Job.JobStatus.COMPLETED;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.config.DataSourceConfig;
import org.onap.vid.config.MockedAaiClientAndFeatureManagerConfig;
import org.onap.vid.job.Job;
import org.onap.vid.job.Job.JobStatus;
import org.onap.vid.model.ResourceInfo;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.ServiceInfo.ServiceAction;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.mso.rest.RequestStatus;
import org.onap.vid.properties.Features;
import org.onap.vid.services.AsyncInstantiationBaseTest;
import org.onap.vid.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

@ContextConfiguration(classes = {DataSourceConfig.class, SystemProperties.class, MockedAaiClientAndFeatureManagerConfig.class})
public class AsyncInstantiationRepositoryTest extends AsyncInstantiationBaseTest {

    @Autowired
    private FeatureManager featureManager;

    @Inject
    private DataAccessService dataAccessService;
    private AsyncInstantiationRepository asyncInstantiationRepository;

    @BeforeClass
    void initServicesInfoService() {
        asyncInstantiationRepository = new AsyncInstantiationRepository(dataAccessService);
        createInstanceParamsMaps();
        createNewTestServicesInfoWithServiceModelID();
    }

    private void createNewTestServicesInfoWithServiceModelID() {

        LocalDateTime NOW = LocalDateTime.now();

        addNewServiceInfoWithAction(UUID.randomUUID(), "abc", "0", NOW.minusYears(1L), NOW, COMPLETED, false, false,
            MODEL_UUID, ServiceAction.RESUME);
        addNewServiceInfoWithAction(UUID.randomUUID(), "abc", "1", NOW.minusYears(1L), NOW, COMPLETED, false, false,
            MODEL_UUID, ServiceAction.INSTANTIATE);
        addNewServiceInfoWithAction(UUID.randomUUID(), "abc", "2", NOW, NOW, COMPLETED, false, false,
            MODEL_UUID_2, ServiceAction.INSTANTIATE);
        addNewServiceInfoWithAction(UUID.randomUUID(), "abc", "3", NOW, NOW, COMPLETED, false, false,
            MODEL_UUID, ServiceAction.INSTANTIATE);
        addNewServiceInfoWithAction(UUID.randomUUID(), "abc", "hidden", NOW, NOW, COMPLETED, true, false,
            MODEL_UUID, ServiceAction.INSTANTIATE);
        addNewServiceInfoWithAction(UUID.randomUUID(), "abc", "4", NOW, NOW, COMPLETED, false, false,
            MODEL_UUID_3, ServiceAction.UPDATE);
    }

    @DataProvider
    public static Object[][] listServicesByServiceModelIdDataProvider() {
        return new Object[][]{
            { "services info filtered by MODEL_UUID not hidden , ordered by newer first", MODEL_UUID, "3", "1" },
            { "services info filtered by MODEL_UUID2", MODEL_UUID_2, "2" },
        };
    }

    @Test(dataProvider = "listServicesByServiceModelIdDataProvider")
    public void testListServicesByServiceModelId(String desc, String modelUUID, String... expectedResult) {
        List<ServiceInfo> serviceInfoListResult = asyncInstantiationRepository.
            listInstantiatedServicesByServiceModelId(UUID.fromString(modelUUID));
        assertThat(desc, serviceInfoListResult.stream().map(ServiceInfo::getServiceInstanceName).collect(toList()),
            contains(expectedResult));
    }

    @Test
    public void getAllTemplatesServiceModelIds_givenDbWithSeveralModelIDs_2ReturnedAnd1OmittedAndDuplicatesRemoved() {
            Set<String> actual = asyncInstantiationRepository.getAllTemplatesServiceModelIds();
            // MODEL_UUID3 is Action=UPDATE, therefore omitted
            assertThat(actual, equalTo(ImmutableSet.of(MODEL_UUID, MODEL_UUID_2)));
    }

    @Test
    public void whenFilterServiceByNotExistUUID_emptyListIsReturned() {
        List<ServiceInfo> serviceInfoListResult = asyncInstantiationRepository.listInstantiatedServicesByServiceModelId(UUID.randomUUID());
        assertThat(serviceInfoListResult, is(empty()));
    }

    @Test
    public void whenSaveNewRequest_thenRequestIsRetrieved() {
        ServiceInstantiation serviceInstantiation = generateALaCarteWithVnfsServiceInstantiationPayload();
        UUID jobUuid = UUID.randomUUID();
        asyncInstantiationRepository.addJobRequest(jobUuid, serviceInstantiation);
        ServiceInstantiation stored = asyncInstantiationRepository.getJobRequest(jobUuid);
        assertThat(stored, jsonEquals(serviceInstantiation).when(IGNORING_ARRAY_ORDER));
    }

    private AsyncRequestStatus createAsyncRequestStatus(String message, String requestState){
        RequestStatus requestStatus = new RequestStatus(requestState, message, TimeUtils.zonedDateTimeToString(ZonedDateTime.now()));
        AsyncRequestStatus.Request request = new AsyncRequestStatus.Request(requestStatus);
        return new AsyncRequestStatus(request);
    }

    @Test
    public void getResourceInfoByRootJobId_returnsMapOfjobIdResources(){
        UUID jobId1= UUID.randomUUID();
        UUID jobId2= UUID.randomUUID();
        AsyncRequestStatus errorMessage= createAsyncRequestStatus("MSO failed resource", "FAILED");
        List<ResourceInfo> requestInfoList= ImmutableList.of(
                new ResourceInfo("aaaaaa",jobId1, "64f3123a-f9a8-4591-b481-d662134bcb52", Job.JobStatus.COMPLETED, null),
                new ResourceInfo("bbbbbb",jobId1, "65f3123a-f9a8-4591-b481-kodj9ig87gdu", getErrorStatus(), null),
                new ResourceInfo("dddddd",jobId1, null, Job.JobStatus.FAILED, null),
                new ResourceInfo("cccccc",jobId1, null, Job.JobStatus.FAILED, errorMessage),
                new ResourceInfo("eeeeee",jobId2, null, Job.JobStatus.FAILED, null),
                new ResourceInfo("ffffff",jobId2, "66f3123a-f9a8-4591-b481-ghfgh6767567", Job.JobStatus.COMPLETED, null)
        );
        for(ResourceInfo info: requestInfoList){
            asyncInstantiationRepository.saveResourceInfo(info);
        }
        Map<String, ResourceInfo> storedByTrackId = asyncInstantiationRepository.getResourceInfoByRootJobId(jobId1);
        assertThat(storedByTrackId.values(), hasSize(4));
        assertThat(storedByTrackId.get("aaaaaa").getInstanceId(), equalTo("64f3123a-f9a8-4591-b481-d662134bcb52"));
        assertThat(storedByTrackId.get("cccccc").getErrorMessage().request.requestStatus.getStatusMessage(), equalTo("MSO failed resource"));
        assertThat(storedByTrackId.get("cccccc").getErrorMessage().request.requestStatus.getRequestState(), equalTo("FAILED"));
        assertThat(storedByTrackId.get("dddddd").getErrorMessage(), equalTo(null));
        assertThat(storedByTrackId.values(),
            jsonEquals(requestInfoList.stream().filter(i -> i.getRootJobId().equals(jobId1)).collect(
                toList())).when(IGNORING_ARRAY_ORDER));
    }
    private JobStatus getErrorStatus() {
        return featureManager.isActive(Features.FLAG_2008_PAUSE_VFMODULE_INSTANTIATION_FAILURE) ?
            JobStatus.FAILED_AND_PAUSED : JobStatus.COMPLETED_WITH_ERRORS;
    }
}
