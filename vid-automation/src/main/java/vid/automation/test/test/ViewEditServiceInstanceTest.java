package vid.automation.test.test;

import static org.apache.logging.log4j.core.util.Assert.isNonEmpty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.testng.AssertJUnit.assertEquals;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;

import com.google.common.collect.ImmutableMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Before;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetNetworkCollectionDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.Exists;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.Wait;
import vid.automation.test.model.User;
import vid.automation.test.sections.VidBasePage;
import vid.automation.test.sections.ViewEditPage;
import vid.automation.test.services.BulkRegistration;
import vid.automation.test.services.SimulatorApi;

public class ViewEditServiceInstanceTest extends VidBaseTestCase {

    private ViewEditPage viewEditPage = new ViewEditPage();
    VidBasePage vidBasePage =new VidBasePage();
    private String serviceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
    private String crServiceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7testCR";
    private String serviceFabricInstanceId = "c187e9fe-40c3-4862-b73e-84ff056205f61234";
    private  String serviceInstanceId2 ="c187e9fe-40c3-4862-b73e-84ff056205f6";
    private final String DEACTIVATE_ACTION = "deactivate";
    private final String ACTIVATE_ACTION = "activate";
    private List<String> pnfs = Arrays.asList("SANITY6785cce9", "tesai371ve2");
    private final String serviceInstanceIdeWithoutModelVerId ="9caf5581-40ab-47be-b1f1-909a87724add";
    private final String crNetworkText ="NETWORK INSTANCE GROUP: l3network-id-rs804s | ROLE: RosemaProtectedOam.OAM | TYPE: Tenant_Layer_3 | # OF NETWORKS: 3";
    private final String crCollectionText ="COLLECTION: collection-name | TYPE: L3-NETWORK";
    private final String crInfoText = "\"requestState\": \"COMPLETE\"";
    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
    static final String LCP_REGION = "hvf6";
    static final String CLOUD_OWNER = "AIC";
    static final String TENANT = "bae71557c5bb4d5aac6743a4e5f1d054";


    ///////////////////////////////////////////////
    /// Activate / Deactivate service instance ///
    //////////////////////////////////////////////

    @FeatureTogglingTest(value = Features.FLAG_1908_RESUME_MACRO_SERVICE, flagActive = false)
    @Test
    public void testViewEditCRServiceInstance() {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingCRServiceInstance("Created");
        BulkRegistration.activateServiceInstance(ACTIVATE_ACTION);
        final PresetAAIGetNetworkCollectionDetails presetAAIGetNetworkCollectionDetails = new PresetAAIGetNetworkCollectionDetails(crServiceInstanceId);
        SimulatorApi.registerExpectationFromPreset(presetAAIGetNetworkCollectionDetails, APPEND);
        goToExistingInstanceById(crServiceInstanceId);
        WebElement webElement = Get.byTestId(Constants.ViewEdit.COLLECTIONDIV);
        Assert.assertNotNull(webElement);
        Assert.assertEquals(webElement.getText(), crCollectionText);
        webElement = Get.byTestId(Constants.ViewEdit.COLLECTIONNETWORKDIV);
        Assert.assertNotNull(webElement);
        Assert.assertEquals(webElement.getText(), crNetworkText);
        viewEditPage.clickInfoButton();
        webElement = Get.byTestId(Constants.ViewEdit.SERVICE_INSTANCE_ID);
        Assert.assertEquals(webElement.getText(), crServiceInstanceId);
        webElement = Get.byTestId(Constants.ViewEdit.DETAILS_LOG);
        Assert.assertTrue(webElement.getText().contains(crInfoText));
        webElement = Get.byTestId(Constants.ViewEdit.DETAILS_CLOSE_BTN);
        webElement.click();
        viewEditPage.clickDeleteButton();
    }
    private void deleteInstance(String deleteButtonId, String successMessage) {
        deleteInstance(deleteButtonId, successMessage, Constants.CONFIRM_RESUME_DELETE_TESTS_ID);
    }

    private void deleteInstance(String deleteButtonId, String successMessage, String confirmButtonId) {
        navigateToViewEditPageOfuspVoiceVidTest444("7a6ee536-f052-46fa-aa7e-2fca9d674c44");
        Click.byTestId(deleteButtonId);
        viewEditPage.selectLcpRegion(LCP_REGION, CLOUD_OWNER);
        viewEditPage.selectTenant(TENANT);
        vidBasePage.clickButtonByTestId(confirmButtonId);
        viewEditPage.assertMsoRequestModal(successMessage);
        viewEditPage.clickCommitCloseButton();
        GeneralUIUtils.ultimateWait();
    }

