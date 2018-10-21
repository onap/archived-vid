/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia. All rights reserved.
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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.NotFoundException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.impl.JobDaoImpl;
import org.onap.vid.model.JobBulk;
import org.onap.vid.model.JobModel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class BulkInstantiationServiceImplTest {

  @Mock
  private JobsBrokerService jobsBrokerService;

  @Mock
  private JobAdapter jobAdapter;

  @InjectMocks
  private BulkInstantiationServiceImpl testSubject;

  @BeforeSuite
  public void before() {
    initMocks(this);
  }

  @BeforeMethod
  public void resetMocks() {
    reset(jobsBrokerService);
    reset(jobAdapter);
  }

  @Test
  public void saveBulkTest() {
    UUID uuid = UUID.randomUUID();
    Map<String, Object> bulkRequest = new HashMap<>();
    List<Job> jobList = new ArrayList<>();
    jobList.add(createJob(uuid));
    when(jobAdapter.createBulkOfJobs(bulkRequest)).thenReturn(jobList);

    JobBulk jobBulk = createJobBulk(jobList);
    when(jobAdapter.toModelBulk(jobList)).thenReturn(jobBulk);

    JobBulk result = testSubject.saveBulk(bulkRequest);

    Assert.assertEquals(result.getJobs().size(), jobList.size());
    Assert.assertEquals(result.getJobs().get(0).getUuid(), uuid);
  }

  @Test
  public void getJobTest() {
    UUID uuid = UUID.randomUUID();
    Job job = createJob(uuid);
    doReturn(job).when(jobsBrokerService).peek(uuid);
    JobModel jobModel = createJobModel(uuid);
    when(jobAdapter.toModel(job)).thenReturn(jobModel);

    JobModel response = testSubject.getJob(uuid);
    Assert.assertEquals(response.getUuid(), uuid);
  }

  @Test(expectedExceptions = {NotFoundException.class})
  public void getJobTest_throwsExceptionOnEmptyUUID() {
    UUID uuid = null;
    Job job = createJob(uuid);
    doReturn(job).when(jobsBrokerService).peek(uuid);
    JobModel response = testSubject.getJob(uuid);
    Assert.fail();
  }

  @Test(expectedExceptions = {NotFoundException.class})
  public void getJobTest_throwsExceptionCauseJobDoesNotExists() {
    UUID uuid = UUID.randomUUID();
    doReturn(null).when(jobsBrokerService).peek(uuid);
    JobModel response = testSubject.getJob(uuid);
    Assert.fail();
  }

  private Job createJob(UUID uuid) {
    Job job = new JobDaoImpl();
    job.setUuid(uuid);
    return job;
  }

  private JobModel createJobModel(UUID uuid) {
    JobModel jobModel = new JobModel();
    jobModel.setUuid(uuid);
    return jobModel;
  }

  private JobBulk createJobBulk(List<Job> jobList) {
    List<JobModel> jobBulkList = new ArrayList<>();
    jobList.stream().forEach(job -> {
      JobModel jm = new JobModel();
      jm.setUuid(job.getUuid());
      jobBulkList.add(jm);
    });
    return new JobBulk(jobBulkList);
  }
}
