package vid.automation.test.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.Get;
import vid.automation.test.sections.VidBasePage;
import vid.automation.test.services.SimulatorApi;

public class HealthStatusTest extends VidBaseTestCase {

    private VidBasePage vidBasePage = new VidBasePage();

    @DataProvider
    public static Object[][] aaiHealthStatusProvider() {
        return new Object[][]{
                {ImmutableList.of(new PresetGetSessionSlotCheckIntervalGet()),
                        "border-not-ok", "No subscriber received", 404},
                {ImmutableList.of(new PresetGetSessionSlotCheckIntervalGet(), new PresetAAIGetSubscribersGet()),
                        "border-is-ok", "OK", 200}

        };
    }


    @Test(dataProvider = "aaiHealthStatusProvider")
    public void testAaiHealthStatus(Collection<BasePreset> presets, String cssName, String description, int httpCode) throws IOException {
        SimulatorApi.registerExpectationFromPresets(presets, CLEAR_THEN_SET);
        refreshStatus();
        GeneralUIUtils.ultimateWait(); //instead of assertTrue(Wait.waitByClassAndTextXpathOnly(cssName, "AAI", 10))
        RemoteWebElement componentName = (RemoteWebElement)Get.byTestId("component-name-AAI");
        assertThat("Wrong component name", componentName.getText(), equalTo("AAI"));
        assertThat("Wrong css for component name", componentName.getAttribute("class"), containsString(cssName));
        ObjectMapper objectMapper = new ObjectMapper();
        RemoteWebElement componentMetadata = ((RemoteWebElement) Get.byTestId("component-metadata-AAI"));
        assertThat("Wrong css for component details", componentName.getAttribute("class"), containsString(cssName));
        Map<String, Object> details =  objectMapper.readValue(componentMetadata.findElementsByTagName("pre").get(0).getText(), Map.class);
        assertThat(details.get("description"), equalTo(description));
        assertThat(details.get("httpMethod"), equalTo("GET"));
        assertThat(details.get("httpCode"), equalTo(httpCode));
    }

    private void refreshStatus() {
        vidBasePage.navigateTo("app/ui/#/healthStatus");
        GeneralUIUtils.ultimateWait();
        Click.byClass("icon-refresh");

    }

}
