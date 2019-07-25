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

package org.onap.vid.mso.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.onap.vid.mso.model.BaseResourceInstantiationRequestDetails.RelatedInstance;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.onap.vid.mso.rest.SubscriberInfo;

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
