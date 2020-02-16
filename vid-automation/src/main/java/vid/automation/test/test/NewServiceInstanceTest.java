package vid.automation.test.test;

import static java.util.Collections.emptyList;
import static junit.framework.TestCase.assertNull;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MTN6_TO_ATT_AIC;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId.PRESET_SOME_LEGACY_REGION_TO_ATT_AIC;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet.COMPLETE;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet.DEFAULT_SERVICE_INSTANCE_ID;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;
import static vid.automation.test.infra.Features.FLAG_1902_VNF_GROUPING;
import static vid.automation.test.infra.Features.FLAG_1908_COLLECTION_RESOURCE_NEW_INSTANTIATION_UI;
import static vid.automation.test.infra.Features.FLAG_1908_INFRASTRUCTURE_VPN;
import static vid.automation.test.infra.Features.FLAG_1908_MACRO_NOT_TRANSPORT_NEW_VIEW_EDIT;
import static vid.automation.test.infra.Features.FLAG_1908_TRANSPORT_SERVICE_NEW_INSTANTIATION_UI;
import static vid.automation.test.infra.Features.FLAG_2002_ANY_ALACARTE_BESIDES_EXCLUDED_NEW_INSTANTIATION_UI;
import static vid.automation.test.infra.Features.FLAG_2006_VFMODULE_TAKES_TENANT_AND_REGION_FROM_VNF;
import static vid.automation.test.infra.Features.FLAG_5G_IN_NEW_INSTANTIATION_UI;
import static vid.automation.test.infra.Features.FLAG_ENABLE_WEBPACK_MODERN_UI;
import static vid.automation.test.infra.ModelInfo.aLaCarteNetworkProvider5G;
import static vid.automation.test.infra.ModelInfo.aLaCarteVnfGroupingService;
import static vid.automation.test.infra.ModelInfo.collectionResourceService;
import static vid.automation.test.infra.ModelInfo.infrastructureVpnService;
import static vid.automation.test.infra.ModelInfo.macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetails;
import static vid.automation.test.infra.ModelInfo.macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetailsVnfEcompNamingFalse;
import static vid.automation.test.infra.ModelInfo.macroSriovWithDynamicFieldsEcompNamingFalsePartialModelDetailsVnfEcompNamingFalse;
import static vid.automation.test.infra.ModelInfo.macroSriovWithDynamicFieldsEcompNamingTruePartialModelDetails;
import static vid.automation.test.infra.ModelInfo.transportWithPnfsService;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPreset;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPresets;
import static vid.automation.test.test.ALaCarteflowTest.AIC;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetL3NetworksByCloudRegionSpecificState;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetTenants;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetVpnsByType;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIPostNamedQueryForViewEdit;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseCreateInstancePost;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateNetworkALaCarte5G;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceAlacarte;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceGen2WithNamesAlacarteGroupingService;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceGen2WithNamesEcompNamingFalse;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateVfModuleALaCarteE2E;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateVnfALaCarteE2E;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestsGet5GServiceInstanceAndNetwork;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestsGet5GServiceInstanceAndNetwork.ResponseDetails;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys;
import org.onap.simulator.presetGenerator.presets.mso.PresetMsoCreateMacroCommonPre1806;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.Constants.BrowseASDC.NewServicePopup;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.Input;
import vid.automation.test.infra.ModelInfo;
import vid.automation.test.infra.ModelInfoWithCustomization;
import vid.automation.test.infra.SelectOption;
import vid.automation.test.infra.Wait;
import vid.automation.test.model.Service;
import vid.automation.test.model.User;
import vid.automation.test.sections.BrowseASDCPage;
import vid.automation.test.sections.DrawingBoardPage;
import vid.automation.test.sections.InstantiationStatusPage;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.sections.VidBasePage;
import vid.automation.test.services.AsyncJobsService;
import vid.automation.test.services.ServicesService;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.test.NewServiceInstanceTest.ServiceData.IS_GENERATED_NAMING;
import vid.automation.test.utils.ReadFile;

@FeatureTogglingTest(FLAG_ENABLE_WEBPACK_MODERN_UI)
public class NewServiceInstanceTest extends ModernUITestBase {

    public static final String COMPLETED = "COMPLETED";
    private static final String IN_PROGRESS = "IN_PROGRESS";
    private static final String PENDING = "PENDING";
    private final String vfModule0Name = "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0";
    private final String vfModule0UUID = "f8360508-3f17-4414-a2ed-6bc71161e8db";
    private ServicesService servicesService = new ServicesService();
    private DrawingBoardPage drawingBoardPage = new DrawingBoardPage();
    List<String> serviceModelLabelList = Arrays.asList("Model version", "Description", "Category", "UUID",
            "Invariant UUID", "Service type", "Service role");
    List<String> mandatoryServiceModelLabelList = Arrays.asList("Model version", "UUID", "Invariant UUID");
    private final VidBasePage vidBasePage = new VidBasePage();
    public static final String VNF_SET_BUTTON_TEST_ID = "form-set";
    private static final Logger logger = LogManager.getLogger(NewServiceInstanceTest.class);
    private static final String NO_MAX_INSTANCES_IN_MODEL = "NO_MAX_INSTANCES_IN_MODEL";

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
    protected void goToWelcome() {
        SideMenu.navigateToWelcomePage();
    }

    @Override
    protected UserCredentials getUserCredentials() {
        String userName = Constants.Users.SILVIA_ROBBINS_TYLER_SILVIA;
        User user = usersService.getUser(userName);
        return new UserCredentials(user.credentials.userId, user.credentials.password, userName, "", "");
    }

    @Test
    public void createNewServiceInstance_fullModelData_LeftPaneLabelsCorrect() throws Exception {
        prepareServicePreset(macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetails, false);
        loadServicePopup(macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetails);
        assertServiceModelLabelsCorrect(serviceModelLabelList);
    }

    @Test
    public void createNewServiceInstance_partialModelData_LeftPaneLabelsCorrect() throws Exception {
        prepareServicePreset(macroSriovWithDynamicFieldsEcompNamingTruePartialModelDetails, false);
        loadServicePopup(macroSriovWithDynamicFieldsEcompNamingTruePartialModelDetails);
        assertServiceModelLabelsCorrect(mandatoryServiceModelLabelList);
    }

    @Test
    public void createNewServiceInstance_setFieldValue_resetDependenciesListsAndValues() {
        resetGetTenantsCache();
        try {
            BrowseASDCPage browseASDCPage = new BrowseASDCPage();
            prepareServicePreset(macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetails, false);
            SimulatorApi.registerExpectation(Constants.RegisterToSimulator.CreateNewServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_CAR_2020_ER, SimulatorApi.RegistrationStrategy.APPEND);
            registerExpectationFromPreset(
                    new PresetAAIGetTenants(
                            "CAR_2020_ER",
                            "MSO-dev-service-type",
                            "registration_to_simulator/create_new_instance/aai_get_tenants_for_customer_CAR_2020_ER.json"),
                    SimulatorApi.RegistrationStrategy.APPEND);

            loadServicePopup(macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetails);
            Wait.waitByClassAndText(Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS, "SILVIA ROBBINS", 30);
            VidBasePage.selectSubscriberById("e433710f-9217-458d-a79d-1c7aff376d89");
            GeneralUIUtils.ultimateWait();
            String serviceType = "TYLER SILVIA";
            Wait.waitByClassAndText(Constants.CreateNewInstance.SERVICE_TYPE_OPTION_CLASS, serviceType, 30);
            browseASDCPage.selectServiceTypeByName(serviceType);
            String lcpRegion = "hvf6";
            Wait.waitByClassAndText("lcpRegionOption", lcpRegion, 30);
            viewEditPage.selectLcpRegion(lcpRegion, AIC);
            browseASDCPage.selectTenant("bae71557c5bb4d5aac6743a4e5f1d054");

            VidBasePage.selectSubscriberById("CAR_2020_ER");
            assertElementDisabled("lcpRegion-select");
            serviceType = "MSO-dev-service-type";
            Wait.waitByClassAndText(Constants.CreateNewInstance.SERVICE_TYPE_OPTION_CLASS, serviceType, 30);
            browseASDCPage.selectServiceTypeByName(serviceType);
            lcpRegion = "CAR_2020_ER";
            Wait.waitByClassAndText("lcpRegionOption", lcpRegion, 30);
            viewEditPage.selectLcpRegion(lcpRegion, AIC);
            browseASDCPage.selectTenant("092eb9e8e4b7412e8787dd091bc58e66");
        } finally {
            resetGetTenantsCache();
        }
    }

