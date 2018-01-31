package vid.automation.test.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openecomp.sdc.ci.tests.utilities.FileHandling;
import vid.automation.test.model.User;
import vid.automation.test.model.UsersObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ReadFile {
    public static <T> T getJsonFile(String fileName, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        T list;
        try {
            File testCaseFile = FileHandling.getConfigFile(fileName);
            if(!testCaseFile.exists()) {
                String basePath = System.getProperty("BASE_PATH");
                testCaseFile = new File( basePath + File.separator + "conf" + File.separator + fileName);
            }
            list = (T) mapper.readValue(testCaseFile, clazz);
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
