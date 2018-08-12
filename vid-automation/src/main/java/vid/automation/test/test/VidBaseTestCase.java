package vid.automation.test.test;

import com.att.automation.common.report_portal_integration.annotations.Step;
import com.att.automation.common.report_portal_integration.listeners.ReportPortalListener;
import com.att.automation.common.report_portal_integration.screenshots.WebDriverScreenshotsProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.opencomp.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.opencomp.simulator.presetGenerator.presets.aai.*;
import org.opencomp.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.opencomp.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceGen2;
import org.opencomp.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstancePost;
import org.opencomp.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.opencomp.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceMetadataGet;
import org.opencomp.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import org.openecomp.sdc.ci.tests.datatypes.UserCredentials;
import org.openecomp.sdc.ci.tests.execute.setup.SetupCDTest;
import org.openecomp.sdc.ci.tests.utilities.FileHandling;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.*;
import vid.automation.test.model.Credentials;
import vid.automation.test.model.User;
import vid.automation.test.sections.*;
import vid.automation.test.services.CategoryParamsService;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.services.UsersService;
import vid.automation.test.utils.DB_CONFIG;
import vid.automation.test.utils.TestConfigurationHelper;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.fail;

@Listeners(com.att.automation.common.report_portal_integration.listeners.ReportPortalListener.class)
public class VidBaseTestCase extends SetupCDTest{

    protected final UsersService usersService = new UsersService();
    protected final CategoryParamsService categoryParamsService = new CategoryParamsService();

