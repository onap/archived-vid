package org.onap.vid.changeManagement;

import java.util.ArrayList;
import java.util.List;

public class ChangeManagementResponse {
	public List<CmResponse> cmResponses = null;



	public ChangeManagementResponse(String vnfName) {
		List<CmResponse> cmResponses = new ArrayList<>();
		cmResponses.add(new CmResponse(vnfName));
		this.cmResponses = cmResponses;
	
	}


}
