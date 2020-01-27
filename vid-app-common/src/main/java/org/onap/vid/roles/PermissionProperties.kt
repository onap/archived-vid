package org.onap.vid.roles

import org.onap.vid.aai.ServiceSubscription


interface WithPermissionProperties {
    val subscriberId: String?
    val serviceType: String?
}

data class PermissionProperties(
        override val subscriberId: String,
        override val serviceType: String
) : WithPermissionProperties {
    constructor(serviceSubscription: ServiceSubscription, subscriberId: String) : this(subscriberId, serviceSubscription.serviceType)
}

