package org.onap.vid.api;

import static java.util.stream.Collectors.toList;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static vid.automation.test.infra.Features.FLAG_2006_LIMIT_OWNING_ENTITY_SELECTION_BY_ROLES;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.testng.annotations.Test;
import vid.automation.test.Constants.Users;
import vid.automation.test.infra.FeatureTogglingTest;

public class PropertyControllerApiTest extends BaseApiTest {

    private final List<Object> allOwningEntities = toOwningEntitiesList(Stream.of("aaa1", "Melissa", "WayneHolland"));

    private final List<Object> singleOwningEntity = toOwningEntitiesList(Stream.of("WayneHolland"));

    @Test
    public void categoryParameter_whenUserFullyAllowed_allOwningEntitiesRetrieved() {
        categoryParameters_loginAndAssertOwningEntities(Users.SILVIA_ROBBINS_TYLER_SILVIA, allOwningEntities);
    }

    @Test
    @FeatureTogglingTest(FLAG_2006_LIMIT_OWNING_ENTITY_SELECTION_BY_ROLES)
    public void categoryParameter_whenUserNotFullyAllowed_onlySomeOwningEntitiesRetrieved() {
        categoryParameters_loginAndAssertOwningEntities(Users.PORFIRIO_GERHARDT, singleOwningEntity);
    }

    @Test
    @FeatureTogglingTest(value = FLAG_2006_LIMIT_OWNING_ENTITY_SELECTION_BY_ROLES, flagActive = false)
    public void categoryParameter_whenUserNotFullyAllowed_stillAllOwningEntitiesRetrieved() {
        categoryParameters_loginAndAssertOwningEntities(Users.PORFIRIO_GERHARDT, allOwningEntities);
    }



    public void categoryParameters_loginAndAssertOwningEntities(String user, List<Object> singleOwningEntities) {
        login(new UserCredentials(usersService.getUser(user)));

        String url = uri.toASCIIString() + "/category_parameter?familyName=PARAMETER_STANDARDIZATION";

        assertThat(restTemplate.getForObject(url, JsonNode.class),
            jsonPartEquals("categoryParameters.owningEntity", singleOwningEntities));
    }

    private List<Object> toOwningEntitiesList(Stream<String> owningEntitiesNames) {
        return owningEntitiesNames.map(it -> ImmutableMap.of(
            "name", it,
            "id", expectedIdByName.apply(it))
        ).collect(toList());
    }

    static private final Function<String, String> expectedIdByName = it ->
        "WayneHolland".equals(it) ? "d61e6f2d-12fa-4cc2-91df-7c244011d6fc" : it;

}
