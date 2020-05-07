package org.onap.vid.api;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonStringEquals;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static vid.automation.test.services.SimulatorApi.registerExpectation;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.annotations.BeforeClass;
import vid.automation.test.services.CategoryParamsService;
import vid.automation.test.services.SimulatorApi;

public class BaseApiAaiTest extends BaseApiTest {

    protected static final CategoryParamsService categoryParamsService = new CategoryParamsService();

    @BeforeClass
    public void login() {
        super.login();
    }


    protected void callAaiWithSimulatedErrorResponse(String [] expectationJsonFileNames, ImmutableMap<String, Object> replacementsForJson, String targetUri, String basicRequestBody, int expectedErrorCode, String expectedResult, HttpMethod method) throws IOException, URISyntaxException {

        registerExpectation(expectationJsonFileNames, replacementsForJson, SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
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
                    assertThat("The response is in the format of JSON", responseWrapper.getBody(),
                            either(is(expectedResult)).or(jsonStringEquals(expectedResult)));
                }
                catch(HttpClientErrorException | HttpServerErrorException e) {
                    assertThat("Wrong propagated status from AAI", e.getStatusCode().value(), is(expectedErrorCode));
                }
                break;
        }


    }
}
