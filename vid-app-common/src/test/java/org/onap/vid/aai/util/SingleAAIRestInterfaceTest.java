/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia. All rights reserved.
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

package org.onap.vid.aai.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.exceptions.InvalidPropertyException;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.utils.Unchecked;
import org.springframework.http.HttpMethod;
import org.testng.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.*;
import static junit.framework.TestCase.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SingleAAIRestInterfaceTest {

    private static final String PATH = "path";
    private static final String HTTP_LOCALHOST = "http://localhost/";
    @Mock
    private Client client;
    @Mock
    private WebTarget webTarget;
    @Mock
    private Invocation.Builder builder;
    @Mock
    private Invocation invocation;
    @Mock
    private ServletRequestHelper servletRequestHelper;
    @Mock
    private HttpsAuthClient httpsAuthClient;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private Response response;
    @Mock
    private SystemPropertyHelper systemPropertyHelper;

    private AAIRestInterface testSubject;

    @Before
    public void setUp() throws Exception {
        mockSystemProperties();
        testSubject = createTestSubject();
        when(client.target(HTTP_LOCALHOST+PATH)).thenReturn(webTarget);
        when(webTarget.request()).thenReturn(builder);
        when(builder.accept(Mockito.anyString())).thenReturn(builder);
        when(builder.header(Mockito.anyString(), Mockito.anyString())).thenReturn(builder);
        when(builder.build(Mockito.anyString())).thenReturn(invocation);
        when(builder.build(Mockito.anyString(), any(Entity.class))).thenReturn(invocation);
        when(invocation.invoke()).thenReturn(response);
        when(servletRequestHelper.extractOrGenerateRequestId()).thenReturn(UUID.randomUUID().toString());
    }

    private AAIRestInterface createTestSubject() {
        return new AAIRestInterface(Optional.of(client), httpsAuthClient, servletRequestHelper, systemPropertyHelper);
    }

    @Test
    public void testEncodeURL() throws Exception {
        String nodeKey = "some unusual uri";
        Assert.assertEquals(testSubject.encodeURL(nodeKey), "some%20unusual%20uri");
    }

    @Test
    public void testSetRestSrvrBaseURLWithNullValue() {
        testSubject.SetRestSrvrBaseURL(null);
    }

    @Test
    public void testSetRestSrvrBaseURL() {
        String baseUrl = "anything";
        testSubject.SetRestSrvrBaseURL(baseUrl);
        Assert.assertEquals(testSubject.getRestSrvrBaseURL(), baseUrl);
    }

    @Test
    public void testRestJsonPutWithResponse200() {
        // given
        String methodName = "RestPut";
        String payload = "{\"id\": 1}";
        Entity<String> entity = Entity.entity(payload, MediaType.APPLICATION_JSON);

        // when
        when(response.getStatusInfo()).thenReturn(OK);
        Response finalResponse = testSubject.RestPut("", PATH, payload, false, true).getResponse();

        // then
        verify(builder).build(HttpMethod.PUT.name(), entity);
        Assert.assertEquals(response, finalResponse);
    }

    @Test(expected = ExceptionWithRequestInfo.class)
    public void testFailedRestJsonPut() {
        // given
        String methodName = "RestPut";
        String payload = "{\"id\": 1}";
        Entity<String> entity = Entity.entity(payload, MediaType.APPLICATION_JSON);

        // when
        when(builder.build(eq(HttpMethod.PUT.name()), any(Entity.class))).thenThrow(new GenericUncheckedException("msg"));
        Response finalResponse = testSubject.RestPut("", PATH, payload, false, true).getResponse();

        // then
        fail("expected unreachable: exception to be thrown");
    }

    @Test
    public void testRestJsonPutWithResponse400() {
        // given
        String methodName = "RestPut";
        String payload = "{\"id\": 1}";
        Entity<String> entity = Entity.entity(payload, MediaType.APPLICATION_JSON);

        // when
        when(response.getStatusInfo()).thenReturn(BAD_REQUEST);
        when(response.getStatus()).thenReturn(BAD_REQUEST.getStatusCode());
        Response finalResponse = testSubject.RestPut("", PATH, payload, false, true).getResponse();

        // then
        verify(builder).build(HttpMethod.PUT.name(), entity);
        Assert.assertEquals(response, finalResponse);
    }

    @Test
    public void testRestPostWithResponse200() {
        // given
        String methodName = "RestPost";
        String payload = "{\"id\": 1}";
        Entity<String> entity = Entity.entity(payload, MediaType.APPLICATION_JSON);

        // when
        when(builder.post(Mockito.any(Entity.class))).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(OK);
        Response finalResponse = testSubject.RestPost("", PATH, payload, false);

        // then
        verify(builder).post(entity);
        Assert.assertEquals(response, finalResponse);
    }

    @Test
    public void testRestPostWithResponse400() {
        // given
        String methodName = "RestPost";
        String payload = "{\"id\": 1}";
        Entity<String> entity = Entity.entity(payload, MediaType.APPLICATION_JSON);

        // when
        when(builder.post(Mockito.any(Entity.class))).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(BAD_REQUEST);
        when(response.getStatus()).thenReturn(BAD_REQUEST.getStatusCode());
        Response finalResponse = testSubject.RestPost("", PATH, payload, false);

        // then
        verify(builder).post(entity);
        Assert.assertEquals(response, finalResponse);
    }

    @Test
    public void testFailedRestPost() {
        // given
        String methodName = "RestPost";
        String payload = "{\"id\": 1}";
        Entity<String> entity = Entity.entity(payload, MediaType.APPLICATION_JSON);

        // when
        when(builder.post(Mockito.any(Entity.class))).thenThrow(new RuntimeException());
        Response finalResponse = testSubject.RestPost("", PATH, payload, false);

        // then
        verify(builder).post(entity);
        Assert.assertEquals(finalResponse, null);
    }

    @Test
    public void testRestDeleteWithResponse400() {
        // given
        String methodName = "Delete";

        // when
        when(builder.delete()).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(BAD_REQUEST);
        String reason = "Any reason";
        when(response.readEntity(String.class)).thenReturn(reason);
        when(response.getStatus()).thenReturn(BAD_REQUEST.getStatusCode());
        boolean finalResponse = testSubject.Delete("", "", PATH);

        // then
        verify(builder).delete();
        Assert.assertFalse(finalResponse);
    }

    @Test
    public void testRestDeleteWithResponse404() {
        // given
        String methodName = "Delete";

        // when
        when(builder.delete()).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(NOT_FOUND);
        String reason = "Any reason";
        when(response.readEntity(String.class)).thenReturn(reason);
        when(response.getStatus()).thenReturn(NOT_FOUND.getStatusCode());
        boolean finalResponse = testSubject.Delete("", "", PATH);

        // then
        verify(builder).delete();
        Assert.assertFalse(finalResponse);
    }

    @Test
    public void testFailedRestDelete() {
        // given
        String methodName = "Delete";

        // when
        when(builder.delete()).thenThrow(new RuntimeException());
        boolean finalResponse = testSubject.Delete("", "", PATH);

        // then
        verify(builder).delete();
        Assert.assertFalse(finalResponse);
    }

    @Test
    public void testRestJsonGetWithResponse200() {
        // given
        String methodName = "RestGet";

        // when
        when(response.getStatusInfo()).thenReturn(OK);
        Response finalResponse = testSubject.RestGet("", "", Unchecked.toURI(PATH), false).getResponse();

        // then
        Assert.assertEquals(response, finalResponse);
    }

    @Test
    public void testRestJsonGetWithResponse400() {
        // given
        String methodName = "RestGet";

        // when
        when(response.getStatusInfo()).thenReturn(BAD_REQUEST);
        when(response.getStatus()).thenReturn(BAD_REQUEST.getStatusCode());
        Response finalResponse = testSubject.RestGet("", "", Unchecked.toURI(PATH), false).getResponse();

        // then
        Assert.assertEquals(response, finalResponse);
    }

    @Test
    public void testFailedRestGet() {
        // given
        String methodName = "RestGet";

        // when
        when(builder.build(HttpMethod.GET.name())).thenThrow(new RuntimeException());
        Response finalResponse = testSubject.RestGet("", "", Unchecked.toURI(PATH), false).getResponse();

        // then
        Assert.assertEquals(finalResponse, null);
    }

    private void mockSystemProperties() throws UnsupportedEncodingException, InvalidPropertyException {
        when(systemPropertyHelper.getEncodedCredentials()).thenReturn("someCredentials");
        when(systemPropertyHelper.getFullServicePath(Mockito.anyString())).thenReturn("http://localhost/path");
        when(systemPropertyHelper.getFullServicePath(Mockito.any(URI.class))).thenReturn("http://localhost/path");
        when(systemPropertyHelper.getServiceBasePath(Mockito.anyString())).thenReturn("http://localhost/path");
    }

}