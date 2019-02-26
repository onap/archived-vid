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

package org.onap.vid.aai;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletContext;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.model.PombaInstance.PombaRequest;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.beans.factory.annotation.Autowired;

public class PombaClientImpl implements PombaClientInterface {

    protected String fromAppId = "VidAaiController";
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AaiClient.class);

    @Autowired
    ServletContext servletContext;

    @Autowired
    PombaRestInterface pombaRestInterface;

    @Autowired
    SystemPropertiesWrapper systemPropertiesWrapper;

    @Override
    public void verify(PombaRequest request) {
        String methodName = "doAaiPost";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + " start");
        String uri = systemPropertiesWrapper.getProperty("pomba.server.url");

        try {
            pombaRestInterface.RestPost(fromAppId, uri, new ObjectMapper().writeValueAsString(request));
        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, methodName + e.toString());
        }
    }
}
