/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

package org.openecomp.vid.mso;

import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.vid.mso.rest.MsoRestClientNew;

/**
 * A factory for creating MsoRestInterface objects.
 */
public class MsoRestInterfaceFactory {

    /**
     * Gets the single instance of MsoRestInterfaceFactory.
     *
     * @return single instance of MsoRestInterfaceFactory
     */
    public static MsoInterface getInstance() {
        String msoPropertyName = "mso.client.type";
        if (SystemProperties.containsProperty(msoPropertyName) &&
                SystemProperties.getProperty(msoPropertyName).equals("LOCAL")) {
            return new MsoLocalClientNew();
        } else
            return new MsoRestClientNew();
    }
}
