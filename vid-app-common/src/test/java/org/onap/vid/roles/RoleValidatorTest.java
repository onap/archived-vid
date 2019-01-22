package org.onap.vid.roles;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.onap.vid.mso.rest.RequestDetails;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleValidatorTest {

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
    private RoleValidator roleValidator;

    @BeforeMethod
    public void setUp() {
        roleValidator = new RoleValidator(roles);
        roleValidator.enableRoles();
        requestDetails = new RequestDetails();
    }

    @Test
    public void shouldPermitSubscriberWhenNameMatchesAndRolesAreEnabled() {
        assertThat(roleValidator.isSubscriberPermitted(SAMPLE_SUBSCRIBER)).isTrue();
    }

    @Test
    public void shouldNotPermitSubscriberWhenNameNotMatches() {
        assertThat(roleValidator.isSubscriberPermitted(NOT_MATCHING_SUBSCRIBER)).isFalse();
    }

    @Test
    public void shouldPermitServiceWhenNamesMatches() {
        assertThat(roleValidator.isServicePermitted(SAMPLE_SUBSCRIBER, SAMPLE_SERVICE_TYPE)).isTrue();
    }


    @Test
    public void shouldNotPermitServiceWhenSubscriberNameNotMatches() {
        assertThat(roleValidator.isServicePermitted(NOT_MATCHING_SUBSCRIBER, SAMPLE_SERVICE_TYPE)).isFalse();
    }

    @Test
    public void shouldNotPermitServiceWhenServiceTypeNotMatches() {
        assertThat(roleValidator.isServicePermitted(SAMPLE_SUBSCRIBER, NOT_MATCHING_SUBSCRIBER)).isFalse();
    }

    @Test
    public void shouldPermitTenantWhenNameMatches() {
        assertThat(roleValidator.isTenantPermitted(SAMPLE_SUBSCRIBER, SAMPLE_SERVICE_TYPE, SAMPLE_TENANT)).isTrue();
    }


    @Test
    public void shouldNotPermitTenantWhenNameNotMatches() {
        assertThat(roleValidator.isTenantPermitted(SAMPLE_SUBSCRIBER, SAMPLE_SERVICE_TYPE, NOT_MATCHING_TENANT)).isFalse();
    }

    @Test
    public void shouldValidateProperlySORequest() {
        requestDetails.setAdditionalProperty("requestDetails", requestDetailsProperties);

        assertThat(roleValidator.isMsoRequestValid(requestDetails)).isTrue();
    }

    @Test
    public void shouldValidateUnknownSORequest() {
        assertThat(roleValidator.isMsoRequestValid(new RequestDetails())).isTrue();
    }

    @Test
    public void shouldRejectSORequestWhenSubscriberNotMatches() {
        Map<String, Object> subscriberInfo = ImmutableMap.of("globalSubscriberId", "sample");
        Map<String, Object> requestDetailsProperties = ImmutableMap.of("subscriberInfo", subscriberInfo, "requestParameters", requestParameters);
        requestDetails.setAdditionalProperty("requestDetails", requestDetailsProperties);

        assertThat(roleValidator.isMsoRequestValid(requestDetails)).isFalse();
    }
}