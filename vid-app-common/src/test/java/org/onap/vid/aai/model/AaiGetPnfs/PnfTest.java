/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia.
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

package org.onap.vid.aai.model.AaiGetPnfs;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PnfTest {

    @Test
    public void builder_shouldProperlyConstructObject() {
        Pnf pnf = Pnf.builder()
            .withPnfId("pnfId")
            .withPnfName("TestPnf")
            .withPnfName2("pnfName2")
            .withPnfName2Source("pnfNameSource")
            .withEquipModel("model")
            .withEquipType("type")
            .withEquipVendor("vendor")
            .build();

        assertThat(pnf.getPnfId()).isEqualTo("pnfId");
        assertThat(pnf.getPnfName()).isEqualTo("TestPnf");
        assertThat(pnf.getPnfName2()).isEqualTo("pnfName2");
        assertThat(pnf.getPnfName2Source()).isEqualTo("pnfNameSource");
        assertThat(pnf.getEquipModel()).isEqualTo("model");
        assertThat(pnf.getEquipType()).isEqualTo("type");
        assertThat(pnf.getEquipVendor()).isEqualTo("vendor");
    }
}
