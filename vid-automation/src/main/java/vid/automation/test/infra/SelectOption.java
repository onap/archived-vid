package vid.automation.test.infra;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.ObjectUtils;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import vid.automation.test.Constants;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by itzikliderman on 18/07/2017.
 */
public class SelectOption {
    public static Select byValue(String value, String dataTestsId) {
        Select select = new Select(GeneralUIUtils.getWebElementByTestID(dataTestsId));
        if(value != null) {
            select.selectByValue(value);
        }

        return select;
    }

    public static Select byIdAndVisibleText(String id, String text) {
        Select selectlist = new Select(Get.byId(id));
        if(text != null) {
            selectlist.selectByVisibleText(text);
        }

        return selectlist;
    }

    public static void byClassAndVisibleText(String className, String text) {
        final List<WebElement> webElements = Get.byClass(className);
        webElements.forEach(webElement -> {
            final String id = webElement.getAttribute("id");
            byIdAndVisibleText(id, text);
        });
    }
    public static List<WebElement> getList(String selectDataTestId) {
        Select selectList = GeneralUIUtils.getSelectList(null, selectDataTestId);
        return selectList.getOptions();
    }
    public static String getSelectedOption(String selectDataTestId) {
        Select selectList = GeneralUIUtils.getSelectList(null, selectDataTestId);
        return selectList.getFirstSelectedOption().getText();
    }
    public static void byTestIdAndVisibleText(String displayName, String selectDataTestId) {
        GeneralUIUtils.getSelectList(displayName, selectDataTestId);
    }

    public static void selectFirstTwoOptionsFromMultiselectById(String multiSelectId) throws InterruptedException {
        Click.byId(multiSelectId);
        Thread.sleep(1000);
        Click.byClass(Constants.MULTI_SELECT_UNSELECTED_CLASS);
        Click.byClass(Constants.MULTI_SELECT_UNSELECTED_CLASS);

    }

    public static void waitForOptionInSelect(String option, String selectTestId) {
        Wait.waitFor(foo ->
                        ObjectUtils.defaultIfNull(SelectOption.getList(selectTestId), ImmutableList.<WebElement>of())
                                .stream().map(o -> o.getText()).filter(o -> option.equals(o)).findAny().isPresent(),
                "", 10, 200, TimeUnit.MILLISECONDS);
    }

    public static void selectOptionsFromMultiselectById(String multiSelectId, List<String> options) {
        WebElement multiselectComponent = Get.byXpath("//*[@data-tests-id='" + multiSelectId +"']//div[contains(@class, 'c-btn')]");
        if(multiselectComponent != null){
            multiselectComponent.click();
            try {
                Thread.sleep(1000);
                for(String option:options) {
                    String multiSelectOptionPath = "//label[@data-tests-id='" + multiSelectId + "-" + option + "']";
                    WebElement multiSelelctOption = Get.byXpath(multiSelectOptionPath);
                    if(multiSelelctOption != null){
                        multiSelelctOption.click();
                    }
                }
                multiselectComponent.click();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
