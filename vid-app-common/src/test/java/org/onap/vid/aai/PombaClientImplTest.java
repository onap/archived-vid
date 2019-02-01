package org.onap.vid.aai;


import org.assertj.core.util.Lists;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.onap.vid.model.PombaInstance.PombaRequest;
import org.onap.vid.model.PombaInstance.ServiceInstance;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class PombaClientImplTest {

    private static final String EXPECTED_REQUEST_IN_JSON = "{\"serviceInstanceList\":[{\"serviceInstanceId\":\"sample\",\"modelVersionId\":\"sample\",\"modelInvariantId\":\"sample\",\"customerId\":\"sample\",\"serviceType\":\"sample\"}]}";
    private static final String SAMPLE_VALUE = "sample";

    @Mock
    private PombaRestInterface pombaRestInterface;


    @InjectMocks
    private PombaClientImpl client;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldProperlySendPombaRequest() {
        client.verify(request());

        verify(pombaRestInterface).RestPost("VidAaiController", "", EXPECTED_REQUEST_IN_JSON);
    }

    PombaRequest request() {
        ServiceInstance instance = new ServiceInstance();
        instance.customerId = SAMPLE_VALUE;
        instance.modelInvariantId = SAMPLE_VALUE;
        instance.modelVersionId = SAMPLE_VALUE;
        instance.serviceType = SAMPLE_VALUE;
        instance.serviceInstanceId = SAMPLE_VALUE;
        PombaRequest request = new PombaRequest();
        request.serviceInstanceList = Lists.list(instance);
        return request;
    }
}