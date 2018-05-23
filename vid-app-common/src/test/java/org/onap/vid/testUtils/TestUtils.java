package org.onap.vid.testUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.onap.vid.asdc.beans.Service;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import static fj.parser.Parser.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

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
                    JSONArray actualJSONArray = (JSONArray)actualValue;
                    for (int i = 0; i < expectedJSONArray.length(); i++) {
                        String expectedItem = expectedJSONArray.get(i).toString();
                        String actualItem = actualJSONArray.get(i).toString();
                        if (expectedValue instanceof JSONObject)
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

    public static <T> T readJsonResourceFileAsObject(String pathInResource, Class<T> valueType) throws IOException {
        return readJsonResourceFileAsObject(pathInResource, valueType, false);
    }

    public static <T> T readJsonResourceFileAsObject(String pathInResource, Class<T> valueType, boolean ignoreUnknownProperties)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, ignoreUnknownProperties);
        return objectMapper.readValue(
                TestUtils.class.getResource(pathInResource),
                valueType);
    }


    public static class JavaxRsClientMocks {
        private final javax.ws.rs.client.Client fakeClient;
        private final javax.ws.rs.client.Invocation.Builder fakeBuilder;
        private final Response fakeResponse;

        public javax.ws.rs.client.Client getFakeClient() {
            return fakeClient;
        }

        public javax.ws.rs.client.Invocation.Builder getFakeBuilder() {
            return fakeBuilder;
        }

        public Response getFakeResponse() {
            return fakeResponse;
        }

        public JavaxRsClientMocks() {
            final MockSettings mockSettings = withSettings().defaultAnswer(new TriesToReturnMockByType());

            fakeClient = mock(javax.ws.rs.client.Client.class, mockSettings);
            fakeBuilder = mock(javax.ws.rs.client.Invocation.Builder.class, mockSettings);
            fakeResponse = mock(Response.class, mockSettings);
            final javax.ws.rs.client.WebTarget fakeWebTarget = mock(javax.ws.rs.client.WebTarget.class, mockSettings);

            TriesToReturnMockByType.setAvailableMocks(
                    fakeClient,
                    fakeWebTarget,
                    fakeBuilder,
                    fakeResponse
            );

            Mockito.when(fakeBuilder.get(any(Class.class))).thenReturn(null);
            Mockito.when(fakeBuilder.get(eq(InputStream.class))).thenReturn(new ByteArrayInputStream(new byte[]{}));
            Mockito.when(fakeBuilder.get(any(GenericType.class))).thenReturn(null);

            Mockito.when(fakeResponse.getStatus()).thenReturn(200);
            Mockito.when(fakeResponse.getStatusInfo()).thenReturn(Response.Status.OK);
            Mockito.when(fakeResponse.readEntity(Service.class)).thenReturn(null);
        }
    }

    /*
       inspired out from newer Mockito version
        returns a mock from given list if it's a matching return-type
    */
    private static class TriesToReturnMockByType implements Answer<Object>, Serializable {
        private final Answer<Object> defaultReturn = RETURNS_DEFAULTS;
        private static List<Object> availableMocks = ImmutableList.of();

        static void setAvailableMocks(Object... mocks) {
            availableMocks = ImmutableList.copyOf(mocks);
        }

        public Object answer(InvocationOnMock invocation) throws Throwable {
            Class<?> methodReturnType = invocation.getMethod().getReturnType();

            return availableMocks.stream()
                    .filter(mock -> methodReturnType.isAssignableFrom(mock.getClass()))
                    //.peek(m -> System.out.println("found a mock: " + m.getClass().getName()))
                    .findFirst()
                    .orElse(defaultReturn.answer(invocation));
        }
    }
}
