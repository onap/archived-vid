package org.onap.vid.mso.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.onap.vid.domain.mso.CloudConfiguration;
import org.onap.vid.domain.mso.ModelInfo;
import org.onap.vid.domain.mso.SubscriberInfo;
import org.onap.vid.model.serviceInstantiation.VfModule;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class ServiceInstantiationRequestDetails {

    @JsonProperty("modelInfo")
    private ModelInfo modelInfo;

    @JsonProperty("owningEntity")
    private ServiceInstantiationOwningEntity owningEntity;

    @JsonProperty("subscriberInfo")
    private SubscriberInfo subscriberInfo;

    @JsonProperty("project")
    private Project project;

    @JsonProperty("requestParameters")
    private RequestParameters requestParameters;

    @JsonProperty("requestInfo")
    private RequestInfo requestInfo;

    public ServiceInstantiationRequestDetails(@JsonProperty(value = "modelInfo", required = true) ModelInfo modelInfo,
                                              @JsonProperty(value = "owningEntity", required = true) ServiceInstantiationOwningEntity owningEntity,
                                              @JsonProperty(value = "subscriberInfo", required = true) SubscriberInfo subscriberInfo,
                                              @JsonProperty(value = "project", required = true) Project project,
                                              @JsonProperty(value = "requestInfo", required = true) RequestInfo requestInfo,
                                              @JsonProperty(value = "requestParameters", required = true) RequestParameters requestParameters) {
        this.modelInfo = modelInfo;
        this.owningEntity = owningEntity;
        this.subscriberInfo = subscriberInfo;
        this.project = project;
        this.requestInfo = requestInfo;
        this.requestParameters = requestParameters;
    }

    public static class  ServiceInstantiationOwningEntity{
        public final String owningEntityId;
        public final String owningEntityName;

        public ServiceInstantiationOwningEntity(String owningEntityId, String owningEntityName) {
            this.owningEntityId = owningEntityId;
            this.owningEntityName = owningEntityName;
        }
    }

    public static class RequestInfo {

        @JsonInclude(NON_NULL) public final String instanceName;
        public final String productFamilyId;
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

    public static class RequestParameters {

        public final String subscriptionServiceType;
        public final boolean aLaCarte;
        public final List<ServiceInstantiationService> userParams;

        public RequestParameters(String subscriptionServiceType, boolean aLaCarte, List<ServiceInstantiationService> userParams) {
            this.subscriptionServiceType = subscriptionServiceType;
            this.aLaCarte = aLaCarte;
            this.userParams = userParams;
        }
    }

    @JsonTypeName("service")
    @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
    public static class ServiceInstantiationService{
        public ModelInfo modelInfo = new ModelInfo();
        @JsonInclude(NON_NULL) public String instanceName;
        public List<Map<String,String>> instanceParams;
        public ServiceInstantiationVnfList resources;

        public ServiceInstantiationService (ModelInfo modelInfo, String instanceName, List<Map<String,String>> instanceParams, ServiceInstantiationVnfList vnfs){
            this.modelInfo.setModelType(modelInfo.getModelType());
            this.modelInfo.setModelName(modelInfo.getModelName());
            this.modelInfo.setModelVersionId(modelInfo.getModelVersionId());
            this.instanceName = instanceName;
            this.instanceParams = instanceParams;
            this.resources = vnfs;
        }
    }

    public static class ServiceInstantiationVnfList{
        public final List<ServiceInstantiationVnf> vnfs;

        public ServiceInstantiationVnfList(List<ServiceInstantiationVnf> vnfList) {
            this.vnfs = vnfList;
        }
    }

    public static class ServiceInstantiationVnf{
        public final ModelInfo modelInfo;
        public final CloudConfiguration cloudConfiguration;
        public final Platform platform;
        public final LineOfBusiness lineOfBusiness;
        public final String productFamilyId;
        public final List<Map<String, String>>  instanceParams;
        @JsonInclude(NON_EMPTY) public final List<VfModule> vfModules;
        @JsonInclude(NON_NULL) public final String instanceName;

        public ServiceInstantiationVnf(ModelInfo modelInfo, CloudConfiguration cloudConfiguration, String platform, String lineOfBusiness, String productFamilyId, List<Map<String, String>>  instanceParams, List<VfModule> vfModules, String instanceName) {
            this.modelInfo = modelInfo;
            this.cloudConfiguration = cloudConfiguration;
            this.platform = new Platform(platform);
            this.lineOfBusiness = new LineOfBusiness(lineOfBusiness);
            this.productFamilyId = productFamilyId;
            this.instanceParams = instanceParams;
            this.vfModules = vfModules;
            this.instanceName = instanceName;
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
}

