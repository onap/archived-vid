package org.onap.simulator.presetGenerator.presets.mso;

import static java.util.Collections.singletonList;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

public class PresetMSOOrchestrationRequestsGetSlimBody extends BaseMSOPreset {

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
        return ImmutableMap.of( "format", singletonList("simpleNoTaskInfo"),
            "filter", singletonList("modelType:EQUALS:vnf")
           );
    }

    @Override
    public Object getResponseBody() {
        return "" +
        "{\n"
            + "  \"requestList\": [\n"
            + "    {\n"
            + "      \"request\": {\n"
            + "        \"requestId\": \"f01fbb5d-c964-44d4-9080-00e83eae419f\",\n"
            + "        \"startTime\": \"Mon, 05 Aug 2019 12:49:35 GMT\",\n"
            + "        \"finishTime\": \"Mon, 05 Aug 2019 12:50:24 GMT\",\n"
            + "        \"requestScope\": \"vnf\",\n"
            + "        \"requestType\": \"updateInstance\",\n"
            + "        \"requestDetails\": {\n"
            + "          \"modelInfo\": {\n"
            + "            \"modelCustomizationName\": \"FEXN_5G_NC_VSP 0\",\n"
            + "            \"modelInvariantId\": \"5fc21c6e-3ca3-4641-90f0-1e2ae66b20be\",\n"
            + "            \"modelType\": \"vnf\",\n"
            + "            \"modelName\": \"FEXN_5G_NC_VSP\",\n"
            + "            \"modelVersion\": \"2\",\n"
            + "            \"modelCustomizationUuid\": \"2ba4f9f3-765f-4172-802e-a56a912c221c\",\n"
            + "            \"modelVersionId\": \"9ba01811-84ea-4878-9fd7-7a0a280e0572\",\n"
            + "            \"modelCustomizationId\": \"2ba4f9f3-765f-4172-802e-a56a912c221c\",\n"
            + "            \"modelUuid\": \"9ba01811-84ea-4878-9fd7-7a0a280e0572\",\n"
            + "            \"modelInvariantUuid\": \"5fc21c6e-3ca3-4641-90f0-1e2ae66b20be\",\n"
            + "            \"modelInstanceName\": \"FEXN_5G_NC_VSP 0\"\n"
            + "          },\n"
            + "          \"requestInfo\": {\n"
            + "            \"productFamilyId\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\",\n"
            + "            \"source\": \"VID\",\n"
            + "            \"instanceName\": \"PST-VNF-1810-None-4751\",\n"
            + "            \"suppressRollback\": false,\n"
            + "            \"requestorId\": \"yy3692\"\n"
            + "          },\n"
            + "          \"relatedInstanceList\": [\n"
            + "            {\n"
            + "              \"relatedInstance\": {\n"
            + "                \"instanceId\": \"a06c0d0b-ae17-42fb-b9b5-4b3a93a7be11\",\n"
            + "                \"modelInfo\": {\n"
            + "                  \"modelInvariantId\": \"abb333ce-c66a-4670-b44a-17fe6a4963f3\",\n"
            + "                  \"modelType\": \"service\",\n"
            + "                  \"modelName\": \"test_VNF_01_by5924\",\n"
            + "                  \"modelVersion\": \"2\",\n"
            + "                  \"modelVersionId\": \"6c756dd3-ffa5-4162-b1a8-501e4a643707\",\n"
            + "                  \"modelUuid\": \"6c756dd3-ffa5-4162-b1a8-501e4a643707\",\n"
            + "                  \"modelInvariantUuid\": \"abb333ce-c66a-4670-b44a-17fe6a4963f3\"\n"
            + "                }\n"
            + "              }\n"
            + "            }\n"
            + "          ],\n"
            + "          \"cloudConfiguration\": {\n"
            + "            \"tenantId\": \"1c099363f335409ea47df3c8db61397a\",\n"
            + "            \"cloudOwner\": \"att-aic\",\n"
            + "            \"lcpCloudRegionId\": \"mdt19b\"\n"
            + "          },\n"
            + "          \"requestParameters\": {\n"
            + "            \"aLaCarte\": true,\n"
            + "            \"testApi\": \"GR_API\"\n"
            + "          },\n"
            + "          \"platform\": {\n"
            + "            \"platformName\": \"None Platform - Collab\"\n"
            + "          },\n"
            + "          \"lineOfBusiness\": {\n"
            + "            \"lineOfBusinessName\": \"None_LOB - Collab\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"instanceReferences\": {\n"
            + "          \"serviceInstanceId\": \"a06c0d0b-ae17-42fb-b9b5-4b3a93a7be11\",\n"
            + "          \"vnfInstanceId\": \"569cd880-e095-466a-b97d-97bb46fea257\",\n"
            + "          \"vnfInstanceName\": \"PST-VNF-1810-None-4751\",\n"
            + "          \"requestorId\": \"yy3692\"\n"
            + "        },\n"
            + "        \"requestStatus\": {\n"
            + "          \"requestState\": \"ROLLED_BACK\",\n"
            + "          \"statusMessage\": \"STATUS: Error Source: SDNC, Error Message: Unable to find l3-network in AAI for network role sgi_direct_net_2 FLOW STATUS: All Rollback flows have completed successfully ROLLBACK STATUS: Rollback has been completed successfully.\",\n"
            + "          \"percentProgress\": 100,\n"
            + "          \"timestamp\": \"Mon, 05 Aug 2019 12:50:24 GMT\"\n"
            + "        }\n"
            + "      }\n"
            + "    },\n"
            + "    {\n"
            + "      \"request\": {\n"
            + "        \"requestId\": \"64970886-ed75-4837-8bf1-0eb472fe65e6\",\n"
            + "        \"startTime\": \"Fri, 06 Mar 2020 23:07:48 GMT\",\n"
            + "        \"finishTime\": \"Fri, 06 Mar 2020 23:08:40 GMT\",\n"
            + "        \"requestScope\": \"vnf\",\n"
            + "        \"requestType\": \"replaceInstance\",\n"
            + "        \"requestDetails\": {\n"
            + "          \"modelInfo\": {\n"
            + "            \"modelCustomizationName\": \"L3VPNvRR-RESOURCE 0\",\n"
            + "            \"modelInvariantId\": \"fdb84cd2-87d8-4b22-bf47-b3ef765d2c11\",\n"
            + "            \"modelType\": \"vnf\",\n"
            + "            \"modelNameVersionId\": \"fdb84cd2-87d8-4b22-bf47-b3ef765d2c11\",\n"
            + "            \"modelName\": \"L3VPNvRR-RESOURCE\",\n"
            + "            \"modelVersion\": \"1\",\n"
            + "            \"modelInvariantUuid\": \"fdb84cd2-87d8-4b22-bf47-b3ef765d2c11\",\n"
            + "            \"modelInstanceName\": \"L3VPNvRR-RESOURCE 0\"\n"
            + "          },\n"
            + "          \"requestInfo\": {\n"
            + "            \"source\": \"VID\",\n"
            + "            \"suppressRollback\": false,\n"
            + "            \"requestorId\": \"ROBOT\"\n"
            + "          },\n"
            + "          \"cloudConfiguration\": {\n"
            + "            \"tenantId\": \"78491aac74be4fab9873db114774b475\",\n"
            + "            \"cloudOwner\": \"att-aic\",\n"
            + "            \"lcpCloudRegionId\": \"dyh2b\"\n"
            + "          },\n"
            + "          \"requestParameters\": {\n"
            + "            \"testApi\": \"GR_API\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"instanceReferences\": {\n"
            + "          \"serviceInstanceId\": \"e648ee86-c091-4d38-8573-99893cd79bfa\",\n"
            + "          \"vnfInstanceId\": \"9a2b3dd1-5fd9-4cb4-a47f-b4f0e8e678e3\",\n"
            + "          \"vnfInstanceName\": \"PST-VNF-2002-None-943501\",\n"
            + "          \"requestorId\": \"ROBOT\"\n"
            + "        },\n"
            + "        \"requestStatus\": {\n"
            + "          \"requestState\": \"COMPLETE\",\n"
            + "          \"statusMessage\": \"STATUS: ALaCarte-Vnf-deleteInstance request was executed correctly. FLOW STATUS: Successfully completed all Building Blocks\",\n"
            + "          \"percentProgress\": 100,\n"
            + "          \"timestamp\": \"Fri, 06 Mar 2020 23:08:40 GMT\"\n"
            + "        }\n"
            + "      }\n"
            + "    }\n"
            + "  ]\n"
            + "}";
        
    }
}
