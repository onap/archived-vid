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

package org.onap.vid.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import javax.servlet.ServletContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class FeaturesTogglingConfigurationTest {

    private final String pathFromServletContext = "/path/from/mockedServletContext";
    private FeaturesTogglingConfiguration testSubject;
    private ServletContext servletContextMock;

    @BeforeTest
    public void initTestSubject() {
        testSubject = new FeaturesTogglingConfiguration();

        servletContextMock = mock(ServletContext.class);
        when(servletContextMock.getRealPath(anyString())).thenReturn(pathFromServletContext);
    }

    @Test
    public void filenameThroughContext_shouldGetPathThroughServletContext() {
        File result = testSubject.file("/WEB_INF/conf/", "filename", servletContextMock);
        assertThat(result.getPath(), is(localize(pathFromServletContext)));
    }

    @Test
    public void filenameIsAbsolutePath_shouldOnlyFilename() {
        File result = testSubject.file("file:///../conf/dev/", "/filename", servletContextMock);
        assertThat(result.getPath(), is(localize("/filename")));
    }

    @Test
    public void pathIsAbsolute_shouldJoinPathAndName() {
        File result = testSubject.file("file://" + "/path/to/WEB-INF/", "filename", servletContextMock);
        assertThat(result.getPath(), is(localize("file:/path/to/WEB-INF/filename")));
    }

    private String localize(String path) {
        return path.replace("/", File.separator);
    }
}
