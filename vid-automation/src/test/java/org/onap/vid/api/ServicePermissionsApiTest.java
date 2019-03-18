package org.onap.vid.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.model.User;
import vid.automation.test.services.SimulatorApi;

public class ServicePermissionsApiTest extends BaseApiTest {

    private final String emanuelSubscriberId = "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb";
    private final String uspVoiceSubscriberId = "e433710f-9217-458d-a79d-1c7aff376d89";

    @BeforeClass
    public void setAaiSubscribers() {
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetSubscribersGet(), SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
    }

    @Test
    public void servicePermissions_loginWithPermissions_1IsPermitted1NotPermitted() {
        login(userCredentials(Constants.Users.EMANUEL_vWINIFRED));
        assertPermissions(emanuelSubscriberId, "vRichardson", true);
        assertPermissions(emanuelSubscriberId, "someNonexistent", false);
    }

    @Test
    public void servicePermissions_loginPermissionsWithTenant_IsPermittedRegardlessOfTenant() {
        login(userCredentials("Emanuel_with_tenant"));
        assertPermissions(emanuelSubscriberId, "vWINIFRED", true);
    }

    @Test
    public void servicePermissions_loginWithNoPermissionsAtAll_nothingIsPermitted() {
        login(userCredentials(Constants.Users.READONLY));
        assertPermissions(emanuelSubscriberId, "vRichardson", false);
        assertPermissions(emanuelSubscriberId, "someNonexistent", false);
    }

    @Test
    public void servicePermissions_serviceTypeWithSpace_isPermitted() {
        login(userCredentials(Constants.Users.SILVIA_ROBBINS_TYLER_SILVIA));
        assertPermissions(uspVoiceSubscriberId, "TYLER SILVIA", true);
    }

    private UserCredentials userCredentials(String userName) {
        User user = usersService.getUser(userName);
        return new UserCredentials(user.credentials.userId, user.credentials.password, userName, "", "");
    }

    private void assertPermissions(final String subscriberId, final String serviceType, boolean isEditPermitted) {
        final Map response = restTemplate.getForObject(uri + "/roles/service_permissions?subscriberId=" + subscriberId + "&serviceType=" + serviceType, Map.class);
        assertThat(response, is(ImmutableMap.of(
                "isEditPermitted", isEditPermitted
        )));
    }
}
