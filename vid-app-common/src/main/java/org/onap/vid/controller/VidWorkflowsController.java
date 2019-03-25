/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2019 Nokia Intellectual Property. All rights reserved.
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

import org.onap.portalsdk.core.controller.UnRestrictedBaseController;
import org.onap.vid.changeManagement.UIWorkflowsRequest;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.mso.rest.MsoRestClientNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VidWorkflowsController extends UnRestrictedBaseController {

    private MsoRestClientNew msoRestClient;

    @Autowired
    VidWorkflowsController(MsoRestClientNew msoRestClient){
        this.msoRestClient = msoRestClient;
    }


    @RequestMapping(value = "/vid/workflows", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public MsoResponseWrapper getWorkflowsFromUI(@RequestBody UIWorkflowsRequest request) {

        return msoRestClient.invokeWorkflow(request.getRequestDetails(),"/invoke/workflows");

    }

}
