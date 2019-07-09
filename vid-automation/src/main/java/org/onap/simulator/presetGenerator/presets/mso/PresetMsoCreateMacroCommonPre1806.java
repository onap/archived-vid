package org.onap.simulator.presetGenerator.presets.mso;

import org.apache.commons.lang3.StringUtils;

public class PresetMsoCreateMacroCommonPre1806 extends PresetMSOBaseCreateInstancePost {

    private String modelInfo;
    private String cloudConfiguration;
    private String relatedInstanceList;
    private String instanceName;

    private PresetMsoCreateMacroCommonPre1806(String requestId, String responseInstanceId, String instanceName, String modelInfo, String relatedInstanceList, String cloudConfiguration) {
        super(requestId, responseInstanceId);
        this.modelInfo = modelInfo;
        this.cloudConfiguration = cloudConfiguration;
        this.relatedInstanceList = relatedInstanceList;
        this.instanceName = instanceName;
    }

    public static PresetMsoCreateMacroCommonPre1806 ofTransportService(String requestId, String responseInstanceId) {
        return new PresetMsoCreateMacroCommonPre1806(requestId, responseInstanceId, "", transportServiceModelInfo(), "", "");
    }

    public static PresetMsoCreateMacroCommonPre1806 ofCollectionResource(String requestId, String responseInstanceId) {
        return new PresetMsoCreateMacroCommonPre1806(
                requestId,
                responseInstanceId,
                "",
                collectionResourceModelInfo(),
                "",
                hvf6CloudConfiguration());
    }

    public static PresetMsoCreateMacroCommonPre1806 ofServiceWithVRF(String requestId, String responseInstanceId, String instanceName) {
        return new PresetMsoCreateMacroCommonPre1806(
                requestId,
                responseInstanceId,
                instanceName,
                ""
                    + "    \"modelInfo\": { "
                    + "      \"modelInvariantId\": \"dfc2c44c-2429-44ca-ae26-1e6dc1f207fb\", "
                    + "      \"modelVersionId\": \"f028b2e2-7080-4b13-91b2-94944d4c42d8\", "
                    + "      \"modelName\": \"infraVPN\", "
                    + "      \"modelType\": \"service\", "
                    + "      \"modelVersion\": \"1.0\" "
                    + "    }, ",
                ""
                    + ", "
                    + "    \"relatedInstanceList\": [{ "
                    + "        \"relatedInstance\": { "
                    + "          \"modelInfo\": { "
                    + "            \"modelType\": \"vpnBinding\" "
                    + "          }, "
                    + "          \"instanceId\": \"120d39fb-3627-473d-913c-d228dd0f8e5b\", "
                    + "          \"instanceName\": \"LPPVPN\" "
                    + "        } "
                    + "      }, { "
                    + "        \"relatedInstance\": { "
                    + "          \"modelInfo\": { "
                    + "            \"modelCustomizationId\": \"10a74149-c9d7-4918-bbcf-d5fb9b1799ce\", "
                    + "            \"modelInvariantId\": \"3b3308d4-0cd3-43e4-9a7b-d1925c861135\", "
                    + "            \"modelVersionId\": \"77010093-df36-4dcb-8428-c3d02bf3f88d\", "
                    + "            \"modelType\": \"network\" "
                    + "          }, "
                    + "          \"instanceId\": \"10a74149-c9d7-4918-bbcf-d5fb9b1799ce\", "
                    + "          \"instanceName\": \"AUK51a_oam_calea_net_2\" "
                    + "        } "
                    + "      } "
                    + "    ] ",
                hvf6CloudConfiguration());
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances";
    }

    @Override
    public boolean isStrictMatch() {
        return true;
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "  \"requestDetails\": {" +
                    modelInfo +
                "    \"owningEntity\": {" +
                "      \"owningEntityId\": \"d61e6f2d-12fa-4cc2-91df-7c244011d6fc\"," +
                "      \"owningEntityName\": \"WayneHolland\"" +
                "    }," +
                "    \"subscriberInfo\": {" +
                "      \"globalSubscriberId\": \"e433710f-9217-458d-a79d-1c7aff376d89\"," +
                "      \"subscriberName\": \"SILVIA ROBBINS\"" +
                "    }," +
                "    \"project\": {" +
                "      \"projectName\": \"WATKINS\"" +
                "    }," +
                "    \"requestParameters\": {" +
                "      \"subscriptionServiceType\": \"TYLER SILVIA\"," +
                "      \"aLaCarte\": false," +
                "      \"userParams\": []" +
                "    }," +
                "    \"requestInfo\": {" +
                (StringUtils.isEmpty(instanceName) ? "" : "\"instanceName\": \"" + instanceName + "\",") +
                "      \"productFamilyId\": \"e433710f-9217-458d-a79d-1c7aff376d89\"," +
                "      \"source\": \"VID\"," +
                "      \"suppressRollback\": false," +
                "      \"requestorId\": \"us16807000\"" +
                "    }" +
                relatedInstanceList +
                cloudConfiguration +
                "  }" +
                "}";
    }

    private String addModelInfo() {
        return modelInfo;
    }

    private static String transportServiceModelInfo() {
        return "    \"modelInfo\": {" +
                "      \"modelInvariantId\": \"561faa57-7bbb-40ec-a81c-c0d4133e98d4\"," +
                "      \"modelVersionId\": \"12550cd7-7708-4f53-a09e-41d3d6327ebc\"," +
                "      \"modelName\": \"AIM Transport SVC_ym161f\"," +
                "      \"modelType\": \"service\"," +
                "      \"modelVersion\": \"1.0\"" +
                "    },";
    }

    private static String hvf6CloudConfiguration() {
        return
        ","+
        "\"cloudConfiguration\": {" +
        "      \"lcpCloudRegionId\": \"hvf6\"," +
        "      \"tenantId\": \"bae71557c5bb4d5aac6743a4e5f1d054\"," +
        "      \"cloudOwner\": \"irma-aic\"" +
        "    }";
    }

    private static String collectionResourceModelInfo() {
        return
        "\"modelInfo\": {" +
                "      \"modelInvariantId\": \"04bdd793-32ed-4045-adea-4e096304a067\"," +
                "      \"modelVersionId\": \"abd0cb02-5f97-42cd-be93-7dd3e31a6a64\"," +
                "      \"modelName\": \"CR_sanity\"," +
                "      \"modelType\": \"service\"," +
                "      \"modelVersion\": \"1.0\"" +
                "    },";
    }
}
