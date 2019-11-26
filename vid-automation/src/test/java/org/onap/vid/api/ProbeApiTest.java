package org.onap.vid.api;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET;
import static vid.automation.test.services.SimulatorApi.getSimulatedResponsesPort;
import static vid.automation.test.services.SimulatorApi.getSimulatorHost;

import com.google.common.collect.ImmutableList;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGetInvalidResponse;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestsManyStatusesGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestsManyStatusesInvalidResponseGet;
import org.onap.simulator.presetGenerator.presets.scheduler.PresetGetSchedulerChangeManagementInvalidResponse;
import org.onap.simulator.presetGenerator.presets.scheduler.PresetGetSchedulerChangeManagements;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGetEmptyResult;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGetInvalidResponse;
import org.onap.vid.model.probe.ExternalComponentStatus;
import org.onap.vid.model.probe.HttpRequestMetadata;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.services.SimulatorApi;

public class ProbeApiTest extends BaseApiTest {

    private static final String MSO_QUERY_PARAMS = "filter=requestExecutionDate:EQUALS:01-01-2100";
    private static final String AAI_QUERY_PARMAS = "business/customers?subscriber-type=INFRA&depth=0";
    private static final String SDC_PATH_PARAMS = "46401eec-35bd-4e96-ad0d-0356ff6b8c8d/toscaModel";
    private static final String SCHEDULER_PATH =
        String.format("http://%s:%d/scheduler/v1/ChangeManagement/schedules/scheduleDetails/", getSimulatorHost(), getSimulatedResponsesPort());


    @BeforeClass
    public void login() {
        super.login();
    }

