/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2020 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.services;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toList;
import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import net.javacrumbs.jsonunit.core.Option;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.model.ModelVer;
import org.onap.vid.asdc.parser.ServiceModelInflator;
import org.onap.vid.asdc.parser.ServiceModelInflator.Names;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.model.aaiTree.NodeType;
import org.onap.vid.properties.Features;
import org.onap.vid.testUtils.TestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

public class AAITreeNodesEnricherTest {

    @Mock
    private AaiClientInterface aaiClient;
    @Mock
    private VidService sdcService;
    @Mock
    private ServiceModelInflator serviceModelInflator;
    @Mock
    private FeatureManager featureManager;
    @InjectMocks
    private AAITreeNodesEnricher aaiTreeNodesEnricher;

    @BeforeMethod
    public void initMocks() {
        TestUtils.initMockitoMocks(this);
    }

    private final static String nullString = "null placeholder";

    @Test
    public void enrichNodesWithModelCustomizationName_simple3NodesCase_nodesEnriched() {

        when(serviceModelInflator.toNamesByVersionId(any())).thenReturn(ImmutableMap.of(
                "version id a", new Names("name a", "key a"),
                "version id b", new Names("name b", "key b"),
                "version id c", new Names("name c", "key c")
        ));

        final ImmutableList<String> versionIds = ImmutableList.of("version id a", "version id b", "version id c");
        final ImmutableList<Names> expectedNames = ImmutableList.of(
                new Names("name a", "key a"),
                new Names("name b", "key b"),
                new Names("name c", "key c"));


        final List<AAITreeNode> nodesUnderTest = nodesWithVersionIds(versionIds);
        aaiTreeNodesEnricher.enrichNodesWithModelCustomizationName(nodesUnderTest, null);

        assertThat(toStrings(nodesUnderTest), containsInAnyOrder(toStringsArray(nodesWithVersionIdsAndCustomizationNames(versionIds, expectedNames))));
    }

    @Test
    public void enrichNodesWithModelCustomizationName_noNodes_noError() {

        when(serviceModelInflator.toNamesByVersionId(any())).thenReturn(ImmutableMap.of(
                "11c6dc3e-cd6a-41b3-a50e-b5a10f7157d0", new Names("my model cust name", "my key")
        ));

        aaiTreeNodesEnricher.enrichNodesWithModelCustomizationName(emptyList(), null);
    }

    @Test
    public void enrichNodesWithModelCustomizationName_nothingInModel_nodesUnchanged() {

        when(serviceModelInflator.toNamesByVersionId(any())).thenReturn(emptyMap());

        final ImmutableList<String> versionIds = ImmutableList.of("version id a", "version id b", "version id c");
        final Names nullNames = new Names(nullString, nullString);
        final ImmutableList<Names> expectedNames = ImmutableList.of(nullNames, nullNames, nullNames);

        
        final List<AAITreeNode> nodesUnderTest = nodesWithVersionIds(versionIds);

        aaiTreeNodesEnricher.enrichNodesWithModelCustomizationName(nodesUnderTest, null);

        assertThat(toStrings(nodesUnderTest), containsInAnyOrder(toStringsArray(nodesWithVersionIdsAndCustomizationNames(versionIds, expectedNames))));
    }

    @Test
    public void enrichNodesWithModelCustomizationName_staggered4NodesAndNull_3nodesEnriched2isNull() {

        when(serviceModelInflator.toNamesByVersionId(any())).thenReturn(ImmutableMap.of(
                "version id Z", new Names("name Z", "key Z"),
                "version id d", new Names(null, "key d"),
                "version id c", new Names("name c", null),
                "version id a", new Names("name a", "key a")
        ));

        final ImmutableList<String> versionIds = ImmutableList.of("version id a", "version id b", "version id c", "version id d", nullString);
        final ImmutableList<Names> expectedNames = ImmutableList.of(
                new Names("name a", "key a"),
                new Names(nullString, nullString),
                new Names("name c", nullString),
                new Names(nullString, "key d"),
                new Names(nullString, nullString)
        );


        final List<AAITreeNode> nodesUnderTest = nodesWithVersionIds(versionIds);

        aaiTreeNodesEnricher.enrichNodesWithModelCustomizationName(nodesUnderTest, null);

        assertThat(toStrings(nodesUnderTest), containsInAnyOrder(toStringsArray(nodesWithVersionIdsAndCustomizationNames(versionIds, expectedNames))));
    }

