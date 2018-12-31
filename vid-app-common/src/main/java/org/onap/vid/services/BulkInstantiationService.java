package org.onap.vid.services;

import org.onap.vid.model.JobModel;

import java.util.UUID;

public interface BulkInstantiationService {

    JobModel getJob(UUID uuid);

}
