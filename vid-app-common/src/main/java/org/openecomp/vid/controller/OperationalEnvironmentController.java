package org.openecomp.vid.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openecomp.portalsdk.core.controller.RestrictedBaseController;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.vid.model.ExceptionResponse;
import org.openecomp.vid.mso.MsoResponseWrapper;
import org.openecomp.vid.mso.rest.OperationalEnvironment.*;

import org.openecomp.vid.model.RequestReferencesContainer;
import org.openecomp.vid.mso.*;
import org.openecomp.vid.mso.model.OperationalEnvironmentActivateInfo;
import org.openecomp.vid.mso.model.OperationalEnvironmentDeactivateInfo;
import org.openecomp.vid.mso.rest.MsoRestClientNew;
import org.openecomp.vid.mso.rest.RequestDetails;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.att.eelf.configuration.Configuration.MDC_KEY_REQUEST_ID;
import static org.openecomp.vid.utils.Logging.getMethodCallerName;
import static org.openecomp.vid.utils.Logging.getMethodName;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("operationalEnvironment")
public class OperationalEnvironmentController extends RestrictedBaseController {

    private final static EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(OperationalEnvironmentController.class);
    private final static String RESOURCE_TYPE_OPERATIONAL_ENVIRONMENT = "operationalEnvironment";
    private final static String SOURCE_OPERATIONAL_ENVIRONMENT = "VID";
    private final RestMsoImplementation restMso;
    private final MsoBusinessLogic msoBusinessLogic;


    @Autowired
    public OperationalEnvironmentController(MsoBusinessLogic msoBusinessLogic, MsoRestClientNew msoClientInterface) {
        this.restMso = msoClientInterface;
        this.msoBusinessLogic = msoBusinessLogic;
    }

    @RequestMapping(value = "/deactivate", method = RequestMethod.POST)
    public MsoResponseWrapper deactivate(HttpServletRequest request,
                                         @RequestParam("operationalEnvironment") String operationalEnvironmentId,
                                         @RequestBody Map deactivationRequest) throws Exception {

        if (StringUtils.isEmpty(operationalEnvironmentId)) {
            throw new MissingServletRequestParameterException("operationalEnvironment", "String");
        }

        String userId = ControllersUtils.extractUserId(request);

        OperationalEnvironmentDeactivateInfo deactivateInfo = new OperationalEnvironmentDeactivateInfo(userId, operationalEnvironmentId);
        debugStart(deactivateInfo);

        String path = msoBusinessLogic.getOperationalEnvironmentDeactivationPath(deactivateInfo);
        RequestDetails requestDetails = msoBusinessLogic.createOperationalEnvironmentDeactivationRequestDetails(deactivateInfo);

        RestObject<RequestReferencesContainer> msoResponse = restMso.PostForObject(requestDetails, "",
                path, RequestReferencesContainer.class);

        debugEnd(msoResponse);
        return MsoUtil.wrapResponse(msoResponse);
    }

        /*

        [in ]  POST  /cloudResources/v1/operationalEnvironments/bc305d54-75b4-431b-adb2-eb6b9e546014/activate HTTPS/1.1
        Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
        content-type: application/json
        content-length: 1024
        accept: application/json

        [   ]  {
        [   ]    "requestDetails": {
        [   ]      "requestInfo": {
        [   ]        "resourceType": "operationalEnvironment",
        [not needed  "instanceName": "myVnfOpEnv",
        [const       "source": "VID",
        [yield       "requestorId": "az2017"
        [   ]      },
        [in/get    "relatedInstanceList": [
        [   ]        {
        [   ]          "relatedInstance": {
        [   ]            "resourceType": "operationalEnvironment",
        [in/get          "instanceId": "{ID of managing ECOMP Operational Environment}",
        [in/get          "instanceName": "{Name of managing ECOMP Operational Environment}"
        [   ]          }
        [   ]        }
        [   ]      ],
        [   ]      "requestParameters": {
        [const       "operationalEnvironmentType": "VNF",
        [yield       "workloadContext": "VNF_E2E-IST",   ///
                  Concatenate(OperationalEnvironmentType, "_", WorkloadType)
        [in+api   WorkloadType is a static set of values controlled outside MSO (e.g. FFA, PSL, etc)
        [   ]        "manifest": {
        [in ]          "serviceModelList": [
        [   ]            {
        [   ]              "serviceModelVersionId": "uuid1",
        [   ]              "recoveryAction": "abort"
        [   ]            },
        [   ]            {
        [   ]              "serviceModelVersionId": "uuid2",
        [   ]              "recoveryAction": "retry"
        [   ]            }
        [   ]          ]
        [   ]        }
        [   ]      }
        [   ]    }
        [   ]  }


         */


    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    public MsoResponseWrapper activate(HttpServletRequest request,
                                       @RequestParam("operationalEnvironment") String operationalEnvironmentId,
                                       @RequestBody OperationalEnvironmentActivateBody activateRequest) throws Exception {

        if (StringUtils.isEmpty(operationalEnvironmentId)) {
            throw new MissingServletRequestParameterException("operationalEnvironment", "String");
        }

        String userId = ControllersUtils.extractUserId(request);

        OperationalEnvironmentActivateInfo activateInfo = new OperationalEnvironmentActivateInfo(activateRequest, userId, operationalEnvironmentId);
        debugStart(activateInfo);

        String path = msoBusinessLogic.getOperationalEnvironmentActivationPath(activateInfo);
        RequestDetails requestDetails = msoBusinessLogic.createOperationalEnvironmentActivationRequestDetails(activateInfo);

        RestObject<RequestReferencesContainer> msoResponse = restMso.PostForObject(requestDetails, "",
                path, RequestReferencesContainer.class);

        debugEnd(msoResponse);
        return MsoUtil.wrapResponse(msoResponse);
    }

