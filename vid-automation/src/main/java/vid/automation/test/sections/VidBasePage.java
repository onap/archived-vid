package vid.automation.test.sections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.onap.sdc.ci.tests.utilities.GeneralUIUtils.getDriver;

import com.aventstack.extentreports.Status;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import org.junit.Assert;
import org.onap.sdc.ci.tests.execute.setup.ExtentTestActions;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import vid.automation.test.Constants;
import vid.automation.test.Constants.ViewEdit;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.Exists;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.Input;
import vid.automation.test.infra.SelectOption;
import vid.automation.test.infra.Wait;

public class VidBasePage {


    public VidBasePage setInstanceName(String name) {
        setInputText(Constants.INSTANCE_NAME_SELECT_TESTS_ID, name);
        return this;
    }


    public VidBasePage setLegacyRegion(String name) {
        setInputText(Constants.ViewEdit.LEGACY_REGION_INPUT_TESTS_ID, name);
        return this;
    }

    public String generateAndSetInstanceName(String prefix) {
        String instanceName = generateInstanceName(prefix);
        setInstanceName(instanceName);
        return instanceName;
    }

    public VidBasePage setInputText(String inputTestsId, String text) {
        WebElement instanceNameInput = GeneralUIUtils.getInputElement(inputTestsId);
        instanceNameInput.sendKeys(text);
        return this;
    }

