package org.opencomp.vid.controller;

import net.javacrumbs.jsonunit.JsonAssert;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openecomp.vid.asdc.AsdcCatalogException;
import org.openecomp.vid.asdc.AsdcClient;
import org.openecomp.vid.asdc.parser.ToscaParserImpl2;
import org.openecomp.vid.controller.WebConfig;
import org.openecomp.vid.model.*;
import org.openecomp.vid.properties.AsdcClientConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

import static org.opencomp.vid.testUtils.TestUtils.assertJsonStringEqualsIgnoreNulls;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class, AsdcClientConfiguration.class})
public class VidControllerTest {

    @Autowired
    private AsdcClient asdcClient;

    private String uuid = "f430728a-4530-42be-a577-1206b9484cef";
    //TODO: add as a test case.
    private String vfFilePath = "vf-csar.JSON";
    private String vlFilePath = "vl-csar.JSON";

    private ToscaParserImpl2 p2 = new ToscaParserImpl2();
    private ObjectMapper om = new ObjectMapper();
    final InputStream jsonFile = VidControllerTest.class.getClassLoader().getResourceAsStream(vfFilePath);


    @Test
    public void assertEqualsBetweenServices() throws Exception {
        Service expectedService = getExpectedServiceModel().getService();
        Service actualService = p2.makeServiceModel(getCsarPath(), getServiceByUuid()).getService();
        JsonAssert.assertJsonEquals(expectedService, actualService);
    }

    @Test
    public void assertEqualBetweenObjects() throws Exception {
        ServiceModel actualServiceModel = p2.makeServiceModel(getCsarPath(), getServiceByUuid());
        JsonAssert.assertJsonEquals(getExpectedServiceModel(), actualServiceModel);
    }

    @Test
    public void assertEqualsBetweenNetworkNodes() throws Exception {
        Map<String, Network> expectedNetworksMap = getExpectedServiceModel().getNetworks();
        Map<String, Network> actualNetworksMap = p2.makeServiceModel(getCsarPath(), getServiceByUuid()).getNetworks();
        for (Map.Entry<String, Network> entry : expectedNetworksMap.entrySet()) {
            Network expectedNetwork = entry.getValue();
            Network actualNetwork = actualNetworksMap.get(entry.getKey());
            Assert.assertEquals(expectedNetwork.getModelCustomizationName(), actualNetwork.getModelCustomizationName());
            verifyBaseNodeProperties(expectedNetwork, actualNetwork);
            compareProperties(expectedNetwork.getProperties(), actualNetwork.getProperties());
        }
    }

    //Because we are not supporting the old flow, the JSON are different by definition.
    @Test
    public void assertEqualsBetweenVnfsOfTosca() throws Exception {
        Map<String, VNF> expectedVnfsMap = getExpectedServiceModel().getVnfs();
        Map<String, VNF> actualVnfsMap = p2.makeServiceModel(getCsarPath(), getServiceByUuid()).getVnfs();
        for (Map.Entry<String, VNF> entry : expectedVnfsMap.entrySet()) {
            VNF expectedVnf = entry.getValue();
            VNF actualVnf = actualVnfsMap.get(entry.getKey());
            verifyBaseNodeProperties(expectedVnf, actualVnf);
            Assert.assertEquals(expectedVnf.getModelCustomizationName(), actualVnf.getModelCustomizationName());
            compareProperties(expectedVnf.getProperties(), actualVnf.getProperties());
            assertJsonStringEqualsIgnoreNulls(om.writeValueAsString(expectedVnf), om.writeValueAsString(actualVnf));
        }
    }

    @Test
    public void assertEqualsBetweenVolumeGroups() throws Exception {
        Map<String, VolumeGroup> actualVolumeGroups = p2.makeServiceModel(getCsarPath(), getServiceByUuid()).getVolumeGroups();
        Map<String, VolumeGroup> expectedVolumeGroups = getExpectedServiceModel().getVolumeGroups();
        JsonAssert.assertJsonEquals(actualVolumeGroups, expectedVolumeGroups);
    }

    @Test
    public void assertEqualsBetweenVfModules() throws Exception {
        Map<String, VfModule> actualVfModules = p2.makeServiceModel(getCsarPath(), getServiceByUuid()).getVfModules();
        Map<String, VfModule> expectedVfModules = getExpectedServiceModel().getVfModules();
        JsonAssert.assertJsonEquals(actualVfModules, expectedVfModules);
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

    private NewServiceModel getExpectedServiceModel() throws IOException {
        String expectedJsonAsString  = IOUtils.toString(jsonFile).toString();
        return om.readValue(expectedJsonAsString,NewServiceModel.class);
    }

    private Path getCsarPath() throws AsdcCatalogException {
        return asdcClient.getServiceToscaModel(UUID.fromString(uuid));
    }

    private org.openecomp.vid.asdc.beans.Service getServiceByUuid() throws AsdcCatalogException {
        return asdcClient.getService(UUID.fromString(uuid));
    }
}