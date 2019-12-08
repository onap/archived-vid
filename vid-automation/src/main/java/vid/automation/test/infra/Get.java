package vid.automation.test.infra;

import org.junit.Assert;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Get {
    public static WebElement byId(String id) {
        try {
            return GeneralUIUtils.getDriver().findElement(By.id(id));
        } catch (Exception var2) {
            return null;
        }
    }

    public static WebElement byTestId(String dataTestId) {
        try {
            return GeneralUIUtils.getDriver().findElement(getXpathForDataTestId(dataTestId));
        } catch (Exception var2) {
            return null;
        }
    }

    public static WebElement byXpath(String xpath) {
        try {
            return GeneralUIUtils.getWebElementBy(By.xpath(xpath));
        } catch (Exception var2) {
            return null;
        }
    }

    public static WebElement byXpath(WebElement context, String xpath) {
        try {
            return context.findElement(By.xpath(xpath));
        } catch (Exception var2) {
            return null;
        }
    }

    public static WebElement byXpath(String xpath, int timeout) {
        try {
            return GeneralUIUtils.getWebElementBy(By.xpath(xpath), timeout);
        } catch (Exception var2) {
            return null;
        }
    }


    public static List<WebElement> multipleElementsByTestId(String dataTestId) {
        try {
            return GeneralUIUtils.getWebElementsListByTestID(dataTestId);
        } catch (Exception var2) {
            return null;
        }
    }

    public static WebElement byClassAndText(String className, String text) {
        return byClassAndText(className, text, null);
    }

    public static WebElement byClassAndText(String className, String text, Integer timeoutInSeconds) {
        WebElement result = null;
        List<WebElement> elements;
        if (timeoutInSeconds!=null) {
            elements = GeneralUIUtils.getWebElementsListByContainsClassName(className, timeoutInSeconds);
        }
        else {
            elements = GeneralUIUtils.getWebElementsListByContainsClassName(className);
        }

        for(WebElement element : elements) {
            if (element.getText().contains(text)) {
                result = element;
                break;
            }
        }

        return result;
    }

    public static WebElement byCssSelectorAndText(String css, String text) {
        WebElement element = GeneralUIUtils.getDriver().findElement(By.cssSelector(css));

        if (element != null && element.getText().contains(text)) {
            return element;
        }

        return null;
    }

    public static String selectedOptionText(String dataTestId) {
        return GeneralUIUtils.getSelectedElementFromDropDown(dataTestId).getText();
    }

    public static Boolean isOptionSelectedInMultiSelect(String dataTestId, String option) {
        return GeneralUIUtils.isOptionSelectedInMultiSelect(dataTestId, option);
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
    public static String alertText() {
        WebDriverWait wait = new WebDriverWait(GeneralUIUtils.getDriver(), 2);
        wait.until(alertIsPresent());
        Alert alert = GeneralUIUtils.getDriver().switchTo().alert();
        Assert.assertTrue(alert != null);
        return alert.getText();
    }
    
	public static Function<WebDriver, Alert> alertIsPresent() {
		return new Function<WebDriver, Alert>() {
			public String toString() {
				return "alert to be present";
			}

			@Override
			public Alert apply(WebDriver driver) {
				try {
					return driver.switchTo().alert();
				} catch (NoAlertPresentException arg2) {
					return null;
				}
			}
		};
	}

    public static List<WebElement> listByTestId(String dataTestId) {
        try {
            return GeneralUIUtils.getDriver().findElements(getXpathForDataTestId(dataTestId));
        } catch (Exception var2) {
            return null;
        }
    }

    public static By getXpathForDataTestId(String dataTestId) {
        return By.xpath("//*[@data-tests-id='" + dataTestId + "']");
    }
}
