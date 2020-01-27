package org.onap.vid.roles

class RoleValidatorsComposer(private vararg val roleValidators: RoleValidator) : RoleValidator {

    override fun isServicePermitted(subscriberName: String?, serviceType: String?): Boolean =
            roleValidators.any { it.isServicePermitted(subscriberName, serviceType) }

    override fun isSubscriberPermitted(subscriberName: String?): Boolean =
            roleValidators.any { it.isSubscriberPermitted(subscriberName) }

    override fun isTenantPermitted(globalCustomerId: String?, serviceType: String?, tenantName: String?): Boolean =
            roleValidators.any { it.isTenantPermitted(globalCustomerId, serviceType, tenantName) }

}
