package org.onap.vid.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javacrumbs.jsonunit.JsonAssert;
import org.apache.commons.io.IOUtils;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.parser.ToscaParserImpl2;
import org.onap.vid.model.*;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.onap.vid.testUtils.TestUtils.assertJsonStringEqualsIgnoreNulls;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

//import org.junit.Assert;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = {LocalWebConfig.class, SystemProperties.class})
//@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration

public class VidControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    MockServletContext context;
    @Autowired
    private AsdcClient asdcClient;
    private ToscaParserImpl2 p2 = new ToscaParserImpl2();
    private ObjectMapper om = new ObjectMapper();


    @Test
    public void assertEqualsBetweenServices() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            Service expectedService = mockHelper.getNewServiceModel().getService();
            Service actualService = p2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getService();
            assertJsonStringEqualsIgnoreNulls(om.writeValueAsString(expectedService), om.writeValueAsString(actualService));
        }
    }

//    @Test
//    public void assertEqualBetweenObjects() throws Exception {
//        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
//            final Path csarPath = getCsarPath(mockHelper.getUuid());
//            System.out.println("Comparing for csar " + csarPath);
//            ServiceModel actualServiceModel = p2.makeServiceModel(csarPath, getServiceByUuid(mockHelper.getUuid()));
//            assertJsonStringEqualsIgnoreNulls(om.writeValueAsString(mockHelper.getNewServiceModel()), om.writeValueAsString(actualServiceModel));
//        }
//    }

