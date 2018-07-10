package org.onap.vid.asdc.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableList;
import net.javacrumbs.jsonunit.JsonAssert;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.onap.sdc.tosca.parser.api.ISdcCsarHelper;
import org.onap.sdc.toscaparser.api.Group;
import org.onap.sdc.toscaparser.api.NodeTemplate;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.local.LocalAsdcClient;
import org.onap.vid.model.*;
import org.onap.vid.controllers.ToscaParserMockHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.vid.testUtils.TestUtils.assertJsonStringEqualsIgnoreNulls;

@Test
public class ToscaParserImpl2Test {

    private final String myUUID = "myUUID";
    private static final Logger log = Logger.getLogger(ToscaParserImpl2Test.class);

    private ToscaParserImpl2 toscaParserImpl2 = new ToscaParserImpl2();

    private AsdcClient asdcClient;
    private ObjectMapper om = new ObjectMapper();

    @BeforeClass
    void init() throws IOException {

        final InputStream asdcServicesFile = this.getClass().getClassLoader().getResourceAsStream("sdcservices.json");

        final JSONTokener jsonTokener = new JSONTokener(IOUtils.toString(asdcServicesFile));
        final JSONObject sdcServicesCatalog = new JSONObject(jsonTokener);

        asdcClient = new LocalAsdcClient.Builder().catalog(sdcServicesCatalog).build();

    }

