package org.onap.vid.model

data class VersionAndFeatures(
        val features:String,
        val build:String,
        val displayVersion:String
) {
    companion object {
        val unknown: VersionAndFeatures = VersionAndFeatures("unknown", "unknown", "unknown")
    }
}

