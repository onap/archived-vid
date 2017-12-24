package org.openecomp.vid.roles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;

//import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by Oren on 7/1/17.
 */

@Component
public class RoleProvider {

    private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(RoleProvider.class);
    final String readPermissionString = "read";
    SubscriberList subscribers;
    ObjectMapper om = new ObjectMapper();
    @Autowired
    private AaiService aaiService;

    public static List<String> extractRoleFromSession(HttpServletRequest request) {

        return new ArrayList<String>();

    }

    @PostConstruct
    public  void init  (){
        LOG.debug(EELFLoggerDelegate.debugLogger,"Role provider => init method started");
        AaiResponse<SubscriberList> subscribersResponse = aaiService.getFullSubscriberList();
        subscribers =  subscribersResponse.getT();
        LOG.debug(EELFLoggerDelegate.debugLogger,"Role provider => init method finished");
    }

    public List<Role> getUserRoles(HttpServletRequest request) throws JsonProcessingException {
        String logPrefix = "Role Provider (" + UserUtils.getUserId(request) + ") ==>";

        LOG.debug(EELFLoggerDelegate.debugLogger,logPrefix + "Entering to get user role for user " + UserUtils.getUserId(request));

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

    public String[] splitRole(String roleAsString,String logPrefix) {
        LOG.debug(EELFLoggerDelegate.debugLogger,logPrefix + "Spliting role = " + roleAsString + "With delimeter = " + ModelConstants.ROLE_DELIMITER);
        return roleAsString.split(ModelConstants.ROLE_DELIMITER);
    }

    public boolean userPermissionIsReadOnly(List<Role> roles) {

        return (!(roles.size() > 0));
    }

    private String replaceSubscriberNameToGlobalCustomerID(String subscriberName,String logPrefix) throws JsonProcessingException {
        if (subscribers == null) {
            LOG.debug(EELFLoggerDelegate.debugLogger,"replaceSubscriberNameToGlobalCustomerID calling init method");
            init();
        }
        LOG.debug(EELFLoggerDelegate.debugLogger,logPrefix + "subscribers list size is  " + subscribers.customer.size() + " with the values " + om.writeValueAsString(subscribers.customer));
        LOG.debug(EELFLoggerDelegate.debugLogger,logPrefix + "subscribers list size is  " + subscribers.customer.size() + " with the values " + om.writeValueAsString(subscribers.customer));


        Optional <Subscriber> s = subscribers.customer.stream().filter(x->x.subscriberName.equals(subscriberName)).findFirst();
        //Fixing bug of logging "optional get" before isPresent
        String replacement = s.isPresent()? s.get().globalCustomerId: "";
        LOG.debug(EELFLoggerDelegate.debugLogger,logPrefix + "Subscribername " +subscriberName+ " changed to  " +replacement);
        return replacement;
    }

    public Role createRoleFromStringArr(String[] roleParts,String rolePrefix) throws JsonProcessingException {
        String globalCustomerID = replaceSubscriberNameToGlobalCustomerID(roleParts[0],rolePrefix);
        if (roleParts.length > 2) {
            return new Role(EcompRole.READ, globalCustomerID, roleParts[1], roleParts[2]);
        } else {
            return new Role(EcompRole.READ, globalCustomerID, roleParts[1],  null);
        }
    }

}

