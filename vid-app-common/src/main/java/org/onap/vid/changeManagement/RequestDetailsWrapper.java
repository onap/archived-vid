package org.onap.vid.changeManagement;

/**
 * Created by Oren on 9/5/17.
 */
public class RequestDetailsWrapper<T> {

    public RequestDetailsWrapper(T requestDetails) {
        this.requestDetails = requestDetails;
    }

    public RequestDetailsWrapper() {
    }

    public T requestDetails;
}
