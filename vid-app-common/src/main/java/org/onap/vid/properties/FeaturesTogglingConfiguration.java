package org.onap.vid.properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.repository.file.FileBasedStateRepository;
import org.togglz.spring.listener.TogglzApplicationContextBinderApplicationListener;

import javax.servlet.ServletContext;
import java.io.File;

@Configuration
public class FeaturesTogglingConfiguration {
    @Bean
    public ApplicationListener getApplicationListener() {
        return new TogglzApplicationContextBinderApplicationListener();
    }

    @Bean
    public FeatureManager featureManager(ServletContext servletContext, Environment environment) {
        final String defaultFilename = "features.properties";

        String filename = environment.getProperty("featureFlags.filename");

        if (StringUtils.isBlank(filename)) {
            filename = defaultFilename;
        }

        return new FeatureManagerBuilder()
                .featureEnum(Features.class)
                .stateRepository(new FileBasedStateRepository(
                        new File(servletContext.getRealPath("/WEB-INF/conf/" + filename))
                ))
                .build();
    }
}
