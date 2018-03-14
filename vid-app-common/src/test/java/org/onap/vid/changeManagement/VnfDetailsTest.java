package org.onap.vid.changeManagement;

import org.junit.Assert;
import org.junit.Test;

public class VnfDetailsTest {

    private VnfDetails createTestSubject() {
        return new VnfDetails();
    }

    @Test
    public void testGetUUID() throws Exception {
        VnfDetails testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getUUID();
    }

    @Test
    public void testSetUUID() throws Exception {
        VnfDetails testSubject;
        String uUID = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setUUID(uUID);
    }

    @Test
    public void testGetInvariantUUID() throws Exception {
        VnfDetails testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getInvariantUUID();
    }

    @Test
    public void testSetInvariantUUID() throws Exception {
        VnfDetails testSubject;
        String invariantUUID = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setInvariantUUID(invariantUUID);
    }

    @Test
    public void testEquals() throws Exception {
        VnfDetails testSubject;
        Object o = null;
        boolean result;

        // test 1
        testSubject = createTestSubject();
        o = null;
        result = testSubject.equals(o);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testHashCode() throws Exception {
        VnfDetails testSubject;
        int result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.hashCode();
    }

    @Test
    public void testToString() throws Exception {
        VnfDetails testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }
}