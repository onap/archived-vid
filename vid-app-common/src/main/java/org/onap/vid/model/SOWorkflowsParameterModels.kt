package org.onap.vid.model

enum class SOWorkflowType(var type: String? = "STRING") {
    STRING("STRING")
}

data class SOWorkflowParameterDefinition(var id: Long? = null, var name: String? = null, var pattern: String? = null, var type: SOWorkflowType? = null, val required: Boolean? = null)
data class SOWorkflowParameterDefinitions(var parameterDefinitions: List<SOWorkflowParameterDefinition>? = null)