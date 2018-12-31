package org.onap.vid.job.command;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.beanutils.BeanUtils;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.model.serviceInstantiation.InstanceGroup;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.springframework.http.HttpMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class InstanceGroupCommandTest {

    @Mock(answer = Answers.RETURNS_MOCKS)
    RestMsoImplementation restMso;

    @Mock InstanceGroup instanceGroupRequest;

    @Mock(answer = Answers.RETURNS_MOCKS)
    MsoResultHandlerService msoResultHandlerService;

    @Mock WatchChildrenJobsBL watchChildrenJobsBL;

    @Mock(answer = Answers.RETURNS_MOCKS)
    AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Mock InProgressStatusService inProgressStatusService;

    @InjectMocks
    private InstanceGroupCommand command;

    @BeforeMethod
    public void initMocks() {
        command = null;
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createMyself_callsMso() {
        final ModelInfo serviceModelInfo = setRandomStrings(new ModelInfo());
        final String serviceInstanceId = "service-instance-id";
        final String userId = "ff3223";

        command.init(new JobSharedData(
                null, userId, instanceGroupRequest
        ), ImmutableMap.of(
                "resourceModelInfos", ImmutableMap.of("SERVICE_MODEL_INFO", serviceModelInfo),
                "resourceInstancesIds", ImmutableMap.of("SERVICE_INSTANCE_ID", serviceInstanceId)
        ));

        command.createMyself();

        verify(asyncInstantiationBL).generateInstanceGroupInstantiationRequest(
                same(instanceGroupRequest), eq(serviceModelInfo), eq(serviceInstanceId), eq(userId));
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
