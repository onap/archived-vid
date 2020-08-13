package org.onap.vid.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.hamcrest.Matchers;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;
import org.onap.simulator.presetGenerator.presets.mso.*;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceMetadataGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import org.onap.vid.model.asyncInstantiation.ServiceInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.ModelInfo;
import vid.automation.test.model.JobStatus;
import vid.automation.test.model.ServiceAction;
import vid.automation.test.services.SimulatorApi;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.SERVICE_NAME;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.VNF_NAME;
import static org.onap.vid.api.TestUtils.hasOrLacksOfEntry;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPreset;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPresets;

//CreateServiceWithFailedVnf is common for API test and UI test,
//so if you change it, make sure both test are compatible with your changes
public class CreateServiceWithFailedVnf {
    private AsyncInstantiationBase asyncInstantiationBase;
    private ImmutableMap<PresetMSOServiceInstanceGen2WithNames.Keys, String> names;
    private List<PresetMSOBaseCreateInstancePost> createPresets;
    private List<PresetMSOOrchestrationRequestGet> inProgressPresets;
    private List<String> uuids;
    private List<String> retryUuids;
    private String originalJobId;
    private String serviceInstanceName = TestUtils.generateRandomAlphaNumeric(10);
    private ResourceIds firstIds = new ResourceIds();
    private ResourceIds retryIds = new ResourceIds();
    private String vnfEditedName = TestUtils.generateRandomAlphaNumeric(10);
    ObjectMapper objectMapper = new ObjectMapper();
    public final ModelInfo serviceComplexService = new ModelInfo("e3c34d88-a216-4f1d-a782-9af9f9588705", "0367689e-d41e-483f-b200-eab17e4a7f8d", "service-Complexservice-aLaCarte-csar-2.zip");

    public CreateServiceWithFailedVnf(AsyncInstantiationBase asyncInstantiationALaCarteApiTest) {
        this.asyncInstantiationBase = asyncInstantiationALaCarteApiTest;
    }

    public ImmutableMap<PresetMSOServiceInstanceGen2WithNames.Keys, String> getNames() {
        return names;
    }

    public ResourceIds getFirstIds() {
        return firstIds;
    }

    public static class ResourceIds {
        public String serviceId =UUID.randomUUID().toString();
        public String serviceReqId =UUID.randomUUID().toString();
        public String vnfId =UUID.randomUUID().toString();
        public String vnfReqId =UUID.randomUUID().toString();
    }


