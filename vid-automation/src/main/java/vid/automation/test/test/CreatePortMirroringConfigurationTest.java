package vid.automation.test.test;

import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Exists;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.Wait;
import vid.automation.test.sections.CreateConfigurationPage;
import vid.automation.test.sections.ServiceProxyPage;
import vid.automation.test.sections.VidBasePage;
import vid.automation.test.sections.ViewEditPage;
import vid.automation.test.services.BulkRegistration;
import vid.automation.test.services.SimulatorApi;

import java.util.HashMap;
import java.util.Map;

public class CreatePortMirroringConfigurationTest extends VidBaseTestCase {

    private ViewEditPage viewEditPage = new ViewEditPage();
    private CreateConfigurationPage createConfigurationPage = new CreateConfigurationPage();
    private ServiceProxyPage serviceProxyPage = new ServiceProxyPage();
    private String serviceInstanceId = "c187e9fe-40c3-4862-b73e-84ff056205f6";
    private String serviceInstanceId_vidTest444 = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
    private String policyConfigurationModelName_0 = "Port Mirroring Configuration 0";
    private String policyConfigurationModelName_1 = "Port Mirroring Configuration By Policy 1";
    private String pnfInstanceName = "AS-pnf2-10219--as988q";
    private String pnfServiceType = "DARREN MCGEE";
    private String vnfServiceType = "TYLER SILVIA";
    private String sourceSubscriberName = "SILVIA ROBBINS";
    private String defaultCollectorServiceType = "TYLER SILVIA";
    private String vnfInstanceName = "zhvf6aepdg01";
    private String active = "Active";
    private String desiredCloudRegionId = "mdt1";


    private boolean featureFlagLetsSelectingCollector() {
        return Features.FLAG_1810_CR_LET_SELECTING_COLLECTOR_TYPE_UNCONDITIONALLY.isActive();
    }

    private boolean featureFlagLetSelectingSourceSubscriber() {
        return Features.FLAG_2006_PORT_MIRRORING_LET_SELECTING_SOURCE_SUBSCRIBER.isActive();
    }

    private String expectedPnfCollectorServiceType() {
        return (featureFlagLetsSelectingCollector() ? pnfServiceType : defaultCollectorServiceType).replace(" ", "%20");
    }

    @Test
    public void testCreatePolicyConfiguration() {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstancePortMirroring("Active", "mdt1");
        BulkRegistration.getNetworkNodeFormData();
        BulkRegistration.createPolicyConfiguration(true, expectedPnfCollectorServiceType());

        navigateToViewEditPageOf_test_sssdad();
        selectConfigurationNode(policyConfigurationModelName_1, getConfigurationExpectedMetadata());
        fillAllFormFields();
        createConfigurationPage.clickNextButton();
        serviceProxyPage.assertButtonState(Constants.ConfigurationCreation.CREATE_BUTTON_TEST_ID,false);
        //test back button
        createConfigurationPage.clickBackButton();
        assertFormFields();
        createConfigurationPage.clickNextButton();

        //assert service proxy models (circles) names
        serviceProxyPage.assertSourceModelName("vflorenceService2 Service Proxy");
        serviceProxyPage.assertCollectorModelName("pProbeService Service Proxy");

        //assert service proxy models metadata
        assertMetadataModal(Constants.ConfigurationCreation.SOURCE_INFO_BUTTON_TEST_ID, getSourceServiceProxyExpectedMetadata());
        assertMetadataModal(Constants.ConfigurationCreation.COLLECTOR_INFO_BUTTON_TEST_ID, getCollectorServiceProxyExpectedMetadata());

        //select source & collector
        if (featureFlagLetsSelectingCollector()) {
            serviceProxyPage.assertCollectorServiceType(defaultCollectorServiceType);
            serviceProxyPage.chooseCollectorServiceType(pnfServiceType);
        }
        serviceProxyPage.chooseCollector(pnfInstanceName);
        serviceProxyPage.assertSelectedInstanceIcon(Constants.ConfigurationCreation.COLLECTOR_INSTANCE_SELECTED_ICON_TEST_ID);

        serviceProxyPage.chooseSourceServiceType(vnfServiceType);
        serviceProxyPage.chooseSource(vnfInstanceName);
        serviceProxyPage.assertSelectedInstanceIcon(Constants.ConfigurationCreation.SOURCE_INSTANCE_SELECTED_ICON_TEST_ID);

        serviceProxyPage.assertButtonState(Constants.ConfigurationCreation.CREATE_BUTTON_TEST_ID,true);
        serviceProxyPage.clickCreateButton();
        serviceProxyPage.assertButtonStateEvenIfButtonNotVisible(Constants.ConfigurationCreation.CREATE_BUTTON_TEST_ID,false);
        serviceProxyPage.assertMsoRequestModal(Constants.ViewEdit.MSO_SUCCESSFULLY_TEXT);
        serviceProxyPage.clickCloseButton();

        //assert redirect back to view/edit
        GeneralUIUtils.ultimateWait();
        Assert.assertTrue(Exists.byTestId(Constants.ViewEdit.ADD_VNF_BUTTON_TEST_ID));
    }


