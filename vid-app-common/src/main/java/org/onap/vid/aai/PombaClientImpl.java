
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
