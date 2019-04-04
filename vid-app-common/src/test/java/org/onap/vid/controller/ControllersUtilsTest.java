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
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.portalsdk.core.domain.User;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.model.ExceptionResponse;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @Test
    public void shouldCreateResponseEntity_fromGivenException() {
        // GIVEN
        EELFLoggerDelegate eelfLoggerDelegate = mock(EELFLoggerDelegate.class);
        Response response = mock(Response.class);
        given(response.getStatus()).willReturn(HttpStatus.OK.value());
        WebApplicationException webApplicationException = new WebApplicationException("ErrorMessage", response);

        // WHEN
        ResponseEntity responseEntity = ControllersUtils
            .handleWebApplicationException(webApplicationException, eelfLoggerDelegate);

        // THEN
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody())
            .isInstanceOfSatisfying(ExceptionResponse.class,
                er -> assertThat(er.getMessage()).isEqualTo("ErrorMessage (Request id: null)"));
        then(eelfLoggerDelegate).should()
            .error(EELFLoggerDelegate.errorLogger, "{}: {}", "handleWebApplicationException",
                ExceptionUtils.getMessage(webApplicationException), webApplicationException);
    }
}