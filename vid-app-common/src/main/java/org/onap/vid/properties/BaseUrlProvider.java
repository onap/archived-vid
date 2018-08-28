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
