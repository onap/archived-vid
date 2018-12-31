package org.onap.vid.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.google.common.base.CaseFormat

class VidNotions(@get:JsonInclude(JsonInclude.Include.NON_NULL)
                 val instantiationUI: InstantiationUI, val modelCategory: ModelCategory, val viewEditUI: InstantiationUI) {
    enum class InstantiationUI {
        NETWORK_WITH_PROPERTY_NETWORK_TECHNOLOGY_EQUALS_STANDARD_SRIOV_OR_OVS,
        SERVICE_WITH_FABRIC_CONFIGURATION,
        LEGACY,
        SERVICE_UUID_IS_1ffce89f_ef3f_4cbb_8b37_82134590c5de,
        ANY_ALACARTE_NEW_UI,
        MACRO_SERVICE,
        SERVICE_WITH_VNF_GROUPING;

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
        @JsonProperty("other")
        OTHER
    }
}
