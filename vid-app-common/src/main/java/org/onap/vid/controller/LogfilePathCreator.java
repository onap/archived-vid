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

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.spi.AppenderAttachable;
import com.att.eelf.configuration.Configuration;
import java.util.stream.Stream;
import javax.ws.rs.InternalServerErrorException;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.utils.Streams;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public final class LogfilePathCreator {

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(LogfilePathCreator.class);

    String getLogfilePath(String loggerName) {
        /*
        Find the requested logger, and pull all of it's appenders.
        Find the first of the appenders that is a FileAppender, and return it's
        write-out filename.
         */
        LOGGER.debug("Searching for logfile path with loggerName: ", loggerName);
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();;
        return context.getLoggerList().stream()
            .filter(logger -> logger.getName().equals(Configuration.GENERAL_LOGGER_NAME + "." + loggerName))
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
}