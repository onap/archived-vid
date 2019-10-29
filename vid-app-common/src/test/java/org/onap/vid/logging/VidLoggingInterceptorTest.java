package org.onap.vid.logging;

import static org.onap.vid.logging.VidLoggingInterceptor.INBOUND_INVO_ID;
import static org.testng.Assert.assertEquals;

import org.onap.logging.ref.slf4j.ONAPLogConstants;
import org.slf4j.MDC;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class VidLoggingInterceptorTest {

    private VidLoggingInterceptor interceptor;

    @BeforeMethod
    public void setup() {
        this.interceptor = new VidLoggingInterceptor();
    }

    @Test
    public void testAdditionalPreHandling() {
        MDC.put(ONAPLogConstants.MDCs.INVOCATION_ID, "987");
        this.interceptor.additionalPreHandling(null);
        assertEquals(MDC.get(INBOUND_INVO_ID), "987");
    }

}
