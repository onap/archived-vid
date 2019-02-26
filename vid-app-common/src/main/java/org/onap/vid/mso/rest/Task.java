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
