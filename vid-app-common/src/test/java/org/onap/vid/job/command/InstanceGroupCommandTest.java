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

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.beanutils.BeanUtils;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.Action;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.model.serviceInstantiation.InstanceGroup;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.springframework.http.HttpMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

public class InstanceGroupCommandTest {

    @Mock(answer = Answers.RETURNS_MOCKS)
    RestMsoImplementation restMso;

    @Mock InstanceGroup instanceGroupRequest;

    @Mock(answer = Answers.RETURNS_MOCKS)
    MsoResultHandlerService msoResultHandlerService;

    @Mock(answer = Answers.RETURNS_MOCKS)
    MsoRequestBuilder msoRequestBuilder;

    @Mock WatchChildrenJobsBL watchChildrenJobsBL;

    @Mock(answer = Answers.RETURNS_MOCKS)
    AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Mock(answer = Answers.RETURNS_MOCKS)
    JobsBrokerService jobsBrokerService;

    @Mock(answer = Answers.RETURNS_MOCKS)
    JobAdapter jobAdapter;

    @Mock InProgressStatusService inProgressStatusService;

    @Mock FeatureManager featureManager;

    @InjectMocks
    private InstanceGroupCommand command;

    @BeforeMethod
    public void initMocks() {
        command = null;
        MockitoAnnotations.initMocks(this);
    }
    @DataProvider
    public static Object[][] testApis() {
        return new Object[][]{
                {"VNF_API"}, {null}};
    }
    @Test(dataProvider = "testApis")
    public void createMyself_callsMso(String testApi) {
        final ModelInfo serviceModelInfo = setRandomStrings(new ModelInfo());
        final String serviceInstanceId = "service-instance-id";
        final String userId = "ff3223";

        when(instanceGroupRequest.getAction()).thenReturn(Action.Delete);

        JobSharedData sharedData = new JobSharedData(
                null, userId, instanceGroupRequest, testApi);
        command.init(sharedData, ImmutableMap.of(
                "resourceModelInfos", ImmutableMap.of("SERVICE_MODEL_INFO", serviceModelInfo),
                "resourceInstancesIds", ImmutableMap.of("SERVICE_INSTANCE_ID", serviceInstanceId)
        ));

        command.createMyself();

        verify(msoRequestBuilder).generateInstanceGroupInstantiationRequest(
                same(instanceGroupRequest), eq(serviceModelInfo), eq(serviceInstanceId), eq(userId), eq(testApi));
        verify(restMso, only()).restCall(eq(HttpMethod.POST), eq(RequestReferencesContainer.class), any(), any(), eq(Optional.empty()));
    }

    private ModelInfo setRandomStrings(ModelInfo object) {
        try {
            Set<String> fields = BeanUtils.describe(object).keySet();
            BeanUtils.populate(object,
                    fields.stream().collect(toMap(identity(), s -> randomAlphanumeric(4))));
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
