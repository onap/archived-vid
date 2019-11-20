package vid.automation.test.infra;

public class ModelInfoWithCustomization extends ModelInfoBase {

    public final String modelCustomizationName;
    public final String modelCustomizationId;

    public ModelInfoWithCustomization(String modelVersionId, String modelInvariantId, String modelName, String modelVersion, String resourceType,
        String modelCustomizationName, String modelCustomizationId) {
        super(modelVersionId, modelInvariantId, modelName, modelVersion, resourceType);
        this.modelCustomizationName = modelCustomizationName;
        this.modelCustomizationId = modelCustomizationId;
    }

    @Override
    protected String addAdditionalFields() {
        return "\"modelCustomizationName\": \""+modelCustomizationName+"\","
            +  "\"modelCustomizationId\": \""+modelCustomizationId+"\",";
    }
}
