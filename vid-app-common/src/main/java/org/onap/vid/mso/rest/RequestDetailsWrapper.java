package org.onap.vid.mso.rest;

public final class RequestDetailsWrapper {

    private final RequestDetails requestDetails;

    public RequestDetailsWrapper(RequestDetails requestDetails) {
        this.requestDetails = requestDetails;
    }

    public RequestDetails getRequestDetails() {
        return requestDetails;
    }
}
