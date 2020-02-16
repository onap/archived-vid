package org.onap.vid.job.command

import org.apache.commons.collections.MapUtils
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate
import org.onap.vid.asdc.AsdcCatalogException
import org.onap.vid.job.*
import org.onap.vid.job.impl.JobSharedData
import org.onap.vid.model.Action
import org.onap.vid.model.serviceInstantiation.BaseResource
import org.onap.vid.model.serviceInstantiation.VfModule
import org.onap.vid.model.serviceInstantiation.Vnf
import org.onap.vid.mso.RestMsoImplementation
import org.onap.vid.properties.Features
import org.onap.vid.services.AsyncInstantiationBusinessLogic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.togglz.core.manager.FeatureManager
import java.util.*
import java.util.stream.Collectors
import kotlin.properties.Delegates

const val NEED_TO_CREATE_BASE_MODULE = "needToCreateBaseModule"

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class VnfCommand @Autowired constructor(
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
        watchChildrenJobsBL, jobsBrokerService, jobAdapter), JobCommand {

    private var needToCreateBaseModule:Boolean by Delegates.notNull<Boolean>()

    override fun getData(): Map<String, Any?> {
        return super.getData() + mapOf(NEED_TO_CREATE_BASE_MODULE to needToCreateBaseModule)
    }

    override fun init(sharedData: JobSharedData, commandData: Map<String, Any>): ResourceCommand {
        super<ResourceCommand>.init(sharedData, commandData)
        needToCreateBaseModule = commandData.getOrDefault(NEED_TO_CREATE_BASE_MODULE, actionPhase != Action.Delete) as Boolean
        return this
    }


    override fun createChildren(): Job.JobStatus {
        val request:Vnf = getRequest()
        if(isNeedToCreateChildJobs()){
            val dataForChild = buildDataForChild(request, actionPhase)
            val vfModules:List<VfModule> = request.vfModules.values.stream().flatMap { vfKey -> vfKey.values.stream() }.collect(Collectors.toList<VfModule>())

            try {
                childJobs = pushChildrenJobsToBroker(vfModulesForChildrenJobs(vfModules), dataForChild, JobType.VolumeGroupInstantiation)
            } catch (e: AsdcCatalogException) {
                LOGGER.error(EELFLoggerDelegate.errorLogger, "Failed to retrieve service definitions from SDC, for VfModule is BaseModule.. Error: " + e.message, e)
                //return Job.JobStatus.FAILED
                throw e;
            }
        }

        return Job.JobStatus.COMPLETED_WITH_NO_ACTION
    }

    private fun vfModulesForChildrenJobs(vfModules: List<VfModule>): List<VfModule> =
            vfModules
                    .filter { filterModuleByNeedToCreateBase(it) }
                    .map { childVfModuleWithVnfRegionAndTenant(it) }

    internal fun childVfModuleWithVnfRegionAndTenant(vfModule: VfModule): VfModule {
        if (!shouldEntailRegionAndTenantToVfModule(vfModule)) {
            return vfModule
        }

        val vnfLcpCloudRegionId = getRequest().lcpCloudRegionId
        val vnfTenantId = getRequest().tenantId
        return vfModule.cloneWith(vnfLcpCloudRegionId, vnfTenantId)
    }

    private fun shouldEntailRegionAndTenantToVfModule(vfModule: VfModule) =
            vfModule.action == Action.Create
                    && featureManager.isActive(Features.FLAG_2006_VFMODULE_TAKES_TENANT_AND_REGION_FROM_VNF)

    private fun filterModuleByNeedToCreateBase(vfModule: VfModule): Boolean {
        return needToCreateBaseModule ==
                commandUtils.isVfModuleBaseModule(
                        serviceModelInfoFromRequest().modelVersionId,
                        vfModule.modelInfo.modelVersionId)
    }

    override fun planCreateMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String, testApi: String?): MsoRestCallPlan {
        val serviceInstanceId = serviceInstanceIdFromRequest()

        val instantiatePath = asyncInstantiationBL.getVnfInstantiationPath(serviceInstanceId)

        val requestDetailsWrapper = msoRequestBuilder.generateVnfInstantiationRequest(
                request as Vnf,
                serviceModelInfoFromRequest(), serviceInstanceId,
                userId,
                testApi
        )

        val actionDescription = "create vnf in $serviceInstanceId"

        return MsoRestCallPlan(HttpMethod.POST, instantiatePath, Optional.of(requestDetailsWrapper), Optional.empty(), actionDescription)

    }

    override fun addMyselfToChildrenData(commandParentData: CommandParentData, request: BaseResource) {
        commandParentData.addModelInfo(CommandParentData.CommandDataKey.VNF_MODEL_INFO, request.modelInfo);
        commandParentData.addInstanceId(CommandParentData.CommandDataKey.VNF_INSTANCE_ID, getActualInstanceId(request))
    }

    override fun getRequest(): Vnf {
        return sharedData.request as Vnf
    }

    private fun isNeedToCreateChildJobs(): Boolean {
        return featureManager.isActive(Features.FLAG_ASYNC_ALACARTE_VFMODULE) &&
                MapUtils.isNotEmpty(getRequest().vfModules)
    }

    override fun planDeleteMyselfRestCall(commandParentData: CommandParentData, request: JobAdapter.AsyncJobRequest, userId: String): MsoRestCallPlan {
        val serviceInstanceId = serviceInstanceIdFromRequest()
        val path = asyncInstantiationBL.getVnfDeletionPath(serviceInstanceId, getRequest().instanceId)
        val requestDetailsWrapper = msoRequestBuilder.generateDeleteVnfRequest(getRequest(), userId)
        return MsoRestCallPlan(HttpMethod.DELETE, path, Optional.of(requestDetailsWrapper), Optional.of(userId),
                "delete vnf ${getRequest().instanceId} from service $serviceInstanceId")

    }

    companion object {
        private val LOGGER = EELFLoggerDelegate.getLogger(VnfCommand::class.java)
    }

    //in Delete phase - we delete all non-base vf-modules first, before base vf-module
    //in Create phase - we create base vf-module first, and then all the others
    override fun watchChildren(): Job.JobStatus {
        val childrenStatus:Job.JobStatus = comulateStatusAndUpdatePropertyIfFinal(watchChildrenJobsBL.retrieveChildrenJobsStatus(childJobs))
        if (!childrenStatus.isFinal ||
                childrenStatus.isFailure ||
                (actionPhase == Action.Create && !needToCreateBaseModule) ||
                (actionPhase == Action.Delete && needToCreateBaseModule)) {
            return childrenStatus
        }

        needToCreateBaseModule = !needToCreateBaseModule;
        createChildren()
        return Job.JobStatus.IN_PROGRESS
    }
}
