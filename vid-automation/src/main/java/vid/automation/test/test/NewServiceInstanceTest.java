package vid.automation.test.test;

import com.google.common.collect.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.opencomp.simulator.presetGenerator.presets.mso.PresetMSOBaseCreateServiceInstancePost;
import org.opencomp.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceGen2;
import org.opencomp.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.openecomp.sdc.ci.tests.datatypes.UserCredentials;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import vid.automation.test.Constants;
import vid.automation.test.Constants.BrowseASDC.NewServicePopup;
import vid.automation.test.infra.*;
import vid.automation.test.model.Service;
import vid.automation.test.model.User;
import vid.automation.test.sections.*;
import vid.automation.test.services.AsyncJobsService;
import vid.automation.test.services.ServicesService;
import vid.automation.test.services.SimulatorApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;
import static vid.automation.test.infra.Features.FLAG_ASYNC_INSTANTIATION;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;

@FeatureTogglingTest(FLAG_ASYNC_INSTANTIATION)
public class NewServiceInstanceTest extends CreateInstanceDialogBaseTest {

    private ServicesService servicesService = new ServicesService();
    private DrawingBoardPage drawingBoardPage = new DrawingBoardPage();
    List<String> serviceModelLabelList = Arrays.asList("Model version", "Description", "Category", "UUID",
            "Invariant UUID", "Service type", "Service role");
    List<String> mandatoryServiceModelLabelList = Arrays.asList("Model version", "UUID", "Invariant UUID");
    private final String defaultServiceModelForMostTests = "6e59c5de-f052-46fa-aa7e-2fca9d674c44";
    private final VidBasePage vidBasePage = new VidBasePage();

    @BeforeClass
    protected void dropAllAsyncJobs() {
        AsyncJobsService asyncJobsService = new AsyncJobsService();
        asyncJobsService.dropAllAsyncJobs();
    }

    @AfterClass
    protected void muteAllAsyncJobs() {
        AsyncJobsService asyncJobsService = new AsyncJobsService();
        asyncJobsService.muteAllAsyncJobs();
    }

    @BeforeMethod
    protected void refreshPage() {
        GeneralUIUtils.ultimateWait();

        vidBasePage.refreshPage();
    }

    @Override
    protected UserCredentials getUserCredentials() {
        String userName = Constants.Users.USP_VOICE_VIRTUAL_USP;
        User user = usersService.getUser(userName);
        return new UserCredentials(user.credentials.userId, user.credentials.password, userName, "", "");
    }

    @Test
    public void createNewServiceInstance_fullModelData_LeftPaneLabelsCorrect() throws Exception {
        loadMacroServicePopup_noDynamicFields_fullModelDetails_ecompNamingFalse();
        assertServiceModelLabelsCorrect(serviceModelLabelList);
    }

    @Test
    public void createNewServiceInstance_partialModelData_LeftPaneLabelsCorrect() throws Exception {
        loadMacroServicePopup_withDynamicFields_partialModelDetails_ecompNamingTrue();
        assertServiceModelLabelsCorrect(mandatoryServiceModelLabelList);
    }

    /**
     * asserts that the provided labels list is visible and that no other detail item appears in the model details panel.
     */
    protected void assertServiceModelLabelsCorrect(List<String> serviceModelLabelList) throws Exception {
        WebElement modelInformation = getDriver().findElement(By.id("model-information"));
        List<WebElement> modelInformationItems = modelInformation.findElements(By.xpath("./div"));
        assertEquals(modelInformationItems.size(), serviceModelLabelList.size());
        serviceModelLabelList.forEach(label -> {
            WebElement webElement = Get.byTestId("model-item-" + label);
            WebElement itemWarpper = webElement.findElements(By.className("wrapper")).get(0);
            assertEquals(itemWarpper.findElements(By.tagName("label")).get(0).getText(), label, "model details item label is incorrect.");
        });
    }

    @Test
    public void createNewServiceInstance_leftPane_serviceModelDataCorrect() {
        Service service = servicesService.getService(NewServicePopup.SERVICE_UUID);
        String prefix = NewServicePopup.SERVICE_MODEL_DATA_TEST_ID_VALUE_PREFIX;
        loadMacroServicePopup_noDynamicFields_fullModelDetails_ecompNamingFalse();
        setNewInstance_leftPane_assertModelDataCorrect(NewServicePopup.SERVICE_MODEL_FIELD_TO_DATA_TESTS_ID, prefix, service);
    }

