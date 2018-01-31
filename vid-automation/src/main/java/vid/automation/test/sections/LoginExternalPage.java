package vid.automation.test.sections;

import org.openecomp.sdc.ci.tests.datatypes.UserCredentials;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import static org.junit.Assert.*;


public class LoginExternalPage {


    static private final String SUCCESSFUL_LOGIN_STRING_SEARCH = "Welcome to VID";

    static void sendUserAndPasswordKeys(UserCredentials userCredentials) {
        WebElement loginIdInputElem = GeneralUIUtils.getWebElementBy(By.name("loginId"));
        loginIdInputElem.sendKeys(userCredentials.getUserId());
        WebElement passwordInputElem = GeneralUIUtils.getWebElementBy(By.name("password"));
        passwordInputElem.sendKeys(userCredentials.getPassword());
    }

    static public void performLoginExternal(UserCredentials userCredentials) {
        sendUserAndPasswordKeys(userCredentials);
        WebElement loginButton = GeneralUIUtils.getWebElementBy(By.id("loginBtn"), 30);
        loginButton.click();
        boolean isLoginSuccess = GeneralUIUtils.findAndWaitByText(SUCCESSFUL_LOGIN_STRING_SEARCH, 30);
        assertTrue(isLoginSuccess);
    }
}
