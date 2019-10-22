package org.onap.vid.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.testng.AssertJUnit.assertEquals;

import com.google.common.collect.ImmutableList;
import java.util.UUID;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.onap.vid.more.LoggerFormatTest;
import org.onap.vid.more.LoggerFormatTest.LogName;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vid.automation.test.services.SimulatorApi;

public class AsyncInfraApiTest extends BaseApiTest {

    public static final String API_URL = "asyncForTests";

    @BeforeClass
    public void login() {
        super.login();
    }

    @Test
    public void testGetStatusBadRequest() {
        ResponseEntity<String> jobResult = getJob("1234");
        assertEquals(HttpStatus.BAD_REQUEST, jobResult.getStatusCode());
    }

    @Test
    public void testGetStatusNotFound() {
        ResponseEntity<String> jobResult = getJob(UUID.randomUUID().toString());
        assertEquals(HttpStatus.NOT_FOUND, jobResult.getStatusCode());
    }

    private ResponseEntity<String> getJob(String uuid) {
        return restTemplateErrorAgnostic.getForEntity(buildUri(API_URL + "/job/{uuid}"), String.class , uuid);
    }

    @Test
    public void testExceptionHandlingOfVidRestrictedBaseController() {
        //get logs require user role that may need simulator presets
        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                new PresetGetSessionSlotCheckIntervalGet(),
                new PresetAAIGetSubscribersGet()), SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
        ResponseEntity<String> jobResult = restTemplateErrorAgnostic.getForEntity(buildUri(API_URL + "/error"), String.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, jobResult.getStatusCode());
        assertThat(jobResult.getBody(), containsString("GenericUncheckedException"));
        assertThat(jobResult.getBody(), containsString("dummy error"));
        String logLines = LoggerFormatTest.getLogLines(LogName.error, 15, 0, restTemplate, uri);
        assertThat(logLines, containsString("GenericUncheckedException"));
        assertThat(logLines, containsString("dummy error"));
    }
}
