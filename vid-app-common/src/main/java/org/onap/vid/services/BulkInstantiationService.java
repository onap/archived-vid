package org.onap.vid.services;

import org.onap.vid.model.JobBulk;
import org.onap.vid.model.JobModel;

import java.util.Map;
import java.util.UUID;

public interface BulkInstantiationService {

    JobBulk saveBulk(Map<String, Object> bulkRequest);

    JobModel getJob(UUID uuid);

}
