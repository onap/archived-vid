package vid.automation.test.test;


import static java.util.Collections.emptyMap;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsNot.not;
import static org.onap.simulator.presetGenerator.presets.aai.PresetBaseAAICustomQuery.FORMAT.SIMPLE;
import static org.onap.vid.api.BaseApiTest.getResourceAsString;
import static vid.automation.test.infra.Features.FLAG_FLASH_REDUCED_RESPONSE_CHANGEMG;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;

import com.aventstack.extentreports.Status;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.javacrumbs.jsonunit.core.Option;
import org.json.JSONException;
import org.junit.Assert;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.onap.sdc.ci.tests.execute.setup.ExtentTestActions;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetTenants;
import org.onap.simulator.presetGenerator.presets.aai.PresetBaseAAICustomQuery;
import org.onap.simulator.presetGenerator.presets.scheduler.PresetDeleteSchedulerChangeManagement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.Select;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.Exists;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.Input;
import vid.automation.test.infra.SelectOption;
import vid.automation.test.infra.Wait;
import vid.automation.test.model.User;
import vid.automation.test.sections.ChangeManagementPage;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.utils.DB_CONFIG;

public class ChangeManagementTest extends VidBaseTestCase {

    public static final String SCHEDULED_ID = "0b87fe60-50b0-4bac-a0a7-49e951b0ba9e";
    private final String SCHEDULE_ERROR_PREFIX = "Portal not found. Cannot send: ";

    @Test
    public void testLeftPanelChangeManagementButton() {
        Assert.assertTrue(Wait.byText(Constants.SideMenu.VNF_CHANGES));
    }

