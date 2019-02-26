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

package org.onap.vid.aai.exceptions;

import org.onap.vid.aai.AaiResponse;
import org.onap.vid.exceptions.GenericUncheckedException;

/**
 * Created by Oren on 7/4/17.
 */
public class InvalidAAIResponseException extends GenericUncheckedException {
    public InvalidAAIResponseException(AaiResponse aaiResponse) {
        super(String.format("errorCode: %d, raw: %s", aaiResponse.getHttpCode(), aaiResponse.getErrorMessage()));
    }

    public InvalidAAIResponseException(int statusCode, String message) {
        super(String.format("errorCode: %d, raw: %s", statusCode, message));
    }
}
