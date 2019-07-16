package org.onap.vid.model.aaiTree;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.collections.CollectionUtils;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Relationship;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.RelationshipList;
import org.onap.vid.aai.util.AAITreeNodeUtils;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.onap.vid.aai.util.AAITreeConverter.*;

@JsonInclude(NON_NULL)
public class Network extends Node {

    private String role;
    private String physicalName;
    private String serviceName;
    private String serviceUUID;
    private String tenantName;
    private Boolean isBoundToVpn;
    private RouteTarget routeTarget;

    public Network(){}

    private Network(AAITreeNode node) {
        super(node);
        fillCloudConfigurationProperties(this, node.getCloudConfiguration());
    }

    public static Network from(AAITreeNode node) {
        Network network = new Network(node);
        network.setInstanceType(readValueAsStringFromAdditionalProperties(node, NETWORK_TYPE));
        network.setRole(readValueAsStringFromAdditionalProperties(node, NETWORK_ROLE));
        network.setPhysicalName(readValueAsStringFromAdditionalProperties(node, PHYSICAL_NETWORK_NAME));
        RelationshipList relationshipList = node.getRelationshipList();
        Relationship serviceInstanceRelationship = AAITreeNodeUtils.findFirstRelationshipByRelatedTo(relationshipList, SERVICE_INSTANCE).orElse(null);
        if (serviceInstanceRelationship != null) {
            network.setServiceName(AAITreeNodeUtils.findFirstValue(serviceInstanceRelationship.getRelatedToPropertyList(), SERVICE_INSTANCE_SERVICE_INSTANCE_NAME).orElse(null));
            network.setServiceUUID(AAITreeNodeUtils.findFirstValue(serviceInstanceRelationship.getRelationDataList(), SERVICE_INSTANCE_SERVICE_INSTANCE_ID).orElse(null));
        }
        AAITreeNodeUtils.findFirstRelationshipByRelatedTo(relationshipList, TENANT).ifPresent(
                tenantRelationship -> network.setTenantName(AAITreeNodeUtils.findFirstValue(tenantRelationship.getRelatedToPropertyList(), TENANT_TENANT_NAME).orElse(null))
        );
        // We are ignoring "is-bound-to-vpn" parameter from additionalProperties because there is a requirement to define vpn binding presence from by related-to: vpn-binding
        network.setBoundToVpn(AAITreeNodeUtils.findFirstRelationshipByRelatedTo(relationshipList, VPN_BINDING).isPresent());

        //get the route target
        node.getChildren().stream()
                .filter(x->x.getType()== NodeType.VPN_BINDING)                      // get all VPN_BINDING related to the network
                .map(x->VpnBindingKt.from(x))                                       // create VPN_BINDING nodes
                .filter(x-> CollectionUtils.isNotEmpty(x.getRouteTargets()))        // get the RouteTargets that are not empty
                .findFirst()                                                        // get the first one
                .ifPresent(x->network.setRouteTarget(x.getRouteTargets().get(0)));  // If there is a route target - add it to the network
        return network;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhysicalName() {
        return physicalName;
    }

    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceUUID() {
        return serviceUUID;
    }

    public void setServiceUUID(String serviceUUID) {
        this.serviceUUID = serviceUUID;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public Boolean isBoundToVpn() {
        return isBoundToVpn;
    }

    public void setBoundToVpn(Boolean boundToVpn) {
        isBoundToVpn = boundToVpn;
    }

    public RouteTarget getRouteTarget() {
        return routeTarget;
    }

    public void setRouteTarget(RouteTarget routeTarget) {
        this.routeTarget = routeTarget;
    }
}
