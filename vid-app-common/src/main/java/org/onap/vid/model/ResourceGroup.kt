package org.onap.vid.model

data class ResourceGroup (

        val type: String,
        override val invariantUuid: String,
        override val uuid: String,
        override val version: String,
        override val name: String,
        val modelCustomizationName: String,
        val properties: Map<String, Any>,
        val members: Map<String, ServiceProxy>
) : MinimalNode
