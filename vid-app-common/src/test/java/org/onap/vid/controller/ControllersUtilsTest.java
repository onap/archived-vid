/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2019 Nokia.
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
package org.onap.vid.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.portalsdk.core.domain.User;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.utils.SystemPropertiesWrapper;

@RunWith(MockitoJUnitRunner.class)
public class ControllersUtilsTest {

    private static final String USER_ATTRIBUTE = "userAttribute";
    @Mock
    private SystemPropertiesWrapper systemPropertiesWrapper;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private HttpServletRequest httpServletRequest;

    @Test
    public void shouldExtractLoginIdAsUserId_fromHttpServletRequest() {
        // GIVEN
        String expectedUserId = "rootUser";
        given(systemPropertiesWrapper.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).willReturn(USER_ATTRIBUTE);
        User user = new User();
        user.setLoginId(expectedUserId);
        given(httpServletRequest.getSession().getAttribute(USER_ATTRIBUTE)).willReturn(user);

        // WHEN
        String loginId = new ControllersUtils(systemPropertiesWrapper).extractUserId(httpServletRequest);

        // THEN
        assertThat(loginId).isEqualTo(expectedUserId);
    }

    @Test
    public void shouldExtractOrgUserIdAsUserId_fromHttpServletRequest_whenLoginIdIsNull() {
        // GIVEN
        String expectedUserId = "secondUser";
        given(systemPropertiesWrapper.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).willReturn(USER_ATTRIBUTE);
        User user = new User();
        user.setOrgUserId(expectedUserId);
        given(httpServletRequest.getSession().getAttribute(USER_ATTRIBUTE)).willReturn(user);

        // WHEN
        String loginId = new ControllersUtils(systemPropertiesWrapper).extractUserId(httpServletRequest);

        // THEN
        assertThat(loginId).isEqualTo(expectedUserId);
    }

    @Test
    public void shouldReturnEmptyString_whenBothLoginIdAndOrgUserIdAreNull() {
        // GIVEN
        given(systemPropertiesWrapper.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).willReturn(USER_ATTRIBUTE);
        given(httpServletRequest.getSession().getAttribute(USER_ATTRIBUTE)).willReturn(new User());

        // WHEN
        String loginId = new ControllersUtils(systemPropertiesWrapper).extractUserId(httpServletRequest);

        // THEN
        assertThat(loginId).isEmpty();
    }

    @Test
    public void shouldReturnEmptyString_whenSessionIsNull() {
        // GIVEN
        given(httpServletRequest.getSession()).willReturn(null);

        // WHEN
        String loginId = new ControllersUtils(systemPropertiesWrapper).extractUserId(httpServletRequest);

        // THEN
        assertThat(loginId).isEmpty();
    }

    @Test
    public void shouldReturnEmptyString_whenUserIsNull() {
        // GIVEN
        given(systemPropertiesWrapper.getProperty(SystemProperties.USER_ATTRIBUTE_NAME)).willReturn(USER_ATTRIBUTE);
        given(httpServletRequest.getSession().getAttribute(USER_ATTRIBUTE)).willReturn(null);

        // WHEN
        String loginId = new ControllersUtils(systemPropertiesWrapper).extractUserId(httpServletRequest);

        // THEN
        assertThat(loginId).isEmpty();
    }
}