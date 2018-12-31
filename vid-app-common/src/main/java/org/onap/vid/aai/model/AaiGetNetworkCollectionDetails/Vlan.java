package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.aai.model.interfaces.AaiModelWithRelationships;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vlan implements AaiModelWithRelationships {

    public Vlan(
            @JsonProperty("vlan-interface") String vlanInterface,
            @JsonProperty("vlan-id-inner") String vlanIdInner,
            @JsonProperty("relationship-list") RelationshipList relationshipList) {
        this.vlanInterface = vlanInterface;
        this.vlanIdInner = vlanIdInner;
        this.relationshipList = relationshipList;
    }

    @JsonProperty("vlan-interface")
    private final String vlanInterface;

    @JsonProperty("vlan-id-inner")
    private final String vlanIdInner;

    @JsonProperty("relationship-list")
    public final RelationshipList relationshipList;

    public String getVlanInterface() {
        return vlanInterface;
    }

    public String getVlanIdInner() {
        return vlanIdInner;
    }

    @Override
    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

}
