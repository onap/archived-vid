package org.onap.vid.job.impl;

import org.onap.vid.job.JobsBrokerService;
import org.quartz.JobExecutionException;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DeleteOldJobsWorkerTest {

    @Test
    public void whenExecuteInternal_thenCallToDeleteOldFinalJobs() throws JobExecutionException {
        JobsBrokerService mockBroker = mock(JobsBrokerService.class);
        long secondsAgo = 42L;
        DeleteOldJobsWorker underTest = new DeleteOldJobsWorker();
        underTest.setJobsBrokerService(mockBroker);
        underTest.setSecondsAgo(secondsAgo);
        underTest.executeInternal(null);
        verify(mockBroker).deleteOldFinalJobs(secondsAgo);
    }

}
