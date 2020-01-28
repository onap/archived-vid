/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2020 AT&T Intellectual Property. All rights reserved.
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

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.withSettings;

import com.google.common.collect.ImmutableSet;
import java.util.function.Function;
import java.util.stream.Stream;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RoleValidatorsComposerTest {

    private RoleValidator alwaysTrueRoles;
    private RoleValidator alwaysFalseRoles;

    @DataProvider
    public static Object[][] allInterfaceFunctions() {
        return Stream.<Function<RoleValidator, Boolean>>of(
            (RoleValidator o) -> o.isSubscriberPermitted("subscriberId"),
            (RoleValidator o) -> o.isServicePermitted(new WithPermissionProperties() {}),
            (RoleValidator o) -> o.isTenantPermitted("subscriberId", "serviceType", "tenantName")
        ).map(it -> new Object[]{it}).collect(toList()).toArray(new Object[][]{});
    }

    @BeforeMethod
    public void setUp() {
        alwaysTrueRoles = mock(RoleValidator.class, withSettings().defaultAnswer(o -> true));
        alwaysFalseRoles = mock(RoleValidator.class);
    }

    @Test(dataProvider = "allInterfaceFunctions")
    public void emptyComposite_returnsFalse(Function<RoleValidator, Boolean> interfaceFunction) {
        RoleValidatorsComposer underTest = new RoleValidatorsComposer();

        assertThat(
            interfaceFunction.apply(underTest),
            is(false)
        );

    }

    @Test(dataProvider = "allInterfaceFunctions")
    public void falseAndTrueComposite_returnsTrue(Function<RoleValidator, Boolean> interfaceFunction) {
        RoleValidatorsComposer underTest =
            new RoleValidatorsComposer(alwaysFalseRoles, alwaysFalseRoles, alwaysTrueRoles);

        assertThat(
            interfaceFunction.apply(underTest),
            is(true)
        );
    }

    @Test(dataProvider = "allInterfaceFunctions")
    public void trueAndFalseComposite_returnsTrueAndShortCircuits(Function<RoleValidator, Boolean> interfaceFunction) {
        RoleValidatorsComposer underTest = new RoleValidatorsComposer(alwaysTrueRoles, alwaysFalseRoles);

        assertThat(
            interfaceFunction.apply(underTest),
            is(true)
        );

        verifyZeroInteractions(alwaysFalseRoles);
    }

    @Test(dataProvider = "allInterfaceFunctions")
    public void falseAndFalseComposite_returnsFalse(Function<RoleValidator, Boolean> interfaceFunction) {
        RoleValidatorsComposer underTest = new RoleValidatorsComposer(alwaysFalseRoles, alwaysFalseRoles);
        
        assertThat(
            interfaceFunction.apply(underTest),
            is(false)
        );
    }

    @Test
    public void secondaryConstructor_givenSetIfValidators_returnsTrue() {
        RoleValidatorsComposer underTest = new RoleValidatorsComposer(
            ImmutableSet.of(alwaysTrueRoles)
        );

        assertThat(underTest.isSubscriberPermitted("anything"), is(true));
    }

}
