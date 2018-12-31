/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 Nokia. All rights reserved.
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
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.vid.aai.exceptions.HttpClientBuilderException;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.togglz.core.manager.FeatureManager;

import javax.net.ssl.SSLContext;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HttpsAuthClientTest {
    @Mock
    private SystemPropertyHelper systemPropertyHelper;
    @Mock
    private SSLContextProvider sslContextProvider;
    @Mock
    private SSLContext sslContext;

    public static final String CERT_FILE_PATH = "any_path";

    private HttpsAuthClient createTestSubject() {
        return new HttpsAuthClient(CERT_FILE_PATH, systemPropertyHelper, sslContextProvider, mock(FeatureManager.class));
    }

    @Before
    public void setUp() throws Exception {
        when(systemPropertyHelper.getAAITruststoreFilename()).thenReturn(Optional.of("filename"));
        when(systemPropertyHelper.getDecryptedKeystorePassword()).thenReturn("password");
        when(systemPropertyHelper.getDecryptedTruststorePassword()).thenReturn("password");
    }

    @Test(expected = HttpClientBuilderException.class)
    public void testHttpClientBuilderExceptionOnGetClient() throws Exception {
        //when
        when(systemPropertyHelper.isClientCertEnabled()).thenReturn(true);
        when(sslContextProvider.getSslContext(anyString(), anyString(), any())).thenThrow(new HttpClientBuilderException(new GenericUncheckedException("msg")));
        createTestSubject().getClient(HttpClientMode.WITH_KEYSTORE);
    }

    @Test
    public void testGetSecuredClient() throws Exception {
        // when
        when(systemPropertyHelper.isClientCertEnabled()).thenReturn(true);
        when(sslContextProvider.getSslContext(anyString(), anyString(), any())).thenReturn(sslContext);
        createTestSubject().getClient(HttpClientMode.WITH_KEYSTORE);

        //then
        verify(sslContextProvider).getSslContext(anyString(), anyString(), any());
    }

    @Test
    public void testGetUnsecuredClient() throws Exception {
        // when
        when(systemPropertyHelper.isClientCertEnabled()).thenReturn(false);
        createTestSubject().getClient(HttpClientMode.WITH_KEYSTORE);

        //then
        verify(sslContextProvider, never()).getSslContext(anyString(), anyString(), any());
    }
}