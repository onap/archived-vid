package vid.automation.test.infra;

public class ModelInfoBase {

    public final String modelVersionId; //aka model uuid
    public final String modelInvariantId;
    public final String modelName;
    public final String modelVersion;
    public final String resourceType;

    public ModelInfoBase(String modelVersionId, String modelInvariantId, String modelName, String modelVersion, String resourceType) {
        this.modelVersionId = modelVersionId;
        this.modelInvariantId = modelInvariantId;
        this.modelName = modelName;
        this.modelVersion = modelVersion;
        this.resourceType = resourceType;
    }

    public String createMsoModelInfo() {
        return
            "    \"modelInfo\": {" +
            "      \"modelInvariantId\": \""+modelInvariantId+"\"," +
            "      \"modelVersionId\": \""+modelVersionId+"\"," +
            "      \"modelName\": \""+modelName+"\"," +
            "      \"modelType\": \""+resourceType+"\"," +
                addAdditionalFields() +
            "      \"modelVersion\": \""+modelVersion+"\""  +
            "    },";
    }

    protected String addAdditionalFields() {
        return "";
    }
}
