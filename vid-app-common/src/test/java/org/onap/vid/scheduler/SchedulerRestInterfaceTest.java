/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 - 2019 Nokia Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */


package org.onap.vid.scheduler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xebialabs.restito.semantics.Action;
import java.util.HashMap;
import java.util.Map;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.vid.mso.RestObject;
import org.onap.vid.testUtils.StubServerUtil;
import org.onap.vid.utils.Logging;
import org.testng.annotations.AfterMethod;


@RunWith(MockitoJUnitRunner.class)
public class SchedulerRestInterfaceTest {

    private static final String SAMPLE_USERNAME = "sample";
    private static final String SAMPLE_PASSWORD = "paS$w0Rd";
    private static final String SAMPLE_SCHEDULER_SERVER_URL = "http://localhost";
    private static final String SAMPLE_SOURCE_ID = "AAI";
    private static final JSONParser JSON_PARSER = new JSONParser();
    private static final String RESPONSE_CONTENT = "\"schedules\": \"SAMPLE STRING\"";
    private static final String ERROR_RESPONSE = "\"error\": \"Internal server error!\"";
    private static Map<String, String> DUMMY_SYSTEM_PROPERTIES = new HashMap<String, String>() {{
        put(SchedulerProperties.SCHEDULER_USER_NAME_VAL, SAMPLE_USERNAME);
        put(SchedulerProperties.SCHEDULER_PASSWORD_VAL, SAMPLE_PASSWORD);
        put(SchedulerProperties.SCHEDULER_SERVER_URL_VAL, SAMPLE_SCHEDULER_SERVER_URL);
    }};
    private static StubServerUtil serverUtil;
    private static SchedulerRestInterface schedulerInterface = new SchedulerRestInterface((key) -> DUMMY_SYSTEM_PROPERTIES.get(key), mock(Logging.class));

    @BeforeClass
    public static void setUpClass() {
        serverUtil = new StubServerUtil();
        serverUtil.runServer();
    }

    @AfterClass
    public static void tearDownClass() {
        serverUtil.stopServer();
    }


    @AfterMethod
    public void tearDown() {
        serverUtil.stopServer();
    }

    @Test
    public void testShouldGetOKWhenStringIsExpected() throws JsonProcessingException, ParseException {
        prepareEnvForTest();
        RestObject<String> sampleRestObj = new RestObject<>();
        serverUtil.prepareGetCall("/test", RESPONSE_CONTENT, Action.ok());

        schedulerInterface.Get("", "", sampleRestObj);

        assertResponseHasExpectedBodyAndStatus(sampleRestObj, RESPONSE_CONTENT, 200);
    }

    @Test(expected = org.onap.vid.aai.ExceptionWithRequestInfo.class)
    public void shouldRaiseExceptionWhenErrorOccursDuringGet() throws JsonProcessingException {
        prepareEnvForTest();
        RestObject<String> sampleRestObj = new RestObject<>();

        serverUtil.prepareGetCall("/test", ERROR_RESPONSE, Action.status(HttpStatus.INTERNAL_SERVER_ERROR_500));

        schedulerInterface.Get("", "", sampleRestObj);
    }

    @Test
    public void shouldDeleteResourceSuccessfully() throws JsonProcessingException, ParseException {
        prepareEnvForTest();
        RestObject<String> sampleRestObj = new RestObject<>();
        serverUtil.prepareDeleteCall("/test", RESPONSE_CONTENT, Action.ok());

        schedulerInterface.Delete("", SAMPLE_SOURCE_ID, "", sampleRestObj);

        assertResponseHasExpectedBodyAndStatus(sampleRestObj, RESPONSE_CONTENT, 200);
    }

    @Test
    public void shouldRaiseExceptionWhenErrorOccursDuringDelete() throws JsonProcessingException, ParseException {
        prepareEnvForTest();
        RestObject<String> sampleRestObj = new RestObject<>();
        serverUtil.prepareDeleteCall("/test", ERROR_RESPONSE, Action.status(HttpStatus.INTERNAL_SERVER_ERROR_500));

        schedulerInterface.Delete("", SAMPLE_SOURCE_ID, "", sampleRestObj);

        assertResponseHasExpectedBodyAndStatus(sampleRestObj, ERROR_RESPONSE, 500);
    }


    private void assertResponseHasExpectedBodyAndStatus(RestObject<String> sampleRestObj, String expectedResponse, int expectedStatusCode) throws ParseException {
        Object parsedResult = JSON_PARSER.parse(sampleRestObj.get());

        assertThat(sampleRestObj.getStatusCode()).isEqualTo(expectedStatusCode);
        assertThat(parsedResult).isInstanceOf(String.class).isEqualTo(expectedResponse);

    }

    private void prepareEnvForTest() {
        String targetUrl = serverUtil.constructTargetUrl("http", "test");
        DUMMY_SYSTEM_PROPERTIES.put(SchedulerProperties.SCHEDULER_SERVER_URL_VAL, targetUrl);
    }
}
