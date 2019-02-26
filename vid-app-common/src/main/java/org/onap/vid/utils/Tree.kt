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

package org.onap.vid.utils

data class Node<T>(val value:T, val children:MutableMap<T, Node<T>> = hashMapOf())

data class Tree<T>(private val root:Node<T>) {

    constructor(value: T) : this(Node(value))

    fun getRootValue():T {
        return root.value;
    }

    fun addPath(vararg path: T) {
        addPath(path.asList())
    }

    fun addPath(path:Collection<T>) {
        var currentNode = root
        path.forEach {
            currentNode = currentNode.children.getOrPut(it) {Node(it)}
        }
    }

    fun getSubTree(vararg path: T): Tree<T>? {
        return getSubTree(path.asList())
    }

    fun getSubTree(path:Collection<T>): Tree<T>? {
        var currentNode:Node<T> = root
        path.forEach {
            currentNode = currentNode.children[it] ?: return null
        }
        return Tree(currentNode)
    }

    fun isPathExist(vararg path: T): Boolean {
        return isPathExist(path.asList())
    }

    fun isPathExist(path:Collection<T>): Boolean {
        return getSubTree(path)!=null
    }
}

