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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.Test;
import org.onap.portalapp.login.LoginStrategyImpl;
import org.onap.portalapp.scheduler.RegistryAdapter;
import org.onap.portalsdk.core.auth.LoginStrategy;
import org.onap.portalsdk.core.onboarding.exception.PortalAPIException;
import org.onap.portalsdk.core.service.DataAccessService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ViewResolver;

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
        assertNotNull(testSubject);
    }



    @Test
    public void testDataAccessService() throws Exception {
        ExternalAppConfig testSubject;
        DataAccessService result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.dataAccessService();
        assertNotNull(testSubject);
    }

    @Test
    public void testAddTileDefinitions() throws Exception {
        ExternalAppConfig testSubject;
        List<String> result;

        // default test
        testSubject = createTestSubject();
        result = testSubject.addTileDefinitions();
        assertNotNull(testSubject);
    }

   

    @Test
    public void testCacheManager() throws Exception {
        ExternalAppConfig testSubject;
        //AbstractCacheManager result;

        // default test
        testSubject = createTestSubject();
        testSubject.cacheManager();
        assertNotNull(testSubject);
    }
   


    @Test
    public void testSetSchedulerRegistryAdapter() throws Exception {
        ExternalAppConfig testSubject;
        RegistryAdapter schedulerRegistryAdapter = null;

        // default test
        testSubject = createTestSubject();
        assertNotNull(testSubject);
    }

    @Test
    public void loginStrategy_givenEmptyString_yieldDefault() throws Exception {
        assertThat(new ExternalAppConfig().loginStrategy(""),
            is(instanceOf(LoginStrategyImpl.class)));
    }

    @Test
    public void loginStrategy_givenNullString_yieldDefault() throws Exception {
        assertThat(new ExternalAppConfig().loginStrategy(null),
            is(instanceOf(LoginStrategyImpl.class)));
    }

    public static class DummyLoginStrategy extends LoginStrategy {
        @Override
        public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response) {
            return null;
        }

        @Override
        public String getUserId(HttpServletRequest request) {
            return null;
        }
    }

    @Test
    public void loginStrategy_givenClassname_yieldClassInstance() throws Exception {
        assertThat(
            new ExternalAppConfig().loginStrategy("org.onap.portalapp.conf.ExternalAppConfigTest$DummyLoginStrategy"),
            is(instanceOf(DummyLoginStrategy.class)));
    }

    @Test(expected = ClassNotFoundException.class)
    public void loginStrategy_givenMissingClassname_throwsException() throws Exception {
        new ExternalAppConfig().loginStrategy("no.real.classname");
        Assert.fail("should throw");
    }
}
