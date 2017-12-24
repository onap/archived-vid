package org.openecomp.vid.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openecomp.portalsdk.core.controller.RestrictedBaseController;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.vid.model.ExceptionResponse;
import org.openecomp.vid.model.RequestReferencesContainer;
import org.openecomp.vid.mso.MsoBusinessLogic;
import org.openecomp.vid.mso.MsoResponseWrapper2;
import org.openecomp.vid.mso.RestMsoImplementation;
import org.openecomp.vid.mso.RestObject;
import org.openecomp.vid.mso.model.OperationalEnvironmentActivateInfo;
import org.openecomp.vid.mso.model.OperationalEnvironmentDeactivateInfo;
import org.openecomp.vid.mso.rest.MsoRestClientNew;
import org.openecomp.vid.mso.rest.OperationalEnvironment.OperationEnvironmentRequestDetails;
import org.openecomp.vid.mso.rest.RequestDetails;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.att.eelf.configuration.Configuration.MDC_KEY_REQUEST_ID;
import static org.openecomp.vid.utils.Logging.getMethodCallerName;
import static org.openecomp.vid.utils.Logging.getMethodName;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("operationalEnvironment")
public class OperationalEnvironmentController extends RestrictedBaseController {

    private final static EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(OperationalEnvironmentController.class);
    private final RestMsoImplementation restMso;
    private final MsoBusinessLogic msoBusinessLogic;


    @Autowired
    public OperationalEnvironmentController(MsoBusinessLogic msoBusinessLogic, MsoRestClientNew msoClientInterface) {
        this.restMso = msoClientInterface;
        this.msoBusinessLogic = msoBusinessLogic;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public MsoResponseWrapper2 createOperationalEnvironment(HttpServletRequest request, @RequestBody OperationalEnvironmentCreateBody operationalEnvironment) throws Exception {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "start {}({})", getMethodName(), operationalEnvironment);
        String userId = ControllersUtils.extractUserId(request);
        OperationEnvironmentRequestDetails requestDetails = msoBusinessLogic.convertParametersToRequestDetails(operationalEnvironment, userId);
        String path = msoBusinessLogic.getOperationalEnvironmentCreationPath();
        RestObject<RequestReferencesContainer> msoResponse = restMso.PostForObject(requestDetails, "",
                path, RequestReferencesContainer.class);
        debugEnd(msoResponse);
        return new MsoResponseWrapper2<>(msoResponse);
    }

    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    public MsoResponseWrapper2 activate(HttpServletRequest request,
                                        @RequestParam("operationalEnvironment") String operationalEnvironmentId,
                                        @RequestBody OperationalEnvironmentActivateBody activateRequest) throws Exception {

        verifyIsNotEmpty(operationalEnvironmentId, "operationalEnvironment");

        String userId = ControllersUtils.extractUserId(request);

        OperationalEnvironmentActivateInfo activateInfo = new OperationalEnvironmentActivateInfo(activateRequest, userId, operationalEnvironmentId);
        debugStart(activateInfo);

        String path = msoBusinessLogic.getOperationalEnvironmentActivationPath(activateInfo);
        RequestDetails requestDetails = msoBusinessLogic.createOperationalEnvironmentActivationRequestDetails(activateInfo);

        RestObject<RequestReferencesContainer> msoResponse = restMso.PostForObject(requestDetails, "",
                path, RequestReferencesContainer.class);

        debugEnd(msoResponse);
        return new MsoResponseWrapper2<>(msoResponse);
    }

    @RequestMapping(value = "/deactivate", method = RequestMethod.POST)
    public MsoResponseWrapper2 deactivate(HttpServletRequest request,
                                         @RequestParam("operationalEnvironment") String operationalEnvironmentId,
                                         @RequestBody Map deactivationRequest) throws Exception {

        verifyIsNotEmpty(operationalEnvironmentId, "operationalEnvironment");

        String userId = ControllersUtils.extractUserId(request);

        OperationalEnvironmentDeactivateInfo deactivateInfo = new OperationalEnvironmentDeactivateInfo(userId, operationalEnvironmentId);
        debugStart(deactivateInfo);

        String path = msoBusinessLogic.getOperationalEnvironmentDeactivationPath(deactivateInfo);
        RequestDetails requestDetails = msoBusinessLogic.createOperationalEnvironmentDeactivationRequestDetails(deactivateInfo);

        RestObject<RequestReferencesContainer> msoResponse = restMso.PostForObject(requestDetails, "",
                path, RequestReferencesContainer.class);

        debugEnd(msoResponse);
        return new MsoResponseWrapper2<>(msoResponse);
    }

