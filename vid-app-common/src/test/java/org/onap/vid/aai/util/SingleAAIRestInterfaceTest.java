package org.onap.vid.aai.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.vid.aai.exceptions.InvalidPropertyException;
import org.testng.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.*;
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
    public void testSetRestSrvrBaseURLWithNullValue() throws Exception {
        testSubject.SetRestSrvrBaseURL(null);
    }

    @Test
    public void testSetRestSrvrBaseURL() throws Exception {
        String baseUrl = "anything";
        testSubject.SetRestSrvrBaseURL(baseUrl);
        Assert.assertEquals(testSubject.getRestSrvrBaseURL(), baseUrl);
    }

    @Test
    public void testRestJsonPutWithResponse200() throws Exception {
        // given
        String methodName = "RestPut";
        String payload = "{\"id\": 1}";
        Entity<String> entity = Entity.entity(payload, MediaType.APPLICATION_JSON);

        // when
        when(builder.put(Mockito.any(Entity.class))).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(OK);
        Response finalResponse = testSubject.RestPut("", PATH, payload, false);

        // then
        verify(builder).put(entity);
        Assert.assertEquals(response, finalResponse);
    }

    @Test
    public void testFailedRestJsonPut() throws Exception {
        // given
        String methodName = "RestPut";
        String payload = "{\"id\": 1}";
        Entity<String> entity = Entity.entity(payload, MediaType.APPLICATION_JSON);

        // when
        when(builder.put(Mockito.any(Entity.class))).thenThrow(new RuntimeException());
        Response finalResponse = testSubject.RestPut("", PATH, payload, false);

        // then
        verify(builder).put(entity);
        Assert.assertEquals(finalResponse, null);
    }

    @Test
    public void testRestJsonPutWithResponse400() throws Exception {
        // given
        String methodName = "RestPut";
        String payload = "{\"id\": 1}";
        Entity<String> entity = Entity.entity(payload, MediaType.APPLICATION_JSON);

        // when
        when(builder.put(Mockito.any(Entity.class))).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(BAD_REQUEST);
        when(response.getStatus()).thenReturn(BAD_REQUEST.getStatusCode());
        Response finalResponse = testSubject.RestPut("", PATH, payload, false);

        // then
        verify(builder).put(entity);
        Assert.assertEquals(response, finalResponse);
    }

    @Test
    public void testRestPostWithResponse200() throws Exception {
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
    public void testRestPostWithResponse400() throws Exception {
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
    public void testFailedRestPost() throws Exception {
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
    public void testRestDeleteWithResponse400() throws Exception {
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
    public void testRestDeleteWithResponse404() throws Exception {
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
    public void testFailedRestDelete() throws Exception {
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
    public void testRestJsonGetWithResponse200() throws Exception {
        // given
        String methodName = "RestGet";

        // when
        when(builder.get()).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(OK);
        Response finalResponse = testSubject.RestGet("", "", PATH, false).getResponse();

        // then
        Assert.assertEquals(response, finalResponse);
    }

    @Test
    public void testRestJsonGetWithResponse400() throws Exception {
        // given
        String methodName = "RestGet";

        // when
        when(builder.get()).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(BAD_REQUEST);
        when(response.getStatus()).thenReturn(BAD_REQUEST.getStatusCode());
        Response finalResponse = testSubject.RestGet("", "", PATH, false).getResponse();

        // then
        Assert.assertEquals(response, finalResponse);
    }

    @Test
    public void testFailedRestGet() throws Exception {
        // given
        String methodName = "RestGet";

        // when
        when(builder.get()).thenThrow(new RuntimeException());
        Response finalResponse = testSubject.RestGet("", "", PATH, false).getResponse();

        // then
        Assert.assertEquals(finalResponse, null);
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