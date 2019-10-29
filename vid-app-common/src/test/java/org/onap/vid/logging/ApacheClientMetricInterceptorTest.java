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

package org.onap.vid.logging;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHttpResponse;
import org.onap.logging.ref.slf4j.ONAPLogConstants;
import org.slf4j.MDC;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ApacheClientMetricInterceptorTest {

    private ApacheClientMetricInterceptor interceptor;
    private final String path = "https://gerrit.onap.org/r/projects/vid/branches?n=16&S=0&m=master";
    private HttpGet request;
    private HttpResponse response;

    @BeforeMethod
    public void before() {
        interceptor = new ApacheClientMetricInterceptor() {};
        request = new HttpGet(path);
        response = new BasicHttpResponse(new ProtocolVersion("a",1,2), 200, "ok");
    }

    @Test
    public void testAddHeader() {
        interceptor.addHeader(request, "key", "value");
        assertEquals(request.getFirstHeader("key").getValue(), "value");
    }

    @Test
    public void testGetTargetServiceName() {
        assertEquals(interceptor.getTargetServiceName(request), path);
    }

    @Test
    public void testGetServiceName() {
        assertEquals(interceptor.getTargetServiceName(request), path);
    }

    @Test
    public void testGetHttpStatusCode() {
        assertEquals(interceptor.getHttpStatusCode(response), 200);
    }

    @Test
    public void testGetResponseCode() {
        assertEquals(interceptor.getResponseCode(response), "200");
    }

    @Test
    public void testGetTargetEntity() {
        assertNull(interceptor.getTargetEntity(request));
    }

    @Test
    protected void testAdditionalPre() {
        request.addHeader(ONAPLogConstants.Headers.INVOCATION_ID, "123");
        interceptor.additionalPre(request, request);
        assertEquals(MDC.get(ONAPLogConstants.MDCs.INVOCATION_ID), "123");
    }

    @Test
    protected void whenThereIsNoInvocationIdHeader_thenMdcValueIsNull() {
        interceptor.additionalPre(request, request);
        assertNull(MDC.get(ONAPLogConstants.MDCs.INVOCATION_ID));
    }
}
