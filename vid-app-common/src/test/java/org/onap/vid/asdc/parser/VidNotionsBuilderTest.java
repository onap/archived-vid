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

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.vid.model.VidNotions.InstantiationType;
import static org.onap.vid.model.VidNotions.InstantiationUI;
import static org.onap.vid.model.VidNotions.ModelCategory;
import static org.testng.AssertJUnit.assertEquals;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.sdc.tosca.parser.api.ISdcCsarHelper;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.onap.sdc.toscaparser.api.NodeTemplate;
import org.onap.sdc.toscaparser.api.Property;
import org.onap.sdc.toscaparser.api.elements.Metadata;
import org.onap.vid.asdc.parser.ToscaParserImpl2.Constants;
import org.onap.vid.model.CR;
import org.onap.vid.model.Network;
import org.onap.vid.model.Node;
import org.onap.vid.model.Service;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.VidNotions;
import org.onap.vid.properties.Features;
import org.onap.vid.testUtils.TestUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

public class VidNotionsBuilderTest {

    @InjectMocks
    VidNotionsBuilder vidNotionsBuilder;

    @Mock
    private FeatureManager featureManagerMock;

    private ServiceModel serviceModel;

    private ISdcCsarHelper csarHelper;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void reset() {
        Mockito.reset(featureManagerMock);
    }

    @Test
    public void VLNetworkWithPropertyNetworkTechnologyOVS_UIHintIsPositive() {
        ISdcCsarHelper csarHelper = mockForNonLegacyInstantiationUI();

        assertThat(vidNotionsBuilder.suggestInstantiationUI(csarHelper, serviceModel, ModelCategory.OTHER), is(InstantiationUI.NETWORK_WITH_PROPERTY_NETWORK_TECHNOLOGY_EQUALS_STANDARD_SRIOV_OR_OVS));
        assertThat(vidNotionsBuilder.suggestModelCategory(csarHelper, serviceModel) , is(ModelCategory.IS_5G_PROVIDER_NETWORK_MODEL));
    }

    @NotNull
    private ISdcCsarHelper mockForNonLegacyInstantiationUI() {
        ISdcCsarHelper csarHelper = ToscaParserImpl2Test.getMockedSdcCsarHelper(UUID.randomUUID().toString());

        NodeTemplate nodeTemplate = mock(NodeTemplate.class);

        when(nodeTemplate.getProperties()).thenReturn(new LinkedHashMap<>(ImmutableMap.of(
                "dummy_val", mock(Property.class),
                "network_technology", new Property(Pair.of("network_technology","ovs"))
        )));

        when(csarHelper.getServiceVlList()).thenReturn(ImmutableList.of(nodeTemplate));
        when(featureManagerMock.isActive(Features.FLAG_5G_IN_NEW_INSTANTIATION_UI)).thenReturn(true);
        return csarHelper;
    }

    @DataProvider
    public static Object[][] anyAlacarteDataProvider() {
        return new Object[][] {
                {"A-La-Carte", InstantiationUI.ANY_ALACARTE_NEW_UI},
                {"Macro", InstantiationUI.LEGACY},
        };
    }

    @Test(dataProvider = "anyAlacarteDataProvider")
    public void FLAG_EXP_ANY_ALACARTE_NEW_INSTANTIATION_UI_is_active_UIHintIsPositive(String instantiationType, InstantiationUI expectedInstantiationUI) {
        initServiceModelAndscarHelperWithMocks();

        when(featureManagerMock.isActive(Features.FLAG_EXP_ANY_ALACARTE_NEW_INSTANTIATION_UI)).thenReturn(true);
        when(csarHelper.getServiceMetadata()).thenReturn(new Metadata(ImmutableMap.of(
                "instantiationType", instantiationType
        )));
        NodeTemplate nodeTemplate = mock(NodeTemplate.class);

        when(nodeTemplate.getProperties()).thenReturn(new LinkedHashMap<>(ImmutableMap.of(
                "dummy_val", mock(Property.class),
                "network_technology", new Property(Pair.of("network_technology","ovs"))
        )));

        when(csarHelper.getServiceVlList()).thenReturn(ImmutableList.of(nodeTemplate));

        assertThat(vidNotionsBuilder.suggestInstantiationUI(csarHelper, serviceModel, ModelCategory.OTHER), is(expectedInstantiationUI));
    }