    @DataProvider
    public static Object[][] probePresetAndResponse(Method test) {
        return new Object[][]{
                {
                        "all good",
                        ImmutableList.of(
                                new PresetAAIGetSubscribersGet(),
                                new PresetMSOOrchestrationRequestsManyStatusesGet(),
                                new PresetSDCGetServiceToscaModelGet("46401eec-35bd-4e96-ad0d-0356ff6b8c8d", "serviceCreationTest.zip"),
                                new PresetGetSchedulerChangeManagements()
                        ),
                        ImmutableList.of(new ExternalComponentStatus(ExternalComponentStatus.Component.AAI,
                                true,
                                new HttpRequestMetadata(HttpMethod.GET,
                                        200,
                                        AAI_QUERY_PARMAS,
                                        "{\"customer\":[{\"global-customer-id\":\"CAR_2020_ER\",\"subscriber-name\":\"CAR_2020_ER\",\"subscriber-type\":\"INFRA\",\"resource-version\":\"1494001902987\",\"relationship-list\":null},{\"global-customer-id\":\"21014aa2-526b-11e6-beb8-9e71128cae77\",\"subscriber-name\":\"JULIO ERICKSON\",\"subscriber-type\":\"INFRA\",\"resource-version\":\"1494001776295\",\"relationship-list\":null},{\"global-customer-id\":\"DHV1707-TestSubscriber-2\",\"subscriber-name\":\"DALE BRIDGES\",\"subscriber-type\":\"INFRA\",\"resource-version\":\"1498751754450\",\"relat",
                                        "OK"
                                )
                        ), new ExternalComponentStatus(ExternalComponentStatus.Component.MSO,
                                true,
                                new HttpRequestMetadata(HttpMethod.GET,
                                        200,
                                        MSO_QUERY_PARAMS,
                                        "{ " +
                                                " \"requestList\": [{ " +
                                                "   \"request\": { " +
                                                "    \"requestId\": \"rq1234d1-5a33-55df-13ab-12abad84e333\", " +
                                                "    \"startTime\": \"Thu, 04 Jun 2009 02:51:59 GMT\", " +
                                                "    \"instanceReferences\": { " +
                                                "     \"serviceInstanceId\": \"bc305d54-75b4-431b-adb2-eb6b9e546014\" " +
                                                "    }, " +
                                                "    \"requestScope\": \"vnf\", " +
                                                "    \"requestType\": \"updateInstance\", " +
                                                "    \"requestDetails\": { " +
                                                "     \"modelInfo\": { " +
                                                "      \"modelType\": \"service\", " +
                                                "      \"modelInvariantId\": \"sn5256d1-5a33-55df-13ab-12abad84e764\", " +
                                                "      \"modelVersionId\": \"ab6478e4-ea33-3346-ac12-ab1",
                                        "OK"
                                )
                        ), new ExternalComponentStatus(ExternalComponentStatus.Component.SDC,
                                true,
                                new HttpRequestMetadata(HttpMethod.GET,
                                        200,
                                        SDC_PATH_PARAMS,
                                        "",
                                        "OK"
                                )
                        ), new ExternalComponentStatus(ExternalComponentStatus.Component.SCHEDULER,
                                true,
                                new HttpRequestMetadata(HttpMethod.GET,
                                        200,
                                        SCHEDULER_PATH,
                                        StringUtils.substring(new PresetGetSchedulerChangeManagements().getResponseBody().toString(), 0, 500),
                                        "OK"
                                )
                        ))
                },
                {
                        "invalid json",
                        ImmutableList.of(
                                new PresetAAIGetSubscribersGetInvalidResponse(200),
                                new PresetMSOOrchestrationRequestsManyStatusesInvalidResponseGet(200),
                                new PresetSDCGetServiceToscaModelGetEmptyResult("46401eec-35bd-4e96-ad0d-0356ff6b8c8d"),
                                new PresetGetSchedulerChangeManagementInvalidResponse(200)
                        ),
                        ImmutableList.of(new ExternalComponentStatus(ExternalComponentStatus.Component.AAI,
                                false,
                                new HttpRequestMetadata(HttpMethod.GET,
                                        200,
                                        AAI_QUERY_PARMAS,
                                        "this payload is an invalid json",
                                        "com.fasterxml.jackson.core.JsonParseException"
                                )
                        ), new ExternalComponentStatus(ExternalComponentStatus.Component.MSO,
                                false,
                                new HttpRequestMetadata(HttpMethod.GET,
                                        200,
                                        MSO_QUERY_PARAMS,
                                        "this payload is an invalid json",
                                        "com.fasterxml.jackson.core.JsonParseException"
                                )
                        ), new ExternalComponentStatus(ExternalComponentStatus.Component.SDC,
                                false,
                                new HttpRequestMetadata(HttpMethod.GET,
                                        200,
                                        SDC_PATH_PARAMS,
                                        "",
                                        "error reading model 46401eec-35bd-4e96-ad0d-0356ff6b8c8d from SDC"
                                )
                        ), new ExternalComponentStatus(ExternalComponentStatus.Component.SCHEDULER,
                                false,
                                new HttpRequestMetadata(HttpMethod.GET,
                                        200,
                                        SCHEDULER_PATH,
                                        "this payload is an invalid json",
                                        "com.fasterxml.jackson.core.JsonParseException"
                                )
                        ))
                },
                {
                        "bad http code",
                        ImmutableList.of(
                                new PresetAAIGetSubscribersGetInvalidResponse(500),
                                new PresetMSOOrchestrationRequestsManyStatusesInvalidResponseGet(406),
                                new PresetSDCGetServiceToscaModelGetInvalidResponse("46401eec-35bd-4e96-ad0d-0356ff6b8c8d", 404),
                                new PresetGetSchedulerChangeManagementInvalidResponse(400)
                        ),
                        ImmutableList.of(new ExternalComponentStatus(ExternalComponentStatus.Component.AAI,
                                false,
                                new HttpRequestMetadata(HttpMethod.GET,
                                        500,
                                        AAI_QUERY_PARMAS,
                                        "this payload is an invalid json",
                                        "No subscriber received"
                                )
                        ), new ExternalComponentStatus(ExternalComponentStatus.Component.MSO,
                                false,
                                new HttpRequestMetadata(HttpMethod.GET,
                                        406,
                                        MSO_QUERY_PARAMS,
                                        "this payload is an invalid json",
                                        "MSO returned no orchestration requests"
                                )
                        ), new ExternalComponentStatus(ExternalComponentStatus.Component.SDC,
                                false,
                                new HttpRequestMetadata(HttpMethod.GET,
                                        404,
                                        SDC_PATH_PARAMS,
                                        "simulated error description from sdc",
                                        "model 46401eec-35bd-4e96-ad0d-0356ff6b8c8d not found in SDC" +
                                                " (consider updating vid probe configuration 'probe.sdc.model.uuid')"
                                )
                        ), new ExternalComponentStatus(ExternalComponentStatus.Component.SCHEDULER,
                                false,
                                new HttpRequestMetadata(HttpMethod.GET,
                                        400,
                                        SCHEDULER_PATH,
                                        "this payload is an invalid json",
                                        "org.apache.http.HttpException"
                                )
                        ))
                }

        };
    }

