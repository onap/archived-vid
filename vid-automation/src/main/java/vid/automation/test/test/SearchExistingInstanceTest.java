package vid.automation.test.test;

import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;
import vid.automation.test.model.User;
import vid.automation.test.sections.SearchExistingPage;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.services.UsersService;

import java.io.IOException;

public class SearchExistingInstanceTest extends VidBaseTestCase {

    private UsersService usersService = new UsersService();

    public SearchExistingInstanceTest() throws IOException {
    }

    @Test
    private void testSearchExistingInstanceById() throws Exception {
        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);
        relogin(user.credentials);
        SideMenu.navigateToSearchExistingPage();
        goToExistingInstanceById("3f93c7cb-2fd0-4557-9514-e189b7b04f9d");

        //a flow that updates an existing instance is tested in testCreateNewServiceInstance.
    }

    @Test
    private void testSearchExistingInstanceByOwningEntitySingleValue() throws Exception {
        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);
        relogin(user.credentials);
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        Click.byId(Constants.EditExistingInstance.SELECT_OWNING_ENTITY_ID);
        Thread.sleep(1000);
        Click.byText("owning-entity-SDN-RXU4");
        searchExistingPage.clickSubmitButton();
        searchExistingPage.clickEditViewByInstanceId("SDN-RXU4-B-servInstance-E1802");
    }

    @Test
    private void testSearchExistingInstanceByOwningEntityMultiValue() throws Exception {
        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);
        relogin(user.credentials);
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        Click.byId(Constants.EditExistingInstance.SELECT_OWNING_ENTITY_ID);
        Thread.sleep(1000);
        Click.byText("owning-entity-SDN-RXU4");
        Click.byText("owning-entity-SDN-LT1");
        searchExistingPage.clickSubmitButton();
        searchExistingPage.clickEditViewByInstanceId("SDN-LT1-B-servInstance-E1802");
    }

    @Test
    private void testSearchExistingInstanceByProjectSingleValue() throws Exception {
        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);
        relogin(user.credentials);
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        Click.byId(Constants.EditExistingInstance.SELECT_PROJECT_ID);
        Thread.sleep(1000);
        Click.byText("x1");
        searchExistingPage.clickSubmitButton();
        searchExistingPage.clickEditViewByInstanceId("SDN-LT1-B-servInstance-E1802");
    }

    @Test
    private void testSearchExistingInstanceByProjectMultiValue() throws Exception {
        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);
        relogin(user.credentials);
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        Click.byId(Constants.EditExistingInstance.SELECT_PROJECT_ID);
        Thread.sleep(1000);
        Click.byText("x1");
        Click.byText("x3");
        searchExistingPage.clickSubmitButton();
        searchExistingPage.clickEditViewByInstanceId("SDN-LT1-B-servInstance-E1802");
    }
}