    @Test
    public void VLNetworkWithPropertyNetworkTechnologyNot5G_UIHintIsNegative() {
        initServiceModelAndscarHelperWithMocks();

        NodeTemplate nodeTemplate = mock(NodeTemplate.class);

        when(nodeTemplate.getProperties()).thenReturn(new LinkedHashMap<>(ImmutableMap.of(
                "dummy_val", mock(Property.class),
                "network_technology", new Property(Pair.of("network_technology","old_value"))
        )));

        when(csarHelper.getServiceVlList()).thenReturn(ImmutableList.of(nodeTemplate));

        assertThat(vidNotionsBuilder.suggestInstantiationUI(csarHelper, serviceModel, ModelCategory.OTHER), is(InstantiationUI.LEGACY));
        assertThat(vidNotionsBuilder.suggestModelCategory(csarHelper, serviceModel) , is(ModelCategory.OTHER));
    }

    @Test
    public void withoutMocks_givenZippedToscaFile_hasAnyNetworkWithPropertyEqualsToAnyOfYieldsTrue() throws SdcToscaParserException, IOException {
        initServiceModelAndscarHelperWithRealCsar("/csars/service-vl-csar.zip");
        assertThat(vidNotionsBuilder.isALaCarte(csarHelper), is(false));
        assertThat(vidNotionsBuilder.hasAnyNetworkWithPropertyEqualsToAnyOf(csarHelper, "unexpected_property_name"), is(false));
        assertThat(vidNotionsBuilder.hasAnyNetworkWithPropertyEqualsToAnyOf(csarHelper, "network_technology","Standard-SR-IOV"), is(true));
        assertThat(vidNotionsBuilder.suggestInstantiationUI(csarHelper, serviceModel, ModelCategory.OTHER), is(InstantiationUI.LEGACY));
    }

    @Test
    public void withoutMocks_givenZippedToscaFile_hasFabricConfigurationYieldsTrue() throws SdcToscaParserException, IOException {
        initServiceModelAndscarHelperWithRealCsar("/csars/service-fabric-configuration.zip");
        assertThat(vidNotionsBuilder.isALaCarte(csarHelper), is(false));
        assertThat(vidNotionsBuilder.hasFabricConfiguration(csarHelper), is(true));
        assertThat(vidNotionsBuilder.suggestInstantiationUI(csarHelper, serviceModel, ModelCategory.OTHER), is(InstantiationUI.LEGACY));
    }

    @Test(dataProvider = "trueAndFalse", dataProviderClass = TestUtils.class)
    public void withoutMocks_givenZippedToscaFileOfTransportService_InstantiationUIAndCategoryAreRight(boolean flagValue) throws SdcToscaParserException, IOException {
        initServiceModelAndscarHelperWithRealCsar("/csars/csarTransportWithPnfs.zip");

        when(featureManagerMock.isActive(Features.FLAG_1908_TRANSPORT_SERVICE_NEW_INSTANTIATION_UI)).thenReturn(flagValue);

        assertThat(vidNotionsBuilder.isALaCarte(csarHelper), is(false));
        assertThat(vidNotionsBuilder.suggestInstantiationUI(csarHelper, serviceModel, ModelCategory.OTHER), is(flagValue ? InstantiationUI.TRANSPORT_SERVICE : InstantiationUI.LEGACY));
        assertThat(vidNotionsBuilder.suggestModelCategory(csarHelper, serviceModel), is(ModelCategory.Transport));
    }

