package vid.automation.test.sections;

import org.junit.Assert;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;
import vid.automation.test.Constants.ConfigurationCreation;
import vid.automation.test.Constants.ViewEdit;
import vid.automation.test.infra.SelectOption;

import static org.hamcrest.core.Is.is;

public class ServiceProxyPage extends VidBasePage {

    public ServiceProxyPage chooseSource(String source){
        SelectOption.byTestIdAndVisibleText(source, Constants.ConfigurationCreation.SOURCE_DROPDOWN_TEST_ID);
        return this;
    }

    public ServiceProxyPage chooseSourceSubscriberName(String subscriberName){
        SelectOption.byTestIdAndVisibleText(subscriberName, "sourceSubscriberName");
        GeneralUIUtils.ultimateWait();
      return this;
    };

    public ServiceProxyPage chooseSourceServiceType(String sourceServiceType){
        SelectOption.byTestIdAndVisibleText(sourceServiceType, "sourceServiceType");
        GeneralUIUtils.ultimateWait();
        return this;
    }
    public ServiceProxyPage chooseCollectorServiceType(String collectorServiceType){
        SelectOption.byTestIdAndVisibleText(collectorServiceType, "collectorServiceType");
        GeneralUIUtils.ultimateWait();
        return this;
    }

    public void assertSourceSubscriberName(String sourceSubscriberName){
        String displayedSubscriberName = SelectOption.getSelectedOption(ConfigurationCreation.SOURCE_SUBSCRIBER_NAME);
        Assert.assertEquals("The displayed source subscriber name is not correct", sourceSubscriberName, displayedSubscriberName);
    }

    public void assertCollectorServiceType(String collectorServiceType) {
        String displayedCollectorServiceType = SelectOption.getSelectedOption("collectorServiceType");
        Assert.assertEquals("The displayed collector service type is incorrect", collectorServiceType, displayedCollectorServiceType);
    }

    public ServiceProxyPage chooseCollector(String collector){
        GeneralUIUtils.ultimateWait();
        SelectOption.byTestIdAndVisibleText(collector, Constants.ConfigurationCreation.COLLECTOR_DROPDOWN_TEST_ID);
        return this;
    }
    public ServiceProxyPage clickCreateButton() {
        GeneralUIUtils.clickOnElementByTestId(Constants.ConfigurationCreation.CREATE_BUTTON_TEST_ID, 60);
        return this;
    }

    public ServiceProxyPage clickDeleteConfigurationButton() {
        GeneralUIUtils.clickOnElementByTestId(deleteConfigurationButtonTestId(), 60);
        return this;
    }

    private String configurationInstanceName() {
        return "dummy_instance";
    }

    private String deleteConfigurationButtonTestId() {
        return Constants.DELETE_CONFIGURATION_BUTTON + "-" + configurationInstanceName();
    }

    private String activateDeactivateButtonTestId() {
        return Constants.ACTIVATE_DEACTIVATE_BUTTON + "-" + configurationInstanceName();
    }

    private String enableDisableButtonTestId(String portName) {
        return Constants.ENABLE_DISABLE_BUTTON + "-" + configurationInstanceName() + "-" + portName;
    }

    public ServiceProxyPage assertDeleteConfigurationButtonExists(boolean shouldExist){

        if (shouldExist) {
            WebElement selectedV = GeneralUIUtils.getWebElementByTestID(deleteConfigurationButtonTestId(), 3);
            Assert.assertThat(selectedV != null, is(shouldExist));
            Assert.assertThat(selectedV.isDisplayed(), is(shouldExist));
        } else {
            boolean webElementExistByTestId = GeneralUIUtils.isWebElementExistByTestId(deleteConfigurationButtonTestId());
            Assert.assertThat(webElementExistByTestId, is(shouldExist));
        }
        return this;
    }

    public void assertSourceModelName(String sourceName){
        String displayedSourceName= getTextByTestID(Constants.ConfigurationCreation.SOURCE_INSTANCE_NAME_TEST_ID);
        Assert.assertEquals("The displayed source name is not correct", sourceName+" i", displayedSourceName);
    }

    public void assertCollectorModelName(String collectorName) {
        String displayedCollectorName = getTextByTestID(Constants.ConfigurationCreation.COLLECTOR_INSTANCE_NAME_TEST_ID);
        Assert.assertEquals("The displayed collector name is not correct", collectorName+"i", displayedCollectorName);
    }

    public void assertNoResultRequirementsDropDown(String msgTestId, String resourceType){
        String noResultText = GeneralUIUtils.getWebElementByTestID(msgTestId, 60).getText();
        Assert.assertEquals("The error message no instance in DropDown is not match","No "+resourceType+" instances found.",noResultText);
    }
    public ServiceProxyPage clickInfoButton(String infoButtonTestId) {
        GeneralUIUtils.clickOnElementByTestId(infoButtonTestId, 90);
        return this;
    }


    public ServiceProxyPage assertSelectedInstanceIcon(String SelectedIconTestId){
        WebElement selectedV = GeneralUIUtils.getWebElementByTestID(SelectedIconTestId, 90);
        Assert.assertTrue(selectedV != null);
        String selectedVClass = selectedV.getAttribute("class");
        Assert.assertTrue(selectedVClass.contains("valid-large"));
        return this;
    }

    public ServiceProxyPage clickActivateDeactivateButton() {
        GeneralUIUtils.clickOnElementByTestId(activateDeactivateButtonTestId(), 60);
        return this;
    }

    public ServiceProxyPage clickEnableDisableButton(){
        GeneralUIUtils.clickOnElementByTestId(enableDisableButtonTestId(ViewEdit.COMMON_PORT_MIRRORING_PORT_NAME), 60);
        return this;
    }
}
