/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.asdc.parser;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.vid.asdc.parser.ToscaParserImpl2.Constants.ECOMP_GENERATED_NAMING_PROPERTY;
import static org.onap.vid.testUtils.TestUtils.assertJsonStringEqualsIgnoreNulls;
import static org.onap.vid.testUtils.TestUtils.testWithSystemProperty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.javacrumbs.jsonunit.JsonAssert;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.sdc.tosca.parser.api.ISdcCsarHelper;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.onap.sdc.toscaparser.api.Group;
import org.onap.sdc.toscaparser.api.NodeTemplate;
import org.onap.sdc.toscaparser.api.Property;
import org.onap.sdc.toscaparser.api.elements.Metadata;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.AsdcClient;
import org.onap.vid.asdc.local.LocalAsdcClient;
import org.onap.vid.controller.ToscaParserMockHelper;
import org.onap.vid.model.CR;
import org.onap.vid.model.Network;
import org.onap.vid.model.NetworkCollection;
import org.onap.vid.model.Node;
import org.onap.vid.model.PortMirroringConfig;
import org.onap.vid.model.ResourceGroup;
import org.onap.vid.model.Service;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.ServiceProxy;
import org.onap.vid.model.VNF;
import org.onap.vid.model.VfModule;
import org.onap.vid.model.VidNotions;
import org.onap.vid.model.VolumeGroup;
import org.onap.vid.properties.Features;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

public class ToscaParserImpl2Test {

    private final String myUUID = "myUUID";
    private static final Logger log = LogManager.getLogger(ToscaParserImpl2Test.class);

    @InjectMocks
    private ToscaParserImpl2 toscaParserImpl2;

    private AsdcClient asdcClient;
    private ObjectMapper om = new ObjectMapper();

    @Mock
    private VidNotionsBuilder vidNotionsBuilder;

    @BeforeClass
    void init() throws IOException {

        final InputStream asdcServicesFile = this.getClass().getClassLoader().getResourceAsStream("sdcservices.json");

        final JSONTokener jsonTokener = new JSONTokener(IOUtils.toString(asdcServicesFile));
        final JSONObject sdcServicesCatalog = new JSONObject(jsonTokener);

        asdcClient = new LocalAsdcClient.Builder().catalog(sdcServicesCatalog).build();

    }

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(dataProvider = "expectedServiceModel")
    public void assertEqualsBetweenServices(String uuid, ToscaParserMockHelper mockHelper) throws Exception {
        Service expectedService = mockHelper.getServiceModel().getService();
        Service actualService = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getService();
        assertJsonStringEqualsIgnoreNulls(om.writeValueAsString(expectedService), om.writeValueAsString(actualService));
    }

    @Test(dataProvider = "expectedServiceModel")
    public void assertEqualBetweenObjects(String uuid, ToscaParserMockHelper mockHelper) throws Exception {
        final Path csarPath = getCsarPath(mockHelper.getUuid());
        log.info("Comparing for csar " + csarPath);
        ServiceModel actualServiceModel = toscaParserImpl2.makeServiceModel(csarPath, getServiceByUuid(mockHelper.getUuid()));
        assertJsonStringEqualsIgnoreNulls(om.writeValueAsString(mockHelper.getServiceModel()), om.writeValueAsString(actualServiceModel));
    }

    @Test(dataProvider = "expectedServiceModel")
    public void assertEqualsBetweenNetworkNodes(String uuid, ToscaParserMockHelper mockHelper) throws Exception {
        Map<String, Network> expectedNetworksMap = mockHelper.getServiceModel().getNetworks();
        Map<String, Network> actualNetworksMap = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getNetworks();
        for (Map.Entry<String, Network> entry : expectedNetworksMap.entrySet()) {
            Network expectedNetwork = entry.getValue();
            Network actualNetwork = actualNetworksMap.get(entry.getKey());
            Assert.assertEquals(expectedNetwork.getModelCustomizationName(), actualNetwork.getModelCustomizationName());
            verifyBaseNodeMetadata(expectedNetwork, actualNetwork);
            compareProperties(expectedNetwork.getProperties(), actualNetwork.getProperties());
        }
    }

