package org.onap.vid.model.aaiTree

import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class VpnBindingKtTest {

    @Test
    fun whenFailedToParseRouteTarget_DefaultValuesAreReturned() {
        val aaiTreeNode = AAITreeNode();
        aaiTreeNode.type = NodeType.VPN_BINDING
        aaiTreeNode.additionalProperties["route-targets"] = 3 //just an object that can't be parsed into list of route targets
        val vpnBinding = from(aaiTreeNode);
        assertEquals(vpnBinding.routeTargets, listOf(RouteTarget("ParsingFailure", "ParsingFailure")))
    }
}