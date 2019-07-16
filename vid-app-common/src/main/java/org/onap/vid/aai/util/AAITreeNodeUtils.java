package org.onap.vid.aai.util;

import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.*;

import java.util.List;
import java.util.Optional;

public class AAITreeNodeUtils {

    private AAITreeNodeUtils() {
    }

    public static Optional<Relationship> findFirstRelationshipByRelatedTo(RelationshipList relationshipList, String relatedTo) {
        if (relationshipList==null || relationshipList.getRelationship()==null) {
            return Optional.empty();
        }
        return relationshipList.getRelationship().stream().filter(x->relatedTo.equals(x.getRelatedTo())).findFirst();
    }

    public static <T extends KeyValueModel> Optional<String> findFirstValue(List<T> data, String key) {
        if (data==null || data.isEmpty()) {
            return Optional.empty();
        }
        Optional<T> optValue = data.stream().filter(x->key.equals(x.getKey())).findFirst();
        return optValue.map(KeyValueModel::getValue);
    }

}
