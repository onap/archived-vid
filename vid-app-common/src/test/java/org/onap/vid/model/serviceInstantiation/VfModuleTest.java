/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2020 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.model.serviceInstantiation;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.AllOf.allOf;
import static org.onap.vid.testUtils.TestUtils.setStringsInStringFields;

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
            setStringsInStringFields(new ModelInfo()),
            null, null, null, null, null,
            null, null, null, true, true,
            null, null, true, null, true,
            true, null, null);

        return setStringsInStringFields(vfModule);
    }
}