    @DataProvider
    public static Object[][] enrichVfModulesFilteredOutCases() {
        AAITreeNode volumeGroup = new AAITreeNode();
        volumeGroup.setType(NodeType.VOLUME_GROUP);

        AAITreeNode vfModuleWithoutData = new AAITreeNode();
        vfModuleWithoutData.setType(NodeType.VF_MODULE);

        AAITreeNode vfModuleWithModelCustomizationName = new AAITreeNode();
        vfModuleWithModelCustomizationName.setType(NodeType.VF_MODULE);
        vfModuleWithModelCustomizationName.setModelCustomizationName("foo");

        AAITreeNode vfModuleWithKeyInModel = new AAITreeNode();
        vfModuleWithKeyInModel.setType(NodeType.VF_MODULE);
        vfModuleWithKeyInModel.setKeyInModel("foo");

        return new Object[][]{
            {"no nodes", null, true},
            {"no vfmodules", volumeGroup, true},
            {"flag is off", vfModuleWithoutData, false},
            {"all vfmodules with either getKeyInModel or getModelCustomizationId", vfModuleWithModelCustomizationName, true},
            {"all vfmodules with either getKeyInModel or getModelCustomizationId", vfModuleWithKeyInModel, true},
        };
    }

    @Test(dataProvider = "enrichVfModulesFilteredOutCases")
    public void enrichVfModulesWithModelCustomizationNameFromOtherVersions_givenFilteredOutCases_doNothing(String reasonToSkip, AAITreeNode node, boolean flagState) {
        when(featureManager.isActive(Features.FLAG_EXP_TOPOLOGY_TREE_VFMODULE_NAMES_FROM_OTHER_TOSCA_VERSIONS))
            .thenReturn(flagState);

        when(aaiClient.getSortedVersionsByInvariantId(any()))
            .thenThrow(new AssertionError("did not expect reaching getSortedVersionsByInvariantId"));

        List<AAITreeNode> nodes = (node == null) ? emptyList() : ImmutableList.of(node);
        aaiTreeNodesEnricher.enrichVfModulesWithModelCustomizationNameFromOtherVersions(nodes, "modelInvariantId");
    }

    @Test
    public void enrichVfModulesWithModelCustomizationNameFromOtherVersions_givenMissingKeysFoundInFirstSdcModels_sdcRequestsNotExhausted() {
        when(featureManager.isActive(Features.FLAG_EXP_TOPOLOGY_TREE_VFMODULE_NAMES_FROM_OTHER_TOSCA_VERSIONS))
            .thenReturn(true);

        final ListIterator<ModelVer> manyVersionsIterator = manyModelVerIteratorMock();

        String customizationIdToFind = "please-find-me";
        Names anyNames = mock(Names.class);

        when(serviceModelInflator.toNamesByCustomizationId(any()))
            .thenReturn(singletonMap("uuidBefore", anyNames))
            .thenReturn(singletonMap(customizationIdToFind, anyNames))
            .thenReturn(singletonMap("uuidAfter", anyNames))
        ;


        final Map<String, Names> inOutMutableNamesByCustomizationId = new HashMap<>();
        aaiTreeNodesEnricher.fetchCustomizationIdsFromToscaModelsWhileNeeded(
            inOutMutableNamesByCustomizationId, manyVersionsIterator, customizationIdToFind);

        assertThat(inOutMutableNamesByCustomizationId, allOf(
            hasKey(customizationIdToFind),
            hasKey("uuidBefore"),
            not(hasKey("uuidAfter")))
        );

        verify(manyVersionsIterator, times(2)).next();
        verify(sdcService, times(2)).getServiceModelOrThrow(any());
    }

    private ListIterator<ModelVer> manyModelVerIteratorMock() {
        final ModelVer modelVerMock = mock(ModelVer.class);
        final ListIterator<ModelVer> result = mock(ListIterator.class);

        when(result.next()).thenReturn(modelVerMock);
        when(result.hasNext())
            .thenReturn(true, true, true, true, true, true, false);

        return result;
    }

    @FunctionalInterface
    public interface Creator<T, R> {
        R by(T t);
    }

