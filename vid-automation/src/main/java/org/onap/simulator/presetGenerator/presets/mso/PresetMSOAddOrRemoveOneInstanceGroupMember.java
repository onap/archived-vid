package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOAddOrRemoveOneInstanceGroupMember extends PresetMSOBaseCreateInstancePost {

    public enum InstanceGroupMemberAction {
        Add("/addMembers"),
        Remove("/removeMembers");

        private final String actionPath;

        InstanceGroupMemberAction(String actionPath) {
            this.actionPath = actionPath;
        }

        public String getActionPAth() {
            return actionPath;
        }
    }


    private final String memberInstanceId;
    private final String userId;
    private final InstanceGroupMemberAction action;

    public PresetMSOAddOrRemoveOneInstanceGroupMember(String vnfGroupInstanceId, String memberInstanceId, String userId, String requestId, InstanceGroupMemberAction action) {
        super(requestId, vnfGroupInstanceId);
        this.memberInstanceId = memberInstanceId;
        this.userId = userId;
        this.action = action;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./instanceGroups/" + responseInstanceId + action.getActionPAth();
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "  \"requestDetails\": {" +
                "    \"requestInfo\": {" +
                "      \"source\": \"VID\"," +
                "      \"requestorId\": \"" + userId + "\"" +
                "    }," +
                "    \"relatedInstanceList\": [" +
                "      {" +
                "        \"relatedInstance\": {" +
                "          \"instanceId\": \"" + memberInstanceId + "\"," +
                "          \"modelInfo\": {" +
                "            \"modelType\": \"vnf\"" +
                "          }" +
                "        }" +
                "      }" +
                "    ]" +
                "  }" +
                "}" +
                "";
    }

}
