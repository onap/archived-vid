package org.openecomp.vid.mso.rest.OperationalEnvironment;

import org.codehaus.jackson.annotate.JsonProperty;

public class RequestDetails {
    private RequestInfo requestInfo;
    private RelatedInstanceList relatedInstanceList;
    private RequestParameters requestParameters;

    public RequestDetails(RequestInfo requestInfo, RelatedInstanceList relatedInstanceList, RequestParameters requestParameters) {
        this.requestInfo = requestInfo;
        this.relatedInstanceList = relatedInstanceList;
        this.requestParameters = requestParameters;
    }

    @JsonProperty("requestInfo")
    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    @JsonProperty("relatedInstanceList")
    public RelatedInstanceList getRelatedInstanceList() {
        return relatedInstanceList;
    }

    public void setRelatedInstanceList(RelatedInstanceList relatedInstanceList) {
        this.relatedInstanceList = relatedInstanceList;
    }

    @JsonProperty("requestParameters")
    public RequestParameters getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(RequestParameters requestParameters) {
        this.requestParameters = requestParameters;
    }
}
