package vid.automation.test.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static vid.automation.test.infra.Features.FLAG_1908_COLLECTION_RESOURCE_NEW_INSTANTIATION_UI;
import static vid.automation.test.infra.Features.FLAG_2002_ANY_ALACARTE_BESIDES_EXCLUDED_NEW_INSTANTIATION_UI;
import static vid.automation.test.infra.Features.FLAG_2002_IDENTIFY_INVARIANT_MACRO_UUID_BY_BACKEND;
import static vid.automation.test.infra.Features.FLAG_5G_IN_NEW_INSTANTIATION_UI;
import static vid.automation.test.infra.Features.FLAG_NETWORK_TO_ASYNC_INSTANTIATION;
import static vid.automation.test.infra.Features.FLAG_SHOW_ORCHESTRATION_TYPE;
import static vid.automation.test.infra.ModelInfo.aLaCarteForBrowseSdc;
import static vid.automation.test.infra.ModelInfo.aLaCarteServiceCreationTest;
import static vid.automation.test.infra.ModelInfo.instantiationTypeAlacarte_vidNotionsInstantiationUIByUUID;
import static vid.automation.test.infra.ModelInfo.macroForBrowseSdc;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetServicesGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIServiceDesignAndCreationPut;
import org.onap.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.Exists;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.ModelInfo;
import vid.automation.test.infra.SelectOption;
import vid.automation.test.model.Service;
import vid.automation.test.model.User;
import vid.automation.test.sections.BrowseASDCPage;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.sections.ViewEditPage;
import vid.automation.test.sections.deploy.DeployDialogBase;
import vid.automation.test.sections.deploy.DeployModernUIALaCarteDialog;
import vid.automation.test.sections.deploy.DeployModernUIMacroDialog;
import vid.automation.test.sections.deploy.DeployOldALaCarteDialog;
import vid.automation.test.sections.deploy.DeployOldMacroDialog;
import vid.automation.test.services.ServicesService;
import vid.automation.test.services.SimulatorApi;


public class BrowseASDCTest extends CreateInstanceDialogBaseTest {
    private final String invariantUUIDAlacarte = aLaCarteForBrowseSdc.modelInvariantId;
    private final String invariantUUIDMacro = macroForBrowseSdc.modelInvariantId;
    public static final String modelInvariantUUID1 = "aeababbc-010b-4a60-8df7-e64c07389466";
    public static final String modelInvariantUUID2 = "aa2f8e9c-9e47-4b15-a95c-4a9385599abc";
    public static final String modelInvariantUUID3 = "d849c57d-b6fe-4843-8349-4ab8bbb08d71";
    public static final String modelUuid = "a8dcd72d-d44d-44f2-aa85-53aa9ca99cba";

    private ServicesService servicesService = new ServicesService();

    private final Logger logger = LogManager.getLogger(BrowseASDCTest.class);

    @BeforeClass
    public void beforeClass() {
        resetGetServicesCache();
    }

    @BeforeMethod
    public void resetSdcModelCaches() {
        invalidateSdcModelsCache();
    }

    @Override
    protected UserCredentials getUserCredentials() {
        User user = usersService.getUser(Constants.Users.EMANUEL_EMANUEL);
        return new UserCredentials(user.credentials.userId, user.credentials.password, Constants.Users.EMANUEL_EMANUEL, "", "");
    }

    //@Test(groups = {"shouldBeMigratedToWorkWithSimulator"})
    public void testPNFOnCreatePopup() {
        Service service = servicesService.getService("f39389e4-2a9c-4085-8ac3-04aea9c651be");
        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();
        browseASDCPage.clickDeployServiceButtonByServiceUUID(service.uuid);
        assertThatServiceCreationDialogIsVisible();
        validatePNFCreationDialog(service, "Emanuel", "pnf");
    }

    private void validatePNFCreationDialog(Service service, String serviceType, String serviceRole) {
        assertServiceMetadata(serviceType, Constants.SERVICE_TYPE);
        assertServiceMetadata(serviceRole, Constants.SERVICE_ROLE);
        validateServiceCreationDialog(service);
    }



