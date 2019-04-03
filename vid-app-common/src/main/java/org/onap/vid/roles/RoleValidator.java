package org.onap.vid.roles;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.onap.portalsdk.core.util.SystemProperties;

public interface RoleValidator {

    static RoleValidator by(List<Role> roles) {
        boolean disableRoles =
            StringUtils.equals(SystemProperties.getProperty("role_management_activated"), "false");

        return disableRoles
            ? new AlwaysValidRoleValidator()
            : new RoleValidatorByRoles(roles);
    }

    boolean isSubscriberPermitted(String subscriberName);

    boolean isServicePermitted(String subscriberName, String serviceType);

    boolean isTenantPermitted(String globalCustomerId, String serviceType, String tenantName);
}
