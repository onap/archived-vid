package org.onap.vid.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;
import org.onap.simulator.presetGenerator.presets.mso.*;
import org.springframework.http.ResponseEntity;
import vid.automation.test.model.JobStatus;
import vid.automation.test.model.ServiceAction;
import vid.automation.test.services.SimulatorApi;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.SERVICE_NAME;
import static org.onap.vid.api.AsyncInstantiationBase.CREATE_BULK_OF_ALACARTE_REQUEST_WITH_VNF;
import static org.onap.vid.api.TestUtils.hasOrLacksOfEntry;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPreset;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPresets;

public class CreateInstanceWithFailedService {
    private CreateServiceWithFailedVnf.ResourceIds firstIds = new CreateServiceWithFailedVnf.ResourceIds();
    private AsyncInstantiationBase asyncInstantiationBase;
    private List<PresetMSOBaseCreateInstancePost> createPresets;
    private String serviceInstanceName = TestUtils.generateRandomAlphaNumeric(10);
    private ImmutableMap<PresetMSOServiceInstanceGen2WithNames.Keys, String> names;
    private List<PresetMSOOrchestrationRequestGet> inProgressPresets;
    private List<String> uuids;
    private String originalJobId;
    ObjectMapper objectMapper = new ObjectMapper();


    public CreateInstanceWithFailedService(AsyncInstantiationBase asyncInstantiationALaCarteApiTest) {
        this.asyncInstantiationBase = asyncInstantiationALaCarteApiTest;
    }

    private CreateInstanceWithFailedService createInstanceWithFailedService() {

        names = ImmutableMap.of(SERVICE_NAME, serviceInstanceName);

        final String serviceFailedStatusMessage = "The service instantiation is failed.";
        createPresets = ImmutableList.of(
                new PresetMSOCreateServiceInstanceGen2WithNamesAlacarteService(names, 0, firstIds.serviceReqId, firstIds.serviceId)
        );
        inProgressPresets = ImmutableList.of(
                new PresetMSOOrchestrationRequestGet("FAILED", firstIds.serviceReqId, serviceFailedStatusMessage)
        );
        List<BasePreset> presets = asyncInstantiationBase.getPresets(createPresets, inProgressPresets);

        registerExpectationFromPresets(presets, SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
        registerExpectationFromPreset(PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MDT1_TO_ATT_NC, SimulatorApi.RegistrationStrategy.APPEND);

        uuids = asyncInstantiationBase.createBulkOfInstances(false, 1, names, CREATE_BULK_OF_ALACARTE_REQUEST_WITH_VNF);
        return this;
    }

    protected void deployServiceFailedInstance() {
        createInstanceWithFailedService();
        firstTimeAssertion();
        prepareAndAssertJsonFileForCypressTest();
    }

    private void prepareAndAssertJsonFileForCypressTest() {
        final ResponseEntity<String> responseEntity = asyncInstantiationBase.getRetryBulk(originalJobId);
        String expected = TestUtils.convertRequest(objectMapper, "asyncInstantiation/ServiceWithFailedServiceInstance.json");
        expected = expected
                .replace("SERVICE_NAME", serviceInstanceName);
        String originalResponse = responseEntity.getBody();
        String responseToCompare = originalResponse.replaceFirst("(instanceName\":\")(.*?)(\")", "$1INSTANCE_NAME$3").replaceAll("(trackById\":\")(.*?)(\")", "$1TRACK_BY_ID$3");
        asyncInstantiationBase.assertJsonEquals(responseToCompare, expected);
    }

    private void firstTimeAssertion() {
        assertThat(uuids, hasSize(1));
        originalJobId = uuids.get(0);
        asyncInstantiationBase.assertServiceInfoSpecific1(originalJobId, JobStatus.FAILED, names.get(SERVICE_NAME), "us16807000", firstIds.serviceId, ServiceAction.INSTANTIATE);
        asyncInstantiationBase.assertAuditStatuses(originalJobId, asyncInstantiationBase.vidAuditStatusesFailed(originalJobId), null);
        assertThat(SimulatorApi.retrieveRecordedRequestsPathCounter(), allOf(
                hasOrLacksOfEntry(createPresets.get(0).getReqPath(), 1L),
                hasOrLacksOfEntry(inProgressPresets.get(0).getReqPath(), 1L)
        ));
    }
}
