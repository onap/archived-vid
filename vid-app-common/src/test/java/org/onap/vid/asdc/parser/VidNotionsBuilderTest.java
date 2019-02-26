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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.sdc.tosca.parser.api.ISdcCsarHelper;
import org.onap.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.onap.sdc.tosca.parser.impl.SdcToscaParserFactory;
import org.onap.sdc.toscaparser.api.NodeTemplate;
import org.onap.sdc.toscaparser.api.Property;
import org.onap.sdc.toscaparser.api.elements.Metadata;
import org.onap.vid.model.*;
import org.onap.vid.properties.Features;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.function.BiConsumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class VidNotionsBuilderTest {

    @InjectMocks
    VidNotionsBuilder vidNotionsBuilder;

    @Mock
    private FeatureManager featureManagerMock;

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

        assertThat(vidNotionsBuilder.suggestInstantiationUI(csarHelper), is(VidNotions.InstantiationUI.NETWORK_WITH_PROPERTY_NETWORK_TECHNOLOGY_EQUALS_STANDARD_SRIOV_OR_OVS));
        assertThat(vidNotionsBuilder.suggestModelCategory(csarHelper) , is(VidNotions.ModelCategory.IS_5G_PROVIDER_NETWORK_MODEL));
    }

    @NotNull
    protected ISdcCsarHelper mockForNonLegacyInstantiationUI() {
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
                {"A-La-Carte", VidNotions.InstantiationUI.ANY_ALACARTE_NEW_UI},
                {"Macro", VidNotions.InstantiationUI.LEGACY},
        };
    }

    @Test(dataProvider = "anyAlacarteDataProvider")
    public void FLAG_EXP_ANY_ALACARTE_NEW_INSTANTIATION_UI_is_active_UIHintIsPositive(String instantiationType, VidNotions.InstantiationUI expectedInstantiationUI) {
        when(featureManagerMock.isActive(Features.FLAG_EXP_ANY_ALACARTE_NEW_INSTANTIATION_UI)).thenReturn(true);
        ISdcCsarHelper csarHelper = ToscaParserImpl2Test.getMockedSdcCsarHelper(UUID.randomUUID().toString());
        when(csarHelper.getServiceMetadata()).thenReturn(new Metadata(ImmutableMap.of(
                "instantiationType", instantiationType
        )));
        NodeTemplate nodeTemplate = mock(NodeTemplate.class);

        when(nodeTemplate.getProperties()).thenReturn(new LinkedHashMap<>(ImmutableMap.of(
                "dummy_val", mock(Property.class),
                "network_technology", new Property(Pair.of("network_technology","ovs"))
        )));

        when(csarHelper.getServiceVlList()).thenReturn(ImmutableList.of(nodeTemplate));

        assertThat(vidNotionsBuilder.suggestInstantiationUI(csarHelper), is(expectedInstantiationUI));
    }

    @Test
    public void VLNetworkWithPropertyNetworkTechnologyNot5G_UIHintIsNegative() {
        ISdcCsarHelper csarHelper = ToscaParserImpl2Test.getMockedSdcCsarHelper(UUID.randomUUID().toString());

        NodeTemplate nodeTemplate = mock(NodeTemplate.class);

        when(nodeTemplate.getProperties()).thenReturn(new LinkedHashMap<>(ImmutableMap.of(
                "dummy_val", mock(Property.class),
                "network_technology", new Property(Pair.of("network_technology","old_value"))
        )));

        when(csarHelper.getServiceVlList()).thenReturn(ImmutableList.of(nodeTemplate));

        assertThat(vidNotionsBuilder.suggestInstantiationUI(csarHelper), is(VidNotions.InstantiationUI.LEGACY));
        assertThat(vidNotionsBuilder.suggestModelCategory(csarHelper) , is(VidNotions.ModelCategory.OTHER));
    }

    @Test
    public void withoutMocks_givenZippedToscaFile_hasAnyNetworkWithPropertyEqualsToAnyOfYieldsTrue() throws SdcToscaParserException {
        SdcToscaParserFactory factory = SdcToscaParserFactory.getInstance();
        ISdcCsarHelper csarHelper = factory.getSdcCsarHelper(getClass().getClassLoader().getResource("service-vl-csar.zip").getPath(),false);

        assertThat(vidNotionsBuilder.isALaCarte(csarHelper), is(false));
        assertThat(vidNotionsBuilder.hasAnyNetworkWithPropertyEqualsToAnyOf(csarHelper, "unexpected_property_name"), is(false));
        assertThat(vidNotionsBuilder.hasAnyNetworkWithPropertyEqualsToAnyOf(csarHelper, "network_technology","Standard-SR-IOV"), is(true));
        assertThat(vidNotionsBuilder.suggestInstantiationUI(csarHelper), is(VidNotions.InstantiationUI.LEGACY));
    }

    //@Test
    //public void withoutMocks_givenZippedToscaFile_hasFabricConfigurationYieldsTrue() throws SdcToscaParserException {
    //    SdcToscaParserFactory factory = SdcToscaParserFactory.getInstance();
    //    ISdcCsarHelper csarHelper = factory.getSdcCsarHelper(getClass().getClassLoader().getResource("service-fabric-configuration.zip").getPath(),false);
    //
    //    assertThat(vidNotionsBuilder.isALaCarte(csarHelper), is(false));
    //    assertThat(vidNotionsBuilder.hasFabricConfiguration(csarHelper), is(true));
    //    assertThat(vidNotionsBuilder.suggestInstantiationUI(csarHelper), is(VidNotions.InstantiationUI.LEGACY));
    //}


    @Test
    public void uuidIsExactly1ffce89fEtc_UIHintIsPositive() {
        ISdcCsarHelper csarHelper = ToscaParserImpl2Test.getMockedSdcCsarHelper(UUID.randomUUID().toString());

        when(csarHelper.getServiceMetadata()).thenReturn(new Metadata(ImmutableMap.of(
                "UUID", "95eb2c44-bff2-4e8b-ad5d-8266870b7717"
        )));
        when(featureManagerMock.isActive(Features.FLAG_5G_IN_NEW_INSTANTIATION_UI)).thenReturn(true);
        assertThat(vidNotionsBuilder.suggestInstantiationUI(csarHelper), is(VidNotions.InstantiationUI.SERVICE_UUID_IS_1ffce89f_ef3f_4cbb_8b37_82134590c5de));
    }


    @DataProvider
    public static Object[][] trueAndFalse() {
        return new Object[][] {{true}, {false}};
    }

    @Test(dataProvider = "trueAndFalse")
    public void buildVidNotions_nullByFlag(boolean flagValue) {
        ISdcCsarHelper csarHelper = ToscaParserImpl2Test.getMockedSdcCsarHelper(UUID.randomUUID().toString());

        when(featureManagerMock.isActive(Features.FLAG_5G_IN_NEW_INSTANTIATION_UI)).thenReturn(flagValue);
        assertThat(vidNotionsBuilder.buildVidNotions(csarHelper, null), hasProperty("instantiationUI", is(VidNotions.InstantiationUI.LEGACY)));
    }

    @DataProvider
    public static Object[][] ServiceRoleTypesDataProvider() {
        return new Object[][] {
                {"gROUPING", VidNotions.InstantiationUI.SERVICE_WITH_VNF_GROUPING},
                {"", VidNotions.InstantiationUI.LEGACY},
        };
    }

    @Test(dataProvider = "ServiceRoleTypesDataProvider")
    public void testGetViewEditUITypeForResourceGroup(String serviceRole, VidNotions.InstantiationUI expectedViewEditUI) {
        when(featureManagerMock.isActive(Features.FLAG_ASYNC_INSTANTIATION)).thenReturn(true);
        when(featureManagerMock.isActive(Features.FLAG_1902_VNF_GROUPING)).thenReturn(true);
        ISdcCsarHelper csarHelper = ToscaParserImpl2Test.getMockedSdcCsarHelper(UUID.randomUUID().toString());
        when(csarHelper.getServiceMetadata()).thenReturn(new Metadata(ImmutableMap.of(
                "serviceRole", serviceRole
        )));

        assertThat(vidNotionsBuilder.suggestViewEditUI(csarHelper, null), is(expectedViewEditUI));
    }

    @DataProvider
    public static Object[][] macroToViewEditDataProvider() {
        return new Object[][] {
                {"macro service + not excluded + needed flags are open", true, false, true, true, VidNotions.InstantiationUI.MACRO_SERVICE},
                {"not macro service", false, false, true, true, VidNotions.InstantiationUI.LEGACY},
                {"macro that shall be excluded because it has pnf", true, true, true, true, VidNotions.InstantiationUI.LEGACY},
                {"macro service + FLAG_ASYNC_INSTANTIATION off", true, false, false, true, VidNotions.InstantiationUI.LEGACY},
                {"macro service + FLAG_1902_NEW_VIEW_EDIT off", true, false, true, false, VidNotions.InstantiationUI.LEGACY},
        };
    }

    @Test(dataProvider="macroToViewEditDataProvider")
    public void whenServiceIsMacro_viewEditIsRight(
            String testDescription,
            boolean isMacro,
            boolean isExcluded,
            boolean isFlagAsyncInstantiationActive,
            boolean isFlag1902NewViewEdit,
            VidNotions.InstantiationUI expectedViewEditUi) {

        ISdcCsarHelper csarHelper = mock(ISdcCsarHelper.class);
        ServiceModel serviceModel = mock(ServiceModel.class);

        //mock for is Macro
        String instantiationType = isMacro ? ToscaParserImpl2.Constants.MACRO : ToscaParserImpl2.Constants.A_LA_CARTE;
        Service service = mock(Service.class);
        when(serviceModel.getService()).thenReturn(service);
        when(service.getInstantiationType()).thenReturn(instantiationType);
        when(featureManagerMock.isActive(Features.FLAG_ASYNC_INSTANTIATION)).thenReturn(isFlagAsyncInstantiationActive);
        when(featureManagerMock.isActive(Features.FLAG_1902_NEW_VIEW_EDIT)).thenReturn(isFlag1902NewViewEdit);

        //mock for isExcluded
        if (isExcluded) {
            when(serviceModel.getPnfs()).thenReturn(ImmutableMap.of("a", mock(Node.class)));
        }

        VidNotions.InstantiationUI result = vidNotionsBuilder.suggestViewEditUI(csarHelper, serviceModel);
        assertEquals(expectedViewEditUi, result);
    }

    @DataProvider
    public static Object[][] instantiationUIToViewEditDataProvider() {
        return new Object[][] {
                {"network cloud(5G) service + needed flags are open", true, true, true, VidNotions.InstantiationUI.NETWORK_WITH_PROPERTY_NETWORK_TECHNOLOGY_EQUALS_STANDARD_SRIOV_OR_OVS},
                {"mocked service + needed flags are open", false, true, true, VidNotions.InstantiationUI.LEGACY},
                {"network cloud(5G) service + FLAG_ASYNC_INSTANTIATION is off", true, false, true, VidNotions.InstantiationUI.LEGACY},
                {"network cloud(5G) service + FLAG_1902_NEW_VIEW_EDIT is off", true, true, false, VidNotions.InstantiationUI.LEGACY},
        };
    }


    @Test(dataProvider="instantiationUIToViewEditDataProvider")
    public void whenInstantiationUIIsNotLegacy_viewEditIsRight(
            String testDescription,
            boolean isInstantiationUINotLegacy,
            boolean isFlagAsyncInstantiationActive,
            boolean isFlag1902NewViewEdit,
            VidNotions.InstantiationUI expectedViewEditUi) {

        ISdcCsarHelper csarHelper = isInstantiationUINotLegacy ?  mockForNonLegacyInstantiationUI() : mock(ISdcCsarHelper.class);
        when(featureManagerMock.isActive(Features.FLAG_ASYNC_INSTANTIATION)).thenReturn(isFlagAsyncInstantiationActive);
        when(featureManagerMock.isActive(Features.FLAG_1902_NEW_VIEW_EDIT)).thenReturn(isFlag1902NewViewEdit);

        ServiceModel serviceModel = mock(ServiceModel.class);
        Service service = mock(Service.class);
        when(serviceModel.getService()).thenReturn(service);
        when(service.getInstantiationType()).thenReturn(ToscaParserImpl2.Constants.A_LA_CARTE);

        VidNotions.InstantiationUI result = vidNotionsBuilder.suggestViewEditUI(csarHelper, serviceModel);
        assertEquals(expectedViewEditUi, result);
    }

    @DataProvider
    public static Object[][] mockerForMacroExcluded() {
        return new Object[][] {
                {"service with pnfs", (BiConsumer<ServiceModel, FeatureManager>) (serviceModel, fm)->when(serviceModel.getPnfs()).thenReturn(ImmutableMap.of("a", mock(Node.class))), true},
                {"service with collection resource", (BiConsumer<ServiceModel, FeatureManager>) (serviceModel, fm)->when(serviceModel.getCollectionResource()).thenReturn(ImmutableMap.of("a", mock(CR.class))), true},
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






}
