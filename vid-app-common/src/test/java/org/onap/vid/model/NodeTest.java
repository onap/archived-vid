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

package org.onap.vid.model;

import org.junit.Test;
import org.onap.vid.asdc.beans.tosca.NodeTemplate;
import org.onap.vid.asdc.beans.tosca.ToscaMetadata;

public class NodeTest {

    private Node createTestSubject() {
        return new Node();
    }

    @Test
    public void testExtractNode() throws Exception {
        Node testSubject;
        NodeTemplate nodeTemplate = new NodeTemplate();
        nodeTemplate.setMetadata(new ToscaMetadata());

        // default test
        testSubject = createTestSubject();
        testSubject.extractNode(nodeTemplate);
    }
}
