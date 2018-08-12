package vid.automation.test.services;

import com.att.automation.common.report_portal_integration.annotations.Step;
import com.google.common.primitives.Ints;
import org.apache.commons.lang3.StringUtils;
import vid.automation.test.model.User;
import vid.automation.test.model.UsersObject;
import vid.automation.test.utils.DB_CONFIG;
import vid.automation.test.utils.ReadFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Created by itzikliderman on 08/09/2017.
 */
public class UsersService {
    private HashMap<String, User> users;

    public UsersService() {
        users = getUsersFromJson();
        users.forEach(this::prepareUser);
    }

    HashMap<String, User> getUsersFromJson() {
        UsersObject usersObject = null;
        usersObject = ReadFile.getJsonFile("users", UsersObject.class);
        return usersObject.users;
    }

    @Step("${method} with id: ${userId}")
    public User getUser(String userId) {
        User res = users.get(userId);
        System.out.println("getUser userId='" + userId + "' returned: " + res);

        if (res == null) {
            throw new RuntimeException("user id '" + userId + "' is not defined (these ARE defined: " + users.keySet() + ")");
        }

        return res;
    }


    private void prepareUser(String userId, User user) {
        /*
        Creates a user in the DB, were:
         -  Login user name is a deterministic number, hashed from the userId string, with 3 trailing zeroes,
            and two leading letters from the userId itself; e.g. "mo26063000" for mobility.
         -  Login user name == user password
         -  'user.credentials.userId' and 'user.credentials.password' input fields are overridden with the generated values.
         -  Roles are "read" (roleId==16) and all other roles in object (like subscriberName___serviceType___tenant).
         -  Yielded role ids are the successive numbers after the user name. e.g "57174000", "57174001", "57174002".
         */

        dropUser(userId);

        System.out.println("Preparing user '" + userId + "': " + user);
        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {

            System.out.println("Database connected!");

            ///////////////////////////////
            // Add user with specific roles
            Statement stmt = connection.createStatement();
            int userNumber = getUserNumber(userId);
            user.credentials.userId = getLoginId(userId);
            user.credentials.password = getLoginId(userId);

            stmt.addBatch("INSERT INTO `fn_user` (`USER_ID`, `ORG_USER_ID`, `FIRST_NAME`, `LOGIN_ID`, `LOGIN_PWD`) " +
                    "VALUES (" + userNumber + ", '" + userId + "', '" + userId + "', '" + user.credentials.userId + "', '" + user.credentials.password + "')");

            List<String> roles = user.roles != null ? user.roles : new LinkedList<>();
            roles.add("Standard User");

            ListIterator<String> iter = roles.listIterator();
            while (iter.hasNext()) {
                int roleNumber = userNumber + iter.nextIndex();

                String sql = "INSERT INTO `fn_role` (`ROLE_ID`, `ROLE_NAME`, `ACTIVE_YN`, `PRIORITY`) VALUES (" + roleNumber + ", '" + iter.next() + "', 'Y', " + 5 + ")";
                System.out.println(sql);
                stmt.addBatch(sql);
                String sql2 = "INSERT INTO `fn_user_role` (`USER_ID`, `ROLE_ID`, `PRIORITY`, `APP_ID`) VALUES (" + userNumber + ", " + roleNumber + ", NULL, 1)";
                System.out.println(sql2);
                stmt.addBatch(sql2);
            }
            stmt.addBatch("INSERT INTO `fn_user_role` (`USER_ID`, `ROLE_ID`, `PRIORITY`, `APP_ID`) VALUES (" + userNumber + ", 16, NULL, 1)");

            int[] executeBatch = stmt.executeBatch();
            assertThat(Ints.asList(executeBatch), everyItem(greaterThan(0)));

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }

    }

    private void dropUser(String userId) {
        System.out.println("Dropping user '" + userId + "'");
        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.url, DB_CONFIG.username, DB_CONFIG.password)) {

            System.out.println("Database connected!");

            int userNumber = getUserNumber(userId);
            Statement stmt = connection.createStatement();
            stmt.addBatch("DELETE FROM `fn_user_role` WHERE `USER_ID` = " + userNumber);
            stmt.addBatch("DELETE FROM `fn_role` WHERE `ROLE_ID` BETWEEN " + userNumber + " AND " + (userNumber + 100));
            stmt.addBatch("DELETE FROM `fn_user` WHERE `USER_ID` = " + userNumber);
            int[] executeBatch = stmt.executeBatch();

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    private int getUserNumber(String userId) {
        return (Math.abs(userId.hashCode()) % 100000) * 1000;
    }

    private String getLoginId(String userId) {
        int userNumber = getUserNumber(userId);
        final String twoCharacters = StringUtils.substring(userId,0, 2).toLowerCase();
        return String.format("%s%d", twoCharacters, userNumber);
    }

}
