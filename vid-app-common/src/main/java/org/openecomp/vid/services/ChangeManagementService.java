package org.openecomp.vid.services;

import org.openecomp.vid.changeManagement.*;
import org.json.simple.JSONArray;
import org.openecomp.vid.mso.rest.Request;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.List;

public interface ChangeManagementService {
    Collection<Request> getMSOChangeManagements();
	ResponseEntity<String> doChangeManagement(ChangeManagementRequest request, String vnfName);
    JSONArray getSchedulerChangeManagements();
    VnfWorkflowRelationResponse addVnfWorkflowRelation(VnfWorkflowRelationRequest vnfWorkflowRelationRequest);
    List<String> getWorkflowsForVnf(GetVnfWorkflowRelationRequest getVnfWorkflowRelationRequest);
    VnfWorkflowRelationResponse deleteVnfWorkflowRelation(VnfWorkflowRelationRequest vnfWorkflowRelationRequest);
    VnfWorkflowRelationAllResponse getAllVnfWorkflowRelations();
}
