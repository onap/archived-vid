package org.onap.vid.services;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.asdc.parser.ToscaParserImpl2;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.properties.Features;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toMap;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

public class VidServiceImplTest {

    @Mock(answer = Answers.RETURNS_MOCKS)
    AsdcClient asdcClientMock;

    @Mock(answer = Answers.RETURNS_MOCKS)
    ToscaParserImpl2 toscaParserMock;

    @Mock
    FeatureManager featureManager;

    private final UUID uuid1 = UUID.randomUUID();
    private final UUID uuid2 = UUID.randomUUID();
    private final UUID uuid3 = UUID.randomUUID();
    private final Map<UUID, Service> pseudoServiceByUuid = ImmutableMap.of(
            uuid1, mock(Service.class),
            uuid2, mock(Service.class),
            uuid3, mock(Service.class)
    );

    private final Map<Service, ServiceModel> pseudoModelByService =
            pseudoServiceByUuid.values().stream()
                    .collect(toMap(service -> service, service -> mock(ServiceModel.class)));
    private VidServiceImpl vidService;

    private ServiceModel expectedServiceModelForUuid(UUID uuid) {
        final ServiceModel serviceModel = pseudoModelByService.get(pseudoServiceByUuid.get(uuid));
        assertThat(serviceModel, is(not(nullValue())));
        return serviceModel;
    }

    @BeforeMethod
    public void initMocks() throws AsdcCatalogException, SdcToscaParserException, IllegalAccessException {
        MockitoAnnotations.initMocks(this);

        vidService = new VidServiceImpl(asdcClientMock, featureManager);
        FieldUtils.writeField(vidService, "toscaParser", toscaParserMock, true);

        when(featureManager.isActive(Features.FLAG_SERVICE_MODEL_CACHE)).thenReturn(true);

        when(asdcClientMock.getService(any())).thenAnswer(invocation ->  pseudoServiceByUuid.get(invocation.getArguments()[0]));
        when(toscaParserMock.makeServiceModel(any(), any())).thenAnswer(invocation ->  pseudoModelByService.get(invocation.getArguments()[1]));
    }

    @Test
    public void whenGetServiceMultipleTimes_asdcIsCalledOnlyOnce() throws AsdcCatalogException {
        vidService.getService(uuid1.toString());
        vidService.getService(uuid1.toString());
        vidService.getService(uuid1.toString());

        verify(asdcClientMock, times(1)).getService(uuid1);
    }

    @Test
    public void whenGetServiceTwiceWithResetBetween_asdcIsCalledTwice() throws AsdcCatalogException {
        vidService.getService(uuid1.toString());
        vidService.invalidateServiceCache();
        vidService.getService(uuid1.toString());

        verify(asdcClientMock, times(2)).getService(uuid1);
    }

    @Test
    public void cache_saves_service_model_correctly() throws AsdcCatalogException {
        ServiceModel service1 = vidService.getService(uuid1.toString());
        ServiceModel service2 = vidService.getService(uuid1.toString());
        ServiceModel service3 = vidService.getService(uuid1.toString());

        assertThat(service1, sameInstance(expectedServiceModelForUuid(uuid1)));
        assertThat(service1, sameInstance(service2));
        assertThat(service1, sameInstance(service3));
    }

    @Test
    public void cache_provide_correct_serviceModel() throws AsdcCatalogException {
        ServiceModel service2 = vidService.getService(uuid2.toString());
        assertThat(service2, sameInstance(expectedServiceModelForUuid(uuid2)));

        ServiceModel service3 = vidService.getService(uuid3.toString());
        assertThat(service3, sameInstance(expectedServiceModelForUuid(uuid3)));

        UUID nonExisting = UUID.randomUUID();
        ServiceModel service4 = vidService.getService(nonExisting.toString());
        assertThat(service4, is(nullValue()));
    }

    @Test(expectedExceptions = AsdcCatalogException.class, expectedExceptionsMessageRegExp = "someMessage")
    public void whenAsdcClientThrowAsdcCatalogException_thenGetServiceAlsoThrowIt() throws AsdcCatalogException {
        when(asdcClientMock.getServiceToscaModel(any())).thenThrow(new AsdcCatalogException("someMessage"));
        vidService.getService(uuid1.toString());
    }

}

