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

package org.onap.vid.controller;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jetbrains.annotations.NotNull;
import org.onap.vid.aai.model.Permissions;
import org.onap.vid.roles.PermissionProperties;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.roles.RoleValidator;
import org.springframework.mock.web.MockHttpServletRequest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ServicePermissionsTest {

    @DataProvider
    public static Object[][] trueAndFalse() {
        return new Object[][]{{TRUE}, {FALSE}};
    }


    @Test(dataProvider = "trueAndFalse")
    public void servicePermissions_responseMatchesMockedRoleValidator(boolean expected) {
        String subscriberId = randomAlphanumeric(8);
        String serviceType = randomAlphanumeric(8);

        RoleProvider roleProvider = mock(RoleProvider.class);
        RoleValidator roleValidator = mock(RoleValidator.class);
        when(roleProvider.getUserRolesValidator(any())).thenReturn(roleValidator);
        when(roleValidator.isServicePermitted(new PermissionProperties(subscriberId, serviceType))).thenReturn(expected);

        AaiController2 aaiController2 = new AaiController2(null, roleProvider, null, null);

        Permissions permissions = aaiController2.servicePermissions(unimportantRequest(), subscriberId, serviceType);
        assertThat(permissions, is(new Permissions(expected)));
    }

    @NotNull
    private MockHttpServletRequest unimportantRequest() {
        return new MockHttpServletRequest("", "");
    }
}
