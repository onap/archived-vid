package vid.automation.test.sections;

import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;
import vid.automation.test.infra.SelectOption;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VidBasePage {

    public VidBasePage setInstanceName(String name) {
        setInputText(Constants.INSTANCE_NAME_SELECT_TESTS_ID, name);
        return this;
    }

    public void generateAndSetInstanceName(String prefix) {
        String instanceName = generateInstanceName(prefix);
        setInstanceName(instanceName);
    }

    public VidBasePage setInputText(String inputTestsId, String text) {
        WebElement instanceNameInput = GeneralUIUtils.getInputElement(inputTestsId);
        instanceNameInput.sendKeys(text);
        return this;
    }

    public String generateInstanceName(String prefix) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.BrowseASDC.DATE_FORMAT);
        Date now = Calendar.getInstance().getTime();
        return prefix + sdf.format(now);
    }

    public VidBasePage selectServiceTypeByName(String serviceType) {
        SelectOption.byTestIdAndVisibleText(serviceType, Constants.SERVICE_TYPE_SELECT_TESTS_ID);
        return this;
    }

    public static void selectSubscriberById(String subscriberId) {
        SelectOption.byValue(subscriberId, Constants.SUBSCRIBER_NAME_SELECT_TESTS_ID);
    }

    public VidBasePage selectProductFamily(String productFamily) {
        SelectOption.byValue(productFamily, Constants.ViewEdit.PRODUCT_FAMILY_SELECT_TESTS_ID);
        return this;
    }

    public VidBasePage selectSuppressRollback(String shouldSuppress) {
        SelectOption.byTestIdAndVisibleText(shouldSuppress, Constants.SUPPRESS_ROLLBACK_SELECT_TESTS_ID);
        return this;
    }

    public VidBasePage clickDeployServiceButtonByServiceUUID(String serviceUUID) {
        setInputText(Constants.BROWSE_SEARCH, serviceUUID);
        String elementTestId = Constants.DEPLOY_BUTTON_TESTS_ID_PREFIX + serviceUUID;
        GeneralUIUtils.clickOnElementByTestId(elementTestId, 30);
        GeneralUIUtils.ultimateWait();
        return this;
    }

    public VidBasePage clickEditViewByInstanceId(String instanceId) {
        String elementTestId = Constants.VIEW_EDIT_TEST_ID_PREFIX + instanceId;
        GeneralUIUtils.clickOnElementByTestId(elementTestId, 30);
        GeneralUIUtils.ultimateWait();
        return this;
    }

    public VidBasePage clickSubmitButton() {
        GeneralUIUtils.clickOnElementByText(Constants.SUBMIT_BUTTON_TEXT, 30);
        return this;
    }

    public VidBasePage clickConfirmButton() {
        GeneralUIUtils.clickOnElementByTestId(Constants.CONFIRM_BUTTON_TESTS_ID, 30);
        return this;
    }

    public VidBasePage clickCloseButton() {
        GeneralUIUtils.clickOnElementByText(Constants.CLOSE_BUTTON_TEXT, 30);
        return this;
    }

    public VidBasePage selectLcpRegion(String lcpRegion) {
        SelectOption.byValue(lcpRegion, Constants.ViewEdit.LCP_REGION_SELECT_TESTS_ID);
        return this;
    }

    public VidBasePage selectTenant(String tenant) {
        SelectOption.byValue(tenant, Constants.ViewEdit.TENANT_SELECT_TESTS_ID);
        return this;
    }

    public VidBasePage selectAicZone(String aicZone) {
        SelectOption.byValue(aicZone, Constants.ViewEdit.AIC_ZONE_TEST_ID);
        return this;
    }
}