    @Test
    public void enrichVfModulesWithModelCustomizationNameFromOtherVersions() {
        /*
        Verifies the following

        [*] aaiClient.getSortedVersionsByInvariantId response is exhausted
            and all models fetched from sdc
            -> we can see that model #1 info is populated in nodes
        [*] relevant nodes enriched
        [*] where data not found: nodes not enriched
        [*] where data there already: node left pristine
        [*] where node is not vfmodule: node left pristine

         */

        String modelInvariantId = "modelInvariantId";

        ///////////// HELPERS

        Creator<Integer, AAITreeNode> nodeMissingData = n -> {
            AAITreeNode node = new AAITreeNode();
            node.setType(NodeType.VF_MODULE);
            node.setModelCustomizationId("model-customization-id-" + n);
            return node;
        };

        Creator<Integer, AAITreeNode> nodeWithData = n -> {
            AAITreeNode node = nodeMissingData.by(n);
            node.setModelCustomizationName("modelCustomizationName-" + n);
            node.setKeyInModel("modelKey-" + n);
            return node;
        };

        Creator<Integer, ImmutableMap<String, Names>> namesMap = n -> ImmutableMap.of(
            "model-customization-id-" + n,
            new Names("modelCustomizationName-" + n, "modelKey-" + n)
        );

        Creator<Integer, ModelVer> modelVer = n -> {
            ModelVer m = new ModelVer();
            m.setModelVersion("model-version-" + n);
            m.setModelVersionId("model-version-id-" + n);
            return m;
        };

        ///////////// SET-UP

        when(featureManager.isActive(Features.FLAG_EXP_TOPOLOGY_TREE_VFMODULE_NAMES_FROM_OTHER_TOSCA_VERSIONS))
            .thenReturn(true);

        /*
        +-------------+----------+----------+----------+-----------+-----------+
        |             | model v1 | model v2 | model v3 | model v77 | model v99 |
        +-------------+----------+----------+----------+-----------+-----------+
        | in AAI list |   v      |   v      |   v      |   v       |           |
        | in SDC      |   v      |   v      |   v      |           |           |
        | in nodes    |   v      |          |   v      |   v       |   v       |
        +-------------+----------+----------+----------+-----------+-----------+
         */
        when(aaiClient.getSortedVersionsByInvariantId(modelInvariantId))
            .thenReturn(ImmutableList.of(modelVer.by(3), modelVer.by(77), modelVer.by(2), modelVer.by(1)));

        ServiceModel serviceModel_1 = mock(ServiceModel.class);
        when(sdcService.getServiceModelOrThrow("model-version-id-1")).thenReturn(serviceModel_1);
        when(serviceModelInflator.toNamesByCustomizationId(serviceModel_1)).thenReturn(namesMap.by(1));

        ServiceModel serviceModel_2 = mock(ServiceModel.class);
        when(sdcService.getServiceModelOrThrow("model-version-id-2")).thenReturn(serviceModel_2);
        when(serviceModelInflator.toNamesByCustomizationId(serviceModel_2)).thenReturn(namesMap.by(2));

        ServiceModel serviceModel_3 = mock(ServiceModel.class);
        when(sdcService.getServiceModelOrThrow("model-version-id-3")).thenReturn(serviceModel_3);
        when(serviceModelInflator.toNamesByCustomizationId(serviceModel_3)).thenReturn(namesMap.by(3));

        when(sdcService.getServiceModelOrThrow("model-version-id-77")).thenThrow(GenericUncheckedException.class);

        AAITreeNode nodeWithDataAlready = nodeMissingData.by(1);
        nodeWithDataAlready.setModelCustomizationName("significant-customization-name");
        nodeWithDataAlready.setKeyInModel("significant-key-in-model");

        AAITreeNode nodeNotVfModule = nodeMissingData.by(1);
        nodeNotVfModule.setType(NodeType.GENERIC_VNF);

        Collection<AAITreeNode> nodes = ImmutableList.of(
            nodeMissingData.by(1), nodeMissingData.by(77),
            nodeMissingData.by(3), nodeMissingData.by(99),
            nodeWithDataAlready, nodeNotVfModule
        );

        ///////////// TEST

        aaiTreeNodesEnricher.enrichVfModulesWithModelCustomizationNameFromOtherVersions(nodes, modelInvariantId);

        assertThat(nodes, jsonEquals(ImmutableList.of(
            nodeWithData.by(1),
            nodeWithData.by(3),
            nodeMissingData.by(77), // not in sdcService
            nodeMissingData.by(99), // not in aaiClient
            nodeWithDataAlready, // pristine
            nodeNotVfModule // pristine
        )).when(Option.IGNORING_ARRAY_ORDER));
    }


    @NotNull
    private String[] toStringsArray(List<AAITreeNode> nodes) {
        return toStrings(nodes).toArray(new String[] {});
    }

    @NotNull
    private List<String> toStrings(List<AAITreeNode> nodes) {
        return nodes.stream().map(n -> {
            final ReflectionToStringBuilder reflectionToStringBuilder = new ReflectionToStringBuilder(n, ToStringStyle.SHORT_PREFIX_STYLE);
            reflectionToStringBuilder.setExcludeNullValues(true);
            return reflectionToStringBuilder.toString();
        }).collect(toList());
    }

    @NotNull
    private List<AAITreeNode> nodesWithVersionIdsAndCustomizationNames(List<String> versionIds, List<Names> customizationNames) {
        return Streams
                .zip(versionIds.stream(), customizationNames.stream(), this::nodeWithVersionIdAndCustomizationName)
                .collect(toList());
    }

    @NotNull
    private List<AAITreeNode> nodesWithVersionIds(List<String> versionIds) {
        return versionIds.stream()
                .map(versionId -> nodeWithVersionIdAndCustomizationName(versionId, new Names(nullString, nullString)))
                .collect(toList());
    }

    private AAITreeNode nodeWithVersionIdAndCustomizationName(String versionId, Names names) {
        AAITreeNode newNode = new AAITreeNode();
        newNode.setModelVersionId(versionId.equals(nullString) ? null : versionId);
        newNode.setModelCustomizationName(names.getModelCustomizationName().equals(nullString) ? null : names.getModelCustomizationName());
        newNode.setKeyInModel(names.getModelKey().equals(nullString) ? null : names.getModelKey());
        return newNode;
    }

}