package vid.automation.test.infra;

import static org.onap.sdc.ci.tests.utilities.GeneralUIUtils.getDriver;

import java.util.List;
import org.junit.Assert;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Click {
    public static void byText(String text) {
        WebElement element = GeneralUIUtils.findByText(text);
        Assert.assertTrue(element != null);

        clickWhenClickable(element);
    }

    public static void byId(String id) {
        WebElement element = Get.byId(id);
        Assert.assertTrue(element != null);

        clickWhenClickable(element);
    }

    public static void byTestId(String testId) {
        WebElement element = Get.byTestId(testId);
        Assert.assertTrue(element != null);
        clickWhenClickable(element);
    }

    public static void byTestIdOnceItsAvailable(String testId, int timeout) {
        GeneralUIUtils.clickElementUsingActions(
            Get.byXpath("//*[@data-tests-id='" + testId + "']", timeout));
    }

    public static void byClass(String className) {
        List<WebElement> elements = Get.byClass(className);
        Assert.assertTrue(elements != null && elements.size() > 0);

        clickWhenClickable(elements.get(0));
    }

    public static void byXpath(String xpath) {
        WebElement element = Get.byXpath(xpath);
        Assert.assertNotNull(element);
        clickWhenClickable(element);
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
        clickWhenClickable(element);
    }



    public static void acceptAlert() {
        Alert alert = GeneralUIUtils.getDriver().switchTo().alert();
        Assert.assertTrue(alert != null);
        alert.accept();
    }

    private static void clickWhenClickable(WebElement element) {
        new WebDriverWait(getDriver(), 1)
                .until(ExpectedConditions.elementToBeClickable(element))
                .click();
    }

}
