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

import com.fasterxml.jackson.module.kotlin.convertValue
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate
import org.onap.vid.changeManagement.RequestDetailsWrapper
import org.onap.vid.job.Job
import org.onap.vid.job.Job.JobStatus
import org.onap.vid.job.JobAdapter
import org.onap.vid.job.JobCommand
import org.onap.vid.job.NextCommand
import org.onap.vid.job.impl.JobSharedData
import org.onap.vid.model.Action
import org.onap.vid.model.RequestReferencesContainer
import org.onap.vid.model.serviceInstantiation.BaseResource
import org.onap.vid.mso.RestMsoImplementation
import org.onap.vid.utils.JACKSON_OBJECT_MAPPER
import org.onap.vid.utils.getEnumFromMapOfStrings
import org.springframework.http.HttpMethod
import java.util.*

const val INTERNAL_STATE = "internalState"
const val ACTION_PHASE = "actionPhase"
const val CHILD_JOBS = "childJobs"
const val MSO_RESOURCE_ID = "msoResourceIds"
const val CUMULATIVE_STATUS = "cumulativeStatus"

enum class InternalState constructor(val immediate:Boolean=false) {
    INITIAL,
    CREATING_CHILDREN(true),
    WATCHING,
    DELETE_MYSELF,
    CREATE_MYSELF,
    IN_PROGRESS,
    TERMINAL
}

data class NextInternalState(val nextActionPhase: Action, val nextInternalState: InternalState)


data class MsoRestCallPlan(
        val httpMethod: HttpMethod,
        val path: String,
        val payload: Optional<RequestDetailsWrapper<out Any>>,
        val userId: Optional<String>,
        val actionDescription: String
)

