package org.onap.vid.roles;


import com.google.common.collect.ImmutableMap;
import org.assertj.core.util.Lists;
import org.testng.annotations.Test;
import org.onap.vid.mso.rest.RequestDetails;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class RoleValidatorTest {

    private static final Role SAMPLE_ROLE = new Role(EcompRole.READ, "sampleSubscriber", "sampleServiceType", "sampleTenant");
    private static final String SAMPLE_SUBSCRIBER = "sampleSubscriber";
    private static final String NOT_MATCHING_SUBSCRIBER = "notMatchingSubscriber";
    private static final String SAMPLE_SERVICE_TYPE = "sampleServiceType";
    private static final String NOT_MATCHING_TENANT = "notMatchingTenant";
    private static final String SAMPLE_TENANT = "sampleTenant";

    private List<Role> roles = Lists.list(SAMPLE_ROLE);
    private Map<String, Object> subscriberInfo = ImmutableMap.of("globalSubscriberId", "sampleSubscriber");
    private Map<String, Object> requestParameters = ImmutableMap.of("subscriptionServiceType", "sampleServiceType");
    private Map<String, Object> requestDetailsProperties = ImmutableMap.of("subscriberInfo", subscriberInfo, "requestParameters", requestParameters);
    private RequestDetails requestDetails = new RequestDetails();

    private RoleValidator roleValidator = new RoleValidator(roles);

    @Test
    public void shouldPermitSubscriberWhenNameMatchesAndRolesAreEnabled() {
        roleValidator.enableRoles();

        assertThat(roleValidator.isSubscriberPermitted(SAMPLE_SUBSCRIBER)).isTrue();
    }

    @Test
    public void shouldNotPermitSubscriberWhenNameNotMatches() {
        roleValidator.enableRoles();

        assertThat(roleValidator.isSubscriberPermitted(NOT_MATCHING_SUBSCRIBER)).isFalse();
    }

    @Test
    public void shouldPermitServiceWhenNamesMatches() {
        roleValidator.enableRoles();

        assertThat(roleValidator.isServicePermitted(SAMPLE_SUBSCRIBER, SAMPLE_SERVICE_TYPE)).isTrue();
    }


    @Test
    public void shouldNotPermitServiceWhenSubscriberNameNotMatches() {
        roleValidator.enableRoles();

        assertThat(roleValidator.isServicePermitted(NOT_MATCHING_SUBSCRIBER, SAMPLE_SERVICE_TYPE)).isFalse();
    }

    @Test
    public void shouldNotPermitServiceWhenServiceTypeNotMatches() {
        roleValidator.enableRoles();

        assertThat(roleValidator.isServicePermitted(SAMPLE_SUBSCRIBER, NOT_MATCHING_SUBSCRIBER)).isFalse();
    }

    @Test
    public void shouldPermitTenantWhenNameMatches() {
        roleValidator.enableRoles();

        assertThat(roleValidator.isTenantPermitted(SAMPLE_SUBSCRIBER, SAMPLE_SERVICE_TYPE, SAMPLE_TENANT)).isTrue();
    }


    @Test
    public void shouldNotPermitTenantWhenNameNotMatches() {
        roleValidator.enableRoles();

        assertThat(roleValidator.isTenantPermitted(SAMPLE_SUBSCRIBER, SAMPLE_SERVICE_TYPE, NOT_MATCHING_TENANT)).isFalse();
    }

    @Test
    public void shouldValidateProperlySORequest() {
        roleValidator.enableRoles();
        requestDetails.setAdditionalProperty("requestDetails", requestDetailsProperties);

        assertThat(roleValidator.isMsoRequestValid(requestDetails)).isTrue();
    }

    @Test
    public void shouldValidateUnknownSORequest() {
        roleValidator.enableRoles();

        assertThat(roleValidator.isMsoRequestValid(new RequestDetails())).isTrue();
    }

    @Test
    public void shouldRejectSORequestWhenSubscriberNotMatches() {
        roleValidator.enableRoles();
        Map<String, Object> subscriberInfo = ImmutableMap.of("globalSubscriberId", "sample");
        Map<String, Object> requestDetailsProperties = ImmutableMap.of("subscriberInfo", subscriberInfo, "requestParameters", requestParameters);
        requestDetails.setAdditionalProperty("requestDetails", requestDetailsProperties);

        assertThat(roleValidator.isMsoRequestValid(requestDetails)).isFalse();
    }
}