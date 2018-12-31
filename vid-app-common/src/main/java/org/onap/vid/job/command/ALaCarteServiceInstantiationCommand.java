package org.onap.vid.job.command;

import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.job.JobCommand;
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ALaCarteServiceInstantiationCommand extends ServiceInstantiationCommand implements JobCommand {

    @Override
    protected RequestDetailsWrapper<ServiceInstantiationRequestDetails> generateServiceInstantiationRequest() {
        return asyncInstantiationBL.generateALaCarteServiceInstantiationRequest(
                getSharedData().getJobUuid(), getRequest(), optimisticUniqueServiceInstanceName, getSharedData().getUserId()
        );
    }
}
