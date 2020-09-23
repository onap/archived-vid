package org.onap.simulator.presetGenerator.presets.mso;

import static java.util.Collections.singletonList;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

public class PresetMSOOrchestrationRequestsGetByRequestIdNew extends BaseMSOPreset {

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
        return ImmutableMap.of("filter", singletonList("requestId:EQUALS:7ba7900c-3e51-4d87-b1b4-3c53bdfaaa7d"),"format",singletonList("statusDetail"));
    }

    @Override
    public Object getResponseBody() {
        return "{\n"
            + "    \"requestList\": [\n"
            + "        {\n"
            + "            \"request\": {\n"
            + "                \"requestId\": \"7ba7900c-3e51-4d87-b1b4-3c53bdfaaa7d\",\n"
            + "                \"startTime\": \"Mon, 24 Aug 2020 22:37:53 GMT\",\n"
            + "                \"finishTime\": \"Mon, 24 Aug 2020 22:38:10 GMT\",\n"
            + "                \"requestScope\": \"service\",\n"
            + "                \"requestType\": \"createInstance\",\n"
            + "                \"requestDetails\": {\n"
            + "                    \"modelInfo\": {\n"
            + "                        \"modelInvariantId\": \"2da904be-d12b-455c-8951-59ec7d207371\",\n"
            + "                        \"modelType\": \"service\",\n"
            + "                        \"modelName\": \"FMGW-NC2-507-SVC\",\n"
            + "                        \"modelVersion\": \"12.0\",\n"
            + "                        \"modelVersionId\": \"c40d56a6-310c-4db9-8455-0aa723d36d53\",\n"
            + "                        \"modelUuid\": \"c40d56a6-310c-4db9-8455-0aa723d36d53\",\n"
            + "                        \"modelInvariantUuid\": \"2da904be-d12b-455c-8951-59ec7d207371\"\n"
            + "                    },\n"
            + "                    \"requestInfo\": {\n"
            + "                        \"source\": \"VID\",\n"
            + "                        \"instanceName\": \"zrdm54cfmgw01_svc\",\n"
            + "                        \"suppressRollback\": false,\n"
            + "                        \"requestorId\": \"cb4449\"\n"
            + "                    },\n"
            + "                    \"subscriberInfo\": {\n"
            + "                        \"globalSubscriberId\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"\n"
            + "                    },\n"
            + "                    \"requestParameters\": {\n"
            + "                        \"subscriptionServiceType\": \"FIRSTNET\",\n"
            + "                        \"aLaCarte\": true,\n"
            + "                        \"testApi\": \"GR_API\"\n"
            + "                    },\n"
            + "                    \"project\": {\n"
            + "                        \"projectName\": \"FIRSTNET\"\n"
            + "                    },\n"
            + "                    \"owningEntity\": {\n"
            + "                        \"owningEntityId\": \"10c645f5-9924-4b89-bec0-b17cf49d3cad\",\n"
            + "                        \"owningEntityName\": \"MOBILITY-CORE\"\n"
            + "                    }\n"
            + "                },\n"
            + "                \"instanceReferences\": {\n"
            + "                    \"serviceInstanceId\": \"937d9e51-03b9-416b-bccd-aa898a85d711\",\n"
            + "                    \"serviceInstanceName\": \"zrdm54cfmgw01_svc\"\n"
            + "                },\n"
            + "                \"requestStatus\": {\n"
            + "                    \"requestState\": \"COMPLETE\",\n"
            + "                    \"statusMessage\": \"STATUS: ALaCarte-Service-createInstance request was executed correctly.\",\n"
            + "                    \"percentProgress\": 100,\n"
            + "                    \"timestamp\": \"Mon, 24 Aug 2020 22:38:10 GMT\",\n"
            + "                    \"flowStatus\": \"Successfully completed all Building Blocks\"\n"
            + "                },\n"
            + "                \"requestProcessingData\": [\n"
            + "                    {}\n"
            + "                ]\n"
            + "            }\n"
            + "        }\n"
            + "    ]\n"
            + "}";
        
        
    }
}
