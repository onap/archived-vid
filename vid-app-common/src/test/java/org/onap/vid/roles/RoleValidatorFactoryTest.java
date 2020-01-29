/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.List;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.properties.Features;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

public class RoleValidatorFactoryTest {

    @InjectMocks
    private RoleValidatorFactory roleValidatorFactory;

    @Mock
    private FeatureManager featureManager;

    @Mock
    private List<Role> roleList;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void reset() {
        Mockito.reset(featureManager);
    }

    private RoleValidator roleValidator;

    @Test (dataProvider = "presetRoleValidatorClass")
    public void returnRoleValidatorByGivenRole_management_activated(Class expectedClass,boolean isDisabledRoles, boolean flagActive ) {

        when(featureManager.isActive(Features.FLAG_2006_USER_PERMISSIONS_BY_OWNING_ENTITY)).thenReturn(flagActive);
        roleValidator = roleValidatorFactory.by(roleList, isDisabledRoles);
        assertThat(roleValidator, instanceOf(expectedClass));
    }

    @DataProvider
    public static Object[][] presetRoleValidatorClass(Method test) {
        return new Object[][] {
            {RoleValidatorByOwningEntity.class, false, true},
            {AlwaysValidRoleValidator.class, true, true},
            {RoleValidatorBySubscriberAndServiceType.class, false, false},
            {AlwaysValidRoleValidator.class, true, false}
        };
    }

}