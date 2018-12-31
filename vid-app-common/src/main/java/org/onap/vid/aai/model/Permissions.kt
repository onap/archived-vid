package org.onap.vid.aai.model

import com.fasterxml.jackson.annotation.JsonProperty


data class Permissions(@get:JsonProperty("isEditPermitted") val isEditPermitted: Boolean)