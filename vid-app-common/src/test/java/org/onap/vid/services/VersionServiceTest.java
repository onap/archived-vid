package org.onap.vid.services;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletContext;
import org.jetbrains.annotations.NotNull;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.model.VersionAndFeatures;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@ContextConfiguration(classes = {SystemProperties.class})
public class VersionServiceTest extends AbstractTestNGSpringContextTests {

    private static final String VERSION_FILE_PATH = "/app/vid/scripts/constants/version.json";
    @Mock
    ServletContext servletContext;

    VersionService versionService;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void resetMocks() {
        Mockito.reset(servletContext);
        versionService = new VersionService(servletContext);
    }

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

    @Test(dataProvider = "majorVersionContainer")
    public void testGetDisplayVersion(String majorVersionContainer, String buildNumberContainer, String expected) {
        assertThat(versionService.buildDisplayVersion(majorVersionContainer, buildNumberContainer), is(expected));
    }

    @Test
    public void testReadFeatureSet() {
        assertEquals("1902.features.properties", versionService.readFeatureSet());
    }

    @Test
    public void whenReadBuildNumber_thenTheRightBuildIsReturn_andReadOnlyOnce() throws MalformedURLException {
        mockForVersionFile();
        assertEquals("1.0.151", versionService.retrieveBuildNumber());
        //second call shall not read resource
        assertEquals("1.0.151", versionService.retrieveBuildNumber());
        verify(servletContext).getResource(any());
    }

    private void mockForVersionFile() throws MalformedURLException {
        URL versionFileExample = this.getClass().getResource("/version.example.json");
        when(servletContext.getResource(eq(VERSION_FILE_PATH))).thenReturn(versionFileExample);
    }

    @NotNull
    protected VersionAndFeatures retrieveAndAssertVersionWithGoodResult() throws MalformedURLException {
        mockForVersionFile();
        VersionAndFeatures expected = new VersionAndFeatures("1902.features.properties", "1.0.151", "1902.151");
        assertEquals(expected, versionService.retrieveVersionAndFeatures());
        return expected;
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "abc")
    public void whenExceptionThrownDuringGetBuildNumber_thenExceptionIsThrown() throws MalformedURLException {
        when(servletContext.getResource(any())).thenThrow(new RuntimeException("abc"));
        versionService.retrieveBuildNumber();
    }

    @Test
    public void whenExceptionThrownDuringVersionAndFeatures_thenUnknownIsReturn() throws MalformedURLException {
        //exception is thrown during retrieveVersionAndFeatures, so expect to "unknown" result
        when(servletContext.getResource(eq(VERSION_FILE_PATH))).thenThrow(new RuntimeException());
        assertEquals(VersionAndFeatures.Companion.getUnknown(), versionService.retrieveVersionAndFeatures());

        //retrieveVersionAndFeatures going smoothly, so expecting to good result
        retrieveAndAssertVersionWithGoodResult();
    }


    @Test
    public void whenRetrieveVersionAndFeatures_expectedValuesReturn_andExecuteOnce() throws MalformedURLException {
        VersionAndFeatures expected = retrieveAndAssertVersionWithGoodResult();
        //second call shall not read resource
        assertEquals(expected, versionService.retrieveVersionAndFeatures());
        verify(servletContext).getResource(eq(VERSION_FILE_PATH));
    }
}
