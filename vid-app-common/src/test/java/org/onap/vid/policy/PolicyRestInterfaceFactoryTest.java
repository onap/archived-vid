package org.onap.vid.policy;

import org.junit.Test;

public class PolicyRestInterfaceFactoryTest {

    private PolicyRestInterfaceFactory createTestSubject() {
        return new PolicyRestInterfaceFactory();
    }

    @Test
    public void testGetInstance() throws Exception {
        PolicyRestInterfaceIfc result;

        // default test
        result = PolicyRestInterfaceFactory.getInstance();
    }
}