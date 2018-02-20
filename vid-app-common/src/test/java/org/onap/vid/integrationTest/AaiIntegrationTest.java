package org.onap.vid.integrationTest;

import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.aai.AaiClient;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.model.GetServiceModelsByDistributionStatusResponse;
import org.onap.vid.aai.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;


@ContextConfiguration(classes = {SystemProperties.class})


@WebAppConfiguration
public class AaiIntegrationTest extends AbstractTestNGSpringContextTests {


    @Autowired
    MockServletContext servletContext;


    @Test
    public void testGetServiceModelsFromAai() throws Exception {
        AaiClientInterface aaiClient = new AaiClient(servletContext);
        AaiResponse<GetServiceModelsByDistributionStatusResponse> serviceModelsByDistributionStatusResponse = aaiClient.getServiceModelsByDistributionStatus();
        GetServiceModelsByDistributionStatusResponse response = serviceModelsByDistributionStatusResponse.getT();
        for(Result result: response.getResults()){
            Assert.assertNotNull(result.getModel().getModelInvariantId());
            Assert.assertNotNull(result.getModel().getModelVers().getModelVer().get(0).getModelVersionId());
            Assert.assertNotNull(result.getModel().getModelVers().getModelVer().get(0).getModelName());
            Assert.assertNotNull(result.getModel().getModelVers().getModelVer().get(0).getModelVersion());
        }
    }


}
