package org.onap.vid.services

import com.google.common.collect.ImmutableMap
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails.UserParamNameAndValue
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class UserParamsConvertorTest {

    @DataProvider
    fun expectedAggregatedParams(): Array<Array<Any?>>? {
        return arrayOf(
            arrayOf<Any?>(
                mapOf("a" to "b", "c" to "d"),
                listOf(UserParamNameAndValue("e", "f"), UserParamNameAndValue("g", "h")),
                mapOf("c" to "d", "a" to "b", "e" to "f", "g" to "h")
            ),
            arrayOf(
                ImmutableMap.of("a" to "b", "c" to "g"),
                listOf(UserParamNameAndValue("c", "d") , UserParamNameAndValue("e", "f")),
                mapOf("c" to "d", "a" to "b", "e" to "f")
            ),
            arrayOf(
                ImmutableMap.of<Any, Any>(),
                listOf(UserParamNameAndValue("c", "d"), UserParamNameAndValue("e", "f")),
                mapOf("c" to "d", "e" to "f")
            ),
            arrayOf(
                ImmutableMap.of("a" to "b", "c" to "d"),
                emptyList<UserParamNameAndValue>(),
                mapOf("a" to "b", "c" to "g")
            ),
            arrayOf<Any?>(
                emptyMap<String,String>(),
                emptyList<UserParamNameAndValue>(),
                emptyMap<String,String>()
            ),
            arrayOf(
                null,
                emptyList<UserParamNameAndValue>(),
                emptyMap<String,String>()
            ),
            arrayOf<Any?>(
                emptyMap<String,String>(),
                null,
                emptyMap<String,String>()
            )
        )
    }

    @Test(dataProvider = "expectedAggregatedParams")
    fun testAggregateInstanceParamsAndSuppFile(instanceParams: Map<String, String>?, suppParams: List<UserParamNameAndValue>?, expected: Map<String, String>) {
        val aggParams: Map<String, String> = UserParamsConvertor(instanceParams, suppParams).params
        MatcherAssert.assertThat("Aggregated params are not as expected", aggParams, equalTo(expected))
    }
}