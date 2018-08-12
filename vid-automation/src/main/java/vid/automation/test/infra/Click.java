package vid.automation.test.infra;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

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

    public static void byXpath(String xpath) {
        WebElement element = Get.byXpath(xpath);
        Assert.assertNotNull(element);
        element.click();
    }


    public static void onFirstSelectOptionById(String id) {
        Select selectlist = new Select(Get.byId(id));
        if(selectlist.getOptions().size() > 1) {
            selectlist.selectByIndex(1);
        }
    }

    public static void onFirstSelectOptionByTestId(String dataTestId) {
        Select selectList = new Select(Get.byTestId(dataTestId));
        if(selectList.getOptions().size() > 1) {
            selectList.selectByIndex(1);
        }
    }

    public static void onFirstSelectOptionByClass(String className) {
        final List<WebElement> webElements = Get.byClass(className);
        webElements.forEach(webElement -> {
            Select selectlist = new Select(webElement);
            if (selectlist.getOptions().size() > 1) {
                selectlist.selectByIndex(1);
            }
        });
    }

    public static void byClassAndVisibleText(String className, String text ) {
        WebElement element = Get.byClassAndText(className, text);
        element.click();
    }



    public static void acceptAlert() {
        Alert alert = GeneralUIUtils.getDriver().switchTo().alert();
        Assert.assertTrue(alert != null);
        alert.accept();
    }
}
