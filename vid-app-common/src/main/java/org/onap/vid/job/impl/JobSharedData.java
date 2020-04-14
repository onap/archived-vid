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

package org.onap.vid.job.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.onap.vid.job.JobAdapter;

import java.util.Objects;
import java.util.UUID;

public class JobSharedData {

    protected UUID jobUuid;
    protected String userId;
    protected Class requestType;
    protected UUID rootJobId;
    protected String testApi;

    @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, property="class")
    protected JobAdapter.AsyncJobRequest request;

    public JobSharedData() {
    }

    public JobSharedData(UUID jobUuid, String userId, JobAdapter.AsyncJobRequest request, String testApi) {
        this.jobUuid = jobUuid;
        this.userId = userId;
        this.requestType = request.getClass();
        this.request = request;
        this.rootJobId = jobUuid;
        this.testApi = testApi;
    }

    public JobSharedData(UUID jobUuid, JobAdapter.AsyncJobRequest request, JobSharedData parentData) {
        this(jobUuid, parentData.getUserId(), request, parentData.getTestApi());
        rootJobId = parentData.getRootJobId() != null ? parentData.getRootJobId() : parentData.getJobUuid();
    }


    public UUID getJobUuid() {
        return jobUuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Class getRequestType() {
        return requestType;
    }

    public void setRequestType(Class requestType) {
        this.requestType = requestType;
    }

    public JobAdapter.AsyncJobRequest getRequest() {
        return request;
    }

    public void setRequest(JobAdapter.AsyncJobRequest request) {
        this.request = request;
    }

    public UUID getRootJobId() {
        return rootJobId;
    }

    public String getTestApi() {
        return testApi;
    }

    public void setTestApi(String testApi) {
        this.testApi = testApi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) 
            return true;
        if (!(o instanceof JobSharedData))
            return false;
        JobSharedData that = (JobSharedData) o;
        return Objects.equals(getJobUuid(), that.getJobUuid()) &&
                Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getRequestType(), that.getRequestType()) &&
                Objects.equals(getRootJobId(), that.getRootJobId()) &&
                Objects.equals(getRequest(), that.getRequest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJobUuid(), getUserId(), getRequestType(), getRootJobId(), getRequest());
    }
}
