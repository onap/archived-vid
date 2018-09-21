/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 Nokia. All rights reserved.
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
    public static final String FAILED_MSO_REQUEST_STATUS = "FAILED";

    @Override
    public void setFailedAuditStatusFromMso(UUID jobUuid, String requestId, int statusCode, String msoResponse){
        String additionalInfo = formatExceptionAdditionalInfo(statusCode, msoResponse);
        asyncInstantiationBL.auditMsoStatus(jobUuid, FAILED_MSO_REQUEST_STATUS, requestId, additionalInfo);
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
