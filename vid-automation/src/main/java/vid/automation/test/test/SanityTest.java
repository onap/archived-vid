package vid.automation.test.test;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.datatypes.UserCredentials;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.SelectOption;
import vid.automation.test.model.User;
import vid.automation.test.sections.SearchExistingPage;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.services.SimulatorApi;

import java.io.IOException;

public class SanityTest extends VidBaseTestCase {

    static final String SUBSCRIBER = "Mobility";
    static final String SERVICE_NAME = "abc_mdns_2017_1011_IST_Service_CSI";
    static final String SERVICE_ID = "ec884145-dbe8-4228-8165-f0c4d7123f8b";

    public SanityTest() throws IOException {
    }


    @BeforeClass
    protected void registerToSimulator() {
        SimulatorApi.registerExpectation(SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET,
                "sanity/get_aai_get_subscribers.json"
                , "sanity/aai_get_services.json"
                , "sanity/get_aai_sub_details.json"
                , "sanity/get_aai_search_named_query.json"
                , "sanity/get_aai_search_instance_by_id.json"
                , "sanity/get_sdc_catalog_services_a1531622.json"
                , "sanity/get_aai_search_instance_by_name.json"
        );
    }

    @Override
    protected UserCredentials getUserCredentials() {
        String userName = Constants.Users.MOBILITY_VMMSC;
        User user = usersService.getUser(userName);
        return new UserCredentials(user.credentials.userId, user.credentials.password, userName, "", "");
    }

    @Test
    private void testSearchExistingInstanceByName() throws Exception {
        SideMenu.navigateToSearchExistingPage();
        goToExistingInstanceByName(SERVICE_NAME);
    }

    @Test
    private void testSearchExistingInstanceById() throws Exception {
        SideMenu.navigateToSearchExistingPage();
        goToExistingInstanceById(SERVICE_ID);
    }



    @Test
    private void testSearchExistingInstanceBySubscriber() throws Exception {
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        SelectOption.byIdAndVisibleText(Constants.EditExistingInstance.SELECT_SUBSCRIBER, SUBSCRIBER);
        GeneralUIUtils.ultimateWait();

        searchExistingPage.clickSubmitButton();
        GeneralUIUtils.ultimateWait();

        confirmResultBySubscriber();


        String filteredId = confirmFilterById();

        confirmViewEditPage(filteredId);

    }

    private void confirmViewEditPage(String filteredId) {
        assertViewEditButtonState( Constants.VIEW_EDIT_BUTTON_TEXT, filteredId);
        Click.byTestId(Constants.VIEW_EDIT_TEST_ID_PREFIX + filteredId);
        GeneralUIUtils.ultimateWait();

        WebElement serviceInstanceIdTH = GeneralUIUtils.getWebElementByTestID(Constants.SERVICE_INSTANCEID_TH_ID, 30);
        String instanceId = serviceInstanceIdTH.getText();
        Assert.assertTrue(instanceId.contains(SERVICE_ID));
    }

    private String confirmFilterById() {
        WebElement filter = GeneralUIUtils.getWebElementByTestID(Constants.FILTER_SUBSCRIBER_DETAILS_ID, 30);
        filter.sendKeys(SERVICE_ID);

        WebElement firstElement = GeneralUIUtils.getWebElementByTestID(Constants.INSTANCE_ID_FOR_NAME_TEST_ID_PREFIX + SERVICE_NAME, 30);
        String filteredId = firstElement.getText();
        Assert.assertTrue(filteredId.equals(SERVICE_ID));
        return filteredId;
    }

    private void confirmResultBySubscriber() {
        for (int i = 0; i < 10; i++) {
            WebElement instanceIdRow = GeneralUIUtils.getWebElementByTestID(Constants.SUBSCRIBER_NAME_TEST_ID_PREFIX + i, 30);
            Assert.assertTrue(instanceIdRow.getText().equals(SUBSCRIBER));
        }
    }


}
