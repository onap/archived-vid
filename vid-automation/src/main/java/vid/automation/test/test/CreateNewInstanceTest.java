package vid.automation.test.test;

import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.model.Service;
import vid.automation.test.model.User;
import vid.automation.test.sections.CreateNewInstancePage;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.services.ServicesService;
import vid.automation.test.services.UsersService;

import java.io.IOException;

public class CreateNewInstanceTest extends CreateInstanceDialogBaseTest {
    private UsersService usersService = new UsersService();
    private ServicesService servicesService = new ServicesService();

    public CreateNewInstanceTest() throws IOException {
    }

    @Test
    private void testCreateNewServiceInstance() throws Exception {
        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);
        relogin(user.credentials);

        SideMenu.navigateToCreateNewServicePage();

        CreateNewInstancePage createNewInstancePage = new CreateNewInstancePage();

        String subscriberName = "USP VOICE";
        assertDropdownPermittedItemsByValue(user.subscribers, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
        createNewInstancePage.selectSubscriberById("e433710f-9217-458d-a79d-1c7aff376d89");
        createNewInstancePage.clickSubmitButton();
        assertSuccessfulSelection(Constants.CreateNewInstance.SELECTED_SUBSCRIBER_NAME_TEST_ID,  subscriberName);

        String serviceType = "VIRTUAL USP";
        assertDropdownPermittedItemsByName(user.serviceTypes, Constants.CreateNewInstance.SERVICE_TYPE_OPTION_CLASS);
        createNewInstancePage.selectServiceTypeByName(serviceType);
        createNewInstancePage.clickSubmitButton();
        assertSuccessfulSelection(Constants.CreateNewInstance.SELECTED_SERVICE_TYPE_NAME_TEST_ID, serviceType);

        Service service = servicesService.getService( "c079d859-4d81-4add-a9c3-94551f96e2b0");

        createNewInstancePage.clickDeployServiceButtonByServiceUUID(service.uuid);

        validateServiceCreationDialog(service, subscriberName, serviceType);

        String instanceName = createNewInstancePage.generateInstanceName();
        createNewInstancePage.setInstanceName(instanceName);
        createNewInstancePage.selectSuppressRollback("false");
        createNewInstancePage.clickConfirmButton();

        assertSuccessfulServiceInstanceCreation();

        createNewInstancePage.clickCloseButton();

        GeneralUIUtils.ultimateWait();

        goToExistingInstanceByName(instanceName);

        addVNF("VID-PCRF-05-15-17 0", "AAIAIC25", "092eb9e8e4b7412e8787dd091bc58e86",
                "false", "some legacy region", "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb", user.tenants);
        addVolumeGroup("VidPcrf051517..pcrf_nimbus_pcm..module-4", "AAIAIC25",
                "092eb9e8e4b7412e8787dd091bc58e86", "false", "some legacy region", user.tenants);
        addVFModule("VidPcrf051517..pcrf_nimbus_psm..module-1", "AAIAIC25",
                "092eb9e8e4b7412e8787dd091bc58e86", "false", "some legacy region", user.tenants);
    }

    private void assertSuccessfulSelection(String elementTestId, String expectedSelection) {
        GeneralUIUtils.ultimateWait();
        WebElement selectedElement = GeneralUIUtils.getWebElementByTestID(elementTestId, 30);
        Assert.assertEquals(selectedElement.getText(), expectedSelection);
    }

    private void validateServiceCreationDialog(Service expectedService, String subscriberName, String serviceType) {
        assertServiceMetadata(subscriberName, Constants.SUBSCRIBER_NAME);
        assertServiceMetadata(serviceType, Constants.SERVICE_TYPE);
        validateServiceCreationDialog(expectedService);

    }
}
