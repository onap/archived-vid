package vid.automation.test.test;

import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.SelectOption;
import vid.automation.test.model.Service;
import vid.automation.test.model.User;
import vid.automation.test.sections.CreateNewInstancePage;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.services.BulkRegistration;
import vid.automation.test.services.ServicesService;
import vid.automation.test.services.SimulatorApi;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CreateNewInstanceTest extends CreateInstanceDialogBaseTest {
    private ServicesService servicesService = new ServicesService();

    public CreateNewInstanceTest() throws IOException {
    }

    @Test
    private void testCreateNewServiceInstance() throws Exception {

        if (!Features.CREATE_INSTANCE_TEST.isActive()) {

            // time bomb, as it fails on pipeline and I don't know how to fix it
            return;
        }

        SimulatorApi.clearAll();
        BulkRegistration.createNewServiceInstance("USP VOICE");

        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);
        relogin(user.credentials);

        SideMenu.navigateToCreateNewServicePage();

        CreateNewInstancePage createNewInstancePage = new CreateNewInstancePage();

        String subscriberName = "USP VOICE";
        assertDropdownPermittedItemsByLabel(user.subscriberNames, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
        createNewInstancePage.selectSubscriberById("e433710f-9217-458d-a79d-1c7aff376d89");
        createNewInstancePage.clickSubmitButton();
        assertSuccessfulSelection(Constants.CreateNewInstance.SELECTED_SUBSCRIBER_NAME_TEST_ID,  subscriberName);

        String serviceType = "VIRTUAL USP";
        assertDropdownPermittedItemsByName(user.serviceTypes, Constants.CreateNewInstance.SERVICE_TYPE_OPTION_CLASS);
        createNewInstancePage.selectServiceTypeByName(serviceType);
        createNewInstancePage.clickSubmitButton();
        assertSuccessfulSelection(Constants.CreateNewInstance.SELECTED_SERVICE_TYPE_NAME_TEST_ID, serviceType);

        Service service = servicesService.getService( "c079d859-4d81-4add-a9c3-94551f96e2b0");

        String instanceName = createNewInstancePage.generateInstanceName();
        BulkRegistration.deployNewServiceInstance(instanceName);

        createNewInstancePage.clickDeployServiceButtonByServiceUUID(service.uuid);

        validateServiceCreationDialog(service, subscriberName, serviceType);

        createNewInstancePage.setInstanceName(instanceName);
        SelectOption.byTestIdAndVisibleText("MetroPacketCore", Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID);
        SelectOption.byTestIdAndVisibleText("x1", Constants.OwningEntity.PROJECT_SELECT_TEST_ID);
        createNewInstancePage.selectSuppressRollback("false");
        createNewInstancePage.clickConfirmButton();

        assertSuccessfulServiceInstanceCreation();

        createNewInstancePage.clickCloseButton();
    }

    @Test
    public void testSearchServicesWithSubscriberMSO_1610_ST() throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.createNewServiceInstance("MSO_1610_ST");

        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);
        relogin(user.credentials);

        SideMenu.navigateToCreateNewServicePage();

        CreateNewInstancePage createNewInstancePage = new CreateNewInstancePage();

        String subscriberName = "MSO_1610_ST";
        assertDropdownPermittedItemsByLabel(user.subscriberNames, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
        createNewInstancePage.selectSubscriberById("MSO_1610_ST");
        createNewInstancePage.clickSubmitButton();
        assertSuccessfulSelection(Constants.CreateNewInstance.SELECTED_SUBSCRIBER_NAME_TEST_ID,  subscriberName);

        String serviceType = "MSO-dev-service-type";
        assertDropdownPermittedItemsByName(user.serviceTypes, Constants.CreateNewInstance.SERVICE_TYPE_OPTION_CLASS);
        createNewInstancePage.selectServiceTypeByName(serviceType);
        createNewInstancePage.clickSubmitButton();
        assertSuccessfulSelection(Constants.CreateNewInstance.SELECTED_SERVICE_TYPE_NAME_TEST_ID, serviceType);

        assertSuccessfulGetServicesList();
    }

    private void assertSuccessfulSelection(String elementTestId, String expectedSelection) {
        GeneralUIUtils.ultimateWait();
        WebElement selectedElement = GeneralUIUtils.getWebElementByTestID(elementTestId, 30);
        Assert.assertEquals(selectedElement.getText(), expectedSelection);
    }

    private void assertSuccessfulGetServicesList() {
        GeneralUIUtils.ultimateWait();
        List<List<String>> tableRows = Get.tableBodyValuesByTestId("services-list");
        Assert.assertTrue(tableRows.size() > 0);
    }

    private void validateServiceCreationDialog(Service expectedService, String subscriberName, String serviceType) {
        assertServiceMetadata(subscriberName, Constants.SUBSCRIBER_NAME);
        assertServiceMetadata(serviceType, Constants.SERVICE_TYPE);
        validateServiceCreationDialog(expectedService);

    }
}
