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


import static org.onap.portalsdk.core.util.SystemProperties.ECOMP_REQUEST_ID;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@WebFilter(urlPatterns = "/*")
public class PromiseRequestIdFilter extends GenericFilterBean {

    private static final String REQUEST_ID_RESPONSE_HEADER = ECOMP_REQUEST_ID + "-echo";

    private static final Pattern uuidRegex = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            request = wrapIfNeeded(request);

            if (response instanceof HttpServletResponse) {
                final String actualRequestId = ((HttpServletRequest) request).getHeader(ECOMP_REQUEST_ID);
                ((HttpServletResponse) response).addHeader(REQUEST_ID_RESPONSE_HEADER, actualRequestId);
            }
        }

        chain.doFilter(request, response);
    }

    public ServletRequest wrapIfNeeded(ServletRequest request) {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String originalRequestId = incomingRequestId(httpRequest);

        if (isWrapNeeded(originalRequestId)) {
            request = new PromiseRequestIdRequestWrapper(httpRequest);
        }

        return request;
    }

    private boolean verifyAndValidateUuid(String value) {
        return uuidRegex.matcher(value).matches();
    }

    protected boolean isWrapNeeded(String originalRequestId) {
        return StringUtils.isEmpty(originalRequestId)
            || !verifyAndValidateUuid(originalRequestId);
    }

    protected String incomingRequestId(HttpServletRequest httpRequest) {
        return httpRequest.getHeader(ECOMP_REQUEST_ID);
    }

    private static class PromiseRequestIdRequestWrapper extends HttpServletRequestWrapper {

        private final UUID requestId;

        PromiseRequestIdRequestWrapper(HttpServletRequest request) {
            super(request);
            requestId = UUID.randomUUID();
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

            if (null == super.getHeader(ECOMP_REQUEST_ID)) {
                return Collections.enumeration(ImmutableList.<String>builder()
                    .add(ECOMP_REQUEST_ID)
                    .addAll(Collections.list(super.getHeaderNames()))
                    .build());
            }

            return super.getHeaderNames();
        }

        private boolean isRequestIdHeaderName(String name) {
            return ECOMP_REQUEST_ID.equalsIgnoreCase(name);
        }
    }
}
