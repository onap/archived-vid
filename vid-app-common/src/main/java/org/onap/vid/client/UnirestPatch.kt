package org.onap.vid.client

import io.joshworks.restclient.http.Headers
import io.joshworks.restclient.http.HttpResponse
import org.apache.http.HttpVersion
import org.apache.http.message.BasicHttpResponse
import java.io.InputStream

/// Patch NPE in joshworks's Unirest HttpResponse::getBody when getRawBody is null
fun <T> patched(httpResponse: HttpResponse<T>) =
        if (willGetBodyTriggerNPE(httpResponse)) HttpResponsePatch(httpResponse) else httpResponse

private fun <T> willGetBodyTriggerNPE(httpResponse: HttpResponse<T>) =
        httpResponse.rawBody == null

private val dummyHttpResponse = BasicHttpResponse(HttpVersion.HTTP_1_1, 200, "ok")

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
