package org.onap.vid.roles;


import com.google.common.collect.ImmutableMap;
import io.joshworks.restclient.http.HttpResponse;
import org.assertj.core.util.Lists;
import org.mockito.Mock;
import org.onap.vid.aai.exceptions.RoleParsingException;
import org.onap.vid.model.Subscriber;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.services.AaiService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RoleProviderTest {

    private static final String SAMPLE_SUBSCRIBER = "sampleSubscriber";
    private static final String SAMPLE_CUSTOMER_ID = "sampleCustomerId";
    private static final String SERVICE_TYPE_LOGS = "LOGS";
    private static final String TENANT_PERMITTED = "PERMITTED";
    private static final String SAMPLE_SERVICE = "sampleService";
    private static final String SAMPLE_TENANT = "sampleTenant";
    private static final String SAMPLE_ROLE_PREFIX = "prefix";

    @Mock
    private AaiService aaiService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpResponse<SubscriberList> subscriberListHttpResponse;


    private RoleProvider roleProvider;


    @BeforeMethod
    public void setUp() {
        initMocks(this);
        roleProvider = new RoleProvider(aaiService, httpServletRequest -> 5, httpServletRequest -> createRoles());
    }

    @Test
    public void shouldSplitRolesWhenDelimiterIsPresent() {
        String roles = "role_a___role_b";

        assertThat(roleProvider.splitRole(roles, "")).isEqualTo(new String[]{"role_a", "role_b"});
    }


    @Test
    public void shouldProperlyCreateRoleFromCorrectArray() throws RoleParsingException {
        setSubscribers();
        String[] roleParts = {SAMPLE_SUBSCRIBER, SAMPLE_SERVICE, SAMPLE_TENANT};

        Role role = roleProvider.createRoleFromStringArr(roleParts, SAMPLE_ROLE_PREFIX);

        assertThat(role.getEcompRole()).isEqualTo(EcompRole.READ);
        assertThat(role.getSubscribeName()).isEqualTo(SAMPLE_CUSTOMER_ID);
        assertThat(role.getTenant()).isEqualTo(SAMPLE_TENANT);
        assertThat(role.getServiceType()).isEqualTo(SAMPLE_SERVICE);
    }

    @Test
    public void shouldProperlyCreateRoleWhenTenantIsNotProvided() throws RoleParsingException {
        setSubscribers();

        String[] roleParts = {SAMPLE_SUBSCRIBER, SAMPLE_SERVICE};

        Role role = roleProvider.createRoleFromStringArr(roleParts, SAMPLE_ROLE_PREFIX);

        assertThat(role.getEcompRole()).isEqualTo(EcompRole.READ);
        assertThat(role.getSubscribeName()).isEqualTo(SAMPLE_CUSTOMER_ID);
        assertThat(role.getServiceType()).isEqualTo(SAMPLE_SERVICE);
        assertThat(role.getTenant()).isNullOrEmpty();
    }

    @Test(expectedExceptions = RoleParsingException.class)
    public void shouldRaiseExceptionWhenRolePartsAreIncomplete() throws RoleParsingException {
        setSubscribers();

        roleProvider.createRoleFromStringArr(new String[]{SAMPLE_SUBSCRIBER}, SAMPLE_ROLE_PREFIX);
    }

    @Test
    public void shouldProperlyRetrieveUserRolesWhenPermissionIsDifferentThanRead() {
        Role expectedRole = new Role(EcompRole.READ, SAMPLE_CUSTOMER_ID, SAMPLE_SERVICE, SAMPLE_TENANT);
        setSubscribers();

        List<Role> userRoles = roleProvider.getUserRoles(request);


        assertThat(userRoles.size()).isEqualTo(1);
        Role actualRole = userRoles.get(0);

        assertThat(actualRole.getTenant()).isEqualTo(expectedRole.getTenant());
        assertThat(actualRole.getSubscribeName()).isEqualTo(expectedRole.getSubscribeName());
        assertThat(actualRole.getServiceType()).isEqualTo(expectedRole.getServiceType());
    }

    @Test
    public void shouldReturnReadOnlyPermissionWhenRolesAreEmpty() {
        assertThat(roleProvider.userPermissionIsReadOnly(Lists.emptyList())).isTrue();
    }

    @Test
    public void shouldReturnNotReadOnlyPermissionWhenRolesArePresent() {
        assertThat(roleProvider.userPermissionIsReadOnly(Lists.list(new Role(EcompRole.READ, SAMPLE_SUBSCRIBER, SAMPLE_SERVICE, SAMPLE_TENANT)))).isFalse();
    }

    @Test
    public void userShouldHavePermissionToReadLogsWhenServiceAndTenantAreCorrect() {
        Role withoutPermission = new Role(EcompRole.READ, SAMPLE_SUBSCRIBER, SAMPLE_SERVICE, SAMPLE_TENANT);
        Role withPermission = new Role(EcompRole.READ, SAMPLE_SUBSCRIBER, SERVICE_TYPE_LOGS, TENANT_PERMITTED);

        assertThat(roleProvider.userPermissionIsReadLogs(Lists.list(withoutPermission, withPermission))).isTrue();
    }

    private void setSubscribers() {
        Subscriber subscriber = new Subscriber();
        subscriber.subscriberName = SAMPLE_SUBSCRIBER;
        subscriber.globalCustomerId = SAMPLE_CUSTOMER_ID;
        SubscriberList subscriberList = new SubscriberList(Lists.list(subscriber));
        when(aaiService.getFullSubscriberList()).thenReturn(subscriberListHttpResponse);
        when(subscriberListHttpResponse.getBody()).thenReturn(subscriberList);
    }

    private Map<Long, org.onap.portalsdk.core.domain.Role> createRoles() {
        org.onap.portalsdk.core.domain.Role role1 = new org.onap.portalsdk.core.domain.Role();
        role1.setName("read___role2");
        org.onap.portalsdk.core.domain.Role role2 = new org.onap.portalsdk.core.domain.Role();
        role2.setName("sampleSubscriber___sampleService___sampleTenant");
        return ImmutableMap.of(1L, role1, 2L, role2);
    }
}