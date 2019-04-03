package org.onap.vid.roles;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class AlwaysValidRoleValidatorTest {

    @Test
    public void testIsSubscriberPermitted() {
        assertTrue(new AlwaysValidRoleValidator().isSubscriberPermitted("any"));
    }

    @Test
    public void testIsServicePermitted() {
        assertTrue(new AlwaysValidRoleValidator().isServicePermitted("any", "any"));
    }

    @Test
    public void testIsTenantPermitted() {
        assertTrue(new AlwaysValidRoleValidator().isTenantPermitted("any", "any", "any"));
    }
}