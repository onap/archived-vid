package vid.automation.test.test;

import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet.COMPLETE;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.fail;
import static vid.automation.test.utils.TestHelper.GET_SERVICE_MODELS_BY_DISTRIBUTION_STATUS;
import static vid.automation.test.utils.TestHelper.GET_TENANTS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.junit.Assert;
import org.onap.sdc.ci.tests.datatypes.Configuration;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.onap.sdc.ci.tests.execute.setup.SetupCDTest;
import org.onap.sdc.ci.tests.utilities.FileHandling;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAICloudRegionAndSourceFromConfigurationPut;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetNetworkZones;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetPortMirroringSourcePorts;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetServicesGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubDetailsGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubDetailsWithoutInstancesGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetTenants;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIPostNamedQueryForViewEdit;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIServiceDesignAndCreationPut;
import org.onap.simulator.presetGenerator.presets.ecompportal_att.EcompPortalPresetsUtils;
import org.onap.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceGen2;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstancePost;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceMetadataGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import vid.automation.reportportal.ReportPortalListenerDelegator;
import vid.automation.test.Constants;
import vid.automation.test.Constants.ViewEdit;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.Exists;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.ModelInfo;
import vid.automation.test.infra.SelectOption;
import vid.automation.test.infra.Wait;
import vid.automation.test.model.Credentials;
import vid.automation.test.model.User;
import vid.automation.test.sections.LoginExternalPage;
import vid.automation.test.sections.SearchExistingPage;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.sections.VidBasePage;
import vid.automation.test.sections.ViewEditPage;
import vid.automation.test.sections.deploy.DeployModernUIMacroDialog;
import vid.automation.test.services.CategoryParamsService;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.services.UsersService;
import vid.automation.test.utils.CookieAndJsonHttpHeadersInterceptor;
import vid.automation.test.utils.DB_CONFIG;
import vid.automation.test.utils.InsecureHttpsClient;
import vid.automation.test.utils.TestConfigurationHelper;
import vid.automation.test.utils.TestHelper;

@Listeners(ReportPortalListenerDelegator.class)
public class VidBaseTestCase extends SetupCDTest{

    protected static final UsersService usersService = new UsersService();
    protected static final CategoryParamsService categoryParamsService = new CategoryParamsService();
    protected final RestTemplate restTemplate = InsecureHttpsClient.newRestTemplate();
    protected final URI uri;
    protected final URI envUrI;

