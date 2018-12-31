package org.onap.vid.aai.model.AaiGetTenatns;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Oren on 7/18/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetTenantsResponse {

    public String cloudRegionID;

    public String cloudOwner;

    public String tenantName;

    public String tenantID;

    public boolean isPermitted;

    @JsonProperty("is-permitted")
    public boolean getJsonIsPermitted() {
        // this is a special case to *duplicate* the permission field
        // as it might be that both -- camelCase and hyphen faces --
        // are in use
        return isPermitted;
    }

    public GetTenantsResponse() {
    }

    public GetTenantsResponse(String cloudRegionId, String cloudOwner, String tenantName, String tenantID, boolean isPermitted) {
        this.cloudRegionID = cloudRegionId;
        this.cloudOwner = cloudOwner;
        this.tenantName = tenantName;
        this.tenantID = tenantID;
        this.isPermitted = isPermitted;
    }
}