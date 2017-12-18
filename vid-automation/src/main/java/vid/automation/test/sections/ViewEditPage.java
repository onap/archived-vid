package vid.automation.test.sections;

import org.junit.Assert;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import vid.automation.test.Constants;
import vid.automation.test.infra.SelectOption;

/**
 * Created by itzikliderman on 13/06/2017.
 */
public class ViewEditPage extends VidBasePage {
    public ViewEditPage selectVNFToAdd(String vnfName) {
        selectFromDropdownByTestId(Constants.ViewEdit.VNF_OPTION_TEST_ID_PREFIX + vnfName,
                Constants.ViewEdit.ADD_VNF_BUTTON_TEST_ID);
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

    public ViewEditPage selectFromDropdownByTestId(String itemTestId, String dropdownButtonTestId) {
        GeneralUIUtils.clickOnElementByTestId(dropdownButtonTestId, 30);
        GeneralUIUtils.clickOnElementByTestId(itemTestId, 30);
        return this;
    }

    public ViewEditPage selectProductFamily(String productFamily){
        SelectOption.byValue(productFamily, Constants.ViewEdit.PRODUCT_FAMILY_SELECT_TESTS_ID);
        return this;
    }

    public ViewEditPage selectLCPRegion(String lcpRegion){
        SelectOption.byValue(lcpRegion, Constants.ViewEdit.LCP_REGION_SELECT_TESTS_ID);
        return this;
    }

    public ViewEditPage setLegacyRegion(String legacyRegionName){
        setInputText(Constants.ViewEdit.LEGACY_REGION_INPUT_TESTS_ID, legacyRegionName);
        return this;
    }

    public ViewEditPage selectTenant(String tenant){
        SelectOption.byValue(tenant, Constants.ViewEdit.TENANT_SELECT_TESTS_ID);
        return this;
    }

    public VidBasePage clickActivateButton() {
        GeneralUIUtils.clickOnElementByText(Constants.ViewEdit.ACTIVATE_BUTTON_TEST_ID, 30);
        return this;
    }
}
