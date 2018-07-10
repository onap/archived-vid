package org.onap.vid.config;

import org.mockito.Mockito;
import org.onap.vid.aai.AaiClientInterface;
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
    public AaiClientInterface aaiClient() {
        return Mockito.mock(AaiClientInterface.class);
    }
}
