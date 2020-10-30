package org.onap.vid.job.command

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate
import org.onap.vid.asdc.AsdcCatalogException
import org.onap.vid.job.*
import org.onap.vid.job.impl.JobSharedData
import org.onap.vid.model.Action
import org.onap.vid.model.serviceInstantiation.BaseResource.PauseInstantiation
import org.onap.vid.model.serviceInstantiation.Pnf
import org.onap.vid.model.serviceInstantiation.VfModule
import org.onap.vid.model.serviceInstantiation.Vnf
import org.onap.vid.mso.RestMsoImplementation
import org.onap.vid.properties.Features
import org.onap.vid.services.AsyncInstantiationBusinessLogic
import org.onap.vid.utils.takeUntilIncluding
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.togglz.core.manager.FeatureManager
import java.util.*
import java.util.stream.Collectors
import kotlin.properties.Delegates

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class PnfCommand @Autowired constructor(
        private val asyncInstantiationBL: AsyncInstantiationBusinessLogic,
        restMso: RestMsoImplementation,
        private val msoRequestBuilder: MsoRequestBuilder,
        msoResultHandlerService: MsoResultHandlerService,
        inProgressStatusService:InProgressStatusService,
        watchChildrenJobsBL: WatchChildrenJobsBL,
        jobsBrokerService: JobsBrokerService,
        jobAdapter: JobAdapter,
        private val featureManager: FeatureManager
) : ResourceCommand(restMso, inProgressStatusService, msoResultHandlerService,
        watchChildrenJobsBL, jobsBrokerService, jobAdapter, featureManager), JobCommand {

    override fun getData(): Map<String, Any?> {
        return super.getData();
    }

    override fun init(sharedData: JobSharedData, commandData: Map<String, Any>): ResourceCommand {
        super<ResourceCommand>.init(sharedData, commandData)
        return this
    }

    override fun createChildren(): Job.JobStatus {
        TODO("Not yet implemented")
    }

    override fun planCreateMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String, testApi: String?): MsoRestCallPlan {
        return MsoRestCallPlan(HttpMethod.POST, "", Optional.empty(), Optional.empty(), "");
    }

    override fun planDeleteMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String): MsoRestCallPlan {
        TODO("Not yet implemented")
    }

    override fun getRequest(): Pnf {
        return sharedData.request as Pnf
    }

    companion object {
        private val LOGGER = EELFLoggerDelegate.getLogger(VnfCommand::class.java)
    }

}
