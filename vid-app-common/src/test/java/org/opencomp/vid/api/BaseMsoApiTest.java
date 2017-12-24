package org.opencomp.vid.api;

import com.google.common.collect.ImmutableMap;
import org.opencomp.vid.api.simulator.SimulatorApi;
import org.opencomp.vid.api.simulator.SimulatorApi.RegistrationStrategy;
import org.openecomp.vid.mso.MsoResponseWrapper2;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.lang.reflect.Method;

import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BaseMsoApiTest extends BaseApiTest {

    @BeforeClass
    public void login() {
        restTemplate.setInterceptors(singletonList(new CookieAndJsonHttpHeadersInterceptor()));
    }

    protected void callMsoWithSimulatedErrorResponse(String expectationJsonFileName, ImmutableMap<String, Object> replacementsForJson, String targetUri, String basicRequestBody, int expectedErrorCode, String expectedResult, HttpMethod method) throws IOException {
        SimulatorApi.registerExpectation(expectationJsonFileName, replacementsForJson, RegistrationStrategy.CLEAR_THEN_SET);

        MsoResponseWrapper2 responseWrapper = callMsoForResponseWrapper(method, targetUri, basicRequestBody);

        assertThat("Wrong propagated status from MSO", responseWrapper.getStatus(), is(expectedErrorCode));
        assertThat("Wrong propagated body from MSO", getCleanJsonString(responseWrapper.getEntity()), is(expectedResult));
    }


    protected void callMsoWithFineRequest(String expectationJsonFileName, ImmutableMap<String, Object> replacementsForJson, String targetUri, String requestBody, int expectedStatusCode, String expectedResult, HttpMethod method) throws IOException {
        SimulatorApi.registerExpectation(expectationJsonFileName, replacementsForJson, RegistrationStrategy.CLEAR_THEN_SET);

        MsoResponseWrapper2 responseWrapper = callMsoForResponseWrapper(method, targetUri, requestBody);

        assertThat("Wrong propagated status from MSO", responseWrapper.getStatus(), is(expectedStatusCode));
        JSONAssert.assertEquals("Wrong propagated body from MSO", expectedResult, getCleanJsonString(responseWrapper.getEntity()), JSONCompareMode.NON_EXTENSIBLE);
    }

    private MsoResponseWrapper2 callMsoForResponseWrapper(HttpMethod method, String uri, String body) {
        MsoResponseWrapper2 responseWrapper;
        switch (method) {
            case POST:
                responseWrapper = restTemplate.postForObject(uri, body, MsoResponseWrapper2.class);
                break;
            case GET:
            default:
                responseWrapper = restTemplate.getForObject(uri, MsoResponseWrapper2.class);
                break;
        }

        System.out.println("response: " + responseWrapper);

        return responseWrapper;
    }

    @DataProvider
    public static Object[][] errorCodes(Method test) {
        return new Object[][]{
                {500},{505}, {400}, {401}, {404}, {405}
        };
    }
}
