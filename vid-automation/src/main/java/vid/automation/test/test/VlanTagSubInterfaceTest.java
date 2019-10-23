package vid.automation.test.test;

import com.google.common.collect.ImmutableList;
import org.onap.simulator.presetGenerator.presets.aai.*;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateVnfVlanTagging;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.SelectOption;
import vid.automation.test.sections.SearchExistingPage;
import vid.automation.test.sections.VidBasePage;
import vid.automation.test.services.BulkRegistration;
import vid.automation.test.services.SimulatorApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId.PRESET_AAIAIC25_TO_ATT_AIC;
import static org.testng.AssertJUnit.assertEquals;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;
import static vid.automation.test.test.ALaCarteFlowTest.AIC;

public class VlanTagSubInterfaceTest extends CreateInstanceDialogBaseTest {

    private final String ecompNamingFalseInstanceId = "dc3a893e-0104-4ef6-abfe-6c2932294a3e";
    private final String ecompNamingTrueInstanceId = "dc3a893e-0104-4ef6-abfe-6c2932294a3f";

    @BeforeClass
    protected void registerToSimulator() {
        SimulatorApi.clearAll();
        BulkRegistration.createNewServiceInstance("SILVIA ROBBINS");

        BulkRegistration.searchExistingVFServiceWithVFCInstanceGroupInstance("Created");
        SimulatorApi.registerExpectation(SimulatorApi.RegistrationStrategy.APPEND
                , "add_subinterface/get_sdc_catalog_services_vid-test-333.json"
                , "add_subinterface/aai_get_aic_zones.json"
        );

        final PresetAAIGetInstanceGroupsByCloudRegionInvalidRequest presetAAIGetInstanceGroupsByCloudRegion1 = new PresetAAIGetInstanceGroupsByCloudRegionInvalidRequest("irma-aic", "hvf6", "untraaa");
        final PresetAAIGetInstanceGroupsByCloudRegionInvalidRequest presetAAIGetInstanceGroupsByCloudRegion2 = new PresetAAIGetInstanceGroupsByCloudRegionInvalidRequest("irma-aic", "AAIAIC25", "untraaa");

        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                    new PresetAAIGetServicesGet(),
                    new PresetAAIGetSubscribersGet(),
                    new PresetMSOCreateVnfVlanTagging(ecompNamingTrueInstanceId, "6bce7302-70bd-4057-b48e-8d5b99e686ca", true),
                    new PresetMSOCreateVnfVlanTagging(ecompNamingFalseInstanceId, "6bce7302-70bd-4057-b48e-8d5b99e686cb", false),
                    PRESET_AAIAIC25_TO_ATT_AIC,
                    new PresetAAIGetTenants(),
                    presetAAIGetInstanceGroupsByCloudRegion1,
                    presetAAIGetInstanceGroupsByCloudRegion2,
                new PresetMSOOrchestrationRequestGet("IN_PROGRESS", false),
                new PresetMSOOrchestrationRequestGet("COMPLETE", false)
                ),
                APPEND);

        final PresetAAIGetRelatedInstanceGroupsByVnfId getRelatedInstanceGroupsByVnfId = new PresetAAIGetRelatedInstanceGroupsByVnfId("c015cc0f-0f37-4488-aabf-53795fd93cd3");
        SimulatorApi.registerExpectationFromPreset(getRelatedInstanceGroupsByVnfId, APPEND);

    }

    @DataProvider
    private Object[][] getServices() {
        return new Object[][]{
                {ecompNamingTrueInstanceId},
                {ecompNamingFalseInstanceId}
        };
    }

    @Test(dataProvider = "getServices")
    public void createSubInterface_validPopupDataAndUI(String serviceUuid) {
        SearchExistingPage searchExistingPage = new SearchExistingPage();

        VidBasePage.goOutFromIframe();
        goToExistingInstanceById(serviceUuid);

        String vnfName = "vDOROTHEA 0";
        Assert.assertNotNull(Get.byClassAndText("instanceGroupTreeNode","instance group name"));
        GeneralUIUtils.clickOnElementByTestId(Constants.ViewEdit.ADD_VNF_BUTTON_TEST_ID, 60);
        GeneralUIUtils.clickOnElementByTestId(Constants.ViewEdit.VNF_OPTION_TEST_ID_PREFIX + vnfName, 60);

        GeneralUIUtils.ultimateWait();

        searchExistingPage.goToIframe();

        WebElement instanceNameInput = GeneralUIUtils.getInputElement(Constants.INSTANCE_NAME_SELECT_TESTS_ID);
        instanceNameInput.sendKeys("NewName");
        SelectOption.byTestIdAndVisibleText("TYLER SILVIA", Constants.ViewEdit.PRODUCT_FAMILY_SELECT_TESTS_ID);
        viewEditPage.selectLcpRegion("AAIAIC25", AIC);
        SelectOption.byTestIdAndVisibleText("USP-SIP-IC-24335-T-01", Constants.ViewEdit.TENANT_SELECT_TESTS_ID);
        SelectOption.byTestIdAndVisibleText("UUUAIAAI-YYY1", Constants.ViewEdit.AIC_ZONE_TEST_ID);
        SelectOption.byTestIdAndVisibleText("xxx1", Constants.OwningEntity.PLATFORM_SELECT_TEST_ID);
        SelectOption.byTestIdAndVisibleText("ONAP", Constants.OwningEntity.LOB_SELECT_TEST_ID);
        SelectOption.byTestIdAndVisibleText("Rollback", Constants.SUPPRESS_ROLLBACK_SELECT_TESTS_ID);


        checkModelItemLabelAndValue(Constants.VlanTagging.MODEL_ITEM_LABEL_SERVICE_INSTANCENAME, "Service Instance Name",
                Constants.VlanTagging.MODEL_ITEM_VALUE_SERVICE_INSTANCENAME, "vid-test-444");
        checkModelItemLabelAndValue(Constants.VlanTagging.MODEL_ITEM_LABEL_MODEL_INVARIANT_UUID, "Model Invariant UUID",
                Constants.VlanTagging.MODEL_ITEM_VALUE_MODEL_INVARIANT_UUID, "fcdf49ce-6f0b-4ca2-b676-a484e650e734");
        checkModelItemLabelAndValue(Constants.VlanTagging.MODEL_ITEM_LABEL_MODEL_VERSION, "Model Version",
                Constants.VlanTagging.MODEL_ITEM_VALUE_MODEL_VERSION, "0.2");
        checkModelItemLabelAndValue(Constants.VlanTagging.MODEL_ITEM_LABEL_MODEL_UUID, "Model UUID",
                Constants.VlanTagging.MODEL_ITEM_VALUE_MODEL_UUID, "61535073-2e50-4141-9000-f66fea69b433");
        checkModelItemLabelAndValue(Constants.VlanTagging.MODEL_ITEM_LABEL_CUSTOMIZATION_UUID, "Model Customization UUID",
                Constants.VlanTagging.MODEL_ITEM_VALUE_CUSTOMIZATION_UUID, "882e5dcb-ba9f-4766-8cde-e326638107db");

        assertEquals(Get.multipleElementsByTestId(Constants.VlanTagging.MODEL_ITEM_LABEL_GROUP_NAME).stream()
                .map(WebElement::getText).collect(Collectors.toList()), Arrays.asList("Group Name", "Group Name"));


        List<String> rightSideGroupsNames = //Will be used here AND in step 2 to check against the left side of the page
                Get.multipleElementsByTestId(Constants.VlanTagging.MODEL_ITEM_VALUE_GROUP_NAME).stream()
                        .map(WebElement::getText).collect(Collectors.toList());

        assertEquals(rightSideGroupsNames, Arrays.asList("untr_group", "oam_group"));

        assertEquals(Get.multipleElementsByTestId(Constants.VlanTagging.MODEL_ITEM_LABEL_NETWORK_COLLECTION_FUNCTION)
                        .stream().map(WebElement::getText).collect(Collectors.toList()),
                Arrays.asList("Network Collection Function", "Network Collection Function"));

        assertEquals(Get.multipleElementsByTestId(Constants.VlanTagging.MODEL_ITEM_VALUE_NETWORK_COLLECTION_FUNCTION)
                        .stream().map(WebElement::getText).collect(Collectors.toList()),
                Arrays.asList("untraaa", "untraaa"));

        assertEquals(Get.multipleElementsByTestId(Constants.VlanTagging.MODEL_ITEM_LABEL_INSTANCE_GROUP_FUNCTION)
                        .stream().map(WebElement::getText).collect(Collectors.toList()),
                Arrays.asList("VFC Instance Group Function", "VFC Instance Group Function"));

        assertEquals(Get.multipleElementsByTestId(Constants.VlanTagging.MODEL_ITEM_VALUE_INSTANCE_GROUP_FUNCTION)
                        .stream().map(WebElement::getText).collect(Collectors.toList()),
                Arrays.asList("", ""));

        assertEquals(Get.multipleElementsByTestId(Constants.VlanTagging.MODEL_ITEM_LABEL_PARENT_PORT_ROLE).stream()
                        .map(WebElement::getText).collect(Collectors.toList()),
                Arrays.asList("Parent Port Role", "Parent Port Role"));

        assertEquals(Get.multipleElementsByTestId(Constants.VlanTagging.MODEL_ITEM_VALUE_PARENT_PORT_ROLE).stream()
                        .map(WebElement::getText).collect(Collectors.toList()),
                Arrays.asList("untr", "untr"));

        assertEquals(Get.multipleElementsByTestId(Constants.VlanTagging.MODEL_ITEM_LABEL_SUBINTERFACE_ROLE).stream()
                        .map(WebElement::getText).collect(Collectors.toList()),
                Arrays.asList("Sub Interface Role", "Sub Interface Role"));

        assertEquals(Get.multipleElementsByTestId(Constants.VlanTagging.MODEL_ITEM_VALUE_SUBINTERFACE_ROLE).stream()
                        .map(WebElement::getText).collect(Collectors.toList()),
                Arrays.asList("untr", "untr"));

        Click.byTestId(Constants.ConfigurationCreation.NEXT_BUTTON_TEST_ID);

        compareTwoGroups(Get.multipleElementsByTestId("groupLabel"), rightSideGroupsNames);

        List<WebElement> leftSideGroups = Get.multipleElementsByTestId("groupTestId");

        GeneralUIUtils.ultimateWait();

        for (WebElement element : leftSideGroups) {
            (new Select(element)).selectByIndex(1);
        }

        Click.byTestId(Constants.ConfigurationCreation.NEXT_BUTTON_TEST_ID);
        VidBasePage.goOutFromIframe();
        assertSuccessfulServiceInstanceCreation();
        viewEditPage.clickCommitCloseButton();
    }

    private void compareTwoGroups(List<WebElement> leftSideGroups, List<String> rightSideGroupsNames) {
        assertEquals(leftSideGroups.size(), rightSideGroupsNames.size());

        //converting list of web elements to list of strings
        ArrayList<String> leftSideGroupsNames = new ArrayList<>();
        for (WebElement element : leftSideGroups) {
            leftSideGroupsNames.add(element.getText());
        }
        assertEquals(leftSideGroupsNames, rightSideGroupsNames);
    }

    private void checkModelItemLabelAndValue(String labelId, String labelValue, String textID, String textValue) {
        assertThat(Get.byTestId(labelId).getText(), is(equalTo(labelValue)));
        assertThat(Get.byTestId(textID).getText(), is(equalTo(textValue)));
    }
}
