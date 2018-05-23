package org.onap.vid.aai;

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.aai.util.*;
import org.onap.vid.utils.Logging;
import org.springframework.http.HttpMethod;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.onap.vid.utils.Logging.REQUEST_ID_HEADER_KEY;

public class PombaRestInterface extends AAIRestInterface {

    public PombaRestInterface (HttpsAuthClient httpsAuthClientFactory, ServletRequestHelper servletRequestHelper, SystemPropertyHelper systemPropertyHelper) {
        super(httpsAuthClientFactory, servletRequestHelper, systemPropertyHelper);
    }

    private Client client = null;

    private void initRestClient()
    {
        if (client == null) {
            try {
                client = httpsAuthClientFactory.getClient(HttpClientMode.UNSECURE);
            }
            catch (Exception e) {
                logger.info(EELFLoggerDelegate.errorLogger, "Exception in REST call to DB in initRestClient" + e.toString());
                logger.debug(EELFLoggerDelegate.debugLogger, "Exception in REST call to DB : " + e.toString());
            }
        }
    }


    public Response RestPost(String fromAppId, String url, String payload) {
        String methodName = "RestPost";
        String transId = UUID.randomUUID().toString();
        try {
            initRestClient();

            Logging.logRequest(outgoingRequestsLogger, HttpMethod.POST, url, payload);
            final Response cres = client.target(url)
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .header(TRANSACTION_ID_HEADER, transId)
                    .header(FROM_APP_ID_HEADER,  fromAppId)
                    .header(REQUEST_ID_HEADER_KEY, extractOrGenerateRequestId())
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));
            Logging.logResponse(outgoingRequestsLogger, HttpMethod.POST, url, cres);

            if (cres.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                logger.info(EELFLoggerDelegate.errorLogger, getValidResponseLogMessage(methodName));
                logger.debug(EELFLoggerDelegate.debugLogger, getValidResponseLogMessage(methodName));
            } else {
                logger.debug(EELFLoggerDelegate.debugLogger, getInvalidResponseLogMessage(url, methodName, cres));
            }
        } catch (Exception e) {
            logger.debug(EELFLoggerDelegate.debugLogger, getFailedResponseLogMessage(url, methodName, e));
        }
        return null;
    }

}


