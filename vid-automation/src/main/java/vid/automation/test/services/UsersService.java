package vid.automation.test.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openecomp.sdc.ci.tests.utilities.FileHandling;
import vid.automation.test.model.User;
import vid.automation.test.model.UsersObject;
import vid.automation.test.utils.ReadFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by itzikliderman on 08/09/2017.
 */
public class UsersService {
    private HashMap<String, User> users;

    public UsersService() throws IOException {
        users = getUsersFromJson();
    }

    HashMap<String, User> getUsersFromJson() throws IOException {
        UsersObject usersObject = ReadFile.getJsonFile("users", UsersObject.class);
        return usersObject.users;
    }

    public User getUser(String userId) {
        return users.get(userId);
    }
}
