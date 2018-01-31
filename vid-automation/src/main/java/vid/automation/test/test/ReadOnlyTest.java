package vid.automation.test.test;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.datatypes.UserCredentials;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.By;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.model.User;
import vid.automation.test.sections.SearchExistingPage;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.services.UsersService;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Oren on 7/16/17.
 */
public class ReadOnlyTest extends VidBaseTestCase {
    UsersService usersService = new UsersService();

    public ReadOnlyTest() throws IOException {
    }

    @Override
    protected UserCredentials getUserCredentials() {
        User user =  usersService.getUser(Constants.Users.READONLY);
        return new UserCredentials(user.credentials.userId, user.credentials.password, "", "", "");
    }

    @Test
    public void testBrowsASDCReadOnly() {
        SideMenu.navigateToBrowseASDCPage();
        Assert.assertTrue(isDeployBtnDisabled());
    }

    private boolean isDeployBtnDisabled(){
        WebElement deployBtn = GeneralUIUtils.getWebElementBy(By.className(Constants.BrowseASDC.DEPOLY_SERVICE_CLASS));
        return !deployBtn.isEnabled();
    }

    @Test
    public void testSearchExistingReadOnly() {
        String UUID = "1dddde21-daad-4433-894e-bd715e98d587";
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        searchExistingPage.searchForInstanceByUuid(UUID);
        searchExistingPage.clickSubmitButton();
        assertViewEditButtonState(Constants.VIEW_BUTTON_TEXT, UUID);
        searchExistingPage.clickEditViewByInstanceId(UUID);
        searchExistingPage.checkForEditButtons();
    }

    @Test
    private void testCreateNewInstanceReadOnly() {
        SideMenu.navigateToCreateNewServicePage();
        assertDropdownPermittedItemsByValue(new ArrayList<String>(), Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
    }


}
