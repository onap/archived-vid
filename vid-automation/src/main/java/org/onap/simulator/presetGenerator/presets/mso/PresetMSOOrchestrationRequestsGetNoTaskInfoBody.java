package org.onap.simulator.presetGenerator.presets.mso;

import static java.util.Collections.singletonList;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

public class PresetMSOOrchestrationRequestsGetNoTaskInfoBody extends BaseMSOPreset {

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/orchestrationRequests/v.";
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of(
            "format", singletonList("simpleNoTaskInfo"),
            "filter", singletonList("modelType:EQUALS:vnf")
           );
    }

    @Override
    public Object getResponseBody() {
        return "" +
           "{"
            + "    \"requestList\": ["
            + "      {"
            + "        \"request\": {"
            + "          \"requestId\": \"f01fbb5d-c964-44d4-9080-00e83eae419f\","
            + "          \"startTime\": \"Mon, 05 Aug 2019 12:49:35 GMT\","
            + "          \"finishTime\": \"Mon, 05 Aug 2019 12:50:24 GMT\","
            + "          \"requestScope\": \"vnf\","
            + "          \"requestType\": \"updateInstance\","
            + "          \"requestDetails\": {"
            + "            \"modelInfo\": {"
            + "              \"modelCustomizationName\": \"FEXN_5G_NC_VSP 0\","
            + "              \"modelInvariantId\": \"5fc21c6e-3ca3-4641-90f0-1e2ae66b20be\","
            + "              \"modelType\": \"vnf\","
            + "              \"modelName\": \"FEXN_5G_NC_VSP\","
            + "              \"modelVersion\": \"2\","
            + "              \"modelCustomizationUuid\": \"2ba4f9f3-765f-4172-802e-a56a912c221c\","
            + "              \"modelVersionId\": \"9ba01811-84ea-4878-9fd7-7a0a280e0572\","
            + "              \"modelCustomizationId\": \"2ba4f9f3-765f-4172-802e-a56a912c221c\","
            + "              \"modelUuid\": \"9ba01811-84ea-4878-9fd7-7a0a280e0572\","
            + "              \"modelInvariantUuid\": \"5fc21c6e-3ca3-4641-90f0-1e2ae66b20be\","
            + "              \"modelInstanceName\": \"FEXN_5G_NC_VSP 0\""
            + "            },"
            + "            \"requestInfo\": {"
            + "              \"productFamilyId\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\","
            + "              \"source\": \"VID\","
            + "              \"instanceName\": \"PST-VNF-1810-None-4751\","
            + "              \"suppressRollback\": false,"
            + "              \"requestorId\": \"yy3692\""
            + "            },"
            + "            \"relatedInstanceList\": ["
            + "              {"
            + "                \"relatedInstance\": {"
            + "                  \"instanceId\": \"a06c0d0b-ae17-42fb-b9b5-4b3a93a7be11\","
            + "                  \"modelInfo\": {"
            + "                    \"modelInvariantId\": \"abb333ce-c66a-4670-b44a-17fe6a4963f3\","
            + "                    \"modelType\": \"service\","
            + "                    \"modelName\": \"test_VNF_01_by5924\","
            + "                    \"modelVersion\": \"2\","
            + "                    \"modelVersionId\": \"6c756dd3-ffa5-4162-b1a8-501e4a643707\","
            + "                    \"modelUuid\": \"6c756dd3-ffa5-4162-b1a8-501e4a643707\","
            + "                    \"modelInvariantUuid\": \"abb333ce-c66a-4670-b44a-17fe6a4963f3\""
            + "                  }"
            + "                }"
            + "              }"
            + "            ],"
            + "            \"cloudConfiguration\": {"
            + "              \"tenantId\": \"1c099363f335409ea47df3c8db61397a\","
            + "              \"cloudOwner\": \"irma-aic\","
            + "              \"lcpCloudRegionId\": \"mdt19b\""
            + "            },"
            + "            \"requestParameters\": {"
            + "              \"aLaCarte\": true,"
            + "              \"testApi\": \"GR_API\""
            + "            },"
            + "            \"platform\": {"
            + "              \"platformName\": \"None Platform - Collab\""
            + "            },"
            + "            \"lineOfBusiness\": {"
            + "              \"lineOfBusinessName\": \"None_LOB - Collab\""
            + "            }"
            + "          },"
            + "          \"instanceReferences\": {"
            + "            \"serviceInstanceId\": \"a06c0d0b-ae17-42fb-b9b5-4b3a93a7be11\","
            + "            \"vnfInstanceId\": \"569cd880-e095-466a-b97d-97bb46fea257\","
            + "            \"vnfInstanceName\": \"PST-VNF-1810-None-4751\","
            + "            \"requestorId\": \"yy3692\""
            + "          },"
            + "          \"requestStatus\": {"
            + "            \"requestState\": \"ROLLED_BACK\","
            + "            \"statusMessage\": \"STATUS: Error Source: SDNC, Error Message: Unable to find l3-network in AAI for network role sgi_direct_net_2 FLOW STATUS: All Rollback flows have completed successfully ROLLBACK STATUS: Rollback has been completed successfully.\","
            + "            \"percentProgress\": 100,"
            + "            \"timestamp\": \"Mon, 05 Aug 2019 12:50:24 GMT\""
            + "          }"
            + "        }"
            + "      },"
            + "      {"
            + "        \"request\": {"
            + "          \"requestId\": \"64970886-ed75-4837-8bf1-0eb472fe65e6\","
            + "          \"startTime\": \"Fri, 06 Mar 2020 23:07:48 GMT\","
            + "          \"finishTime\": \"Fri, 06 Mar 2020 23:08:40 GMT\","
            + "          \"requestScope\": \"vnf\","
            + "          \"requestType\": \"replaceInstance\","
            + "          \"requestDetails\": {"
            + "            \"modelInfo\": {"
            + "              \"modelCustomizationName\": \"L3VPNvRR-RESOURCE 0\","
            + "              \"modelInvariantId\": \"fdb84cd2-87d8-4b22-bf47-b3ef765d2c11\","
            + "              \"modelType\": \"vnf\","
            + "              \"modelNameVersionId\": \"fdb84cd2-87d8-4b22-bf47-b3ef765d2c11\","
            + "              \"modelName\": \"L3VPNvRR-RESOURCE\","
            + "              \"modelVersion\": \"1\","
            + "              \"modelInvariantUuid\": \"fdb84cd2-87d8-4b22-bf47-b3ef765d2c11\","
            + "              \"modelInstanceName\": \"L3VPNvRR-RESOURCE 0\""
            + "            },"
            + "            \"requestInfo\": {"
            + "              \"source\": \"VID\","
            + "              \"suppressRollback\": false,"
            + "              \"requestorId\": \"ROBOT\""
            + "            },"
            + "            \"cloudConfiguration\": {"
            + "              \"tenantId\": \"78491aac74be4fab9873db114774b475\","
            + "              \"cloudOwner\": \"irma-aic\","
            + "              \"lcpCloudRegionId\": \"dyh2b\""
            + "            },"
            + "            \"requestParameters\": {"
            + "              \"testApi\": \"GR_API\""
            + "            }"
            + "          },"
            + "          \"instanceReferences\": {"
            + "            \"serviceInstanceId\": \"e648ee86-c091-4d38-8573-99893cd79bfa\","
            + "            \"vnfInstanceId\": \"9a2b3dd1-5fd9-4cb4-a47f-b4f0e8e678e3\","
            + "            \"vnfInstanceName\": \"PST-VNF-2002-None-943501\","
            + "            \"requestorId\": \"ROBOT\""
            + "          },"
            + "          \"requestStatus\": {"
            + "            \"requestState\": \"COMPLETE\","
            + "            \"statusMessage\": \"STATUS: ALaCarte-Vnf-deleteInstance request was executed correctly. FLOW STATUS: Successfully completed all Building Blocks\","
            + "            \"percentProgress\": 100,"
            + "            \"timestamp\": \"Fri, 06 Mar 2020 23:08:40 GMT\""
            + "          }"
            + "        }"
            + "      }"
            + "    ]"
            + "}";
        
    }
}