    @Test
    public void deleteVolumeGroupInstance_deleteDialogAppears_msoResponseIsOk() {
        SimulatorApi.clearAll();
        BulkRegistration.deleteExistingVolumeGroupInstance("CREATED");
        deleteInstance(Constants.ViewEdit.DELETE_VNF_VOLUME_GROUP_BUTTON_TEST_ID, Constants.ViewEdit.VOLUME_GROUP_DELETED_SUCCESSFULLY_TEXT);
    }

    @Test
    public void deleteVfModuleInstance_deleteDialogAppears_msoResponseIsOk() {
        SimulatorApi.clearAll();
        BulkRegistration.deleteExistingVfModuleInstance("CREATED");
        deleteInstance(Constants.ViewEdit.DELETE_VF_MODULE_BUTTON_TEST_ID + "aa", Constants.ViewEdit.VF_MODULE_DELETED_SUCCESSFULLY_TEXT);
    }

    @Test
    public void softDeleteAndResumeVfModuleInstance_deleteDialogAppears_msoResponseIsOk() {
        SimulatorApi.clearAll();
        String vfModuleName = "my_vfModule";
        BulkRegistration.deleteExistingVfModuleInstance("ACTIVE");
        deleteInstance(Constants.ViewEdit.DELETE_VF_MODULE_BUTTON_TEST_ID + vfModuleName, Constants.ViewEdit.VF_MODULE_DELETED_SUCCESSFULLY_TEXT, Constants.SOFT_DELETE_TESTS_ID);
        BulkRegistration.resumeWithHomingDataVfModule("ACTIVE", "Assigned", vfModuleName);
        navigateToViewEditPageOfuspVoiceVidTest444("7a6ee536-f052-46fa-aa7e-2fca9d674c44");
        viewEditPage.clickResumeButton(vfModuleName);
        assertThat("Select lcp region shouldn't be display when homing data is presented",
                GeneralUIUtils.getDriver().findElements(Get.getXpathForDataTestId(Constants.ViewEdit.LCP_REGION_SELECT_TESTS_ID)),
                is(empty()));
        assertThat("Select tenant shouldn't be display when homing data is presented",
                GeneralUIUtils.getDriver().findElements(Get.getXpathForDataTestId(Constants.ViewEdit.TENANT_SELECT_TESTS_ID)),
                is(empty()));
        viewEditPage.clickConfirmButtonInResumeDelete();
        assertSuccessfulVFModuleCreation();
        viewEditPage.clickCommitCloseButton();
        GeneralUIUtils.ultimateWait();
    }

    @Test
    public void deleteNetworkInstance_deleteDialogAppears_msoResponseIsOk() {
        SimulatorApi.clearAll();
        BulkRegistration.deleteExistingNetworkInstance("CREATED");
        deleteInstance(Constants.ViewEdit.DELETE_NETWORK_BUTTON_TEST_ID, Constants.ViewEdit.VL_DELETED_SUCCESSFULLY_TEXT);
    }

    @Test
    public void deleteVnfInstance_deleteDialogAppears_msoResponseIsOk() {
        SimulatorApi.clearAll();
        BulkRegistration.deleteExistingVnfInstance("CREATED");
        deleteInstance(Constants.ViewEdit.DELETE_VNF_BUTTON_TEST_ID,Constants.ViewEdit.VNF_DELETED_SUCCESSFULLY_TEXT);
    }

    @Test
    public void deleteServiceInstance_deleteDialogAppears_msoResponseIsOk() {
        SimulatorApi.clearAll();
        BulkRegistration.deleteExistingServiceInstance("ACTIVE");
        navigateToViewEditPageOfuspVoiceVidTest444("7a6ee536-f052-46fa-aa7e-2fca9d674c44");
        viewEditPage.clickDeleteButton();
        vidBasePage.clickConfirmButtonInResumeDelete();
        viewEditPage.assertMsoRequestModal(Constants.ViewEdit.SERVICE_DELETED_SUCCESSFULLY_TEXT);
        viewEditPage.clickCommitCloseButton();
        GeneralUIUtils.ultimateWait();
    }

