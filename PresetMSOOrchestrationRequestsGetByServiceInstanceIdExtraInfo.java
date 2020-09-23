package org.onap.simulator.presetGenerator.presets.mso;

import com.google.common.collect.ImmutableMap;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

public class PresetMSOOrchestrationRequestsGetByServiceInstanceIdExtraInfo extends BaseMSOPreset {

    private final String instanceId;

    public PresetMSOOrchestrationRequestsGetByServiceInstanceIdExtraInfo() {
        this.instanceId = "937d9e51-03b9-416b-bccd-aa898a85d711";
    }

    public PresetMSOOrchestrationRequestsGetByServiceInstanceIdExtraInfo(String instanceId) {
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
        return ImmutableMap.of("filter", singletonList("serviceInstanceId:EQUALS:" + instanceId ), "format",singletonList("statusDetail"));
    }

    @Override
    public Object getResponseBody() {
        String json = "{\n" +
                "  \"requestList\": [\n" +
                "    {\n" +
                "      \"request\": {\n" +
                "        \"requestId\": \"7ba7900c-3e51-4d87-b1b4-3c53bdfaaa7d\",\n" +
                "        \"startTime\": \"Mon, 24 Aug 2020 22:37:53 GMT\",\n" +
                "        \"finishTime\": \"Mon, 24 Aug 2020 22:38:10 GMT\",\n" +
                "        \"requestScope\": \"service\",\n" +
                "        \"requestType\": \"createInstance\",\n" +
                "        \"requestDetails\": {\n" +
                "          \"modelInfo\": {\n" +
                "            \"modelInvariantId\": \"2da904be-d12b-455c-8951-59ec7d207371\",\n" +
                "            \"modelType\": \"service\",\n" +
                "            \"modelName\": \"FMGW-NC2-507-SVC\",\n" +
                "            \"modelVersion\": \"12.0\",\n" +
                "            \"modelVersionId\": \"c40d56a6-310c-4db9-8455-0aa723d36d53\",\n" +
                "            \"modelUuid\": \"c40d56a6-310c-4db9-8455-0aa723d36d53\",\n" +
                "            \"modelInvariantUuid\": \"2da904be-d12b-455c-8951-59ec7d207371\"\n" +
                "          },\n" +
                "          \"requestInfo\": {\n" +
                "            \"source\": \"VID\",\n" +
                "            \"instanceName\": \"zrdm54cfmgw01_svc\",\n" +
                "            \"suppressRollback\": false,\n" +
                "            \"requestorId\": \"cb4449\"\n" +
                "          },\n" +
                "          \"subscriberInfo\": {\n" +
                "            \"globalSubscriberId\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"\n" +
                "          },\n" +
                "          \"requestParameters\": {\n" +
                "            \"subscriptionServiceType\": \"FIRSTNET\",\n" +
                "            \"aLaCarte\": true,\n" +
                "            \"testApi\": \"GR_API\"\n" +
                "          },\n" +
                "          \"project\": {\n" +
                "            \"projectName\": \"FIRSTNET\"\n" +
                "          },\n" +
                "          \"owningEntity\": {\n" +
                "            \"owningEntityId\": \"10c645f5-9924-4b89-bec0-b17cf49d3cad\",\n" +
                "            \"owningEntityName\": \"MOBILITY-CORE\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"instanceReferences\": {\n" +
                "          \"serviceInstanceId\": \"937d9e51-03b9-416b-bccd-aa898a85d711\",\n" +
                "          \"serviceInstanceName\": \"zrdm54cfmgw01_svc\"\n" +
                "        },\n" +
                "        \"requestStatus\": {\n" +
                "          \"requestState\": \"COMPLETE\",\n" +
                "          \"statusMessage\": \"STATUS: ALaCarte-Service-createInstance request was executed correctly.\",\n" +
                "          \"percentProgress\": 100,\n" +
                "          \"timestamp\": \"Mon, 24 Aug 2020 22:38:10 GMT\",\n" +
                "          \"flowStatus\": \"Successfully completed all Building Blocks\"\n" +
                "        },\n" +
                "        \"requestProcessingData\": [\n" +
                "          {\n" +
                "            \"tag\": \"BPMNExecutionData\",\n" +
                "            \"dataPairs\": [\n" +
                "              {\n" +
                "                \"flowExecutionPath\": \"[{\\\"buildingBlock\\\":{\\\"mso-id\\\":\\\"3ae0a9af-08ac-4674-ac76-e53335bf3b10\\\",\\\"bpmn-flow-name\\\":\\\"AssignServiceInstanceATTBB\\\",\\\"key\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"is-virtual-link\\\":false,\\\"virtual-link-key\\\":null,\\\"scope\\\":null,\\\"action\\\":null},\\\"requestId\\\":\\\"7ba7900c-3e51-4d87-b1b4-3c53bdfaaa7d\\\",\\\"apiVersion\\\":\\\"7\\\",\\\"resourceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"requestAction\\\":\\\"createInstance\\\",\\\"vnfType\\\":\\\"\\\",\\\"oldVolumeGroupName\\\":null,\\\"aLaCarte\\\":true,\\\"homing\\\":false,\\\"workflowResourceIds\\\":{\\\"serviceInstanceId\\\":\\\"\\\",\\\"pnfId\\\":null,\\\"vnfId\\\":\\\"\\\",\\\"networkId\\\":\\\"\\\",\\\"volumeGroupId\\\":\\\"\\\",\\\"vfModuleId\\\":\\\"\\\",\\\"networkCollectionId\\\":null,\\\"configurationId\\\":null,\\\"instanceGroupId\\\":\\\"\\\"},\\\"requestDetails\\\":{\\\"modelInfo\\\":{\\\"modelInvariantId\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\",\\\"modelType\\\":\\\"service\\\",\\\"modelId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelName\\\":\\\"FMGW-NC2-507-SVC\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelVersionId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelUuid\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelInvariantUuid\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\"},\\\"requestInfo\\\":{\\\"source\\\":\\\"VID\\\",\\\"instanceName\\\":\\\"zrdm54cfmgw01_svc\\\",\\\"suppressRollback\\\":false,\\\"requestorId\\\":\\\"cb4449\\\"},\\\"subscriberInfo\\\":{\\\"globalSubscriberId\\\":\\\"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\\\"},\\\"requestParameters\\\":{\\\"subscriptionServiceType\\\":\\\"FIRSTNET\\\",\\\"aLaCarte\\\":true,\\\"usePreload\\\":true,\\\"testApi\\\":\\\"GR_API\\\"},\\\"project\\\":{\\\"projectName\\\":\\\"FIRSTNET\\\"},\\\"owningEntity\\\":{\\\"owningEntityId\\\":\\\"10c645f5-9924-4b89-bec0-b17cf49d3cad\\\",\\\"owningEntityName\\\":\\\"MOBILITY-CORE\\\"}},\\\"configurationResourceKeys\\\":null}, {\\\"buildingBlock\\\":{\\\"mso-id\\\":\\\"c80fcbcc-f1e8-4908-abd2-9014df93a36b\\\",\\\"bpmn-flow-name\\\":\\\"ActivateServiceInstanceATTBB\\\",\\\"key\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"is-virtual-link\\\":false,\\\"virtual-link-key\\\":null,\\\"scope\\\":null,\\\"action\\\":null},\\\"requestId\\\":\\\"7ba7900c-3e51-4d87-b1b4-3c53bdfaaa7d\\\",\\\"apiVersion\\\":\\\"7\\\",\\\"resourceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"requestAction\\\":\\\"createInstance\\\",\\\"vnfType\\\":\\\"\\\",\\\"oldVolumeGroupName\\\":null,\\\"aLaCarte\\\":true,\\\"homing\\\":false,\\\"workflowResourceIds\\\":{\\\"serviceInstanceId\\\":\\\"\\\",\\\"pnfId\\\":null,\\\"vnfId\\\":\\\"\\\",\\\"networkId\\\":\\\"\\\",\\\"volumeGroupId\\\":\\\"\\\",\\\"vfModuleId\\\":\\\"\\\",\\\"networkCollectionId\\\":null,\\\"configurationId\\\":null,\\\"instanceGroupId\\\":\\\"\\\"},\\\"requestDetails\\\":{\\\"modelInfo\\\":{\\\"modelInvariantId\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\",\\\"modelType\\\":\\\"service\\\",\\\"modelId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelName\\\":\\\"FMGW-NC2-507-SVC\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelVersionId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelUuid\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelInvariantUuid\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\"},\\\"requestInfo\\\":{\\\"source\\\":\\\"VID\\\",\\\"instanceName\\\":\\\"zrdm54cfmgw01_svc\\\",\\\"suppressRollback\\\":false,\\\"requestorId\\\":\\\"cb4449\\\"},\\\"subscriberInfo\\\":{\\\"globalSubscriberId\\\":\\\"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\\\"},\\\"requestParameters\\\":{\\\"subscriptionServiceType\\\":\\\"FIRSTNET\\\",\\\"aLaCarte\\\":true,\\\"usePreload\\\":true,\\\"testApi\\\":\\\"GR_API\\\"},\\\"project\\\":{\\\"projectName\\\":\\\"FIRSTNET\\\"},\\\"owningEntity\\\":{\\\"owningEntityId\\\":\\\"10c645f5-9924-4b89-bec0-b17cf49d3cad\\\",\\\"owningEntityName\\\":\\\"MOBILITY-CORE\\\"}},\\\"configurationResourceKeys\\\":null}]\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"request\": {\n" +
                "        \"requestId\": \"f1aa7175-c237-4b56-ba64-7cb728a38ff2\",\n" +
                "        \"startTime\": \"Mon, 24 Aug 2020 22:38:18 GMT\",\n" +
                "        \"finishTime\": \"Mon, 24 Aug 2020 22:44:24 GMT\",\n" +
                "        \"requestScope\": \"vnf\",\n" +
                "        \"requestType\": \"createInstance\",\n" +
                "        \"requestDetails\": {\n" +
                "          \"modelInfo\": {\n" +
                "            \"modelCustomizationName\": \"FMGW-NC2-507 0\",\n" +
                "            \"modelInvariantId\": \"bb32f2eb-8880-4993-b866-20835836fbf6\",\n" +
                "            \"modelType\": \"vnf\",\n" +
                "            \"modelName\": \"FMGW-NC2-507\",\n" +
                "            \"modelVersion\": \"12.0\",\n" +
                "            \"modelCustomizationUuid\": \"9b649dde-872b-417d-99bc-1f28916ebe50\",\n" +
                "            \"modelVersionId\": \"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\",\n" +
                "            \"modelCustomizationId\": \"9b649dde-872b-417d-99bc-1f28916ebe50\",\n" +
                "            \"modelUuid\": \"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\",\n" +
                "            \"modelInvariantUuid\": \"bb32f2eb-8880-4993-b866-20835836fbf6\",\n" +
                "            \"modelInstanceName\": \"FMGW-NC2-507 0\"\n" +
                "          },\n" +
                "          \"requestInfo\": {\n" +
                "            \"productFamilyId\": \"db171b8f-115c-4992-a2e3-ee04cae357e0\",\n" +
                "            \"productFamilyName\": \"FIRSTNET\",\n" +
                "            \"source\": \"VID\",\n" +
                "            \"instanceName\": \"zrdm54cfmgw01\",\n" +
                "            \"suppressRollback\": false,\n" +
                "            \"requestorId\": \"cb4449\"\n" +
                "          },\n" +
                "          \"relatedInstanceList\": [\n" +
                "            {\n" +
                "              \"relatedInstance\": {\n" +
                "                \"instanceId\": \"937d9e51-03b9-416b-bccd-aa898a85d711\",\n" +
                "                \"modelInfo\": {\n" +
                "                  \"modelInvariantId\": \"2da904be-d12b-455c-8951-59ec7d207371\",\n" +
                "                  \"modelType\": \"service\",\n" +
                "                  \"modelName\": \"FMGW-NC2-507-SVC\",\n" +
                "                  \"modelVersion\": \"12.0\",\n" +
                "                  \"modelVersionId\": \"c40d56a6-310c-4db9-8455-0aa723d36d53\",\n" +
                "                  \"modelUuid\": \"c40d56a6-310c-4db9-8455-0aa723d36d53\",\n" +
                "                  \"modelInvariantUuid\": \"2da904be-d12b-455c-8951-59ec7d207371\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          ],\n" +
                "          \"cloudConfiguration\": {\n" +
                "            \"tenantId\": \"ad299b37da30413391e9c28138f0b0cd\",\n" +
                "            \"tenantName\": \"FNCORE-30052-D-MC-RDM54c\",\n" +
                "            \"cloudOwner\": \"att-nc\",\n" +
                "            \"lcpCloudRegionId\": \"rdm54c\"\n" +
                "          },\n" +
                "          \"requestParameters\": {\n" +
                "            \"testApi\": \"GR_API\"\n" +
                "          },\n" +
                "          \"platform\": {\n" +
                "            \"platformName\": \"FIRSTNET-DEDICATED,NETWORK-CLOUD\"\n" +
                "          },\n" +
                "          \"lineOfBusiness\": {\n" +
                "            \"lineOfBusinessName\": \"FIRSTNET\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"instanceReferences\": {\n" +
                "          \"serviceInstanceId\": \"937d9e51-03b9-416b-bccd-aa898a85d711\",\n" +
                "          \"vnfInstanceId\": \"7a7387d0-f020-4297-9459-dfbf8869752e\",\n" +
                "          \"vnfInstanceName\": \"zrdm54cfmgw01\"\n" +
                "        },\n" +
                "        \"requestStatus\": {\n" +
                "          \"requestState\": \"COMPLETE\",\n" +
                "          \"statusMessage\": \"STATUS: ALaCarte-Vnf-createInstance request was executed correctly.\",\n" +
                "          \"percentProgress\": 100,\n" +
                "          \"timestamp\": \"Mon, 24 Aug 2020 22:44:24 GMT\",\n" +
                "          \"flowStatus\": \"Successfully completed all Building Blocks\"\n" +
                "        },\n" +
                "        \"requestProcessingData\": [\n" +
                "          {\n" +
                "            \"tag\": \"BPMNExecutionData\",\n" +
                "            \"dataPairs\": [\n" +
                "              {\n" +
                "                \"flowExecutionPath\": \"[{\\\"buildingBlock\\\":{\\\"mso-id\\\":\\\"c7586ffc-3955-4a09-a3f3-831abcaa8c89\\\",\\\"bpmn-flow-name\\\":\\\"AssignVnfBB\\\",\\\"key\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"is-virtual-link\\\":false,\\\"virtual-link-key\\\":null,\\\"scope\\\":null,\\\"action\\\":null},\\\"requestId\\\":\\\"f1aa7175-c237-4b56-ba64-7cb728a38ff2\\\",\\\"apiVersion\\\":\\\"7\\\",\\\"resourceId\\\":\\\"7a7387d0-f020-4297-9459-dfbf8869752e\\\",\\\"requestAction\\\":\\\"createInstance\\\",\\\"vnfType\\\":\\\"FMGW-NC2-507-SVC/FMGW-NC2-507 0\\\",\\\"oldVolumeGroupName\\\":null,\\\"aLaCarte\\\":true,\\\"homing\\\":false,\\\"workflowResourceIds\\\":{\\\"serviceInstanceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"pnfId\\\":null,\\\"vnfId\\\":\\\"\\\",\\\"networkId\\\":\\\"\\\",\\\"volumeGroupId\\\":\\\"\\\",\\\"vfModuleId\\\":\\\"\\\",\\\"networkCollectionId\\\":null,\\\"configurationId\\\":null,\\\"instanceGroupId\\\":\\\"\\\"},\\\"requestDetails\\\":{\\\"modelInfo\\\":{\\\"modelCustomizationName\\\":\\\"FMGW-NC2-507 0\\\",\\\"modelInvariantId\\\":\\\"bb32f2eb-8880-4993-b866-20835836fbf6\\\",\\\"modelType\\\":\\\"vnf\\\",\\\"modelId\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelName\\\":\\\"FMGW-NC2-507\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelCustomizationUuid\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"modelVersionId\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelCustomizationId\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"modelUuid\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelInvariantUuid\\\":\\\"bb32f2eb-8880-4993-b866-20835836fbf6\\\",\\\"modelInstanceName\\\":\\\"FMGW-NC2-507 0\\\"},\\\"requestInfo\\\":{\\\"productFamilyId\\\":\\\"db171b8f-115c-4992-a2e3-ee04cae357e0\\\",\\\"source\\\":\\\"VID\\\",\\\"instanceName\\\":\\\"zrdm54cfmgw01\\\",\\\"suppressRollback\\\":false,\\\"requestorId\\\":\\\"cb4449\\\",\\\"applicationId\\\":\\\"30626\\\"},\\\"relatedInstanceList\\\":[{\\\"relatedInstance\\\":{\\\"instanceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"modelInfo\\\":{\\\"modelInvariantId\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\",\\\"modelType\\\":\\\"service\\\",\\\"modelId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelName\\\":\\\"FMGW-NC2-507-SVC\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelVersionId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelUuid\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelInvariantUuid\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\"}}}],\\\"cloudConfiguration\\\":{\\\"tenantId\\\":\\\"ad299b37da30413391e9c28138f0b0cd\\\",\\\"cloudOwner\\\":\\\"att-nc\\\",\\\"lcpCloudRegionId\\\":\\\"rdm54c\\\"},\\\"requestParameters\\\":{\\\"testApi\\\":\\\"GR_API\\\"},\\\"platform\\\":{\\\"platformName\\\":\\\"FIRSTNET-DEDICATED,NETWORK-CLOUD\\\"},\\\"lineOfBusiness\\\":{\\\"lineOfBusinessName\\\":\\\"FIRSTNET\\\"}},\\\"configurationResourceKeys\\\":null}, {\\\"buildingBlock\\\":{\\\"mso-id\\\":\\\"389b67e7-3086-4f15-a718-67a0f0f8892c\\\",\\\"bpmn-flow-name\\\":\\\"ActivateVnfBB\\\",\\\"key\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"is-virtual-link\\\":false,\\\"virtual-link-key\\\":null,\\\"scope\\\":null,\\\"action\\\":null},\\\"requestId\\\":\\\"f1aa7175-c237-4b56-ba64-7cb728a38ff2\\\",\\\"apiVersion\\\":\\\"7\\\",\\\"resourceId\\\":\\\"7a7387d0-f020-4297-9459-dfbf8869752e\\\",\\\"requestAction\\\":\\\"createInstance\\\",\\\"vnfType\\\":\\\"FMGW-NC2-507-SVC/FMGW-NC2-507 0\\\",\\\"oldVolumeGroupName\\\":null,\\\"aLaCarte\\\":true,\\\"homing\\\":false,\\\"workflowResourceIds\\\":{\\\"serviceInstanceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"pnfId\\\":null,\\\"vnfId\\\":\\\"\\\",\\\"networkId\\\":\\\"\\\",\\\"volumeGroupId\\\":\\\"\\\",\\\"vfModuleId\\\":\\\"\\\",\\\"networkCollectionId\\\":null,\\\"configurationId\\\":null,\\\"instanceGroupId\\\":\\\"\\\"},\\\"requestDetails\\\":{\\\"modelInfo\\\":{\\\"modelCustomizationName\\\":\\\"FMGW-NC2-507 0\\\",\\\"modelInvariantId\\\":\\\"bb32f2eb-8880-4993-b866-20835836fbf6\\\",\\\"modelType\\\":\\\"vnf\\\",\\\"modelId\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelName\\\":\\\"FMGW-NC2-507\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelCustomizationUuid\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"modelVersionId\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelCustomizationId\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"modelUuid\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelInvariantUuid\\\":\\\"bb32f2eb-8880-4993-b866-20835836fbf6\\\",\\\"modelInstanceName\\\":\\\"FMGW-NC2-507 0\\\"},\\\"requestInfo\\\":{\\\"productFamilyId\\\":\\\"db171b8f-115c-4992-a2e3-ee04cae357e0\\\",\\\"source\\\":\\\"VID\\\",\\\"instanceName\\\":\\\"zrdm54cfmgw01\\\",\\\"suppressRollback\\\":false,\\\"requestorId\\\":\\\"cb4449\\\",\\\"applicationId\\\":\\\"30626\\\"},\\\"relatedInstanceList\\\":[{\\\"relatedInstance\\\":{\\\"instanceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"modelInfo\\\":{\\\"modelInvariantId\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\",\\\"modelType\\\":\\\"service\\\",\\\"modelId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelName\\\":\\\"FMGW-NC2-507-SVC\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelVersionId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelUuid\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelInvariantUuid\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\"}}}],\\\"cloudConfiguration\\\":{\\\"tenantId\\\":\\\"ad299b37da30413391e9c28138f0b0cd\\\",\\\"cloudOwner\\\":\\\"att-nc\\\",\\\"lcpCloudRegionId\\\":\\\"rdm54c\\\"},\\\"requestParameters\\\":{\\\"testApi\\\":\\\"GR_API\\\"},\\\"platform\\\":{\\\"platformName\\\":\\\"FIRSTNET-DEDICATED,NETWORK-CLOUD\\\"},\\\"lineOfBusiness\\\":{\\\"lineOfBusinessName\\\":\\\"FIRSTNET\\\"}},\\\"configurationResourceKeys\\\":null}]\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"request\": {\n" +
                "        \"requestId\": \"a4e43d9e-4813-42e4-94bf-c5c6f22ed0bc\",\n" +
                "        \"startTime\": \"Mon, 24 Aug 2020 22:44:42 GMT\",\n" +
                "        \"finishTime\": \"Mon, 24 Aug 2020 22:54:17 GMT\",\n" +
                "        \"requestScope\": \"vfModule\",\n" +
                "        \"requestType\": \"createInstance\",\n" +
                "        \"requestDetails\": {\n" +
                "          \"modelInfo\": {\n" +
                "            \"modelCustomizationName\": \"FmgwNc2507..mbmsgw_vrp_v2_54c_base_0824..module-14\",\n" +
                "            \"modelInvariantId\": \"b5aa4157-b6dd-4f7c-86c0-468831bd2daa\",\n" +
                "            \"modelType\": \"vfModule\",\n" +
                "            \"modelName\": \"FmgwNc2507..mbmsgw_vrp_v2_54c_base_0824..module-14\",\n" +
                "            \"modelVersion\": \"1\",\n" +
                "            \"modelCustomizationUuid\": \"d4887caf-1efc-4d89-8d95-fbad350a3b05\",\n" +
                "            \"modelVersionId\": \"646ec275-e3c0-4a18-8ad4-c4ac41747d28\",\n" +
                "            \"modelCustomizationId\": \"d4887caf-1efc-4d89-8d95-fbad350a3b05\",\n" +
                "            \"modelUuid\": \"646ec275-e3c0-4a18-8ad4-c4ac41747d28\",\n" +
                "            \"modelInvariantUuid\": \"b5aa4157-b6dd-4f7c-86c0-468831bd2daa\",\n" +
                "            \"modelInstanceName\": \"FmgwNc2507..mbmsgw_vrp_v2_54c_base_0824..module-14\"\n" +
                "          },\n" +
                "          \"requestInfo\": {\n" +
                "            \"source\": \"VID\",\n" +
                "            \"instanceName\": \"zrdm54cfmgw01_base\",\n" +
                "            \"suppressRollback\": false,\n" +
                "            \"requestorId\": \"cb4449\"\n" +
                "          },\n" +
                "          \"relatedInstanceList\": [\n" +
                "            {\n" +
                "              \"relatedInstance\": {\n" +
                "                \"instanceId\": \"937d9e51-03b9-416b-bccd-aa898a85d711\",\n" +
                "                \"modelInfo\": {\n" +
                "                  \"modelInvariantId\": \"2da904be-d12b-455c-8951-59ec7d207371\",\n" +
                "                  \"modelType\": \"service\",\n" +
                "                  \"modelName\": \"FMGW-NC2-507-SVC\",\n" +
                "                  \"modelVersion\": \"12.0\",\n" +
                "                  \"modelVersionId\": \"c40d56a6-310c-4db9-8455-0aa723d36d53\",\n" +
                "                  \"modelUuid\": \"c40d56a6-310c-4db9-8455-0aa723d36d53\",\n" +
                "                  \"modelInvariantUuid\": \"2da904be-d12b-455c-8951-59ec7d207371\"\n" +
                "                }\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"relatedInstance\": {\n" +
                "                \"instanceId\": \"7a7387d0-f020-4297-9459-dfbf8869752e\",\n" +
                "                \"modelInfo\": {\n" +
                "                  \"modelCustomizationName\": \"FMGW-NC2-507 0\",\n" +
                "                  \"modelInvariantId\": \"bb32f2eb-8880-4993-b866-20835836fbf6\",\n" +
                "                  \"modelType\": \"vnf\",\n" +
                "                  \"modelName\": \"FMGW-NC2-507\",\n" +
                "                  \"modelVersion\": \"12.0\",\n" +
                "                  \"modelCustomizationUuid\": \"9b649dde-872b-417d-99bc-1f28916ebe50\",\n" +
                "                  \"modelVersionId\": \"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\",\n" +
                "                  \"modelCustomizationId\": \"9b649dde-872b-417d-99bc-1f28916ebe50\",\n" +
                "                  \"modelUuid\": \"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\",\n" +
                "                  \"modelInvariantUuid\": \"bb32f2eb-8880-4993-b866-20835836fbf6\",\n" +
                "                  \"modelInstanceName\": \"FMGW-NC2-507 0\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          ],\n" +
                "          \"cloudConfiguration\": {\n" +
                "            \"tenantId\": \"ad299b37da30413391e9c28138f0b0cd\",\n" +
                "            \"tenantName\": \"FNCORE-30052-D-MC-RDM54c\",\n" +
                "            \"cloudOwner\": \"att-nc\",\n" +
                "            \"lcpCloudRegionId\": \"rdm54c\"\n" +
                "          },\n" +
                "          \"requestParameters\": {\n" +
                "            \"usePreload\": true,\n" +
                "            \"testApi\": \"GR_API\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"instanceReferences\": {\n" +
                "          \"serviceInstanceId\": \"937d9e51-03b9-416b-bccd-aa898a85d711\",\n" +
                "          \"vnfInstanceId\": \"7a7387d0-f020-4297-9459-dfbf8869752e\",\n" +
                "          \"vfModuleInstanceId\": \"f6793e0d-b639-4d57-a16f-7c8c92e7f682\",\n" +
                "          \"vfModuleInstanceName\": \"zrdm54cfmgw01_base\"\n" +
                "        },\n" +
                "        \"requestStatus\": {\n" +
                "          \"requestState\": \"COMPLETE\",\n" +
                "          \"statusMessage\": \"STATUS: ALaCarte-VfModule-createInstance request was executed correctly.\",\n" +
                "          \"percentProgress\": 100,\n" +
                "          \"timestamp\": \"Mon, 24 Aug 2020 22:54:17 GMT\",\n" +
                "          \"flowStatus\": \"Successfully completed all Building Blocks\"\n" +
                "        },\n" +
                "        \"requestProcessingData\": [\n" +
                "          {\n" +
                "            \"groupingId\": \"fab36297-284c-4068-8382-480b8d1b46dc\",\n" +
                "            \"tag\": \"pincFabricConfigRequest\",\n" +
                "            \"dataPairs\": [\n" +
                "              {\n" +
                "                \"requestAction\": \"activate\",\n" +
                "                \"pincRequestId\": \"9a2ba39b-6695-4815-a332-e2aa570a6aa8\",\n" +
                "                \"configurationId\": \"7df8e686-a7ad-412d-b92b-0855a1f25c10\"\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"groupingId\": \"71e99ecd-59e3-4809-8dc2-29e03074f5c7\",\n" +
                "            \"tag\": \"pincFabricConfigRequest\",\n" +
                "            \"dataPairs\": [\n" +
                "              {\n" +
                "                \"requestAction\": \"activate\",\n" +
                "                \"pincRequestId\": \"f36030d1-5e50-4cb9-b843-4beb1c052245\",\n" +
                "                \"configurationId\": \"6f997f52-354f-4a37-a13c-38c6bc34fe29\"\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"groupingId\": \"599389a8-5243-45fd-94b6-e86cf1630574\",\n" +
                "            \"tag\": \"pincFabricConfigRequest\",\n" +
                "            \"dataPairs\": [\n" +
                "              {\n" +
                "                \"requestAction\": \"assign\",\n" +
                "                \"pincRequestId\": \"47ace6d9-a0c2-4151-b325-1138073a5c2d\",\n" +
                "                \"configurationId\": \"6f997f52-354f-4a37-a13c-38c6bc34fe29\"\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"groupingId\": \"588263b8-4647-446c-977a-6e6e1f7359d5\",\n" +
                "            \"tag\": \"pincFabricConfigRequest\",\n" +
                "            \"dataPairs\": [\n" +
                "              {\n" +
                "                \"requestAction\": \"assign\",\n" +
                "                \"pincRequestId\": \"6161e5e2-9068-4ac4-bfef-696435b9353a\",\n" +
                "                \"configurationId\": \"7df8e686-a7ad-412d-b92b-0855a1f25c10\"\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"groupingId\": \"0748f4b5-9332-4036-a9f3-87344665cc58\",\n" +
                "            \"tag\": \"StackInformation\",\n" +
                "            \"dataPairs\": [\n" +
                "              {\n" +
                "                \"zrdm54cfmgw01_base\": \"{\\\"outputs\\\":[{\\\"description\\\":\\\"No description given\\\",\\\"output_value\\\":\\\"107.124.250.27\\\",\\\"output_key\\\":\\\"oam_management_v4_address\\\"}],\\\"description\\\":\\\"vEPG - virtio HOT Package without HOT ResourceGroups - EPG_cxp9026845_embms_155r21a115\\\",\\\"links\\\":[{\\\"href\\\":\\\"https://orchestration-nc.rdm54c.cci.att.com/v1/ad299b37da30413391e9c28138f0b0cd/stacks/zrdm54cfmgw01_base/0748f4b5-9332-4036-a9f3-87344665cc58\\\",\\\"rel\\\":\\\"self\\\"}],\\\"stack_status_reason\\\":\\\"Stack CREATE completed successfully\\\",\\\"stack_name\\\":\\\"zrdm54cfmgw01_base\\\",\\\"updated_time\\\":null,\\\"creation_time\\\":1598309163000,\\\"stack_status\\\":\\\"CREATE_COMPLETE\\\",\\\"id\\\":\\\"0748f4b5-9332-4036-a9f3-87344665cc58\\\",\\\"template_description\\\":\\\"vEPG - virtio HOT Package without HOT ResourceGroups - EPG_cxp9026845_embms_155r21a115\\\",\\\"stack_owner\\\":null,\\\"disable_rollback\\\":true,\\\"stack_user_project_id\\\":\\\"9ae57a367788477d9d989907078763e3\\\",\\\"timeout_mins\\\":30,\\\"project\\\":null,\\\"files\\\":null,\\\"parameters\\\":{\\\"OS::stack_id\\\":\\\"0748f4b5-9332-4036-a9f3-87344665cc58\\\",\\\"OS::project_id\\\":\\\"ad299b37da30413391e9c28138f0b0cd\\\",\\\"OS::stack_name\\\":\\\"zrdm54cfmgw01_base\\\",\\\"availability_zone_0\\\":\\\"rdm54c-kvm-az01\\\",\\\"availability_zone_1\\\":\\\"rdm54c-kvm-az03\\\",\\\"VRP_compute_node_0\\\":\\\"rdm54r11c001.rdm54c.cci.att.com\\\",\\\"VRP_compute_node_1\\\":\\\"rdm54r13c001.rdm54c.cci.att.com\\\",\\\"VRP_flavor_name\\\":\\\"p1.c2r16d40.i2\\\",\\\"VRP_image_name\\\":\\\"FIRSTNET_EMBMS_epg_vrp_cxp9029285_155r21a122.qcow2\\\",\\\"VRP_names\\\":\\\"zrdm54cfmgw01vrp001,zrdm54cfmgw01vrp002\\\",\\\"vnf_id\\\":\\\"7a7387d0-f020-4297-9459-dfbf8869752e\\\",\\\"vnf_name\\\":\\\"zrdm54cfmgw01\\\",\\\"vf_module_id\\\":\\\"f6793e0d-b639-4d57-a16f-7c8c92e7f682\\\",\\\"workload_context\\\":\\\"Production\\\",\\\"environment_context\\\":\\\"General_Revenue-Bearing\\\",\\\"VRP_oam_protected1_floating_ip\\\":\\\"107.124.250.27\\\",\\\"VRP_0_mate1_ip_0\\\":\\\"172.26.42.1\\\",\\\"VRP_1_mate1_ip_0\\\":\\\"172.26.42.2\\\",\\\"VRP_0_bp1_ip_0\\\":\\\"172.26.43.5\\\",\\\"VRP_1_bp1_ip_0\\\":\\\"172.26.43.6\\\",\\\"VRP_0_bp1_mac\\\":\\\"02:00:00:01:fc:01\\\",\\\"VRP_1_bp1_mac\\\":\\\"02:00:00:01:fd:01\\\",\\\"VRP_0_mate1_mac\\\":\\\"02:00:00:02:fc:fc\\\",\\\"VRP_1_mate1_mac\\\":\\\"02:00:00:02:fd:fc\\\",\\\"oam_protected1_net_name\\\":\\\"FNCORE-30052-D-MC-RDM54c_oam_protected_net_1\\\",\\\"VRP_oam_protected1_vlan_filter\\\":\\\"167\\\",\\\"VRP_oam_protected1_public_vlans\\\":\\\"167\\\",\\\"VRP_oam_protected1_private_vlans\\\":\\\"\\\",\\\"mate1_net_name\\\":\\\"FNCORE-30052-D-MC-RDM54c_int_mate_net_1\\\",\\\"VRP_mate1_vlan_filter\\\":\\\"616\\\",\\\"VRP_mate1_public_vlans\\\":\\\"616\\\",\\\"VRP_mate1_private_vlans\\\":\\\"\\\",\\\"bp1_net_name\\\":\\\"FNCORE-30052-D-MC-RDM54c_int_bp_net_1\\\",\\\"VRP_bp1_vlan_filter\\\":\\\"617\\\",\\\"VRP_bp1_public_vlans\\\":\\\"617\\\",\\\"VRP_bp1_private_vlans\\\":\\\"\\\"}}\"\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"tag\": \"BPMNExecutionData\",\n" +
                "            \"dataPairs\": [\n" +
                "              {\n" +
                "                \"flowExecutionPath\": \"[{\\\"buildingBlock\\\":{\\\"mso-id\\\":\\\"da597c7d-fbf7-4ef7-926b-4db4fca43022\\\",\\\"bpmn-flow-name\\\":\\\"AssignVfModuleBB\\\",\\\"key\\\":\\\"d4887caf-1efc-4d89-8d95-fbad350a3b05\\\",\\\"is-virtual-link\\\":false,\\\"virtual-link-key\\\":null,\\\"scope\\\":null,\\\"action\\\":null},\\\"requestId\\\":\\\"a4e43d9e-4813-42e4-94bf-c5c6f22ed0bc\\\",\\\"apiVersion\\\":\\\"7\\\",\\\"resourceId\\\":\\\"f6793e0d-b639-4d57-a16f-7c8c92e7f682\\\",\\\"requestAction\\\":\\\"createInstance\\\",\\\"vnfType\\\":\\\"FMGW-NC2-507-SVC/FMGW-NC2-507 0\\\",\\\"oldVolumeGroupName\\\":null,\\\"aLaCarte\\\":true,\\\"homing\\\":false,\\\"workflowResourceIds\\\":{\\\"serviceInstanceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"pnfId\\\":null,\\\"vnfId\\\":\\\"7a7387d0-f020-4297-9459-dfbf8869752e\\\",\\\"networkId\\\":\\\"\\\",\\\"volumeGroupId\\\":\\\"\\\",\\\"vfModuleId\\\":\\\"\\\",\\\"networkCollectionId\\\":null,\\\"configurationId\\\":null,\\\"instanceGroupId\\\":\\\"\\\"},\\\"requestDetails\\\":{\\\"modelInfo\\\":{\\\"modelCustomizationName\\\":\\\"FmgwNc2507..mbmsgw_vrp_v2_54c_base_0824..module-14\\\",\\\"modelInvariantId\\\":\\\"b5aa4157-b6dd-4f7c-86c0-468831bd2daa\\\",\\\"modelType\\\":\\\"vfModule\\\",\\\"modelId\\\":\\\"646ec275-e3c0-4a18-8ad4-c4ac41747d28\\\",\\\"modelName\\\":\\\"FmgwNc2507..mbmsgw_vrp_v2_54c_base_0824..module-14\\\",\\\"modelVersion\\\":\\\"1\\\",\\\"modelCustomizationUuid\\\":\\\"d4887caf-1efc-4d89-8d95-fbad350a3b05\\\",\\\"modelVersionId\\\":\\\"646ec275-e3c0-4a18-8ad4-c4ac41747d28\\\",\\\"modelCustomizationId\\\":\\\"d4887caf-1efc-4d89-8d95-fbad350a3b05\\\",\\\"modelUuid\\\":\\\"646ec275-e3c0-4a18-8ad4-c4ac41747d28\\\",\\\"modelInvariantUuid\\\":\\\"b5aa4157-b6dd-4f7c-86c0-468831bd2daa\\\",\\\"modelInstanceName\\\":\\\"FmgwNc2507..mbmsgw_vrp_v2_54c_base_0824..module-14\\\"},\\\"requestInfo\\\":{\\\"source\\\":\\\"VID\\\",\\\"instanceName\\\":\\\"zrdm54cfmgw01_base\\\",\\\"suppressRollback\\\":false,\\\"requestorId\\\":\\\"cb4449\\\"},\\\"relatedInstanceList\\\":[{\\\"relatedInstance\\\":{\\\"instanceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"modelInfo\\\":{\\\"modelInvariantId\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\",\\\"modelType\\\":\\\"service\\\",\\\"modelId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelName\\\":\\\"FMGW-NC2-507-SVC\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelVersionId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelUuid\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelInvariantUuid\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\"}}},{\\\"relatedInstance\\\":{\\\"instanceId\\\":\\\"7a7387d0-f020-4297-9459-dfbf8869752e\\\",\\\"modelInfo\\\":{\\\"modelCustomizationName\\\":\\\"FMGW-NC2-507 0\\\",\\\"modelInvariantId\\\":\\\"bb32f2eb-8880-4993-b866-20835836fbf6\\\",\\\"modelType\\\":\\\"vnf\\\",\\\"modelId\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelName\\\":\\\"FMGW-NC2-507\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelCustomizationUuid\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"modelVersionId\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelCustomizationId\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"modelUuid\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelInvariantUuid\\\":\\\"bb32f2eb-8880-4993-b866-20835836fbf6\\\",\\\"modelInstanceName\\\":\\\"FMGW-NC2-507 0\\\"}}}],\\\"cloudConfiguration\\\":{\\\"tenantId\\\":\\\"ad299b37da30413391e9c28138f0b0cd\\\",\\\"cloudOwner\\\":\\\"att-nc\\\",\\\"lcpCloudRegionId\\\":\\\"rdm54c\\\"},\\\"requestParameters\\\":{\\\"usePreload\\\":true,\\\"testApi\\\":\\\"GR_API\\\"}},\\\"configurationResourceKeys\\\":null}, {\\\"buildingBlock\\\":{\\\"mso-id\\\":\\\"8ca86f17-a20c-4507-9111-f5254c5a41e0\\\",\\\"bpmn-flow-name\\\":\\\"CreateVfModuleATTBB\\\",\\\"key\\\":\\\"d4887caf-1efc-4d89-8d95-fbad350a3b05\\\",\\\"is-virtual-link\\\":false,\\\"virtual-link-key\\\":null,\\\"scope\\\":null,\\\"action\\\":null},\\\"requestId\\\":\\\"a4e43d9e-4813-42e4-94bf-c5c6f22ed0bc\\\",\\\"apiVersion\\\":\\\"7\\\",\\\"resourceId\\\":\\\"f6793e0d-b639-4d57-a16f-7c8c92e7f682\\\",\\\"requestAction\\\":\\\"createInstance\\\",\\\"vnfType\\\":\\\"FMGW-NC2-507-SVC/FMGW-NC2-507 0\\\",\\\"oldVolumeGroupName\\\":null,\\\"aLaCarte\\\":true,\\\"homing\\\":false,\\\"workflowResourceIds\\\":{\\\"serviceInstanceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"pnfId\\\":null,\\\"vnfId\\\":\\\"7a7387d0-f020-4297-9459-dfbf8869752e\\\",\\\"networkId\\\":\\\"\\\",\\\"volumeGroupId\\\":\\\"\\\",\\\"vfModuleId\\\":\\\"\\\",\\\"networkCollectionId\\\":null,\\\"configurationId\\\":null,\\\"instanceGroupId\\\":\\\"\\\"},\\\"requestDetails\\\":{\\\"modelInfo\\\":{\\\"modelCustomizationName\\\":\\\"FmgwNc2507..mbmsgw_vrp_v2_54c_base_0824..module-14\\\",\\\"modelInvariantId\\\":\\\"b5aa4157-b6dd-4f7c-86c0-468831bd2daa\\\",\\\"modelType\\\":\\\"vfModule\\\",\\\"modelId\\\":\\\"646ec275-e3c0-4a18-8ad4-c4ac41747d28\\\",\\\"modelName\\\":\\\"FmgwNc2507..mbmsgw_vrp_v2_54c_base_0824..module-14\\\",\\\"modelVersion\\\":\\\"1\\\",\\\"modelCustomizationUuid\\\":\\\"d4887caf-1efc-4d89-8d95-fbad350a3b05\\\",\\\"modelVersionId\\\":\\\"646ec275-e3c0-4a18-8ad4-c4ac41747d28\\\",\\\"modelCustomizationId\\\":\\\"d4887caf-1efc-4d89-8d95-fbad350a3b05\\\",\\\"modelUuid\\\":\\\"646ec275-e3c0-4a18-8ad4-c4ac41747d28\\\",\\\"modelInvariantUuid\\\":\\\"b5aa4157-b6dd-4f7c-86c0-468831bd2daa\\\",\\\"modelInstanceName\\\":\\\"FmgwNc2507..mbmsgw_vrp_v2_54c_base_0824..module-14\\\"},\\\"requestInfo\\\":{\\\"source\\\":\\\"VID\\\",\\\"instanceName\\\":\\\"zrdm54cfmgw01_base\\\",\\\"suppressRollback\\\":false,\\\"requestorId\\\":\\\"cb4449\\\"},\\\"relatedInstanceList\\\":[{\\\"relatedInstance\\\":{\\\"instanceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"modelInfo\\\":{\\\"modelInvariantId\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\",\\\"modelType\\\":\\\"service\\\",\\\"modelId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelName\\\":\\\"FMGW-NC2-507-SVC\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelVersionId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelUuid\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelInvariantUuid\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\"}}},{\\\"relatedInstance\\\":{\\\"instanceId\\\":\\\"7a7387d0-f020-4297-9459-dfbf8869752e\\\",\\\"modelInfo\\\":{\\\"modelCustomizationName\\\":\\\"FMGW-NC2-507 0\\\",\\\"modelInvariantId\\\":\\\"bb32f2eb-8880-4993-b866-20835836fbf6\\\",\\\"modelType\\\":\\\"vnf\\\",\\\"modelId\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelName\\\":\\\"FMGW-NC2-507\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelCustomizationUuid\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"modelVersionId\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelCustomizationId\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"modelUuid\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelInvariantUuid\\\":\\\"bb32f2eb-8880-4993-b866-20835836fbf6\\\",\\\"modelInstanceName\\\":\\\"FMGW-NC2-507 0\\\"}}}],\\\"cloudConfiguration\\\":{\\\"tenantId\\\":\\\"ad299b37da30413391e9c28138f0b0cd\\\",\\\"cloudOwner\\\":\\\"att-nc\\\",\\\"lcpCloudRegionId\\\":\\\"rdm54c\\\"},\\\"requestParameters\\\":{\\\"usePreload\\\":true,\\\"testApi\\\":\\\"GR_API\\\"}},\\\"configurationResourceKeys\\\":null}, {\\\"buildingBlock\\\":{\\\"mso-id\\\":\\\"858d9a30-8ed2-477b-a8a4-23d5627d0e85\\\",\\\"bpmn-flow-name\\\":\\\"ActivateVfModuleBB\\\",\\\"key\\\":\\\"d4887caf-1efc-4d89-8d95-fbad350a3b05\\\",\\\"is-virtual-link\\\":false,\\\"virtual-link-key\\\":null,\\\"scope\\\":null,\\\"action\\\":null},\\\"requestId\\\":\\\"a4e43d9e-4813-42e4-94bf-c5c6f22ed0bc\\\",\\\"apiVersion\\\":\\\"7\\\",\\\"resourceId\\\":\\\"f6793e0d-b639-4d57-a16f-7c8c92e7f682\\\",\\\"requestAction\\\":\\\"createInstance\\\",\\\"vnfType\\\":\\\"FMGW-NC2-507-SVC/FMGW-NC2-507 0\\\",\\\"oldVolumeGroupName\\\":null,\\\"aLaCarte\\\":true,\\\"homing\\\":false,\\\"workflowResourceIds\\\":{\\\"serviceInstanceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"pnfId\\\":null,\\\"vnfId\\\":\\\"7a7387d0-f020-4297-9459-dfbf8869752e\\\",\\\"networkId\\\":\\\"\\\",\\\"volumeGroupId\\\":\\\"\\\",\\\"vfModuleId\\\":\\\"\\\",\\\"networkCollectionId\\\":null,\\\"configurationId\\\":null,\\\"instanceGroupId\\\":\\\"\\\"},\\\"requestDetails\\\":{\\\"modelInfo\\\":{\\\"modelCustomizationName\\\":\\\"FmgwNc2507..mbmsgw_vrp_v2_54c_base_0824..module-14\\\",\\\"modelInvariantId\\\":\\\"b5aa4157-b6dd-4f7c-86c0-468831bd2daa\\\",\\\"modelType\\\":\\\"vfModule\\\",\\\"modelId\\\":\\\"646ec275-e3c0-4a18-8ad4-c4ac41747d28\\\",\\\"modelName\\\":\\\"FmgwNc2507..mbmsgw_vrp_v2_54c_base_0824..module-14\\\",\\\"modelVersion\\\":\\\"1\\\",\\\"modelCustomizationUuid\\\":\\\"d4887caf-1efc-4d89-8d95-fbad350a3b05\\\",\\\"modelVersionId\\\":\\\"646ec275-e3c0-4a18-8ad4-c4ac41747d28\\\",\\\"modelCustomizationId\\\":\\\"d4887caf-1efc-4d89-8d95-fbad350a3b05\\\",\\\"modelUuid\\\":\\\"646ec275-e3c0-4a18-8ad4-c4ac41747d28\\\",\\\"modelInvariantUuid\\\":\\\"b5aa4157-b6dd-4f7c-86c0-468831bd2daa\\\",\\\"modelInstanceName\\\":\\\"FmgwNc2507..mbmsgw_vrp_v2_54c_base_0824..module-14\\\"},\\\"requestInfo\\\":{\\\"source\\\":\\\"VID\\\",\\\"instanceName\\\":\\\"zrdm54cfmgw01_base\\\",\\\"suppressRollback\\\":false,\\\"requestorId\\\":\\\"cb4449\\\"},\\\"relatedInstanceList\\\":[{\\\"relatedInstance\\\":{\\\"instanceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"modelInfo\\\":{\\\"modelInvariantId\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\",\\\"modelType\\\":\\\"service\\\",\\\"modelId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelName\\\":\\\"FMGW-NC2-507-SVC\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelVersionId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelUuid\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelInvariantUuid\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\"}}},{\\\"relatedInstance\\\":{\\\"instanceId\\\":\\\"7a7387d0-f020-4297-9459-dfbf8869752e\\\",\\\"modelInfo\\\":{\\\"modelCustomizationName\\\":\\\"FMGW-NC2-507 0\\\",\\\"modelInvariantId\\\":\\\"bb32f2eb-8880-4993-b866-20835836fbf6\\\",\\\"modelType\\\":\\\"vnf\\\",\\\"modelId\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelName\\\":\\\"FMGW-NC2-507\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelCustomizationUuid\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"modelVersionId\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelCustomizationId\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"modelUuid\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelInvariantUuid\\\":\\\"bb32f2eb-8880-4993-b866-20835836fbf6\\\",\\\"modelInstanceName\\\":\\\"FMGW-NC2-507 0\\\"}}}],\\\"cloudConfiguration\\\":{\\\"tenantId\\\":\\\"ad299b37da30413391e9c28138f0b0cd\\\",\\\"cloudOwner\\\":\\\"att-nc\\\",\\\"lcpCloudRegionId\\\":\\\"rdm54c\\\"},\\\"requestParameters\\\":{\\\"usePreload\\\":true,\\\"testApi\\\":\\\"GR_API\\\"}},\\\"configurationResourceKeys\\\":null}]\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"request\": {\n" +
                "        \"requestId\": \"1fc2ef3b-26f0-4e62-a00a-6a31502d39e2\",\n" +
                "        \"startTime\": \"Mon, 24 Aug 2020 22:54:29 GMT\",\n" +
                "        \"finishTime\": \"Mon, 24 Aug 2020 22:56:35 GMT\",\n" +
                "        \"requestScope\": \"vfModule\",\n" +
                "        \"requestType\": \"createInstance\",\n" +
                "        \"requestDetails\": {\n" +
                "          \"modelInfo\": {\n" +
                "            \"modelCustomizationName\": \"FmgwNc2507..mbmsgw_scp_v2_54c_0820..module-13\",\n" +
                "            \"modelInvariantId\": \"19ad8bd1-9d83-43a1-94fa-bc5ee0bdd52a\",\n" +
                "            \"modelType\": \"vfModule\",\n" +
                "            \"modelName\": \"FmgwNc2507..mbmsgw_scp_v2_54c_0820..module-13\",\n" +
                "            \"modelVersion\": \"2\",\n" +
                "            \"modelCustomizationUuid\": \"15bd6af8-aff5-4538-8b14-c92986ea2d4b\",\n" +
                "            \"modelVersionId\": \"8c7aa631-d5e2-49ae-832b-d5b1b5c8cd36\",\n" +
                "            \"modelCustomizationId\": \"15bd6af8-aff5-4538-8b14-c92986ea2d4b\",\n" +
                "            \"modelUuid\": \"8c7aa631-d5e2-49ae-832b-d5b1b5c8cd36\",\n" +
                "            \"modelInvariantUuid\": \"19ad8bd1-9d83-43a1-94fa-bc5ee0bdd52a\",\n" +
                "            \"modelInstanceName\": \"FmgwNc2507..mbmsgw_scp_v2_54c_0820..module-13\"\n" +
                "          },\n" +
                "          \"requestInfo\": {\n" +
                "            \"source\": \"VID\",\n" +
                "            \"instanceName\": \"zrdm54cfmgw01_sup_1\",\n" +
                "            \"suppressRollback\": false,\n" +
                "            \"requestorId\": \"cb4449\"\n" +
                "          },\n" +
                "          \"relatedInstanceList\": [\n" +
                "            {\n" +
                "              \"relatedInstance\": {\n" +
                "                \"instanceId\": \"937d9e51-03b9-416b-bccd-aa898a85d711\",\n" +
                "                \"modelInfo\": {\n" +
                "                  \"modelInvariantId\": \"2da904be-d12b-455c-8951-59ec7d207371\",\n" +
                "                  \"modelType\": \"service\",\n" +
                "                  \"modelName\": \"FMGW-NC2-507-SVC\",\n" +
                "                  \"modelVersion\": \"12.0\",\n" +
                "                  \"modelVersionId\": \"c40d56a6-310c-4db9-8455-0aa723d36d53\",\n" +
                "                  \"modelUuid\": \"c40d56a6-310c-4db9-8455-0aa723d36d53\",\n" +
                "                  \"modelInvariantUuid\": \"2da904be-d12b-455c-8951-59ec7d207371\"\n" +
                "                }\n" +
                "              }\n" +
                "            },\n" +
                "            {\n" +
                "              \"relatedInstance\": {\n" +
                "                \"instanceId\": \"7a7387d0-f020-4297-9459-dfbf8869752e\",\n" +
                "                \"modelInfo\": {\n" +
                "                  \"modelCustomizationName\": \"FMGW-NC2-507 0\",\n" +
                "                  \"modelInvariantId\": \"bb32f2eb-8880-4993-b866-20835836fbf6\",\n" +
                "                  \"modelType\": \"vnf\",\n" +
                "                  \"modelName\": \"FMGW-NC2-507\",\n" +
                "                  \"modelVersion\": \"12.0\",\n" +
                "                  \"modelCustomizationUuid\": \"9b649dde-872b-417d-99bc-1f28916ebe50\",\n" +
                "                  \"modelVersionId\": \"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\",\n" +
                "                  \"modelCustomizationId\": \"9b649dde-872b-417d-99bc-1f28916ebe50\",\n" +
                "                  \"modelUuid\": \"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\",\n" +
                "                  \"modelInvariantUuid\": \"bb32f2eb-8880-4993-b866-20835836fbf6\",\n" +
                "                  \"modelInstanceName\": \"FMGW-NC2-507 0\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          ],\n" +
                "          \"cloudConfiguration\": {\n" +
                "            \"tenantId\": \"ad299b37da30413391e9c28138f0b0cd\",\n" +
                "            \"tenantName\": \"FNCORE-30052-D-MC-RDM54c\",\n" +
                "            \"cloudOwner\": \"att-nc\",\n" +
                "            \"lcpCloudRegionId\": \"rdm54c\"\n" +
                "          },\n" +
                "          \"requestParameters\": {\n" +
                "            \"usePreload\": true,\n" +
                "            \"testApi\": \"GR_API\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"instanceReferences\": {\n" +
                "          \"serviceInstanceId\": \"937d9e51-03b9-416b-bccd-aa898a85d711\",\n" +
                "          \"vnfInstanceId\": \"7a7387d0-f020-4297-9459-dfbf8869752e\",\n" +
                "          \"vfModuleInstanceId\": \"fde94d73-6fab-4b9e-9d48-01ca0840ca88\",\n" +
                "          \"vfModuleInstanceName\": \"zrdm54cfmgw01_sup_1\"\n" +
                "        },\n" +
                "        \"requestStatus\": {\n" +
                "          \"requestState\": \"ROLLED_BACK_TO_ASSIGNED\",\n" +
                "          \"statusMessage\": \"STATUS: Error Source: OPENSTACK, Error Message: Received vfModuleException from VnfAdapter: category='INTERNAL' message='Exception during create VF 400 Bad Request: The server could not comply with the request since it is either malformed or otherwise incorrect., error.type=UserParameterMissing, error.message=The Parameter (VSFO_CP0_compute_node) was not provided.' rolledBack='true'\",\n" +
                "          \"percentProgress\": 100,\n" +
                "          \"timestamp\": \"Mon, 24 Aug 2020 22:56:35 GMT\",\n" +
                "          \"extSystemErrorSource\": \"OPENSTACK\",\n" +
                "          \"flowStatus\": \"All Rollback flows have completed successfully\",\n" +
                "          \"rollbackStatusMessage\": \"Rollback has been completed successfully.\"\n" +
                "        },\n" +
                "        \"requestProcessingData\": [\n" +
                "          {\n" +
                "            \"tag\": \"BPMNExecutionData\",\n" +
                "            \"dataPairs\": [\n" +
                "              {\n" +
                "                \"flowExecutionPath\": \"[{\\\"buildingBlock\\\":{\\\"mso-id\\\":\\\"32818ded-88da-43d9-b687-ef2de6c9c809\\\",\\\"bpmn-flow-name\\\":\\\"AssignVfModuleBB\\\",\\\"key\\\":\\\"15bd6af8-aff5-4538-8b14-c92986ea2d4b\\\",\\\"is-virtual-link\\\":false,\\\"virtual-link-key\\\":null,\\\"scope\\\":null,\\\"action\\\":null},\\\"requestId\\\":\\\"1fc2ef3b-26f0-4e62-a00a-6a31502d39e2\\\",\\\"apiVersion\\\":\\\"7\\\",\\\"resourceId\\\":\\\"fde94d73-6fab-4b9e-9d48-01ca0840ca88\\\",\\\"requestAction\\\":\\\"createInstance\\\",\\\"vnfType\\\":\\\"FMGW-NC2-507-SVC/FMGW-NC2-507 0\\\",\\\"oldVolumeGroupName\\\":null,\\\"aLaCarte\\\":true,\\\"homing\\\":false,\\\"workflowResourceIds\\\":{\\\"serviceInstanceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"pnfId\\\":null,\\\"vnfId\\\":\\\"7a7387d0-f020-4297-9459-dfbf8869752e\\\",\\\"networkId\\\":\\\"\\\",\\\"volumeGroupId\\\":\\\"\\\",\\\"vfModuleId\\\":\\\"\\\",\\\"networkCollectionId\\\":null,\\\"configurationId\\\":null,\\\"instanceGroupId\\\":\\\"\\\"},\\\"requestDetails\\\":{\\\"modelInfo\\\":{\\\"modelCustomizationName\\\":\\\"FmgwNc2507..mbmsgw_scp_v2_54c_0820..module-13\\\",\\\"modelInvariantId\\\":\\\"19ad8bd1-9d83-43a1-94fa-bc5ee0bdd52a\\\",\\\"modelType\\\":\\\"vfModule\\\",\\\"modelId\\\":\\\"8c7aa631-d5e2-49ae-832b-d5b1b5c8cd36\\\",\\\"modelName\\\":\\\"FmgwNc2507..mbmsgw_scp_v2_54c_0820..module-13\\\",\\\"modelVersion\\\":\\\"2\\\",\\\"modelCustomizationUuid\\\":\\\"15bd6af8-aff5-4538-8b14-c92986ea2d4b\\\",\\\"modelVersionId\\\":\\\"8c7aa631-d5e2-49ae-832b-d5b1b5c8cd36\\\",\\\"modelCustomizationId\\\":\\\"15bd6af8-aff5-4538-8b14-c92986ea2d4b\\\",\\\"modelUuid\\\":\\\"8c7aa631-d5e2-49ae-832b-d5b1b5c8cd36\\\",\\\"modelInvariantUuid\\\":\\\"19ad8bd1-9d83-43a1-94fa-bc5ee0bdd52a\\\",\\\"modelInstanceName\\\":\\\"FmgwNc2507..mbmsgw_scp_v2_54c_0820..module-13\\\"},\\\"requestInfo\\\":{\\\"source\\\":\\\"VID\\\",\\\"instanceName\\\":\\\"zrdm54cfmgw01_sup_1\\\",\\\"suppressRollback\\\":false,\\\"requestorId\\\":\\\"cb4449\\\"},\\\"relatedInstanceList\\\":[{\\\"relatedInstance\\\":{\\\"instanceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"modelInfo\\\":{\\\"modelInvariantId\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\",\\\"modelType\\\":\\\"service\\\",\\\"modelId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelName\\\":\\\"FMGW-NC2-507-SVC\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelVersionId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelUuid\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelInvariantUuid\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\"}}},{\\\"relatedInstance\\\":{\\\"instanceId\\\":\\\"7a7387d0-f020-4297-9459-dfbf8869752e\\\",\\\"modelInfo\\\":{\\\"modelCustomizationName\\\":\\\"FMGW-NC2-507 0\\\",\\\"modelInvariantId\\\":\\\"bb32f2eb-8880-4993-b866-20835836fbf6\\\",\\\"modelType\\\":\\\"vnf\\\",\\\"modelId\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelName\\\":\\\"FMGW-NC2-507\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelCustomizationUuid\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"modelVersionId\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelCustomizationId\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"modelUuid\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelInvariantUuid\\\":\\\"bb32f2eb-8880-4993-b866-20835836fbf6\\\",\\\"modelInstanceName\\\":\\\"FMGW-NC2-507 0\\\"}}}],\\\"cloudConfiguration\\\":{\\\"tenantId\\\":\\\"ad299b37da30413391e9c28138f0b0cd\\\",\\\"cloudOwner\\\":\\\"att-nc\\\",\\\"lcpCloudRegionId\\\":\\\"rdm54c\\\"},\\\"requestParameters\\\":{\\\"usePreload\\\":true,\\\"testApi\\\":\\\"GR_API\\\"}},\\\"configurationResourceKeys\\\":null}, {\\\"buildingBlock\\\":{\\\"mso-id\\\":\\\"7954dc2e-f0fc-44f2-b34c-af1d131d2e6c\\\",\\\"bpmn-flow-name\\\":\\\"CreateVfModuleATTBB\\\",\\\"key\\\":\\\"15bd6af8-aff5-4538-8b14-c92986ea2d4b\\\",\\\"is-virtual-link\\\":false,\\\"virtual-link-key\\\":null,\\\"scope\\\":null,\\\"action\\\":null},\\\"requestId\\\":\\\"1fc2ef3b-26f0-4e62-a00a-6a31502d39e2\\\",\\\"apiVersion\\\":\\\"7\\\",\\\"resourceId\\\":\\\"fde94d73-6fab-4b9e-9d48-01ca0840ca88\\\",\\\"requestAction\\\":\\\"createInstance\\\",\\\"vnfType\\\":\\\"FMGW-NC2-507-SVC/FMGW-NC2-507 0\\\",\\\"oldVolumeGroupName\\\":null,\\\"aLaCarte\\\":true,\\\"homing\\\":false,\\\"workflowResourceIds\\\":{\\\"serviceInstanceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"pnfId\\\":null,\\\"vnfId\\\":\\\"7a7387d0-f020-4297-9459-dfbf8869752e\\\",\\\"networkId\\\":\\\"\\\",\\\"volumeGroupId\\\":\\\"\\\",\\\"vfModuleId\\\":\\\"\\\",\\\"networkCollectionId\\\":null,\\\"configurationId\\\":null,\\\"instanceGroupId\\\":\\\"\\\"},\\\"requestDetails\\\":{\\\"modelInfo\\\":{\\\"modelCustomizationName\\\":\\\"FmgwNc2507..mbmsgw_scp_v2_54c_0820..module-13\\\",\\\"modelInvariantId\\\":\\\"19ad8bd1-9d83-43a1-94fa-bc5ee0bdd52a\\\",\\\"modelType\\\":\\\"vfModule\\\",\\\"modelId\\\":\\\"8c7aa631-d5e2-49ae-832b-d5b1b5c8cd36\\\",\\\"modelName\\\":\\\"FmgwNc2507..mbmsgw_scp_v2_54c_0820..module-13\\\",\\\"modelVersion\\\":\\\"2\\\",\\\"modelCustomizationUuid\\\":\\\"15bd6af8-aff5-4538-8b14-c92986ea2d4b\\\",\\\"modelVersionId\\\":\\\"8c7aa631-d5e2-49ae-832b-d5b1b5c8cd36\\\",\\\"modelCustomizationId\\\":\\\"15bd6af8-aff5-4538-8b14-c92986ea2d4b\\\",\\\"modelUuid\\\":\\\"8c7aa631-d5e2-49ae-832b-d5b1b5c8cd36\\\",\\\"modelInvariantUuid\\\":\\\"19ad8bd1-9d83-43a1-94fa-bc5ee0bdd52a\\\",\\\"modelInstanceName\\\":\\\"FmgwNc2507..mbmsgw_scp_v2_54c_0820..module-13\\\"},\\\"requestInfo\\\":{\\\"source\\\":\\\"VID\\\",\\\"instanceName\\\":\\\"zrdm54cfmgw01_sup_1\\\",\\\"suppressRollback\\\":false,\\\"requestorId\\\":\\\"cb4449\\\"},\\\"relatedInstanceList\\\":[{\\\"relatedInstance\\\":{\\\"instanceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"modelInfo\\\":{\\\"modelInvariantId\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\",\\\"modelType\\\":\\\"service\\\",\\\"modelId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelName\\\":\\\"FMGW-NC2-507-SVC\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelVersionId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelUuid\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelInvariantUuid\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\"}}},{\\\"relatedInstance\\\":{\\\"instanceId\\\":\\\"7a7387d0-f020-4297-9459-dfbf8869752e\\\",\\\"modelInfo\\\":{\\\"modelCustomizationName\\\":\\\"FMGW-NC2-507 0\\\",\\\"modelInvariantId\\\":\\\"bb32f2eb-8880-4993-b866-20835836fbf6\\\",\\\"modelType\\\":\\\"vnf\\\",\\\"modelId\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelName\\\":\\\"FMGW-NC2-507\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelCustomizationUuid\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"modelVersionId\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelCustomizationId\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"modelUuid\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelInvariantUuid\\\":\\\"bb32f2eb-8880-4993-b866-20835836fbf6\\\",\\\"modelInstanceName\\\":\\\"FMGW-NC2-507 0\\\"}}}],\\\"cloudConfiguration\\\":{\\\"tenantId\\\":\\\"ad299b37da30413391e9c28138f0b0cd\\\",\\\"cloudOwner\\\":\\\"att-nc\\\",\\\"lcpCloudRegionId\\\":\\\"rdm54c\\\"},\\\"requestParameters\\\":{\\\"usePreload\\\":true,\\\"testApi\\\":\\\"GR_API\\\"}},\\\"configurationResourceKeys\\\":null}, {\\\"buildingBlock\\\":{\\\"mso-id\\\":\\\"b5885412-8ecf-47fc-80d5-c4de8b4913f8\\\",\\\"bpmn-flow-name\\\":\\\"ActivateVfModuleBB\\\",\\\"key\\\":\\\"15bd6af8-aff5-4538-8b14-c92986ea2d4b\\\",\\\"is-virtual-link\\\":false,\\\"virtual-link-key\\\":null,\\\"scope\\\":null,\\\"action\\\":null},\\\"requestId\\\":\\\"1fc2ef3b-26f0-4e62-a00a-6a31502d39e2\\\",\\\"apiVersion\\\":\\\"7\\\",\\\"resourceId\\\":\\\"fde94d73-6fab-4b9e-9d48-01ca0840ca88\\\",\\\"requestAction\\\":\\\"createInstance\\\",\\\"vnfType\\\":\\\"FMGW-NC2-507-SVC/FMGW-NC2-507 0\\\",\\\"oldVolumeGroupName\\\":null,\\\"aLaCarte\\\":true,\\\"homing\\\":false,\\\"workflowResourceIds\\\":{\\\"serviceInstanceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"pnfId\\\":null,\\\"vnfId\\\":\\\"7a7387d0-f020-4297-9459-dfbf8869752e\\\",\\\"networkId\\\":\\\"\\\",\\\"volumeGroupId\\\":\\\"\\\",\\\"vfModuleId\\\":\\\"\\\",\\\"networkCollectionId\\\":null,\\\"configurationId\\\":null,\\\"instanceGroupId\\\":\\\"\\\"},\\\"requestDetails\\\":{\\\"modelInfo\\\":{\\\"modelCustomizationName\\\":\\\"FmgwNc2507..mbmsgw_scp_v2_54c_0820..module-13\\\",\\\"modelInvariantId\\\":\\\"19ad8bd1-9d83-43a1-94fa-bc5ee0bdd52a\\\",\\\"modelType\\\":\\\"vfModule\\\",\\\"modelId\\\":\\\"8c7aa631-d5e2-49ae-832b-d5b1b5c8cd36\\\",\\\"modelName\\\":\\\"FmgwNc2507..mbmsgw_scp_v2_54c_0820..module-13\\\",\\\"modelVersion\\\":\\\"2\\\",\\\"modelCustomizationUuid\\\":\\\"15bd6af8-aff5-4538-8b14-c92986ea2d4b\\\",\\\"modelVersionId\\\":\\\"8c7aa631-d5e2-49ae-832b-d5b1b5c8cd36\\\",\\\"modelCustomizationId\\\":\\\"15bd6af8-aff5-4538-8b14-c92986ea2d4b\\\",\\\"modelUuid\\\":\\\"8c7aa631-d5e2-49ae-832b-d5b1b5c8cd36\\\",\\\"modelInvariantUuid\\\":\\\"19ad8bd1-9d83-43a1-94fa-bc5ee0bdd52a\\\",\\\"modelInstanceName\\\":\\\"FmgwNc2507..mbmsgw_scp_v2_54c_0820..module-13\\\"},\\\"requestInfo\\\":{\\\"source\\\":\\\"VID\\\",\\\"instanceName\\\":\\\"zrdm54cfmgw01_sup_1\\\",\\\"suppressRollback\\\":false,\\\"requestorId\\\":\\\"cb4449\\\"},\\\"relatedInstanceList\\\":[{\\\"relatedInstance\\\":{\\\"instanceId\\\":\\\"937d9e51-03b9-416b-bccd-aa898a85d711\\\",\\\"modelInfo\\\":{\\\"modelInvariantId\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\",\\\"modelType\\\":\\\"service\\\",\\\"modelId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelName\\\":\\\"FMGW-NC2-507-SVC\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelVersionId\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelUuid\\\":\\\"c40d56a6-310c-4db9-8455-0aa723d36d53\\\",\\\"modelInvariantUuid\\\":\\\"2da904be-d12b-455c-8951-59ec7d207371\\\"}}},{\\\"relatedInstance\\\":{\\\"instanceId\\\":\\\"7a7387d0-f020-4297-9459-dfbf8869752e\\\",\\\"modelInfo\\\":{\\\"modelCustomizationName\\\":\\\"FMGW-NC2-507 0\\\",\\\"modelInvariantId\\\":\\\"bb32f2eb-8880-4993-b866-20835836fbf6\\\",\\\"modelType\\\":\\\"vnf\\\",\\\"modelId\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelName\\\":\\\"FMGW-NC2-507\\\",\\\"modelVersion\\\":\\\"12.0\\\",\\\"modelCustomizationUuid\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"modelVersionId\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelCustomizationId\\\":\\\"9b649dde-872b-417d-99bc-1f28916ebe50\\\",\\\"modelUuid\\\":\\\"94102fa8-6c0a-44ad-95ef-3a994e2aaf07\\\",\\\"modelInvariantUuid\\\":\\\"bb32f2eb-8880-4993-b866-20835836fbf6\\\",\\\"modelInstanceName\\\":\\\"FMGW-NC2-507 0\\\"}}}],\\\"cloudConfiguration\\\":{\\\"tenantId\\\":\\\"ad299b37da30413391e9c28138f0b0cd\\\",\\\"cloudOwner\\\":\\\"att-nc\\\",\\\"lcpCloudRegionId\\\":\\\"rdm54c\\\"},\\\"requestParameters\\\":{\\\"usePreload\\\":true,\\\"testApi\\\":\\\"GR_API\\\"}},\\\"configurationResourceKeys\\\":null}]\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        return json;
        
        
    }
}
