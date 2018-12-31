package org.onap.vid.mso.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonProperty

class ServiceDeletionRequestDetails(val modelInfo: ModelInfo,
                                    val requestInfo: RequestInfo,
                                    val requestParameters: RequestParameters) {

    class RequestInfo(val source: String, val requestorId: String)

    class RequestParameters(@JsonInclude(NON_NULL) @get:JsonProperty("aLaCarte") val aLaCarte: Boolean?,
                            @JsonInclude(NON_NULL) val testApi: String?)
}