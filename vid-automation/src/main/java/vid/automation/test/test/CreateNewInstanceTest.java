package vid.automation.test.test;

import java.io.IOException;
import java.util.List;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.SelectOption;
import vid.automation.test.model.Service;
import vid.automation.test.model.User;
import vid.automation.test.sections.CreateNewInstancePage;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.services.BulkRegistration;
import vid.automation.test.services.ServicesService;
import vid.automation.test.services.SimulatorApi;

public class CreateNewInstanceTest extends CreateInstanceDialogBaseTest {
    private ServicesService servicesService = new ServicesService();

    public CreateNewInstanceTest() throws IOException {
    }

    @Test(groups = { "underDevelopment" })
    private void testCreateNewServiceInstance() throws Exception {

        SimulatorApi.clearAll();
        BulkRegistration.createNewServiceInstance("SILVIA ROBBINS");

        User user = usersService.getUser(Constants.Users.SILVIA_ROBBINS_TYLER_SILVIA);
        relogin(user.credentials);

        SideMenu.navigateToCreateNewServicePage();

        CreateNewInstancePage createNewInstancePage = new CreateNewInstancePage();

        String subscriberName = "SILVIA ROBBINS";
        assertDropdownPermittedItemsByLabel(user.subscriberNames, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
        createNewInstancePage.selectSubscriberById("e433710f-9217-458d-a79d-1c7aff376d89");
        createNewInstancePage.clickSubmitButton();
        assertSuccessfulSelection(Constants.CreateNewInstance.SELECTED_SUBSCRIBER_NAME_TEST_ID,  subscriberName);

        String serviceType = "TYLER SILVIA";
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
        SelectOption.byTestIdAndVisibleText("WayneHolland", Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID);
        SelectOption.byTestIdAndVisibleText("x1", Constants.OwningEntity.PROJECT_SELECT_TEST_ID);
        createNewInstancePage.selectSuppressRollback("false");
        createNewInstancePage.clickConfirmButton();

        assertSuccessfulServiceInstanceCreation();

        createNewInstancePage.clickCloseButton();
    }

    @Test
    public void testSearchServicesWithSubscriberCAR_2020_ER() throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.createNewServiceInstance("CAR_2020_ER");

        User user = usersService.getUser(Constants.Users.SILVIA_ROBBINS_TYLER_SILVIA);
        relogin(user.credentials);

        SideMenu.navigateToCreateNewServicePage();

        CreateNewInstancePage createNewInstancePage = new CreateNewInstancePage();

        String subscriberName = "CAR_2020_ER";
        assertDropdownPermittedItemsByLabel(user.subscriberNames, Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
        createNewInstancePage.selectSubscriberById("CAR_2020_ER");
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
