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

package org.onap.vid.utils;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.onap.vid.testUtils.RegExMatcher.matchesRegEx;
import static org.testng.AssertJUnit.assertEquals;

import com.att.eelf.configuration.EELFLogger;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.joshworks.restclient.http.HttpResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.crypto.BadPaddingException;
import javax.net.ssl.SSLHandshakeException;
import javax.ws.rs.ProcessingException;
import org.apache.commons.io.IOUtils;
import org.mockito.ArgumentCaptor;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.testUtils.TestUtils;
import org.springframework.http.HttpMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sun.security.provider.certpath.SunCertPathBuilderException;
import sun.security.validator.ValidatorException;

public class LoggingUtilsTest {

    private static final String TEST_OBJECT_JSON = "{\"key\":\"myNumber\",\"value\":42}";

    public static class TestModel {
        public String key;
        public int value;

        public TestModel(String key, int value) {
            this.key = key;
            this.value = value;
        }

        public TestModel() {}
    }

    private EELFLogger loggerMock;

    private Logging logginService = new Logging();
    private String url = "someUrl";
    private final TestModel testObject = new TestModel("myNumber", 42);


    @BeforeMethod
    public void setUp() {
        loggerMock = mock(EELFLogger.class);
    }

    @Test
    public void whenLogRequest_thenLoggedInDebug() {
        //when
        logginService.logRequest(loggerMock, HttpMethod.GET, url);

        //then
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        verify(loggerMock).debug(contains("Sending"), argumentCaptor.capture());
        assertEquals("GET", argumentCaptor.getAllValues().get(0));
        assertEquals(url, argumentCaptor.getAllValues().get(1));
    }



    @Test
    public void whenLogResponseOfHttpResponse_thenLoggedInDebug() throws Exception {
        HttpResponse<TestModel> response = TestUtils.createTestHttpResponse(200, testObject, TestModel.class);
        logginService.logResponse(loggerMock, HttpMethod.POST, url, response);

        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        ArgumentCaptor<String> messageCaptur = ArgumentCaptor.forClass(String.class);
        verify(loggerMock).debug(messageCaptur.capture(), argumentCaptor.capture());

        assertThat(messageCaptur.getValue(), matchesPattern("Received.*Status.*Body.*"));
        assertEquals("POST", argumentCaptor.getAllValues().get(0));
        assertEquals(url, argumentCaptor.getAllValues().get(1));
        assertEquals(200, argumentCaptor.getAllValues().get(2));
        assertEquals(TEST_OBJECT_JSON, argumentCaptor.getAllValues().get(3));
    }

    @Test
    public void whenLogResponseOfHttpResponse_thenCanReadEntityAfterwards() throws Exception {
        HttpResponse<TestModel> response = TestUtils.createTestHttpResponse(200, testObject, TestModel.class);
        logginService.logResponse(loggerMock, HttpMethod.POST, url, response);
        assertThat(response.getBody(), jsonEquals(TEST_OBJECT_JSON));
    }

    @Test
    public void whenLogResponseOfHttpResponse_thenCanReadRawEntityAfterwards() throws Exception {
        HttpResponse<TestModel> response = TestUtils.createTestHttpResponse(200, testObject, TestModel.class);
        logginService.logResponse(loggerMock, HttpMethod.POST, url, response);
        assertThat(IOUtils.toString(response.getRawBody(), StandardCharsets.UTF_8), jsonEquals(TEST_OBJECT_JSON));
    }

