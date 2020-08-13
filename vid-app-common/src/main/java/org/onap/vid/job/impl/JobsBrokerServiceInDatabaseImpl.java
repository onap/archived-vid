/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.job.impl;

import static org.onap.vid.job.Job.JobStatus.CREATING;
import static org.onap.vid.job.Job.JobStatus.FINAL_STATUS;
import static org.onap.vid.job.Job.JobStatus.IN_PROGRESS;
import static org.onap.vid.job.Job.JobStatus.PENDING_RESOURCE;
import static org.onap.vid.job.Job.JobStatus.RESOURCE_IN_PROGRESS;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.exceptions.OperationNotAllowedException;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.properties.Features;
import org.onap.vid.properties.VidProperties;
import org.onap.vid.services.VersionService;
import org.onap.vid.utils.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.togglz.core.manager.FeatureManager;

@Service
public class JobsBrokerServiceInDatabaseImpl implements JobsBrokerService {

    static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(JobsBrokerServiceInDatabaseImpl.class);

    private final DataAccessService dataAccessService;

    private final SessionFactory sessionFactory;
    private int maxOpenedInstantiationRequestsToMso;
    private int pollingIntervalSeconds;

    private final VersionService versionService;
    private final FeatureManager featureManager;
    @Autowired
    public JobsBrokerServiceInDatabaseImpl(DataAccessService dataAccessService,
                                           SessionFactory sessionFactory,
                                           @Value("0") int maxOpenedInstantiationRequestsToMso,
                                           @Value("10") int pollingIntervalSeconds,
                                           VersionService versionService,
                                           FeatureManager featureManager) {
        // tha @Value will inject conservative defaults; overridden in @PostConstruct from configuration
        this.dataAccessService = dataAccessService;
        this.sessionFactory = sessionFactory;
        this.maxOpenedInstantiationRequestsToMso = maxOpenedInstantiationRequestsToMso;
        this.pollingIntervalSeconds = pollingIntervalSeconds;
        this.versionService = versionService;
        this.featureManager = featureManager;
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
        jobDao.setBuild(versionService.retrieveBuildNumber());
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
        return nowMinusInterval(pollingIntervalSeconds);
    }

    private java.sql.Timestamp nowMinusInterval(long seconds) {
        return Timestamp.valueOf(LocalDateTime.now().minusSeconds(seconds));
    }

    private String selectQueryByJobStatus(Job.JobStatus topic){

        String intervalCondition = (topic==CREATING) ? "" : (" and MODIFIED_DATE <= '" + nowMinusInterval()+"'");
        return "" +
                "select * from VID_JOB" +
                " where" +
                // select only non-deleted in-progress jobs
                filterByStatusNotTakenNotDeleted(topic) +
                // give some breath, don't select jos that were recently reached
                intervalCondition +
                // take the oldest handled one
                " order by MODIFIED_DATE ASC" +
                // select only one result
                " limit 1";
    }

    @NotNull
    private String filterByStatusNotTakenNotDeleted(Job.JobStatus topic) {
        return  "    JOB_STATUS = '" + topic + "'" +
                "    and TAKEN_BY is null" +
                "    and DELETED_AT is null "+
                "    and BUILD = '"+ versionService.retrieveBuildNumber() +"'";
    }

    private String sqlQueryForTopic(Job.JobStatus topic) {
        switch (topic) {
            case IN_PROGRESS:
            case RESOURCE_IN_PROGRESS:
            case CREATING:
                return selectQueryByJobStatus(topic);
            case PENDING:
                return selectQueryForPendingJob();
            case PENDING_RESOURCE:
                return selectQueryForPendingResource();
            default:
                throw new GenericUncheckedException("Unsupported topic to pull from: " + topic);
        }
    }

    @NotNull
    private String selectQueryForPendingJob() {
        return "" +
                // select only pending jobs
                "select vid_job.* from VID_JOB " +
                // select users have in_progress jobs
                "left join \n" +
                " (select user_Id, 1 as has_any_in_progress_job from VID_JOB  where JOB_STATUS = 'IN_PROGRESS' or TAKEN_BY IS NOT NULL \n" +
                "group by user_id)  users_have_any_in_progress_job_tbl\n" +
                "on vid_job.user_id = users_have_any_in_progress_job_tbl.user_id " +
                "where "+filterByStatusNotTakenNotDeleted(Job.JobStatus.PENDING)+" and (\n" +
                // limit in-progress to some amount
                "select sum(CASE WHEN JOB_STATUS='IN_PROGRESS' or (JOB_STATUS='PENDING' and TAKEN_BY IS NOT NULL) THEN 1 ELSE 0 END) as in_progress\n" +
                "from VID_JOB ) <" + maxOpenedInstantiationRequestsToMso + " \n " +
                // don't take jobs from templates that already in-progress/failed
                "and TEMPLATE_Id not in \n" +
                "(select TEMPLATE_Id from vid_job where" +
                "   TEMPLATE_Id IS NOT NULL and("+
                "   (JOB_STATUS IN('FAILED','FAILED_AND_PAUSED') and DELETED_AT is null)" + // failed but not deleted
                "   or JOB_STATUS='IN_PROGRESS'" +
                "   or TAKEN_BY IS NOT NULL))" + " \n " +
                // prefer older jobs, but the earlier in each bulk
                "order by has_any_in_progress_job, CREATED_DATE, INDEX_IN_BULK " +
                // select only one result
                "limit 1";
    }

