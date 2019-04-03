/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.roles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.joshworks.restclient.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.web.support.UserUtils;
import org.onap.vid.aai.exceptions.RoleParsingException;
import org.onap.vid.model.ModelConstants;
import org.onap.vid.model.Subscriber;
import org.onap.vid.model.SubscriberList;
import org.onap.vid.services.AaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by Oren on 7/1/17.
 */

@Component
public class RoleProvider {

    private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(RoleProvider.class);
    static final String READ_PERMISSION_STRING = "read";
    private final ObjectMapper om = new ObjectMapper();

    private AaiService aaiService;

    private Function<HttpServletRequest, Integer> getUserIdFunction;
    private Function<HttpServletRequest, Map> getRolesFunction;

    @Autowired
    public RoleProvider(AaiService aaiService) {
        this.aaiService=aaiService;
        getUserIdFunction = UserUtils::getUserId;
        getRolesFunction = UserUtils::getRoles;
    }

    RoleProvider(AaiService aaiService, Function<HttpServletRequest, Integer> getUserIdFunction, Function<HttpServletRequest, Map> getRolesFunction) {
        this.aaiService = aaiService;
        this.getRolesFunction = getRolesFunction;
        this.getUserIdFunction = getUserIdFunction;
    }

    public List<Role> getUserRoles(HttpServletRequest request) {
        int userId= getUserIdFunction.apply(request);
        String logPrefix = "Role Provider (" + userId + ") ==>";

        LOG.debug(EELFLoggerDelegate.debugLogger, logPrefix + "Entering to get user role for user " + userId);

        List<Role> roleList = new ArrayList<>();
        Map roles = getRolesFunction.apply(request);
        for (Object role : roles.keySet()) {
            org.onap.portalsdk.core.domain.Role sdkRol = (org.onap.portalsdk.core.domain.Role) roles.get(role);

            LOG.debug(EELFLoggerDelegate.debugLogger, logPrefix + "Role " + sdkRol.getName() + " is being proccessed");
            try {
                if (sdkRol.getName().contains(READ_PERMISSION_STRING)) {
                    LOG.debug(EELFLoggerDelegate.debugLogger, logPrefix + " Role " + sdkRol.getName() + " contain " + READ_PERMISSION_STRING);

                    continue;
                }
                String[] roleParts = splitRole((sdkRol.getName()), logPrefix);
                roleList.add(createRoleFromStringArr(roleParts, logPrefix));
                String msg = String.format("%s User %s got permissions %s", logPrefix, userId, Arrays.toString(roleParts));
                LOG.debug(EELFLoggerDelegate.debugLogger, msg);
            } catch (Exception e) {
                LOG.error(logPrefix + " Failed to parse permission");

            }
        }

        return roleList;
    }

    public String[] splitRole(String roleAsString, String logPrefix) {
        LOG.debug(EELFLoggerDelegate.debugLogger, logPrefix + "Spliting role = " + roleAsString + "With delimeter = " + ModelConstants.ROLE_DELIMITER);
        return roleAsString.split(ModelConstants.ROLE_DELIMITER);
    }

    public boolean userPermissionIsReadOnly(List<Role> roles) {
        return roles.isEmpty();
    }

    public boolean userPermissionIsReadLogs(List<Role> roles){
        for(Role role: roles){
            if ( role.getServiceType().equals("LOGS") && role.getTenant().equals("PERMITTED") ) {
                return true;
            }
        }
        return false;
    }

    private String replaceSubscriberNameToGlobalCustomerID(String subscriberName, String logPrefix) {
        // SubscriberList should be cached by cacheProvider so by calling getFullSubscriberList() method we just gat it from cache
        HttpResponse<SubscriberList> subscribersResponse = aaiService.getFullSubscriberList();
        SubscriberList subscribers = subscribersResponse.getBody();

        try {
            LOG.debug(EELFLoggerDelegate.debugLogger, logPrefix + "subscribers list size is  " + subscribers.customer.size() + " with the values " + om.writeValueAsString(subscribers.customer));
        } catch (JsonProcessingException e) {
            // log subscriberNames without object mapper
            LOG.debug(EELFLoggerDelegate.debugLogger, logPrefix + "subscribers list size is  " + subscribers.customer.size()
                    + " with the values " + subscribers.customer.stream().map(subscriber -> subscriber.subscriberName).collect(Collectors.joining(",")));
        }

        Optional<Subscriber> s = subscribers.customer.stream().filter(x -> x.subscriberName.equals(subscriberName)).findFirst();
        //Fixing bug of logging "optional get" before isPresent
        String replacement = s.isPresent() ? s.get().globalCustomerId : "";
        LOG.debug(EELFLoggerDelegate.debugLogger, logPrefix + "Subscribername " + subscriberName + " changed to  " + replacement);
        return replacement;
    }

    public Role createRoleFromStringArr(String[] roleParts, String rolePrefix) throws RoleParsingException {
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

    public RoleValidator getUserRolesValidator(HttpServletRequest request) {
        return RoleValidator.by(getUserRoles(request));
    }
}

