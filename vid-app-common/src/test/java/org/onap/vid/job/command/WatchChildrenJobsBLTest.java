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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.vid.job.Job.JobStatus;
import org.onap.vid.job.impl.JobDaoImpl;
import org.onap.vid.properties.Features;
import org.onap.vid.utils.DaoUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

public class WatchChildrenJobsBLTest {
    @Mock
    private DataAccessService dataAccessService;

    @InjectMocks
    private WatchChildrenJobsBL watchChildrenJobsBL;

    @Mock
    private static FeatureManager featureManager;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    public static Object[][] dataProviderForChildrenStatusOnly() {
        return new Object[][]{
                 {Arrays.asList(JobStatus.STOPPED, JobStatus.COMPLETED_WITH_NO_ACTION, JobStatus.COMPLETED), JobStatus.COMPLETED_WITH_ERRORS},
                 {Arrays.asList(JobStatus.COMPLETED, JobStatus.FAILED, JobStatus.COMPLETED_WITH_NO_ACTION), JobStatus.COMPLETED_WITH_ERRORS},
                 {Arrays.asList(null, JobStatus.COMPLETED), JobStatus.COMPLETED_WITH_ERRORS},
                 {Arrays.asList(null, JobStatus.IN_PROGRESS), JobStatus.IN_PROGRESS},
                 {Arrays.asList(null, JobStatus.FAILED), JobStatus.FAILED},
                 {new ArrayList<>(), JobStatus.COMPLETED_WITH_NO_ACTION}
        };
    }

    @DataProvider
    public static Object[][] childrenStatusDataProvider() {

        List<Object[]> result = new ArrayList<>();
        result.addAll(Arrays.asList(dataProviderForChildrenStatusOnly()));
        result.addAll(Arrays.asList(inputsStatusAndExpectedOutputDataProvider()));
        return result.toArray(new Object[result.size()][]);
    }

    @DataProvider
    public static Object[][] inputsStatusAndExpectedOutputDataProvider() {
        return new Object[][]{
                {Arrays.asList(JobStatus.COMPLETED, JobStatus.COMPLETED), JobStatus.COMPLETED},
                {Arrays.asList(JobStatus.COMPLETED, JobStatus.COMPLETED_WITH_NO_ACTION), JobStatus.COMPLETED},
                {Arrays.asList(JobStatus.FAILED, JobStatus.COMPLETED_WITH_NO_ACTION), JobStatus.FAILED},
                {Arrays.asList(JobStatus.FAILED, JobStatus.COMPLETED), getErrorStatus()},
                {Arrays.asList(JobStatus.RESOURCE_IN_PROGRESS, JobStatus.FAILED), JobStatus.IN_PROGRESS},
                {Arrays.asList(JobStatus.PAUSE, JobStatus.FAILED), JobStatus.IN_PROGRESS},
                {Arrays.asList(JobStatus.PENDING, JobStatus.FAILED), JobStatus.IN_PROGRESS},
                {Arrays.asList(JobStatus.IN_PROGRESS, JobStatus.COMPLETED), JobStatus.IN_PROGRESS},
                {Arrays.asList(JobStatus.IN_PROGRESS, JobStatus.IN_PROGRESS),  JobStatus.IN_PROGRESS},
                {Arrays.asList(JobStatus.COMPLETED, getErrorStatus()), getErrorStatus()},
                {Arrays.asList(getErrorStatus(), JobStatus.FAILED), getErrorStatus()},
                {Arrays.asList(getErrorStatus(), getErrorStatus()), getErrorStatus()},
                {Arrays.asList(getErrorStatus(), JobStatus.COMPLETED_WITH_NO_ACTION), getErrorStatus()},
                {Arrays.asList(JobStatus.COMPLETED_WITH_NO_ACTION, JobStatus.COMPLETED_WITH_NO_ACTION), JobStatus.COMPLETED_WITH_NO_ACTION},
                {Arrays.asList(JobStatus.COMPLETED_AND_PAUSED, JobStatus.RESOURCE_IN_PROGRESS), JobStatus.IN_PROGRESS},
                {Arrays.asList(JobStatus.COMPLETED_AND_PAUSED, JobStatus.COMPLETED), JobStatus.COMPLETED_AND_PAUSED},
                {Arrays.asList(JobStatus.COMPLETED_AND_PAUSED, JobStatus.IN_PROGRESS), JobStatus.IN_PROGRESS},
        };
    }

    @Test(dataProvider = "childrenStatusDataProvider")
    public void whenRetrieveListOfChildrenWithStatues_thenAccumulatedChildrenStatusAsExpected(List<JobStatus> childJobs, JobStatus expectedChildrenJobsStatus) {
        //init sql result mock
        List<JobDaoImpl> mockChildren = childJobs.stream().map(st -> {
            JobDaoImpl job = new JobDaoImpl();
            job.setUuid(UUID.randomUUID());
            job.setStatus(st);
            return job;
        }).collect(Collectors.toList());
        when(dataAccessService.getList(eq(JobDaoImpl.class), anyString(), any(), eq(DaoUtils.getPropsMap())))
                .thenReturn(mockChildren);

        List<String> uuids = mockChildren.stream().map(job -> job.getUuid().toString()).collect(Collectors.toList());
        assertEquals(expectedChildrenJobsStatus, watchChildrenJobsBL.retrieveChildrenJobsStatus(uuids));
    }

    @Test(dataProvider = "inputsStatusAndExpectedOutputDataProvider")
    public void whenCumulate2JobStatus_thenResultAsExpected(List<JobStatus> jobs, JobStatus expectedChildrenJobsStatus) {
        assertEquals(expectedChildrenJobsStatus, watchChildrenJobsBL.cumulateJobStatus(jobs.get(0), jobs.get(1)));
    }
    private static JobStatus getErrorStatus() {
        return featureManager.isActive(Features.FLAG_2008_PAUSE_VFMODULE_INSTANTIATION_FAILURE) ?
            JobStatus.FAILED_AND_PAUSED : JobStatus.COMPLETED_WITH_ERRORS;
    }
}
