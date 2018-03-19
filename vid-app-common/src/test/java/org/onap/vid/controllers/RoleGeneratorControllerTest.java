package org.onap.vid.controllers;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class RoleGeneratorControllerTest {

    private RoleGeneratorController createTestSubject() {
        return new RoleGeneratorController();
    }

    @Test
    public void testGenerateRoleScript() throws Exception {
        RoleGeneratorController testSubject;
        boolean firstRun = false;
        ResponseEntity<String> result;

        // default test
        try {
            testSubject = createTestSubject();
            result = testSubject.generateRoleScript(firstRun);
        } catch (Exception e) {

        }
    }
}