    @Test(dataProvider = "trueAndFalse", dataProviderClass = TestUtils.class)
    public void withoutMocks_givenZippedToscaFileOfInfraStructureVpn_InstantiationUIIsRight(boolean flagValue) throws SdcToscaParserException, IOException {
        initServiceModelAndscarHelperWithRealCsar("/csars/service-Infravpn-csar.zip");
        when(featureManagerMock.isActive(Features.FLAG_1908_INFRASTRUCTURE_VPN)).thenReturn(flagValue);
        assertThat(vidNotionsBuilder.suggestInstantiationUI(csarHelper, serviceModel, ModelCategory.OTHER), is(flagValue ? InstantiationUI.INFRASTRUCTURE_VPN : InstantiationUI.LEGACY));
        assertThat(vidNotionsBuilder.suggestModelCategory(csarHelper, serviceModel), is(ModelCategory.INFRASTRUCTURE_VPN));
    }

    @Test()
    public void withoutMocks_givenToscaOfPortMirroring_InstantiationUIIsLegacyAndCategoryIsPortMirroring() throws SdcToscaParserException, IOException {
        initServiceModelAndscarHelperWithRealCsar("/csars/portMirroringService.zip");
        when(featureManagerMock.isActive(Features.FLAG_2002_ANY_ALACARTE_BESIDES_EXCLUDED_NEW_INSTANTIATION_UI)).thenReturn(true);
        assertThat(vidNotionsBuilder.buildVidNotions(csarHelper, serviceModel),
            equalTo(new VidNotions(InstantiationUI.LEGACY, ModelCategory.PORT_MIRRORING, InstantiationUI.LEGACY, InstantiationType.ClientConfig)));

    }

    @Test()
    public void withoutMocks_givenToscaOfVLanTagging_InstantiationUIIsLegacyAndCategoryIsVlanTagging() throws SdcToscaParserException, IOException {
        initServiceModelAndscarHelperWithRealCsar("/csars/service-VdorotheaSrv-csar.zip");
        when(featureManagerMock.isActive(Features.FLAG_2002_ANY_ALACARTE_BESIDES_EXCLUDED_NEW_INSTANTIATION_UI)).thenReturn(true);
        assertThat(vidNotionsBuilder.buildVidNotions(csarHelper, serviceModel),
            equalTo(new VidNotions(InstantiationUI.LEGACY, ModelCategory.VLAN_TAGGING, InstantiationUI.LEGACY, InstantiationType.ALaCarte)));
    }

    @Test
    public void withoutMocks_givenToscaWithoutTypeAndFlagOn_InstantiationUIisAlacarte()
        throws SdcToscaParserException, IOException {
        initServiceModelAndscarHelperWithRealCsar("/csars/service-Vocg1804Svc.zip");
        when(featureManagerMock.isActive(Features.FLAG_2002_ANY_ALACARTE_BESIDES_EXCLUDED_NEW_INSTANTIATION_UI)).thenReturn(true);
        assertThat(vidNotionsBuilder.buildVidNotions(csarHelper, serviceModel),
            equalTo(new VidNotions(
                InstantiationUI.ANY_ALACARTE_WHICH_NOT_EXCLUDED,
                ModelCategory.OTHER,
                InstantiationUI.LEGACY,
                InstantiationType.ClientConfig)));
    }

    @DataProvider
    public static Object[][] anyAlaCarteDataProvider() {
        return new Object[][] {
            {true, Constants.A_LA_CARTE, InstantiationUI.ANY_ALACARTE_WHICH_NOT_EXCLUDED},
            {false, Constants.A_LA_CARTE, InstantiationUI.LEGACY},
            {true, Constants.MACRO, InstantiationUI.LEGACY},
            {true, Constants.CLIENT_CONFIG, InstantiationUI.ANY_ALACARTE_WHICH_NOT_EXCLUDED},
            {true, null, InstantiationUI.ANY_ALACARTE_WHICH_NOT_EXCLUDED},
            {true, "", InstantiationUI.ANY_ALACARTE_WHICH_NOT_EXCLUDED}
        };
    }

