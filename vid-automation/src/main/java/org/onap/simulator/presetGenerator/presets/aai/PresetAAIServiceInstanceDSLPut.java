package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PresetAAIServiceInstanceDSLPut extends BaseAAIPreset {

    public PresetAAIServiceInstanceDSLPut(String globalCustomerId, String serviceInstanceIdentifier, String instanceIdentifierType) {
        this.serviceInstanceIdentifier = serviceInstanceIdentifier;
        this.globalCustomerId = globalCustomerId;
        this.instanceIdentifierType = instanceIdentifierType;
    }

    public String getInstanceIdentifierType() {
        return instanceIdentifierType;
    }

    private final String instanceIdentifierType;
    private final String globalCustomerId;

    public String getGlobalCustomerId() {
        return globalCustomerId;
    }

    public String getServiceInstanceIdentifier() {
        return serviceInstanceIdentifier;
    }

    private final String serviceInstanceIdentifier;

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.PUT;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/dsl";
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of(
            "format", Collections.singletonList("resource"),
            "nodesOnly", Collections.singletonList("true"),
            "depth", Collections.singletonList("0"),
            "as-tree", Collections.singletonList("true")
        );
    }

    @Override
    public Object getRequestBody() {
        String requestBody = null;
        String query = null;
        if(getInstanceIdentifierType().equals("Service Instance Id")) {
            query = "customer*('global-customer-id','" + getGlobalCustomerId() + "')>" +
                "service-subscription>service-instance*('service-instance-id','" + getServiceInstanceIdentifier() + "')";
            requestBody = "{\"dsl\":\"" + query + "\"}";
        } else {
            query = "customer*('global-customer-id','" + getGlobalCustomerId() + "')>" +
                "service-subscription>service-instance*('service-instance-name','" + getServiceInstanceIdentifier() + "')";
            requestBody = "{\"dsl\":\"" + query + "\"}";
        }
        return requestBody;
    }


    @Override
    public Object getResponseBody() {
        return "{\"results\": [\n"
            + "{\n"
            + "\"customer\": {\n"
            + "\"global-customer-id\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\",\n"
            + "\"subscriber-name\": \"Mobility\",\n"
            + "\"subscriber-type\": \"INFRA\",\n"
            + "\"resource-version\": \"1602518417955\",\n"
            + "\"related-nodes\": [\n"
            + "{\n"
            + "\"service-subscription\": {\n"
            + "\"service-type\": \"VPMS\",\n"
            + "\"resource-version\": \"1629183620246\",\n"
            + "\"related-nodes\": [\n"
            + "{\n"
            + "\"service-instance\": {\n"
            + "\"service-instance-id\": \"5d942bc7-3acf-4e35-836a-393619ebde66\",\n"
            + "\"service-instance-name\": \"dpa2actsf5001v_Port_Mirroring_dpa2a_SVC\",\n"
            + "\"service-type\": \"PORT-MIRROR\",\n"
            + "\"service-role\": \"VPROBE\",\n"
            + "\"environment-context\": \"General_Revenue-Bearing\",\n"
            + "\"workload-context\": \"Production\",\n"
            + "\"model-invariant-id\": \"0757d856-a9c6-450d-b494-e1c0a4aab76f\",\n"
            + "\"model-version-id\": \"a9088517-efe8-4bed-9c54-534462cb08c2\",\n"
            + "\"resource-version\": \"1615330529236\",\n"
            + "\"selflink\": \"SOME_SELF_LINK\",\n"
            + "\"orchestration-status\": \"Active\"\n"
            + "}}]}}]}}]}";

    }
}
