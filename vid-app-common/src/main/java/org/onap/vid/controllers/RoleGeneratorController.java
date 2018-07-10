package org.onap.vid.controllers;

import org.onap.vid.services.RoleGeneratorService;
import org.onap.portalsdk.core.controller.UnRestrictedBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleGeneratorController extends UnRestrictedBaseController {
    @Autowired
    private RoleGeneratorService roleGeneratorService;
    public static final String GENERATE_ROLE_SCRIPT = "generateRoleScript";
    @RequestMapping(value =  GENERATE_ROLE_SCRIPT +"/{firstRun}", method = RequestMethod.GET )
    public ResponseEntity<String> generateRoleScript (@PathVariable("firstRun") boolean firstRun) {
        ResponseEntity<String> response = null;
        String query = roleGeneratorService.generateRoleScript(firstRun);
        response = new ResponseEntity<String>(query, HttpStatus.OK);
        return response;
    }

}
