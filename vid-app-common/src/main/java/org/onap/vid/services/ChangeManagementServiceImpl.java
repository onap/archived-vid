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
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.NonUniqueObjectException;
import org.json.JSONObject;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.changeManagement.*;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.exceptions.NotFoundException;
import org.onap.vid.model.VNFDao;
import org.onap.vid.model.VidWorkflow;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoResponseWrapperInterface;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.RestObjectWithRequestInfo;
import org.onap.vid.mso.rest.Request;
import org.onap.vid.scheduler.SchedulerProperties;
import org.onap.vid.scheduler.SchedulerRestInterfaceIfc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ChangeManagementServiceImpl implements ChangeManagementService {

    private static final String PRIMARY_KEY = "payload";
    private static final Set<String> REQUIRED_KEYS = new HashSet<>(Arrays.asList("request-parameters", "configuration-parameters"));
    private final DataAccessService dataAccessService;
    private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ChangeManagementServiceImpl.class);
    private MsoBusinessLogic msoBusinessLogic;
    private final SchedulerRestInterfaceIfc restClient;
    private final CloudOwnerService cloudOwnerService;

    @Autowired
    private CsvService csvService;

    @Autowired
    public ChangeManagementServiceImpl(DataAccessService dataAccessService, MsoBusinessLogic msoBusinessLogic, SchedulerRestInterfaceIfc schedulerRestInterface, CloudOwnerService cloudOwnerService) {
        this.dataAccessService = dataAccessService;
        this.msoBusinessLogic = msoBusinessLogic;
        this.restClient = schedulerRestInterface;
        this.cloudOwnerService = cloudOwnerService;
    }

    @Override
    public Collection<Request> getMSOChangeManagements() {
            return msoBusinessLogic.getOrchestrationRequestsForDashboard();
    }

    protected RequestDetails findRequestByVnfName(List<RequestDetails> requests, String vnfName) {

        if (requests == null)
            return null;

        for (RequestDetails requestDetails : requests) {
            if (requestDetails.getVnfName().equals(vnfName)) {
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
        MsoResponseWrapperInterface msoResponseWrapperObject = null;
        if (currentRequestDetails != null) {

            String serviceInstanceId = extractServiceInstanceId(currentRequestDetails, request.getRequestType());
            String vnfInstanceId = extractVnfInstanceId(currentRequestDetails, request.getRequestType());
            String requestType = request.getRequestType();
            try {
                switch (requestType.toLowerCase()) {
                    case ChangeManagementRequest.UPDATE: {
                        cloudOwnerService.enrichRequestWithCloudOwner(currentRequestDetails);
                        msoResponseWrapperObject = msoBusinessLogic.updateVnf(currentRequestDetails, serviceInstanceId, vnfInstanceId);
                        break;
                    }
                    case ChangeManagementRequest.REPLACE: {
                        cloudOwnerService.enrichRequestWithCloudOwner(currentRequestDetails);
                        msoResponseWrapperObject = msoBusinessLogic.replaceVnf(currentRequestDetails, serviceInstanceId, vnfInstanceId);
                        break;
                    }
                    case ChangeManagementRequest.VNF_IN_PLACE_SOFTWARE_UPDATE: {
                        cloudOwnerService.enrichRequestWithCloudOwner(currentRequestDetails);
                        msoResponseWrapperObject = msoBusinessLogic.updateVnfSoftware(currentRequestDetails, serviceInstanceId, vnfInstanceId);
                        break;
                    }
                    case ChangeManagementRequest.CONFIG_UPDATE: {
                        msoResponseWrapperObject = msoBusinessLogic.updateVnfConfig(currentRequestDetails, serviceInstanceId, vnfInstanceId);
                        break;
                    }
                    case ChangeManagementRequest.SCALE_OUT:{
                        msoResponseWrapperObject = msoBusinessLogic.scaleOutVfModuleInstance(currentRequestDetails, serviceInstanceId, vnfInstanceId);
                        break;
                    }
                    default:
                        throw new GenericUncheckedException("Failure during doChangeManagement with request " + request.toString());
                }
                response = new ResponseEntity<>(msoResponseWrapperObject.getResponse(), HttpStatus.OK);
                return response;
            } catch (Exception e) {
                logger.error("Failure during doChangeManagement with request " + request.toString(), e);
                throw e;
            }

        }

        // AH:TODO: return ChangeManagementResponse
        return null;
    }

    private String extractVnfInstanceId(RequestDetails currentRequestDetails, String requestType) {
        if (currentRequestDetails.getVnfInstanceId() == null) {
            logger.error("Failed to extract vnfInstanceId");
            throw new BadRequestException("No vnfInstanceId in request " + requestType);
        }
        return currentRequestDetails.getVnfInstanceId();
    }

    protected String extractServiceInstanceId(RequestDetails currentRequestDetails, String requestType) {
        try {
            String serviceInstanceId = currentRequestDetails.getRelatedInstList().get(0).getRelatedInstance().getInstanceId();
            serviceInstanceId.toString(); //throw exception in case that serviceInstanceId is null...
            return serviceInstanceId;
        } catch (Exception e) {
            logger.error("Failed to extract serviceInstanceId");
            throw new BadRequestException("No instanceId in request " + requestType);
        }
    }

    @Override
    public RestObjectWithRequestInfo<ArrayNode> getSchedulerChangeManagementsWithRequestInfo() {
        String path = SystemProperties.getProperty(SchedulerProperties.SCHEDULER_GET_SCHEDULES);
        RestObject<ArrayNode> restObject = new RestObject<>();
        ArrayNode jsonArray = new ArrayNode(new JsonNodeFactory(true));
        restObject.set(jsonArray);
        return restClient.Get(jsonArray, path, restObject);
    }

    @Override
    public ArrayNode getSchedulerChangeManagements() {
        RestObjectWithRequestInfo<ArrayNode> responseWithRequestInfo = getSchedulerChangeManagementsWithRequestInfo();
        return responseWithRequestInfo.getRestObject().get();
    }

    @Override
    public Pair<String, Integer> deleteSchedule(String scheduleId) {
        try {
            String path = String.format(SystemProperties.getProperty(SchedulerProperties.SCHEDULER_DELETE_SCHEDULE), scheduleId);
            RestObject<String> restObject = new RestObject<>();
            String str = "";
            restObject.set(str);
            restClient.Delete(str, "", path, restObject);
            String restCallResult = restObject.get();
            return new ImmutablePair<>(restCallResult, restObject.getStatusCode());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ImmutablePair<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public VnfWorkflowRelationResponse addVnfWorkflowRelation(VnfWorkflowRelationRequest vnfWorkflowRelationRequest) {
        VnfWorkflowRelationResponse vnfWorkflowRelationResponse = new VnfWorkflowRelationResponse();
        for (WorkflowsDetail workflowsDetail : vnfWorkflowRelationRequest.getWorkflowsDetails()) {
            if (StringUtils.isEmpty(workflowsDetail.getVnfDetails().getUUID()) ||
                    StringUtils.isEmpty(workflowsDetail.getVnfDetails().getInvariantUUID())) {
                vnfWorkflowRelationResponse.getErrors().add("Using empty UUID or invariantUUID is not allowed. Relation details: " + workflowsDetail.toString());
                continue;
            }
            @SuppressWarnings("unchecked") List<VNFDao> vnfList = dataAccessService.getList(VNFDao.class, getVnfQueryString(workflowsDetail.getVnfDetails().getUUID(), workflowsDetail.getVnfDetails().getInvariantUUID()), null, null);
            if (vnfList.isEmpty()) {
                vnfList.add(saveNewVnf(workflowsDetail));
            }
            @SuppressWarnings("unchecked") List<VidWorkflow> workflowList = dataAccessService.getList(VidWorkflow.class, String.format(" where wokflowName = '%s'", workflowsDetail.getWorkflowName()), null, null);
            if (workflowList.isEmpty()) {
                vnfWorkflowRelationResponse.getErrors().add("Not Found instance of workflow " + workflowsDetail.getWorkflowName() + " for vnf with UUID " + workflowsDetail.getVnfDetails().getUUID() + " and with invariantUUID " + workflowsDetail.getVnfDetails().getInvariantUUID());
                continue;
            }
            vnfList.get(0).getWorkflows().add(workflowList.get(0));
            try {
                dataAccessService.saveDomainObject(vnfList.get(0), null);
            } catch (NonUniqueObjectException e) {
                //In case the relation already exists, we continue running on the list
            }
        }
        return vnfWorkflowRelationResponse;
    }

    @Override
    public VnfWorkflowRelationResponse deleteVnfWorkflowRelation(VnfWorkflowRelationRequest vnfWorkflowRelationRequest) {
        VnfWorkflowRelationResponse vnfWorkflowRelationResponse = new VnfWorkflowRelationResponse();
        for (WorkflowsDetail workflowsDetail : vnfWorkflowRelationRequest.getWorkflowsDetails()) {
            @SuppressWarnings("unchecked") List<VNFDao> vnfList = dataAccessService.getList(VNFDao.class, getVnfQueryString(workflowsDetail.getVnfDetails().getUUID(), workflowsDetail.getVnfDetails().getInvariantUUID()), null, null);
            if (vnfList.size() != 1) {
                vnfWorkflowRelationResponse.getErrors().add("Found " + vnfList.size() + " instances of vnf with UUID " + workflowsDetail.getVnfDetails().getUUID() + " and vnfInvariantUUID " + workflowsDetail.getVnfDetails().getInvariantUUID());
                continue;
            }
            VidWorkflow vidWorkflow = getWorkflowOfVnf(vnfList.get(0), workflowsDetail.getWorkflowName());
            if (vidWorkflow == null) {
                vnfWorkflowRelationResponse.getErrors().add("Not Found instance of workflow " + workflowsDetail.getWorkflowName() + " for vnf with UUID " + workflowsDetail.getVnfDetails().getUUID() + " and with invariantUUID " + workflowsDetail.getVnfDetails().getInvariantUUID());
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

    private void getVnfDaoList(List<VNFDao> vnfDaoList, GetVnfWorkflowRelationRequest getVnfWorkflowRelationRequest) {
        for (VnfDetails vnfDetails : getVnfWorkflowRelationRequest.getVnfDetails()) {
            @SuppressWarnings("unchecked") List<VNFDao> vnfList = dataAccessService.getList(VNFDao.class, getVnfQueryString(vnfDetails.getUUID(), vnfDetails.getInvariantUUID()), null, null);
            if (vnfList.size() != 1) {
                throw new NotFoundException("Found" + vnfList.size() + " instances of vnf with UUID" + vnfDetails.getUUID() + " and vnfInvariantUUID" + vnfDetails.getInvariantUUID());
            }
            vnfDaoList.add(vnfList.get(0));
        }
    }

    private void getWorkflowsList(List<Set<String>> workflowsList, List<VNFDao> vnfDaoList) {
        for (VNFDao vnfDao : vnfDaoList) {
            Set<String> tempWorkflows = vnfDao.getWorkflows().stream().map(VidWorkflow::getWokflowName).collect(Collectors.toSet());
            workflowsList.add(tempWorkflows);
        }
    }

    private List<String> intersectWorkflows(List<Set<String>> workflowsList) {
        Set<String> workflows = workflowsList.get(0);
        for (Set<String> workflow : workflowsList) {
            workflows.retainAll(workflow);
        }
        return new ArrayList<>(workflows);
    }

    private String getVnfQueryString(String UUID, String invariantUUID) {
        return " where vnfInvariantUUID = '" + invariantUUID + "' and vnfUUID = '" + UUID + "'";
    }

    private VidWorkflow getWorkflowOfVnf(VNFDao vnfDao, String workflowName) {
        VidWorkflow vidWorkflowRes = null;
        for (VidWorkflow vidWorkflow : vnfDao.getWorkflows()) {
            if (vidWorkflow.getWokflowName().equals(workflowName)) {
                vidWorkflowRes = vidWorkflow;
            }
        }
        return vidWorkflowRes;
    }

    private VNFDao saveNewVnf(WorkflowsDetail workflowsDetail) {
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

    @Override
    public String uploadConfigUpdateFile(MultipartFile file) {
        JSONObject json = null;
        try {
            json = csvService.convertCsvToJson(csvService.readCsv(file));
        } catch (InstantiationException | IllegalAccessException | IOException e) {
            throw new BadRequestException("Invalid csv file", e);
        }
        if (!validateJsonOutput(json))
            throw new BadRequestException("Invalid csv file");
        json = json.getJSONObject(PRIMARY_KEY);
        json = new JSONObject().put(PRIMARY_KEY, json.toString());
        return json.toString();
    }

    private boolean validateJsonOutput(org.json.JSONObject json) {
        if (!json.has(PRIMARY_KEY) || !json.getJSONObject(PRIMARY_KEY).keySet().containsAll(REQUIRED_KEYS))
            return false;
        return true;
    }
}