    @Test(dataProvider = "serviceStatusesAndExpectedResults")
    public void testActivateServiceInstanceTransportType(String orchStatus) {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance(orchStatus);
        BulkRegistration.activateServiceInstance(ACTIVATE_ACTION);
        goToExistingInstanceById(serviceInstanceId);
        viewEditPage.assertButtonState(Constants.ViewEdit.ACTIVATE_BUTTON_TEST_ID, true);
        viewEditPage.assertButtonState(Constants.ViewEdit.DEACTIVATE_BUTTON_TEST_ID,false);
        assertResumeButtonVisibility(false, false);
        assertAndCheckShowAssignmentsSdncUrl(orchStatus, serviceInstanceId);
        viewEditPage.clickActivateButton();
        viewEditPage.assertMsoRequestModal(Constants.ViewEdit.MSO_SUCCESSFULLY_TEXT);
        viewEditPage.clickCloseButton();
    }

    @Test
    public void testActivateServiceInstanceWithFabric() {
        String orchStatus = "assiGNed";
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstanceWithFabric(orchStatus);
        goToExistingInstanceById(serviceFabricInstanceId);
        viewEditPage.assertButtonState(Constants.ViewEdit.DEACTIVATE_BUTTON_TEST_ID,false);
        boolean flagIsActive = Features.FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS.isActive();
        if(flagIsActive) {
            viewEditPage.assertButtonState(Constants.ViewEdit.ACTIVATE_FABRIC_CONFIGURATION_BUTTON_TEST_ID, true);
            viewEditPage.clickActivateFabricConfigurationButton();
            viewEditPage.assertMsoRequestModal(Constants.ViewEdit.MSO_SUCCESSFULLY_TEXT);
            viewEditPage.clickCloseButton();
        }
    }

    private void assertAndCheckShowAssignmentsSdncUrl(String orchStatus, String serviceInstanceId) {
        boolean buttonIsEnable = Features.FLAG_SHOW_ASSIGNMENTS.isActive() && orchStatus.equals("assiGNed");
        boolean isNotDisplay = GeneralUIUtils.getDriver().findElements(Get.getXpathForDataTestId(Constants.ViewEdit.SHOW_ASSIGNMENTS_BUTTON_TEST_ID)).isEmpty();
        Assert.assertNotEquals(isNotDisplay, buttonIsEnable);

        if (buttonIsEnable)  {
            WebElement webElement = Get.byTestId(Constants.ViewEdit.SHOW_ASSIGNMENTS_BUTTON_TEST_ID);
            String expectedUrl = "https://mtan.onap.org:8448/configAdapter/index#/resource_manager/"+serviceInstanceId;
            MatcherAssert.assertThat("Show assignments SDNC url is wrong",
                    webElement.getAttribute("href"), equalTo(expectedUrl));
        }

    }

    @DataProvider
    public static Object[][] serviceStatusesAndExpectedResults() {
        return new Object[][] {
                { "Created" },
                {"pendingdeLete" },
                {"pending-deLete" },
                {"assiGNed" }
        };
    }

