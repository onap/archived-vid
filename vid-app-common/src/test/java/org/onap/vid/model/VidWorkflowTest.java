package org.onap.vid.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.junit.Test;

public class VidWorkflowTest {

    private VidWorkflow createTestSubject() {
        return new VidWorkflow();
    }

    @Test
    public void testGetId() throws Exception {
        VidWorkflow testSubject;
        Long result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getId();
    }

    @Test
    public void testGetCreated() throws Exception {
        VidWorkflow testSubject;
        Date result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getCreated();
    }

    @Test
    public void testGetModified() throws Exception {
        VidWorkflow testSubject;
        Date result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModified();
    }

    @Test
    public void testGetCreatedId() throws Exception {
        VidWorkflow testSubject;
        Long result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getCreatedId();
    }

    @Test
    public void testGetModifiedId() throws Exception {
        VidWorkflow testSubject;
        Long result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModifiedId();
    }

    @Test
    public void testGetAuditUserId() throws Exception {
        VidWorkflow testSubject;
        Serializable result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAuditUserId();
    }

    @Test
    public void testGetRowNum() throws Exception {
        VidWorkflow testSubject;
        Long result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRowNum();
    }

    @Test
    public void testGetAuditTrail() throws Exception {
        VidWorkflow testSubject;
        Set result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAuditTrail();
    }

    @Test
    public void testGetWokflowName() throws Exception {
        VidWorkflow testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getWokflowName();
    }

    @Test
    public void testSetWokflowName() throws Exception {
        VidWorkflow testSubject;
        String wokflowName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setWokflowName(wokflowName);
    }
}