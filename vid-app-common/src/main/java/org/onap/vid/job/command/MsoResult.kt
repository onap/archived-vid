package org.onap.vid.job.command

import org.onap.vid.job.Job

data class MsoResourceIds (val requestId:String, val instanceId:String)

val EMPTY_MSO_RESOURCE_ID = MsoResourceIds("","")

data class MsoResult @JvmOverloads constructor(val jobStatus: Job.JobStatus, val msoResourceIds: MsoResourceIds = EMPTY_MSO_RESOURCE_ID)
