package org.opencomp.vid.more;

import org.opencomp.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.opencomp.simulator.presetGenerator.presets.aai.*;
import org.opencomp.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.opencomp.simulator.presetGenerator.presets.ecompportal_att.PresetGetUserGet;
import org.opencomp.simulator.presetGenerator.presets.mso.PresetActivateServiceInstancePost;
import org.opencomp.simulator.presetGenerator.presets.mso.PresetDeactivateServiceInstancePost;
import org.opencomp.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstancePost;
import org.opencomp.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.opencomp.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceMetadataGet;
import org.opencomp.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import org.opencomp.vid.api.BaseApiTest;
import org.springframework.http.HttpMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.services.SimulatorApi.RegistrationStrategy;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPreset;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPresets;

public class SimulatorLoaderTest extends BaseApiTest {


    protected Invocation.Builder createSimulatorRequestBuilder(BasePreset preset) {
        WebTarget webTarget = client.target(SimulatorApi.getSimulationUri() + preset.getReqPath());
        webTarget = addQueryParamsToWebTarget(preset, webTarget);
        return webTarget.request()
                .accept("application/json");
    }

    private WebTarget addQueryParamsToWebTarget(BasePreset preset, WebTarget webTarget) {
        if (preset.getQueryParams() != null) {
            for (Map.Entry<String, List> entry : preset.getQueryParams().entrySet()) {
                webTarget = webTarget.queryParam(entry.getKey(), entry.getValue().toArray());
            }
        }
        return webTarget;
    }

    @DataProvider
    public static Object[][] presetClassesWithPutPost(Method test) {
        return new Object[][]{
                {new PresetAAIGetPNFByRegionErrorPut()},
                {new PresetAAIServiceDesignAndCreationPut("a","b")},
                {new PresetDeactivateServiceInstancePost()},
                {new PresetActivateServiceInstancePost()},
                {new PresetMSOCreateServiceInstancePost()}
        };
    }

    @Test(dataProvider = "presetClassesWithPutPost")
    public<C extends BasePreset> void presetPutPost_WhenLoaded_SimulatorReturnsFakeValues(C preset) {
        registerExpectationFromPreset(preset, RegistrationStrategy.CLEAR_THEN_SET);

        Response cres = createSimulatorRequestBuilder(preset)
                .method(preset.getReqMethod().name(),
                        Entity.entity(preset.getRequestBody(), MediaType.APPLICATION_JSON));

        int status = cres.getStatus();

        assertEquals(status, preset.getResponseCode());
    }

    @DataProvider
    public static Object[][] presetWithGetInstances(Method test) {
        return new Object[][]{
                    {new PresetAAIGetSubscribersGet()},
                    {new PresetGetSessionSlotCheckIntervalGet()},
                    {new PresetGetUserGet()},
                    {new PresetAAIGetServicesGet()},
                    {new PresetSDCGetServiceMetadataGet("a" , "b", "serviceCreationTest.zip")},
                    {new PresetSDCGetServiceToscaModelGet( "a", "serviceCreationTest.zip")},
                    {new PresetMSOOrchestrationRequestGet()},
                    {new PresetAAIGetNetworkZones()},
                    {new PresetAAISearchNodeQueryEmptyResult()},
                    {new PresetAAIBadBodyForGetServicesGet("not a json")},
                };
    }

    @Test(dataProvider = "presetWithGetInstances")
    public <C extends BasePreset> void assertPresetWithGetMethod(C preset) {
        registerExpectationFromPreset(preset, RegistrationStrategy.CLEAR_THEN_SET);

        Response cres = createSimulatorRequestBuilder(preset).get();

        int status = cres.getStatus();

        assertEquals(status, preset.getResponseCode());
    }

    @Test(expectedExceptions = { RuntimeException.class }, expectedExceptionsMessageRegExp = ".*SimulatorLoaderTest.*")
    public void assertPresetThatThrowException() {

        registerExpectationFromPresets(Collections.singletonList(
                new BasePreset() {

            @Override
            public HttpMethod getReqMethod() {
                throw new RuntimeException();
            }

            @Override
            public String getReqPath() {
                return null;
            }

            @Override
            protected String getRootPath() {
                return null;
            }
        }), RegistrationStrategy.CLEAR_THEN_SET);
    }

}