    public VidBaseTestCase() {
        try {
            this.envUrI = new URI(System.getProperty("ENV_URL"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.uri = new JerseyUriBuilder().host(envUrI.getHost()).port(envUrI.getPort()).scheme(envUrI.getScheme()).path("vid").build();
    }

    public void login() {
        UserCredentials userCredentials = getUserCredentials();
        final List<ClientHttpRequestInterceptor> interceptors = singletonList(new CookieAndJsonHttpHeadersInterceptor(uri, userCredentials));
        restTemplate.setInterceptors(interceptors);
    }

    public void invalidateSdcModelsCache() {
        if (Features.FLAG_SERVICE_MODEL_CACHE.isActive()) {
            restTemplate.postForObject(uri + "/rest/models/reset", "", Object.class);
        }
    }

    protected void resetGetServicesCache() {
        login();
        TestHelper.resetAaiCache(GET_SERVICE_MODELS_BY_DISTRIBUTION_STATUS, restTemplate, uri);
    }

    protected void resetGetTenantsCache() {
        login();
        TestHelper.resetAaiCache(GET_TENANTS, restTemplate, uri);
    }

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
    protected Configuration getEnvConfiguration() {

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
            ReportPortalListenerDelegator.setScreenShotsWebDriver(getDriver());
            System.out.println("Called to ReportPortalListener to set ScreenShotsProvider");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeSuite(alwaysRun = true)
    public void setSmallDefaultTimeout() throws Exception {
        getDriver().manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void loginToLocalSimulator(UserCredentials userCredentials) {
        LoginExternalPage.performLoginExternal(userCredentials);
    }

    protected void registerExpectationForLegacyServiceDeployment(ModelInfo modelInfo, String subscriberId) {
        List<BasePreset> presets = new ArrayList<>(Arrays.asList(
                new PresetAAIPostNamedQueryForViewEdit(BaseMSOPreset.DEFAULT_INSTANCE_ID, true, false),
                new PresetAAIGetPortMirroringSourcePorts("9533-config-LB1113", "myRandomInterfaceId", ViewEdit.COMMON_PORT_MIRRORING_PORT_NAME, true)
        ));

        presets.add(new PresetMSOCreateServiceInstancePost());
        presets.add(new PresetMSOOrchestrationRequestGet(COMPLETE, false));

        presets.addAll(getPresetForServiceBrowseAndDesign(ImmutableList.of(modelInfo), subscriberId));

        SimulatorApi.registerExpectationFromPresets(presets, SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
    }

    protected void registerExpectationForServiceDeployment(List<ModelInfo> modelInfoList, String subscriberId, PresetMSOCreateServiceInstanceGen2 createServiceInstancePreset) {
        List<BasePreset> presets = new ArrayList<>(Arrays.asList(
                new PresetAAIPostNamedQueryForViewEdit(BaseMSOPreset.DEFAULT_INSTANCE_ID, true, false),
                new PresetAAIGetPortMirroringSourcePorts("9533-config-LB1113", "myRandomInterfaceId", ViewEdit.COMMON_PORT_MIRRORING_PORT_NAME, true)
        ));

        if (createServiceInstancePreset != null) {
            presets.add(createServiceInstancePreset);
        }
        presets.add(new PresetMSOOrchestrationRequestGet("IN_PROGRESS"));

        presets.addAll(getPresetForServiceBrowseAndDesign(modelInfoList, subscriberId));

        SimulatorApi.registerExpectationFromPresets(presets, SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
    }

    protected void registerExpectationForServiceBrowseAndDesign(List<ModelInfo> modelInfoList, String subscriberId) {
        SimulatorApi.registerExpectationFromPresets(getPresetForServiceBrowseAndDesign(modelInfoList, subscriberId), SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
    }

    protected List<BasePreset> getPresetForServiceBrowseAndDesign(List<ModelInfo> modelInfoList, String subscriberId) {

        List<BasePreset> presets = new ArrayList<>(Arrays.asList(
                    new PresetGetSessionSlotCheckIntervalGet(),
                    new PresetAAIGetSubDetailsGet(subscriberId),
                    new PresetAAIGetSubDetailsWithoutInstancesGet(subscriberId),
                    new PresetAAIGetSubscribersGet(),
                    new PresetAAIGetServicesGet(),
                    new PresetAAICloudRegionAndSourceFromConfigurationPut("9533-config-LB1113", "myRandomCloudRegionId"),
                    new PresetAAIGetNetworkZones(),
                    new PresetAAIGetTenants(),
                    new PresetAAIServiceDesignAndCreationPut()
                    ));

        presets.addAll(EcompPortalPresetsUtils.getEcompPortalPresets());

        modelInfoList.forEach(modelInfo -> {
            presets.add(new PresetSDCGetServiceMetadataGet(modelInfo.modelVersionId, modelInfo.modelInvariantId, modelInfo.zipFileName));
            presets.add(new PresetSDCGetServiceToscaModelGet(modelInfo.modelVersionId, modelInfo.zipFileName));
        });

        return presets;
    }

    protected void relogin(Credentials credentials)  {
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

        final Map<Boolean, Set<String>> optionsMap = optionsList.stream()
                .collect(groupingBy(WebElement::isEnabled, mapping(option -> option.getAttribute(attribute), toSet())));

        assertGroupedPermissionsAreCorrect(permittedItems, optionsMap);
    }

    private void assertGroupedPermissionsAreCorrect(ArrayList<String> permittedItems, Map<Boolean, Set<String>> optionsMap) {
        if (permittedItems.isEmpty()) {
            assertThat(Constants.DROPDOWN_PERMITTED_ASSERT_FAIL_MESSAGE, optionsMap.getOrDefault(Boolean.TRUE, emptySet()), is(empty()));
        }else {
            assertThat(Constants.DROPDOWN_PERMITTED_ASSERT_FAIL_MESSAGE, optionsMap.getOrDefault(Boolean.TRUE, emptySet()), containsInAnyOrder(permittedItems.toArray()));
            assertThat(Constants.DROPDOWN_PERMITTED_ASSERT_FAIL_MESSAGE, optionsMap.getOrDefault(Boolean.FALSE, emptySet()), not(contains(permittedItems.toArray())));
        }
    }

    protected void assertAllIsPermitted(String dropdownOptionsClassName) {
        GeneralUIUtils.ultimateWait();
        List<WebElement> optionsList =
                GeneralUIUtils.getWebElementsListBy(By.className(dropdownOptionsClassName), 30);
        for (WebElement option :
                optionsList) {
            //String optionValue = option.getAttribute("value");
            if (!option.isEnabled()) {
                fail(Constants.DROPDOWN_PERMITTED_ASSERT_FAIL_MESSAGE);
            }
        }
    }

    protected void assertDropdownPermittedItemsByName(ArrayList<String> permittedItems, String dropdownOptionsClassName) {
        GeneralUIUtils.ultimateWait();
        List<WebElement> optionsList =
                GeneralUIUtils.getWebElementsListBy(By.className(dropdownOptionsClassName), 30);

        final Map<Boolean, Set<String>> optionsMap = optionsList.stream()
                .collect(groupingBy(WebElement::isEnabled, mapping(WebElement::getText, toSet())));

        assertGroupedPermissionsAreCorrect(permittedItems, optionsMap);
    }

    protected void assertViewEditButtonState(String expectedButtonText, String UUID) {
        WebElement viewEditWebElement = GeneralUIUtils.getWebElementByTestID(Constants.VIEW_EDIT_TEST_ID_PREFIX + UUID, 100);
        Assert.assertEquals(expectedButtonText, viewEditWebElement.getText());
        GeneralUIUtils.ultimateWait();
    }


    protected void addNetwork(Map<String, String> metadata,String instanceName, String name, String lcpRegion, String cloudOwner, String productFamily,String platform, String lineOfBusiness, String tenant, String suppressRollback,
                               String legacyRegion, ArrayList<String> permittedTenants) {
        ViewEditPage viewEditPage = new ViewEditPage();

        viewEditPage.selectNetworkToAdd(name);
        assertModelInfo(metadata, false);
        viewEditPage.setInstanceName(instanceName);
        viewEditPage.selectLcpRegion(lcpRegion, cloudOwner);
        viewEditPage.selectProductFamily(productFamily);
        viewEditPage.selectLineOfBusiness(lineOfBusiness);
        assertDropdownPermittedItemsByValue(permittedTenants, Constants.ViewEdit.TENANT_OPTION_CLASS);
        viewEditPage.selectTenant(tenant);

        viewEditPage.selectSuppressRollback(suppressRollback);
        if(platform != null){
            viewEditPage.selectPlatform(platform);
        }
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

    //@Step("${method}: ${instanceUUID}")
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

    void resumeVFModule(String vfModuleName, String lcpRegion, String cloudOwner, String tenant, String legacyRegion, ArrayList<String> permittedTenants){
        ViewEditPage viewEditPage = new ViewEditPage();
        viewEditPage.clickResumeButton(vfModuleName);
        viewEditPage.selectLcpRegion(lcpRegion, cloudOwner);
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

    protected <T> void assertModelDataCorrect(Map<String, String> modelKeyToDataTestsIdMap, String prefix, T model) {
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
        assertThat(String.format(Constants.ServiceModelInfo.METADETA_ERROR_MESSAGE, elementTestId),  infoItemText, is(value));
    }

    protected void loadServicePopup(ModelInfo modelInfo) {
        loadServicePopup(modelInfo.modelVersionId);
    }


    protected void loadServicePopup(String modelVersionId) {
        SideMenu.navigateToBrowseASDCPage();
        GeneralUIUtils.ultimateWait();
        loadServicePopupOnBrowseASDCPage(modelVersionId);
    }

    protected void loadServicePopupOnBrowseASDCPage(String modelVersionId) {
        loadServicePopupOnBrowseASDCPage(modelVersionId, "Model version");
    }

    protected void loadTemplatesPopupOnBrowseASDCPage (String modelVersionId) {
        loadServicePopupOnBrowseASDCPage(modelVersionId, "Templates");
    }

    protected void loadServicePopupOnBrowseASDCPage(String modelVersionId, String expectedText) {
        DeployModernUIMacroDialog deployMacroDialog = new DeployModernUIMacroDialog();
        VidBasePage.goOutFromIframe();
        deployMacroDialog.clickDeployServiceButtonByServiceUUID(modelVersionId);
        deployMacroDialog.goToIframe();
        GeneralUIUtils.ultimateWait();
        Wait.byText(expectedText);
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

    public boolean isElementByIdRequired(String id)  {
        return Get.byId(id).getAttribute("class").contains("required");
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
        navigateToViewEditPage("3f93c7cb-2fd0-4557-9514-e189b7b04f9d", aaiModelVersionId);
    }

    protected void navigateToViewEditPageOf_test_sssdad() {
        navigateToViewEditPage("c187e9fe-40c3-4862-b73e-84ff056205f6", "ee6d61be-4841-4f98-8f23-5de9da846ca7");
    }

    protected void navigateToViewEditPage(final String serviceInstanceId, String aaiModelVersionId) {
        VidBasePage vidBasePage = new VidBasePage();
        SideMenu.navigateToWelcomePage();
        vidBasePage.navigateTo("serviceModels.htm#/instantiate?" +
                "subscriberId=e433710f-9217-458d-a79d-1c7aff376d89&" +
                "subscriberName=SILVIA%20ROBBINS&" +
                "serviceType=TYLER%20SILVIA&" +
                "serviceInstanceId=" + serviceInstanceId + "&" +
                "aaiModelVersionId=" + aaiModelVersionId + "&" +
                "isPermitted=true");
        GeneralUIUtils.ultimateWait();
    }


    public void hoverAndClickMenuByName(String nodeName, String nodeToEdit, String contextMenuItem ) {
        String buttonOfEdit = Constants.DrawingBoard.NODE_PREFIX + nodeToEdit + Constants.DrawingBoard.CONTEXT_MENU_BUTTON;

        WebElement rightTreeNode = getTreeNodeByName(nodeName);
        WebElement menuButton = Get.byXpath(rightTreeNode, ".//span[@data-tests-id='" + buttonOfEdit + "']");

        GeneralUIUtils.clickElementUsingActions(menuButton);
        Click.byTestId(contextMenuItem);
    }

    private WebElement getTreeNodeByName(String nodeName) {
        return Get.byXpath("//tree-node-content[.//*[contains(text(), '" + nodeName + "')]]");
    }
}
