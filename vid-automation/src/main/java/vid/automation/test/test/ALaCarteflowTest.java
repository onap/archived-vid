package vid.automation.test.test;

import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.openecomp.sdc.ci.tests.datatypes.UserCredentials;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.SelectOption;
import vid.automation.test.model.ServiceModel;
import vid.automation.test.model.User;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.sections.VidBasePage;
import vid.automation.test.sections.ViewEditPage;
import vid.automation.test.services.ServicesService;
import vid.automation.test.services.SimulatorApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;


public class ALaCarteflowTest extends CreateInstanceDialogBaseTest {
    static final String SUBSCRIBER = "Mobility";
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
    public static final String TENANT = "092eb9e8e4b7412e8787dd091bc58e86";
    public static final String FALSE = "false";
    public static final String NODE_INSTANCE = "VSP1710PID298109_vMMSC 0";
    public static final String MODEL = "959a7ba0-89ee-4984-9af6-65d5bdda4b0e";
    public static final String LEGACY_REGION = "some legacy region";


    @BeforeClass
    protected void registerToSimulator() {
        SimulatorApi.clearAll();
        SimulatorApi.registerExpectation(APPEND,
                "ecompportal_getSessionSlotCheckInterval.json",
                "search_for_service_instance/aai_get_services.json"
                , "create_configuration/aai_get_tenants.json"
                , "sanity/get_aai_get_subscribers.json"
                , "sanity/get_aai_sub_details.json"
                , "aLacarteFlow/get_aai_search_named_query.json"
                , "aLacarteFlow/get_sdc_catalog_services_ggghhh.json"
        );

    }


    @Test(dataProvider = "msoTestApiOptions")
    private void testAddVfModule(String msoTestApiOption, String msoTestApiValue) {
        withMsoTestApiConfiguration(msoTestApiOption, msoTestApiValue, () -> {
            final String REQUEST_ID = "dbe54591-c8ed-46d3-abc7-d3a24873bddd";
            final String MODEL_UUID = "d205e01d-e5da-4e68-8c52-f95cb0607959";
            SimulatorApi.registerExpectation("aLacarteFlow/mso_create_vf_module.json", ImmutableMap.of(SERVICE_INSTANCE_ID, SERVICE_ID, "VNF_INSTANCE_ID", VNF_ID, "REQUEST_ID", REQUEST_ID, DEFAULT_TEST_API_VALUE, msoTestApiValue), APPEND);
            SimulatorApi.registerExpectation(A_LACARTE_FLOW_GET_ORCHESTRATION,
                    ImmutableMap.of(ORCHESTRATION_REQUEST_ID, REQUEST_ID, STATUS_MESSAGE, Constants.ViewEdit.VF_MODULE_CREATED_SUCCESSFULLY_TEXT,
                            REQUEST_TYPE, CREATE), APPEND);
            GeneralUIUtils.ultimateWait();
            goToInstance();
            ServiceModel serviceInstance = new ServicesService().getServiceModel(MODEL_UUID);
            addVFModule("Vsp1710pid298109Vmmsc..mmsc_mod1_ltm..module-8", AAIAIC_25,
                    TENANT, FALSE, "mdt1", getCurrentUser().tenants, serviceInstance);

        });
    }

    @Test
    private void testTenant() throws Exception {
        ViewEditPage viewEditPage = new ViewEditPage();
        User user = usersService.getUser("Mobility_with_tenant");
        relogin(user.credentials);
        goToInstance();
        viewEditPage.selectNodeInstanceToAdd(NODE_INSTANCE);
        viewEditPage.setInstanceName("New and fun instance");
        viewEditPage.selectProductFamily("a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb");
        viewEditPage.selectLCPRegion("mtn6");
        List<WebElement> tenantOptions = Get.byClass(Constants.ViewEdit.TENANT_OPTION_CLASS);
        List<WebElement> enabledTenantOption = tenantOptions.stream().filter(webElement -> webElement.isEnabled()).collect(Collectors.toList());
        Assert.assertTrue(enabledTenantOption.size() == 1);
        Assert.assertTrue(enabledTenantOption.get(0).getText().equals("test-mtn6-09"));

        //        SimulatorApi.registerExpectation("aLacarteFlow/get_sdc_catalog_services_ggghhh.json",APPEND);
    }

    @Test(dataProvider = "msoTestApiOptions")
    private void testAddVnf(String msoTestApiOption, String msoTestApiValue) {
        withMsoTestApiConfiguration(msoTestApiOption, msoTestApiValue, () -> {
            final String MODEL_UUID = MODEL;
            String instanceName = new VidBasePage().generateInstanceName(Constants.ViewEdit.VNF_INSTANCE_NAME_PREFIX);
            SimulatorApi.registerExpectation("aLacarteFlow/mso_create_vnf.json", ImmutableMap.of(
                    SERVICE_INSTANCE_ID, SERVICE_ID,
                    "</?LOB>", "",
                    "INSTANCE_NAME", instanceName,
                    DEFAULT_TEST_API_VALUE, msoTestApiValue
            ), APPEND);
            SimulatorApi.registerExpectation(A_LACARTE_FLOW_GET_ORCHESTRATION,
                    ImmutableMap.of(ORCHESTRATION_REQUEST_ID, "dbe54591-c8ed-46d3-abc7-d3a24873dfbd", STATUS_MESSAGE, Constants.ViewEdit.VNF_CREATED_SUCCESSFULLY_TEXT,
                            REQUEST_TYPE, CREATE), APPEND);
            GeneralUIUtils.ultimateWait();
            goToInstance();
            ServiceModel serviceInstance = new ServicesService().getServiceModel(MODEL_UUID);
            addVNF(NODE_INSTANCE, AAIAIC_25, TENANT,
                    FALSE, LEGACY_REGION, "ebc3bc3d-62fd-4a3f-a037-f619df4ff034", "platform", getCurrentUser().tenants, new ArrayList<>(Arrays.asList("ECOMP")), serviceInstance, instanceName);
        });
    }

