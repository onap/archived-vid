package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;
import vid.automation.test.infra.ModelInfo;
import vid.automation.test.test.BrowseASDCTest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static vid.automation.test.infra.ModelInfo.ModelInfoWithMultipleVersions.modelInfoWithMultipleVersions;

public class PresetAAIServiceDesignAndCreationPut extends BaseAAIPreset {

    public PresetAAIServiceDesignAndCreationPut(boolean emptyList) {
        modelInfos = ImmutableList.of();
        this.emptyList = emptyList;
    }


    private List<ModelInfo> modelInfos;
    boolean emptyList;


    //Constructor that use the super set of model infos as response
    //all test should be migrated to use this constructor
    public PresetAAIServiceDesignAndCreationPut() {
        this.modelInfos = ModelInfo.superSetOfModelInfos;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.PUT;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/query";
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of("format", Collections.singletonList("resource"));
    }

    @Override
    public Object getRequestBody() {
        return "{\"start\" : \"service-design-and-creation/models/\", \"query\" : \"query/serviceModels-byDistributionStatus?distributionStatus=DISTRIBUTION_COMPLETE_OK\"}";
    }



    private String presetModels() {
        return
                modelInfos.stream().map(modelInfo ->
                        "       {" +
                                "          \"model\": {" +
                                "            \"model-invariant-id\": \"" + modelInfo.modelInvariantId + "\"," +
                                "            \"model-type\": \"resource\"," +
                                "            \"resource-version\": \"1500138206526\"," +
                                "            \"model-vers\": {" +
                                "              \"model-ver\": [" +
                                "                {" +
                                "                  \"model-version-id\": \"" + modelInfo.modelVersionId + "\"," +
                                "                  \"model-name\": \"action-data\"," +
                                "                  \"model-version\": \"1.0\"," +
                                "                  \"model-description\": \"lustre settler sideways volcanic eight cellular\"," +
                                "                  \"resource-version\": \"1500137463984\"," +
                                "                  \"relationship-list\": {" +
                                "                    \"relationship\": [" +
                                "                      {" +
                                "                        \"related-to\": \"model-element\"," +
                                "                        \"relationship-label\": \"isA\"," +
                                "                        \"related-link\": \"/aai/v12/service-design-and-creation/models/model/l2-bridge-for-wan-connector-model-id-ps-02/model-vers/model-ver/l2-bridge-for-wan-connector-resource-id-ps-02/model-elements/model-element/e874da22-729d-47bd-8c08-d596fd9c213d/model-elements/model-element/ccbb65b8-2faf-4f5f-80d9-804bb50f1455\"," +
                                "                        \"relationship-data\": [" +
                                "                          {" +
                                "                            \"relationship-key\": \"model.model-invariant-id\"," +
                                "                            \"relationship-value\": \"l2-bridge-for-wan-connector-model-id-ps-02\"" +
                                "                          }," +
                                "                          {" +
                                "                            \"relationship-key\": \"model-ver.model-version-id\"," +
                                "                            \"relationship-value\": \"l2-bridge-for-wan-connector-resource-id-ps-02\"" +
                                "                          }," +
                                "                          {" +
                                "                            \"relationship-key\": \"model-element.model-element-uuid\"," +
                                "                            \"relationship-value\": \"e874da22-729d-47bd-8c08-d596fd9c213d\"" +
                                "                          }," +
                                "                          {" +
                                "                            \"relationship-key\": \"model-element.model-element-uuid\"," +
                                "                            \"relationship-value\": \"ccbb65b8-2faf-4f5f-80d9-804bb50f1455\"" +
                                "                          }" +
                                "                        ]" +
                                "                      }" +
                                "                    ]" +
                                "                  }" +
                                "                }" +
                                "              ]" +
                                "            }" +
                                "          }" +
                                "        },"
                ).collect(Collectors.joining());
    }


