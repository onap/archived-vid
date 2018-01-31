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
