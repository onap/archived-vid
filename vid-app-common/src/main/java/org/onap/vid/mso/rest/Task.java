package org.onap.vid.mso.rest;

import java.util.List;

public class Task {

    private String taskId;
    private String type;
    private String nfRole;
    private String subscriptionServiceType;
    private String originalRequestId;
    private String originalRequestorId;
    private String errorSource;
    private String errorCode;
    private String errorMessage;
    private String buildingBlockName;
    private String buildingBlockStep;
    private List<String> validResponses;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNfRole() {
        return nfRole;
    }

    public void setNfRole(String nfRole) {
        this.nfRole = nfRole;
    }

    public String getSubscriptionServiceType() {
        return subscriptionServiceType;
    }

    public void setSubscriptionServiceType(String subscriptionServiceType) {
        this.subscriptionServiceType = subscriptionServiceType;
    }

    public String getOriginalRequestId() {
        return originalRequestId;
    }

    public void setOriginalRequestId(String originalRequestId) {
        this.originalRequestId = originalRequestId;
    }

    public String getOriginalRequestorId() {
        return originalRequestorId;
    }

    public void setOriginalRequestorId(String originalRequestorId) {
        this.originalRequestorId = originalRequestorId;
    }

    public String getErrorSource() {
        return errorSource;
    }

    public void setErrorSource(String errorSource) {
        this.errorSource = errorSource;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getBuildingBlockName() {
        return buildingBlockName;
    }

    public void setBuildingBlockName(String buildingBlockName) {
        this.buildingBlockName = buildingBlockName;
    }

    public String getBuildingBlockStep() {
        return buildingBlockStep;
    }

    public void setBuildingBlockStep(String buildingBlockStep) {
        this.buildingBlockStep = buildingBlockStep;
    }

    public List<String> getValidResponses() {
        return validResponses;
    }

    public void setValidResponses(List<String> validResponses) {
        this.validResponses = validResponses;
    }



}
