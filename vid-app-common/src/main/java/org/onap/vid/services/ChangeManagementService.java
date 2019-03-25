/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang3.tuple.Pair;
import org.onap.vid.changeManagement.*;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.mso.RestObjectWithRequestInfo;
import org.onap.vid.mso.rest.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ChangeManagementService {
    Collection<Request> getMSOChangeManagements();
	ResponseEntity<String> doChangeManagement(ChangeManagementRequest request, String vnfName);
    ArrayNode getSchedulerChangeManagements();
    RestObjectWithRequestInfo<ArrayNode> getSchedulerChangeManagementsWithRequestInfo();

    /**
     * Deleting a scheduled flow.
     * @param scheduleId - the ID of the schedule.
     * @return - a pair, left - String representation of the response, right - response code.
     */
    Pair<String, Integer> deleteSchedule(String scheduleId);
    VnfWorkflowRelationResponse addVnfWorkflowRelation(VnfWorkflowRelationRequest vnfWorkflowRelationRequest);
    List<String> getWorkflowsForVnf(GetVnfWorkflowRelationRequest getVnfWorkflowRelationRequest);
    VnfWorkflowRelationResponse deleteVnfWorkflowRelation(VnfWorkflowRelationRequest vnfWorkflowRelationRequest);
    VnfWorkflowRelationAllResponse getAllVnfWorkflowRelations();
    String uploadConfigUpdateFile(MultipartFile file);

    MsoResponseWrapper invokeVnfWorkflow(WorkflowRequestDetail request, UUID serviceInstanceId, UUID vnfInstanceId, UUID workflow_UUID);

}
