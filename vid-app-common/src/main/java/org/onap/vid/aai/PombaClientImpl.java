package org.onap.vid.aai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.model.PombaInstance.PombaRequest;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;

public class PombaClientImpl implements PombaClientInterface {

    protected String fromAppId = "VidAaiController";
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AaiClient.class);

    @Autowired
    ServletContext servletContext;

    @Autowired
    PombaRestInterface pombaRestInterface;


    @Override
    public void verify(PombaRequest request) {
        String methodName = "doAaiPost";
        logger.debug(EELFLoggerDelegate.debugLogger, methodName + " start");
        String uri = SystemProperties.getProperty("pomba.server.url");


        try {
            pombaRestInterface.RestPost(fromAppId, uri, new ObjectMapper().writeValueAsString(request));
        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, methodName + e.toString());
        }
    }
}
