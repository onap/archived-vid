package org.onap.vid.utils;

import com.att.eelf.configuration.EELFLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Optional;

import static org.onap.vid.utils.Streams.not;

public class Logging {

    private Logging() {
    }

    public static final String HTTP_REQUESTS_OUTGOING = "http.requests.outgoing.";

    public static final String requestIdHeaderKey = SystemProperties.ECOMP_REQUEST_ID;

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String getMethodName() {
        return getMethodName(0);
    }

    public static String getMethodCallerName() {
        return getMethodName(1);
    }

    private static String getMethodName(int depth) {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String thisClassName = stackTrace[1].getClassName();
        final Optional<String> caller =
                Arrays.stream(stackTrace)
                        .skip(1)
                        .filter(not(frame -> frame.getClassName().equals(thisClassName)))
                        .skip(depth)
                        .map(StackTraceElement::getMethodName)
                        .findFirst();
        return caller.orElse("<unknonwn method name>");
    }

    public static EELFLogger getRequestsLogger(String serverName) {
        return EELFLoggerDelegate.getLogger(HTTP_REQUESTS_OUTGOING +serverName);
    }

    public static void logRequest(final EELFLogger logger, final HttpMethod method, final String url, final Object body) {
        if (!logger.isDebugEnabled()) {
            return;
        }

        if (body == null) {
            logRequest(logger, method, url);
            return;
        }

        try {
            String bodyAsJson = objectMapper.writeValueAsString(body);
            logger.debug("Sending  {} {} Body: {}", method.name(), url, bodyAsJson);
        } catch (JsonProcessingException e) {
            logRequest(logger, method, url);
            logger.debug("Failed to parse object in logRequest. {}", body);
        }
    }

    public static void logRequest(final EELFLogger logger, final HttpMethod method, final String url) {
        logger.debug("Sending  {} {}", method.name(), url);
    }

    public static <T> void logResponse(final EELFLogger logger, final HttpMethod method, final String url, final Response response, final Class<T> entityClass) {
        if (!logger.isDebugEnabled()) {
            return;
        }
        if (response == null) {
            logger.debug("Received {} {} response: null", method.name(), url);
            return;
        }
        try {
            response.bufferEntity();
            logger.debug("Received {} {} Status: {} . Body: {}", method.name(), url, response.getStatus(), response.readEntity(entityClass));
        }
        catch (ProcessingException | IllegalStateException e) {
            logger.debug("Received {} {} Status: {} . Failed to read response as {}", method.name(), url, response.getStatus(), entityClass.getName());
        }
    }

    public static void logResponse(final EELFLogger logger, final HttpMethod method, final String url, final Response response) {
        logResponse(logger, method, url, response, String.class);
    }

    public static HttpServletRequest getHttpServletRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }


}
