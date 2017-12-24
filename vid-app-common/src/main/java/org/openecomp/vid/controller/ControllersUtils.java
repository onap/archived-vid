package org.openecomp.vid.controller;

import org.openecomp.portalsdk.core.domain.User;
import org.openecomp.portalsdk.core.util.SystemProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ControllersUtils {


    public static String extractUserId(HttpServletRequest request) {
        String userId = "";
        HttpSession session = request.getSession();
        if (session != null) {
            User user = (User) session.getAttribute(SystemProperties.getProperty(SystemProperties.USER_ATTRIBUTE_NAME));
            if (user != null) {
                //userId = user.getHrid();
                userId = user.getLoginId();
                if (userId == null)
                    userId = user.getOrgUserId();
            }
        }
        return userId;
    }
}
