package org.onap.vid.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.model.ExceptionResponse;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoResponseWrapper2;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.model.OperationalEnvironmentActivateInfo;
import org.onap.vid.mso.model.OperationalEnvironmentDeactivateInfo;
import org.onap.vid.mso.rest.MsoRestClientNew;
import org.onap.vid.mso.rest.OperationalEnvironment.OperationEnvironmentRequestDetails;
import org.onap.vid.mso.rest.RequestDetails;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.onap.vid.utils.Logging.getMethodCallerName;
import static org.onap.vid.utils.Logging.getMethodName;

@RestController
@RequestMapping("operationalEnvironment")
public class OperationalEnvironmentController extends VidRestrictedBaseController {

    private final RestMsoImplementation restMso;
    private final MsoBusinessLogic msoBusinessLogic;

    private static final Pattern RECOVERY_ACTION_MESSAGE_PATTERN = Pattern.compile("String value \'(.*)\': value not");


    @Autowired
    public OperationalEnvironmentController(MsoBusinessLogic msoBusinessLogic, MsoRestClientNew msoClientInterface) {
        this.restMso = msoClientInterface;
        this.msoBusinessLogic = msoBusinessLogic;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public MsoResponseWrapper2 createOperationalEnvironment(HttpServletRequest request, @RequestBody OperationalEnvironmentCreateBody operationalEnvironment) {
        debugStart(operationalEnvironment);
        String userId = ControllersUtils.extractUserId(request);
        RequestDetailsWrapper<OperationEnvironmentRequestDetails> requestDetailsWrapper = msoBusinessLogic.convertParametersToRequestDetails(operationalEnvironment, userId);
        String path = msoBusinessLogic.getOperationalEnvironmentCreationPath();
        RestObject<RequestReferencesContainer> msoResponse = restMso.PostForObject(requestDetailsWrapper, "",
                path, RequestReferencesContainer.class);
        debugEnd(msoResponse);
        return new MsoResponseWrapper2<>(msoResponse);
    }

    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    public MsoResponseWrapper2 activate(HttpServletRequest request,
                                        @RequestParam("operationalEnvironment") String operationalEnvironmentId,
                                        @RequestBody OperationalEnvironmentActivateBody activateRequest) throws MissingServletRequestParameterException {

        verifyIsNotEmpty(operationalEnvironmentId, "operationalEnvironment");

        //manifest is null in case of wrong manifest structure (deserialization failure of the manifest)
        if (activateRequest.getManifest()==null || activateRequest.getManifest().getServiceModelList()==null) {
            throw new BadManifestException("Manifest structure is wrong");
        }

        String userId = ControllersUtils.extractUserId(request);

        OperationalEnvironmentActivateInfo activateInfo = new OperationalEnvironmentActivateInfo(activateRequest, userId, operationalEnvironmentId);
        debugStart(activateInfo);

        String path = msoBusinessLogic.getOperationalEnvironmentActivationPath(activateInfo);
        RequestDetailsWrapper<RequestDetails> requestDetailsWrapper = msoBusinessLogic.createOperationalEnvironmentActivationRequestDetails(activateInfo);

        RestObject<RequestReferencesContainer> msoResponse = restMso.PostForObject(requestDetailsWrapper, "",
                path, RequestReferencesContainer.class);

        debugEnd(msoResponse);
        return new MsoResponseWrapper2<>(msoResponse);
    }

    @RequestMapping(value = "/deactivate", method = RequestMethod.POST)
    public MsoResponseWrapper2 deactivate(HttpServletRequest request,
                                          @RequestParam("operationalEnvironment") String operationalEnvironmentId,
                                          @RequestBody Map deactivationRequest) throws MissingServletRequestParameterException {

        verifyIsNotEmpty(operationalEnvironmentId, "operationalEnvironment");

        String userId = ControllersUtils.extractUserId(request);

        OperationalEnvironmentDeactivateInfo deactivateInfo = new OperationalEnvironmentDeactivateInfo(userId, operationalEnvironmentId);
        debugStart(deactivateInfo);

        String path = msoBusinessLogic.getOperationalEnvironmentDeactivationPath(deactivateInfo);
        RequestDetailsWrapper<RequestDetails> requestDetailsWrapper =  msoBusinessLogic.createOperationalEnvironmentDeactivationRequestDetails(deactivateInfo);

        RestObject<RequestReferencesContainer> msoResponse = restMso.PostForObject(requestDetailsWrapper, "",
                path, RequestReferencesContainer.class);

        debugEnd(msoResponse);
        return new MsoResponseWrapper2<>(msoResponse);
    }

    @RequestMapping(value = "/requestStatus", method = RequestMethod.GET)
    public MsoResponseWrapper2 status(HttpServletRequest request, @RequestParam("requestId") String requestId) throws MissingServletRequestParameterException {

        debugStart(requestId);

        verifyIsNotEmpty(requestId, "requestId");
        String path = msoBusinessLogic.getCloudResourcesRequestsStatusPath(requestId);

        final RestObject<HashMap> msoResponse = restMso.GetForObject("", path, HashMap.class);

        LOGGER.debug(EELFLoggerDelegate.debugLogger, "end {}() => {}", getMethodName(), msoResponse);
        return new MsoResponseWrapper2<>(msoResponse);
    }

    @ExceptionHandler({
            org.springframework.web.bind.MissingServletRequestParameterException.class,
            BadManifestException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResponse clientDerivedExceptionAsBadRequest(Exception e) {
        // same handler, different HTTP Code
        return exceptionHandler(e);
    }

    @ExceptionHandler({
            org.springframework.http.converter.HttpMessageNotReadableException.class,
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResponse handlingHttpMessageNotReadableException(Exception e) {
        //in case of wrong value in manifest for RecoveryAction the message contains the class name.
        //The wrong value is in also part of this messages
        //within the pattern of: String value '<WRONG_VALUE>': value not
        //so we use regex to find the wrong value
        if (e.getMessage().contains(OperationalEnvironmentRecoveryAction.class.getName())) {
            LOGGER.error(EELFLoggerDelegate.errorLogger, "{}: {}", getMethodName(), ExceptionUtils.getMessage(e), e);
            String message = "Wrong value for RecoveryAction in manifest. Allowed options are: "+OperationalEnvironmentRecoveryAction.options;

            Matcher matcher = RECOVERY_ACTION_MESSAGE_PATTERN.matcher(e.getMessage());
            if (matcher.find()) {
                String wrongValue = matcher.group(1);
                message = message+". Wrong value is: "+wrongValue;
            }
            return new ExceptionResponse(new BadManifestException(message));
        }
        return exceptionHandler(e);
    }


    public enum OperationalEnvironmentRecoveryAction {
        abort,
        retry,
        skip;

        public static final String options = Stream.of(OperationalEnvironmentRecoveryAction.values()).map(OperationalEnvironmentRecoveryAction::name).collect(Collectors.joining(", "));
    }

    public static class ActivateServiceModel {
        private String serviceModelVersionId;
        private OperationalEnvironmentRecoveryAction recoveryAction;

        public ActivateServiceModel() {
        }

        public ActivateServiceModel(String serviceModelVersionId, OperationalEnvironmentRecoveryAction recoveryAction) {
            this.serviceModelVersionId = serviceModelVersionId;
            this.recoveryAction = recoveryAction;
        }

        public String getServiceModelVersionId() {
            return serviceModelVersionId;
        }

        public void setServiceModelVersionId(String serviceModelVersionId) {
            this.serviceModelVersionId = serviceModelVersionId;
        }

        public OperationalEnvironmentRecoveryAction getRecoveryAction() {
            return recoveryAction;
        }

        public void setRecoveryAction(OperationalEnvironmentRecoveryAction recoveryAction) {
            this.recoveryAction = recoveryAction;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OperationalEnvironmentManifest {


        private List<ActivateServiceModel> serviceModelList;

        public OperationalEnvironmentManifest() {
        }

        public OperationalEnvironmentManifest(List<ActivateServiceModel> serviceModelList) {
            this.serviceModelList = serviceModelList;
        }

        public List<ActivateServiceModel> getServiceModelList() {
            return serviceModelList;
        }

        public void setServiceModelList(List<ActivateServiceModel> serviceModelList) {
            this.serviceModelList = serviceModelList;
        }
    }

    public static class OperationalEnvironmentActivateBody {
        private final String relatedInstanceId;
        private final String relatedInstanceName;
        private final String workloadContext;
        private final OperationalEnvironmentManifest manifest;

        public OperationalEnvironmentActivateBody(@JsonProperty(value = "relatedInstanceId", required = true) String relatedInstanceId,
                                                  @JsonProperty(value = "relatedInstanceName", required = true) String relatedInstanceName,
                                                  @JsonProperty(value = "workloadContext", required = true) String workloadContext,
                                                  @JsonProperty(value = "manifest", required = true) OperationalEnvironmentManifest manifest) {
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

        public OperationalEnvironmentManifest getManifest() {
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

    public static class BadManifestException extends RuntimeException {
        public BadManifestException(String message) {
            super(message);
        }
    }

}
