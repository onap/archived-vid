package vid.automation.test.sections;

import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class DeployMacroDialog extends DeployMacroDialogBase {
    String dialogTitle = Constants.BrowseASDC.CREATE_SERVICE_INSTANCE_MACRO_MODAL;
    @Override
    public void assertTitle(){

        WebElement modalTitle = GeneralUIUtils.getWebElementByTestID(Constants.CREATE_MODAL_TITLE_ID, 30);
        assertThat(modalTitle.getText().toLowerCase(), containsString(dialogTitle));
    }
    @Override
    public void closeDialog(){
        GeneralUIUtils.ultimateWait();
        clickCancelButtonByTestID();
        goOutFromIframe();
    }

    @Override
    public void assertDialogExists() {
        assertTitle();
    }

    @Override
    public void clickOwningEntitySelect() {
        GeneralUIUtils.clickOnElementByTestId(Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID);
    }

    @Override
    public void clickProjectSelect() {
        GeneralUIUtils.clickOnElementByTestId(Constants.OwningEntity.PROJECT_SELECT_TEST_ID);
    }
}
