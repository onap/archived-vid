package vid.automation.test.test;

import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.SERVICE_NAME;
import static org.testng.AssertJUnit.assertTrue;
import static vid.automation.test.infra.ModelInfo.aLaCarteVnfGroupingService;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPresets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import java.util.ArrayList;
import java.util.Collections;
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
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIStandardQueryGet;
import org.onap.simulator.presetGenerator.presets.ecompportal_att.PresetGetSessionSlotCheckIntervalGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOAddOrRemoveOneInstanceGroupMember;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceGen2WithNamesAlacarteGroupingService;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeleteInstanceGroup;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceMetadataGet;
import org.onap.simulator.presetGenerator.presets.sdc.PresetSDCGetServiceToscaModelGet;
import org.testng.annotations.Test;
import vid.automation.test.Constants;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Features;
import vid.automation.test.infra.Wait;
import vid.automation.test.sections.DrawingBoardPage;
import vid.automation.test.sections.VidBasePage;
import vid.automation.test.services.SimulatorApi;

public class ViewEditWithDrawingBoardTest extends VidBaseTestCase {
    private final String RELATED_VNF_UUID1 = "a9f1b136-11ed-471f-8d77-f123c7501a01";
    private final String RELATED_VNF_UUID2 = "963b67e1-079a-404e-abef-b745d770bd85";
    private final String RELATED_VNF_UUID3 = "5a34a4f4-81a4-4eed-871b-f0b1187160d4";
    private static final String MSO_COMPLETE_STATUS = "COMPLETE";
    private static final String COMPLETED = "COMPLETED";
    private String vnfGroupInstanceId;
    private String vnfGroupInstanceName;
    private String serviceInstanceName;
    private String serviceInstanceId = "b9af7c1d-a2d7-4370-b747-1b266849ad32";
    String subscriberId = "e433710f-9217-458d-a79d-1c7aff376d89";
    String serviceType = "TYLER SILVIA";
    String serviceReqId = "3cf5ea96-6b34-4945-b5b1-4a7798b1caf2";

    @FeatureTogglingTest(Features.FLAG_1902_VNF_GROUPING)
    @Test
    public void testDeleteVnfGroupWithMembers() {

        String vnf1Name = registerServiceInstanceTopologyExpectationOnAAIAndGetVnf1Name();
        final ImmutableMap<PresetMSOServiceInstanceGen2WithNames.Keys, String> names = ImmutableMap.of(SERVICE_NAME, serviceInstanceName);


        List<BasePreset> presets = ImmutableList.of(
                new PresetAAIGetSubscribersGet(),
                new PresetAAIGetServicesGet(),
                new PresetAAIFilterServiceInstanceById(subscriberId, serviceType, serviceInstanceId),
                new PresetAAIGetSubDetailsGetSpecificService(subscriberId, serviceType, "Active", aLaCarteVnfGroupingService, serviceInstanceId),
                new PresetAAIGetSubDetailsWithoutInstancesGetSpecificService(subscriberId, serviceType),
                new PresetSDCGetServiceMetadataGet(aLaCarteVnfGroupingService),
                new PresetSDCGetServiceToscaModelGet(aLaCarteVnfGroupingService),
                new PresetMSOCreateServiceInstanceGen2WithNamesAlacarteGroupingService(names, 0, serviceReqId)
        );

        SimulatorApi.registerExpectationFromPresets(presets, APPEND);
        registerMsoPreset();
        GeneralUIUtils.ultimateWait();
        goToExistingInstanceById(serviceInstanceId);
        GeneralUIUtils.ultimateWait();

        DrawingBoardPage drawingBoardPage = new DrawingBoardPage();
        drawingBoardPage.goToIframe();
        GeneralUIUtils.ultimateWait();
        hoverAndClickMenuByName(vnf1Name, "daeb6568-cef8-417f-9075-ed259ce59f48-groupingservicefortest..ResourceInstanceGroup..0", Constants.InstantiationStatus.CONTEXT_MENU_DELETE);
        GeneralUIUtils.ultimateWait();
        drawingBoardPage.clickDeployButton();


        VidBasePage.goOutFromIframe();
        GeneralUIUtils.ultimateWait();
        drawingBoardPage.goToIframe();
        GeneralUIUtils.ultimateWait();

        DrawingBoardPage.ServiceStatusChecker serviceStatusChecker = new DrawingBoardPage.ServiceStatusChecker(serviceInstanceName, Collections.singleton(COMPLETED));
        boolean statusIsShown = Wait.waitFor(serviceStatusChecker, null, 10, 1);
        assertTrue("Service "+serviceInstanceName+" wasn't completed after in time", statusIsShown);

    }

    public void registerMsoPreset(){
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


    public String registerServiceInstanceTopologyExpectationOnAAIAndGetVnf1Name() {
        PresetAAIStandardQueryGet relatedVnf1 =
                PresetAAIStandardQueryGet.ofVnf(RELATED_VNF_UUID1, "7a6ee536-f052-46fa-aa7e-2fca9d674c44", "7a6ee536-f052-46fa-aa7e-2fca9d674c44",
                        "", ImmutableMultimap.of());

        PresetAAIStandardQueryGet relatedVnf2 =
                PresetAAIStandardQueryGet.ofVnf(RELATED_VNF_UUID2, "d6557200-ecf2-4641-8094-5393ae3aae60","d6557200-ecf2-4641-8094-5393ae3aae60",
                        "", ImmutableMultimap.of());

        PresetAAIStandardQueryGet relatedVnf3 =
                PresetAAIStandardQueryGet.ofVnf(RELATED_VNF_UUID3, "d6557200-ecf2-4641-8094-5393ae3aae60","d6557200-ecf2-4641-8094-5393ae3aae60",
                        "", ImmutableMultimap.of());

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
        SimulatorApi.registerExpectationFromPresets(ImmutableList.of(
                serviceInstance,
                vnfGroup1,vnfGroup2, relatedVnf1, relatedVnf2, relatedVnf3,
                new PresetAAIModelsByInvariantIdGet(ImmutableList.of(aLaCarteVnfGroupingService.modelInvariantId)),
                new PresetGetSessionSlotCheckIntervalGet(),
                new PresetAAIGetSubscribersGet(),
                new PresetSDCGetServiceMetadataGet(aLaCarteVnfGroupingService),
                new PresetSDCGetServiceToscaModelGet(aLaCarteVnfGroupingService)
        ), CLEAR_THEN_SET);

        serviceInstanceName = serviceInstance.getInstanceName();
        vnfGroupInstanceName = vnfGroup1.getInstanceName();
        vnfGroupInstanceId = vnfGroup1.getInstanceId();
        return vnfGroupInstanceName;
    }


}