    @NotNull
    private String selectQueryForPendingResource() {
        return "select * from vid_job as JOB left join \n" +
                //count jobs
                "(select template_id,count(*) as in_progress_count from vid_job \n" +
                String.format("where (\n"+
                "    (\n"+
                //with job_status IN_PROGRESS or RESOURCE_IN_PROGRESS
                "        (job_status in ('%s','%s') and DELETED_AT is NULL) \n",IN_PROGRESS, RESOURCE_IN_PROGRESS)+
                //or that with job_status PENDING_RESOURCE that are taken
                String.format("        or (JOB_STATUS='%s' and TAKEN_BY IS NOT NULL)\n    )\n", PENDING_RESOURCE) +
                //with template ID and are not deleted
                "    and TEMPLATE_ID IS NOT NULL and DELETED_AT is NULL\n)\n" +
                //join them to vid_job by template_id
                "group by template_id)\n"+
                "as COUNTER on COUNTER.template_id=JOB.template_id \n" +

                "where (\n"+
                //select jobs with job_status PENDING_RESOURCE that are nit taken and not deleted
                filterByStatusNotTakenNotDeleted(PENDING_RESOURCE) + "\n" +
                //that have no count in the counter (no other in progress job with same templateId)
                "    and in_progress_count is NULL \n" +
                //and that have valid templateId
                "    and JOB.template_id is not NULL \n"+

                filterFailedStatusForPendingResource()

                + ")" +
                //INDEX_IN_BULK is for order them inside same templateId,
                //template_id - for order between different templateId (just to be deterministic)
                "order by INDEX_IN_BULK,JOB.template_id \n" +
                "limit 1;";
    }
    private String filterFailedStatusForPendingResource() {
        String sql = "and JOB.template_id not in \n" +
                "(select TEMPLATE_Id from vid_job where" +
                "   TEMPLATE_Id IS NOT NULL and (JOB_STATUS IN('FAILED','FAILED_AND_PAUSED') "
                + " AND JOB_TYPE NOT IN('NetworkInstantiation','InstanceGroup','InstanceGroupMember') and DELETED_AT is null)" + // failed but not deleted
                "   or TAKEN_BY IS NOT NULL)";
        return featureManager.isActive(Features.FLAG_2008_PAUSE_VFMODULE_INSTANTIATION_FAILURE) ?
                sql : "";
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

        logger.debug(EELFLoggerDelegate.debugLogger, "pushing back jobDao {} of {}: {}/{}",
            StringUtils.substring(String.valueOf(jobDao.getUuid()), 0, 8),
            StringUtils.substring(String.valueOf(jobDao.getTemplateId()), 0, 8),
            jobDao.getStatus(), jobDao.getType());

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

            if ((remoteDaoJob.getStatus() != Job.JobStatus.PENDING) && (remoteDaoJob.getStatus() != Job.JobStatus.STOPPED) || !StringUtils.isEmpty(remoteDaoJob.getTakenBy()) ) {
                logger.debug(EELFLoggerDelegate.debugLogger,"jobId {}: Service status does not allow deletion from the queue, status = {}", jobId, remoteDaoJob.getStatus() +
                ", takenBy " + remoteDaoJob.getTakenBy());
                throw new OperationNotAllowedException("Service status does not allow deletion from the queue");
            }

            throw new OperationNotAllowedException("Service deletion failed");
        }
    }

    @Override
    public boolean mute(UUID jobId) {
        if (jobId == null) {
            return false;
        }

        final String prefix = "DUMP";
        int updatedEntities;

        // Changing the topic (i.e. `job.status`) makes the job non-fetchable.
        String hqlUpdate = "" +
                "update JobDaoImpl job set" +
                "   job.status = concat('" + prefix + "_', job.status)," +
                //  empty `takenBy`, because some logics treat taken as in-progress
                "   takenBy = null" +
                " where " +
                "   job.id = :id" +
                //  if prefix already on the topic -- no need to do it twice.
                "   and job.status NOT LIKE '" + prefix + "\\_%'";

        updatedEntities = DaoUtils.tryWithSessionAndTransaction(sessionFactory, session ->
                session.createQuery(hqlUpdate)
                        .setText("id", jobId.toString())
                        .executeUpdate());

        return updatedEntities != 0;
    }

    private static String sqlListOfFinalStatus =
            String.format("(%s)",
                FINAL_STATUS.stream().
                map(x->String.format("'%s'",x)).
                collect(Collectors.joining(","))
            );

    @Override
    public void deleteOldFinalJobs(long secondsAgo) {
        String select = String.format(" MODIFIED_DATE <= '%s' and JOB_STATUS in %s", nowMinusInterval(secondsAgo), sqlListOfFinalStatus);
        dataAccessService.deleteDomainObjects(JobDaoImpl.class, select, null);
    }
}
