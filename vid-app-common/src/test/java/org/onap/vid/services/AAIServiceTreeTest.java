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

package org.onap.vid.services;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.vid.services.AAITreeNodeBuilderTest.createExpectedVnfTreeNode;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.List;
import java.util.concurrent.ExecutorService;
import net.javacrumbs.jsonunit.core.Option;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.AaiClient;
import org.onap.vid.aai.util.AAITreeConverter;
import org.onap.vid.asdc.parser.ServiceModelInflator;
import org.onap.vid.model.ModelUtil;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.model.aaiTree.NodeType;
import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.testUtils.TestUtils;
import org.onap.vid.utils.Logging;
import org.onap.vid.utils.Unchecked;
import org.springframework.http.HttpMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

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

    @Test
    public void whenBuildTreeForOneResource_resultAsExpected() {

        AaiClient aaiClientMock = mock(AaiClient.class);
        ExecutorService executorService = MoreExecutors.newDirectExecutorService();
        AAIServiceTree aaiServiceTree = new AAIServiceTree(
            new AAITreeNodeBuilder(aaiClientMock, new Logging()),
            null,
            new AAITreeConverter(new ModelUtil()), null,
            executorService
        );

        String url = "anyUrl/vnf";

        JsonNode mockedAaiGetVnfResponse = TestUtils.readJsonResourceFileAsObject("/getTopology/vnf.json", JsonNode.class);
        when(aaiClientMock.typedAaiRest(Unchecked.toURI(url), JsonNode.class, null, HttpMethod.GET, false)).thenReturn(mockedAaiGetVnfResponse);

        CloudConfiguration expectedCloudConfiguration = new CloudConfiguration("dyh3b", "c8035f5ee95d4c62bbc8074c044122b9", "irma-aic");
        AAITreeNode expectedVnfNode = createExpectedVnfTreeNode(expectedCloudConfiguration);

        List<AAITreeNode> aaiTreeNodes = aaiServiceTree.buildAAITreeForUniqueResource(url, NodeType.GENERIC_VNF);
        assertEquals(1, aaiTreeNodes.size());
        assertThat(aaiTreeNodes.get(0), jsonEquals(expectedVnfNode).when(Option.IGNORING_EXTRA_FIELDS).whenIgnoringPaths("relationshipList", "children[0].relationshipList"));
    }
}