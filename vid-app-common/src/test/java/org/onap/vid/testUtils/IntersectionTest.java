/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 Nokia. All rights reserved.
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

package org.onap.vid.testUtils;

import org.junit.Test;
import org.onap.vid.utils.Intersection;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.emptyList;
import static org.assertj.core.util.Lists.list;

public class IntersectionTest {

    @Test
    public void testFourArrays() {
        // given
        List<List<String>> input = list(
                list("1", "2"),
                list("2", "3"),
                list("2", "4"),
                list("2", "5")
        );
        // when
        List<String> output = Intersection.of(input);
        // then
        assertThat(output).containsExactlyInAnyOrder("2");

    }

    @Test
    public void testTwoArrays() {
        // given
        List<List<String>> input = list(
                list("1", "2"),
                list("2", "3")
        );
        // when
        List<String> output = Intersection.of(input);
        // then
        assertThat(output).containsExactlyInAnyOrder("2");

    }


    @Test
    public void testNoIntersection() {
        // given
        List<List<String>> input = list(
                list("1", "2"),
                list("3", "4")
        );
        // when
        List<String> output = Intersection.of(input);
        // then
        assertThat(output).isEmpty();

    }

    @Test
    public void testOneArray() {
        // given
        List<List<String>> input = list(list("1", "2"));
        // when
        List<String> output = Intersection.of(input);
        // then
        assertThat(output).containsExactlyInAnyOrder("1", "2");
    }

    @Test
    public void testEmptyInput() {
        // when
        List<String> output = Intersection.of(emptyList());
        // then
        assertThat(output).isEmpty();
    }

    @Test
    public void shouldIgnoreRepetitions() {
        // when
        List<String> output = Intersection.of(list(
                list("1", "1"),
                list("1", "1")
        ));
        // then
        assertThat(output).containsExactly("1");
    }
}
