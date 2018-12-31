package org.onap.vid.mso.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AsyncRequestStatusList {
    AsyncRequestStatusList(@JsonProperty("requestList") List<AsyncRequestStatus> requestList) {
        this.requestList = requestList;
    }

    public List<AsyncRequestStatus> getRequestList() {
        return requestList;
    }

    private final List<AsyncRequestStatus> requestList;
}
