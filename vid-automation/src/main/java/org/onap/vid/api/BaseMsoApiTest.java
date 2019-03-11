package org.onap.vid.api;

import com.google.common.collect.ImmutableMap;
import org.json.JSONException;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.vid.model.mso.MsoResponseWrapper2;
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
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BaseMsoApiTest extends BaseApiTest {

    @BeforeClass
    public void login() {
        super.login();
    }

    protected void callMsoWithSimulatedErrorResponse(String expectationJsonFileName, ImmutableMap<String, Object> replacementsForJson, String targetUri, String basicRequestBody, int expectedErrorCode, String expectedResult, HttpMethod method) throws IOException {
        SimulatorApi.registerExpectation(expectationJsonFileName, replacementsForJson, RegistrationStrategy.CLEAR_THEN_SET);
        callMsoAndAssertError(targetUri, basicRequestBody, expectedErrorCode, expectedResult, method);
    }

    protected void callMsoWithSimulatedErrorResponse(BaseMSOPreset expectation, String targetUri, String basicRequestBody, int expectedErrorCode, String expectedResult, HttpMethod method) throws IOException {
        SimulatorApi.registerExpectationFromPreset(expectation, RegistrationStrategy.CLEAR_THEN_SET);
        callMsoAndAssertError(targetUri, basicRequestBody, expectedErrorCode, expectedResult, method);
    }

    protected void callMsoWithSimulatedErrorResponse(List<BasePreset> expectations, String targetUri, String basicRequestBody, int expectedErrorCode, String expectedResult, HttpMethod method) throws IOException {
        SimulatorApi.registerExpectationFromPresets(expectations, RegistrationStrategy.CLEAR_THEN_SET);
        callMsoAndAssertError(targetUri, basicRequestBody, expectedErrorCode, expectedResult, method);
    }

    private void callMsoAndAssertError(String targetUri, String basicRequestBody, int expectedErrorCode, String expectedResult, HttpMethod method) throws IOException {
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
        callMsoAndAssertSuccess(targetUri, requestBody, expectedStatusCode, expectedResult, method);
    }

    protected void callMsoWithFineRequest(BaseMSOPreset expectation, String targetUri, String requestBody, int expectedStatusCode, String expectedResult, HttpMethod method) throws IOException {
        SimulatorApi.registerExpectationFromPreset(expectation, RegistrationStrategy.CLEAR_THEN_SET);
        callMsoAndAssertSuccess(targetUri, requestBody, expectedStatusCode, expectedResult, method);
    }

    protected void callMsoWithFineRequest(List<BasePreset> expectations, String targetUri, String requestBody, int expectedStatusCode, String expectedResult, HttpMethod method) throws IOException {
        SimulatorApi.registerExpectationFromPresets(expectations, RegistrationStrategy.CLEAR_THEN_SET);
        callMsoAndAssertSuccess(targetUri, requestBody, expectedStatusCode, expectedResult, method);
    }

    private void callMsoAndAssertSuccess(String targetUri, String requestBody, int expectedStatusCode, String expectedResult, HttpMethod method) throws IOException {
        MsoResponseWrapper2 responseWrapper = callMsoForResponseWrapper(method, targetUri, requestBody);

        assertThat("Wrong propagated status from MSO", responseWrapper.getStatus(), is(expectedStatusCode));
        try {
            JSONAssert.assertEquals("Wrong propagated body from MSO", expectedResult, getCleanJsonString(responseWrapper.getEntity()), JSONCompareMode.NON_EXTENSIBLE);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

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
