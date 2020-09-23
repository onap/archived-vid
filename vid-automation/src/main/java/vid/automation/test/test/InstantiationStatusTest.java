package vid.automation.test.test;

import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.SERVICE_NAME;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.VNF_NAME;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;
import static vid.automation.test.Constants.DrawingBoard.DEPLOY_BUTTON;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import org.junit.Assert;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames;
import org.onap.vid.api.AsyncInstantiationBase;
import org.onap.vid.api.CreateServiceWithFailedVnf;
import org.onap.vid.api.TestUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.Get;
import vid.automation.test.infra.Wait;
import vid.automation.test.sections.DrawingBoardPage;
import vid.automation.test.sections.InstantiationStatusPage;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.sections.VidBasePage;
import vid.automation.test.services.AsyncJobsService;

public class InstantiationStatusTest extends VidBaseTestCase {

    private final String serviceModelVersion = "5.1";
    private final String regionId = "a93f8383-707e-43fa-8191-a6e69a1aab17";
    final static String owningEntityName  = "Lucine Sarika";
    final static String subscriberName  = "SILVIA ROBBINS";
    private static final String COMPLETED = "COMPLETED";
    private static final String CREATE_BULK_OF_ALACARTE_REQUEST = "asyncInstantiation/vidRequestCreateALaCarte.json";
    private final VidBasePage vidBasePage = new VidBasePage();

    private AsyncInstantiationBase asyncInstantiationBase;

    @BeforeClass
    protected void dropAllAsyncJobs() {
        AsyncJobsService asyncJobsService = new AsyncJobsService();
        asyncJobsService.dropAllAsyncJobs();
        asyncInstantiationBase = new AsyncInstantiationBase();
        asyncInstantiationBase.init();
        UserCredentials userCredentials = getUserCredentials();
        //login for API test (needed besides selenium test via browser)
        asyncInstantiationBase.login(userCredentials);
    }

    @AfterClass
    protected void muteAllAsyncJobs() {
        AsyncJobsService asyncJobsService = new AsyncJobsService();
        asyncJobsService.muteAllAsyncJobs();
    }

    private String addOneJob() {
        String serviceName = TestUtils.generateRandomAlphaNumeric(8);
        final ImmutableMap<PresetMSOServiceInstanceGen2WithNames.Keys, String> names =
                ImmutableMap.of(SERVICE_NAME, serviceName);
        asyncInstantiationBase.createBulkOfInstances(false, 1, names, CREATE_BULK_OF_ALACARTE_REQUEST).get(0);
        return serviceName;
    }

    @Test
    public void testServiceInfoIsPresentedInTable() {
        String serviceName = addOneJob();
        SideMenu.navigateToMacroInstantiationStatus();
        InstantiationStatusPage.clickRefreshButton();

        InstantiationStatusPage.assertInstantiationStatusRow(serviceName, ImmutableMap.of(
                "subscriberName", subscriberName,
                "regionId", regionId,
                "serviceModelVersion", serviceModelVersion,
                "owningEntityName", owningEntityName
        ));
    }

    @Test
    public void testServiceInfoDataUpdatingAfterClickRefresh() {
        addOneJob();
        SideMenu.navigateToMacroInstantiationStatus();
        InstantiationStatusPage.clickRefreshButton();
        long numberOfRows = InstantiationStatusPage.getNumberOfTableRows(60);

        addOneJob();
        InstantiationStatusPage.clickRefreshButton();
        int numberOfRowsAfterRefresh = InstantiationStatusPage.getNumberOfTableRows(60);
        Assert.assertEquals(numberOfRows + 1 , numberOfRowsAfterRefresh);
    }
    
