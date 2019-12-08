package vid.automation.test.sections;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.Wait;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

public abstract class InstantiationStatusPage extends VidBasePage {

    public static final String refreshButtonId = "refresh-btn";

    private static final String NEW_VIEW_EDIT_RELATIVE_URL = "serviceModels.htm#/servicePlanning";


    public static String getWebTrTdSpanElementByParentID(WebElement tr, String id, int timeout) {
            return tr.findElements(By.xpath(".//*[@id='" + id + "']//span")).get(0).getText();
    }

    public static int getNumberOfTableRows(int timeout){
        WebDriverWait wait = waitUntilDriverIsReady(timeout);
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[table]//tbody/tr"))).size();
    }

    public static void verifyUrlMatchInstantiationStatusWithFilterSearchParam(String serviceModelId) {

        Map<String, String> paramsMap = extractQueryParamsFromCurrentURL("instantiationStatus?");
        assertEquals(paramsMap.get("filterText"), serviceModelId);

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

    public static void checkMenuItem(String actualInstanceName, String contextMenuItem, boolean shouldBeEnabled, Consumer<String> doIfEnabled) {
        Wait.waitFor(name -> {
            if (null == getInstantiationStatusRow(name)) {
                clickRefreshButton();
                return false;
            } else {
                return true;
            }
        }, actualInstanceName, 8, 1);
        final WebElement row = getInstantiationStatusRow(actualInstanceName);
        row.findElement(By.className("menu-div")).click();
        String clazz = Get.byXpath("//div[@data-tests-id='" + contextMenuItem + "']/ancestor::li").getAttribute("class");
        assertThat("item " + contextMenuItem + " of " + actualInstanceName +
                " should be " + (shouldBeEnabled ? "enabled" : "disabled"), !clazz.equals("disabled"), is(shouldBeEnabled));
        if (shouldBeEnabled) {
            doIfEnabled.accept(contextMenuItem);
        } else {
            // dismiss menu
            Get.byTestId("instantiation-status-title").click();
        }
    }

    public void showTooltipByHoverAboveStatusIcon(String elementTestId){
        WebElement selectedElement = GeneralUIUtils.getWebElementByTestID(elementTestId, 30);
    }

    public static void openDrawingBoardForRetry(String serviceInstanceName) {
        InstantiationStatusPage.checkMenuItem(serviceInstanceName, Constants.InstantiationStatus.CONTEXT_MENU_RETRY, true, contextMenuRetry -> {
            Click.byTestId(contextMenuRetry);
            VidBasePage.goOutFromIframe();
            verifyUrlPrefixMatchNewViewEdit("RETRY_EDIT");
        });
    }

    public static void verifyOpenNewViewEdit(String serviceInstanceName, String serviceInstanceId, String serviceModelId, String serviceType, String subscriberId, String mode) {
        InstantiationStatusPage.checkMenuItem(serviceInstanceName, Constants.InstantiationStatus.CONTEXT_MENU_HEADER_OPEN_ITEM, true, contextMenuOpen -> {
            Click.byTestId(contextMenuOpen);
            VidBasePage.goOutFromIframe();
            verifyUrlMatchNewViewEdit(serviceInstanceId, serviceModelId, serviceType, subscriberId, mode);
            SideMenu.navigateToMacroInstantiationStatus();
        });
    }

    public static void verifyOpenNewViewEdit(String serviceInstanceName, boolean openShouldBeEnabled, String expectedMode) {
        InstantiationStatusPage.checkMenuItem(serviceInstanceName, Constants.InstantiationStatus.CONTEXT_MENU_HEADER_OPEN_ITEM, openShouldBeEnabled, contextMenuOpen -> {
            Click.byTestId(contextMenuOpen);
            VidBasePage.goOutFromIframe();
            verifyUrlPrefixMatchNewViewEdit(expectedMode);
            SideMenu.navigateToMacroInstantiationStatus();
        });
    }

    public static void verifyUrlMatchNewViewEdit(String serviceInstanceId, String serviceModelId, String serviceType, String subscriberId, String expectedMode) {
        verifyUrlPrefixMatchNewViewEdit(expectedMode);
        Map<String, String> paramsMap = extractQueryParamsFromCurrentURL(NEW_VIEW_EDIT_RELATIVE_URL + "/" + expectedMode + "?");
        //assertEquals(paramsMap.get("mode"), expectedMode);
        assertEquals(paramsMap.get("serviceInstanceId"), serviceInstanceId);
        assertEquals(paramsMap.get("serviceModelId"), serviceModelId);
        assertEquals(paramsMap.get("serviceType"), serviceType);
        assertEquals(paramsMap.get("subscriberId"), subscriberId);
    }

    protected static Map<String, String> extractQueryParamsFromCurrentURL(String relativePath) {
        String currentUrl = GeneralUIUtils.getDriver().getCurrentUrl();
        //unfortunately parse(final URI uri, final String charset) can't handle with the #/ part of the uri
        String urlSuffix = currentUrl.substring(currentUrl.indexOf(relativePath)+relativePath.length());
        List<NameValuePair> params = URLEncodedUtils.parse(urlSuffix, Charset.forName("UTF-8"));
        return params.stream().collect(toMap(NameValuePair::getName, NameValuePair::getValue));
    }

    public static void verifyUrlPrefixMatchNewViewEdit(String expectedMode) {
        String currentUrl = GeneralUIUtils.getDriver().getCurrentUrl();
        assertThat(currentUrl, containsString(NEW_VIEW_EDIT_RELATIVE_URL  + "/" + expectedMode));
    }
}
