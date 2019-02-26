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

package org.onap.vid.aai.util;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.onap.vid.aai.util.CacheProvider.compileKey;
import static org.onap.vid.aai.util.CacheProvider.decompileKey;

public class CacheProviderTest {
    @Test(dataProvider = "aaiClientCompileDecompileKeySameData")
    public void compileDecompileKeySameTest(List<String> args) {
        assertThat(decompileKey(compileKey(args)), is(args.toArray()));
    }

    @Test(dataProvider = "aaiClientCompileDecompileKeyDifferentData")
    public void compileDecompileKeyDifferentTest(List<String> expectedResult, List<String> args) {
        assertThat(decompileKey(compileKey(args)), is(expectedResult.toArray()));
    }

    @DataProvider
    public static Object[][] aaiClientCompileDecompileKeySameData() {
        return new Object[][] {
                {Arrays.asList( "a", "b", "c")},
                {Arrays.asList("a")},
                {Arrays.asList("a!", "@#?b")},
                {Arrays.asList("a", "", "c")}
        };
    }

    @DataProvider
    public static Object[][] aaiClientCompileDecompileKeyDifferentData() {
        return new Object[][] {
                {Arrays.asList("a", "", "c"), Arrays.asList("a", null, "c")}
        };
    }
}