    @Test
    public void testDeletePolicyConfiguration() {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstancePortMirroring("Created", desiredCloudRegionId);
        BulkRegistration.getNetworkNodeFormData();
        BulkRegistration.deletePolicyConfiguration(true);
        navigateToViewEditPageOf_test_sssdad();
        serviceProxyPage.clickDeleteConfigurationButton();
        serviceProxyPage.assertMsoRequestModal(Constants.ViewEdit.MSO_SUCCESSFULLY_TEXT);
    }

    @Test
    public void testConfigurationCreatedPortEnabled(){
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstancePortMirroring("Created", desiredCloudRegionId);
        BulkRegistration.getNetworkNodeFormData();
        BulkRegistration.activateDeactivateConfiguration("deactivate");
        navigateToViewEditPageOf_test_sssdad();
        WebElement isPortEnableButtonExists = Get.byTestId("enableDisableButton");
        Assert.assertNull(isPortEnableButtonExists);
    }

    @Test
    public void testDisablePort() {
        enableDisablePortPresets(active, true);
        BulkRegistration.enableDisablePort("disablePort");
        navigateToViewEditPageOf_test_sssdad();
        serviceProxyPage.clickEnableDisableButton();
        serviceProxyPage.assertMsoRequestModal(Constants.ViewEdit.MSO_SUCCESSFULLY_TEXT);
    }

    @Test
    public void testEnablePort() {
        enableDisablePortPresets(active, false);
        BulkRegistration.enableDisablePort("enablePort");
        navigateToViewEditPageOf_test_sssdad();
        serviceProxyPage.clickEnableDisableButton();
        serviceProxyPage.assertMsoRequestModal(Constants.ViewEdit.MSO_SUCCESSFULLY_TEXT);
    }




    private void enableDisablePortPresets(String orchStatus, boolean isMirrored){
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstancePortMirroring(orchStatus, isMirrored, desiredCloudRegionId);
        BulkRegistration.getNetworkNodeFormData();
    }



    @Test
    public void testActivateConfigurationTest(){
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstancePortMirroring("Created", desiredCloudRegionId);
        BulkRegistration.getNetworkNodeFormData();
        BulkRegistration.activateDeactivateConfiguration("activate");
        navigateToViewEditPageOf_test_sssdad();
        serviceProxyPage.assertDeleteConfigurationButtonExists(true);
        serviceProxyPage.clickActivateDeactivateButton();
        serviceProxyPage.assertMsoRequestModal(Constants.ViewEdit.MSO_SUCCESSFULLY_TEXT);
    }


    @Test
    public void testDeleteConfigurationTest(){
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstancePortMirroring("Created", desiredCloudRegionId);
        BulkRegistration.getNetworkNodeFormData();
        BulkRegistration.deleteConfiguration();
        navigateToViewEditPageOf_test_sssdad();
        serviceProxyPage.clickDeleteConfigurationButton();
        serviceProxyPage.assertMsoRequestModal(Constants.ViewEdit.MSO_SUCCESSFULLY_TEXT);
    }


    @Test
    public void testDeactivateConfigurationTest(){
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstancePortMirroring("Active", desiredCloudRegionId);
        BulkRegistration.getNetworkNodeFormData();
        BulkRegistration.activateDeactivateConfiguration("deactivate");
        navigateToViewEditPageOf_test_sssdad();
        serviceProxyPage.assertDeleteConfigurationButtonExists(false);
        serviceProxyPage.clickActivateDeactivateButton();
        serviceProxyPage.assertMsoRequestModal(Constants.ViewEdit.MSO_SUCCESSFULLY_TEXT);

    }


