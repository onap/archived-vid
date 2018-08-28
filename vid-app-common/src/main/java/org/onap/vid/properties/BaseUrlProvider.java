/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia. All rights reserved.
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

package org.onap.vid.properties;

import org.togglz.core.manager.FeatureManager;

import java.util.function.Supplier;

public class BaseUrlProvider {

    private FeatureManager featuresManager;
    private Supplier<String> httpsPropertiesSupplier;
    private Supplier<String> httpPropertiesSupplier;

    public BaseUrlProvider(FeatureManager featureManager, Supplier<String> httpsPropertiesSupplier, Supplier<String> httpPropertiesSupplier) {
        this.featuresManager = featureManager;
        this.httpsPropertiesSupplier = httpsPropertiesSupplier;
        this.httpPropertiesSupplier = httpPropertiesSupplier;
    }

    public String getBaseUrl() {
        return featuresManager.isActive(Features.FLAG_SECURED_ENDPOINTS) ? httpsPropertiesSupplier.get() : httpPropertiesSupplier.get();
    }

}
