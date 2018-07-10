package org.onap.vid.job;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface JobsBrokerService {

    UUID add(Job job);

    Optional<Job> pull(Job.JobStatus topic, String ownerId);

    void pushBack(Job job);

    Collection<Job> peek();

    Job peek(UUID jobId);

    void delete(UUID jobId);

}
