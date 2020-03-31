/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.testUtils;

import static com.fasterxml.jackson.module.kotlin.ExtensionsKt.jacksonObjectMapper;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;
import static org.apache.http.HttpVersion.HTTP_1_1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;
import static org.onap.vid.utils.KotlinUtilsKt.JOSHWORKS_JACKSON_OBJECT_MAPPER;
import static org.testng.Assert.fail;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.common.collect.ImmutableList;
import io.joshworks.restclient.http.HttpResponse;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.http.HttpResponseFactory;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicStatusLine;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.model.VidNotions;
import org.onap.vid.model.VidNotions.InstantiationType;
import org.onap.vid.model.VidNotions.InstantiationUI;
import org.onap.vid.model.VidNotions.ModelCategory;
import org.onap.vid.mso.model.CloudConfiguration;
import org.springframework.core.env.Environment;
import org.testng.annotations.DataProvider;

/**
 * Created by Oren on 6/7/17.
 */
public class TestUtils {

    private static final Logger logger = LogManager.getLogger(TestUtils.class);

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
                Assert.assertEquals("assertion fail for key:"+key, expectedValue, actualValue);
            }
        }
    }

    public static <T> T readJsonResourceFileAsObject(String pathInResource, Class<T> valueType) {
        return readJsonResourceFileAsObject(pathInResource, valueType, false);
    }

    public static <T> T readJsonResourceFileAsObject(String pathInResource, Class<T> valueType, boolean failOnUnknownProperties) {
        ObjectMapper objectMapper =
                jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return objectMapper.readValue(jsonResourceString, valueType);
        } catch (IOException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    public static <T> T readJsonResourceFileAsObject(String pathInResource, Class<T> valueType) {
        return readJsonResourceFileAsObject(pathInResource, valueType, false);
    }

    public static <T> T readJsonResourceFileAsObject(String pathInResource, Class<T> valueType, boolean failOnUnknownProperties) {
        ObjectMapper objectMapper =
            jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);

        try {
            return objectMapper.readValue(TestUtils.class.getResource(pathInResource), valueType);
        } catch (IOException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    public static String readFileAsString(String pathInResource) {
        try {
            return IOUtils.toString(TestUtils.class.getResource(pathInResource), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] allPropertiesOf(Class<?> aClass) {
        return getPropertyDescriptorsRecursively(aClass).stream()
            .map(PropertyDescriptor::getDisplayName)
            .distinct()
            .toArray(String[]::new);
    }

    private static List<PropertyDescriptor> getPropertyDescriptorsRecursively(Class<?> aClass) {
        List<PropertyDescriptor> result = new LinkedList<>();

        for (Class<?> i = aClass; i != null && i != Object.class; i = i.getSuperclass()) {
            Collections.addAll(result, getPropertyDescriptors(i));
        }

        return result;
    }

    private static <T> List<String> allStringFieldsOf(T object) {
        return FieldUtils.getAllFieldsList(object.getClass()).stream()
            .filter(field -> field.getType().isAssignableFrom(String.class))
            .map(Field::getName)
            .distinct()
            .collect(toList());
    }

    private static List<Field> allMockitoFieldsOf(Object object) {
        final Predicate<Field> hasMockAnnotation = field -> field.getAnnotation(Mock.class) != null;
        final Predicate<Field> hasInjectMocksAnnotation = field -> field.getAnnotation(InjectMocks.class) != null;

        return Arrays.stream(FieldUtils.getAllFields(object.getClass()))
            .filter(hasMockAnnotation.or(hasInjectMocksAnnotation))
            .collect(toList());
    }

    /**
     * Calls MockitoAnnotations.initMocks after nullifying any field which is annotated @Mocke or @InjectMock.
     * This makes a "hard rest" to any mocked state or instance. Expected to be invoked between any @Tests in class, by
     * being called in TestNG's @BeforeMethod (or equivalently JUnit's @BeforeTest).
     */
    public static void initMockitoMocks(Object testClass) {
        for (Field field : allMockitoFieldsOf(testClass)) {
            try {
                // Write null to fields
                FieldUtils.writeField(field, testClass, null, true);
            } catch (ReflectiveOperationException e) {
                ExceptionUtils.rethrow(e);
            }
        }

        MockitoAnnotations.initMocks(testClass);
    }

    /**
     * Sets each String property with a value equal to the name of
     * the property; e.g.: { name: "name", city: "city" }
     * @param object
     * @param <T>
     * @return The modified object
     */
    public static <T> T setStringsInStringFields(T object) {
        allStringFieldsOf(object).forEach(it -> {
            try {
                FieldUtils.writeField(object, it, it, true);
            } catch (IllegalAccessException e) {
                // YOLO
            }
        });

        return object;
    }

    public static void registerCloudConfigurationValueGenerator() {
        BeanMatchers.registerValueGenerator(() -> new CloudConfiguration(
                randomAlphabetic(7), randomAlphabetic(7), randomAlphabetic(7)
            ), CloudConfiguration.class);
    }

    public static void registerVidNotionsValueGenerator() {
        BeanMatchers.registerValueGenerator(() -> new VidNotions(
            randomEnum(InstantiationUI.class), randomEnum(ModelCategory.class),
            randomEnum(InstantiationUI.class), randomEnum(InstantiationType.class)
        ), VidNotions.class);
    }

    private static <T> T randomEnum(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        return values[RandomUtils.nextInt(0, values.length)];
    }

    public static OngoingStubbing<InputStream> mockGetRawBodyWithStringBody(HttpResponse<String> httpResponse, String body) {
        try {
            return when(httpResponse.getRawBody()).thenReturn(IOUtils.toInputStream(body, StandardCharsets.UTF_8.name()));
        } catch (IOException e) {
            ExceptionUtils.rethrow(e);
        }
        return null; //never shall get here
    }

    public static HttpResponse<String> createTestHttpResponse(int statusCode, String entity) throws Exception {
        HttpResponseFactory factory = new DefaultHttpResponseFactory();
        org.apache.http.HttpResponse response = factory.newHttpResponse(new BasicStatusLine(HTTP_1_1, statusCode, null), null);
        if (entity != null) {
            response.setEntity(new StringEntity(entity));
        }
        return new HttpResponse<>(response, String.class, null);
    }

    public static <T> HttpResponse<T> createTestHttpResponse(int statusCode, T entity, final Class<T> entityClass) throws Exception {
        HttpResponseFactory factory = new DefaultHttpResponseFactory();
        org.apache.http.HttpResponse response = factory.newHttpResponse(new BasicStatusLine(HTTP_1_1, statusCode, null), null);
        if (entity != null) {
            InputStream inputStream = IOUtils.toInputStream(JACKSON_OBJECT_MAPPER.writeValueAsString(entity), StandardCharsets.UTF_8.name());
            response.setEntity(new InputStreamEntity(inputStream));
        }
        return new HttpResponse(response, entityClass, JOSHWORKS_JACKSON_OBJECT_MAPPER);
    }


    public static class JavaxRsClientMocks {
        private final javax.ws.rs.client.Client fakeClient;
        private final javax.ws.rs.client.Invocation.Builder fakeBuilder;
        private final javax.ws.rs.client.Invocation fakeInvocation;
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
            fakeInvocation = mock(javax.ws.rs.client.Invocation.class, mockSettings);
            fakeResponse = mock(Response.class, mockSettings);
            final javax.ws.rs.client.WebTarget fakeWebTarget = mock(javax.ws.rs.client.WebTarget.class, mockSettings);

            TriesToReturnMockByType.setAvailableMocks(
                    fakeClient,
                    fakeWebTarget,
                    fakeBuilder,
                    fakeInvocation,
                    fakeResponse
            );
            Mockito.when(fakeBuilder.get(any(Class.class))).thenReturn(null);
            Mockito.when(fakeBuilder.get(any(GenericType.class))).thenReturn(null);
            Mockito.when(fakeResponse.getStatus()).thenReturn(200);
            Mockito.when(fakeResponse.getStatusInfo()).thenReturn(Response.Status.OK);
            Mockito.when(fakeResponse.readEntity(Service.class)).thenReturn(null);
            Mockito.when(fakeResponse.readEntity(InputStream.class)).thenReturn(new ByteArrayInputStream(new byte[]{}));
            Mockito.when(fakeResponse.readEntity(String.class)).thenReturn(null);
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
                    //.peek(m -> logger.info("found a mock: " + m.getClass().getName()))
                    .findFirst()
                    .orElse(defaultReturn.answer(invocation));
        }
    }


    //The method mocks only some methods used in my case
    //You may add some other when for your test here
    public static Response mockResponseForJavaxClient(Client javaxClientMock) {
        Response  mockResponse = mock(Response.class);
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(javaxClientMock.target(any(URI.class))).thenReturn(webTarget);
        when(webTarget.path(any())).thenReturn(webTarget);
        when(webTarget.request(any(MediaType.class))).thenReturn(builder);
        when(builder.headers(any())).thenReturn(builder);
        when(builder.header(any(), any())).thenReturn(builder);
        when(builder.get()).thenReturn(mockResponse);
        return mockResponse;
    }


    public interface Test {

        void apply() throws AsdcCatalogException;
    }

    public static void testWithSystemProperty(String key, String value, Test test) throws Exception {
        SystemProperties systemProperties = new SystemProperties();
        //use reflection to invoke protected method
        Environment originalEnvironment = (Environment) MethodUtils
            .invokeMethod(systemProperties, true, "getEnvironment");

        try {
            Environment environment = mock(Environment.class);
            systemProperties.setEnvironment(environment);
            when(environment.getRequiredProperty(key)).thenReturn(value);
            when(environment.containsProperty(key)).thenReturn(true);
            test.apply();
        }
        finally {
            systemProperties.setEnvironment(originalEnvironment);
        }
    }

    private static RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('0', 'z')
            .filteredBy(LETTERS, DIGITS)
            .build();

    public static String generateRandomAlphaNumeric(int length) {
        return generator.generate(length);
    }

    @DataProvider
    public static Object[][] trueAndFalse() {
        return new Object[][]{{true}, {false}};
    }

    @DataProvider
    public static Object[][] trueAndFalseAndNull() {
        return new Boolean[][]{{Boolean.TRUE}, {Boolean.FALSE}, {null}};
    }

}
