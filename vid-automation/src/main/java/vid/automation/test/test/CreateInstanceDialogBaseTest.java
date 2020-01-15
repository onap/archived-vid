package vid.automation.test.test;

import java.util.ArrayList;
import org.junit.Assert;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.Exists;
import vid.automation.test.infra.SelectOption;
import vid.automation.test.model.Service;
import vid.automation.test.model.ServiceModel;
import vid.automation.test.sections.ViewEditPage;

public class CreateInstanceDialogBaseTest extends VidBaseTestCase {

    protected ViewEditPage viewEditPage= new ViewEditPage();

    void assertServiceMetadata(String expectedMetadata, String actualMetadata) {
        WebElement serviceNameElem = GeneralUIUtils.getWebElementByTestID(actualMetadata);
        String actualServiceName = serviceNameElem.getText();
        Assert.assertEquals(expectedMetadata, actualServiceName);
    }

    void validateServiceCreationDialog(Service expectedService) {
        assertThatServiceCreationDialogIsVisible();
        assertServiceMetadata(expectedService.name, Constants.SERVICE_NAME);
        assertServiceMetadata(expectedService.uuid, Constants.SERVICE_UUID);
        assertServiceMetadata(expectedService.invariantUuid, Constants.SERVICE_INVARIANT_UUID);
        assertServiceMetadata(expectedService.category, Constants.SERVICE_CATEGORY);
        assertServiceMetadata(expectedService.version, Constants.SERVICE_VERSION);
        assertServiceMetadata(expectedService.description, Constants.SERVICE_DESCRIPTION);
    }

    public void assertThatServiceCreationDialogIsVisible() {
        boolean byText = GeneralUIUtils.findAndWaitByText(Constants.BrowseASDC.CREATE_SERVICE_INSTANCE, 15);
        Assert.assertTrue(byText);
    }

    void assertSuccessfulServiceInstanceCreation() {
        boolean byText = GeneralUIUtils.findAndWaitByText(Constants.BrowseASDC.SERVICE_INSTANCE_CREATED_SUCCESSFULLY_TEXT, 100);
        Assert.assertTrue(Constants.BrowseASDC.SERVICE_INSTANCE_CREATION_FAILED_MESSAGE, byText);
    }

    void validateVnfCreationDialog(ServiceModel expectedServiceInstance) {
        GeneralUIUtils.ultimateWait();
        validateNodeTemplateCreationDialog(expectedServiceInstance);
        assertServiceMetadata(expectedServiceInstance.resourceName, Constants.CreateNewInstance.RESOURCE_NAME);
        assertServiceMetadata(expectedServiceInstance.nfType, Constants.CreateNewInstance.NF_TYPE);
        assertServiceMetadata(expectedServiceInstance.nfRole, Constants.CreateNewInstance.NF_ROLE);
        assertServiceMetadata(expectedServiceInstance.nfFunction, Constants.CreateNewInstance.NF_FUNCTION);
        assertServiceMetadata(expectedServiceInstance.nfNamingCode, Constants.CreateNewInstance.NF_NAMING_CODE);
    }

    void validateNodeTemplateCreationDialog(ServiceModel expectedServiceInstance){
        GeneralUIUtils.ultimateWait();
        assertServiceMetadata(expectedServiceInstance.serviceName, Constants.SERVICE_NAME);
        assertServiceMetadata(expectedServiceInstance.subscriberName, Constants.SUBSCRIBER_NAME);
        assertServiceMetadata(expectedServiceInstance.serviceInstanceName, Constants.SERVICE_INSTANCE_NAME);
        assertServiceMetadata(expectedServiceInstance.modelName, Constants.CreateNewInstance.MODEL_NAME);
        assertServiceMetadata(expectedServiceInstance.modelInvariantUuid, Constants.CreateNewInstance.MODEL_INVARIANT_UUID);
        assertServiceMetadata(expectedServiceInstance.modelVersion, Constants.CreateNewInstance.MODEL_VERSION);
        assertServiceMetadata(expectedServiceInstance.modelUuid, Constants.CreateNewInstance.MODEL_UUID);
        assertServiceMetadata(expectedServiceInstance.modelCustomizationUuid, Constants.CreateNewInstance.MODEL_CUSTOMIZATION_UUID);
    }