    @Override
    protected UserCredentials getUserCredentials() {
        ObjectMapper mapper = new ObjectMapper().enableDefaultTyping();
        try {
            File configFile = FileHandling.getConfigFile("credentials");
            if(!configFile.exists()) {
                String basePath = System.getProperty("BASE_PATH");
                configFile = new File( basePath + File.separator + "conf" + File.separator + "credentials");
            }
            Credentials credentials = mapper.readValue(configFile, Credentials.class);
            User user = usersService.getUser(credentials.userId);
            return new UserCredentials(user.credentials.userId, user.credentials.password, credentials.userId, "", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected org.openecomp.sdc.ci.tests.datatypes.Configuration getEnvConfiguration() {

        return TestConfigurationHelper.getEnvConfiguration();
    }

    @BeforeMethod(alwaysRun = true)
    public void setBrowserBeforeTestIfDataProvider(Method method, ITestContext context, Object[] params) {
        // Hack to overcome limitations of SetupCDTest.setBrowserBeforeTest(java.lang.reflect.Method, org.testng.ITestContext)
        // that skips over dataProvided methods
        boolean emptyDataProvider = method.getAnnotation(Test.class).dataProvider().isEmpty();
        if (!emptyDataProvider) {
            final String testName = method.getName();
            final String listOfParams = Arrays.deepToString(params)
                    .replace('[', '(')
                    .replace(']', ')')
                    .replaceAll("[\\\\/:*?\"<>|]", "_");

            setLog(testName+listOfParams);
        }
    }

    @BeforeSuite(alwaysRun = true)
    public void screenShotsForReportPortal(){
        try {
            ReportPortalListener.setScreenShotsProvider(new WebDriverScreenshotsProvider(getDriver()));
            System.out.println("Called to ReportPortalListener to set ScreenShotsProvider");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void loginToLocalSimulator(UserCredentials userCredentials) {
        LoginExternalPage.performLoginExternal(userCredentials);
    }

    static public class ModelInfo {
        public final String modelVersionId;
        public final String modelInvariantId;
        public final String zipFileName;

        public ModelInfo(String modelVersionId, String modelInvariantId, String zipFileName) {
            this.modelVersionId = modelVersionId;
            this.modelInvariantId = modelInvariantId;
            this.zipFileName = zipFileName;
        }
    }

    protected void registerExpectationForLegacyServiceDeployment(String modelVersionId, String modelInvariantId, String zipFileName, String subscriberId) {
        registerExpectationForServiceDeployment(ServiceDeployment.LEGACY, ImmutableList.of(new ModelInfo(modelVersionId,modelInvariantId,zipFileName)), subscriberId);
    }

    private enum ServiceDeployment {ASYNC, LEGACY}

    protected void registerExpectationForServiceDeployment(ServiceDeployment serviceDeploymentOnMsoExpectations, List<ModelInfo> modelInfoList, String subscriberId) {
        List<BasePreset> presets = new ArrayList<>(Arrays.asList(
                new PresetGetSessionSlotCheckIntervalGet(),
                new PresetAAIGetSubscribersGet(),
                new PresetAAIGetServicesGet(),
                new PresetAAIGetSubDetailsGet(subscriberId),
                new PresetAAIPostNamedQueryForViewEdit("f8791436-8d55-4fde-b4d5-72dd2cf13cfb"),
                new PresetAAICloudRegionAndSourceFromConfigurationPut("9533-config-LB1113", "myRandomCloudRegionId"),
                new PresetAAIGetPortMirroringSourcePorts("9533-config-LB1113", "myRandomInterfaceId", "i'm a port", true),
                new PresetAAIGetNetworkZones(),
                new PresetAAIGetTenants(),
                new PresetAAIServiceDesignAndCreationPut(modelInfoList.stream().map(
                        x-> new PresetAAIServiceDesignAndCreationPut.ServiceModelIdentifiers(x.modelVersionId, x.modelInvariantId))
                        .collect(Collectors.toList()))
                ));

        modelInfoList.forEach(modelInfo -> {
            presets.add(new PresetSDCGetServiceMetadataGet(modelInfo.modelVersionId, modelInfo.modelInvariantId, modelInfo.zipFileName));
            presets.add(new PresetSDCGetServiceToscaModelGet(modelInfo.modelVersionId, modelInfo.zipFileName));
        });

        switch (serviceDeploymentOnMsoExpectations) {
            case ASYNC:
                presets.add(new PresetAAISearchNodeQueryEmptyResult());
                presets.add(new PresetMSOCreateServiceInstanceGen2());
                presets.add(new PresetMSOOrchestrationRequestGet("IN_PROGRESS"));
                break;
            case LEGACY:
                presets.add(new PresetMSOCreateServiceInstancePost());
                presets.add(new PresetMSOOrchestrationRequestGet());
                break;
        }

        SimulatorApi.registerExpectationFromPresets(presets, SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
    }

    protected void relogin(Credentials credentials) throws Exception {
        // `getWindowTest().getPreviousUser()` is SetupCDTest's state of previous user used
        if (!credentials.userId.equals(getWindowTest().getPreviousUser())) {
            UserCredentials userCredentials = new UserCredentials(credentials.userId,
                    credentials.password, "", "", "");
            reloginWithNewRole(userCredentials);
        } else {
            System.out.println(String.format("VidBaseTestCase.relogin() " +
                    "-> '%s' is already logged in, so skipping", credentials.userId));
        }
    }

    /**
     * Validates that permitted options are enabled and others are disabled.
     *
     * @param permittedItems           the list of permitted items.
     * @param dropdownOptionsClassName the class name of the specific dropdown options.
     * @return true, if all dropdown options disabled state is according to the permissions.
     */
    protected void assertDropdownPermittedItemsByValue(ArrayList<String> permittedItems, String dropdownOptionsClassName) {
        assertDropdownPermittedItemsByValue(permittedItems, dropdownOptionsClassName, "value");
    }

    protected void assertDropdownPermittedItemsByLabel(ArrayList<String> permittedItems, String dropdownOptionsClassName) {
        assertDropdownPermittedItemsByValue(permittedItems, dropdownOptionsClassName, "label");
    }

    /**
     * Validates that permitted options are enabled and others are disabled.
     *
     * @param permittedItems           the list of permitted items.
     * @param dropdownOptionsClassName the class name of the specific dropdown options.
     * @param attribute
     * @return true, if all dropdown options disabled state is according to the permissions.
     */
    private void assertDropdownPermittedItemsByValue(ArrayList<String> permittedItems, String dropdownOptionsClassName, String attribute) {
        GeneralUIUtils.ultimateWait();
        List<WebElement> optionsList =
                GeneralUIUtils.getWebElementsListBy(By.className(dropdownOptionsClassName), 30);
        for (WebElement option :
                optionsList) {
            String optionValue = option.getAttribute(attribute);
            if ((option.isEnabled() && !permittedItems.contains(optionValue)) ||
                    !option.isEnabled() && permittedItems.contains(optionValue)) {
                fail(Constants.DROPDOWN_PERMITTED_ASSERT_FAIL_MESSAGE);
            }
        }
    }

    protected void assertAllIsPermitted(String dropdownOptionsClassName) {
        GeneralUIUtils.ultimateWait();
        List<WebElement> optionsList =
                GeneralUIUtils.getWebElementsListBy(By.className(dropdownOptionsClassName), 30);
        for (WebElement option :
                optionsList) {
            String optionValue = option.getAttribute("value");
            if (!option.isEnabled()) {
                fail(Constants.DROPDOWN_PERMITTED_ASSERT_FAIL_MESSAGE);
            }
        }
    }

    protected void assertDropdownPermittedItemsByName(ArrayList<String> permittedItems, String dropdownOptionsClassName) {
        GeneralUIUtils.ultimateWait();
        List<WebElement> optionsList =
                GeneralUIUtils.getWebElementsListBy(By.className(dropdownOptionsClassName), 30);
        for (WebElement option :
                optionsList) {
            String optionText = option.getText();
            if ((option.isEnabled() && !permittedItems.contains(optionText)) ||
                    !option.isEnabled() && permittedItems.contains(optionText)) {
                fail(Constants.DROPDOWN_PERMITTED_ASSERT_FAIL_MESSAGE);
            }
        }
    }

    protected void assertViewEditButtonState(String expectedButtonText, String UUID) {
        WebElement viewEditWebElement = GeneralUIUtils.getWebElementByTestID(Constants.VIEW_EDIT_TEST_ID_PREFIX + UUID, 100);
        Assert.assertEquals(expectedButtonText, viewEditWebElement.getText());
        GeneralUIUtils.ultimateWait();
    }


    protected void addNetwork(Map<String, String> metadata,String instanceName, String name, String lcpRegion, String productFamily,String platform, String lineOfBusiness, String tenant, String suppressRollback,
                               String legacyRegion, ArrayList<String> permittedTenants) {
        ViewEditPage viewEditPage = new ViewEditPage();

        viewEditPage.selectNetworkToAdd(name);
        assertModelInfo(metadata, false);
        viewEditPage.setInstanceName(instanceName);
        viewEditPage.selectLCPRegion(lcpRegion);
        viewEditPage.selectProductFamily(productFamily);
        viewEditPage.selectLineOfBusiness(lineOfBusiness);
        assertDropdownPermittedItemsByValue(permittedTenants, Constants.ViewEdit.TENANT_OPTION_CLASS);
        viewEditPage.selectTenant(tenant);

        viewEditPage.selectSuppressRollback(suppressRollback);
        viewEditPage.selectPlatform(platform);
        //viewEditPage.setLegacyRegion(legacyRegion);

        viewEditPage.clickConfirmButton();
        viewEditPage.assertMsoRequestModal(Constants.ViewEdit.MSO_SUCCESSFULLY_TEXT);
        viewEditPage.clickCloseButton();
        GeneralUIUtils.ultimateWait();
    }

    void assertSuccessfulVNFCreation() {
        boolean byText = GeneralUIUtils.findAndWaitByText(Constants.ViewEdit.VNF_CREATED_SUCCESSFULLY_TEXT, 100);
        Assert.assertTrue(Constants.ViewEdit.VNF_CREATION_FAILED_MESSAGE, byText);
    }

    void assertSuccessfulPNFAssociation() {
        //TODO
        boolean byText = GeneralUIUtils.findAndWaitByText(Constants.PnfAssociation.PNF_ASSOCIATED_SUCCESSFULLY_TEXT, 100);
        Assert.assertTrue(Constants.PnfAssociation.PNF_ASSOCIATED_FAILED_MESSAGE, byText);
    }
    void assertSuccessfulVolumeGroupCreation() {
        boolean byText = GeneralUIUtils.findAndWaitByText(Constants.ViewEdit.VOLUME_GROUP_CREATED_SUCCESSFULLY_TEXT, 100);
        Assert.assertTrue(Constants.ViewEdit.VOLUME_GROUP_CREATION_FAILED_MESSAGE, byText);
    }

    void assertSuccessfulVFModuleCreation() {
        boolean byText = GeneralUIUtils.findAndWaitByText(Constants.ViewEdit.VF_MODULE_CREATED_SUCCESSFULLY_TEXT, 100);
        Assert.assertTrue(Constants.ViewEdit.VF_MODULE_CREATION_FAILED_MESSAGE, byText);
    }

    @Step("${method}: ${instanceUUID}")
    void goToExistingInstanceById(String instanceUUID) {
        SearchExistingPage searchExistingPage = searchExistingInstanceById(instanceUUID);
        assertViewEditButtonState( Constants.VIEW_EDIT_BUTTON_TEXT, instanceUUID);

        searchExistingPage.clickEditViewByInstanceId(instanceUUID);
        GeneralUIUtils.ultimateWait();
    }

    void searchForExistingInstanceByIdReadonlyMode(String instanceUUID) {
        searchExistingInstanceById(instanceUUID);
        assertViewEditButtonState( Constants.VIEW_BUTTON_TEXT, instanceUUID);
    }

    SearchExistingPage searchExistingInstanceById(String instanceUUID){
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        searchExistingPage.searchForInstanceByUuid(instanceUUID);
        return searchExistingPage;
    }


    void goToExistingInstanceByIdNoWait(String instanceUUID) {
        SearchExistingPage searchExistingPage = searchExistingInstanceById(instanceUUID);
        searchExistingPage.clickEditViewByInstanceId(instanceUUID);
    }

    void resumeVFModule(String vfModuleName, String lcpRegion, String tenant, String legacyRegion, ArrayList<String> permittedTenants){
        ViewEditPage viewEditPage = new ViewEditPage();
        viewEditPage.clickResumeButton(vfModuleName);
        viewEditPage.selectLCPRegion(lcpRegion);
        assertDropdownPermittedItemsByValue(permittedTenants, Constants.ViewEdit.TENANT_OPTION_CLASS);
        viewEditPage.selectTenant(tenant);
        viewEditPage.setLegacyRegion(legacyRegion);
        viewEditPage.clickConfirmButtonInResumeDelete();
        assertSuccessfulVFModuleCreation();
        viewEditPage.clickCommitCloseButton();
        GeneralUIUtils.ultimateWait();
    }

    void goToExistingInstanceByName(String instanceName) {
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        searchExistingPage.searchForInstanceByName(instanceName);
        WebElement instanceIdRow = GeneralUIUtils.getWebElementByTestID(Constants.INSTANCE_ID_FOR_NAME_TEST_ID_PREFIX + instanceName, 30);
        String instanceId = instanceIdRow.getText();
        assertViewEditButtonState( Constants.VIEW_EDIT_BUTTON_TEXT, instanceId);
        searchExistingPage.clickEditViewByInstanceId(instanceId);
        GeneralUIUtils.ultimateWait();
    }

    String confirmFilterById(String instanceName, String instanceUUID) {
        WebElement filter = GeneralUIUtils.getWebElementByTestID(Constants.FILTER_SUBSCRIBER_DETAILS_ID, 30);
        filter.sendKeys(instanceUUID);

        WebElement firstElement = GeneralUIUtils.getWebElementByTestID(Constants.INSTANCE_ID_FOR_NAME_TEST_ID_PREFIX + instanceName, 30);
        String filteredId = firstElement.getText();
        Assert.assertTrue(filteredId.equals(instanceUUID));
        return filteredId;
    }

    void goToExistingInstanceBySubscriber(String subscriberName,String instanceName,String instanceUUID) {
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        SelectOption.byIdAndVisibleText(Constants.EditExistingInstance.SELECT_SUBSCRIBER, subscriberName);
        searchExistingPage.clickSubmitButton();
        GeneralUIUtils.ultimateWait();
        confirmFilterById(instanceName, instanceUUID);
        searchExistingPage.clickEditViewByInstanceId(instanceUUID);
        GeneralUIUtils.ultimateWait();
    }

    void selectMsoTestApiOption(String msoTestApiOption) {
        final String id = "selectTestApi";
        final String sectionId = "selectTestApiSection";

        SideMenu.navigateToWelcomePage();

        if (Exists.byId(sectionId)) {
            final JavascriptExecutor javascriptExecutor = (JavascriptExecutor) GeneralUIUtils.getDriver();
            javascriptExecutor.executeScript(
                    "document.getElementById('" + sectionId + "').style.visibility = 'inherit';"
            );

            if (null == SelectOption.byIdAndVisibleText(id, msoTestApiOption)) {
                Assert.fail("selectMsoTestApiOptionIfPossible couldnt apply " + msoTestApiOption);
            }
        }
    }

    protected void assertModelInfo(Map<String, String> expectedMetadata, boolean withPrefix) {
        Wait.angularHttpRequestsLoaded();
        GeneralUIUtils.ultimateWait();
        for (Map.Entry<String, String> item: expectedMetadata.entrySet()) {
            assertMetadataItem(item.getKey(), item.getValue(), withPrefix);
        }
    }

    protected <T> void setNewInstance_leftPane_assertModelDataCorrect(Map<String, String> modelKeyToDataTestsIdMap, String prefix, T model) {
        modelKeyToDataTestsIdMap.forEach((fieldName, dataTestsId) -> {
            WebElement webElement = Get.byTestId(prefix + dataTestsId);
            assertEquals(webElement.getText(), getServiceFieldByName(fieldName, model));
        });
    }

    protected <T> void setNewInstance_leftPane_assertModelLabelsVisibilityCorrect(Map<String, String> modelKeyToDataTestsIdMap, String prefix, T model) {
        modelKeyToDataTestsIdMap.forEach((fieldName, dataTestsId) -> {
            WebElement webElement = Get.byTestId(prefix + dataTestsId);
            String field = getServiceFieldByName(fieldName, model);
            assertEquals(webElement.isDisplayed(), !(StringUtils.isEmpty(field)) , dataTestsId + " label shouldn't appear when " + fieldName + " is empty");
        });
    }

    private <T> String getServiceFieldByName(String name, T model) {
        try {
            return model.getClass().getField(name).get(model).toString();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private void assertMetadataItem(String keyTestId, String value, boolean withPrefix) {
        String elementTestId = (withPrefix ? Constants.ServiceModelInfo.INFO_TEST_ID_PREFIX:"") + keyTestId;
        String infoItemText = GeneralUIUtils.getWebElementByTestID(elementTestId, 60).getText();
        Assert.assertThat(String.format(Constants.ServiceModelInfo.METADETA_ERROR_MESSAGE, elementTestId),  infoItemText, is(value));
    }

    public DeployMacroDialogBase getMacroDialog(){
        if (Features.FLAG_ASYNC_INSTANTIATION.isActive()) {
            VidBasePage vidBasePage =new VidBasePage();
            vidBasePage.goToIframe();
            return new DeployMacroDialog();
        }
        else
            return  new DeployMacroDialogOld();
    }

    protected void loadServicePopup(String zipFileName, String modelVersionId ) {
        String modelInvariantId = "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0";
        String subscriberId = "e433710f-9217-458d-a79d-1c7aff376d89";
        registerExpectationForServiceDeployment(
                ServiceDeployment.ASYNC,
                ImmutableList.of(
                    new ModelInfo(modelVersionId, modelInvariantId, zipFileName),
                    new ModelInfo("f4d84bb4-a416-4b4e-997e-0059973630b9", "598e3f9e-3244-4d8f-a8e0-0e5d7a29eda9", "service-AdiodVmxVpeBvService488-csar-annotations.zip")
                ),
                subscriberId);
        SideMenu.navigateToBrowseASDCPage();
        GeneralUIUtils.ultimateWait();
        loadServicePopupOnBrowseASDCPage(modelVersionId);
    }

    protected void loadServicePopupOnBrowseASDCPage(String modelVersionId ) {
        DeployMacroDialog deployMacroDialog = new DeployMacroDialog();
        deployMacroDialog.goOutFromIframe();
        deployMacroDialog.clickDeployServiceButtonByServiceUUID(modelVersionId);
        deployMacroDialog.goToIframe();
        GeneralUIUtils.ultimateWait();
        Wait.byText("Model version");
    }

    public void assertSetButtonDisabled(String buttonTestId) {
        WebElement webElement = Get.byTestId(buttonTestId);
        org.testng.Assert.assertFalse(webElement.isEnabled(), "Set button should be disabled if not all mandatory fields are field.");
    }

    public void assertSetButtonEnabled(String buttonTestId) {

        WebElement webElement = Get.byTestId(buttonTestId);
        org.testng.Assert.assertTrue(webElement.isEnabled(), "Set button should be enabled if all mandatory fields are field.");
    }

    public void assertElementDisabled(String id) {
        WebElement webElement = Get.byId(id);
        assert webElement != null;
        org.testng.Assert.assertFalse(webElement.isEnabled(), "field should be disabled if the field it depends on was not selected yet.");
    }

    protected int getUserIdNumberFromDB(User user) {
        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {
            Statement stmt = connection.createStatement();
            ResultSet userIdResultSet;
            userIdResultSet = stmt.executeQuery("SELECT USER_ID FROM fn_user where LOGIN_ID = '" + user.credentials.userId + "'");
            Assert.assertTrue("Exactly one user should be found", userIdResultSet.next());
            int userId = userIdResultSet.getInt("USER_ID");
            Assert.assertFalse("There are more than one user for id " + userId, userIdResultSet.next());
            return userId;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    protected List<Integer> getRoleIDsAssignedToUser(int userId) {
        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {
            Statement stmt = connection.createStatement();
            ResultSet userRolesResultSet;
            userRolesResultSet = stmt.executeQuery("SELECT ROLE_ID FROM fn_user_role where USER_ID = '" + userId + "' order by ROLE_ID");

            List<Integer> userRoles = new ArrayList<Integer>();
            while (userRolesResultSet.next()) {
                userRoles.add(userRolesResultSet.getInt("ROLE_ID"));
            }
            return userRoles;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    protected void navigateToViewEditPageOfuspVoiceVidTest444(String aaiModelVersionId) {
        VidBasePage vidBasePage = new VidBasePage();
        SideMenu.navigateToWelcomePage();
        vidBasePage.navigateTo("serviceModels.htm#/instantiate?" +
                "subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&" +
                "subscriberName=USP%20VOICE&" +
                "serviceType=VIRTUAL%20USP&" +
                "serviceInstanceId=3f93c7cb-2fd0-4557-9514-e189b7b04f9d&" +
                "aaiModelVersionId=" + aaiModelVersionId + "&" +
                "isPermitted=true");
        GeneralUIUtils.ultimateWait();
    }
}
