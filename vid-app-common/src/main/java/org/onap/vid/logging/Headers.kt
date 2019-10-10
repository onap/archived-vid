/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

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
