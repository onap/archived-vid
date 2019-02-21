package org.onap.vid.model

data class SOWorkflow @JvmOverloads constructor(var id: Long? = null, var name: String? = null) {
    fun clone(): SOWorkflow {
        return copy()
    }
}

data class SOWorkflows @JvmOverloads constructor(var workflows: List<SOWorkflow>? = emptyList()) {
    fun clone(): SOWorkflows {
        return copy(workflows?.toMutableList())
    }
}


enum class SOWorkflowType(var type: String? = "STRING") {
    STRING("STRING")
}

data class SOWorkflowParameterDefinition(var id: Long? = null, var name: String? = null, var pattern: String? = null, var type: SOWorkflowType? = null, val required: Boolean? = null)
data class SOWorkflowParameterDefinitions(var parameterDefinitions: List<SOWorkflowParameterDefinition>? = null)