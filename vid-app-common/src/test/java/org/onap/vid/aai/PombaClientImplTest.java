/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2019 Nokia
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
package org.onap.vid.aai;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.URL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.vid.model.PombaInstance.PombaRequest;
import org.onap.vid.model.PombaInstance.ServiceInstance;
import org.onap.vid.utils.SystemPropertiesWrapper;

@RunWith(MockitoJUnitRunner.class)
public class PombaClientImplTest {

    @Mock
    private SystemPropertiesWrapper systemPropertiesWrapper;
    @Mock
    private PombaRestInterface pombaRestInterface;
    @InjectMocks
    private PombaClientImpl pombaClient;

    @Test
    public void should_doHttpPost_withGivenPombaRequest() throws IOException {
        //Given
        String expectedUrl = "http://localhost/dummyUrl";
        given(systemPropertiesWrapper.getProperty("pomba.server.url")).willReturn(expectedUrl);
        String expectedPayload = readExpectedPombaJsonRequest();
        PombaRequest pombaRequest = createPombaRequest();

        //When
        pombaClient.verify(pombaRequest);

        //Then
        then(pombaRestInterface).should().RestPost("VidAaiController", expectedUrl, expectedPayload);
    }

    private String readExpectedPombaJsonRequest() throws IOException {
        URL url = PombaClientImplTest.class.getClassLoader().getResource("pomba_request.json");
        PombaRequest expectedPombaRequest = new ObjectMapper().readValue(url, PombaRequest.class);
        return new ObjectMapper().writeValueAsString(expectedPombaRequest);
    }

    private PombaRequest createPombaRequest() {
        ServiceInstance serviceInstance1 = createServiceInstance("serviceType1", "serviceInstanceId1", "customerId1",
            "modelVersion1", "modelInvariantId1");
        ServiceInstance serviceInstance2 = createServiceInstance("serviceType2", "serviceInstanceId2", "customerId2",
            "modelVersion2", "modelInvariantId2");

        PombaRequest pombaRequest = new PombaRequest(Lists.newArrayList(serviceInstance1, serviceInstance2));
        return pombaRequest;
    }

    private ServiceInstance createServiceInstance(String serviceType, String serviceInstanceId, String customerId,
        String modelVersionId, String modelInvariantId) {
        ServiceInstance serviceInstance = new ServiceInstance(
            serviceInstanceId,
            modelVersionId,
            modelInvariantId,
            customerId,
            serviceType);
        return serviceInstance;
    }

}