    public String getInputValue(String inputTestsId) {
        WebElement instanceNameInput = GeneralUIUtils.getInputElement(inputTestsId);
        return instanceNameInput.getAttribute("value");
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
    public VidBasePage selectFromDropdownByTestId(String itemTestId, String dropdownButtonTestId) {
        GeneralUIUtils.clickOnElementByTestId(dropdownButtonTestId, 60);
        Assert.assertTrue(String.format(Constants.ViewEdit.OPTION_IN_DROPDOWN_NOT_EXISTS,itemTestId, dropdownButtonTestId),GeneralUIUtils.getWebElementByTestID(itemTestId) != null );
        GeneralUIUtils.clickOnElementByTestId(itemTestId, 60);
        return this;
    }
    public VidBasePage noOptionDropdownByTestId( String dropdownButtonTestId) {
        List<WebElement> selectList= SelectOption.getList(dropdownButtonTestId);
        Assert.assertTrue("The Select Input "+ dropdownButtonTestId+" should be empty",selectList.size()==1);
        return this;
    }

    public static void selectSubscriberById(String subscriberId) {
        SelectOption.byValue(subscriberId, Constants.SUBSCRIBER_NAME_SELECT_TESTS_ID);
    }

    public VidBasePage selectSubscriberByName(String subscriberName) {
        SelectOption.byTestIdAndVisibleText(subscriberName, Constants.SUBSCRIBER_NAME_SELECT_TESTS_ID);
        return this;
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
        Input.replaceText(serviceUUID, Constants.BROWSE_SEARCH);
        GeneralUIUtils.ultimateWait();
        String elementTestId = Constants.DEPLOY_BUTTON_TESTS_ID_PREFIX + serviceUUID;
        GeneralUIUtils.clickOnElementByTestId(elementTestId, 30);
        GeneralUIUtils.ultimateWait();

        screenshotDeployDialog(serviceUUID);

        return this;
    }

    public boolean isModelWithGivenServiceUUIDVisible(String serviceUUID) {
        String elementTestId = Constants.DEPLOY_BUTTON_TESTS_ID_PREFIX + serviceUUID;
        try {
            GeneralUIUtils.getWebElementByTestID(elementTestId, 10);
            GeneralUIUtils.ultimateWait();
        } catch (TimeoutException te) {
            return false;
        }
        return true;
    }

    public void screenshotDeployDialog(String serviceUUID) {
        try {
            GeneralUIUtils.ultimateWait();
            GeneralUIUtils.ultimateWait(); // better screenshot
            String screenshotName = "deployService-" + serviceUUID;
            ExtentTestActions.addScreenshot(Status.INFO, screenshotName, screenshotName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public VidBasePage clickEditViewByInstanceId(String instanceId) {
        String elementTestId = Constants.VIEW_EDIT_TEST_ID_PREFIX + instanceId;
        GeneralUIUtils.clickOnElementByTestId(elementTestId, 100);

        return this;
    }

    public Boolean checkEditOrViewExistsByInstanceId(String instanceId) {
        String elementTestId = Constants.VIEW_EDIT_TEST_ID_PREFIX + instanceId;
        return Exists.byTestId(elementTestId);
    }



    public VidBasePage clickSubmitButton() {
        GeneralUIUtils.clickOnElementByText(Constants.SUBMIT_BUTTON_TEXT, 30);
        return this;
    }

    public VidBasePage clickCancelButton() {
        Click.byId(Constants.generalCancelButtonId);
        return this;
    }

    public VidBasePage clickCancelButtonByTestID() {
        GeneralUIUtils.clickOnElementByTestId(Constants.CANCEL_BUTTON_TEST_ID, 30);
        return this;
    }


    public VidBasePage clickConfirmButton() {
        GeneralUIUtils.clickOnElementByTestId(Constants.CONFIRM_BUTTON_TESTS_ID, 30);
        return this;
    }

    public VidBasePage clickConfirmButtonInResumeDelete() {
        GeneralUIUtils.clickOnElementByTestId(Constants.CONFIRM_RESUME_DELETE_TESTS_ID);
        return this;
    }

    public VidBasePage clickButtonByTestId(String testId) {
        GeneralUIUtils.clickOnElementByTestId(testId);
        return this;
    }

    public VidBasePage clickCommitCloseButton() {
        GeneralUIUtils.clickOnElementByTestId(Constants.COMMIT_CLOSE_BUTTON_ID, 30);
        return this;
    }

    public VidBasePage clickCloseButton() {
        return clickCloseButton(30);
    }

    public VidBasePage clickCloseButton(int customTimeout) {
        GeneralUIUtils.clickOnElementByText(Constants.CLOSE_BUTTON_TEXT, customTimeout);
        return this;
    }

    public VidBasePage selectLcpRegion(String lcpRegion) {
        return selectLcpRegion(lcpRegion, "AIC");
    }

    public VidBasePage selectLcpRegion(String lcpRegion, String cloudOwner) {
        GeneralUIUtils.ultimateWait();
        String visibleText = (Features.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST.isActive()) ?
            String.format("%s (%s)", lcpRegion, cloudOwner) : lcpRegion;
        SelectOption.byTestIdAndVisibleText(visibleText, Constants.ViewEdit.LCP_REGION_SELECT_TESTS_ID);
        return this;
    }

    public VidBasePage selectLineOfBusiness(String lineOfBusines) {
        GeneralUIUtils.ultimateWait();
        SelectOption.byValue(lineOfBusines, Constants.ViewEdit.LINE_OF_BUSINESS_SELECT_TESTS_ID);
        return this;
    }

    public VidBasePage selectTenant(String tenant) {
        GeneralUIUtils.ultimateWait();
        SelectOption.byValue(tenant, Constants.ViewEdit.TENANT_SELECT_TESTS_ID);
        return this;
    }

    public VidBasePage selectAicZone(String aicZone) {
        SelectOption.byValue(aicZone, Constants.ViewEdit.AIC_ZONE_TEST_ID);
        return this;
    }

    public VidBasePage selectRollbackOption(boolean rollback) {
        SelectOption.byValue(String.valueOf(rollback) , Constants.ViewEdit.ROLLBACK_TEST_ID);
        return this;
    }

    public VidBasePage selectPlatform(String platform) {
        SelectOption.byValue(platform, Constants.OwningEntity.PLATFORM_SELECT_TEST_ID);
        return this;
    }


    public void assertButtonState(String dataTestId, boolean shouldBeEnabled) {
        assertButtonStateInternal(dataTestId, shouldBeEnabled,
                (dataTestIdInner) -> GeneralUIUtils.getWebElementByTestID(dataTestIdInner, 60));
    }

    public void assertButtonStateEvenIfButtonNotVisible(String dataTestId, boolean shouldBeEnabled) {
        // getInputElement is quite similar to getWebElementByTestID, but doesn't use
        // the visibility predicate, so button is reachable bhind the grayed-out panel
        assertButtonStateInternal(dataTestId, shouldBeEnabled,
                (dataTestIdInner) -> GeneralUIUtils.getInputElement(dataTestIdInner));
    }

    protected void assertButtonStateInternal(String dataTestId, boolean shouldBeEnabled, Function<String,WebElement> strategy) {
        GeneralUIUtils.ultimateWait();
        boolean enabledElement= strategy.apply(dataTestId).getAttribute("disabled") == null;
        if(shouldBeEnabled) {
            Assert.assertTrue(String.format(Constants.ViewEdit.DISABLE_ERROR_MESSAGE,dataTestId), enabledElement);
        }else{
            Assert.assertFalse(String.format(Constants.ViewEdit.ENABLE_ERROR_MESSAGE,dataTestId),enabledElement);
        }

    }
    public VidBasePage assertMsoRequestModal(String statusMsg) {
        boolean waitForTextResult = Wait.waitByClassAndText("status", statusMsg, 20);
        Assert.assertTrue(statusMsg + " message didn't appear on time", waitForTextResult);

        return this;
    }

    public VidBasePage refreshPage() {
        getDriver().navigate().refresh();
        return this;
    }

    public String navigateTo(String path) {
        String envUrl = System.getProperty("ENV_URL");
        URI uri;
        try {
            uri = new URI(envUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String target = uri.resolve(path).toString();

        getDriver().navigate().to(target);
        GeneralUIUtils.ultimateWait();

        return target;
    }

    public String getTextByTestID(String testId){
        WebElement webElement= GeneralUIUtils.getWebElementByTestID(testId);
        return webElement.getText();
    }

    public void checkAndCloseAlert(String expectedText) {
       String alertText= Get.alertText();
       Assert.assertEquals(expectedText, alertText);
       Click.acceptAlert();
    }
    public static void goToIframe() {
        final long start = System.currentTimeMillis();
        goOutFromIframe();
        GeneralUIUtils.ultimateWait();
        System.out.println("ultimateWait waited " + (System.currentTimeMillis() - start));
        final WebDriver iframeReady = new WebDriverWait(getDriver(), 20).until(
                ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe"))
        );
        Assert.assertNotNull("failed going into iframe", iframeReady);

        final long start2 = System.currentTimeMillis();
        GeneralUIUtils.ultimateWait();
        System.out.println("ultimateWait waited " + (System.currentTimeMillis() - start2));
    }

    public static void goOutFromIframe(){
        GeneralUIUtils.acceptDeadObjectException(GeneralUIUtils::ultimateWait);
        getDriver().switchTo().defaultContent();
    }


    public void verifyOpenOldViewEdit(String serviceInstanceName, String serviceInstanceId, boolean openShouldBeEnabled, boolean checkPortMirroring, boolean checkAddVnf) {
        InstantiationStatusPage.checkMenuItem(serviceInstanceName, Constants.InstantiationStatus.CONTEXT_MENU_HEADER_OPEN_ITEM, openShouldBeEnabled, contextMenuOpen -> {
            Click.byTestId(contextMenuOpen);
            VidBasePage.goOutFromIframe();
            GeneralUIUtils.ultimateWait();

            Wait.byText("View/Edit Service Instance");
            if (serviceInstanceId != null) {
                Wait.byText(serviceInstanceId);
            }
            Wait.byText(serviceInstanceName);

            if (checkPortMirroring) {
                Wait.byText("Add node instance");
                Wait.byText(ViewEdit.COMMON_PORT_MIRRORING_PORT_NAME);
            }

            if (checkAddVnf) {
                // Validate bug fix - we open old popup in view/edit
                Click.byTestId("addVNFButton");
                Click.byTestId("addVNFOption-2017-488_PASQUALE-vPE 0");
                assertThat(Get.byTestId("create-modal-title").getText(), containsString("a la carte"));
                Click.byTestId("cancelButton");
                //end of bug fix validation
            }

            screenshotDeployDialog("view-edit-" + serviceInstanceName);
            SideMenu.navigateToMacroInstantiationStatus();
        });
    }


    public static WebDriverWait waitUntilDriverIsReady(int time) {
        return new WebDriverWait(getDriver(), (long)time);
    }

    public String getReduxState() {
        final JavascriptExecutor javascriptExecutor = (JavascriptExecutor) GeneralUIUtils.getDriver();
        String reduxState = (String)javascriptExecutor.executeScript("return window.sessionStorage.getItem('reduxState');");
        System.out.println(reduxState);
        return reduxState;
    }

    public void setReduxState(String state) {
        final JavascriptExecutor javascriptExecutor = (JavascriptExecutor) GeneralUIUtils.getDriver();
        String script = String.format("window.sessionStorage.setItem('reduxState', '%s');", state);
        System.out.println("executing script:");
        System.out.println(script);
        javascriptExecutor.executeScript(script);
    }

}
