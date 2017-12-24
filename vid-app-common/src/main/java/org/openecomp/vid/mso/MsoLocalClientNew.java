package org.openecomp.vid.mso;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.vid.mso.rest.RequestDetails;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pickjonathan on 21/06/2017.
 */
public class MsoLocalClientNew implements MsoInterface {


    /**
     * The logger.
     */
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MsoLocalClient.class);

    /**
     * The Constant dateFormat.
     */
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");


    @Override
    public MsoResponseWrapper createSvcInstance(RequestDetails requestDetails, String endpoint) throws Exception {

        String methodName = "createSvcInstance ";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
        MsoResponseWrapper w = createInstance(requestDetails, "");

        return w;
    }

    public MsoResponseWrapper createInstance(RequestDetails request, String path) throws Exception {
        String methodName = "createInstance";
        logger.debug(dateFormat.format(new Date()) + "<== " + methodName + " start");

        try {
            RestObject<String> restObjStr = new RestObject<String>();

            String str = new String();

            restObjStr.set(str);

            final InputStream asdcServicesFile = MsoLocalClient.class.getClassLoader().getResourceAsStream("mso_create_instance_response.json");

            restObjStr.setStatusCode(200);
            restObjStr.set(IOUtils.toString(asdcServicesFile));

            MsoResponseWrapper w = MsoUtil.wrapResponse(restObjStr);

            return w;
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }

    @Override
    public MsoResponseWrapper createVnf(RequestDetails requestDetails, String endpoint) throws Exception {
        return null;
    }

    @Override
    public MsoResponseWrapper createNwInstance(RequestDetails requestDetails, String endpoint) throws Exception {
        return null;
    }

    @Override
    public MsoResponseWrapper createVolumeGroupInstance(RequestDetails requestDetails, String path) throws Exception {
        return null;
    }

    @Override
    public MsoResponseWrapper createVfModuleInstance(RequestDetails requestDetails, String endpoint) throws Exception {
        return null;
    }

    @Override
    public MsoResponseWrapper createConfigurationInstance(RequestDetails requestDetails, String endpoint) throws Exception {
        return createInstance(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper deleteSvcInstance(RequestDetails requestDetails, String endpoint) throws Exception {
        return null;
    }

    @Override
    public MsoResponseWrapper deleteVnf(RequestDetails requestDetails, String endpoint) throws Exception {
        return null;
    }

    @Override
    public MsoResponseWrapper deleteVfModule(RequestDetails requestDetails, String endpoint) throws Exception {
        return null;
    }

    @Override
    public MsoResponseWrapper deleteVolumeGroupInstance(RequestDetails requestDetails, String endpoint) throws Exception {
        return null;
    }

    @Override
    public MsoResponseWrapper deleteNwInstance(RequestDetails requestDetails, String endpoint) throws Exception {
        return null;
    }

    @Override
    public void getOrchestrationRequest(String t, String sourceId, String endpoint, RestObject restObject) throws Exception { }

    @Override
    public MsoResponseWrapper getOrchestrationRequestsForDashboard(String t, String sourceId, String endpoint, RestObject restObject) throws Exception {
        String methodName = "getOrchestrationRequestsForDashboard";
        logger.debug(dateFormat.format(new Date()) + "<== " + methodName + " start");

        try {

            final InputStream asdcServicesFile = MsoLocalClient.class.getClassLoader().getResourceAsStream("mso_get_orchestration_requests.json");

            restObject.setStatusCode(200);
            restObject.set(IOUtils.toString(asdcServicesFile));

            MsoResponseWrapper w = MsoUtil.wrapResponse(restObject);

            return w;
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }

    public void activateServiceInstance(RequestDetails requestDetails, String t, String sourceId, String endpoint, RestObject<String> restObject) throws Exception{
        String methodName = "activateServiceInstance";
        logger.debug(dateFormat.format(new Date()) + "<== " + methodName + " start");

        try {

            final InputStream asdcServicesFile = MsoLocalClient.class.getClassLoader().getResourceAsStream("mso_activate_service_instance.json");

            restObject.setStatusCode(200);
            restObject.set(IOUtils.toString(asdcServicesFile));

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }

    @Override
    public MsoResponseWrapper getManualTasksByRequestId(String t, String sourceId, String endpoint, RestObject restObject) throws Exception {
        String methodName = "getManualTasksByRequestId";
        logger.debug(dateFormat.format(new Date()) + "<== " + methodName + " start");

        try {

            final InputStream asdcServicesFile = MsoLocalClient.class.getClassLoader().getResourceAsStream("mso_get_manual_task_by_request_id.json");

            restObject.setStatusCode(200);
            restObject.set(IOUtils.toString(asdcServicesFile));

            MsoResponseWrapper w = MsoUtil.wrapResponse(restObject);

            return w;
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }

    @Override
    public MsoResponseWrapper completeManualTask(RequestDetails requestDetails, String t, String sourceId, String endpoint, RestObject restObject) throws Exception {
        String methodName = "getManualTasksByRequestId";
        logger.debug(dateFormat.format(new Date()) + "<== " + methodName + " start");

        try {

            final InputStream asdcServicesFile = MsoLocalClient.class.getClassLoader().getResourceAsStream("mso_complete_manual_task.json");

            restObject.setStatusCode(200);
            restObject.set(IOUtils.toString(asdcServicesFile));

            MsoResponseWrapper w = MsoUtil.wrapResponse(restObject);

            return w;
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }

	@Override
	public MsoResponseWrapper replaceVnf(org.openecomp.vid.changeManagement.RequestDetails requestDetails, String vnf_endpoint) throws Exception {
        throw new NotImplementedException("Function was not implemented at this point.");
	}

    @Override
    public MsoResponseWrapper deleteConfiguration(RequestDetails requestDetails, String pmc_endpoint) {
        return null;
    }

    @Override
    public MsoResponseWrapper setConfigurationActiveStatus(RequestDetails requestDetails, String endpoint) {
        return null;
    }

    @Override
    public MsoResponseWrapper setPortOnConfigurationStatus(RequestDetails requestDetails, String endpoint) {
        return null;
    }


    @Override
	public MsoResponseWrapper updateVnf(org.openecomp.vid.changeManagement.RequestDetails requestDetails,
			String vnf_endpoint) {
        // TODO Auto-generated method stub
        return null;
    }
}
