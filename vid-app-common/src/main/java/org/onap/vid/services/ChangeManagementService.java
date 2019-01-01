package org.onap.vid.services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang3.tuple.Pair;
import org.onap.vid.changeManagement.*;
import org.onap.vid.mso.RestObjectWithRequestInfo;
import org.onap.vid.mso.rest.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

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

}
