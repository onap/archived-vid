package org.onap.vid.job.command;

import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.command.CommandParentData.CommandDataKey;
import org.onap.vid.model.serviceInstantiation.Network;
import org.onap.vid.mso.model.NetworkInstantiationRequestDetails;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NetworkInstantiationCommand extends ResourceInstantiationCommand {

    @Inject
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Override
    protected String getRequestPath() {
        return asyncInstantiationBL.getNetworkInstantiationPath(commandParentData.getInstanceId(CommandDataKey.SERVICE_INSTANCE_ID));
    }

    @Override
    protected RequestDetailsWrapper<NetworkInstantiationRequestDetails> generateMSORequest(JobAdapter.AsyncJobRequest request, String userId) {
        return asyncInstantiationBL.generateNetworkInstantiationRequest(
                (Network) getSharedData().getRequest(),
                commandParentData.getModelInfo(CommandDataKey.SERVICE_MODEL_INFO),
                commandParentData.getInstanceId(CommandDataKey.SERVICE_INSTANCE_ID),
                getSharedData().getUserId()
        );
    }

    @Override
    protected String getJobAuditMSOStatus() {
        return "NETWORK_REQUESTED";
    }
}
