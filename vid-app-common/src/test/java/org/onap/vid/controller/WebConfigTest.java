/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2020 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.onap.vid.testUtils.TestUtils.setStringsInStringFields;

import javax.inject.Inject;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.portalsdk.core.domain.App;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.config.DataSourceConfig;
import org.onap.vid.properties.Features;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

@ContextConfiguration(classes = {DataSourceConfig.class, SystemProperties.class})
public class WebConfigTest extends AbstractTestNGSpringContextTests {

    @Inject private DataAccessService dataAccessService;
    @Mock FeatureManager featureManager;
    @InjectMocks WebConfig webConfig;

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        webConfig.dataAccessService = dataAccessService;

        // set default app values
        App defaultApp = setStringsInStringFields(new App());
        defaultApp.setId(1L);
        dataAccessService.saveDomainObject(defaultApp, null);

        // enable feature
        when(featureManager.isActive(Features.FLAG_GUILIN_CONFIG_PORTAL_APP_PASSWORD)).thenReturn(true);
    }

    @Test
    public void persistPortalAppPassword_givenFlagAndValue_thenValueIsPersisted() {
        assertThat(setAndGetPortalAppPassword("fresh password"),
            equalTo("fresh password"));
    }

    @Test
    public void persistPortalAppPassword_givenEmptyValue_thenDbValueUnchanged() {
        assertThat(setAndGetPortalAppPassword(""),
            equalTo("appPassword"));
    }

    @Test
    public void persistPortalAppPassword_givenNullValue_thenDbValueUnchanged() {
        assertThat(setAndGetPortalAppPassword(null),
            equalTo("appPassword"));
    }

    public String setAndGetPortalAppPassword(String password) {
        webConfig.portalAppPassword = password;

        webConfig.persistPortalAppPassword();
        return appDomainPasswordFromDB();
    }

    private String appDomainPasswordFromDB() {
        return ((App) dataAccessService.getDomainObject(App.class, 1L, null))
            .getAppPassword();
    }
}