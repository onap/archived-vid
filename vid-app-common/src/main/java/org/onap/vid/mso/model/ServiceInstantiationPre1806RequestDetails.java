package org.onap.vid.mso.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;
import org.onap.vid.mso.rest.SubscriberInfo;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.onap.vid.mso.model.BaseResourceInstantiationRequestDetails.RelatedInstance;

public class ServiceInstantiationPre1806RequestDetails extends ServiceInstantiationRequestDetails {

    @JsonInclude(NON_NULL)
    public final CloudConfiguration cloudConfiguration;
    @JsonInclude(NON_EMPTY)
    public final List<RelatedInstance> relatedInstanceList;

    public ServiceInstantiationPre1806RequestDetails(ModelInfo modelInfo, ServiceInstantiationOwningEntity owningEntity,
                                                     SubscriberInfo subscriberInfo, Project project, RequestInfo requestInfo,
                                                     RequestParameters requestParameters, CloudConfiguration cloudConfiguration,
                                                     List<RelatedInstance> relatedInstanceList) {
        super(modelInfo, owningEntity, subscriberInfo, project, requestInfo, requestParameters);
        if ((cloudConfiguration != null) && (!StringUtils.isEmpty(cloudConfiguration.getLcpCloudRegionId())) && (!StringUtils.isEmpty(cloudConfiguration.getTenantId()))){
            this.cloudConfiguration = cloudConfiguration;
        } else {
            this.cloudConfiguration = null;
        }
        this.relatedInstanceList = relatedInstanceList;
    }
}
