package org.onap.vid.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.junit.Test;

public class VNFDaoTest {

    private VNFDao createTestSubject() {
        return new VNFDao();
    }

    @Test
    public void testGetId() throws Exception {
        VNFDao testSubject;
        Long result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getId();
    }

    @Test
    public void testGetCreated() throws Exception {
        VNFDao testSubject;
        Date result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getCreated();
    }

    @Test
    public void testGetModified() throws Exception {
        VNFDao testSubject;
        Date result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModified();
    }

    @Test
    public void testGetCreatedId() throws Exception {
        VNFDao testSubject;
        Long result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getCreatedId();
    }

    @Test
    public void testGetModifiedId() throws Exception {
        VNFDao testSubject;
        Long result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModifiedId();
    }

    @Test
    public void testGetAuditUserId() throws Exception {
        VNFDao testSubject;
        Serializable result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAuditUserId();
    }

    @Test
    public void testGetRowNum() throws Exception {
        VNFDao testSubject;
        Long result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRowNum();
    }

    @Test
    public void testGetAuditTrail() throws Exception {
        VNFDao testSubject;
        Set result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAuditTrail();
    }

    @Test
    public void testGetVnfUUID() throws Exception {
        VNFDao testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getVnfUUID();
    }

    @Test
    public void testGetVnfInvariantUUID() throws Exception {
        VNFDao testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getVnfInvariantUUID();
    }

    @Test
    public void testSetVnfUUID() throws Exception {
        VNFDao testSubject;
        String vnfUUID = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setVnfUUID(vnfUUID);
    }

    @Test
    public void testSetVnfInvariantUUID() throws Exception {
        VNFDao testSubject;
        String vnfInvariantUUID = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setVnfInvariantUUID(vnfInvariantUUID);
    }

    @Test
    public void testGetWorkflows() throws Exception {
        VNFDao testSubject;
        Set<VidWorkflow> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getWorkflows();
    }

    @Test
    public void testSetWorkflows() throws Exception {
        VNFDao testSubject;
        Set<VidWorkflow> workflows = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setWorkflows(workflows);
    }
}