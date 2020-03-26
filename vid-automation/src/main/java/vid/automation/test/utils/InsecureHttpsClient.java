package vid.automation.test.utils;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class InsecureHttpsClient {

    public static RestTemplate newRestTemplate() {

        CloseableHttpClient insecureTLSHttpClient = HttpClients.custom()
            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
            .setSSLContext(trustAllCertificates())
            .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(insecureTLSHttpClient);
        return new RestTemplate(factory);
    }

    private static SSLContext trustAllCertificates() {
        try {
            return new SSLContextBuilder()
                .loadTrustMaterial(null, TrustAllStrategy.INSTANCE)
                .build();
        } catch (Exception e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    public static Client newJaxrsClient() {
        return ClientBuilder.newBuilder()
            .hostnameVerifier(NoopHostnameVerifier.INSTANCE)
            .sslContext(trustAllCertificates())
            .build();
    }

}
