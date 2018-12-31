package org.onap.vid.model

interface MinimalNode {
    val invariantUuid: String
    val uuid: String
    val version: String
    val name: String
}