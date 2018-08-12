package vid.automation.test.sections;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;
import vid.automation.test.infra.SelectOption;

public class CreateConfigurationPage extends VidBasePage {

    public CreateConfigurationPage setInstanceName(String name) {
        setInputText(Constants.ConfigurationCreation.INSTANCE_NAME_INPUT_TEST_ID, name);
        return this;
    }

    public CreateConfigurationPage chooseRegion(String region){
        SelectOption.byTestIdAndVisibleText(region, Constants.ConfigurationCreation.REGION_DROPDOWN_TEST_ID);
        return this;
    }

    public CreateConfigurationPage chooseTenant(String tenant){
        SelectOption.byTestIdAndVisibleText(tenant, Constants.ConfigurationCreation.TENANT_DROPDOWN_TEST_ID);
        return this;
    }

    public CreateConfigurationPage clickNextButton() {
        GeneralUIUtils.clickOnElementByTestId(Constants.ConfigurationCreation.NEXT_BUTTON_TEST_ID, 60);
        return this;
    }

    public CreateConfigurationPage clickBackButton() {
        GeneralUIUtils.clickOnElementByTestId(Constants.ConfigurationCreation.BACK_BUTTON_TEST_ID, 60);
        return this;
    }

    public String getInstanceName() {
        return getInputValue(Constants.ConfigurationCreation.INSTANCE_NAME_INPUT_TEST_ID);
    }
    public String getRegion() {
        return getInputValue(Constants.ConfigurationCreation.REGION_DROPDOWN_TEST_ID);
    }
    public String getTenant() {
       return SelectOption.getSelectedOption(Constants.ConfigurationCreation.TENANT_DROPDOWN_TEST_ID);
    }
}
