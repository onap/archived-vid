package vid.automation.test.sections;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class DeployMacroDialogOld extends DeployMacroDialogBase {
    String dialogTitle = "macro";

    @Override
    public void assertTitle(){
        WebElement modalTitle = GeneralUIUtils.getWebElementByTestID(Constants.CREATE_MODAL_TITLE_ID, 30);
        assertThat(modalTitle.getText().toLowerCase(), containsString(dialogTitle));
    }

    @Override
    public void closeDialog(){
        GeneralUIUtils.ultimateWait();
        clickCancelButtonByTestID();
    }

    @Override
    public  void assertDialogExists(){
        boolean byText = GeneralUIUtils.findAndWaitByText(Constants.BrowseASDC.CREATE_SERVICE_INSTANCE, 15);
        Assert.assertTrue(byText);
    }

    @Override
    public  void clickOwningEntitySelect(){
        GeneralUIUtils.clickOnElementByTestId(Constants.OwningEntity.OWNING_ENTITY_SELECT_TEST_ID);
    }

    @Override
    public  void clickProjectSelect(){
        GeneralUIUtils.clickOnElementByTestId(Constants.OwningEntity.PROJECT_SELECT_TEST_ID);
    }

}