    /**
     * asserts that the provided labels list is visible and that no other detail item appears in the model details panel.
     */
    protected void assertServiceModelLabelsCorrect(List<String> serviceModelLabelList) throws Exception {
        WebElement genericPopup = getDriver().findElement(By.tagName("generic-form-popup"));
        WebElement modelInformation = genericPopup.findElement(By.id("model-information"));
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
        Service service = servicesService.getService(macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetails.modelVersionId);
        String prefix = NewServicePopup.SERVICE_MODEL_DATA_TEST_ID_VALUE_PREFIX;
        prepareServicePreset(macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetails, false);
        loadServicePopup(macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetails);
        logger.info("Expected service model properties: "+service.toString());
        assertModelDataCorrect(NewServicePopup.SERVICE_MODEL_FIELD_TO_DATA_TESTS_ID, prefix, service);
    }

    @Test
    public void createNewServiceInstance_macro_validPopupDataAndUI__ecompNamingFalse() {

        ServiceData serviceData = new ServiceData(
                macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetails.modelVersionId,
                new ArrayList<>(),
                IS_GENERATED_NAMING.FALSE, true, true, true,
                "2017-488_PASQUALE-vPE 0",
                "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1",
            0, NO_MAX_INSTANCES_IN_MODEL, new ArrayList<>(), "25284168-24bb-4698-8cb4-3f509146eca5", false);

        prepareServicePreset(macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetails, false);

        final String serviceInstanceName = createMacroService(serviceData, true);
        createVnf(serviceData, true, serviceInstanceName);

        createVfModule(serviceData, serviceInstanceName, false, false);

    }

    @Test(groups = "underDevelopment")
    public void createNewServiceInstance_macro_validPopupDataAndUI__dynamicFieldsEcompNamingFalse_DEV() {
        /*
        Upon failure in test dynamicFieldsEcompNamingFalse_FLESH(), exception will provide
        the needed data for this DEV method:

          1. "Current step" when the failure occurred
          2. "Random alphabetic" that was randomized while test
          3. "Starting reduxState" that was on the step that failed.

        These data can be used for, accordingly, 1. startInStep param; 2, randomAlphabetic
        param; 3. reduxForStep param.
         */

        // It should be easier to put `reduxForStep` in this file, to avoid Java's code-clutter and json escaping.
        final String reduxForStep = ReadFile.loadResourceAsString(
                "NewServiceInstanceTest/createNewServiceInstance_macro_validPopupDataAndUI__dynamicFieldsEcompNamingFalse.json");

        createNewServiceInstance_macro_validPopupDataAndUI__dynamicFieldsEcompNamingFalse_FLESH("DEV", 5, reduxForStep, "mCaNk");
    }

    @Test
    public void createNewServiceInstance_macro_validPopupDataAndUI__dynamicFieldsEcompNamingFalse() {
        createNewServiceInstance_macro_validPopupDataAndUI__dynamicFieldsEcompNamingFalse_FLESH("RUNTIME", 0, null, randomAlphabetic(5));
    }

    private void createNewServiceInstance_macro_validPopupDataAndUI__dynamicFieldsEcompNamingFalse_FLESH(String mode, int startInStep, String reduxForStep, String randomAlphabetic) {

        MutableInt i = new MutableInt();
        Map<String, String> reduxStates = new HashMap<>();

        ServiceData serviceData = new ServiceData(
                macroSriovWithDynamicFieldsEcompNamingFalsePartialModelDetailsVnfEcompNamingFalse.modelVersionId,
                Collections.singletonList("2017488 pasqualevpe0 asn:"),
                IS_GENERATED_NAMING.FALSE, false, true, false,
                "2017-488_PASQUALE-vPE 0",
                "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1", 0,
                NO_MAX_INSTANCES_IN_MODEL, ImmutableList.of("Bandwidth", "Bandwidth units"),
                "25284168-24bb-4698-8cb4-3f509146eca5", false);

        // this is the instance-name that createMacroService is going to use
        String serviceInstanceName = randomAlphabetic + "instancename";

        doReduxStep(reduxStates, randomAlphabetic, startInStep, reduxForStep, i, mode, () -> {
            prepareServicePreset(macroSriovWithDynamicFieldsEcompNamingFalsePartialModelDetailsVnfEcompNamingFalse,
                    false);
            createMacroService(serviceData, false, randomAlphabetic, true, 3);
        });

        doReduxStep(reduxStates, randomAlphabetic, startInStep, reduxForStep, i, mode, () ->
                createVnf(serviceData, false, serviceInstanceName)
        );

        final String vnfInstanceName2 = randomAlphabetic + "instanceName";
        final String vnfName2 = "2017-388_PASQUALE-vPE";

        doReduxStep(reduxStates, randomAlphabetic, startInStep, reduxForStep, i, mode, () ->
                createVnf(new VnfData(vnfName2 + " 0", "afacccf6-397d-45d6-b5ae-94c39734b168", vnfInstanceName2, false),
                        false, serviceInstanceName)
        );

        doReduxStep(reduxStates, randomAlphabetic, startInStep, reduxForStep, i, mode, () ->
                createVfModule(serviceData, serviceInstanceName, false, true)
        );

        doReduxStep(reduxStates, randomAlphabetic, startInStep, reduxForStep, i, mode, () -> {

            editVfModuleAndJustSetName(vfModule0Name, vfModule0UUID);
            duplicateVnf(serviceData.vnfData, 2);
            vidBasePage.screenshotDeployDialog(serviceInstanceName);
        });

        doReduxStep(reduxStates, randomAlphabetic, startInStep, reduxForStep, i, mode, () -> {
            prepareServicePreset(macroSriovWithDynamicFieldsEcompNamingFalsePartialModelDetailsVnfEcompNamingFalse,
                    true);

            final String vfModuleName1 = "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0";
            final String vfModuleName2 = "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1";
            final String request1 = PresetMSOBaseCreateInstancePost.DEFAULT_REQUEST_ID;
            final String request2 = "ce010256-3fdd-4cb5-aed7-37112a2c6e93";
            final ImmutableMap<Keys, String> vars = ImmutableMap.<Keys, String>builder()
                    .put(Keys.SERVICE_NAME, serviceInstanceName)
                    .put(Keys.VNF_NAME, cleanSeparators("2017-488_PASQUALE-vPE", serviceData.vnfData.vnfInstanceName))
                    .put(Keys.VFM_NAME1, cleanSeparators(vfModuleName1 , "VF instance name ZERO"))
                    .put(Keys.VFM_NAME2, cleanSeparators(vfModuleName2 , "VF instance name"))
                    .put(Keys.VG_NAME, cleanSeparators(vfModuleName2 , "VF instance name") + "_vol_abc")
                    .put(Keys.VNF_NAME2, cleanSeparators(vnfName2, vnfInstanceName2))
                    .build();
            registerExpectationFromPresets(ImmutableList.of(
                    // although "some legacy region" is provided for vnf, Service's region "hvf6" overrides it
                    PRESET_MTN6_TO_ATT_AIC,
                    new PresetMSOCreateServiceInstanceGen2WithNamesEcompNamingFalse(vars, 0, request1),
                    new PresetMSOCreateServiceInstanceGen2WithNamesEcompNamingFalse(vars, 1, request2)
            ), SimulatorApi.RegistrationStrategy.APPEND);

            deployAndVerifyModuleInPendingTableMacro(serviceInstanceName, request1, request2);
            verifyOpenAuditInfo(serviceInstanceName);
            verifyOpenViewEdit(serviceInstanceName);
            verifyDeleteJob(serviceInstanceName);
            verifyHideJob(serviceInstanceName);
        });
    }

