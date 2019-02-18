/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 AT&T
 * ================================================================================
 * Modifications Copyright (C) 2019 Nokia
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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import java.util.List;

public final class PombaRequest {

    private ImmutableList<ServiceInstance> serviceInstanceList;

    @JsonCreator
    public PombaRequest(@JsonProperty("serviceInstanceList") List<ServiceInstance> serviceInstanceList) {
        this.serviceInstanceList = ImmutableList.copyOf(serviceInstanceList);
    }

    public List<ServiceInstance> getServiceInstanceList() {
        return serviceInstanceList;
    }
}
