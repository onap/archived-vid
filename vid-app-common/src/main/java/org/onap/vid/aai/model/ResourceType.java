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

package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ResourceType {

    SERVICE_INSTANCE("service-instances", "service-instance-name"),
    GENERIC_VNF("generic-vnfs", "vnf-name"),
    PNF("pnfs", "pnf-name"),
    L3_NETWORK("l3-networks", "network-name"),
    VF_MODULE("vf-modules", "vf-module-name"),
    INSTANCE_GROUP("instance-groups", "instance-group-name"),
    VOLUME_GROUP("volume-groups", "volume-group-name");

    private static final Map<String, ResourceType> AAI_FORMAT_MAP = Stream
            .of(ResourceType.values())
            .collect(Collectors.toMap(s -> s.aaiFormat, Function.identity()));

    private final String aaiFormat;
    private final String nameFilter;

    ResourceType(String formatted, String nameFilter) {
        this.aaiFormat = formatted;
        this.nameFilter = nameFilter;
    }

    public String getAaiFormat() {
        return aaiFormat;
    }

    public String getNameFilter() {
        return nameFilter;
    }

    @JsonCreator
    public static ResourceType fromString(String string) {
        return Optional
                .ofNullable(AAI_FORMAT_MAP.get(string))
                .orElseThrow(() -> new IllegalArgumentException(string));
    }
}
