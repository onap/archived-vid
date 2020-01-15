package vid.automation.test.test;

import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId.PRESET_SOME_LEGACY_REGION_TO_ATT_AIC;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet.COMPLETE;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.SERVICE_NAME;
import static org.onap.vid.api.TestUtils.generateRandomAlphaNumeric;
import static vid.automation.test.Constants.BrowseASDC.NewServicePopup.SET_BUTTON;
import static vid.automation.test.Constants.DrawingBoard.CONTEXT_MENU_BUTTON_HEADER;
import static vid.automation.test.Constants.DrawingBoard.CONTEXT_MENU_HEADER_EDIT_ITEM;
import static vid.automation.test.Constants.DrawingBoard.DEPLOY_BUTTON;
import static vid.automation.test.infra.Features.FLAG_2004_INSTANTIATION_TEMPLATES_POPUP;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPresets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceAlacarte;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys;
import org.onap.vid.api.AsyncInstantiationBase;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.Input;
import vid.automation.test.infra.ModelInfo;
import vid.automation.test.sections.DrawingBoardPage;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.sections.VidBasePage;
import vid.automation.test.services.AsyncJobsService;

public class TemplateInstantiationTest extends ModernUITestBase {

    private AsyncInstantiationBase asyncInstantiationBase;

    @BeforeClass
    protected void setUp() {
        AsyncJobsService asyncJobsService = new AsyncJobsService();
        asyncJobsService.dropAllAsyncJobs();
        asyncInstantiationBase = new AsyncInstantiationBase();
        asyncInstantiationBase.init();
        UserCredentials userCredentials = getUserCredentials();
        //login for API test (needed besides selenium test via browser)
        asyncInstantiationBase.login(userCredentials);
    }

    @AfterClass
    protected void tearDown() {
        AsyncJobsService asyncJobsService = new AsyncJobsService();
        asyncJobsService.muteAllAsyncJobs();
    }

    /*
        In this test we create an simple aLaCarte service via api,
        Then browse SDC -> search for same model uuid -> deploy ->
        template popup is opened -> select the service -> click load template ->
        drawing board is opened -> edit service instance name -> deploy ->
        instantiation status is opened -> wait for service to completed.
     */
    @FeatureTogglingTest(FLAG_2004_INSTANTIATION_TEMPLATES_POPUP)
    @Test
    public void instantiateALaCarteServiceFromTemplateTest() {

        final ModelInfo modelInfo = ModelInfo.aLaCarteServiceCreationNewUI;
        String templateInstanceName = "template"+generateRandomAlphaNumeric(10);
        String requestorID = getUserCredentials().getUserId();
        String serviceRequestId = uuid();
        String serviceInstanceId = uuid();

        //prepare presets for first instantiation (template), and rest of scenario till deploy
        prepareServicePreset(modelInfo, true);
        registerExpectationFromPresets(
            ImmutableList.of(
                new PresetMSOCreateServiceInstanceAlacarte(
                    ImmutableMap.of(Keys.SERVICE_NAME, templateInstanceName),
                    serviceRequestId, serviceInstanceId,
                    requestorID, modelInfo),
                PRESET_SOME_LEGACY_REGION_TO_ATT_AIC,
                new PresetMSOOrchestrationRequestGet(COMPLETE, serviceRequestId)
            ),
            APPEND
        );

        //create service instance via API
        final List<String> jobsIds = asyncInstantiationBase.createBulkOfInstances(false, 1,
            ImmutableMap.of(SERVICE_NAME, templateInstanceName), "asyncInstantiation/vidRequestCreateALaCarteForTemplate.json");
        asyncInstantiationBase.waitForJobsToSuccessfullyCompleted(1, jobsIds);

        String newInstanceName = "template"+generateRandomAlphaNumeric(10);
        String serviceRequestId2 = uuid();
        String serviceInstanceId2 = uuid();

        //load template and and edit service instance name
        browseSdcAndClickDeploy(modelInfo);
        selectTemplateByNameAndLoadTemplate(templateInstanceName);
        VidBasePage.goToIframe();
        editServiceInstanceName(newInstanceName);

        //prepare presets for second service instance deploy
        registerExpectationFromPresets(
            ImmutableList.of(
                new PresetMSOCreateServiceInstanceAlacarte(
                    ImmutableMap.of(Keys.SERVICE_NAME, newInstanceName),
                    serviceRequestId2, serviceInstanceId2,
                    requestorID, modelInfo),
                PRESET_SOME_LEGACY_REGION_TO_ATT_AIC,
                new PresetMSOOrchestrationRequestGet(COMPLETE, serviceRequestId2)
            ),
            CLEAR_THEN_SET
        );

        //deploy the service and wait for completion
        Click.byTestId(DEPLOY_BUTTON);
        VidBasePage.goToIframe();
        new DrawingBoardPage().verifyServiceCompletedOnTime(newInstanceName, "service deployed from template");
    }

    private void browseSdcAndClickDeploy(ModelInfo modelInfo) {
        SideMenu.navigateToBrowseASDCPage();
        GeneralUIUtils.ultimateWait();
        loadTemplatesPopupOnBrowseASDCPage(modelInfo.modelVersionId);
    }

    private void selectTemplateByNameAndLoadTemplate(String templateInstanceName) {
        Click.byText(templateInstanceName);
        Click.byTestId("LoadTemplateButton");
    }

    private void editServiceInstanceName(String newInstanceName) {
        GeneralUIUtils.getClickableButtonBy(Get.getXpathForDataTestId(CONTEXT_MENU_BUTTON_HEADER), 60).click();
        Click.byTestId(CONTEXT_MENU_HEADER_EDIT_ITEM);
        GeneralUIUtils.ultimateWait();
        Input.replaceText(newInstanceName, "instanceName");
        Click.byTestId(SET_BUTTON);
    }

}
