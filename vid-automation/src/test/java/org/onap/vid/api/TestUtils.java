package org.onap.vid.api;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpStatus;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Oren on 6/7/17.
 */
public class TestUtils {

    protected static ObjectMapper objectMapper = new ObjectMapper();

    public static void assertStatusOK(Object request, WebTarget webTarget, Response response) throws IOException {
        assertHttpStatus(request, webTarget, response, HttpStatus.OK);
    }

    public static void assertHttpStatus(Object request, WebTarget webTarget, Response response, HttpStatus exceptedHttpStatus) throws IOException {
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        org.testng.Assert.assertEquals(response.getStatus(), exceptedHttpStatus.value(),
                String.format("Failed post URI: %s with request %s. Got Status:%d and body: %s",
                        webTarget.getUri(),
                        objectMapper.writeValueAsString(request),
                        response.getStatus(),
                        objectMapper.writeValueAsString(response.getEntity())));
    }

    public static String convertRequest(ObjectMapper objectMapper, String msoRequestDetailsFileName) {

        ClassLoader cl = pProbeMsoApiTest.class.getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource[] resources;
        try {
            resources = resolver.getResources(msoRequestDetailsFileName);
            String content;
            File file = resources[0].getFile();
            content = new Scanner(file).useDelimiter("\\Z").next();
            objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            return objectMapper.writeValueAsString(objectMapper.readValue(content, Object.class));
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String getNestedPropertyInMap(Object item, String path) {
        return getNestedPropertyInMap(item, path, String.class, "/");
    }

    public static <T> T getNestedPropertyInMap(Object item, String path, Class<T> valueType) {
        return getNestedPropertyInMap(item, path, valueType, "/");
    }

    /*
    Use this method to extract item from Map that represent Json hierarchy (Map<String,Map>)
     */
    public static <T> T getNestedPropertyInMap(Object item, String path, Class<T> valueType, String delimeter) {
        String[] pathes  = path.split(delimeter);
        return valueType.cast(getNestedPropertyInMap(item,pathes,0));
    }

    private static Object getNestedPropertyInMap(Object item, String[] pathes, int index) {
        if (index==pathes.length) {
            return item;
        }
        return getNestedPropertyInMap(((Map<String,Object>)item).get(pathes[index]), pathes, ++index);
    }
}
