package org.onap.vid.aai.model;

import org.junit.Test;

public class ProjectTest {

    private Project createTestSubject() {
        return new Project();
    }

    @Test
    public void testGetProjectName() throws Exception {
        Project testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getProjectName();
    }

    @Test
    public void testSetProjectName() throws Exception {
        Project testSubject;
        String projectName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setProjectName(projectName);
    }
}