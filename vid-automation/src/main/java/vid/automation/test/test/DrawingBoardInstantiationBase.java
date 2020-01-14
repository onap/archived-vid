package vid.automation.test.test;

import static org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MTN6_TO_ATT_AIC;
import static vid.automation.test.infra.ModelInfo.macroSriovWithDynamicFieldsEcompNamingFalsePartialModelDetailsVnfEcompNamingFalse;
import static vid.automation.test.infra.ModelInfo.pasqualeVmxVpeBvService488Annotations;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPresets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseCreateInstancePost;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOCreateServiceInstanceGen2WithNamesEcompNamingFalse;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys;
import vid.automation.test.infra.ModelInfo;
import vid.automation.test.sections.ViewEditPage;
import vid.automation.test.services.SimulatorApi;

public class DrawingBoardInstantiationBase extends VidBaseTestCase {

    protected ViewEditPage viewEditPage= new ViewEditPage();

    protected ImmutableMap<Keys, String> generateNamesForEcompNamingFalsePreset(String vnfInstanceName, String serviceInstanceName, String vnfInstanceName2, String vnfName2) {
        final String vfModuleName1 = "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0";
        final String vfModuleName2 = "2017488PasqualeVpe..PASQUALE_vRE_BV..module-1";
        return ImmutableMap.<Keys, String>builder()
            .put(Keys.SERVICE_NAME, serviceInstanceName)
            .put(Keys.VNF_NAME, cleanSeparators("2017-488_PASQUALE-vPE", vnfInstanceName))
            .put(Keys.VFM_NAME1, cleanSeparators(vfModuleName1 , "VF instance name ZERO"))
            .put(Keys.VFM_NAME2, cleanSeparators(vfModuleName2 , "VF instance name"))
            .put(Keys.VG_NAME, cleanSeparators(vfModuleName2 , "VF instance name") + "_vol_abc")
            .put(Keys.VNF_NAME2, cleanSeparators(vnfName2, vnfInstanceName2))
            .build();
    }

    @NotNull
    protected String registerPresetsForEcompNamingFalseFirstService(ImmutableMap<Keys, String> vars) {
        final String request1 = PresetMSOBaseCreateInstancePost.DEFAULT_REQUEST_ID;
        prepareServicePreset(macroSriovWithDynamicFieldsEcompNamingFalsePartialModelDetailsVnfEcompNamingFalse,
            true);
        registerExpectationFromPresets(ImmutableList.of(
                // although "some legacy region" is provided for vnf, Service's region "hvf6" overrides it
                PRESET_MTN6_TO_ATT_AIC,
                new PresetMSOCreateServiceInstanceGen2WithNamesEcompNamingFalse(vars, 0, request1)
        ), SimulatorApi.RegistrationStrategy.APPEND);
        return request1;
    }

    protected String cleanSeparators(String... s) {
        return String.join("", s).replace(" ", "");
    }

    //@Step("prepare service preset")
    protected void prepareServicePreset(ModelInfo modelInfo, boolean deploy) {
        String subscriberId = "e433710f-9217-458d-a79d-1c7aff376d89";

        if (deploy) {
            registerExpectationForServiceDeployment(
                ImmutableList.of(
                    modelInfo,
                    pasqualeVmxVpeBvService488Annotations
                ),
                subscriberId, null);
        } else {
            registerExpectationForServiceBrowseAndDesign(ImmutableList.of(modelInfo), subscriberId);
        }
    }
}
