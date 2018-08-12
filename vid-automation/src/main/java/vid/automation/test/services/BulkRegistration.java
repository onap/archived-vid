package vid.automation.test.services;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.opencomp.simulator.presetGenerator.presets.aai.*;
import org.opencomp.simulator.presetGenerator.presets.mso.*;
import org.opencomp.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceMetadataGet;
import org.opencomp.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import vid.automation.test.Constants;

import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;

public class BulkRegistration {

    public static void searchExistingServiceInstance() {
        searchExistingServiceInstance("Active");
    }

    public static void searchExistingServiceInstance(String orchStatus) {
        genericSearchExistingServiceInstance();
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_USP_VOICE,
                        Constants.RegisterToSimulator.SearchForServiceInstance.FILTER_SERVICE_INSTANCE_BY_ID,
                        Constants.RegisterToSimulator.SearchForServiceInstance.NAMED_QUERY_VIEW_EDIT,
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SDC_CATALOG_SERVICE_VID_TEST_444,
                        Constants.RegisterToSimulator.CreateNewServiceInstance.deploy.GET_AIC_ZONES
                }, ImmutableMap.<String, Object>of("<ORCH_STATUS>", orchStatus), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void searchExistingServiceInstanceByOEAndProject(){
        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                new PresetAAIGetModelsByOwningEntity("Wireline"),
                new PresetAAIGetModelsByProject("x1"),
                new PresetAAIGetModelsByProject("yyy1")
                ), APPEND);
    }

    public static void searchExistingCRServiceInstance(String orchStatus) {
        genericSearchExistingServiceInstance();
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_USP_VOICE_CR,
                        Constants.RegisterToSimulator.SearchForServiceInstance.FILTER_CR_SERVICE_INSTANCE_BY_ID,
                        Constants.RegisterToSimulator.SearchForServiceInstance.NAMED_QUERY_CR_VIEW_EDIT,
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SDC_CATALOG_SERVICE_VID_TEST_CR,
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_MSO_INSTANCE_ORCH_STATUS_REQ,
                        Constants.RegisterToSimulator.CreateNewServiceInstance.deploy.GET_AIC_ZONES
                }, ImmutableMap.<String, Object>of("<ORCH_STATUS>", orchStatus, "<SERVICE_INSTANCE_ID>", "3f93c7cb-2fd0-4557-9514-e189b7testCR"), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void searchExistingVFServiceWithVFCInstanceGroupInstance(String orchStatus) {
        genericSearchExistingServiceInstance();
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.AddSubinterface.GET_SUBSCRIBERS_FOR_CUSTOMER_USP_VOICE_VFC_IG,
                        Constants.RegisterToSimulator.AddSubinterface.FILTER_VFC_IG_SERVICE_INSTANCE_BY_ID,
                        Constants.RegisterToSimulator.AddSubinterface.NAMED_QUERY_VFC_IG_VIEW_EDIT,
                        Constants.RegisterToSimulator.AddSubinterface.GET_SDC_CATALOG_SERVICE_VID_TEST_444,
                        Constants.RegisterToSimulator.AddSubinterface.GET_MSO_VFC_IG_INSTANCE_ORCH_STATUS_REQ,
                 }, ImmutableMap.<String, Object>of("<ORCH_STATUS>", orchStatus), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void genericSearchExistingServiceInstance() {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.genericRequest.ECOMP_PORTAL_GET_SESSION_SLOT_CHECK_INTERVAL,
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_FULL_SUBSCRIBERS,
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SERVICES

                }, ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void searchExistingServiceInstance2(String orchStatus) {
        genericSearchExistingServiceInstance();
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_FIREWALL_MISC,
                        Constants.RegisterToSimulator.SearchForServiceInstance.FILTER_SERVICE_INSTANCE_BY_ID_2,
                        Constants.RegisterToSimulator.SearchForServiceInstance.NAMED_QUERY_VIEW_EDIT_2
                }, ImmutableMap.<String, Object>of("<ORCH_STATUS>", orchStatus), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void searchExistingServiceInstancePortMirroring(String orchStatus, String desiredCloudRegionId) {
        searchExistingServiceInstancePortMirroring(orchStatus, true, desiredCloudRegionId);
    }

    public static void searchExistingServiceInstancePortMirroring(String orchStatus, boolean isMirrored, String desiredCloudRegionId) {
        genericSearchExistingServiceInstance();
        final String configurationId = "9533-config-LB1113";
        final String portInterfaceId = "d35bf534-7d8e-4cb4-87f9-0a8bb6cd47b2";
        final String modelToReplaceWith ="pm1111_equip_model_rename.zip";

        SimulatorApi.registerExpectationFromPreset(new PresetAAICloudRegionAndSourceFromConfigurationPut(configurationId, desiredCloudRegionId), APPEND);
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetPortMirroringSourcePorts(configurationId, portInterfaceId, "i'm a port", isMirrored), APPEND);
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_USP_VOICE,
                        Constants.RegisterToSimulator.SearchForServiceInstance.FILTER_SERVICE_INSTANCE_BY_ID_PM,
                        Constants.RegisterToSimulator.SearchForServiceInstance.NAMED_QUERY_VIEW_EDIT_PM,
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SDC_CATALOG_SERVICE_PM,
                        Constants.RegisterToSimulator.CreateNewServiceInstance.deploy.GET_AIC_ZONES
                }, ImmutableMap.<String, Object>of(
                        "<ORCH_STATUS>", orchStatus, "<IS_MIRRORED>", isMirrored,
                        "pm1111.zip", modelToReplaceWith
                        ), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void associatePnf() {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.pProbe.GET_SERVICE_INSTANCE_WITH_LOGICAL_LINKS,
                        Constants.RegisterToSimulator.pProbe.GET_SPECIFIC_PNF,
                        Constants.RegisterToSimulator.pProbe.ADD_PNF_RELATIONSHIP,
                        Constants.RegisterToSimulator.pProbe.GET_ADD_PNF_RELATIONSHIP_ORCH_REQ
                }, ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void searchPnfError() {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.pProbe.GET_SERVICE_INSTANCE_WITH_LOGICAL_LINKS,
                        Constants.RegisterToSimulator.pProbe.GET_SPECIFIC_PNF_ERROR
                }, ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
    }
    public static void associatePnfError() {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.pProbe.GET_SERVICE_INSTANCE_WITH_LOGICAL_LINKS,
                        Constants.RegisterToSimulator.pProbe.GET_SPECIFIC_PNF,
                        Constants.RegisterToSimulator.pProbe.ADD_PNF_RELATIONSHIP_ERROR
                }, ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void dissociatePnf() {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.pProbe.REMOVE_PNF_RELATIONSHIP,
                        Constants.RegisterToSimulator.pProbe.GET_REMOVE_PNF_RELATIONSHIP_ORCH_REQ
                }, ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void getAssociatedPnfs() {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.pProbe.GET_SERVICE_INSTANCE_WITH_LOGICAL_LINKS,
                        Constants.RegisterToSimulator.pProbe.GET_LOGICAL_LINK
                }, ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void activateServiceInstance(String action) {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.pProbe.GET_SERVICE_INSTANCE_WITH_LOGICAL_LINKS,
                        Constants.RegisterToSimulator.activateDeactivate.ACTIVATE_SERVICE_INSTANCE,
                        Constants.RegisterToSimulator.activateDeactivate.ACTIVATE_SERVICE_INSTANCE_ORCH_REQUEST
                }, ImmutableMap.<String, Object>of("<ACTIVE_ACTION>", action), SimulatorApi.RegistrationStrategy.APPEND);
    }


    public static void activateDeactivateConfiguration(String orchStatus, String action, String desiredCloudRegionId) {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.createConfiguration.MSO_ACTIVATE_CONFIGURATION,
                }, ImmutableMap.<String, Object>of("<ACTION>",action,"mdt1", desiredCloudRegionId), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void deleteConfiguration(String desiredCloudRegionId) {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.createConfiguration.MSO_DELETE_CONFIGURATION,
                        Constants.RegisterToSimulator.createConfiguration.MSO_CREATE_CONFIGURATION_ORCH_REQ
                }, ImmutableMap.of("mdt1", desiredCloudRegionId), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void enableDisablePort(String action, String desiredCloudRegionId){
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.createConfiguration.MSO_ACTIVATE_CONFIGURATION,
                        Constants.RegisterToSimulator.createConfiguration.MSO_ENABLE_DISABLE_PORT,
                }, ImmutableMap.<String, Object>of("<ACTION>", action,"mdt1", desiredCloudRegionId), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void addNetwork() {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_Mobility,
                        Constants.RegisterToSimulator.addNetwork.AAI_GET_TENANTS,
                        Constants.RegisterToSimulator.addNetwork.AAI_NAMED_QUERY_FOR_VIEW_EDIT,
                        Constants.RegisterToSimulator.addNetwork.FILTER_SERVICE_INSTANCE_BY_ID,
                        //Constants.RegisterToSimulator.addNetwork.FILTER_SERVICE_INSTANCE_BY_NAME,
                        Constants.RegisterToSimulator.addNetwork.GET_SDC_CATALOG_SERVICES_NETWORK,

                }, ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
    }
    public static void msoAddNetwork(String instanceName){
        SimulatorApi.registerExpectation(
                new String [] {
                    Constants.RegisterToSimulator.addNetwork.MSO_ADD_NETWORK_ORCH_REQ,
                    Constants.RegisterToSimulator.addNetwork.MSO_ADD_NETWORK
                }, ImmutableMap.<String, Object>of("<SERVICE_INSTANCE_NAME>",instanceName), SimulatorApi.RegistrationStrategy.APPEND);
    }
    public static void msoAddNetworkError(String instanceName){
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.addNetwork.MSO_ADD_NETWORK_ERROR
                }, ImmutableMap.<String, Object>of("<SERVICE_INSTANCE_NAME>",instanceName), SimulatorApi.RegistrationStrategy.APPEND);
    }
    public static void activateServiceInstanceError(String action) {
        SimulatorApi.registerExpectation(
                new String []{
                        Constants.RegisterToSimulator.pProbe.GET_SERVICE_INSTANCE_WITH_LOGICAL_LINKS,
                        Constants.RegisterToSimulator.activateDeactivate.ACTIVATE_SERVICE_INSTANCE_ERROR
                } , ImmutableMap.<String, Object>of("<ACTIVE_ACTION>", action), SimulatorApi.RegistrationStrategy.APPEND);
    }


    public static void createPolicyConfiguration(boolean isSuccessFlow, String desiredCloudRegionId) {
        createConfiguration();
        SimulatorApi.registerExpectation(
                new String []{
                        Constants.RegisterToSimulator.createConfiguration.GET_PNF_INSTANCES,
                        Constants.RegisterToSimulator.createConfiguration.GET_MODEL_BY_ONE_INVARIANT_ID
                } , ImmutableMap.<String, Object>of("mdt1", desiredCloudRegionId), SimulatorApi.RegistrationStrategy.APPEND);
        if (isSuccessFlow) {
            msoCreatePProbeConfiguration();
        } else {
            msoCreatePProbeConfigurationError();
        }
    }

    public static void deletePolicyConfiguration(boolean isSuccessFlow, String desiredCloudRegionId) {
        createConfiguration();
        SimulatorApi.registerExpectation(
                new String []{
                        Constants.RegisterToSimulator.createConfiguration.GET_PNF_INSTANCES,
                        Constants.RegisterToSimulator.createConfiguration.GET_MODEL_BY_ONE_INVARIANT_ID,
                        Constants.RegisterToSimulator.createConfiguration.MSO_DELETE_CONFIGURATION,

                } , ImmutableMap.<String, Object>of("mdt1", desiredCloudRegionId), SimulatorApi.RegistrationStrategy.APPEND);
        if (isSuccessFlow) {
            msoCreatePProbeConfiguration();
        } else {
            msoCreatePProbeConfigurationError();
        }
    }




    private static void msoCreatePProbeConfiguration() {
        SimulatorApi.registerExpectation(
                new String []{
                        Constants.RegisterToSimulator.createConfiguration.MSO_CREATE_CONFIGURATION,
                        Constants.RegisterToSimulator.createConfiguration.MSO_CREATE_CONFIGURATION_ORCH_REQ
                } , ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    private static void msoCreatePProbeConfigurationError() {
        SimulatorApi.registerExpectation(
                new String []{
                        Constants.RegisterToSimulator.createConfiguration.MSO_CREATE_CONFIGURATION_ERROR
                } , ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void createConfiguration() {
        createConfiguration("model-version-id=2a2ea15f-07c6-4b89-bfca-e8aba39a34d6&model-invariant-id=a7eac2b3-8444-40ee-92e3-b3359b32445c");
    }

    public static void createConfiguration(String model) {
        SimulatorApi.registerExpectation(
                new String []{
                        Constants.RegisterToSimulator.createConfiguration.GET_VNF_INSTANCES,
                        Constants.RegisterToSimulator.createConfiguration.GET_MODEL_BY_2_INVARIANT_IDS
                } , ImmutableMap.of("model-version-id=2a2ea15f-07c6-4b89-bfca-e8aba39a34d6&model-invariant-id=a7eac2b3-8444-40ee-92e3-b3359b32445c", model), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void getNetworkNodeFormData() {
        SimulatorApi.registerExpectation(
                new String []{
                        Constants.RegisterToSimulator.createConfiguration.GET_TENANTS
                } , ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void createNewServiceInstance(String subscriber) {
        SimulatorApi.registerExpectation(
                new String []{
                        Constants.RegisterToSimulator.CreateNewServiceInstance.GET_FULL_SUBSCRIBES,
                        Constants.RegisterToSimulator.CreateNewServiceInstance.GET_SERVICES
                } , ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);

        switch (subscriber) {
            case "USP VOICE": createNewServiceInstanceUspVoice(); break;
            case "MSO_1610_ST": createNewServiceInstanceMso1610ST(); break;
        }
    }

    private static void createNewServiceInstanceMso1610ST() {
        SimulatorApi.registerExpectation(
                new String []{
                        Constants.RegisterToSimulator.CreateNewServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_MSO_1610_ST,
                        Constants.RegisterToSimulator.CreateNewServiceInstance.GET_MODELS_BY_SERVICE_TYPE_MSO_1610_ST
                } , ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    private static void createNewServiceInstanceUspVoice() {
        SimulatorApi.registerExpectation(
                new String []{
                        Constants.RegisterToSimulator.CreateNewServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_USP_VOICE,
                        Constants.RegisterToSimulator.CreateNewServiceInstance.GET_MODELS_BY_SERVICE_TYPE_USP_VOICE
                } , ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void deployNewServiceInstance(String instanceName) {
        SimulatorApi.registerExpectation(
                new String []{
                        Constants.RegisterToSimulator.CreateNewServiceInstance.deploy.SDC_GET_CATALOG,
                        Constants.RegisterToSimulator.CreateNewServiceInstance.deploy.GET_AIC_ZONES,
                        Constants.RegisterToSimulator.CreateNewServiceInstance.deploy.MSO_CREATE_SVC_INSTANCE,
                        Constants.RegisterToSimulator.CreateNewServiceInstance.deploy.MSO_CREATE_SVC_INSTANCE_ORCH_REQ
                } , ImmutableMap.<String, Object>of("<INSTANCE_NAME>", instanceName), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void searchExistingServiceInstanceWithoutModelVerId() {
        SimulatorApi.registerExpectation(
                new String []{
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_FIREWALL_MISC,
                        Constants.RegisterToSimulator.SearchForServiceInstance.FILTER_SERVICE_INSTANCE_BY_ID_NO_MODEL_VER_ID,
                       // Constants.RegisterToSimulator.SearchForServiceInstance.NAMED_QUERY_VIEW_EDIT_NO_MODEL_VER_ID
                } ,  ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void deleteExistingInstance(String orchStatus, String type) {
        genericSearchExistingServiceInstance();
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_USP_VOICE,
                        Constants.RegisterToSimulator.SearchForServiceInstance.NAMED_QUERY_VIEW_EDIT,
                        Constants.RegisterToSimulator.CreateNewServiceInstance.deploy.GET_AIC_ZONES
                }, ImmutableMap.<String, Object>of("<ORCH_STATUS>", orchStatus), SimulatorApi.RegistrationStrategy.APPEND);
        SimulatorApi.registerExpectationFromPresets(
                ImmutableList.of(
                        new PresetAAIGetTenants(),
                        new PresetMSODeleteInstanceOrchestrationRequestGet(type),
                        new PresetSDCGetServiceMetadataGet("7a6ee536-f052-46fa-aa7e-2fca9d674c44", "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0", "service-Complexservice-aLaCarte-csar.zip"),
                        new PresetSDCGetServiceToscaModelGet("7a6ee536-f052-46fa-aa7e-2fca9d674c44", "service-Complexservice-aLaCarte-csar.zip")),
                SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void deleteExistingVolumeGroupInstance(String orchStatus) {
        deleteExistingInstance(orchStatus, "Volume Group");
        SimulatorApi.registerExpectationFromPreset(new PresetMSODeleteVolumeGroup(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void deleteExistingVfModuleInstance(String orchStatus) {
        deleteExistingInstance(orchStatus, "VF Module");
        SimulatorApi.registerExpectationFromPreset(new PresetMSODeleteVfModule(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void deleteExistingVnfInstance(String orchStatus) {
        deleteExistingInstance(orchStatus, "Vnf");
        SimulatorApi.registerExpectationFromPreset(new PresetMSODeleteVnf(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void deleteExistingNetworkInstance(String orchStatus) {
        deleteExistingInstance(orchStatus, "Network");
        SimulatorApi.registerExpectationFromPreset(new PresetMSODeleteNetwork(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void deleteExistingServiceInstance(String orchStatus) {
        deleteExistingInstance(orchStatus, "Service");
        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                new PresetMSODeleteService(),
                new PresetAAIPostNamedQueryForViewEdit("3f93c7cb-2fd0-4557-9514-e189b7b04f9d", false)), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void resumeVfModule(String serviceInstanceId, String vnfInstanceId ){
        BulkRegistration.searchExistingServiceInstance();
        SimulatorApi.registerExpectationFromPresets(
                ImmutableList.of (
                        new PresetAAIGetTenants(),
                        new PresetMSOCreateVfModuleInstancePost(serviceInstanceId,vnfInstanceId),
                        new PresetMSOOrchestrationRequestGet("COMPLETE","c0011670-0e1a-4b74-945d-8bf5aede1d9c",Constants.ViewEdit.VF_MODULE_CREATED_SUCCESSFULLY_TEXT)),
                SimulatorApi.RegistrationStrategy.APPEND);

    }
}
