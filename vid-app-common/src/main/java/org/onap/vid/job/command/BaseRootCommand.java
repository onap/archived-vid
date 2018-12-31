package org.onap.vid.job.command;

import org.onap.vid.job.NextCommand;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.RestObject;

import javax.inject.Inject;


public abstract class BaseRootCommand extends CommandBase{

    @Inject
    private MsoResultHandlerService msoResultHandlerService;

    @Override
    protected CommandBase init(JobSharedData sharedData) {
        super.init(sharedData);
        return this;
    }

    protected ServiceInstantiation getRequest() {
        return msoResultHandlerService.getRequest(getSharedData());
    }

    protected NextCommand handleRootResponse(RestObject<RequestReferencesContainer> msoResponse){
        MsoResult msoResult = msoResultHandlerService.handleRootResponse(getSharedData().getJobUuid(), msoResponse);
        return new NextCommand(msoResult.getJobStatus(),
                (msoResult.getMsoResourceIds()!=null) ?
                        new ServiceInProgressStatusCommand(getSharedData(), msoResult.getMsoResourceIds()) :
                        null
        );

    }

    protected NextCommand handleCommandFailed() {
        return new NextCommand(msoResultHandlerService.handleRootCommandFailed(getSharedData().getJobUuid()).getJobStatus());
    }

}
