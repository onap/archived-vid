package org.onap.vid.logging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onap.logging.filter.spring.LoggingInterceptor;
import org.onap.logging.ref.slf4j.ONAPLogConstants;
import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;

public class VidLoggingInterceptor extends LoggingInterceptor {

    private static final String INBOUND_INVO_ID = "InboundInvoId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean result = super.preHandle(request, response, handler);
        MDC.put(INBOUND_INVO_ID, MDC.get(ONAPLogConstants.MDCs.INVOCATION_ID));
        return result;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        MDC.put(ONAPLogConstants.MDCs.INVOCATION_ID, MDC.get(INBOUND_INVO_ID));
        super.postHandle(request, response, handler, modelAndView);
    }

}
