package org.onap.simulator.presetGenerator.presets.mso;

import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.SERVICE_NAME;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.VFM_NAME1;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.VFM_NAME2;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.VG_NAME;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.VNF_NAME;
import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.VNF_NAME2;

import java.util.Map;
import vid.automation.test.infra.Features;

public class PresetMSOCreateServiceInstanceGen2WithNamesEcompNamingFalse extends PresetMSOCreateServiceInstanceGen2WithNames {

    protected static final String INSTANCE_PARAMS_VNF = "{ " +
            "                        \"bandwidth\": \"10\", " +
            "                        \"vnf_instance_name\": \"mtnj309me6\", " +
            "                        \"vnf_config_template_version\": \"17.2\", " +
            "                        \"AIC_CLLI\": \"ATLMY8GA\", " +
            "                        \"ASN\": \"AV_vPE\", " +
            "                        \"bandwidth_units\": \"Gbps\" " +
            "                       }";

    protected static final String INSTANCE_PARAMS_WITH_SUPP_FILE_VNF = "{ " +
            "                        \"vnf_config_template_version\": \"17.2\", " +
            "                        \"AIC_CLLI\": \"ATLMY8GA\", " +
            "                        \"bandwidth\": \"10\", " +
            "                        \"bandwidth_units\": \"Gbps\", " +
            "                        \"ASN\": \"AV_vPE\", " +
            "                        \"param\": \"ABCD\", " +
            "                        \"vnf_instance_name\": \"sample\" " +
            "                       }";

    protected static final String INSTANCE_PARAMS_VF_MODULE = "{ " +
            "                        \"bandwidth\": \"10\", " +
            "                        \"vnf_instance_name\": \"mtnj309me6\", " +
            "                        \"vnf_config_template_version\": \"17.2\", " +
            "                        \"AIC_CLLI\": \"ATLMY8GA\", " +
            "                        \"bandwidth_units\": \"Gbps\" " +
            "                       }";

    protected static final String INSTANCE_PARAMS_WITH_SUPP_FILE_VF_MODULE = "{ " +
            "                        \"vnf_config_template_version\": \"17.2\", " +
            "                        \"AIC_CLLI\": \"ATLMY8GA\", " +
            "                        \"bandwidth\": \"10\", " +
            "                        \"bandwidth_units\": \"Gbps\", " +
            "                        \"param\": \"ABCD\", " +
            "                        \"vnf_instance_name\": \"sample\" " +
            "                       }";



    public PresetMSOCreateServiceInstanceGen2WithNamesEcompNamingFalse(Map<Keys, String> names, int suffix, String requestId) {
        super(names, suffix, requestId);
    }

    @Override
    public boolean isStrictMatch() {
        return false;
    }

    protected String getVnfInstanceParams() {
        if (!Features.FLAG_SHIFT_VFMODULE_PARAMS_TO_VNF.isActive()) {
            return "";
        }

        if (Features.FLAG_SUPPLEMENTARY_FILE.isActive())  {
            return INSTANCE_PARAMS_WITH_SUPP_FILE_VNF;
        }
        return INSTANCE_PARAMS_VNF;
    }

    protected String getVFModuleInstanceParams() {
        if (Features.FLAG_SUPPLEMENTARY_FILE.isActive())  {
            return INSTANCE_PARAMS_WITH_SUPP_FILE_VF_MODULE;
        }
        return INSTANCE_PARAMS_VF_MODULE;
    }

