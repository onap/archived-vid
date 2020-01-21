package vid.automation.test.test;

import static org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset.DEFAULT_CLOUD_OWNER;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId.MDT_1;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MDT1_TO_ATT_NC;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId.PRESET_SOME_LEGACY_REGION_TO_ATT_AIC;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId.SOME_LEGACY_REGION;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateVfModuleOldViewEdit;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateVnfALaCarteOldViewEdit;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateVolumeGroupOldViewEdit;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Get;
import vid.automation.test.model.ServiceModel;
import vid.automation.test.model.User;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.sections.VidBasePage;
import vid.automation.test.sections.ViewEditPage;
import vid.automation.test.services.ServicesService;
import vid.automation.test.services.SimulatorApi;


public class ALaCarteflowTest extends CreateInstanceDialogBaseTest {
    static final String SUBSCRIBER = "Emanuel";
    static final String SERVICE_NAME = "ggghhh";
    static final String SERVICE_ID = "537d3eb0-b7ab-4fe8-a438-6166ab6af49b";
    static final String VNF_ID = "0eb38f69-d96b-4d5e-b8c9-3736c292f0f7";
    static final String DEFAULT_TEST_API_VALUE = "GR_API";
    public static final String SERVICE_INSTANCE_ID = "SERVICE_INSTANCE_ID";
    public static final String A_LACARTE_FLOW_GET_ORCHESTRATION = "aLacarteFlow/get_orchestration_request_status.json";
    public static final String ORCHESTRATION_REQUEST_ID = "orchestrationRequestId";
    public static final String STATUS_MESSAGE = "status_message";
    public static final String REQUEST_TYPE = "REQUEST-TYPE";
    public static final String CREATE = "Create";
    public static final String AAIAIC_25 = "AAIAIC25";
    public static final String AIC = "AIC";
    public static final String TENANT = "092eb9e8e4b7412e8787dd091bc58e86";
    public static final String FALSE = "false";
    public static final String NODE_INSTANCE = "VSP1710PID298109_vWINIFRED 0";
    public static final String MODEL = "959a7ba0-89ee-4984-9af6-65d5bdda4b0e";
    private static final String CREATE_VNF_REQUEST_ID = "dbe54591-c8ed-46d3-abc7-d3a24873dfbd";


