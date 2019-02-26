/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.aai.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class PnfResultTest {

    private PnfResult pnfResult;

    @Before
    public void setUp(){
        pnfResult = new PnfResult();
        pnfResult.setAdditionalProperty("key1", "value1");
        pnfResult.setAdditionalProperty("key2", "value2");
    }

    @Test
    public void shouldHaveValidGettersAndSetters() throws IOException {
        String result = new ObjectMapper().writeValueAsString(pnfResult);
        assertThat(result, containsString("key1"));
        assertThat(result, containsString("value2"));
        assertThat(result, containsString("key2"));
        assertThat(result, containsString("value2"));
    }
}
