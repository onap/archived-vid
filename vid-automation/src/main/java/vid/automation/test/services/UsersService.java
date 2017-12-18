package vid.automation.test.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openecomp.sdc.ci.tests.utilities.FileHandling;
import vid.automation.test.model.User;
import vid.automation.test.model.UsersObject;

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
        String fileName = "users";
        ObjectMapper mapper = new ObjectMapper();
        UsersObject usersObject;
        try {
            File testCaseFile = FileHandling.getConfigFile(fileName);
            if(!testCaseFile.exists()) {
                String basePath = System.getProperty("BASE_PATH");
                testCaseFile = new File( basePath + File.separator + "conf" + File.separator + fileName);
            }
            usersObject = mapper.readValue(testCaseFile, UsersObject.class);
            return usersObject.users;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUser(String userId) {
        return users.get(userId);
    }
}
