package vid.automation.test.test;

import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.sections.InstantiationStatusPage;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.services.AsyncJobsService;

import java.util.UUID;

import static vid.automation.test.infra.Features.FLAG_ASYNC_INSTANTIATION;
import static vid.automation.test.sections.InstantiationStatusPage.assertInstantiationStatusRow;
import static vid.automation.test.sections.InstantiationStatusPage.getNumberOfTableRows;

@FeatureTogglingTest(FLAG_ASYNC_INSTANTIATION)
public class InstantiationStatusTest extends VidBaseTestCase {


    private final String serviceModelVersion = "1.0";
    private final String subscriberId = "ac040e8a-b43a-441b-ab87-603f5b70be55";
    private final String regionId = "my-expected-region-id";
    private final String projectName = "a-project-name";
    final static String owningEntityName  = "expected-owningEntityName";
    final static String subscriberName  = "expected-subscriberName";


    private String currentUUI;

    @BeforeClass
    protected void dropAllAsyncJobs() {
        AsyncJobsService asyncJobsService = new AsyncJobsService();
        asyncJobsService.dropAllAsyncJobs();
    }

    @AfterClass
    protected void muteAllAsyncJobs() {
        AsyncJobsService asyncJobsService = new AsyncJobsService();
        asyncJobsService.muteAllAsyncJobs();
    }

    @BeforeMethod
    protected void createJobsData() {
        addOneJob();
        SideMenu.navigateToMacroInstantiationStatus();
    }

    private String addOneJob() {
        currentUUI = UUID.randomUUID().toString();
        final JavascriptExecutor javascriptExecutor = (JavascriptExecutor) GeneralUIUtils.getDriver();
        Object result = javascriptExecutor.executeScript(
                "return (function postJob(){var xhttp = new XMLHttpRequest(); " +
                        "     " +
                        "  xhttp.onreadystatechange = function() { " +
                        "    return this.responseText; " +
                        "  }; " +
                        " " +
                        "  xhttp.open(\"POST\", '/vid/asyncInstantiation/bulk', false); " +
                        "  xhttp.setRequestHeader(\"Content-type\", \"application/json\"); " +
                        "  xhttp.send(`{ " +
                        "    \"modelInfo\": { " +
                        "      \"modelType\": \"service\", " +
                        "      \"modelInvariantId\": \"300adb1e-9b0c-4d52-bfb5-fa5393c4eabb\", " +
                        "      \"modelVersionId\": \"5c9e863f-2716-467b-8799-4a67f378dcaa\", " +
                        "      \"modelName\": \"AIM_TRANSPORT_00004\", " +
                        "      \"modelVersion\": \"" + serviceModelVersion + "\" " +
                        "    }, " +
                        "    \"owningEntityId\" : \"someID\", " +
                        "    \"owningEntityName\": \"" + owningEntityName + "\", " +
                        "    \"projectName\" : \"" + projectName + currentUUI + "\", " +
                        "    \"globalSubscriberId\":  \"" + subscriberId + "\", " +
                        "    \"subscriberName\":  \"" + subscriberName + "\", " +
                        "    \"productFamilyId\" : \"myProductFamilyId\", " +
                        "    \"instanceName\" : \"MichaelJordan\", " +
                        "    \"subscriptionServiceType\" : \"mySubType\", " +
                        "    \"lcpCloudRegionId\" : \"" + regionId + "\", " +
                        "    \"tenantId\" : \"greatTenant\", " +
                        "    \"bulkSize\": 1, " +
                        "    \"isUserProvidedNaming\": \"true\", " +
                        "    \"vnfs\": {} " +
                        "} `); " +
                        " " +
                        "return JSON.parse(xhttp.responseText).entity;})()"
        );

        return result.toString();
    }

    @Test
    public void testServiceInfoIsPresentedInTable() {
        InstantiationStatusPage.clickRefreshButton();

        assertInstantiationStatusRow(projectName + currentUUI, ImmutableMap.of(
                "subscriberName", subscriberName,
                "regionId", regionId,
                "serviceModelVersion", serviceModelVersion,
                "owningEntityName", owningEntityName
        ));
    }


    @Test
    public void testServiceInfoDataUpdatingAfterClickRefresh() {
        long numberOfRows = getNumberOfTableRows(60);

        addOneJob();
        InstantiationStatusPage.clickRefreshButton();
        int numberOfRowsAfterRefresh = getNumberOfTableRows(60);
        Assert.assertEquals(numberOfRows + 1 , numberOfRowsAfterRefresh);
    }

}
