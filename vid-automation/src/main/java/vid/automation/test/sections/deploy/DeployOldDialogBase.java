package vid.automation.test.sections.deploy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.junit.Assert;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;

public abstract class DeployOldDialogBase extends DeployDialogBase {

    @Override
    public void waitForDialogToLoad() {
        GeneralUIUtils.ultimateWait();
    }

    @Override
    public void closeDialog(){
        clickCancelButtonByTestID();
    }

    @Override
    public  void assertDialog(){
        assertTitle();
        boolean byText = GeneralUIUtils.findAndWaitByText(Constants.BrowseASDC.CREATE_SERVICE_INSTANCE, 15);
        Assert.assertTrue(byText);
    }

    public void assertTitle(){
        WebElement modalTitle = GeneralUIUtils.getWebElementByTestID(Constants.CREATE_MODAL_TITLE_ID, 30);
        assertThat(modalTitle.getText().toLowerCase(), containsString(getTitle()));
    }

    public abstract String getTitle();
}
