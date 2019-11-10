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

package org.onap.vid.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.google.common.base.CaseFormat

class VidNotions(@get:JsonInclude(JsonInclude.Include.NON_NULL)
                 val instantiationUI: InstantiationUI,
                 val modelCategory: ModelCategory,
                 val viewEditUI: InstantiationUI,
                 val instantiationType: InstantiationType) {
    enum class InstantiationUI {
        NETWORK_WITH_PROPERTY_NETWORK_TECHNOLOGY_EQUALS_STANDARD_SRIOV_OR_OVS,
        SERVICE_WITH_FABRIC_CONFIGURATION,
        LEGACY,
        SERVICE_UUID_IS_1ffce89f_ef3f_4cbb_8b37_82134590c5de,
        ANY_ALACARTE_NEW_UI,
        MACRO_SERVICE,
        SERVICE_WITH_VNF_GROUPING,
        TRANSPORT_SERVICE,
        SERVICE_WITH_COLLECTION_RESOURCE,
        A_LA_CARTE_VNF_SERVICE_ROLE,
        INFRASTRUCTURE_VPN
        ;

        @JsonValue
        fun toLowerCamel(): String {
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, this.name)
        }

    }

    enum class ModelCategory {
        @JsonProperty("5G Provider Network")
        IS_5G_PROVIDER_NETWORK_MODEL,
        @JsonProperty("5G Fabric Configuration")
        IS_5G_FABRIC_CONFIGURATION_MODEL,
        Transport,
        SERVICE_WITH_COLLECTION_RESOURCE,
        INFRASTRUCTURE_VPN,
        PORT_MIRRORING,        
        @JsonProperty("other")
        OTHER
    }

    enum class InstantiationType {
        Macro,
        ALaCarte,
        ClientConfig
    }
}
