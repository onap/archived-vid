package org.onap.vid.roles

class RoleValidatorsComposer(private vararg val roleValidators: RoleValidator) : RoleValidator {

    constructor(roleValidators: Collection<RoleValidator>) : this(*roleValidators.toTypedArray())

    override fun isServicePermitted(p: WithPermissionProperties): Boolean =
            roleValidators.any { it.isServicePermitted(p) }

    override fun isSubscriberPermitted(subscriberId: String?): Boolean =
            roleValidators.any { it.isSubscriberPermitted(subscriberId) }

    override fun isTenantPermitted(subscriberId: String?, serviceType: String?, tenantName: String?): Boolean =
            roleValidators.any { it.isTenantPermitted(subscriberId, serviceType, tenantName) }

}
