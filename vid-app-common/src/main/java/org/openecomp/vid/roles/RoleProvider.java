package org.openecomp.vid.roles;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.web.support.UserUtils;
import org.openecomp.vid.aai.AaiResponse;
import org.openecomp.vid.model.ModelConstants;
import org.openecomp.vid.model.Subscriber;
import org.openecomp.vid.model.SubscriberList;
import org.openecomp.vid.services.AaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.apache.tiles.request.servlet.ServletUtil.getServletContext;

/**
 * Created by Oren on 7/1/17.
 */

@Component
public class RoleProvider {

    private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(RoleProvider.class);
    final String readPermissionString = "read";
    SubscriberList subscribers;

    @Autowired
    private AaiService aaiService;

    public static List<String> extractRoleFromSession(HttpServletRequest request) {

        return new ArrayList<String>();

    }

    @PostConstruct
    public  void init  (){
        AaiResponse<SubscriberList> subscribersResponse = aaiService.getFullSubscriberList();
        subscribers =  subscribersResponse.getT();
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
        return roleAsString.split(ModelConstants.ROLE_DELIMITER);
    }

    public boolean userPermissionIsReadOnly(List<Role> roles) {

        return (!(roles.size() > 0));
    }

    private String replaceSubscriberNameToGlobalCustomerID(String subscriberName)  {
        Optional <Subscriber> s = subscribers.customer.stream().filter(x->x.subscriberName.equals(subscriberName)).findFirst();
        return s.isPresent()? s.get().globalCustomerId: "";

    }
    public Role createRoleFromStringArr(String[] roleParts) {
        String globalCustomerID = replaceSubscriberNameToGlobalCustomerID(roleParts[0]);
        if (roleParts.length > 2) {
            return new Role(EcompRole.READ, globalCustomerID, roleParts[1], roleParts[2]);
        } else {
            return new Role(EcompRole.READ, globalCustomerID, roleParts[1],  null);
        }
    }

}

