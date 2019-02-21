package org.onap.vid.config;

import org.mockito.Mockito;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.services.CloudOwnerService;
import org.onap.vid.services.CloudOwnerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.manager.FeatureManager;

@Configuration
public class MockedAaiClientAndFeatureManagerConfig {

    @Bean
    public FeatureManager featureManager() {
        return Mockito.mock(FeatureManager.class);
    }

    @Bean
    public CloudOwnerService cloudOwnerService(AaiClientInterface aaiClient, FeatureManager featureManager) {
        return new CloudOwnerServiceImpl(aaiClient, featureManager);
    }

    @Bean
    public AaiClientInterface aaiClient() {
        return Mockito.mock(AaiClientInterface.class);
    }
}
