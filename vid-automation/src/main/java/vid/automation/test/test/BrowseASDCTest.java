package vid.automation.test.test;

import com.google.common.collect.ImmutableList;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.hamcrest.Matchers;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetServiceModelList;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetServicesGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIServiceDesignAndCreationPut;
import org.onap.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.openecomp.sdc.ci.tests.datatypes.UserCredentials;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.*;
import vid.automation.test.model.Service;
import vid.automation.test.model.User;
import vid.automation.test.sections.*;
import vid.automation.test.services.ServicesService;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.utils.CookieAndJsonHttpHeadersInterceptor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;


public class BrowseASDCTest extends CreateInstanceDialogBaseTest {
    private final String invariantUUIDAlacarte = "a8dcd72d-d44d-44f2-aa85-53aa9ca99cba";
    private final String invariantUUIDMacro = "d27e42cf-087e-4d31-88ac-6c4b7585f800";
    private final String instantiationTypeNameAlacarte = "a la carte";
    private final String instantiationTypeNameMacro = "macro";
    private final String oldMacro = "old macro";
    private String modelInvariantUUID1 = "aeababbc-010b-4a60-8df7-e64c07389466";
    private String modelInvariantUUID2 = "aa2f8e9c-9e47-4b15-a95c-4a9385599abc";
    private String modelInvariantUUID3 = "d849c57d-b6fe-4843-8349-4ab8bbb08d71";
    private String modelUuid = "a8dcd72d-d44d-44f2-aa85-53aa9ca99cba";
    protected final RestTemplate restTemplate = new RestTemplate();

    private ServicesService servicesService = new ServicesService();
    final URI envUrI;
    protected final URI uri;

    public BrowseASDCTest() throws URISyntaxException {
        this.envUrI = new URI(System.getProperty("ENV_URL"));
        this.uri = new JerseyUriBuilder().host(envUrI.getHost()).port(envUrI.getPort()).scheme("http").path("vid").build();
    }

    @BeforeClass
    public void login() {
        UserCredentials userCredentials = getUserCredentials();
        final List<ClientHttpRequestInterceptor> interceptors = singletonList(new CookieAndJsonHttpHeadersInterceptor(uri, userCredentials));
        restTemplate.setInterceptors(interceptors);
    }

    @BeforeMethod
    public void invalidateTheCacheSelenium() {
        if (Features.FLAG_SERVICE_MODEL_CACHE.isActive()) {
            restTemplate.postForObject(uri + "/rest/models/reset", "", Object.class);
        }
    }

    @Override
    protected UserCredentials getUserCredentials() {
        User user = usersService.getUser(Constants.Users.MOBILITY_MOBILITY);
        return new UserCredentials(user.credentials.userId, user.credentials.password, Constants.Users.MOBILITY_MOBILITY, "", "");
    }

    @Test(groups = {"shouldBeMigratedToWorkWithSimulator"})
    public void testPNFOnCreatePopup() {
        Service service = servicesService.getService("f39389e4-2a9c-4085-8ac3-04aea9c651be");
        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();
        browseASDCPage.clickDeployServiceButtonByServiceUUID(service.uuid);
        assertThatServiceCreationDialogIsVisible();
        validatePNFCreationDialog(service, "Mobility", "pnf");
    }

    private void validatePNFCreationDialog(Service service, String serviceType, String serviceRole) {
        assertServiceMetadata(serviceType, Constants.SERVICE_TYPE);
        assertServiceMetadata(serviceRole, Constants.SERVICE_ROLE);
        validateServiceCreationDialog(service);
    }

//    @BeforeMethod
//    public void clearSimulator() {
//        SimulatorApi.clearAll();
//    }