    @Override
    public Object getRequestBody() {
        return "" +
                "{ " +
                "  \"requestDetails\": { " +
                "    \"modelInfo\": { " +
                "      \"modelInvariantId\": \"e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0\", " +
                "      \"modelVersionId\": \"6b528779-44a3-4472-bdff-9cd15ec93450\", " +
                "      \"modelName\": \"action-data\", " +
                "      \"modelType\": \"service\", " +
                "      \"modelVersion\": \"1.0\" " +
                "    }, " +
                "    \"owningEntity\": { " +
                "      \"owningEntityId\": \"d61e6f2d-12fa-4cc2-91df-7c244011d6fc\", " +
                "      \"owningEntityName\": \"MetroPacketCore\" " +
                "    }, " +
                "    \"subscriberInfo\": { " +
                "      \"globalSubscriberId\": \"e433710f-9217-458d-a79d-1c7aff376d89\" " +
                "    }, " +
                "    \"project\": { " +
                "      \"projectName\": \"DFW\" " +
                "    }, " +
                "    \"requestInfo\": { " +
                "      \"instanceName\": \"" + names.get(SERVICE_NAME) + suffix + "\", " +
                "      \"productFamilyId\": \"e433710f-9217-458d-a79d-1c7aff376d89\", " +
                "      \"source\": \"VID\", " +
                "      \"suppressRollback\": false, " +
                "      \"requestorId\": \"us16807000\" " +
                "    }, " +
                "    \"requestParameters\": { " +
                "      \"subscriptionServiceType\": \"TYLER SILVIA\", " +
                "      \"aLaCarte\": false, " +
                "      \"userParams\": [{ " +
                "          \"service\": { " +
                "            \"instanceName\": \"" + names.get(SERVICE_NAME) + suffix + "\", " +
                "            \"modelInfo\": { " +
                "              \"modelVersionId\": \"6b528779-44a3-4472-bdff-9cd15ec93450\", " +
                "              \"modelName\": \"action-data\", " +
                "              \"modelType\": \"service\" " +
                "            }, " +
                "            \"instanceParams\": [{ " +
                "                \"2017488_PASQUALEvpe0_ASN\": \"AV_vPE\"" +
                "              }" +
                "            ], " +
                "            \"resources\": { " +
                "              \"vnfs\": [{ " +
                "                  \"instanceName\": \"" + names.get(VNF_NAME2) + suffix + "\", " +
                "                  \"modelInfo\": { " +
                "                   \"modelCustomizationName\": \"2017-388_PASQUALE-vPE 0\", " +
                "                   \"modelCustomizationId\": \"b3c76f73-eeb5-4fb6-9d31-72a889f1811c\", " +
                "                   \"modelInvariantId\": \"72e465fe-71b1-4e7b-b5ed-9496118ff7a8\", " +
                "                   \"modelVersionId\": \"afacccf6-397d-45d6-b5ae-94c39734b168\", " +
                "                   \"modelName\": \"2017-388_PASQUALE-vPE\", " +
                "                   \"modelType\": \"vnf\", " +
                "                   \"modelVersion\": \"4.0\" " +
                "                  }, " +
                "                  \"cloudConfiguration\": { " +
                "                   \"lcpCloudRegionId\": \"hvf6\", " +
                                    addCloudOwnerIfNeeded() +
                "                   \"tenantId\": \"bae71557c5bb4d5aac6743a4e5f1d054\" " +
                "                  }, " +
                "                  \"platform\": { " +
                "                   \"platformName\": \"platform\" " +
                "                  }, " +
                "                  \"lineOfBusiness\": { " +
                "                   \"lineOfBusinessName\": \"ECOMP\" " +
                "                  }, " +
                "                  \"productFamilyId\": \"e433710f-9217-458d-a79d-1c7aff376d89\", " +
                "                  \"instanceParams\": [" + INSTANCE_PARAMS_VNF + "] " +
                "                }," +
                "                { " + //start of vnf
                "                  \"instanceName\": \"" + names.get(VNF_NAME) + suffix + "\", " +
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
                "                    \"lineOfBusinessName\": \"ECOMP\" " +
                "                  }, " +
                "                  \"productFamilyId\": \"e433710f-9217-458d-a79d-1c7aff376d89\", " +
                "                  \"instanceParams\": [" + getVnfInstanceParams() + "], " +
                "                  \"vfModules\": [{ " +
                "                     \"instanceName\": \"" + names.get(VFM_NAME1) + suffix + "\", " +
                "                     \"modelInfo\": { " +
                "                      \"modelCustomizationName\": \"2017488PASQUALEVpe..PASQUALE_base_vPE_BV..module-0\", " +
                "                      \"modelCustomizationId\": \"a55961b2-2065-4ab0-a5b7-2fcee1c227e3\", " +
                "                      \"modelInvariantId\": \"b34833bb-6aa9-4ad6-a831-70b06367a091\", " +
                "                      \"modelVersionId\": \"f8360508-3f17-4414-a2ed-6bc71161e8db\", " +
                "                      \"modelName\": \"2017488PASQUALEVpe..PASQUALE_base_vPE_BV..module-0\", " +
                "                      \"modelType\": \"vfModule\", " +
                "                      \"modelVersion\": \"5\" " +
                "                     }, " +
                "                     \"instanceParams\": [] " +
                "                    }, { " +
                "                      \"instanceName\": \"" + names.get(VFM_NAME2) + suffix + "\", " +
                "                      \"volumeGroupInstanceName\": \"" + names.get(VG_NAME) + suffix + "\", " +
                "                      \"modelInfo\": { " +
                "                        \"modelCustomizationName\": \"2017488PASQUALEVpe..PASQUALE_vRE_BV..module-1\", " +
                "                        \"modelCustomizationId\": \"f7e7c365-60cf-49a9-9ebf-a1aa11b9d401\", " +
                "                        \"modelInvariantId\": \"7253ff5c-97f0-4b8b-937c-77aeb4d79aa1\", " +
                "                        \"modelVersionId\": \"25284168-24bb-4698-8cb4-3f509146eca5\", " +
                "                        \"modelName\": \"2017488PASQUALEVpe..PASQUALE_vRE_BV..module-1\", " +
                "                        \"modelType\": \"vfModule\", " +
                "                        \"modelVersion\": \"6\" " +
                "                      }, " +
                "                      \"instanceParams\": [" + getVFModuleInstanceParams() + "] " +
                "                    }" + //end of vfModule
                "                  ] " + //end of vfModules list
                "                 }" + //end of vnf
                addDuplicatedVnfIfFeatureOn() +
                "              ] " + //end of vnf list
                "            } " +
                "          } " +
                "        } " +
                "      ] " +
                "    } " +
                "  } " +
                "}";

    }

