package org.onap.vid.aai;


import org.mockito.Mock;
import org.onap.vid.aai.util.HttpClientMode;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.aai.util.ServletRequestHelper;
import org.onap.vid.aai.util.SystemPropertyHelper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.FROM_APP_ID_HEADER;
import static org.onap.vid.aai.AaiOverTLSClientInterface.HEADERS.TRANSACTION_ID_HEADER;
import static org.onap.vid.utils.Logging.REQUEST_ID_HEADER_KEY;

public class PombaRestInterfaceTest {
    private static final String UUID_REGEX = "[a-f0-9]{8}(-[a-f0-9]{4}){3}-[a-f0-9]{12}";
    private static final String SAMPLE_APP_ID = "vid";
    private static final String SAMPLE_URL = "sampleUrl";
    private static final String SAMPLE_PAYLOAD = "samplePayload";
    private static final Pattern UUID_PATTERN = Pattern.compile(UUID_REGEX);

    @Mock
    private HttpsAuthClient authClient;

    @Mock
    private ServletRequestHelper requestHelper;

    @Mock
    private SystemPropertyHelper systemPropertyHelper;

    @Mock
    private Client client;

    @Mock
    private WebTarget webTarget;

    @Mock
    private Invocation.Builder builder;

    @Mock
    private Response response;

    private PombaRestInterface pombaRestInterface;

    @BeforeMethod
    public void setUp() throws GeneralSecurityException, IOException {
        initMocks(this);

        when(requestHelper.extractOrGenerateRequestId()).thenReturn(UUID.randomUUID().toString());
        when(authClient.getClient(HttpClientMode.WITH_KEYSTORE)).thenReturn(client);
        setUpBuilder();
        pombaRestInterface = new PombaRestInterface(authClient, requestHelper, systemPropertyHelper);
    }


    @Test
    public void shouldProperlySendRequestWithAllRequiredHeaders() {
        Response actualResponse = pombaRestInterface.RestPost(SAMPLE_APP_ID, SAMPLE_URL, SAMPLE_PAYLOAD);

        assertThat(actualResponse).isEqualTo(response);

        verify(builder).accept(MediaType.APPLICATION_JSON);
        verify(builder).header(FROM_APP_ID_HEADER, SAMPLE_APP_ID);
        verify(builder).header(matches(TRANSACTION_ID_HEADER), matches(UUID_PATTERN));
        verify(builder).header(matches(REQUEST_ID_HEADER_KEY), matches(UUID_PATTERN));
        verify(builder).post(any(Entity.class));
    }

    @Test
    public void shouldReturnNullWhenExceptionWasRaised() {
        doThrow(new RuntimeException()).when(client).target(anyString());

        Response actualResponse = pombaRestInterface.RestPost(SAMPLE_APP_ID, SAMPLE_URL, SAMPLE_PAYLOAD);

        assertThat(actualResponse).isNull();
    }

    private void setUpBuilder() {
        when(client.target(anyString())).thenReturn(webTarget);
        when(webTarget.request()).thenReturn(builder);
        when(builder.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.header(anyString(), any())).thenReturn(builder);
        when(builder.post(any(Entity.class))).thenReturn(response);
        when(response.getStatusInfo()).thenReturn(Response.Status.OK);
    }
}