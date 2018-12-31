package org.onap.vid.utils;

import com.att.eelf.configuration.EELFLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableList;
import io.joshworks.restclient.http.HttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.apache.commons.lang3.exception.ExceptionUtils.getThrowableList;
import static org.onap.vid.utils.Streams.not;

public class Logging {

    private Logging() {
    }

    public static final String HTTP_REQUESTS_OUTGOING = "http.requests.outgoing.";

    public static final String REQUEST_ID_HEADER_KEY = SystemProperties.ECOMP_REQUEST_ID;
    public static final String ONAP_REQUEST_ID_HEADER_KEY = "X-ONAP-RequestID";


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

    public static <T> void logResponse(final EELFLogger logger, final HttpMethod method, final String url, final HttpResponse<T> response) {
        try {
            logger.debug("Received {} {} Status: {} . Body: {}", method.name(), url, response.getStatus(), response.getBody());
        }
        catch (ProcessingException | IllegalStateException e) {
            logger.debug("Received {} {} Status: {} . Failed to read response", method.name(), url, response.getStatus());
        }
    }

    public static void logResponse(final EELFLogger logger, final HttpMethod method, final String url, final Response response) {
        logResponse(logger, method, url, response, String.class);
    }

    public static HttpServletRequest getHttpServletRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    public static String extractOrGenerateRequestId() {
        try {
            return getHttpServletRequest().getHeader(REQUEST_ID_HEADER_KEY);
        }
        catch (IllegalStateException e) {
            //in async jobs we don't have any HttpServletRequest
            return UUID.randomUUID().toString();
        }
    }

    public static void debugRequestDetails(Object requestDetails, final EELFLogger logger) {
        if (logger.isDebugEnabled()) {
            String requestDetailsAsString;
            try {
                requestDetailsAsString = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(requestDetails);
            } catch (JsonProcessingException e) {
                requestDetailsAsString = "error: cannot stringify RequestDetails";
            }
            logger.debug("requestDetailsAsString: {}", requestDetailsAsString);
        }
    }

    public static String exceptionToDescription(Throwable exceptionToDescribe) {
        // Ignore top-most GenericUnchecked or Runtime exceptions that has no added message
        final Throwable top = getThrowableList(exceptionToDescribe).stream()
                .filter(not(e -> ImmutableList.of(GenericUncheckedException.class, RuntimeException.class).contains(e.getClass())
                        && StringUtils.equals(e.getMessage(), e.getCause() == null ? null : e.getCause().toString())))
                .findFirst().orElse(exceptionToDescribe);

        final Throwable root = defaultIfNull(getRootCause(top), top);

        String rootToString = root.toString();

        // nullPointer description will include some context
        if (root.getClass().equals(NullPointerException.class) && root.getStackTrace().length > 0) {
            rootToString = String.format("NullPointerException at %s:%d",
                    root.getStackTrace()[0].getFileName(),
                    root.getStackTrace()[0].getLineNumber());
        }

        // if input is a single exception, without cause: top.toString
        // else: return top.toString + root.toString
        //       but not if root is already described in top.toString
        if (top.equals(root)) {
            return rootToString;
        } else {
            final String topToString = top.toString();
            if (topToString.contains(root.getClass().getName()) && topToString.contains(root.getLocalizedMessage())) {
                return topToString;
            } else {
                return topToString + ": " + rootToString;
            }
        }
    }


}
