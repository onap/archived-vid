package org.openecomp.vid.roles;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.web.support.UserUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Oren on 7/1/17.
 */
public class RoleProvider {

    private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(RoleProvider.class);
    final String readPermissionString = "read";
    final String roleStringDelimiter = "_";

    public static List<String> extractRoleFromSession(HttpServletRequest request) {

        return new ArrayList<String>();

    }

    public List<Role> getUserRoles(HttpServletRequest request) {
        List<Role> roleList = new ArrayList<>();
		//Disable roles until AAF integration finishes
        /*HashMap roles = UserUtils.getRoles(request);
        for (Object role : roles.keySet()) {
            org.openecomp.portalsdk.core.domain.Role sdkRol = (org.openecomp.portalsdk.core.domain.Role) roles.get(role);
            try {
                if (sdkRol.getName().contains(readPermissionString))
                    continue;
                String[] roleParts = splitRole((sdkRol.getName()));
                roleList.add(createRoleFromStringArr(roleParts));
            } catch (Exception e) {
                LOG.error("Failed to parse permission", e);

            }
        }*/

        return roleList;
    }

    public String[] splitRole(String roleAsString) {
        return roleAsString.split(roleStringDelimiter);
    }

    public boolean userPermissionIsReadOnly(List<Role> roles) {

        return (!(roles.size() > 0));
    }

    public Role createRoleFromStringArr(String[] roleParts) {
        if (roleParts.length > 2) {
            return new Role(EcompRole.READ, roleParts[0], roleParts[1], roleParts[2]);
        } else {
            return new Role(EcompRole.READ, roleParts[0], roleParts[1], null);
        }
    }

}

