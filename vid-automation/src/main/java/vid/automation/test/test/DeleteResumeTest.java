package vid.automation.test.test;

import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.model.User;
import vid.automation.test.services.BulkRegistration;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.services.UsersService;

import java.io.IOException;

public class DeleteResumeTest extends CreateInstanceDialogBaseTest {
    private UsersService usersService = new UsersService();

    public DeleteResumeTest() throws IOException {
    }

    @Test()
    private void testResumePendingActivationVfModule() throws Exception {
        User user = usersService.getUser(Constants.Users.USP_VOICE_VIRTUAL_USP);
        String serviceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
        String vnfInstanceId = "c015cc0f-0f37-4488-aabf-53795fd93cd3";
        SimulatorApi.clearAll();
        BulkRegistration.resumeVfModule(serviceInstanceId,vnfInstanceId);
        navigateToViewEditPageOfuspVoiceVidTest444("240376de-870e-48df-915a-31f140eedd2c");
        resumeVFModule("aa","AAIAIC25","092eb9e8e4b7412e8787dd091bc58e86","mdt1",user.tenants);
    }

}