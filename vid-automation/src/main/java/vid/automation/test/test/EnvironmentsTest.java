package vid.automation.test.test;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.*;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.sections.TestEnvironmentPage;
import vid.automation.test.services.SimulatorApi;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/*

1.	Activate
    a.	Happy scenario
        i.	Polling screen is displayed
        ii.	 Environment status become active in environments table
        iii. Button become deactivation
        iv.	 Attach file button is shown
    b.	Non JSON file content – error is shown
    c.	Invalid manifest format in the JSON – error is shown
    d.	MSO Error – 500/400 – error is shown
2.	Attach manifest
    a.	Happy scenario
    i.	Polling screen is displayed
    b.	MSO Error – 500/400 – error is shown

3.	Deactivate
    a.	Happy scenario
        i.	Polling screen is displayed
        ii.	Environment status become inactive in environments table
        iii.	Button become activation
        iv.	Attach file button is not shown
    b.	MSO Error – 500/400 – error is shown

 */

public class EnvironmentsTest extends VidBaseTestCase {
    @Test
    public void testLeftPanelTestEnvironmentButton() {
        List<WebElement> leftPanelButtons = Get.byClass(Constants.SideMenu.buttonClass);
        Assert.assertTrue(Wait.byText(Constants.SideMenu.TEST_ENVIRONMENTS));
    }

    @Test
    public void testEnvironmentHeaderLine() {
        SideMenu.navigateToTestEnvironmentsPage();
        Assert.assertTrue(GeneralUIUtils.isWebElementExistByTestId(Constants.TestEnvironments.pageHeadlineId));
        Assert.assertTrue(GeneralUIUtils.isWebElementExistByTestId(Constants.TestEnvironments.headlineNewButtonId));
        Assert.assertTrue(GeneralUIUtils.isWebElementExistByTestId(Constants.TestEnvironments.headlineSearchInputId));
    }

    @Test
    public void testTable() {
        String table = Constants.TestEnvironments.environmentsTable;
        SideMenu.navigateToTestEnvironmentsPage();
        Assert.assertTrue(GeneralUIUtils.isWebElementExistByTestId(Constants.TestEnvironments.refreshButtonId));
        Assert.assertTrue(GeneralUIUtils.isWebElementExistByTestId(table));
        boolean tableData = Exists.tableContent(table);
        boolean noData = GeneralUIUtils.isWebElementExistByTestId(Constants.TestEnvironments.noDataMessage);
        Assert.assertTrue(tableData || noData);
    }

    @Test
    public void testApplicationEnvironmentActivation() throws Exception {
        SimulatorApi.registerExpectation("environment/activate/get_operational_environments_aai1.json");
        String envId  = "f07ca256-96dd-40ad-b4d2-7a77e2a974eb";
        WebElement activationButton = TestEnvironmentPage.getTestEnvironmentActivationButton(envId);
        assertTrue("Failed to find Activate button for test env with id: "+envId, activationButton.isDisplayed());
        activationButton.click();

        Input.file("applicationEnvironment/non_valid_json.json", "testEnvManifestFileInput");
        WebElement manifestFileName = Get.byId("manifestFileName");
        assertEquals("Manifest file name is wrong in test environment activation modal", "non_valid_json.json", manifestFileName.getText());
        WebElement errorLabel = Get.byId("errorLabel");
        assertEquals("wrong error message for non valid json file", "non_valid_json.json is not a valid JSON file", errorLabel.getText());

        Input.file("applicationEnvironment/manifest.json", "testEnvManifestFileInput");
        manifestFileName = Get.byId("manifestFileName");
        assertEquals("Manifest file name is wrong in test environment activation modal", "manifest.json", manifestFileName.getText());

    }

}
