package org.onap.simulator.presetGenerator.presets.mso;

import static java.util.Collections.singletonList;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

public class PresetMSOOrchestrationRequestsGetByServiceInstanceId extends BaseMSOPreset {

    private final String instanceId;

    public PresetMSOOrchestrationRequestsGetByServiceInstanceId() {
        this.instanceId = "bc305d54-75b4-431b-adb2-eb6b9e546014";
    }

    public PresetMSOOrchestrationRequestsGetByServiceInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

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
        return ImmutableMap.of("filter", singletonList("serviceInstanceId:EQUALS:" + instanceId));
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
                "        \"instanceName\": \"instance name 1\"," +
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
                "      \"serviceInstanceId\": \"64f3123a-f9a8-4591-b481-d662134bcb52\"," +
                "      \"serviceInstanceName\": \"CGWY27-SVC-olson5b\"," +
                "      \"requestorId\": \"cb4449\"" +
                "    }," +
                "    \"requestStatus\": {" +
                "      \"requestState\": \"COMPLETE\"," +
                "      \"statusMessage\": \"Service Instance was created successfully.\"," +
                "      \"percentProgress\": 100," +
                "      \"finishTime\": \"Mon, 13 Aug 2018 18:13:39 GMT\"" +
                "    }" +
                "  }" +
                "},{" +
                "  \"request\":{" +
                "    \"requestId\":\"688d40cd-6bfd-4a4b-95f4-5e4ffa6d6fc5\"," +
                "    \"startTime\":\"Mon, 13 Aug 2018 18:15:14 GMT\"," +
                "    \"requestScope\":\"vnf\"," +
                "    \"requestType\":\"createInstance\"," +
                "    \"requestDetails\":{" +
                "      \"modelInfo\":{" +
                "        \"modelCustomizationName\":\"CGWY27-VF 0\"," +
                "        \"modelInvariantId\":\"8f508753-a546-48be-b931-2c949e215972\"," +
                "        \"modelType\":\"vnf\"," +
                "        \"modelName\":\"CGWY27-VF\"," +
                "        \"modelVersion\":\"1.0\"," +
                "        \"modelCustomizationUuid\":\"fa35ceb6-38de-428a-93b2-89be64c19f86\"," +
                "        \"modelVersionId\":\"4d279e16-de09-4108-b32f-82b05df2f41a\"," +
                "        \"modelCustomizationId\":\"fa35ceb6-38de-428a-93b2-89be64c19f86\"," +
                "        \"modelUuid\":\"4d279e16-de09-4108-b32f-82b05df2f41a\"," +
                "        \"modelInvariantUuid\":\"8f508753-a546-48be-b931-2c949e215972\"," +
                "        \"modelInstanceName\":\"CGWY27-VF 0\"" +
                "      }," +
                "      \"requestInfo\":{" +
                "        \"productFamilyId\":\"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"," +
                "        \"source\":\"VID\"," +
                "        \"instanceName\":\"instance name 2\"," +
                "        \"suppressRollback\":false," +
                "        \"requestorId\":\"cb4449\"" +
                "      }," +
                "      \"relatedInstanceList\":[" +
                "        {" +
                "          \"relatedInstance\":{" +
                "            \"instanceId\":\"64f3123a-f9a8-4591-b481-d662134bcb52\"," +
                "            \"modelInfo\":{" +
                "              \"modelInvariantId\":\"c42c7d13-435e-4a38-84e5-158972673ff2\"," +
                "              \"modelType\":\"service\"," +
                "              \"modelName\":\"CGWY27-SVC\"," +
                "              \"modelVersion\":\"1.0\"," +
                "              \"modelVersionId\":\"c0e6858a-e467-412c-9766-3872f03ac0ab\"," +
                "              \"modelUuid\":\"c0e6858a-e467-412c-9766-3872f03ac0ab\"," +
                "              \"modelInvariantUuid\":\"c42c7d13-435e-4a38-84e5-158972673ff2\"" +
                "            }" +
                "          }" +
                "        }" +
                "      ]," +
                "      \"cloudConfiguration\":{" +
                "        \"tenantId\":\"8830e9086a0f40cc9cf868e792602c4d\"," +
                "        \"lcpCloudRegionId\":\"olson5b\"" +
                "      }," +
                "      \"requestParameters\":{" +
                "        \"testApi\":\"GR_API\"" +
                "      }," +
                "      \"platform\":{" +
                "        \"platformName\":\"AIC\"" +
                "      }," +
                "      \"lineOfBusiness\":{" +
                "        \"lineOfBusinessName\":\"EMANUEL-CONSUMER\"" +
                "      }" +
                "    }," +
                "    \"instanceReferences\":{" +
                "      \"serviceInstanceId\":\"64f3123a-f9a8-4591-b481-d662134bcb52\"," +
                "      \"vnfInstanceId\":\"96c98f10-d20a-47a9-a790-94e3ac3dfb7e\"," +
                "      \"vnfInstanceName\":\"zolson5bcgwy22\"," +
                "      \"requestorId\":\"cb4449\"" +
                "    }," +
                "    \"requestStatus\":{" +
                "      \"requestState\":\"COMPLETE\"," +
                "      \"statusMessage\":\"Vnf has been created successfully.\"," +
                "      \"percentProgress\":100," +
                "      \"finishTime\":\"Mon, 13 Aug 2018 18:15:23 GMT\"" +
                "    }" +
                "  }" +
                "}"+
                " ] " +
                "} ";
        
        
    }
}
