package org.openecomp.vid.mso.rest;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.vid.mso.MsoClientFactory;
import org.openecomp.vid.mso.MsoInterface;
import org.openecomp.vid.mso.MsoResponseWrapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pickjonathan on 21/06/2017.
 * This class was created only for testing the new logic.
 * It is not used by any of the controllers binded to the ui.
 * This can be deleted in the future in order to keep a cleaner project.
 * If deleting please dont forget to delete the controllers, factory and all involved in the assert test.
 */
public class MsoBusinessLogicNew {

    /**
     * \
     * The MSO Client
     */
    private MsoInterface msoClient;

    private MsoInterface msoRestTempClient;

    /**
     * The logger.
     */
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MsoBusinessLogicNew.class);

    /**
     * The Constant dateFormat.
     */
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

    public MsoBusinessLogicNew() {
        msoClient = MsoClientFactory.getInstance();
        msoRestTempClient = new MsoRestClientNew();
    }

    public MsoResponseWrapper createSvcInstance(RequestDetails msoRequest) throws Exception {
        String methodName = "createSvcInstance ";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

//        String endpoint = SystemProperties.getProperty(MsoProperties.MSO_REST_API_SVC_INSTANCE);
//
//        MsoResponseWrapper w = createInstance(msoRequest, p);

        MsoResponseWrapper w = msoClient.createSvcInstance(msoRequest, "");

        return w;
    }


    public MsoResponseWrapper createSvcInstanceRest(RequestDetails msoRequest) throws Exception {
        String methodName = "createSvcInstance ";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");


        MsoResponseWrapper w = msoRestTempClient.createSvcInstance(msoRequest, "");

        return w;
    }
}
