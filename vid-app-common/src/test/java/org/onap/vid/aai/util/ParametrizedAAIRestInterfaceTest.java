package org.onap.vid.aai.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.exceptions.InvalidPropertyException;
import org.testng.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class ParametrizedAAIRestInterfaceTest {

    private static final String PATH = "path";
    public static final String HTTP_LOCALHOST = "http://localhost/";
    @Mock
    private LogHelper logHelper;
    @Mock
    private Client client;
    @Mock
    private WebTarget webTarget;
    @Mock
    private Invocation.Builder builder;
    @Mock
    private ServletRequestHelper servletRequestHelper;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private Response response;
    @Mock
    private SystemPropertyHelper systemPropertyHelper;

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
        when(servletRequestHelper.getServletRequest()).thenReturn(httpServletRequest);
    }

    public ParametrizedAAIRestInterfaceTest(Response.Status status) {
        this.status = status;
    }

    private AAIRestInterface createTestSubject() {
        return new AAIRestInterface("", logHelper, Optional.of(client), servletRequestHelper, systemPropertyHelper);
    }

    @Test
    public void testRestDeleteWithValidResponse() throws Exception {
        // given
        String methodName = "Delete";

        // when
        when(builder.delete()).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(status);
        boolean finalResponse = testSubject.Delete("", "", PATH);

        // then
        verify(builder).delete();
        verify(logHelper).logDebug(methodName + " start");
        verify(logHelper).logDebug("Resource " + HTTP_LOCALHOST + PATH + " deleted");
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