    @Test
    public void testCreatePortMirroringConfiguration() {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstance();
        BulkRegistration.getNetworkNodeFormData();
        BulkRegistration.createPolicyConfiguration(true, expectedPnfCollectorServiceType());
        BulkRegistration.createConfiguration("model-version-id=7482279e-5901-492f-a963-6331aa6b995e&model-invariant-id=f2ae9911-95c4-40d0-8908-0175c206ab2d");

        navigateToViewEditPageOfuspVoiceVidTest444("240376de-870e-48df-915a-31f140eedd2c");
        selectConfigurationNode(policyConfigurationModelName_0, ImmutableMap.<String, String>builder()
                .put(Constants.ServiceModelInfo.SERVIICE_NAME_KEY, "Demo Service 1")
                .put(Constants.ServiceModelInfo.MODEL_NAME, "Port Mirroring Configuration")
                .put(Constants.ServiceModelInfo.SERVICE_INSTANCE_NAME, "vid-test-444")
                .put(Constants.ServiceModelInfo.MODEL_INVARIANT_UUID, "5dd839fa-5e09-47d4-aa5c-5ba62161b569")
                .put(Constants.ServiceModelInfo.SUBSCRIBER_NAME_KEY, "SILVIA ROBBINS")
                .put(Constants.ServiceModelInfo.MODEL_VERSION, "1.0")
                .put(Constants.ServiceModelInfo.MODEL_UUID, "9d6b09b1-7527-49b1-b6cf-398cb67c5523")
                .put(Constants.ServiceModelInfo.MODEL_CUSTOMIZATION_UUID, "3db39baa-35bc-4b97-b199-44e758823502")
                .put(Constants.ServiceModelInfo.RESOURCE_NAME, policyConfigurationModelName_0)
                .build());
        fillAllFormFields();
        createConfigurationPage.clickNextButton();
        serviceProxyPage.assertButtonState(Constants.ConfigurationCreation.CREATE_BUTTON_TEST_ID,false);
        //test back button
        createConfigurationPage.clickBackButton();
        assertFormFields();
        createConfigurationPage.clickNextButton();

        //assert service proxy models (circles) names
        serviceProxyPage.assertSourceModelName("Service 1 Service Proxy");
        serviceProxyPage.assertCollectorModelName("Service 1 Service Proxy");

        //assert service proxy models metadata
        final ImmutableMap<String, String> expectedMetadata = ImmutableMap.<String, String>builder()
                .put(Constants.ServiceProxyModelInfo.MODEL_NAME, "Service 1 Service Proxy")
                .put(Constants.ServiceProxyModelInfo.MODEL_VERSION, "2.0")
                .put(Constants.ServiceProxyModelInfo.MODEL_DESCRIPTION, "A Proxy for Service Service 1")
                .put(Constants.ServiceProxyModelInfo.MODEL_TYPE, "Service Proxy")
                .put(Constants.ServiceProxyModelInfo.MODEL_INVARIANT_UUID, "0aaefad3-9409-4ab1-be00-a1571e8a0545")
                .put(Constants.ServiceProxyModelInfo.MODEL_UUID, "8685fd6a-c0b1-40f7-be94-ab232e4424c1")
                .put(Constants.ServiceProxyModelInfo.SOURCE_MODEL_UUID, "7482279e-5901-492f-a963-6331aa6b995e")
                .put(Constants.ServiceProxyModelInfo.SOURCE_MODEL_INVARIANT, "f2ae9911-95c4-40d0-8908-0175c206ab2d")
                .put(Constants.ServiceProxyModelInfo.SOURCE_MODEL_NAME, "Service 1")
                .build();
        assertMetadataModal(Constants.ConfigurationCreation.SOURCE_INFO_BUTTON_TEST_ID, expectedMetadata);
        assertMetadataModal(Constants.ConfigurationCreation.COLLECTOR_INFO_BUTTON_TEST_ID, expectedMetadata);

        //select source & collector
        serviceProxyPage.assertCollectorServiceType(defaultCollectorServiceType);
        if(featureFlagLetSelectingSourceSubscriber()){
            serviceProxyPage.assertSourceSubscriberName(sourceSubscriberName);
            serviceProxyPage.chooseSourceSubscriberName(sourceSubscriberName);
        }
        serviceProxyPage.chooseCollectorServiceType(vnfServiceType);
        serviceProxyPage.chooseCollector(vnfInstanceName);
        serviceProxyPage.assertSelectedInstanceIcon(Constants.ConfigurationCreation.COLLECTOR_INSTANCE_SELECTED_ICON_TEST_ID);

        serviceProxyPage.chooseSourceServiceType(vnfServiceType);
        serviceProxyPage.chooseSource(vnfInstanceName);
        serviceProxyPage.assertSelectedInstanceIcon(Constants.ConfigurationCreation.SOURCE_INSTANCE_SELECTED_ICON_TEST_ID);

        serviceProxyPage.assertButtonState(Constants.ConfigurationCreation.CREATE_BUTTON_TEST_ID,true);
    }

