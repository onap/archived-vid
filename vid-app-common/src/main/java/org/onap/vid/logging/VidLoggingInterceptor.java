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

package org.onap.vid.logging;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onap.logging.filter.spring.LoggingInterceptor;
import org.onap.logging.ref.slf4j.ONAPLogConstants.MDCs;
import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;

public class VidLoggingInterceptor extends LoggingInterceptor {

    static final String INBOUND_INVO_ID = "InboundInvoId";

    private static final String canonicalHostName = getCanonicalName();

    private static String getCanonicalName() {
        try {
            return InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            // YOLO
            return "";
        }
    }

    @Override
    protected void additionalPreHandling(HttpServletRequest request)  {
        super.additionalPreHandling(request);
        storeInboundInvocationId();
    }

    /*
     * store inbound invocationId for later use in restoreInvocationId
     */
    private void storeInboundInvocationId() {
        MDC.put(INBOUND_INVO_ID, MDC.get(MDCs.INVOCATION_ID));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        restoreInvocationId();
        fixServerFQDN();
        super.postHandle(request, response, handler, modelAndView);
    }

    /*
     * fix SERVER_FQDN, because EELFLoggerDelegate::setGlobalLoggingContext put wrong value
     */
    private void fixServerFQDN() {
        MDC.put(MDCs.SERVER_FQDN, canonicalHostName);
    }

    /**
     * restore invocationId that was overwritten by metrics interceptor
     */
    private void restoreInvocationId() {
        MDC.put(MDCs.INVOCATION_ID, MDC.get(INBOUND_INVO_ID));
    }

}
