package org.onap.vid.api;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGetInvalidResponse;
import org.onap.vid.model.probe.ExternalComponentStatus;
import org.onap.vid.model.probe.HttpRequestMetadata;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.services.SimulatorApi;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET;

public class ProbeApiTest extends BaseApiTest {

    @BeforeClass
    public void login() {
        super.login();
    }

    @DataProvider
    public static Object[][] probePresetAndResponse(Method test) {
        return new Object[][]{
                {
                    new PresetAAIGetSubscribersGet(),
                    new ExternalComponentStatus(ExternalComponentStatus.Component.AAI,
                        true,
                        new HttpRequestMetadata(HttpMethod.GET,
                                200,
                                "business/customers?subscriber-type=INFRA&depth=0",
                                "{\"customer\":[{\"global-customer-id\":\"MSO_1610_ST\",\"subscriber-name\":\"MSO_1610_ST\",\"subscriber-type\":\"INFRA\",\"resource-version\":\"1494001902987\"},{\"global-customer-id\":\"21014aa2-526b-11e6-beb8-9e71128cae77\",\"subscriber-name\":\"PACKET CORE\",\"subscriber-type\":\"INFRA\",\"resource-version\":\"1494001776295\"},{\"global-customer-id\":\"DHV1707-TestSubscriber-2\",\"subscriber-name\":\"ICORE CORE\",\"subscriber-type\":\"INFRA\",\"resource-version\":\"1498751754450\"},{\"global-customer-id\":\"DHV1707-TestSubscriber-1\",\"subscriber",
                                "OK"
                                )
                    )
                },
                {
                        new PresetAAIGetSubscribersGetInvalidResponse(200),
                        new ExternalComponentStatus(ExternalComponentStatus.Component.AAI,
                                false,
                                new HttpRequestMetadata(HttpMethod.GET,
                                        200,
                                        "business/customers?subscriber-type=INFRA&depth=0",
                                        "this payload is an invalid json",
                                        "org.codehaus.jackson.JsonParseException"
                                )
                        )
                },
                {
                        new PresetAAIGetSubscribersGetInvalidResponse(500),
                        new ExternalComponentStatus(ExternalComponentStatus.Component.AAI,
                                false,
                                new HttpRequestMetadata(HttpMethod.GET,
                                        500,
                                        "business/customers?subscriber-type=INFRA&depth=0",
                                        "this payload is an invalid json",
                                        "No subscriber received"
                                )
                        )
                }

        };
    }

    @Test(dataProvider = "probePresetAndResponse")
    public void probeRequest_returnsResponseAsExpected(BasePreset preset, ExternalComponentStatus expectedStatus ){
        SimulatorApi.registerExpectationFromPreset(preset, CLEAR_THEN_SET);
        ResponseEntity<List<ExternalComponentStatus>> response = restTemplate.exchange(
                uri + "/probe",
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExternalComponentStatus>>() {});
        List<ExternalComponentStatus> probeResults = response.getBody();
        Assert.assertEquals(probeResults.size(),1);
        assertAaiGetAllSubscribersAsExpected(probeResults,expectedStatus);

    }

    private void assertAaiGetAllSubscribersAsExpected(List<ExternalComponentStatus> probeResults,ExternalComponentStatus expectedStatus){
        Optional<ExternalComponentStatus> aaiGetAllSubscribersResult = probeResults.stream().filter(x -> x.getComponent()== ExternalComponentStatus.Component.AAI).findFirst();
        Assert.assertTrue(aaiGetAllSubscribersResult.isPresent());
        ExternalComponentStatus aaiGetAllSubscribersStatus = aaiGetAllSubscribersResult.get();
        Assert.assertEquals(aaiGetAllSubscribersStatus.isAvailable(),expectedStatus.isAvailable());

        Assert.assertThat(requestMetadataReflected(aaiGetAllSubscribersStatus.getMetadata()),is(requestMetadataReflected(expectedStatus.getMetadata())));
        Assert.assertThat(aaiGetAllSubscribersStatus.getMetadata().getUrl(), both(endsWith(expectedStatus.getMetadata().getUrl())).and(startsWith("http")));

        Assert.assertThat(aaiGetAllSubscribersStatus.getMetadata().getDescription(),
                anyOf(equalTo(expectedStatus.getMetadata().getDescription()), startsWith(expectedStatus.getMetadata().getDescription())));
    }

    //serialize fields except of fields we cannot know ahead of time
    private static String requestMetadataReflected(HttpRequestMetadata metadata) {
        return new ReflectionToStringBuilder(metadata, ToStringStyle.SHORT_PREFIX_STYLE)
                .setExcludeFieldNames("duration", "url", "description")
                .toString();
    }
}
