/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia. All rights reserved.
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

package org.onap.vid.mso;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MsoUtilTest {

    @Test
    public void shouldWrapRestObject() {
        // given
        int statusCode = 200;
        String entity = "entity";
        RestObject<String> restObject = new RestObject<>();
        restObject.set(entity);
        restObject.setStatusCode(statusCode);
        // when
        MsoResponseWrapper result = MsoUtil.wrapResponse(restObject);
        // then
        assertThat(result.getEntity()).isEqualTo(entity);
        assertThat(result.getStatus()).isEqualTo(statusCode);
    }
}