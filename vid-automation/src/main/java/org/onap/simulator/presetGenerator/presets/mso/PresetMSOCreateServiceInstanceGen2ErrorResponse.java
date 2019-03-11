package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateServiceInstanceGen2ErrorResponse extends PresetMSOBaseCreateInstancePost {

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances";
    }

    @Override
    public int getResponseCode() {
        return 500;
    }

    @Override
    public Object getResponseBody() {
        return "{" +
                "    \"serviceException\": {" +
                "        \"messageId\": \"SVC0002\"," +
                "        \"text\": \"JSON Object Mapping Request\"" +
                "    }" +
                "}";
    }
}
