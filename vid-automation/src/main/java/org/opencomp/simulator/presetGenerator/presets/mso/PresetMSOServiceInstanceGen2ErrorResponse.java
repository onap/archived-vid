package org.opencomp.simulator.presetGenerator.presets.mso;

public class PresetMSOServiceInstanceGen2ErrorResponse extends PresetMSOBaseCreateServiceInstancePost {

    private final int responseCode;

    public PresetMSOServiceInstanceGen2ErrorResponse(int responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public String getReqPath() {
        return  getRootPath() + "/serviceInstantiation/v7/serviceInstances/assign";
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
