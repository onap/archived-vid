@file:JvmName("Headers")

package org.onap.vid.logging

import org.onap.portalsdk.core.util.SystemProperties
import org.onap.vid.logging.RequestIdHeader.*
import java.util.*
import javax.servlet.http.HttpServletRequest

interface Header {
    val headerName: String
    fun stringEquals(header: String): Boolean = headerName.equals(header, true)
}

abstract class NamedHeader(override val headerName: String) : Header {
    abstract fun getHeaderValue(): String
}

@JvmField
val PARTNER_NAME = object : NamedHeader("X-ONAP-PartnerName") {
    override fun getHeaderValue() = "VID.VID"
}

@JvmField
val INVOCATION_ID = object : NamedHeader("X-InvocationID") {
    override fun getHeaderValue() = UUID.randomUUID().toString()
}

enum class RequestIdHeader(override val headerName: String) : Header {
    ONAP_ID("X-ONAP-RequestID"),
    REQUEST_ID("X-RequestID"),
    TRANSACTION_ID("X-TransactionID"),
    ECOMP_ID(SystemProperties.ECOMP_REQUEST_ID),
    ;

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
