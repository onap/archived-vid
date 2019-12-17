package org.onap.vid.api;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.greaterThan;
import static org.onap.vid.api.TestUtils.convertRequest;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPreset;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.vid.model.mso.MsoResponseWrapper2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import vid.automation.test.Constants.Users;
import vid.automation.test.model.User;
import vid.automation.test.services.AsyncJobsService;
import vid.automation.test.services.SimulatorApi.RegistrationStrategy;

public class InstantiationTemplatesApiTest extends AsyncInstantiationBase {

    /*
    Testing the Template Topology API should be very thin, given the following
    assumptions:

      - Template topology API is relying on Retry's logic.

      - The templates themselves are an actual representation of the initial
        state in VID's backend. This is all the knowledge that used to create
        a service in the first time. So if API is fed with same state, it already
        should be able to reiterate another instance.


    The tests below will verify that:

      - A request resulting from Cypress test on "instantiation-templates.e2e.ts"
        is accepted by API endpoint

      - A valid "regular" (not from template) request, yields a template that a
        Cypress is able to deploy.

      These two tests are, technically,  cyclic.

      Currently the only test below is shortcutting the both tests, by checking
      that feeding a Cypress input yields a Template that is the same. This is
      not perfect, but currently what we have.

     */

    @Override
    public UserCredentials getUserCredentials() {
        User user = usersService.getUser(Users.SILVIA_ROBBINS_TYLER_SILVIA);
        return new UserCredentials(user.credentials.userId, user.credentials.password, Users.SILVIA_ROBBINS_TYLER_SILVIA, "", "");
    }

    @AfterMethod
    protected void dropAllFromNameCounter() {
        AsyncJobsService asyncJobsService = new AsyncJobsService();
        asyncJobsService.muteAllAsyncJobs();
        asyncJobsService.dropAllFromNameCounter();
    }

    protected String templateTopologyUri(String jobId) {
        return uri.toASCIIString() + "/asyncInstantiation/templateTopology/" + jobId;
    }

    @Test
    public void templateTopology_givenDeployFromCypressE2E_getTemplateTopologyDataIsEquivalent() throws IOException {
        templateTopology_givenDeploy_templateTopologyIsEquivalentToBody(
            fileAsJsonNode("asyncInstantiation/templates__instance_template.json"));
    }

    @Test
    public void templateTopology_givenDeployFromEditedTemplateCypressE2E_getTemplateTopologyDataIsEquivalentToOriginalTemplate() throws IOException {
        templateTopology_givenDeploy_templateTopologyIsEquivalent(
            fileAsJsonNode("asyncInstantiation/templates__instance_from_template__set_without_modify.json"),
            fileAsJsonNode("asyncInstantiation/templates__instance_template.json"));
    }

    private JsonNode fileAsJsonNode(String fileName) throws IOException {
        return objectMapper.readValue(
            convertRequest(objectMapper, fileName),
            JsonNode.class);
    }

    public void templateTopology_givenDeploy_templateTopologyIsEquivalentToBody(JsonNode body) {
        templateTopology_givenDeploy_templateTopologyIsEquivalent(body, body);
    }

    public void templateTopology_givenDeploy_templateTopologyIsEquivalent(JsonNode body, JsonNode expectedTemplateTopology) {
        registerExpectationFromPreset(new PresetAAIGetSubscribersGet(), RegistrationStrategy.CLEAR_THEN_SET);

        String uuid1 = postAsyncInstanceRequest(body);
        JsonNode templateTopology1 = restTemplate.getForObject(templateTopologyUri(uuid1), JsonNode.class);

        assertThat(cleanupTemplate(templateTopology1), jsonEquals(cleanupTemplate(expectedTemplateTopology)));
    }

    private JsonNode cleanupTemplate(JsonNode templateTopology) {
        return Stream.of(templateTopology)
            .map(this::removeTrackById)
            .map(this::removeNullValues)
            .findAny().get();
    }

    private JsonNode removeTrackById(JsonNode node) {
        return removeAny(node, it -> it.getKey().equals("trackById"));
    }

    private JsonNode removeNullValues(JsonNode node) {
        return removeAny(node, it -> it.getValue().isNull());
    }

    private JsonNode removeAny(JsonNode node, Predicate<Entry<String, JsonNode>> entryPredicate) {
        if (node.isObject()) {
            ((ObjectNode) node).remove(
                Streams.fromIterator(node.fields())
                    .filter(entryPredicate)
                    .map(Entry::getKey)
                    .collect(Collectors.toList())
            );

            for (JsonNode child : node) {
                removeAny(child, entryPredicate);
            }
        }

        return node;
    }

    private <T> String postAsyncInstanceRequest(T body) {
        String[] jobsUuids = (String[]) restTemplate.exchange(
            getCreateBulkUri(),
            HttpMethod.POST,
            new HttpEntity<>(body),
            new ParameterizedTypeReference<MsoResponseWrapper2<String[]>>() {
            })
            .getBody().getEntity();

        assertThat(jobsUuids, arrayWithSize(greaterThan(0)));
        return jobsUuids[0];
    }

}
