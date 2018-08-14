package vid.automation.test.test;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetMultipleVersion;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetNetworkZones;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetOneVersion;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetServicesGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubDetailsGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstancePost;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceMetadataGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.sections.BrowseASDCPage;
import vid.automation.test.sections.CreateNewInstancePage;
import vid.automation.test.sections.PreviousVersionDialog;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.services.SimulatorApi;

public class PreviousVersionsPopupTest extends CreateInstanceDialogBaseTest{

    BrowseASDCPage browseASDCPage = new BrowseASDCPage();
    PreviousVersionDialog newVersionDialog = new PreviousVersionDialog();
    private String versionNumber1 = "1.0";
    private String versionNumber2 = "2.0";
    private String versionNumber3 = "3.0";
    private String modelVersionId1 = "aeababbc-010b-4a60-8df7-e64c07389466";
    private String modelVersionId2 = "aa2f8e9c-9e47-4b15-a95c-4a9385599abc";
    private String modelVersionId3 = "d849c57d-b6fe-4843-8349-4ab8bbb08d71";
    private static final String modelInvariantId = "a8dcd72d-d44d-44f2-aa85-53aa9ca99cba";
    private static final String serviceName = "action-data";
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
        SideMenu.navigateToBrowseASDCPage();
        browseASDCPage.clickPreviousVersionButton();
        browseASDCPage.clickCancelButton();// to change
        newVersionDialog.assertVersionRow(modelInvariantId,modelVersionId3,versionNumber3,"Browse_SDC_Service_Models-uuid-");
    }
    @Test
    private void browseSDC_previousVersionButton_notExists(){
        prepareSimulatorWithOneVersionBeforeBrowseASDCService();
        SideMenu.navigateToBrowseASDCPage();
        browseASDCPage.assertPreviousVersionButtonNotExists(modelInvariantId);
    }

    @Test
    private void openPreviousVersionPopup_newestVersionButton_notExists(){
        prepareSimulatorWithThreeVersionsBeforeBrowseASDCService();
        SideMenu.navigateToBrowseASDCPage();
        browseASDCPage.clickPreviousVersionButton();
        newVersionDialog.assertHighestVersionNotExists(modelVersionId3);
        newVersionDialog.clickCancelButton();
    }
    @Test
    private void openPreviousVersionPopup_deployOldVersion_creationPopupIsALaCarte(){
        String expectedPopupIsALaCarteName = "Create Service Instance -- a la carte";
        prepareSimulatorWithThreeVersionsBeforeBrowseASDCService();
        CreateNewInstancePage newInstance= new CreateNewInstancePage();
        newVersionDialog = new PreviousVersionDialog();
        SideMenu.navigateToBrowseASDCPage();
        browseASDCPage.clickPreviousVersionButton();
        newVersionDialog.clickDeployServiceButtonByServiceUUID(modelVersionId2);
        assertNewInstanceFormOpened(createModalTitleTestId,expectedPopupIsALaCarteName);
        newInstance.clickCancelButtonByTestID();
        GeneralUIUtils.ultimateWait();
        newVersionDialog.clickCancelButton();

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
                new PresetAAIGetMultipleVersion(modelVersionId1,modelVersionId2,modelVersionId3, modelInvariantId),
                new PresetAAIGetServicesGet(),
                new PresetSDCGetServiceMetadataGet(modelVersionId2, modelInvariantId, zipFileName),
                new PresetSDCGetServiceToscaModelGet(modelVersionId2, zipFileName),
                new PresetAAIGetSubDetailsGet(null),
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
                new PresetAAIGetOneVersion(modelVersionId1, modelInvariantId),
                new PresetAAIGetServicesGet());

        SimulatorApi.registerExpectationFromPresets(presets, SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
    }
}

