package vid.automation.test.test;

import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.model.User;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.services.UsersService;

import java.io.IOException;

public class SearchExistingInstanceTest extends VidBaseTestCase {

    private UsersService usersService = new UsersService();

    public SearchExistingInstanceTest() throws IOException {
    }

    @Test
    private void testSearchExistingInstance() throws Exception {
        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);
        relogin(user.credentials);
        SideMenu.navigateToSearchExistingPage();
        goToExistingInstanceById("3f93c7cb-2fd0-4557-9514-e189b7b04f9d");

        //a flow that updates an existing instance is tested in testCreateNewServiceInstance.
    }
}
