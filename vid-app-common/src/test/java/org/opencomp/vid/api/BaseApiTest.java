package org.opencomp.vid.api;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeClass;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class BaseApiTest {
    @SuppressWarnings("WeakerAccess")
    protected URI uri;
    @SuppressWarnings("WeakerAccess")
    protected ObjectMapper objectMapper = new ObjectMapper();
    @SuppressWarnings("WeakerAccess")
    protected Client client;
    protected Random random;
    private final RestTemplate restTemplate = new RestTemplate();

    @BeforeClass
    public void init() {
        String host = System.getProperty("VID_HOST", "127.0.0.1");
        Integer port = Integer.valueOf(System.getProperty("VID_PORT", "8080"));
        uri = new JerseyUriBuilder().host(host).port(port).scheme("http").path("vid").build();
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        client = ClientBuilder.newClient();
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        random = new Random(System.currentTimeMillis());
    }

    protected HttpHeaders getCookieAndJsonHttpHeaders() {
        final Properties configProp = new Properties();
        try {
            InputStream input = ClassLoader.getSystemResourceAsStream("objectconfig.properties");
            configProp.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders loginRequestHeaders = new HttpHeaders();
        loginRequestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String loginId = configProp.getProperty("test.loginId", "1");
        String loginPassword = configProp.getProperty("test.loginPassword", "1");
        ResponseEntity<String> loginRes = restTemplate.postForEntity(uri.toASCIIString() + "/login_external.htm", new HttpEntity<>("loginId=" + loginId + "&password=" + loginPassword, loginRequestHeaders), String.class);
        HttpHeaders loginResponseHeaders = loginRes.getHeaders();
        List<String> cookie = loginResponseHeaders.get(HttpHeaders.SET_COOKIE);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.put(HttpHeaders.COOKIE, cookie);
        return headers;
    }

    class CookieAndJsonHttpHeadersInterceptor implements ClientHttpRequestInterceptor {
        private final HttpHeaders cookieAndJsonHttpHeaders;

        public CookieAndJsonHttpHeadersInterceptor() {
            this.cookieAndJsonHttpHeaders = getCookieAndJsonHttpHeaders();
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {

            HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
            requestWrapper.getHeaders().putAll(cookieAndJsonHttpHeaders);

            return execution.execute(requestWrapper, body);
        }
    }


}
