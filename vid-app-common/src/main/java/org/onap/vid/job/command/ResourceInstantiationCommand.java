package org.onap.vid.job.command;

import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobCommand;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.model.BaseResourceInstantiationRequestDetails;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AuditService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Map;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class ResourceInstantiationCommand extends BaseInstantiationCommand implements JobCommand {


    @Inject
    protected RestMsoImplementation restMso;

    @Inject
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Inject
    private AuditService auditService;

    @Override
    public ResourceInstantiationCommand init(JobSharedData sharedData, Map<String, Object> commandData) {
      super.init(sharedData, commandData);
        return this;
    }

    @Override
    public Map<String, Object> getData() {
       return commandParentData.getParentData();
    }

    @Override
    public NextCommand call() {
        if (!shouldInstantiateMyself()) {
            return new NextCommand(Job.JobStatus.COMPLETED_WITH_NO_ACTION);
        }

        RequestDetailsWrapper<? extends BaseResourceInstantiationRequestDetails> requestDetailsWrapper = generateMSORequest(
                getSharedData().getRequest(),
                getSharedData().getUserId()
                );
        String instantiatePath = getRequestPath();

        RestObject<RequestReferencesContainer> msoResponse = restMso.PostForObject(requestDetailsWrapper,
                instantiatePath, RequestReferencesContainer.class);

        if (msoResponse.getStatusCode() >= 200 && msoResponse.getStatusCode() < 400) {
            String requestId = msoResponse.get().getRequestReferences().getRequestId();
            String instanceId = msoResponse.get().getRequestReferences().getInstanceId();
            asyncInstantiationBL.auditMsoStatus(getSharedData().getRootJobId(), getJobAuditMSOStatus(), requestId, null);
            return getNextCommand(requestId, instanceId);
        }
        else {
            auditService.setFailedAuditStatusFromMso(getSharedData().getRootJobId(), null, msoResponse.getStatusCode(), msoResponse.getRaw());
            return new NextCommand(Job.JobStatus.FAILED);
        }
    }
    protected NextCommand getNextCommand(String requestId, String instanceId){
        return new NextCommand(Job.JobStatus.RESOURCE_IN_PROGRESS, new ResourceInProgressStatusCommand(getSharedData(), requestId, instanceId));
    }

    protected boolean shouldInstantiateMyself() {
        return true;
    }

    protected abstract String getRequestPath();
    protected abstract RequestDetailsWrapper<? extends BaseResourceInstantiationRequestDetails> generateMSORequest(JobAdapter.AsyncJobRequest request, String userId);
    protected abstract String getJobAuditMSOStatus();
}


