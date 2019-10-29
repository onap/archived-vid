package org.onap.vid.logging;

import static org.onap.vid.logging.LoggingFilterHelper.updateInvocationIDInMdcWithHeaderValue;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import org.onap.logging.filter.base.MetricLogClientFilter;
import org.onap.logging.ref.slf4j.ONAPLogConstants;

public class VidMetricLogClientFilter extends MetricLogClientFilter {

    @Override
    protected void additionalPre(ClientRequestContext clientRequestContext, MultivaluedMap<String, Object> stringObjectMultivaluedMap) {
        updateInvocationIDInMdcWithHeaderValue(
            ()->(String)stringObjectMultivaluedMap.getFirst(ONAPLogConstants.Headers.INVOCATION_ID)
        );
    }
}