    private String serviceWithMultipleVersions() {
        return
                "        {" +
                "          \"model\": {" +
                "            \"model-invariant-id\": \"" + modelInfoWithMultipleVersions.modelInvariantId + "\"," +
                "            \"model-type\": \"resource\"," +
                "            \"resource-version\": \"1500138206526\"," +
                "            \"model-vers\": {" +
                "              \"model-ver\": [" +
                "                {" +
                "                  \"model-version-id\": \"" + modelInfoWithMultipleVersions.modelVersionId1 + "\"," +
                "                  \"model-name\": \""+modelInfoWithMultipleVersions.modelName+"\"," +
                "                  \"model-version\": \"1.0\"," +
                "                  \"model-description\": \"decontamination arm circus ammonia hump edge\"," +
                "                  \"resource-version\": \"1500137463984\"" +
                "                }," +
                "				{" +
                "                  \"model-version-id\": \"" + modelInfoWithMultipleVersions.modelVersionId3 + "\"," +
                "                  \"model-name\": \""+modelInfoWithMultipleVersions.modelName+"\"," +
                "                  \"model-version\": \"3.0\"," +
                "                  \"model-description\": \"Non decontamination arm circus ammonia hump edge\"," +
                "                  \"resource-version\": \"1500137463986\"" +
                "                }," +
                "				{" +
                "                  \"model-version-id\": \"" + modelInfoWithMultipleVersions.modelVersionId2 + "\"," +
                "                  \"model-name\": \""+modelInfoWithMultipleVersions.modelName+"\"," +
                "                  \"model-version\": \"2.0\"," +
                "                  \"model-description\": \"The oldest one\"," +
                "                  \"resource-version\": \"1500137463980\"" +
                "                }" +
                "              ]" +
                "            }" +
                "          }" +
                "        }," ;
    }
    
    public String modelsForBrowseSDCServiceModelListCheckAAIResponse() {
        return  "   {\"model\":    { " +
                "     \"model-invariant-id\": \"" + BrowseASDCTest.modelInvariantUUID1 + "\"," +
                "      \"model-type\": \"service\"," +
                "      \"resource-version\": \"1515103312329\"," +
                "      \"model-vers\": {\"model-ver\":       [" +
                "                  {" +
                "            \"model-version-id\": \"" + BrowseASDCTest.modelUuid + "\"," +
                "            \"model-name\": \"CheckAAIResponse_AAAvIRC_mm779p_Service\"," +
                "            \"model-version\": \"1.0\"," +
                "            \"distribution-status\": \"DISTRIBUTION_COMPLETE_OK\"," +
                "            \"model-description\": \"tbd\"," +
                "            \"resource-version\": \"1516206395612\"" +
                "         }" +
                "      ]}" +
                "   }}," +
                "   {\"model\":    { " +
                "      \"model-invariant-id\": \"" + BrowseASDCTest.modelInvariantUUID2 + "\"," +
                "      \"model-type\": \"service\"," +
                "      \"resource-version\": \"1515103312329\"," +
                "      \"model-vers\": {\"model-ver\":       [" +
                "                  {" +
                "            \"model-version-id\": \"1dae721c-a1ef-435f-b811-760c23f467bf\"," +
                "            \"model-name\": \"CheckAAIResponse_BBBvIRC_mm779p_Service\"," +
                "            \"model-version\": \"3.0\"," +
                "            \"model-description\": \"tbd\"," +
                "            \"resource-version\": \"1516025197086\"" +
                "         }" +
                "      ]}" +
                "   }}," +
                "   {\"model\":    { " +
                "      \"model-invariant-id\": \"" + BrowseASDCTest.modelInvariantUUID3 + "\"," +
                "      \"model-type\": \"service\"," +
                "      \"resource-version\": \"1515103312329\"," +
                "      \"model-vers\": {\"model-ver\":       [" +
                "          {" +
                "            \"model-version-id\": \"29236d45-e790-4c17-a115-1533cc09b7b1\"," +
                "            \"model-name\": \"CheckAAIResponse_CCCvIRC_mm779p_Service\"," +
                "            \"model-version\": \"4.0\"," +
                "            \"distribution-status\": \"DISTRIBUTION_COMPLETE_ERROR\"," +
                "            \"model-description\": \"tbd\"," +
                "            \"resource-version\": \"1517319724440\"" +
                "         }" +
                "      ]}" +
                "   }},";
    }
    
