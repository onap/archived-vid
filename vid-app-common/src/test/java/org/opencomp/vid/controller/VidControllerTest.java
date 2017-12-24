package org.opencomp.vid.controller;

import net.javacrumbs.jsonunit.JsonAssert;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.runner.RunWith;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.sdc.tosca.parser.api.ISdcCsarHelper;
import org.openecomp.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.openecomp.sdc.tosca.parser.impl.SdcToscaParserFactory;
import org.openecomp.sdc.tosca.parser.impl.SdcTypes;
import org.openecomp.vid.aai.AaiClient;
import org.openecomp.vid.aai.AaiResponse;
import org.openecomp.vid.asdc.AsdcCatalogException;
import org.openecomp.vid.asdc.AsdcClient;
import org.openecomp.vid.asdc.parser.ToscaParserImpl2;
import org.openecomp.vid.controller.LocalWebConfig;
import org.openecomp.vid.controller.WebConfig;
import org.openecomp.vid.model.*;
import org.openecomp.vid.properties.AsdcClientConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.opencomp.vid.testUtils.TestUtils.assertJsonStringEqualsIgnoreNulls;

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
            JsonAssert.assertJsonEquals(expectedService, actualService);
        }
    }

    @Test
    public void assertEqualBetweenObjects() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            final Path csarPath = getCsarPath(mockHelper.getUuid());
            System.out.println("Comparing for csar "+csarPath);
            ServiceModel actualServiceModel = p2.makeServiceModel(csarPath, getServiceByUuid(mockHelper.getUuid()));
            JsonAssert.assertJsonEquals(mockHelper.getNewServiceModel(), actualServiceModel);
        }
    }

    @Test
    public void assertEqualsBetweenNetworkNodes() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            Map<String, Network> expectedNetworksMap = mockHelper.getNewServiceModel().getNetworks();
            Map<String, Network> actualNetworksMap = p2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getNetworks();
            for (Map.Entry<String, Network> entry : expectedNetworksMap.entrySet()) {
                Network expectedNetwork = entry.getValue();
                Network actualNetwork = actualNetworksMap.get(entry.getKey());
                Assert.assertEquals(expectedNetwork.getModelCustomizationName(), actualNetwork.getModelCustomizationName());
                verifyBaseNodeProperties(expectedNetwork, actualNetwork);
                compareProperties(expectedNetwork.getProperties(), actualNetwork.getProperties());
            }
        }
    }

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
        ToscaParserMockHelper[] mockHelpers = {new ToscaParserMockHelper(Constants.vlUuid, Constants.vlFilePath), new ToscaParserMockHelper(Constants.vfUuid, Constants.vfFilePath)};
        for (ToscaParserMockHelper mockHelper : mockHelpers) {
            InputStream jsonFile = VidControllerTest.class.getClassLoader().getResourceAsStream(mockHelper.getFilePath());
            String expectedJsonAsString = IOUtils.toString(jsonFile).toString();
            NewServiceModel newServiceModel1 = om.readValue(expectedJsonAsString, NewServiceModel.class);
            mockHelper.setNewServiceModel(newServiceModel1);
        }
        return mockHelpers;
    }

    private Path getCsarPath(String uuid) throws AsdcCatalogException {
        return asdcClient.getServiceToscaModel(UUID.fromString(uuid));
    }

    private org.openecomp.vid.asdc.beans.Service getServiceByUuid(String uuid) throws AsdcCatalogException {
        return asdcClient.getService(UUID.fromString(uuid));
    }

    public class Constants {
        public static final String vfUuid = "48a52540-8772-4368-9cdb-1f124ea5c931";
        public static final String vlUuid = "cb49608f-5a24-4789-b0f7-2595473cb997";
//        public static final String PNFUuid = "68101369-6f08-4e99-9a28-fa6327d344f3";
        public static final String vfFilePath = "vf-csar.JSON";
        public static final String vlFilePath = "vl-csar.JSON";
//        public static final String PNFFilePath = "/Users/Oren/Git/Att/vid_internal/vid-app-common/src/main/resources/pnf.csar";
    }

}