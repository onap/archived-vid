package vid.automation.test.sections;

import org.junit.Assert;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;
import vid.automation.test.infra.Get;

import java.util.List;

/**
 * Created by itzikliderman on 13/06/2017.
 */
public class BrowseASDCPage extends VidBasePage {

    public String generateInstanceName() {
        return generateInstanceName(Constants.BrowseASDC.SERVICE_INSTANCE_NAME_PREFIX);
    }

    public VidBasePage clickPreviousVersionButton() {
        GeneralUIUtils.clickOnElementByText(Constants.PREVIOUS_VERSION, 30);
        return this;
    }

    public void assertPreviousVersionButtonNotExists(String expectedInvariantUUID){
        boolean exists = Get.byTestId("PreviousVersion-" + expectedInvariantUUID).isDisplayed();
        Assert.assertFalse(exists);
    }

    public void assertSearchFilterValue(String value){
        String searchKey = this.getInputValue(Constants.BROWSE_SEARCH);
        org.testng.Assert.assertEquals(searchKey, value);
    }

    public void fillFilterText(String text){
        this.setInputText(Constants.BROWSE_SEARCH, text);
    }

    public int countCurrentRowsInTable(){
        List<WebElement> rowsInTable = Get.byClass("sdcServiceModel");
       return rowsInTable.size();
    }

}
