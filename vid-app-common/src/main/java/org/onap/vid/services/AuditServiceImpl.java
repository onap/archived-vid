package org.onap.vid.services;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.UUID;


@Service
public class AuditServiceImpl implements AuditService{

    @Inject
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Override
    public void setFailedAuditStatusFromMso(UUID jobUuid, String requestId, int statusCode, String msoResponse){
        final String failedMsoRequestStatus = "FAILED";
        String additionalInfo = formatExceptionAdditionalInfo(statusCode, msoResponse);
        asyncInstantiationBL.auditMsoStatus(jobUuid, failedMsoRequestStatus, requestId, additionalInfo);
    }

    private String formatExceptionAdditionalInfo(int statusCode, String msoResponse) {
        String errorMsg = "Http Code:" + statusCode;
        if (!StringUtils.isEmpty(msoResponse)) {
            String filteredJson;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                filteredJson = StringUtils.defaultIfEmpty(
                        objectMapper.readTree(msoResponse).path("serviceException").toString().replaceAll("[\\{\\}]","") ,
                        msoResponse
                );
            } catch (JsonParseException e) {
                filteredJson = msoResponse;
            } catch (IOException e) {
                throw new GenericUncheckedException(e);
            }

            errorMsg = errorMsg + ", " + filteredJson;
        }
        return errorMsg;
    }
}
