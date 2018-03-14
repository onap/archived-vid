package org.onap.vid.services;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;

public class RoleGenaratorServiceImplTest {

    private RoleGenaratorServiceImpl createTestSubject() {
        return new RoleGenaratorServiceImpl();
    }

    @Test
    public void testGenerateRoleScript() throws Exception {
        RoleGenaratorServiceImpl testSubject;
        Boolean firstRun = null;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.generateRoleScript(firstRun);
    }


}