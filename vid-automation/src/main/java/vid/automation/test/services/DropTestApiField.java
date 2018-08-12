package vid.automation.test.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import vid.automation.test.infra.Features;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.UnaryOperator;

public class DropTestApiField {

    public static UnaryOperator<String> dropTestApiFieldFromString() {
        if (Features.FLAG_ADD_MSO_TESTAPI_FIELD.isActive()) {
            // do nothing
            return in -> in;
        } else {
            final ObjectMapper objectMapper = new ObjectMapper();
            return in -> {
                if (!in.contains("testApi")) {
                    // short circuit
                    return in;
                }

                try {
                    final JsonNode tree = objectMapper.readTree(in);
                    final JsonNode node = tree.path("simulatorRequest");
                    if (removePath(node, "body", "requestDetails", "requestParameters", "testApi") != null) {
                        // tree modified, write back to string
                        return objectMapper.writeValueAsString(tree);
                    } else {
                        // else...
                        return in;
                    }
                } catch (IOException e) {
                    return in;
                }
            };
        }
    }

    private static JsonNode removePath(JsonNode tree, String... nodes) {
        // remove the nodes; remove also the parent, if an empty object was left
        // returns the removed node
        // returns null if no modification to tree
        if (nodes.length > 1) {
            final JsonNode node = tree.path(nodes[0]);
            final JsonNode removed = removePath(node, Arrays.copyOfRange(nodes, 1, nodes.length));
            if (removed != null && node.size() == 0) {
                return removePath(tree, nodes[0]);
            } else {
                return removed; // non-null if node.size() != 0
            }
        } else {
            if (tree instanceof ObjectNode) {
                return ((ObjectNode) tree).remove(nodes[0]);
            } else {
                return null;
            }
        }
    }

}
