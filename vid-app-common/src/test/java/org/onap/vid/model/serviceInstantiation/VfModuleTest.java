package org.onap.vid.model.serviceInstantiation;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.AllOf.allOf;
import static org.onap.vid.testUtils.TestUtils.setStringsInStringProperties;

import org.onap.vid.mso.model.ModelInfo;
import org.testng.annotations.Test;

public class VfModuleTest {

    @Test
    public void cloneWithLcpCloudRegionIdAndTenantId() {
        String targetLcpCloudRegionId = "dictated lcpCloudRegionId";
        String targetTenantId = "dictated tenantId";

        VfModule originVfModule = createVfModule();

        assertThat(originVfModule.cloneWith(targetLcpCloudRegionId, targetTenantId), allOf(
            hasProperty("lcpCloudRegionId", equalTo(targetLcpCloudRegionId)),
            hasProperty("tenantId", equalTo(targetTenantId)),
            jsonEquals(originVfModule).whenIgnoringPaths("lcpCloudRegionId", "tenantId")
        ));

        // verify vfModule did not mutate
        assertThat(originVfModule, jsonEquals(createVfModule()));
    }

    private VfModule createVfModule() {
        VfModule vfModule = new VfModule(
            setStringsInStringProperties(new ModelInfo()),
            null, null, null, null, null,
            null, null, null, true, true,
            null, null, true, null, true,
            true, null, null);

        return setStringsInStringProperties(vfModule);
    }
}