    //@Test(groups = {"shouldBeMigratedToWorkWithSimulator"})
    private void testPNFMacroInstantation() throws Exception {
        User user = usersService.getUser(Constants.Users.EMANUEL_EMANUEL);
        relogin(user.credentials);

        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();
        browseASDCPage.clickDeployServiceButtonByServiceUUID("f39389e4-2a9c-4085-8ac3-04aea9c651be");
        assertThatServiceCreationDialogIsVisible();
        assertDropdownPermittedItemsByLabel(user.subscriberNames, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
        browseASDCPage.selectSubscriberById("a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb");
        browseASDCPage.selectProductFamily("ebc3bc3d-62fd-4a3f-a037-f619df4ff034");
        GeneralUIUtils.ultimateWait();

        browseASDCPage.selectServiceTypeByName("Emanuel");
        GeneralUIUtils.ultimateWait();
        browseASDCPage.selectLcpRegion("hvf16");

        browseASDCPage.selectTenant("a259ae7b7c3f493cb3d91f95a7c18149");
        assertAllIsPermitted(Constants.BrowseASDC.AIC_OPTION_CLASS);
        browseASDCPage.selectAicZone("NFT1");

        Click.onFirstSelectOptionById(Constants.OwningEntity.PROJECT_SELECT_TEST_ID);
        Click.onFirstSelectOptionById(Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID);

        browseASDCPage.clickConfirmButton();

        assertSuccessfulServiceInstanceCreation();

        browseASDCPage.clickCloseButton();

        ViewEditPage viewEditPage = new ViewEditPage();
        viewEditPage.clickActivateButton();
    }

    @Test
    private void browseServiceModel_deployServiceALaCarteByBackendInput_creationPopupIsALaCarte()  {
        // model uuid should be of macro
        deployServiceAndAssertInstantiationType(
                "csar15782222_instantiationTypeAlacarte_invariantUUIDMacro.zip",
                invariantUUIDMacro,
            getAlacarteDialogByFlagValue()
        );
    }

    @NotNull
    public static DeployDialogBase getAlacarteDialogByFlagValue() {
        return FLAG_2002_ANY_ALACARTE_BESIDES_EXCLUDED_NEW_INSTANTIATION_UI.isActive() ?
            new DeployModernUIALaCarteDialog() :
            new DeployOldALaCarteDialog();
    }

    @Test
    @FeatureTogglingTest(FLAG_5G_IN_NEW_INSTANTIATION_UI)
    private void browseServiceModel_deployServiceALaCarteByBackendInputHintNewUI_creationPopupIsAngular2()  {
        deployServiceAndAssertInstantiationType(
                instantiationTypeAlacarte_vidNotionsInstantiationUIByUUID,
                new DeployModernUIALaCarteDialog()
        );
    }

    @Test
    private void browseServiceModel_deployServiceALaCarteBecauseNotOnMACRO_SERVICESConfig_creationPopupIsALaCarte()  {
        deployServiceAndAssertInstantiationType(
                "csar15782222_instantiationTypeEmpty_invariantUUIDAlacarte.zip",
                invariantUUIDAlacarte,
            getAlacarteDialogByFlagValue()
        );
    }

    @Test
    private void browseServiceModel_deployServiceMacroByBackendInput_creationPopupIsMacro()  {
        deployServiceAndAssertInstantiationType(
            ModelInfo.serviceWithInstantiationTypeMacro,
            new DeployModernUIMacroDialog()
        );
    }

    @Test
    private void browseServiceModel_deployServiceMacroByMACRO_SERVICESConfig_creationPopupIsOldMacro()  {
        if (FLAG_2002_ANY_ALACARTE_BESIDES_EXCLUDED_NEW_INSTANTIATION_UI.isActive() &&
        ! FLAG_2002_IDENTIFY_INVARIANT_MACRO_UUID_BY_BACKEND.isActive()) {
            throw new SkipException("some 2002 flags shall come along together");
        }
        deployServiceAndAssertInstantiationType(
                "csar15782222_invariantUUIDMacro.zip",
                invariantUUIDMacro,
                new DeployOldMacroDialog()
        );
    }

    @Test
    private void browseServiceModel_deployServiceMacroWithPnf_creationPopupIsOldMacro() {
        deployServiceAndAssertInstantiationType(
                "csar15782222_instantiationTypeMacroWithPnf.zip",
                invariantUUIDMacro,
            new DeployOldMacroDialog()
        );
    }

    @Test
    public void browseServiceModel_deployServiceMacroWithCR_creationPopupIsOldMacro()  {
        deployServiceAndAssertInstantiationType(
            ModelInfo.collectionResourceService,
                FLAG_1908_COLLECTION_RESOURCE_NEW_INSTANTIATION_UI.isActive() ?
                    new DeployModernUIMacroDialog() :
                    new DeployOldMacroDialog()
        );
    }

    @Test
    private void browseServiceModel_deployServiceMacroWithNetwork_creationPopupIsMacroByFF()  {
        deployServiceAndAssertInstantiationType(
                "csar15782222_instantiationTypeMacroWithNetwork.zip",
                invariantUUIDMacro,
                FLAG_NETWORK_TO_ASYNC_INSTANTIATION.isActive() ?
                new DeployModernUIMacroDialog() :
                new DeployOldMacroDialog()
        );
    }


    private void deployServiceAndAssertInstantiationType(String modelZipFileName, String modelInvariantId, DeployDialogBase deployDialog)  {
        deployServiceAndAssertInstantiationType(new ModelInfo("4d71990b-d8ad-4510-ac61-496288d9078e", modelInvariantId, modelZipFileName), deployDialog);
    }

    private void deployServiceAndAssertInstantiationType(ModelInfo modelInfo, DeployDialogBase deployDialog)  {

        registerExpectationForLegacyServiceDeployment(modelInfo, "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb");
        User user = usersService.getUser(Constants.Users.EMANUEL_EMANUEL);
        relogin(user.credentials);

        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();

        GeneralUIUtils.ultimateWait();
        browseASDCPage.clickDeployServiceButtonByServiceUUID(modelInfo.modelVersionId);

        deployDialog.waitForDialogAssertAndClose();
    }

    @FeatureTogglingTest(value = FLAG_2002_ANY_ALACARTE_BESIDES_EXCLUDED_NEW_INSTANTIATION_UI, flagActive = false)
    @Test
    private void testServiceInstantiationAlaCarte()  {
        User user = usersService.getUser(Constants.Users.EMANUEL_EMANUEL);
        relogin(user.credentials);

        registerExpectationForLegacyServiceDeployment(aLaCarteServiceCreationTest, "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb");

        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();

        Service service = new Service(
                "pnf",
                aLaCarteServiceCreationTest.modelVersionId,
                aLaCarteServiceCreationTest.modelInvariantId,
                "action-data",
                "1.0",
                "Network L1-3",
                "PASQUALE vMX vPE based on Juniper 17.2 release. Updated with updated VF for v8.0 of VLM",
                null);


        logger.info("Expected service model properties: "+service.toString());
        browseASDCPage.clickDeployServiceButtonByServiceUUID(service.uuid);
        validateServiceCreationDialog(service);

        browseASDCPage.setInstanceName(browseASDCPage.generateInstanceName());

        assertDropdownPermittedItemsByLabel(user.subscriberNames, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
        browseASDCPage.selectSubscriberById("a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb");

        String serviceType = "Emanuel";
        GeneralUIUtils.findAndWaitByText(serviceType, 30);

        assertDropdownPermittedItemsByValue(user.serviceTypes, Constants.CreateNewInstance.SERVICE_TYPE_OPTION_CLASS);
        browseASDCPage.selectServiceTypeByName(serviceType);

        SelectOption.byTestIdAndVisibleText("yyy1", Constants.OwningEntity.PROJECT_SELECT_TEST_ID);
        SelectOption.byTestIdAndVisibleText("aaa1", Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID);

        browseASDCPage.selectSuppressRollback("false");

        browseASDCPage.clickConfirmButton();

        assertSuccessfulServiceInstanceCreation();
    }

    private static final String serviceName = "CONTRAIL30_L2NODHCP";
    private static final String modelInvariantId = "5b607929-6088-4614-97ef-cac817508e0e";
    private static final String serviceUuid = "797a6c41-0f80-4d35-a288-3920c4e06baa";


    @DataProvider
    public static Object[][] filterTexts() {
        return new Object[][]{{serviceName},{modelInvariantId},{serviceUuid}};
    }

    @Test(dataProvider = "filterTexts")
    public void testFilterOptionsInBrowseSdc(String filterText){
        BrowseASDCPage browseAsdcPage = registerSimulatorAndGoToBrowseSDC();
        GeneralUIUtils.ultimateWait();
        assertThat(browseAsdcPage.countCurrentRowsInTable(),(Matchers.greaterThan(1)));
        browseAsdcPage.fillFilterText(filterText);
        Assert.assertEquals(browseAsdcPage.countCurrentRowsInTable(),1);
        Assert.assertTrue(Exists.byTestId("deploy-" + serviceUuid));
        browseAsdcPage.fillFilterText("");

    }

    @Test
    private void testCategoryParamsDropdownsExistsInCreationDialog() throws Exception {
        BrowseASDCPage browseASDCPage = registerSimulatorAndGoToBrowseSDC();
        Service service = servicesService.getService("2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd");
        browseASDCPage.clickDeployServiceButtonByServiceUUID(service.uuid);
        DeployModernUIMacroDialog deployMacroDialog = new DeployModernUIMacroDialog();
        deployMacroDialog.waitForDialogToLoad();
        deployMacroDialog.assertDialog();
        deployMacroDialog.clickProjectSelect();
        deployMacroDialog.clickOwningEntitySelect();
    }

    private BrowseASDCPage registerSimulatorAndGoToBrowseSDC() {
        SimulatorApi.registerExpectation(SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET,
                "ecompportal_getSessionSlotCheckInterval.json",
                "browseASDC/aai_get_services.json",
                "browseASDC/get_sdc_catalog_services_2f80c596.json"
        );
        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                    new PresetAAIGetSubscribersGet(),
                    new PresetAAIServiceDesignAndCreationPut()
                ),
                SimulatorApi.RegistrationStrategy.APPEND);
        SideMenu.navigateToBrowseASDCPage();
        return new BrowseASDCPage();
    }

