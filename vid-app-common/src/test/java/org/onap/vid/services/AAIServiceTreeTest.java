package org.onap.vid.services;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jetbrains.annotations.NotNull;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.asdc.parser.ServiceModelInflator;
import org.onap.vid.asdc.parser.ServiceModelInflator.Names;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class AAIServiceTreeTest {

    @Mock
    private VidService sdcService;
    @Mock
    private ServiceModelInflator serviceModelInflator;
    @InjectMocks
    private AAIServiceTree aaiServiceTree;

    @BeforeTest
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
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
        aaiServiceTree.enrichNodesWithModelCustomizationName(nodesUnderTest, null);

        assertThat(toStrings(nodesUnderTest), containsInAnyOrder(toStringsArray(nodesWithVersionIdsAndCustomizationNames(versionIds, expectedNames))));
    }

    @Test
    public void enrichNodesWithModelCustomizationName_noNodes_noError() {

        when(serviceModelInflator.toNamesByVersionId(any())).thenReturn(ImmutableMap.of(
                "11c6dc3e-cd6a-41b3-a50e-b5a10f7157d0", new Names("my model cust name", "my key")
        ));

        aaiServiceTree.enrichNodesWithModelCustomizationName(emptyList(), null);
    }

    @Test
    public void enrichNodesWithModelCustomizationName_nothingInModel_nodesUnchanged() {

        when(serviceModelInflator.toNamesByVersionId(any())).thenReturn(emptyMap());

        final ImmutableList<String> versionIds = ImmutableList.of("version id a", "version id b", "version id c");
        final Names nullNames = new Names(nullString, nullString);
        final ImmutableList<Names> expectedNames = ImmutableList.of(nullNames, nullNames, nullNames);

        
        final List<AAITreeNode> nodesUnderTest = nodesWithVersionIds(versionIds);

        aaiServiceTree.enrichNodesWithModelCustomizationName(nodesUnderTest, null);

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

        aaiServiceTree.enrichNodesWithModelCustomizationName(nodesUnderTest, null);

        assertThat(toStrings(nodesUnderTest), containsInAnyOrder(toStringsArray(nodesWithVersionIdsAndCustomizationNames(versionIds, expectedNames))));
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