//    @Test
//    public void assertEqualsBetweenNetworkNodes() throws Exception {
//        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
//            Map<String, Network> expectedNetworksMap = mockHelper.getNewServiceModel().getNetworks();
//            Map<String, Network> actualNetworksMap = p2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getNetworks();
//            for (Map.Entry<String, Network> entry : expectedNetworksMap.entrySet()) {
//                Network expectedNetwork = entry.getValue();
//                Network actualNetwork = actualNetworksMap.get(entry.getKey());
//                Assert.assertEquals(expectedNetwork.getModelCustomizationName(), actualNetwork.getModelCustomizationName());
//                verifyBaseNodeProperties(expectedNetwork, actualNetwork);
//                compareProperties(expectedNetwork.getProperties(), actualNetwork.getProperties());
//            }
//        }
//    }

    //Because we are not supporting the old flow, the JSON are different by definition.
    @Test
    public void assertEqualsBetweenVnfsOfTosca() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            Map<String, VNF> expectedVnfsMap = mockHelper.getNewServiceModel().getVnfs();
            Map<String, VNF> actualVnfsMap = p2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getVnfs();
            for (Map.Entry<String, VNF> entry : expectedVnfsMap.entrySet()) {
                VNF expectedVnf = entry.getValue();
                VNF actualVnf = actualVnfsMap.get(entry.getKey());
                verifyBaseNodeProperties(expectedVnf, actualVnf);
                Assert.assertEquals(expectedVnf.getModelCustomizationName(), actualVnf.getModelCustomizationName());
                compareProperties(expectedVnf.getProperties(), actualVnf.getProperties());
                assertJsonStringEqualsIgnoreNulls(om.writeValueAsString(expectedVnf), om.writeValueAsString(actualVnf));
            }
        }
    }

    @Test
    public void assertEqualsBetweenVolumeGroups() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            Map<String, VolumeGroup> actualVolumeGroups = p2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getVolumeGroups();
            Map<String, VolumeGroup> expectedVolumeGroups = mockHelper.getNewServiceModel().getVolumeGroups();
            JsonAssert.assertJsonEquals(actualVolumeGroups, expectedVolumeGroups);
        }
    }

    @Test
    public void assertEqualsBetweenVfModules() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            Map<String, VfModule> actualVfModules = p2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getVfModules();
            Map<String, VfModule> expectedVfModules = mockHelper.getNewServiceModel().getVfModules();
            JsonAssert.assertJsonEquals(actualVfModules, expectedVfModules);
        }
    }

    /*@Test
    public void assertEqualsBetweenPolicyConfigurationNodes() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            Map<String, PortMirroringConfig> actualConfigurations = p2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getConfigurations();
            Map<String, PortMirroringConfig> expectedConfigurations = mockHelper.getNewServiceModel().getConfigurations();
            JsonAssert.assertJsonEquals(actualConfigurations, expectedConfigurations);
        }
    }*/

    @Test
    public void assertEqualsBetweenServiceProxyNodes() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            Map<String, ServiceProxy> actualServiceProxies = p2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getServiceProxies();
            Map<String, ServiceProxy> expectedServiceProxies = mockHelper.getNewServiceModel().getServiceProxies();
            JsonAssert.assertJsonEquals(actualServiceProxies, expectedServiceProxies);
        }
    }

    private void verifyBaseNodeProperties(Node expectedNode, Node actualNode) {
        Assert.assertEquals(expectedNode.getName(), actualNode.getName());
        Assert.assertEquals(expectedNode.getCustomizationUuid(), actualNode.getCustomizationUuid());
        Assert.assertEquals(expectedNode.getDescription(), actualNode.getDescription());
        Assert.assertEquals(expectedNode.getInvariantUuid(), actualNode.getInvariantUuid());
        Assert.assertEquals(expectedNode.getUuid(), actualNode.getUuid());
        Assert.assertEquals(expectedNode.getVersion(), actualNode.getVersion());
    }

    private void compareProperties(Map<String, String> expectedProperties, Map<String, String> actualProperties) {
        for (Map.Entry<String, String> property : expectedProperties.entrySet()) {
            String expectedValue = property.getValue();
            String key = property.getKey();
            String actualValue = actualProperties.get(key);
            Assert.assertEquals(expectedValue, actualValue);
        }
    }

    private ToscaParserMockHelper[] getExpectedServiceModel() throws IOException {
        ToscaParserMockHelper[] mockHelpers = {
                new ToscaParserMockHelper(Constants.vlUuid, Constants.vlFilePath),
                new ToscaParserMockHelper(Constants.vfUuid, Constants.vfFilePath),
                new ToscaParserMockHelper(Constants.configurationUuid, Constants.configurationFilePath),
        };
        for (ToscaParserMockHelper mockHelper : mockHelpers) {
            InputStream jsonFile = VidControllerTest.class.getClassLoader().getResourceAsStream(mockHelper.getFilePath());
            String expectedJsonAsString = IOUtils.toString(jsonFile);
            NewServiceModel newServiceModel1 = om.readValue(expectedJsonAsString, NewServiceModel.class);
            mockHelper.setNewServiceModel(newServiceModel1);
        }
        return mockHelpers;
    }

    private Path getCsarPath(String uuid) throws AsdcCatalogException {
        return asdcClient.getServiceToscaModel(UUID.fromString(uuid));
    }

    private org.onap.vid.asdc.beans.Service getServiceByUuid(String uuid) throws AsdcCatalogException {
        return asdcClient.getService(UUID.fromString(uuid));
    }

    public class Constants {
        public static final String configurationUuid = "ee6d61be-4841-4f98-8f23-5de9da846ca7";
        public static final String configurationFilePath = "policy-configuration-csar.JSON";
        static final String vfUuid = "48a52540-8772-4368-9cdb-1f124ea5c931";
        static final String vlUuid = "cb49608f-5a24-4789-b0f7-2595473cb997";
        //        public static final String PNFUuid = "68101369-6f08-4e99-9a28-fa6327d344f3";
        static final String vfFilePath = "vf-csar.JSON";
        static final String vlFilePath = "vl-csar.JSON";
//        public static final String PNFFilePath = "/Users/Oren/Git/Att/vid_internal/vid-app-common/src/main/resources/pnf.csar";

    }

}