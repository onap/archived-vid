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

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.exceptions.HttpClientBuilderException;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

public class SSLContextProvider {

    private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SSLContextProvider.class);

    public SSLContext getSslContext(String keystorePath, String keystorePassword, HttpClientMode httpClientMode){
        try {
            final SSLContext ctx = SSLContext.getInstance("TLSv1.2");
            KeyManager[] keyManagers = getKeyManagerFactory(keystorePath, keystorePassword, httpClientMode);
            ctx.init(keyManagers, getTrustManager(httpClientMode), null);
            return ctx;
        } catch (IOException | GeneralSecurityException e) {
            logger.debug(EELFLoggerDelegate.debugLogger, "Error setting up ssl context.");
            throw new HttpClientBuilderException(e);
        }
    }

    /**
     * @param keystorePath
     * @param keystorePassword - in clear
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private KeyManager[] getKeyManagerFactory(String keystorePath, String keystorePassword, HttpClientMode httpClientMode) throws IOException, GeneralSecurityException {
        switch (httpClientMode) {
            case WITH_KEYSTORE:
                final KeyManagerFactory kmf;
                try (FileInputStream fin = new FileInputStream(keystorePath)) {
                    kmf = KeyManagerFactory.getInstance("SunX509");
                    KeyStore ks = KeyStore.getInstance("PKCS12");
                    char[] pwd = keystorePassword.toCharArray();
                    ks.load(fin, pwd);
                    kmf.init(ks, pwd);
                } catch (Exception e) {
                    logger.debug(EELFLoggerDelegate.debugLogger, "Error setting up kmf");
                    logger.error(EELFLoggerDelegate.errorLogger, "Error setting up kmf (keystore path: {}, deobfuascated keystore password: {})", keystorePath, keystorePassword, e);
                    throw e;
                }
                return kmf.getKeyManagers();

            case WITHOUT_KEYSTORE:
                return null;

            default:
                logger.debug(EELFLoggerDelegate.debugLogger, "Error setting up getKeyManagerFactory. HttpClientMode is " + httpClientMode);
                throw new IllegalStateException("Error setting up getKeyManagerFactory. HttpClientMode is " + httpClientMode);
        }
    }

    private TrustManager[] getTrustManager(HttpClientMode httpClientMode) {
        //Creating a trustManager that will accept all certificates.
        //TODO - remove this one the POMBA certificate is added to the tomcat_keystore file
        TrustManager[] trustAllCerts = null;
        if (httpClientMode == HttpClientMode.UNSECURE) {

            trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    // trust all
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    // trust all
                }
            }};
        }
        return trustAllCerts;
    }

}
