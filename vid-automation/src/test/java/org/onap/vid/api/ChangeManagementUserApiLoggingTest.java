package org.onap.vid.api;

import com.google.common.collect.ImmutableList;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.scheduler.PresetDeleteSchedulerChangeManagement;
import org.onap.simulator.presetGenerator.presets.scheduler.PresetGetSchedulerChangeManagements;
import org.onap.vid.more.LoggerFormatTest;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.services.SimulatorApi.RegistrationStrategy;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static vid.automation.test.services.SimulatorApi.registerExpectationFromPresets;

public class ChangeManagementUserApiLoggingTest extends BaseApiTest {

  public static final String MSO = "/mso";
  public static final String CHANGE_MANAGEMENT = "change-management";
  public static final String  MSO_GET_CHANGE_MANAGEMENTS_SCALEOUT ="changeManagement/mso_get_change_managements_scaleout.json";
  public static final String  MSO_GET_CHANGE_MANAGEMENTS = "changeManagement/mso_get_change_managements.json";


  @BeforeClass
  public void login() {
    super.login();
  }

  @Test
  public void testGetOrchestrationRequestsLoggedInMetricsLog () {
    SimulatorApi.registerExpectation(MSO_GET_CHANGE_MANAGEMENTS_SCALEOUT, RegistrationStrategy.CLEAR_THEN_SET);
    SimulatorApi.registerExpectation(MSO_GET_CHANGE_MANAGEMENTS, RegistrationStrategy.APPEND);
    SimulatorApi.registerExpectationFromPreset( new PresetAAIGetSubscribersGet(), RegistrationStrategy.APPEND);

    ResponseEntity<String> responseEntity = restTemplate.getForEntity(buildUri(CHANGE_MANAGEMENT + MSO ), String.class);
    String requestId = responseEntity.getHeaders().getFirst("X-ECOMP-RequestID-echo");

    LoggerFormatTest.assertHeadersAndMetricLogs(restTemplate, uri, requestId, "/mso/orchestrationRequests/", 2);
  }

  @Test
  public void whenCallScheduler_thenRequestRecordedInMetricsLog() {

    String SCHEDULER_PATH = "change-management/scheduler";

    String VID_TO_SCHEDULER_PATH = "/scheduler/v1/ChangeManagement/schedules" ;

    registerExpectationFromPresets(ImmutableList.of(
            new PresetGetSchedulerChangeManagements(),
            new PresetAAIGetSubscribersGet()),
            RegistrationStrategy.CLEAR_THEN_SET);

    ResponseEntity<String> responseEntity = restTemplate.getForEntity(buildUri(SCHEDULER_PATH), String.class);

    String requestId = responseEntity.getHeaders().getFirst("X-ECOMP-RequestID-echo");
    LoggerFormatTest.assertHeadersAndMetricLogs(restTemplate, uri, requestId, VID_TO_SCHEDULER_PATH, 1);
  }


  @Test
  public void whenCalldeleteScheduler_thenRequestRecordedInMetricsLog() {

    String SCHEDULER_PATH = "change-management/scheduler/schedules/83aec7bf-602f-49eb-9788-bbc33ac550d9";

    String VID_TO_SCHEDULER_PATH = "/scheduler/v1/ChangeManagement/schedules/83aec7bf-602f-49eb-9788-bbc33ac550d9" ;

    registerExpectationFromPresets(ImmutableList.of(
            new PresetDeleteSchedulerChangeManagement(),
            new PresetAAIGetSubscribersGet()),
            RegistrationStrategy.CLEAR_THEN_SET);

    WebTarget webTarget = client.target(uri).path(SCHEDULER_PATH);
    Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).delete();


    String requestId = response.getHeaders().getFirst("X-ECOMP-RequestID-echo").toString();
    LoggerFormatTest.assertHeadersAndMetricLogs(restTemplate, uri, requestId, VID_TO_SCHEDULER_PATH, 1);
  }
}
