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

    public static WebElement byTestId(String testId) {
        try {
            return GeneralUIUtils.getWebElementByTestID(testId);
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

    public static String selectedOptionText(String dataTestId) {
        return GeneralUIUtils.getSelectedElementFromDropDown(dataTestId).getText();
    }

    public static List<WebElement> byClass(String className) {
        return GeneralUIUtils.getWebElementsListByContainsClassName(className);
    }

    public static WebElement byCssSelector(String css) {
        return GeneralUIUtils.getDriver().findElement(By.cssSelector(css));
    }

    public static List<String> tableHeaderValuesByTestId(String tableId) {
        return tableValuesById(tableId, "thead", "th").get(0);
    }

    public static List<List<String>> tableBodyValuesByTestId(String tableId) {
        return tableValuesById(tableId, "tbody", "td");
    }

    private static List<WebElement> rowsByTableId(String tableId,String section, String column) {
        try {
            return GeneralUIUtils.getElemenetsFromTable(By.xpath("//table[@data-tests-id=\"" + tableId + "\"]/" + section + "/tr"));
        } catch (Exception var2) {
            return null;
        }
    }

    private static List<List<String>> tableValuesById(String tableId, String section, String column) {
        List<WebElement> rows = rowsByTableId(tableId, section, column);
        if(rows != null) {
            List<List<String>> tableContent = new ArrayList<List<String>>();
            for(WebElement row:rows) {
                List<WebElement> columns = row.findElements(By.xpath(column));
                tableContent.add(GeneralUIUtils.getWebElementListText(columns));
            }
            return tableContent;
        }
        else {
            return null;
        }
    }
}
