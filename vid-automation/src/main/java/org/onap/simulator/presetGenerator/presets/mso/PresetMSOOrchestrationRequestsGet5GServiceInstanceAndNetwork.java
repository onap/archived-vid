package org.onap.simulator.presetGenerator.presets.mso;

import static java.util.Collections.singletonList;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

public class PresetMSOOrchestrationRequestsGet5GServiceInstanceAndNetwork extends BaseMSOPreset {

    public PresetMSOOrchestrationRequestsGet5GServiceInstanceAndNetwork(ResponseDetails parentDetails, ResponseDetails childDetails, String parentInstanceId) {
        this.parentDetails = parentDetails;
        this.childDetails = childDetails;
        this.parentInstanceId = parentInstanceId;
    }

    public static class ResponseDetails {
        public final String instanceName;
        public final String requestId;
        public final String status;
        public final String type;

        public ResponseDetails(String instanceName, String requestId, String status, String type) {
            this.instanceName = instanceName;
            this.requestId = requestId;
            this.status = status;
            this.type = type;
        }
    }

    protected final ResponseDetails parentDetails;
    protected final ResponseDetails childDetails;
    protected final String parentInstanceId;


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
        return ImmutableMap.of("filter", singletonList("serviceInstanceId:EQUALS:"+ parentInstanceId));
    }

    @Override
    public Object getResponseBody() {
        return "{" +
                "  \"requestList\": [{" +
                "      \"request\": {" +
                "        \"requestId\": \""+ parentDetails.requestId+"\"," +
                "        \"startTime\": \"Mon, 05 Nov 2018 09:22:23 GMT\"," +
                "        \"requestScope\": \""+ parentDetails.type+"\"," +
                "        \"requestType\": \"createInstance\"," +
                "        \"requestDetails\": {" +
                "          \"modelInfo\": {" +
                "            \"modelInvariantId\": \"1469946d-d566-467e-867b-88b29f6cb6c7\"," +
                "            \"modelType\": \""+ parentDetails.type+"\"," +
                "            \"modelName\": \"FCGI_5G_NC\"," +
                "            \"modelVersion\": \"1.0\"," +
                "            \"modelVersionId\": \"ed2a3691-c3f3-4ac3-98b3-b0b12acfd1b1\"," +
                "            \"modelUuid\": \"ed2a3691-c3f3-4ac3-98b3-b0b12acfd1b1\"," +
                "            \"modelInvariantUuid\": \"1469946d-d566-467e-867b-88b29f6cb6c7\"" +
                "          }," +
                "          \"requestInfo\": {" +
                "            \"source\": \"VID\"," +
                "            \"instanceName\": \""+ parentDetails.instanceName+"\"," +
                "            \"suppressRollback\": false," +
                "            \"requestorId\": \"us16807000\"" +
                "          }," +
                "          \"subscriberInfo\": {" +
                "            \"globalSubscriberId\": \"e433710f-9217-458d-a79d-1c7aff376d89\"" +
                "          }," +
                "          \"requestParameters\": {" +
                "            \"subscriptionServiceType\": \"TYLER SILVIA\"," +
                "            \"aLaCarte\": true," +
                "            \"testApi\": \"GR_API\"" +
                "          }," +
                "          \"project\": {" +
                "            \"projectName\": \"WATKINS\"" +
                "          }," +
                "          \"owningEntity\": {" +
                "            \"owningEntityId\": \"3f592a6f-459b-435e-b0d4-59959ab1d385\"," +
                "            \"owningEntityName\": \"own1\"" +
                "          }" +
                "        }," +
                "        \"instanceReferences\": {" +
                "          \""+parentDetails.type+"InstanceId\": \"f4c4d4f7-311b-4ecb-bb86-eb3138aac0fb\"," +
                "          \""+parentDetails.type+"InstanceName\": \""+parentDetails.instanceName+"\"," +
                "          \"requestorId\": \"us16807000\"" +
                "        }," +
                "        \"requestStatus\": {" +
                "          \"requestState\": \""+ parentDetails.status+"\"," +
                "          \"statusMessage\": \"STATUS: "+parentDetails.type+" Instance was created successfully.\"," +
                "          \"percentProgress\": 100," +
                "          \"finishTime\": \"Wed, 07 Nov 2018 09:22:35 GMT\"" +
                "        }" +
                "      }" +
                "    }, {" +
                "      \"request\": {" +
                "        \"requestId\": \""+childDetails.requestId+"\"," +
                "        \"startTime\": \"Mon, 05 Nov 2018 09:22:41 GMT\"," +
                "        \"requestScope\": \""+childDetails.type+"\"," +
                "        \"requestType\": \"createInstance\"," +
                "        \"requestDetails\": {" +
                "          \"modelInfo\": {" +
                "            \"modelCustomizationName\": \"FCGI 0\"," +
                "            \"modelInvariantId\": \"ba2ee320-23ce-4d2e-94dd-1ec450cec62c\"," +
                "            \"modelType\": \""+childDetails.type+"\"," +
                "            \"modelName\": \"FCGI\"," +
                "            \"modelVersion\": \"1.0\"," +
                "            \"modelCustomizationUuid\": \"de9b7aea-9727-4a7c-8e72-292263fb61a9\"," +
                "            \"modelVersionId\": \"0d23f7bc-eee4-4151-9fb9-37f5bea834c2\"," +
                "            \"modelCustomizationId\": \"de9b7aea-9727-4a7c-8e72-292263fb61a9\"," +
                "            \"modelUuid\": \"0d23f7bc-eee4-4151-9fb9-37f5bea834c2\"," +
                "            \"modelInvariantUuid\": \"ba2ee320-23ce-4d2e-94dd-1ec450cec62c\"," +
                "            \"modelInstanceName\": \"FCGI 0\"" +
                "          }," +
                "          \"requestInfo\": {" +
                "            \"productFamilyId\": \"e433710f-9217-458d-a79d-1c7aff376d89\"," +
                "            \"source\": \"VID\"," +
                "            \"instanceName\": \""+childDetails.instanceName+"\"," +
                "            \"suppressRollback\": false," +
                "            \"requestorId\": \"us16807000\"" +
                "          }," +
                "          \"relatedInstanceList\": [{" +
                "              \"relatedInstance\": {" +
                "                \"instanceId\": \"f4c4d4f7-311b-4ecb-bb86-eb3138aac0fb\"," +
                "                \"modelInfo\": {" +
                "                  \"modelInvariantId\": \"1469946d-d566-467e-867b-88b29f6cb6c7\"," +
                "                  \"modelType\": \"service\"," +
                "                  \"modelName\": \"FCGI_5G_NC\"," +
                "                  \"modelVersion\": \"1.0\"," +
                "                  \"modelVersionId\": \"ed2a3691-c3f3-4ac3-98b3-b0b12acfd1b1\"," +
                "                  \"modelUuid\": \"ed2a3691-c3f3-4ac3-98b3-b0b12acfd1b1\"," +
                "                  \"modelInvariantUuid\": \"1469946d-d566-467e-867b-88b29f6cb6c7\"" +
                "                }" +
                "              }" +
                "            }" +
                "          ]," +
                "          \"cloudConfiguration\": {" +
                "            \"tenantId\": \"460f35aeb53542dc9f77105066483e83\"," +
                "            \"cloudOwner\": \"irma-aic\"," +
                "            \"lcpCloudRegionId\": \"olson5b\"" +
                "          }," +
                "          \"requestParameters\": {}," +
                "          \"platform\": {" +
                "            \"platformName\": \"plat1\"" +
                "          }," +
                "          \"lineOfBusiness\": {" +
                "            \"lineOfBusinessName\": \"ONAP\"" +
                "          }" +
                "        }," +
                "        \"instanceReferences\": {" +
                "          \""+parentDetails.type+"InstanceId\": \"f4c4d4f7-311b-4ecb-bb86-eb3138aac0fb\"," +
                "          \""+childDetails.type+"InstanceId\": \"2f668980-7dbc-4231-a67c-8b69cd266b3a\"," +
                "          \""+childDetails.type+"InstanceName\": \""+childDetails.instanceName+"\"," +
                "          \"requestorId\": \"us16807000\"" +
                "        }," +
                "        \"requestStatus\": {" +
                "          \"requestState\": \""+childDetails.status+"\"," +
                "          \"statusMessage\": \"STATUS: "+childDetails.type+" has been created successfully.\"," +
                "          \"percentProgress\": 100," +
                "          \"finishTime\": \"Wed, 07 Nov 2018 09:22:45 GMT\"" +
                "        }" +
                "      }" +
                "    }" +
                "  ]" +
                "}";
        
    }
}
