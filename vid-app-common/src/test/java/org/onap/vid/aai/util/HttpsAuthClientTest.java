package org.onap.vid.aai.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.vid.aai.exceptions.HttpClientBuilderException;

import javax.net.ssl.SSLContext;
import java.util.Optional;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpsAuthClientTest {
    @Mock
    private SystemPropertyHelper systemPropertyHelper;
    @Mock
    private SSLContextProvider sslContextProvider;
    @Mock
    private SSLContext sslContext;

    public static final String CERT_FILE_PATH = "any_path";

    /*
    TO BE IMPLEMENTED
    
    private HttpsAuthClient createTestSubject() {
        return new HttpsAuthClient(systemPropertyHelper, sslContextProvider);
    }

    @Before
    public void setUp() throws Exception {
        when(systemPropertyHelper.getAAITruststoreFilename()).thenReturn(Optional.of("filename"));
        when(systemPropertyHelper.getEncodedTruststorePassword()).thenReturn("password");
    }

    @Test(expected = HttpClientBuilderException.class)
    public void testHttpClientBuilderExceptionOnGetClient() throws HttpClientBuilderException {
        //when
        when(systemPropertyHelper.getAAIUseClientCert()).thenReturn(Optional.of("true"));
        when(sslContextProvider.getSslContext(anyString(), anyString())).thenThrow(new HttpClientBuilderException());
        createTestSubject().getClient("nonExistingFile");
    }

    @Test
    public void testGetSecuredClient() throws Exception {
        // when
        when(systemPropertyHelper.getAAIUseClientCert()).thenReturn(Optional.of("true"));
        when(sslContextProvider.getSslContext(anyString(), anyString())).thenReturn(sslContext);
        createTestSubject().getClient(CERT_FILE_PATH);

        //then
        verify(sslContextProvider).getSslContext(anyString(), anyString());
    }

    @Test
    public void testGetUnsecuredClient() throws Exception {
        // when
        when(systemPropertyHelper.getAAIUseClientCert()).thenReturn(Optional.of("false"));
        when(sslContextProvider.getSslContext(anyString(), anyString())).thenReturn(sslContext);
        createTestSubject().getClient(CERT_FILE_PATH);

        //then
        verify(sslContextProvider, never()).getSslContext(anyString(), anyString());
    }
    */
}