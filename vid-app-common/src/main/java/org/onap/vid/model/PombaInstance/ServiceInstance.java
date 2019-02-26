/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia. All rights reserved.
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
package org.onap.vid.model.PombaInstance;

public final class ServiceInstance {

    private String serviceInstanceId;
    private String modelVersionId;
    private String modelInvariantId;
    private String customerId;
    private String serviceType;

    public ServiceInstance() {
    }

    public ServiceInstance(String serviceInstanceId,
        String modelVersionId,
        String modelInvariantId,
        String customerId,
        String serviceType) {
        this.serviceInstanceId = serviceInstanceId;
        this.modelVersionId = modelVersionId;
        this.modelInvariantId = modelInvariantId;
        this.customerId = customerId;
        this.serviceType = serviceType;
    }

    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    public String getModelVersionId() {
        return modelVersionId;
    }

    public String getModelInvariantId() {
        return modelInvariantId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getServiceType() {
        return serviceType;
    }
}
