package org.onap.vid.services;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.UUID;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.onap.vid.dal.AsyncInstantiationRepository;
import org.onap.vid.model.ModelUtil;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiationTemplate;
import org.onap.vid.model.serviceInstantiation.Vnf;
import org.onap.vid.testUtils.TestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class InstantiationTemplatesServiceTest {

    @Mock
    private AsyncInstantiationRepository asyncInstantiationRepository;

    @Mock
    private ModelUtil modelUtil;

    @InjectMocks
    private InstantiationTemplatesService instantiationTemplatesService;

    @BeforeMethod
    public void initMocks() {
        TestUtils.initMockitoMocks(this);
    }

    @Test
    public void getJobRequestAsTemplate_whenIsCalled_asyncInstantiationRepositoryGetJobRequestIsInvoked() {
        UUID jobId = UUID.randomUUID();
        ServiceInstantiation serviceInstantiationMock = mock(ServiceInstantiation.class, RETURNS_DEEP_STUBS);
        doReturn(serviceInstantiationMock).when(asyncInstantiationRepository).getJobRequest(jobId);

        // When...
        instantiationTemplatesService.getJobRequestAsTemplate(jobId);

        verify(asyncInstantiationRepository).getJobRequest(jobId);
    }

    @Test
    public void getJobRequestAsTemplate_givenModelUtilReturnsValue_thenVnfCounterMapIsPopulatedWithThatValue() {
        Map<String, Integer> dummyNonEmptyMap = ImmutableMap.of("dummyKey", 9);
        ServiceInstantiation serviceInstantiation = mock(ServiceInstantiation.class, RETURNS_DEEP_STUBS);
        doReturn(serviceInstantiation).when(asyncInstantiationRepository).getJobRequest(any());

        // Given...
        when(modelUtil.getExistingCounterMap(any(), any())).thenAnswer(
            // return empty counterMap if argument is an empty map; otherwise return a mocked response
            invocation -> ((Map)invocation.getArgument(0)).size() == 0 // isEmpty() does not work on mocks
                ? ImmutableMap.of()
                : dummyNonEmptyMap
        );

        // only vnf will have a non-empty value
        when(serviceInstantiation.getVnfs()).thenReturn(ImmutableMap.of("1", mock(Vnf.class)));

        // When...
        ServiceInstantiationTemplate result = instantiationTemplatesService.getJobRequestAsTemplate(UUID.randomUUID());

        assertThat(result, hasProperty("existingVNFCounterMap", jsonEquals(dummyNonEmptyMap)));
        assertThat(result, hasProperty("existingNetworksCounterMap", anEmptyMap()));
        assertThat(result, hasProperty("existingVnfGroupCounterMap", anEmptyMap()));
        assertThat(result, hasProperty("existingVRFCounterMap", anEmptyMap()));
    }

}
