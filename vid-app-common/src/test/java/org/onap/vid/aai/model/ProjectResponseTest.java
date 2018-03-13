package org.onap.vid.aai.model;

import java.util.List;

import org.junit.Test;

public class ProjectResponseTest {

    private ProjectResponse createTestSubject() {
        return new ProjectResponse();
    }

    @Test
    public void testGetProject() throws Exception {
        ProjectResponse testSubject;
        List<Project> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getProject();
    }

    @Test
    public void testSetProject() throws Exception {
        ProjectResponse testSubject;
        List<Project> project = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setProject(project);
    }
}