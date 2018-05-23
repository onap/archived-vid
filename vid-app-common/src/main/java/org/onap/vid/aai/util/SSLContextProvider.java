package org.onap.vid.aai.util;

import org.onap.vid.aai.exceptions.HttpClientBuilderException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class SSLContextProvider {

    private final LogHelper logHelper;

    public SSLContextProvider(LogHelper logHelper) {
        this.logHelper = logHelper;
    }

    public SSLContext getSslContext(String keystore_path, String decrypted_keystore_password) throws HttpClientBuilderException {
        try {
            final SSLContext ctx = SSLContext.getInstance("TLS");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(keystore_path), decrypted_keystore_password.toCharArray());
            kmf.init(ks, decrypted_keystore_password.toCharArray());
            ctx.init(kmf.getKeyManagers(), null, null);
            return ctx;
        } catch (IOException | CertificateException | UnrecoverableKeyException | NoSuchAlgorithmException
                | KeyStoreException | KeyManagementException e) {
            logHelper.logDebug("Error setting up ssl context.");
            throw new HttpClientBuilderException(e);
        }
    }

}
