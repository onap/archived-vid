package vid.automation.test.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static vid.automation.test.infra.ModelInfo.macroForBrowseSdc;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIServiceDesignAndCreationPut;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateMacroPre1806Post;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.SelectOption;
import vid.automation.test.model.User;
import vid.automation.test.sections.BrowseASDCPage;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.services.SimulatorApi;

public class SanityMacroDeployTest extends CreateInstanceDialogBaseTest {

    static final String NEW_INSTANCE_NAME = "New Instance Name";

    static final String SERVICE_NAME_PNF = "vidmacrofalsenaming";
    static final String SUBSCRIBER = "CRAIG/ROBERTS";
    static final String PRODUCT = "Transport";
    static final String SERVICE_TYPE = "AIM Transport";
    static final String LCP_REGION = "olson3";

    static final String TENANT = "AIN Web Tool-15-D-testalexandria";
    static final String AIC_ZONE = "NFTJSSSS-NFT1";
    static final String PROJECT = "yyy1";
    static final String OWNING_ENTITY = "aaa1";

    static final String RESOURCE_NAME = "MULTI_PROVIDER_PORT_GROUP 0";
    static final String RESOURCE_DESCRIPTION = "Creates a neutron multi-provider VLAN network with dummy subnet";
    static final String SERVICE_VERSION = "1.0";
    static final String SERVICE_DESCRIPTION = "vidmacrofalsenaming";
    static final String SERVICE_CATEGORY = "Network L1-3";

    @BeforeClass
    protected void registerToSimulator() {
        resetGetServicesCache();
        invalidateSdcModelsCache();
        SimulatorApi.registerExpectation(SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET,
                "sanity/aai_get_services.json",
                "sanity/aai_get_aic_zones.json",
                "search_for_service_instance/aai_get_subscribers_for_customer_CRAIG-ROBERTS.json",
                "sanity/aai_get_tenants.json",
                "sanity/get_mso_get_orch_request.json",
                "sanity/get_sdc_catalog_services_4d71990b.json",
                "sanity/aai_named_query_for_view_edit.json"
        );
        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                new PresetAAIServiceDesignAndCreationPut(),
                new PresetMSOCreateMacroPre1806Post(),
                new PresetAAIGetSubscribersGet()
                ),
                APPEND);
    }

    @Override
    protected UserCredentials getUserCredentials() {
        String userName = Constants.Users.CRAIG_ROBERTS_AIM_TRANSPORT;
        User user = usersService.getUser(userName);
        return new UserCredentials(user.credentials.userId, user.credentials.password, userName, "", "");
    }

    @Test
    private void browseServiceModel_MacroService_CreatedSuccessfully() {

        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();

        GeneralUIUtils.ultimateWait();
        browseASDCPage.clickDeployServiceButtonByServiceUUID(macroForBrowseSdc.modelVersionId);

        WebElement modalTitle = GeneralUIUtils.getWebElementByTestID(Constants.CREATE_MODAL_TITLE_ID, 30);
        assertThat(modalTitle.getText().toLowerCase(), containsString("macro"));
        GeneralUIUtils.ultimateWait();

        assertServiceDetails();

        fillCreateInstanceModal();

        browseASDCPage.clickConfirmButton();

        assertSuccessfulServiceInstanceCreation();

        browseASDCPage.clickCloseButton();
    }

    private void fillCreateInstanceModal() {


        waitForInstanceField();
        GeneralUIUtils.ultimateWait();
        GeneralUIUtils.sleep(10000); // stuff is getting weird

        WebElement instance_name = GeneralUIUtils.getWebElementByTestID(Constants.INSTANCE_NAME_SELECT_TESTS_ID, 30);
        instance_name.sendKeys(NEW_INSTANCE_NAME);

        waitForInstanceField();
        GeneralUIUtils.ultimateWait();
        SelectOption.byTestIdAndVisibleText(SUBSCRIBER, Constants.SUBSCRIBER_NAME_SELECT_TESTS_ID);

        waitForInstanceField();
        GeneralUIUtils.ultimateWait();
        SelectOption.byTestIdAndVisibleText(PRODUCT, Constants.ViewEdit.PRODUCT_FAMILY_SELECT_TESTS_ID);

        waitForInstanceField();
        GeneralUIUtils.ultimateWait();
        SelectOption.byTestIdAndVisibleText(SERVICE_TYPE, Constants.SERVICE_TYPE_SELECT_TESTS_ID);

        GeneralUIUtils.ultimateWait();
        viewEditPage.selectLcpRegion(LCP_REGION, "AIC");

        SelectOption.byTestIdAndVisibleText(TENANT, Constants.ViewEdit.TENANT_SELECT_TESTS_ID);
        SelectOption.byTestIdAndVisibleText(AIC_ZONE, Constants.ViewEdit.AIC_ZONE_TEST_ID);
        SelectOption.byTestIdAndVisibleText(PROJECT, Constants.OwningEntity.PROJECT_SELECT_TEST_ID);
        SelectOption.byTestIdAndVisibleText(OWNING_ENTITY, Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID);
    }

    private void waitForInstanceField() {
        GeneralUIUtils.sleep(200); // stuff is getting weird
        SelectOption.waitForOptionInSelect(SUBSCRIBER, Constants.SUBSCRIBER_NAME_SELECT_TESTS_ID);
        SelectOption.waitForOptionInSelect(TENANT, Constants.ViewEdit.TENANT_SELECT_TESTS_ID);
        SelectOption.waitForOptionInSelect(AIC_ZONE, Constants.ViewEdit.AIC_ZONE_TEST_ID);
        SelectOption.waitForOptionInSelect(PROJECT, Constants.OwningEntity.PROJECT_SELECT_TEST_ID);
        SelectOption.waitForOptionInSelect(OWNING_ENTITY, Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID);
        GeneralUIUtils.sleep(200); // stuff is getting weird
    }

    private void assertServiceDetails() {
        WebElement currElem = GeneralUIUtils.getWebElementByTestID(Constants.SERVICE_NAME, 30);
        Assert.assertEquals(currElem.getText(), SERVICE_NAME_PNF);

        currElem = GeneralUIUtils.getWebElementByTestID(Constants.SERVICE_UUID, 30);
        Assert.assertEquals(currElem.getText(), macroForBrowseSdc.modelVersionId);

        currElem = GeneralUIUtils.getWebElementByTestID(Constants.RESOURCE_NAME, 30);
        Assert.assertEquals(currElem.getText(), RESOURCE_NAME);

        currElem = GeneralUIUtils.getWebElementByTestID(Constants.RESOURCE_DESCRIPTION, 30);
        Assert.assertEquals(currElem.getText(), RESOURCE_DESCRIPTION);

        currElem = GeneralUIUtils.getWebElementByTestID(Constants.SERVICE_INVARIANT_UUID, 30);
        Assert.assertEquals(currElem.getText(), "a8dcd72d-d44d-44f2-aa85-53aa9ca99cba");

        currElem = GeneralUIUtils.getWebElementByTestID(Constants.SERVICE_VERSION, 30);
        Assert.assertEquals(currElem.getText(), SERVICE_VERSION);

        currElem = GeneralUIUtils.getWebElementByTestID(Constants.SERVICE_DESCRIPTION, 30);
        Assert.assertEquals(currElem.getText(), SERVICE_DESCRIPTION);

        currElem = GeneralUIUtils.getWebElementByTestID(Constants.SERVICE_CATEGORY, 30);
        Assert.assertEquals(currElem.getText(), SERVICE_CATEGORY);
    }


}
