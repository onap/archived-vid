/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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


import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.onap.vid.aai.exceptions.HttpClientBuilderException;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.nio.file.FileSystems;

/**
 * The Class HttpsAuthClient.
 */
public class HttpsAuthClient {

    private static final String SSL_TRUST_STORE = "javax.net.ssl.trustStore";
    private static final String SSL_TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";

    private final SystemPropertyHelper systemPropertyHelper;
    private final SSLContextProvider sslContextProvider;

    public HttpsAuthClient(SystemPropertyHelper systemPropertyHelper, SSLContextProvider sslContextProvider) {
        this.systemPropertyHelper = systemPropertyHelper;
        this.sslContextProvider = sslContextProvider;
    }

    /**
     * Gets the client.
     *
     * @param certFilePath the cert file path
     * @throws HttpClientBuilderException some problem was found during build of ssl context
     * @return the client
     */
    public Client getClient(String certFilePath) throws HttpClientBuilderException {
        ClientConfig config = prepareClientConfig();
        boolean useClientCert = Boolean.valueOf(systemPropertyHelper.getAAIUseClientCert().orElse("false"));

        setSystemProperties(certFilePath);
        ignoreHostname();

        return useClientCert ? getTrustedClient(config, getKeystorePath(certFilePath),
                systemPropertyHelper.getEncodedKeystorePassword()) : getUntrustedClient(config);
    }

    private void ignoreHostname() {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

    private Client getUntrustedClient(ClientConfig config) {
        return ClientBuilder.newBuilder().withConfig(config).build().register(CustomJacksonJaxBJsonProvider.class);
    }

    private Client getTrustedClient(ClientConfig config, String keystore_path, String decrypted_keystore_password) throws HttpClientBuilderException {
        return ClientBuilder.newBuilder()
                .sslContext(sslContextProvider.getSslContext(keystore_path, decrypted_keystore_password))
                .withConfig(config)
                .build()
                .register(CustomJacksonJaxBJsonProvider.class);
    }

    private String getKeystorePath(String certFilePath) {
        return certFilePath + FileSystems.getDefault().getSeparator() + systemPropertyHelper.getAAIKeystoreFilename();
    }

    private void setSystemProperties(String certFilePath) {
        System.setProperty(SSL_TRUST_STORE, certFilePath + FileSystems.getDefault().getSeparator() +
                systemPropertyHelper.getAAITruststoreFilename().orElse(""));
        System.setProperty(SSL_TRUST_STORE_PASSWORD, systemPropertyHelper.getEncodedTruststorePassword());
    }

    private ClientConfig prepareClientConfig() {
        ClientConfig config = new ClientConfig();
        config.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, Boolean.TRUE);
        config.connectorProvider(new HttpUrlConnectorProvider().useSetMethodWorkaround());
        return config;
    }
}
