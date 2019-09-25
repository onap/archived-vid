@file:JvmName("Headers")

package org.onap.vid.logging

import org.onap.portalsdk.core.util.SystemProperties
import org.onap.vid.logging.RequestIdHeader.*
import javax.servlet.http.HttpServletRequest

enum class RequestIdHeader(val headerName: String) {

    ONAP_ID("X-ONAP-RequestID"),
    REQUEST_ID("X-RequestID"),
    TRANSACTION_ID("X-TransactionID"),
    ECOMP_ID(SystemProperties.ECOMP_REQUEST_ID),
    ;

    fun stringEquals(header: String) = headerName.equals(header, true)

    fun getHeaderValue(request: HttpServletRequest): String? = request.getHeader(headerName)
}

fun prioritizedRequestIdHeaders() = listOf(
        ONAP_ID,
        REQUEST_ID,
        TRANSACTION_ID,
        ECOMP_ID
)

fun highestPriorityHeader(httpRequest: HttpServletRequest): RequestIdHeader? {
    val headers = httpRequest.headerNames.asSequence().toSet()
    return prioritizedRequestIdHeaders().firstOrNull {
        requestIdHeader -> headers.any { requestIdHeader.stringEquals(it) }
    }
}
