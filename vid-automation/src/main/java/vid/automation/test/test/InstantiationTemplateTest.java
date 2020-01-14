package vid.automation.test.test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.SERVICE_NAME;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.VNF_NAME;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys;
import org.onap.vid.api.AsyncInstantiationBase;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vid.automation.test.services.AsyncJobsService;

public class InstantiationTemplateTest extends DrawingBoardInstantiationBase {

    protected AsyncInstantiationBase asyncInstantiationBase;

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

    @Test
    public void createInstance_deployFromTemplate() {
        String instanceName = randomAlphabetic(5) +"instancename";
        final ImmutableMap<Keys, String> vars = generateNamesForEcompNamingFalsePreset("2017-488_PASQUALE-vPE 0", instanceName, instanceName, "2017-388_PASQUALE-vPE");
        String requestId = registerPresetsForEcompNamingFalseFirstService(vars);
        List<String> uuids = asyncInstantiationBase.createBulkOfInstances(false, 1,
            ImmutableMap.of(SERVICE_NAME, instanceName, VNF_NAME, instanceName), "asyncInstantiation/ecompNamingFalse.json");
        asyncInstantiationBase.waitForAllJobsToComplete(1, uuids);
    }
}
