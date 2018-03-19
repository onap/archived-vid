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