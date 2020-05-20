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

package org.onap.vid.job.command

import org.apache.commons.lang3.StringUtils
import org.onap.portalsdk.core.service.DataAccessService
import org.onap.vid.job.Job
import org.onap.vid.job.Job.JobStatus.*
import org.onap.vid.job.impl.JobDaoImpl
import org.onap.vid.utils.DaoUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream


@Service
class WatchChildrenJobsBL @Autowired
constructor(private val dataAccessService: DataAccessService) {

    fun retrieveChildrenJobsStatus(childrenJobsIds: List<String>): Job.JobStatus {
        val jobs = getAllChildrenJobs(childrenJobsIds)

        val jobsStatuses = childrenJobsIds.stream()
                .map<JobDaoImpl> { jobId -> jobs[UUID.fromString(jobId)] }
                .map {when {
                    (it == null || it.status == null) -> Job.JobStatus.FAILED
                    else -> it.status
                }}

        return cumulateJobStatus(jobsStatuses)

    }

    fun cumulateJobStatus(childrenComulatedStatus: Job.JobStatus, fatherJobStatus: Job.JobStatus): Job.JobStatus {
        return cumulateJobStatus(Stream.of(childrenComulatedStatus, fatherJobStatus))
    }

    private fun cumulateJobStatus(jobsStatuses: Stream<Job.JobStatus>): Job.JobStatus {

        return jobsStatuses.reduce{ a, b ->
            when {
                !a.isFinal || !b.isFinal -> IN_PROGRESS
                a == COMPLETED_WITH_ERRORS || b == COMPLETED_WITH_ERRORS-> COMPLETED_WITH_ERRORS
                a == COMPLETED && b.isFailure -> COMPLETED_WITH_ERRORS
                b == COMPLETED && a.isFailure -> COMPLETED_WITH_ERRORS
                a == COMPLETED_AND_PAUSED || b == COMPLETED_AND_PAUSED -> COMPLETED_AND_PAUSED
                a == COMPLETED || b == COMPLETED -> COMPLETED
                a.isFailure || b.isFailure -> FAILED
                else ->  COMPLETED_WITH_NO_ACTION
            }
        } .orElse(COMPLETED_WITH_NO_ACTION)
  }

    private fun getAllChildrenJobs(childrenJobsIds: List<String>): Map<UUID, JobDaoImpl> {
        val jobs:MutableList<JobDaoImpl> = dataAccessService.getList(JobDaoImpl::class.java, filterByJobIds(childrenJobsIds), null, DaoUtils.getPropsMap()) as MutableList<JobDaoImpl>
        return jobs.stream().collect(Collectors.toMap( { it.uuid }, { it }))
    }

    private fun filterByJobIds(childrenJobsIds: List<String>): String {
        return " WHERE JOB_ID IN('" + StringUtils.join(childrenJobsIds, "', '") + "')"
    }
}