    //Because we are not supporting the old flow, the JSON are different by definition.
    @Test(dataProvider = "expectedServiceModel")
    public void assertEqualsBetweenVnfsOfTosca(String uuid, ToscaParserMockHelper mockHelper) throws Exception {
        Map<String, VNF> expectedVnfsMap = mockHelper.getServiceModel().getVnfs();
        Map<String, VNF> actualVnfsMap = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getVnfs();
        for (Map.Entry<String, VNF> entry : expectedVnfsMap.entrySet()) {
            VNF expectedVnf = entry.getValue();
            VNF actualVnf = actualVnfsMap.get(entry.getKey());
            verifyBaseNodeMetadata(expectedVnf, actualVnf);
            Assert.assertEquals(expectedVnf.getModelCustomizationName(), actualVnf.getModelCustomizationName());
            compareProperties(expectedVnf.getProperties(), actualVnf.getProperties());
            assertJsonStringEqualsIgnoreNulls(om.writeValueAsString(expectedVnf), om.writeValueAsString(actualVnf));
        }
    }



    @Test(dataProvider = "expectedServiceModel")
    public void assertEqualsBetweenCollectionResourcesOfTosca(String uuid, ToscaParserMockHelper mockHelper) throws Exception {
        Map<String, CR> expectedVnfsMap = mockHelper.getServiceModel().getCollectionResources();
            Map<String, CR> actualCRsMap = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getCollectionResources();
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

    @Test
    public void verifyFabricConfiguration() throws Exception {
        ToscaParserMockHelper toscaParserMockHelper = Arrays.stream(getExpectedServiceModel()).filter(x -> x.getUuid().equals(Constants.fabricConfigurationUuid)).findFirst().get();
        ServiceModel actualServiceModel = toscaParserImpl2.makeServiceModel(getCsarPath(Constants.fabricConfigurationUuid), getServiceByUuid(Constants.fabricConfigurationUuid));
        final Map<String, Node> fabricConfigurations = actualServiceModel.getFabricConfigurations();
        String fabricConfigName = "Fabric Configuration 0";
        Map<String, Node> expectedFC = toscaParserMockHelper.getServiceModel().getFabricConfigurations();
        verifyBaseNodeMetadata(expectedFC.get(fabricConfigName), fabricConfigurations.get(fabricConfigName));
    }


    private void verifyCollectionResource(CR expectedCR, CR actualCR) {
        verifyBaseNodeMetadata(expectedCR, actualCR);
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


    @Test(dataProvider = "expectedServiceModel")
    public void assertEqualsBetweenVolumeGroups(String uuid, ToscaParserMockHelper mockHelper) throws Exception {
            Map<String, VolumeGroup> actualVolumeGroups = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getVolumeGroups();
            Map<String, VolumeGroup> expectedVolumeGroups = mockHelper.getServiceModel().getVolumeGroups();
            assertJsonStringEqualsIgnoreNulls(om.writeValueAsString(expectedVolumeGroups), om.writeValueAsString(actualVolumeGroups));
    }

    @Test(dataProvider = "expectedServiceModel")
    public void assertEqualsBetweenVfModules(String uuid, ToscaParserMockHelper mockHelper) throws Exception {
            Map<String, VfModule> actualVfModules = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getVfModules();
            Map<String, VfModule> expectedVfModules = mockHelper.getServiceModel().getVfModules();
            assertJsonStringEqualsIgnoreNulls(om.writeValueAsString(expectedVfModules), om.writeValueAsString(actualVfModules));
    }

    @Test(dataProvider = "expectedServiceModel")
    public void assertEqualsBetweenPolicyConfigurationNodes(String uuid, ToscaParserMockHelper mockHelper) throws Exception {
            Map<String, PortMirroringConfig> actualConfigurations = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getConfigurations();
            Map<String, PortMirroringConfig> expectedConfigurations = mockHelper.getServiceModel().getConfigurations();
            JsonAssert.assertJsonEquals(actualConfigurations, expectedConfigurations);
    }

    @Test
    public void assertEqualsBetweenPolicyConfigurationByPolicyFalse() throws Exception {
        ToscaParserMockHelper mockHelper = new ToscaParserMockHelper(Constants.configurationByPolicyFalseUuid, Constants.configurationByPolicyFalseFilePath);
        Map<String, PortMirroringConfig> expectedConfigurations = mockHelper.getServiceModel().getConfigurations();
        Map<String, PortMirroringConfig> actualConfigurations = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getConfigurations();

        setPprobeServiceProxy(expectedConfigurations);

        JsonAssert.assertJsonEquals(expectedConfigurations, actualConfigurations);
    }

    @Test
    public void once5GInNewInstantiationFlagIsActive_vidNotionsIsAppended() throws Exception {
        FeatureManager featureManager = mock(FeatureManager.class);
        when(featureManager.isActive(Features.FLAG_5G_IN_NEW_INSTANTIATION_UI)).thenReturn(true);

        ToscaParserImpl2 toscaParserImpl2_local = new ToscaParserImpl2(new VidNotionsBuilder(featureManager));

        final ToscaParserMockHelper mockHelper = new ToscaParserMockHelper(Constants.vlUuid, Constants.vlFilePath);
        final ServiceModel serviceModel = toscaParserImpl2_local.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid()));

        assertThat(serviceModel.getService().getVidNotions().getInstantiationUI(), is(VidNotions.InstantiationUI.LEGACY));
        assertThat(serviceModel.getService().getVidNotions().getModelCategory(), is(VidNotions.ModelCategory.OTHER));
        assertJsonStringEqualsIgnoreNulls("{ service: { vidNotions: { instantiationUI: \"legacy\", modelCategory: \"other\" } } }", om.writeValueAsString(serviceModel));
    }

