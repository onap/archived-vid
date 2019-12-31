package org.onap.vid.api;

import static java.util.Arrays.stream;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_VALUES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.greaterThan;
import static org.onap.vid.api.TestUtils.convertRequest;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPreset;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.onap.sdc.ci.tests.datatypes.UserCredentials;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.vid.model.mso.MsoResponseWrapper2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.Constants.Users;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Features;
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

    @BeforeMethod
    public void setUp() {
        registerExpectationFromPreset(new PresetAAIGetSubscribersGet(), RegistrationStrategy.CLEAR_THEN_SET);
    }

    @AfterMethod
    protected void dropAllFromNameCounter() {
        AsyncJobsService asyncJobsService = new AsyncJobsService();
        asyncJobsService.muteAllAsyncJobs();
        asyncJobsService.dropAllFromNameCounter();
    }

    protected String templateTopologyUri(String jobId) {
        return uri.toASCIIString() + "/instantiationTemplates/templateTopology/" + jobId;
    }

    @DataProvider
    public static Object[][] deployFromCypressE2EFilenames() {
        return new Object[][]{
            {"asyncInstantiation/templates__instance_template.json"},
            {"asyncInstantiation/templates__instance_template_network.json"},
        };
    }

    @Test(dataProvider = "deployFromCypressE2EFilenames")
    public void templateTopology_givenDeployFromCypressE2E_getTemplateTopologyDataIsEquivalent(String fileName) {
        templateTopology_givenDeploy_templateTopologyIsEquivalentToBody(
            fileAsJsonNode(fileName));
    }

    @Test
    public void templateTopology_givenDeployFromEditedTemplateCypressE2E_getTemplateTopologyDataIsEquivalentToOriginalTemplate() {
        templateTopology_givenDeploy_templateTopologyIsEquivalent(
            fileAsJsonNode("asyncInstantiation/templates__instance_from_template__set_without_modify1.json"),
            fileAsJsonNode("asyncInstantiation/templates__instance_template.json"));
    }

    private ObjectNode templateInfoFromFile() {
        return fileAsJsonNode("asyncInstantiation/vidRequestCreateBulkOfMacro__template_info.json");
    }

    @Test
    public void templateTopology_givenDeploy_OriginalTemplateNotChanged() {
        String uuidOriginTemplate = postAsyncInstanceRequest(fileAsJsonNode("asyncInstantiation/templates__instance_template.json"));
        JsonNode originTemplateBeforeDeploy = restTemplate.getForObject(templateTopologyUri(uuidOriginTemplate), JsonNode.class);

        ObjectNode changedNode = originTemplateBeforeDeploy.deepCopy();
        changedNode.put("instanceName", "Cloned_Node_Instance_Name");
        postAsyncInstanceRequest(changedNode);

        JsonNode originTemplateAfterDeploy = restTemplate.getForObject(templateTopologyUri(uuidOriginTemplate), JsonNode.class);
        assertThat(cleanupTemplate(originTemplateBeforeDeploy), jsonEquals(cleanupTemplate(originTemplateAfterDeploy)));

    }

    @Test
    @FeatureTogglingTest(Features.FLAG_2004_CREATE_ANOTHER_INSTANCE_FROM_TEMPLATE)
    public void templateTopology_givenDeploy_getServiceInfoHoldsRequestSummary() {
        ObjectNode request =
            fileAsJsonNode(CREATE_BULK_OF_MACRO_REQUEST)
                .put("bulkSize", 1)
                .put("pause", false);

        String jobId = postAsyncInstanceRequest(request);

        assertThat(fetchRecentTemplateInfo(request.at("/modelInfo/modelVersionId").asText()), allOf(
            jsonPartEquals("jobId", jobId),
            jsonPartEquals("requestSummary", ImmutableMap.of(
                "vnf", 1L,
                "vfModule", 2L,
                "volumeGroup", 1L
            ))));
    }

    @Test
    @FeatureTogglingTest(Features.FLAG_2004_CREATE_ANOTHER_INSTANCE_FROM_TEMPLATE)
    public void templateTopology_givenDeploy_getServiceInfoReturnsCypressE2EFile() {
        ObjectNode request =
            fileAsJsonNode(CREATE_BULK_OF_MACRO_REQUEST)
                .put("bulkSize", 1)
                .put("pause", false);

        String jobId = postAsyncInstanceRequest(request);

        assertThat(fetchRecentTemplateInfo(request.at("/modelInfo/modelVersionId").asText()), allOf(
            jsonPartEquals("jobId", jobId),
            jsonEquals(templateInfoFromFile()).when(IGNORING_VALUES), // Assert only field types
            jsonEquals(templateInfoFromFile()).whenIgnoringPaths(
                // Ignore the fields where values are always changing
                "id", "templateId", "jobId",
                "created", "createdBulkDate",
                "modified", "statusModifiedDate",
                "jobStatus"
            )));
    }

    private JsonNode fetchRecentTemplateInfo(String serviceModelId) {
        return stream(restTemplate.getForObject(getTemplateInfoUrl(serviceModelId), JsonNode[].class))
            .findFirst()
            .orElseGet(() -> {
                throw new AssertionError(getTemplateInfoUrl(serviceModelId) + " returned zero results");
            });
    }

    private ObjectNode fileAsJsonNode(String fileName) {
        try {
            return objectMapper.readValue(
                convertRequest(objectMapper, fileName),
                ObjectNode.class);
        } catch (IOException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    public void templateTopology_givenDeploy_templateTopologyIsEquivalentToBody(JsonNode body) {
        templateTopology_givenDeploy_templateTopologyIsEquivalent(body, body);
    }

    public void templateTopology_givenDeploy_templateTopologyIsEquivalent(JsonNode body, JsonNode expectedTemplateTopology) {
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
