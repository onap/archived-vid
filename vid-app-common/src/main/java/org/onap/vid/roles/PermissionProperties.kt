package org.onap.vid.roles

import org.onap.vid.aai.ServiceSubscription


interface WithPermissionProperties

interface WithPermissionPropertiesSubscriberAndServiceType: WithPermissionProperties {
    val subscriberId: String?
    val serviceType: String?
}

interface WithPermissionPropertiesOwningEntity: WithPermissionProperties {
    val owningEntityId: String?
}


data class AllPermissionProperties(
        override val subscriberId: String,
        override val serviceType: String,
        override val owningEntityId: String
): WithPermissionPropertiesOwningEntity, WithPermissionPropertiesSubscriberAndServiceType

data class PermissionPropertiesOwningEntity(
        override val owningEntityId: String
): WithPermissionPropertiesOwningEntity

data class PermissionPropertiesSubscriberAndServiceType(
        override val subscriberId: String,
        override val serviceType: String
) : WithPermissionPropertiesSubscriberAndServiceType {
    constructor(serviceSubscription: ServiceSubscription, subscriberId: String) : this(subscriberId, serviceSubscription.serviceType)
}

