package org.openecomp.vid.changeManagement;

public class InPlaceSoftwareUpdateRequest {

    public static class RequestDetails {
        public org.openecomp.vid.domain.mso.RequestInfo requestInfo;
        public RequestParameters requestParameters;
        public LeanCloudConfiguration cloudConfiguration;

        public RequestDetails(org.openecomp.vid.changeManagement.RequestDetails requestDetails) {
            requestInfo = requestDetails.getRequestInfo();
            requestParameters = requestDetails.getRequestParameters();
            cloudConfiguration = new LeanCloudConfiguration(requestDetails.getCloudConfiguration());
        }
    }

    public RequestDetails requestDetails;

    public InPlaceSoftwareUpdateRequest(RequestDetails requestDetails) {
        this.requestDetails = requestDetails;
    }
}
