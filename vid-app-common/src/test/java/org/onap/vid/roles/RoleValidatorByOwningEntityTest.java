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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import static org.testng.Assert.assertFalse;

import com.google.common.collect.ImmutableList;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RoleValidatorByOwningEntityTest {

    private static final String OWNING_ENTITY_ID = "owningEntityId";
    private static final String SUBSCRIBER_NAME = "subscriber_name";
    private static final String NOT_MATCHING_OWNING_ENTITY_ID = "notMatchingOwningEntityId";

    private static final String SERVICE_TYPE = "serviceType";
    private static final String GLOBAL_CUSTOMER_ID = "globalCustomerId";
    private static final String TENANT_NAME = "tenantName";


    private RoleValidatorByOwningEntity roleValidatorByOwningEntity;

    @BeforeMethod
    public void setup() {
        final Role SAMPLE_ROLE = new Role(EcompRole.READ, "", "", "", OWNING_ENTITY_ID);
        roleValidatorByOwningEntity = new RoleValidatorByOwningEntity(ImmutableList.of(SAMPLE_ROLE));
    }

    @Test
    public void testIsSubscriberPermitted() {
        assertFalse(roleValidatorByOwningEntity.isSubscriberPermitted(SUBSCRIBER_NAME));
    }

    @Test
    public void isServicePermitted_owningEntityMatch_returnTrue() {
        PermissionPropertiesOwningEntity permittedOwningEntity =
            new PermissionPropertiesOwningEntity(OWNING_ENTITY_ID);

        assertThat(roleValidatorByOwningEntity.isServicePermitted(permittedOwningEntity), is(true));
    }

    @DataProvider
    public static Object[][] nonMatchingPermissionProperties() {
        return new Object[][]{
            {new PermissionPropertiesOwningEntity(NOT_MATCHING_OWNING_ENTITY_ID)},
            {new PermissionPropertiesOwningEntity("")},
            {new WithPermissionProperties() {}},
            {mock(PermissionPropertiesOwningEntity.class,
                withSettings().name("PermissionPropertiesOwningEntity with null owningEntityId"))},
            {new PermissionPropertiesSubscriberAndServiceType(OWNING_ENTITY_ID, OWNING_ENTITY_ID)},
        };
    }

    @Test(dataProvider = "nonMatchingPermissionProperties")
    public void isServicePermitted_nonMatchingPermissionProperties_returnFalse(WithPermissionProperties permissionProperties) {
        assertThat(permissionProperties.toString(), roleValidatorByOwningEntity.isServicePermitted(
            permissionProperties
        ), is(false));
    }

    @Test
    public void testIsTenantPermitted() {
        assertFalse(roleValidatorByOwningEntity.isTenantPermitted(GLOBAL_CUSTOMER_ID, SERVICE_TYPE, TENANT_NAME));
    }

}
