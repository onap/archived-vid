package vid.automation.test.sections;

import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.Get;

import java.util.List;

public class TestEnvironmentPage extends VidBasePage {

    public static void openTestEnvironmentPage() {
        Click.byText(Constants.SideMenu.TEST_ENVIRONMENTS);
        GeneralUIUtils.ultimateWait();
    }

    public static void clickTestEnvironmentActivate(String envId) {
        getTestEnvironmentActivationButton(envId).click();
    }

    public static WebElement getTestEnvironmentActivationButton(String envId) {
        TestEnvironmentPage.openTestEnvironmentPage();
        WebElement webElement = Get.byId("testEnvActivate-"+envId);
        return webElement;
    }
}
