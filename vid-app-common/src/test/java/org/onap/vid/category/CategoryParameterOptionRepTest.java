package org.onap.vid.category;

import org.junit.Test;

public class CategoryParameterOptionRepTest {

    private CategoryParameterOptionRep createTestSubject() {
        return new CategoryParameterOptionRep();
    }

    @Test
    public void testGetId() throws Exception {
        CategoryParameterOptionRep testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getId();
    }

    @Test
    public void testSetId() throws Exception {
        CategoryParameterOptionRep testSubject;
        String id = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setId(id);
    }

    @Test
    public void testGetName() throws Exception {
        CategoryParameterOptionRep testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getName();
    }

    @Test
    public void testSetName() throws Exception {
        CategoryParameterOptionRep testSubject;
        String name = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setName(name);
    }
}