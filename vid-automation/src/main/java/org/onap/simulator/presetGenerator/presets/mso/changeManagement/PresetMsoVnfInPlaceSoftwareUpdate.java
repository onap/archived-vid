package org.onap.simulator.presetGenerator.presets.mso.changeManagement;

import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;
import org.springframework.http.HttpMethod;

public class PresetMsoVnfInPlaceSoftwareUpdate extends PresetMsoChangeManagementBase {

    public PresetMsoVnfInPlaceSoftwareUpdate(String serviceInstanceId, String vnfInstanceId) {
        super(serviceInstanceId, vnfInstanceId, "inPlaceSoftwareUpdate");
        this.cloudOwner = PresetAAIGetCloudOwnersByCloudRegionId.ATT_NC;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "   \"requestDetails\": {" +
                "      \"cloudConfiguration\": {" +
                "          \"lcpCloudRegionId\": \"mdt1\"," +
                            addCloudOwnerIfNeeded() +
                "          \"tenantId\": \"88a6ca3ee0394ade9403f075db23167e\"" +
                "      }," +
                "      \"requestInfo\": {" +
                "          \"source\": \"VID\"," +
                "          \"requestorId\": \"az2016\"" +
                "      }," +
                "      \"requestParameters\": {" +
                "           \"payload\": \"{\\\"existing_software_version\\\": \\\"3.1\\\", \\\"new_software_version\\\": \\\"3.2\\\", \\\"operations_timeout\\\": \\\"3600\\\"}\"" +
                "      }" +
                "   }" +
                "}";
    }
}
