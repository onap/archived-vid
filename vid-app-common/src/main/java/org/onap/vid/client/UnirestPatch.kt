package org.onap.vid.client

import io.joshworks.restclient.http.Headers
import io.joshworks.restclient.http.HttpResponse
import io.joshworks.restclient.http.mapper.ObjectMapper
import org.apache.commons.lang3.reflect.FieldUtils
import org.apache.http.HttpVersion
import org.apache.http.message.BasicHttpResponse
import java.io.InputStream

/// Patch NPE in Unirest HttpResponse::getBody when getRawBody is null
fun <T> patched(httpResponse: HttpResponse<T>) =
        if (willGetBodyTriggerNPE(httpResponse)) HttpResponsePatch(httpResponse) else httpResponse

/**
 * This class inherits HttpResponse to have compatible interface,
 * but implementation is done through delegation to another
 * instance.
 */
class HttpResponsePatch<T>(val delegatee: HttpResponse<T>) : HttpResponse<T>(
        BasicHttpResponse(HttpVersion.HTTP_1_1, 200, "ok"),
        responseClassOf(delegatee),
        objectMapperOf(delegatee)
) {
    override fun getBody(): T? = if (willGetBodyTriggerNPE(delegatee)) null else delegatee.body
    override fun getHeaders(): Headers? = delegatee.headers
    override fun getStatus() = delegatee.status
    override fun isSuccessful() = delegatee.isSuccessful
    override fun getStatusText(): String? = delegatee.statusText
    override fun getRawBody(): InputStream? = delegatee.rawBody
}

private fun <T> willGetBodyTriggerNPE(httpResponse: HttpResponse<T>) =
        httpResponse.rawBody == null

private fun <T> objectMapperOf(httpResponse: HttpResponse<T>) =
        FieldUtils.readDeclaredField(httpResponse, "objectMapper", true) as ObjectMapper?

@Suppress("UNCHECKED_CAST")
private fun <T> responseClassOf(httpResponse: HttpResponse<T>) =
        FieldUtils.readDeclaredField(httpResponse, "responseClass", true) as Class<T>