    @BeforeClass
    protected void registerToSimulator() {
        SimulatorApi.clearAll();
        SimulatorApi.registerExpectation(APPEND,
                "ecompportal_getSessionSlotCheckInterval.json",
                "search_for_service_instance/aai_get_services.json"
                , "create_configuration/aai_get_tenants.json"
                , "sanity/get_aai_sub_details.json"
                , "aLacarteFlow/get_aai_search_named_query.json"
                , "aLacarteFlow/get_sdc_catalog_services_ggghhh.json"
        );
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetSubscribersGet(), SimulatorApi.RegistrationStrategy.APPEND);
    }


    @Test(dataProvider = "msoTestApiOptions")
    private void testAddVfModule(String msoTestApiOption, String msoTestApiValue) {
        withMsoTestApiConfiguration(msoTestApiOption, msoTestApiValue, () -> {
            final String REQUEST_ID = "dbe54591-c8ed-46d3-abc7-d3a24873bddd";
            final String MODEL_UUID = "d205e01d-e5da-4e68-8c52-f95cb0607959";

            String vfModuleName = viewEditPage.generateInstanceName(Constants.ViewEdit.VF_MODULE_INSTANCE_NAME_PREFIX);

            SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                    PRESET_MDT1_TO_ATT_NC,
                    new PresetMSOCreateVfModuleOldViewEdit(
                            REQUEST_ID,
                            BaseMSOPreset.DEFAULT_INSTANCE_ID,
                            SERVICE_ID,
                            VNF_ID,
                            vfModuleName,
                            msoTestApiValue,
                            DEFAULT_CLOUD_OWNER)),
                    SimulatorApi.RegistrationStrategy.APPEND);


            SimulatorApi.registerExpectation(A_LACARTE_FLOW_GET_ORCHESTRATION,
                    ImmutableMap.of(ORCHESTRATION_REQUEST_ID, REQUEST_ID, STATUS_MESSAGE, Constants.ViewEdit.VF_MODULE_CREATED_SUCCESSFULLY_TEXT,
                            REQUEST_TYPE, CREATE), APPEND);
            GeneralUIUtils.ultimateWait();
            goToInstance();
            ServiceModel serviceInstance = new ServicesService().getServiceModel(MODEL_UUID);
            addVFModule("Vsp1710pid298109Vwinifred..mmsc_mod1_ltm..module-8", vfModuleName, AAIAIC_25, AIC,
                    TENANT, FALSE, MDT_1, getCurrentUser().tenants, serviceInstance);

        });
    }

    @Test
    private void testTenant() throws Exception {
        ViewEditPage viewEditPage = new ViewEditPage();
        User user = usersService.getUser("Emanuel_with_tenant");
        relogin(user.credentials);
        goToInstance();
        viewEditPage.selectNodeInstanceToAdd(NODE_INSTANCE);
        viewEditPage.setInstanceName("New and fun instance");
        viewEditPage.selectProductFamily("a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb");
        viewEditPage.selectLcpRegion("hvf6", AIC);
        List<WebElement> tenantOptions = Get.byClass(Constants.ViewEdit.TENANT_OPTION_CLASS);
        List<WebElement> enabledTenantOption = tenantOptions.stream().filter(webElement -> webElement.isEnabled()).collect(Collectors.toList());
        Assert.assertTrue(enabledTenantOption.size() == 1);
        Assert.assertTrue(enabledTenantOption.get(0).getText().equals("test-hvf6-09"));
    }

    @Test(dataProvider = "msoTestApiOptions")
    private void testAddVnf(String msoTestApiOption, String msoTestApiValue) {
        withMsoTestApiConfiguration(msoTestApiOption, msoTestApiValue, () -> {
            final String MODEL_UUID = MODEL;
            String instanceName = new VidBasePage().generateInstanceName(Constants.ViewEdit.VNF_INSTANCE_NAME_PREFIX);
            SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                    PRESET_SOME_LEGACY_REGION_TO_ATT_AIC,
                    new PresetMSOCreateVnfALaCarteOldViewEdit(
                            CREATE_VNF_REQUEST_ID,
                            SERVICE_ID,
                            instanceName,
                            true,
                            msoTestApiValue)),
                    SimulatorApi.RegistrationStrategy.APPEND);
            SimulatorApi.registerExpectation(A_LACARTE_FLOW_GET_ORCHESTRATION,
                    ImmutableMap.of(ORCHESTRATION_REQUEST_ID, "dbe54591-c8ed-46d3-abc7-d3a24873dfbd", STATUS_MESSAGE, Constants.ViewEdit.VNF_CREATED_SUCCESSFULLY_TEXT,
                            REQUEST_TYPE, CREATE), APPEND);
            GeneralUIUtils.ultimateWait();
            goToInstance();
            ServiceModel serviceInstance = new ServicesService().getServiceModel(MODEL_UUID);
            addVNF(NODE_INSTANCE, AAIAIC_25, AIC, TENANT,
                    FALSE, SOME_LEGACY_REGION, "ebc3bc3d-62fd-4a3f-a037-f619df4ff034", "platform", getCurrentUser().tenants, "ONAP", serviceInstance, instanceName);
        });
    }

    @Test(dataProvider = "msoTestApiOptions")
    private void requiredLineOfBussiness_confirmVnfWithNoLob(String msoTestApiOption, String msoTestApiValue) throws Exception {
        withMsoTestApiConfiguration(msoTestApiOption, msoTestApiValue, () -> {
            goToInstance();
            String instanceName = new VidBasePage().generateInstanceName(Constants.ViewEdit.VNF_INSTANCE_NAME_PREFIX);
            SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                    PRESET_SOME_LEGACY_REGION_TO_ATT_AIC,
                    new PresetMSOCreateVnfALaCarteOldViewEdit(
                            CREATE_VNF_REQUEST_ID,
                            SERVICE_ID,
                            instanceName,
                            false,
                            msoTestApiValue)),
                    SimulatorApi.RegistrationStrategy.APPEND);
            SimulatorApi.registerExpectation(A_LACARTE_FLOW_GET_ORCHESTRATION, ImmutableMap.of(
                    ORCHESTRATION_REQUEST_ID, "dbe54591-c8ed-46d3-abc7-d3a24873dfbd",
                    STATUS_MESSAGE, Constants.ViewEdit.VNF_CREATED_SUCCESSFULLY_TEXT,
                    REQUEST_TYPE, CREATE
            ), APPEND);
            openAndFillVnfPopup(NODE_INSTANCE, AAIAIC_25, AIC, TENANT,
                    FALSE, SOME_LEGACY_REGION, "ebc3bc3d-62fd-4a3f-a037-f619df4ff034", "platform", getCurrentUser().tenants, null, null, instanceName);
            assertConfirmShowMissingDataErrorOnCurrentPopup(Constants.ViewEdit.LINE_OF_BUSINESS_TEXT);
        });
    }

    @Test
    private void emptyLobAfterReopenCreateVnfDialog() throws Exception {
        final String lobToSelect = "ONAP";
        goToInstance();
        ViewEditPage viewEditPage = new ViewEditPage();
        viewEditPage.selectNodeInstanceToAdd(NODE_INSTANCE);
        GeneralUIUtils.ultimateWait();
        viewEditPage.selectLineOfBusiness(lobToSelect);
        viewEditPage.clickCancelButtonByTestID();
        viewEditPage.selectNodeInstanceToAdd(NODE_INSTANCE);
        GeneralUIUtils.ultimateWait();
        viewEditPage.selectLineOfBusiness(lobToSelect);
        viewEditPage.clickCancelButtonByTestID();
    }

    @Test(dataProvider = "msoTestApiOptions")
    private void testAddVolumeGroup(String msoTestApiOption, String msoTestApiValue) throws Exception {
        withMsoTestApiConfiguration(msoTestApiOption, msoTestApiValue, () -> {
            final String REQUEST_ID = "dbe54591-c8ed-46d3-abc7-d3a24873bdaa";
            final String MODEL_UUID = "13f022c4-651e-4326-b8e1-61e9a8c7a7ad";
            String vgName = viewEditPage.generateInstanceName(Constants.ViewEdit.VOLUME_GROUP_INSTANCE_NAME_PREFIX);
            SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                    PRESET_SOME_LEGACY_REGION_TO_ATT_AIC,
                    new PresetMSOCreateVolumeGroupOldViewEdit(
                            REQUEST_ID,
                            BaseMSOPreset.DEFAULT_INSTANCE_ID,
                            SERVICE_ID,
                            VNF_ID,
                            vgName,
                            msoTestApiValue)),
                    SimulatorApi.RegistrationStrategy.APPEND);
            SimulatorApi.registerExpectation(A_LACARTE_FLOW_GET_ORCHESTRATION,
                    ImmutableMap.of(ORCHESTRATION_REQUEST_ID, REQUEST_ID, STATUS_MESSAGE, Constants.ViewEdit.VOLUME_GROUP_CREATED_SUCCESSFULLY_TEXT,
                            REQUEST_TYPE, CREATE), SimulatorApi.RegistrationStrategy.APPEND);
            GeneralUIUtils.ultimateWait();
            goToInstance();
            ServiceModel serviceInstance = new ServicesService().getServiceModel(MODEL_UUID);
            addVolumeGroup("Vsp1710pid298109Vwinifred..mmsc_mod6_eca_oam..module-3", vgName, AAIAIC_25, AIC, TENANT,
                    FALSE, SOME_LEGACY_REGION, getCurrentUser().tenants, serviceInstance);
        });
    }

    private User getCurrentUser() {
        return usersService.getUser(Constants.Users.EMANUEL_vWINIFRED);
    }

    private void goToInstance() {
        SideMenu.navigateToSearchExistingPage();
        goToExistingInstanceBySubscriber(SUBSCRIBER, SERVICE_NAME, SERVICE_ID);

    }

    @Override
    protected UserCredentials getUserCredentials() {
        User user = getCurrentUser();
        return new UserCredentials(user.credentials.userId, user.credentials.password, Constants.Users.EMANUEL_vWINIFRED, "", "");
    }

    @DataProvider
    public static Object[][] msoTestApiOptions() {
        return new Object[][]{
                {"GR_API (old)", DEFAULT_TEST_API_VALUE}
                , {"GR_API (new)", "GR_API"}
        };
    }

    private void withMsoTestApiConfiguration(String msoTestApiOption, String msoTestApiValue, Runnable test) {
        if (msoTestApiValue.equals(DEFAULT_TEST_API_VALUE)) {
            test.run();
        } else {
            try {
                selectMsoTestApiOption(msoTestApiOption);
                test.run();
            } finally {
                // back to default
                selectMsoTestApiOption("GR_API (old)");
            }
        }
    }


}
