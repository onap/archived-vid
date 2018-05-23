/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 Nokia. All rights reserved.
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

package org.onap.vid.aai.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.Test;

public class CustomJacksonJaxBJsonProviderTest {

    private CustomJacksonJaxBJsonProvider createTestSubject() {
        return new CustomJacksonJaxBJsonProvider();
    }

    @Test
    public void testMapperHasCorrectConfig() throws Exception {
        CustomJacksonJaxBJsonProvider testSubject = createTestSubject();
        DeserializationConfig deserializationConfig = testSubject.getMapper().getDeserializationConfig();
        SerializationConfig serializationConfig = testSubject.getMapper().getSerializationConfig();

        Assert.assertFalse(serializationConfig.hasSerializationFeatures(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS.getMask()));
        Assert.assertFalse(serializationConfig.hasSerializationFeatures(SerializationFeature.INDENT_OUTPUT.getMask()));
        Assert.assertFalse(serializationConfig.hasSerializationFeatures(SerializationFeature.WRAP_ROOT_VALUE.getMask()));
        Assert.assertFalse(serializationConfig.hasSerializationFeatures(SerializationFeature.CLOSE_CLOSEABLE.getMask()));

        Assert.assertFalse(deserializationConfig.hasDeserializationFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES.getMask()));
        Assert.assertFalse(deserializationConfig.hasDeserializationFeatures(DeserializationFeature.UNWRAP_ROOT_VALUE.getMask()));

        Assert.assertEquals(serializationConfig.getSerializationInclusion(), JsonInclude.Include.NON_NULL);
    }
}