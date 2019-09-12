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

import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.exceptions.InvalidPropertyException;
import org.onap.vid.utils.Logging;
import org.testng.Assert;

@RunWith(Parameterized.class)
public class ParametrizedAAIRestInterfaceTest {

    private static final String PATH = "path";
    private static final String HTTP_LOCALHOST = "http://localhost/";
    @Mock
    private Client client;
    @Mock
    private WebTarget webTarget;
    @Mock
    private Invocation.Builder builder;
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
    private Response.Status status;

    @Parameterized.Parameters
    public static Collection<Object> data() {
        return Arrays.asList(OK, NO_CONTENT);
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockSystemProperties();
        testSubject = createTestSubject();
        when(client.target(HTTP_LOCALHOST+PATH)).thenReturn(webTarget);
        when(webTarget.request()).thenReturn(builder);
        when(builder.accept(Mockito.anyString())).thenReturn(builder);
        when(builder.header(Mockito.anyString(), Mockito.anyString())).thenReturn(builder);
        when(servletRequestHelper.extractOrGenerateRequestId()).thenReturn(UUID.randomUUID().toString());
    }

    public ParametrizedAAIRestInterfaceTest(Response.Status status) {
        this.status = status;
    }

    private AAIRestInterface createTestSubject() {
        return new AAIRestInterface(Optional.of(client), httpsAuthClient, servletRequestHelper, systemPropertyHelper, loggingService);
    }

    @Test
    public void testRestDeleteWithValidResponse() {

        // when
        when(builder.delete()).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(status);
        boolean finalResponse = testSubject.Delete("", "", PATH);

        // then
        verify(builder).delete();
        Assert.assertTrue(finalResponse);
    }

    private void mockSystemProperties() throws UnsupportedEncodingException, InvalidPropertyException {
        when(systemPropertyHelper.getAAIServerUrl()).thenReturn(Optional.of(HTTP_LOCALHOST));
        when(systemPropertyHelper.getAAIUseClientCert()).thenReturn(Optional.of("cert"));
        when(systemPropertyHelper.getAAIVIDPasswd()).thenReturn(Optional.of("passwd"));
        when(systemPropertyHelper.getAAIVIDUsername()).thenReturn(Optional.of("user"));
        when(systemPropertyHelper.getEncodedCredentials()).thenReturn("someCredentials");
        when(systemPropertyHelper.getFullServicePath(Mockito.anyString())).thenReturn("http://localhost/path");
    }

}
