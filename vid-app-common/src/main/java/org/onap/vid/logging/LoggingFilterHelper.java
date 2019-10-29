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
