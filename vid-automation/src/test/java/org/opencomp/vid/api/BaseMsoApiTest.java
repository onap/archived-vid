package org.opencomp.vid.api;

import com.att.automation.common.report_portal_integration.annotations.Step;
import com.google.common.collect.ImmutableMap;
import org.json.JSONException;
import org.opencomp.vid.model.mso.MsoResponseWrapper2;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.services.SimulatorApi.RegistrationStrategy;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BaseMsoApiTest extends BaseApiTest {

    @BeforeClass
    public void login() {
        super.login();
    }

    protected void callMsoWithSimulatedErrorResponse(String expectationJsonFileName, ImmutableMap<String, Object> replacementsForJson, String targetUri, String basicRequestBody, int expectedErrorCode, String expectedResult, HttpMethod method) throws IOException {
        SimulatorApi.registerExpectation(expectationJsonFileName, replacementsForJson, RegistrationStrategy.CLEAR_THEN_SET);
        try {
            MsoResponseWrapper2 responseWrapper = callMsoForResponseWrapper(method, targetUri, basicRequestBody);

            assertThat("Wrong propagated status from MSO", responseWrapper.getStatus(), is(expectedErrorCode));
            assertThat("Wrong propagated body from MSO", getCleanJsonString(responseWrapper.getEntity()), is(expectedResult));
        }catch(HttpClientErrorException | HttpServerErrorException e) {
            assertThat("Wrong propagated status from MSO", e.getStatusCode().value(), is(expectedErrorCode));
        }
    }


    protected void callMsoWithFineRequest(String expectationJsonFileName, ImmutableMap<String, Object> replacementsForJson, String targetUri, String requestBody, int expectedStatusCode, String expectedResult, HttpMethod method) throws IOException {
        SimulatorApi.registerExpectation(expectationJsonFileName, replacementsForJson, RegistrationStrategy.CLEAR_THEN_SET);

        MsoResponseWrapper2 responseWrapper = callMsoForResponseWrapper(method, targetUri, requestBody);

        assertThat("Wrong propagated status from MSO", responseWrapper.getStatus(), is(expectedStatusCode));
        try {
            JSONAssert.assertEquals("Wrong propagated body from MSO", expectedResult, getCleanJsonString(responseWrapper.getEntity()), JSONCompareMode.NON_EXTENSIBLE);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Step(description = "method: ${method}, uri: ${uri}, body: ${body}")
    protected MsoResponseWrapper2 callMsoForResponseWrapper(HttpMethod method, String uri, String body) {
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
