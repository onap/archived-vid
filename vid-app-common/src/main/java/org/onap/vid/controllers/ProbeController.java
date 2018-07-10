package org.onap.vid.controllers;

import org.onap.vid.aai.AaiClient;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("probe")
public class ProbeController extends RestrictedBaseController {
    @Autowired
    private AaiClient aaiClient;

    @RequestMapping(method= RequestMethod.GET)
    public List<ExternalComponentStatus> getProbe(){
        List<ExternalComponentStatus> componentStatuses = new ArrayList<>();
        componentStatuses.add(aaiClient.probeAaiGetAllSubscribers());
        return componentStatuses;
    }

}
