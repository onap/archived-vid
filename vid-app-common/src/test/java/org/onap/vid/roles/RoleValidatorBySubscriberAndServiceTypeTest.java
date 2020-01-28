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
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.onap.vid.mso.rest.RequestDetails;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RoleValidatorBySubscriberAndServiceTypeTest {

    private static final String SAMPLE_SUBSCRIBER = "sampleSubscriber";
    private static final String NOT_MATCHING_SUBSCRIBER = "notMatchingSubscriber";
    private static final String SAMPLE_SERVICE_TYPE = "sampleServiceType";
    private static final String NOT_MATCHING_TENANT = "notMatchingTenant";
    private static final String SAMPLE_TENANT = "sampleTenant";

    private static final Role SAMPLE_ROLE = new Role(EcompRole.READ, SAMPLE_SUBSCRIBER, SAMPLE_SERVICE_TYPE, SAMPLE_TENANT);

    private List<Role> roles = ImmutableList.of(SAMPLE_ROLE);
    private Map<String, Object> subscriberInfo = ImmutableMap.of("globalSubscriberId", SAMPLE_SUBSCRIBER);
    private Map<String, Object> requestParameters = ImmutableMap.of("subscriptionServiceType", SAMPLE_SERVICE_TYPE);
    private Map<String, Object> requestDetailsProperties = ImmutableMap.of("subscriberInfo", subscriberInfo, "requestParameters", requestParameters);
    private RequestDetails requestDetails;
    private RoleValidatorBySubscriberAndServiceType roleValidatorBySubscriberAndServiceType;

    @BeforeMethod
    public void setUp() {
        roleValidatorBySubscriberAndServiceType = new RoleValidatorBySubscriberAndServiceType(roles);
        requestDetails = new RequestDetails();
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
        assertThat(roleValidatorBySubscriberAndServiceType.isServicePermitted(new PermissionProperties(SAMPLE_SUBSCRIBER, SAMPLE_SERVICE_TYPE))).isTrue();
    }


    @Test
    public void shouldNotPermitServiceWhenSubscriberNameNotMatches() {
        assertThat(
            roleValidatorBySubscriberAndServiceType.isServicePermitted(new PermissionProperties(NOT_MATCHING_SUBSCRIBER, SAMPLE_SERVICE_TYPE))).isFalse();
    }

    @Test
    public void shouldNotPermitServiceWhenServiceTypeNotMatches() {
        assertThat(roleValidatorBySubscriberAndServiceType.isServicePermitted(new PermissionProperties(SAMPLE_SUBSCRIBER, NOT_MATCHING_SUBSCRIBER))).isFalse();
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
