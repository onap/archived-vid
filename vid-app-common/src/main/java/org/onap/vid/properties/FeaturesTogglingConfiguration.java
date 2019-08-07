/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

import static org.apache.commons.lang3.StringUtils.startsWith;

import java.io.File;
import javax.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.repository.file.FileBasedStateRepository;
import org.togglz.spring.listener.TogglzApplicationContextBinderApplicationListener;

@Configuration
public class FeaturesTogglingConfiguration {
    @Bean
    public ApplicationListener getApplicationListener() {
        return new TogglzApplicationContextBinderApplicationListener();
    }

    @Bean
    public FeatureManager featureManager(ServletContext servletContext, Environment environment) {
        final String defaultFilename = "features.properties";

        String pathToWebinfConf = environment.resolveRequiredPlaceholders("${container.classpath:}/WEB-INF/conf/");
        String filename = environment.getProperty("features.set.filename");

        if (StringUtils.isBlank(filename)) {
            filename = defaultFilename;
        }

        filename = StringUtils.trimToNull(filename);

        return new FeatureManagerBuilder()
            .featureEnum(Features.class)
            .stateRepository(
                new FileBasedStateRepository(file(pathToWebinfConf, filename, servletContext))
            ).build();
    }

    protected File file(String path, String filename, ServletContext servletContext) {
        if (filename.startsWith("/")) {
            // ignore path
            return new File(filename);
        } else if (startsWith(path, "file:")) {
            // use path
            return new File(path + filename);
        } else {
            // load through context
            return new File(servletContext.getRealPath(path + filename));
        }
    }
}
