package vid.automation.test.services;

import static org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset.DEFAULT_CLOUD_OWNER;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet.COMPLETE;
import static vid.automation.test.infra.ModelInfo.serviceFabricSriovService;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAICloudRegionAndSourceFromConfigurationPut;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIFilterServiceInstanceById;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetHomingForVfModule;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetModelsByOwningEntity;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetModelsByProject;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetNetworkZones;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetPortMirroringSourcePorts;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubDetailsGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubDetailsWithoutInstancesGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetTenants;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIPostNamedQueryForViewEdit;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOActivateFabricConfiguration;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseCreateInstancePost;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateVfModule;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeactivateAndCloudDelete;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeleteInstanceOrchestrationRequestGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeleteNetwork;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeleteService;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeleteVfModule;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeleteVnf;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeleteVolumeGroup;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.onap.simulator.presetGenerator.presets.mso.configuration.PresetMSOActOnConfiguration;
import org.onap.simulator.presetGenerator.presets.mso.configuration.PresetMSOCreateConfiguration;
import org.onap.simulator.presetGenerator.presets.mso.configuration.PresetMSODeleteConfiguration;
import org.onap.simulator.presetGenerator.presets.mso.configuration.PresetMsoEnableDisablePort;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceMetadataGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import vid.automation.test.Constants;
import vid.automation.test.Constants.ViewEdit;

public class BulkRegistration {

    public static void searchExistingServiceInstance() {
        searchExistingServiceInstance("Active");
    }

    public static void searchExistingServiceInstance(String orchStatus)  {
        searchExistingServiceInstance(orchStatus, "pending-delete");
    }

    public static void searchExistingServiceInstance(String orchStatus, String vfModuleOrchStatus) {
        genericSearchExistingServiceInstance();
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_SILVIA_ROBBINS,
                        Constants.RegisterToSimulator.CreateNewServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_CAR_2020_ER,
                        Constants.RegisterToSimulator.SearchForServiceInstance.FILTER_SERVICE_INSTANCE_BY_ID,
                        Constants.RegisterToSimulator.SearchForServiceInstance.NAMED_QUERY_VIEW_EDIT,
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SDC_CATALOG_SERVICE_VID_TEST_444,
                        Constants.RegisterToSimulator.CreateNewServiceInstance.deploy.GET_AIC_ZONES
                }, ImmutableMap.<String, Object>of("<ORCH_STATUS>", orchStatus, "<VF_MODULE_ORCH_STATUS>", vfModuleOrchStatus), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void searchExistingServiceInstanceWithFabric(String orchStatus) {
        genericSearchExistingServiceInstance();
        SimulatorApi.registerExpectationFromPresets(
                ImmutableList.of(
                        new PresetAAIFilterServiceInstanceById("e433710f-9217-458d-a79d-1c7aff376d89",
                                 "TYLER SILVIA",
                                "c187e9fe-40c3-4862-b73e-84ff056205f61234"),
                        new PresetAAIGetSubDetailsGet("e433710f-9217-458d-a79d-1c7aff376d89", orchStatus),
                        new PresetAAIGetSubDetailsWithoutInstancesGet("e433710f-9217-458d-a79d-1c7aff376d89", true),
                        new PresetAAIPostNamedQueryForViewEdit("c187e9fe-40c3-4862-b73e-84ff056205f61234", false, true),
                        new PresetSDCGetServiceMetadataGet(serviceFabricSriovService),
                        new PresetSDCGetServiceToscaModelGet(serviceFabricSriovService),
                        new PresetAAIGetNetworkZones(),
                        new PresetMSOActivateFabricConfiguration("c187e9fe-40c3-4862-b73e-84ff056205f61234"),
                        new PresetMSOOrchestrationRequestGet(COMPLETE, "318cc766-b673-4a50-b9c5-471f68914584", "Success", false)),
                SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void searchExistingServiceInstanceByOEAndProject(){
        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                new PresetAAIGetModelsByOwningEntity("Melissa"),
                new PresetAAIGetModelsByProject("x1"),
                new PresetAAIGetModelsByProject("yyy1")
                ), APPEND);
    }

