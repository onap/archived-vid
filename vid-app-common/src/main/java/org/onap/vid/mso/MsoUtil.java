/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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

package org.onap.vid.mso;

import static org.apache.commons.lang3.StringUtils.firstNonBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.joshworks.restclient.http.HttpResponse;
import java.io.IOException;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class MsoUtil {


    private MsoUtil() {
    }

    public static MsoResponseWrapper wrapResponse(RestObject<String> restObject) {
        String response = restObject.get() != null ? restObject.get() : restObject.getRaw();
        int status = restObject.getStatusCode();
        return new MsoResponseWrapper(status, response);
    }

    public static <T> MsoResponseWrapper wrapResponse(HttpResponse<T> httpResponse)  {
        MsoResponseWrapper msoResponseWrapper = new MsoResponseWrapper();
        msoResponseWrapper.setStatus(httpResponse.getStatus());
        if (httpResponse.getRawBody() != null) {
            try {
                T body = httpResponse.getBody();
                String entityStr = (body instanceof String || body==null) ? (String) body :
                    JACKSON_OBJECT_MAPPER.writeValueAsString(httpResponse.getBody());
                msoResponseWrapper.setEntity(entityStr);
            } catch(JsonProcessingException e) {
                ExceptionUtils.rethrow(e);
            }
        }
        return msoResponseWrapper;
    }

    public static <T> MsoResponseWrapper2<T> wrapResponse2(HttpResponse<String> httpResponse, Class<T> clazz) {
        String raw = httpResponse.getBody(); // As body's T is String, use getBody as a simple way to get raw
        if (raw != null) {
            try {
                T entity = JACKSON_OBJECT_MAPPER.readValue(raw, clazz);
                return new MsoResponseWrapper2<>(httpResponse.getStatus(), entity, raw);
            } catch (JsonProcessingException exception) {
                return new MsoResponseWrapper2<>(httpResponse.getStatus(), null, raw);
            } catch (IOException exception) {
                ExceptionUtils.rethrow(exception);
            }
        }

        return new MsoResponseWrapper2<>(httpResponse.getStatus(), null, null);
    }

    public static String formatExceptionAdditionalInfo(int statusCode, String msoResponse) {
        final String errorMsg = "Http Code:" + statusCode;

        if (isEmpty(msoResponse)) {
            return errorMsg;
        }

        try {
            JsonNode jsonNode = JACKSON_OBJECT_MAPPER.readTree(msoResponse);

            return errorMsg + ", " + firstNonBlank(
                removeBraces(jsonNode.get("serviceException")),
                removeBraces(jsonNode.path("requestError").get("serviceException")),
                msoResponse
            );

        } catch (Exception e) {
            return errorMsg + ", " + msoResponse;
        }
    }

    private static String removeBraces(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isMissingNode()) {
            return null;
        }

        return jsonNode.toString().replaceAll("[\\{\\}]", "");
    }
}