    private String addDuplicatedVnfIfFeatureOn() {
        if (!Features.FLAG_DUPLICATE_VNF.isActive()) {
            return "";
        }

        return
                "                ,{ " + //start of vnf
                "                  \"instanceName\": \"" + names.get(VNF_NAME)+ "_001" + suffix + "\", " +
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
                "                    \"lineOfBusinessName\": \"ECOMP\" " +
                "                  }, " +
                "                  \"productFamilyId\": \"e433710f-9217-458d-a79d-1c7aff376d89\", " +
                "                  \"instanceParams\": [" + getVnfInstanceParams() + "], " +
                "                  \"vfModules\": [{ " +
                "                     \"instanceName\": \"" + names.get(VFM_NAME1) + "_001" + suffix + "\", " +
                "                     \"modelInfo\": { " +
                "                      \"modelCustomizationName\": \"2017488PASQUALEVpe..PASQUALE_base_vPE_BV..module-0\", " +
                "                      \"modelCustomizationId\": \"a55961b2-2065-4ab0-a5b7-2fcee1c227e3\", " +
                "                      \"modelInvariantId\": \"b34833bb-6aa9-4ad6-a831-70b06367a091\", " +
                "                      \"modelVersionId\": \"f8360508-3f17-4414-a2ed-6bc71161e8db\", " +
                "                      \"modelName\": \"2017488PASQUALEVpe..PASQUALE_base_vPE_BV..module-0\", " +
                "                      \"modelType\": \"vfModule\", " +
                "                      \"modelVersion\": \"5\" " +
                "                     }, " +
                "                     \"instanceParams\": [] " +
                "                    }, { " +
                "                      \"instanceName\": \"" + names.get(VFM_NAME2) + "_001" + suffix + "\", " +
                "                      \"volumeGroupInstanceName\": \"" + names.get(VG_NAME) + "_001" + suffix + "\", " +
                "                      \"modelInfo\": { " +
                "                        \"modelCustomizationName\": \"2017488PASQUALEVpe..PASQUALE_vRE_BV..module-1\", " +
                "                        \"modelCustomizationId\": \"f7e7c365-60cf-49a9-9ebf-a1aa11b9d401\", " +
                "                        \"modelInvariantId\": \"7253ff5c-97f0-4b8b-937c-77aeb4d79aa1\", " +
                "                        \"modelVersionId\": \"25284168-24bb-4698-8cb4-3f509146eca5\", " +
                "                        \"modelName\": \"2017488PASQUALEVpe..PASQUALE_vRE_BV..module-1\", " +
                "                        \"modelType\": \"vfModule\", " +
                "                        \"modelVersion\": \"6\" " +
                "                      }, " +
                "                      \"instanceParams\": [" + getVFModuleInstanceParams() + "] " +
                "                    }" + //end of vfModule
                "                  ] " + //end of vfModules list
                "                 }" + //end of vnf
                "                ,{ " + //start of vnf
                "                  \"instanceName\": \"" + names.get(VNF_NAME)+ "_002" + suffix + "\", " +
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
                "                    \"lineOfBusinessName\": \"ECOMP\" " +
                "                  }, " +
                "                  \"productFamilyId\": \"e433710f-9217-458d-a79d-1c7aff376d89\", " +
                "                  \"instanceParams\": [" + getVnfInstanceParams() + "], " +
                "                  \"vfModules\": [{ " +
                "                     \"instanceName\": \"" + names.get(VFM_NAME1) + "_002" + suffix + "\", " +
                "                     \"modelInfo\": { " +
                "                      \"modelCustomizationName\": \"2017488PASQUALEVpe..PASQUALE_base_vPE_BV..module-0\", " +
                "                      \"modelCustomizationId\": \"a55961b2-2065-4ab0-a5b7-2fcee1c227e3\", " +
                "                      \"modelInvariantId\": \"b34833bb-6aa9-4ad6-a831-70b06367a091\", " +
                "                      \"modelVersionId\": \"f8360508-3f17-4414-a2ed-6bc71161e8db\", " +
                "                      \"modelName\": \"2017488PASQUALEVpe..PASQUALE_base_vPE_BV..module-0\", " +
                "                      \"modelType\": \"vfModule\", " +
                "                      \"modelVersion\": \"5\" " +
                "                     }, " +
                "                     \"instanceParams\": [] " +
                "                    }, { " +
                "                      \"instanceName\": \"" + names.get(VFM_NAME2) + "_002" + suffix + "\", " +
                "                      \"volumeGroupInstanceName\": \"" + names.get(VG_NAME) + "_002" + suffix + "\", " +
                "                      \"modelInfo\": { " +
                "                        \"modelCustomizationName\": \"2017488PASQUALEVpe..PASQUALE_vRE_BV..module-1\", " +
                "                        \"modelCustomizationId\": \"f7e7c365-60cf-49a9-9ebf-a1aa11b9d401\", " +
                "                        \"modelInvariantId\": \"7253ff5c-97f0-4b8b-937c-77aeb4d79aa1\", " +
                "                        \"modelVersionId\": \"25284168-24bb-4698-8cb4-3f509146eca5\", " +
                "                        \"modelName\": \"2017488PASQUALEVpe..PASQUALE_vRE_BV..module-1\", " +
                "                        \"modelType\": \"vfModule\", " +
                "                        \"modelVersion\": \"6\" " +
                "                      }, " +
                "                      \"instanceParams\": [" + getVFModuleInstanceParams() + "] " +
                "                    }" + //end of vfModule
                "                  ] " + //end of vfModules list
                "                 }" ; //end of vnf
    }


}