    @Test(dataProvider = "anyAlaCarteDataProvider")
    public void testAnyAlaCarteNewUI_byInstantiationTypeAndFeatureFlag(boolean flag, String instantiationType, InstantiationUI expected) {
        initServiceModelAndscarHelperWithMocks();
        mockInstantiationType(serviceModel, instantiationType);
        when(featureManagerMock.isActive(Features.FLAG_2002_ANY_ALACARTE_BESIDES_EXCLUDED_NEW_INSTANTIATION_UI)).thenReturn(flag);
        assertThat(vidNotionsBuilder.suggestInstantiationUI(csarHelper, serviceModel, ModelCategory.OTHER), is(expected));
    }

    @Test
    public void uuidIsExactly1ffce89fEtc_UIHintIsPositive() {
        initServiceModelAndscarHelperWithMocks();

        when(csarHelper.getServiceMetadata()).thenReturn(new Metadata(ImmutableMap.of(
                "UUID", "95eb2c44-bff2-4e8b-ad5d-8266870b7717"
        )));
        when(featureManagerMock.isActive(Features.FLAG_5G_IN_NEW_INSTANTIATION_UI)).thenReturn(true);
        assertThat(vidNotionsBuilder.suggestInstantiationUI(csarHelper, serviceModel, ModelCategory.OTHER), is(InstantiationUI.SERVICE_UUID_IS_1ffce89f_ef3f_4cbb_8b37_82134590c5de));
    }

    @Test(dataProvider = "trueAndFalse", dataProviderClass = TestUtils.class)
    public void buildVidNotions_nullByFlag(boolean flagValue) {
        initServiceModelAndscarHelperWithMocks();

        when(featureManagerMock.isActive(Features.FLAG_5G_IN_NEW_INSTANTIATION_UI)).thenReturn(flagValue);
        assertThat(vidNotionsBuilder.buildVidNotions(csarHelper, serviceModel), hasProperty("instantiationUI", is(InstantiationUI.LEGACY)));
    }

    private void mockInstantiationType(ServiceModel serviceModel, String instantiationType) {
        Service mockService = mock(Service.class);
        when(serviceModel.getService()).thenReturn(mockService);
        when(mockService.getInstantiationType()).thenReturn(instantiationType);
    }

    @DataProvider
    public static Object[][] ServiceRoleTypesDataProvider() {
        return new Object[][] {
                {"gROUPING", InstantiationUI.SERVICE_WITH_VNF_GROUPING},
                {"", InstantiationUI.LEGACY},
        };
    }

    @Test(dataProvider = "ServiceRoleTypesDataProvider")
    public void testGetViewEditUITypeForResourceGroup(String serviceRole, InstantiationUI expectedViewEditUI) {
        initServiceModelAndscarHelperWithMocks();
        when(featureManagerMock.isActive(Features.FLAG_1902_VNF_GROUPING)).thenReturn(true);
        when(csarHelper.getServiceMetadata()).thenReturn(new Metadata(ImmutableMap.of(
                "serviceRole", serviceRole
        )));

        assertThat(vidNotionsBuilder.suggestViewEditUI(csarHelper, serviceModel, ModelCategory.OTHER), is(expectedViewEditUI));
    }

    @DataProvider
    public static Object[][] instantiationUIToViewEditDataProvider() {
        return new Object[][] {
                {"network cloud(5G) service + needed flags are open", true, true, InstantiationUI.NETWORK_WITH_PROPERTY_NETWORK_TECHNOLOGY_EQUALS_STANDARD_SRIOV_OR_OVS},
                {"mocked service + needed flags are open", false, true, InstantiationUI.LEGACY},
                {"network cloud(5G) service + FLAG_1902_NEW_VIEW_EDIT is off", true, false, InstantiationUI.LEGACY},
        };
    }


    @Test(dataProvider="instantiationUIToViewEditDataProvider")
    public void whenInstantiationUIIsNotLegacy_viewEditIsRight(
            String testDescription,
            boolean isInstantiationUINotLegacy,
            boolean isFlag1902NewViewEdit,
            InstantiationUI expectedViewEditUi) {

        ISdcCsarHelper csarHelper = isInstantiationUINotLegacy ?  mockForNonLegacyInstantiationUI() : mock(ISdcCsarHelper.class);
        when(featureManagerMock.isActive(Features.FLAG_1902_NEW_VIEW_EDIT)).thenReturn(isFlag1902NewViewEdit);

        ServiceModel serviceModel = mock(ServiceModel.class);
        mockInstantiationType(serviceModel, Constants.A_LA_CARTE);

        InstantiationUI result = vidNotionsBuilder.suggestViewEditUI(csarHelper, serviceModel, ModelCategory.OTHER);
        assertEquals(expectedViewEditUi, result);
    }

