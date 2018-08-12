package org.opencomp.simulator.presetGenerator.presets.aai;

import org.opencomp.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

/**
 * Created by itzikliderman on 21/12/2017.
 */
public class PresetAAIGetPNFByRegionErrorPut extends BaseAAIPreset {

    @Override
    public Object getResponseBody() {
        return "{" +
            "      \"start\": \"/business/customers/customer/e433710f-9217-458d-a79d-1c7aff376d89/service-subscriptions/service-subscription/VIRTUAL%20USP/service-instances?model-version-id=8a84e59b-45fe-4851-8ff1-34225a0b32c3&model-invariant-id=83b458fd-5dd3-419b-a9e3-7335814a0911\"," +
            "      \"query\": \"query/pnf-fromModel-byRegion?cloudRegionId=AAIAIC25&equipVendor=Cisco&equipModel=Nexus%203048-TP\"" +
            "    }";
    }

    @Override
    public int getResponseCode() {
        return 500;
    }

    public HttpMethod getReqMethod() {
        return HttpMethod.PUT;
    }

    public String getReqPath() {
        return getRootPath() + "/query";
    }
}
