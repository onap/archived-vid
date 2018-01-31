package org.onap.vid.controller;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.spi.AppenderAttachable;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.lang3.StringUtils;
import org.openecomp.portalsdk.core.controller.RestrictedBaseController;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.model.ExceptionResponse;
import org.onap.vid.roles.Role;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.utils.Streams;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.att.eelf.configuration.Configuration.GENERAL_LOGGER_NAME;


@RestController
@RequestMapping("logger")
public class LoggerController extends RestrictedBaseController {

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(LoggerController.class);

    @Autowired
    RoleProvider roleProvider;

    @RequestMapping(value = "/{loggerName:audit|error|metrics}", method = RequestMethod.GET)
    public String getLog(@PathVariable String loggerName, HttpServletRequest request,
                         @RequestParam(value="limit", defaultValue = "5000") Integer limit) throws IOException {

        List<Role> roles = roleProvider.getUserRoles(request);
        boolean userPermitted = roleProvider.userPermissionIsReadLogs(roles);
        if (!userPermitted) {
            throw new NotAuthorizedException("User not authorized to get logs");
        }

        String logfilePath = getLogfilePath(loggerName);

        try (final ReversedLinesFileReader reader = new ReversedLinesFileReader(new File(logfilePath))) {
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
                    line -> !StringUtils.contains(line, "Logging is started"))
                    .limit(limit)
                    .limit(5_000)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("\n"));
        }
    }

    private String getLogfilePath(String loggerName) {
        /*
        Find the requested logger, and pull all of it's appenders.
        Find the first of the appenders that is a FileAppender, and return it's
        write-out filename.
         */
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        return context.getLoggerList().stream()
                .filter(logger -> logger.getName().equals(GENERAL_LOGGER_NAME + "." + loggerName))
                .flatMap(this::pullSubAppenders)
                .flatMap(appender -> {
                    // Appender might be "attachable", if so - roll-up its sub-appenders
                    return (appender instanceof AppenderAttachable) ?
                            pullSubAppenders((AppenderAttachable<?>) appender) : Stream.of(appender);
                })
                .filter(appender -> appender instanceof FileAppender)
                .map(appender -> (FileAppender<?>) appender)
                .map(FileAppender::getFile)
                .findFirst()
                .orElseThrow(() -> new InternalServerErrorException("logfile for " + loggerName + " not found"));
    }

    private <T> Stream<Appender<T>> pullSubAppenders(AppenderAttachable<T> logger) {
        return Streams.fromIterator(logger.iteratorForAppenders());
    }

    @ExceptionHandler({ NotAuthorizedException.class })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String notAuthorizedHandler(NotAuthorizedException e) {
        return "UNAUTHORIZED";
    }

    @ExceptionHandler({ IOException.class, InternalServerErrorException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse ioExceptionHandler(Exception e) {
        return org.onap.vid.controller.ControllersUtils.handleException(e, LOGGER);
    }

}