    @Test
    public void createNewServiceInstance_macro_validPopupDataAndUI__ecompNamingFalse() {

        ServiceData serviceData = new ServiceData("csar-noDynamicFields-ecompNamingFalse-fullModelDetails-1a80c596.zip",
                "1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd",
                new ArrayList<>(),
                false, true, true, true,
                "2017-488_ADIOD-vPE 0",
                "2017488_adiodvpe0..2017488AdiodVpe..ADIOD_vRE_BV..module-1", 0, 1,  new ArrayList<>());

        String serviceInstanceName = deployServiceInstance(serviceData, false);
        vidBasePage.screenshotDeployDialog(serviceInstanceName);
        deployAndVerifyModuleInPendingTable(serviceInstanceName);
        verifyOpenAuditInfo(serviceInstanceName);
        verifyOpenViewEdit(serviceInstanceName);
        verifyDeleteJob(serviceInstanceName);
        verifyHideJob(serviceInstanceName);
    }

    @Test
    public void createNewServiceInstance_macro_validPopupDataAndUI__ecompNamingServiceFalseVnfTrue_vgNameFalse() {
        ServiceData serviceData = new ServiceData("csar-noDynamicFields-ecompNamingFalse-fullModelDetails-1a80c596.zip",
                "1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd",
                new ArrayList<>(),
                false, true, false, true,
                "2017-488_ADIOD-vPE 0",
                "2017488_adiodvpe0..2017488AdiodVpe..ADIOD_base_vPE_BV..module-0", 1, 1, new ArrayList<>());

        deployServiceInstance(serviceData);
    }

    @Test
    public void createNewServiceInstance_macro_validPopupDataAndUI__ecompNamingServiceFalseVnfFalse_vgNameFalse() {
        ServiceData serviceData = new ServiceData("csar-noDynamicFields-ecompNamingFalse-fullModelDetails-1a80c596-vnfEcompNamingFalse.zip",
                "4a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd",
                new ArrayList<>(),
                false, false, false, false,
                "2017-488_ADIOD-vPE 0",
                "2017488_adiodvpe0..2017488AdiodVpe..ADIOD_base_vPE_BV..module-0", 1, 1, new ArrayList<>());

        deployServiceInstance(serviceData);
    }

    @Test
    public void createNewServiceInstance_macro_validPopupDataAndUI__ecompNamingServiceFalseVnfFalse_vgNameTrue() {
        ServiceData serviceData = new ServiceData("csar-noDynamicFields-ecompNamingFalse-fullModelDetails-1a80c596-vnfEcompNamingFalse.zip",
                "4a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd",
                new ArrayList<>(),
                false, false, true, false,
                "2017-488_ADIOD-vPE 0",
                "2017488_adiodvpe0..2017488AdiodVpe..ADIOD_vRE_BV..module-1", 0, 1, new ArrayList<>());

        deployServiceInstance(serviceData);
    }

    private void deployServiceInstance(ServiceData serviceData) {
        deployServiceInstance(serviceData, true);
    }

    private String deployServiceInstance(ServiceData serviceData, boolean tryCancelsAndReentries) {
        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);

        final String serviceInstanceName = createSriovService(
                user, serviceData.dynamicFields, serviceData.csarName, serviceData.modelUuid,
                serviceData.isGeneratedNaming, serviceData.multiStageDesign,
                tryCancelsAndReentries);

        createVnf(serviceData.vnfData.isGeneratedNaming, serviceData.vnfData.vnfName, serviceInstanceName, tryCancelsAndReentries);

        createVfModule(
                serviceData.vfData.vfName, serviceData.vnfData.isGeneratedNaming,
                serviceData.vfData.vgEnabled, serviceData.vfData.vfMin, serviceData.vfData.vfMax,
                serviceData.vfData.dynamicFields, serviceInstanceName);

