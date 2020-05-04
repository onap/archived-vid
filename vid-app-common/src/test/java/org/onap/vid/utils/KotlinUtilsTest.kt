package org.onap.vid.utils

import org.testng.AssertJUnit.assertEquals
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

internal class KotlinUtilsTest {
    @DataProvider
    fun listsAndPerdicates(): Array<Array<Any>>? {
        return arrayOf(
                arrayOf("stop on second item", listOf("a", "b", "c", "d"), "b", listOf("a", "b")),
                arrayOf("return all of the list", listOf("a", "b", "c", "d"), "z", listOf("a", "b", "c", "d")),
                arrayOf("only first item returns", listOf("a", "b", "c", "d"), "a", listOf("a")),
                arrayOf("returns an empty list", emptyList<String>(), "z", emptyList<String>()))
    }

    @Test(dataProvider = "listsAndPerdicates")
    fun testTakeUntilIncludingReturendValue(desc: String, list: List<String>, predicate: String, expectedResultList: List<String>) {
        assertEquals(desc, expectedResultList, list.takeUntilIncluding { it == predicate })
    }
}
