package org.onap.vid.model;

import org.junit.Test;

public class ServiceInstanceSearchResultTest {

    private ServiceInstanceSearchResult createTestSubject() {
        return new ServiceInstanceSearchResult();
    }

    @Test
    public void testGetServiceInstanceId() throws Exception {
        ServiceInstanceSearchResult testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getServiceInstanceId();
    }

    @Test
    public void testSetServiceInstanceId() throws Exception {
        ServiceInstanceSearchResult testSubject;
        String serviceInstanceId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setServiceInstanceId(serviceInstanceId);
    }

    @Test
    public void testGetGlobalCustomerId() throws Exception {
        ServiceInstanceSearchResult testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getGlobalCustomerId();
    }

    @Test
    public void testSetGlobalCustomerId() throws Exception {
        ServiceInstanceSearchResult testSubject;
        String globalCustomerId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setGlobalCustomerId(globalCustomerId);
    }

    @Test
    public void testGetServiceType() throws Exception {
        ServiceInstanceSearchResult testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getServiceType();
    }

    @Test
    public void testSetServiceType() throws Exception {
        ServiceInstanceSearchResult testSubject;
        String serviceType = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setServiceType(serviceType);
    }

    @Test
    public void testGetServiceInstanceName() throws Exception {
        ServiceInstanceSearchResult testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getServiceInstanceName();
    }

    @Test
    public void testSetServiceInstanceName() throws Exception {
        ServiceInstanceSearchResult testSubject;
        String serviceInstanceName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setServiceInstanceName(serviceInstanceName);
    }

    @Test
    public void testGetSubscriberName() throws Exception {
        ServiceInstanceSearchResult testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getSubscriberName();
    }

    @Test
    public void testSetSubscriberName() throws Exception {
        ServiceInstanceSearchResult testSubject;
        String subscriberName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setSubscriberName(subscriberName);
    }

    @Test
    public void testGetAaiModelInvariantId() throws Exception {
        ServiceInstanceSearchResult testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAaiModelInvariantId();
    }

    @Test
    public void testSetAaiModelInvariantId() throws Exception {
        ServiceInstanceSearchResult testSubject;
        String aaiModelInvariantId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setAaiModelInvariantId(aaiModelInvariantId);
    }

    @Test
    public void testGetAaiModelVersionId() throws Exception {
        ServiceInstanceSearchResult testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAaiModelVersionId();
    }

    @Test
    public void testSetAaiModelVersionId() throws Exception {
        ServiceInstanceSearchResult testSubject;
        String aaiModelVersionId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setAaiModelVersionId(aaiModelVersionId);
    }

    @Test
    public void testGetIsPermitted() throws Exception {
        ServiceInstanceSearchResult testSubject;
        boolean result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getIsPermitted();
    }

    @Test
    public void testSetIsPermitted() throws Exception {
        ServiceInstanceSearchResult testSubject;
        boolean isPermitted = false;

        // default test
        testSubject = createTestSubject();
        testSubject.setIsPermitted(isPermitted);
    }

    @Test
    public void testEquals() throws Exception {
        ServiceInstanceSearchResult testSubject;
        Object other = null;
        boolean result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.equals(other);
    }

}