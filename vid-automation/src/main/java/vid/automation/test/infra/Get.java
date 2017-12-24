package vid.automation.test.infra;

import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class Get {
    public static WebElement byId(String id) {
        try {
            return GeneralUIUtils.getDriver().findElement(By.id(id));
        } catch (Exception var2) {
            return null;
        }
    }

    public static WebElement byClassAndText(String className, String text) {
        WebElement result = null;
        List<WebElement> elements = GeneralUIUtils.getWebElementsListByContainsClassName(className);

        for(WebElement element : elements) {
            if (element.getText().contains(text)) {
                result = element;
                break;
            }
        }

        return result;
    }

    public static List<WebElement> byClass(String className) {
        return GeneralUIUtils.getWebElementsListByContainsClassName(className);
    }

    public static WebElement byCssSelector(String css) {
        return GeneralUIUtils.getDriver().findElement(By.cssSelector(css));
    }

    public static List<WebElement> byTableId(String tableId) {
        try {
            return GeneralUIUtils.getElemenetsFromTable(By.xpath("//table[@data-test-id=\"" + tableId + "\"]/tbody/tr/td"));
        } catch (Exception var2) {
            return null;
        }
    }

    public static List<String> tableValuesById(String tableId) {
        List<WebElement> elements = byTableId(tableId);
        if(elements != null) {
            return GeneralUIUtils.getWebElementListText(elements);
        }
        else {
            return null;
        }
    }
}
