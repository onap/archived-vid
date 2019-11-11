package vid.automation.test.sections.deploy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;

public abstract class DeployModernUIBase extends DeployDialogBase {

    @Override
    public void waitForDialogToLoad() {
        goToIframe();
    }

    String dialogTitle = "Set a new service instance";

    public void assertTitle(){
        WebElement modalTitle = GeneralUIUtils.getWebElementByTestID(Constants.CREATE_MODAL_TITLE_ID, 30);
        assertThat(modalTitle.getText(), containsString(dialogTitle));
    }

    @Override
    public void closeDialog(){
        GeneralUIUtils.ultimateWait();
        clickCancelButtonByTestID();
        goOutFromIframe();
    }

    @Override
    public void assertDialog() {
        assertTitle();
    }

    public void clickOwningEntitySelect() {
        GeneralUIUtils.clickOnElementByTestId(Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID);
    }

    public void clickProjectSelect() {
        GeneralUIUtils.clickOnElementByTestId(Constants.OwningEntity.PROJECT_SELECT_TEST_ID);
    }
}
