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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RoleValidatorByOwningEntityTest {

    private static final String SAMPLE_OWNING_ENTITY_ID = "sampleOwningEntityId";
    private static final String NOT_MATCHING__OWNING_ENTITY_ID = "notMatchingOwningEntityId";
    private static final Role SAMPLE_ROLE = new Role(EcompRole.READ, "", "", "", SAMPLE_OWNING_ENTITY_ID);
    private List<Role> roles = ImmutableList.of(SAMPLE_ROLE);



    private RoleValidatorByOwningEntity roleValidatorByOwningEntity;

    @BeforeMethod
    public void setup(){
        roleValidatorByOwningEntity = new RoleValidatorByOwningEntity(roles);
    }

    @Test
    public void testIsOwningEntityIdPermitted() {
        assertFalse(roleValidatorByOwningEntity.isOwningEntityIdPermitted(""));
        assertFalse(roleValidatorByOwningEntity.isOwningEntityIdPermitted(NOT_MATCHING__OWNING_ENTITY_ID));
        assertTrue(roleValidatorByOwningEntity.isOwningEntityIdPermitted(SAMPLE_OWNING_ENTITY_ID));
    }

    @Test
    public void testIsSubscriberPermitted() {
        assertFalse(roleValidatorByOwningEntity.isSubscriberPermitted(anyString()));
    }

    @Test
    public void testIsServicePermitted() {
        assertFalse(roleValidatorByOwningEntity.isServicePermitted(any()));
    }

    @Test
    public void testIsTenantPermitted() {
        assertFalse(roleValidatorByOwningEntity.isTenantPermitted(anyString() , anyString(), anyString()));
    }

}