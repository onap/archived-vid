package org.onap.vid.aai;

import org.onap.vid.aai.util.AAIRestInterface;
import org.onap.vid.aai.util.HttpClientMode;
import org.onap.vid.aai.util.HttpsAuthClient;
import org.onap.vid.utils.Logging;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.http.HttpMethod;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.UUID;

import static org.onap.vid.utils.Logging.REQUEST_ID_HEADER_KEY;

public class PombaRestInterface extends AAIRestInterface {

    public PombaRestInterface (HttpsAuthClient httpsAuthClientFactory) {
        super(httpsAuthClientFactory);
    }

    private Client client = null;

    private void initRestClient()
    {
        if (client == null) {
            try {
                client = httpsAuthClientFactory.getClient(HttpClientMode.UNSECURE);
            }
            catch (Exception e) {
                logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  "<== Exception in REST call to DB in initRestClient" + e.toString());
                logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) +  "<== Exception in REST call to DB : " + e.toString());
            }
        }
    }


    public Response RestPost(String fromAppId, String url, String payload) {
        String methodName = "RestPost";
        String transId = UUID.randomUUID().toString();
        try {
            String responseType = MediaType.APPLICATION_JSON;
            initRestClient();

            Logging.logRequest(outgoingRequestsLogger, HttpMethod.POST, url, payload);
            final Response cres = client.target(url)
                    .request()
                    .accept(responseType)
                    .header(TRANSACTION_ID_HEADER, transId)
                    .header(FROM_APP_ID_HEADER,  fromAppId)
                    .header(REQUEST_ID_HEADER_KEY, extractOrGenerateRequestId())
                    .post(Entity.entity(payload, MediaType.APPLICATION_JSON));
            Logging.logResponse(outgoingRequestsLogger, HttpMethod.POST, url, cres);

            if (cres.getStatus() == 200 && cres.getStatus() <= 299) {
                logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + methodName + URL_DECLARATION);
                logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + URL_DECLARATION);
            } else {
                logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " with status="+cres.getStatus()+ URL_DECLARATION +url);
            }
            return cres;
        } catch (Exception e) {
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + URL_DECLARATION +url+ ", Exception: " + e.toString());
        }
        return null;
    }

}


