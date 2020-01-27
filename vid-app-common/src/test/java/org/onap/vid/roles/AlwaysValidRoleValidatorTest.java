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

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class AlwaysValidRoleValidatorTest {

    @Test
    public void testIsSubscriberPermitted() {
        assertTrue(new AlwaysValidRoleValidator().isSubscriberPermitted("any"));
    }

    @Test
    public void testIsServicePermitted() {
        assertTrue(new AlwaysValidRoleValidator().isServicePermitted(new PermissionProperties("any", "any")));
    }

    @Test
    public void testIsTenantPermitted() {
        assertTrue(new AlwaysValidRoleValidator().isTenantPermitted("any", "any", "any"));
    }
}