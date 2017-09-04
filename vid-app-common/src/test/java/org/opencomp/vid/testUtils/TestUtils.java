package org.opencomp.vid.testUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

import java.util.Iterator;

import static fj.parser.Parser.fail;

/**
 * Created by Oren on 6/7/17.
 */
public class TestUtils {

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
}
