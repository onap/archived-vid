package vid.automation.test.infra;

import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import vid.automation.test.utils.ReadFile;

/**
 * Created by itzikliderman on 11/09/2017.
 */
public class Input {
    public static void text(String text, String inputTestsId) {
        WebElement inputElement = GeneralUIUtils.getWebElementByTestID(inputTestsId, 30);
        inputElement.sendKeys(text);
    }

    public static void replaceText(String text, String inputTestsId) {
        WebElement inputElement = GeneralUIUtils.getWebElementByTestID(inputTestsId, 30);
        inputElement.clear();
        inputElement.sendKeys(text);
    }


    public static String getValueByTestId(String testId) {
        WebElement input = GeneralUIUtils.getInputElement(testId);
        return input.getAttribute("value");
    }

    /*
        Get relative path to resource and id of file input element,
        and send the resource full path to the input element
     */
    public static void file(String pathInResources, String inputId) {

        // Copy files from resources upon file-input field, so files will be accessible from inside a jar
        String path = ReadFile.copyOfFileFromResources(pathInResources);
        WebElement inputElement = Get.byId(inputId);
        inputElement.sendKeys(path);
    }
}
