/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.controller;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.lang3.StringUtils;
import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.model.ExceptionResponse;
import org.onap.vid.roles.Role;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.utils.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("logger")
public class LoggerController extends RestrictedBaseController {

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(LoggerController.class);
    public static final String VID_IS_STARTED = "VID is started";
    private RoleProvider roleProvider;
    private LogfilePathCreator logfilePathCreator;

    @Autowired
    public LoggerController(RoleProvider roleProvider, LogfilePathCreator logfilePathCreator) {
        this.roleProvider = roleProvider;
        this.logfilePathCreator = logfilePathCreator;
    }

    @GetMapping(value = "/{loggerName:audit|audit2019|error|metrics|metrics2019|debug}")
    public String getLog(@PathVariable String loggerName, HttpServletRequest request,
                         @RequestParam(value="limit", defaultValue = "5000") Integer limit) throws IOException {

        List<Role> roles = roleProvider.getUserRoles(request);
        boolean userPermitted = roleProvider.userPermissionIsReadLogs(roles);
        if (!userPermitted) {
            throw new NotAuthorizedException("User not authorized to get logs");
        }

        String logfilePath = logfilePathCreator.getLogfilePath(loggerName);
        try (final ReversedLinesFileReader reader = new ReversedLinesFileReader(new File(logfilePath), UTF_8)) {
            Supplier<String> reverseLinesSupplier = () -> {
                try {
                    return reader.readLine();
                } catch (NullPointerException e) {
                    // EOF Reached
                    return null;
                } catch (IOException e) {
                    throw new InternalServerErrorException("error while reading " + logfilePath, e);
                }
            };

            return Streams.takeWhile(
                    Stream.generate(reverseLinesSupplier),
                    line -> !StringUtils.contains(line, "Logging is started") &&
                            !StringUtils.contains(line, VID_IS_STARTED))
                    .limit(limit)
                    .limit(5_000)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("\n"));
        }
    }

    @ExceptionHandler({ NotAuthorizedException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String notAuthorizedHandler(NotAuthorizedException e) {
        return "UNAUTHORIZED";
    }

    @ExceptionHandler({ IOException.class, InternalServerErrorException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse ioExceptionHandler(Exception e) {
        return ControllersUtils.handleException(e, LOGGER);
    }

}
