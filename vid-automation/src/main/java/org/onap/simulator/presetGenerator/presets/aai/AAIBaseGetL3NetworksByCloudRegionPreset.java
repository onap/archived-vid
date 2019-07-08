package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableMap;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import java.util.*;

public class AAIBaseGetL3NetworksByCloudRegionPreset extends BaseAAIPreset {

    public static final String DEFAULT_TENANT_ID = "b530fc990b6d4334bd45518bebca6a51";
    public static final String DEFAULT_TENANT_NAME = "ecomp_ispt";
    public static final String DEFAULT_CLOUD_REGION_ID = PresetAAIGetCloudOwnersByCloudRegionId.AUK51A;
    private static final String DEFAULT_CLOUD_OWNER = PresetAAIGetCloudOwnersByCloudRegionId.ATT_NC;
    protected String tenantId;
    protected String tenantName;
    protected String cloudRegionId;
    protected String cloudOwner;
    protected String networkRole;

    public AAIBaseGetL3NetworksByCloudRegionPreset(String tenentId, String tenantName, String cloudRegionId, String cloudOwner, String networkRole) {
        this.tenantId = tenentId;
        this.tenantName = tenantName;
        this.cloudRegionId = cloudRegionId;
        this.cloudOwner = cloudOwner;
        this.networkRole = networkRole;
    }

    public AAIBaseGetL3NetworksByCloudRegionPreset() {
        this.tenantId = DEFAULT_TENANT_ID;
        this.tenantName = DEFAULT_TENANT_NAME;
        this.cloudRegionId = DEFAULT_CLOUD_REGION_ID;
        this.cloudOwner = DEFAULT_CLOUD_OWNER;
    }

    public String getTenantName() {
        return tenantName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getCloudRegionId() {
        return cloudRegionId;
    }

    public String getCloudOwner() {
        return cloudOwner;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.PUT;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/query";
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of(
                "format", Collections.singletonList("resource")
        );
    }

    @Override
    public Object getRequestBody() {
        return ImmutableMap.of(
                "start", "/cloud-infrastructure/cloud-regions/cloud-region/" + cloudOwner + "/" + cloudRegionId,
                "query", "query/l3-networks-by-cloud-region?tenantId=" + tenantId + (networkRole == null ? "" : "&networkRole=" + networkRole)
        );
    }
}
