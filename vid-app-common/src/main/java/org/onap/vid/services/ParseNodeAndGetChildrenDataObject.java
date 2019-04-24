/*-
 * ============LICENSE_START=======================================================
 *  Copyright (C) 2019 Samsung Electronics Co., Ltd. All rights reserved.
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
 *
 * SPDX-License-Identifier: Apache-2.0
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.services;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import org.onap.vid.model.aaiTree.AAITreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import org.onap.vid.utils.Tree;

public class ParseNodeAndGetChildrenDataObject {

    private String nodeType;
    private String requestURL;
    private JsonNode topLevelJson;
    private ConcurrentSkipListSet<AAITreeNode> nodesAccumulator;
    private ExecutorService threadPool;
    private ConcurrentLinkedQueue<String> visitedNodes;
    private AtomicInteger nodesCounter;
    private Tree<AAIServiceTree.AaiRelationship> pathsTree;

    public String getNodeType() {
        return nodeType;
    }

    public ParseNodeAndGetChildrenDataObject setNodeType(String nodeType) {
        this.nodeType = nodeType;
        return this;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public ParseNodeAndGetChildrenDataObject setRequestURL(String requestURL) {
        this.requestURL = requestURL;
        return this;
    }

    public JsonNode getTopLevelJson() {
        return topLevelJson;
    }

    public ParseNodeAndGetChildrenDataObject setTopLevelJson(JsonNode topLevelJson) {
        this.topLevelJson = topLevelJson;
        return this;
    }

    public ConcurrentSkipListSet<AAITreeNode> getNodesAccumulator() {
        return nodesAccumulator;
    }

    public ParseNodeAndGetChildrenDataObject setNodesAccumulator(ConcurrentSkipListSet<AAITreeNode> nodesAccumulator) {
        this.nodesAccumulator = nodesAccumulator;
        return this;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public ParseNodeAndGetChildrenDataObject setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
        return this;
    }

    public ConcurrentLinkedQueue<String> getVisitedNodes() {
        return visitedNodes;
    }

    public ParseNodeAndGetChildrenDataObject setVisitedNodes(ConcurrentLinkedQueue<String> visitedNodes) {
        this.visitedNodes = visitedNodes;
        return this;
    }

    public AtomicInteger getNodesCounter() {
        return nodesCounter;
    }

    public ParseNodeAndGetChildrenDataObject setNodesCounter(AtomicInteger nodesCounter) {
        this.nodesCounter = nodesCounter;
        return this;
    }

    public Tree<AAIServiceTree.AaiRelationship> getPathsTree() {
        return pathsTree;
    }

    public ParseNodeAndGetChildrenDataObject setPathsTree(Tree<AAIServiceTree.AaiRelationship> pathsTree) {
        this.pathsTree = pathsTree;
        return this;
    }



}