    @RequestMapping(value = "/requestStatus", method = RequestMethod.GET)
    public MsoResponseWrapper status(HttpServletRequest request, @RequestParam("requestId") String requestId) throws Exception {

        LOGGER.debug(EELFLoggerDelegate.debugLogger, "start {}({})", getMethodName(), requestId);

        if (StringUtils.isEmpty(requestId)) {
            throw new MissingServletRequestParameterException("requestId", "String");
        }
        String path = msoBusinessLogic.getCloudResourcesRequestsStatusPath(requestId);

        final RestObject<String> msoResponse = restMso.GetForObject("", path, String.class);

        LOGGER.debug(EELFLoggerDelegate.debugLogger, "end {}() => {}", getMethodName(), msoResponse);
        return MsoUtil.wrapResponse(msoResponse);
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public MsoResponseWrapper createOperationalEnvironment(HttpServletRequest request, @RequestBody OperationalEnvironmnetPostParameters operationalEnvironmnet) throws Exception {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "start {}({})", getMethodName(), operationalEnvironmnet);
        String userId = ControllersUtils.extractUserId(request);
        org.openecomp.vid.mso.rest.OperationalEnvironment.RequestDetails requestDetails = convertParametersToRequestDetails(operationalEnvironmnet,userId);
        String path = msoBusinessLogic.getOperationalEnvironmentCreationPath();
        RestObject<RequestReferencesContainer> msoResponse = restMso.PostForObject(requestDetails, "",
                path, RequestReferencesContainer.class);
        debugEnd(msoResponse);
        return MsoUtil.wrapResponse(msoResponse);
    }

    private org.openecomp.vid.mso.rest.OperationalEnvironment.RequestDetails convertParametersToRequestDetails(OperationalEnvironmnetPostParameters operationalEnvironmnet, String userId){
        RequestInfo requestInfo =  new RequestInfo(RESOURCE_TYPE_OPERATIONAL_ENVIRONMENT,operationalEnvironmnet.getInstanceName(),SOURCE_OPERATIONAL_ENVIRONMENT,userId);
        RelatedInstance relatedInstance = new RelatedInstance(RESOURCE_TYPE_OPERATIONAL_ENVIRONMENT,operationalEnvironmnet.getEcompInstanceId(),operationalEnvironmnet.getEcompInstanceName());
        RelatedInstanceList relatedInstanceList = new RelatedInstanceList();
        relatedInstanceList.addItem(relatedInstance);
        RequestParameters requestParameters= new RequestParameters(operationalEnvironmnet.getOperationalEnvironmentType(),operationalEnvironmnet.getTenantContext(),operationalEnvironmnet.getWorkloadContext());
        org.openecomp.vid.mso.rest.OperationalEnvironment.RequestDetails requestDetails = new org.openecomp.vid.mso.rest.OperationalEnvironment.RequestDetails(requestInfo,relatedInstanceList,requestParameters);
        return requestDetails;
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

    private void debugEnd(RestObject<RequestReferencesContainer> msoResponse) {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "end {}() => {}", getMethodCallerName(), msoResponse);
    }

    private void debugStart(Object requestInfo) {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "start {}({})", getMethodCallerName(), requestInfo);
    }

}
