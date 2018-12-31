package org.onap.vid.scheduler;

import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.model.probes.ErrorMetadata;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.model.probes.HttpRequestMetadata;
import org.onap.vid.mso.RestObjectWithRequestInfo;
import org.onap.vid.services.ChangeManagementService;
import org.onap.vid.utils.Logging;
import org.springframework.beans.factory.annotation.Autowired;

public class SchedulerServiceImpl implements SchedulerService{

    private final ChangeManagementService changeManagementService;


    @Autowired
    public SchedulerServiceImpl(ChangeManagementService changeManagementService) {
        this.changeManagementService = changeManagementService;
    }

    @Override
    public ExternalComponentStatus probeGetSchedulerChangeManagements() {
        long startTime = System.currentTimeMillis();
        try {
            RestObjectWithRequestInfo response = this.changeManagementService.getSchedulerChangeManagementsWithRequestInfo();
            return new ExternalComponentStatus(
                    ExternalComponentStatus.Component.SCHEDULER,
                    true,
                    new HttpRequestMetadata(response, "OK", startTime)
            );
        } catch (ExceptionWithRequestInfo e) {
            long duration = System.currentTimeMillis() - startTime;
            return new ExternalComponentStatus(ExternalComponentStatus.Component.SCHEDULER,
                    false,
                    new HttpRequestMetadata(e, duration));
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            return new ExternalComponentStatus(ExternalComponentStatus.Component.SCHEDULER, false,
                    new ErrorMetadata(Logging.exceptionToDescription(e), duration));
        }
    }
}
