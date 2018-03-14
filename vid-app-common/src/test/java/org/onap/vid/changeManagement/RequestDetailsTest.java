package org.onap.vid.changeManagement;

import java.util.List;

import org.junit.Test;

public class RequestDetailsTest {

    private RequestDetails createTestSubject() {
        return new RequestDetails();
    }

    @Test
    public void testGetVnfName() throws Exception {
        RequestDetails testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getVnfName();
    }

    @Test
    public void testSetVnfName() throws Exception {
        RequestDetails testSubject;
        String vnfName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setVnfName(vnfName);
    }

    @Test
    public void testGetVnfInstanceId() throws Exception {
        RequestDetails testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getVnfInstanceId();
    }

    @Test
    public void testSetVnfInstanceId() throws Exception {
        RequestDetails testSubject;
        String vnfInstanceId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setVnfInstanceId(vnfInstanceId);
    }

    @Test
    public void testGetRelatedInstList() throws Exception {
        RequestDetails testSubject;
        List<RelatedInstanceList> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRelatedInstList();
    }

    @Test
    public void testSetRelatedInstList() throws Exception {
        RequestDetails testSubject;
        List<RelatedInstanceList> relatedInstList = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setRelatedInstList(relatedInstList);
    }
}