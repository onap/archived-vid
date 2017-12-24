package vid.automation.test.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.openecomp.sdc.ci.tests.datatypes.Configuration;
import org.openecomp.sdc.ci.tests.datatypes.UserCredentials;
import org.openecomp.sdc.ci.tests.execute.setup.SetupCDTest;
import org.openecomp.sdc.ci.tests.utilities.FileHandling;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.SelectOption;
import vid.automation.test.model.*;
import vid.automation.test.sections.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.testng.AssertJUnit.fail;


public class VidBaseTestCase extends SetupCDTest {

    static String currentUserId = null;

    @Override
    protected UserCredentials getUserCredentials() {
        ObjectMapper mapper = new ObjectMapper().enableDefaultTyping();
        try {
            File configFile = FileHandling.getConfigFile("credentials");
            if(!configFile.exists()) {
                String basePath = System.getProperty("BASE_PATH");
                configFile = new File( basePath + File.separator + "conf" + File.separator + "credentials");
            }
            Credentials credentials = mapper.readValue(configFile, Credentials.class);
            return new UserCredentials(credentials.userId, credentials.password, "", "", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected org.openecomp.sdc.ci.tests.datatypes.Configuration getEnvConfiguration() {

        try {
            String envUrl = System.getProperty("ENV_URL");
            boolean isCustomLogin = Boolean.valueOf(System.getProperty("CUSTOM_LOGIN"));
            Configuration configuration = new org.openecomp.sdc.ci.tests.datatypes.Configuration(envUrl, isCustomLogin);
            //configuration.setBrowser("chorme");
            return configuration;
            //return new org.openecomp.sdc.ci.tests.datatypes.Configuration(envUrl, isCustomLogin);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void loginToLocalSimulator(UserCredentials userCredentials) {
        LoginExternalPage.performLoginExternal(userCredentials);
    }

    protected void relogin(Credentials credentials) throws Exception {
        if (!credentials.userId.equals(currentUserId)) {
            currentUserId = credentials.userId;
            UserCredentials userCredentials = new UserCredentials(credentials.userId,
                    credentials.password, "", "", "");
            reloginWithNewRole(userCredentials);
        }
    }

    /**
     * Validates that permitted options are enabled and others are disabled.
     *
     * @param permittedItems           the list of permitted items.
     * @param dropdownOptionsClassName the class name of the specific dropdown options.
     * @return true, if all dropdown options disabled state is according to the permissions.
     */
    protected void assertDropdownPermittedItemsByValue(ArrayList<String> permittedItems, String dropdownOptionsClassName) {
        GeneralUIUtils.ultimateWait();
        List<WebElement> optionsList =
                GeneralUIUtils.getWebElementsListBy(By.className(dropdownOptionsClassName), 30);
        for (WebElement option :
                optionsList) {
            String optionValue = option.getAttribute("value");
            if ((option.isEnabled() && !permittedItems.contains(optionValue)) ||
                    !option.isEnabled() && permittedItems.contains(optionValue)) {
                fail(Constants.DROPDOWN_PERMITTED_ASSERT_FAIL_MESSAGE);
            }
        }
    }

    protected void assertAllIsPermitted(String dropdownOptionsClassName) {
        GeneralUIUtils.ultimateWait();
        List<WebElement> optionsList =
                GeneralUIUtils.getWebElementsListBy(By.className(dropdownOptionsClassName), 30);
        for (WebElement option :
                optionsList) {
            String optionValue = option.getAttribute("value");
            if (!option.isEnabled()) {
                fail(Constants.DROPDOWN_PERMITTED_ASSERT_FAIL_MESSAGE);
            }
        }
    }

    protected void assertDropdownPermittedItemsByName(ArrayList<String> permittedItems, String dropdownOptionsClassName) {
        GeneralUIUtils.ultimateWait();
        List<WebElement> optionsList =
                GeneralUIUtils.getWebElementsListBy(By.className(dropdownOptionsClassName), 30);
        for (WebElement option :
                optionsList) {
            String optionText = option.getText();
            if ((option.isEnabled() && !permittedItems.contains(optionText)) ||
                    !option.isEnabled() && permittedItems.contains(optionText)) {
                fail(Constants.DROPDOWN_PERMITTED_ASSERT_FAIL_MESSAGE);
            }
        }
    }

    protected void assertViewEditButtonState(String expectedButtonText, String UUID) {
        WebElement viewEditWebElement = GeneralUIUtils.getWebElementByTestID(Constants.VIEW_EDIT_TEST_ID_PREFIX + UUID, 100);
        Assert.assertEquals(expectedButtonText, viewEditWebElement.getText());
        GeneralUIUtils.ultimateWait();
    }

    protected void addVNF(String name, String lcpRegion, String tenant, String suppressRollback,
                          String legacyRegion, String productFamily, ArrayList<String> permittedTenants) throws InterruptedException {
        ViewEditPage viewEditPage = new ViewEditPage();

        viewEditPage.selectNodeInstanceToAdd(name);
        viewEditPage.generateAndSetInstanceName(Constants.ViewEdit.VNF_INSTANCE_NAME_PREFIX);
        viewEditPage.selectProductFamily(productFamily);
        viewEditPage.selectLCPRegion(lcpRegion);

        assertDropdownPermittedItemsByValue(permittedTenants, Constants.ViewEdit.TENANT_OPTION_CLASS);
        viewEditPage.selectTenant(tenant);

        Click.onFirstSelectOptionById(Constants.OwningEntity.PLATFORM_SELECT_TEST_ID);
        SelectOption.selectFirstTwoOptionsFromMultiselectById(Constants.OwningEntity.LOB_SELECT_TEST_ID);

        viewEditPage.selectSuppressRollback(suppressRollback);

        viewEditPage.setLegacyRegion(legacyRegion);

        viewEditPage.clickConfirmButton();
        assertSuccessfulVNFCreation();
        viewEditPage.clickCloseButton();
        GeneralUIUtils.ultimateWait();
    }

    protected void addVFModule(String name, String lcpRegion, String tenant, String suppressRollback,
                               String legacyRegion, ArrayList<String> permittedTenants) {
        ViewEditPage viewEditPage = new ViewEditPage();

        viewEditPage.selectVolumeGroupToAdd(name);
        viewEditPage.generateAndSetInstanceName(Constants.ViewEdit.VF_MODULE_INSTANCE_NAME_PREFIX);
        viewEditPage.selectLCPRegion(lcpRegion);

        assertDropdownPermittedItemsByValue(permittedTenants, Constants.ViewEdit.TENANT_OPTION_CLASS);
        viewEditPage.selectTenant(tenant);

        viewEditPage.selectSuppressRollback(suppressRollback);

        viewEditPage.setLegacyRegion(legacyRegion);

        viewEditPage.clickConfirmButton();
        assertSuccessfulVFModuleCreation();
        viewEditPage.clickCloseButton();
        GeneralUIUtils.ultimateWait();
    }

    protected void addVolumeGroup(String name, String lcpRegion, String tenant, String suppressRollback,
                                  String legacyRegion, ArrayList<String> permittedTenants) {
        ViewEditPage viewEditPage = new ViewEditPage();

        viewEditPage.selectVolumeGroupToAdd(name);
        viewEditPage.generateAndSetInstanceName(Constants.ViewEdit.VOLUME_GROUP_INSTANCE_NAME_PREFIX);
        viewEditPage.selectLCPRegion(lcpRegion);

        assertDropdownPermittedItemsByValue(permittedTenants, Constants.ViewEdit.TENANT_OPTION_CLASS);
        viewEditPage.selectTenant(tenant);

        viewEditPage.selectSuppressRollback(suppressRollback);

        viewEditPage.setLegacyRegion(legacyRegion);

        viewEditPage.clickConfirmButton();
        assertSuccessfulVolumeGroupCreation();
        viewEditPage.clickCloseButton();
        GeneralUIUtils.ultimateWait();
    }

    void assertSuccessfulVNFCreation() {
        boolean byText = GeneralUIUtils.findAndWaitByText(Constants.ViewEdit.VNF_CREATED_SUCCESSFULLY_TEXT, 100);
        Assert.assertTrue(Constants.ViewEdit.VNF_CREATION_FAILED_MESSAGE, byText);
    }

    void assertSuccessfulPNFAssociation() {
        //TODO
        boolean byText = GeneralUIUtils.findAndWaitByText(Constants.PnfAssociation.PNF_ASSOCIATED_SUCCESSFULLY_TEXT, 100);
        Assert.assertTrue(Constants.PnfAssociation.PNF_ASSOCIATED_FAILED_MESSAGE, byText);
    }
    void assertSuccessfulVolumeGroupCreation() {
        boolean byText = GeneralUIUtils.findAndWaitByText(Constants.ViewEdit.VOLUME_GROUP_CREATED_SUCCESSFULLY_TEXT, 100);
        Assert.assertTrue(Constants.ViewEdit.VOLUME_GROUP_CREATION_FAILED_MESSAGE, byText);
    }

    void assertSuccessfulVFModuleCreation() {
        boolean byText = GeneralUIUtils.findAndWaitByText(Constants.ViewEdit.VF_MODULE_CREATED_SUCCESSFULLY_TEXT, 100);
        Assert.assertTrue(Constants.ViewEdit.VF_MODULE_CREATION_FAILED_MESSAGE, byText);
    }

    void goToExistingInstanceById(String instanceUUID) {
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        searchExistingPage.searchForInstanceByUuid(instanceUUID);
        assertViewEditButtonState( Constants.VIEW_EDIT_BUTTON_TEXT, instanceUUID);
        searchExistingPage.clickEditViewByInstanceId(instanceUUID);
    }

    void goToExistingInstanceByName(String instanceName) {
        SearchExistingPage searchExistingPage = new SearchExistingPage();
        SideMenu.navigateToSearchExistingPage();
        searchExistingPage.searchForInstanceByName(instanceName);
        WebElement instanceIdRow = GeneralUIUtils.getWebElementByTestID(Constants.INSTANCE_ID_FOR_NAME_TEST_ID_PREFIX + instanceName, 30);
        String instanceId = instanceIdRow.getText();
        assertViewEditButtonState( Constants.VIEW_EDIT_BUTTON_TEXT, instanceId);
        searchExistingPage.clickEditViewByInstanceId(instanceId);
    }

}