    @DataProvider
    public static Object[][] mockerForMacroExcluded() {
        return new Object[][] {
                {"service with pnfs", (BiConsumer<ServiceModel, FeatureManager>) (serviceModel, fm)->when(serviceModel.getPnfs()).thenReturn(ImmutableMap.of("a", mock(Node.class))), true},
                {"service with collection resource", (BiConsumer<ServiceModel, FeatureManager>) (serviceModel, fm) -> when(serviceModel.getCollectionResources()).thenReturn(ImmutableMap.of("a", mock(CR.class))), true},
                {"service with network + FLAG_NETWORK_TO_ASYNC_INSTANTIATION false ", (BiConsumer<ServiceModel, FeatureManager>) (serviceModel, fm)->{
                    when(serviceModel.getNetworks()).thenReturn(ImmutableMap.of("a", mock(Network.class)));
                    when(fm.isActive(Features.FLAG_NETWORK_TO_ASYNC_INSTANTIATION)).thenReturn(false);}
                        , true},
                {"service with network + FLAG_NETWORK_TO_ASYNC_INSTANTIATION true", (BiConsumer<ServiceModel, FeatureManager>) (serviceModel, fm)->{
                    when(serviceModel.getNetworks()).thenReturn(ImmutableMap.of("a", mock(Network.class)));
                    when(fm.isActive(Features.FLAG_NETWORK_TO_ASYNC_INSTANTIATION)).thenReturn(true);}
                        , false},
                {"empty service + FLAG_NETWORK_TO_ASYNC_INSTANTIATION false", (BiConsumer<ServiceModel, FeatureManager>) (serviceModel, fm)->when(fm.isActive(Features.FLAG_NETWORK_TO_ASYNC_INSTANTIATION)).thenReturn(false), false},
        };
    }

    @Test(dataProvider="mockerForMacroExcluded")
    public void testIsMacroExcludedFromAsyncFlow(String testDescription, BiConsumer<ServiceModel, FeatureManager> mocker, boolean shallBeExcluded) {
        ServiceModel serviceModel = mock(ServiceModel.class);
        mocker.accept(serviceModel, featureManagerMock);
        assertEquals(shallBeExcluded, vidNotionsBuilder.isMacroExcludedFromAsyncFlow(serviceModel));
    }

    @DataProvider
    public static Object[][] toscaParserInstantiationTypeToVidNotion() {
        return new Object[][] {
                {ToscaParserImpl2.Constants.MACRO, InstantiationType.Macro},
                {ToscaParserImpl2.Constants.A_LA_CARTE, InstantiationType.ALaCarte},
                {ToscaParserImpl2.Constants.CLIENT_CONFIG, InstantiationType.ClientConfig},
                {"I dont know", InstantiationType.ClientConfig},
                {"", InstantiationType.ClientConfig}
        };
    }

    @Test(dataProvider="toscaParserInstantiationTypeToVidNotion")
    public void testSuggestInstantiationTypeWhenInstantiationUiLegacy(String toscaParserInstantiationType, InstantiationType expectedInstantiationType) {
        ServiceModel serviceModel = mock(ServiceModel.class);
        mockInstantiationType(serviceModel, toscaParserInstantiationType);
        assertEquals(expectedInstantiationType, vidNotionsBuilder.suggestInstantiationType(serviceModel, ModelCategory.OTHER));
    }

