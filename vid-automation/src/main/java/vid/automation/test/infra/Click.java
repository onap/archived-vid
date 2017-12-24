package vid.automation.test.infra;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class Click {
    public static void byText(String text) {
        WebElement element = GeneralUIUtils.findByText(text);
        Assert.assertTrue(element != null);

        element.click();
    }

    public static void byId(String id) {
        WebElement element = Get.byId(id);
        Assert.assertTrue(element != null);

        element.click();
    }

    public static void byTestId(String testId) {
        WebElement element = Get.byTestId(testId);
        Assert.assertTrue(element != null);
        element.click();
    }

    public static void byClass(String className) {
        List<WebElement> elements = Get.byClass(className);
        Assert.assertTrue(elements != null && elements.size() > 0);

        elements.get(0).click();
    }

    public static void onFirstSelectOptionById(String id) {
        Select selectlist = new Select(Get.byId(id));
        if(selectlist.getOptions().size() > 1) {
            selectlist.selectByIndex(1);
        }
    }
}