    @Test
    public void testChangeManagementHeaderLine() {
        ChangeManagementPage.openChangeManagementPage();
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.pageHeadlineId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.headlineNewButtonId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.headlineSearchInputId));
    }

    @Test
    public void testOpenNewChangeManagementModal() {
        ChangeManagementPage.openNewChangeManagementModal();
        Assert.assertTrue(Exists.modal());
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.newModalSubscriberInputId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.newModalServiceTypeInputId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.newModalVNFNameInputId));

        if (Features.FLAG_FLASH_CLOUD_REGION_AND_NF_ROLE_OPTIONAL_SEARCH.isActive()) {
            Assert.assertTrue(Exists.byId(Constants.ChangeManagement.newModalVNFTypeInputId1));
            Assert.assertTrue(Exists.byId(Constants.ChangeManagement.newModalVNFCloudRegion));
            Assert.assertTrue(Exists.byId(Constants.ChangeManagement.newModalVNFSearchVNF));
        } else {
            Assert.assertTrue(Exists.byId(Constants.ChangeManagement.newModalVNFTypeInputId));
        }

        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.newModalFromVNFVersionInputId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.newModalWorkFlowInputId));
        Assert.assertTrue(Exists.byId(Constants.generalSubmitButtonId));
        Assert.assertTrue(Exists.byId(Constants.generalCancelButtonId));
    }

    private void openAndFill1stScreen(String vnfName, String vnfTargetVersion, String workflow) {
        String subscriberId = VNF_DATA_WITH_IN_PLACE.subscriberId;
        String subscriberName = VNF_DATA_WITH_IN_PLACE.subscriberName;
        String serviceType = VNF_DATA_WITH_IN_PLACE.serviceType;
        String vnfType = VNF_DATA_WITH_IN_PLACE.vnfType;
        String cloudRegion = VNF_DATA_WITH_IN_PLACE.cloudRegion;
        String vnfSourceVersion = VNF_DATA_WITH_IN_PLACE.vnfSourceVersion;
        ChangeManagementPage.openNewChangeManagementModal();
        Wait.angularHttpRequestsLoaded();
        SelectOption.waitForOptionInSelect(subscriberName, "subscriberName");
        ChangeManagementPage.selectSubscriberById(subscriberId);
        Wait.angularHttpRequestsLoaded();

        SelectOption.byIdAndVisibleText(Constants.ChangeManagement.newModalServiceTypeInputId, serviceType);
        Wait.angularHttpRequestsLoaded();

        if (Features.FLAG_FLASH_CLOUD_REGION_AND_NF_ROLE_OPTIONAL_SEARCH.isActive()) {
            Input.text(vnfType, Constants.ChangeManagement.newModalVNFTypeInputId);
            SelectOption.byIdAndVisibleText(Constants.ChangeManagement.newModalVNFCloudRegion, cloudRegion);
            Click.byId(Constants.ChangeManagement.newModalVNFSearchVNF);
        } else {
            SelectOption.byIdAndVisibleText(Constants.ChangeManagement.newModalVNFTypeInputId, vnfType);
        }
        Wait.angularHttpRequestsLoaded();

        SelectOption.byIdAndVisibleText(Constants.ChangeManagement.newModalFromVNFVersionInputId, vnfSourceVersion);
        Wait.angularHttpRequestsLoaded();
        Click.byId(Constants.ChangeManagement.newModalVNFNameInputId);
        Click.byText(vnfName);
        // close the multi-select
        Click.byId(Constants.ChangeManagement.newModalVNFNameInputId);
        Wait.angularHttpRequestsLoaded();
        GeneralUIUtils.ultimateWait();

        if (vnfTargetVersion != null) {
            SelectOption.byClassAndVisibleText(Constants.ChangeManagement.newModalTargetVersionInputsClass, vnfTargetVersion);
            Wait.angularHttpRequestsLoaded();
        }
        SelectOption.byIdAndVisibleText(Constants.ChangeManagement.newModalWorkFlowInputId, workflow);

    }

    public void scheduleChange2ndScreen(String duration, String fallback, String concurrencyLimit, String policy) {

        Wait.byText(Constants.ChangeManagement.schedulerModalNowLabel);
        Click.byText(Constants.ChangeManagement.schedulerModalNowLabel);


        SelectOption.byValue(Constants.ChangeManagement.schedulerModalHoursOption, Constants.ChangeManagement.schedulerModalTimeUnitSelectId);

        Input.text(duration, Constants.ChangeManagement.schedulerModalDurationInputTestId);
        Input.text(fallback, Constants.ChangeManagement.schedulerModalFallbackInputTestId);
        Input.text(concurrencyLimit, Constants.ChangeManagement.schedulerModalConcurrencyLimitInputTestId);
        Wait.angularHttpRequestsLoaded();
        SelectOption.byIdAndVisibleText(Constants.ChangeManagement.schedulerModalPolicySelectId, policy);

        Click.byText(Constants.ChangeManagement.schedulerModalScheduleButtonText);

    }

    static class VNF_DATA_WITH_IN_PLACE {
        static final int vnfZrdm3amdns02test2Id = 11822;
        static final int vnfHarrisonKrisId = 12822;
        static String subscriberId = "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb";
        static String subscriberName = "Emanuel";
        static String serviceType = "vRichardson";
        static String vnfType = "vMobileDNS";
        static String cloudRegion = "AAIAIC25 (AIC)";
        static String vnfSourceVersion = "1.0";
        static String vnfName = "zolson3amdns02test2";
        static String vnfTargetVersion = "5.0";
        static String workflowName = "VNF In Place Software Update";
    }

    @AfterClass
    protected void dropSpecialVNFs() {

        resetGetServicesCache();

        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {
            System.out.println("Database connected!");

            Statement stmt = connection.createStatement();
            stmt.addBatch("DELETE FROM `vid_vnf_workflow` WHERE `VNF_DB_ID` = " + VNF_DATA_WITH_IN_PLACE.vnfZrdm3amdns02test2Id);
            stmt.addBatch("DELETE FROM `vid_vnf` WHERE `VNF_DB_ID` = " + VNF_DATA_WITH_IN_PLACE.vnfZrdm3amdns02test2Id);
            int[] executeBatch = stmt.executeBatch();

            stmt = connection.createStatement();
            stmt.addBatch("DELETE FROM `vid_vnf_workflow` WHERE `VNF_DB_ID` = " + VNF_DATA_WITH_IN_PLACE.vnfHarrisonKrisId);
            stmt.addBatch("DELETE FROM `vid_vnf` WHERE `VNF_DB_ID` = " + VNF_DATA_WITH_IN_PLACE.vnfHarrisonKrisId);
            executeBatch = stmt.executeBatch();

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    @BeforeClass
    protected void registerToSimulator() {
        SimulatorApi.clearAll();
        SimulatorApi.registerExpectation(APPEND,
                "changeManagement/ecompportal_getSessionSlotCheckInterval.json"
                , "changeManagement/get_aai_sub_details.json"
                , "changeManagement/get_sdc_catalog_services_2f80c596.json"
                , "changeManagement/get_service-design-and-creation.json"
                , "changeManagement/get_vnf_data_by_globalid_and_service_type.json"
                , "changeManagement/service-design-and-creation.json"
                , "changeManagement/mso_get_manual_task.json"
                , "changeManagement/mso_post_manual_task.json"
                , "changeManagement/mso_get_change_managements_scaleout.json"
        );
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetSubscribersGet(), APPEND);
        if (FLAG_FLASH_REDUCED_RESPONSE_CHANGEMG.isActive()) {
            String AAI_VNFS_FOR_CHANGE_MANAGEMENT_JSON_BY_PARAMS = "registration_to_simulator/changeManagement/get_vnf_data_by_globalid_and_service_type_with_modelVer.json";
            String globalCustomerId = "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb";
            String serviceType = "vRichardson";
            SimulatorApi.registerExpectationFromPreset(new PresetBaseAAICustomQuery(
                    SIMPLE,
                    "/business/customers/customer/" + globalCustomerId + "/service-subscriptions/service-subscription/"
                            + serviceType + "/service-instances",
                    "query/vnfs-fromServiceInstance-filterByCloudRegion?nfRole=vMobileDNS&cloudRegionID=AAIAIC25"
            ) {
                @Override
                public Object getResponseBody() {
                    return getResourceAsString(
                            AAI_VNFS_FOR_CHANGE_MANAGEMENT_JSON_BY_PARAMS);
                }
            }, APPEND);
        }


        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetSubscribersGet(), SimulatorApi.RegistrationStrategy.APPEND);

        if (Features.FLAG_FLASH_CLOUD_REGION_AND_NF_ROLE_OPTIONAL_SEARCH.isActive()) {
            SimulatorApi.registerExpectationFromPreset(new PresetAAIGetTenants(
                            VNF_DATA_WITH_IN_PLACE.subscriberId,
                            VNF_DATA_WITH_IN_PLACE.serviceType),
                    SimulatorApi.RegistrationStrategy.APPEND);

        }

        registerDefaultTablesData();
        resetGetServicesCache();
    }

    private void registerDefaultTablesData() {
        SimulatorApi.registerExpectation(
                new String[]{"changeManagement/get_scheduler_details_short.json",
                        "changeManagement/mso_get_change_managements.json"
                        , "changeManagement/delete_scheduled_task.json"},
                ImmutableMap.of(
                        "<SCHEDULE_ID>", SCHEDULED_ID,
                        "<IN_PROGRESS_DATE>", "Fri, 08 Sep 2017 19:34:32 GMT"), APPEND
        );
    }

    @BeforeClass
    protected void prepareSpecialVNFs() {

        dropSpecialVNFs();

        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {

            System.out.println("Database connected!");

            ///////////////////////////////
            // Add 2 vnfs with some workflows
            Statement stmt = connection.createStatement();
            stmt.addBatch("INSERT INTO `vid_vnf` (`VNF_DB_ID`, `VNF_APP_UUID`, `VNF_APP_INVARIANT_UUID`) " +
                    "VALUES (" + VNF_DATA_WITH_IN_PLACE.vnfZrdm3amdns02test2Id + ", '76e908e0-5201-44d2-a3e2-9e6128d05820', '72e465fe-71b1-4e7b-b5ed-9496118ff7a8')");
            stmt.addBatch("INSERT INTO `vid_vnf_workflow` (`VNF_DB_ID`, `WORKFLOW_DB_ID`) VALUES (" + VNF_DATA_WITH_IN_PLACE.vnfZrdm3amdns02test2Id + ", 2)");
            stmt.addBatch("INSERT INTO `vid_vnf_workflow` (`VNF_DB_ID`, `WORKFLOW_DB_ID`) VALUES (" + VNF_DATA_WITH_IN_PLACE.vnfZrdm3amdns02test2Id + ", 3)");
            stmt.addBatch("INSERT INTO `vid_vnf_workflow` (`VNF_DB_ID`, `WORKFLOW_DB_ID`) VALUES (" + VNF_DATA_WITH_IN_PLACE.vnfZrdm3amdns02test2Id + ", 4)");
            int[] executeBatch = stmt.executeBatch();
            assertThat(Ints.asList(executeBatch), everyItem(greaterThan(0)));

            stmt = connection.createStatement();
            stmt.addBatch("INSERT INTO `vid_vnf` (`VNF_DB_ID`, `VNF_APP_UUID`, `VNF_APP_INVARIANT_UUID`) " +
                    "VALUES (" + VNF_DATA_WITH_IN_PLACE.vnfHarrisonKrisId + ", '0903e1c0-8e03-4936-b5c2-260653b96413', '00beb8f9-6d39-452f-816d-c709b9cbb87d')");
            stmt.addBatch("INSERT INTO `vid_vnf_workflow` (`VNF_DB_ID`, `WORKFLOW_DB_ID`) VALUES (" + VNF_DATA_WITH_IN_PLACE.vnfHarrisonKrisId + ", 1)");
            stmt.addBatch("INSERT INTO `vid_vnf_workflow` (`VNF_DB_ID`, `WORKFLOW_DB_ID`) VALUES (" + VNF_DATA_WITH_IN_PLACE.vnfHarrisonKrisId + ", 2)");
            executeBatch = stmt.executeBatch();
            assertThat(Ints.asList(executeBatch), everyItem(greaterThan(0)));

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }

    }

    @Override
    protected UserCredentials getUserCredentials() {

        String userName = Constants.Users.EMANUEL_vWINIFRED;
        User user = usersService.getUser(userName);
        return new UserCredentials(user.credentials.userId, user.credentials.password, userName, "", "");
    }

    private void updateConfigFile(String fileName) {
        Assert.assertFalse(Exists.byId(Constants.ChangeManagement.newModalConfigUpdateInputId));
        openAndFill1stScreen(VNF_DATA_WITH_IN_PLACE.vnfName, VNF_DATA_WITH_IN_PLACE.vnfTargetVersion, "VNF Config Update");
        Assert.assertFalse(Get.byId(Constants.generalSubmitButtonId).isEnabled());
        Input.file("changeManagement/" + fileName, Constants.ChangeManagement.newModalConfigUpdateInputId);
        GeneralUIUtils.ultimateWait();
    }

    @Test
    public void regretToCancelWorkflowOnPendingPopUp() {
        updateSimulatorWithParametersOfScheduledJod("get_scheduler_details_short.json");
        ChangeManagementPage.openChangeManagementPage();

        Wait.angularHttpRequestsLoaded();
        ChangeManagementPage.clickOnRefreshButton();

        GeneralUIUtils.ultimateWait();
        clickAndAssertOnCancelButton(SCHEDULED_ID);
        Click.byClass("pull-right modal-close");
        //TODO: if refresh button functional will be change to refactor next line.
        ChangeManagementPage.clickOnRefreshButton();

        assertAndCheckStatusCellOnDeletedSheduledJob(SCHEDULED_ID, "cancel-action icon-pending");
    }

    @Test
    public void clickOnScheduledJob_SuccessfulMessageAppear() {

        SimulatorApi.registerExpectationFromPreset(new PresetDeleteSchedulerChangeManagement(), APPEND);

        ChangeManagementPage.openChangeManagementPage();
        GeneralUIUtils.ultimateWait();
        clickAndAssertOnCancelButton(SCHEDULED_ID);
        updateSimulatorWithParametersOfScheduledJod("get_scheduler_details_short_with_after_cancel" +
                ".json");

        clickAndAssertClickOnCancelWorkflowButtonOnPendingPopUp();

        GeneralUIUtils.ultimateWait();
        //TODO: To develop automatic table refresh to avoid click on refresh button.
        ChangeManagementPage.clickOnRefreshButton();
        assertCorrectJobDeleted("ctsf0002v");

        assertAndCheckStatusCellOnDeletedSheduledJob(SCHEDULED_ID, "ng-hide");


    }

    private void clickAndAssertOnCancelButton(String scheduledID) {
        Wait.waitByTestId("icon-status-" + scheduledID, 5);
        Click.byTestId("icon-status-" + scheduledID);
        GeneralUIUtils.ultimateWait();
        WebElement cancelPendingConfirmationMessage = Get.byTestId("btn-cancel-workflow");
        assertThat(cancelPendingConfirmationMessage.getText(), containsString("Are you sure you want to delete workflow"));
        screenshot("delete workflow");
    }

    private void clickAndAssertClickOnCancelWorkflowButtonOnPendingPopUp() {

        try {
            Click.byClass(Constants.ChangeManagement.pendingModalCancelWorkflowButtonClass);
            GeneralUIUtils.ultimateWait();
            Assert.assertTrue(Exists.byClassAndText(Constants.generalModalTitleClass, "Success"));
        } finally {
            if (Exists.byClassAndText("modal-title", "Pending")) {
                Click.byClass("pull-right modal-close");
            }
        }
        screenshot("cancel workflow");
        Click.byClassAndVisibleText("btn", "OK");
    }

    private void assertCorrectJobDeleted(String vnfName) {
        WebElement canceledScheduledJobRow = GeneralUIUtils.getWebElementByTestID("pending-table-cm-row");
        String scheduledVnfName = ((RemoteWebElement) canceledScheduledJobRow).findElementsByTagName("td").get(1).getText();
        String scheduledState = ((RemoteWebElement) canceledScheduledJobRow).findElementsByTagName("td").get(5).getText();
        Assert.assertEquals(vnfName, scheduledVnfName);
        Assert.assertEquals("Deleted", scheduledState);
    }

    private void assertAndCheckStatusCellOnDeletedSheduledJob(String scheduledId, String classString) {
        boolean isNotDisplayed = GeneralUIUtils.waitForElementInVisibilityByTestId("icon-status-" + scheduledId, 5);
        Assert.assertTrue(isNotDisplayed);
    }

    public void updateSimulatorWithParametersOfScheduledJod(String jasonFile) {
        SimulatorApi.registerExpectation(
                new String[]{"changeManagement/" + jasonFile},
                ImmutableMap.of("<SCHEDULE_ID>", SCHEDULED_ID), APPEND
        );
    }

    @FeatureTogglingTest(value = Features.FLAG_HANDLE_SO_WORKFLOWS, flagActive = false)
    @Test
    public void testWorkflowVNFInPlaceSoftwareUpdateNotInWorkflowsListWhenNotExpected() {

        List<String> workflows = getListOfWorkflowsFor("Harrison Kris");
        assertThat(workflows, not(hasItem(VNF_DATA_WITH_IN_PLACE.workflowName)));
    }

    @FeatureTogglingTest(value = Features.FLAG_HANDLE_SO_WORKFLOWS, flagActive = false)
    @Test
    public void testWorkflowVNFInPlaceSoftwareUpdateInWorkflowsListWhenExpected() {
        List<String> workflows = getListOfWorkflowsFor(VNF_DATA_WITH_IN_PLACE.vnfName);
        assertThat(workflows, hasItem(VNF_DATA_WITH_IN_PLACE.workflowName));
    }

    public void openAndFill1stScreenWithWorkflowVNFInPlaceSoftwareUpdate() {
        openAndFill1stScreen(VNF_DATA_WITH_IN_PLACE.vnfName, VNF_DATA_WITH_IN_PLACE.vnfTargetVersion, VNF_DATA_WITH_IN_PLACE.workflowName);
    }

    @AfterMethod(alwaysRun = true)
    public void screenshotAndCloseForm(ITestResult testResult) {

        screenshot(testResult.getName() + (testResult.isSuccess() ? "" : " FAIL"));

        // Tries closing left-out popups, if any
        // If none -- catch clause will swallow the exception
        try {
            Click.byId(Constants.generalCancelButtonId);
            Click.byId(Constants.generalCancelButtonId);
            Click.byId(Constants.generalCancelButtonId);
        } catch (Exception | Error e) {
            // ok, stop
        }
        Wait.modalToDisappear();
    }

    protected void screenshot(String testName) {
        try {
            ExtentTestActions.addScreenshot(Status.INFO, "ChangeManagementTest." + testName, testName);
        } catch (IOException e) {
            rethrow(e);
        }
    }

    @Test
    public void testWorkflowVNFInPlaceSoftwareUpdateShows3Fields() {

        openAndFill1stScreenWithWorkflowVNFInPlaceSoftwareUpdate();

        List<String> idsWithoutMatchingElement =
                Stream.of(
                        "internal-workflow-parameter-text-2-operations-timeout",
                        "internal-workflow-parameter-text-3-existing-software-version",
                        "internal-workflow-parameter-text-4-new-software-version")
                        .filter(id -> Get.byId(id) == null)
                        .collect(Collectors.toList());
        assertThat("all three special VNFInPlace fields should appear", idsWithoutMatchingElement, is(empty()));

        assertThat(Get.byId(Constants.generalSubmitButtonId).isEnabled(), is(false));
    }

    @Test
    public void testWorkflowVNFInPlaceSoftwareUpdate3ValidValues() {
        openAndFill1stScreenWithWorkflowVNFInPlaceSoftwareUpdate();

        final String[][] options = {
                {"true", "111", "222", "333"}
                , {"true", "14710454", "Cz-Ou0EK5eH9.gAK1", "G9bUiFX3QM8xpxF8TlZ7b5T0"}
                , {"true", "25316893", "fMx9V5kp.5.JGtYRhNGVTPoJ", "Jv5IieY0kTNjkfZ64bHXngR6"}
                , {"true", "8", "3t3MhTRqkyjB85o5NC9OacAw", "B.bJ6f7KYI6WzDMR0fyNM9r4"}
                , {"true", "3176", "ZlRS7tczf0cbMxQbBfyc6AP5", "1G1"}
                , {"true", "78058488", "n", "WkH"}
                , {"true", "501778", "1.d74LrJbBmcR.7bfvH.UZMX", "tFTAel7PS4RKEJeJ0b6mTeVT"}
                , {"true", "76639623", "m2.EhbBxRE.rJj3j6qDtMxGR", "Rgkm-EPM1K0KAWm43Ex1wwjj"}
                , {"true", "91244280", "zPDHRrXW65xR6GV.gVZND8C0", "mkrqFG26m7Vmv-28etQVyp04"}
                , {"true", "8966", "7k2sRK2qSFRVCFpEvrlbmxAL", "IlvfmWTqzpF0Jo3elpZPHXx"}
                , {"true", "01303495", "G26yl8B0NbLIKxu23h86QbZz", "vSou1seqCrcv9KoVbhlj4Wa4"}
                , {"true", "787", "ce7joKCHYowpM2PtCb53Zs2v", ".qw1oY9HKjfAF2Yt05JNgib9"}
                , {"true", "40116583", "-3bDEzEn.RbNnT2hWKQqf2HL", "QzlKlKZiIpc7sQ.EzO"}
                , {"false", "", "222", "333"}
                , {"false", "111", "", "333"}
                , {"false", "111", "222", ""}
                , {"false", "111a", "222", "333"}
                , {"false", "aaa", "222", "333"}
                , {"false", "111-", "222", "333"}
//                , {"false", " 111", "222", "333"}
//                , {"false", "111", "222 ", "333"}
//                , {"false", "111", "222", " 333"}
                , {"false", "111", "222", "3 33"}
                , {"false", "111", "22,2", "333"}
                , {"false", "111", "222~", "333"}
                , {"false", "111", "222", "333&"}
                , {"false", "$", "222", "333"}
                , {"false", "111", "@", "333"}
                , {"false", "111", "222", "^^^^^^"}
        };

        for (String[] option : options) {
            fillVNFInPlace3Fields(option[1], option[2], option[3]);
            assertThat("failed for set: " + Arrays.toString(option),
                    Get.byId(Constants.generalSubmitButtonId).isEnabled(), is(Boolean.parseBoolean(option[0])));
        }

    }

    private void fillVNFInPlace3Fields(String operationsTimeout, String existingSwVersion, String newSwVersion) {
        Get.byId("internal-workflow-parameter-text-2-operations-timeout").clear();
        Get.byId("internal-workflow-parameter-text-3-existing-software-version").clear();
        Get.byId("internal-workflow-parameter-text-4-new-software-version").clear();

        Get.byId("internal-workflow-parameter-text-2-operations-timeout").sendKeys(operationsTimeout);
        Get.byId("internal-workflow-parameter-text-3-existing-software-version").sendKeys(existingSwVersion);
        Get.byId("internal-workflow-parameter-text-4-new-software-version").sendKeys(newSwVersion);
    }

    private List<String> getListOfWorkflowsFor(String vnfName) {

        openAndFill1stScreen(vnfName, null /*no matter*/, "Replace");

        Select selectlist = new Select(Get.byId("workflow"));
        List<String> workflows = selectlist.getOptions().stream().map(we -> we.getText()).collect(Collectors.toList());

        Click.byId(Constants.generalCancelButtonId);

        return workflows;
    }

    @DataProvider
    public static Object[][] dataForUpdateWorkflowPartialWithInPlace() {
        return new Object[][]{
                {"1111", "22222", "33333"}
                , {"8", "3t3MhTRqkyjB85o5NC9OacAw", "B.bJ6f7KYI6Wz-----DMR0.fyNM9r4"}
                , {"78058488", "n", "WkH"}
        };
    }

    @Test(dataProvider = "dataForUpdateWorkflowPartialWithInPlace")
    public void testVidToMsoCallbackDataWithInPlaceSWUpdate(String operationsTimeout, String existingSwVersion, String newSwVersion) {

        openAndFill1stScreenWithWorkflowVNFInPlaceSoftwareUpdate();
        fillVNFInPlace3Fields(operationsTimeout, existingSwVersion, newSwVersion);

        assertThatVidToPortalCallbackDataIsOk(VNF_DATA_WITH_IN_PLACE.workflowName, ImmutableMap.of(
                "existingSoftwareVersion", existingSwVersion,
                "newSoftwareVersion", newSwVersion,
                "operationTimeout", operationsTimeout
        ));
    }

    @Test
    public void testUploadConfigUpdateFile() {
        String fileName = "configuration_file.csv";
        updateConfigFile(fileName);
        Assert.assertEquals(Get.byId(Constants.ChangeManagement.newModalConfigUpdateInputId + "-label").getText(), fileName);
        Assert.assertTrue(Get.byId(Constants.generalSubmitButtonId).isEnabled());
        assertThatVidToPortalCallbackDataIsOk("VNF Config Update", ImmutableMap.of(
                "configUpdateFile",
                "{\"request-parameters\":{\"vm\":[{\"vnfc\":["
                        + "{\"vnfc-name\":\"ibcx0001vm001dbg001\",\"vnfc-function-code\":\"dbg\"}],\"vm-name\":\"ibcx0001vm001\"},"
                        + "{\"vnfc\":[{\"vnfc-name\":\"ibcx0001vm002dbg001\"}],\"vm-name\":\"ibcx0001vm002\"}]},\"configuration-parameters\":{\"node0_hostname\":\"dbtx0001vm001\"}}"
        ));
    }

    @FeatureTogglingTest(value = Features.FLAG_FLASH_CLOUD_REGION_AND_NF_ROLE_OPTIONAL_SEARCH, flagActive = false)
    @Test
    public void testUploadConfigUpdateNonCsvFile() {
        String fileName = "non-valid.json";
        updateConfigFile(fileName);
        WebElement errorLabel = Get.byId("errorLabel");
        Assert.assertEquals("wrong error message for non csv file", "Invalid file type. Please select a file with a CSV extension.", errorLabel.getText());
        Assert.assertFalse(Get.byId(Constants.generalSubmitButtonId).isEnabled());
    }

    @Test(dataProvider = "invalidCsvFiles")
    public void testUploadInvalidConfigUpdateFile(String fileName) {
        updateConfigFile(fileName);
        WebElement errorLabel = Get.byId("errorContentLabel");
        Assert.assertEquals("wrong error message for non csv file", "Invalid file structure.", errorLabel.getText());
        Assert.assertFalse(Get.byId(Constants.generalSubmitButtonId).isEnabled());
    }

    @DataProvider
    public static Object[][] invalidCsvFiles() {
        return new Object[][]{
                {"emptyFile.csv"},
                {"withoutPayload.csv"},
                {"withoutConfigurationParameters.csv"},
                {"withoutRequestParameters.csv"}
        };
    }

    @Test
    public void testVidToMsoCallbackData() {
        String workflow = "Replace";

        openAndFill1stScreen(VNF_DATA_WITH_IN_PLACE.vnfName, VNF_DATA_WITH_IN_PLACE.vnfTargetVersion, workflow);

        assertThatVidToPortalCallbackDataIsOk(workflow, emptyMap());
    }

    private void assertThatVidToMsoCallbackDataIsOk(String workflow, String payload) {
        Assert.assertTrue(Get.byId(Constants.generalSubmitButtonId).isEnabled());
        Click.byId(Constants.generalSubmitButtonId);

        String vidToMsoCallbackData = Input.getValueByTestId("vidToMsoCallbackData");

        String modelInvariantId = "72e465fe-71b1-4e7b-b5ed-9496118ff7a8";
        String vnfInstanceId = "8e5e3ba1-3fe6-4d86-966e-f9f03dab4855";
        String expected = getExpectedVidToMsoCallbackData(modelInvariantId, vnfInstanceId, VNF_DATA_WITH_IN_PLACE.vnfName, VNF_DATA_WITH_IN_PLACE.vnfTargetVersion, workflow, payload);

        try {
            JSONAssert.assertEquals("built mso request is not ok", expected, vidToMsoCallbackData, JSONCompareMode.STRICT);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Click.byId(Constants.generalCancelButtonId);
    }

    private void assertThatVidToPortalCallbackDataIsOk(String workflowName, Map<String, String> workflowParams) {
        screenshot("submit to scheduler");
        Assert.assertTrue(Get.byId(Constants.generalSubmitButtonId).isEnabled());
        Click.byId(Constants.generalSubmitButtonId);

        String errorMessage = GeneralUIUtils.getWebElementByTestID("error-message", 2).getText();

        String modelInvariantId = "72e465fe-71b1-4e7b-b5ed-9496118ff7a8";
        String vnfInstanceId = "8e5e3ba1-3fe6-4d86-966e-f9f03dab4855";

        assertThat(errorMessage, startsWith(SCHEDULE_ERROR_PREFIX));
        assertThat(errorMessage.replace(SCHEDULE_ERROR_PREFIX, ""), jsonEquals(
                ImmutableMap.of(
                        "widgetName", "Portal-Common-Scheduler",
                        "widgetParameter", "",
                        "widgetData", ImmutableMap.builder()
                                .put("vnfNames", ImmutableList.of(ImmutableMap.of(
                                        "id", vnfInstanceId,
                                        "invariant-id", modelInvariantId,
                                        "version", "5.0"
                                )))
                                .put("workflowParameters", emptyMap())
                                .put("subscriberId", "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb")
                                .put("fromVNFVersion", "" + "76e908e0-5201-44d2-a3e2-9e6128d05820" + "")
                                .put("workflow", "" + workflowName + "")
                                .put("policyYN", "Y")
                                .put("sniroYN", "Y")
                                .put("testApi", "GR_API")
                                .put("vnfType", "vMobileDNS")
                                .putAll(workflowParams)
                                .build()
                )
        ).when(Option.IGNORING_EXTRA_FIELDS));


        Click.byId(Constants.generalCancelButtonId);
    }

    @Test(enabled = false)
    public void testUpdateWorkflowNow() {

        String workflow = "Update";

        String duration = "1";
        String fallback = "1";
        String concurrencyLimit = "1";
        String policy = "SNIRO_1710.Config_MS_PlacementOptimizationPolicy_dhv_v1.1.xml";

        openAndFill1stScreen(VNF_DATA_WITH_IN_PLACE.vnfName, VNF_DATA_WITH_IN_PLACE.vnfTargetVersion, workflow);
        Assert.assertTrue(Get.byId(Constants.generalSubmitButtonId).isEnabled());
        Click.byId(Constants.generalSubmitButtonId);

        scheduleChange2ndScreen(duration, fallback, concurrencyLimit, policy);
    }

    @Test
    public void testNewChangeManagementCreation() {
        ChangeManagementPage.openChangeManagementPage();

        //TODO: After scheduler will be ready than we will examine if the creation working fine.
    }

    @Test
    public void testMainDashboardTable() {
        ChangeManagementPage.openChangeManagementPage();
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.dashboardActiveTabId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.dashboardFinishedTabId));

        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.dashboardActiveTableId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.dashboardInProgressTheadId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.dashboardPendingTheadId));

        Click.byId(Constants.ChangeManagement.dashboardFinishedTabId);
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.dashboardFinishedTableId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.dashboardFinishedTheadId));

        Click.byId(Constants.ChangeManagement.dashboardActiveTabId);
    }

    @Test
    public void testFinishedSectionIncludeUnlockedItem() {
        ChangeManagementPage.openChangeManagementPage();
        Click.byId(Constants.ChangeManagement.dashboardFinishedTabId);
        Assert.assertThat(Get.byClassAndText("vnf-name", "Unlocked instance"), is(notNullValue()));
    }

    @Test
    public void testMainDashboardTableContent() {
        ChangeManagementPage.openChangeManagementPage();
        GeneralUIUtils.ultimateWait();
        List<WebElement> webElements = Get.multipleElementsByTestId(Constants.ChangeManagement.activeTableRowId);
        assertThat("List of pending workflows is empty", webElements, is(not(empty())));
        //TODO: After scheduler will be ready than we will examine if the content is valid.
    }

    @Test
    public void testOnlyOneModalIsOpen() throws Exception {

        updateSimulatorWithParametersOfScheduledJod("mso_get_change_managements.json");

        ChangeManagementPage.openChangeManagementPage();

        Wait.byText("ReplaceVnfInfra");
        GeneralUIUtils.ultimateWait();


        List<WebElement> elements = Get.byClass(Constants.ChangeManagement.pendingIconClass);
        Assert.assertTrue(elements != null && elements.size() > 0);

        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView();", elements.get(0));


        elements.get(0).click();

        GeneralUIUtils.ultimateWait();

        elements = Get.byClass(Constants.ChangeManagement.pendingIconClass);
        Assert.assertTrue(elements != null && elements.size() > 0);
        elements.get(2).click();

        GeneralUIUtils.ultimateWait();
        List<WebElement> webElements = Get.byClass("modal-dialog");
        Assert.assertTrue(webElements.size() == 1);

    }

    @Test(enabled = false)
    public void testOpenFailedStatusModal() {
        ChangeManagementPage.openChangeManagementPage();

        if (!Exists.byClass(Constants.ChangeManagement.failedIconClass)) {
            //TODO: Create a job which will shown with status fail.
        }

        Click.byClass(Constants.ChangeManagement.failedIconClass);
        Wait.modalToBeDisplayed();
        Assert.assertTrue(Exists.modal());
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.failedModalHeaderId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.failedModalContentId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.failedModalRetryButtonId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.failedModalRollbackButtonId));

        Click.byClass(Constants.generalCloseModalButtonClass);
        Wait.modalToDisappear();
    }

    @Test(enabled = false)
    public void testOpenInProgressStatusModal() {
        ChangeManagementPage.openChangeManagementPage();

        if (!Exists.byClass(Constants.ChangeManagement.processIconClass)) {
            //TODO: Create a job which will shown with status in-progress.
        }

        Click.byClass(Constants.ChangeManagement.processIconClass);
        Wait.modalToBeDisplayed();
        Assert.assertTrue(Exists.modal());
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.inProgressModalHeaderId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.inProgressModalContentId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.inProgressModalStopButtonId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.inProgressModalRollbackButtonId));

        Click.byClass(Constants.generalCloseModalButtonClass);
        Wait.modalToDisappear();
    }

    @Test(enabled = false)
    public void testOpenAlertStatusModal() {
        ChangeManagementPage.openChangeManagementPage();

        if (!Exists.byClass(Constants.ChangeManagement.alertIconClass)) {
            //TODO: Create a job which will shown with status alert.
        }

        Click.byClass(Constants.ChangeManagement.alertIconClass);
        Wait.modalToBeDisplayed();
        Assert.assertTrue(Exists.modal());
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.alertModalHeaderId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.alertModalContentId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.alertModalContinueButtonId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.alertModalRollbackButtonId));

        Click.byClass(Constants.generalCloseModalButtonClass);
        Wait.modalToDisappear();
    }

    @Test(enabled = false)
    public void testOpenPendingStatusModal() {
        ChangeManagementPage.openChangeManagementPage();

        if (!Exists.byClass(Constants.ChangeManagement.pendingIconClass)) {
            //TODO: Create a job which will shown with status pending.
        }

        Click.byClass(Constants.ChangeManagement.pendingIconClass);
        Wait.modalToBeDisplayed();
        Assert.assertTrue(Exists.modal());
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.pendingModalHeaderId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.pendingModalContentId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.pendingModalRescheduleButtonId));
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.pendingModalRollbackButtonId));

        Click.byClass(Constants.generalCloseModalButtonClass);
        Wait.modalToDisappear();
    }

    @Test
    public void testSuccessCancelPendingWorkflow() {
        ChangeManagementPage.openChangeManagementPage();
        Wait.angularHttpRequestsLoaded();

        Click.byClass(Constants.ChangeManagement.cancelPendingButtonClass); //cancel pending workflow modal
        Wait.modalToBeDisplayed();
        Assert.assertTrue(Exists.modal());
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.pendingModalHeaderId));
        Assert.assertTrue(Exists.byClass(Constants.ChangeManagement.pendingModalCancelWorkflowButtonClass));
        screenshot("pendingModalCancelWorkflow");
        Click.byClass(Constants.ChangeManagement.pendingModalCancelWorkflowButtonClass);
        Wait.angularHttpRequestsLoaded();

        Wait.modalToBeDisplayed(); //success alert modal should appear
        Assert.assertTrue(Exists.modal());
        Assert.assertTrue(Exists.byId(Constants.ChangeManagement.alertModalHeaderId));
        Assert.assertTrue(Exists.byClassAndText(Constants.generalModalTitleClass, "Success"));
        screenshot("successCancelWorkflow");
        Click.byClass(Constants.generalCloseModalButtonClass);
        Wait.modalToDisappear();
        //TODO check the workflow deleted from table/changed to deleted action
    }

    @Test
    public void testRefreshPageButton() {
        ChangeManagementPage.openChangeManagementPage();
        GeneralUIUtils.ultimateWait();
        List<WebElement> pendingRows = Get.multipleElementsByTestId(Constants.ChangeManagement.pendingTableRowId);
        List<WebElement> activeRows = Get.multipleElementsByTestId(Constants.ChangeManagement.activeTableRowId);
        assertThat("The pending table has no content", pendingRows, is(not(empty())));
        assertThat("The active table has no content", activeRows, is(not(empty())));
        Click.byTestId(Constants.ChangeManagement.refreshBtnTestId);
        GeneralUIUtils.ultimateWait();
        pendingRows = Get.multipleElementsByTestId(Constants.ChangeManagement.pendingTableRowId);
        assertThat("The pending table has no content", pendingRows, is(not(empty())));
        assertThat("The active table has no content", activeRows, is(not(empty())));
        //return the register requests to the default state
        registerDefaultTablesData();
    }

    private String getExpectedVidToMsoCallbackData(String modelInvariantId, String vnfInstanceId, String vnfName, String vnfTargetVersion, String workflow, String payload) {
        return "" +
                "{" +
                "  \"requestType\": \"" + workflow + "\"," +
                "  \"requestDetails\": [" +
                "    {" +
                "      \"vnfName\": \"" + vnfName + "\"," +
                "      \"vnfInstanceId\": \"" + vnfInstanceId + "\"," +
                "      \"modelInfo\": {" +
                "        \"modelType\": \"vnf\"," +
                "        \"modelInvariantId\": \"" + modelInvariantId + "\"," +
                "        \"modelVersionId\": \"76e908e0-5201-44d2-a3e2-9e6128d05820\"," +
                "        \"modelName\": \"" + vnfName + "\"," +
                "        \"modelVersion\": \"" + vnfTargetVersion + "\"," +
                "        \"modelCustomizationId\": \"c00e8fc8-af39-4da8-8c78-a7efc2fe5994\"" +
                "      }," +
                "      \"cloudConfiguration\": {" +
                "        \"lcpCloudRegionId\": \"mdt1\"," +
                "        \"tenantId\": \"88a6ca3ee0394ade9403f075db23167e\"" +
                "      }," +
                "      \"requestInfo\": {" +
                "        \"source\": \"VID\"," +
                "        \"suppressRollback\": false," +
                "        \"requestorId\": \"az2016\"" +
                "      }," +
                "      \"relatedInstanceList\": [" +
                "        {" +
                "          \"relatedInstance\": {" +
                "            \"instanceId\": \"97315a05-e6f3-4c47-ae7e-d850c327aa08\"," +
                "            \"modelInfo\": {" +
                "              \"modelType\": \"service\"," +
                "              \"modelInvariantId\": \"e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0\"," +
                "              \"modelVersionId\": \"76e908e0-5201-44d2-a3e2-9e6128d05820\"," +
                "              \"modelName\": \"action-data\"," +
                "              \"modelVersion\": \"1.0\"" +
                "            }" +
                "          }" +
                "        }" +
                "      ]," +
                "      \"requestParameters\": {" +
                payload +
                "        \"usePreload\": true" +
                "      }" +
                "    }" +
                "  ]" +
                "}";
    }

}
