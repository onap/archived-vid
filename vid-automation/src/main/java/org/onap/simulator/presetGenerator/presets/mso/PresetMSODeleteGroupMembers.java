package org.onap.simulator.presetGenerator.presets.mso;

import com.google.common.collect.ImmutableList;
import org.springframework.http.HttpMethod;

import static java.util.stream.Collectors.joining;

public class PresetMSODeleteGroupMembers extends PresetMSOBaseCreateInstancePost {


    private final ImmutableList<String> groupMembersInstanceIds;

    public PresetMSODeleteGroupMembers(String vnfGroupInstanceId, ImmutableList<String> groupMembersInstanceIds, String requestId) {
        super(requestId, vnfGroupInstanceId);
        this.groupMembersInstanceIds = groupMembersInstanceIds;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./instanceGroups/" + responseInstanceId + "/removeMembers";
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "  \"requestDetails\": {" +
                "    \"requestInfo\": {" +
                "      \"source\": \"VID\"," +
                "      \"requestorId\": \"us16807000\"" +
                "    }," +
                "    \"relatedInstanceList\": [" +
                groupMembersInstanceIds.stream().map(groupMemberInstanceId ->
                "       { " +
                "          \"relatedInstance\": { " +
                "           \"instanceId\": \""+ groupMemberInstanceId +"\"," +
                "           \"modelInfo\": {" +
                "               \"modelType\": \"vnf\"" +
                "             }" +
                "          }" +
                "       }").collect(joining(", ")) +
                "      ]" +
                "    }" +
                "}" ;
        }

}