    @Test(groups = {"shouldBeMigratedToWorkWithSimulator"})
    private void testPNFMacroInstantation() throws Exception {
        User user = usersService.getUser(Constants.Users.MOBILITY_MOBILITY);
        relogin(user.credentials);

        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();
        browseASDCPage.clickDeployServiceButtonByServiceUUID("f39389e4-2a9c-4085-8ac3-04aea9c651be");
        assertThatServiceCreationDialogIsVisible();
        assertDropdownPermittedItemsByLabel(user.subscriberNames, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
        browseASDCPage.selectSubscriberById("a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb");
        browseASDCPage.selectProductFamily("ebc3bc3d-62fd-4a3f-a037-f619df4ff034");
        GeneralUIUtils.ultimateWait();

        browseASDCPage.selectServiceTypeByName("Mobility");
        GeneralUIUtils.ultimateWait();
        browseASDCPage.selectLcpRegion("mtn16");

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

//    @Test
//    private void browseServiceModel_deployServiceUnexpectedDeploymentMode_creationPopupIsMacro() throws Exception {
//        deployServiceAndAssertInstantiationType(
//                "don't know.zip",
//                "a8dcd72d-d44d-44f2-aa85-53aa9ca99cba",
//                "macro"
//        );
//    }

    @Test
    private void browseServiceModel_deployServiceALaCarteByBackendInput_creationPopupIsALaCarte() throws Exception {
        // model uuid should be of macro
        deployServiceAndAssertInstantiationType(
                "csar15782222_instantiationTypeAlacarte_invariantUUIDMacro.zip",
                invariantUUIDMacro,
                instantiationTypeNameAlacarte
        );
    }

    @Test
    private void browseServiceModel_deployServiceALaCarteBecauseNotOnMACRO_SERVICESConfig_creationPopupIsALaCarte() throws Exception {
        deployServiceAndAssertInstantiationType(
                "csar15782222_instantiationTypeEmpty_invariantUUIDAlacarte.zip",
                invariantUUIDAlacarte,
                instantiationTypeNameAlacarte
        );
    }

    @Test
    private void browseServiceModel_deployServiceMacroByBackendInput_creationPopupIsMacro() throws Exception {
        deployServiceAndAssertInstantiationType(
                "csar15782222_instantiationTypeMacro_invariantUUIDAlacarte.zip",
                invariantUUIDAlacarte,
                instantiationTypeNameMacro
        );
    }

    @Test
    private void browseServiceModel_deployServiceMacroByMACRO_SERVICESConfig_creationPopupIsOldMacro() throws Exception {
        deployServiceAndAssertInstantiationType(
                "csar15782222_invariantUUIDMacro.zip",
                invariantUUIDMacro,
                oldMacro

        );
    }

    @Test
    private void browseServiceModel_deployServiceMacroWithPnf_creationPopupIsOldMacro() throws Exception {
        deployServiceAndAssertInstantiationType(
                "csar15782222_instantiationTypeMacroWithPnf.zip",
                invariantUUIDMacro,
                oldMacro

        );
    }

    @Test
    private void browseServiceModel_deployServiceMacroWithCR_creationPopupIsOldMacro() throws Exception {
        deployServiceAndAssertInstantiationType(
                "csar15782222_instantiationTypeMacroWithCR.zip",
                invariantUUIDMacro,
                oldMacro

        );
    }

    @Test
    private void browseServiceModel_deployServiceMacroWithNetwork_creationPopupIsMacroByFF() throws Exception {
        String macroInstantiationAccordingFF = Features.FLAG_ASYNC_INSTANTIATION.isActive() && Features.FLAG_NETWORK_TO_ASYNC_INSTANTIATION.isActive() ? instantiationTypeNameMacro : oldMacro;
        deployServiceAndAssertInstantiationType(
                "csar15782222_instantiationTypeMacroWithNetwork.zip",
                invariantUUIDMacro,
                macroInstantiationAccordingFF
        );
    }


    private void deployServiceAndAssertInstantiationType(String modelZipFileName, String modelInvariantId, String expectedInstantiationType) throws Exception {
        String modelVersionId = "4d71990b-d8ad-4510-ac61-496288d9078e";

        registerExpectationForLegacyServiceDeployment(modelVersionId, modelInvariantId, modelZipFileName, null);
        User user = usersService.getUser(Constants.Users.MOBILITY_MOBILITY);
        relogin(user.credentials);

        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();

        GeneralUIUtils.ultimateWait();
        browseASDCPage.clickDeployServiceButtonByServiceUUID(modelVersionId);
        DeployMacroDialogBase macroDialog = null;
        if (expectedInstantiationType.equals(instantiationTypeNameAlacarte)) {
            GeneralUIUtils.ultimateWait();
            browseASDCPage.clickCancelButtonByTestID();
        } else { //macro
            macroDialog = expectedInstantiationType.equals(oldMacro) ? new DeployMacroDialogOld() : getMacroDialog();
            macroDialog.assertTitle();
            macroDialog.closeDialog();
        }
    }

    @Test
    private void testServiceInstantiationAlaCarte() throws Exception {
        User user = usersService.getUser(Constants.Users.MOBILITY_MOBILITY);
        relogin(user.credentials);

        String zipFileName = "serviceCreationTest.zip";
        String modelVersionId = "aa2f8e9c-9e47-4b15-a95c-4a9385599abc"; //uuid of model. must be same as in serviceCreationTest.zip
        String modelInvariantId = invariantUUIDAlacarte; //must be same as in serviceCreationTest.zip

        registerExpectationForLegacyServiceDeployment(modelVersionId, modelInvariantId, zipFileName, null);

        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();

        Service service = new Service(
                "pnf",
                modelVersionId,
                modelInvariantId,
                "action-data",
                "1.0",
                "Network L1-3",
                "ADIOD vMX vPE based on Juniper 17.2 release. Updated with updated VF for v8.0 of VLM",
                null);

        browseASDCPage.clickDeployServiceButtonByServiceUUID(service.uuid);
        validateServiceCreationDialog(service);

        browseASDCPage.setInstanceName(browseASDCPage.generateInstanceName());

        assertDropdownPermittedItemsByLabel(user.subscriberNames, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
        browseASDCPage.selectSubscriberById("a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb");

        String serviceType = "Mobility";
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
        SimulatorApi.registerExpectation(SimulatorApi.RegistrationStrategy.APPEND,
                "ecompportal_getSessionSlotCheckInterval.json",
                "browseASDC/aai_get_services.json",
                "browseASDC/get_aai_get_subscribers.json",
                "browseASDC/get_sdc_catalog_services_2f80c596.json",
                "browseASDC/service_design_and_creation.json");
        SideMenu.navigateToBrowseASDCPage();
        BrowseASDCPage browseAsdcPage = new BrowseASDCPage();
        GeneralUIUtils.ultimateWait();
        assertThat(browseAsdcPage.countCurrentRowsInTable(),(Matchers.greaterThan(1)));
        browseAsdcPage.fillFilterText(filterText);
        Assert.assertEquals(browseAsdcPage.countCurrentRowsInTable(),1);
        Assert.assertTrue(Exists.byTestId("deploy-" + serviceUuid));
        browseAsdcPage.fillFilterText("");

    }

    @Test
    private void testCategoryParamsDropdownsExistsInCreationDialog() throws Exception {
        SimulatorApi.registerExpectation(SimulatorApi.RegistrationStrategy.APPEND,
                "ecompportal_getSessionSlotCheckInterval.json",
                "browseASDC/aai_get_services.json",
                "browseASDC/get_aai_get_subscribers.json",
                "browseASDC/get_sdc_catalog_services_2f80c596.json",
                "browseASDC/service_design_and_creation.json");
        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);
        relogin(user.credentials);
        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();
        Service service = servicesService.getService("2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd");
        browseASDCPage.clickDeployServiceButtonByServiceUUID(service.uuid);
        DeployMacroDialogBase deployMacroDialog = getMacroDialog();
        deployMacroDialog.assertDialogExists();
        deployMacroDialog.clickProjectSelect();
        deployMacroDialog.clickOwningEntitySelect();
    }

    @Test(groups = {"shouldBeMigratedToWorkWithSimulator"})
    private void testOwningEntityRequiredAndProjectOptional() throws Exception {
        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);
        relogin(user.credentials);

        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();

        Service service = servicesService.getService("c079d859-4d81-4add-a9c3-94551f96e2b0");

        browseASDCPage.clickDeployServiceButtonByServiceUUID(service.uuid);
        validateServiceCreationDialog(service);

        browseASDCPage.setInstanceName(browseASDCPage.generateInstanceName());

        assertDropdownPermittedItemsByLabel(user.subscriberNames, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
        browseASDCPage.selectSubscriberById("e433710f-9217-458d-a79d-1c7aff376d89");

        String serviceType = "VIRTUAL USP";
        GeneralUIUtils.findAndWaitByText(serviceType, 30);

        assertDropdownPermittedItemsByValue(user.serviceTypes, Constants.CreateNewInstance.SERVICE_TYPE_OPTION_CLASS);
        browseASDCPage.selectServiceTypeByName(serviceType);

        browseASDCPage.clickConfirmButton();

        GeneralUIUtils.findAndWaitByText("Missing data", 5);

        Click.onFirstSelectOptionById(Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID);

        browseASDCPage.clickConfirmButton();
        assertSuccessfulServiceInstanceCreation();
    }

    @Test(groups = {"shouldBeMigratedToWorkWithSimulator"})
    protected void testLineOfBusinessOptionalAndPlatformRequired() throws Exception {

        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);
        relogin(user.credentials);

        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();

        Service service = servicesService.getService("c079d859-4d81-4add-a9c3-94551f96e2b0");

        browseASDCPage.clickDeployServiceButtonByServiceUUID(service.uuid);
        validateServiceCreationDialog(service);

        browseASDCPage.setInstanceName(browseASDCPage.generateInstanceName());

        assertDropdownPermittedItemsByLabel(user.subscriberNames, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
        browseASDCPage.selectSubscriberById("e433710f-9217-458d-a79d-1c7aff376d89");

        String serviceType = "VIRTUAL USP";
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

        viewEditPage.selectNodeInstanceToAdd("VID-PCRF-05-15-17 0");
        viewEditPage.generateAndSetInstanceName(Constants.ViewEdit.VNF_INSTANCE_NAME_PREFIX);
        viewEditPage.selectProductFamily("a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb");
        viewEditPage.selectLCPRegion("AAIAIC25");
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
                new PresetAAIGetServiceModelList(modelUuid, modelInvariantUUID1, modelInvariantUUID2, modelInvariantUUID3),
                new PresetAAIGetServicesGet());

        SimulatorApi.registerExpectationFromPresets(presets, SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
    }

    @Test
    private void browseSDCServiceModelListCheckAAIResponse(){
        prepareSimulatorWithServiceModelListBeforeBrowseASDCService();
        SideMenu.navigateToBrowseASDCPage();
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
        assertEquals("AAAvIRC_mm779p_Service" , sdcFirstModel.findElement(name).getText());
        assertEquals("BBBvIRC_mm779p_Service" , sdcSecondModel.findElement(name).getText());
        assertEquals("CCCvIRC_mm779p_Service" , sdcThirdModel.findElement(name).getText());

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
    public void browseSdcModel_getEmptyList_noModelsMessageIsShown() throws Exception {
        SimulatorApi.clearAll();
        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                new PresetAAIServiceDesignAndCreationPut(true),
                new PresetAAIGetSubscribersGet()), SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
        SideMenu.navigateToBrowseASDCPage();
        GeneralUIUtils.ultimateWait();
        WebElement serviceModelsTbody = Get.byXpath("//table[@data-tests-id='serviceModelsTable']/tbody");
        assertFalse(Exists.tagNameInAnotherElement(serviceModelsTbody, "tr"), "Table should be empty on empty results");
    }
}
