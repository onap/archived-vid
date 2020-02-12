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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.assertj.core.util.Lists;
import org.mockito.Mock;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.exceptions.RoleParsingException;
import org.onap.vid.category.CategoryParametersResponse;
import org.onap.vid.model.CategoryParameter.Family;
import org.onap.vid.model.Subscriber;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.services.AaiService;
import org.onap.vid.services.CategoryParameterService;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RoleProviderTest {

    private static final String SAMPLE_SUBSCRIBER = "sampleSubscriber";
    private static final String SAMPLE_SUBSCRIBER_ID = "subscriberId";
    private static final String SERVICE_TYPE_LOGS = "LOGS";
    private static final String TENANT_PERMITTED = "PERMITTED";
    private static final String SAMPLE_SERVICE = "sampleService";
    private static final String SAMPLE_TENANT = "sampleTenant";
    private static final String SAMPLE_ROLE_PREFIX = "prefix";
    private static final String EXISTING_OWNING_ENTITY_NAME = "WayneHolland";
    private static final String EXISTING_OWNING_ENTITY_ID = "d61e6f2d-12fa-4cc2-91df-7c244011d6fc";
    private static final String NOT_EXISTING_OWNING_ENTITY_NAME = "notExistingOwningEntity";

    private static final String CATEGORY_PARAMETER_RESPONSE = "{\n"
                        + "  \"categoryParameters\": {\n"
                        + "    \"lineOfBusiness\": [\n"
                        + "      {\n"
                        + "        \"id\": \"ONAP\",\n"
                        + "        \"name\": \"ONAP\"\n"
                        + "      },\n"
                        + "      {\n"
                        + "        \"id\": \"zzz1\",\n"
                        + "        \"name\": \"zzz1\"\n"
                        + "      }\n"
                        + "    ],\n"
                        + "    \"owningEntity\": [\n"
                        + "      {\n"
                        + "        \"id\": \"aaa1\",\n"
                        + "        \"name\": \"aaa1\"\n"
                        + "      },\n"
                        + "      {\n"
                        + "        \"id\": \"" + EXISTING_OWNING_ENTITY_ID + "\",\n"
                        + "        \"name\": \"" + EXISTING_OWNING_ENTITY_NAME + "\"\n"
                        + "      },\n"
                        + "      {\n"
                        + "        \"id\": \"Melissa\",\n"
                        + "        \"name\": \"Melissa\"\n"
                        + "      }\n"
                        + "    ],\n"
                        + "    \"project\": [\n"
                        + "      {\n"
                        + "        \"id\": \"WATKINS\",\n"
                        + "        \"name\": \"WATKINS\"\n"
                        + "      },\n"
                        + "      {\n"
                        + "        \"id\": \"x1\",\n"
                        + "        \"name\": \"x1\"\n"
                        + "      },\n"
                        + "      {\n"
                        + "        \"id\": \"yyy1\",\n"
                        + "        \"name\": \"yyy1\"\n"
                        + "      }\n"
                        + "    ],\n"
                        + "    \"platform\": [\n"
                        + "      {\n"
                        + "        \"id\": \"platform\",\n"
                        + "        \"name\": \"platform\"\n"
                        + "      },\n"
                        + "      {\n"
                        + "        \"id\": \"xxx1\",\n"
                        + "        \"name\": \"xxx1\"\n"
                        + "      }\n"
                        + "    ]\n"
                        + "  }\n"
                        + "}";


    @Mock
    private AaiService aaiService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private AaiResponse<SubscriberList> subscriberListResponse;

    @Mock
    private RoleValidatorFactory roleValidatorFactory;

    @Mock
    private CategoryParameterService categoryParameterService;

    private RoleProvider roleProvider;


    @BeforeMethod
    public void setUp() throws JsonProcessingException {
        initMocks(this);
        roleProvider = new RoleProvider(aaiService, roleValidatorFactory, httpServletRequest -> 5, httpServletRequest -> createRoles(),
            categoryParameterService);

        ObjectMapper objectMapper = new ObjectMapper();
        CategoryParametersResponse categoryParameterResponse = objectMapper.readValue(CATEGORY_PARAMETER_RESPONSE, CategoryParametersResponse.class);;
        when(categoryParameterService.getCategoryParameters(Family.PARAMETER_STANDARDIZATION)).thenReturn(categoryParameterResponse);

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

        Role role = roleProvider.createRoleFromStringArr(roleParts, SAMPLE_ROLE_PREFIX,
            roleProvider.owningEntityNameToOwningEntityIdMapper());

        assertThat(role.getEcompRole()).isEqualTo(EcompRole.READ);
        assertThat(role.getSubscriberId()).isEqualTo(SAMPLE_SUBSCRIBER_ID);
        assertThat(role.getTenant()).isEqualTo(SAMPLE_TENANT);
        assertThat(role.getServiceType()).isEqualTo(SAMPLE_SERVICE);
    }

    @Test
    public void shouldProperlyCreateRoleWhenTenantIsNotProvided() throws RoleParsingException {
        setSubscribers();

        String[] roleParts = {SAMPLE_SUBSCRIBER, SAMPLE_SERVICE};

        Role role = roleProvider.createRoleFromStringArr(roleParts, SAMPLE_ROLE_PREFIX,
            roleProvider.owningEntityNameToOwningEntityIdMapper());

        assertThat(role.getEcompRole()).isEqualTo(EcompRole.READ);
        assertThat(role.getSubscriberId()).isEqualTo(SAMPLE_SUBSCRIBER_ID);
        assertThat(role.getServiceType()).isEqualTo(SAMPLE_SERVICE);
        assertThat(role.getTenant()).isNullOrEmpty();
    }

    @Test(expectedExceptions = RoleParsingException.class)
    public void shouldRaiseExceptionWhenRolePartsAreIncomplete() throws RoleParsingException {
        setSubscribers();

        roleProvider.createRoleFromStringArr(new String[]{SAMPLE_SUBSCRIBER}, SAMPLE_ROLE_PREFIX,
            roleProvider.owningEntityNameToOwningEntityIdMapper());
    }

    @Test
    public void shouldProperlyRetrieveUserRolesWhenPermissionIsDifferentThanRead() {
        Role expectedRole = new Role(EcompRole.READ, SAMPLE_SUBSCRIBER_ID, SAMPLE_SERVICE, SAMPLE_TENANT, owningEntityId());
        setSubscribers();

        List<Role> userRoles = roleProvider.getUserRoles(request);


        assertThat(userRoles.size()).isEqualTo(1);
        Role actualRole = userRoles.get(0);

        assertThat(actualRole.getTenant()).isEqualTo(expectedRole.getTenant());
        assertThat(actualRole.getSubscriberId()).isEqualTo(expectedRole.getSubscriberId());
        assertThat(actualRole.getServiceType()).isEqualTo(expectedRole.getServiceType());
    }

    @Test
    public void shouldReturnReadOnlyPermissionWhenRolesAreEmpty() {
        assertThat(roleProvider.userPermissionIsReadOnly(Lists.emptyList())).isTrue();
    }

    @Test
    public void shouldReturnNotReadOnlyPermissionWhenRolesArePresent() {
        assertThat(roleProvider.userPermissionIsReadOnly(Lists.list(new Role(
            EcompRole.READ, SAMPLE_SUBSCRIBER, SAMPLE_SERVICE, SAMPLE_TENANT, owningEntityId())))).isFalse();
    }

    @Test
    public void userShouldHavePermissionToReadLogsWhenServiceAndTenantAreCorrect() {
        Role withoutPermission = new Role(EcompRole.READ, SAMPLE_SUBSCRIBER, SAMPLE_SERVICE, SAMPLE_TENANT, owningEntityId());
        Role withPermission = new Role(EcompRole.READ, SAMPLE_SUBSCRIBER, SERVICE_TYPE_LOGS, TENANT_PERMITTED, owningEntityId());

        assertThat(roleProvider.userPermissionIsReadLogs(Lists.list(withoutPermission, withPermission))).isTrue();
    }

    @Test
    public void getUserRolesValidator_shouldReturnValidatorFromFactory() {
        RoleValidator expectedRoleValidator = new AlwaysValidRoleValidator();
        when(roleValidatorFactory.by(any())).thenReturn(expectedRoleValidator);

        RoleValidator result = roleProvider.getUserRolesValidator(request);

        assertThat(result).isEqualTo(expectedRoleValidator);
    }

    @DataProvider
    public static Object[][] owningEntityNameAndId() {
        return new Object[][] {
            {"owning entity name exist on the response, id is returned ", EXISTING_OWNING_ENTITY_NAME, EXISTING_OWNING_ENTITY_ID},
            {"owning entity name dont exist on the response, name is returned", NOT_EXISTING_OWNING_ENTITY_NAME, NOT_EXISTING_OWNING_ENTITY_NAME},
        };
    }

    @Test(dataProvider = "owningEntityNameAndId")
    public void translateOwningEntityNameToOwningEntityId_shouldTranslateNameToId(String description, String existingOwningEntityName, String expectedId) throws JsonProcessingException {
        String owningEntityId = roleProvider.translateOwningEntityNameToOwningEntityId(existingOwningEntityName,
            roleProvider.owningEntityNameToOwningEntityIdMapper());
        Assert.assertEquals(owningEntityId, expectedId);
    }



    private String owningEntityId() {
        // while translateOwningEntityNameToOwningEntityId does nothing, no translation happens.
        // this will be changed later.
        return SAMPLE_SUBSCRIBER;
    }

    private void setSubscribers() {
        Subscriber subscriber = new Subscriber();
        subscriber.subscriberName = SAMPLE_SUBSCRIBER;
        subscriber.globalCustomerId = SAMPLE_SUBSCRIBER_ID;
        SubscriberList subscriberList = new SubscriberList(Lists.list(subscriber));
        when(aaiService.getFullSubscriberList()).thenReturn(subscriberListResponse);
        when(subscriberListResponse.getT()).thenReturn(subscriberList);
    }

    private Map<Long, org.onap.portalsdk.core.domain.Role> createRoles() {
        org.onap.portalsdk.core.domain.Role role1 = new org.onap.portalsdk.core.domain.Role();
        role1.setName("read___role2");
        org.onap.portalsdk.core.domain.Role role2 = new org.onap.portalsdk.core.domain.Role();
        role2.setName("sampleSubscriber___sampleService___sampleTenant");
        return ImmutableMap.of(1L, role1, 2L, role2);
    }
}
