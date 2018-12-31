package org.onap.vid.utils;

import com.att.eelf.configuration.EELFLogger;
import org.junit.Test;

public class LoggingTest {

    @Test
    public void testGetMethodName() throws Exception {
        String result;

        // default test
        result = Logging.getMethodName();
    }

    @Test
    public void testGetMethodCallerName() throws Exception {
        String result;

        // default test
        result = Logging.getMethodCallerName();
    }

    @Test
    public void testGetRequestsLogger() throws Exception {
        String serverName = "";
        EELFLogger result;

        // default test
        result = Logging.getRequestsLogger(serverName);
    }


}