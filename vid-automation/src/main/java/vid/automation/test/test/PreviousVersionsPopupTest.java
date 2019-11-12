package vid.automation.test.test;

import static org.testng.Assert.assertEquals;
import static vid.automation.test.infra.ModelInfo.ModelInfoWithMultipleVersions.modelInfoWithMultipleVersions;
import static vid.automation.test.infra.ModelInfo.serviceWithOneVersion;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetNetworkZones;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetServicesGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubDetailsGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubDetailsWithoutInstancesGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIServiceDesignAndCreationPut;
import org.onap.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstancePost;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceMetadataGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.ModelInfo;
import vid.automation.test.sections.BrowseASDCPage;
import vid.automation.test.sections.PreviousVersionDialog;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.sections.deploy.DeployDialogBase;
import vid.automation.test.services.SimulatorApi;

public class PreviousVersionsPopupTest extends CreateInstanceDialogBaseTest{

    BrowseASDCPage browseASDCPage = new BrowseASDCPage();
    PreviousVersionDialog newVersionDialog = new PreviousVersionDialog();
    private String versionNumber1 = "1.0";
    private String versionNumber2 = "2.0";
    private String versionNumber3 = "3.0";
    private String modelVersionId1 = modelInfoWithMultipleVersions.modelVersionId1;
    private String modelVersionId2 = modelInfoWithMultipleVersions.modelVersionId2;
    private String modelVersionId3 = modelInfoWithMultipleVersions.modelVersionId3;
    private static final String modelInvariantId = modelInfoWithMultipleVersions.modelInvariantId;
    private static final String serviceName = modelInfoWithMultipleVersions.modelName;
    private String createModalTitleTestId = "create-modal-title";


    @DataProvider
    public static Object[][] filterTexts() {
        return new Object[][]{{serviceName},{modelInvariantId}};
    }

    @Test(dataProvider ="filterTexts" )
    private void openPreviousVersionPopup_twoPreviousVersions_Exists(String filterText){
        prepareSimulatorWithThreeVersionsBeforeBrowseASDCService();
        SideMenu.navigateToBrowseASDCPage();
        browseASDCPage.fillFilterText(filterText);
        newVersionDialog.assertPreviousVersionButtonExists(modelInvariantId);
        browseASDCPage.clickPreviousVersionButton();
        newVersionDialog.assertVersionRow(modelInvariantId,modelVersionId1,versionNumber1,"Previous-version-pop-up-uuid-");
        newVersionDialog.assertVersionRow(modelInvariantId,modelVersionId2,versionNumber2,"Previous-version-pop-up-uuid-");
        browseASDCPage.clickCancelButton();
    }

    @Test
    private void browseSDC_afterCancelOnPopup_browseSDCpageExists(){
        prepareSimulatorWithThreeVersionsBeforeBrowseASDCService();
        navigateToBrowseAsdcAndClickPreviousButton();
        browseASDCPage.clickCancelButton();// to change
        newVersionDialog.assertVersionRow(modelInvariantId,modelVersionId3,versionNumber3,"Browse_SDC_Service_Models-uuid-");
    }
    @Test
    private void browseSDC_previousVersionButton_notExists(){
        prepareSimulatorWithOneVersionBeforeBrowseASDCService();
        SideMenu.navigateToBrowseASDCPage();
        browseASDCPage.assertPreviousVersionButtonNotExists(serviceWithOneVersion.modelInvariantId);
    }

    @Test
    private void openPreviousVersionPopup_newestVersionButton_notExists(){
        prepareSimulatorWithThreeVersionsBeforeBrowseASDCService();
        navigateToBrowseAsdcAndClickPreviousButton();
        newVersionDialog.assertHighestVersionNotExists(modelVersionId3);
        newVersionDialog.clickCancelButton();
    }
    @Test
    private void openPreviousVersionPopup_deployOldVersion_creationPopupIsALaCarte(){
        prepareSimulatorWithThreeVersionsBeforeBrowseASDCService();
        registerExpectationForLegacyServiceDeployment(ModelInfo.aLaCarteServiceOldVersionTest, "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb");
        newVersionDialog = new PreviousVersionDialog();
        navigateToBrowseAsdcAndClickPreviousButton();
        newVersionDialog.clickDeployServiceButtonByServiceUUID(modelVersionId2);
        final DeployDialogBase deployDialog = BrowseASDCTest.getAlacarteDialogByFlagValue();
        deployDialog.waitForDialogToLoad();
        deployDialog.assertDialog();
        assertEquals(deployDialog.getModelVersionId(), modelVersionId2);
        deployDialog.closeDialog();
    }

    private void navigateToBrowseAsdcAndClickPreviousButton() {
        SideMenu.navigateToBrowseASDCPage();
        Click.byTestId("view-per-page-50");
        GeneralUIUtils.ultimateWait();
        Get.byTestId("PreviousVersion-"+modelInvariantId).findElement(By.tagName("button")).click();
    }

    private void assertNewInstanceFormOpened(String createModalTitleTestId,String expectedInstanceFormName) {
        String newInstanceFormName  = GeneralUIUtils.getWebElementByTestID(createModalTitleTestId).getText();
        Assert.assertEquals(expectedInstanceFormName, newInstanceFormName);
    }

    private void prepareSimulatorWithThreeVersionsBeforeBrowseASDCService(){
        SimulatorApi.clearAll();
        String zipFileName = "serviceCreationTest.zip";

        ImmutableList<BasePreset> presets = ImmutableList.of(
                new PresetGetSessionSlotCheckIntervalGet(),
                new PresetAAIGetSubscribersGet(),
                new PresetAAIServiceDesignAndCreationPut(),
                new PresetAAIGetServicesGet(),
                new PresetSDCGetServiceMetadataGet(modelVersionId2, modelInvariantId, zipFileName),
                new PresetSDCGetServiceToscaModelGet(modelVersionId2, zipFileName),
                new PresetAAIGetSubDetailsGet(null),
                new PresetAAIGetSubDetailsWithoutInstancesGet(null),
                new PresetAAIGetNetworkZones(),
                new PresetMSOCreateServiceInstancePost(),
                new PresetMSOOrchestrationRequestGet());
        SimulatorApi.registerExpectationFromPresets(presets, SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
    }

    private void prepareSimulatorWithOneVersionBeforeBrowseASDCService(){
        SimulatorApi.clearAll();
        ImmutableList<BasePreset> presets = ImmutableList.of(
                new PresetGetSessionSlotCheckIntervalGet(),
                new PresetAAIGetSubscribersGet(),
                new PresetAAIServiceDesignAndCreationPut(),
                new PresetAAIGetServicesGet());

        SimulatorApi.registerExpectationFromPresets(presets, SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
    }
}

