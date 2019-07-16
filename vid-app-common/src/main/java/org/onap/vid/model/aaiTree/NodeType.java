package org.onap.vid.model.aaiTree;

public enum NodeType {
    SERVICE_INSTANCE("service-instance", "service-instance-id", "service-instance-name", "service"),
    GENERIC_VNF ("generic-vnf", "vnf-id", "vnf-name", "vnf"),
    NETWORK ("l3-network", "network-id", "network-name", "network"),
    FAILURE ("failure_node", NodeType.NONE, NodeType.NONE, NodeType.NONE),
    COLLECTION_RESOURCE ("collection", "collection-id", "collection-name", "collection"),
    CONFIGURATION ("configuration", "configuration-id", "configuration-name", "configuration"),
    PNF ("pnf", "pnf-id", "pnf-name", "pnf"),
    VF_MODULE ("vf-module", "vf-module-id", "vf-module-name", "vfModule"),
    INSTANCE_GROUP ("instance-group", "id", "instance-group-name", "instanceGroup"),
    PORT ("l-interface", "interface-id", "interface-name", "connectionPoint"),
    VOLUME_GROUP ("volume-group", "volume-group-id", "volume-group-name", "volumeGroup"),
    VLAN_TAG("vlan-tag", "vlan-tag-id", NodeType.NONE, NodeType.NONE),
    VPN_BINDING("vpn-binding", "vpn-id", "vpn-name", "vpnBinding"),
    ;

    private String type;
    private String id;
    private String name;
    private String modelType;

    public static final String NONE = "";

    NodeType(String type, String id, String name, String modelType) {
        this.type = type;
        this.id = id;
        this.name = name;
        this.modelType = modelType;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getModelType() {
        return modelType;
    }

    public static NodeType fromString(String type) {
        for (NodeType nodeType : NodeType.values()) {
            if (nodeType.type.equalsIgnoreCase(type)) {
                return nodeType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.type;
    }
}