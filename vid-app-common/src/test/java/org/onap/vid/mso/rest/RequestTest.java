package org.onap.vid.mso.rest;

import org.junit.Test;
import org.onap.vid.domain.mso.InstanceIds;
import org.onap.vid.domain.mso.RequestStatus;

public class RequestTest {

    private Request createTestSubject() {
        return new Request();
    }

    @Test
    public void testGetInstanceIds() throws Exception {
        Request testSubject;
        InstanceIds result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getInstanceIds();
    }

    @Test
    public void testSetInstanceIds() throws Exception {
        Request testSubject;
        InstanceIds instanceIds = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setInstanceIds(instanceIds);
    }

    @Test
    public void testGetRequestDetails() throws Exception {
        Request testSubject;
        RequestDetails result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRequestDetails();
    }

    @Test
    public void testSetRequestDetails() throws Exception {
        Request testSubject;
        RequestDetails requestDetails = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setRequestDetails(requestDetails);
    }

    @Test
    public void testGetRequestStatus() throws Exception {
        Request testSubject;
        RequestStatus result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRequestStatus();
    }

    @Test
    public void testSetRequestStatus() throws Exception {
        Request testSubject;
        RequestStatus requestStatus = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setRequestStatus(requestStatus);
    }

    @Test
    public void testToString() throws Exception {
        Request testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.toString();
    }

    @Test
    public void testEquals() throws Exception {
        Request testSubject;
        Object other = null;
        boolean result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.equals(other);
    }

    @Test
    public void testHashCode() throws Exception {
        Request testSubject;
        int result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.hashCode();
    }
}