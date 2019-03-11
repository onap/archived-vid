package org.onap.simulator.presetGenerator.presets.scheduler;

import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.springframework.http.HttpMethod;

public class PresetGetSchedulerChangeManagements extends BasePreset {

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/scheduleDetails/";
    }

    @Override
    protected String getRootPath() {
        return "/scheduler/v1/ChangeManagement/schedules";
    }

    @Override
    public Object getResponseBody() {
        return "[" +
                "  {" +
                "    \"vnfName\": \"dbox0001v\"," +
                "    \"status\": \"Triggered\"," +
                "    \"aotsChangeId\": \"CHG000000000001\"," +
                "    \"aotsApprovalStatus\": \"Approved\"," +
                "    \"groupId\": \"groupId\"," +
                "    \"dispatchTime\": \"2018-05-09T14:05:43Z\"," +
                "    \"msoRequestId\": \"2fb4edd1-01c4-4fee-bd4a-4ae6282aa213\"," +
                "    \"scheduleRequest\": {" +
                "      \"id\": 1," +
                "      \"createDateTime\": \"2018-05-09T14:05:34Z\"," +
                "      \"optimizerAttemptsToSchedule\": 0," +
                "      \"optimizerTransactionId\": \"70f05563-6705-4be0-802a-8b6b78a69d63\"," +
                "      \"scheduleId\": \"70f05563-6705-4be0-802a-8b6b78a69d63\"," +
                "      \"scheduleName\": \"70f05563-6705-4be0-802a-8b6b78a69d63\"," +
                "      \"status\": \"Notifications Initiated\"," +
                "      \"userId\": \"wl849v\"," +
                "      \"domain\": \"ChangeManagement\"," +
                "      \"domainData\": [" +
                "        {" +
                "          \"id\": 1," +
                "          \"name\": \"WorkflowName\"," +
                "          \"value\": \"VNF In Place Software Update\"" +
                "        }," +
                "        {" +
                "          \"id\": 2," +
                "          \"name\": \"CallbackUrl\"," +
                "          \"value\": \"https://vid.onap.org:8000/vid/change-management/workflow/\"" +
                "        }," +
                "        {" +
                "          \"id\": 3," +
                "          \"name\": \"CallbackData\"," +
                "          \"value\": \"{\\\"requestType\\\":\\\"VNF In Place Software Update\\\",\\\"requestDetails\\\":[{\\\"vnfName\\\":\\\"dbox0001v\\\",\\\"vnfInstanceId\\\":\\\"815d38c0-b686-491c-9a74-0b49add524ca\\\",\\\"modelInfo\\\":{\\\"modelType\\\":\\\"vnf\\\",\\\"modelInvariantId\\\":\\\"59f4e0b2-e1b0-4e3b-bae3-e7b8c5d32985\\\",\\\"modelVersionId\\\":\\\"345643c1-3a51-423f-aac1-502e027d8dab\\\",\\\"modelName\\\":\\\"dbox0001v\\\",\\\"modelCustomizationId\\\":\\\"01ce23cb-d276-4d71-a5f1-f9d42d0df543\\\"},\\\"cloudConfiguration\\\":{\\\"lcpCloudRegionId\\\":\\\"dpa2b\\\",\\\"tenantId\\\":\\\"b60da4f71c1d4b35b8113d4eca6deaa1\\\"},\\\"requestInfo\\\":{\\\"source\\\":\\\"VID\\\",\\\"suppressRollback\\\":false,\\\"requestorId\\\":\\\"wl849v\\\"},\\\"relatedInstanceList\\\":[{\\\"relatedInstance\\\":{\\\"instanceId\\\":\\\"eb774932-e9fa-4c7f-bbc1-229b6b2b11e2\\\",\\\"modelInfo\\\":{\\\"modelType\\\":\\\"service\\\",\\\"modelInvariantId\\\":\\\"57dd617b-d64e-4441-a287-4d158b24ba65\\\",\\\"modelVersionId\\\":\\\"345643c1-3a51-423f-aac1-502e027d8dab\\\",\\\"modelName\\\":\\\"control_loop_dbe_svc\\\",\\\"modelVersion\\\":\\\"2.0\\\"}}}],\\\"requestParameters\\\":{\\\"payload\\\":\\\"{\\\\\\\"existing_software_version\\\\\\\":\\\\\\\"2\\\\\\\",\\\\\\\"new_software_version\\\\\\\":\\\\\\\"1\\\\\\\",\\\\\\\"operations_timeout\\\\\\\":\\\\\\\"3\\\\\\\"}\\\",\\\"testApi\\\":\\\"GR_API\\\"}}]}\"" +
                "        }" +
                "      ]," +
                "      \"scheduleApprovals\": []" +
                "    }," +
                "    \"schedulesId\": 0" +
                "  }," +
                "  {" +
                "    \"vnfName\": \"aaiaic25ctsf0002v\"," +
                "    \"vnfId\": \"\"," +
                "    \"status\": \"Cancelled\"," +
                "    \"aotsChangeId\": \"CHG000000000001\"," +
                "    \"aotsApprovalStatus\": \"Approved\"," +
                "    \"startTime\": \"2018-06-07T04:00:24Z\"," +
                "    \"finishTime\": \"2018-06-07T04:02:14Z\"," +
                "    \"groupId\": \"group\"," +
                "    \"lastInstanceStartTime\": \"2018-06-07T04:00:24Z\"," +
                "    \"policyId\": \"SNIRO_CM.TimeLimitAndVerticalTopology_pserver\"," +
                "    \"scheduleRequest\": {" +
                "      \"id\": 5," +
                "      \"createDateTime\": \"2018-06-06T13:00:25Z\"," +
                "      \"optimizerDateTime\": \"2018-06-06T13:00:34Z\"," +
                "      \"optimizerMessage\": \"A feasible scheduled has been found\"," +
                "      \"optimizerStatus\": \"complete\"," +
                "      \"optimizerAttemptsToSchedule\": 0," +
                "      \"optimizerReturnDateTime\": \"2018-06-06T13:07:02Z\"," +
                "      \"optimizerTransactionId\": \"01da00a6-9664-4db6-b97d-50847bc05144\"," +
                "      \"schedule\": \"[{\\\"groupId\\\":\\\"group\\\",\\\"startTime\\\":\\\"2018-06-07 04:00:24\\\",\\\"finishTime\\\":\\\"2018-06-07 04:02:14\\\",\\\"latestInstanceStartTime\\\":\\\"2018-06-07 04:00:24\\\",\\\"node\\\":[\\\"aaiaic25ctsf0002v\\\"]}]\"," +
                "      \"scheduleId\": \"01da00a6-9664-4db6-b97d-50847bc05144\"," +
                "      \"scheduleName\": \"01da00a6-9664-4db6-b97d-50847bc05144\"," +
                "      \"status\": \"Cancelled\"," +
                "      \"userId\": \"jf9860\"," +
                "      \"domain\": \"ChangeManagement\"," +
                "      \"deleteDateTime\": \"2018-06-06T13:07:11Z\"," +
                "      \"domainData\": [" +
                "        {" +
                "          \"id\": 5," +
                "          \"name\": \"CallbackData\"," +
                "          \"value\": \"{\\\"requestType\\\": \\\"Update\\\", \\\"requestDetails\\\": [{\\\"vnfInstanceId\\\": \\\"b8d99523-1e83-4fd1-b42f-849361ef7024\\\", \\\"relatedInstanceList\\\": [{\\\"relatedInstance\\\": {\\\"instanceId\\\": \\\"54ba3628-9ee5-4b32-8a2a-3abf001bed4e\\\", \\\"modelInfo\\\": {\\\"modelVersionId\\\": \\\"4ec07a2d-6bb5-4373-8ed6-4bc7ac1246fd\\\", \\\"modelVersion\\\": \\\"4.0\\\", \\\"modelName\\\": \\\"CHARLOTTE_2017_1011\\\", \\\"modelInvariantId\\\": \\\"e58733ef-43cb-4b6b-b641-922078b6c88b\\\", \\\"modelType\\\": \\\"service\\\"}}}], \\\"requestParameters\\\": {\\\"usePreload\\\": true}, \\\"requestInfo\\\": {\\\"source\\\": \\\"VID\\\", \\\"requestorId\\\": \\\"az2016\\\", \\\"suppressRollback\\\": false}, \\\"vnfName\\\": \\\"CHARLOTTE_2017_1011_oh22u_20171103\\\", \\\"modelInfo\\\": {\\\"modelVersionId\\\": \\\"4ec07a2d-6bb5-4373-8ed6-4bc7ac1246fd\\\", \\\"modelName\\\": \\\"CHARLOTTE_2017_1011_oh22u_20171103\\\", \\\"modelInvariantId\\\": \\\"93e16072-715a-42ef-9d0a-080052d6b716\\\", \\\"modelType\\\": \\\"vnf\\\", \\\"modelCustomizationId\\\": \\\"1779a999-ea17-4f31-98e9-75b6fbdd0acb\\\"}, \\\"cloudConfiguration\\\": {\\\"tenantId\\\": \\\"88a6ca3ee0394ade9403f075db23167e\\\", \\\"lcpCloudRegionId\\\": \\\"mdt1\\\"}}]}\"" +
                "        }," +
                "        {" +
                "          \"id\": 9," +
                "          \"name\": \"WorkflowName\"," +
                "          \"value\": \"Update\"" +
                "        }," +
                "        {" +
                "          \"id\": 13," +
                "          \"name\": \"CallbackUrl\"," +
                "          \"value\": \"http://127.0.0.1:8900/scheduler/v1/loopbacktest/vid\"" +
                "        }" +
                "      ]," +
                "      \"scheduleApprovals\": [" +
                "        {" +
                "          \"approvalDateTime\": \"2018-06-06T13:07:08Z\"," +
                "          \"status\": \"Accepted\"," +
                "          \"userId\": \"jf9860\"," +
                "          \"approvalTypeId\": 1" +
                "        }" +
                "      ]" +
                "    }," +
                "    \"schedulesId\": 0" +
                "  }," +
                "  {" +
                "    \"vnfName\": \"Zolson1MMSC04ee0f\"," +
                "    \"status\": \"Pending Schedule\"," +
                "    \"groupId\": \"\"," +
                "    \"policyId\": \"SNIRO.TimeLimitAndVerticalTopology\"," +
                "    \"scheduleRequest\": {" +
                "      \"id\": 9," +
                "      \"createDateTime\": \"2018-06-06T13:07:16Z\"," +
                "      \"optimizerDateTime\": \"2018-06-06T13:07:24Z\"," +
                "      \"optimizerMessage\": \"Error while processing request ID: CM-502adbb3-1c37-4cc6-bc9f-6777ad4227a3 -- Failed Processing; cause: Traceback (most recent call last):\\n  File \\\"/opt/app/sniro/sniroapp/sniro/optimizers/cmopt/rcscheduler/local_opt_processor.py\\\", line 15, in process_local_cm_scheduler_opt\\n    full_process_local_cm_scheduler_opt(request_json, policies, sniro_config)\\n  File \\\"/opt/app/sniro/sniroapp/sniro/optimizers/cmopt/rcscheduler/local_opt_processor.py\\\", line 38, in full_process_local_cm_scheduler_opt\\n    json_req = create_cm_scheduler_request(request_json, policies, all_req_info)\\n  File \\\"/opt/app/sniro/sniroapp/sniro/optimizers/cmopt/rcscheduler/request_builder.py\\\", line 16, in create_cm_scheduler_request\\n    cm = vertical_topology.VerticalConflicts(request_json)  # TODO: rename + refactor\\n  File \\\"/opt/app/sniro/sniroapp/sniro/datasources/aai/vertical_topology.py\\\", line 68, in __init__\\n    self.tzinfo = dict((x, tzxref[self.clli[x]]) for x in self.vnfs)\\n  File \\\"/opt/app/sniro/sniroapp/sniro/datasources/aai/vertical_topology.py\\\", line 68, in <genexpr>\\n    self.tzinfo = dict((x, tzxref[self.clli[x]]) for x in self.vnfs)\\nKeyError: ('Zolson1MMSC04ee0f', '(Unable to get timezones for some elements)')\\n\"," +
                "      \"optimizerStatus\": \"failed\"," +
                "      \"optimizerAttemptsToSchedule\": 0," +
                "      \"optimizerReturnDateTime\": \"2018-06-06T13:13:47Z\"," +
                "      \"optimizerTransactionId\": \"645c4cfb-e00f-4995-bf23-a7df892eee0f\"," +
                "      \"scheduleId\": \"645c4cfb-e00f-4995-bf23-a7df892eee0f\"," +
                "      \"scheduleName\": \"645c4cfb-e00f-4995-bf23-a7df892eee0f\"," +
                "      \"status\": \"Optimization Failed\"," +
                "      \"userId\": \"jf9860\"," +
                "      \"domain\": \"ChangeManagement\"," +
                "      \"domainData\": [" +
                "        {" +
                "          \"id\": 17," +
                "          \"name\": \"CallbackData\"," +
                "          \"value\": \"{\\\"requestType\\\": \\\"Update\\\", \\\"requestDetails\\\": [{\\\"vnfInstanceId\\\": \\\"b8d99523-1e83-4fd1-b42f-849361ef7024\\\", \\\"relatedInstanceList\\\": [{\\\"relatedInstance\\\": {\\\"instanceId\\\": \\\"54ba3628-9ee5-4b32-8a2a-3abf001bed4e\\\", \\\"modelInfo\\\": {\\\"modelVersionId\\\": \\\"4ec07a2d-6bb5-4373-8ed6-4bc7ac1246fd\\\", \\\"modelVersion\\\": \\\"4.0\\\", \\\"modelName\\\": \\\"CHARLOTTE_2017_1011\\\", \\\"modelInvariantId\\\": \\\"e58733ef-43cb-4b6b-b641-922078b6c88b\\\", \\\"modelType\\\": \\\"service\\\"}}}], \\\"requestParameters\\\": {\\\"usePreload\\\": true}, \\\"requestInfo\\\": {\\\"source\\\": \\\"VID\\\", \\\"requestorId\\\": \\\"az2016\\\", \\\"suppressRollback\\\": false}, \\\"vnfName\\\": \\\"CHARLOTTE_2017_1011_oh22u_20171103\\\", \\\"modelInfo\\\": {\\\"modelVersionId\\\": \\\"4ec07a2d-6bb5-4373-8ed6-4bc7ac1246fd\\\", \\\"modelName\\\": \\\"CHARLOTTE_2017_1011_oh22u_20171103\\\", \\\"modelInvariantId\\\": \\\"93e16072-715a-42ef-9d0a-080052d6b716\\\", \\\"modelType\\\": \\\"vnf\\\", \\\"modelCustomizationId\\\": \\\"1779a999-ea17-4f31-98e9-75b6fbdd0acb\\\"}, \\\"cloudConfiguration\\\": {\\\"tenantId\\\": \\\"88a6ca3ee0394ade9403f075db23167e\\\", \\\"lcpCloudRegionId\\\": \\\"mdt1\\\"}}]}\"" +
                "        }," +
                "        {" +
                "          \"id\": 21," +
                "          \"name\": \"WorkflowName\"," +
                "          \"value\": \"Replace\"" +
                "        }," +
                "        {" +
                "          \"id\": 25," +
                "          \"name\": \"CallbackUrl\"," +
                "          \"value\": \"http://127.0.0.1:8900/scheduler/v1/loopbacktest/vid\"" +
                "        }" +
                "      ]," +
                "      \"scheduleApprovals\": []" +
                "    }," +
                "    \"schedulesId\": 0" +
                "  }" +
                "]";
    }
}