    @DataProvider
    public static Object[][] instantiationUIAndFeatureFlagsForInstantiationType() {
        return new Object[][] {
                {ModelCategory.Transport, Features.FLAG_1908_TRANSPORT_SERVICE_NEW_INSTANTIATION_UI, true, InstantiationType.Macro},
                {ModelCategory.Transport, Features.FLAG_1908_TRANSPORT_SERVICE_NEW_INSTANTIATION_UI, false, InstantiationType.ALaCarte},
                {ModelCategory.INFRASTRUCTURE_VPN, Features.FLAG_1908_INFRASTRUCTURE_VPN, true, InstantiationType.Macro},
                {ModelCategory.INFRASTRUCTURE_VPN, Features.FLAG_1908_INFRASTRUCTURE_VPN, false, InstantiationType.ALaCarte},
                {ModelCategory.OTHER, Features.FLAG_1908_INFRASTRUCTURE_VPN, true, InstantiationType.ALaCarte}, //not mapped InstantiationUI
        };
    }

    @Test(dataProvider="instantiationUIAndFeatureFlagsForInstantiationType")
    public void testSuggestInstantiationTypeByModelCategoryAndFeatureFlags(
            ModelCategory instantiationUI,
            Features featureFlag,
            boolean isFeatureOn,
            InstantiationType expectedInstantiationType) {
        ServiceModel serviceModel = mock(ServiceModel.class);
        mockInstantiationType(serviceModel, Constants.A_LA_CARTE);
        when(featureManagerMock.isActive(featureFlag)).thenReturn(isFeatureOn);
        assertEquals(expectedInstantiationType, vidNotionsBuilder.suggestInstantiationType(serviceModel, instantiationUI));
    }

    @DataProvider
    public static Object[][] FLAG_1908_COLLECTION_RESOURCE_NEW_INSTANTIATION_UIValueAndCollectionResourceForVidNotions() {
        return new Object[][] {
                {true, ImmutableMap.of("Some string", mock(CR.class)), InstantiationUI.SERVICE_WITH_COLLECTION_RESOURCE, ModelCategory.SERVICE_WITH_COLLECTION_RESOURCE},
                {true, Collections.EMPTY_MAP, InstantiationUI.LEGACY, ModelCategory.OTHER},
                {true, null, InstantiationUI.LEGACY, ModelCategory.OTHER},
                {false, ImmutableMap.of("Some string", mock(CR.class)), InstantiationUI.LEGACY, ModelCategory.SERVICE_WITH_COLLECTION_RESOURCE},
                {false, Collections.EMPTY_MAP, InstantiationUI.LEGACY, ModelCategory.OTHER},
                {false, null, InstantiationUI.LEGACY, ModelCategory.OTHER}
        };
    }

    @Test(dataProvider="FLAG_1908_COLLECTION_RESOURCE_NEW_INSTANTIATION_UIValueAndCollectionResourceForVidNotions")
    public void testSuggestInstantiationUiAndModelCategoryByCollectionResourceAndFeatureFlag_FLAG_1908_COLLECTION_RESOURCE_NEW_INSTANTIATION_UI(
            boolean featureFlagValue,
            Map<String, CR> collectionResource,
            VidNotions.InstantiationUI expectedInstantiationUi,
            VidNotions.ModelCategory expectedModelCategory) {
        initServiceModelAndscarHelperWithMocks();

        Service service = mock(Service.class);
        when(service.getInstantiationType()).thenReturn(ToscaParserImpl2.Constants.MACRO);
        when(serviceModel.getService()).thenReturn(service);
        when(serviceModel.getCollectionResources()).thenReturn(collectionResource);
        when(featureManagerMock.isActive(Features.FLAG_1908_COLLECTION_RESOURCE_NEW_INSTANTIATION_UI)).thenReturn(featureFlagValue);
        VidNotions vidNotions = vidNotionsBuilder.buildVidNotions(csarHelper, serviceModel);
        assertEquals(expectedInstantiationUi, vidNotions.getInstantiationUI());
        assertEquals(expectedModelCategory, vidNotions.getModelCategory());
        assertEquals(InstantiationUI.LEGACY, vidNotions.getViewEditUI());
        assertEquals(InstantiationType.Macro, vidNotions.getInstantiationType());
    }

    @Test
    public void whenServiceModelIsNull_thenInstantiationTypeIsClientConfig() {
        assertEquals( InstantiationType.ClientConfig, vidNotionsBuilder.suggestInstantiationType(null, ModelCategory.OTHER));
    }

