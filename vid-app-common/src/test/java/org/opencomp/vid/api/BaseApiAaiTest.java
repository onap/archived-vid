package org.opencomp.vid.api;

import com.google.common.collect.ImmutableMap;
import org.codehaus.jackson.map.ObjectMapper;
import org.openecomp.vid.aai.AaiResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.opencomp.vid.api.simulator.SimulatorApi.RegistrationStrategy;
import static org.opencomp.vid.api.simulator.SimulatorApi.registerExpectation;

/**
 * Created by Oren on 11/1/17.
 */
public class BaseApiAaiTest extends BaseApiTest {
    protected final RestTemplate restTemplate = new RestTemplate();

    @BeforeClass
    public void login() {
        restTemplate.setInterceptors(singletonList(new CookieAndJsonHttpHeadersInterceptor()));
    }


    protected void callAaiWithSimulatedErrorResponse(String [] expectationJsonFileNames, ImmutableMap<String, Object> replacementsForJson, String targetUri, String basicRequestBody, int expectedErrorCode, String expectedResult, HttpMethod method) throws IOException, URISyntaxException {

        registerExpectation(expectationJsonFileNames, replacementsForJson);
        RequestEntity<String> request;
        switch (method) {
            case POST:
                //not supported yet
                break;

            case PUT:
               request = RequestEntity
                        .put(new URI(targetUri))
                        .body(basicRequestBody);
                try {
                   restTemplate.exchange(request, String.class);
                }
                catch(HttpStatusCodeException e) {
                    assertThat("Wrong propagated status from AAI", e.getStatusCode().value(), is(expectedErrorCode));
                }


            case GET:
                try {
                    ResponseEntity<String> responseWrapper = restTemplate.getForEntity(targetUri, String.class);
                    assertThat("Wrong propagated status from AAI", responseWrapper.getStatusCode().value(), is(expectedErrorCode));
                    assertThat("The reospnse is in the format of JSON",responseWrapper.getBody(),is(expectedResult));

                }
                catch(HttpClientErrorException | HttpServerErrorException e) {
                    assertThat("Wrong propagated status from AAI", e.getStatusCode().value(), is(expectedErrorCode));
                }
                break;
        }


    }
}
