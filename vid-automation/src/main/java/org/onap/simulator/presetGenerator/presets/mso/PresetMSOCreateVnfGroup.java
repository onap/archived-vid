package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateVnfGroup extends PresetMSOBaseCreateInstancePost {
    private String serviceInstanceId;
    private String modelInfo;
    private String instanceName;
    private boolean suppressRollback;

    public PresetMSOCreateVnfGroup(String instanceName, String vnfGroupRequestId, String groupModelInfo, String serviceInstanceId,boolean suppressRollback ) {
        super(vnfGroupRequestId);
        this.serviceInstanceId = serviceInstanceId;
        this.modelInfo = groupModelInfo;
        this.instanceName = instanceName;
        this.suppressRollback = suppressRollback;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./instanceGroups";
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "  \"requestDetails\": {" +
                modelInfo +
                "    \"requestInfo\": {" +
                "      \"instanceName\": \"" + instanceName + "\"," +
                "      \"source\": \"VID\"," +
                "      \"suppressRollback\": "+suppressRollback+"," +
                "      \"requestorId\": \"us16807000\"" +
                "    }," +
                "    \"relatedInstanceList\": [" +
                "      {" +
                "        \"relatedInstance\": {" +
                "          \"instanceId\": \"" + serviceInstanceId + "\"," +
                "          \"modelInfo\": {" +
                "            \"modelType\": \"service\"," +
                "            \"modelInvariantId\": \"7ee41ce4-4827-44b0-a48e-2707a59905d2\"," +
                "            \"modelVersionId\": \"4117a0b6-e234-467d-b5b9-fe2f68c8b0fc\"," +
                "            \"modelName\": \"Grouping Service for Test\"," +
                "            \"modelVersion\": \"1.0\"" +
                "          }" +
                "        }" +
                "      }" +
                "    ]," +
                "    \"requestParameters\": {" +
                "      \"testApi\": \"GR_API\", " +
                "      \"userParams\": []" +
                "    }" +
                "  }" +
                "}";
    }

    //only modelType and modelVersionId are required by MSO fro create instance group
    public static final String MODEL_INFO_0 = "" +
            "    \"modelInfo\": {" +
            "      \"modelType\": \"instanceGroup\"," +
            "      \"modelVersionId\": \"daeb6568-cef8-417f-9075-ed259ce59f48\"" +
            "    },";

    public static final String MODEL_INFO_1 = "" +
            "    \"modelInfo\": {" +
            "      \"modelType\": \"instanceGroup\"," +
            "      \"modelVersionId\": \"c2b300e6-45de-4e5e-abda-3032bee2de56\"" +
            "    },";

}
