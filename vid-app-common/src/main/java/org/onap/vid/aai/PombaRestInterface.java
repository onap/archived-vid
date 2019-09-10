/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
 * Modifications Copyright (C) 2019 IBM.
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

import static org.onap.vid.utils.Logging.REQUEST_ID_HEADER_KEY;

import java.util.UUID;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.aai.util.HttpClientMode;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.aai.util.ServletRequestHelper;
import org.onap.vid.aai.util.SystemPropertyHelper;
import org.onap.vid.utils.Logging;
import org.springframework.http.HttpMethod;

public class PombaRestInterface extends AAIRestInterface {
	
    private Client client = null;

    public PombaRestInterface (HttpsAuthClient httpsAuthClientFactory,
        ServletRequestHelper servletRequestHelper,
        SystemPropertyHelper systemPropertyHelper,
        Logging loggingService) {
        super(httpsAuthClientFactory, servletRequestHelper, systemPropertyHelper, loggingService);
    }

    @Override
    protected void initRestClient()
    {
        if (client == null) {
            try {
                client = httpsAuthClientFactory.getClient(HttpClientMode.WITH_KEYSTORE);
            }
            catch (Exception e) {
                logger.info(EELFLoggerDelegate.errorLogger, "Exception in REST call to DB in initRestClient", e);
                logger.debug(EELFLoggerDelegate.debugLogger, "Exception in REST call to DB : ", e);
            }
        }
    }


    public Response RestPost(String fromAppId, String url, String payload) {
        String methodName = "RestPost";
        String transId = UUID.randomUUID().toString();
        try {
            initRestClient();

            loggingService.logRequest(outgoingRequestsLogger, HttpMethod.POST, url, payload);
            final Response cres = client.target(url)
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .header(TRANSACTION_ID_HEADER, transId)
                    .header(FROM_APP_ID_HEADER,  fromAppId)
                    .header(REQUEST_ID_HEADER_KEY, extractOrGenerateRequestId())
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));
            loggingService.logResponse(outgoingRequestsLogger, HttpMethod.POST, url, cres);

            if (cres.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                logger.info(EELFLoggerDelegate.errorLogger, getValidResponseLogMessage(methodName));
                logger.debug(EELFLoggerDelegate.debugLogger, getValidResponseLogMessage(methodName));
            } else {
                logger.debug(EELFLoggerDelegate.debugLogger, getInvalidResponseLogMessage(url, methodName, cres));
            }
            return cres;
        } catch (Exception e) {
            logger.debug(EELFLoggerDelegate.debugLogger, getFailedResponseLogMessage(url, methodName, e));
        }
        return null;
    }

}


