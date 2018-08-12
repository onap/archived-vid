package vid.automation.test.test;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.datatypes.UserCredentials;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.SelectOption;
import vid.automation.test.model.User;
import vid.automation.test.sections.BrowseASDCPage;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.services.SimulatorApi;

import static org.hamcrest.Matchers.containsString;
import static vid.automation.test.infra.Features.FLAG_ASYNC_INSTANTIATION;

public class SanityMacroDeployTest extends CreateInstanceDialogBaseTest {

    static final String NEW_INSTANCE_NAME = "New Instance Name";

    static final String SERVICE_NAME_PNF = "vidmacrofalsenaming";
    static final String SERVICE_UUID = "4d71990b-d8ad-4510-ac61-496288d9078e";
    static final String SUBSCRIBER = "FIREWALL/MISC";
    static final String PRODUCT = "Transport";
    static final String SERVICE_TYPE = "AIM Transport";
    static final String LCP_REGION = "rdm3";

    static final String TENANT = "AIN Web Tool-15-D-testgamma";
    static final String AIC_ZONE = "NFTJSSSS-NFT1";
    static final String PROJECT = "yyy1";
    static final String OWNING_ENTITY = "aaa1";

    static final String RESOURCE_NAME = "MULTI_PROVIDER_PORT_GROUP 0";
    static final String RESOURCE_DESCRIPTION = "Creates a neutron multi-provider VLAN network with dummy subnet";
    static final String SERVICE_INVARIANT_UUID = "d27e42cf-087e-4d31-88ac-6c4b7585f800";
    static final String SERVICE_VERSION = "1.0";
    static final String SERVICE_DESCRIPTION = "vidmacrofalsenaming";
    static final String SERVICE_CATEGORY = "Network L1-3";

    public SanityMacroDeployTest() {
    }


    @BeforeClass
    protected void registerToSimulator() {
        SimulatorApi.registerExpectation(SimulatorApi.RegistrationStrategy.APPEND,
                "sanity/browse_sdc_catalog_two_services.json",
                "sanity/aai_get_services.json",
                "sanity/aai_get_aic_zones.json",
                "search_for_service_instance/aai_get_full_subscribers.json",
                "search_for_service_instance/aai_get_subscribers_for_customer_FIREWALL-MISC.json",
                "sanity/aai_get_tenants.json",
                "sanity/get_mso_get_orch_request.json",
                "sanity/post_mso_create_service_instance.json",
                "sanity/get_sdc_catalog_services_4d71990b.json",
                "sanity/aai_named_query_for_view_edit.json"
        );
    }

    @Override
    protected UserCredentials getUserCredentials() {
        String userName = Constants.Users.FIREWALL_MISC_AIM_TRANSPORT;
        User user = usersService.getUser(userName);
        return new UserCredentials(user.credentials.userId, user.credentials.password, userName, "", "");
    }

    @FeatureTogglingTest(value = FLAG_ASYNC_INSTANTIATION, flagActive = false)
    @Test
    private void browseServiceModel_MacroService_CreatedSuccessfully() {
        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();

        GeneralUIUtils.ultimateWait();
        browseASDCPage.clickDeployServiceButtonByServiceUUID(SERVICE_UUID);

        WebElement modalTitle = GeneralUIUtils.getWebElementByTestID(Constants.CREATE_MODAL_TITLE_ID, 30);
        Assert.assertThat(modalTitle.getText().toLowerCase(), containsString("macro"));

        assertServiceDetails();

        fillCreateInstanceModal();

        browseASDCPage.clickConfirmButton();

        assertSuccessfulServiceInstanceCreation();

        browseASDCPage.clickCloseButton();
    }

    private void fillCreateInstanceModal() {
        WebElement instance_name = GeneralUIUtils.getWebElementByTestID(Constants.INSTANCE_NAME_SELECT_TESTS_ID, 30);
        instance_name.sendKeys(NEW_INSTANCE_NAME);

        SelectOption.byTestIdAndVisibleText(SUBSCRIBER, Constants.SUBSCRIBER_NAME_SELECT_TESTS_ID);

        GeneralUIUtils.ultimateWait();
        SelectOption.byTestIdAndVisibleText(PRODUCT, Constants.ViewEdit.PRODUCT_FAMILY_SELECT_TESTS_ID);
        GeneralUIUtils.ultimateWait();
        SelectOption.byTestIdAndVisibleText(SERVICE_TYPE, Constants.SERVICE_TYPE_SELECT_TESTS_ID);
        GeneralUIUtils.ultimateWait();
        SelectOption.byTestIdAndVisibleText(LCP_REGION, Constants.ViewEdit.LCP_REGION_SELECT_TESTS_ID);

        SelectOption.byTestIdAndVisibleText(TENANT, Constants.ViewEdit.TENANT_SELECT_TESTS_ID);
        SelectOption.byTestIdAndVisibleText(AIC_ZONE, Constants.ViewEdit.AIC_ZONE_TEST_ID);
        SelectOption.byTestIdAndVisibleText(PROJECT, Constants.OwningEntity.PROJECT_SELECT_TEST_ID);
        SelectOption.byTestIdAndVisibleText(OWNING_ENTITY, Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID);
    }

    private void assertServiceDetails() {
        WebElement currElem = GeneralUIUtils.getWebElementByTestID(Constants.SERVICE_NAME, 30);
        Assert.assertEquals(currElem.getText(), SERVICE_NAME_PNF);

        currElem = GeneralUIUtils.getWebElementByTestID(Constants.SERVICE_UUID, 30);
        Assert.assertEquals(currElem.getText(), SERVICE_UUID);

        currElem = GeneralUIUtils.getWebElementByTestID(Constants.RESOURCE_NAME, 30);
        Assert.assertEquals(currElem.getText(), RESOURCE_NAME);

        currElem = GeneralUIUtils.getWebElementByTestID(Constants.RESOURCE_DESCRIPTION, 30);
        Assert.assertEquals(currElem.getText(), RESOURCE_DESCRIPTION);

        currElem = GeneralUIUtils.getWebElementByTestID(Constants.SERVICE_INVARIANT_UUID, 30);
        Assert.assertEquals(currElem.getText(), SERVICE_INVARIANT_UUID);

        currElem = GeneralUIUtils.getWebElementByTestID(Constants.SERVICE_VERSION, 30);
        Assert.assertEquals(currElem.getText(), SERVICE_VERSION);

        currElem = GeneralUIUtils.getWebElementByTestID(Constants.SERVICE_DESCRIPTION, 30);
        Assert.assertEquals(currElem.getText(), SERVICE_DESCRIPTION);

        currElem = GeneralUIUtils.getWebElementByTestID(Constants.SERVICE_CATEGORY, 30);
        Assert.assertEquals(currElem.getText(), SERVICE_CATEGORY);
    }


}
