package org.openecomp.vid.controller;

import org.openecomp.portalsdk.core.controller.UnRestrictedBaseController;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.vid.category.AddCategoryOptionResponse;
import org.openecomp.vid.category.AddCategoryOptionsRequest;
import org.openecomp.vid.category.CategoryParameterOptionRep;

import org.openecomp.vid.model.CategoryParameterOption;

import org.openecomp.vid.services.CategoryParameterService;
import org.openecomp.vid.services.CategoryParameterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ForbiddenException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Controler for APIs that are used only by vid operators
 */

@RestController
@RequestMapping(MaintenanceController.Maintenance)
public class MaintenanceController extends UnRestrictedBaseController {

    public static final String Maintenance = "maintenance";

    @Autowired
    protected CategoryParameterService categoryParameterService;
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MaintenanceController.class);

    /**
     * Add list of options to one category parameter
     * @param request the request
     * @return the new option
     * @throws Exception the exception
     */
    @RequestMapping(value = "/category_parameter/{categoryName}", method = RequestMethod.POST)
    public ResponseEntity addCategoryOptions (
            HttpServletRequest request, @PathVariable String categoryName, @RequestBody AddCategoryOptionsRequest option) throws Exception {
        try {
            AddCategoryOptionResponse response = categoryParameterService.createCategoryParameterOptions(categoryName, option);
            HttpStatus httpStatus = response.getErrors().size()>0 ? HttpStatus.MULTI_STATUS : HttpStatus.OK;
            return new ResponseEntity<>(response, httpStatus);
        }
        catch (CategoryParameterServiceImpl.UnfoundedCategoryException exception) {
            return new ResponseEntity<>(new AddCategoryOptionResponse(Collections.singletonList(exception.getMessage())), HttpStatus.NOT_FOUND);
        }
        catch (Exception exception) {
            logger.error("failed to add option to parameter category " + categoryName, exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/category_parameter/{categoryName}", method = RequestMethod.PUT)
    public ResponseEntity updateNameForOption (
            HttpServletRequest request, @PathVariable String categoryName, @RequestBody CategoryParameterOptionRep option) throws Exception {
        try {
            AddCategoryOptionResponse response = categoryParameterService.updateCategoryParameterOption(categoryName, option);
            HttpStatus httpStatus = response.getErrors().size()>0 ? HttpStatus.MULTI_STATUS : HttpStatus.OK;
            return new ResponseEntity<>(response, httpStatus);
        }
        catch (ForbiddenException exception) {
            return new ResponseEntity<>(new AddCategoryOptionResponse(Collections.singletonList(exception.getMessage())), HttpStatus.FORBIDDEN);
        }
        catch (CategoryParameterServiceImpl.UnfoundedCategoryException|CategoryParameterServiceImpl.UnfoundedCategoryOptionException exception) {
            return new ResponseEntity<>(new AddCategoryOptionResponse(Collections.singletonList(exception.getMessage())), HttpStatus.NOT_FOUND);
        }
        catch (CategoryParameterServiceImpl.AlreadyExistOptionNameException exception) {
            return new ResponseEntity<>(new AddCategoryOptionResponse(Collections.singletonList(exception.getMessage())), HttpStatus.CONFLICT);
        }
        catch (Exception exception) {
            logger.error("failed to update option to parameter category " + categoryName, exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets the owning entity properties.
     * @param request the request
     * @return the property
     * @throws Exception the exception
     */
    @RequestMapping(value = "/category_parameter", method = RequestMethod.GET)
    public ResponseEntity getCategoryParameter(HttpServletRequest request) throws Exception {
        try {
            return new ResponseEntity<>(categoryParameterService.getCategoryParameters(), HttpStatus.OK);
        }
        catch (Exception exception) {
            logger.error("failed to retrieve category parameter list from DB.", exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Delete option of the category.
     * @param request the request
     * @throws Exception the exception
     */
    @RequestMapping(value = "/delete_category_parameter/{categoryName}", method = RequestMethod.POST)
    public ResponseEntity deleteCategoryOption (
            HttpServletRequest request, @PathVariable String categoryName, @RequestBody CategoryParameterOption option) throws Exception {
        String methodName = "deleteOwningEntityProperty";

        try {
            categoryParameterService.deleteCategoryOption(categoryName, option);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (CategoryParameterServiceImpl.UnfoundedCategoryException exception) {
            return new ResponseEntity<>(new AddCategoryOptionResponse(Arrays.asList(exception.getMessage())), HttpStatus.NOT_FOUND);
        }
        catch (Exception exception) {
            logger.error("failed to add/update owning entity option", exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
