package vid.automation.test.test;

import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.model.User;
import vid.automation.test.services.BulkRegistration;
import vid.automation.test.services.SimulatorApi;

public class DeleteResumeTest extends CreateInstanceDialogBaseTest {

    @Test
    private void testResumePendingActivationVfModule() {
        User user = usersService.getUser(Constants.Users.SILVIA_ROBBINS_TYLER_SILVIA);
        String serviceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
        String vnfInstanceId = "c015cc0f-0f37-4488-aabf-53795fd93cd3";
        SimulatorApi.clearAll();
        BulkRegistration.resumeVfModule(serviceInstanceId,vnfInstanceId);
        navigateToViewEditPageOfuspVoiceVidTest444("240376de-870e-48df-915a-31f140eedd2c");
        resumeVFModule("aa","AAIAIC25", "AIC", "092eb9e8e4b7412e8787dd091bc58e86","hvf6",user.tenants);
    }

}
