package org.opencomp.simulator.presetGenerator.presets.sdc;

import org.springframework.http.HttpMethod;

/**
 * Created by itzikliderman on 21/12/2017.
 */
public class PresetSDCGetServiceMetadataGet extends SdcPresetWithModelVersionId {

    public PresetSDCGetServiceMetadataGet(String modelVersionId, String modelInvariantId, String zipFileName) {
        super(modelVersionId);
        this.modelInvariantId = modelInvariantId;
        this.zipFileName = zipFileName;
    }

    private final String zipFileName;
    private final String modelInvariantId;


    @Override
    public Object getResponseBody() {
        return "{" +
                "        \"uuid\": \""+getModelVersionId()+"\"," +
                "        \"invariantUUID\": \""+getModelInvariantId()+"\"," +
                "        \"name\": \"action-data\"," +
                "        \"version\": \"1.0\"," +
                "        \"toscaModelURL\": \"./"+zipFileName+"\"," +
                "        \"category\": \"Mobility\"," +
                "        \"lifecycleState\": \"CERTIFIED\"," +
                "        \"lastUpdaterUserId\": \"rg276b\"," +
                "        \"lastUpdaterFullName\": null," +
                "        \"distributionStatus\": \"DISTRIBUTED\"," +
                "        \"artifacts\": null," +
                "        \"resources\": null" +
                "      }";
    }
    
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return super.getReqPath()+"/metadata";
    }

    public String getModelInvariantId() {
        return modelInvariantId;
    }
}
