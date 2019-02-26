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

package org.onap.vid.job.command;

import com.google.common.collect.ImmutableMap;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.model.GroupProperties;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.VfModule;
import org.onap.vid.services.VidService;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class CommandUtilsTest {

    @InjectMocks
    CommandUtils commandUtils;

    @Mock
    VidService vidService;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void resetVidService() {
        reset(vidService);
    }

    @DataProvider
    public static Object[][] trueAndFalse() {
        return new Object[][]{ {true}, {false} };
    }

    @Test(dataProvider="trueAndFalse")
    void testIsVfModelIsBaseModule(boolean isBase) throws AsdcCatalogException {
        final String serviceModelUuid = UUID.randomUUID().toString();
        final String vfModuleUuid = UUID.randomUUID().toString();

        ServiceModel mockedServiceModel = mock(ServiceModel.class);
        VfModule mockedVfModule = mock(VfModule.class);
        GroupProperties mockedGroupProperties = mock(GroupProperties.class);
        Map<String, VfModule> vfModulesMap = ImmutableMap.of(randomAlphanumeric(10), mockedVfModule);

        when(vidService.getService(serviceModelUuid)).thenReturn(mockedServiceModel);
        when(mockedServiceModel.getVfModules()).thenReturn(vfModulesMap);
        when(mockedVfModule.getUuid()).thenReturn(vfModuleUuid);
        when(mockedVfModule.getProperties()).thenReturn(mockedGroupProperties);
        when(mockedGroupProperties.getBaseModule()).thenReturn(isBase);

        assertThat(commandUtils.isVfModuleBaseModule(serviceModelUuid, vfModuleUuid), equalTo(isBase));
    }

    @Test(expectedExceptions = AsdcCatalogException.class)
    void whenCantFindModelInSdc_thenExceptionIsThrown() throws AsdcCatalogException {
        String serviceModelUuid = UUID.randomUUID().toString();
        when(vidService.getService(serviceModelUuid)).thenReturn(null);
        commandUtils.isVfModuleBaseModule(serviceModelUuid, "abc");
    }

    @Test(expectedExceptions = AsdcCatalogException.class)
    void whenCantFindVfModuleInModel_thenExceptionIsThrown() throws AsdcCatalogException {

        String serviceModelUuid = UUID.randomUUID().toString();

        ServiceModel mockedServiceModel = mock(ServiceModel.class);
        Map<String, VfModule> emptyMap = Collections.emptyMap();

        when(vidService.getService(serviceModelUuid)).thenReturn(mockedServiceModel);
        when(mockedServiceModel.getVfModules()).thenReturn(emptyMap);

        commandUtils.isVfModuleBaseModule(serviceModelUuid, "abc");
    }
}
