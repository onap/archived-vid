package org.onap.vid.aai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.vid.model.PombaInstance.PombaRequest;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Response;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PombaClientImpl implements PombaClientInterface {

    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
    protected String fromAppId = "VidAaiController";
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(AaiClient.class);

    @Autowired
    ServletContext servletContext;

    @Autowired
    PombaRestInterface pombaRestInterface;


    @Override
    public void verify(PombaRequest request) {
        String methodName = "doAaiPost";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
        String uri = SystemProperties.getProperty("pomba.server.url");


        try {
            Response response = pombaRestInterface.RestPost(fromAppId, uri, new ObjectMapper().writeValueAsString(request));
        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
        }
    }


    private File getCertificatesFile() {
        if (servletContext != null)
            return new File(servletContext.getRealPath("/WEB-INF/cert/"));
        return null;
    }
}
