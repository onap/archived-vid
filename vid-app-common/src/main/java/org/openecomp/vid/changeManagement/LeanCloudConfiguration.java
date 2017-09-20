package org.openecomp.vid.changeManagement;

/**
 * Created by Oren on 9/5/17.
 */
public class LeanCloudConfiguration {

    public LeanCloudConfiguration() {
    }

    public LeanCloudConfiguration(org.openecomp.vid.domain.mso.CloudConfiguration cloudConfiguration) {
        this.tenantId = cloudConfiguration.getTenantId();
        this.lcpCloudRegionId = cloudConfiguration.getLcpCloudRegionId();

    }
    public String lcpCloudRegionId;

    public String tenantId;

}
