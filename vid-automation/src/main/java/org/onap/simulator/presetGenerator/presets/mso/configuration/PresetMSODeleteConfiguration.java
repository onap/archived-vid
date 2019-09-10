package org.onap.simulator.presetGenerator.presets.mso.configuration;

import org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseCreateInstancePost;
import org.springframework.http.HttpMethod;

public class PresetMSODeleteConfiguration extends PresetMSOBaseCreateInstancePost {

    public PresetMSODeleteConfiguration() {
        this.cloudOwner = "att-nc";
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.DELETE;
    }

    @Override
    public boolean isStrictMatch() {
        return true;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/c187e9fe-40c3-4862-b73e-84ff056205f6/configurations/9533-config-LB1113";
    }

    @Override
    public Object getRequestBody() {
        return "" +
                "{" +
                "  \"requestDetails\": {" +
                "    \"cloudConfiguration\": {" +
                       addCloudOwnerIfNeeded() +
                "      \"lcpCloudRegionId\": \"mdt1\"" +
                "    }," +
                "    \"modelInfo\": {" +
                "      \"modelCustomizationId\": \"08a181aa-72eb-435f-9593-e88a3ad0a86b\"," +
                "      \"modelInvariantId\": \"model-invariant-id-9533\"," +
                "      \"modelVersionId\": \"model-version-id-9533\"," +
                "      \"modelType\": \"configuration\"" +
                "    }," +
                "    \"requestInfo\": {" +
                "      \"source\": \"VID\"," +
                "      \"requestorId\": \"us16807000\"" +
                "    }," +
                "    \"requestParameters\": {" +
                "      \"userParams\": []" +
                "    }" +
                "  }" +
                "}";
    }

}
