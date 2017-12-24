package vid.automation.test.infra;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
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

    /*
        Get relative path to resource and id of file input element,
        and send the resource full path to the input element
     */
    public static void file(String pathInResources, String inputId) {

        String path = Input.class.getResource("../../../../"+pathInResources).getPath().toString();
        if (SystemUtils.IS_OS_WINDOWS) {
            path = FilenameUtils.separatorsToSystem(path);
            if (path.charAt(0)=='\\') {
                path = path.substring(1);
            }
        }
        WebElement inputElement = Get.byId(inputId);
        inputElement.sendKeys(path);
    }

}
