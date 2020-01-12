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

package org.onap.vid.mso.model;

import static java.util.Collections.emptyList;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonPartEquals;
import static org.hamcrest.MatcherAssert.assertThat;

import org.testng.annotations.Test;

public class RequestParametersVfModuleTest {

    @Test
    public void RequestParametersVfModuleOrVolumeGroupInstantiation_whenUsePreloadIsNull_thenLiteralFalseIsSerialized() {
        Boolean usePreload = null;
        assertThat(
            new RequestParametersVfModuleOrVolumeGroupInstantiation(emptyList(), usePreload, ""),
            jsonPartEquals("usePreload", false)
        );
    }

    @Test
    public void RequestParametersVfModuleUpgrade_whenUsePreloadIsNull_thenLiteralFalseIsSerialized() {
        Boolean usePreload = null;
        assertThat(
            new RequestParametersVfModuleUpgrade(emptyList(), usePreload, "", false, false),
            jsonPartEquals("usePreload", false)
        );
    }

}