    @Override
    public Object getResponseBody() {
        if(emptyList) {
            return "{\"results\": [{}]}";
        }
        return "{\"results\": [" +
                presetModels()+
                serviceWithMultipleVersions() +
                modelsForBrowseSDCServiceModelListCheckAAIResponse() +
                "        {" +
                "          \"model\": {" +
                "            \"model-invariant-id\": \"00beb8f9-6d39-452f-816d-c709b9cbb87d\"," +
                "            \"model-type\": \"resource\"," +
                "            \"resource-version\": \"4076846985447\"," +
                "            \"model-vers\": {" +
                "              \"model-ver\": [" +
                "                {" +
                "                  \"model-version-id\": \"0903e1c0-8e03-4936-b5c2-260653b96413\"," +
                "                  \"model-name\": \"action-data\"," +
                "                  \"orchestration-type\": \"a la carte\"," +
                "                  \"model-version\": \"1.0\"," +
                "                  \"model-description\": \"honor immunity exile prong below misshapen\"," +
                "                  \"resource-version\": \"4076846985447\"" +
                "                }" +
                "              ]" +
                "            }" +
                "          }" +
                "        }," +
                "        {" +
                "          \"model\": {" +
                "            \"model-invariant-id\": \"ea78c9e3-514d-4a0a-9162-13837fa54c35\"," +
                "            \"model-type\": \"resource\"," +
                "            \"resource-version\": \"1500137034452\"," +
                "            \"model-vers\": {" +
                "              \"model-ver\": [" +
                "                {" +
                "                  \"model-version-id\": \"666a06ee-4b57-46df-bacf-908da8f10c3f\"," +
                "                  \"model-name\": \"multicast-configuration\"," +
                "                  \"orchestration-type\": \"a la carte\"," +
                "                  \"model-version\": \"1.0\"," +
                "                  \"model-description\": \"python bullwhip appointment computation ambidextrous heaving\"," +
                "                  \"resource-version\": \"1500136282691\"" +
                "                }" +
                "              ]" +
                "            }" +
                "          }" +
                "        }," +
                "        {" +
                "          \"model\": {" +
                "            \"model-invariant-id\": \"78ca26d0-246d-11e7-93ae-92361f002671\"," +
                "            \"model-type\": \"resource\"," +
                "            \"resource-version\": \"1492627634298\"," +
                "            \"model-vers\": {" +
                "              \"model-ver\": [" +
                "                {" +
                "                  \"model-version-id\": \"20c4431c-246d-11e7-93ae-92361f002671\"," +
                "                  \"model-name\": \"vSAMP10aDEV::base::module-0\"," +
                "                  \"orchestration-type\": \"a la carte\"," +
                "                  \"model-version\": \"2\"," +
                "                  \"model-description\": \"MSO aLaCarte VF vSAMP10aDEV Base\"," +
                "                  \"resource-version\": \"1492627634300\"" +
                "                }" +
                "              ]" +
                "            }" +
                "          }" +
                "        }," +
                "        {" +
                "          \"model\": {" +
                "            \"model-invariant-id\": \"5b607929-6088-4614-97ef-cac817508e0e\"," +
                "            \"model-type\": \"resource\"," +
                "            \"resource-version\": \"1492814035001\"," +
                "            \"model-vers\": {" +
                "              \"model-ver\": [" +
                "                {" +
                "                  \"model-version-id\": \"797a6c41-0f80-4d35-a288-3920c4e06baa\"," +
                "                  \"model-name\": \"CONTRAIL30_L2NODHCP\"," +
                "                  \"orchestration-type\": \"macro\"," +
                "                  \"model-version\": \"1.0\"," +
                "                  \"model-description\": \"contrail 3.0.x L2 network for AIC 3.x sites (and No DHCP).\"," +
                "                  \"resource-version\": \"1492814035003\"," +
                "                  \"relationship-list\": {" +
                "                    \"relationship\": [" +
                "                      {" +
                "                        \"related-to\": \"model-element\"," +
                "                        \"relationship-label\": \"isA\"," +
                "                        \"related-link\": \"/aai/v12/service-design-and-creation/models/model/52b49b5d-3086-4ffd-b5e6-1b1e5e7e062f/model-vers/model-ver/aed5a5b7-20d3-44f7-90a3-ddbd16f14d1e/model-elements/model-element/2f622421-1c67-4142-be6e-cecad5242af6/model-elements/model-element/2add7556-a23b-46b8-b6b4-ad300b47ceab\"," +
                "                        \"relationship-data\": [" +
                "                          {" +
                "                            \"relationship-key\": \"model.model-invariant-id\"," +
                "                            \"relationship-value\": \"52b49b5d-3086-4ffd-b5e6-1b1e5e7e062f\"" +
                "                          }," +
                "                          {" +
                "                            \"relationship-key\": \"model-ver.model-version-id\"," +
                "                            \"relationship-value\": \"aed5a5b7-20d3-44f7-90a3-ddbd16f14d1e\"" +
                "                          }," +
                "                          {" +
                "                            \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                            \"relationship-value\": \"2f622421-1c67-4142-be6e-cecad5242af6\"" +
                "                          }," +
                "                          {" +
                "                            \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                            \"relationship-value\": \"2add7556-a23b-46b8-b6b4-ad300b47ceab\"" +
                "                          }" +
                "                        ]" +
                "                      }" +
                "                    ]" +
                "                  }" +
                "                }" +
                "              ]" +
                "            }" +
                "          }" +
                "        }," +
                "        {" +
                "          \"model\": {" +
                "            \"model-invariant-id\": \"0143d57b-a517-4de9-a0a1-eb76db51f402\"," +
                "            \"model-type\": \"resource\"," +
                "            \"resource-version\": \"1497897268768\"," +
                "            \"model-vers\": {" +
                "              \"model-ver\": [" +
                "                {" +
                "                  \"model-version-id\": \"f1bde010-cc5f-4765-941f-75f15b24f9fc\"," +
                "                  \"model-name\": \"BkVmxAv061917..base_vPE_AV..module-0\"," +
                "                  \"orchestration-type\": \"macro\"," +
                "                  \"model-version\": \"2\"," +
                "                  \"resource-version\": \"1497897268769\"," +
                "                  \"relationship-list\": {" +
                "                    \"relationship\": [" +
                "                      {" +
                "                        \"related-to\": \"model-element\"," +
                "                        \"relationship-label\": \"isA\"," +
                "                        \"related-link\": \"/aai/v12/service-design-and-creation/models/model/267ef491-3c1a-4c32-8e69-0e557bfb61e7/model-vers/model-ver/18655c7f-c846-4934-9e25-34378dfd33d6/model-elements/model-element/578492b2-36a4-47da-83dc-91b58c699ad1/model-elements/model-element/6b1fc7a5-fc01-4a9d-a87f-0ad0d8caa13f\"," +
                "                        \"relationship-data\": [" +
                "                          {" +
                "                            \"relationship-key\": \"model.model-invariant-id\"," +
                "                            \"relationship-value\": \"267ef491-3c1a-4c32-8e69-0e557bfb61e7\"" +
                "                          }," +
                "                          {" +
                "                            \"relationship-key\": \"model-ver.model-version-id\"," +
                "                            \"relationship-value\": \"18655c7f-c846-4934-9e25-34378dfd33d6\"" +
                "                          }," +
                "                          {" +
                "                            \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                            \"relationship-value\": \"578492b2-36a4-47da-83dc-91b58c699ad1\"" +
                "                          }," +
                "                          {" +
                "                            \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                            \"relationship-value\": \"6b1fc7a5-fc01-4a9d-a87f-0ad0d8caa13f\"" +
                "                          }" +
                "                        ]" +
                "                      }" +
                "                    ]" +
                "                  }" +
                "                }" +
                "              ]" +
                "            }" +
                "          }" +
                "        }," +
                "        {" +
                "          \"model\": {" +
                "            \"model-invariant-id\": \"ipe-resource-id-ps-02\"," +
                "            \"model-type\": \"resource\"," +
                "            \"resource-version\": \"1493389430122\"," +
                "            \"model-vers\": {" +
                "              \"model-ver\": [" +
                "                {" +
                "                  \"model-version-id\": \"ipe-resource-id-ps-02\"," +
                "                  \"model-name\": \"abc\"," +
                "                  \"orchestration-type\": \"macro\"," +
                "                  \"model-version\": \"v1.0\"," +
                "                  \"resource-version\": \"1493389520357\"," +
                "                  \"relationship-list\": {" +
                "                    \"relationship\": [" +
                "                      {" +
                "                        \"related-to\": \"model-element\"," +
                "                        \"relationship-label\": \"isA\"," +
                "                        \"related-link\": \"/aai/v12/service-design-and-creation/models/model/l2-bridge-for-wan-connector-model-id-ps-02/model-vers/model-ver/l2-bridge-for-wan-connector-resource-id-ps-02/model-elements/model-element/e874da22-729d-47bd-8c08-d596fd9c213d/model-elements/model-element/ccbb65b8-2faf-4f5f-80d9-804bb50f1455\"," +
                "                        \"relationship-data\": [" +
                "                          {" +
                "                            \"relationship-key\": \"model.model-invariant-id\"," +
                "                            \"relationship-value\": \"l2-bridge-for-wan-connector-model-id-ps-02\"" +
                "                          }," +
                "                          {" +
                "                            \"relationship-key\": \"model-ver.model-version-id\"," +
                "                            \"relationship-value\": \"l2-bridge-for-wan-connector-resource-id-ps-02\"" +
                "                          }," +
                "                          {" +
                "                            \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                            \"relationship-value\": \"e874da22-729d-47bd-8c08-d596fd9c213d\"" +
                "                          }," +
                "                          {" +
                "                            \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                            \"relationship-value\": \"ccbb65b8-2faf-4f5f-80d9-804bb50f1455\"" +
                "                          }" +
                "                        ]" +
                "                      }" +
                "                    ]" +
                "                  }" +
                "                }" +
                "              ]" +
                "            }" +
                "          }" +
                "        }," +
                "        {" +
                "          \"model\": {" +
                "            \"model-invariant-id\": \"lmoser410-connector-model-id\"," +
                "            \"model-type\": \"widget\"," +
                "            \"resource-version\": \"1493389512180\"," +
                "            \"model-vers\": {" +
                "              \"model-ver\": [" +
                "                {" +
                "                  \"model-version-id\": \"lmoser410-connector-model-version-id\"," +
                "                  \"model-name\": \"connector\"," +
                "                  \"orchestration-type\": \"macro\"," +
                "                  \"model-version\": \"v1.0\"," +
                "                  \"resource-version\": \"1493389444766\"" +
                "                }" +
                "              ]" +
                "            }," +
                "            \"relationship-list\": {" +
                "              \"relationship\": [" +
                "                {" +
                "                  \"related-to\": \"named-query-element\"," +
                "                  \"relationship-label\": \"isA\"," +
                "                  \"related-link\": \"/aai/v12/service-design-and-creation/named-queries/named-query/lmoser410-named-query-uuid/named-query-elements/named-query-element/48278a7d-2b1e-454f-89e6-5c0ba145f486\"," +
                "                  \"relationship-data\": [" +
                "                    {" +
                "                      \"relationship-key\": \"named-query.named-query-uuid\"," +
                "                      \"relationship-value\": \"lmoser410-named-query-uuid\"" +
                "                    }," +
                "                    {" +
                "                      \"relationship-key\": \"named-query-element.named-query-element-uuid\"," +
                "                      \"relationship-value\": \"48278a7d-2b1e-454f-89e6-5c0ba145f486\"" +
                "                    }" +
                "                  ]" +
                "                }" +
                "              ]" +
                "            }" +
                "          }" +
                "        }," +
                "        {" +
                "          \"model\": {" +
                "            \"model-invariant-id\": \"3a97db99-c4bb-498a-a13a-38f65f1ced3d\"," +
                "            \"model-type\": \"resource\"," +
                "            \"resource-version\": \"1492630209768\"," +
                "            \"model-vers\": {" +
                "              \"model-ver\": [" +
                "                {" +
                "                  \"model-version-id\": \"ff2ae348-214a-11e7-93ae-92361f002673\"," +
                "                  \"model-name\": \"vSAMP10aDEV::base::module-0\"," +
                "                  \"model-version\": \"1.0\"," +
                "                  \"model-description\": \"MACRO_vSAMP_module\"," +
                "                  \"resource-version\": \"1492630209769\"" +
                "                }" +
                "              ]" +
                "            }" +
                "          }" +
                "        }," +
                "        {" +
                "          \"model\": {" +
                "            \"model-invariant-id\": \"3c504d40-b847-424c-9d25-4fb7e0a3e994\"," +
                "            \"model-type\": \"widget\"," +
                "            \"resource-version\": \"1493389430124\"," +
                "            \"model-vers\": {" +
                "              \"model-ver\": [" +
                "                {" +
                "                  \"model-version-id\": \"204c641a-3494-48c8-979a-86856f5fd32a\"," +
                "                  \"model-name\": \"named-query-element\"," +
                "                  \"model-version\": \"1.0\"," +
                "                  \"resource-version\": \"1493389423212\"" +
                "                }" +
                "              ]" +
                "            }" +
                "          }" +
                "        }," +
                "        {" +
                "          \"model\": {" +
                "            \"model-invariant-id\": \"93e56950-cb19-44e6-ace4-8b50f2d02e45\"," +
                "            \"model-type\": \"resource\"," +
                "            \"resource-version\": \"1497898974232\"," +
                "            \"model-vers\": {" +
                "              \"model-ver\": [" +
                "                {" +
                "                  \"model-version-id\": \"acba1f72-c6e0-477f-9426-ad190151e100\"," +
                "                  \"model-name\": \"RG_6-19_Test\"," +
                "                  \"model-version\": \"1.0\"," +
                "                  \"model-description\": \"RG_6-19_Test\"," +
                "                  \"resource-version\": \"1497898974233\"," +
                "                  \"relationship-list\": {" +
                "                    \"relationship\": [" +
                "                      {" +
                "                        \"related-to\": \"model-element\"," +
                "                        \"relationship-label\": \"isA\"," +
                "                        \"related-link\": \"/aai/v12/service-design-and-creation/models/model/a07220ba-954e-422f-86b5-e8b95fe6b116/model-vers/model-ver/bf6dbd68-2c7d-4ba2-ade1-dfd4476aa505/model-elements/model-element/6e10fe2e-6d04-4198-9124-dd051a5ae65f/model-elements/model-element/cb3c22e1-9dc6-467f-9636-0a7aef10d5d0\"," +
                "                        \"relationship-data\": [" +
                "                          {" +
                "                            \"relationship-key\": \"model.model-invariant-id\"," +
                "                            \"relationship-value\": \"a07220ba-954e-422f-86b5-e8b95fe6b116\"" +
                "                          }," +
                "                          {" +
                "                            \"relationship-key\": \"model-ver.model-version-id\"," +
                "                            \"relationship-value\": \"bf6dbd68-2c7d-4ba2-ade1-dfd4476aa505\"" +
                "                          }," +
                "                          {" +
                "                            \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                            \"relationship-value\": \"6e10fe2e-6d04-4198-9124-dd051a5ae65f\"" +
                "                          }," +
                "                          {" +
                "                            \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                            \"relationship-value\": \"cb3c22e1-9dc6-467f-9636-0a7aef10d5d0\"" +
                "                          }" +
                "                        ]" +
                "                      }" +
                "                    ]" +
                "                  }" +
                "                }" +
                "              ]" +
                "            }" +
                "          }" +
                "        }" +
                "      ]}";
    }
}
