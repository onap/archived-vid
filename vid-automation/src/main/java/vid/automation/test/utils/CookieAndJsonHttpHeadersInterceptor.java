package vid.automation.test.utils;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestTemplate;

public class CookieAndJsonHttpHeadersInterceptor implements ClientHttpRequestInterceptor {
    private final HttpHeaders cookieAndJsonHttpHeaders;

    public CookieAndJsonHttpHeadersInterceptor(URI uri, UserCredentials userCredentials) {
        this.cookieAndJsonHttpHeaders = getCookieAndJsonHttpHeaders(uri, userCredentials);
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
        requestWrapper.getHeaders().putAll(cookieAndJsonHttpHeaders);

        return execution.execute(requestWrapper, body);
    }

    protected HttpHeaders getCookieAndJsonHttpHeaders(URI uri, UserCredentials userCredentials) {
        HttpHeaders loginRequestHeaders = new HttpHeaders();
        loginRequestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> requestEntity =
            new HttpEntity<>("loginId=" + userCredentials.getUserId() + "&password=" + userCredentials.getPassword(), loginRequestHeaders);

        RestTemplate restTemplate = InsecureHttpsClient.newRestTemplate();
        ResponseEntity<String> loginRes = restTemplate.postForEntity(uri.toASCIIString() + "/login_external.htm", requestEntity, String.class);
        Assert.assertEquals("Failed to login " + describeLoginRes(uri, requestEntity, loginRes), HttpStatus.FOUND, loginRes.getStatusCode());
        Assert.assertNull("Failed to login " + describeLoginRes(uri, requestEntity, loginRes), loginRes.getBody());
        HttpHeaders loginResponseHeaders = loginRes.getHeaders();
        List<String> cookie = loginResponseHeaders.get(HttpHeaders.SET_COOKIE);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.put(HttpHeaders.COOKIE, cookie);
        return headers;
    }

    private String describeLoginRes(URI uri, HttpEntity<String> requestEntity, ResponseEntity<String> loginRes) {
        return ""
            + "Request was: "
            + uri.toASCIIString() + " POST " + requestEntity
            + "And response is: "
            + loginRes;
    }

}
