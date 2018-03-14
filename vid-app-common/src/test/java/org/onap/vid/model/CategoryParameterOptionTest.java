package org.onap.vid.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class CategoryParameterOptionTest {

    private CategoryParameterOption createTestSubject() {
        return new CategoryParameterOption();
    }

    @Test
    public void testGetId() throws Exception {
        CategoryParameterOption testSubject;
        Long result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getId();
    }

    @Test
    public void testSetId() throws Exception {
        CategoryParameterOption testSubject;
        Long id = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setId(id);
    }

    @Test
    public void testGetAppId() throws Exception {
        CategoryParameterOption testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAppId();
    }

    @Test
    public void testSetAppId() throws Exception {
        CategoryParameterOption testSubject;
        String appId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setAppId(appId);
    }

    @Test
    public void testGetName() throws Exception {
        CategoryParameterOption testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getName();
    }

    @Test
    public void testSetName() throws Exception {
        CategoryParameterOption testSubject;
        String name = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setName(name);
    }

    @Test
    public void testGetCategoryParameter() throws Exception {
        CategoryParameterOption testSubject;
        CategoryParameter result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getCategoryParameter();
    }

    @Test
    public void testSetCategoryParameter() throws Exception {
        CategoryParameterOption testSubject;
        CategoryParameter categoryParameter = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setCategoryParameter(categoryParameter);
    }

    @Test
    public void testGetCreated() throws Exception {
        CategoryParameterOption testSubject;
        Date result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getCreated();
    }

    @Test
    public void testGetModified() throws Exception {
        CategoryParameterOption testSubject;
        Date result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModified();
    }

    @Test
    public void testGetCreatedId() throws Exception {
        CategoryParameterOption testSubject;
        Long result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getCreatedId();
    }

    @Test
    public void testGetModifiedId() throws Exception {
        CategoryParameterOption testSubject;
        Long result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModifiedId();
    }

    @Test
    public void testGetAuditUserId() throws Exception {
        CategoryParameterOption testSubject;
        Serializable result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAuditUserId();
    }

    @Test
    public void testGetRowNum() throws Exception {
        CategoryParameterOption testSubject;
        Long result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getRowNum();
    }

    @Test
    public void testGetAuditTrail() throws Exception {
        CategoryParameterOption testSubject;
        Set result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAuditTrail();
    }

    @Test
    public void testEquals() throws Exception {
        CategoryParameterOption testSubject;
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
        CategoryParameterOption testSubject;
        int result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.hashCode();
    }

}