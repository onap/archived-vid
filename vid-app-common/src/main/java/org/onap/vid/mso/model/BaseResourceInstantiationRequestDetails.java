package org.onap.vid.mso.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class BaseResourceInstantiationRequestDetails {

    @JsonProperty("modelInfo")
    protected ModelInfo modelInfo;

    @JsonProperty("cloudConfiguration")
    protected CloudConfiguration cloudConfiguration;

    @JsonProperty("requestInfo")
    protected RequestInfo requestInfo;

    @JsonProperty("platform")
    protected Platform platform;

    @JsonProperty("lineOfBusiness")
    protected LineOfBusiness lineOfBusiness;

    @JsonProperty("relatedInstanceList")
    protected List<RelatedInstance> relatedInstanceList;

    @JsonProperty("requestParameters")
    protected RequestParameters requestParameters;

    public BaseResourceInstantiationRequestDetails(@JsonProperty(value = "modelInfo", required = true) ModelInfo modelInfo,
                                                   @JsonProperty(value = "cloudConfiguration", required = true) CloudConfiguration cloudConfiguration,
                                                   @JsonProperty(value = "requestInfo", required = true) RequestInfo requestInfo,
                                                   @JsonProperty(value = "platform", required = true) Platform platform,
                                                   @JsonProperty(value = "lineOfBusiness", required = true) LineOfBusiness lineOfBusiness,
                                                   @JsonProperty(value = "relatedInstanceList", required = true) List<RelatedInstance> relatedInstanceList,
                                                   @JsonProperty(value = "requestParameters", required = true) RequestParameters requestParameters)
    {
        this.modelInfo = modelInfo;
        this.cloudConfiguration = cloudConfiguration;
        this.requestInfo = requestInfo;
        this.platform = platform;
        this.lineOfBusiness = lineOfBusiness;
        this.relatedInstanceList = relatedInstanceList;
        this.requestParameters = requestParameters;
    }

    public BaseResourceInstantiationRequestDetails(@JsonProperty(value = "modelInfo", required = true) ModelInfo modelInfo,
                                                   @JsonProperty(value = "cloudConfiguration", required = true) CloudConfiguration cloudConfiguration,
                                                   @JsonProperty(value = "requestInfo", required = true) RequestInfo requestInfo,
                                                   @JsonProperty(value = "relatedInstanceList", required = true) List<RelatedInstance> relatedInstanceList,
                                                   @JsonProperty(value = "requestParameters", required = true) RequestParameters requestParameters)
    {
        this.modelInfo = modelInfo;
        this.cloudConfiguration = cloudConfiguration;
        this.requestInfo = requestInfo;
        this.relatedInstanceList = relatedInstanceList;
        this.requestParameters = requestParameters;
    }

     public static class RequestInfo {

        @JsonInclude(NON_NULL) public final String instanceName;
        @JsonInclude(NON_NULL) public final String productFamilyId;
        public final String source;
        public final boolean suppressRollback;
        public final String requestorId;

        public RequestInfo(String instanceName, String productFamilyId, String source, boolean rollbackOnFailure, String requestorId) {
            this.instanceName = instanceName;
            this.productFamilyId = productFamilyId;
            this.source = source;
            this.requestorId = requestorId;
            // in the FE we are asking for "RollbackOnFailure" but to MSO we are passing the negative value "suppressRollback"
            this.suppressRollback = !rollbackOnFailure;
        }
    }

    public static class Project{
        public final String projectName;

        public Project(String projectName) {
            this.projectName = projectName;
        }
    }

    public static class Platform{
        public final String platformName;

        public Platform(String platformName) {
            this.platformName = platformName;
        }
    }

    public static class LineOfBusiness{
        public final String lineOfBusinessName;

        public LineOfBusiness(String lineOfBusiness) {
            this.lineOfBusinessName = lineOfBusiness;
        }
    }

    @JsonTypeName("relatedInstance")
    @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
    public static class RelatedInstance{
        public ModelInfo modelInfo;
        @JsonInclude(NON_NULL) public String instanceId; //TODO ask Eylon - is this needed, and if yes, for other fields as well?
        @JsonInclude(NON_NULL) public String instanceName;

        public RelatedInstance (@JsonProperty(value = "modelInfo", required = true) ModelInfo modelInfo,
                                @JsonProperty (value = "instanceId", required = true) String instanceId){
            this.modelInfo = modelInfo;
            this.instanceId = instanceId;
        }

        public RelatedInstance (@JsonProperty(value = "modelInfo", required = true) ModelInfo modelInfo,
                                @JsonProperty (value = "instanceId", required = true) String instanceId,
                                @JsonProperty (value = "instanceName", required = true) String instanceName){
            this.modelInfo = modelInfo;
            this.instanceId = instanceId;
            this.instanceName = instanceName;
        }
    }

    public static class RequestParameters {
        public final List<? extends UserParamTypes> userParams;

        public RequestParameters(List<? extends UserParamTypes> userParams) {
            this.userParams = userParams;
        }

        public List<? extends UserParamTypes> getUserParams() {
            return userParams;
        }
    }
}

