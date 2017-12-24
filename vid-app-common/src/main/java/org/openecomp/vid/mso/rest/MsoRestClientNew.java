package org.openecomp.vid.mso.rest;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.vid.changeManagement.MsoRequestDetails;
import org.openecomp.vid.changeManagement.RequestDetailsWrapper;
import org.openecomp.vid.mso.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pickjonathan on 21/06/2017.
 */
public class MsoRestClientNew extends RestMsoImplementation implements MsoInterface {

    /**
     * The logger.
     */
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MsoRestClientNew.class);

    /**
     * The Constant dateFormat.
     */
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

    private final String ORIGINAL_REQUEST_ID = "originalRequestId";


    @Override
    public MsoResponseWrapper createSvcInstance(RequestDetails requestDetails, String endpoint) throws Exception {
        String methodName = "createSvcInstance ";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        return createInstance(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper createVnf(RequestDetails requestDetails, String endpoint) throws Exception {

        String methodName = "createVnf";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        return createInstance(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper createNwInstance(RequestDetails requestDetails, String endpoint) throws Exception {

        String methodName = "createNwInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        return createInstance(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper createVolumeGroupInstance(RequestDetails requestDetails, String endpoint) throws Exception {
        String methodName = "createVolumeGroupInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        return createInstance(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper createVfModuleInstance(RequestDetails requestDetails, String endpoint) throws Exception {
        String methodName = "createVfModuleInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        return createInstance(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper createConfigurationInstance(RequestDetails requestDetails, String endpoint) throws Exception {
        String methodName = "createConfigurationInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        return createInstance(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper deleteSvcInstance(RequestDetails requestDetails, String endpoint) throws Exception {
        String methodName = "deleteSvcInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        return deleteInstance(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper deleteVnf(RequestDetails requestDetails, String endpoint) throws Exception {
        String methodName = "deleteVnf";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        return deleteInstance(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper deleteVfModule(RequestDetails requestDetails, String endpoint) throws Exception {
        String methodName = "deleteVfModule";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        return deleteInstance(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper deleteVolumeGroupInstance(RequestDetails requestDetails, String endpoint) throws Exception {
        String methodName = "deleteVolumeGroupInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        return deleteInstance(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper deleteNwInstance(RequestDetails requestDetails, String endpoint) throws Exception {
        String methodName = "deleteNwInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        return deleteInstance(requestDetails, endpoint);
    }

    @Override
    public void getOrchestrationRequest(String t, String sourceId, String endpoint, RestObject restObject) throws Exception {
        Get(t, sourceId, endpoint, restObject);
    }

    public void getManualTasks(String t, String sourceId, String endpoint, RestObject restObject) throws Exception {
        Get(t, sourceId, endpoint, restObject);
    }


    public MsoResponseWrapper createInstance(RequestDetails request, String path) throws Exception {
        String methodName = "createInstance";
        logger.debug(dateFormat.format(new Date()) + "<== " + methodName + " start");

        try {
            RestObject<String> restObjStr = new RestObject<String>();

            String str = new String();

            restObjStr.set(str);

            Post(str, request, "", path, restObjStr);
            MsoResponseWrapper w = MsoUtil.wrapResponse(restObjStr);

            return w;
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }


    /**
     * Delete instance.
     *
     * @param request the request
     * @param path    the path
     * @return the mso response wrapper
     * @throws Exception the exception
     */
    public MsoResponseWrapper deleteInstance(RequestDetails request, String path) throws Exception {
        String methodName = "deleteInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        try {
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " calling Delete, path =[" + path + "]");

            RestObject<String> restObjStr = new RestObject<String>();
            String str = new String();
            restObjStr.set(str);
            Delete(str, request, "", path, restObjStr);
            MsoResponseWrapper w = MsoUtil.wrapResponse(restObjStr);

            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " w=" + w.getResponse());
            return w;

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }

    }

    public MsoResponseWrapper getOrchestrationRequestsForDashboard(String t, String sourceId, String endpoint, RestObject restObject) throws Exception{
        String methodName = "getOrchestrationRequestsForDashboard";
        logger.debug(dateFormat.format(new Date()) + "<== " + methodName + " start");

        try {
            getOrchestrationRequest(t, sourceId, endpoint, restObject);
            MsoResponseWrapper w = MsoUtil.wrapResponse(restObject);
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " w=" + w.getResponse());

            return w;

        } catch (Exception e){
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }

    public MsoResponseWrapper getManualTasksByRequestId(String t , String sourceId , String endpoint , RestObject restObject) throws Exception{
        String methodName = "getManualTasksByRequestId";
        logger.debug(dateFormat.format(new Date()) + "<== " + methodName + " start");

        try {
            getManualTasks(t , sourceId , endpoint , restObject);
            MsoResponseWrapper w = MsoUtil.wrapResponse(restObject);
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " w=" + w.getResponse());

            return MsoUtil.wrapResponse(restObject);

        } catch (Exception e){
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }



    @Override
    public MsoResponseWrapper completeManualTask(RequestDetails requestDetails, String t, String sourceId, String endpoint, RestObject restObject) throws Exception {
        String methodName = "completeManualTask";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " calling Complete ");
        try {

            Post(t, requestDetails , sourceId, endpoint, restObject);
            MsoResponseWrapper w = MsoUtil.wrapResponse(restObject);

            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " w=" + w.getResponse());
            return w;

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }

    @Override
    public MsoResponseWrapper replaceVnf(org.openecomp.vid.changeManagement.RequestDetails requestDetails, String endpoint) throws Exception {
        String methodName = "replaceVnf";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        return replaceInstance(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper deleteConfiguration(RequestDetails requestDetails, String pmc_endpoint)
            throws Exception {
        String methodName = "deleteConfiguration";
        logger.debug(EELFLoggerDelegate.debugLogger,
                dateFormat.format(new Date()) + "<== " + methodName + " start");

        return deleteInstance(requestDetails, pmc_endpoint);
    }

    @Override
    public MsoResponseWrapper setConfigurationActiveStatus(RequestDetails request, String path) throws Exception {
        String methodName = "setConfigurationActiveStatus";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        try {
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " calling change configuration active status, path =[" + path + "]");

            RestObject<String> restObjStr = new RestObject<String>();
            String str = new String();
            restObjStr.set(str);
            Post(str, request, "", path, restObjStr);
            MsoResponseWrapper msoResponseWrapperObject = MsoUtil.wrapResponse(restObjStr);

            return msoResponseWrapperObject;
        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }

    @Override
    public MsoResponseWrapper setPortOnConfigurationStatus(RequestDetails request, String path) throws Exception {
        String methodName = "setPortOnConfigurationStatus";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        try {
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " calling change port configuration status, path =[" + path + "]");

            RestObject<String> restObjStr = new RestObject<String>();
            String str = new String();
            restObjStr.set(str);
            Post(str, request, "", path, restObjStr);
            MsoResponseWrapper msoResponseWrapperObject = MsoUtil.wrapResponse(restObjStr);

            return msoResponseWrapperObject;
        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }

    public MsoResponseWrapper replaceInstance(org.openecomp.vid.changeManagement.RequestDetails request, String path) throws Exception {
        String methodName = "replaceInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        try {
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " calling Replace VNF, path =[" + path + "]");

            RestObject<String> restObjStr = new RestObject<String>();
            String str = new String();
            restObjStr.set(str);
            RequestDetailsWrapper requestDetailsWrapper = new RequestDetailsWrapper();
            requestDetailsWrapper.requestDetails = new MsoRequestDetails(request);

            Post(str, request, "", path, restObjStr);
            MsoResponseWrapper msoResponseWrapperObject = MsoUtil.wrapResponse(restObjStr);
            int status = msoResponseWrapperObject.getStatus();
            if (status == 202){
	            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName +
	            		",post succeeded, msoResponseWrapperObject response:" + msoResponseWrapperObject.getResponse());
            }
            else {
            		logger.error(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName +
            				": post failed, msoResponseWrapperObject status" + status + ", response:" + msoResponseWrapperObject.getResponse());

            		// TODO
            }
            return msoResponseWrapperObject;

        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }

    }

    @Override
    public MsoResponseWrapper updateVnf(org.openecomp.vid.changeManagement.RequestDetails requestDetails, String endpoint) throws Exception {
        String methodName = "updateVnf";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
        RequestDetailsWrapper wrapper = new RequestDetailsWrapper();
        wrapper.requestDetails = new MsoRequestDetails(requestDetails);;
        return updateInstance(requestDetails, endpoint);
    }

    public MsoResponseWrapper updateInstance(org.openecomp.vid.changeManagement.RequestDetails request, String path) throws Exception {
        String methodName = "updateInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        try {
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " calling Delete, path =[" + path + "]");

            RestObject<String> restObjStr = new RestObject<String>();
            String str = new String();
            restObjStr.set(str);
            RequestDetailsWrapper requestDetailsWrapper = new RequestDetailsWrapper();
            requestDetailsWrapper.requestDetails = new MsoRequestDetails(request);



            Put(str, requestDetailsWrapper, "", path, restObjStr);
            MsoResponseWrapper w = MsoUtil.wrapResponse(restObjStr);

            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " w=" + w.getResponse());
            return w;

        } catch (Exception e) {
            logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }

    }

    public void activateServiceInstance(RequestDetails requestDetails, String t, String sourceId, String endpoint, RestObject<String> restObject) throws Exception{
        String methodName = "activateServiceInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start ");
        try {

            Post(t, requestDetails , sourceId, endpoint, restObject);
            MsoResponseWrapper w = MsoUtil.wrapResponse(restObject);

            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " w =" + w.getResponse());

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }

}