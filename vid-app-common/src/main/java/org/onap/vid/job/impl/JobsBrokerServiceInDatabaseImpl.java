package org.onap.vid.job.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.exceptions.OperationNotAllowedException;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.properties.VidProperties;
import org.onap.vid.utils.DaoUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class JobsBrokerServiceInDatabaseImpl implements JobsBrokerService {

    static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(JobsBrokerServiceInDatabaseImpl.class);

    private final DataAccessService dataAccessService;

    private final SessionFactory sessionFactory;
    private int maxOpenedInstantiationRequestsToMso;
    private int pollingIntervalSeconds;

    @Autowired
    public JobsBrokerServiceInDatabaseImpl(DataAccessService dataAccessService, SessionFactory sessionFactory,
                                           @Value("0") int maxOpenedInstantiationRequestsToMso,
                                           @Value("10") int pollingIntervalSeconds) {
        // tha @Value will inject conservative defaults; overridden in @PostConstruct from configuration
        this.dataAccessService = dataAccessService;
        this.sessionFactory = sessionFactory;
        this.maxOpenedInstantiationRequestsToMso = maxOpenedInstantiationRequestsToMso;
        this.pollingIntervalSeconds = pollingIntervalSeconds;
    }

    @PostConstruct
    public void configure() {
        maxOpenedInstantiationRequestsToMso = Integer.parseInt(SystemProperties.getProperty(VidProperties.MSO_MAX_OPENED_INSTANTIATION_REQUESTS));
        pollingIntervalSeconds = Integer.parseInt(SystemProperties.getProperty(VidProperties.MSO_ASYNC_POLLING_INTERVAL_SECONDS));
    }

    public void deleteAll() {
        dataAccessService.deleteDomainObjects(JobDaoImpl.class, "1=1", null);
    }

    @Override
    public UUID add(Job job) {
        final JobDaoImpl jobDao = castToJobDaoImpl(job);
        jobDao.setUuid(UUID.randomUUID());
        dataAccessService.saveDomainObject(jobDao, DaoUtils.getPropsMap());
        return job.getUuid();
    }

    @Override
    public Optional<Job> pull(Job.JobStatus topic, String ownerId) {
        JobDaoImpl daoJob;
        int updatedEntities;
        do {
            String query = sqlQueryForTopic(topic);
            List<JobDaoImpl> jobs = dataAccessService.executeSQLQuery(query, JobDaoImpl.class, null);
            if (jobs.isEmpty()) {
                return Optional.empty();
            }

            daoJob = jobs.get(0);

            final UUID uuid = daoJob.getUuid();
            final Integer age = daoJob.getAge();

            daoJob.setTakenBy(ownerId);

            // It might become that daoJob was taken and pushed-back already, before we
            // arrived here, so we're verifying the age was not pushed forward.
            // Age is actually forwarded upon pushBack().
            String hqlUpdate = "update JobDaoImpl job set job.takenBy = :takenBy where " +
                    " job.id = :id" +
                    " and job.age = :age" +
                    " and takenBy is null";
            updatedEntities = DaoUtils.tryWithSessionAndTransaction(sessionFactory, session ->
                    session.createQuery(hqlUpdate)
                            .setText("id", uuid.toString())
                            .setInteger("age", age)
                            .setText("takenBy", ownerId)
                            .executeUpdate());

        } while (updatedEntities == 0);

        return Optional.ofNullable(daoJob);
    }

    private java.sql.Timestamp nowMinusInterval() {
        return Timestamp.valueOf(LocalDateTime.now().minusSeconds(pollingIntervalSeconds));
    }

    private String sqlQueryForTopic(Job.JobStatus topic) {
        switch (topic) {
            case IN_PROGRESS:
                return "" +
                        "select * from VID_JOB" +
                        " where" +
                        // select only non-deleted in-progress jobs
                        "    JOB_STATUS = 'IN_PROGRESS'" +
                        "    and TAKEN_BY is null" +
                        "    and DELETED_AT is null" +
                        // give some breath, don't select jos that were recently reached
                        "    and MODIFIED_DATE <= '" + nowMinusInterval() +
                        // take the oldest handled one
                        "' order by MODIFIED_DATE ASC" +
                        // select only one result
                        " limit 1";

            case PENDING:
                return "" +
                        // select only pending jobs
                        "select vid_job.* from VID_JOB " +
                        // select users have in_progress jobs
                        "left join \n" +
                        " (select user_Id, 1 as has_any_in_progress_job from VID_JOB  where JOB_STATUS = 'IN_PROGRESS' or TAKEN_BY IS NOT NULL \n" +
                        "group by user_id)  users_have_any_in_progress_job_tbl\n" +
                        "on vid_job.user_id = users_have_any_in_progress_job_tbl.user_id " +
                        "where JOB_STATUS = 'PENDING' and TAKEN_BY is null" +
                        // job is not deleted
                        "      AND DELETED_AT is null and (\n" +
                        // limit in-progress to some amount
                        "select sum(CASE WHEN JOB_STATUS='IN_PROGRESS' or (JOB_STATUS='PENDING' and TAKEN_BY IS NOT NULL) THEN 1 ELSE 0 END) as in_progress\n" +
                        "from VID_JOB ) <" + maxOpenedInstantiationRequestsToMso + " \n " +
                        // don't take jobs from templates that already in-progress/failed
                        "and TEMPLATE_Id not in \n" +
                        "(select TEMPLATE_Id from vid_job where" +
                        "   (JOB_STATUS='FAILED' and DELETED_AT is null)" + // failed but not deleted
                        "   or JOB_STATUS='IN_PROGRESS'" +
                        "   or TAKEN_BY IS NOT NULL)" + " \n " +
                        // prefer older jobs, but the earlier in each bulk
                        "order by has_any_in_progress_job, CREATED_DATE, INDEX_IN_BULK " +
                        // select only one result
                        "limit 1";
            default:
                throw new GenericUncheckedException("Unsupported topic to pull from: " + topic);
        }
    }


    private byte[] getUuidAsByteArray(UUID owner) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(owner.getMostSignificantBits());
        bb.putLong(owner.getLeastSignificantBits());
        return bb.array();
    }

    @Override
    public void pushBack(Job job) {
        final JobDaoImpl remoteDaoJob = (JobDaoImpl) dataAccessService.getDomainObject(JobDaoImpl.class, job.getUuid(), null);

        if (remoteDaoJob == null) {
            throw new IllegalStateException("Can push back only pulled jobs. Add new jobs using add()");
        }

        if (remoteDaoJob.getTakenBy() == null) {
            throw new IllegalStateException("Can push back only pulled jobs. This one already pushed back.");
        }

        final JobDaoImpl jobDao = castToJobDaoImpl(job);

        jobDao.setTakenBy(null);

        Integer age = jobDao.getAge();
        jobDao.setAge(age + 1);

        logger.debug(EELFLoggerDelegate.debugLogger, "{}/{}", jobDao.getStatus(), jobDao.getType());

        dataAccessService.saveDomainObject(jobDao, DaoUtils.getPropsMap());
    }

    private JobDaoImpl castToJobDaoImpl(Job job) {
        if (!(job instanceof JobDaoImpl)) {
            throw new UnsupportedOperationException("Can't add " + job.getClass() + " to " + this.getClass());
        }
        return (JobDaoImpl) job;
    }

    @Override
    public Collection<Job> peek() {
        return dataAccessService.getList(JobDaoImpl.class, null);
    }

    @Override
    public Job peek(UUID jobId) {
        return (JobDaoImpl) dataAccessService.getDomainObject(JobDaoImpl.class, jobId, null);
    }

    @Override
    public void delete(UUID jobId) {
        int updatedEntities;
        Date now = new Date();

        String hqlUpdate = "update JobDaoImpl job set job.deletedAt = :now where " +
                " job.id = :id" +
                " and job.status in(:pending, :stopped)" +
                " and takenBy is null";

        updatedEntities = DaoUtils.tryWithSessionAndTransaction(sessionFactory, session ->
                session.createQuery(hqlUpdate)
                        .setTimestamp("now", now)
                        .setText("id", jobId.toString())
                        .setText("pending", Job.JobStatus.PENDING.toString())
                        .setText("stopped", Job.JobStatus.STOPPED.toString())
                        .executeUpdate());

        if (updatedEntities == 0) {
            final JobDaoImpl remoteDaoJob = (JobDaoImpl) dataAccessService.getDomainObject(JobDaoImpl.class, jobId, null);

            if (remoteDaoJob == null || remoteDaoJob.getUuid() == null) {
                logger.debug(EELFLoggerDelegate.debugLogger,"jobId {}: Service does not exist", jobId);
                throw new OperationNotAllowedException("Service does not exist");
            }

            if (!remoteDaoJob.getStatus().equals(Job.JobStatus.PENDING) && !remoteDaoJob.getStatus().equals(Job.JobStatus.STOPPED) || !StringUtils.isEmpty(remoteDaoJob.getTakenBy()) ) {
                logger.debug(EELFLoggerDelegate.debugLogger,"jobId {}: Service status does not allow deletion from the queue, status = {}", jobId, remoteDaoJob.getStatus() +
                ", takenBy " + remoteDaoJob.getTakenBy());
                throw new OperationNotAllowedException("Service status does not allow deletion from the queue");
            }

            throw new OperationNotAllowedException("Service deletion failed");
        }
    }
}
