package org.openecomp.vid.services;

import org.openecomp.portalsdk.core.service.DataAccessService;
import org.openecomp.vid.changeManagement.*;
import org.openecomp.vid.exceptions.NotFoundException;
import org.openecomp.vid.model.VNFDao;
import org.openecomp.vid.model.VidWorkflow;
import org.openecomp.vid.mso.MsoBusinessLogic;
import org.openecomp.vid.mso.MsoResponseWrapper;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.vid.scheduler.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.openecomp.vid.mso.rest.Request;
import org.springframework.stereotype.Service;
import org.hibernate.NonUniqueObjectException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ChangeManagementServiceImpl implements ChangeManagementService {

	private final DataAccessService dataAccessService;

    @Autowired
    public ChangeManagementServiceImpl(DataAccessService dataAccessService) {
        this.dataAccessService = dataAccessService;
    }

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
		ResponseEntity<String> response;
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

	@Override
	public VnfWorkflowRelationResponse addVnfWorkflowRelation(VnfWorkflowRelationRequest vnfWorkflowRelationRequest) {
		VnfWorkflowRelationResponse vnfWorkflowRelationResponse = new VnfWorkflowRelationResponse();
    	for (WorkflowsDetail workflowsDetail: vnfWorkflowRelationRequest.getWorkflowsDetails()){
    		if (StringUtils.isEmpty(workflowsDetail.getVnfDetails().getUUID()) ||
					StringUtils.isEmpty(workflowsDetail.getVnfDetails().getInvariantUUID())) {
				vnfWorkflowRelationResponse.getErrors().add("Using empty UUID or invariantUUID is not allowed. Relation details: "+workflowsDetail.toString());
				continue;
			}
        	@SuppressWarnings("unchecked") List<VNFDao> vnfList = dataAccessService.getList(VNFDao.class, getVnfQueryString(workflowsDetail.getVnfDetails().getUUID(), workflowsDetail.getVnfDetails().getInvariantUUID()),null, null);
        	if (vnfList.size() == 0) {
        		vnfList.add(saveNewVnf(workflowsDetail));
            }
            @SuppressWarnings("unchecked") List<VidWorkflow> workflowList =  dataAccessService.getList(VidWorkflow.class,  String.format(" where wokflowName = '%s'", workflowsDetail.getWorkflowName()), null, null);
    		if (workflowList.size() == 0) {
    			vnfWorkflowRelationResponse.getErrors().add("Not Found instance of workflow " +  workflowsDetail.getWorkflowName() + " for vnf with UUID " + workflowsDetail.getVnfDetails().getUUID() + " and with invariantUUID " + workflowsDetail.getVnfDetails().getInvariantUUID());
    			continue; 
            }
    		vnfList.get(0).getWorkflows().add(workflowList.get(0));
    		try{
    		    dataAccessService.saveDomainObject(vnfList.get(0), null);
    		} catch(NonUniqueObjectException e){
    			//In case the relation already exists, we continue running on the list
    		}
    	}
    	return vnfWorkflowRelationResponse;
	}
	
	
	@Override
	public VnfWorkflowRelationResponse deleteVnfWorkflowRelation(VnfWorkflowRelationRequest vnfWorkflowRelationRequest) {
		VnfWorkflowRelationResponse vnfWorkflowRelationResponse = new VnfWorkflowRelationResponse();
	 	for(WorkflowsDetail workflowsDetail: vnfWorkflowRelationRequest.getWorkflowsDetails()){
            @SuppressWarnings("unchecked") List<VNFDao> vnfList = dataAccessService.getList(VNFDao.class, getVnfQueryString(workflowsDetail.getVnfDetails().getUUID(), workflowsDetail.getVnfDetails().getInvariantUUID()),null, null);
        	if (vnfList.size()!=1) {
        		vnfWorkflowRelationResponse.getErrors().add("Found " + vnfList.size() + " instances of vnf with UUID "  +workflowsDetail.getVnfDetails().getUUID() + " and vnfInvariantUUID " + workflowsDetail.getVnfDetails().getInvariantUUID());
        		continue;
            }
        	VidWorkflow vidWorkflow = getWorkflowOfVnf(vnfList.get(0), workflowsDetail.getWorkflowName());
    		if (vidWorkflow == null) {
    			vnfWorkflowRelationResponse.getErrors().add("Not Found instance of workflow " +  workflowsDetail.getWorkflowName() + " for vnf with UUID " + workflowsDetail.getVnfDetails().getUUID() + " and with invariantUUID " + workflowsDetail.getVnfDetails().getInvariantUUID());
    			continue;
            }
    		vnfList.get(0).getWorkflows().remove(vidWorkflow);
    		dataAccessService.saveDomainObject(vnfList.get(0), null);
	 	}
	 	return vnfWorkflowRelationResponse;
	 	
	}
	
	
    @Override
    public List<String> getWorkflowsForVnf(GetVnfWorkflowRelationRequest getVnfWorkflowRelationRequest) {
    	List<VNFDao> vnfDaoList = new ArrayList<>();
    	List<Set<String>> workflowsList = new ArrayList<>();
    	getVnfDaoList(vnfDaoList, getVnfWorkflowRelationRequest);
    	getWorkflowsList(workflowsList, vnfDaoList);
        return intersectWorkflows(workflowsList);
    }
      
    private void getVnfDaoList(List<VNFDao> vnfDaoList, GetVnfWorkflowRelationRequest getVnfWorkflowRelationRequest){
    	for(VnfDetails vnfDetails: getVnfWorkflowRelationRequest.getVnfDetails()){
            @SuppressWarnings("unchecked") List<VNFDao> vnfList = dataAccessService.getList(VNFDao.class,  getVnfQueryString(vnfDetails.getUUID(), vnfDetails.getInvariantUUID()),null, null);
        	if (vnfList.size() != 1) {
                throw new NotFoundException("Found" + vnfList.size() + " instances of vnf with UUID"  + vnfDetails.getUUID() + " and vnfInvariantUUID" + vnfDetails.getInvariantUUID());
            }
        	vnfDaoList.add(vnfList.get(0));
    	}
    }
    
    private void getWorkflowsList(List<Set<String>> workflowsList, List<VNFDao> vnfDaoList){
    	for(VNFDao vnfDao: vnfDaoList){
    		Set<String> tempWorkflows = vnfDao.getWorkflows().stream().map(VidWorkflow::getWokflowName).collect(Collectors.toSet());
    		workflowsList.add(tempWorkflows);
    	}
    }
    
    private List<String> intersectWorkflows(List<Set<String>> workflowsList){
        Set<String> workflows = workflowsList.get(0);
    	for(Set<String> workflow: workflowsList){
    		workflows.retainAll(workflow);
    	}
    	return new ArrayList<>(workflows);
    }
    
    private String getVnfQueryString(String UUID, String invariantUUID){
    	return " where vnfInvariantUUID = '" + invariantUUID + "' and vnfUUID = '" + UUID + "'";
    }
    
    private VidWorkflow getWorkflowOfVnf(VNFDao vnfDao, String workflowName){
	  VidWorkflow vidWorkflowRes = null;
    	for(VidWorkflow vidWorkflow: vnfDao.getWorkflows()){
    		if(vidWorkflow.getWokflowName().equals(workflowName)){
    			vidWorkflowRes = vidWorkflow;
    		}
    	}
    	return vidWorkflowRes;
    }
    
	private VNFDao saveNewVnf(WorkflowsDetail workflowsDetail){
		VNFDao vnfDao = new VNFDao();
		vnfDao.setVnfUUID(workflowsDetail.getVnfDetails().getUUID());
		vnfDao.setVnfInvariantUUID(workflowsDetail.getVnfDetails().getInvariantUUID());
		dataAccessService.saveDomainObject(vnfDao, null);
		return vnfDao;
	}

    @Override
    public VnfWorkflowRelationAllResponse getAllVnfWorkflowRelations() {
        @SuppressWarnings("unchecked") List<VNFDao> vnfList = dataAccessService.getList(VNFDao.class, null);
        return new VnfWorkflowRelationAllResponse(
            vnfList.stream()
                .map(VnfDetailsWithWorkflows::new)
                .collect(Collectors.toList()));
    }
}
