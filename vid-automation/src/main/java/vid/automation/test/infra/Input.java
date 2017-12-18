package vid.automation.test.infra;

import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;

/**
 * Created by itzikliderman on 11/09/2017.
 */
public class Input {
    public static void text(String text, String inputTestsId) {
        WebElement inputElement = GeneralUIUtils.getWebElementByTestID(inputTestsId, 30);
        inputElement.sendKeys(text);
    }
}
