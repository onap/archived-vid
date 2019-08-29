package vid.automation.test.test;

import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIStandardQueryGet.defaultPlacement;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.SERVICE_NAME;
import static vid.automation.test.Constants.DrawingBoard.CONTEXT_MENU_BUTTON_HEADER;
import static vid.automation.test.Constants.DrawingBoard.CONTEXT_MENU_HEADER_RESUME_ITEM;
import static vid.automation.test.infra.ModelInfo.aLaCarteVnfGroupingService;
import static vid.automation.test.infra.ModelInfo.collectionResourceForResume;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPresets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.onap.sdc.ci.tests.utilities.GeneralUIUtils;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIFilterServiceInstanceById;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetServicesGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubDetailsGetSpecificService;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubDetailsWithoutInstancesGetSpecificService;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIModelsByInvariantIdGet;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIModelsByInvariantIdGetForServiceWithCR;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIStandardQueryGet;
import org.onap.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOAddOrRemoveOneInstanceGroupMember;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceGen2WithNamesAlacarteGroupingService;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeleteInstanceGroup;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestsGetByServiceInstanceId;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOResumeRequest;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceMetadataGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.Click;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.ModelInfo;
import vid.automation.test.sections.DrawingBoardPage;
import vid.automation.test.services.SimulatorApi;

public class ViewEditWithDrawingBoardTest extends VidBaseTestCase {

    private static final String MSO_COMPLETE_STATUS = "COMPLETE";
    private String vnfGroupInstanceId;
    private String serviceInstanceToDeleteName;
    private String vnf1Name;
    private String subscriberId = "e433710f-9217-458d-a79d-1c7aff376d89";
    private String serviceType = "TYLER SILVIA";
    private String serviceInstanceToResumeName;

    @FeatureTogglingTest(Features.FLAG_1902_VNF_GROUPING)
    @Test
    public void testDeleteVnfGroupWithMembers() {

        String serviceInstanceId = "b9af7c1d-a2d7-4370-b747-1b266849ad32";
        String serviceReqId = "3cf5ea96-6b34-4945-b5b1-4a7798b1caf2";

        //createPresetsForServiceInstanceTopologyExpectationOnAAIAndGetVnf1Name init serviceInstanceToDeleteName
        final List<BasePreset> presetsForGetTopology = createPresetsForServiceInstanceTopologyExpectationOnAAIAndGetVnf1Name(serviceInstanceId);
        final ImmutableMap<PresetMSOServiceInstanceGen2WithNames.Keys, String> names = ImmutableMap.of(SERVICE_NAME, serviceInstanceToDeleteName);
        SimulatorApi.registerExpectationFromPresetsCollections(ImmutableList.of(
                presetsForSearchAndEdit(aLaCarteVnfGroupingService, subscriberId, serviceType, serviceInstanceId),
                presetsForGetTopology,
                ImmutableList.of(
                        new PresetAAIModelsByInvariantIdGet(ImmutableList.of(aLaCarteVnfGroupingService.modelInvariantId)),
                        new PresetMSOCreateServiceInstanceGen2WithNamesAlacarteGroupingService(names, 0, serviceReqId)
                )
                ),
                CLEAR_THEN_SET);

        registerMsoPresetForRemoveInstanceGroupMember();
        searchEditAndWaitForCompletion(
                serviceInstanceId,
                serviceInstanceToDeleteName,
                () -> hoverAndClickMenuByName(vnf1Name, "daeb6568-cef8-417f-9075-ed259ce59f48-groupingservicefortest..ResourceInstanceGroup..0", Constants.InstantiationStatus.CONTEXT_MENU_DELETE)
        );
    }

    protected void searchEditAndWaitForCompletion(String serviceInstanceId, String serviceInstanceName, Runnable action) {
        GeneralUIUtils.ultimateWait();
        goToExistingInstanceById(serviceInstanceId);
        GeneralUIUtils.ultimateWait();

        DrawingBoardPage drawingBoardPage = new DrawingBoardPage();
        DrawingBoardPage.goToIframe();
        GeneralUIUtils.ultimateWait();
        action.run();
        GeneralUIUtils.ultimateWait();
        drawingBoardPage.screenshotDeployDialog(serviceInstanceId);
        drawingBoardPage.deploy();
        drawingBoardPage.verifyServiceCompletedOnTime(serviceInstanceName, "Service " + serviceInstanceName);
    }

