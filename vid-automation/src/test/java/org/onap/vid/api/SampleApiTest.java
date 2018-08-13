package org.onap.vid.api;

import com.google.common.collect.ImmutableMap;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vid.automation.test.services.SimulatorApi;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SampleApiTest extends BaseApiTest {

    private static final String UUID = "927befca-e32c-4f7d-be8d-b4107e0ac31e";
    private static final String FILE_NAME = "a_file_with_request_setup.json";
    private static final String REQUEST_BODY = "{ \"foo\": \"bar\" }";

    @BeforeClass
    public void login() {
        super.login();
    }

    @Test(enabled = false)
    public void createWithSimplestBody() throws IOException, JSONException {
        final String expectedResult = "" +
                "{" +
                "  \"requestReferences\": {" +
                "     \"requestId\": \"rq1234d1-5a33-55df-13ab-12abad84e331\","+
                "     \"instanceId\": \"" + UUID + "\"" +
                "  }" +
                "}";

        callWithFineRequest(FILE_NAME,
                ImmutableMap.of("UUID", UUID),
                buildUri(), REQUEST_BODY,
                HttpStatus.ACCEPTED.value(), expectedResult);
    }

    private String buildUri() {
        return uri + "/foo";
    }

    private void callWithFineRequest(String expectationJsonFileName, ImmutableMap<String, Object> replacementsForJson, String targetUri, String requestBody, int expectedStatusCode, String expectedResult) throws JSONException {
        SimulatorApi.registerExpectation(expectationJsonFileName, replacementsForJson, SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);

        MyFooResponseType response = restTemplate.postForObject(targetUri, requestBody, MyFooResponseType.class);

        assertThat("Wrong propagated status from MSO", response.getStatus(), is(expectedStatusCode));
        JSONAssert.assertEquals("Wrong propagated body from MSO", expectedResult, getCleanJsonString(response.getEntity()), JSONCompareMode.NON_EXTENSIBLE);
    }


    private class MyFooResponseType {
        public int getStatus() {
            return 202;
        }

        public String getEntity() {
            return "baz";
        }
    }
}
