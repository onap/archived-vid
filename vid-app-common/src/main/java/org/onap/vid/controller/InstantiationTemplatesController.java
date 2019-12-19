package org.onap.vid.controller;


import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.onap.vid.dal.AsyncInstantiationRepository;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.InstantiationTemplatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(InstantiationTemplatesController.INSTANTIATION_TEMPLATES)
public class InstantiationTemplatesController extends VidRestrictedBaseController {

    public static final String INSTANTIATION_TEMPLATES = "instantiationTemplates";

    protected final AsyncInstantiationBusinessLogic asyncInstantiationBL;
    protected final InstantiationTemplatesService instantiationTemplates;
    protected final AsyncInstantiationRepository asyncInstantiationRepository;


    @Autowired
    public InstantiationTemplatesController(AsyncInstantiationBusinessLogic asyncInstantiationBL,
        InstantiationTemplatesService instantiationTemplates,
        AsyncInstantiationRepository asyncInstantiationRepository) {
        this.asyncInstantiationBL = asyncInstantiationBL;
        this.instantiationTemplates = instantiationTemplates;
        this.asyncInstantiationRepository = asyncInstantiationRepository;
    }

    @GetMapping("templateTopology/{jobId}")
    public ServiceInstantiation getTemplateTopology(HttpServletRequest request, @PathVariable(value="jobId") UUID jobId) {
        return instantiationTemplates.getJobRequestAsTemplate(jobId);
    }
}