    @Test
    public void testRainyCreatePolicyConfiguration() {

        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstancePortMirroring("Active", desiredCloudRegionId);
        BulkRegistration.getNetworkNodeFormData();
        BulkRegistration.createPolicyConfiguration(false, expectedPnfCollectorServiceType());

        navigateToViewEditPageOf_test_sssdad();
        selectConfigurationNode(policyConfigurationModelName_1, getConfigurationExpectedMetadata());
        fillAllFormFields();
        createConfigurationPage.clickNextButton();
        serviceProxyPage.assertButtonState(Constants.ConfigurationCreation.CREATE_BUTTON_TEST_ID,false);

        //select source & collector
        if (featureFlagLetsSelectingCollector()) {
            serviceProxyPage.assertCollectorServiceType(defaultCollectorServiceType);
            serviceProxyPage.chooseCollectorServiceType(pnfServiceType);
        }
        serviceProxyPage.chooseCollector(pnfInstanceName);
        serviceProxyPage.chooseSourceServiceType(vnfServiceType);
        serviceProxyPage.chooseSource(vnfInstanceName);
        serviceProxyPage.clickCreateButton();
        serviceProxyPage.assertMsoRequestModal("Error");
        serviceProxyPage.clickCloseButton();
        serviceProxyPage.assertButtonState(Constants.ConfigurationCreation.CREATE_BUTTON_TEST_ID,true);
    }

    @Test
    public void testRainyNoResultsInDropdowns(){
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingServiceInstancePortMirroring("Active", desiredCloudRegionId);
        BulkRegistration.getNetworkNodeFormData();
       //not register createPolicyConfiguration for no results in DDLs

        navigateToViewEditPageOf_test_sssdad();
        selectConfigurationNode(policyConfigurationModelName_1, getConfigurationExpectedMetadata());
        fillAllFormFields();
        createConfigurationPage.clickNextButton();
        serviceProxyPage.assertButtonState(Constants.ConfigurationCreation.CREATE_BUTTON_TEST_ID,false);
        //source & collector should be empty
        serviceProxyPage.chooseSourceServiceType(vnfServiceType);
        serviceProxyPage.noOptionDropdownByTestId(Constants.ConfigurationCreation.SOURCE_DROPDOWN_TEST_ID);
        serviceProxyPage.noOptionDropdownByTestId(Constants.ConfigurationCreation.COLLECTOR_DROPDOWN_TEST_ID);
        //error message no instance found
        serviceProxyPage.assertNoResultRequirementsDropDown(Constants.ConfigurationCreation.COLLECTOR_NO_RESULT_MSG_TEST_ID,"pnf");
        serviceProxyPage.assertNoResultRequirementsDropDown(Constants.ConfigurationCreation.SOURCE_NO_RESULT_MSG_TEST_ID,"vnf");
    }

    private void selectConfigurationNode(String name, Map<String, String> configurationExpectedMetadata){
        viewEditPage.selectNodeInstanceToAdd(name);
        assertModelInfo(configurationExpectedMetadata,true);
        createConfigurationPage.assertButtonState(Constants.ConfigurationCreation.NEXT_BUTTON_TEST_ID, false);
    }


    public void assertMetadataModal(String btnTestId, Map<String, String> expectedMetadata) {
        serviceProxyPage.clickInfoButton(btnTestId);
        assertModelInfo(expectedMetadata, true);
        serviceProxyPage.clickCloseButton();
        Wait.modalToDisappear();
    }

    private void fillAllFormFields() {
        createConfigurationPage.setInstanceName("dummy_instance");
        createConfigurationPage.assertButtonState(Constants.ConfigurationCreation.TENANT_DROPDOWN_TEST_ID,false);
        VidBasePage vidBasePage = new VidBasePage();
        vidBasePage.selectLcpRegion("AAIAIC25", "AIC");
        GeneralUIUtils.ultimateWait();
        createConfigurationPage.chooseTenant("USP-SIP-IC-24335-T-01");
        createConfigurationPage.assertButtonState(Constants.ConfigurationCreation.NEXT_BUTTON_TEST_ID,true);
    }
    private void assertFormFields() {
        Assert.assertEquals("dummy_instance",createConfigurationPage.getInstanceName());
        Assert.assertEquals("AAIAIC25 (AIC)", createConfigurationPage.getRegion());
        Assert.assertEquals("USP-SIP-IC-24335-T-01",createConfigurationPage.getTenant());

        createConfigurationPage.assertButtonState(Constants.ConfigurationCreation.NEXT_BUTTON_TEST_ID,true);
    }