    @DataProvider
    public static Object[][] exceptions() {
        Exception e0 = new CertificateException("No X509TrustManager implementation available");
        Exception noTrustMngrImplException = new SSLHandshakeException(e0.toString());
        noTrustMngrImplException.initCause(e0);

        Exception e1 = new BadPaddingException("Given final block not properly padded");
        Exception incorrectPasswordException = new IOException("keystore password was incorrect",
                new UnrecoverableKeyException("failed to decrypt safe contents entry: " + e1));
        String incorrectPasswordExceptionDescription = "" +
                "java.io.IOException: keystore password was incorrect: " +
                "java.security.UnrecoverableKeyException: failed to decrypt safe contents entry: " +
                "javax.crypto.BadPaddingException: Given final block not properly padded";

        Exception e2 = new SunCertPathBuilderException("unable to find valid certification path to requested target");
        Exception noValidCert = new ProcessingException(new ValidatorException("PKIX path building failed: " + e2.toString(), e2));
        String noValidCertDescription = "" +
                "javax.ws.rs.ProcessingException: " +
                "sun.security.validator.ValidatorException: PKIX path building failed: " +
                "sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target";

        RuntimeException codehausParseException = new RuntimeException(new JsonParseException("Unexpected character ('<' (code 60)):" +
                " expected a valid value (number, String, array, object, 'true', 'false' or 'null')",
                new JsonLocation("<html>i'm an error</html>", 25, 1, 1)));
        String codehausParseDescription = "" +
                "com.fasterxml.jackson.core.JsonParseException: Unexpected character ('<' (code 60)):" +
                " expected a valid value (number, String, array, object, 'true', 'false' or 'null')\n" +
                " at [Source: (String)\"<html>i'm an error</html>\"; line: 1, column: 1]";

        RuntimeException fasterxmlMappingException = new RuntimeException(new JsonMappingException("Can not deserialize instance of java.lang.String out of START_ARRAY token",
                new com.fasterxml.jackson.core.JsonLocation("{ example json }", 15, 1, 20)));
        String fasterxmlMappingDescription = "" +
                "com.fasterxml.jackson.databind.JsonMappingException: Can not deserialize instance of java.lang.String out of START_ARRAY token\n" +
                " at [Source: (String)\"{ example json }\"; line: 1, column: 20]";

        return new Object[][]{
                {"javax.net.ssl.SSLHandshakeException: java.security.cert.CertificateException: No X509TrustManager implementation available",
                        noTrustMngrImplException},
                {"java.lang.StringIndexOutOfBoundsException: String index out of range: 4",
                        new StringIndexOutOfBoundsException(4)},
                {"java.io.FileNotFoundException: vid/WEB-INF/cert/aai-client-cert.p12",
                        new FileNotFoundException("vid/WEB-INF/cert/aai-client-cert.p12")},
                {"NullPointerException at LoggingUtilsTest.java:[0-9]+",
                        new NullPointerException("null")},
                {incorrectPasswordExceptionDescription,
                        incorrectPasswordException},
                {incorrectPasswordExceptionDescription,
                        new GenericUncheckedException(incorrectPasswordException)},
                {"javax.ws.rs.ProcessingException: javax.net.ssl.SSLHandshakeException: Received fatal alert: certificate_expired",
                        new ProcessingException(new SSLHandshakeException("Received fatal alert: certificate_expired"))},
                {noValidCertDescription,
                        noValidCert},
                {escapeBrackets(codehausParseDescription),
                        codehausParseException},
                {escapeBrackets(fasterxmlMappingDescription),
                        fasterxmlMappingException},
                {"org.onap.vid.exceptions.GenericUncheckedException: top message: org.onap.vid.exceptions.GenericUncheckedException: root message",
                        new GenericUncheckedException("top message", new IOException("sandwich message", new GenericUncheckedException("root message")))},
                {"org.onap.vid.exceptions.GenericUncheckedException: basa",
                        new GenericUncheckedException("basa")}
        };

    }

    @Test(dataProvider = "exceptions")
    public void testExceptionToDescription(String expectedDescription, Exception exceptionToDescribe) {
        String expectedButDotsEscaped = expectedDescription.replace(".", "\\.");

        assertThat(Logging.exceptionToDescription(exceptionToDescribe), matchesRegEx(expectedButDotsEscaped));
    }

    private static String escapeBrackets(String in) {
        return in.replaceAll("[\\(\\[\\{\\)]", "\\\\$0");
    }
}