    //@Test(groups = {"shouldBeMigratedToWorkWithSimulator"})
    private void testOwningEntityRequiredAndProjectOptional() throws Exception {
        User user = usersService.getUser(Constants.Users.SILVIA_ROBBINS_TYLER_SILVIA);
        relogin(user.credentials);

        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();

        Service service = servicesService.getService("c079d859-4d81-4add-a9c3-94551f96e2b0");

        browseASDCPage.clickDeployServiceButtonByServiceUUID(service.uuid);
        validateServiceCreationDialog(service);

        browseASDCPage.setInstanceName(browseASDCPage.generateInstanceName());

        assertDropdownPermittedItemsByLabel(user.subscriberNames, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
        browseASDCPage.selectSubscriberById("e433710f-9217-458d-a79d-1c7aff376d89");

        String serviceType = "TYLER SILVIA";
        GeneralUIUtils.findAndWaitByText(serviceType, 30);

        assertDropdownPermittedItemsByValue(user.serviceTypes, Constants.CreateNewInstance.SERVICE_TYPE_OPTION_CLASS);
        browseASDCPage.selectServiceTypeByName(serviceType);

        browseASDCPage.clickConfirmButton();

        GeneralUIUtils.findAndWaitByText("Missing data", 5);

        Click.onFirstSelectOptionById(Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID);

        browseASDCPage.clickConfirmButton();
        assertSuccessfulServiceInstanceCreation();
    }

    //@Test(groups = {"shouldBeMigratedToWorkWithSimulator"})
    protected void testLineOfBusinessOptionalAndPlatformRequired() throws Exception {

        User user = usersService.getUser(Constants.Users.SILVIA_ROBBINS_TYLER_SILVIA);
        relogin(user.credentials);

        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();

        Service service = servicesService.getService("c079d859-4d81-4add-a9c3-94551f96e2b0");

        browseASDCPage.clickDeployServiceButtonByServiceUUID(service.uuid);
        validateServiceCreationDialog(service);

        browseASDCPage.setInstanceName(browseASDCPage.generateInstanceName());

        assertDropdownPermittedItemsByLabel(user.subscriberNames, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
        browseASDCPage.selectSubscriberById("e433710f-9217-458d-a79d-1c7aff376d89");

        String serviceType = "TYLER SILVIA";
        GeneralUIUtils.findAndWaitByText(serviceType, 30);

        assertDropdownPermittedItemsByValue(user.serviceTypes, Constants.CreateNewInstance.SERVICE_TYPE_OPTION_CLASS);
        browseASDCPage.selectServiceTypeByName(serviceType);

        Click.onFirstSelectOptionById(Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID);

        browseASDCPage.clickConfirmButton();
        assertSuccessfulServiceInstanceCreation();

        browseASDCPage.clickCloseButton();
        GeneralUIUtils.ultimateWait();

        //now add the VNF
        ViewEditPage viewEditPage = new ViewEditPage();

        viewEditPage.selectNodeInstanceToAdd("VID-RODERICK-05-15-17 0");
        viewEditPage.generateAndSetInstanceName(Constants.ViewEdit.VNF_INSTANCE_NAME_PREFIX);
        viewEditPage.selectProductFamily("a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb");
        viewEditPage.selectLcpRegion("AAIAIC25", "AIC");
        viewEditPage.selectTenant("092eb9e8e4b7412e8787dd091bc58e86");
        viewEditPage.setLegacyRegion("llkjhlkjhlkjh");

        browseASDCPage.clickConfirmButton();

        GeneralUIUtils.findAndWaitByText("Missing data", 5);

        Click.onFirstSelectOptionById(Constants.OwningEntity.PLATFORM_SELECT_TEST_ID);

        viewEditPage.clickConfirmButton();
        assertSuccessfulVNFCreation();
    }

    private void prepareSimulatorWithServiceModelListBeforeBrowseASDCService(){
        SimulatorApi.clearAll();

        ImmutableList<BasePreset> presets = ImmutableList.of(
                new PresetGetSessionSlotCheckIntervalGet(),
                new PresetAAIGetSubscribersGet(),
                new PresetAAIServiceDesignAndCreationPut(),
                new PresetAAIGetServicesGet());

        SimulatorApi.registerExpectationFromPresets(presets, SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
    }

    @Test
    private void browseSDCServiceModelListCheckAAIResponse(){
        prepareSimulatorWithServiceModelListBeforeBrowseASDCService();
        SideMenu.navigateToBrowseASDCPage();
        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        browseASDCPage.fillFilterText("CheckAAIResponse");
        GeneralUIUtils.ultimateWait();

        WebElement sdcTableElement = Get.byId("sdcModelsTable");
        List<WebElement> sdcModelList = sdcTableElement.findElements(By.className("sdcServiceModel"));

        WebElement sdcFirstModel = sdcModelList.get(0);
        WebElement sdcSecondModel = sdcModelList.get(1);
        WebElement sdcThirdModel = sdcModelList.get(2);

        //Check uuid
        By uuid = By.className("uuid");
        assertEquals(modelUuid, sdcFirstModel.findElement(uuid).getText());
        assertEquals("1dae721c-a1ef-435f-b811-760c23f467bf" , sdcSecondModel.findElement(uuid).getText());
        assertEquals("29236d45-e790-4c17-a115-1533cc09b7b1" , sdcThirdModel.findElement(uuid).getText());

        //Check invariantUUID
        By invariantUUID = By.className("invariantUUID");
        assertEquals(modelInvariantUUID1, sdcFirstModel.findElement(invariantUUID).getText());
        assertEquals(modelInvariantUUID2, sdcSecondModel.findElement(invariantUUID).getText());
        assertEquals(modelInvariantUUID3, sdcThirdModel.findElement(invariantUUID).getText());

        //Check Names
        By name = By.className("name");
        assertEquals("CheckAAIResponse_AAAvIRC_mm779p_Service" , sdcFirstModel.findElement(name).getText());
        assertEquals("CheckAAIResponse_BBBvIRC_mm779p_Service" , sdcSecondModel.findElement(name).getText());
        assertEquals("CheckAAIResponse_CCCvIRC_mm779p_Service" , sdcThirdModel.findElement(name).getText());

        //Check distribution Status
        By distributionStatus = By.className("distributionStatus");
        assertEquals("DISTRIBUTION_COMPLETE_OK" , sdcFirstModel.findElement(distributionStatus).getText());
        assertEquals("", sdcSecondModel.findElement(distributionStatus).getText());
        assertEquals("DISTRIBUTION_COMPLETE_ERROR" , sdcThirdModel.findElement(distributionStatus).getText());

        //Check another fields
        assertEquals("service" , sdcFirstModel.findElement(By.className("category")).getText());
        assertEquals("1.0" , sdcFirstModel.findElement(By.className("version")).getText());
    }

    @Test
    public void browseSdcModel_getEmptyList_noModelsMessageIsShown() {
        resetGetServicesCache();
        SimulatorApi.clearAll();
        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                new PresetAAIServiceDesignAndCreationPut(true),
                new PresetAAIGetSubscribersGet()), SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
        SideMenu.navigateToBrowseASDCPage();
        GeneralUIUtils.ultimateWait();
        WebElement serviceModelsTbody = Get.byXpath("//table[@data-tests-id='serviceModelsTable']/tbody");
        assertFalse(Exists.tagNameInAnotherElement(serviceModelsTbody, "tr"), "Table should be empty on empty results");
        resetGetServicesCache();
    }

    private static final String[] macroModelsIds = {
            "f1bde010-cc5f-4765-941f-75f15b24f9fc",
            "lmoser410-connector-model-version-id",
            "ipe-resource-id-ps-02",
            "797a6c41-0f80-4d35-a288-3920c4e06baa",
    };
    private static final String[] alacarteModelsIds = {
            "0903e1c0-8e03-4936-b5c2-260653b96413",
            "666a06ee-4b57-46df-bacf-908da8f10c3f",
            "20c4431c-246d-11e7-93ae-92361f002671",
    };

    @DataProvider
    public static Object[][] filterOrchestrationType() {
        return new Object[][]{{"a la carte", 3, alacarteModelsIds},{"macro", 4, macroModelsIds}};
    }

    @Test(dataProvider = "filterOrchestrationType")
    @FeatureTogglingTest(FLAG_SHOW_ORCHESTRATION_TYPE)
    public void browseSdcModel_filterModelsWithOrchestrationType_alacarte(
            String orchestrationType,int numberOfOccurrence, String[] expectedModelsIds) {
        resetGetServicesCache();
        SimulatorApi.clearAll();
        BrowseASDCPage browseAsdcPage = registerSimulatorAndGoToBrowseSDC();
        GeneralUIUtils.ultimateWait();
        assertThat(browseAsdcPage.countCurrentRowsInTable(),(Matchers.greaterThan(numberOfOccurrence)));
        browseAsdcPage.fillFilterText(orchestrationType);
        Assert.assertEquals(browseAsdcPage.countCurrentRowsInTable(),numberOfOccurrence);
        for(String id : expectedModelsIds) {
            Assert.assertTrue(browseAsdcPage.isModelWithGivenServiceUUIDVisible(id));
        }
        browseAsdcPage.fillFilterText("");
    }

}