    @Test(dataProvider = "probePresetAndResponse")
    public void probeRequest_returnsResponseAsExpected(String desc, Collection<BasePreset> presets, Collection<ExternalComponentStatus> expectedStatuses) {
        SimulatorApi.registerExpectationFromPresets(presets, CLEAR_THEN_SET);
        ResponseEntity<List<ExternalComponentStatus>> response = restTemplate.exchange(
                uri + "/probe",
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExternalComponentStatus>>() {
                });
        List<ExternalComponentStatus> probeResults = response.getBody();
        Assert.assertEquals(5, probeResults.size());
        assertResultAsExpected(ExternalComponentStatus.Component.AAI, probeResults, expectedStatuses);
        assertResultAsExpected(ExternalComponentStatus.Component.SDC, probeResults, expectedStatuses);
        assertResultAsExpected(ExternalComponentStatus.Component.MSO, probeResults, expectedStatuses);
        assertResultAsExpected(ExternalComponentStatus.Component.SCHEDULER, probeResults, expectedStatuses);
    }

    private void assertResultAsExpected(ExternalComponentStatus.Component component, List<ExternalComponentStatus> probeResults, Collection<ExternalComponentStatus> expectedStatuses) {
        ExternalComponentStatus expectedStatus = expectedStatuses.stream().filter(x -> x.getComponent() == component)
                .findFirst().orElseThrow(() -> new AssertionError("Missing setup for " + component + " expected result"));
        ExternalComponentStatus componentStatus = probeResults.stream().filter(x -> x.getComponent() == component)
                .findFirst().orElseThrow(() -> new AssertionError(component.name()+" result not found in response"));

        Assert.assertThat("wrong metadata for " + component, requestMetadataReflected(componentStatus.getMetadata()),
                is(requestMetadataReflected(expectedStatus.getMetadata())));

        Assert.assertThat("wrong url for " + component, componentStatus.getMetadata().getUrl(),
                both(endsWith(expectedStatus.getMetadata().getUrl())).and(startsWith("http")));

        Assert.assertThat("wrong description for " + component, componentStatus.getMetadata().getDescription(),
                anyOf(equalTo(expectedStatus.getMetadata().getDescription()), startsWith(expectedStatus.getMetadata().getDescription())));

        Assert.assertThat("wrong status for " + component, componentStatus.isAvailable(), is(expectedStatus.isAvailable()));
    }

    //serialize fields except of fields we cannot know ahead of time
    private static String requestMetadataReflected(HttpRequestMetadata metadata) {
        return new ReflectionToStringBuilder(metadata, ToStringStyle.SHORT_PREFIX_STYLE)
                .setExcludeFieldNames("duration", "url", "description")
                .toString();
    }
}
