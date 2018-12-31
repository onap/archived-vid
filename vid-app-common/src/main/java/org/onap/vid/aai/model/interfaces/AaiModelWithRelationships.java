package org.onap.vid.aai.model.interfaces;

import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.RelationshipList;

public interface AaiModelWithRelationships {
    RelationshipList getRelationshipList();
}
