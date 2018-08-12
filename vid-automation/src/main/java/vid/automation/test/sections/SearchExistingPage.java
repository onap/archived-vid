package vid.automation.test.sections;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.Wait;

import static org.testng.Assert.assertTrue;


/**
 * Created by pickjonathan on 10/07/2017.
 */

public class SearchExistingPage extends VidBasePage {

    public static final String PROJECT_NAME = "Project Name";

    public SearchExistingPage() {}

    public void selectSearchById(){
        selectSearchBy(Constants.EditExistingInstance.SERVICE_INSTANCE_ID);
    }

    public void selectSearchByName(){
        selectSearchBy(Constants.EditExistingInstance.SERVICE_INSTANCE_NAME);
    }

    public void selectSearchBy(String searchTypeOptionText) {
        boolean findAndWaitByText = GeneralUIUtils.findAndWaitByText(Constants.EditExistingInstance.SELECT_SERVICE_INSTANCE, 30);
        Assert.assertTrue(findAndWaitByText);
        GeneralUIUtils.clickOnElementByText(Constants.EditExistingInstance.SELECT_SERVICE_INSTANCE, 30);

        boolean isSearchByIdAvailable = GeneralUIUtils.findAndWaitByText(searchTypeOptionText, 30);
        Assert.assertTrue(isSearchByIdAvailable);
        GeneralUIUtils.clickOnElementByText(searchTypeOptionText, 30);
    }

    public void searchForInstanceByUuid(String uuid) {
        selectSearchById();
        startSearchByInstanceIdentifier(uuid);
    }

    private void startSearchByInstanceIdentifier(String text) {
        WebElement textInputWebElement = GeneralUIUtils.getWebElementByTestID(Constants.EditExistingInstance.SEARCH_FOR_EXISTING_INSTANCES_INPUT, 30);
        Assert.assertTrue(textInputWebElement != null);
        textInputWebElement.clear();
        textInputWebElement.sendKeys(text);
        clickSubmitButton();
    }

    public void searchForInstanceByName(String name) {
        selectSearchByName();
        startSearchByInstanceIdentifier(name);
    }

    public void searchByProject(String projectName) {
        assertTrue(Wait.waitByClassAndText("fn-ebz-text-label", PROJECT_NAME, 1), "Failed to find label "+PROJECT_NAME); //Test bug VID-495468
        selectValueDDL(Constants.EditExistingInstance.SELECT_PROJECT_ID, projectName);
    }

    public void searchByOwningEntity(String oeName) {
        selectValueDDL(Constants.EditExistingInstance.SELECT_OWNING_ENTITY_ID, oeName);
    }

    private void selectValueDDL(String ddlId, String value) {
        Click.byId(ddlId);
        GeneralUIUtils.ultimateWait();
        Click.byText(value);
        //click again on the element that will not cover the submit button
        Click.byId(ddlId);
    }

    public void checkForEditButtons() {
        boolean isDeleteVisible = GeneralUIUtils.waitForElementInVisibilityByTestId(Constants.EditExistingInstance.DELETE_VNF_BTN, 30);
        Assert.assertFalse(isDeleteVisible);

        boolean isAddVnfModuleVisible = GeneralUIUtils.waitForElementInVisibilityByTestId(Constants.EditExistingInstance.ADD_VNF_MODULE_DROPDOWN, 30);
        Assert.assertFalse(isAddVnfModuleVisible);

        boolean isAddCustomVnfModuleVisible = GeneralUIUtils.waitForElementInVisibilityByTestId(Constants.EditExistingInstance.ADD_CUSTOM_VNF_MODULE_DROPDOWN, 30);
        Assert.assertFalse(isAddCustomVnfModuleVisible);

        boolean isAddCustomVolumeGroupVisible = GeneralUIUtils.waitForElementInVisibilityByTestId(Constants.EditExistingInstance.ADD_CUSTOM_VOLUME_GROUP, 30);
        Assert.assertFalse(isAddCustomVolumeGroupVisible);

        boolean isAddVolumeGroupVisible = GeneralUIUtils.waitForElementInVisibilityByTestId(Constants.EditExistingInstance.ADD_VOLUME_GROUP, 30);
        Assert.assertFalse(isAddVolumeGroupVisible);

        boolean isDeleteVfModuleVisible = GeneralUIUtils.waitForElementInVisibilityByTestId(Constants.EditExistingInstance.DELETE_VF_MODULE, 30);
        Assert.assertFalse(isDeleteVfModuleVisible);

        boolean isDeleteVnfGroupVisible = GeneralUIUtils.waitForElementInVisibilityByTestId(Constants.EditExistingInstance.DELETE_VNF_VOLUME_GROUP, 30);
        Assert.assertFalse(isDeleteVnfGroupVisible);

        boolean isDeleteNetworkVisible = GeneralUIUtils.waitForElementInVisibilityByTestId(Constants.EditExistingInstance.DELETE_NETWORK);
        Assert.assertFalse(isDeleteNetworkVisible);
    }
}
