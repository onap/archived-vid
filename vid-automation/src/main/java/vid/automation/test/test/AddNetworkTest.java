package vid.automation.test.test;

import org.openecomp.sdc.ci.tests.datatypes.UserCredentials;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.model.User;
import vid.automation.test.sections.ViewEditPage;
import vid.automation.test.services.BulkRegistration;
import vid.automation.test.services.SimulatorApi;

import java.util.HashMap;
import java.util.Map;

public class AddNetworkTest extends VidBaseTestCase {

    private ViewEditPage viewEditPage = new ViewEditPage();
    private String serviceInstanceIdWithNetwork = "d198cc45-158b-480e-8d2c-03943c51268e";
    private String currentUser;

    @Test
    public void testAddNetworkFullFlow() throws Exception {
        User user = usersService.getUser(Constants.Users.MOBILITY_MOBILITY);
        SimulatorApi.clearAll();
        BulkRegistration.genericSearchExistingServiceInstance();
        BulkRegistration.addNetwork();
        String instanceName = viewEditPage.generateInstanceName(Constants.ViewEdit.NETWORK_INSTANCE_NAME_PREFIX);

        BulkRegistration.msoAddNetwork(instanceName);
        goToExistingInstanceById(serviceInstanceIdWithNetwork);
        Map<String, String> networkMetadata=  getNetworkExpectedMetadata();
        addNetwork(networkMetadata,instanceName, "AIC30_CONTRAIL_BASIC 0", "One","a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb","xxx1", "y1","c630e297a3ae486497d63eacec1d7c14",
                "false", "some legacy region",user.tenants);
    }

    private Map<String, String> getNetworkExpectedMetadata() {
        return new HashMap<String, String>(){
            {
                put(Constants.NetworkModelInfo.SERVICE_NAME, "Using VID for VoIP Network Instantiations Shani");
                put(Constants.NetworkModelInfo.SUBSCRIBER_NAME, "Mobility");
                put(Constants.NetworkModelInfo.SERVICE_INSTANCE_NAME, "service_with_network_shani");
                put(Constants.NetworkModelInfo.MODEL_NAME, "AIC30_CONTRAIL_BASIC");
                put(Constants.NetworkModelInfo.MODEL_INVARIANT_UUID, "de01afb5-532b-451d-aac4-ff9ff0644060");
                put(Constants.NetworkModelInfo.MODEL_VERSION, "3.0");
                put(Constants.NetworkModelInfo.MODEL_UUID, "ac815c68-35b7-4ea4-9d04-92d2f844b27c");
                put(Constants.NetworkModelInfo.MODEL_CUSTOMIZATION_UUID, "e94d61f7-b4b2-489a-a4a7-30b1a1a80daf");
            }
        };
    }

    @Override
    protected UserCredentials getUserCredentials() {
        User user = usersService.getUser(Constants.Users.MOBILITY_MOBILITY);
        return new UserCredentials(user.credentials.userId, user.credentials.password, Constants.Users.MOBILITY_MOBILITY, "", "");
    }
}
