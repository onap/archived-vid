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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.joshworks.restclient.http.HttpResponse;
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
                String entityStr = body instanceof String ? (String) body : new ObjectMapper().writeValueAsString(httpResponse.getBody());
                msoResponseWrapper.setEntity(entityStr);
            }
            catch(JsonProcessingException e)
            {
                ExceptionUtils.rethrow(e);
            }
        }
        return msoResponseWrapper;
    }
}
