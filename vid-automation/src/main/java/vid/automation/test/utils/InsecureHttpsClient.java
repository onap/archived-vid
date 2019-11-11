package vid.automation.test.utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class InsecureHttpsClient {

    public static RestTemplate newRestTemplate() {
        CloseableHttpClient insecureTLSHttpClient
            = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(insecureTLSHttpClient);
        return new RestTemplate(factory);
    }

    public static Client newJaxrsClient() {
        return ClientBuilder.newBuilder()
            .hostnameVerifier(NoopHostnameVerifier.INSTANCE)
            .build();
    }

}
