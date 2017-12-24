package vid.automation.test.test;

import com.google.common.collect.ImmutableMap;
import javafx.geometry.Side;
import org.junit.Assert;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.*;
import vid.automation.test.model.Environment;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.utils.ReadFile;
import vid.automation.test.sections.TestEnvironmentPage;
import vid.automation.test.sections.VidBasePage;
import vid.automation.test.services.SimulatorApi;

import java.util.List;
import java.util.Map;

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

    public static final String GET_OPERATIONAL_ENVIRONMENT_JSON = "get_operational_environment.json";
    public static final String GET_EMPTY_OPERATIONAL_ENVIRONMENT_JSON = "get_empty_operational_environment.json";
    public static final String GET_FULL_OPERATIONAL_ENVIRONMENT_JSON = "get_full_operational_environment.json";
    public static final String GET_ERROR_OPERATIONAL_ENVIRONMENT_JSON = "get_error_operational_environment.json";
    static final String ENVIRONMENTS_CONF = "environments";
    static final String NEW_ENVIRONMENT_CONF = "newEnvironment";

    @Test
    public void testLeftPanelTestEnvironmentButton() {
        List<WebElement> leftPanelButtons = Get.byClass(Constants.SideMenu.buttonClass);
        Assert.assertTrue(Wait.byText(Constants.SideMenu.TEST_ENVIRONMENTS));
    }

    @Test
    public void testEnvironmentHeaderLine() {
        SideMenu.navigateToTestEnvironmentsPage();
        Assert.assertTrue(Exists.byTestId(Constants.TestEnvironments.PAGE_HEADLINE));
        Assert.assertTrue(Exists.byTestId(Constants.TestEnvironments.HEADLINE_NEW_BUTTON));
        Assert.assertTrue(Exists.byTestId(Constants.TestEnvironments.HEADLINE_SEARCH_INPUT));
    }

    @Test
    public void testTable() throws Exception {
        SimulatorApi.registerExpectation(GET_OPERATIONAL_ENVIRONMENT_JSON);
        GeneralUIUtils.getDriver().navigate().refresh();
        Map <String, List<String>> file = ReadFile.getJsonFile(ENVIRONMENTS_CONF, Map.class);
        String tableId = Constants.TestEnvironments.ENVIRONMENTS_TABLE;
        SideMenu.navigateToTestEnvironmentsPage();
        boolean emptyTableMessage = Exists.byTestId(Constants.TestEnvironments.NO_DATA_MESSAGE);
        boolean errorMessage = Exists.byTestId(Constants.TestEnvironments.ERROR_MESSAGE);
        Assert.assertFalse(emptyTableMessage || errorMessage);
        List<String> headers = Get.tableHeaderValuesByTestId(tableId);
        Assert.assertEquals(file.get(Constants.TestEnvironments.HEADERS_LIST), headers);
        List<List<String>> body = Get.tableBodyValuesByTestId(tableId);
        Assert.assertEquals(file.get(Constants.TestEnvironments.BASIC_LIST), body);
        // refresh table
        SimulatorApi.registerExpectation(GET_FULL_OPERATIONAL_ENVIRONMENT_JSON);
        Click.byTestId(Constants.TestEnvironments.REFRESH_BUTTON);
        Wait.angularHttpRequestsLoaded();
        body = Get.tableBodyValuesByTestId(tableId);
        Assert.assertEquals(file.get(Constants.TestEnvironments.FULL_LIST), body);
        GeneralUIUtils.ultimateWait();
    }

    @Test
    public void testSortTable() throws Exception {
        SimulatorApi.registerExpectation(GET_FULL_OPERATIONAL_ENVIRONMENT_JSON);
        GeneralUIUtils.getDriver().navigate().refresh();
        Map <String, List<String>> file = ReadFile.getJsonFile(ENVIRONMENTS_CONF, Map.class);
        String tableId = Constants.TestEnvironments.ENVIRONMENTS_TABLE;
        SideMenu.navigateToTestEnvironmentsPage();
        Click.byClass(Constants.TestEnvironments.TABLE_HEADER_ASC);
        GeneralUIUtils.ultimateWait();
        List<List<String>> body = Get.tableBodyValuesByTestId(tableId);
        Assert.assertEquals(file.get(Constants.TestEnvironments.SORTED_LIST), body);
        GeneralUIUtils.ultimateWait();
    }

    @Test
    public void testFilterTable() throws Exception {
        SimulatorApi.registerExpectation(GET_FULL_OPERATIONAL_ENVIRONMENT_JSON);
        GeneralUIUtils.getDriver().navigate().refresh();
        Map <String, List<String>> file = ReadFile.getJsonFile(ENVIRONMENTS_CONF, Map.class);
        String tableId = Constants.TestEnvironments.ENVIRONMENTS_TABLE;
        SideMenu.navigateToTestEnvironmentsPage();
        Input.text(Constants.TestEnvironments.TEXT_TO_FILTER, Constants.TestEnvironments.SEARCH_INPUT);
        GeneralUIUtils.ultimateWait();
        List<List<String>> body = Get.tableBodyValuesByTestId(tableId);
        Assert.assertEquals(file.get(Constants.TestEnvironments.FILTERED_LIST), body);
        GeneralUIUtils.ultimateWait();
    }


    @Test
    public void testEmptyTableMessage() throws Exception {
        SimulatorApi.registerExpectation(GET_EMPTY_OPERATIONAL_ENVIRONMENT_JSON);
        GeneralUIUtils.getDriver().navigate().refresh();
        SideMenu.navigateToTestEnvironmentsPage();
        boolean emptyTableMessage = Exists.byTestId(Constants.TestEnvironments.NO_DATA_MESSAGE);
        Assert.assertTrue(emptyTableMessage);
        Assert.assertFalse(Exists.byTestId(Constants.TestEnvironments.ENVIRONMENTS_TABLE));
        GeneralUIUtils.ultimateWait();
    }

    @Test
    public void testErrorMessage() throws Exception {
        SimulatorApi.registerExpectation(GET_ERROR_OPERATIONAL_ENVIRONMENT_JSON);
        GeneralUIUtils.getDriver().navigate().refresh();
        SideMenu.navigateToTestEnvironmentsPage();
        boolean errorMessage = Exists.byTestId(Constants.TestEnvironments.ERROR_MESSAGE);
        Assert.assertTrue(errorMessage);
        Assert.assertFalse(Exists.byTestId(Constants.TestEnvironments.ENVIRONMENTS_TABLE));
        GeneralUIUtils.ultimateWait();
    }

    @Test
    public void testNewEnvironmentPopup() throws Exception {
        SimulatorApi.registerExpectation(GET_FULL_OPERATIONAL_ENVIRONMENT_JSON);
        GeneralUIUtils.getDriver().navigate().refresh();
        Environment environment = ReadFile.getJsonFile(NEW_ENVIRONMENT_CONF, Environment.class);
        SideMenu.navigateToTestEnvironmentsPage();
        Click.byTestId(Constants.TestEnvironments.HEADLINE_NEW_BUTTON);
        Assert.assertTrue(Exists.byTestId(Constants.TestEnvironments.NEW_ENVIRONMENT_FORM));

        boolean errorMessage = Exists.byTestId(Constants.TestEnvironments.POPUP_ERROR_MESSAGE);
        Assert.assertFalse(errorMessage);

        Assert.assertFalse(Get.byTestId(Constants.TestEnvironments.SUBMIT_BUTTON).isEnabled());
        Input.text(environment.operationalEnvironmentName, Constants.TestEnvironments.INSTANCE_NAME_INPUT);

        GeneralUIUtils.ultimateWait();
        SelectOption.byTestIdAndVisibleText(environment.EcompEnvironmentId, Constants.TestEnvironments.ECOMP_ID_DROP_DOWN);

        String ecompNameText = Input.getValueByTestId(Constants.TestEnvironments.ECOMP_NAME_INPUT);
        Assert.assertEquals(environment.EcompEnvironmentName, ecompNameText);

        String tenantContextText = Input.getValueByTestId(Constants.TestEnvironments.TENANT_CONTEXT_INPUT);
        Assert.assertEquals(environment.tenantContext, tenantContextText);

        String environmentTypeDefault = Get.selectedOptionText(Constants.TestEnvironments.ENVIRONMENT_TYPE_DROP_DOWN);
        Assert.assertEquals(environment.operationalEnvironmentType, environmentTypeDefault);

        SelectOption.byTestIdAndVisibleText(environment.workloadContext, Constants.TestEnvironments.WORKLOAD_CONTEXT_DROP_DOWN);

        WebElement submitButton = Get.byTestId(Constants.TestEnvironments.SUBMIT_BUTTON);
        Assert.assertTrue(submitButton.isEnabled());
        Click.byTestId(Constants.TestEnvironments.CANCEL_BUTTON);
        Wait.modalToDisappear();
        GeneralUIUtils.ultimateWait();
    }

    @Test
    public void testAaiErrorNewEnvironmentPopup() throws Exception {
        SimulatorApi.registerExpectation(GET_ERROR_OPERATIONAL_ENVIRONMENT_JSON);
        GeneralUIUtils.getDriver().navigate().refresh();
        SideMenu.navigateToTestEnvironmentsPage();
        Click.byTestId(Constants.TestEnvironments.HEADLINE_NEW_BUTTON);
        Assert.assertTrue(Exists.byTestId(Constants.TestEnvironments.NEW_ENVIRONMENT_FORM));
        GeneralUIUtils.ultimateWait();
        boolean errorMessage = Exists.byTestId(Constants.TestEnvironments.POPUP_ERROR_MESSAGE);
        Assert.assertTrue(errorMessage);
        Click.byTestId(Constants.TestEnvironments.CANCEL_BUTTON);
        Wait.modalToDisappear();
        GeneralUIUtils.ultimateWait();
    }

    @Test
    public void testCancelNewPopup() {
        SideMenu.navigateToTestEnvironmentsPage();
        Click.byTestId(Constants.TestEnvironments.HEADLINE_NEW_BUTTON);
        Assert.assertTrue(Exists.modal());
        Click.byTestId(Constants.TestEnvironments.CANCEL_BUTTON);
        Wait.modalToDisappear();
        Assert.assertFalse(Exists.modal());
        GeneralUIUtils.ultimateWait();
    }

    @Test
    public void testCloseNewPopup() {
        SideMenu.navigateToTestEnvironmentsPage();
        Click.byTestId(Constants.TestEnvironments.HEADLINE_NEW_BUTTON);
        Wait.modalToBeDisplayed();
        Assert.assertTrue(Exists.modal());
        GeneralUIUtils.ultimateWait();
        Click.byClass(Constants.TestEnvironments.MODAL_CLOSE_BUTTON_CLASS);
        Wait.modalToDisappear();
        GeneralUIUtils.ultimateWait();
        Assert.assertFalse(Exists.modal());
        GeneralUIUtils.ultimateWait();
    }

    @Test
    public void testApplicationEnvironmentActivation() throws Exception {
        SimulatorApi.registerExpectation("environment/activate/get_operational_environments_aai1.json");
        SimulatorApi.registerExpectation("environment/status/get_cloud_resources_request_status.json",
                ImmutableMap.of("status_message", Constants.TestEnvironments.environmentActivatedSuccesfullyMessage));

        SideMenu.navigateToTestEnvironmentsPage();

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

        Click.byId(Constants.generalSubmitButtonId);

        SimulatorApi.registerExpectation("environment/activate/get_operational_environments_aai1.json",
                ImmutableMap.of("Deactivate", "Activate"));

        GeneralUIUtils.findAndWaitByText(Constants.TestEnvironments.environmentActivatedSuccesfullyMessage, 60);

        VidBasePage vidBasePage = new VidBasePage();
        vidBasePage.clickCloseButton();

        GeneralUIUtils.ultimateWait();

        WebElement deactivationButton = TestEnvironmentPage.getTestEnvironmentDeactivationButton(envId);
        assertTrue("Failed to find Deactivate button for test env with id: "+envId, deactivationButton.isDisplayed());
        WebElement status = Get.byId(Constants.TestEnvironments.environmentStatusIdPrefix + envId);
        assertEquals("Active", status.getText());
    }

    @Test
    public void testApplicationEnvironmentDeactivation() throws Exception {
        SimulatorApi.registerExpectation("environment/activate/get_operational_environments_aai1.json");

        SideMenu.navigateToTestEnvironmentsPage();

        String envId  = "f07ca256-96dd-40ad-b4d2-7a77e2a974ec";
        WebElement deactivationButton = TestEnvironmentPage.getTestEnvironmentDeactivationButton(envId);
        assertTrue("Failed to find Deactivation button for test env with id: "+envId, deactivationButton.isDisplayed());

        deactivationButton.click();

        SimulatorApi.registerExpectation("environment/deactivate/get_operational_environments_aai1.json");
        GeneralUIUtils.findAndWaitByText(Constants.TestEnvironments.environmentDeactivatedSuccesfullyMessage, 60);

        VidBasePage vidBasePage = new VidBasePage();
        vidBasePage.clickCloseButton();

        GeneralUIUtils.ultimateWait();

        WebElement activationButton = TestEnvironmentPage.getTestEnvironmentActivationButton(envId);
        assertTrue("Failed to find Activate button for test env with id: "+envId, activationButton.isDisplayed());
        WebElement status = Get.byId(Constants.TestEnvironments.environmentStatusIdPrefix + envId);
        assertEquals("Inactive", status.getText());
    }

}
