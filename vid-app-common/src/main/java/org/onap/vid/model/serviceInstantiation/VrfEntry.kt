package org.onap.vid.model.serviceInstantiation

import org.onap.vid.model.aaiTree.Network
import org.onap.vid.model.aaiTree.VpnBinding

data class VrfEntry(val networks: Map<String, Network>, val vpns: Map<String, VpnBinding>)
