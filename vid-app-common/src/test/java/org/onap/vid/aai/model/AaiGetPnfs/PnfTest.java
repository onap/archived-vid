package org.onap.vid.aai.model.AaiGetPnfs;

import org.junit.Test;

public class PnfTest {

    private Pnf createTestSubject() {
        return new Pnf();
    }

    @Test
    public void testGetPnfName() throws Exception {
        Pnf testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getPnfName();
    }

    @Test
    public void testSetPnfName() throws Exception {
        Pnf testSubject;
        String pnfName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setPnfName(pnfName);
    }

    @Test
    public void testGetEquipType() throws Exception {
        Pnf testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getEquipType();
    }

    @Test
    public void testSetEquipType() throws Exception {
        Pnf testSubject;
        String equipType = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setEquipType(equipType);
    }

    @Test
    public void testGetEquipVendor() throws Exception {
        Pnf testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getEquipVendor();
    }

    @Test
    public void testSetEquipVendor() throws Exception {
        Pnf testSubject;
        String equipVendor = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setEquipVendor(equipVendor);
    }

    @Test
    public void testGetPnfName2() throws Exception {
        Pnf testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getPnfName2();
    }

    @Test
    public void testSetPnfName2() throws Exception {
        Pnf testSubject;
        String pnfName2 = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setPnfName2(pnfName2);
    }

    @Test
    public void testGetPnfId() throws Exception {
        Pnf testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getPnfId();
    }

    @Test
    public void testSetPnfId() throws Exception {
        Pnf testSubject;
        String pnfId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setPnfId(pnfId);
    }

    @Test
    public void testGetEquipModel() throws Exception {
        Pnf testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getEquipModel();
    }

    @Test
    public void testSetEquipModel() throws Exception {
        Pnf testSubject;
        String equipModel = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setEquipModel(equipModel);
    }

    @Test
    public void testGetPnfName2Source() throws Exception {
        Pnf testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getPnfName2Source();
    }

    @Test
    public void testSetPnfName2Source() throws Exception {
        Pnf testSubject;
        String pnfName2Source = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setPnfName2Source(pnfName2Source);
    }
}