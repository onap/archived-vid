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

package org.onap.vid.changeManagement;

import java.util.Map;

import org.junit.Test;

public class ModelInfoTest {

    private ModelInfo createTestSubject() {
        return new ModelInfo();
    }

    @Test
    public void testGetModelType() throws Exception {
        ModelInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModelType();
    }

    @Test
    public void testSetModelType() throws Exception {
        ModelInfo testSubject;
        String modelType = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setModelType(modelType);
    }

    @Test
    public void testGetModelInvariantId() throws Exception {
        ModelInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModelInvariantId();
    }

    @Test
    public void testSetModelInvariantId() throws Exception {
        ModelInfo testSubject;
        String modelInvariantId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setModelInvariantId(modelInvariantId);
    }

    @Test
    public void testGetModelVersionId() throws Exception {
        ModelInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModelVersionId();
    }

    @Test
    public void testSetModelVersionId() throws Exception {
        ModelInfo testSubject;
        String modelVersionId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setModelVersionId(modelVersionId);
    }

    @Test
    public void testGetModelName() throws Exception {
        ModelInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModelName();
    }

    @Test
    public void testSetModelName() throws Exception {
        ModelInfo testSubject;
        String modelName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setModelName(modelName);
    }

    @Test
    public void testGetModelVersion() throws Exception {
        ModelInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModelVersion();
    }

    @Test
    public void testSetModelVersion() throws Exception {
        ModelInfo testSubject;
        String modelVersion = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setModelVersion(modelVersion);
    }

    @Test
    public void testGetModelCustomizationName() throws Exception {
        ModelInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModelCustomizationName();
    }

    @Test
    public void testSetModelCustomizationName() throws Exception {
        ModelInfo testSubject;
        String modelCustomizationName = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setModelCustomizationName(modelCustomizationName);
    }

    @Test
    public void testGetModelCustomizationId() throws Exception {
        ModelInfo testSubject;
        String result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getModelCustomizationId();
    }

    @Test
    public void testSetModelCustomizationId() throws Exception {
        ModelInfo testSubject;
        String modelCustomizationId = "";

        // default test
        testSubject = createTestSubject();
        testSubject.setModelCustomizationId(modelCustomizationId);
    }

    @Test
    public void testGetAdditionalProperties() throws Exception {
        ModelInfo testSubject;
        Map<String, Object> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.getAdditionalProperties();
    }

    @Test
    public void testSetAdditionalProperty() throws Exception {
        ModelInfo testSubject;
        String name = "";
        Object value = null;

        // default test
        testSubject = createTestSubject();
        testSubject.setAdditionalProperty(name, value);
    }
}
