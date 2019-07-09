package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateServiceInstanceGen2 extends PresetMSOBaseCreateInstancePost {

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances";
    }

    @Override
    public boolean isStrictMatch() {
        return true;
    }

    @Override
    public Object getRequestBody() {
        return "" +
                "{ " +
                "  \"requestDetails\": { " +
                "    \"modelInfo\": { " +
                "      \"modelInvariantId\": \"e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0\", " +
                "      \"modelVersionId\": \"1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd\", " +
                "      \"modelName\": \"action-data\", " +
                "      \"modelType\": \"service\", " +
                "      \"modelVersion\": \"1.0\" " +
                "    }, " +
                "    \"owningEntity\": { " +
                "      \"owningEntityId\": \"d61e6f2d-12fa-4cc2-91df-7c244011d6fc\", " +
                "      \"owningEntityName\": \"WayneHolland\" " +
                "    }, " +
                "    \"subscriberInfo\": { " +
                "      \"globalSubscriberId\": \"e433710f-9217-458d-a79d-1c7aff376d89\" " +
                "    }, " +
                "    \"project\": { " +
                "      \"projectName\": \"WATKINS\" " +
                "    }, " +
                "    \"requestInfo\": { " +
//                "      \"instanceName\": \"some instance name_0" + suffix + "\", " +
                "      \"productFamilyId\": \"e433710f-9217-458d-a79d-1c7aff376d89\", " +
                "      \"source\": \"VID\", " +
                "      \"suppressRollback\": true, " +
                "      \"requestorId\": \"us16807000\" " +
                "    }, " +
                "    \"requestParameters\": { " +
                "      \"subscriptionServiceType\": \"TYLER SILVIA\", " +
                "      \"aLaCarte\": false, " +
                "      \"userParams\": [{ " +
                "          \"service\": { " +
                "            \"modelInfo\": { " +
                "              \"modelVersionId\": \"1a80c596-27e5-4ca9-b5bb-e03a7fd4c0fd\", " +
                "              \"modelName\": \"action-data\", " +
                "              \"modelType\": \"service\" " +
                "            }, " +
//                "            \"instanceName\": \"some instance name_0" + suffix + "\", " +
                "            \"instanceParams\": [], " +
                "            \"resources\": { " +
                "              \"vnfs\": [{ " +
                "                  \"modelInfo\": { " +
                "                    \"modelCustomizationName\": \"2017-488_PASQUALE-vPE 0\", " +
                "                    \"modelCustomizationId\": \"1da7b585-5e61-4993-b95e-8e6606c81e45\", " +
                "                    \"modelInvariantId\": \"72e465fe-71b1-4e7b-b5ed-9496118ff7a8\", " +
                "                    \"modelVersionId\": \"69e09f68-8b63-4cc9-b9ff-860960b5db09\", " +
                "                    \"modelName\": \"2017-488_PASQUALE-vPE\", " +
                "                    \"modelType\": \"vnf\", " +
                "                    \"modelVersion\": \"5.0\" " +
                "                  }, " +
                "                  \"cloudConfiguration\": { " +
                "                    \"lcpCloudRegionId\": \"hvf6\", " +
                                     addCloudOwnerIfNeeded() +
                "                    \"tenantId\": \"bae71557c5bb4d5aac6743a4e5f1d054\" " +
                "                  }, " +
                "                  \"platform\": { " +
                "                    \"platformName\": \"platform\" " +
                "                  }, " +
                "                  \"lineOfBusiness\": { " +
                "                    \"lineOfBusinessName\": \"ONAP\" " +
                "                  }, " +
                "                  \"productFamilyId\": \"e433710f-9217-458d-a79d-1c7aff376d89\", " +
                "                  \"instanceParams\": [], " +
                "                  \"vfModules\": [" +
                "                    { " +
                "                      \"modelInfo\": { " +
                "                        \"modelInvariantId\": \"7253ff5c-97f0-4b8b-937c-77aeb4d79aa1\", " +
                "                        \"modelVersionId\": \"25284168-24bb-4698-8cb4-3f509146eca5\", " +
                "                        \"modelName\": \"2017488PasqualeVpe..PASQUALE_vRE_BV..module-1\", " +
                "                        \"modelType\": \"vfModule\", " +
                "                        \"modelVersion\": \"6\" " +
                "                      }, " +
//                                       \"instanceName\": \"VFinstancename_00" + suffix + "\", " +
                "                      \"instanceParams\": [] " +
                "                    }" +
                "                  ] " +
//                "                  \"instanceName\": \"2017488_PASQUALEvPEVNFinstancename_00" + suffix + "\" " +
                "                } " +
                "              ] " +
                "            } " +
                "          } " +
                "        } " +
                "      ] " +
                "    } " +
                "  } " +
                "}";
    }
}
