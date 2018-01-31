package vid.automation.test.test;

import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Exists;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.Wait;
import vid.automation.test.model.User;
import vid.automation.test.sections.ViewEditPage;
import vid.automation.test.services.BulkRegistration;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.services.UsersService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ViewEditServiceInstanceTest extends VidBaseTestCase {

    private ViewEditPage viewEditPage = new ViewEditPage();
    private UsersService usersService = new UsersService();
    private String serviceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
    private  String serviceInstanceId2 ="f36f5734-e9df-4fbf-9f35-61be13f028a1";
    private final String DEACTIVATE_ACTION = "deactivate";
    private final String ACTIVATE_ACTION = "activate";
    private List<String> pnfs = Arrays.asList("SANITY6785cce9", "tesai371ve2");

    public ViewEditServiceInstanceTest() throws IOException { }

    @Test
    public void testGetAssociatedPnfsForServiceInstance() {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance();
        BulkRegistration.getAssociatedPnfs();

        goToExistingInstanceById(serviceInstanceId);
        for (String pnf: pnfs) {
            viewEditPage.getPnf(pnf);
        }
    }

    @Test
    public void testPnfsNotExistForServiceInstance() {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance();

        goToExistingInstanceById(serviceInstanceId);
        assertNoPnfExists();
    }
    @Test
    public void testActivateServiceInstance()throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance("Created");
        BulkRegistration.activateServiceInstance(ACTIVATE_ACTION);
        goToExistingInstanceById(serviceInstanceId);
        viewEditPage.assertButtonState(Constants.ViewEdit.ACTIVATE_BUTTON_TEST_ID,true);
        viewEditPage.assertButtonState(Constants.ViewEdit.DEACTIVATE_BUTTON_TEST_ID,false);
        viewEditPage.clickActivateButton();
        viewEditPage.assertMsoRequestModal("COMPLETE - Success");
        viewEditPage.clickCloseButton();
    }

    @Test
    public void testActivateServiceInstanceError()throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance("Created");
        BulkRegistration.activateServiceInstanceError(ACTIVATE_ACTION);
        goToExistingInstanceById(serviceInstanceId);
        viewEditPage.assertButtonState(Constants.ViewEdit.ACTIVATE_BUTTON_TEST_ID,true);
        viewEditPage.assertButtonState(Constants.ViewEdit.DEACTIVATE_BUTTON_TEST_ID,false);
        viewEditPage.clickActivateButton();
        viewEditPage.assertMsoRequestModal("Error");
        viewEditPage.clickCloseButton();
    }
    @Test
    public void testDeactivateServiceInstance(){
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance("Active");
        BulkRegistration.activateServiceInstance(DEACTIVATE_ACTION);
        goToExistingInstanceById(serviceInstanceId);
        viewEditPage.assertButtonState(Constants.ViewEdit.DEACTIVATE_BUTTON_TEST_ID,true);
        viewEditPage.assertButtonState(Constants.ViewEdit.ACTIVATE_BUTTON_TEST_ID,false);
        viewEditPage.clickDeactivateButton();
        viewEditPage.assertMsoRequestModal("COMPLETE - Success");
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance("PendingDelete");
        BulkRegistration.activateServiceInstance(ACTIVATE_ACTION);
        viewEditPage.clickCloseButton();
        viewEditPage.assertButtonState(Constants.ViewEdit.ACTIVATE_BUTTON_TEST_ID,true);
        viewEditPage.assertButtonState(Constants.ViewEdit.DEACTIVATE_BUTTON_TEST_ID,false);
    }

    @Test
    public void testDeactivateServiceInstanceError()throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance("Active");
        BulkRegistration.activateServiceInstanceError(DEACTIVATE_ACTION);
        goToExistingInstanceById(serviceInstanceId);
        viewEditPage.assertButtonState(Constants.ViewEdit.ACTIVATE_BUTTON_TEST_ID,false);
        viewEditPage.assertButtonState(Constants.ViewEdit.DEACTIVATE_BUTTON_TEST_ID,true);
        viewEditPage.clickDeactivateButton();
        viewEditPage.assertMsoRequestModal("Error");
        viewEditPage.clickCloseButton();
    }
     @Test
    public void testSuccessDissociatePnfFromServiceInstance() throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance();
        BulkRegistration.getAssociatedPnfs();
        BulkRegistration.dissociatePnf();
        goToExistingInstanceById(serviceInstanceId);
        dissociatePnf(pnfs.get(0)); //SANITY6785cce9
        viewEditPage.assertMsoRequestModal("COMPLETE - Success");
         viewEditPage.clickCloseButton();
    }

    @Test
    public void testFailDissociatePnfFromServiceInstance() throws Exception {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance();
        BulkRegistration.getAssociatedPnfs();
        SimulatorApi.registerExpectation(Constants.RegisterToSimulator.pProbe.REMOVE_PNF_RELATIONSHIP_ERROR);

        goToExistingInstanceById(serviceInstanceId);
        dissociatePnf(pnfs.get(0)); //SANITY6785cce9
        viewEditPage.assertMsoRequestModal("Error");
        viewEditPage.clickCloseButton();
    }

    private void assertNoPnfExists() {
        WebElement pnfElement = viewEditPage.getPnf("");
        Assert.assertNull("Pnf found under service instance", pnfElement);
    }

    private void dissociatePnf(String pnfName) throws InterruptedException {
        viewEditPage.clickDissociatePnfButton(pnfName);
        assertDissociateConfirmModal(pnfName);
    }

    private void assertDissociateConfirmModal(String pnfName) {
        Wait.modalToBeDisplayed();
        Assert.assertTrue(Exists.modal());
        Assert.assertTrue(Exists.byCssSelectorAndText(".modal-body span", String.format(Constants.ViewEdit.DISSOCIATE_CONFIRM_MODAL_TEXT, pnfName)));
        WebElement confirmBtn = Get.byId(Constants.ViewEdit.DISSOCIATE_CONFIRM_MODAL_BTN_ID);
        Assert.assertNotNull(confirmBtn);
        confirmBtn.click();
        Wait.modalToDisappear();
    }
    @Before
    public void before() throws Exception {
        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);
        relogin(user.credentials);
    }

}
