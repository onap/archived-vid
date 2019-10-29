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

import java.util.function.Supplier;
import org.onap.logging.filter.base.AbstractMetricLogFilter;
import org.onap.logging.ref.slf4j.ONAPLogConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LoggingFilterHelper {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractMetricLogFilter.class);

    static void updateInvocationIDInMdcWithHeaderValue(Supplier<String> headerSupplier) {
        //align invocationId with actual header value, due to bug in AbstractMetricLogFilter
        try {
            String invocationId = headerSupplier.get();
            MDC.put(ONAPLogConstants.MDCs.INVOCATION_ID, invocationId);
        }
        catch (Exception e) {
            logger.debug("Failed to retrieve "+ONAPLogConstants.Headers.INVOCATION_ID+" header", e);
        }
    }
}
