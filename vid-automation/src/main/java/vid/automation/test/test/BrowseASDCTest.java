package vid.automation.test.test;

import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Exists;
import vid.automation.test.infra.SelectOption;
import vid.automation.test.model.*;
import vid.automation.test.sections.BrowseASDCPage;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.sections.ViewEditPage;
import vid.automation.test.services.ServicesService;
import vid.automation.test.services.UsersService;

import java.io.IOException;

public class BrowseASDCTest extends CreateInstanceDialogBaseTest {
    private UsersService usersService = new UsersService();
    private ServicesService servicesService = new ServicesService();

    public BrowseASDCTest() throws IOException {
    }

    @Test
    public void testPNFOnCreatePopup() throws Exception {
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

    @Test
    private void testPNFMacroInstantation() throws Exception {
        User user = usersService.getUser(Constants.Users.MOBILITY_MOBILITY);
        relogin(user.credentials);

        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();
        browseASDCPage.clickDeployServiceButtonByServiceUUID("f39389e4-2a9c-4085-8ac3-04aea9c651be");
        assertThatServiceCreationDialogIsVisible();
        assertDropdownPermittedItemsByValue(user.subscribers, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
        browseASDCPage.selectSubscriberById("a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb");
        browseASDCPage.selectProductFamily("ebc3bc3d-62fd-4a3f-a037-f619df4ff034");
        GeneralUIUtils.ultimateWait();

        browseASDCPage.selectServiceTypeByName("Mobility");
        GeneralUIUtils.ultimateWait();
        browseASDCPage.selectLcpRegion("mtn16");

        browseASDCPage.selectTenant("a259ae7b7c3f493cb3d91f95a7c18149");
        assertAllIsPermitted(Constants.BrowseASDC.AIC_OPTION_CLASS);
        browseASDCPage.selectAicZone("NFT1");

        SelectOption.byTestIdAndVisibleText("Project-name", Constants.OwningEntity.PROJECT_SELECT_TEST_ID);

        browseASDCPage.clickConfirmButton();

        assertSuccessfulServiceInstanceCreation();

        browseASDCPage.clickCloseButton();

        ViewEditPage viewEditPage = new ViewEditPage();
        viewEditPage.clickActivateButton();
    }

    @Test
    private void testServiceInstantation() throws Exception {
        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);
        relogin(user.credentials);

        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();

        Service service = servicesService.getService("c079d859-4d81-4add-a9c3-94551f96e2b0");

        browseASDCPage.clickDeployServiceButtonByServiceUUID(service.uuid);
        validateServiceCreationDialog(service);

        browseASDCPage.setInstanceName(browseASDCPage.generateInstanceName());

        assertDropdownPermittedItemsByValue(user.subscribers, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
        browseASDCPage.selectSubscriberById("e433710f-9217-458d-a79d-1c7aff376d89");

        String serviceType = "VIRTUAL USP";
        GeneralUIUtils.findAndWaitByText(serviceType, 30);

        assertDropdownPermittedItemsByValue(user.serviceTypes, Constants.CreateNewInstance.SERVICE_TYPE_OPTION_CLASS);
        browseASDCPage.selectServiceTypeByName(serviceType);

        SelectOption.byTestIdAndVisibleText("Project-name", Constants.OwningEntity.PROJECT_SELECT_TEST_ID);

        browseASDCPage.selectSuppressRollback("false");

        browseASDCPage.clickConfirmButton();

        assertSuccessfulServiceInstanceCreation();

        browseASDCPage.clickCloseButton();
        GeneralUIUtils.ultimateWait();

        addVNF("VID-PCRF-05-15-17 0", "AAIAIC25", "092eb9e8e4b7412e8787dd091bc58e86",
                "false", "some legacy region", "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb", user.tenants);
        addVolumeGroup("VidPcrf051517..pcrf_nimbus_pcm..module-4", "AAIAIC25",
                "092eb9e8e4b7412e8787dd091bc58e86", "false", "some legacy region", user.tenants);
        addVFModule("VidPcrf051517..pcrf_nimbus_psm..module-1", "AAIAIC25",
                "092eb9e8e4b7412e8787dd091bc58e86", "false", "some legacy region", user.tenants);
    }

    @Test
    private void testProjectDropdownsExistsInCreationDialog() throws Exception {
        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);
        relogin(user.credentials);

        BrowseASDCPage browseASDCPage = new BrowseASDCPage();
        SideMenu.navigateToBrowseASDCPage();

        Service service = servicesService.getService("c079d859-4d81-4add-a9c3-94551f96e2b0");

        browseASDCPage.clickDeployServiceButtonByServiceUUID(service.uuid);
        assertThatServiceCreationDialogIsVisible();

        GeneralUIUtils.clickOnElementByTestId(Constants.OwningEntity.PROJECT_SELECT_TEST_ID);
    }
}
