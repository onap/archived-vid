package org.onap.vid.roles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.web.support.UserUtils;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.exceptions.RoleParsingException;
import org.onap.vid.model.ModelConstants;
import org.onap.vid.model.Subscriber;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.services.AaiService;
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
    public void init() {
        LOG.debug(EELFLoggerDelegate.debugLogger, "Role provider => init method started");
        AaiResponse<SubscriberList> subscribersResponse = aaiService.getFullSubscriberList();
        subscribers = subscribersResponse.getT();
        LOG.debug(EELFLoggerDelegate.debugLogger, "Role provider => init method finished");
    }

    public List<Role> getUserRoles(HttpServletRequest request) throws JsonProcessingException {
        String logPrefix = "Role Provider (" + UserUtils.getUserId(request) + ") ==>";

        LOG.debug(EELFLoggerDelegate.debugLogger, logPrefix + "Entering to get user role for user " + UserUtils.getUserId(request));

        List<Role> roleList = new ArrayList<>();
        //Disable roles until AAF integration finishes
        /*Map roles = UserUtils.getRoles(request);
        for (Object role : roles.keySet()) {
            org.onap.portalsdk.core.domain.Role sdkRol = (org.onap.portalsdk.core.domain.Role) roles.get(role);

            LOG.debug(EELFLoggerDelegate.debugLogger, logPrefix + "Role " + sdkRol.getName() + " is being proccessed");
            try {
                if (sdkRol.getName().contains(readPermissionString)) {
                    LOG.debug(EELFLoggerDelegate.debugLogger, logPrefix + " Role " + sdkRol.getName() + " contain " + readPermissionString);

                    continue;
                }
                String[] roleParts = splitRole((sdkRol.getName()), logPrefix);
                roleList.add(createRoleFromStringArr(roleParts, logPrefix));
                String msg = String.format(logPrefix + " User %s got permissions %s", UserUtils.getUserId(request), Arrays.toString(roleParts));
                LOG.debug(EELFLoggerDelegate.debugLogger, msg);
            } catch (RoleParsingException e) {
                LOG.error(logPrefix + " Failed to parse permission");

            }
        }*/

        return roleList;
    }

    public String[] splitRole(String roleAsString, String logPrefix) {
        LOG.debug(EELFLoggerDelegate.debugLogger, logPrefix + "Spliting role = " + roleAsString + "With delimeter = " + ModelConstants.ROLE_DELIMITER);
        return roleAsString.split(ModelConstants.ROLE_DELIMITER);
    }

    public boolean userPermissionIsReadOnly(List<Role> roles) {

        return (!(roles.size() > 0));
    }

    public boolean userPermissionIsReadLogs(List<Role> roles){
        for(Role role: roles){
            if(role.getServiceType().equals("LOGS")){
                if(role.getTenant().equals("PERMITTED")){
                    return true;
                }
            }
        }
        return false;
    }

    private String replaceSubscriberNameToGlobalCustomerID(String subscriberName, String logPrefix) throws JsonProcessingException {
        if (subscribers == null) {
            LOG.debug(EELFLoggerDelegate.debugLogger, "replaceSubscriberNameToGlobalCustomerID calling init method");
            init();
        }
        LOG.debug(EELFLoggerDelegate.debugLogger, logPrefix + "subscribers list size is  " + subscribers.customer.size() + " with the values " + om.writeValueAsString(subscribers.customer));
        LOG.debug(EELFLoggerDelegate.debugLogger, logPrefix + "subscribers list size is  " + subscribers.customer.size() + " with the values " + om.writeValueAsString(subscribers.customer));


        Optional<Subscriber> s = subscribers.customer.stream().filter(x -> x.subscriberName.equals(subscriberName)).findFirst();
        //Fixing bug of logging "optional get" before isPresent
        String replacement = s.isPresent() ? s.get().globalCustomerId : "";
        LOG.debug(EELFLoggerDelegate.debugLogger, logPrefix + "Subscribername " + subscriberName + " changed to  " + replacement);
        return replacement;
    }

    public Role createRoleFromStringArr(String[] roleParts, String rolePrefix) throws JsonProcessingException, RoleParsingException {
        String globalCustomerID = replaceSubscriberNameToGlobalCustomerID(roleParts[0], rolePrefix);
        try {
            if (roleParts.length > 2) {
                return new Role(EcompRole.READ, globalCustomerID, roleParts[1], roleParts[2]);
            } else {
                return new Role(EcompRole.READ, globalCustomerID, roleParts[1], null);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            if (roleParts.length > 0)
                LOG.debug(EELFLoggerDelegate.debugLogger, "Could not parse role ", roleParts[0]);
            else {
                LOG.debug(EELFLoggerDelegate.debugLogger, "Got empty role, Could not parse it ");

            }
            throw new RoleParsingException();
        }

    }

}

