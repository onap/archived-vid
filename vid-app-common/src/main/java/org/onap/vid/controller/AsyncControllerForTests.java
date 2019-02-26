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

import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.ExceptionResponse;
import org.onap.vid.model.JobModel;
import org.onap.vid.services.BulkInstantiationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.WebApplicationException;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("asyncForTests")
public class AsyncControllerForTests extends VidRestrictedBaseController {

    private BulkInstantiationService bulkInstantiationService;

    @Autowired
    public AsyncControllerForTests(BulkInstantiationService bulkInstantiationService) {
        this.bulkInstantiationService = bulkInstantiationService;
    }

    @RequestMapping(value = "/job/{uuid}", method = RequestMethod.GET)
    public JobModel getJob(@PathVariable UUID uuid) {
        return bulkInstantiationService.getJob(uuid);
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public void throwError() {
        throw new GenericUncheckedException("dummy error");
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(value=BAD_REQUEST)
    private ExceptionResponse exceptionHandlerBadRequest(Exception e) {
        return ControllersUtils.handleException(e, LOGGER);
    }

    @ExceptionHandler(WebApplicationException.class)
    private ResponseEntity webApplicationExceptionHandler(WebApplicationException e) {
        return ControllersUtils.handleWebApplicationException(e, LOGGER);
    }

}
