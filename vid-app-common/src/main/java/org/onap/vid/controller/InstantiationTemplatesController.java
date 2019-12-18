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

package org.onap.vid.controller;


import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.onap.vid.dal.AsyncInstantiationRepository;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.InstantiationTemplatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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


    @GetMapping
    public List<ServiceInfo> getTemplatesInfo(HttpServletRequest request,
        @RequestParam(value = "serviceModelId") UUID serviceModelId) {
        return  asyncInstantiationRepository.listInstantiatedServicesByServiceModelId(serviceModelId);
    }

    @GetMapping("templateTopology/{jobId}")
    public ServiceInstantiation getTemplateTopology(HttpServletRequest request, @PathVariable(value="jobId") UUID jobId) {
        return instantiationTemplates.getJobRequestAsTemplate(jobId);
    }
}
