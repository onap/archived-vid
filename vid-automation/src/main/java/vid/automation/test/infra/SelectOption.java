package vid.automation.test.infra;

import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import vid.automation.test.Constants;
import vid.automation.test.sections.VidBasePage;

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

    public static void byIdAndVisibleText(String id, String text) {
        Select selectlist = new Select(Get.byId(id));
        selectlist.selectByVisibleText(text);
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
}
