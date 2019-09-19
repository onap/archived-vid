@file:JvmName("Headers")

package org.onap.vid.logging

import org.onap.portalsdk.core.util.SystemProperties.ECOMP_REQUEST_ID
import javax.servlet.http.HttpServletRequest

fun prioritizedRequestIdHeaders() = listOf(
        "X-ONAP-RequestID",
        "X-RequestID",
        "X-TransactionID",
        ECOMP_REQUEST_ID
)

fun highestPriorityHeader(httpRequest: HttpServletRequest): String? {
    val headers = httpRequest.headerNames.asSequence().toSet().map { it.toUpperCase() }
    return prioritizedRequestIdHeaders().firstOrNull { headers.contains(it.toUpperCase()) }
}