    public static void searchExistingCRServiceInstance(String orchStatus) {
        genericSearchExistingServiceInstance();
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_SILVIA_ROBBINS_CR,
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
                        Constants.RegisterToSimulator.AddSubinterface.GET_SUBSCRIBERS_FOR_CUSTOMER_SILVIA_ROBBINS_VFC_IG,
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
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SERVICES
                }, ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetSubscribersGet(),SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void searchExistingServiceInstance2(String orchStatus) {
        genericSearchExistingServiceInstance();
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_CRAIG_ROBERTS,
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
        final String configurationId2 = "9533-config-LB1114";
        final String portInterfaceId = "d35bf534-7d8e-4cb4-87f9-0a8bb6cd47b2";
        final String modelToReplaceWith ="pm1111_equip_model_rename.zip";

        SimulatorApi.registerExpectationFromPreset(new PresetAAICloudRegionAndSourceFromConfigurationPut(configurationId, desiredCloudRegionId), APPEND);
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetPortMirroringSourcePorts(configurationId, portInterfaceId, ViewEdit.COMMON_PORT_MIRRORING_PORT_NAME, isMirrored), APPEND);
        SimulatorApi.registerExpectationFromPreset(new PresetAAICloudRegionAndSourceFromConfigurationPut(configurationId2, desiredCloudRegionId), APPEND);
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetPortMirroringSourcePorts(configurationId2, portInterfaceId, "i'm not your port", isMirrored), APPEND);
        SimulatorApi.registerExpectationFromPreset(PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MDT1_TO_ATT_NC, APPEND);
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_SILVIA_ROBBINS,
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


    public static void activateDeactivateConfiguration(String action) {
        appendWithGetStatus(new PresetMSOActOnConfiguration(action), PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MDT1_TO_ATT_NC);
    }

    public static void deleteConfiguration() {
        appendWithGetStatus(new PresetMSODeleteConfiguration(), PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MDT1_TO_ATT_NC);
    }

    public static void enableDisablePort(String action){
        appendWithGetStatus(new PresetMsoEnableDisablePort(
                "c187e9fe-40c3-4862-b73e-84ff056205f6",
                "9533-config-LB1113", action), PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MDT1_TO_ATT_NC);
    }

    public static void addNetwork() {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_Emanuel,
                        Constants.RegisterToSimulator.addNetwork.AAI_GET_TENANTS,
                        Constants.RegisterToSimulator.addNetwork.AAI_NAMED_QUERY_FOR_VIEW_EDIT,
                        Constants.RegisterToSimulator.addNetwork.FILTER_SERVICE_INSTANCE_BY_ID,
                        //Constants.RegisterToSimulator.addNetwork.FILTER_SERVICE_INSTANCE_BY_NAME,
                        Constants.RegisterToSimulator.addNetwork.GET_SDC_CATALOG_SERVICES_NETWORK,

                }, ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
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


    public static void createPolicyConfiguration(boolean isSuccessFlow, String serviceType) {
        createConfiguration();

        SimulatorApi.registerExpectation(SimulatorApi.RegistrationStrategy.APPEND,
                Constants.RegisterToSimulator.createConfiguration.GET_MODEL_BY_ONE_INVARIANT_ID);

        SimulatorApi.registerExpectation(Constants.RegisterToSimulator.createConfiguration.GET_PNF_INSTANCES,
                ImmutableMap.of("<SERVICE-TYPE>", serviceType), SimulatorApi.RegistrationStrategy.APPEND);

        if (isSuccessFlow) {
            msoCreatePProbeConfiguration();
        } else {
            msoCreatePProbeConfigurationError();
        }
    }

    public static void deletePolicyConfiguration(boolean isSuccessFlow) {
        createConfiguration();
        SimulatorApi.registerExpectation(SimulatorApi.RegistrationStrategy.APPEND,
                Constants.RegisterToSimulator.createConfiguration.GET_MODEL_BY_ONE_INVARIANT_ID);
        appendWithGetStatus(new PresetMSODeleteConfiguration(), PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MDT1_TO_ATT_NC);
    }




    private static void msoCreatePProbeConfiguration() {
        appendWithGetStatus(new PresetMSOCreateConfiguration("c187e9fe-40c3-4862-b73e-84ff056205f6"), PresetAAIGetCloudOwnersByCloudRegionId.PRESET_AAIAIC25_TO_ATT_AIC);
    }

    private static void appendWithGetStatus(PresetMSOBaseCreateInstancePost createInstancePreset, PresetAAIGetCloudOwnersByCloudRegionId cloudOwnerPreset) {
        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                createInstancePreset,
                cloudOwnerPreset,
                new PresetMSOOrchestrationRequestGet(COMPLETE, createInstancePreset.getRequestId(), "Success", false)),
                SimulatorApi.RegistrationStrategy.APPEND);
    }

    private static void msoCreatePProbeConfigurationError() {
        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                new PresetMSOCreateConfiguration("c187e9fe-40c3-4862-b73e-84ff056205f6", 500, null),
                PresetAAIGetCloudOwnersByCloudRegionId.PRESET_AAIAIC25_TO_ATT_AIC),
                SimulatorApi.RegistrationStrategy.APPEND);
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
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetSubscribersGet(),SimulatorApi.RegistrationStrategy.APPEND);
        SimulatorApi.registerExpectation(Constants.RegisterToSimulator.CreateNewServiceInstance.GET_SERVICES, SimulatorApi.RegistrationStrategy.APPEND);

        switch (subscriber) {
            case "SILVIA ROBBINS": createNewServiceInstanceUspVoice(); break;
            case "CAR_2020_ER": createNewServiceInstanceMso1610ST(); break;
        }
    }

    private static void createNewServiceInstanceMso1610ST() {
        SimulatorApi.registerExpectation(
                new String []{
                        Constants.RegisterToSimulator.CreateNewServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_CAR_2020_ER,
                        Constants.RegisterToSimulator.CreateNewServiceInstance.GET_MODELS_BY_SERVICE_TYPE_CAR_2020_ER
                } , ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    private static void createNewServiceInstanceUspVoice() {
        SimulatorApi.registerExpectation(
                new String []{
                        Constants.RegisterToSimulator.CreateNewServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_SILVIA_ROBBINS,
                        Constants.RegisterToSimulator.CreateNewServiceInstance.GET_MODELS_BY_SERVICE_TYPE_SILVIA_ROBBINS
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
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_CRAIG_ROBERTS,
                        Constants.RegisterToSimulator.SearchForServiceInstance.FILTER_SERVICE_INSTANCE_BY_ID_NO_MODEL_VER_ID,
                       // Constants.RegisterToSimulator.SearchForServiceInstance.NAMED_QUERY_VIEW_EDIT_NO_MODEL_VER_ID
                } ,  ImmutableMap.<String, Object>of(), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void deleteExistingInstance(String orchStatus, String type)  {
        deleteExistingInstance(orchStatus, type, "pending-delete");
    }

    public static void deleteExistingInstance(String orchStatus, String type, String vfModuleOrchStatus) {
        genericSearchExistingServiceInstance();
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_SILVIA_ROBBINS,
                        Constants.RegisterToSimulator.CreateNewServiceInstance.deploy.GET_AIC_ZONES
                }, ImmutableMap.<String, Object>of("<ORCH_STATUS>", orchStatus, "<VF_MODULE_ORCH_STATUS>", vfModuleOrchStatus), SimulatorApi.RegistrationStrategy.APPEND);

        //for delete service instance we will use other preset , so the service would be empty
        if (!StringUtils.equals(type, "Service")) {
            SimulatorApi.registerExpectation(
                new String [] {
                    Constants.RegisterToSimulator.SearchForServiceInstance.NAMED_QUERY_VIEW_EDIT,
                }, ImmutableMap.<String, Object>of("<ORCH_STATUS>", orchStatus, "<VF_MODULE_ORCH_STATUS>", vfModuleOrchStatus), SimulatorApi.RegistrationStrategy.APPEND);
        }

        SimulatorApi.registerExpectationFromPresets(
                ImmutableList.of(
                        new PresetAAIGetTenants(),
                        new PresetMSODeleteInstanceOrchestrationRequestGet(type),
                        new PresetSDCGetServiceMetadataGet("7a6ee536-f052-46fa-aa7e-2fca9d674c44", "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0", "service-Complexservice-aLaCarte-csar.zip"),
                        new PresetSDCGetServiceToscaModelGet("7a6ee536-f052-46fa-aa7e-2fca9d674c44", "service-Complexservice-aLaCarte-csar.zip"),
                        new PresetMSODeactivateAndCloudDelete("3f93c7cb-2fd0-4557-9514-e189b7b04f9d", "c015cc0f-0f37-4488-aabf-53795fd93cd3",
                                "a231a99c-7e75-4d6d-a0fb-5c7d26f30f77", "c0011670-0e1a-4b74-945d-8bf5aede1d9c", "irma-aic"),
                        PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MTN6_TO_ATT_AIC
                ),
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
                new PresetAAIPostNamedQueryForViewEdit("3f93c7cb-2fd0-4557-9514-e189b7b04f9d", false, false)), SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void resumeWithHomingDataVfModule(String serviceOrchStatus, String vfModuleOrchStatus, String vfModuleName)  {
        SimulatorApi.registerExpectation(Constants.RegisterToSimulator.SearchForServiceInstance.NAMED_QUERY_VIEW_EDIT,
                ImmutableMap.<String, Object>of("<ORCH_STATUS>", serviceOrchStatus, "<VF_MODULE_ORCH_STATUS>", vfModuleOrchStatus),
                SimulatorApi.RegistrationStrategy.APPEND);

        SimulatorApi.registerExpectationFromPresets(
                ImmutableList.of (
                        new PresetAAIGetHomingForVfModule("c015cc0f-0f37-4488-aabf-53795fd93cd3", "a231a99c-7e75-4d6d-a0fb-5c7d26f30f77", "092eb9e8e4b7412e8787dd091bc58e86", "hvf6"),
                        new PresetMSOCreateVfModule("3f93c7cb-2fd0-4557-9514-e189b7b04f9d",
                                "c015cc0f-0f37-4488-aabf-53795fd93cd3", DEFAULT_CLOUD_OWNER, vfModuleName,
                                "7a6ee536-f052-46fa-aa7e-2fca9d674c44", "e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0", "ComplexService"),
                        new PresetMSOOrchestrationRequestGet(
                                COMPLETE,
                                "c0011670-0e1a-4b74-945d-8bf5aede1d9c",
                                Constants.ViewEdit.VF_MODULE_CREATED_SUCCESSFULLY_TEXT,
                                false)),
                SimulatorApi.RegistrationStrategy.APPEND);
    }

    public static void resumeVfModule(String serviceInstanceId, String vnfInstanceId ){
        BulkRegistration.searchExistingServiceInstance();
        SimulatorApi.registerExpectationFromPresets(
                ImmutableList.of (
                        new PresetAAIGetTenants(),
                        new PresetMSOCreateVfModule(serviceInstanceId,vnfInstanceId, DEFAULT_CLOUD_OWNER),
                        new PresetMSOOrchestrationRequestGet(
                                COMPLETE,
                                "c0011670-0e1a-4b74-945d-8bf5aede1d9c",
                                Constants.ViewEdit.VF_MODULE_CREATED_SUCCESSFULLY_TEXT,
                                false)),
                SimulatorApi.RegistrationStrategy.APPEND);

    }
}
