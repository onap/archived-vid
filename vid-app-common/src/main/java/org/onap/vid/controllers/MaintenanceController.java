package org.onap.vid.controllers;

/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Modifications Copyright 2018 Nokia
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

import static org.onap.vid.utils.Logging.getMethodCallerName;

import javax.ws.rs.ForbiddenException;
import org.onap.portalsdk.core.controller.UnRestrictedBaseController;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.category.AddCategoryOptionResponse;
import org.onap.vid.category.AddCategoryOptionsRequest;
import org.onap.vid.category.CategoryParameterOptionRep;
import org.onap.vid.category.CategoryParametersResponse;
import org.onap.vid.model.CategoryParameter.Family;
import org.onap.vid.model.CategoryParameterOption;
import org.onap.vid.services.CategoryParameterService;
import org.onap.vid.services.CategoryParameterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controler for APIs that are used only by vid operators
 */

@RestController
@RequestMapping("maintenance")
public class MaintenanceController extends UnRestrictedBaseController {

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(MaintenanceController.class);
    private CategoryParameterService categoryParameterService;

    @Autowired
    public MaintenanceController(CategoryParameterService categoryParameterService) {
        this.categoryParameterService = categoryParameterService;
    }

    /**
     * Add list of options to one category parameter
     */
    @RequestMapping(value = "/category_parameter/{categoryName}", method = RequestMethod.POST)
    public ResponseEntity addCategoryOptions(@PathVariable String categoryName,
        @RequestBody AddCategoryOptionsRequest option) {
        debugStartLog();
        try {
            AddCategoryOptionResponse response = categoryParameterService
                .createCategoryParameterOptions(categoryName, option);
            HttpStatus httpStatus = response.getErrors().isEmpty() ? HttpStatus.OK : HttpStatus.MULTI_STATUS;
            debugEndLog(response);
            return createResponseWithBody(httpStatus, response);
        } catch (CategoryParameterServiceImpl.UnfoundedCategoryException exception) {
            return createResponseWithBody(HttpStatus.NOT_FOUND, new AddCategoryOptionResponse(exception.getMessage()));
        } catch (RuntimeException exception) {
            LOGGER.error("failed to add option to parameter category " + categoryName, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/category_parameter/{categoryName}", method = RequestMethod.PUT)
    public ResponseEntity updateNameForOption(@PathVariable String categoryName,
        @RequestBody CategoryParameterOptionRep option) {
        debugStartLog();
        try {
            AddCategoryOptionResponse response = categoryParameterService
                .updateCategoryParameterOption(categoryName, option);
            HttpStatus httpStatus = response.getErrors().isEmpty() ? HttpStatus.OK : HttpStatus.MULTI_STATUS;
            debugEndLog(response);
            return createResponseWithBody(httpStatus, response);
        } catch (ForbiddenException exception) {
            return createResponseWithBody(HttpStatus.FORBIDDEN, new AddCategoryOptionResponse(exception.getMessage()));
        } catch (CategoryParameterServiceImpl.UnfoundedCategoryException | CategoryParameterServiceImpl.UnfoundedCategoryOptionException exception) {
            return createResponseWithBody(HttpStatus.NOT_FOUND, new AddCategoryOptionResponse(exception.getMessage()));

        } catch (CategoryParameterServiceImpl.AlreadyExistOptionNameException exception) {
            return createResponseWithBody(HttpStatus.CONFLICT, new AddCategoryOptionResponse(exception.getMessage()));

        } catch (RuntimeException exception) {
            LOGGER.error("failed to update option to parameter category " + categoryName, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Gets the owning entity properties.
     */
    @RequestMapping(value = "/category_parameter", method = RequestMethod.GET)
    public ResponseEntity getCategoryParameter(@RequestParam(value = "familyName", required = true) Family familyName) {
        debugStartLog();
        try {
            CategoryParametersResponse response = categoryParameterService.getCategoryParameters(familyName);
            debugEndLog(response);
            return ResponseEntity.ok().body(response);
        } catch (RuntimeException exception) {
            LOGGER.error("failed to retrieve category parameter list from DB.", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete option of the category.
     */
    @RequestMapping(value = "/delete_category_parameter/{categoryName}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCategoryOption(@PathVariable String categoryName,
        @RequestBody CategoryParameterOption option) {
        debugStartLog();

        try {
            categoryParameterService.deleteCategoryOption(categoryName, option);
            debugEndLog(HttpStatus.OK);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (CategoryParameterServiceImpl.UnfoundedCategoryException exception) {
            return createResponseWithBody(HttpStatus.NOT_FOUND, new AddCategoryOptionResponse(exception.getMessage()));
        } catch (RuntimeException exception) {
            LOGGER.error("failed to add/update owning entity option", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ResponseEntity createResponseWithBody(HttpStatus status, AddCategoryOptionResponse response) {
        return ResponseEntity.status(status).body(response);
    }

    private void debugStartLog() {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "start {}({})", getMethodCallerName());
    }

    private void debugEndLog(Object response) {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "end {}() => {}", getMethodCallerName(), response);
    }
}