    public void registerMsoPresetForRemoveInstanceGroupMember() {
        String vnfGroupRequestId = UUID.randomUUID().toString();
        String firstMemberRequestId = UUID.randomUUID().toString();
        String secondMemberRequestId = UUID.randomUUID().toString();
        String thirdMemberRequestId = UUID.randomUUID().toString();

        List <PresetMSODeleteInstanceGroup> vnfGroupPreset = ImmutableList.of(
        new PresetMSODeleteInstanceGroup(vnfGroupRequestId, vnfGroupInstanceId, getUserCredentials().getUserId()));

        List<PresetMSOAddOrRemoveOneInstanceGroupMember> instanceGroupMemberPreset = ImmutableList.of(
                new PresetMSOAddOrRemoveOneInstanceGroupMember(vnfGroupInstanceId, "963b67e1-079a-404e-abef-b745d770bd85", getUserCredentials().getUserId(), firstMemberRequestId, PresetMSOAddOrRemoveOneInstanceGroupMember.InstanceGroupMemberAction.Remove),
                new PresetMSOAddOrRemoveOneInstanceGroupMember(vnfGroupInstanceId, "a9f1b136-11ed-471f-8d77-f123c7501a01", getUserCredentials().getUserId(), secondMemberRequestId, PresetMSOAddOrRemoveOneInstanceGroupMember.InstanceGroupMemberAction.Remove),
                new PresetMSOAddOrRemoveOneInstanceGroupMember(vnfGroupInstanceId, "5a34a4f4-81a4-4eed-871b-f0b1187160d4", getUserCredentials().getUserId(), thirdMemberRequestId, PresetMSOAddOrRemoveOneInstanceGroupMember.InstanceGroupMemberAction.Remove)
        );

        List<PresetMSOOrchestrationRequestGet> inProgressPresets = new ArrayList<>();
        inProgressPresets.add(new PresetMSOOrchestrationRequestGet(MSO_COMPLETE_STATUS, vnfGroupRequestId));
        inProgressPresets.add(new PresetMSOOrchestrationRequestGet(MSO_COMPLETE_STATUS, firstMemberRequestId));
        inProgressPresets.add(new PresetMSOOrchestrationRequestGet(MSO_COMPLETE_STATUS, secondMemberRequestId));
        inProgressPresets.add(new PresetMSOOrchestrationRequestGet(MSO_COMPLETE_STATUS, thirdMemberRequestId));

        final ImmutableList.Builder<BasePreset> basePresetBuilder = new ImmutableList.Builder<>();
        basePresetBuilder
                .addAll(instanceGroupMemberPreset)
                .addAll(inProgressPresets)
                .addAll(vnfGroupPreset);
        List<BasePreset> presets = basePresetBuilder.build();
        registerExpectationFromPresets(presets, SimulatorApi.RegistrationStrategy.APPEND);
    }


    public List<BasePreset> createPresetsForServiceInstanceTopologyExpectationOnAAIAndGetVnf1Name(String serviceInstanceId) {

        final String RELATED_VNF_UUID1 = "a9f1b136-11ed-471f-8d77-f123c7501a01";
        final String RELATED_VNF_UUID2 = "963b67e1-079a-404e-abef-b745d770bd85";
        final String RELATED_VNF_UUID3 = "5a34a4f4-81a4-4eed-871b-f0b1187160d4";

        PresetAAIStandardQueryGet relatedVnf1 =
                PresetAAIStandardQueryGet.ofVnf(RELATED_VNF_UUID1, "7a6ee536-f052-46fa-aa7e-2fca9d674c44", "7a6ee536-f052-46fa-aa7e-2fca9d674c44",
                        "", ImmutableMultimap.of(), defaultPlacement());

        PresetAAIStandardQueryGet relatedVnf2 =
                PresetAAIStandardQueryGet.ofVnf(RELATED_VNF_UUID2, "d6557200-ecf2-4641-8094-5393ae3aae60","d6557200-ecf2-4641-8094-5393ae3aae60",
                        "", ImmutableMultimap.of(), defaultPlacement());

        PresetAAIStandardQueryGet relatedVnf3 =
                PresetAAIStandardQueryGet.ofVnf(RELATED_VNF_UUID3, "d6557200-ecf2-4641-8094-5393ae3aae60","d6557200-ecf2-4641-8094-5393ae3aae60",
                        "", ImmutableMultimap.of(), defaultPlacement());

        final PresetAAIStandardQueryGet vnfGroup1 =
                PresetAAIStandardQueryGet.ofInstanceGroup("vnfGroup-type", "Teresa Bradley",
                        ImmutableMultimap.<String, String>builder()
                                .putAll("generic-vnf", relatedVnf1.getReqPath(), relatedVnf2.getReqPath(), relatedVnf3.getReqPath())
                                .build()
                );

        final PresetAAIStandardQueryGet vnfGroup2 =
                PresetAAIStandardQueryGet.ofInstanceGroup("vnfGroup-type", "Stanley Mccarthy", ImmutableMultimap.of());

        final PresetAAIStandardQueryGet serviceInstance =
                PresetAAIStandardQueryGet.ofServiceInstance(serviceInstanceId, aLaCarteVnfGroupingService.modelVersionId, aLaCarteVnfGroupingService.modelInvariantId, "e433710f-9217-458d-a79d-1c7aff376d89", "TYLER SILVIA",
                        ImmutableMultimap.<String, String>builder()
                                .putAll("instance-group", vnfGroup1.getReqPath(), vnfGroup2.getReqPath())
                                .build()
                );

        serviceInstanceToDeleteName = serviceInstance.getInstanceName();
        vnfGroupInstanceId = vnfGroup1.getInstanceId();
        vnf1Name = vnfGroup1.getInstanceName();
        return ImmutableList.of(
                serviceInstance,
                vnfGroup1, vnfGroup2, relatedVnf1, relatedVnf2, relatedVnf3);
    }

