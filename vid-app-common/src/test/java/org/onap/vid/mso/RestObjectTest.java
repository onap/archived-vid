package org.onap.vid.mso;

import org.apache.poi.hssf.record.formula.functions.T;
import org.junit.Test;

public class RestObjectTest {

    private RestObject createTestSubject() {
        return new RestObject();
    }

    @Test
    public void testSet() throws Exception {
        RestObject testSubject;
        T t = null;

        // default test
        testSubject = createTestSubject();
        testSubject.set(t);
    }

    @Test
    public void testGet() throws Exception {
        RestObject testSubject;

        // default test
        testSubject = createTestSubject();
        testSubject.get();
    }

    @Test
    public void testSetStatusCode() throws Exception {
        RestObject testSubject;
        int v = 0;

        // default test
        testSubject = createTestSubject();
        testSubject.setStatusCode(v);
    }

    @Test
    public void testGetStatusCode() throws Exception {
        RestObject testSubject;
        int result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getStatusCode();
    }

    @Test
    public void testGetRaw() throws Exception {
        RestObject testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRaw();
    }

    @Test
    public void testSetRaw() throws Exception {
        RestObject testSubject;
        String rawT = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setRaw(rawT);
    }

    @Test
    public void testToString() throws Exception {
        RestObject testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }
}