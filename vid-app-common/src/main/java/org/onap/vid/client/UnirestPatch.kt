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

package org.onap.vid.client

import io.joshworks.restclient.http.Headers
import io.joshworks.restclient.http.HttpResponse
import org.apache.commons.io.IOUtils
import org.apache.http.HttpVersion
import org.apache.http.message.BasicHttpResponse
import java.io.InputStream
import java.nio.charset.StandardCharsets

/// Patch NPE in joshworks's Unirest HttpResponse::getBody when getRawBody is null
fun <T> patched(httpResponse: HttpResponse<T>) =
        if (willGetBodyTriggerNPE(httpResponse)) HttpResponsePatch(httpResponse) else httpResponse

private fun <T> willGetBodyTriggerNPE(httpResponse: HttpResponse<T>) =
        httpResponse.rawBody == null || httpResponse.rawBody.available() == 0

private val dummyHttpResponse = BasicHttpResponse(HttpVersion.HTTP_1_1, 200, "ok")

fun extractRawAsString(response: HttpResponse<*>?): String {
    try {
        if (response == null || response.rawBody==null) return ""
        response.rawBody.reset()
        return IOUtils.toString(response.rawBody, StandardCharsets.UTF_8.name())
    } catch (e: Exception) {
        //Nothing to do here
    }

    return ""
}
/**
 * This class inherits HttpResponse to have compatible interface,
 * but implementation is done through delegation to another
 * instance.
 * For that, it's enough to pass dummy values to HttpResponse's
 * constructor, as parent HttpResponse methods won't be used,
 * only overridden.
 */
private class HttpResponsePatch<T>(private val delegatee: HttpResponse<T>) : HttpResponse<T>(
        dummyHttpResponse, null, null
) {
    override fun getBody(): T? = if (willGetBodyTriggerNPE(delegatee)) null else delegatee.body
    override fun getHeaders(): Headers? = delegatee.headers
    override fun getStatus() = delegatee.status
    override fun isSuccessful() = delegatee.isSuccessful
    override fun getStatusText(): String? = delegatee.statusText
    override fun getRawBody(): InputStream? = delegatee.rawBody
}
