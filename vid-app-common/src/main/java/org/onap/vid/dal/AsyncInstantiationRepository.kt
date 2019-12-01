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

package org.onap.vid.dal

import org.onap.portalsdk.core.domain.support.DomainVo
import org.onap.portalsdk.core.service.DataAccessService
import org.onap.vid.dao.JobRequest
import org.onap.vid.exceptions.GenericUncheckedException
import org.onap.vid.exceptions.NotFoundException
import org.onap.vid.job.Job
import org.onap.vid.model.JobAuditStatus
import org.onap.vid.model.ResourceInfo
import org.onap.vid.model.ServiceInfo
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation
import org.onap.vid.utils.DaoUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

@Repository
class AsyncInstantiationRepository @Autowired constructor(val dataAccessService:DataAccessService) {

    fun addJobRequest(jobUuid: UUID, request:ServiceInstantiation) {
        save(JobRequest(jobUuid, request))
    }

    fun getJobRequest(jobUuid: UUID):ServiceInstantiation? {
        return getSingleItem(JobRequest::class.java, "JOB_ID", jobUuid).request
    }

    fun saveResourceInfo(resource:ResourceInfo) {
        save(resource)
    }

    fun getResourceInfoByRootJobId(rootJobId: UUID): Map<String, ResourceInfo> {
        val resourceInfoList:List<ResourceInfo> = getResultList(ResourceInfo::class.java, "ROOT_JOB_ID", rootJobId)

        if (resourceInfoList.isEmpty()) {
            throw GenericUncheckedException("Failed to retrieve resource info with rootJobId " + rootJobId + " from ResourceInfo table. no resource found")
        }
        return resourceInfoList.fold(HashMap(), { accumulator, item ->
            accumulator.put(item.trackById, item); accumulator})
    }

    fun getResourceInfoByTrackId(trackById: String):ResourceInfo {
        return getSingleItem(ResourceInfo::class.java, "TRACK_BY_ID", trackById)
    }

    fun saveServiceInfo(serviceInfo: ServiceInfo) {
        save(serviceInfo)
    }

    fun getServiceInfoByJobId(jobUuid: UUID): ServiceInfo {
        return getSingleItem(ServiceInfo::class.java, "jobId", jobUuid)
    }

    fun getServiceInfoByTemplateIdAndJobStatus(templateId: UUID, jobStatus: Job.JobStatus): List<ServiceInfo> {
        return getResultList(ServiceInfo::class.java, mapOf("templateId" to templateId, "jobStatus" to jobStatus), "AND")
    }

    fun getAllServicesInfo(): List<ServiceInfo> {
        return dataAccessService.getList(ServiceInfo::class.java, filterByCreationDateAndNotDeleted(), orderByCreatedDateAndStatus(), null) as List<ServiceInfo>
    }

    private fun filterByCreationDateAndNotDeleted(): String {
        val minus3Months = LocalDateTime.now().minusMonths(3)
        val filterDate = Timestamp.valueOf(minus3Months)
        return filterServicesByNotHiddenAndNotDeleted() +
                "   and created >= '" + filterDate + "' "
    }

    private fun filterByServiceModelId(serviceModelUuid: UUID): String {
        return filterServicesByNotHiddenAndNotDeleted() +
                " and SERVICE_MODEL_ID = '$serviceModelUuid'"
    }

    private fun filterServicesByNotHiddenAndNotDeleted(): String {
        return " WHERE" +
                "   hidden = false" +
                "   and deleted_at is null" // don't fetch deleted
    }


    private fun orderByCreatedDateAndStatus(): String {
        return " createdBulkDate DESC ,\n" +
                "  (CASE jobStatus\n" +
                "   WHEN 'COMPLETED' THEN 0\n" +
                "   WHEN 'FAILED' THEN 0\n" +
                "   WHEN 'COMPLETED_WITH_ERRORS' THEN 0\n" +
                "   WHEN 'IN_PROGRESS' THEN 1\n" +
                "   WHEN 'PAUSE' THEN 2\n" +
                "   WHEN 'PENDING' THEN 3\n" +
                "   WHEN 'STOPPED' THEN 3 END),\n" +
                "  statusModifiedDate "
    }

    fun getAuditStatuses(jobUUID: UUID, source: JobAuditStatus.SourceStatus): List<JobAuditStatus> {
        // order by ORDINAL.
        // CREATED_DATE is kept for backward compatibility: when all Ordinals are zero
        return getResultList(JobAuditStatus::class.java, mapOf("SOURCE" to source, "JOB_ID" to jobUUID), "AND", " ORDINAL, CREATED_DATE ")
    }

    fun addJobAudiStatus(jobAuditStatus:JobAuditStatus) {
        save(jobAuditStatus)
    }

    private fun <T: DomainVo> save(item:T) {
        dataAccessService.saveDomainObject(item, DaoUtils.getPropsMap())
    }

    private fun <T> getSingleItem(className:Class<T>, filterKey:String, filterValue:Any): T {
        val resultList:List<T> = getResultList(className, filterKey, filterValue)
        if (resultList.size < 1) {
            throw NotFoundException("Failed to retrieve $className with $filterKey $filterValue from table. no resource found")
        }else if (resultList.size > 1) {
            throw GenericUncheckedException("Failed to retrieve $className with $filterKey $filterValue from table. found more than 1 resources")
        }
        return resultList[0]
    }

    private fun <T> getResultList(className:Class<T>, filterKey:String, filterValue:Any): List<T> {
        return getResultList(className, mapOf(filterKey to filterValue), "AND", null)
    }

    private fun <T> getResultList(className:Class<T>, filters: Map<String, Any>, conditionType: String): List<T> {
        return getResultList(className, filters, conditionType, null)
    }

    private fun <T> getResultList(className:Class<T>, filters: Map<String, Any>, conditionType: String, orderBy: String?): List<T> {
        var condition:String = filters
                .map{f -> f.key + " = '" + f.value + "'"}
                .joinToString(" $conditionType ")
        return dataAccessService.getList(className, " WHERE $condition", orderBy, null) as List<T>
    }

    fun listServicesByServiceModelId(modelUuid: UUID): List<ServiceInfo> =
            dataAccessService.getList(ServiceInfo::class.java, filterByServiceModelId(modelUuid), orderByCreatedDateAndStatus(), null) as List<ServiceInfo>;
}
