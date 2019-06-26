package org.onap.vid.model.errorReport

data class ReportCreationParameters(
        var requestId: String? = null,
        var serviceUuid: String? = null
)