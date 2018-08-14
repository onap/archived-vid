package vid.automation.test.test;

import org.junit.Assert;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetNetworkCollectionDetails;
import org.openecomp.sdc.ci.tests.datatypes.UserCredentials;
import org.openecomp.sdc.ci.tests.utilities.GeneralUIUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.model.User;
import vid.automation.test.sections.SideMenu;
import vid.automation.test.services.BulkRegistration;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.utils.DB_CONFIG;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;

/**
 * Created by Oren on 7/16/17.
 */
public class ReadOnlyTest extends VidBaseTestCase {

    private String serviceInstanceId = "3f93c7cb-2fd0-4557-9514-e189b7testCR";


    @Override
    protected UserCredentials getUserCredentials() {
        User user = usersService.getUser(Constants.Users.READONLY);
        return new UserCredentials(user.credentials.userId, user.credentials.password, "", "", "");
    }

    @Test
    public void userIsDefinedAsReadOnlyInDBTest() throws SQLException {
        User user = usersService.getUser(Constants.Users.READONLY);

        int userId = getUserIdNumberFromDB(user);

        List<Integer> userRoles = getRoleIDsAssignedToUser(userId);
        Assert.assertTrue("The user should have role number 16", userRoles.contains(16));
        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {
            Statement stmt = connection.createStatement();
            verifyRolesAssignedToUser(stmt, userRoles);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    private void verifyRolesAssignedToUser(Statement stmt, List<Integer> userRoles) throws SQLException {
        String roleName;

        for (Integer roleId : userRoles) {
            ResultSet roleDetailsResult = stmt.executeQuery("SELECT ROLE_NAME FROM fn_role WHERE ROLE_ID = '" + roleId + "';");
            Assert.assertTrue("role id " + roleId + "was not found!", roleDetailsResult.next());
            roleName = roleDetailsResult.getString("ROLE_NAME");
            Assert.assertTrue("The user should have only 'Standard user", roleName.equalsIgnoreCase("STANDARD USER"));
            Assert.assertFalse("More than one role was found for role id " + roleId, roleDetailsResult.next());
        }
    }

    @Test
    public void testBrowsASDCReadOnly() {
        String zipFileName = "serviceCreationTest.zip";
        String modelVersionId = "aa2f8e9c-9e47-4b15-a95c-4a9385599abc";
        String modelInvariantId = "a8dcd72d-d44d-44f2-aa85-53aa9ca99cba";

        registerExpectationForLegacyServiceDeployment(modelVersionId, modelInvariantId, zipFileName, null);

        SideMenu.navigateToBrowseASDCPage();
        Assert.assertTrue(isDeployBtnDisabled());
    }

    private boolean isDeployBtnDisabled() {
        WebElement deployBtn = GeneralUIUtils.getWebElementBy(By.className(Constants.BrowseASDC.DEPOLY_SERVICE_CLASS));
        return !deployBtn.isEnabled();
    }

    @Test
    public void testSearchExistingReadOnly() {
        SimulatorApi.clearAll();
        BulkRegistration.searchExistingCRServiceInstance("Created");
        final PresetAAIGetNetworkCollectionDetails presetAAIGetNetworkCollectionDetails = new PresetAAIGetNetworkCollectionDetails(serviceInstanceId);
        SimulatorApi.registerExpectationFromPreset(presetAAIGetNetworkCollectionDetails, APPEND);

        searchForExistingInstanceByIdReadonlyMode(serviceInstanceId);
    }

    @Test
    private void testCreateNewInstanceReadOnly() {
        SimulatorApi.registerExpectation(SimulatorApi.RegistrationStrategy.APPEND,
        "search_for_service_instance/aai_get_full_subscribers.json");
        SideMenu.navigateToCreateNewServicePage();
        assertDropdownPermittedItemsByValue(new ArrayList<String>(), Constants.CreateNewInstance.SUBSCRIBER_NAME_OPTION_CLASS);
    }


}
