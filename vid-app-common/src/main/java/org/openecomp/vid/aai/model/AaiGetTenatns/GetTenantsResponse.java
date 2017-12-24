package org.openecomp.vid.aai.model.AaiGetTenatns;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Oren on 7/18/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetTenantsResponse {

    @JsonProperty("cloudRegionID")
    public String cloudRegionId;

    @JsonProperty("tenantName")
    public String tenantName;

    @JsonProperty("tenantID")
    public String tenantID;

    @JsonProperty("is-permitted")
    public boolean isPermitted;

    public GetTenantsResponse() {
    }

    public GetTenantsResponse(String cloudRegionId, String tenantName, String tenantID, boolean isPermitted) {
        this.cloudRegionId = cloudRegionId;
        this.tenantName = tenantName;
        this.tenantID = tenantID;
        this.isPermitted = isPermitted;
    }
}