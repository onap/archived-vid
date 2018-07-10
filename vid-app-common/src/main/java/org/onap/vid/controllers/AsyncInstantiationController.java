package org.onap.vid.controllers;


import org.onap.vid.exceptions.OperationNotAllowedException;
import org.onap.vid.model.ExceptionResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import org.onap.vid.model.JobAuditStatus;
import org.onap.vid.model.ServiceInfo;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.MsoResponseWrapper2;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;


@RestController
@RequestMapping(AsyncInstantiationController.ASYNC_INSTANTIATION)
public class AsyncInstantiationController extends VidRestrictedBaseController {

    public static final String ASYNC_INSTANTIATION = "asyncInstantiation";

    protected final AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Autowired
    public AsyncInstantiationController(AsyncInstantiationBusinessLogic asyncInstantiationBL) {
        this.asyncInstantiationBL = asyncInstantiationBL;
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    @ResponseStatus(value=METHOD_NOT_ALLOWED)
    public ExceptionResponse illegalStateExceptionHandler(Exception e) {
        return ControllersUtils.handleException(e, LOGGER);
    }

    /**
     * Gets the new services status.
     * @param request the request
     * @return the services list
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<ServiceInfo> getServicesInfo(HttpServletRequest request) {
        return asyncInstantiationBL.getAllServicesInfo();
    }

    @RequestMapping(value = "bulk", method = RequestMethod.POST)
    public MsoResponseWrapper2<List<String>> createBulkOfServices(@RequestBody ServiceInstantiation request, HttpServletRequest httpServletRequest) {
        //Push to DB according the model

        String userId = ControllersUtils.extractUserId(httpServletRequest);
        List<UUID> uuids =  asyncInstantiationBL.pushBulkJob(request, userId);

        return new MsoResponseWrapper2(200, uuids);
    }

    @RequestMapping(value = "job/{jobId}", method = RequestMethod.DELETE)
    public void deleteServiceInfo(@PathVariable("jobId") UUID jobId) {
        asyncInstantiationBL.deleteJob(jobId);
    }

    @RequestMapping(value = "hide/{jobId}", method = RequestMethod.POST)
    public void hideServiceInfo(@PathVariable("jobId") UUID jobId) {
        asyncInstantiationBL.hideServiceInfo(jobId);
    }

    @RequestMapping(value = "auditStatus/{jobId}", method = RequestMethod.GET)
    public List<JobAuditStatus> getJobAuditStatus(HttpServletRequest request, @PathVariable(value="jobId") UUID jobId, @RequestParam(value="source") JobAuditStatus.SourceStatus source){
        return asyncInstantiationBL.getAuditStatuses(jobId, source);
    }


}
