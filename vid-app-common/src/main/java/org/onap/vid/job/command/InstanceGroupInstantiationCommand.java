package org.onap.vid.job.command;//package org.onap.vid.job.command;

import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.command.CommandParentData.CommandDataKey;
import org.onap.vid.model.Action;
import org.onap.vid.model.serviceInstantiation.InstanceGroup;
import org.onap.vid.mso.model.InstanceGroupInstantiationRequestDetails;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InstanceGroupInstantiationCommand extends ResourceInstantiationCommand {

    @Inject
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Override
    protected String getRequestPath() {
        return asyncInstantiationBL.getInstanceGroupInstantiationPath();
    }

    @Override
    protected RequestDetailsWrapper<InstanceGroupInstantiationRequestDetails> generateMSORequest(JobAdapter.AsyncJobRequest request, String userId) {
        return asyncInstantiationBL.generateInstanceGroupInstantiationRequest(
                (InstanceGroup) getSharedData().getRequest(),
                commandParentData.getModelInfo(CommandDataKey.SERVICE_MODEL_INFO),
                commandParentData.getInstanceId(CommandDataKey.SERVICE_INSTANCE_ID),
                getSharedData().getUserId()
        );
    }

    @Override
    protected String getJobAuditMSOStatus() {
        return "INSTANCE_GROUP_REQUESTED";
    }

    @Override
    protected boolean shouldInstantiateMyself() {
        return Action.Create == ((InstanceGroup) getSharedData().getRequest()).getAction();
    }
}
