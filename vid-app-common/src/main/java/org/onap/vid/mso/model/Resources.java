/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2020 Nokia Intellectual Property. All rights reserved.
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

package org.onap.vid.mso.model;

import java.util.List;

public class Resources {

    private final List<ServiceInstantiationRequestDetails.ServiceInstantiationVnf> vnfs;
    private final List<ServiceInstantiationRequestDetails.ServiceInstantiationPnf> pnfs;

    public Resources(List<ServiceInstantiationRequestDetails.ServiceInstantiationVnf> vnfs, List<ServiceInstantiationRequestDetails.ServiceInstantiationPnf> pnfs) {
        this.vnfs = vnfs;
        this.pnfs = pnfs;
    }

    public List<ServiceInstantiationRequestDetails.ServiceInstantiationVnf> getVnfs() {
        return vnfs;
    }

    public List<ServiceInstantiationRequestDetails.ServiceInstantiationPnf> getPnfs() {
        return pnfs;
    }
}

