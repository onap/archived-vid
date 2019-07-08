package vid.automation.test.sections;

import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.SelectOption;

import static org.junit.Assert.assertEquals;

public class TestEnvironmentPage extends VidBasePage {

    public static void clickTestEnvironmentActivate(String envId) {
        getTestEnvironmentActivationButton(envId).click();
    }

    public static void clickTestEnvironmentDeactivate(String envId) {
        getTestEnvironmentDeactivationButton(envId).click();
    }

    public static WebElement getTestEnvironmentActivationButton(String envId) {
        WebElement webElement = Get.byId(Constants.TestEnvironments.activateButtonIdPrefix + envId);
        return webElement;
    }

    public static WebElement getTestEnvironmentDeactivationButton(String envId) {
        WebElement webElement = Get.byId(Constants.TestEnvironments.deactivateButtonIdPrefix + envId);
        return webElement;
    }
    
    public static WebElement getTestEnvironmentAttachButton(String envId) {
        WebElement webElement = Get.byId(Constants.TestEnvironments.attachButtonIdPrefix + envId);
        return webElement;
    }

    public static String selectEnvRelease(String envRelease){
        GeneralUIUtils.ultimateWait();
        String selectedOption;
        selectedOption = SelectOption.getSelectedOption(Constants.TestEnvironments.ENVIRONMENT_RELEASE);
        return selectedOption;
    }
}
