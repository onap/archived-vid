package vid.automation.test.test;

import com.google.common.collect.ImmutableList;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseCreateInstancePost;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateNetworkALaCarteOldViewEdit;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
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
    private  User user = usersService.getUser(Constants.Users.EMANUEL_EMANUEL);
    private String instanceName = viewEditPage.generateInstanceName(Constants.ViewEdit.NETWORK_INSTANCE_NAME_PREFIX);

    @Test
    public void testAddNetworkFullFlow() {
        String platform = "xxx1";
        this.goToNetworkPopup(platform);
        Map<String, String> networkMetadata=  getNetworkExpectedMetadata();
        addNetwork(networkMetadata,instanceName, "AIC30_CONTRAIL_BASIC 0", "One", "FUSION", "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb", platform, "zzz1","c630e297a3ae486497d63eacec1d7c14",
                "false", "some legacy region",user.tenants);
    }


    @Test
    public void testAddNetworkFullFlowWithoutPlatform() {
        this.goToNetworkPopup(null);
        Map<String, String> networkMetadata=  getNetworkExpectedMetadata();
        addNetwork(networkMetadata,instanceName, "AIC30_CONTRAIL_BASIC 0", "One", "FUSION", "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",null, "zzz1","c630e297a3ae486497d63eacec1d7c14",
                "false", "some legacy region",user.tenants);
    }


    private void goToNetworkPopup(String platform){

        SimulatorApi.clearAll();
        BulkRegistration.genericSearchExistingServiceInstance();
        BulkRegistration.addNetwork();


        SimulatorApi.registerExpectationFromPresets(
                ImmutableList.of(
                        PresetAAIGetCloudOwnersByCloudRegionId.PRESET_ONE_TO_ATT_AIC,
                        new PresetMSOCreateNetworkALaCarteOldViewEdit(
                                PresetMSOBaseCreateInstancePost.DEFAULT_REQUEST_ID,
                                "d198cc45-158b-480e-8d2c-03943c51268e",
                                "c187e9fe-40c3-4862-b73e-84ff056205f6",
                                instanceName,
                                platform
                        ),
                        new PresetMSOOrchestrationRequestGet(
                                PresetMSOOrchestrationRequestGet.COMPLETE,
                                PresetMSOOrchestrationRequestGet.DEFAULT_REQUEST_ID,
                                "Success",
                                false)),
                SimulatorApi.RegistrationStrategy.APPEND);

        goToExistingInstanceById(serviceInstanceIdWithNetwork);
    }

    private Map<String, String> getNetworkExpectedMetadata() {
        return new HashMap<String, String>(){
            {
                put(Constants.NetworkModelInfo.SERVICE_NAME, "Using VID for VoIP Network Instantiations Shani");
                put(Constants.NetworkModelInfo.SUBSCRIBER_NAME, "Emanuel");
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
        User user = usersService.getUser(Constants.Users.EMANUEL_EMANUEL);
        return new UserCredentials(user.credentials.userId, user.credentials.password, Constants.Users.EMANUEL_EMANUEL, "", "");
    }
}
