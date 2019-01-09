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
