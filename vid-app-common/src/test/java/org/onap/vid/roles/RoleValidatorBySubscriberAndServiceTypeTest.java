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


import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RoleValidatorBySubscriberAndServiceTypeTest {

    private static final String SAMPLE_SUBSCRIBER = "sampleSubscriber";
    private static final String NOT_MATCHING_SUBSCRIBER = "notMatchingSubscriber";
    private static final String SAMPLE_SERVICE_TYPE = "sampleServiceType";
    private static final String NOT_MATCHING_TENANT = "notMatchingTenant";
    private static final String SAMPLE_TENANT = "sampleTenant";
    private static final String SOME_OWNING_ENTITY_ID = "someOwningEntityId";

    private static final Role SAMPLE_ROLE = new Role(
        EcompRole.READ, SAMPLE_SUBSCRIBER, SAMPLE_SERVICE_TYPE, SAMPLE_TENANT, SOME_OWNING_ENTITY_ID);

    private List<Role> roles = ImmutableList.of(SAMPLE_ROLE);
    private RoleValidatorBySubscriberAndServiceType roleValidatorBySubscriberAndServiceType;

    @BeforeMethod
    public void setUp() {
        roleValidatorBySubscriberAndServiceType = new RoleValidatorBySubscriberAndServiceType(roles);
    }

    @Test
    public void shouldPermitSubscriberWhenNameMatchesAndRolesAreEnabled() {
        assertThat(roleValidatorBySubscriberAndServiceType.isSubscriberPermitted(SAMPLE_SUBSCRIBER)).isTrue();
    }

    @Test
    public void shouldNotPermitSubscriberWhenNameNotMatches() {
        assertThat(roleValidatorBySubscriberAndServiceType.isSubscriberPermitted(NOT_MATCHING_SUBSCRIBER)).isFalse();
    }

    @Test
    public void shouldPermitServiceWhenNamesMatches() {
        assertThat(roleValidatorBySubscriberAndServiceType.isServicePermitted(
            new PermissionPropertiesSubscriberAndServiceType(SAMPLE_SUBSCRIBER, SAMPLE_SERVICE_TYPE))).isTrue();
    }

    @Test
    public void isServicePermitted_serviceWithAllPermissionProperties_isPermitted() {
        assertThat(roleValidatorBySubscriberAndServiceType.isServicePermitted(
            new AllPermissionProperties(SAMPLE_SUBSCRIBER, SAMPLE_SERVICE_TYPE, SOME_OWNING_ENTITY_ID))).isTrue();
    }

    @Test
    public void shouldNotPermitServiceWhenSubscriberNameNotMatches() {
        assertThat(
            roleValidatorBySubscriberAndServiceType.isServicePermitted(
                new PermissionPropertiesSubscriberAndServiceType(NOT_MATCHING_SUBSCRIBER, SAMPLE_SERVICE_TYPE))).isFalse();
    }

    @Test
    public void shouldNotPermitServiceWhenServiceTypeNotMatches() {
        assertThat(roleValidatorBySubscriberAndServiceType.isServicePermitted(
            new PermissionPropertiesSubscriberAndServiceType(SAMPLE_SUBSCRIBER, NOT_MATCHING_SUBSCRIBER))).isFalse();
    }

    @Test
    public void isServicePermitted_owningEntityPermissionProperties_isNotPermitted() {
        assertThat(roleValidatorBySubscriberAndServiceType.isServicePermitted(
            new PermissionPropertiesOwningEntity(SAMPLE_SUBSCRIBER))).isFalse();
    }

    @Test
    public void shouldPermitTenantWhenNameMatches() {
        assertThat(roleValidatorBySubscriberAndServiceType
            .isTenantPermitted(SAMPLE_SUBSCRIBER, SAMPLE_SERVICE_TYPE, SAMPLE_TENANT)).isTrue();
    }

    @Test
    public void shouldPermitTenantWhenNameMatchesCaseInsensitive() {
        assertThat(roleValidatorBySubscriberAndServiceType
            .isTenantPermitted(SAMPLE_SUBSCRIBER, SAMPLE_SERVICE_TYPE, SAMPLE_TENANT.toUpperCase())).isTrue();
    }


    @Test
    public void shouldNotPermitTenantWhenNameNotMatches() {
        assertThat(roleValidatorBySubscriberAndServiceType
            .isTenantPermitted(SAMPLE_SUBSCRIBER, SAMPLE_SERVICE_TYPE, NOT_MATCHING_TENANT)).isFalse();
    }

}
