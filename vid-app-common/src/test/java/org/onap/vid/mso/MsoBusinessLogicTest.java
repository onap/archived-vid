package org.onap.vid.mso;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.controllers.MsoConfig;
import org.onap.vid.mso.MsoBusinessLogicImpl;
import org.onap.vid.mso.MsoInterface;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.mso.rest.RequestDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URL;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Test
@ContextConfiguration(classes = { SystemProperties.class, MsoConfig.class })
@WebAppConfiguration
public class MsoBusinessLogicTest extends AbstractTestNGSpringContextTests {

    @InjectMocks
    private MsoBusinessLogicImpl msoBusinessLogic;

    @Mock
    private MsoInterface msoClient;

    @BeforeMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateInstance() throws Exception {
        String instanceId = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
        final RequestDetails requestDetails = setRequestDetails("mso_request_create_configuration.json");
        Mockito.doReturn(getOkResponse(instanceId)).when(msoClient).createConfigurationInstance(requestDetails, "/serviceInstances/v6/3f93c7cb-2fd0-4557-9514-e189b7b04f9d/configurations");
        final MsoResponseWrapper msoResponseWrapper = msoBusinessLogic.createConfigurationInstance(requestDetails, instanceId);

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
