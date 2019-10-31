package org.onap.vid.api;

import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.vid.more.LoggerFormatTest;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.services.SimulatorApi.RegistrationStrategy;

public class ChangeManagementMsoApiLoggingTest extends BaseApiTest {

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

}
