package vid.automation.test.sections;

import static org.hamcrest.core.Is.is;

import java.util.List;
import org.junit.Assert;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.SelectOption;


public class ViewEditPage extends VidBasePage {
    public ViewEditPage selectNodeInstanceToAdd(String vnfName) {
        selectFromDropdownByTestId(Constants.ViewEdit.VNF_OPTION_TEST_ID_PREFIX + vnfName,
                Constants.ViewEdit.ADD_VNF_BUTTON_TEST_ID);
        return this;
    }

    public ViewEditPage selectVfModuleToAdd(String vfModuleName) {
        selectFromDropdownByTestId(Constants.ViewEdit.VF_MODULE_OPTION_TEST_ID_PREFIX + vfModuleName,
                Constants.ViewEdit.ADD_VF_MODULE_BUTTON_TEST_ID);
        return this;
    }

    public ViewEditPage clickResumeButton(String instanceName) {
        //instanceName = "my_vfModule";
        String instanceId = Constants.ViewEdit.VF_MODULE_RESUME_ID_PREFIX + instanceName;
        checkIfExistResumeButton(instanceName,true);
        GeneralUIUtils.clickOnElementByTestId(instanceId);
        return this;
    }


    public ViewEditPage checkIfExistResumeButton(String instanceName, Boolean expected) {
        //instanceName = "my_vfModule";
        String instanceId = Constants.ViewEdit.VF_MODULE_RESUME_ID_PREFIX + instanceName;
        WebElement resumeButton = GeneralUIUtils.getWebElementByTestID(instanceId, 30);
        Assert.assertThat(resumeButton != null, is(expected));
        return this;
    }

    public ViewEditPage selectVolumeGroupToAdd(String volumeGroupName) {
        selectFromDropdownByTestId(Constants.ViewEdit.VOLUME_GROUP_OPTION_TEST_ID_PREFIX + volumeGroupName,
                Constants.ViewEdit.ADD_VOLUME_GROUP_BUTTON_TEST_ID);
        return this;
    }

    public ViewEditPage selectFromDropdownByText(String itemText, String dropdownButtonTestId) {
        GeneralUIUtils.clickOnElementByTestId(dropdownButtonTestId, 30);
        GeneralUIUtils.clickOnElementByText(itemText, 30);
        return this;
    }

    public ViewEditPage selectNetworkToAdd(String networkName) {
        selectFromDropdownByTestId(Constants.ViewEdit.NETWORK_OPTION_TEST_ID_PREFIX + networkName,
                Constants.ViewEdit.ADD_NETWORK_BUTTON_TEST_ID);
        return this;
    }

    public ViewEditPage selectProductFamily(String productFamily){
        SelectOption.byValue(productFamily, Constants.ViewEdit.PRODUCT_FAMILY_SELECT_TESTS_ID);
        return this;
    }

    public ViewEditPage selectLineOfBusiness(String lineOfBusiness, String cloudOwner){
        selectLineOfBusiness(lineOfBusiness);
        return this;
    }

    public ViewEditPage setLegacyRegion(String legacyRegionName){
        setInputText(Constants.ViewEdit.LEGACY_REGION_INPUT_TESTS_ID, legacyRegionName);
        return this;
    }

    public ViewEditPage selectTenant(String tenant){
        SelectOption.byValue(tenant, Constants.ViewEdit.TENANT_SELECT_TESTS_ID);
       // GeneralUIUtils.clickOnElementByTestId(Constants.ViewEdit.TENANT_SELECT_TESTS_ID, 60);
        return this;
    }

    public VidBasePage clickActivateButton() {
        GeneralUIUtils.clickOnElementByTestId(Constants.ViewEdit.ACTIVATE_BUTTON_TEST_ID, 60);
        return this;
    }

    public VidBasePage clickActivateFabricConfigurationButton() {
        GeneralUIUtils.clickOnElementByTestId(Constants.ViewEdit.ACTIVATE_FABRIC_CONFIGURATION_BUTTON_TEST_ID, 60);
        return this;
    }
    public VidBasePage clickInfoButton() {
        GeneralUIUtils.clickOnElementByTestId(Constants.ViewEdit.INFOSERVICEBUTTON, 30);
        return this;
    }

    public VidBasePage clickDeleteButton() {
        GeneralUIUtils.clickOnElementByTestId(Constants.ViewEdit.DELETESERVICEBUTTON, 3);
        return this;
    }


    public WebElement getPnf(String pnfName) {
        WebElement pnfElement = Get.byClassAndText("tree-node", "PNF: " + pnfName);
        Assert.assertNotNull("Pnf "+ pnfName +" not found under service instance", pnfElement);
        return pnfElement;
    }

    public ViewEditPage clickDissociatePnfButton(String pnfName) {
        WebElement pnfToDissociate = getPnf(pnfName);
        WebElement dissociateBtn = pnfToDissociate.findElement(By.className(Constants.ViewEdit.DISSOCIATE_BTN_CLASS));
        Assert.assertNotNull("Dissociate button not found for pnf " + pnfName, dissociateBtn);
        dissociateBtn.click();
        return this;
    }

    public VidBasePage clickDeactivateButton() {
        GeneralUIUtils.clickOnElementByTestId(Constants.ViewEdit.DEACTIVATE_BUTTON_TEST_ID, 30);
        return this;
    }

//    public ViewEditPage selectLineOfBusiness(String lineOfBusiness) {
//        try {
//            SelectOption.selectFirstTwoOptionsFromMultiselectById(Constants.ViewEdit.LINE_OF_BUSINESS_SELECT_TESTS_ID);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            return this;
//        }
//        //TODO multi SelectOption.byValue(lineOfBusiness, Constants.ViewEdit.LINE_OF_BUSINESS_SELECT_TESTS_ID);
//        return this;
//    }

    public ViewEditPage selectPlatform(List<String> platformList) {
        SelectOption.selectOptionsFromMultiselectById("multi-selectPlatform", platformList);
        return this;
    }
}