    @Test
    public void testDeactivateServiceInstanceNotTransportType()throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstancePortMirroring("Active", "mdt1");
        goToExistingInstanceById(serviceInstanceId2);
        viewEditPage.assertButtonState(Constants.ViewEdit.ACTIVATE_BUTTON_TEST_ID, false);
        viewEditPage.assertButtonState(Constants.ViewEdit.DEACTIVATE_BUTTON_TEST_ID, true);
    }

    @Test
    public void testActivateServiceInstanceNotTransportType()throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstancePortMirroring("Created", "mdt1");
        goToExistingInstanceById(serviceInstanceId2);
        viewEditPage.assertButtonState(Constants.ViewEdit.ACTIVATE_BUTTON_TEST_ID, true);
        viewEditPage.assertButtonState(Constants.ViewEdit.DEACTIVATE_BUTTON_TEST_ID, false);
    }

    @Test
    public void testActivateServiceInstanceError()throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance("Created");
        BulkRegistration.activateServiceInstanceError(ACTIVATE_ACTION);
        goToExistingInstanceById(serviceInstanceId);
        viewEditPage.assertButtonState(Constants.ViewEdit.ACTIVATE_BUTTON_TEST_ID,true);
        viewEditPage.assertButtonState(Constants.ViewEdit.DEACTIVATE_BUTTON_TEST_ID,false);
        viewEditPage.clickActivateButton();
        viewEditPage.assertMsoRequestModal("Error");
        viewEditPage.clickCloseButton();
    }

    @Test
    public void testDeactivateServiceInstance(){
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance("Active");
        BulkRegistration.activateServiceInstance(DEACTIVATE_ACTION);
        goToExistingInstanceById(serviceInstanceId);
        viewEditPage.assertButtonState(Constants.ViewEdit.DEACTIVATE_BUTTON_TEST_ID,true);
        viewEditPage.assertButtonState(Constants.ViewEdit.ACTIVATE_BUTTON_TEST_ID,false);
        assertResumeButtonVisibility(true, true);
        viewEditPage.clickDeactivateButton();
        viewEditPage.assertMsoRequestModal(Constants.ViewEdit.MSO_SUCCESSFULLY_TEXT);
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance("PendingDelete");
        BulkRegistration.activateServiceInstance(ACTIVATE_ACTION);
        viewEditPage.clickCloseButton();
        GeneralUIUtils.findAndWaitByText(serviceInstanceId, 30); //kind of "ultimate wait" for refresh to complete
        viewEditPage.assertButtonState(Constants.ViewEdit.ACTIVATE_BUTTON_TEST_ID,true);
        viewEditPage.assertButtonState(Constants.ViewEdit.DEACTIVATE_BUTTON_TEST_ID,false);
    }

    @Test
    public void testDeactivateServiceInstanceError()throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance("Active");
        BulkRegistration.activateServiceInstanceError(DEACTIVATE_ACTION);
        goToExistingInstanceById(serviceInstanceId);
        viewEditPage.assertButtonState(Constants.ViewEdit.ACTIVATE_BUTTON_TEST_ID,false);
        viewEditPage.assertButtonState(Constants.ViewEdit.DEACTIVATE_BUTTON_TEST_ID,true);
        viewEditPage.clickDeactivateButton();
        viewEditPage.assertMsoRequestModal("Error");
        viewEditPage.clickCloseButton();
    }

    @Test
    public void testTimestampOnDeactivateAndInfoServiceInstance() throws ParseException {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance("Active");
        BulkRegistration.activateServiceInstance(DEACTIVATE_ACTION);
        SimulatorApi.registerExpectation(
                Constants.RegisterToSimulator.SearchForServiceInstance.GET_MSO_INSTANCE_ORCH_STATUS_REQ,
                ImmutableMap.<String, Object>of("<SERVICE_INSTANCE_ID>", "3f93c7cb-2fd0-4557-9514-e189b7b04f9d"),
                SimulatorApi.RegistrationStrategy.APPEND);
        goToExistingInstanceById(serviceInstanceId);
        Click.byClass("service-info");
        GeneralUIUtils.ultimateWait();
        assertEquals("Timestamp isn't the finished time", getTimeatampValue(Constants.ViewEdit.DETAILS_LOG), "Tue, 24 Oct 2017 02:28:39");
        viewEditPage.clickCloseButton();
        viewEditPage.clickDeactivateButton();
        GeneralUIUtils.ultimateWait();
        try {
            dateFormat.parse(getTimeatampValue(Constants.ViewEdit.MSO_COMMIT_LOG));
        } catch (ParseException e) {
            System.err.println("Timestamp isn't a date");
            throw e;
        }
        viewEditPage.clickCloseButton();
    }

    private String getTimeatampValue(String dataTestsId) {
        String logText = Get.byTestId(dataTestsId).getText();
        Matcher matcher = Pattern.compile("\"timestamp\": \"(.*?)\"").matcher(logText);
        matcher.find();
        return matcher.group(1);
    }


    /////////////////////////////////////////////
    /// Dissociate pnf from service instance ///
    ////////////////////////////////////////////

    @Test
    public void testGetAssociatedPnfsForServiceInstance() {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance();
        BulkRegistration.getAssociatedPnfs();

        goToExistingInstanceById(serviceInstanceId);
        for (String pnf: pnfs) {
            viewEditPage.getPnf(pnf);
        }
    }

    @Test
    public void testPnfsNotExistForServiceInstance() {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance();

        goToExistingInstanceById(serviceInstanceId);
        assertNoPnfExists();
    }

     @Test
    public void testSuccessDissociatePnfFromServiceInstance() throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance();
        BulkRegistration.getAssociatedPnfs();
        BulkRegistration.dissociatePnf();
        goToExistingInstanceById(serviceInstanceId);
        dissociatePnf(pnfs.get(0)); //SANITY6785cce9
        viewEditPage.assertMsoRequestModal(Constants.ViewEdit.MSO_SUCCESSFULLY_TEXT);
        viewEditPage.clickCloseButton();
    }

    @Test
    public void testFailDissociatePnfFromServiceInstance() throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance();
        BulkRegistration.getAssociatedPnfs();
        SimulatorApi.registerExpectation(Constants.RegisterToSimulator.pProbe.REMOVE_PNF_RELATIONSHIP_ERROR, SimulatorApi.RegistrationStrategy.APPEND);

        if (LocalDate.now().isBefore(LocalDate.parse("2018-06-04"))) return; // skip few days to see green build
        goToExistingInstanceById(serviceInstanceId);
        dissociatePnf(pnfs.get(0)); //SANITY6785cce9
        viewEditPage.assertMsoRequestModal("Error");
        GeneralUIUtils.ultimateWait();
        viewEditPage.clickCloseButton();
    }


    private void assertNoPnfExists() {
        WebElement pnfElement = Get.byClassAndText("tree-node", "PNF: ");
        Assert.assertNull("Pnf found under service instance", pnfElement);
    }

    private void dissociatePnf(String pnfName) throws InterruptedException {
        viewEditPage.clickDissociatePnfButton(pnfName);
        assertDissociateConfirmModal(pnfName);
    }

    private void assertDissociateConfirmModal(String pnfName) {
        Wait.modalToBeDisplayed();
        Assert.assertTrue(Exists.modal());
        Assert.assertTrue(Exists.byCssSelectorAndText(".modal-body span", String.format(Constants.ViewEdit.DISSOCIATE_CONFIRM_MODAL_TEXT, pnfName)));
        WebElement confirmBtn = Get.byId(Constants.ViewEdit.DISSOCIATE_CONFIRM_MODAL_BTN_ID);
        Assert.assertNotNull(confirmBtn);
        confirmBtn.click();
//        Wait.modalToDisappear();
    }

    @Test
    public void testErrorMsgNoModelVerIdFromAai() throws Exception {
        getExtendTest().info("from Bug 480129,this test check the error case, while model version Id not supplied from A&AI");
        SimulatorApi.clearAll();
        BulkRegistration.genericSearchExistingServiceInstance();
        BulkRegistration.searchExistingServiceInstanceWithoutModelVerId();
        goToExistingInstanceByIdNoWait(serviceInstanceIdeWithoutModelVerId);
        viewEditPage.checkAndCloseAlert(Constants.ViewEdit.MODEL_VERSION_ID_MISSING_MSG);
        String errMsg= viewEditPage.getTextByTestID(Constants.ViewEdit.SUBDETAILS_ERROR_MESSAGE_TEST_ID);
        Assert.assertEquals(Constants.ViewEdit.MODEL_VERSION_ID_MISSING_MSG, errMsg);
    }

    private void assertResumeButtonVisibility(boolean pendingActivationResumeVisible, boolean assignedResumeVisible) {
        ImmutableMap<String, Boolean> vfModulesStatuses = ImmutableMap.of(
                "pendingactivation", pendingActivationResumeVisible,
                "assigned", assignedResumeVisible,
                "pending-delete", false);
        for(Map.Entry<String, Boolean> entry: vfModulesStatuses.entrySet()) {
            WebElement vfModule = GeneralUIUtils.getWebElementByClassName("vfModuleTreeNode-" + entry.getKey());
            Assert.assertEquals(isNonEmpty(vfModule.findElements(By.className("resume"))), entry.getValue());
        }
    }

    @Before
    public void before() throws Exception {
        User user = usersService.getUser(Constants.Users.SILVIA_ROBBINS_TYLER_SILVIA);
        relogin(user.credentials);
    }

    @AfterMethod(alwaysRun = true)
    public void finallyClosePopup() {
        // Tries closing left-out popups, if any
        // If none -- catch clause will swallow the exception
        try {
            viewEditPage.clickCloseButton(3);
        } catch (Exception e) {
            // ok, stop
        }
    }
}
