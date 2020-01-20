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

package org.onap.vid.controller.filter;


import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.onap.portalsdk.core.util.SystemProperties.ECOMP_REQUEST_ID;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import org.onap.logging.ref.slf4j.ONAPLogConstants.MDCs;
import org.onap.vid.logging.Headers;
import org.onap.vid.logging.RequestIdHeader;
import org.slf4j.MDC;
import org.springframework.web.filter.GenericFilterBean;

@WebFilter(urlPatterns = "/*")
public class PromiseRequestIdFilter extends GenericFilterBean {

    // The wrapped request is guaranteed to have the transaction id as the value
    // of the header PROMISED_HEADER_NAME.
    // PROMISED_HEADER_NAME is set to ECOMP_REQUEST_ID as long as
    // org.onap.portalsdk...UserUtils.getRequestId() is using the header
    // "X-ECOMP-RequestID".
    private static final RequestIdHeader PROMISED_HEADER = RequestIdHeader.ECOMP_ID;
    private static final String REQUEST_ID_RESPONSE_HEADER = ECOMP_REQUEST_ID + "-echo";

    private static final Pattern uuidRegex = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", Pattern.CASE_INSENSITIVE);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            request = wrapIfNeeded(request);

            if (response instanceof HttpServletResponse) {
                final String actualRequestId = PROMISED_HEADER.getHeaderValue((HttpServletRequest) request);
                ((HttpServletResponse) response).addHeader(REQUEST_ID_RESPONSE_HEADER, actualRequestId);
            }
        }

        chain.doFilter(request, response);
    }

    public ServletRequest wrapIfNeeded(ServletRequest request) {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;

        final RequestIdHeader highestPriorityHeader = highestPriorityHeader(httpRequest);
        final String originalRequestId = highestPriorityHeader.getHeaderValue(httpRequest);

        if (isWrapNeeded(highestPriorityHeader, originalRequestId)) {
            // Copy originalRequestId to the promised header value
            request = new PromiseRequestIdRequestWrapper(httpRequest,
                toUuidOrElse(originalRequestId, MDC.get(MDCs.REQUEST_ID), UUID::randomUUID));
        }

        return request;
    }

    private boolean verifyAndValidateUuid(String value) {
        return isNotEmpty(value) && uuidRegex.matcher(value).matches();
    }

    private boolean isWrapNeeded(RequestIdHeader highestPriorityHeader, String originalRequestId) {
        boolean headerExistsAndValid =
            PROMISED_HEADER == highestPriorityHeader && verifyAndValidateUuid(originalRequestId);

        return !headerExistsAndValid;
    }

    UUID toUuidOrElse(String uuid1, String uuid2, Supplier<UUID> uuidSupplier) {
        return toUuidOrElse(uuid1, () -> toUuidOrElse(uuid2, uuidSupplier));
    }

    UUID toUuidOrElse(String uuid, Supplier<UUID> uuidSupplier) {
        if (verifyAndValidateUuid(uuid)) {
            try {
                return UUID.fromString(uuid);
            } catch (IllegalArgumentException e) {
                return uuidSupplier.get();
            }
        } else {
            return uuidSupplier.get();
        }
    }

    RequestIdHeader highestPriorityHeader(HttpServletRequest httpRequest) {
        return defaultIfNull(Headers.highestPriorityHeader(httpRequest), PROMISED_HEADER);
    }

    private static class PromiseRequestIdRequestWrapper extends HttpServletRequestWrapper {

        private final UUID requestId;

        PromiseRequestIdRequestWrapper(HttpServletRequest request, @NotNull UUID requestId) {
            super(request);
            this.requestId = requestId;
        }

        @Override
        public String getHeader(String name) {
            return isRequestIdHeaderName(name) ?
                    requestId.toString() : super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if (isRequestIdHeaderName(name)) {
                return Collections.enumeration(Collections.singleton(requestId.toString()));
            } else {
                return super.getHeaders(name);
            }
        }

        @Override
        public Enumeration<String> getHeaderNames() {

            if (null == super.getHeader(PROMISED_HEADER.getHeaderName())) {
                return Collections.enumeration(ImmutableList.<String>builder()
                    .add(PROMISED_HEADER.getHeaderName())
                    .addAll(Collections.list(super.getHeaderNames()))
                    .build());
            }

            return super.getHeaderNames();
        }

        private boolean isRequestIdHeaderName(String name) {
            return PROMISED_HEADER.stringEquals(name);
        }
    }
}
