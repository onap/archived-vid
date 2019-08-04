package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateMacroPre1806Post extends PresetMSOBaseCreateInstancePost {

    public PresetMSOCreateMacroPre1806Post() {
        this.cloudOwner = "irma-aic";
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstances/v.";
    }

    @Override
    public Object getRequestBody() {
        return "" +
                "{" +
                "      \"requestDetails\": {" +
                "    \"requestInfo\": {" +
                "      \"instanceName\": \"New Instance Name\"," +
                "      \"source\": \"VID\"," +
                "      \"suppressRollback\": false," +
                "      \"requestorId\": \"fi5777000\"," +
                "      \"productFamilyId\": \"e30755dc-5673-4b6b-9dcf-9abdd96b93d1\"" +
                "    }," +
                "    \"modelInfo\": {" +
                "      \"modelType\": \"service\"," +
                "      \"modelInvariantId\": \"d27e42cf-087e-4d31-88ac-6c4b7585f800\"," +
                "      \"modelVersionId\": \"4d71990b-d8ad-4510-ac61-496288d9078e\"," +
                "      \"modelName\": \"vidmacrofalsenaming\"," +
                "      \"modelVersion\": \"1.0\"" +
                "    }," +
                "    \"requestParameters\": {" +
                "      \"userParams\": [" +
                "        {" +
                "          \"name\": \"aic_zone\"," +
                "          \"value\": \"NFT1\"" +
                "        }" +
                "      ]," +
                "      \"subscriptionServiceType\": \"AIM Transport\"," +
                "      \"aLaCarte\": false" +
                "    }," +
                "    \"cloudConfiguration\": {" +
                "      \"lcpCloudRegionId\": \"olson3\"," +
                addCloudOwnerIfNeeded() +
                "      \"tenantId\": \"bae71557c5bb4d5aac6743a4e5f1d054\"" +
                "    }," +
                "    \"subscriberInfo\": {" +
                "      \"globalSubscriberId\": \"31739f3e-526b-11e6-beb8-9e71128cae77\"" +
                "    }," +
                "    \"project\": {" +
                "      \"projectName\": \"yyy1\"" +
                "    }," +
                "    \"owningEntity\": {" +
                "      \"owningEntityId\": \"aaa1\"," +
                "      \"owningEntityName\": \"aaa1\"" +
                "    }" +
                "  }" +
                "}";
    }
}
