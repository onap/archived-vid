package org.onap.vid.mso;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.vid.mso.MsoBusinessLogicImpl;
import org.onap.vid.mso.MsoInterface;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.mso.rest.RequestDetails;
import org.onap.vid.mso.rest.RequestDetailsWrapper;
import org.testng.annotations.Test;

import java.net.URL;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class MsoBusinessLogicTest {

    @InjectMocks
    private MsoBusinessLogicImpl msoBusinessLogic;

    @Mock
    private MsoInterface msoClient;

    @Test
    public void testCreateInstance() throws Exception {
        String instanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
        final RequestDetailsWrapper requestDetailsWrapper = new RequestDetailsWrapper();
        requestDetailsWrapper.requestDetails = setRequestDetails("mso_request_create_configuration.json");
        Mockito.doReturn(getOkResponse(instanceId)).when(msoClient).createConfigurationInstance(requestDetailsWrapper, "/serviceInstances/v6/3f93c7cb-2fd0-4557-9514-e189b7b04f9d/configurations");
        final MsoResponseWrapper msoResponseWrapper = msoBusinessLogic.createConfigurationInstance(requestDetailsWrapper, instanceId);

        assertNotNull(msoResponseWrapper);
        assertEquals(202, msoResponseWrapper.getStatus());
    }

    private MsoResponseWrapper getOkResponse(String instanceId){
        MsoResponseWrapper responseWrapper = new MsoResponseWrapper();
        String entity = " \"body\": {\n" +
                "      \"requestReferences\": {\n" +
                "        \"instanceId\": \""+instanceId+"\",\n" +
                "        \"requestId\": \"b6dc9806-b094-42f7-9386-a48de8218ce8\"\n" +
                "      }";
        responseWrapper.setEntity(entity);
        responseWrapper.setStatus(202);
        return responseWrapper;
    }

    private RequestDetails setRequestDetails(String bodyFileName)throws Exception {
        final URL resource = this.getClass().getResource("/payload_jsons/" + bodyFileName);
        ObjectMapper mapper = new ObjectMapper();
        RequestDetails requestDetails = mapper.readValue(resource, RequestDetails.class);
        return requestDetails;

    }
}
