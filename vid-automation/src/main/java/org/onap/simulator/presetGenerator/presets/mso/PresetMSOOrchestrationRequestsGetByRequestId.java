package org.onap.simulator.presetGenerator.presets.mso;

import static java.util.Collections.singletonList;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

public class PresetMSOOrchestrationRequestsGetByRequestId extends BaseMSOPreset {

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
        return ImmutableMap.of("filter", singletonList("requestId:EQUALS:405652f4-ceb3-4a75-9474-8aea71480a77"));
    }

    @Override
    public Object getResponseBody() {
        return "" +
                "{ " +
                " \"requestList\": [{ " +
                "  \"request\": {" +
                "    \"requestId\": \"405652f4-ceb3-4a75-9474-8aea71480a77\"," +
                "    \"startTime\": \"Mon, 13 Aug 2018 18:13:28 GMT\"," +
                "    \"requestScope\": \"service\"," +
                "    \"requestType\": \"createInstance\"," +
                "    \"requestDetails\": {" +
                "      \"modelInfo\": {" +
                "        \"modelInvariantId\": \"c42c7d13-435e-4a38-84e5-158972673ff2\"," +
                "        \"modelType\": \"service\"," +
                "        \"modelName\": \"CGWY27-SVC\"," +
                "        \"modelVersion\": \"1.0\"," +
                "        \"modelVersionId\": \"c0e6858a-e467-412c-9766-3872f03ac0ab\"," +
                "        \"modelUuid\": \"c0e6858a-e467-412c-9766-3872f03ac0ab\"," +
                "        \"modelInvariantUuid\": \"c42c7d13-435e-4a38-84e5-158972673ff2\"" +
                "      }," +
                "      \"requestInfo\": {" +
                "        \"source\": \"VID\"," +
                "        \"suppressRollback\": false," +
                "        \"requestorId\": \"cb4449\"" +
                "      }," +
                "      \"subscriberInfo\": {" +
                "        \"globalSubscriberId\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"" +
                "      }," +
                "      \"requestParameters\": {" +
                "        \"subscriptionServiceType\": \"Kennedy\"," +
                "        \"aLaCarte\": true," +
                "        \"testApi\": \"GR_API\"" +
                "      }," +
                "      \"project\": {" +
                "        \"projectName\": \"Kennedy\"" +
                "      }," +
                "      \"owningEntity\": {" +
                "        \"owningEntityId\": \"10c645f5-9924-4b89-bec0-b17cf49d3cad\"," +
                "        \"owningEntityName\": \"EMANUEL-CORE\"" +
                "      }" +
                "    }," +
                "    \"instanceReferences\": {" +
                "      \"requestorId\": \"cb4449\"" +
                "    }," +
                "    \"requestStatus\": {" +
                "      \"requestState\": \"FAILED\"," +
                "      \"statusMessage\": \"Service Instance was failed.\"," +
                "      \"percentProgress\": 100," +
                "      \"finishTime\": \"Mon, 13 Aug 2018 18:13:39 GMT\"" +
                "    }" +
                "  }" +
                "}" +
                " ] " +
                "} ";
        
        
    }
}
