package org.onap.vid.changeManagement;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.onap.vid.mso.model.CloudConfiguration;

/**
 * Created by Oren on 9/5/17.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LeanCloudConfiguration {

    public LeanCloudConfiguration() {
    }

    public LeanCloudConfiguration(CloudConfiguration cloudConfiguration) {
        this.tenantId = cloudConfiguration.getTenantId();
        this.lcpCloudRegionId = cloudConfiguration.getLcpCloudRegionId();
        this.cloudOwner = cloudConfiguration.getCloudOwner();

    }
    public String lcpCloudRegionId;

    public String tenantId;

    public String cloudOwner;

}
