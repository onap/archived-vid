package org.onap.vid.model

data class SOWorkflow @JvmOverloads constructor(var id: Long? = null, var name: String? = null) {
    fun clone(): SOWorkflow {
        return copy()
    }
}

data class SOWorkflows @JvmOverloads constructor(var workflows: List<SOWorkflow>? = null) {
    fun clone(): SOWorkflows {
        return copy()
    }
}