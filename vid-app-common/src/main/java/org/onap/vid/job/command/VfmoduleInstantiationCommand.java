package org.onap.vid.job.command;

import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.command.CommandParentData.CommandDataKey;
import org.onap.vid.model.serviceInstantiation.VfModule;
import org.onap.vid.mso.model.VfModuleInstantiationRequestDetails;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VfmoduleInstantiationCommand extends ResourceInstantiationCommand {
    @Inject
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Override
    protected String getRequestPath() {
        return asyncInstantiationBL.getVfmoduleInstantiationPath(commandParentData.getInstanceId(CommandDataKey.SERVICE_INSTANCE_ID),commandParentData.getInstanceId(CommandDataKey.VNF_INSTANCE_ID));
    }

    @Override
    protected RequestDetailsWrapper<VfModuleInstantiationRequestDetails> generateMSORequest(JobAdapter.AsyncJobRequest request, String userId) {
        return asyncInstantiationBL.generateVfModuleInstantiationRequest(
                (VfModule) getSharedData().getRequest(),
                commandParentData.getModelInfo(CommandDataKey.SERVICE_MODEL_INFO),
                commandParentData.getInstanceId(CommandDataKey.SERVICE_INSTANCE_ID),
                commandParentData.getModelInfo(CommandDataKey.VNF_MODEL_INFO),
                commandParentData.getInstanceId(CommandDataKey.VNF_INSTANCE_ID),
                commandParentData.getInstanceId(CommandDataKey.VG_INSTANCE_ID),
                 getSharedData().getUserId()
        );
    }

    @Override
    protected String getJobAuditMSOStatus() {
        return "VF_MODULE_REQUESTED";
    }

}
