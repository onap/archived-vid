/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Modifications Copyright 2019 Nokia
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

package org.onap.vid.utils;

import org.apache.commons.lang.StringUtils;

import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.stereotype.Component;

@Component
public class SystemPropertiesWrapper {
    public String getProperty(String key) {
        return SystemProperties.getProperty(key);
    }
    public String getOrDefault(String key, String defaultValue) {
        return StringUtils.defaultIfEmpty(getProperty(key), defaultValue);
    }
}
