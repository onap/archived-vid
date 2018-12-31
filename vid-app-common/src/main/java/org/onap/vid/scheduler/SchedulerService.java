package org.onap.vid.scheduler;

import org.onap.vid.model.probes.ExternalComponentStatus;

public interface SchedulerService {
    ExternalComponentStatus probeGetSchedulerChangeManagements();
}