    @RequestMapping(value = "/requestStatus", method = RequestMethod.GET)
    public MsoResponseWrapper2 status(HttpServletRequest request, @RequestParam("requestId") String requestId) throws Exception {

        LOGGER.debug(EELFLoggerDelegate.debugLogger, "start {}({})", getMethodName(), requestId);

        verifyIsNotEmpty(requestId, "requestId");
        String path = msoBusinessLogic.getCloudResourcesRequestsStatusPath(requestId);

        final RestObject<HashMap> msoResponse = restMso.GetForObject("", path, HashMap.class);

        LOGGER.debug(EELFLoggerDelegate.debugLogger, "end {}() => {}", getMethodName(), msoResponse);
        return new MsoResponseWrapper2<>(msoResponse);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value=INTERNAL_SERVER_ERROR)
    private ExceptionResponse exceptionHandler(Exception e) {
        LOGGER.error(EELFLoggerDelegate.errorLogger, "{}: {}", getMethodName(), ExceptionUtils.getMessage(e), e);

        ExceptionResponse exceptionResponse = new ExceptionResponse();

        exceptionResponse.setException(e.getClass().toString().replaceFirst("^.*\\.", ""));
        exceptionResponse.setMessage(e.getMessage() + " (Request id: " + MDC.get(MDC_KEY_REQUEST_ID) + ")");

        return exceptionResponse;
    }


    @ExceptionHandler({
            org.springframework.http.converter.HttpMessageNotReadableException.class,
            org.springframework.web.bind.MissingServletRequestParameterException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResponse clientDerivedExceptionAsBadRequest(Exception e) {
        // same handler, different HTTP Code
        return exceptionHandler(e);
    }

    public static class OperationalEnvironmentActivateBody {
        private final String relatedInstanceId;
        private final String relatedInstanceName;
        private final String workloadContext;
        private final Object manifest;

        public OperationalEnvironmentActivateBody(@JsonProperty(value = "relatedInstanceId", required = true) String relatedInstanceId,
                                                  @JsonProperty(value = "relatedInstanceName", required = true) String relatedInstanceName,
                                                  @JsonProperty(value = "workloadContext", required = true) String workloadContext,
                                                  @JsonProperty(value = "manifest", required = true) Object manifest) {
            this.relatedInstanceId = relatedInstanceId;
            this.relatedInstanceName = relatedInstanceName;
            this.workloadContext = workloadContext;
            this.manifest = manifest;
        }


        public String getRelatedInstanceId() {
            return relatedInstanceId;
        }

        public String getRelatedInstanceName() {
            return relatedInstanceName;
        }

        public String getWorkloadContext() {
            return workloadContext;
        }

        public Object getManifest() {
            return manifest;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("relatedInstanceId", relatedInstanceId)
                    .add("relatedInstanceName", relatedInstanceName)
                    .add("workloadContext", workloadContext)
                    .add("manifest", manifest)
                    .toString();
        }
    }

    public static class OperationalEnvironmentCreateBody {
        private final String instanceName;
        private final String ecompInstanceId;
        private final String ecompInstanceName;
        private final String operationalEnvironmentType;
        private final String tenantContext;
        private final String workloadContext;

        public OperationalEnvironmentCreateBody(@JsonProperty(value = "instanceName", required = true) String instanceName,
                                                @JsonProperty(value = "ecompInstanceId", required = true) String ecompInstanceId,
                                                @JsonProperty(value = "ecompInstanceName", required = true) String ecompInstanceName,
                                                @JsonProperty(value = "operationalEnvironmentType", required = true) String operationalEnvironmentType,
                                                @JsonProperty(value = "tenantContext", required = true) String tenantContext,
                                                @JsonProperty(value = "workloadContext", required = true) String workloadContext) {
            this.instanceName = instanceName;
            this.ecompInstanceId = ecompInstanceId;
            this.ecompInstanceName = ecompInstanceName;
            this.operationalEnvironmentType = operationalEnvironmentType;
            this.tenantContext = tenantContext;
            this.workloadContext = workloadContext;
        }

        public String getInstanceName() {
            return instanceName;
        }

        public String getEcompInstanceId() {
            return ecompInstanceId;
        }

        public String getEcompInstanceName() {
            return ecompInstanceName;
        }

        public String getOperationalEnvironmentType() {
            return operationalEnvironmentType;
        }

        public String getTenantContext() {
            return tenantContext;
        }

        public String getWorkloadContext() {
            return workloadContext;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("instanceName", instanceName)
                    .add("ecompInstanceId", ecompInstanceId)
                    .add("ecompInstanceName", ecompInstanceName)
                    .add("operationalEnvironmentType", operationalEnvironmentType)
                    .add("tenantContext", tenantContext)
                    .add("workloadContext", workloadContext)
                    .toString();
        }
    }

    private void debugEnd(RestObject<RequestReferencesContainer> msoResponse) {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "end {}() => {}", getMethodCallerName(), msoResponse);
    }

    private void debugStart(Object requestInfo) {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "start {}({})", getMethodCallerName(), requestInfo);
    }

    private void verifyIsNotEmpty(String fieldValue, String fieldName) throws MissingServletRequestParameterException {
        if (StringUtils.isEmpty(fieldValue)) {
            throw new MissingServletRequestParameterException(fieldName, "String");
        }
    }

}
