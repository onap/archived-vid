package org.onap.simulator.presetGenerator.presets.mso;

import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.SERVICE_NAME;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.VFM_NAME1;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.VFM_NAME2;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.VG_NAME;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.VNF_NAME;

import java.util.Map;
import vid.automation.test.infra.Features;

public abstract class PresetMSOServiceInstanceGen2WithNames extends PresetMSOBaseCreateInstancePost {

    public enum Keys {
        SERVICE_NAME, VNF_NAME, VNF_NAME2, VFM_NAME1, VFM_NAME2, VG_NAME
        , VNF_GROUP1_ACTION, RELATED_VNF1_ACTION, RELATED_VNF2_ACTION,
    }

    protected final Map<Keys, String> names;

    protected final String suffix;

    public PresetMSOServiceInstanceGen2WithNames(Map<Keys, String> names, int suffix) {
        this(names, suffix, PresetMSOBaseCreateInstancePost.DEFAULT_REQUEST_ID);
    }

    public PresetMSOServiceInstanceGen2WithNames(Map<Keys, String> names, int suffix, String requestId) {
        super(requestId);
        this.names = names;
        this.suffix = formatSuffix(suffix);
    }

    public PresetMSOServiceInstanceGen2WithNames(Map<Keys, String> names, int suffix, String requestId, String responseInstanceId) {
        super(requestId, responseInstanceId);
        this.names = names;
        this.suffix = formatSuffix(suffix);
    }

    @Override
    public boolean isStrictMatch() {
        return true;
    }

    protected String getVnfInstanceParams() {
        if (!Features.FLAG_SHIFT_VFMODULE_PARAMS_TO_VNF.isActive()) {
            return "";
        }
        return " {" +
                "  \"vmx_int_net_len\": \"24\"," +
                "  \"vre_a_volume_size_0\": \"100\"," +
                "  \"availability_zone_0\": \"mtpocdv-kvm-az01\"" +
                "}";
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "  \"requestDetails\": {" +
                "    \"modelInfo\": {" +
                "      \"modelInvariantId\": \"300adb1e-9b0c-4d52-bfb5-fa5393c4eabb\"," +
                "      \"modelVersionId\": \"5c9e863f-2716-467b-8799-4a67f378dcaa\"," +
                "      \"modelName\": \"AIM_TRANSPORT_00004\"," +
                "      \"modelType\": \"service\"," +
                "      \"modelVersion\": \"1.0\"" +
                "    }," +
                "    \"owningEntity\": {" +
                "      \"owningEntityId\": \"someID\"," +
                "      \"owningEntityName\": \"someName\"" +
                "    }," +
                "    \"subscriberInfo\": {" +
                "      \"globalSubscriberId\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"" +
                "    }," +
                "    \"project\": {" +
                "      \"projectName\": \"myProject\"" +
                "    }," +
                "    \"requestInfo\": {" +
                "      \"instanceName\": \""+ names.get(SERVICE_NAME) + suffix + "\"," +
                "      \"productFamilyId\": \"myProductFamilyId\"," +
                "      \"source\": \"VID\"," +
                "      \"suppressRollback\": false," +
                "      \"requestorId\": \"us16807000\"" +
                "    }," +
                "    \"requestParameters\": {" +
                "      \"subscriptionServiceType\": \"mySubType\"," +
                "      \"aLaCarte\": false," +
                "      \"userParams\": [" +
                "        {" +
                "          \"service\": {" +
                "            \"modelInfo\": {" +
                "              \"modelVersionId\": \"5c9e863f-2716-467b-8799-4a67f378dcaa\"," +
                "              \"modelName\": \"AIM_TRANSPORT_00004\"," +
                "              \"modelType\": \"service\"" +
                "            }," +
                "            \"instanceName\": \""+ names.get(SERVICE_NAME) + suffix + "\"," +
                "            \"instanceParams\": []," +
                "            \"resources\": {" +
                "              \"vnfs\": [" +
                "                {" +
                "                  \"modelInfo\": {" +
                "                    \"modelCustomizationName\": \"2016-73_MOW-AVPN-vPE-BV-L 0\"," +
                "                    \"modelCustomizationId\": \"ab153b6e-c364-44c0-bef6-1f2982117f04\"," +
                "                    \"modelVersionId\": \"7f40c192-f63c-463e-ba94-286933b895f8\"," +
                "                    \"modelName\": \"2016-73_MOW-AVPN-vPE-BV-L\"," +
                "                    \"modelType\": \"vnf\"" +
                "                  }," +
                "                  \"cloudConfiguration\": {" +
                "                    \"lcpCloudRegionId\": \"hvf3\"," +
                                    addCloudOwnerIfNeeded() +
                "                    \"tenantId\": \"greatTenant\"" +
                "                  }," +
                "                  \"platform\": {" +
                "                    \"platformName\": \"platformName\"" +
                "                  }," +
                "                  \"lineOfBusiness\": {" +
                "                    \"lineOfBusinessName\": \"lineOfBusinessName\"" +
                "                  }," +
                "                  \"productFamilyId\": \"myProductFamilyId\"," +
                "                  \"instanceParams\": ["+getVnfInstanceParams()+"]," +
                "                  \"vfModules\": [" +
                "                    {" +
                "                      \"modelInfo\": {" +
                "                        \"modelCustomizationId\": \"a25e8e8c-58b8-4eec-810c-97dcc1f5cb7f\"," +
                "                        \"modelVersionId\": \"4c75f813-fa91-45a4-89d0-790ff5f1ae79\"," +
                "                        \"modelName\": \"201673MowAvpnVpeBvL..AVPN_base_vPE_BV..module-0\"," +
                "                        \"modelType\": \"vfModule\"" +
                "                      }," +
                "                      \"instanceName\": \""+ names.get(VFM_NAME1)  + suffix +"\"," +
                "                      \"instanceParams\": [" +
                "                        {" +
                "                          \"vmx_int_net_len\": \"24\"" +
                "                        }" +
                "                      ]" +
                "                    }," +
                "                    {" +
                "                      \"modelInfo\": {" +
                "                        \"modelCustomizationId\": \"72d9d1cd-f46d-447a-abdb-451d6fb05fa8\"," +
                "                        \"modelVersionId\": \"56e2b103-637c-4d1a-adc8-3a7f4a6c3240\"," +
                "                        \"modelName\": \"201673MowAvpnVpeBvL..AVPN_vRE_BV..module-1\"," +
                "                        \"modelType\": \"vfModule\"" +
                "                      }," +
                "                      \"instanceName\": \"" + names.get(VFM_NAME2) + suffix + "\"," +
                "                      \"volumeGroupInstanceName\" : \"" + names.get(VG_NAME) + suffix + "\"," +
                "                      \"instanceParams\": [" +
                "                        {" +
                "                          \"vre_a_volume_size_0\": \"100\"," +
                "                          \"availability_zone_0\": \"mtpocdv-kvm-az01\"" +
                "                        }" +
                "                      ]" +
                "                    }" +
                "                  ]," +
                "                  \"instanceName\": \"" + names.get(VNF_NAME)+ suffix + "\"" +
                "                }" +
                "              ]" +
                "            }" +
                "          }" +
                "        }" +
                "      ]" +
                "    }" +
                "  }" +
                "}";

    }


}
