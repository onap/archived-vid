/*
 * ============LICENSE_START==========================================
 * ONAP Portal SDK
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */
package org.onap.portalapp.conf;

import java.util.List;

import org.junit.Test;
import org.onap.portalapp.scheduler.RegistryAdapter;
import org.onap.portalsdk.core.auth.LoginStrategy;
import org.onap.portalsdk.core.service.DataAccessService;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

public class ExternalAppConfigTest {

    private ExternalAppConfig createTestSubject() {
        return new ExternalAppConfig();
    }

    @Test
    public void testViewResolver() throws Exception {
        ExternalAppConfig testSubject;
        ViewResolver result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.viewResolver();
    }



    @Test
    public void testDataAccessService() throws Exception {
        ExternalAppConfig testSubject;
        DataAccessService result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.dataAccessService();
    }

    @Test
    public void testAddTileDefinitions() throws Exception {
        ExternalAppConfig testSubject;
        List<String> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.addTileDefinitions();
    }

   

    @Test
    public void testCacheManager() throws Exception {
        ExternalAppConfig testSubject;
        //AbstractCacheManager result;

        // default test
        testSubject = createTestSubject();
        testSubject.cacheManager();
    }
   


    @Test
    public void testSetSchedulerRegistryAdapter() throws Exception {
        ExternalAppConfig testSubject;
        RegistryAdapter schedulerRegistryAdapter = null;

        // default test
        testSubject = createTestSubject();
    }

    @Test
    public void testLoginStrategy() throws Exception {
        ExternalAppConfig testSubject;
        LoginStrategy result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.loginStrategy();
    }
}