package org.onap.vid.utils;

import com.fasterxml.jackson.databind.JsonMappingException;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonParseException;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import sun.security.provider.certpath.SunCertPathBuilderException;
import sun.security.validator.ValidatorException;

import javax.crypto.BadPaddingException;
import javax.net.ssl.SSLHandshakeException;
import javax.ws.rs.ProcessingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.onap.vid.testUtils.RegExMatcher.matchesRegEx;

public class LoggingUtilsTest {

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
                "org.codehaus.jackson.JsonParseException: Unexpected character ('<' (code 60)):" +
                " expected a valid value (number, String, array, object, 'true', 'false' or 'null')\n" +
                " at [Source: <html>i'm an error</html>; line: 1, column: 1]";

        RuntimeException fasterxmlMappingException = new RuntimeException(new JsonMappingException("Can not deserialize instance of java.lang.String out of START_ARRAY token",
                new com.fasterxml.jackson.core.JsonLocation("{ example json }", 15, 1, 20)));
        String fasterxmlMappingDescription = "" +
                "com.fasterxml.jackson.databind.JsonMappingException: Can not deserialize instance of java.lang.String out of START_ARRAY token\n" +
                " at [Source: { example json }; line: 1, column: 20]";

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