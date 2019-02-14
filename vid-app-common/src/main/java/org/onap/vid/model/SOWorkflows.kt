package org.onap.vid.model

data class SOWorkflow constructor(val id: Long, val name: String) {
    fun clone(): SOWorkflow {
        return copy()
    }
}

data class SOWorkflows @JvmOverloads constructor(val workflows: List<SOWorkflow> = emptyList()) {
    fun clone(): SOWorkflows {
        return copy(workflows.toMutableList())
    }
}

enum class SOWorkflowType(val type: String) {
    STRING("STRING")
}

data class SOWorkflowParameterDefinition constructor(val id: Long, val name: String, val pattern: String,
                                         val type: SOWorkflowType, val required: Boolean)

data class SOWorkflowParameterDefinitions constructor(val parameterDefinitions: List<SOWorkflowParameterDefinition> = emptyList()) {
    fun clone(): SOWorkflowParameterDefinitions {
        return copy(parameterDefinitions.toMutableList())
    }
}