    @Test
    @FeatureTogglingTest(Features.FLAG_1902_RETRY_JOB)
    public void testRedeployFromDrawingBoardOfServiceWithFailedVnf() {
        SideMenu.navigateToMacroInstantiationStatus();

        CreateServiceWithFailedVnf createServiceWithFailedVnf = createServiceWithFailedVnfAssertStausAndSimulatorRegistration();
        String originalServiceName = createServiceWithFailedVnf.getNames().get(SERVICE_NAME);

        InstantiationStatusPage.clickRefreshButton();

        //Open job
        InstantiationStatusPage.openDrawingBoardForRetry(originalServiceName);
        DrawingBoardPage.goToIframe();

        Wait.waitByTestId("error-msg-wrapper", 10);
        boolean isErrorShown = Wait.waitByClassAndText("sub-title", "Attention: You are currently viewing instances from the MSO. 1 of the instances failed, please try again.", 10);
        assertTrue(isErrorShown);

        //validate audit info for failed vnf
        String originalVnfName = createServiceWithFailedVnf.getNames().get(VNF_NAME);
        hoverAndClickMenuByName(originalVnfName, "fe042c22-ba82-43c6-b2f6-8f1fc4164091-vSAMP12 1", Constants.DrawingBoard.CONTEXT_MENU_SHOW_AUDIT);
        checkFailedAuditInfoOnRetry(originalVnfName, createServiceWithFailedVnf.getFirstIds().vnfReqId, "Vnf failed.");

        Click.byTestId(DEPLOY_BUTTON);
        VidBasePage.goOutFromIframe();
        GeneralUIUtils.ultimateWait();
        vidBasePage.goToIframe();
        GeneralUIUtils.ultimateWait();

        checkRetryRequestToBeComplete(createServiceWithFailedVnf, originalServiceName);
    }

    private void checkFailedAuditInfoOnRetry(String instanceName, String requestId, String message) {
        GeneralUIUtils.ultimateWait();

        WebElement msoTableElement = Get.byId("service-instantiation-audit-info-mso");
        assertEquals(7, msoTableElement.findElement(By.tagName("thead")).findElements(By.tagName("th")).size(), "Audit info MSO table must contain 7 columns");
        assertEquals(requestId, msoTableElement.findElement(By.id("msoRequestId")).getText(), "Audit info Request Id is not equal");
        assertEquals("Failed", msoTableElement.findElement(By.id("msoJobStatus")).getText(), "Audit info Job Status is not equal");
        assertEquals(message, msoTableElement.findElement(By.id("msoAdditionalInfo")).getText(), "Audit info AdditionalInfo is not equal");

        vidBasePage.screenshotDeployDialog("retry-audit-info-" + instanceName);
        Click.byId(Constants.AuditInfoModal.CANCEL_BUTTON);
        GeneralUIUtils.ultimateWait();
    }

    private CreateServiceWithFailedVnf createServiceWithFailedVnfAssertStausAndSimulatorRegistration() {

        //CreateServiceWithFailedVnf is common for API test and UI test,
        //so if you change it, make sure both test are compatible with your changes
        CreateServiceWithFailedVnf createServiceWithFailedVnf = new CreateServiceWithFailedVnf(asyncInstantiationBase);
        createServiceWithFailedVnf.createServicesWithVnfCompletedWithError();
        createServiceWithFailedVnf.firstTimeAssertion();
        createServiceWithFailedVnf.secondRegistration();
        return createServiceWithFailedVnf;
    }

    private void checkRetryRequestToBeComplete(CreateServiceWithFailedVnf createServiceWithFailedVnf, String originalServiceName) {
        DrawingBoardPage.ServiceStatusChecker serviceStatusChecker = new DrawingBoardPage.ServiceStatusChecker(originalServiceName, Collections.singleton(COMPLETED));
        //there shall be 2 rows with same service name, one with completed with error and one completed
        //so the following line might be buggy, and we need to improve it one day ...
        boolean statusIsShown = Wait.waitFor(serviceStatusChecker, null, 30, 2);
        assertTrue("service " + originalServiceName + " wasn't completed after in time", statusIsShown);
        createServiceWithFailedVnf.simulatorCallsAssertion();
    }

}
