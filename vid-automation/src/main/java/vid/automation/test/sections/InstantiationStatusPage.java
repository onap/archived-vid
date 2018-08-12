package vid.automation.test.sections;

import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import vid.automation.test.infra.Get;

import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public abstract class InstantiationStatusPage extends VidBasePage {

    public static final String refreshButtonId = "refresh-btn";

    public static String getWebTrTdSpanElementByParentID(WebElement tr, String id, int timeout) {
            return tr.findElements(By.xpath(".//*[@id='" + id + "']//span")).get(0).getText();
    }

    public static int getNumberOfTableRows(int timeout){
        WebDriverWait wait = waitUntilDriverIsReady(timeout);
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[table]//tbody/tr"))).size();
    }

    public static WebElement assertInstantiationStatusRow(String spanIdSelector, Map<String, String> fieldsIdsAndExpected) {
        try {
            WebElement newTrRow = getInstantiationStatusRow(spanIdSelector);
            final Map<String, String> fieldIdAndActual = fieldsIdsAndExpected.entrySet().stream()
                    .collect(Collectors.toMap(
                            kv -> kv.getKey(),
                            kv -> getWebTrTdSpanElementByParentID(newTrRow, kv.getKey(), 1)
                    ));

            assertThat("failed comparing spanIdSelector " + spanIdSelector, fieldIdAndActual, is(fieldsIdsAndExpected));

            return newTrRow;
        } catch (Exception e) {
            throw new RuntimeException("error while assertInstantiationStatusRow with: String spanIdSelector=" +
                    spanIdSelector + ", fieldsIdsAndExpected=" + fieldsIdsAndExpected, e);
        }
    }

    public static WebElement getInstantiationStatusRow(String spanIdSelector) {
        GeneralUIUtils.ultimateWait();
        return Get.byXpath("//*[@id='" + spanIdSelector + "']/parent::*/parent::*/parent::*", 0);
    }

    public static void clickRefreshButton() {
        WebDriverWait wait = waitUntilDriverIsReady(0);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='"+ refreshButtonId + "']"))).click();
        GeneralUIUtils.ultimateWait();
    }


}
