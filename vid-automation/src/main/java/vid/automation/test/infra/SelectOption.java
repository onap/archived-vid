package vid.automation.test.infra;

import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import vid.automation.test.Constants;

import java.util.List;

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

    public static void selectOptionsFromMultiselectById(String multiSelectId, List<String> options) {
        Click.byId(multiSelectId);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(String option:options) {
            Click.byClassAndVisibleText(Constants.MULTI_SELECT_UNSELECTED_CLASS, option);
        }
    }
}