    //@Test
    public void assertEqualsBetweenServices() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            Service expectedService = mockHelper.getNewServiceModel().getService();
            Service actualService = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getService();
            assertJsonStringEqualsIgnoreNulls(om.writeValueAsString(expectedService), om.writeValueAsString(actualService));
        }
    }

    //@Test
    public void assertEqualBetweenObjects() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            final Path csarPath = getCsarPath(mockHelper.getUuid());
            System.out.println("Comparing for csar " + csarPath);
            ServiceModel actualServiceModel = toscaParserImpl2.makeServiceModel(csarPath, getServiceByUuid(mockHelper.getUuid()));
            assertJsonStringEqualsIgnoreNulls(om.writeValueAsString(mockHelper.getNewServiceModel()), om.writeValueAsString(actualServiceModel));
        }
    }

    //@Test
    public void assertEqualsBetweenNetworkNodes() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            Map<String, Network> expectedNetworksMap = mockHelper.getNewServiceModel().getNetworks();
            Map<String, Network> actualNetworksMap = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getNetworks();
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
    //@Test
    public void assertEqualsBetweenVnfsOfTosca() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            Map<String, VNF> expectedVnfsMap = mockHelper.getNewServiceModel().getVnfs();
            Map<String, VNF> actualVnfsMap = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getVnfs();
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

    //@Test
    public void assertEqualsBetweenCollectionResourcesOfTosca() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            Map<String, CR> expectedVnfsMap = mockHelper.getNewServiceModel().getCollectionResource();
            Map<String, CR> actualCRsMap = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getCollectionResource();
            if(!actualCRsMap.isEmpty()) {
                for (Map.Entry<String, CR> entry : expectedVnfsMap.entrySet()) {
                    CR expectedCR = entry.getValue();
                    CR actualCR = actualCRsMap.get(entry.getKey());
                    verifyCollectionResource(expectedCR, actualCR);
                    Assert.assertEquals(expectedCR.getName(), actualCR.getName());
                    compareProperties(expectedCR.getProperties(), actualCR.getProperties());
                    assertJsonStringEqualsIgnoreNulls(om.writeValueAsString(expectedCR), om.writeValueAsString(actualCR));
                }
            }
        }
    }

    private void verifyCollectionResource(CR expectedCR, CR actualCR) {
        verifyBaseNodeProperties(expectedCR, actualCR);
        Assert.assertEquals(expectedCR.getCategory(), actualCR.getCategory());
        Assert.assertEquals(expectedCR.getSubcategory(), actualCR.getSubcategory());
        Assert.assertEquals(expectedCR.getResourceVendor(), actualCR.getResourceVendor());
        Assert.assertEquals(expectedCR.getResourceVendorRelease(), actualCR.getResourceVendorRelease());
        Assert.assertEquals(expectedCR.getResourceVendorModelNumber(), actualCR.getResourceVendorModelNumber());
        Assert.assertEquals(expectedCR.getCustomizationUUID(), actualCR.getCustomizationUUID());
        verifyNetworkCollections(expectedCR.getNetworksCollection(), actualCR.getNetworksCollection());
    }

    private void verifyNetworkCollections(Map<String, NetworkCollection> expectedNetworksCollection, Map<String, NetworkCollection> actualNetworksCollection) {
        for (Map.Entry<String, NetworkCollection> property : expectedNetworksCollection.entrySet()) {
            NetworkCollection expectedValue = property.getValue();
            String key = property.getKey();
            NetworkCollection actualValue = actualNetworksCollection.get(key);
            verifyNetworkCollection(expectedValue, actualValue);
        }
    }

    private void verifyNetworkCollection(NetworkCollection expectedValue, NetworkCollection actualValue) {
        Assert.assertEquals(expectedValue.getInvariantUuid(), actualValue.getInvariantUuid());
        Assert.assertEquals(expectedValue.getName(), actualValue.getName());
        Assert.assertEquals(expectedValue.getUuid(), actualValue.getUuid());
        Assert.assertEquals(expectedValue.getVersion(), actualValue.getVersion());
        Assert.assertEquals(expectedValue.getNetworkCollectionProperties().getNetworkCollectionDescription(), actualValue.getNetworkCollectionProperties().getNetworkCollectionDescription());
        Assert.assertEquals(expectedValue.getNetworkCollectionProperties().getNetworkCollectionFunction(), actualValue.getNetworkCollectionProperties().getNetworkCollectionFunction());
    }


    //@Test
    public void assertEqualsBetweenVolumeGroups() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            Map<String, VolumeGroup> actualVolumeGroups = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getVolumeGroups();
            Map<String, VolumeGroup> expectedVolumeGroups = mockHelper.getNewServiceModel().getVolumeGroups();
            assertJsonStringEqualsIgnoreNulls(om.writeValueAsString(expectedVolumeGroups), om.writeValueAsString(actualVolumeGroups));
        }
    }

    //@Test
    public void assertEqualsBetweenVfModules() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            Map<String, VfModule> actualVfModules = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getVfModules();
            Map<String, VfModule> expectedVfModules = mockHelper.getNewServiceModel().getVfModules();
            assertJsonStringEqualsIgnoreNulls(om.writeValueAsString(expectedVfModules), om.writeValueAsString(actualVfModules));
        }
    }

    //@Test
    public void assertEqualsBetweenPolicyConfigurationNodes() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            Map<String, PortMirroringConfig> actualConfigurations = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getConfigurations();
            Map<String, PortMirroringConfig> expectedConfigurations = mockHelper.getNewServiceModel().getConfigurations();
            JsonAssert.assertJsonEquals(actualConfigurations, expectedConfigurations);
        }
    }
    //@Test
    public void assertEqualsBetweenPolicyConfigurationByPolicyFalse() throws Exception {
        ToscaParserMockHelper mockHelper = new ToscaParserMockHelper(Constants.configurationByPolicyFalseUuid, Constants.configurationByPolicyFalseFilePath);
        InputStream jsonFile = this.getClass().getClassLoader().getResourceAsStream(mockHelper.getFilePath());
        String expectedJsonAsString = IOUtils.toString(jsonFile, StandardCharsets.UTF_8.name());
        NewServiceModel newServiceModel1 = om.readValue(expectedJsonAsString, NewServiceModel.class);
        mockHelper.setNewServiceModel(newServiceModel1);
        Map<String, PortMirroringConfig> expectedConfigurations = mockHelper.getNewServiceModel().getConfigurations();
        Map<String, PortMirroringConfig> actualConfigurations = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getConfigurations();

        setPprobeServiceProxy(expectedConfigurations);

        JsonAssert.assertJsonEquals(expectedConfigurations, actualConfigurations);
    }

    private void setPprobeServiceProxy(Map<String, PortMirroringConfig> expectedConfigurations){
        //Port Mirroring Configuration By Policy 0 doesn't contains pProbe.
        // But due to sdc design if pProbe not exists parser expects to get it from other source.
        // In a follow implementation provided the expected pProbe.
        PortMirroringConfig pmconfig = expectedConfigurations.get("Port Mirroring Configuration By Policy 0");
        pmconfig.setCollectorNodes(new ArrayList<>(Arrays.asList("pprobeservice_proxy 4")));

    }
    //@Test
    public void assertEqualsBetweenServiceProxyNodes() throws Exception {
        for (ToscaParserMockHelper mockHelper : getExpectedServiceModel()) {
            Map<String, ServiceProxy> actualServiceProxies = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getServiceProxies();
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
                new ToscaParserMockHelper(Constants.crUuid, Constants.crFilePath),
                new ToscaParserMockHelper(Constants.vfWithAnnotationUuid, Constants.vfWithAnnotationFilePath),
                new ToscaParserMockHelper(Constants.vfWithVfcGroup, Constants.vfWithVfcGroupFilePath),
                new ToscaParserMockHelper(Constants.configurationUuid, Constants.configurationFilePath)
        };
        for (ToscaParserMockHelper mockHelper : mockHelpers) {
            InputStream jsonFile = this.getClass().getClassLoader().getResourceAsStream(mockHelper.getFilePath());
            System.out.println(jsonFile);
            String expectedJsonAsString = IOUtils.toString(jsonFile, StandardCharsets.UTF_8.name());
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
        static final String vfWithAnnotationUuid = "f4d84bb4-a416-4b4e-997e-0059973630b9";
        static final String vlUuid = "cb49608f-5a24-4789-b0f7-2595473cb997";
        static final String crUuid = "76f27dfe-33e5-472f-8e0b-acf524adc4f0";
        static final String vfWithVfcGroup = "6bce7302-70bd-4057-b48e-8d5b99e686ca";
        //        public static final String PNFUuid = "68101369-6f08-4e99-9a28-fa6327d344f3";
        static final String vfFilePath = "vf-csar.JSON";
        static final String vlFilePath = "vl-csar.JSON";
        static final String crFilePath = "cr-csar.JSON";
        static final String vfWithAnnotationFilePath = "vf-with-annotation-csar.json";
        static final String vfWithVfcGroupFilePath = "vf-with-vfcInstanceGroups.json";
        public static final String configurationByPolicyFalseUuid = "ee6d61be-4841-4f98-8f23-5de9da845544";
        public static final String configurationByPolicyFalseFilePath = "policy-configuration-by-policy-false.JSON";


    }



    @Test
    public void testGetNFModuleFromVf() {
        ISdcCsarHelper csarHelper = getMockedSdcCsarHelper();

        Map<String, VfModule> vfModulesFromVF = toscaParserImpl2.getVfModulesFromVF(csarHelper, myUUID);

        assertThat(vfModulesFromVF, allOf(
                aMapWithSize(2),
                hasKey("withoutVol"),
                hasKey("withVol")
        ));
    }

    @Test
    public void testGetVolumeGroupsFromVF() {
        ISdcCsarHelper csarHelper = getMockedSdcCsarHelper();

        Map<String, VolumeGroup> volumeGroupsFromVF = toscaParserImpl2.getVolumeGroupsFromVF(csarHelper, myUUID);

        assertThat(volumeGroupsFromVF, allOf(
                aMapWithSize(1),
                hasKey("withVol")
        ));
    }

    private ISdcCsarHelper getMockedSdcCsarHelper() {
        ISdcCsarHelper csarHelper = mock(ISdcCsarHelper.class);

        Group withVol = createMinimalGroup("withVol", true);
        Group withoutVol = createMinimalGroup("withoutVol", false);

        when(csarHelper.getVfModulesByVf(myUUID))
                .thenReturn(ImmutableList.of(withVol, withoutVol));

        return csarHelper;
    }

    private static Group createMinimalGroup(String name, boolean isVolumeGroup) {
        LinkedHashMap<String, Object>
                templates,
                properties,
                metadata,
                customDef,
                vfModule,
                vfModuleProperties,
                volumeGroup;

        templates = new LinkedHashMap<>();
        templates.put("type", "org.onap.groups.VfModule");

        properties = addNewNamedMap(templates, "properties");
        properties.put("volume_group", isVolumeGroup);

        metadata = addNewNamedMap(templates, "metadata");

        ArrayList<NodeTemplate> memberNodes = new ArrayList<>();

        customDef = new LinkedHashMap<>();
        vfModule = addNewNamedMap(customDef, "org.onap.groups.VfModule");
        vfModuleProperties = addNewNamedMap(vfModule, "properties");

        volumeGroup = addNewNamedMap(vfModuleProperties, "volume_group");
        volumeGroup.put("type", "boolean");
        volumeGroup.put("default", false);
        volumeGroup.put("required", true);


        Group group = new Group(
                name,
                templates,
                memberNodes,
                customDef
        );

        try {
            log.info(String.format("Built a group: %s",
                    (new com.fasterxml.jackson.databind.ObjectMapper())
                            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                            .writeValueAsString(group)
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return group;
    }

    private static LinkedHashMap<String, Object> addNewNamedMap(LinkedHashMap<String, Object> root, String key) {
        LinkedHashMap<String, Object> properties = new LinkedHashMap<>();
        root.put(key, properties);
        return properties;
    }

}
