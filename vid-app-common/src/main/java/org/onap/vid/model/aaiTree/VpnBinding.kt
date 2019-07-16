package org.onap.vid.model.aaiTree

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate
import org.onap.vid.utils.JACKSON_OBJECT_MAPPER

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class VpnBinding(aaiNode: AAITreeNode) : Node(aaiNode) {
    @JsonCreator
    constructor() : this(AAITreeNode())
    var region: String? = null
    var customerId: String? = null
    var routeTargets: List<RouteTarget>? = null
}

val LOGGER: EELFLoggerDelegate = EELFLoggerDelegate.getLogger(VpnBinding::class.java)

fun from(node: AAITreeNode): VpnBinding {
    val vpnBinding = VpnBinding(node)
    vpnBinding.platformName = Node.readValueAsStringFromAdditionalProperties(node, "vpn-platform")
    vpnBinding.instanceType = Node.readValueAsStringFromAdditionalProperties(node, "vpn-type")
    vpnBinding.region = Node.readValueAsStringFromAdditionalProperties(node, "vpn-region")
    vpnBinding.customerId = Node.readValueAsStringFromAdditionalProperties(node, "customer-vpn-id")

    vpnBinding.routeTargets = try {
        JACKSON_OBJECT_MAPPER.convertValue(
                node.additionalProperties.getOrDefault("route-targets", emptyList<RouteTarget>()),
                object : TypeReference<List<RouteTarget>>() {})
    } catch (exception: Exception) {
        LOGGER.error("Failed to parse route-targets of vpn with id:${vpnBinding.instanceId}", exception)
        listOf(RouteTarget("ParsingFailure", "ParsingFailure"))
    }

    return vpnBinding
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class RouteTarget(

        @JsonAlias("global-route-target")
        val globalRouteTarget: String? = null,

        @JsonAlias("route-target-role")
        val routeTargetRole: String? = null
)
