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

import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;
import static org.apache.commons.lang3.exception.ExceptionUtils.getThrowableList;
import static org.onap.vid.utils.Streams.not;

import com.att.eelf.configuration.EELFLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableList;
import io.joshworks.restclient.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.utils.Unchecked.UncheckedThrowingSupplier;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class Logging {

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

    public void logRequest(final EELFLogger logger, final HttpMethod method, final String url, final Object body) {
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

    public void logRequest(final EELFLogger logger, final HttpMethod method, final String url) {
        logger.debug("Sending  {} {}", method.name(), url);
    }

    public <T> void logResponse(final EELFLogger logger, final HttpMethod method, final String url, final Response response, final Class<T> entityClass) {
        if (!logger.isDebugEnabled()) {
            return;
        }
        if (response == null) {
            logger.debug("Received {} {} response: null", method.name(), url);
            return;
        }
        try {
            response.bufferEntity();
            logger.debug("Received {} {} Status: {} . Body: {}", method.name(), url, response.getStatus(), new Substring(response.readEntity(entityClass)));
        }
        catch (Exception e) {
            logger.debug("Received {} {} Status: {} . Failed to read response as {}", method.name(), url, response.getStatus(), entityClass.getName());
        }
    }

    public <T> void logResponse(final EELFLogger logger, final HttpMethod method, final String url, final HttpResponse<T> response) {
        try {
            logger.debug("Received {} {} Status: {} . Body: {}", method.name(),
                url, response.getStatus(), new Substring(IOUtils.toString(response.getRawBody(), StandardCharsets.UTF_8)));
            response.getRawBody().reset();
        }
        catch (Exception e) {
            logger.debug("Received {} {} Status: {} . Failed to read response", method.name(), url, response.getStatus());
        }
    }

    public void logResponse(final EELFLogger logger, final HttpMethod method, final String url, final Response response) {
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

    /**
     * This class defers the toString() and truncation to the point in time where logger needs it.
     * This will save some bytes in memory if logger will decide to discard the logging (mostly because logging level
     * is filtering the message out).
     */
    static class Substring {
        private final Object obj;
        private final int maxLen = 1_000_000;

        public Substring(Object obj) {
            this.obj = obj;
        }

        @Override
        public String toString() {
            // null safe truncation
            return StringUtils.left(Objects.toString(obj), maxLen);
        }
    }

    /**
     * in order to be able to write the correct data while creating the node on a new thread save a copy of the current
     * thread's context map, with keys and values of type String.
     */
    public <T> Callable<T> withMDC(Map<String, String> copyOfParentMDC, Callable<T> callable) {
        return () -> withMDCInternal(copyOfParentMDC, callable::call);
    }

    /**
     * in order to be able to write the correct data while creating the node on a new thread save a copy of the current
     * thread's context map, with keys and values of type String.
     */
    public <T, U> Function<T, U> withMDC(Map<String, String> copyOfParentMDC, Function<T, U> function) {
        return t -> withMDCInternal(copyOfParentMDC, () -> function.apply(t));
    }

    <T> T withMDCInternal(Map<String, String> copyOfParentMDC, UncheckedThrowingSupplier<T> supplier) {
        try {
            MDC.setContextMap(defaultIfNull(copyOfParentMDC, emptyMap()));
            return supplier.get();
        } finally {
            MDC.clear();
        }
    }

}
