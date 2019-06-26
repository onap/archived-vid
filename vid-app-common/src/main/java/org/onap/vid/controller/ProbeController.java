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

import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.vid.model.probes.ExternalComponentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("probe")
public class ProbeController extends RestrictedBaseController {

    private final List<ProbeInterface> probes;

    @Autowired
    public ProbeController(List<ProbeInterface> probes) {
        this.probes = probes;
    }

    @GetMapping
    public List<ExternalComponentStatus> getProbe() {
        return probes.stream().map(ProbeInterface::probeComponent).collect(Collectors.toList());
    }
}
