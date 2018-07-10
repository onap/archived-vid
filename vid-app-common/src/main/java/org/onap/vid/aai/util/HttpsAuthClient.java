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


import org.eclipse.jetty.util.security.Password;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;

import javax.net.ssl.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * The Class HttpsAuthClient.
 */
public class HttpsAuthClient {


    public HttpsAuthClient(String certFilePath) {
        this.certFilePath = certFilePath;
    }

    private final String certFilePath;

    /** The logger. */
    static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(HttpsAuthClient.class);

    /**
     * Gets the client.
     *
     * @return the client
     * @throws KeyManagementException the key management exception
     */
    public Client getClient(HttpClientMode mode) throws GeneralSecurityException, IOException {
        ClientConfig config = new ClientConfig();
        SSLContext ctx;

        try {
            String truststorePath = getCertificatesPath() + org.onap.vid.aai.util.AAIProperties.FILESEPARTOR + SystemProperties.getProperty(org.onap.vid.aai.util.AAIProperties.AAI_TRUSTSTORE_FILENAME);
            String truststorePassword = SystemProperties.getProperty(org.onap.vid.aai.util.AAIProperties.AAI_TRUSTSTORE_PASSWD_X);
            String decryptedTruststorePassword = Password.deobfuscate(truststorePassword);

            System.setProperty("javax.net.ssl.trustStore", truststorePath);
            System.setProperty("javax.net.ssl.trustStorePassword", decryptedTruststorePassword);

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            });
            ctx = SSLContext.getInstance("TLSv1.2");
            KeyManager[] keyManagers = null;
            TrustManager[] trustManagers = getTrustManager(mode);

            switch (mode) {
                case WITH_KEYSTORE:
                    String aaiKeystorePath = getCertificatesPath() + org.onap.vid.aai.util.AAIProperties.FILESEPARTOR + SystemProperties.getProperty(org.onap.vid.aai.util.AAIProperties.AAI_KEYSTORE_FILENAME);
                    String aaiKeystorePassword = SystemProperties.getProperty(org.onap.vid.aai.util.AAIProperties.AAI_KEYSTORE_PASSWD_X);
                    config.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, Boolean.TRUE);
                    config.connectorProvider(new HttpUrlConnectorProvider().useSetMethodWorkaround());
                    KeyManagerFactory kmf = getKeyManagerFactory(aaiKeystorePath, aaiKeystorePassword);
                    keyManagers = kmf.getKeyManagers();
                    break;

                case WITHOUT_KEYSTORE:
                    config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
                    break;

                default:
                    logger.debug(EELFLoggerDelegate.debugLogger, "Error setting up config. HttpClientMode is " + mode);
            }

            ctx.init(keyManagers, trustManagers, null);
            return ClientBuilder.newBuilder()
                    .sslContext(ctx)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String s, SSLSession sslSession) {
                            return true;
                        }
                    }).withConfig(config)
                    .build()
                    .register(org.onap.vid.aai.util.CustomJacksonJaxBJsonProvider.class);
        } catch (Exception e) {
            logger.debug(EELFLoggerDelegate.debugLogger, "Error setting up config", e);
            throw e;
        }

    }

    /**
     * @param aaiKeystorePath
     * @param aaiKeystorePassword - in OBF format
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     */
    private KeyManagerFactory getKeyManagerFactory(String aaiKeystorePath, String aaiKeystorePassword) throws IOException, GeneralSecurityException {
        String aaiDecryptedKeystorePassword = Password.deobfuscate(aaiKeystorePassword);
        KeyManagerFactory kmf = null;
        try (FileInputStream fin = new FileInputStream(aaiKeystorePath)) {
            kmf = KeyManagerFactory.getInstance("SunX509");
            KeyStore ks = KeyStore.getInstance("PKCS12");
            char[] pwd = aaiDecryptedKeystorePassword.toCharArray();
            ks.load(fin, pwd);
            kmf.init(ks, pwd);
        } catch (Exception e) {
            logger.debug(EELFLoggerDelegate.debugLogger, "Error setting up kmf");
            logger.error(EELFLoggerDelegate.errorLogger, "Error setting up kmf (keystore path: {}, obfuascated keystore password: {})", aaiKeystorePath, aaiKeystorePassword, e);
            throw e;
        }
        return kmf;
    }

    private String getCertificatesPath() {
        return certFilePath;
    }

    private TrustManager[] getTrustManager(HttpClientMode httpClientMode) {
        //Creating a trustManager that will accept all certificates.
        //TODO - remove this one the POMBA certificate is added to the tomcat_keystore file
        TrustManager[] trustAllCerts = null;
        if (httpClientMode == HttpClientMode.UNSECURE) {

            trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
        }
        return trustAllCerts;
    }


}