    private List<BasePreset> presetsForSearchAndEdit(ModelInfo modelInfo, String subscriberId, String serviceType, String serviceInstanceId) {
        return ImmutableList.of(
                new PresetGetSessionSlotCheckIntervalGet(),
                new PresetAAIGetSubscribersGet(),
                new PresetSDCGetServiceMetadataGet(modelInfo),
                new PresetSDCGetServiceToscaModelGet(modelInfo),
                new PresetAAIGetServicesGet(),
                new PresetAAIFilterServiceInstanceById(subscriberId, serviceType, serviceInstanceId),
                new PresetAAIGetSubDetailsGetSpecificService(subscriberId, serviceType, "Assigned", modelInfo, serviceInstanceId),
                new PresetAAIGetSubDetailsWithoutInstancesGetSpecificService(subscriberId, serviceType)
        );
    }

    @FeatureTogglingTest({Features.FLAG_1908_MACRO_NOT_TRANSPORT_NEW_VIEW_EDIT, Features.FLAG_1908_RESUME_MACRO_SERVICE})
    @Test
    public void testResumeServiceInstanceWithCollectionResource() {

        String serviceInstanceId = "0d7b5429-da18-475b-8b67-1b8c0a596f68";
        String serviceReqId = "405652f4-ceb3-4a75-9474-8aea71480a77"; //from PresetMSOOrchestrationRequestsGetByServiceInstanceId

        //createPresetsForServiceInstanceTopologyExpectationOnAAIAndGetVnf1Name init serviceInstanceToDeleteName
        final List<BasePreset> presetsForGetTopology = createPresetsForGetTopologyOfServiceInstanceWithCRandInstanceGroup(serviceInstanceId);
        SimulatorApi.registerExpectationFromPresetsCollections(ImmutableList.of(
                presetsForSearchAndEdit(collectionResourceForResume, subscriberId, serviceType, serviceInstanceId),
                presetsForGetTopology,
                createPresetsForResume(serviceInstanceId, serviceReqId)
                ),
                CLEAR_THEN_SET);

        searchEditAndWaitForCompletion(
                serviceInstanceId,
                serviceInstanceToResumeName, //side effect of createPresetsForGetTopologyOfServiceInstanceWithCRandInstanceGroup
                () -> {
                    Click.byTestId(CONTEXT_MENU_BUTTON_HEADER);
                    Click.byTestId(CONTEXT_MENU_HEADER_RESUME_ITEM);
                });
    }

    private List<BasePreset> createPresetsForResume(String serviceInstanceId, String serviceReqId) {
        String resumeRequestId = UUID.randomUUID().toString();
        return ImmutableList.of(
                new PresetMSOOrchestrationRequestsGetByServiceInstanceId(serviceInstanceId),
                new PresetMSOResumeRequest(serviceReqId, resumeRequestId, serviceInstanceId, "us16807000"),
                new PresetMSOOrchestrationRequestGet(MSO_COMPLETE_STATUS, resumeRequestId)
        );
    }

    private List<BasePreset> createPresetsForGetTopologyOfServiceInstanceWithCRandInstanceGroup(String serviceInstanceId) {
        PresetAAIStandardQueryGet instanceGroup = PresetAAIStandardQueryGet.ofInstanceGroup(
                "L3-NETWORK", "SUB_INTERFACE", ImmutableMultimap.of(),
                "868b109c-9481-4a18-891b-af974db7705a", "dd182d7d-6949-4b90-b3cc-5befe400742e");

        PresetAAIStandardQueryGet cr = PresetAAIStandardQueryGet.ofCollectionResource(
                "Assigned",
                ImmutableMultimap.of("instance-group", instanceGroup.getReqPath()),
                "081ceb56-eb71-4566-a72d-3e7cbee5cdf1",
                "ce8c98bc-4691-44fb-8ff0-7a47487c11c4"
        );

        PresetAAIStandardQueryGet service = PresetAAIStandardQueryGet.ofServiceInstance(
                serviceInstanceId,
                "INFRASTRUCTURE", "", collectionResourceForResume.modelVersionId,
                collectionResourceForResume.modelInvariantId,
                subscriberId,
                serviceType,
                "Assigned",
                ImmutableMultimap.of("collection", cr.getReqPath())
        );

        PresetAAIModelsByInvariantIdGetForServiceWithCR aaiModelsPreset = new PresetAAIModelsByInvariantIdGetForServiceWithCR(
                ImmutableList.of(
                        "868b109c-9481-4a18-891b-af974db7705a",
                        "081ceb56-eb71-4566-a72d-3e7cbee5cdf1",
                        collectionResourceForResume.modelInvariantId));

        serviceInstanceToResumeName = service.getInstanceName();

        return ImmutableList.of(service, cr, instanceGroup, aaiModelsPreset);
    }

}