abstract class ResourceCommand(
        protected val restMso: RestMsoImplementation,
        protected val inProgressStatusService: InProgressStatusService,
        protected val msoResultHandlerService: MsoResultHandlerService,
        protected val watchChildrenJobsBL: WatchChildrenJobsBL
) : CommandBase(), JobCommand {

    companion object {
        private val Logger = EELFLoggerDelegate.getLogger(ResourceCommand::class.java)
    }

    abstract fun createChildren():JobStatus

    abstract fun planCreateMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String): MsoRestCallPlan

    abstract fun planDeleteMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String): MsoRestCallPlan

    private val commandByInternalState: Map<InternalState, () -> JobStatus> = hashMapOf(
            Pair(InternalState.CREATING_CHILDREN, ::createChildren),
            Pair(InternalState.WATCHING, ::watchChildren),
            Pair(InternalState.CREATE_MYSELF, ::createMyself),
            Pair(InternalState.DELETE_MYSELF, ::deleteMyself),
            Pair(InternalState.IN_PROGRESS, ::inProgress)
    )

    private lateinit var internalState:InternalState
    protected lateinit var actionPhase: Action
    private var commandParentData: CommandParentData = CommandParentData()
    private var msoResourceIds: MsoResourceIds = EMPTY_MSO_RESOURCE_ID
    protected var childJobs:List<String> = emptyList()
    private lateinit var cumulativeStatus:JobStatus


    override fun call(): NextCommand {
        var jobStatus:JobStatus = invokeCommand()
        jobStatus = comulateStatusAndUpdatePropertyIfFinal(jobStatus)

        Logger.debug("command for job ${sharedData.jobUuid} invoked and finished with jobStatus $jobStatus")
        if (shallStopJob(jobStatus)) {
            onFinal(jobStatus)
            return NextCommand(jobStatus)
        }

        val (nextActionPhase, nextInternalState) = calcNextInternalState(jobStatus, internalState, actionPhase)
        Logger.debug("next state for job ${sharedData.jobUuid} is $nextInternalState")
        actionPhase = nextActionPhase
        internalState = nextInternalState

        if (internalState==InternalState.TERMINAL) {
            onFinal(jobStatus)
            return NextCommand(jobStatus)
        }

        jobStatus = getExternalInProgressStatus()
        Logger.debug("next status for job ${sharedData.jobUuid} is $jobStatus")
//        if (internalState.immediate) return call() //shortcut instead of execute another command
        return NextCommand(jobStatus, this)
    }

    //we want to stop in faliures, except for service witn no action, since service with no action trigger 2 phases (delete and create)
    protected fun shallStopJob(jobStatus: JobStatus) =
            jobStatus.isFailure && !(isServiceCommand() && getActionType()==Action.None)

    //this method is used to expose the job status after successful completion of current state
    //should be override by subclass (like ServiceCommand) that need to return other default job status
    protected open fun getExternalInProgressStatus() = JobStatus.RESOURCE_IN_PROGRESS

    private fun invokeCommand(): JobStatus {
        return commandByInternalState.getOrDefault (internalState, ::throwIllegalState).invoke()
    }

    private fun throwIllegalState():JobStatus {
             throw IllegalStateException("can't find action for pashe $actionPhase and state $internalState")
    }

    private fun calcNextInternalState(jobStatus: JobStatus, internalState: InternalState, actionPhase: Action): NextInternalState {

        val nextInternalState = when (actionPhase) {
            Action.Delete -> calcNextStateDeletePhase(jobStatus, internalState)
            Action.Create -> calcNextStateCreatePhase(jobStatus, internalState)
            else -> InternalState.TERMINAL
        }

        if (nextInternalState == InternalState.TERMINAL
                && actionPhase == Action.Delete
                && isServiceCommand()) {
            // Loop over to "Create" phase
            return NextInternalState(Action.Create, InternalState.INITIAL)
        }

        return NextInternalState(actionPhase, nextInternalState)

    }

    //no need to refer to failed (final) states here
    //This method is called only for non final states or COMPLETED
    protected fun calcNextStateDeletePhase(jobStatus: JobStatus, internalState: InternalState): InternalState {
        return when (internalState) {

            InternalState.CREATING_CHILDREN -> InternalState.WATCHING

            InternalState.WATCHING -> {
                when {
                    !jobStatus.isFinal -> InternalState.WATCHING
                    isNeedToDeleteMyself() -> InternalState.DELETE_MYSELF
                    else -> InternalState.TERMINAL
                }
            }

            InternalState.DELETE_MYSELF -> InternalState.IN_PROGRESS

            InternalState.IN_PROGRESS -> {
                if (jobStatus == Job.JobStatus.COMPLETED) InternalState.TERMINAL else InternalState.IN_PROGRESS
            }

            else -> InternalState.TERMINAL
        }
    }

    protected fun calcNextStateCreatePhase(jobStatus: JobStatus, internalState: InternalState): InternalState {
        return when (internalState) {

            InternalState.CREATE_MYSELF -> InternalState.IN_PROGRESS

            InternalState.IN_PROGRESS -> {
                if (jobStatus == Job.JobStatus.COMPLETED) InternalState.CREATING_CHILDREN else InternalState.IN_PROGRESS
            }

            InternalState.CREATING_CHILDREN -> InternalState.WATCHING

            InternalState.WATCHING -> {
                when {
                    !jobStatus.isFinal -> InternalState.WATCHING
                    else -> InternalState.TERMINAL
                }
            }


            else -> InternalState.TERMINAL
        }
    }

    override fun getData(): Map<String, Any?> {
        return mapOf(
                ACTION_PHASE to actionPhase,
                INTERNAL_STATE to internalState,
                MSO_RESOURCE_ID to msoResourceIds,
                CHILD_JOBS to childJobs,
                CUMULATIVE_STATUS to cumulativeStatus
        )
    }

    override fun init(sharedData: JobSharedData, commandData: Map<String, Any>): ResourceCommand {
        init(sharedData)
        val resourceIdsRaw:Any? = commandData[MSO_RESOURCE_ID]
        commandParentData.initParentData(commandData)
        msoResourceIds =
                if (resourceIdsRaw != null) JACKSON_OBJECT_MAPPER.convertValue(resourceIdsRaw)
                else EMPTY_MSO_RESOURCE_ID

        childJobs = JACKSON_OBJECT_MAPPER.convertValue(commandData.getOrDefault(CHILD_JOBS, emptyList<String>()))
        cumulativeStatus = getEnumFromMapOfStrings(commandData, CUMULATIVE_STATUS, JobStatus.COMPLETED_WITH_NO_ACTION)
        actionPhase = getEnumFromMapOfStrings(commandData, ACTION_PHASE, Action.Delete)
        internalState = calcInitialState(commandData, actionPhase)
        return this
    }

    private fun calcInitialState(commandData: Map<String, Any>, phase: Action):InternalState {
        val status:InternalState = getEnumFromMapOfStrings(commandData, INTERNAL_STATE, InternalState.INITIAL)
        if (status == InternalState.INITIAL) {
            onInitial(phase)
            return when (phase) {
                Action.Delete -> InternalState.CREATING_CHILDREN
                Action.Create -> if (isNeedToCreateMyself()) InternalState.CREATE_MYSELF else InternalState.CREATING_CHILDREN
                else -> throw IllegalStateException("state $internalState is not supported yet")
            }
        }
        return status
    }

    //command may override it in order to do something while init state
    protected open fun onInitial(phase: Action) {
        //do nothing
    }

    //command may override it in order to do something while final status
    protected open fun onFinal(jobStatus: JobStatus) {
        //do nothing
    }

    protected open fun getRequest(): BaseResource {
        return sharedData.request as BaseResource
    }

    protected open fun getActionType(): Action {
        return getRequest().action
    }

    protected open fun isServiceCommand(): Boolean = false

    protected open fun isNeedToDeleteMyself(): Boolean = getActionType() == Action.Delete

    protected open fun isNeedToCreateMyself(): Boolean = getActionType() == Action.Create

    protected open fun inProgress(): Job.JobStatus {
        val requestId:String = msoResourceIds.requestId;
        return try {
            val jobStatus = inProgressStatusService.call(getExpiryChecker(), sharedData, requestId)
            handleInProgressStatus(jobStatus)
        } catch (e: javax.ws.rs.ProcessingException) {
            // Retry when we can't connect MSO during getStatus
            Logger.error(EELFLoggerDelegate.errorLogger, "Cannot get orchestration status for {}, will retry: {}", requestId, e, e)
            Job.JobStatus.IN_PROGRESS;
        } catch (e: InProgressStatusService.BadResponseFromMso) {
            inProgressStatusService.handleFailedMsoResponse(sharedData.jobUuid, requestId, e.msoResponse)
            Job.JobStatus.IN_PROGRESS
        } catch (e: RuntimeException) {
            Logger.error(EELFLoggerDelegate.errorLogger, "Cannot get orchestration status for {}, stopping: {}", requestId, e, e)
            Job.JobStatus.STOPPED
        }
    }

    fun createMyself(): Job.JobStatus {
        val createMyselfCommand = planCreateMyselfRestCall(commandParentData, sharedData.request, sharedData.userId)

        return executeAndHandleMsoInstanceRequest(createMyselfCommand)
    }

    fun deleteMyself(): Job.JobStatus {
        val deleteMyselfCommand = planDeleteMyselfRestCall(commandParentData, sharedData.request, sharedData.userId)

        return executeAndHandleMsoInstanceRequest(deleteMyselfCommand)
    }

    private fun executeAndHandleMsoInstanceRequest(restCallPlan: MsoRestCallPlan): JobStatus {
        val msoResponse = restMso.restCall(
                restCallPlan.httpMethod,
                RequestReferencesContainer::class.java,
                restCallPlan.payload.orElse(null),
                restCallPlan.path,
                restCallPlan.userId
        )

        val msoResult = if (isServiceCommand()) {
            msoResultHandlerService.handleRootResponse(sharedData.jobUuid, msoResponse)
        } else {
            msoResultHandlerService.handleResponse(msoResponse, restCallPlan.actionDescription)
        }

        this.msoResourceIds = msoResult.msoResourceIds
        return msoResult.jobStatus
    }

    protected open fun getExpiryChecker(): ExpiryChecker = ExpiryChecker {false}

    protected open fun handleInProgressStatus(jobStatus: JobStatus): JobStatus {
        return  if (jobStatus == Job.JobStatus.PAUSE) Job.JobStatus.IN_PROGRESS else jobStatus
    }

    protected open fun watchChildren():JobStatus {
        return watchChildrenJobsBL.retrieveChildrenJobsStatus(childJobs)
    }

    private fun comulateStatusAndUpdatePropertyIfFinal(internalStateStatus: JobStatus): JobStatus {
        val status = watchChildrenJobsBL.cumulateJobStatus(internalStateStatus, cumulativeStatus)

        //we want to update cumulativeStatus only for final status
        if (status.isFinal) {
            cumulativeStatus = status;
        }

        return status
    }
}



