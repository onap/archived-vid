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

package org.onap.vid.testUtils;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class RegExMatcher extends TypeSafeMatcher<String> {

    private final String regEx;

    public RegExMatcher(String regEx) {
        this.regEx = regEx;
    }


    @Override
    public void describeTo(Description description) {
        description.appendText("matches regEx="+regEx);
    }


    @Override
    protected boolean matchesSafely(String item) {
        return item.matches(regEx);
    }

    public static RegExMatcher matchesRegEx(final String regEx) {
        return new RegExMatcher(regEx);
    }
}
