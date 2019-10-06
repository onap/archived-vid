/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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


import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.exceptions.InvalidPropertyException;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.utils.Logging;
import org.onap.vid.utils.Unchecked;
import org.springframework.http.HttpMethod;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class AAIRestInterfaceTest {

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
    @Mock
    private Logging loggingService;


    private AAIRestInterface testSubject;

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
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
        return new AAIRestInterface(Optional.of(client), httpsAuthClient, servletRequestHelper, systemPropertyHelper, loggingService);
    }

    @Test
    public void shouldEncodeURL() throws Exception {
        String nodeKey = "some unusual uri";
        String nodeKey2 = "some/usual/uri";
        Assert.assertEquals(testSubject.encodeURL(nodeKey), "some%20unusual%20uri");
        Assert.assertEquals(testSubject.encodeURL(nodeKey2), "some%2Fusual%2Furi");
    }

    @Test
    public void shouldSetRestSrvrBaseURL() {
        String baseUrl = "anything";
        testSubject.setRestSrvrBaseURL(baseUrl);
        Assert.assertEquals(testSubject.getRestSrvrBaseURL(), baseUrl);
    }

    @Test
    public void shouldExecuteRestJsonPutMethodWithResponse200() {
        // given
        String payload = "{\"id\": 1}";
        Entity<String> entity = Entity.entity(payload, MediaType.APPLICATION_JSON);

        // when
        when(response.getStatusInfo()).thenReturn(OK);
        Response finalResponse = testSubject.RestPut("", PATH, payload, false, true).getResponse();

        // then
        verify(builder).build(HttpMethod.PUT.name(), entity);
        Assert.assertEquals(response, finalResponse);
    }

    @Test(expectedExceptions = {ExceptionWithRequestInfo.class})
    public void shouldFailWhenRestJsonPutMethodExecuted() {
        // given
        String payload = "{\"id\": 1}";

        // when
        when(builder.build(eq(HttpMethod.PUT.name()), any(Entity.class))).thenThrow(new GenericUncheckedException("msg"));
        testSubject.RestPut("", PATH, payload, false, true);

        //then
    }

    @Test
    public void shouldExecuteRestJsonPutMethodWithResponse400() {
        // given
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
    public void shouldExecuteRestPostMethodWithResponse200() {
        // given
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
    public void shouldExecuteRestPostMethodWithResponse400() {
        // given
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
    public void shouldFailWhenRestPostMethodExecuted() {
        // given
        String payload = "{\"id\": 1}";
        Entity<String> entity = Entity.entity(payload, MediaType.APPLICATION_JSON);

        // when
        when(builder.post(Mockito.any(Entity.class))).thenThrow(new RuntimeException());
        Response finalResponse = testSubject.RestPost("", PATH, payload, false);

        // then
        verify(builder).post(entity);
        Assert.assertNull(finalResponse);
    }

    @Test
    public void shouldExecuteRestGetMethodWithResponse200() {
        // given
        // when
        when(response.getStatusInfo()).thenReturn(OK);
        Response finalResponse = testSubject.RestGet("", "", Unchecked.toURI(PATH), false).getResponse();

        // then
        Assert.assertEquals(response, finalResponse);
    }

    @Test
    public void shouldExecuteRestGetMethodWithResponse400() {
        // given
        // when
        when(response.getStatusInfo()).thenReturn(BAD_REQUEST);
        when(response.getStatus()).thenReturn(BAD_REQUEST.getStatusCode());
        Response finalResponse = testSubject.RestGet("", "", Unchecked.toURI(PATH), false).getResponse();

        // then
        Assert.assertEquals(response, finalResponse);
    }

    @Test
    public void shouldFailWhenRestGetMethodExecuted() {
        // given
        // when
        when(builder.build(HttpMethod.GET.name())).thenThrow(new RuntimeException());
        Response finalResponse = testSubject.RestGet("", "", Unchecked.toURI(PATH), false).getResponse();

        // then
        Assert.assertNull(finalResponse);
    }

    private void mockSystemProperties() throws UnsupportedEncodingException, InvalidPropertyException {
        when(systemPropertyHelper.getEncodedCredentials()).thenReturn("someCredentials");
        when(systemPropertyHelper.getFullServicePath(Mockito.anyString())).thenReturn("http://localhost/path");
        when(systemPropertyHelper.getFullServicePath(Mockito.any(URI.class))).thenReturn("http://localhost/path");
        when(systemPropertyHelper.getServiceBasePath(Mockito.anyString())).thenReturn("http://localhost/path");
    }

}
