package org.opencomp.vid.testUtils;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.opencomp.vid.api.pProbeMsoApiTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpStatus;
import org.codehaus.jackson.map.SerializationConfig;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import static fj.parser.Parser.fail;

/**
 * Created by Oren on 6/7/17.
 */
public class TestUtils {

    protected static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * The method compares between two jsons. the function assert that the actual object does not reduce or change the functionallity/parsing of the expected json.
     * This means that if the expected JSON has a key which is null or the JSON doesn't have a key which contained in the expected JSON the assert will succeed and the will pass.
     * For example : For JSON expected = {a:null} and actual {a:3} the test will pass
     * Other example : For JSON expected = {a:3} and actual {a:null} the test will fail
     *
     * @param expected JSON
     * @param actual JSON
     */
    public static void assertJsonStringEqualsIgnoreNulls(String expected, String actual) {
        if (expected == null || expected == JSONObject.NULL) {return;}

        JSONObject expectedJSON = new JSONObject(expected);
        JSONObject actualJSON = new JSONObject(actual);
        Iterator<?> keys = expectedJSON.keys();

        while( keys.hasNext() ) {
            String key = (String)keys.next();
            Object expectedValue = expectedJSON.get(key);
            if (expectedValue == JSONObject.NULL){
                continue;
            }

            Object actualValue = actualJSON.get(key);

            if (expectedValue instanceof JSONObject) {
                String expectedVal = expectedValue.toString();
                String actualVal = actualValue.toString();
                assertJsonStringEqualsIgnoreNulls(expectedVal, actualVal);
            }
            else if (expectedValue instanceof JSONArray) {
                if (actualValue instanceof JSONArray) {
                    JSONArray expectedJSONArray = (JSONArray)expectedValue;
                    JSONArray actualJSONArray = (JSONArray)expectedValue;
                    for (int i = 0; i < expectedJSONArray.length(); i++) {
                        String expectedItem = expectedJSONArray.getJSONObject(i).toString();
                        String actualItem = actualJSONArray.getJSONObject(i).toString();
                        assertJsonStringEqualsIgnoreNulls(expectedItem, actualItem);
                    }
                }
                else {
                    fail("expected: " + expectedValue + " got:" + actualValue);
                }
            }
            else {
                Assert.assertEquals(expectedValue, actualValue);
            }
        }
    }

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

    public static String convertRequest(ObjectMapper objectMapper, String msoRequestDetailsFileName) throws IOException {

        ClassLoader cl = pProbeMsoApiTest.class.getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource[] resources;
        try {
            resources = resolver.getResources(msoRequestDetailsFileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        String content;
        try {
            File file = resources[0].getFile();
            content = new Scanner(file).useDelimiter("\\Z").next();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        return objectMapper.writeValueAsString(objectMapper.readValue(content, Object.class));
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
