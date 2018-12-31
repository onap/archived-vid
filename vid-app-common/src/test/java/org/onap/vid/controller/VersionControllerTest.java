package org.onap.vid.controller;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class VersionControllerTest {

    @DataProvider
    public static Object[][] majorVersionContainer() {
        return new Object[][]{
                {"features.properties", "1.0.2000", "features.properties.2000"},
                {"", "1.0.2000", ".2000"},
                {"kuku", "1.0.2000", "kuku.2000"},
                {"/kuku", "1.0.2000", "kuku.2000"},
                {"1810p.features.properties", "1.0.2000", "1810p.2000"},
                {"/opt/app/dev.features.properties", "1.0.2000", "dev.2000"},
                {"foo", "2000", "foo.2000"},
        };
    }

    final VersionController versionController = new VersionController(null);

    @Test(dataProvider = "majorVersionContainer")
    public void testGetDisplayVersion(String majorVersionContainer, String buildNumberContainer, String expected) {
        assertThat(versionController.getDisplayVersion(majorVersionContainer, buildNumberContainer), is(expected));
    }
}