    @Test(dataProvider = "msoTestApiOptions")
    private void requiredLineOfBussiness_confirmVnfWithNoLob(String msoTestApiOption, String msoTestApiValue) throws Exception {
        withMsoTestApiConfiguration(msoTestApiOption, msoTestApiValue, () -> {
            goToInstance();
            String instanceName = new VidBasePage().generateInstanceName(Constants.ViewEdit.VNF_INSTANCE_NAME_PREFIX);
            SimulatorApi.registerExpectation("aLacarteFlow/mso_create_vnf.json", ImmutableMap.of(
                    SERVICE_INSTANCE_ID, SERVICE_ID,
                    "<LOB>.*</LOB>", "",
                    "INSTANCE_NAME", instanceName,
                    DEFAULT_TEST_API_VALUE, msoTestApiValue
            ), APPEND);
            SimulatorApi.registerExpectation(A_LACARTE_FLOW_GET_ORCHESTRATION, ImmutableMap.of(
                    ORCHESTRATION_REQUEST_ID, "dbe54591-c8ed-46d3-abc7-d3a24873dfbd",
                    STATUS_MESSAGE, Constants.ViewEdit.VNF_CREATED_SUCCESSFULLY_TEXT,
                    REQUEST_TYPE, CREATE
            ), APPEND);
            openAndFillVnfPopup(NODE_INSTANCE, AAIAIC_25, TENANT,
                    FALSE, LEGACY_REGION, "ebc3bc3d-62fd-4a3f-a037-f619df4ff034", "platform", getCurrentUser().tenants, null, null, instanceName);
            assertConfirmShowMissingDataErrorOnCurrentPopup(Constants.ViewEdit.LINE_OF_BUSINESS_TEXT);
            successConfirmVnfAndClosePopup();
        });
    }

    @Test
    private void emptyLobAfterReopenCreateVnfDialog() throws Exception {
        final String lobToSelect = "ECOMP";
        goToInstance();
        ViewEditPage viewEditPage = new ViewEditPage();
        viewEditPage.selectNodeInstanceToAdd(NODE_INSTANCE);
        GeneralUIUtils.ultimateWait();
        SelectOption.selectOptionsFromMultiselectById(Constants.OwningEntity.LOB_SELECT_TEST_ID, new ArrayList<>(Arrays.asList(lobToSelect)));
        viewEditPage.clickCancelButtonByTestID();
        viewEditPage.selectNodeInstanceToAdd(NODE_INSTANCE);
        GeneralUIUtils.ultimateWait();
        AssertUnselectedOptionInMultiselectById(Constants.OwningEntity.LOB_SELECT_TEST_ID, lobToSelect);
        viewEditPage.clickCancelButtonByTestID();
    }

    @Test(dataProvider = "msoTestApiOptions")
    private void testAddVolumeGroup(String msoTestApiOption, String msoTestApiValue) throws Exception {
        withMsoTestApiConfiguration(msoTestApiOption, msoTestApiValue, () -> {
            final String REQUEST_ID = "dbe54591-c8ed-46d3-abc7-d3a24873bdaa";
            final String MODEL_UUID = "13f022c4-651e-4326-b8e1-61e9a8c7a7ad";
            SimulatorApi.registerExpectation("aLacarteFlow/mso_create_volume_group.json", ImmutableMap.of(SERVICE_INSTANCE_ID, SERVICE_ID, "VNF_INSTANCE_ID", VNF_ID, "REQUEST_ID", REQUEST_ID, DEFAULT_TEST_API_VALUE, msoTestApiValue), SimulatorApi.RegistrationStrategy.APPEND);
            SimulatorApi.registerExpectation(A_LACARTE_FLOW_GET_ORCHESTRATION,
                    ImmutableMap.of(ORCHESTRATION_REQUEST_ID, REQUEST_ID, STATUS_MESSAGE, Constants.ViewEdit.VOLUME_GROUP_CREATED_SUCCESSFULLY_TEXT,
                            REQUEST_TYPE, CREATE), SimulatorApi.RegistrationStrategy.APPEND);
            GeneralUIUtils.ultimateWait();
            goToInstance();
            ServiceModel serviceInstance = new ServicesService().getServiceModel(MODEL_UUID);
            addVolumeGroup("Vsp1710pid298109Vmmsc..mmsc_mod6_eca_oam..module-3", AAIAIC_25, TENANT,
                    FALSE, LEGACY_REGION, getCurrentUser().tenants, serviceInstance);
        });
    }

    private User getCurrentUser() {
        return usersService.getUser(Constants.Users.MOBILITY_VMMSC);
    }

    private void goToInstance() {
        SideMenu.navigateToSearchExistingPage();
        goToExistingInstanceBySubscriber(SUBSCRIBER, SERVICE_NAME, SERVICE_ID);

    }

    @Override
    protected UserCredentials getUserCredentials() {
        User user = getCurrentUser();
        return new UserCredentials(user.credentials.userId, user.credentials.password, Constants.Users.MOBILITY_VMMSC, "", "");
    }

    @DataProvider
    public static Object[][] msoTestApiOptions() {
        return new Object[][]{
                {"GR_API (new)", DEFAULT_TEST_API_VALUE}
                , {"VNF_API (old)", "VNF_API"}
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
                selectMsoTestApiOption("GR_API (new)");
            }
        }
    }


}
