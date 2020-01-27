package org.onap.vid.roles;

import static org.testng.Assert.assertFalse;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RoleValidatorByOwningEntityTest {

    private static final String OWNING_ENTITY_ID = "owningEntityId";
    private static final String SUBSCRIBER_NAME = "subscriber_name";
    private static final String SERVICE_TYPE = "serviceType";
    private static final String GLOBAL_CUSTOMER_ID = "globalCustomerId";
    private static final String TENANT_NAME = "tenantName";


    private RoleValidatorByOwningEntity roleValidatorByOwningEntity;

    @BeforeMethod
    public void setup(){
        roleValidatorByOwningEntity = new RoleValidatorByOwningEntity();
    }
    @Test
    public void testIsOwningEntityIdPermitted() {
        assertFalse(roleValidatorByOwningEntity.isOwningEntityIdPermitted(OWNING_ENTITY_ID));
    }

    @Test
    public void testIsSubscriberPermitted() {
        assertFalse(roleValidatorByOwningEntity.isSubscriberPermitted(SUBSCRIBER_NAME));
    }

    @Test
    public void testIsServicePermitted() {
        assertFalse(roleValidatorByOwningEntity.isServicePermitted(SUBSCRIBER_NAME, SERVICE_TYPE));
    }

    @Test
    public void testIsTenantPermitted() {
        assertFalse(roleValidatorByOwningEntity.isTenantPermitted(GLOBAL_CUSTOMER_ID , SERVICE_TYPE, TENANT_NAME));
    }

}