    @Test
    public void whenServiceInServiceModelIsNull_thenInstantiationTypeIsClientConfig() {
        assertEquals( InstantiationType.ClientConfig, vidNotionsBuilder.suggestInstantiationType(mock(ServiceModel.class), ModelCategory.OTHER));
    }

    @Test
    public void whenInstantiationTypeInServiceModelIsNull_thenInstantiationTypeIsClientConfig() {
        initServiceModelAndscarHelperWithMocks();
        mockInstantiationType(serviceModel, null);
        assertEquals( InstantiationType.ClientConfig, vidNotionsBuilder.suggestInstantiationType(serviceModel, ModelCategory.OTHER));
    }

    private void initServiceModelAndscarHelperWithRealCsar(String path) throws SdcToscaParserException, IOException {
        Path csarPath = Paths.get(new File(getClass().getResource(path).getPath()).getCanonicalPath());
        ToscaParserImpl2 toscaParser = new ToscaParserImpl2(vidNotionsBuilder);
        org.onap.vid.asdc.beans.Service asdcServiceMetadata = mock(org.onap.vid.asdc.beans.Service.class);
        when(asdcServiceMetadata.getVersion()).thenReturn("versions");
        serviceModel = toscaParser.makeServiceModel(csarPath, asdcServiceMetadata);
        csarHelper = toscaParser.getSdcCsarHelper(csarPath);
    }

    private void initServiceModelAndscarHelperWithMocks() {
        csarHelper = ToscaParserImpl2Test.getMockedSdcCsarHelper(UUID.randomUUID().toString());
        serviceModel = mock(ServiceModel.class);
    }

    @DataProvider
    public static Object[][] VnfNcIndicationDataProvider() {
        return new Object[][] {
                {true, "VNF",  InstantiationUI.A_LA_CARTE_VNF_SERVICE_ROLE},
                {false, "VNF", InstantiationUI.LEGACY},
                {false, "notVNF", InstantiationUI.LEGACY},
                {true, null, InstantiationUI.LEGACY},
                {true, "notVNF", InstantiationUI.LEGACY},
                {true, "vnf", InstantiationUI.A_LA_CARTE_VNF_SERVICE_ROLE},
        };
    }

    @Test (dataProvider = "VnfNcIndicationDataProvider")
    public void whenServiceRoleVnf_thenInstantiationTypeNewUI(boolean flagOn, String serviceRole, InstantiationUI expectedViewEditUi){
        initServiceModelAndscarHelperWithMocks();

        when(featureManagerMock.isActive(Features.FLAG_1908_A_LA_CARTE_VNF_NEW_INSTANTIATION_UI)).thenReturn(flagOn);

        when(csarHelper.getServiceMetadata()).thenReturn(new Metadata(serviceRole == null ?
                emptyMap() : ImmutableMap.of(ToscaParserImpl2.Constants.SERVICE_ROLE, serviceRole)
        ));

        assertEquals(expectedViewEditUi, vidNotionsBuilder.suggestInstantiationUI(csarHelper, serviceModel, ModelCategory.OTHER));
    }

    private static NodeTemplate mockNodeTemplateChild(boolean withFabricConfiguration) {
        NodeTemplate child = mock(NodeTemplate.class);
        when(child.getType()).thenReturn(withFabricConfiguration ? ToscaParserImpl2.Constants.FABRIC_CONFIGURATION_TYPE : "nothing");
        return child;
    }

    private static ISdcCsarHelper mockServiceNodeTemplates(ISdcCsarHelper csarHelper, ImmutableList<NodeTemplate> children) {
        when(csarHelper.getNodeTemplateChildren(any())).thenReturn(children);

        NodeTemplate parent = mock(NodeTemplate.class);
        List<NodeTemplate> nodeTemplates = ImmutableList.of(parent);

        when(csarHelper.getServiceNodeTemplates()).thenReturn(nodeTemplates);
        return csarHelper;
    }

