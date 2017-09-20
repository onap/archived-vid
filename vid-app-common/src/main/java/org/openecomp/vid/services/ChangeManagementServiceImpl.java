package org.openecomp.vid.services;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.vid.changeManagement.ChangeManagementRequest;
import org.openecomp.vid.changeManagement.RequestDetails;
import org.openecomp.vid.mso.MsoBusinessLogic;
import org.openecomp.vid.mso.MsoResponseWrapper;
import org.openecomp.vid.controller.MsoController;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.vid.scheduler.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.openecomp.vid.mso.rest.Request;
import org.springframework.stereotype.Service;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.util.Date;
import java.util.List;
import java.util.Collection;


@Service
public class ChangeManagementServiceImpl implements ChangeManagementService {
    @Override
    public Collection<Request> getMSOChangeManagements() {
        Collection<Request> result = null;
		MsoBusinessLogic msoBusinessLogic = new MsoBusinessLogic();
		try {
            result = msoBusinessLogic.getOrchestrationRequestsForDashboard();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private RequestDetails findRequestByVnfName(List<RequestDetails> requests, String vnfName){

    	if (requests == null)
    		return null;

    	for(RequestDetails requestDetails: requests){
			if(requestDetails.getVnfName().equals(vnfName)){
				return requestDetails;
			}
		}

		return null;
	}

	@Override
	public ResponseEntity<String> doChangeManagement(ChangeManagementRequest request, String vnfName) {
		if (request == null)
			return null;
		ResponseEntity<String> response = null;
		RequestDetails currentRequestDetails = findRequestByVnfName(request.getRequestDetails(), vnfName);
		MsoResponseWrapper msoResponseWrapperObject = null;
		if(currentRequestDetails != null){
			MsoBusinessLogic msoBusinessLogicObject = new MsoBusinessLogic();
			String serviceInstanceId = currentRequestDetails.getRelatedInstList().get(0).getRelatedInstance().getInstanceId();
			String vnfInstanceId = currentRequestDetails.getVnfInstanceId();
			try {
				if (request.getRequestType().equalsIgnoreCase("update")) {
					
					 msoResponseWrapperObject = msoBusinessLogicObject.updateVnf(currentRequestDetails, serviceInstanceId, vnfInstanceId);
				}
				else if (request.getRequestType().equalsIgnoreCase("replace"))
				{
					msoResponseWrapperObject = msoBusinessLogicObject.replaceVnf(currentRequestDetails, serviceInstanceId, vnfInstanceId);
//					throw new NotImplementedException();
				}
				response = new ResponseEntity<String>(msoResponseWrapperObject.getResponse(), HttpStatus.OK);
				return response;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		// AH:TODO: return ChangeManagementResponse
		return null;
	}

    @Override
    public JSONArray getSchedulerChangeManagements() {
        JSONArray result = null;
        try {
            String path = SystemProperties.getProperty(SchedulerProperties.SCHEDULER_GET_SCHEDULES);
            org.openecomp.vid.scheduler.RestObject<String> restObject = new org.openecomp.vid.scheduler.RestObject<>();
            SchedulerRestInterfaceIfc restClient = SchedulerRestInterfaceFactory.getInstance();

            String str = new String();
            restObject.set(str);
            restClient.Get(str, "", path, restObject);
            String restCallResult = restObject.get();
            JSONParser parser = new JSONParser();
            Object parserResult = parser.parse(restCallResult);
            result = (JSONArray) parserResult;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