        return serviceInstanceName;
    }


    @Test
    public void createNewServiceInstance_macro_validPopupDataAndUI__ecompNamingTrue() {

        List<String> serviceDynamicFields = Arrays.asList("2017488 adiodvpe0 asn:");
        ServiceData serviceData = new ServiceData("csar-withDynamicFields-ecompNamingTrue-partialModelDetails.zip",
                "2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd",
                serviceDynamicFields,
                true, true, false, false,
                "2017-488_ADIOD-vPE 0",
                "2017488_adiodvpe0..2017488AdiodVpe..ADIOD_vRE_BV..module-1", 0, 1, new ArrayList<>());

        deployServiceInstance(serviceData);

    }

    private void deployAndVerifyModuleInPendingTable(String serviceInstanceName) {
        drawingBoardPage.clickDeployButton();

        boolean simulatorUpdated = false;

        vidBasePage.goToIframe();
        GeneralUIUtils.ultimateWait();
        int[] ids = {1, 1, 2, 3};
        String[] statuses = {"IN_PROGRESS", "COMPLETED", "IN_PROGRESS", "PENDING"};
        for (int i = 0; i < ids.length; i++) {
            String actualInstanceName = getActualInstanceName(serviceInstanceName, ids[i], statuses[i]);

            final Set<String> expectedStatuses = ImmutableSet.of(statuses[i]);
            ServiceStatusChecker serviceStatusChecker = new ServiceStatusChecker(actualInstanceName, expectedStatuses);
            boolean statusIsShown = Wait.waitFor(serviceStatusChecker, null, 20, 2);
            final String assertionMessage = String.format("service %s: none of rowClasses [%s] is in expectedStatuses: [%s]  ",
                    actualInstanceName,
                    String.join(",", serviceStatusChecker.getColumnClassesSet()),
                    String.join(",", expectedStatuses));

            assertTrue(assertionMessage, statusIsShown);

            InstantiationStatusPage.assertInstantiationStatusRow(
                    actualInstanceName, expectedRowFields(actualInstanceName));

            if (!simulatorUpdated) {
                simulatorUpdated = true;
                final String request1 = PresetMSOBaseCreateServiceInstancePost.DEFAULT_REQUEST_ID;
                final String request2 = "ce010256-3fdd-4cb5-aed7-37112a2c6e93";
                SimulatorApi.registerExpectationFromPreset(new PresetMSOCreateServiceInstanceGen2(request2), APPEND);
                SimulatorApi.registerExpectationFromPreset(new PresetMSOOrchestrationRequestGet("IN_PROGRESS", request2), APPEND);
                SimulatorApi.registerExpectationFromPreset(new PresetMSOOrchestrationRequestGet("COMPLETE", request1), APPEND);
            }
        }
        vidBasePage.screenshotDeployDialog(serviceInstanceName);
    }

    private String getActualInstanceName(String serviceInstanceName, Integer i, String status) {
        return "PENDING".equals(status) ? serviceInstanceName : serviceInstanceName + "_00" + i;
    }

    private void verifyOpenViewEdit(String serviceInstanceName) {
        boolean[] openEnabled = {true, false, false};
        String[] statuses = {"COMPLETED", "IN_PROGRESS", "PENDING"};
        ImmutableList.of(1, 2, 3).forEach(i -> {
            String actualInstanceName = getActualInstanceName(serviceInstanceName, i, statuses[i - 1]);
            checkMenuItem(actualInstanceName, Constants.InstantiationStatus.CONTEXT_MENU_HEADER_OPEN_ITEM, openEnabled[i - 1], contextMenuOpen -> {
                Click.byTestId(contextMenuOpen);
                vidBasePage.goOutFromIframe();
                GeneralUIUtils.ultimateWait();

                Wait.byText("View/Edit Service Instance");
                Wait.byText("Add node instance");
                Wait.byText("i'm a port");
                Wait.byText("f8791436-8d55-4fde-b4d5-72dd2cf13cfb");

                vidBasePage.screenshotDeployDialog("view-edit-" + actualInstanceName);
                SideMenu.navigateToMacroInstantiationStatus();
            });
        });
    }


    private void verifyOpenAuditInfo(String serviceInstanceName) {
        boolean auditInfoEnabled = true;
        String[] statuses = {"COMPLETED", "IN_PROGRESS", "PENDING"};
        for (Integer i : ImmutableList.of(1, 2, 3)) {
            String actualInstanceName = getActualInstanceName(serviceInstanceName, i, statuses[i - 1]);
            checkMenuItem(actualInstanceName, Constants.InstantiationStatus.CONTEXT_MENU_HEADER_AUDIT_INFO_ITEM, auditInfoEnabled, contextMenuOpen -> {
                Click.byTestId(contextMenuOpen);
                checkAuditInfoModal(actualInstanceName, i, statuses);
            });
            final WebElement row = InstantiationStatusPage.getInstantiationStatusRow(actualInstanceName);
            row.findElement(By.id(Constants.InstantiationStatus.TD_JOB_STATUS_ICON + "-" + (i - 1))).click();
            checkAuditInfoModal(actualInstanceName, i, statuses);
        }
    }

    private void checkAuditInfoModal(String actualInstanceName, Integer i, String[] statuses) {

        Wait.waitByTestId("vidJobStatus", 10);

        WebElement webElement = Get.byTestId("model-item-value-serviceInstanceName");
        assertEquals(webElement.getText(), actualInstanceName, "Service Instance Name must be equal");

        WebElement vidTableElement = Get.byId("service-instantiation-audit-info-vid");
        assertEquals(3, vidTableElement.findElement(By.tagName("thead")).findElements(By.tagName("th")).size(), "VID table must contain 3 columns");

        List<WebElement> vidStatusesElements = vidTableElement.findElements(By.id("vidJobStatus"));
        List<String> vidStatuses = vidStatusesElements.stream()
                .map(s ->
                        convertUITextCapitalizeAndFormatPipe(s.getText()))
                .collect(Collectors.toList());

        List<String> serviceStatus = Arrays.asList(Arrays.copyOfRange(statuses, i - 1, statuses.length));
        assertThat("statuses for " + actualInstanceName + " must be as expected", vidStatuses, is(Lists.reverse(serviceStatus)));

        String dateString = vidTableElement.findElements(By.id("vidStatusTime")).get(0).getText();
        assertTrue("vid Status Time column must contains valid date in format : MMM dd, yyyy HH:mm", isDateValid(dateString, "MMM dd, yyyy HH:mm"));

        WebElement MSOTableElement = Get.byId("service-instantiation-audit-info-mso");
        assertEquals(4, MSOTableElement.findElement(By.tagName("thead")).findElements(By.tagName("th")).size(), "MSO table must contain 4 columns");

        if (statuses[i - 1].equals("PENDING")) {
            assertEquals(0, MSOTableElement.findElement(By.tagName("tbody")).findElements(By.tagName("tr")).size(), "When status is PENDING MSO table is empty");
        }

        vidBasePage.screenshotDeployDialog("audit-info-" + actualInstanceName);
        Click.byId(Constants.AuditInfoModal.CANCEL_BUTTON);
    }

    private String convertUITextCapitalizeAndFormatPipe(String text) {
        return text.toUpperCase().replace("-", "_");
    }

    private boolean isDateValid(String dateToValidate, String dateFromat) {

        if (dateToValidate == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);
        try {
            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void verifyDeleteJob(String serviceInstanceName) {
        boolean[] deleteEnabled = {false, false, true};
        String[] statuses = {"COMPLETED", "IN_PROGRESS", "PENDING"};
        verifyDeleteOrHideOperation(serviceInstanceName, Constants.InstantiationStatus.CONTEXT_MENU_DELETE, statuses, deleteEnabled, "deleted");
    }

    private void verifyHideJob(String serviceInstanceName) {
        boolean[] hideEnabled = {true, false};
        String[] statuses = {"COMPLETED", "IN_PROGRESS"};
        verifyDeleteOrHideOperation(serviceInstanceName, Constants.InstantiationStatus.CONTEXT_MENU_HIDE, statuses, hideEnabled, "hidden");
    }

    private void verifyDeleteOrHideOperation(String serviceInstanceName, String contextMenuItem, String[] statuses, boolean[] operationEnabled, String operationName) {
        for (int i = 1; i <= statuses.length; i++) {
            String actualInstanceName = getActualInstanceName(serviceInstanceName, i, statuses[i - 1]);
            checkMenuItem(actualInstanceName, contextMenuItem, operationEnabled[i - 1], contextMenuDelete -> {
                Click.byTestId(contextMenuDelete);
                GeneralUIUtils.ultimateWait();
                assertNull(actualInstanceName + " should be " + operationName,
                        InstantiationStatusPage.getInstantiationStatusRow(actualInstanceName));
            });
        }
        vidBasePage.screenshotDeployDialog(serviceInstanceName);
    }

    private void checkMenuItem(String actualInstanceName, String contextMenuItem, boolean shouldBeEnabled, Consumer<String> doIfEnabled) {
        Wait.waitFor(name -> {
            if (null == InstantiationStatusPage.getInstantiationStatusRow(name)) {
                InstantiationStatusPage.clickRefreshButton();
                return false;
            } else {
                return true;
            }
        }, actualInstanceName, 8, 1);
        final WebElement row = InstantiationStatusPage.getInstantiationStatusRow(actualInstanceName);
        row.findElement(By.className("menu-div")).click();
        String clazz = Get.byXpath("//div[@data-tests-id='" + contextMenuItem + "']/ancestor::li").getAttribute("class");
        assertThat("item " + contextMenuItem + " of " + actualInstanceName +
                " should be " + (shouldBeEnabled ? "enabled" : "disabled"), !clazz.equals("disabled"), is(shouldBeEnabled));
        if (shouldBeEnabled) {
            doIfEnabled.accept(contextMenuItem);
        } else {
            // dismiss menu
            Get.byClass("title").get(0).click();
        }
    }

    private ImmutableMap<String, String> expectedRowFields(String actualInstanceName) {
        return ImmutableMap.<String, String>builder()
                .put("userId", getUserCredentials().getUserId())
                .put("serviceModelName", "action-data")
                .put("serviceInstanceName", actualInstanceName)
                .put("serviceModelVersion", "1.0")
                .put("subscriberName", "USP VOICE")
                .put("serviceType", "VIRTUAL USP")
                .put("regionId", "mtn6")
                .put("tenantName", "AIN Web Tool-15-D-testgamma")
                .put("aicZoneName", "NFTJSSSS-NFT1")
                .put("project", "DFW")
                .put("owningEntityName", "MetroPacketCore")
                .put("pause", "false")
                .build();
    }

    private String createSriovService(User user, List<String> serviceDynamicFields,
                                      String csarName, String modelId, boolean modelWithGeneratedName,
                                      boolean multiStageDesign, boolean tryCancelsAndReentries) {
        BrowseASDCPage browseASDCPage = new BrowseASDCPage();

        // simulate typing with spaces, but expected is without spaces
        String serviceInstanceNameWithSpaces = RandomStringUtils.randomAlphabetic(5) + " instance name";
        String serviceInstanceName = serviceInstanceNameWithSpaces.replace(" ", "");

        List<String> cycles = tryCancelsAndReentries ? ImmutableList.of("WILL_CANCEL", "AFTER_CANCEL") : ImmutableList.of("SINGLE_SHOT");
        cycles.forEach(cycle -> {
            if ("AFTER_CANCEL".equals(cycle)) {
                loadServicePopupOnBrowseASDCPage(modelId);
            } else {
                loadServicePopup(csarName, modelId);
            }

            WebElement instanceName = Get.byId("instance-name");
            if (modelWithGeneratedName) {
                Assert.assertNull(instanceName, "instance name input should be invisible when serviceEcompNaming == true.");
            } else {
                instanceName.sendKeys(serviceInstanceNameWithSpaces);
            }

            //serviceType should be dependent on subscriber selection
            assertElementDisabled("service-type-select");

            GeneralUIUtils.ultimateWait();
            assertDropdownPermittedItemsByLabel(user.subscriberNames, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
            VidBasePage.selectSubscriberById("e433710f-9217-458d-a79d-1c7aff376d89");
            GeneralUIUtils.ultimateWait();

            //lcpRegion should be dependent on serviceType selection
            assertElementDisabled("lcpRegion-select");

            String serviceType = "VIRTUAL USP";
            Wait.waitByClassAndText(Constants.CreateNewInstance.SERVICE_TYPE_OPTION_CLASS, serviceType, 30);
            browseASDCPage.selectServiceTypeByName(serviceType);

            //tenant should be dependent on lcpRegion selection
            assertElementDisabled("tenant-select");

            String lcpRegion = "mtn6";
            Wait.waitByClassAndText("lcpRegionOption", lcpRegion, 30);
            browseASDCPage.selectLcpRegion(lcpRegion);

            browseASDCPage.selectTenant("bae71557c5bb4d5aac6743a4e5f1d054");

            String setButtonTestId = "service-form-set";
            assertSetButtonDisabled(setButtonTestId);

            SelectOption.byTestIdAndVisibleText("MetroPacketCore", (Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID));
            assertSetButtonDisabled(setButtonTestId);

            SelectOption.byTestIdAndVisibleText("DHV", Constants.ViewEdit.PRODUCT_FAMILY_SELECT_TESTS_ID);
            assertSetButtonEnabled(setButtonTestId);

            browseASDCPage.selectProductFamily("e433710f-9217-458d-a79d-1c7aff376d89");

            assertAllIsPermitted(Constants.BrowseASDC.AIC_OPTION_CLASS);
            browseASDCPage.selectAicZone("NFT1");

            SelectOption.byTestIdAndVisibleText("DFW", Constants.OwningEntity.PROJECT_SELECT_TEST_ID);

            assertNotificationAreaVisibilityBehaviour();

            assertPauseOnPausePointsVisibility(multiStageDesign);

            validateDynamicFields(serviceDynamicFields);

            vidBasePage.screenshotDeployDialog("createSriovService-" + serviceInstanceName);

            if ("WILL_CANCEL".equals(cycle)) {
                Click.byTestId(Constants.CANCEL_BUTTON_TEST_ID);
            } else {
                Click.byTestId(setButtonTestId);
            }

            browseASDCPage.goOutFromIframe();

            browseASDCPage.goToIframe();

        });
        return serviceInstanceName;
    }

    private void createVnf(boolean isInstanceNameGenerated, String vnfName, String serviceInstanceName, boolean tryCancelsAndReentries) {
        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        drawingBoardPage.clickAddButtonByNodeName(vnfName);

        if (isInstanceNameGenerated) {
            WebElement instanceName = Get.byId("instance-name");
            Assert.assertNull(instanceName, "instance name input should be invisible when serviceEcompNaming == true.");
        } else {
            Input.text("VNF instance name", "instanceName");
        }

        //tenant should be dependent on lcpRegion selection
        assertElementDisabled("tenant-select");

        WebElement legacyRegion = Get.byTestId("lcpRegionText");
        Assert.assertNull(legacyRegion, "legacy region shouldn't be visible when lcp region isn't AAIAIC25,rdm3 or rdm5a.");

        browseASDCPage.selectLcpRegion("AAIAIC25");

        legacyRegion = Get.byTestId("lcpRegionText");
        Assert.assertNotNull(legacyRegion, "legacy region should be visible when lcp region is AAIAIC25,rdm3 or rdm5a.");

        browseASDCPage.selectTenant("092eb9e8e4b7412e8787dd091bc58e86");

        String setButtonTestId = "vnf-form-set";
        assertSetButtonDisabled(setButtonTestId);

        browseASDCPage.selectPlatform("platform");

        assertSetButtonEnabled(setButtonTestId);

        browseASDCPage.setLegacyRegion("some legacy region");
        browseASDCPage.selectLineOfBusiness("ECOMP");

        Wait.waitByTestId("model-item-value-subscriberName", 10);
        Assert.assertEquals(Get.byTestId("model-item-value-subscriberName").getText(), "USP VOICE", "Subscriber name should be shown in vf module");
        Assert.assertEquals(Get.byTestId("model-item-value-min"), null, "Min value should not be shown in VNF popup");
        Assert.assertEquals(Get.byTestId("model-item-value-max"), null, "Max value should not be show in VNF popup");
        if (!isInstanceNameGenerated) {
            Assert.assertEquals(Get.byTestId("model-item-value-serviceName").getText(), serviceInstanceName, "Subscriber name should be shown in vf module");
        }

        vidBasePage.screenshotDeployDialog("createVnf-" + serviceInstanceName);
        Click.byTestId(setButtonTestId);

        String nodeToEdit = "69e09f68-8b63-4cc9-b9ff-860960b5db09-2017-488_ADIOD-vPE 0";
        if (tryCancelsAndReentries) {
            hoverAndClickEditButton(nodeToEdit);

            Wait.byText("VIRTUAL USP");
            GeneralUIUtils.ultimateWait();
            Assert.assertEquals(Get.selectedOptionText(Constants.ViewEdit.LCP_REGION_SELECT_TESTS_ID), "AAIAIC25");
            Assert.assertEquals(Get.selectedOptionText(Constants.ViewEdit.TENANT_SELECT_TESTS_ID), "USP-SIP-IC-24335-T-01");
            Assert.assertEquals(Get.selectedOptionText(Constants.ViewEdit.LINE_OF_BUSINESS_SELECT_TESTS_ID), "ECOMP");
            Assert.assertEquals(Get.selectedOptionText(Constants.OwningEntity.PLATFORM_SELECT_TEST_ID), "platform");
            Click.byTestId(Constants.CANCEL_BUTTON_TEST_ID);
        } else {
            toggleItemInTree(nodeToEdit);
        }
    }

    private void toggleItemInTree(String nodeToEdit) {
        Click.byXpath("//tree-root[@data-tests-id='" + Constants.DrawingBoard.AVAILABLE_MODELS_TREE + "']//span[@class='" + Constants.DrawingBoard.TOGGLE_CHILDREN + "']");
    }

    private void hoverAndClickEditButton(String nodeToEdit) {
        String buttonOfEdit = Constants.DrawingBoard.NODE_PREFIX + nodeToEdit + Constants.DrawingBoard.CONTEXT_MENU_BUTTON;
        GeneralUIUtils.hoverOnAreaByTestId(buttonOfEdit);
        Click.byTestId(buttonOfEdit);
        Click.byTestId(Constants.DrawingBoard.CONTEXT_MENU_ITEM);
    }

    private void createVfModule(String vfModuleName, boolean isInstanceNameGenerated, boolean vgEnabled, int vgMin,
                                int vgMax, List<String> vfModuleDynamicFields, String serviceInstanceName) {
        String setButtonTestId = "vnf-form-set";
        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
//        Click.byTestId(Constants.DrawingBoard.NODE_PREFIX + vnfName);
        drawingBoardPage.clickAddButtonByNodeName(vfModuleName);
        if (!isInstanceNameGenerated) {
            Input.text("VF instance name", "instanceName");
        } else {
            Assert.assertNull(Get.byTestId("instanceName"));
            Assert.assertNull(Get.byTestId("volumeGroupName"));

        }
        if (vgEnabled && !isInstanceNameGenerated) {
            browseASDCPage.setInputText("volumeGroupName", "some group name name");
        } else {
            Assert.assertNull(Get.byTestId("volumeGroupName"), "volumeGroupName input should be invisible when vgEnabled == false");
        }
        Wait.waitByTestId("model-item-value-subscriberName", 10);
        Assert.assertEquals(Get.byTestId("model-item-value-subscriberName").getText(), "USP VOICE", "Subscriber name should be shown in vf module");
        Assert.assertEquals(Get.byTestId("model-item-value-min").getText(), Integer.toString(vgMin), "Min should be shown");
        Assert.assertEquals(Get.byTestId("model-item-value-max").getText(), Integer.toString(vgMax), "Max should be shown");
        if (!isInstanceNameGenerated) {
            Wait.byText(serviceInstanceName);
            Assert.assertEquals(Get.byTestId("model-item-value-serviceName").getText(), serviceInstanceName, "Service name should be shown in vf module");
        }
        validateDynamicFields(vfModuleDynamicFields);

        browseASDCPage.screenshotDeployDialog("createVfModule-" + serviceInstanceName);
        Click.byTestId(setButtonTestId);
    }

    private void assertPauseOnPausePointsVisibility(boolean visibility) {
        WebElement pauseElem = Get.byId("pause");
        final String assertionMessage = "pause on pause points visibility should be " + visibility;
        if (visibility) {
            Assert.assertNotNull(pauseElem, assertionMessage);
        } else {
            Assert.assertNull(pauseElem, assertionMessage);
        }
    }

    private void assertNotificationAreaVisibilityBehaviour() {
        WebElement webElement = Get.byId("notification-area");
        Assert.assertNull(webElement, "notification area should be invisible if only 1 qty.");

        SelectOption.byIdAndVisibleText("quantity-select", "3");

        webElement = Get.byId("notification-area");
        Assert.assertNotNull(webElement, "notification area should be visible if more then 1 qty.");
    }

    /**
     * following are two popup service models that represent two opposite/extreme states of the popup.
     * they should be enough to test all the possible popup states.
     */
    private void loadMacroServicePopup_withDynamicFields_partialModelDetails_ecompNamingTrue() {
        loadServicePopup("csar-withDynamicFields-ecompNamingTrue-partialModelDetails.zip", "2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd");
    }

    private void loadMacroServicePopup_noDynamicFields_fullModelDetails_ecompNamingFalse() {
        loadServicePopup("csar-noDynamicFields-ecompNamingFalse-fullModelDetails-1a80c596.zip", "1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd");
    }

    @DataProvider
    private Object[][] createSriovService(
    ) {
        List<String> dynamicFields = Arrays.asList(
                "2017488 adiodvpe0 VNF config template version:",
                "Adiodvpe0 bandwidth units:",
                "Adiodvpe0 bandwidth:",
                "2017488 adiodvpe0 aic clli:",
                "2017488 adiodvpe0 asn:",
                "2017488 adiodvpe0 VNF instance name");
        ServiceData withEcompNaming = new ServiceData("csar-withDynamicFields-ecompNamingTrue-partialModelDetails.zip",
                "2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd",
                dynamicFields,
                true, true, true, false,
                "2017-488_ADIOD-vPE 0",
                "2017488_adiodvpe0..2017488AdiodVpe..ADIOD_vRE_BV..module-1", 0, 1, new ArrayList<>());
        return new Object[][]{{withEcompNaming}};
    }


    private class ServiceData {

        public ServiceData(String csarName, String modelUuid, List<String> dynamicFields, boolean isServiceGeneratedNaming, boolean isVnfGeneratedNaming, boolean isVgEnabled, boolean multiStageDesign, String vnfName, String vfName, int vfMin, int vfMax, List<String> vfModuleDynamicFields) {
            this.csarName = csarName;
            this.modelUuid = modelUuid;
            this.dynamicFields = dynamicFields;
            this.isGeneratedNaming = isServiceGeneratedNaming;
            this.multiStageDesign = multiStageDesign;
            this.vnfData = new VnfData(vnfName, isVnfGeneratedNaming);
            this.vfData = new VfData(vfName, isVgEnabled, vfMin, vfMax, vfModuleDynamicFields);
        }

        public String csarName;
        public String modelUuid;
        public List<String> dynamicFields;
        public boolean isGeneratedNaming;
        public boolean multiStageDesign;
        public VnfData vnfData;
        public VfData vfData;

    }

    private class VnfData {

        public VnfData(String vnfName, boolean isGeneratedNaming) {
            this.vnfName = vnfName;
            this.isGeneratedNaming = isGeneratedNaming;
        }

        public String vnfName;
        public boolean isGeneratedNaming;
    }


    private class VfData {
        public VfData(String vfName, boolean vgEnabled, int vfMin, int vfMax, List<String> dynamicFields) {
            this.vfName = vfName;
            this.vgEnabled = vgEnabled;
            this.vfMin = vfMin;
            this.vfMax = vfMax;
            this.dynamicFields = dynamicFields;
        }

        public int vfMin;

        public int vfMax;

        public String vfName;

        public boolean vgEnabled;

        public List<String> dynamicFields;

    }


    private class ServiceStatusChecker implements Predicate<Boolean> {
        private String actualInstanceName;
        private Set<String> expectedStatuses;
        private Set<String> columnClassesSet;

        public ServiceStatusChecker(String actualInstanceName, Set<String> expectedStatuses) {
            this.actualInstanceName = actualInstanceName;
            this.expectedStatuses = expectedStatuses;
        }

        @Override
        public boolean test(Boolean noMeaning) {
            InstantiationStatusPage.clickRefreshButton();
            final WebElement row = InstantiationStatusPage.getInstantiationStatusRow(actualInstanceName);
            if (row == null) {
                System.err.println("**********************" + actualInstanceName + "************************************************");
                columnClassesSet = Collections.singleton(actualInstanceName + " NOT FOUND");
                return false; // treat missing row as if test condition not fulfilled
            } else {
                columnClassesSet = new HashSet<>(Arrays.asList(
                        row.findElements(By.xpath(".//*[@id='" + "jobStatus" + "']")).get(0).getAttribute("class").split(" ")));
                return !(Sets.intersection(expectedStatuses, columnClassesSet).isEmpty());
            }
        }

        public Set<String> getColumnClassesSet() {
            return columnClassesSet;
        }
    }
}
