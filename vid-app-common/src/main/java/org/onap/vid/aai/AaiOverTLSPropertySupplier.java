/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia Intellectual Property. All rights reserved.
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

package org.onap.vid.aai;

import java.util.UUID;
import org.eclipse.jetty.util.security.Password;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.util.AAIProperties;
import org.onap.vid.utils.Logging;

public class AaiOverTLSPropertySupplier {

    public String getUsername() {
        return SystemProperties.getProperty(AAIProperties.AAI_VID_USERNAME);
    }

    public String getPassword() {
        return Password.deobfuscate(SystemProperties.getProperty(AAIProperties.AAI_VID_PASSWD_X));
    }

    public String getRequestId() {
        return Logging.extractOrGenerateRequestId();
    }

    public String getRandomUUID(){
       return UUID.randomUUID().toString();
    }

}
