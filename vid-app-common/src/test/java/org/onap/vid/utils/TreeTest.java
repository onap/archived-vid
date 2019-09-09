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

package org.onap.vid.utils;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.testng.Assert.*;

public class TreeTest {

    @NotNull
    protected Tree<String> buildTreeForTest() {
        Tree<String> tree = new Tree<>("a");
        tree.addPath("b","c","d");
        tree.addPath("b","cc","dd");
        tree.addPath("1","2","dd");
        return tree;
    }

    @DataProvider
    public static Object[][] pathsToFind() {
        return new Object[][]{
                {ImmutableList.of("b","c","d"), true},
                {ImmutableList.of("b","c"), true},
                {ImmutableList.of("b","cc","dd"), true},
                {ImmutableList.of("b","cc","d"), false},
                {ImmutableList.of("1","2","dd"), true},
                {ImmutableList.of("b"), true},
                {ImmutableList.of("c"), false},
                {ImmutableList.of("z", "z", "z", "z", "z"), false},
        };
    }

    @Test(dataProvider="pathsToFind")
    public void whenBuildTree_nodesFoundsInRoute(List<String> path, boolean isFound) {
        Tree<String> tree = buildTreeForTest();
        assertEquals(isFound, tree.isPathExist(path));
    }

    @Test(dataProvider="pathsToFind")
    public void whenBuildTree_subTreeGetRight(List<String> path, boolean isFound) {
        Tree<String> tree = buildTreeForTest();
        if (isFound) {
            assertNotNull(tree.getSubTree(path));
        }
        else {
            assertNull(tree.getSubTree(path));
        }
    }

    @Test
    public void whenBuildTree_getSubTreeAsExpected() {
        Tree<String> tree = buildTreeForTest();
        Tree<String> subTree = tree.getSubTree("b","c");
        assertEquals(subTree.getRootValue(), "c");
        assertTrue(subTree.isPathExist("d"));
        assertFalse(subTree.isPathExist("b","c","d"));
    }

    @Test
    public void getChildrenDepthTest() {
        Tree<String> tree = buildTreeForTest();
        assertEquals(3, tree.getChildrenDepth());
        Tree<String> subTree = tree.getSubTree("b");
        assertEquals(2, subTree.getChildrenDepth());
    }
}