    @Test
    public void modelWithAnnotatedInputWithTwoProperties_vfModuleGetsTheInput() throws Exception {
        final ToscaParserMockHelper mockHelper = new ToscaParserMockHelper("90fe6842-aa76-4b68-8329-5c86ff564407", "empty.json");
        final ServiceModel serviceModel = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid()));

        assertJsonStringEqualsIgnoreNulls("{ vfModules: { 201712488_pasqualevpe10..201712488PasqualeVpe1..PASQUALE_vRE_BV..module-1: { inputs: { availability_zone_0: { } } } } }", om.writeValueAsString(serviceModel));
    }

    @DataProvider
    public static Object[] oldCsarUuid() {
        return new Object[][]{{ "2a53419b-3f85-4ad5-a9c9-d79905500a27", "MNS VNN1B EXN VF 1" }
            , {"e32a5014-357f-4be4-b3f9-fecb0010811e", "MNS VNN1B DMZ VF 1"}};
    }

    @Test(dataProvider = "oldCsarUuid")
    public void csarWithVnfWithVfModuleInModel(String oldCsarUuid, String vnfName) throws Exception {
        testWithSystemProperty("asdc.model.namespace", "com.att.d2.", ()-> {
            ToscaParser tosca = new ToscaParserImpl();
            final UUID uuid = UUID.fromString(oldCsarUuid);
            final ServiceModel serviceModel = tosca.makeServiceModel(oldCsarUuid, asdcClient.getServiceToscaModel(uuid), asdcClient.getService(uuid));
            assertThat(serviceModel.getVnfs(), aMapWithSize(1));
            assertThat(serviceModel.getVfModules(), aMapWithSize(2));
            assertThat(serviceModel.getVolumeGroups(), aMapWithSize(0));
            assertThat(serviceModel.getVnfs().get(vnfName).getVfModules(), aMapWithSize(2));
        });
    }

    @Test
    public void modelWithNfNamingWithToValues_ecompGeneratedNamingIsExtracted() throws Exception {
        final ToscaParserMockHelper mockHelper = new ToscaParserMockHelper("90fe6842-aa76-4b68-8329-5c86ff564407", "empty.json");
        final ServiceModel serviceModel = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid()));

        assertJsonStringEqualsIgnoreNulls("" +
                "{ vnfs: " +
                "  { \"201712-488_PASQUALE-vPE-1 0\": " +
                "    { properties: { " +
                "      ecomp_generated_naming: \"true\", " +
                "      nf_naming: \"{naming_policy=SDNC_Policy.Config_MS_1806SRIOV_VPE_ADIoDJson, ecomp_generated_naming=true}\" " +
                "} } } }", om.writeValueAsString(serviceModel));
    }

    private void setPprobeServiceProxy(Map<String, PortMirroringConfig> expectedConfigurations){
        //Port Mirroring Configuration By Policy 0 doesn't contains pProbe.
        // But due to sdc design if pProbe not exists parser expects to get it from other source.
        // In a follow implementation provided the expected pProbe.
        PortMirroringConfig pmconfig = expectedConfigurations.get("Port Mirroring Configuration By Policy 0");
        pmconfig.setCollectorNodes(new ArrayList<>(Arrays.asList("pprobeservice_proxy 4")));

    }
    @Test(dataProvider = "expectedServiceModel")
    public void assertEqualsBetweenServiceProxyNodes(String uuid, ToscaParserMockHelper mockHelper) throws Exception {
            Map<String, ServiceProxy> actualServiceProxies = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getServiceProxies();
            Map<String, ServiceProxy> expectedServiceProxies = mockHelper.getServiceModel().getServiceProxies();
            JsonAssert.assertJsonEquals(actualServiceProxies, expectedServiceProxies);
    }

    @Test(dataProvider = "expectedServiceModel")
    public void assertEqualsBetweenVnfGroups(String uuid, ToscaParserMockHelper mockHelper) throws Exception {
        Map<String, ResourceGroup> actualVnfGroups = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid())).getVnfGroups();
        Map<String, ResourceGroup> expectedVnfGroups = mockHelper.getServiceModel().getVnfGroups();
        JsonAssert.assertJsonEquals(actualVnfGroups, expectedVnfGroups);
    }

    @Test
    public void assertEqualsBetweenVrfs() throws Exception {
        ToscaParserMockHelper  mockHelper = new ToscaParserMockHelper(Constants.vrfUuid, Constants.vrfFilePath);
        ServiceModel serviceModel = toscaParserImpl2.makeServiceModel(getCsarPath(mockHelper.getUuid()), getServiceByUuid(mockHelper.getUuid()));
        Map<String, Node> actualVrfs = serviceModel.getVrfs();
        Map<String, Node> expectedVrfs = mockHelper.getServiceModel().getVrfs();
        JsonAssert.assertJsonEquals(expectedVrfs, actualVrfs);
        //assert that vrf isn't returned also as configuration (because it's type is configuration)
        Map<String, PortMirroringConfig> actualConfigurations = serviceModel.getConfigurations();
        Map<String, PortMirroringConfig> expectedConfigurations = mockHelper.getServiceModel().getConfigurations();
        JsonAssert.assertJsonEquals(expectedConfigurations, actualConfigurations );
    }

    private void verifyBaseNodeMetadata(Node expectedNode, Node actualNode) {
        Assert.assertEquals(expectedNode.getName(), actualNode.getName());
        Assert.assertEquals(expectedNode.getCustomizationUuid(), actualNode.getCustomizationUuid());
        Assert.assertEquals(expectedNode.getDescription(), actualNode.getDescription());
        Assert.assertEquals(expectedNode.getInvariantUuid(), actualNode.getInvariantUuid());
        Assert.assertEquals(expectedNode.getUuid(), actualNode.getUuid());
        Assert.assertEquals(expectedNode.getVersion(), actualNode.getVersion());
    }

    private void compareProperties(Map<String, String> expectedProperties, Map<String, String> actualProperties) {
        JsonAssert.assertJsonEquals(expectedProperties, actualProperties);
    }

    @DataProvider
    public Object[][] expectedServiceModel() throws IOException {
        return Stream.of(getExpectedServiceModel())
                        .map(l -> ImmutableList.of(l.getUuid(), l).toArray()).collect(Collectors.toList()).toArray(new Object[][]{});
    }


    private ToscaParserMockHelper[] getExpectedServiceModel() throws IOException {
        ToscaParserMockHelper[] mockHelpers = {
                new ToscaParserMockHelper(Constants.vlUuid, Constants.vlFilePath),
                new ToscaParserMockHelper(Constants.vfUuid, Constants.vfFilePath),
                new ToscaParserMockHelper(Constants.crUuid, Constants.crFilePath),
                new ToscaParserMockHelper(Constants.vfWithAnnotationUuid, Constants.vfWithAnnotationFilePath),
                new ToscaParserMockHelper(Constants.vfWithVfcGroup, Constants.vfWithVfcGroupFilePath),
                new ToscaParserMockHelper(Constants.configurationUuid, Constants.configurationFilePath),
                new ToscaParserMockHelper(Constants.fabricConfigurationUuid, Constants.fabricConfigurationFilePath),
                new ToscaParserMockHelper(Constants.vlanTaggingUuid, Constants.vlanTaggingFilePath),
                new ToscaParserMockHelper(Constants.vnfGroupingUuid, Constants.vnfGroupingFilePath),
            new ToscaParserMockHelper("3f6bd9e9-0942-49d3-84e8-6cdccd6de339", "./vLoadBalancerMS-with-policy.TOSCA.json"),
        };

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
        static final String vfUuid = "48a52540-8772-4368-9cdb-1f124ea5c931";    //service-vf-csar.zip
        static final String vfWithAnnotationUuid = "f4d84bb4-a416-4b4e-997e-0059973630b9";
        static final String vlUuid = "cb49608f-5a24-4789-b0f7-2595473cb997";
        static final String crUuid = "76f27dfe-33e5-472f-8e0b-acf524adc4f0";
        static final String vfWithVfcGroup = "6bce7302-70bd-4057-b48e-8d5b99e686ca"; //service-VdorotheaSrv-csar.zip
        //        public static final String PNFUuid = "68101369-6f08-4e99-9a28-fa6327d344f3";
        static final String vfFilePath = "vf-csar.JSON";
        static final String vlFilePath = "vl-csar.JSON";
        static final String crFilePath = "cr-csar.JSON";
        static final String vfWithAnnotationFilePath = "vf-with-annotation-csar.json";
        static final String vfWithVfcGroupFilePath = "vf-with-vfcInstanceGroups.json";
        public static final String configurationByPolicyFalseUuid = "ee6d61be-4841-4f98-8f23-5de9da845544";
        public static final String configurationByPolicyFalseFilePath = "policy-configuration-by-policy-false.JSON";
        public static final String fabricConfigurationUuid = "12344bb4-a416-4b4e-997e-0059973630b9";
        public static final String fabricConfigurationFilePath = "fabric-configuration.json";
        public static final String vlanTaggingUuid = "1837481c-fa7d-4362-8ce1-d05fafc87bd1";
        public static final String vlanTaggingFilePath = "vlan-tagging.json";
        public static final String vnfGroupingUuid = "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc";
        public static final String vnfGroupingFilePath = "vnf-grouping-csar.json";
        public static final String vrfUuid = "f028b2e2-7080-4b13-91b2-94944d4c42d8";
        public static final String vrfFilePath = "vrf-csar.json";

        public static final String QUANTITY = "quantity";

    }



    @Test
    public void testGetNFModuleFromVf() {
        ISdcCsarHelper csarHelper = getMockedSdcCsarHelper(myUUID);

        Map<String, VfModule> vfModulesFromVF = toscaParserImpl2.getVfModulesFromVF(csarHelper, myUUID);

        assertThat(vfModulesFromVF, allOf(
                aMapWithSize(2),
                hasKey("withoutVol"),
                hasKey("withVol")
        ));
    }

    @Test
    public void testGetVolumeGroupsFromVF() {
        ISdcCsarHelper csarHelper = getMockedSdcCsarHelper(myUUID);

        Map<String, VolumeGroup> volumeGroupsFromVF = toscaParserImpl2.getVolumeGroupsFromVF(csarHelper, myUUID);

        assertThat(volumeGroupsFromVF, allOf(
                aMapWithSize(1),
                hasKey("withVol")
        ));
    }

    @DataProvider
    public Object[][] expectedPoliciesTargets() {
        return new Object[][] {
            {Constants.vnfGroupingUuid, newArrayList("groupingservicefortest..ResourceInstanceGroup..0", "groupingservicefortest..ResourceInstanceGroup..1")},
            {Constants.vfUuid, newArrayList()},
            {Constants.vlanTaggingUuid, newArrayList()}
        };
    }

    @Test(dataProvider = "expectedPoliciesTargets")
    public void testExtractNamingPoliciesTargets(String uuid, ArrayList<String> expectedTargets) throws AsdcCatalogException, SdcToscaParserException {
        ISdcCsarHelper sdcCsarHelper = toscaParserImpl2.getSdcCsarHelper(getCsarPath(uuid));
        List<String> policiesTargets = toscaParserImpl2.extractNamingPoliciesTargets(sdcCsarHelper);

        assertEquals(expectedTargets, policiesTargets);
    }

    @Test
    public void testScalingPolicyOfVnfGroup() throws AsdcCatalogException, SdcToscaParserException {
        String vnfGroupingUuid = "4117a0b6-e234-467d-b5b9-fe2f68c8b0fc";
        Map<String, ResourceGroup> actualVnfGroups = toscaParserImpl2.makeServiceModel(getCsarPath(vnfGroupingUuid), getServiceByUuid(vnfGroupingUuid)).getVnfGroups();
        assertFalse(actualVnfGroups.get("groupingservicefortest..ResourceInstanceGroup..0").getProperties().containsKey(Constants.QUANTITY));
        assertEquals(3, actualVnfGroups.get("groupingservicefortest..ResourceInstanceGroup..1").getProperties().get(Constants.QUANTITY));
    }

    @DataProvider
    public Object[][] expectedEcompGeneratedNaming() {
        return new Object[][] {
                {"nf_naming property false", "nf_naming", "false", "false"},
                {"nf_naming property true", "nf_naming", "true", "true"},
                {"nf_naming property doesn't exist", "nf_naming", null, "false"},
                {"exVL_naming property false", "exVL_naming", "false", "false"},
                {"exVL_naming property true", "exVL_naming", "true", "true"},
                {"exVL_naming property doesn't exist", "exVL_naming", null, "false"},
        };
    }

    @Test(dataProvider = "expectedEcompGeneratedNaming")
    public void testEcompGeneratedNamingForNode(String description, String parentProperty, String ecompNamingProperty, String expectedResult) {
        Property property = mock(Property.class);
        when(property.getName()).thenReturn("any_key");
        when(property.getValue()).thenReturn("any_value");
        ArrayList<Property> properties = newArrayList(property);

        if (ecompNamingProperty != null) {
            Property nfNamingProperty = mock(Property.class);
            when(nfNamingProperty.getName()).thenReturn(parentProperty);
            when(nfNamingProperty.getValue()).thenReturn(ImmutableMap.of(ECOMP_GENERATED_NAMING_PROPERTY, ecompNamingProperty));
            properties.add(nfNamingProperty);
        }

        NodeTemplate node = mock(NodeTemplate.class);
        when(node.getName()).thenReturn("node_name");
        when(node.getPropertiesObjects()).thenReturn(properties);

        String result = ToscaNamingPolicy.getEcompNamingValueForNode(node, parentProperty);
        assertEquals(expectedResult, result);
    }

    public static ISdcCsarHelper getMockedSdcCsarHelper(String myUUID) {
        ISdcCsarHelper csarHelper = mock(ISdcCsarHelper.class);

        Group withVol = createMinimalGroup("withVol", true);
        Group withoutVol = createMinimalGroup("withoutVol", false);

        when(csarHelper.getServiceMetadata()).thenReturn(new Metadata(ImmutableMap.of(
                "instantiationType", "A-La-Carte"
        )));

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
                    (new ObjectMapper())
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
