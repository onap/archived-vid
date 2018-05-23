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


import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.exceptions.HttpClientBuilderException;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.security.GeneralSecurityException;

import static org.onap.vid.aai.util.HttpClientMode.WITH_KEYSTORE;

/**
 * The Class HttpsAuthClient.
 */
public class HttpsAuthClient {

    private static final String SSL_TRUST_STORE = "javax.net.ssl.trustStore";
    private static final String SSL_TRUST_STORE_PASS_WORD = "javax.net.ssl.trustStorePassword";

    private final SystemPropertyHelper systemPropertyHelper;
    private final SSLContextProvider sslContextProvider;

    public HttpsAuthClient(String certFilePath, SystemPropertyHelper systemPropertyHelper, SSLContextProvider sslContextProvider) {
        this.certFilePath = certFilePath;
        this.systemPropertyHelper = systemPropertyHelper;
        this.sslContextProvider = sslContextProvider;
    }

    private final String certFilePath;

    /** The logger. */
    static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(HttpsAuthClient.class);


    /**
     * Gets the client.
     *
     * @return the client
     */
    public Client getClient(HttpClientMode mode) throws GeneralSecurityException, IOException {
        ClientConfig config = prepareClientConfig(mode);

        try {
            setSystemProperties();

            ignoreHostname();

            return systemPropertyHelper.isClientCertEnabled() ?
                    getTrustedClient(config, getKeystorePath(), systemPropertyHelper.getDecryptedKeystorePassword(), mode)
                    : getUntrustedClient(config);

        } catch (Exception e) {
            logger.debug(EELFLoggerDelegate.debugLogger, "Error setting up config", e);
            throw e;
        }

    }

    private void ignoreHostname() {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

    private Client getUntrustedClient(ClientConfig config) {
        return ClientBuilder.newBuilder().withConfig(config).build().register(CustomJacksonJaxBJsonProvider.class);
    }

    private Client getTrustedClient(ClientConfig config, String keystorePath, String keystorePassword, HttpClientMode httpClientMode) throws HttpClientBuilderException {
        return ClientBuilder.newBuilder()
                .sslContext(sslContextProvider.getSslContext(keystorePath, keystorePassword, httpClientMode))
                .hostnameVerifier((s, sslSession) -> true)
                .withConfig(config)
                .build()
                .register(CustomJacksonJaxBJsonProvider.class);
    }

    private String getKeystorePath() {
        return getCertificatesPath() + FileSystems.getDefault().getSeparator() + systemPropertyHelper.getAAIKeystoreFilename();
    }

    private void setSystemProperties() {
        System.setProperty(SSL_TRUST_STORE, getCertificatesPath() + FileSystems.getDefault().getSeparator() +
                systemPropertyHelper.getAAITruststoreFilename().orElse(""));
        System.setProperty(SSL_TRUST_STORE_PASS_WORD, systemPropertyHelper.getDecryptedTruststorePassword());
    }

    private ClientConfig prepareClientConfig(HttpClientMode mode) {
        ClientConfig config = new ClientConfig();
        if (mode.equals(WITH_KEYSTORE)) {
            config.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, Boolean.TRUE);
            config.connectorProvider(new HttpUrlConnectorProvider().useSetMethodWorkaround());
        }
        return config;
    }

    private String getCertificatesPath() {
        return certFilePath;
    }

}
