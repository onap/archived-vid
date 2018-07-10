package org.onap.vid.job.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.onap.vid.job.Job.JobStatus;
import org.onap.vid.job.JobCommand;
import org.onap.vid.job.NextCommand;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AuditService;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InProgressStatusCommand implements JobCommand {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(InProgressStatusCommand.class);

    @Inject
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Inject
    private RestMsoImplementation restMso;

    @Inject
    private AuditService auditService;

    private String requestId;

    private UUID jobUuid;

    public InProgressStatusCommand() {
    }

    InProgressStatusCommand(UUID jobUuid, String requestId) {
        init(jobUuid, requestId);
    }

    @Override
    public NextCommand call() {

        try {
            String path = asyncInstantiationBL.getOrchestrationRequestsPath()+"/"+requestId;
            RestObject<AsyncRequestStatus> msoResponse = restMso.GetForObject("", path, AsyncRequestStatus.class);
            JobStatus jobStatus;
            if (msoResponse.getStatusCode() >= 400 || msoResponse.get() == null) {
                auditService.setFailedAuditStatusFromMso(jobUuid, requestId, msoResponse.getStatusCode(), msoResponse.getRaw());
                LOGGER.error(EELFLoggerDelegate.errorLogger,
                        "Failed to get orchestration status for {}. Status code: {},  Body: {}",
                        requestId, msoResponse.getStatusCode(), msoResponse.getRaw());
                return new NextCommand(JobStatus.IN_PROGRESS, this);
            }
            else {
                jobStatus = asyncInstantiationBL.calcStatus(msoResponse.get());
            }

            asyncInstantiationBL.auditMsoStatus(jobUuid,msoResponse.get().request);


            if (jobStatus == JobStatus.FAILED) {
                asyncInstantiationBL.handleFailedInstantiation(jobUuid);
            }
            else {
                asyncInstantiationBL.updateServiceInfoAndAuditStatus(jobUuid, jobStatus);
            }
            //in case of JobStatus.PAUSE we leave the job itself as IN_PROGRESS, for keep tracking job progress
            if (jobStatus == JobStatus.PAUSE) {
                return new NextCommand(JobStatus.IN_PROGRESS, this);
            }
            return new NextCommand(jobStatus, this);
        } catch (javax.ws.rs.ProcessingException e) {
            // Retry when we can't connect MSO during getStatus
            LOGGER.error(EELFLoggerDelegate.errorLogger, "Cannot get orchestration status for {}, will retry: {}", requestId, e, e);
            return new NextCommand(JobStatus.IN_PROGRESS, this);
        } catch (RuntimeException e) {
            LOGGER.error(EELFLoggerDelegate.errorLogger, "Cannot get orchestration status for {}, stopping: {}", requestId, e, e);
            return new NextCommand(JobStatus.STOPPED, this);
        }
    }

    @Override
    public InProgressStatusCommand init(UUID jobUuid, Map<String, Object> data) {
        return init(jobUuid, (String) data.get("requestId"));
    }

    private InProgressStatusCommand init(UUID jobUuid, String requestId) {
        this.requestId = requestId;
        this.jobUuid = jobUuid;
        return this;
    }

    @Override
    public Map<String, Object> getData() {
        return ImmutableMap.of("requestId", requestId);
    }


}