    @DataProvider
    public static Object[][] csarHelpersForFabricConfiguration() {
        ISdcCsarHelper csarHelperWithNoNodes = mock(ISdcCsarHelper.class);
        when(csarHelperWithNoNodes.getServiceNodeTemplates()).thenReturn(emptyList());

        return new Object[][] {
                { "zero nodes", false, csarHelperWithNoNodes },
                { "single node with no child", false, mockServiceNodeTemplates(mock(ISdcCsarHelper.class), ImmutableList.of()) },
                { "single node with single fabric child", true, mockServiceNodeTemplates(mock(ISdcCsarHelper.class), ImmutableList.of(mockNodeTemplateChild(true))) },
                { "single node with single fabric child and single non-fabric", true, mockServiceNodeTemplates(mock(ISdcCsarHelper.class), ImmutableList.of(
                        mockNodeTemplateChild(true), mockNodeTemplateChild(true))) },
        };
    }

    @Test (dataProvider = "csarHelpersForFabricConfiguration")
    public void hasFabricConfiguration(String desc, boolean shouldHaveFabricConfiguration, ISdcCsarHelper csarHelper) {
        assertThat(desc, vidNotionsBuilder.hasFabricConfiguration(csarHelper), is(shouldHaveFabricConfiguration));
    }

    @DataProvider
    public static Object[][] macroTransportDataProvider() {
        return new Object[][]{
            {"transport service flag is open", true, true, true, InstantiationUI.LEGACY},
            {"macro service flag is open", true, false, true, InstantiationUI.MACRO_SERVICE},
            {"macro service flag is closed", false, true, true, InstantiationUI.LEGACY},
            {"transport service flag is closed", false, false, true, InstantiationUI.LEGACY},
            {"not a macro service", true, false, false, InstantiationUI.LEGACY}
        };
    }

    @Test (dataProvider = "macroTransportDataProvider")
    public void viewEditMacroService_transportOrNotTransport(String desc, boolean flagActive, boolean isTransport, boolean isMacro, InstantiationUI expectedViewEditUi) {
        initServiceModelAndscarHelperWithMocks();
        Service service = mock(Service.class);

        String instantiationType = isMacro ? ToscaParserImpl2.Constants.MACRO : ToscaParserImpl2.Constants.A_LA_CARTE;
        when(serviceModel.getService()).thenReturn(service);
        when(service.getInstantiationType()).thenReturn(instantiationType);
        when(featureManagerMock.isActive(Features.FLAG_1908_MACRO_NOT_TRANSPORT_NEW_VIEW_EDIT)).thenReturn(flagActive);
        when(featureManagerMock.isActive(Features.FLAG_1902_NEW_VIEW_EDIT)).thenReturn(false);
        when(csarHelper.getServiceMetadata()).thenReturn(new Metadata(isTransport ? ImmutableMap.of(ToscaParserImpl2.Constants.SERVICE_TYPE, "TRANSPORT") : emptyMap()
        ));

        assertEquals(expectedViewEditUi, vidNotionsBuilder.suggestViewEditUI(csarHelper, serviceModel, ModelCategory.OTHER));
    }

    @DataProvider
    public static Object[][] invariantUuidToMacroDataProvider() {
        return new Object[][]{
            {"117f5f1a-1b47-4ae1-ae04-489c9a7ada28", true},
            {"117F5f1a-1b47-4AE1-ae04-489C9A7ada28", true},
            {"2efab359-cdd4-4da2-9b79-61df990796c2", true},
            {"67e09a1f-9e42-4b63-8dee-bc60bae50de1", false},
            {"67e09A1F-9E42-4b63-8Dee-bc60bae50de1", false},
            {"5d854f6b-759c-4aa6-b472-7e4bb1c003d4", false},
            {"I'm not a uuid", false},
            {null, false},
        };
    }

    @Test(dataProvider = "invariantUuidToMacroDataProvider")
    public void testIsMacroByInvariantUuid(String uuid, boolean expectedIsMacro) {
        assertEquals(expectedIsMacro, vidNotionsBuilder.isMacroByInvariantUuid(uuid));
    }
}
