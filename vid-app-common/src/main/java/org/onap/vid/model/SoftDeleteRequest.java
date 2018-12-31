package org.onap.vid.model;

public class SoftDeleteRequest {

    private String tenantId;

    private String lcpCloudRegionId;

    private String userId;

    public SoftDeleteRequest() {}

    public SoftDeleteRequest(String tenantId, String lcpCloudRegionId, String userId) {
        this.tenantId = tenantId;
        this.lcpCloudRegionId = lcpCloudRegionId;
        this.userId = userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getLcpCloudRegionId() {
        return lcpCloudRegionId;
    }

    public void setLcpCloudRegionId(String lcpCloudRegionId) {
        this.lcpCloudRegionId = lcpCloudRegionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
