package org.onap.vid.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;
import org.onap.portalsdk.core.util.SystemProperties.SecurityEventTypeEnum;


public class VidLoggerAspectTest {

    private VidLoggerAspect createTestSubject() {
        return new VidLoggerAspect();
    }

    @Test
    public void testVidControllers() throws Exception {
        VidLoggerAspect testSubject;

        // default test
        testSubject = createTestSubject();
        testSubject.vidControllers();
    }

}