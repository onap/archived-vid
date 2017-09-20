package org.openecomp.vid.services;

import org.openecomp.vid.changeManagement.ChangeManagementRequest;
import org.json.simple.JSONArray;
import org.openecomp.vid.mso.rest.Request;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

public interface ChangeManagementService {
    Collection<Request> getMSOChangeManagements();
	ResponseEntity<String> doChangeManagement(ChangeManagementRequest request, String vnfName);
    JSONArray getSchedulerChangeManagements();
}