    private String fillDetailsInDialog(String name, String lcpRegion, String cloudOwner, String tenant, String suppressRollback,
                      String legacyRegion, ArrayList<String> permittedTenants)
    {
        viewEditPage.setInstanceName(name);
        viewEditPage.selectLcpRegion(lcpRegion, cloudOwner);
        assertDropdownPermittedItemsByValue(permittedTenants, Constants.ViewEdit.TENANT_OPTION_CLASS);
        viewEditPage.selectTenant(tenant);
        viewEditPage.selectSuppressRollback(suppressRollback);
        viewEditPage.setLegacyRegion(legacyRegion);
        return name;
    }


    protected String addVFModule(String modelName, String instanceName, String lcpRegion, String cloudOwner, String tenant, String suppressRollback,
                                 String legacyRegion, ArrayList<String> permittedTenants,ServiceModel expectedServiceInstance) {
        viewEditPage.selectVfModuleToAdd(modelName);
        if(expectedServiceInstance != null) {
            validateNodeTemplateCreationDialog(expectedServiceInstance);
        }
        fillDetailsInDialog(instanceName, lcpRegion, cloudOwner, tenant, suppressRollback, legacyRegion, permittedTenants);
        viewEditPage.clickConfirmButton();
        assertSuccessfulVFModuleCreation();
        viewEditPage.clickCloseButton();
        GeneralUIUtils.ultimateWait();
        return instanceName;
    }

    protected void addVolumeGroup (String modelName, String instanceName, String lcpRegion, String cloudOwner, String tenant, String suppressRollback,
                                  String legacyRegion, ArrayList<String> permittedTenants, ServiceModel expectedServiceInstance) {
        viewEditPage.selectVolumeGroupToAdd(modelName);
        if(expectedServiceInstance != null) {
            validateNodeTemplateCreationDialog(expectedServiceInstance);
        }
        fillDetailsInDialog(instanceName, lcpRegion, cloudOwner, tenant, suppressRollback, legacyRegion, permittedTenants);
        viewEditPage.clickConfirmButton();
        assertSuccessfulVolumeGroupCreation();
        viewEditPage.clickCloseButton();
        GeneralUIUtils.ultimateWait();
    }

    protected void addVNF(String name, String lcpRegion, String cloudOwner, String tenant, String suppressRollback,
                          String legacyRegion, String productFamily, String platform, ArrayList<String> permittedTenants, String lobs, ServiceModel serviceModel, String instanceName) {
        openAndFillVnfPopup(name, lcpRegion, cloudOwner, tenant, suppressRollback, legacyRegion, productFamily, platform,permittedTenants, lobs, serviceModel, instanceName);
        viewEditPage.clickConfirmButton();
        successConfirmVnfAndClosePopup();

    }

    protected void openAndFillVnfPopup(String name, String lcpRegion, String cloudOwner, String tenant, String suppressRollback,
                                       String legacyRegion, String productFamily, String platform, ArrayList<String> permittedTenants, String lobs, ServiceModel serviceModel, String instanceName) {
        viewEditPage.selectNodeInstanceToAdd(name);
        GeneralUIUtils.ultimateWait();
        if(serviceModel != null) {
            validateVnfCreationDialog(serviceModel);
        }
        viewEditPage.selectProductFamily(productFamily);
        if(platform != null) {
            SelectOption.byTestIdAndVisibleText(platform, Constants.OwningEntity.PLATFORM_SELECT_TEST_ID);
        }
        else {
            Click.onFirstSelectOptionById(Constants.OwningEntity.PLATFORM_SELECT_TEST_ID);
        }
        if(lobs != null) {
            viewEditPage.selectLineOfBusiness(lobs);
        }
        fillDetailsInDialog(instanceName, lcpRegion, cloudOwner, tenant, suppressRollback, legacyRegion, permittedTenants);
    }

    protected void successConfirmVnfAndClosePopup() {
        assertSuccessfulVNFCreation();
        viewEditPage.clickCloseButton();
        GeneralUIUtils.ultimateWait();
    }

    protected void assertConfirmShowMissingDataErrorOnCurrentPopup(String field) {
        viewEditPage.clickConfirmButton();
        boolean byclassAndText = Exists.byClassAndText(Constants.ViewEdit.ERROR_CLASS, String.format(Constants.MISSING_DATA, field));
        Assert.assertTrue(field + " " + Constants.REQUIRED, byclassAndText);
    }

}