    @Test
    @FeatureTogglingTest(FLAG_1908_INFRASTRUCTURE_VPN)
    public void createNewServiceInstance_infraStructureVpn() {
        String requestId = uuid();
        String instanceId = uuid();

        prepareServicePreset(infrastructureVpnService, false);

        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
            PRESET_MTN6_TO_ATT_AIC,
                new PresetAAIGetL3NetworksByCloudRegionSpecificState("irma-aic", "hvf6", "bae71557c5bb4d5aac6743a4e5f1d054"),
            new PresetAAIGetVpnsByType()
        ), APPEND);

        ServiceData serviceData = new ServiceData(
                infrastructureVpnService.modelVersionId,
                new ArrayList<>(),
                IS_GENERATED_NAMING.TRUE_BUT_GIVE_NAME_EITHER_WAY, true, true, false,
                null,
                null, 0, null, new ArrayList<>(), null, false);
        final String serviceInstanceName = createMacroService(serviceData, false);

        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
            PresetMsoCreateMacroCommonPre1806.ofServiceWithVRF(requestId, instanceId, serviceInstanceName),
            new PresetMSOOrchestrationRequestGet(COMPLETE, requestId)
        ), APPEND);

        // Wizard pages of Network and VPN
        Click.byTestIdOnceItsAvailable("10a74149-c9d7-4918-bbcf-d5fb9b1799ce", 20);
        clickToCloseModal("setMembersBtn");

        Click.byTestIdOnceItsAvailable("120d39fb-3627-473d-913c-d228dd0f8e5b", 20);
        clickToCloseModal("setMembersBtn");

        Assert.assertEquals(Get.byTestId("node-type-indicator").getText(),"VRF");

        drawingBoardPage.deploy();
        drawingBoardPage.verifyServiceCompletedOnTime(serviceInstanceName, "Service "+serviceInstanceName);
    }

    @Test
    @FeatureTogglingTest(FLAG_1908_COLLECTION_RESOURCE_NEW_INSTANTIATION_UI)
    public void createNewServiceInstance_collectionResource() {
        prepareServicePreset(collectionResourceService, false);
        String requestId = uuid();
        String instanceId = uuid();

        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                PRESET_MTN6_TO_ATT_AIC,
                PresetMsoCreateMacroCommonPre1806.ofCollectionResource(requestId, instanceId),
                new PresetMSOOrchestrationRequestGet(COMPLETE, requestId)
        ), APPEND);

        ServiceData serviceData = new ServiceData(
                collectionResourceService.modelVersionId,
                new ArrayList<>(),
                IS_GENERATED_NAMING.TRUE, true, true, false,
                null,
                null, 0, null, new ArrayList<>(), null, false);
        createMacroService(serviceData, false, randomAlphabetic(5), true, 1);

        drawingBoardPage.deploy();
        drawingBoardPage.verifyServiceCompletedOnTime("CR_sanity", "service with collection resource");
    }

    @Test
    @FeatureTogglingTest(FLAG_1908_TRANSPORT_SERVICE_NEW_INSTANTIATION_UI)
    public void createNewServiceInstance_transportService() {
        prepareServicePreset(transportWithPnfsService, false);
        String requestId = uuid();
        String instanceId = uuid();

        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                PresetMsoCreateMacroCommonPre1806.ofTransportService(requestId, instanceId),
                new PresetMSOOrchestrationRequestGet(COMPLETE, requestId)
            ), APPEND);

        ServiceData serviceData = new ServiceData(
                transportWithPnfsService.modelVersionId,
                new ArrayList<>(),
                IS_GENERATED_NAMING.TRUE, true, true, false,
                null,
                null, 0, null, new ArrayList<>(), null, false);
        createMacroService(serviceData, false, randomAlphabetic(5), false, 1);

        drawingBoardPage.deploy();
        drawingBoardPage.verifyServiceCompletedOnTime("AIM Transport SVC_ym161f", "transport service");
    }

    @Test
    @FeatureTogglingTest(FLAG_1902_VNF_GROUPING)
    public void createNewServiceInstance_aLaCarte_VnfGrouping() {

        String randomAlphabetic = randomAlphabetic(5);

        ServiceData serviceData = new ServiceData(
                aLaCarteVnfGroupingService.modelVersionId,
                ImmutableList.of(),
                IS_GENERATED_NAMING.FALSE, false, true, false,
                null, null, 0, null, ImmutableList.of(), null, false);
        prepareServicePreset(aLaCarteVnfGroupingService, false);

        createALaCarteService(serviceData, randomAlphabetic);

        // this is the instance-name that createALaCarteService is using
        String serviceInstanceName = randomAlphabetic + "instancename";

        final String requestId = PresetMSOBaseCreateInstancePost.DEFAULT_REQUEST_ID;
        final String serviceInstanceId = "d2391436-8d55-4fde-b4d5-72dd2cf13cgh";
        final ImmutableMap<Keys, String> names = ImmutableMap.<Keys, String>builder()
                .put(Keys.SERVICE_NAME, serviceInstanceName)
                .build();
        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                new PresetMSOCreateServiceInstanceGen2WithNamesAlacarteGroupingService(names, 0, requestId, serviceInstanceId, "us16807000"),
                new PresetAAIPostNamedQueryForViewEdit(serviceInstanceId, serviceInstanceName, false, false)
        ), SimulatorApi.RegistrationStrategy.APPEND);

        drawingBoardPage.deploy();
        verifyModuleInPendingTable(serviceInstanceName, requestId, null, ImmutableSet.of(IN_PROGRESS), false, false);
        verifyModuleInPendingTable(serviceInstanceName, requestId, null, ImmutableSet.of(COMPLETED), false, true);
        InstantiationStatusPage.verifyOpenNewViewEdit(serviceInstanceName, serviceInstanceId, aLaCarteVnfGroupingService.modelVersionId, "TYLER SILVIA", "e433710f-9217-458d-a79d-1c7aff376d89", "EDIT");
    }

    public interface Invoker{
        void invoke();
    }

    private void doReduxStep(Map<String, String> reduxStates, String randomAlphabetic, int startInStep, String reduxForStep, MutableInt currentStep, String mode, Invoker todo) {
        try {
            switch (mode) {
                case "DEV":
                    if (currentStep.getValue() < startInStep) {
                        // skip up to startInStep
                        return;
                    } else if (currentStep.getValue() == startInStep) {

                        vidBasePage.setReduxState(reduxForStep);

                        vidBasePage.navigateTo("serviceModels.htm#/servicePlanning?serviceModelId=6b528779-44a3-4472-bdff-9cd15ec93450");
                        vidBasePage.goToIframe();
                    }

                    reduxStates.put(String.valueOf(currentStep), vidBasePage.getReduxState());
                    break;

                case "RUNTIME":
                default:
                    // log current redux state, before invocation
                    reduxStates.put(String.valueOf(currentStep), vidBasePage.getReduxState());
                    logger.info("reduxGator runtime reduxState for step {}:\n{}", currentStep, vidBasePage.getReduxState());
                    break;
            }

            try {
                todo.invoke();
            } catch (AssertionError | Exception e) {
                throw new AssertionError(String.join("\n",
                        "Current step: " + currentStep,
                        "Random alphabetic: " + randomAlphabetic,
                        "Starting reduxState: " + reduxStates.get(String.valueOf(currentStep)),
                        "Current reduxState:  " + vidBasePage.getReduxState()
                ), e);
            }
        } finally {
            logger.info("Cumulative reduxState: {}", reduxStates);
            currentStep.increment();
        }
    }

    //@Step("duplicate vnf")
    private void duplicateVnf(VnfData vnfData, int count) {
        hoverAndClickDuplicateButton(extractNodeToEdit(vnfData));
        vidBasePage.screenshotDeployDialog("duplicateVnf-" + vnfData.vnfName);
        List<WebElement> options = ((RemoteWebElement)Get.byId("duplicate-select")).findElementsByTagName("option");
        assertThat(options.stream().map(x -> x.getText()).collect(Collectors.toList()), Matchers.contains("1","2"));
        SelectOption.byIdAndVisibleText("duplicate-select", String.valueOf(count));
        Click.byClassAndVisibleText("sdc-button__primary", "DUPLICATE");
    }

    private String cleanSeparators(String... s) {
        return String.join("", s).replace(" ", "");
    }

    //@Step("edit vf module and just set name")
    private void editVfModuleAndJustSetName(String vfModuleName, String vfModuleUUID) {
        hoverAndClickEditButton(vfModuleUUID + "-" + vfModuleName);
        Input.text("VF instance name ZERO", "instanceName");
        clickToCloseModal(VNF_SET_BUTTON_TEST_ID);
    }

    @Test
    public void createNewServiceInstance_macro_validPopupDataAndUI__ecompNamingServiceFalseVnfTrue_vgNameFalse() {
        ServiceData serviceData = new ServiceData(
                macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetails.modelVersionId,
                new ArrayList<>(),
                IS_GENERATED_NAMING.FALSE, true, false, true,
                "2017-488_PASQUALE-vPE 0",
                vfModule0Name, 1, "1", new ArrayList<>(), vfModule0UUID, false);

        prepareServicePreset(macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetails, false);

        final String serviceInstanceName = createMacroService(serviceData, true);
        createVnf(serviceData, true, serviceInstanceName);
        createVfModule(serviceData, serviceInstanceName, true, false);

    }

    @Test
    public void createNewServiceInstance_macro_validPopupDataAndUI__ecompNamingServiceFalseVnfFalse_vgNameFalse() {
        ServiceData serviceData = new ServiceData(
                macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetailsVnfEcompNamingFalse.modelVersionId,
                new ArrayList<>(),
                IS_GENERATED_NAMING.FALSE, false, false, false,
                "2017-488_PASQUALE-vPE 0",
                vfModule0Name, 1, "1", new ArrayList<>(), vfModule0UUID, false);

        prepareServicePreset(macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetailsVnfEcompNamingFalse, false);

        final String serviceInstanceName = createMacroService(serviceData, true);
        createVnf(serviceData, true, serviceInstanceName);
        createVfModule(serviceData, serviceInstanceName, true, false);

    }

    @Test
    public void createNewServiceInstance_macro_validPopupDataAndUI__ecompNamingServiceFalseVnfFalse_vgNameTrue() throws Exception {
        ServiceData serviceData = new ServiceData(
                macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetailsVnfEcompNamingFalse.modelVersionId,
                new ArrayList<>(),
                IS_GENERATED_NAMING.FALSE, false, true, false,
                "2017-488_PASQUALE-vPE 0",
                "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1", 0,
                NO_MAX_INSTANCES_IN_MODEL, new ArrayList<>(), "25284168-24bb-4698-8cb4-3f509146eca5", false);

        prepareServicePreset(macroSriovNoDynamicFieldsEcompNamingFalseFullModelDetailsVnfEcompNamingFalse, false);

        final String serviceInstanceName = createMacroService(serviceData, true);
        createVnf(serviceData, true, serviceInstanceName);
        clickRemoveVfModule(vfModule0UUID, vfModule0Name);
        createVfModule(serviceData, serviceInstanceName, false, true);

    }

    @Test
    @FeatureTogglingTest(FLAG_2002_ANY_ALACARTE_BESIDES_EXCLUDED_NEW_INSTANTIATION_UI)
    public void createNewServiceInstance_aLaCarte_WithVnf() {
        final ModelInfo serviceModelInfo = ModelInfo.aLaCarteServiceCreationNewUI;
        String serviceInstanceName = "ALaCarteWithVnf"+randomAlphabetic(5);
        String vnfInstanceName= "VnfForALaCarte"+randomAlphabetic(5);
        VnfData vnfData = new VnfData("vOCG_1804_VF 0", "aca3f7b1-15f9-45a5-b182-b8b5aca84a76", vnfInstanceName, true);
        VfData vfmData = new VfData("vocg_1804_vf0..Vocg1804Vf..base_ocg..module-0", false, 1, "1", emptyList(), "815db6e5-bdfd-4cb6-9575-82c36df8747a", null);
        ServiceData serviceData = new ServiceData(IS_GENERATED_NAMING.TRUE, vnfData, vfmData, true);

        resetGetServicesCache();

        prepareServicePreset(serviceModelInfo, true);

        String requestorID = getUserCredentials().getUserId();

        String serviceRequestId = uuid();
        String serviceInstanceId = uuid();
        String vnfRequestId = uuid();
        String vnfInstanceId = uuid();
        String vfm0RequestId = uuid();
        String vfm0InstanceId = uuid();
        String vg1RequestId = uuid();
        String vg1InstanceId = uuid();
        String vfm1RequestId = uuid();
        String vfm1InstanceId = uuid();
        String vfm12RequestId = uuid();
        String vfm12InstanceId = uuid();

        ModelInfoWithCustomization vfm0 = new ModelInfoWithCustomization(
            "815db6e5-bdfd-4cb6-9575-82c36df8747a",
            "e9c795c8-6b98-4db3-bd90-a84b8ca5181b",
            "Vocg1804Vf..base_ocg..module-0",
            "4",
            "vfModule",
            "Vocg1804Vf..base_ocg..module-0",
            "a7b333d7-7633-4197-b40d-80fcfcadee94");

        ModelInfoWithCustomization vg1 = new ModelInfoWithCustomization(
            "9c219e70-1177-494b-8977-1395c9f9168c",
            "0ad14d60-98b6-4575-a9b8-458a796c3f98",
            "Vocg1804Vf..ocgmgr..module-1",
            "4",
            "volumeGroup",
            "Vocg1804Vf..ocgmgr..module-1",
            "f332f3ce-434d-4084-a1e7-5261c16d4940"
        );

        ModelInfoWithCustomization vfm1 = new ModelInfoWithCustomization(
            "9c219e70-1177-494b-8977-1395c9f9168c",
            "0ad14d60-98b6-4575-a9b8-458a796c3f98",
            "Vocg1804Vf..ocgmgr..module-1",
            "4",
            "vfModule",
            "Vocg1804Vf..ocgmgr..module-1",
            "f332f3ce-434d-4084-a1e7-5261c16d4940"
        );

        ModelInfoWithCustomization vfm12 = new ModelInfoWithCustomization(
            "b601eef4-62fd-4201-a788-ae30e06a1aec",
            "e3cb8b85-7a3c-4897-b20b-70640c26d671",
            "Vocg1804Vf..ocgapp_001..module-12",
            "2",
            "vfModule",
            "Vocg1804Vf..ocgapp_001..module-12",
            "fcc82961-865e-4d38-9c7a-207c511405b6"
        );

        final String vgName = "vg_for_module1";
        
        String vgRelatedInstance = ",{\"relatedInstance\": {"
            + "                    \"modelInfo\": {"
            + "                        \"modelType\": \"volumeGroup\""
            + "                    },"
            + "                    \"instanceId\": \""+vg1InstanceId+"\","
            + "                    \"instanceName\": \""+vgName+"\""
            + "                }}";


        registerExpectationFromPresets(
            ImmutableList.of(
                new PresetMSOCreateServiceInstanceAlacarte(
                    ImmutableMap.of(Keys.SERVICE_NAME, serviceInstanceName),
                    serviceRequestId, serviceInstanceId,
                    requestorID, serviceModelInfo),
                PRESET_SOME_LEGACY_REGION_TO_ATT_AIC,
                new PresetMSOOrchestrationRequestGet(COMPLETE, serviceRequestId),
                new PresetMSOCreateVnfALaCarteE2E(vnfRequestId, serviceInstanceId, vnfInstanceId, "ONAP", requestorID, serviceModelInfo),
                new PresetMSOOrchestrationRequestGet(COMPLETE, vnfRequestId),
                PRESET_MTN6_TO_ATT_AIC,
                new PresetMSOCreateVfModuleALaCarteE2E(vfm0RequestId, vfm0InstanceId, serviceInstanceId, vnfInstanceId, requestorID, serviceModelInfo, null, vfm0, null),
                new PresetMSOOrchestrationRequestGet(COMPLETE, vfm0RequestId),
                new PresetMSOCreateVfModuleALaCarteE2E(vg1RequestId, vg1InstanceId, serviceInstanceId, vnfInstanceId, requestorID, serviceModelInfo, vgName, vg1, null),
                new PresetMSOOrchestrationRequestGet(COMPLETE, vg1RequestId),
                new PresetMSOCreateVfModuleALaCarteE2E(vfm1RequestId, vfm1InstanceId, serviceInstanceId, vnfInstanceId, requestorID, serviceModelInfo, null, vfm1, vgRelatedInstance),
                new PresetMSOOrchestrationRequestGet(COMPLETE, vfm1RequestId),
                new PresetMSOCreateVfModuleALaCarteE2E(vfm12RequestId, vfm12InstanceId, serviceInstanceId, vnfInstanceId, requestorID, serviceModelInfo, null, vfm12, null),
                new PresetMSOOrchestrationRequestGet(COMPLETE, vfm12RequestId)
            ),
            APPEND
        );

        loadServicePopup(serviceModelInfo.modelVersionId);
        fillALaCarteServicePopup(serviceInstanceName);

        createVnf(vnfData, false, serviceInstanceName);
        createVfModule(serviceData, serviceInstanceName, true, false);
        serviceData.vfData =  new VfData("vocg_1804_vf0..Vocg1804Vf..ocgmgr..module-1", true, 0, NO_MAX_INSTANCES_IN_MODEL, emptyList(), "9c219e70-1177-494b-8977-1395c9f9168c", vgName);
        createVfModule(serviceData, serviceInstanceName, false, false);
        serviceData.vfData =  new VfData("vocg_1804_vf0..Vocg1804Vf..ocgapp_001..module-12", true, 0, NO_MAX_INSTANCES_IN_MODEL, emptyList(), "b601eef4-62fd-4201-a788-ae30e06a1aec", null);
        createVfModule(serviceData, serviceInstanceName, false, false);
        drawingBoardPage.deploy();
        drawingBoardPage.verifyServiceCompletedOnTime(serviceInstanceName, "service "+serviceInstanceName);
    }

    @Test
    @FeatureTogglingTest(FLAG_5G_IN_NEW_INSTANTIATION_UI)
    public void createNewServiceInstance_aLaCarte_withNetwork_validPopupDataAndUI() {
        String serviceInstanceName = "NcService"+randomAlphabetic(5);
        String networkInstanceName= "NcNetowrk"+randomAlphabetic(5);
        String defactoNetworkInstanceName = "ExtVL"+networkInstanceName;

        prepareServicePreset(aLaCarteNetworkProvider5G, true);
        String serviceRequestId = uuid();
        String networkRequestId = uuid();
        String requestorID = getUserCredentials().getUserId();
        registerExpectationFromPresets(
                ImmutableList.of(
                    new PresetMSOCreateServiceInstanceAlacarte(
                        ImmutableMap.of(Keys.SERVICE_NAME, serviceInstanceName),
                        serviceRequestId, DEFAULT_SERVICE_INSTANCE_ID,
                            requestorID, aLaCarteNetworkProvider5G),
                    new PresetMSOOrchestrationRequestGet(COMPLETE, serviceRequestId),
                    PRESET_SOME_LEGACY_REGION_TO_ATT_AIC,
                    new PresetMSOCreateNetworkALaCarte5G(networkRequestId, DEFAULT_SERVICE_INSTANCE_ID, defactoNetworkInstanceName, requestorID),
                    new PresetMSOOrchestrationRequestGet(COMPLETE, networkRequestId),
                    new PresetMSOOrchestrationRequestsGet5GServiceInstanceAndNetwork(
                            new ResponseDetails(serviceInstanceName, serviceRequestId, COMPLETE, "service"),
                            new ResponseDetails(defactoNetworkInstanceName, networkRequestId, COMPLETE, "network"),
                            DEFAULT_SERVICE_INSTANCE_ID)
                ),
            APPEND
        );
        loadServicePopup(aLaCarteNetworkProvider5G.modelVersionId);
        fillALaCarteServicePopup(serviceInstanceName);
        VnfData networkData = new VnfData("SR-IOV Provider-1", "840ffc47-e4cf-46de-8e23-525fd8c6fdc3", defactoNetworkInstanceName, false);
        createNetwork(networkData, false, false, serviceInstanceName);

        drawingBoardPage.deploy();
        drawingBoardPage.verifyServiceCompletedOnTime(serviceInstanceName, "service "+serviceInstanceName);
    }

    private void fillALaCarteServicePopup(String serviceInstanceName) {
        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        WebElement instanceNameInput = Get.byId("instanceName");
        instanceNameInput.sendKeys(serviceInstanceName);
        VidBasePage.selectSubscriberById("e433710f-9217-458d-a79d-1c7aff376d89");
        String serviceType = "TYLER SILVIA";
        Wait.waitByClassAndText(Constants.CreateNewInstance.SERVICE_TYPE_OPTION_CLASS, serviceType, 30);
        browseASDCPage.selectServiceTypeByName(serviceType);
        SelectOption.byTestIdAndVisibleText("WayneHolland", (Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID));
        SelectOption.byTestIdAndVisibleText("WATKINS", Constants.OwningEntity.PROJECT_SELECT_TEST_ID);
        Click.byTestId("form-set");
        VidBasePage.goOutFromIframe();
        browseASDCPage.goToIframe();
    }

    @Test
    public void createNewServiceInstance_macro_validPopupDataAndUI() {

        List<String> serviceDynamicFields = Arrays.asList("2017488 pasqualevpe0 asn:");
        ServiceData serviceData = new ServiceData(
                macroSriovWithDynamicFieldsEcompNamingTruePartialModelDetails.modelVersionId,
                serviceDynamicFields,
                IS_GENERATED_NAMING.TRUE, true, true, false,
                "2017-488_PASQUALE-vPE 0",
                "2017488_pasqualevpe0..2017488PasqualeVpe..PASQUALE_vRE_BV..module-1", 0,
            NO_MAX_INSTANCES_IN_MODEL, new ArrayList<>(),
            "25284168-24bb-4698-8cb4-3f509146eca5", false);

        prepareServicePreset(macroSriovWithDynamicFieldsEcompNamingTruePartialModelDetails, false);

        final String serviceInstanceName = createMacroService(serviceData, true);
        createVnf(serviceData, true, serviceInstanceName);
        clickRemoveVfModule(vfModule0UUID, vfModule0Name);
        createVfModule(serviceData, serviceInstanceName, false, false);

    }

    //@Step("deploy and verify module in pending table")
    private void deployAndVerifyModuleInPendingTableMacro(String serviceInstanceName, String requestId1, String requestId2) {
        drawingBoardPage.deploy();

        boolean simulatorUpdated = false;

        int[] ids = {0, 0, 1, 2};
        String[] statuses = {IN_PROGRESS, COMPLETED, IN_PROGRESS, PENDING};
        for (int i = 0; i < ids.length; i++) {
            String actualInstanceName = getActualInstanceName(serviceInstanceName, ids[i]);
            verifyModuleInPendingTable(actualInstanceName, requestId1, requestId2, ImmutableSet.of(statuses[i]), true, simulatorUpdated);
            simulatorUpdated = true;
        }
        vidBasePage.screenshotDeployDialog(serviceInstanceName);
    }

    private void verifyModuleInPendingTable(String serviceInstanceName, String requestId1, String requestId2, Set<String> expectedStatuses, boolean isMacro, boolean simulatorUpdated) {
        DrawingBoardPage.ServiceStatusChecker serviceStatusChecker = new DrawingBoardPage.ServiceStatusChecker(serviceInstanceName, expectedStatuses);
        boolean statusIsShown = Wait.waitFor(serviceStatusChecker, null, 20, 2);
        final String assertionMessage = String.format("service %s: none of rowClasses [%s] is in expectedStatuses: [%s]  ",
                serviceInstanceName,
                String.join(",", serviceStatusChecker.getColumnClassesSet()),
                String.join(",", expectedStatuses));

        assertTrue(assertionMessage, statusIsShown);

        if (isMacro) {
            InstantiationStatusPage.assertInstantiationStatusRow(
                    serviceInstanceName, expectedRowFields(serviceInstanceName));
        } else {
            InstantiationStatusPage.assertInstantiationStatusRow(
                    serviceInstanceName, expectedALaCarteRowFields(serviceInstanceName));
        }

        if (!simulatorUpdated) {
            if (requestId2 != null) {
                registerExpectationFromPreset(new PresetMSOOrchestrationRequestGet(IN_PROGRESS, requestId2), APPEND);
            }
            registerExpectationFromPreset(new PresetMSOOrchestrationRequestGet("COMPLETE", requestId1), APPEND);
        }
        vidBasePage.screenshotDeployDialog(serviceInstanceName);
    }

    private String getActualInstanceName(String serviceInstanceName, Integer i) {
        return i==0 ? serviceInstanceName : serviceInstanceName + "_00" + i;
    }

    //@Step("verify open view edit")
    private void verifyOpenViewEdit(String serviceInstanceName) {
        boolean[] openEnabled = {true, false, false};
        ImmutableList.of(0, 1, 2).forEach(i -> {
            String actualInstanceName = getActualInstanceName(serviceInstanceName, i);
            if (Features.FLAG_1902_NEW_VIEW_EDIT.isActive() || FLAG_1908_MACRO_NOT_TRANSPORT_NEW_VIEW_EDIT.isActive()) {
                InstantiationStatusPage.verifyOpenNewViewEdit(actualInstanceName, openEnabled[i], "EDIT");
            }
            else {
                vidBasePage.verifyOpenOldViewEdit(actualInstanceName, null, openEnabled[i], true, true);
            }
        });
    }

    //@Step("verify open audit info")
    private void verifyOpenAuditInfo(String serviceInstanceName) {
        boolean auditInfoEnabled = true;
        String[] statuses = {COMPLETED, IN_PROGRESS, PENDING};
        for (Integer i : ImmutableList.of(0, 1, 2)) {
            String actualInstanceName = getActualInstanceName(serviceInstanceName, i);
            InstantiationStatusPage.checkMenuItem(actualInstanceName, Constants.InstantiationStatus.CONTEXT_MENU_HEADER_AUDIT_INFO_ITEM, auditInfoEnabled, contextMenuOpen -> {
                Click.byTestId(contextMenuOpen);
                checkAuditInfoModal(actualInstanceName, i, statuses);
            });
            final WebElement row = InstantiationStatusPage.getInstantiationStatusRow(actualInstanceName);
            row.findElement(By.id(Constants.InstantiationStatus.TD_JOB_STATUS_ICON + "-" + (i))).click();
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

        List<String> serviceStatus = Arrays.asList(Arrays.copyOfRange(statuses, i, statuses.length));
        assertThat("statuses for " + actualInstanceName + " must be as expected", vidStatuses, containsInAnyOrder(serviceStatus.toArray()));
        String dateString = vidTableElement.findElements(By.id("vidStatusTime")).get(0).getText();
        assertTrue("vid Status Time column must contains valid date in format : MMM dd, yyyy HH:mm", isDateValid(dateString, "MMM dd, yyyy HH:mm"));

        WebElement MSOTableElement = Get.byId("service-instantiation-audit-info-mso");
        assertEquals(3, MSOTableElement.findElement(By.tagName("thead")).findElements(By.tagName("th")).size(), "MSO table must contain 3 columns");

        if (statuses[i].equals(PENDING)) {
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

    //@Step("verifyDeleteJob")
    private void verifyDeleteJob(String serviceInstanceName) {
        boolean[] deleteEnabled = {false, false, true};
        String[] statuses = {COMPLETED, IN_PROGRESS, PENDING};
        verifyDeleteOrHideOperation(serviceInstanceName, Constants.InstantiationStatus.CONTEXT_MENU_REMOVE, statuses, deleteEnabled, "deleted");
    }

    //@Step("verify hide job")
    private void verifyHideJob(String serviceInstanceName) {
        boolean[] hideEnabled = {true, false};
        String[] statuses = {COMPLETED, IN_PROGRESS};
        verifyDeleteOrHideOperation(serviceInstanceName, Constants.InstantiationStatus.CONTEXT_MENU_HIDE, statuses, hideEnabled, "hidden");
    }

    private void verifyDeleteOrHideOperation(String serviceInstanceName, String contextMenuItem, String[] statuses, boolean[] operationEnabled, String operationName) {
        for (int i = 0; i < statuses.length; i++) {
            String actualInstanceName = getActualInstanceName(serviceInstanceName, i);
            InstantiationStatusPage.checkMenuItem(actualInstanceName, contextMenuItem, operationEnabled[i], contextMenuDelete -> {
                Click.byTestId(contextMenuDelete);
                GeneralUIUtils.ultimateWait();
                assertNull(actualInstanceName + " should be " + operationName,
                        InstantiationStatusPage.getInstantiationStatusRow(actualInstanceName));
            });
        }
        vidBasePage.screenshotDeployDialog(serviceInstanceName);
    }

    private ImmutableMap<String, String> expectedRowFields(String actualInstanceName) {
        return ImmutableMap.<String, String>builder()
                .put("userId", getUserCredentials().getUserId())
                .put("serviceModelName", "action-data")
                .put("serviceInstanceName", actualInstanceName)
                .put("serviceModelVersion", "1.0")
                .put("subscriberName", "SILVIA ROBBINS")
                .put("serviceType", "TYLER SILVIA")
                .put("regionId", "hvf6")
                .put("tenantName", "AIN Web Tool-15-D-testalexandria")
                .put("aicZoneName", "NFTJSSSS-NFT1")
                .put("project", "WATKINS")
                .put("owningEntityName", "WayneHolland")
                .put("pause", "false")
                .build();
    }

    private ImmutableMap<String, String> expectedALaCarteRowFields(String actualInstanceName) {
        return ImmutableMap.<String, String>builder()
                .put("userId", getUserCredentials().getUserId())
                .put("serviceModelName", "Grouping Service for Test")
                .put("serviceInstanceName", actualInstanceName)
                .put("serviceModelVersion", "1.0")
                .put("subscriberName", "SILVIA ROBBINS")
                .put("serviceType", "TYLER SILVIA")
                .put("project", "WATKINS")
                .put("owningEntityName", "WayneHolland")
                .put("pause", "false")
                .build();
    }

    //@Step("create sriov service")
    private String createMacroService(ServiceData serviceData, boolean tryCancelsAndReentries) {
        return createMacroService(serviceData, tryCancelsAndReentries, randomAlphabetic(5), true, 3);
    }

    //@Step("create sriov service")
    private String createMacroService(ServiceData serviceData, boolean tryCancelsAndReentries, String randomAlphabetic, boolean fillLcpRegionAndTenantAndZone, int bulkSize) {
        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        User user = usersService.getUser(Constants.Users.SILVIA_ROBBINS_TYLER_SILVIA);

        // simulate typing with spaces, but expected is without spaces
        String serviceInstanceNameWithSpaces = randomAlphabetic + " instance name";
        String serviceInstanceName = cleanSeparators(serviceInstanceNameWithSpaces);

        List<String> cycles = tryCancelsAndReentries ? ImmutableList.of("WILL_CANCEL", "AFTER_CANCEL") : ImmutableList.of("SINGLE_SHOT");
        cycles.forEach(cycle -> {
            if ("AFTER_CANCEL".equals(cycle)) {
                loadServicePopupOnBrowseASDCPage(serviceData.modelUuid);
            } else {
                loadServicePopup(serviceData.modelUuid);
            }

            WebElement instanceName = Get.byId("instanceName");
            boolean isRequired = isElementByIdRequired("instanceName-label");
            if (serviceData.isGeneratedNaming == IS_GENERATED_NAMING.TRUE || serviceData.isGeneratedNaming == IS_GENERATED_NAMING.TRUE_BUT_GIVE_NAME_EITHER_WAY) {
                Assert.assertNotNull(instanceName, "instance name input should be visible when serviceEcompNaming == true.");
                Assert.assertFalse(isRequired,"instance name input should be optional when ecompNaming == true.");
            } else {
                Assert.assertTrue(isRequired,"instance name input should be required when serviceEcompNaming == false.");
            }

            if (serviceData.isGeneratedNaming == IS_GENERATED_NAMING.FALSE || serviceData.isGeneratedNaming == IS_GENERATED_NAMING.TRUE_BUT_GIVE_NAME_EITHER_WAY) {
                instanceName.sendKeys(serviceInstanceName);
            }
            String setButtonTestId = "form-set";

            //serviceType should be dependent on subscriber selection
            assertElementDisabled("serviceType-select");
            Wait.waitByClassAndText(Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS, "SILVIA ROBBINS", 30);
            GeneralUIUtils.ultimateWait();
            Click.byTestId(Constants.SUBSCRIBER_NAME_SELECT_TESTS_ID);
            if (Features.FLAG_RESTRICTED_SELECT.isActive())
                assertElementExistAccordingTagNameAndTestId("select", Constants.SUBSCRIBER_SELECT_ID);
            else{
                assertDropdownPermittedItemsByLabel(user.subscriberNames, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);

            }
            VidBasePage.selectSubscriberById("e433710f-9217-458d-a79d-1c7aff376d89");
            //lcpRegion should be dependent on serviceType selection
            assertElementDisabled("lcpRegion-select");

            String serviceType = "TYLER SILVIA";
            Wait.waitByClassAndText(Constants.CreateNewInstance.SERVICE_TYPE_OPTION_CLASS, serviceType, 30);
            browseASDCPage.selectServiceTypeByName(serviceType);

            String owningEntity = "WayneHolland";
            GeneralUIUtils.ultimateWait();
            Wait.waitByClassAndText(Constants.CreateNewInstance.OWNING_ENTITY_OPTION, owningEntity, 30);
            SelectOption.byTestIdAndVisibleText(owningEntity, (Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID));
            assertSetButtonDisabled(setButtonTestId);

            SelectOption.byTestIdAndVisibleText("ERICA", Constants.ViewEdit.PRODUCT_FAMILY_SELECT_TESTS_ID);
            browseASDCPage.selectProductFamily("e433710f-9217-458d-a79d-1c7aff376d89");

            if (fillLcpRegionAndTenantAndZone) {

                //we assume that if fillLcpRegionAndTenantAndZone is true tenant and lcpRegion are required for this service model
                //If you want to fill lcpRegionAndTenant where they are optional you can refactor this test...
                assertSetButtonDisabled(setButtonTestId);

                //tenant should be dependent on lcpRegion selection
                assertElementDisabled("tenant-select");

                String lcpRegion = "hvf6";
                Wait.waitByClassAndText("lcpRegionOption", lcpRegion, 30);
                viewEditPage.selectLcpRegion(lcpRegion, AIC);

                GeneralUIUtils.ultimateWait();
                browseASDCPage.selectTenant("bae71557c5bb4d5aac6743a4e5f1d054");

                browseASDCPage.selectAicZone("NFT1");
            }

            assertSetButtonEnabled(setButtonTestId);


            SelectOption.byTestIdAndVisibleText("WATKINS", Constants.OwningEntity.PROJECT_SELECT_TEST_ID);
            if (bulkSize!=1) {
                assertNotificationAreaVisibilityBehaviourAndSetBulkSize(bulkSize);
            }

            assertPauseOnPausePointsVisibility(serviceData.multiStageDesign);

            validateDynamicFields(serviceData.dynamicFields);

            vidBasePage.screenshotDeployDialog("createMacroService-" + serviceInstanceName);

            if ("WILL_CANCEL".equals(cycle)) {
                Click.byTestId(Constants.CANCEL_BUTTON_TEST_ID);
            } else {
                Click.byTestId(setButtonTestId);
            }

            VidBasePage.goOutFromIframe();

            browseASDCPage.goToIframe();

        });
        return serviceInstanceName;
    }

    private String createALaCarteService(ServiceData serviceData, String randomAlphabetic) {
        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        User user = usersService.getUser(Constants.Users.SILVIA_ROBBINS_TYLER_SILVIA);

        // simulate typing with spaces, but expected is without spaces
        String serviceInstanceNameWithSpaces = randomAlphabetic + " instance name";
        String serviceInstanceName = cleanSeparators(serviceInstanceNameWithSpaces);

        loadServicePopup(serviceData.modelUuid);

        WebElement instanceName = Get.byId("instanceName");
        if (serviceData.isGeneratedNaming == IS_GENERATED_NAMING.TRUE || serviceData.isGeneratedNaming == IS_GENERATED_NAMING.TRUE_BUT_GIVE_NAME_EITHER_WAY) {
            Assert.assertNull(instanceName, "instance name input should be invisible when serviceEcompNaming == true.");
        }
        if (serviceData.isGeneratedNaming == IS_GENERATED_NAMING.FALSE || serviceData.isGeneratedNaming == IS_GENERATED_NAMING.TRUE_BUT_GIVE_NAME_EITHER_WAY) {
            instanceName.sendKeys(serviceInstanceName);
        }

        //serviceType should be dependent on subscriber selection
        assertElementDisabled("serviceType-select");
        Wait.waitByClassAndText(Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS, "SILVIA ROBBINS", 30);
        GeneralUIUtils.ultimateWait();
        Click.byTestId(Constants.SUBSCRIBER_NAME_SELECT_TESTS_ID);
        if (Features.FLAG_RESTRICTED_SELECT.isActive())
            assertElementExistAccordingTagNameAndTestId("select", Constants.SUBSCRIBER_SELECT_ID);
        else{
            assertDropdownPermittedItemsByLabel(user.subscriberNames, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);

        }
        VidBasePage.selectSubscriberById("e433710f-9217-458d-a79d-1c7aff376d89");

        String serviceType = "TYLER SILVIA";
        Wait.waitByClassAndText(Constants.CreateNewInstance.SERVICE_TYPE_OPTION_CLASS, serviceType, 30);
        browseASDCPage.selectServiceTypeByName(serviceType);

        String setButtonTestId = "form-set";
        assertSetButtonDisabled(setButtonTestId);

        SelectOption.byTestIdAndVisibleText("WayneHolland", (Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID));

        SelectOption.byTestIdAndVisibleText("WATKINS", Constants.OwningEntity.PROJECT_SELECT_TEST_ID);

        validateDynamicFields(serviceData.dynamicFields);

        vidBasePage.screenshotDeployDialog("createALaCarteService-" + serviceInstanceName);

        Click.byTestId(setButtonTestId);

        VidBasePage.goOutFromIframe();

        browseASDCPage.goToIframe();

        return serviceInstanceName;
    }

    private void assertElementExistAccordingTagNameAndTestId(String tag, String testId) {
        WebElement webElement = Get.byId(testId);
        Assert.assertEquals(webElement.getTagName(), tag);
    }

    //@Step("create vnf")
    private void createVnf(ServiceData serviceData, boolean tryCancelsAndReentries, String serviceInstanceName) {
        createVnf(serviceData.vnfData, tryCancelsAndReentries, serviceInstanceName);
    }

    private void createNetwork(VnfData vnfData, boolean tryCancelsAndReentries, boolean addedByDefault, String serviceInstanceName) {
        createVnf(vnfData, tryCancelsAndReentries, addedByDefault, serviceInstanceName, true);
    }

    private void createVnf(VnfData vnfData, boolean tryCancelsAndReentries, String serviceInstanceName) {
        createVnf(vnfData, tryCancelsAndReentries, true, serviceInstanceName, false);
    }

    private void createVnf(VnfData vnfData, boolean tryCancelsAndReentries, boolean addedByDefault, String serviceInstanceName, boolean isNetwork) {
        BrowseASDCPage browseASDCPage = new BrowseASDCPage();

        String nodeToEdit = extractNodeToEdit(vnfData);
        if (addedByDefault) {
            hoverAndClickEditButton(nodeToEdit);
        } else {
            drawingBoardPage.clickAddButtonByNodeName(vnfData.vnfName);
        }

        GeneralUIUtils.ultimateWait();

        if (vnfData.isGeneratedNaming) {
            Assert.assertFalse(isElementByIdRequired("instanceName-label") ,"instance name input should be optional when EcompNaming == true, and required when false.");
        } else {
            Input.text(vnfData.vnfInstanceName, "instanceName");
        }


        //tenant should be dependent on lcpRegion selection
        assertElementDisabled("tenant-select");

        WebElement legacyRegion = Get.byTestId("lcpRegionText");
        Assert.assertNull(legacyRegion, "legacy region shouldn't be visible when lcp region isn't AAIAIC25,olson3 or olson5a.");

        browseASDCPage.selectLcpRegion("AAIAIC25");

        legacyRegion = Get.byTestId("lcpRegionText");
        Assert.assertNotNull(legacyRegion, "legacy region should be visible when lcp region is AAIAIC25,olson3 or olson5a.");

        browseASDCPage.selectTenant("092eb9e8e4b7412e8787dd091bc58e86");


        assertSetButtonDisabled(VNF_SET_BUTTON_TEST_ID);

        if(isNetwork){
            browseASDCPage.selectPlatform("platform");
        }else {
            SelectOption.selectOptionsFromMultiselectById("multi-selectPlatform", ImmutableList.of("platform"));
            SelectOption.byTestIdAndVisibleText("TYLER SILVIA", Constants.ViewEdit.PRODUCT_FAMILY_SELECT_TESTS_ID);
            browseASDCPage.selectProductFamily("e433710f-9217-458d-a79d-1c7aff376d89");
        }

        browseASDCPage.selectLineOfBusiness("ONAP");
        assertSetButtonEnabled(VNF_SET_BUTTON_TEST_ID);

        browseASDCPage.setLegacyRegion("some legacy region");


        Wait.waitByTestId("model-item-value-subscriberName", 10);
        Assert.assertEquals(Get.byTestId("model-item-value-subscriberName").getText(), "SILVIA ROBBINS", "Subscriber name should be shown in vf module");
        if (!vnfData.isGeneratedNaming) {
            Assert.assertEquals(Get.byTestId("model-item-value-serviceName").getText(), serviceInstanceName, "Subscriber name should be shown in vf module");
        }

        vidBasePage.screenshotDeployDialog("createVnf-" + serviceInstanceName);
        clickToCloseModal(VNF_SET_BUTTON_TEST_ID);
        if (isNetwork) {
            return;
        }
        if (tryCancelsAndReentries) {
            hoverAndClickEditButton(nodeToEdit);

            Wait.byText("TYLER SILVIA");
            GeneralUIUtils.ultimateWait();
            assertThat(Get.selectedOptionText(Constants.ViewEdit.LCP_REGION_SELECT_TESTS_ID), startsWith("AAIAIC25"));
            Assert.assertEquals(Get.selectedOptionText(Constants.ViewEdit.TENANT_SELECT_TESTS_ID), "USP-SIP-IC-24335-T-01");
            Assert.assertEquals(Get.selectedOptionText(Constants.ViewEdit.LINE_OF_BUSINESS_SELECT_TESTS_ID), "ONAP");

            Assert.assertTrue(Get.isOptionSelectedInMultiSelect(Constants.OwningEntity.PLATFORM_MULTI_SELECT_TEST_ID, "platform"));

            clickToCloseModal(Constants.CANCEL_BUTTON_TEST_ID);
        } else {
            toggleItemInTree(Constants.DrawingBoard.AVAILABLE_MODELS_TREE);
        }
        Click.byTestId("node-" + nodeToEdit);
    }

    private String extractNodeToEdit(VnfData vnfData) {
        return vnfData.vnfUuid + "-" + vnfData.vnfName;
    }


    private void toggleItemInTree(String tree) {
        Click.byXpath("//tree-root[@data-tests-id='" + tree + "']//span[@class='" + Constants.DrawingBoard.TOGGLE_CHILDREN + "']");
    }

    private void hoverAndClickEditButton(String nodeToEdit) {
        hoverAndClickButton(nodeToEdit, Constants.DrawingBoard.CONTEXT_MENU_EDIT);
    }

    private void hoverAndClickDeleteButton(String nodeToEdit) {
        hoverAndClickButton(nodeToEdit, Constants.InstantiationStatus.CONTEXT_MENU_REMOVE);
    }

    private void hoverAndClickDuplicateButton(String nodeToEdit) {
        hoverAndClickButton(nodeToEdit, Constants.InstantiationStatus.CONTEXT_MENU_DUPLICATE);
    }
    private void hoverAndClickButton(String nodeToEdit, String contextMenuItem) {
        String nodeOfEdit = Constants.DrawingBoard.NODE_PREFIX + nodeToEdit;
        String buttonOfEdit = nodeOfEdit + Constants.DrawingBoard.CONTEXT_MENU_BUTTON;
        GeneralUIUtils.hoverOnAreaByTestId(buttonOfEdit);
        Click.byTestId(buttonOfEdit);
        Click.byTestId(contextMenuItem);
    }

    private void uploadSupplementaryFile(String inputFileName, boolean isValid, BrowseASDCPage browseASDCPage, String setButtonTestId) {
        if (Features.FLAG_SUPPLEMENTARY_FILE.isActive()) {
            GeneralUIUtils.ultimateWait();
            Input.file("supplementaryFiles/" + inputFileName, "supplementaryFile");
            GeneralUIUtils.ultimateWait();
            WebElement fileName = Get.byTestId("file-name");
            Assert.assertEquals(fileName.getText(),inputFileName);
            browseASDCPage.assertButtonState(setButtonTestId, isValid);
        }
    }

    private void deleteSupplementaryFile() {
        if (Features.FLAG_SUPPLEMENTARY_FILE.isActive()) {
            Click.byTestId("remove-uploaded-file");
            GeneralUIUtils.ultimateWait();
            WebElement fileName = Get.byTestId("file-name");
            Assert.assertEquals(fileName.getText(),"Choose file");
        }
    }

    //@Step("create vf module")
    private void createVfModule(ServiceData serviceData, String serviceInstanceName, boolean addedByDefault, boolean addOpensPopup) {
        clickAddVfModule(serviceData, addedByDefault);
        if (!addOpensPopup) {
            clickEditVfModule(serviceData);
        }
        fillAndSetVfModulePopup(serviceData, serviceInstanceName);
    }

    private void fillAndSetVfModulePopup(ServiceData serviceData, String serviceInstanceName) {
        String setButtonTestId = "form-set";
        BrowseASDCPage browseASDCPage = new BrowseASDCPage();

        Assert.assertEquals(isElementByIdRequired("instanceName-label"), !serviceData.vnfData.isGeneratedNaming,"instance name input should be optional when EcompNaming == true, and required when false.");

        if (!serviceData.vnfData.isGeneratedNaming) {
            Input.text("VF instance name", "instanceName");
        }

        if (!serviceData.vfData.vgEnabled || (!serviceData.isALaCarte && serviceData.vnfData.isGeneratedNaming)) {
            Assert.assertNull(Get.byTestId("volumeGroupName"), "volumeGroupName input should be invisible "
                + "when vgEnabled is false or when vgEnabled is true and EcompGenName is true "
                + "(was: serviceData.vfData.vgEnabled=>" + serviceData.vfData.vgEnabled + ", serviceData.isGeneratedNaming=>" + IS_GENERATED_NAMING.FALSE + ")");
        }
        else {
            Assert.assertFalse(isElementByIdRequired("volumeGroupName-label"),
                "volume Group name input should be always optional");
            if (serviceData.vfData.vgName!=null) {
                browseASDCPage.setInputText("volumeGroupName", serviceData.vfData.vgName);
            }
        }


        Wait.waitByTestId("model-item-value-subscriberName", 10);
        Assert.assertEquals(Get.byTestId("model-item-value-subscriberName").getText(), "SILVIA ROBBINS", "Subscriber name should be shown in vf module");
        Assert.assertEquals(Get.byTestId("model-item-value-min").getText(), Integer.toString(serviceData.vfData.vfMin), "Min should be shown");
        if (serviceData.vfData.vfMax!=null) {
            if (!serviceData.vfData.vfMax.equals(NO_MAX_INSTANCES_IN_MODEL)) {
                Assert.assertEquals(Get.byTestId("model-item-value-max").getText(), serviceData.vfData.vfMax, "Max should be shown");
            }
            else {
                String defaultMaxText = Features.FLAG_2002_UNLIMITED_MAX.isActive() ? "Unlimited (default)" : "1";
                Assert.assertEquals(Get.byTestId("model-item-value-max").getText(), defaultMaxText, "Max should be shown with default value");
            }
        }

        if (serviceData.isGeneratedNaming!=IS_GENERATED_NAMING.TRUE) {
            Wait.byText(serviceInstanceName);
            Assert.assertEquals(Get.byTestId("model-item-value-serviceName").getText(), serviceInstanceName, "Service name should be shown in vf module");
        }

        if (serviceData.isALaCarte && !FLAG_2006_VFMODULE_TAKES_TENANT_AND_REGION_FROM_VNF.isActive()) {
            String lcpRegion = "hvf6";
            Wait.waitByClassAndText("lcpRegionOption", lcpRegion, 30);
            viewEditPage.selectLcpRegion(lcpRegion, AIC);
            browseASDCPage.selectTenant("bae71557c5bb4d5aac6743a4e5f1d054");
        }

        validateDynamicFields(serviceData.vfData.dynamicFields);

        uploadSupplementaryFile("invalid-file.json", false, browseASDCPage, setButtonTestId);
        deleteSupplementaryFile();
        uploadSupplementaryFile("sample.json", true, browseASDCPage, setButtonTestId);

        browseASDCPage.screenshotDeployDialog("createVfModule-" + serviceInstanceName);
        clickToCloseModal(setButtonTestId);
    }

    private void clickToCloseModal(String setOrCancelButtonTestId) {
        Click.byTestId(setOrCancelButtonTestId);
        GeneralUIUtils.ultimateWait();
    }

    private void clickEditVfModule(ServiceData serviceData) {
        hoverAndClickEditButton(serviceData.vfData.uuid + "-" + serviceData.vfData.vfName);
    }

    private void clickAddVfModule(ServiceData serviceData, boolean addedByDefault) {
        if (addedByDefault) {
            return;
        }
        System.out.println("VFModule should be added 'manually'");

        final WebElement vfModuleNode = Get.byTestId(Constants.DrawingBoard.NODE_PREFIX + serviceData.vfData.vfName);

        if (vfModuleNode == null || !vfModuleNode.isDisplayed()) {
            // expand tree
            drawingBoardPage.clickNode(serviceData.vnfData.vnfName);
        }
        drawingBoardPage.clickAddButtonByNodeName(serviceData.vfData.vfName);
    }

    private void clickRemoveVfModule(String vfModuleId, String vfModuleName) {
        System.out.println("will remove " + vfModule0Name);
        hoverAndClickDeleteButton(vfModuleId + "-" + vfModuleName);
    }

    private void assertPauseOnPausePointsVisibility(boolean visibility) {
        WebElement pauseElem = Get.byId("Pause");
        final String assertionMessage = "pause on pause points visibility should be " + visibility;
        if (visibility) {
            Assert.assertNotNull(pauseElem, assertionMessage);
        } else {
            Assert.assertNull(pauseElem, assertionMessage);
        }
    }

    public void validateDynamicFields(List<String> dynamicFields) {
        for (String field : dynamicFields) {
            WebElement fieldElement = GeneralUIUtils.findByText(field);
            assertNotNull("couldn't find dynamic field: " + field, fieldElement);
        }
    }

    private void assertNotificationAreaVisibilityBehaviourAndSetBulkSize(int size) {
        WebElement webElement = Get.byId("notification-area");
        Assert.assertNull(webElement, "notification area should be invisible if only 1 qty.");

        SelectOption.byIdAndVisibleText("quantity-select", String.valueOf(size));

        webElement = Get.byId("notification-area");
        Assert.assertNotNull(webElement, "notification area should be visible if more then 1 qty.");
    }

    static class ServiceData {

        ServiceData(String modelUuid, List<String> dynamicFields, IS_GENERATED_NAMING isServiceGeneratedNaming,
            boolean isVnfGeneratedNaming, boolean isVgEnabled, boolean multiStageDesign, String vnfName,
            String vfName, int vfMin, String vfMax, List<String> vfModuleDynamicFields, String vfVersionId, boolean isALaCarte) {
            this.modelUuid = modelUuid;
            this.dynamicFields = dynamicFields;
            this.isGeneratedNaming = isServiceGeneratedNaming;
            this.multiStageDesign = multiStageDesign;
            this.isALaCarte = isALaCarte;
            this.vnfData = new VnfData(vnfName, "69e09f68-8b63-4cc9-b9ff-860960b5db09", "VNF instance name", isVnfGeneratedNaming);
            this.vfData = new VfData(vfName, isVgEnabled, vfMin, vfMax, vfModuleDynamicFields, vfVersionId, "_abc");
        }

        public ServiceData(IS_GENERATED_NAMING isGeneratedNaming, VnfData vnfData, VfData vfData, boolean isALaCarte) {
            this.isGeneratedNaming = isGeneratedNaming;
            this.vnfData = vnfData;
            this.vfData = vfData;
            this.isALaCarte = isALaCarte;
        }

        String modelUuid;
        List<String> dynamicFields;
        IS_GENERATED_NAMING isGeneratedNaming;
        boolean multiStageDesign;
        VnfData vnfData;
        VfData vfData;
        boolean isALaCarte;

        enum IS_GENERATED_NAMING { TRUE, FALSE, TRUE_BUT_GIVE_NAME_EITHER_WAY}
    }

    private static class VnfData {
        VnfData(String vnfName, String vnfUuid, String vnfInstanceName, boolean isGeneratedNaming) {
            this.vnfName = vnfName;
            this.vnfUuid = vnfUuid;
            this.vnfInstanceName = vnfInstanceName;
            this.isGeneratedNaming = isGeneratedNaming;
        }

        final String vnfName;
        final String vnfUuid;
        final String vnfInstanceName;
        final boolean isGeneratedNaming;
    }


    private static class VfData {
        VfData(String vfName, boolean vgEnabled, int vfMin, String vfMax, List<String> dynamicFields, String uuid, String vgName) {
            this.vfName = vfName;
            this.vgEnabled = vgEnabled;
            this.vfMin = vfMin;
            this.vfMax = vfMax;
            this.dynamicFields = dynamicFields;
            this.uuid = uuid;
            this.vgName = vgName;
        }

        final int vfMin;
        final String vfMax;
        final String uuid;
        final String vfName;
        final boolean vgEnabled;
        final List<String> dynamicFields;
        final String vgName;
    }


}
