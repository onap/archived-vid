package org.onap.vid.client

import org.onap.vid.testUtils.TestUtils
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class UnirestPatchKtTest {

    @Test
    fun givenHttpResponseIsNull_whenExtractRawAsString_emptyStringIsReturn() {
        val result = extractRawAsString(null)
        assertEquals(result, "")
    }

    @Test
    fun givenHttpResponseBodyIsNull_whenExtractRawAsString_emptyStringIsReturn() {
        val result = extractRawAsString(TestUtils.createTestHttpResponse(200, null))
        assertEquals(result, "")
    }

    @Test
    fun givenHttpResponseBodyIsString_whenExtractRawAsString_sameStringIsReturn() {
        val body = "someBody"
        val result = extractRawAsString(TestUtils.createTestHttpResponse(200, body))
        assertEquals(result, body)
    }

    @Test
    fun givenHttpResponseBodyIsString_whenReadBodyAndExtractRawAsString_sameStringIsReturn() {
        val body = "someBody"
        val httpResponse = TestUtils.createTestHttpResponse(200, body);
        assertEquals(httpResponse.body, body) //read body once
        val result = extractRawAsString(httpResponse)
        assertEquals(result, body)
    }
}