    private Map<String, String> getConfigurationExpectedMetadata() {
        return new HashMap<String, String>(){
            {
                put(Constants.ServiceModelInfo.SERVIICE_NAME_KEY, "ServiceContainerMultiplepProbes");
                put(Constants.ServiceModelInfo.MODEL_NAME, "Port Mirroring Configuration By Policy");
                put(Constants.ServiceModelInfo.SERVICE_INSTANCE_NAME, "test_sssdad");
                put(Constants.ServiceModelInfo.MODEL_INVARIANT_UUID, "c30a024e-a6c6-4670-b73c-3df64eb57ff6");
                put(Constants.ServiceModelInfo.SUBSCRIBER_NAME_KEY, "SILVIA ROBBINS");
                put(Constants.ServiceModelInfo.MODEL_VERSION, "1.0");
                put(Constants.ServiceModelInfo.MODEL_UUID, "f58d039d-4cfc-40ec-bd75-1f05f0458a6c");
                put(Constants.ServiceModelInfo.MODEL_CUSTOMIZATION_UUID, "4b7ebace-bad6-4526-9be6-bf248e20fc5f");
                put(Constants.ServiceModelInfo.RESOURCE_NAME, policyConfigurationModelName_1);
            }
        };
    }

    private Map<String, String> getSourceServiceProxyExpectedMetadata() {
        return new HashMap<String, String>(){
            {
                put(Constants.ServiceProxyModelInfo.MODEL_NAME, "vflorenceService2 Service Proxy");
                put(Constants.ServiceProxyModelInfo.MODEL_VERSION, "1.0");
                put(Constants.ServiceProxyModelInfo.MODEL_DESCRIPTION, "A Proxy for Service vflorenceService2");
                put(Constants.ServiceProxyModelInfo.MODEL_TYPE, "Service Proxy");
                put(Constants.ServiceProxyModelInfo.MODEL_INVARIANT_UUID, "2933b574-d28d-45ea-bf22-4df2907e4a10");
                put(Constants.ServiceProxyModelInfo.MODEL_UUID, "a32fee17-5b59-4c34-ba6f-6dd2f1c61fee");
                put(Constants.ServiceProxyModelInfo.MODEL_CUSTOMIZATION_UUID, "060be63d-5f9c-4fd0-8ef7-830d5e8eca17");
                put(Constants.ServiceProxyModelInfo.SOURCE_MODEL_UUID, "2a2ea15f-07c6-4b89-bfca-e8aba39a34d6");
                put(Constants.ServiceProxyModelInfo.SOURCE_MODEL_INVARIANT, "a7eac2b3-8444-40ee-92e3-b3359b32445c");
                put(Constants.ServiceProxyModelInfo.SOURCE_MODEL_NAME, "vflorenceService2");
            }
        };
    }

    private Map<String, String> getCollectorServiceProxyExpectedMetadata() {
        return new HashMap<String, String>(){
            {
                put(Constants.ServiceProxyModelInfo.MODEL_NAME, "pProbeService Service Proxy");
                put(Constants.ServiceProxyModelInfo.MODEL_VERSION, "1.0");
                put(Constants.ServiceProxyModelInfo.MODEL_DESCRIPTION, "A Proxy for Service pProbeService");
                put(Constants.ServiceProxyModelInfo.MODEL_TYPE, "Service Proxy");
                put(Constants.ServiceProxyModelInfo.MODEL_INVARIANT_UUID, "2933b574-d28d-45ea-bf22-4df2907e4a10");
                put(Constants.ServiceProxyModelInfo.MODEL_UUID, "a32fee17-5b59-4c34-ba6f-6dd2f1c61fee");
                put(Constants.ServiceProxyModelInfo.MODEL_CUSTOMIZATION_UUID, "d64623ae-5935-4afd-803e-c86e94d8e740");
                put(Constants.ServiceProxyModelInfo.SOURCE_MODEL_UUID, "8a84e59b-45fe-4851-8ff1-34225a0b32c3");
                put(Constants.ServiceProxyModelInfo.SOURCE_MODEL_INVARIANT, "83b458fd-5dd3-419b-a9e3-7335814a0911");
                put(Constants.ServiceProxyModelInfo.SOURCE_MODEL_NAME, "pProbeService");
            }
        };
    }
}
