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

