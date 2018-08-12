package vid.automation.test.utils;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.datatypes.UserCredentials;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;

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
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> loginRes = restTemplate.postForEntity(uri.toASCIIString() + "/login_external.htm", new HttpEntity<>("loginId=" + userCredentials.getUserId() + "&password=" + userCredentials.getPassword(), loginRequestHeaders), String.class);
        Assert.assertEquals("Login failed - wrong http status with user:" + userCredentials.getUserId() + " password:" + userCredentials.getPassword(), HttpStatus.FOUND, loginRes.getStatusCode());
        Assert.assertNull("Failed to login with user:" + userCredentials.getUserId() + " password:" + userCredentials.getPassword(), loginRes.getBody());
        HttpHeaders loginResponseHeaders = loginRes.getHeaders();
        List<String> cookie = loginResponseHeaders.get(HttpHeaders.SET_COOKIE);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.put(HttpHeaders.COOKIE, cookie);
        return headers;
    }

}
