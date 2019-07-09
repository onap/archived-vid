package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class PresetAAIModelsByInvariantIdGet extends BaseAAIPreset {
    private final ImmutableList<String> modelInvariantIds;

    public PresetAAIModelsByInvariantIdGet(ImmutableList<String> modelInvariantIds) {
        this.modelInvariantIds = modelInvariantIds;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/service-design-and-creation/models";
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.<String, List>builder()
                .put("depth", ImmutableList.of("2"))
                .put("model-invariant-id", modelInvariantIds)
                .build();
    }

    @Override
    public Object getResponseBody() {
        // based on aai_get_model_by_2_invariant_ids.json
        // should be refined to match input
        return "" +
                "{" +
                "      \"model\": [" +
                modelInvariantIds.stream().map(modelInvariantId ->
                "        {" +
                "          \"model-invariant-id\": \""+modelInvariantId+"\"," +
                "          \"model-type\": \"resource\"," +
                "          \"resource-version\": \"1507472057666\"," +
                "          \"model-vers\": {" +
                "            \"model-ver\": [" +
                "              {" +
                "                \"model-version-id\": \"7a6ee536-f052-46fa-aa7e-2fca9d674c44\"," +
                "                \"model-name\": \"vf_vEPDG\"," +
                "                \"model-version\": \"2.0\"," +
                "                \"model-description\": \"vEPDG\"," +
                "                \"distribution-status\": \"DISTRIBUTION_COMPLETE_ERROR\"," +
                "                \"resource-version\": \"1507649620979\"," +
                "                \"model-elements\": {" +
                "                  \"model-element\": [" +
                "                    {" +
                "                      \"model-element-uuid\": \"ccbc6dc9-58ad-4082-a81f-630114d99a70\"," +
                "                      \"new-data-del-flag\": \"T\"," +
                "                      \"cardinality\": \"unbounded\"," +
                "                      \"resource-version\": \"1507649620982\"," +
                "                      \"relationship-list\": {" +
                "                        \"relationship\": [" +
                "                          {" +
                "                            \"related-to\": \"model-ver\"," +
                "                            \"related-link\": \"/aai/v11/service-design-and-creation/models/model/acc6edd8-a8d4-4b93-afaa-0994068be14c/model-vers/model-ver/93a6166f-b3d5-4f06-b4ba-aed48d009ad9\"," +
                "                            \"relationship-data\": [" +
                "                              {" +
                "                                \"relationship-key\": \"model.model-invariant-id\"," +
                "                                \"relationship-value\": \"acc6edd8-a8d4-4b93-afaa-0994068be14c\"" +
                "                              }," +
                "                              {" +
                "                                \"relationship-key\": \"model-ver.model-version-id\"," +
                "                                \"relationship-value\": \"93a6166f-b3d5-4f06-b4ba-aed48d009ad9\"" +
                "                              }" +
                "                            ]," +
                "                            \"related-to-property\": [" +
                "                              {" +
                "                                \"property-key\": \"model-ver.model-name\"," +
                "                                \"property-value\": \"generic-vnf\"" +
                "                              }" +
                "                            ]" +
                "                          }" +
                "                        ]" +
                "                      }" +
                "                    }" +
                "                  ]" +
                "                }," +
                "                \"relationship-list\": {" +
                "                  \"relationship\": [" +
                "                    {" +
                "                      \"related-to\": \"model-element\"," +
                "                      \"related-link\": \"/aai/v11/service-design-and-creation/models/model/d661b5fd-e0be-457e-a5ca-e345621af761/model-vers/model-ver/6019af4d-1902-47b1-a7ee-4609c198bf37/model-elements/model-element/8cf71918-6cce-4e01-8049-3f980f8f2b00/model-elements/model-element/4703eeb3-7200-4546-aff2-6e942ac0c0e1\"," +
                "                      \"relationship-data\": [" +
                "                        {" +
                "                          \"relationship-key\": \"model.model-invariant-id\"," +
                "                          \"relationship-value\": \"d661b5fd-e0be-457e-a5ca-e345621af761\"" +
                "                        }," +
                "                        {" +
                "                          \"relationship-key\": \"model-ver.model-version-id\"," +
                "                          \"relationship-value\": \"6019af4d-1902-47b1-a7ee-4609c198bf37\"" +
                "                        }," +
                "                        {" +
                "                          \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                          \"relationship-value\": \"8cf71918-6cce-4e01-8049-3f980f8f2b00\"" +
                "                        }," +
                "                        {" +
                "                          \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                          \"relationship-value\": \"4703eeb3-7200-4546-aff2-6e942ac0c0e1\"" +
                "                        }" +
                "                      ]" +
                "                    }" +
                "                  ]" +
                "                }" +
                "              }," +
                "              {" +
                "                \"model-version-id\": \"f028b2e2-7080-4b13-91b2-94944d4c42d8\"," +
                "                \"model-name\": \"Service with VRF\"," +
                "                \"model-version\": \"5.0\"," +
                "                \"model-description\": \"vEPDG\"," +
                "                \"resource-version\": \"1507472057702\"" +
                "              }," +
                "              {" +
                "                \"model-version-id\": \"9cac02be-2489-4374-888d-2863b4511a59\"," +
                "                \"model-name\": \"VRF Entry Configuration\"," +
                "                \"model-version\": \"5.0\"," +
                "                \"model-description\": \"vEPDG\"," +
                "                \"resource-version\": \"1507472057702\"" +
                "              }," +
                "              {" +
                "                \"model-version-id\": \"network-instance-model-version-id\"," +
                "                \"model-name\": \"Network Entry\"," +
                "                \"model-version\": \"6.0\"," +
                "                \"model-description\": \"vEPDG\"," +
                "                \"resource-version\": \"1507472057702\"" +
                "              }," +
                "              {" +
                "                \"model-version-id\": \"vpn-model-version-id\"," +
                "                \"model-name\": \"vf_vEPDG\"," +
                "                \"model-version\": \"6.0\"," +
                "                \"model-description\": \"vEPDG\"," +
                "                \"resource-version\": \"1507472057702\"" +
                "              }," +
                "              {" +
                "                \"model-version-id\": \"eb5f56bf-5855-4e61-bd00-3e19a953bf02\"," +
                "                \"model-name\": \"vf_vEPDG\"," +
                "                \"model-version\": \"1.0\"," +
                "                \"model-description\": \"vEPDG\"," +
                "                \"resource-version\": \"1507472057702\"," +
                "                \"model-elements\": {" +
                "                  \"model-element\": [" +
                "                    {" +
                "                      \"model-element-uuid\": \"18881687-8dab-4ec8-ab65-ebf8f95a6599\"," +
                "                      \"new-data-del-flag\": \"T\"," +
                "                      \"cardinality\": \"unbounded\"," +
                "                      \"resource-version\": \"1507472057707\"," +
                "                      \"relationship-list\": {" +
                "                        \"relationship\": [" +
                "                          {" +
                "                            \"related-to\": \"model-ver\"," +
                "                            \"related-link\": \"/aai/v11/service-design-and-creation/models/model/acc6edd8-a8d4-4b93-afaa-0994068be14c/model-vers/model-ver/93a6166f-b3d5-4f06-b4ba-aed48d009ad9\"," +
                "                            \"relationship-data\": [" +
                "                              {" +
                "                                \"relationship-key\": \"model.model-invariant-id\"," +
                "                                \"relationship-value\": \"acc6edd8-a8d4-4b93-afaa-0994068be14c\"" +
                "                              }," +
                "                              {" +
                "                                \"relationship-key\": \"model-ver.model-version-id\"," +
                "                                \"relationship-value\": \"93a6166f-b3d5-4f06-b4ba-aed48d009ad9\"" +
                "                              }" +
                "                            ]," +
                "                            \"related-to-property\": [" +
                "                              {" +
                "                                \"property-key\": \"model-ver.model-name\"," +
                "                                \"property-value\": \"generic-vnf\"" +
                "                              }" +
                "                            ]" +
                "                          }" +
                "                        ]" +
                "                      }" +
                "                    }" +
                "                  ]" +
                "                }," +
                "                \"relationship-list\": {" +
                "                  \"relationship\": [" +
                "                    {" +
                "                      \"related-to\": \"model-element\"," +
                "                      \"related-link\": \"/aai/v11/service-design-and-creation/models/model/d661b5fd-e0be-457e-a5ca-e345621af761/model-vers/model-ver/de6f879b-37a9-44a5-97ed-cd52cc14bc47/model-elements/model-element/7a074be4-fcf1-46a5-88ee-41a7c7a6faab/model-elements/model-element/72f0adb5-5294-46f7-8085-a8aee9cb5f49\"," +
                "                      \"relationship-data\": [" +
                "                        {" +
                "                          \"relationship-key\": \"model.model-invariant-id\"," +
                "                          \"relationship-value\": \"d661b5fd-e0be-457e-a5ca-e345621af761\"" +
                "                        }," +
                "                        {" +
                "                          \"relationship-key\": \"model-ver.model-version-id\"," +
                "                          \"relationship-value\": \"de6f879b-37a9-44a5-97ed-cd52cc14bc47\"" +
                "                        }," +
                "                        {" +
                "                          \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                          \"relationship-value\": \"7a074be4-fcf1-46a5-88ee-41a7c7a6faab\"" +
                "                        }," +
                "                        {" +
                "                          \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                          \"relationship-value\": \"72f0adb5-5294-46f7-8085-a8aee9cb5f49\"" +
                "                        }" +
                "                      ]" +
                "                    }" +
                "                  ]" +
                "                }" +
                "              }," +
                "              {" +
                "                \"model-version-id\": \"b7f2e8fb-ac71-4ea0-a801-06ef1479ea84\"," +
                "                \"model-name\": \"vf_vEPDG\"," +
                "                \"model-version\": \"4.0\"," +
                "                \"model-description\": \"vEPDG_Up\"," +
                "                \"resource-version\": \"1508954433176\"," +
                "                \"model-elements\": {" +
                "                  \"model-element\": [" +
                "                    {" +
                "                      \"model-element-uuid\": \"4ed7025a-d37b-444f-8008-5c7c41d76d47\"," +
                "                      \"new-data-del-flag\": \"T\"," +
                "                      \"cardinality\": \"unbounded\"," +
                "                      \"resource-version\": \"1508954433179\"," +
                "                      \"relationship-list\": {" +
                "                        \"relationship\": [" +
                "                          {" +
                "                            \"related-to\": \"model-ver\"," +
                "                            \"related-link\": \"/aai/v11/service-design-and-creation/models/model/acc6edd8-a8d4-4b93-afaa-0994068be14c/model-vers/model-ver/93a6166f-b3d5-4f06-b4ba-aed48d009ad9\"," +
                "                            \"relationship-data\": [" +
                "                              {" +
                "                                \"relationship-key\": \"model.model-invariant-id\"," +
                "                                \"relationship-value\": \"acc6edd8-a8d4-4b93-afaa-0994068be14c\"" +
                "                              }," +
                "                              {" +
                "                                \"relationship-key\": \"model-ver.model-version-id\"," +
                "                                \"relationship-value\": \"93a6166f-b3d5-4f06-b4ba-aed48d009ad9\"" +
                "                              }" +
                "                            ]," +
                "                            \"related-to-property\": [" +
                "                              {" +
                "                                \"property-key\": \"model-ver.model-name\"," +
                "                                \"property-value\": \"generic-vnf\"" +
                "                              }" +
                "                            ]" +
                "                          }" +
                "                        ]" +
                "                      }" +
                "                    }" +
                "                  ]" +
                "                }," +
                "                \"relationship-list\": {" +
                "                  \"relationship\": [" +
                "                    {" +
                "                      \"related-to\": \"model-element\"," +
                "                      \"related-link\": \"/aai/v11/service-design-and-creation/models/model/d661b5fd-e0be-457e-a5ca-e345621af761/model-vers/model-ver/0e3f3390-48dd-4640-aed7-0eb873d40a97/model-elements/model-element/106d11a5-2b8c-4586-a56d-ee387e822757/model-elements/model-element/3d7e724a-6f05-4111-8b22-78d024c53081\"," +
                "                      \"relationship-data\": [" +
                "                        {" +
                "                          \"relationship-key\": \"model.model-invariant-id\"," +
                "                          \"relationship-value\": \"d661b5fd-e0be-457e-a5ca-e345621af761\"" +
                "                        }," +
                "                        {" +
                "                          \"relationship-key\": \"model-ver.model-version-id\"," +
                "                          \"relationship-value\": \"0e3f3390-48dd-4640-aed7-0eb873d40a97\"" +
                "                        }," +
                "                        {" +
                "                          \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                          \"relationship-value\": \"106d11a5-2b8c-4586-a56d-ee387e822757\"" +
                "                        }," +
                "                        {" +
                "                          \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                          \"relationship-value\": \"3d7e724a-6f05-4111-8b22-78d024c53081\"" +
                "                        }" +
                "                      ]" +
                "                    }" +
                "                  ]" +
                "                }" +
                "              }," +
                "              {" +
                "                \"model-version-id\": \"4117a0b6-e234-467d-b5b9-fe2f68c8b0fc\"," +
                "                \"model-name\": \"Grouping Service for Test\"," +
                "                \"model-version\": \"1.0\"," +
                "                \"model-description\": \"vEPDG_Up\"," +
                "                \"resource-version\": \"1508954433176\"," +
                "                \"model-elements\": {" +
                "                  \"model-element\": [" +
                "                    {" +
                "                      \"model-element-uuid\": \"4ed7025a-d37b-444f-8008-5c7c41d76d47\"," +
                "                      \"new-data-del-flag\": \"T\"," +
                "                      \"cardinality\": \"unbounded\"," +
                "                      \"resource-version\": \"1508954433179\"," +
                "                      \"relationship-list\": {" +
                "                        \"relationship\": [" +
                "                          {" +
                "                            \"related-to\": \"model-ver\"," +
                "                            \"related-link\": \"/aai/v11/service-design-and-creation/models/model/acc6edd8-a8d4-4b93-afaa-0994068be14c/model-vers/model-ver/93a6166f-b3d5-4f06-b4ba-aed48d009ad9\"," +
                "                            \"relationship-data\": [" +
                "                              {" +
                "                                \"relationship-key\": \"model.model-invariant-id\"," +
                "                                \"relationship-value\": \"acc6edd8-a8d4-4b93-afaa-0994068be14c\"" +
                "                              }," +
                "                              {" +
                "                                \"relationship-key\": \"model-ver.model-version-id\"," +
                "                                \"relationship-value\": \"93a6166f-b3d5-4f06-b4ba-aed48d009ad9\"" +
                "                              }" +
                "                            ]," +
                "                            \"related-to-property\": [" +
                "                              {" +
                "                                \"property-key\": \"model-ver.model-name\"," +
                "                                \"property-value\": \"generic-vnf\"" +
                "                              }" +
                "                            ]" +
                "                          }" +
                "                        ]" +
                "                      }" +
                "                    }" +
                "                  ]" +
                "                }," +
                "                \"relationship-list\": {" +
                "                  \"relationship\": [" +
                "                    {" +
                "                      \"related-to\": \"model-element\"," +
                "                      \"related-link\": \"/aai/v11/service-design-and-creation/models/model/d661b5fd-e0be-457e-a5ca-e345621af761/model-vers/model-ver/0e3f3390-48dd-4640-aed7-0eb873d40a97/model-elements/model-element/106d11a5-2b8c-4586-a56d-ee387e822757/model-elements/model-element/3d7e724a-6f05-4111-8b22-78d024c53081\"," +
                "                      \"relationship-data\": [" +
                "                        {" +
                "                          \"relationship-key\": \"model.model-invariant-id\"," +
                "                          \"relationship-value\": \"d661b5fd-e0be-457e-a5ca-e345621af761\"" +
                "                        }," +
                "                        {" +
                "                          \"relationship-key\": \"model-ver.model-version-id\"," +
                "                          \"relationship-value\": \"0e3f3390-48dd-4640-aed7-0eb873d40a97\"" +
                "                        }," +
                "                        {" +
                "                          \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                          \"relationship-value\": \"106d11a5-2b8c-4586-a56d-ee387e822757\"" +
                "                        }," +
                "                        {" +
                "                          \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                          \"relationship-value\": \"3d7e724a-6f05-4111-8b22-78d024c53081\"" +
                "                        }" +
                "                      ]" +
                "                    }" +
                "                  ]" +
                "                }" +
                "              }," +
                "              {" +
                "                \"model-version-id\": \"6e59c5de-f052-46fa-aa7e-2fca9d674c44\"," +
                "                \"model-name\": \"vf_vEPDG\"," +
                "                \"model-version\": \"5.0\"," +
                "                \"model-description\": \"vEPDG_Up\"," +
                "                \"resource-version\": \"1509570916147\"," +
                "                \"model-elements\": {" +
                "                  \"model-element\": [" +
                "                    {" +
                "                      \"model-element-uuid\": \"49899999-076d-456b-915c-078d1b2a05b3\"," +
                "                      \"new-data-del-flag\": \"T\"," +
                "                      \"cardinality\": \"unbounded\"," +
                "                      \"resource-version\": \"1509570916150\"," +
                "                      \"relationship-list\": {" +
                "                        \"relationship\": [" +
                "                          {" +
                "                            \"related-to\": \"model-ver\"," +
                "                            \"related-link\": \"/aai/v11/service-design-and-creation/models/model/acc6edd8-a8d4-4b93-afaa-0994068be14c/model-vers/model-ver/93a6166f-b3d5-4f06-b4ba-aed48d009ad9\"," +
                "                            \"relationship-data\": [" +
                "                              {" +
                "                                \"relationship-key\": \"model.model-invariant-id\"," +
                "                                \"relationship-value\": \"acc6edd8-a8d4-4b93-afaa-0994068be14c\"" +
                "                              }," +
                "                              {" +
                "                                \"relationship-key\": \"model-ver.model-version-id\"," +
                "                                \"relationship-value\": \"93a6166f-b3d5-4f06-b4ba-aed48d009ad9\"" +
                "                              }" +
                "                            ]," +
                "                            \"related-to-property\": [" +
                "                              {" +
                "                                \"property-key\": \"model-ver.model-name\"," +
                "                                \"property-value\": \"generic-vnf\"" +
                "                              }" +
                "                            ]" +
                "                          }" +
                "                        ]" +
                "                      }" +
                "                    }" +
                "                  ]" +
                "                }," +
                "                \"relationship-list\": {" +
                "                  \"relationship\": [" +
                "                    {" +
                "                      \"related-to\": \"model-element\"," +
                "                      \"related-link\": \"/aai/v11/service-design-and-creation/models/model/d661b5fd-e0be-457e-a5ca-e345621af761/model-vers/model-ver/03655348-0148-4482-b0b7-a5de71ab6264/model-elements/model-element/e97f008f-fc19-4b6e-8842-e54ec6eafb6e/model-elements/model-element/cfc574f2-6869-41d3-bfae-42b54b711568\"," +
                "                      \"relationship-data\": [" +
                "                        {" +
                "                          \"relationship-key\": \"model.model-invariant-id\"," +
                "                          \"relationship-value\": \"d661b5fd-e0be-457e-a5ca-e345621af761\"" +
                "                        }," +
                "                        {" +
                "                          \"relationship-key\": \"model-ver.model-version-id\"," +
                "                          \"relationship-value\": \"03655348-0148-4482-b0b7-a5de71ab6264\"" +
                "                        }," +
                "                        {" +
                "                          \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                          \"relationship-value\": \"e97f008f-fc19-4b6e-8842-e54ec6eafb6e\"" +
                "                        }," +
                "                        {" +
                "                          \"relationship-key\": \"model-element.model-element-uuid\"," +
                "                          \"relationship-value\": \"cfc574f2-6869-41d3-bfae-42b54b711568\"" +
                "                        }" +
                "                      ]" +
                "                    }" +
                "                  ]" +
                "                }" +
                "              }" +
                "            ]" +
                "          }" +
                "        }").collect(joining(", ")) +
                "      ]" +
                "    }";
    }
}