    public CreateServiceWithFailedVnf createServicesWithVnfCompletedWithError() {
/*
Legit Preset  ||  deploy 1 Service, 1 VNF which will fail
        -> JobStatus of service is COMPLETED_WITH_ERRORS, audit
           is adequate; strict simulator comapre
 */

        names = ImmutableMap.of(SERVICE_NAME, serviceInstanceName, VNF_NAME, serviceInstanceName+"_vnf");



        final String vnfFailedStatusMessage = "Vnf failed.";
        createPresets = ImmutableList.of(
                new PresetMSOCreateServiceInstanceGen2WithNamesAlacarteService(names, 0, firstIds.serviceReqId, firstIds.serviceId),
                new PresetMSOCreateVNFInstanceOnlyRelatedServiceInstance(names.get(VNF_NAME),firstIds.vnfReqId , firstIds.serviceId, firstIds.vnfId, 0)
        );
        inProgressPresets = ImmutableList.of(
                new PresetMSOOrchestrationRequestGet("COMPLETE", firstIds.serviceReqId),
                new PresetMSOOrchestrationRequestGet("FAILED", firstIds.vnfReqId, vnfFailedStatusMessage)
        );
        List<BasePreset> presets = asyncInstantiationBase.getPresets(createPresets, inProgressPresets);

        registerExpectationFromPresets(presets, SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
        registerExpectationFromPreset(PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MDT1_TO_ATT_NC, SimulatorApi.RegistrationStrategy.APPEND);

        uuids = asyncInstantiationBase.createBulkOfInstances(false, 1, names, AsyncInstantiationBase.CREATE_BULK_OF_ALACARTE_REQUEST_WITH_VNF);
        return this;
    }

    public void firstTimeAssertion() {
        assertThat(uuids, hasSize(1));
        originalJobId = uuids.get(0);
        boolean isPauseOnFailureFlagOn = Features.FLAG_2008_PAUSE_VFMODULE_INSTANTIATION_FAILURE.isActive();

        asyncInstantiationBase.assertServiceInfoSpecific1(originalJobId, isPauseOnFailureFlagOn ?
                JobStatus.FAILED_AND_PAUSED : JobStatus.COMPLETED_WITH_ERRORS, names.get(SERVICE_NAME), "us16807000", firstIds.serviceId, ServiceAction.INSTANTIATE);
        asyncInstantiationBase.assertAuditStatuses(originalJobId, isPauseOnFailureFlagOn ?
                asyncInstantiationBase.vidAuditStatusesFailedAndPaused(originalJobId) :
                asyncInstantiationBase.vidAuditStatusesCompletedWithErrors(originalJobId),null);
        assertThat(SimulatorApi.retrieveRecordedRequestsPathCounter(), allOf(
                hasOrLacksOfEntry(createPresets.get(0).getReqPath(), 1L),
                hasOrLacksOfEntry(createPresets.get(1).getReqPath(), 1L),
                hasOrLacksOfEntry(inProgressPresets.get(0).getReqPath(), 1L),
                hasOrLacksOfEntry(inProgressPresets.get(1).getReqPath(), 1L)
        ));
    }

    public String getBulkForRetry(){
        final ResponseEntity<String> responseEntity= asyncInstantiationBase.getRetryBulk(originalJobId);
        String expected = TestUtils.convertRequest(objectMapper, "asyncInstantiation/ServiceTreeForRetry_serviceInstance.json");
        expected = expected
                .replace("SERVICE_NAME", serviceInstanceName);
        String originalResponse = responseEntity.getBody();
        String responseToCompare = originalResponse.replaceFirst( "(instanceId\":\")(.*?)(\")", "$1INSTANCE_ID$3")
                .replaceAll( "(trackById\":\")(.*?)(\")", "$1TRACK_BY_ID$3");
        asyncInstantiationBase.assertJsonEquals(responseToCompare, expected);
        return originalResponse;
    }

    public void getBulkForRetryNotFound() {
        UUID jobId= UUID.randomUUID();
        final ResponseEntity<String> response = asyncInstantiationBase.getRetryBulk(jobId.toString());
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(response.getBody(),containsString("Failed to retrieve class org.onap.vid.dao.JobRequest with JOB_ID "+jobId+" from table. no resource found"));
    }

    private void secondRegistration(String vnfName) {
        createPresets = ImmutableList.of(
                new PresetMSOCreateVNFInstanceOnlyRelatedServiceInstance(vnfName, retryIds.vnfReqId, firstIds.serviceId, retryIds.vnfId, 0)
        );
        inProgressPresets = ImmutableList.of(
                new PresetMSOOrchestrationRequestGet("COMPLETE", retryIds.vnfReqId)
        );

        registerExpectationFromPresets(asyncInstantiationBase.getPresets(createPresets, inProgressPresets), SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
        registerExpectationFromPresets(ImmutableList.of(
                        PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MDT1_TO_ATT_NC,
                        new PresetSDCGetServiceMetadataGet(serviceComplexService),
                        new PresetSDCGetServiceToscaModelGet(serviceComplexService)),
                SimulatorApi.RegistrationStrategy.APPEND);


    }

    public String deployService1FailedVnf(){
        createServicesWithVnfCompletedWithError();
        firstTimeAssertion();
        return getBulkForRetry();
    }

    public void assertResourceAuditStatus(String bulkForRetry) {
        String vnfTrackById = extractVnfTrackById(bulkForRetry);

        Map<String, Object> auditStatus = (Map) asyncInstantiationBase.getResourceAuditInfo(vnfTrackById);
        assertThat(auditStatus.get("jobStatus"), equalTo("FAILED"));
        assertThat(auditStatus.get("additionalInfo"), equalTo("Vnf failed."));
        assertThat(auditStatus.get("requestId"), equalTo(firstIds.vnfReqId));
    }

    private String extractVnfTrackById(String bulk) {
        Map<String, Object> serviceInstantiation = null;
        try {
            serviceInstantiation = objectMapper.readValue(bulk, new TypeReference<Map<String, Object>>(){});
            Map<String, Object> vnf = (Map) ((Map) serviceInstantiation.get("vnfs")).get("vSAMP12 1");
            return vnf.get("trackById").toString();
        } catch (IOException e) {
            return null;
        }
    }


    public void secondRegistration() {
        secondRegistration(names.get(VNF_NAME));
    }

    public void retryJob() {
        //retry the previous job
        retryUuids = asyncInstantiationBase.retryJob(originalJobId);
    }

    public void retryJobWithOtherDataAndAssert(String requestBody){
        retryUuids = asyncInstantiationBase.retryJobWithChangedData(originalJobId, requestBody);
        retryAssertion();
        simulatorCallsAssertion();
    }

    public String changeSomeDataAndRegisterToSimulator(String payload){
        payload = payload.replace(names.get(VNF_NAME), vnfEditedName);
        secondRegistration(vnfEditedName);
        return payload;
    }


    public void retryAssertion() {

        assertThat(retryUuids, Matchers.hasSize(1));
        String retryJobId = retryUuids.get(0);
        assertThat(retryJobId, not(equalTo(originalJobId)));
        asyncInstantiationBase.assertServiceInfoSpecific1(retryJobId, JobStatus.COMPLETED, names.get(SERVICE_NAME), "us16807000", firstIds.serviceId, ServiceAction.UPDATE);

        //make sure original job is retry is disabled.
        Optional<ServiceInfo> optionalServiceInfo = asyncInstantiationBase.serviceListCall().getBody().stream().filter(si -> originalJobId.equals(si.jobId)).findFirst();
        assertTrue(optionalServiceInfo.isPresent());
        assertFalse(optionalServiceInfo.get().isRetryEnabled);
    }

    public void simulatorCallsAssertion() {
        assertThat(SimulatorApi.retrieveRecordedRequestsPathCounter(), allOf(
                hasOrLacksOfEntry(createPresets.get(0).getReqPath(), 1L),
                hasOrLacksOfEntry(inProgressPresets.get(0).getReqPath(), 1L)
        ));
    }


}
