package vid.automation.test.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static vid.automation.test.Constants.TestEnvironments.REFRESH_BUTTON;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.Exists;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.Input;
import vid.automation.test.infra.SelectOption;
import vid.automation.test.infra.Wait;
import vid.automation.test.model.Environment;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.sections.TestEnvironmentPage;
import vid.automation.test.sections.VidBasePage;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.utils.ReadFile;


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

    public static final String GET_EMPTY_OPERATIONAL_ENVIRONMENT_JSON = "get_empty_operational_environment.json";
    public static final String GET_OPERATIONAL_ENVIRONMENT_JSON = "get_operational_environment.json";
    public static final String GET_FULL_OPERATIONAL_ENVIRONMENT_JSON = "get_full_operational_environment.json";
    public static final String GET_ERROR_OPERATIONAL_ENVIRONMENT_JSON = "get_error_operational_environment.json";
    public static final String POST_OPERATIONAL_ENVIRONMENT_JSON = "post_operational_environment.json";
    static final String ENVIRONMENTS_CONF = "environments";
    static final String NEW_ENVIRONMENT_CONF = "newEnvironment";

    public static final String ENV_ID_FOR_ACTIVATION = "f07ca256-96dd-40ad-b4d2-7a77e2a974eb";
    public static final String ENV_ID_FOR_DEACTIVATION = "f07ca256-96dd-40ad-b4d2-7a77e2a974ec";
    private VidBasePage vidBasePage = new VidBasePage();

    public enum FailureType {
        ACTIVATION_FAILURE,
        DEACTIVATION_FAILURE,
        GET_STATUS_FAILURE
    }

    @BeforeMethod
    //Sometimes we clear registration while we are in view/edit page
    //And there is alert so we can not navigate any more.
    //So we first navigate to welcome page, and only the, clear registration
    public void navigateToWelcome() {
        SideMenu.navigateToWelcomePage();
    }

    @BeforeMethod
    private void clearAllSimulatorExpectations() {
        SimulatorApi.clearAll();
        SimulatorApi.registerExpectation("ecompportal_getSessionSlotCheckInterval.json", SimulatorApi.RegistrationStrategy.APPEND);
    }

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
    public void testTable() {
        SimulatorApi.registerExpectation(GET_OPERATIONAL_ENVIRONMENT_JSON, SimulatorApi.RegistrationStrategy.APPEND);
        vidBasePage.refreshPage();
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
    }

    @Test
    public void testSortTable() {
        SimulatorApi.registerExpectation(GET_OPERATIONAL_ENVIRONMENT_JSON, SimulatorApi.RegistrationStrategy.APPEND);
        vidBasePage.refreshPage();
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
    public void testFilterTable() {
        SimulatorApi.registerExpectation(GET_OPERATIONAL_ENVIRONMENT_JSON, SimulatorApi.RegistrationStrategy.APPEND);
        vidBasePage.refreshPage();
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
    public void testEmptyTableMessage() {
        SimulatorApi.registerExpectation(GET_EMPTY_OPERATIONAL_ENVIRONMENT_JSON, SimulatorApi.RegistrationStrategy.APPEND);
        vidBasePage.refreshPage();
        SideMenu.navigateToTestEnvironmentsPage();
        boolean emptyTableMessage = Exists.byTestId(Constants.TestEnvironments.NO_DATA_MESSAGE);
        Assert.assertTrue(emptyTableMessage);
        Assert.assertFalse(Exists.byTestId(Constants.TestEnvironments.TRY_AGAIN_BUTTON));
        Assert.assertFalse(Exists.byTestId(Constants.TestEnvironments.ENVIRONMENTS_TABLE));
        GeneralUIUtils.ultimateWait();
    }

    @Test
    public void testErrorMessage() {
        SimulatorApi.registerExpectation(GET_ERROR_OPERATIONAL_ENVIRONMENT_JSON, SimulatorApi.RegistrationStrategy.APPEND);
        vidBasePage.refreshPage();
        SideMenu.navigateToTestEnvironmentsPage();
        boolean errorMessage = Exists.byTestId(Constants.TestEnvironments.ERROR_MESSAGE);
        Assert.assertTrue(errorMessage);
        Assert.assertTrue(Exists.byTestId(Constants.TestEnvironments.TRY_AGAIN_BUTTON));
        Assert.assertFalse(Exists.byTestId(Constants.TestEnvironments.ENVIRONMENTS_TABLE));
        GeneralUIUtils.ultimateWait();
    }

    @Test
    public void testNewEnvironmentPopup() {
        SimulatorApi.registerExpectation(GET_OPERATIONAL_ENVIRONMENT_JSON, SimulatorApi.RegistrationStrategy.APPEND);
        SimulatorApi.registerExpectation(POST_OPERATIONAL_ENVIRONMENT_JSON, SimulatorApi.RegistrationStrategy.APPEND);
        SimulatorApi.registerExpectation("environment/status/get_cloud_resources_request_status.json",
                ImmutableMap.of("status_message", Constants.TestEnvironments.environmentCreatedSuccesfullyMessage,
                        "REQUEST-TYPE","Create"), SimulatorApi.RegistrationStrategy.APPEND);
        vidBasePage.refreshPage();
        Environment environment = ReadFile.getJsonFile(NEW_ENVIRONMENT_CONF, Environment.class);
        SideMenu.navigateToTestEnvironmentsPage();
        Click.byTestId(Constants.TestEnvironments.HEADLINE_NEW_BUTTON);
        Wait.modalToBeDisplayed();
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

        if (Features.FLAG_1908_RELEASE_TENANT_ISOLATION.isActive()) {
            SelectOption.byTestIdAndVisibleText(environment.release, Constants.TestEnvironments.ENVIRONMENT_RELEASE);
            environment.workloadContext = environment.workloadContext.concat("_" + environment.release);
        }
        SelectOption.byTestIdAndVisibleText(environment.workloadContext, Constants.TestEnvironments.WORKLOAD_CONTEXT_DROP_DOWN);

        WebElement submitButton = Get.byTestId(Constants.TestEnvironments.SUBMIT_BUTTON);
        Assert.assertTrue(submitButton.isEnabled());
        Click.byTestId(Constants.TestEnvironments.SUBMIT_BUTTON);
        boolean waitForTextResult = Wait.waitByClassAndText("status", Constants.TestEnvironments.environmentCreatedSuccesfullyMessage, 60);
        assertTrue(Constants.TestEnvironments.environmentCreatedSuccesfullyMessage + " message didn't appear on time", waitForTextResult);

        vidBasePage.clickCloseButton();
        GeneralUIUtils.ultimateWait();

        // refresh table
        Map <String, List<List<String>>> file = ReadFile.getJsonFile(ENVIRONMENTS_CONF, Map.class);
        String tableId = Constants.TestEnvironments.ENVIRONMENTS_TABLE;
        SimulatorApi.registerExpectation(GET_FULL_OPERATIONAL_ENVIRONMENT_JSON,
                ImmutableMap.of("new_name", environment.operationalEnvironmentName, "new_tenant", environment.tenantContext, "new_ecomp_id", environment.EcompEnvironmentId, "new_ecomp_name", environment.EcompEnvironmentName, "new_workload_context", environment.workloadContext), SimulatorApi.RegistrationStrategy.APPEND);
        Click.byTestId(REFRESH_BUTTON);
        Wait.angularHttpRequestsLoaded();
        List<List<String>> body = Get.tableBodyValuesByTestId(tableId);
        Assert.assertEquals(file.get(fullListId()), body);
        GeneralUIUtils.ultimateWait();
    }

    @Test
    public void testAaiErrorNewEnvironmentPopup() {
        SimulatorApi.registerExpectation(GET_ERROR_OPERATIONAL_ENVIRONMENT_JSON, SimulatorApi.RegistrationStrategy.APPEND);
        vidBasePage.refreshPage();
        SideMenu.navigateToTestEnvironmentsPage();
        Click.byTestId(Constants.TestEnvironments.HEADLINE_NEW_BUTTON);
        Wait.modalToBeDisplayed();
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
    public void testApplicationEnvironmentActivation() {
        String envId = ENV_ID_FOR_ACTIVATION;
        SimulatorApi.registerExpectation("environment/activate/get_operational_environments_aai1.json", SimulatorApi.RegistrationStrategy.APPEND);
        SimulatorApi.registerExpectation("environment/activate/post_activate_operational_environment.json",
                ImmutableMap.of("ENV-UUID", envId), SimulatorApi.RegistrationStrategy.APPEND);
        SimulatorApi.registerExpectation("environment/status/get_cloud_resources_request_status.json",
                ImmutableMap.of("status_message", Constants.TestEnvironments.environmentActivatedSuccesfullyMessage,
                        "REQUEST-TYPE","Activate"), SimulatorApi.RegistrationStrategy.APPEND);
        clickOnActivationButtonAndUploadFile(envId, "manifest.json");

        Click.byId(Constants.generalSubmitButtonId);
        SimulatorApi.registerExpectation("environment/activate/get_operational_environments_aai1.json",
                ImmutableMap.of("INACTIVE", "ACTIVE"), SimulatorApi.RegistrationStrategy.APPEND);

        boolean waitForTextResult = Wait.waitByClassAndText("status", Constants.TestEnvironments.environmentActivatedSuccesfullyMessage, 60);
        assertTrue(Constants.TestEnvironments.environmentActivatedSuccesfullyMessage + " message didn't appear on time", waitForTextResult);

        vidBasePage.clickCloseButton();

        GeneralUIUtils.ultimateWait();

        WebElement deactivationButton = TestEnvironmentPage.getTestEnvironmentDeactivationButton(envId);
        assertTrue("Failed to find Deactivate button for test env with id: "+envId, deactivationButton.isDisplayed());

        WebElement status = Get.byId(Constants.TestEnvironments.environmentStatusIdPrefix + envId);
        assertEquals("ACTIVE", status.getText());

        WebElement attachButton = TestEnvironmentPage.getTestEnvironmentAttachButton(envId);
        assertTrue("Failed to find Attach button for test env with id: "+envId, attachButton.isDisplayed());

        //make sure page is clickable
        SideMenu.navigateToTestEnvironmentsPage();
    }

    @Test
    public void testApplicationEnvironmentDeactivation() {
        String envId  = ENV_ID_FOR_DEACTIVATION;
        SimulatorApi.registerExpectation("environment/activate/get_operational_environments_aai1.json", SimulatorApi.RegistrationStrategy.APPEND);
        SimulatorApi.registerExpectation("environment/deactivate/post_deactivate_operational_environment.json",
                ImmutableMap.of("ENV-UUID", envId), SimulatorApi.RegistrationStrategy.APPEND);
        SimulatorApi.registerExpectation("environment/status/get_cloud_resources_request_status.json",
                ImmutableMap.of("status_message", Constants.TestEnvironments.environmentDeactivatedSuccesfullyMessage,
                        "REQUEST-TYPE","Deactivate"), SimulatorApi.RegistrationStrategy.APPEND);
        deactivateEnv(envId);

        SimulatorApi.registerExpectation("environment/deactivate/get_operational_environments_aai1.json", SimulatorApi.RegistrationStrategy.APPEND);
        GeneralUIUtils.findAndWaitByText(Constants.TestEnvironments.environmentDeactivatedSuccesfullyMessage, 60);

        vidBasePage.clickCloseButton();

        GeneralUIUtils.ultimateWait();

        WebElement activationButton = TestEnvironmentPage.getTestEnvironmentActivationButton(envId);
        assertTrue("Failed to find Activate button for test env with id: "+envId, activationButton.isDisplayed());

        WebElement status = Get.byId(Constants.TestEnvironments.environmentStatusIdPrefix + envId);
        assertEquals("INACTIVE", status.getText());

        WebElement attachButton = TestEnvironmentPage.getTestEnvironmentAttachButton(envId);
        assertFalse("attach button shouldn't be displayed for test env with id: "+envId, attachButton.isDisplayed());

        SideMenu.navigateToTestEnvironmentsPage();
    }

    private void deactivateEnv(String envId) {
        vidBasePage.refreshPage();
        SideMenu.navigateToTestEnvironmentsPage();
        WebElement deactivationButton = TestEnvironmentPage.getTestEnvironmentDeactivationButton(envId);
        assertTrue("Failed to find Deactivation button for test env with id: "+envId, deactivationButton.isDisplayed());
        deactivationButton.click();
    }

    @DataProvider
    public static Object[][] badManifestProvider() {
        return new Object[][]{
                {"bad_manifest_structure.json","Manifest structure is wrong"},
                {"manifest_with_wrong_recovery_action.json",  "Wrong value for RecoveryAction in manifest. Allowed options are: abort,retry,skip. Wrong value is: leave"}
        };
    }

    @Test
    public void testApplicationEnvironmentActivationBadManifestStructure() {
        testApplicationEnvironmentActivationBadManifestStructure("bad_manifest_structure.json", "Manifest structure is wrong");
        testApplicationEnvironmentActivationBadManifestStructure("manifest_with_wrong_recovery_action.json",
                "Wrong value for RecoveryAction in manifest. Allowed options are: abort, retry, skip. Wrong value is: leave");
    }

    //@Test(dataProvider = "badManifestProvider") TODO : use data provider here (for some reason not work with ui-ci framework)
    public void testApplicationEnvironmentActivationBadManifestStructure(String badManifestFileName, String exceptedErrorMsg) {
        SimulatorApi.registerExpectation("environment/activate/get_operational_environments_aai1.json", SimulatorApi.RegistrationStrategy.APPEND);
        clickOnActivationButtonAndUploadFile(ENV_ID_FOR_ACTIVATION, badManifestFileName);
        WebElement attachButton = Get.byId("submit");
        assertEquals("Wrong text for submit button in activate modal", "Attach", attachButton.getText());
        attachButton.click();
        boolean waitForTextResult = Wait.waitByClassAndText("error", exceptedErrorMsg, 30);
        assertTrue(exceptedErrorMsg+ " message didn't appear on time", waitForTextResult);
        GeneralUIUtils.ultimateWait();
        vidBasePage.clickCloseButton();
        Wait.modalToDisappear();
        SideMenu.navigateToTestEnvironmentsPage();
    }

    @Test
    public void testApplicationEnvironmentActivationNonJsonManifest() {
        SimulatorApi.registerExpectation("environment/activate/get_operational_environments_aai1.json", SimulatorApi.RegistrationStrategy.APPEND);
        String fileName = "non_valid_json.json";
        clickOnActivationButtonAndUploadFile(ENV_ID_FOR_ACTIVATION, fileName);
        WebElement errorLabel = Get.byId("errorLabel");
        assertEquals("wrong error message for non valid json file", "file: " + fileName + " is not a valid JSON", errorLabel.getText());
        vidBasePage.clickCancelButton();
        GeneralUIUtils.ultimateWait();
    }

    private void clickOnActivationButtonAndUploadFile(String envId, String inputFileName) {
        vidBasePage.refreshPage();
        SideMenu.navigateToTestEnvironmentsPage();
        WebElement activationButton = TestEnvironmentPage.getTestEnvironmentActivationButton(envId);
        assertTrue("Failed to find Activate button for test env with id: "+envId, activationButton.isDisplayed());
        activationButton.click();
        updateEnvManifestFile(inputFileName);
        assertTrue("Manifest file name is wrong in test environment activation modal",
                Wait.waitByIdAndText("manifestFileName", inputFileName, 10));
    }

    private void updateEnvManifestFile(String inputFileName) {
        GeneralUIUtils.ultimateWait();
        Input.file("applicationEnvironment/"+inputFileName, "testEnvManifestFileInput");
        GeneralUIUtils.ultimateWait();
    }

    @Test
    public void testAttachManifestFileHappyFlow() {
        String envId = ENV_ID_FOR_ACTIVATION;
        SimulatorApi.registerExpectation("environment/activate/post_activate_operational_environment.json",
                ImmutableMap.of("ENV-UUID", envId), SimulatorApi.RegistrationStrategy.APPEND);
        SimulatorApi.registerExpectation("environment/attachManifest/get_attachable_operational_environment.json", SimulatorApi.RegistrationStrategy.APPEND);
        SimulatorApi.registerExpectation("environment/status/get_cloud_resources_request_status.json",
                ImmutableMap.of("status_message", Constants.TestEnvironments.environmentActivatedSuccesfullyMessage), SimulatorApi.RegistrationStrategy.APPEND);
        vidBasePage.refreshPage();
        SideMenu.navigateToTestEnvironmentsPage();
        WebElement attachButton = TestEnvironmentPage.getTestEnvironmentAttachButton(ENV_ID_FOR_ACTIVATION);
        attachButton.click();

        updateEnvManifestFile("manifest.json");

        Click.byId(Constants.generalSubmitButtonId);

        boolean waitForTextResult = Wait.waitByClassAndText("status", Constants.TestEnvironments.environmentActivatedSuccesfullyMessage, 60);
        assertTrue(Constants.TestEnvironments.environmentActivatedSuccesfullyMessage + " message didn't appear on time", waitForTextResult);

        vidBasePage.clickCloseButton();
    }
    @Test
    public void testApplicationEnvironmentActivationErrorResponseFromMso() throws Exception {
        String payload = "ERROR_PAYLOAD";
        testApplicationEnvironmentActivationErrorResponseFromMso(HttpStatus.INTERNAL_SERVER_ERROR, payload, FailureType.ACTIVATION_FAILURE);
        testApplicationEnvironmentActivationErrorResponseFromMso(HttpStatus.BAD_REQUEST, payload, FailureType.ACTIVATION_FAILURE);
        testApplicationEnvironmentActivationErrorResponseFromMso(HttpStatus.INTERNAL_SERVER_ERROR, payload, FailureType.GET_STATUS_FAILURE);
        testApplicationEnvironmentActivationErrorResponseFromMso(HttpStatus.BAD_REQUEST, payload, FailureType.GET_STATUS_FAILURE);
    }


    public void testApplicationEnvironmentActivationErrorResponseFromMso(HttpStatus errorStatus, String payload, FailureType failureType) {

        String envId = ENV_ID_FOR_ACTIVATION;
        SimulatorApi.registerExpectation("environment/activate/get_operational_environments_aai1.json", SimulatorApi.RegistrationStrategy.APPEND);
        switch (failureType) {
            case ACTIVATION_FAILURE:
                SimulatorApi.registerExpectation("environment/activate/mso_error_response_for_post_operational_environment.json",
                        ImmutableMap.of("ERROR_CODE", errorStatus.value(), "ENV-UUID", envId), SimulatorApi.RegistrationStrategy.APPEND);
                break;
            case GET_STATUS_FAILURE:
                SimulatorApi.registerExpectation("environment/activate/post_activate_operational_environment.json",
                        ImmutableMap.of("ENV-UUID", envId), SimulatorApi.RegistrationStrategy.APPEND);
                break;
        }

        SimulatorApi.registerExpectation("environment/status/get_cloud_resources_request_status_bad_response.json",
                ImmutableMap.of("ERROR_CODE", errorStatus.value(), "ENV-UUID", envId), SimulatorApi.RegistrationStrategy.APPEND);

        clickOnActivationButtonAndUploadFile(envId, "manifest.json");
        Click.byId(Constants.generalSubmitButtonId);
        Wait.waitByClassAndText("error", "System failure", 60);
        Wait.waitByClassAndText("log", errorStatus.getReasonPhrase(), 60);
        Wait.waitByClassAndText("log", payload, 60);
        vidBasePage.clickCloseButton();

        GeneralUIUtils.waitForAngular();

        WebElement status = Get.byId(Constants.TestEnvironments.environmentStatusIdPrefix + envId);
        assertEquals(Constants.INACTIVE, status.getText());
        //make sure page is clickable
        SideMenu.navigateToTestEnvironmentsPage();
    }

    @Test
    public void testApplicationEnvironmentDeactivationErrorResponseFromMso() throws Exception {
        String payload = "<html><head><title>Error</title></head><body>REASON</body></html>";
        testApplicationEnvironmentDeactivationErrorResponseFromMso(HttpStatus.INTERNAL_SERVER_ERROR, payload, FailureType.DEACTIVATION_FAILURE);
        testApplicationEnvironmentDeactivationErrorResponseFromMso(HttpStatus.BAD_REQUEST, payload, FailureType.DEACTIVATION_FAILURE);
        testApplicationEnvironmentDeactivationErrorResponseFromMso(HttpStatus.INTERNAL_SERVER_ERROR, payload, FailureType.GET_STATUS_FAILURE);
        testApplicationEnvironmentDeactivationErrorResponseFromMso(HttpStatus.BAD_REQUEST, payload, FailureType.GET_STATUS_FAILURE);
    }

    public void testApplicationEnvironmentDeactivationErrorResponseFromMso(HttpStatus errorStatus, String payload, FailureType failureType) {
        String envId  = ENV_ID_FOR_DEACTIVATION;
        payload = payload.replace("REASON", errorStatus.getReasonPhrase());
        SimulatorApi.registerExpectation("environment/activate/get_operational_environments_aai1.json", SimulatorApi.RegistrationStrategy.APPEND);
        switch (failureType) {
            case DEACTIVATION_FAILURE:
                SimulatorApi.registerExpectation("environment/deactivate/error_deactivate_operational_environment.json",
                        ImmutableMap.of("ERROR_CODE", errorStatus.value(), "ENV-UUID", envId,"ERROR_PAYLOAD", payload), SimulatorApi.RegistrationStrategy.APPEND);
                break;
            case GET_STATUS_FAILURE:
                SimulatorApi.registerExpectation("environment/deactivate/post_deactivate_operational_environment.json",
                        ImmutableMap.of("ENV-UUID", envId), SimulatorApi.RegistrationStrategy.APPEND);
                break;
        }
        SimulatorApi.registerExpectation("environment/status/get_cloud_resources_request_status_bad_response.json",
                ImmutableMap.of("ERROR_CODE", errorStatus.value(), "ENV-UUID", envId, "ERROR_PAYLOAD", payload), SimulatorApi.RegistrationStrategy.APPEND);
        deactivateEnv(envId);

        Wait.waitByClassAndText("error", "System failure", 60);
        Wait.waitByClassAndText("log", errorStatus.getReasonPhrase(), 60);
        Wait.waitByClassAndText("log", payload, 60);
        vidBasePage.clickCloseButton();

        GeneralUIUtils.ultimateWait();

        WebElement status = Get.byId(Constants.TestEnvironments.environmentStatusIdPrefix + envId);
        assertEquals(Constants.ACTIVE, status.getText());
        //make sure page is clickable
        SideMenu.navigateToTestEnvironmentsPage();
    }

    private String fullListId() {
        return Features.FLAG_1908_RELEASE_TENANT_ISOLATION.isActive() ?
                Constants.TestEnvironments.FULL_LIST : Constants.TestEnvironments.FULL_LIST_WITHOUT_RELEASE_LABEL;
    }



}
