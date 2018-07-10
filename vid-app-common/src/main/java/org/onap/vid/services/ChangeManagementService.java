package org.onap.vid.services;

import org.apache.commons.lang3.tuple.Pair;
import org.json.simple.JSONArray;
import org.onap.vid.changeManagement.*;
import org.onap.vid.mso.rest.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

public interface ChangeManagementService {
    Collection<Request> getMSOChangeManagements();
	ResponseEntity<String> doChangeManagement(ChangeManagementRequest request, String vnfName);
    JSONArray getSchedulerChangeManagements();

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
