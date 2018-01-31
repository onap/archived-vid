package org.onap.vid.controller.filter;


import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.UUID;

import static org.openecomp.portalsdk.core.util.SystemProperties.ECOMP_REQUEST_ID;

@WebFilter(urlPatterns = "/*")
public class PromiseEcompRequestIdFilter extends GenericFilterBean {

    private final static EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(PromiseEcompRequestIdFilter.class);
    private final static String REQUEST_ID_RESPONSE_HEADER = ECOMP_REQUEST_ID + "-echo";


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

    public static ServletRequest wrapIfNeeded(ServletRequest request) {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final String originalRequestId = httpRequest.getHeader(ECOMP_REQUEST_ID);

        if (StringUtils.isEmpty(originalRequestId)) {
            request = new PromiseEcompRequestIdRequestWrapper(httpRequest);
        }

        return request;
    }

    private static class PromiseEcompRequestIdRequestWrapper extends HttpServletRequestWrapper {

        private final UUID requestId;

        PromiseEcompRequestIdRequestWrapper(HttpServletRequest request) {
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
            return Collections.enumeration(ImmutableList.<String>builder()
                    .add(ECOMP_REQUEST_ID)
                    .addAll(Collections.list(super.getHeaderNames()))
                    .build());
        }

        private boolean isRequestIdHeaderName(String name) {
            return ECOMP_REQUEST_ID.equalsIgnoreCase(name);
        }
    }
}