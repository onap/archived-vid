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

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.onap.vid.testUtils.TestUtils.setStringsInStringFields;

import java.util.UUID;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.model.GroupProperties;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.VfModule;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.services.VidService;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
    public static Object[][] vfModuleModelInfos() {
        ModelInfo modelInfoMatchByUuid = setStringsInStringFields(new ModelInfo());
        modelInfoMatchByUuid.setModelCustomizationId("toscaCustomizationUuid");

        ModelInfo modelInfoMatchByName = setStringsInStringFields(new ModelInfo());
        modelInfoMatchByName.setModelCustomizationName("toscaCustomizationName");

        ModelInfo modelInfoDontMatch = setStringsInStringFields(new ModelInfo());

        return new Object[][]{
            {true, modelInfoMatchByUuid, true},
            {false, modelInfoMatchByUuid, false},

            {true, modelInfoMatchByName, true},
            {false, modelInfoMatchByName, false},

            {true, modelInfoDontMatch, false},
            {false, modelInfoDontMatch, false},
        };
    }

    @Test(dataProvider="vfModuleModelInfos")
    void testIsVfModelIsBaseModule(boolean isBaseInTosca, ModelInfo instanceModelInfo, boolean expected) {
        GroupProperties mockedGroupProperties = mock(GroupProperties.class);
        when(mockedGroupProperties.getBaseModule()).thenReturn(isBaseInTosca);

        VfModule toscaVfModuleModelInfo = mock(VfModule.class);
        when(toscaVfModuleModelInfo.getCustomizationUuid()).thenReturn("toscaCustomizationUuid");
        when(toscaVfModuleModelInfo.getModelCustomizationName()).thenReturn("toscaCustomizationName");
        when(toscaVfModuleModelInfo.getProperties()).thenReturn(mockedGroupProperties);


        ServiceModel mockedServiceModel = mock(ServiceModel.class);
        when(mockedServiceModel.getVfModules()).thenReturn(singletonMap("some-name", toscaVfModuleModelInfo));

        String serviceModelUuid = UUID.randomUUID().toString();
        when(vidService.getServiceModelOrThrow(serviceModelUuid)).thenReturn(mockedServiceModel);

        assertThat(commandUtils.isVfModuleBaseModule(serviceModelUuid, instanceModelInfo), equalTo(expected));
    }

    @Test
    void whenCantFindVfModulesInModel_thenResultIsFalse() {
        String serviceModelUuid = UUID.randomUUID().toString();

        ServiceModel mockedServiceModel = mock(ServiceModel.class);

        when(vidService.getServiceModelOrThrow(serviceModelUuid)).thenReturn(mockedServiceModel);
        when(mockedServiceModel.getVfModules()).thenReturn(emptyMap());

        assertThat(
            commandUtils.isVfModuleBaseModule(serviceModelUuid, mock(ModelInfo.class)), is(false));
    }
}
