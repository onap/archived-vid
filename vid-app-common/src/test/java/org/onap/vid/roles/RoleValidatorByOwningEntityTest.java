/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

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