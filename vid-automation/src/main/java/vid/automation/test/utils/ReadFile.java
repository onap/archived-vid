package vid.automation.test.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.openecomp.sdc.ci.tests.utilities.FileHandling;
import vid.automation.test.infra.Input;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.createTempDirectory;

public class ReadFile {

    private static Path tempDirectory;

    public static <T> T getJsonFile(String fileName, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        T list;
        try {
            java.io.File testCaseFile = FileHandling.getConfigFile(fileName);
            if(!testCaseFile.exists()) {
                String basePath = System.getProperty("BASE_PATH");
                testCaseFile = new java.io.File( basePath + java.io.File.separator + "conf" + java.io.File.separator + fileName);
            }
            list = (T) mapper.readValue(testCaseFile, clazz);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("cannot read file '" + fileName + "' file as '" + clazz.getCanonicalName() + "'", e);
        }
    }

    public static String copyOfFileFromResources(String pathInResources) {

        // takes a file from resources, copies it to a temp folder, then
        // returns the newly created path file

        URL resource = Input.class.getClassLoader().getResource(pathInResources);
        if (resource == null) {
            throw new RuntimeException("file not found in resources: " + pathInResources);
        }

        Path target;
        try {
            tempDirectory = (tempDirectory != null) ? tempDirectory : createTempDirectory("resources-");
            target = tempDirectory.resolve(FilenameUtils.getName(resource.getPath()));
            copy(resource.openStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return target.toString();
    }

    public static String loadResourceAsString(String relativePath){
        final InputStream resource = ReadFile.class.getClassLoader().getResourceAsStream(relativePath);
        if (resource == null) throw new RuntimeException("template file not found: " + relativePath);
        try {
            return IOUtils.toString(resource, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
