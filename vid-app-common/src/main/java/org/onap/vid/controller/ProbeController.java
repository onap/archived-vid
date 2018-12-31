package org.onap.vid.controller;

import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.vid.aai.AaiClient;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.scheduler.SchedulerService;
import org.onap.vid.services.VidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("probe")
public class ProbeController extends RestrictedBaseController {

    private final AaiClient aaiClient;
    private final VidService vidService;
    private final MsoBusinessLogic msoBusinessLogic;
    private final SchedulerService schedulerService;

    @Autowired
    public ProbeController(AaiClient aaiClient, VidService vidService, MsoBusinessLogic msoBusinessLogic, SchedulerService schedulerService) {
        this.aaiClient = aaiClient;
        this.vidService = vidService;
        this.msoBusinessLogic = msoBusinessLogic;
        this.schedulerService = schedulerService;
    }

    @RequestMapping(method= RequestMethod.GET)
    public List<ExternalComponentStatus> getProbe() {
        List<ExternalComponentStatus> componentStatuses = new ArrayList<>();
        componentStatuses.add(aaiClient.probeAaiGetAllSubscribers());
        componentStatuses.add(schedulerService.probeGetSchedulerChangeManagements());
        return componentStatuses;
    }

}
