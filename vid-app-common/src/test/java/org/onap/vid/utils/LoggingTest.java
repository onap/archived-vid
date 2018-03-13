package org.onap.vid.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.record.formula.functions.T;
import org.junit.Test;
import org.springframework.http.HttpMethod;

import com.att.eelf.configuration.EELFLogger;

public class LoggingTest {

    private Logging createTestSubject() {
        return new Logging();
    }

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