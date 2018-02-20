/*
 * ============LICENSE_START==========================================
 * ONAP Portal SDK
 * ===================================================================
 * Copyright © 2017 AT&T Intellectual Property. All rights reserved.
 * ===================================================================
 *
 * Unless otherwise specified, all software contained herein is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Unless otherwise specified, all documentation contained herein is licensed
 * under the Creative Commons License, Attribution 4.0 Intl. (the "License");
 * you may not use this documentation except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             https://creativecommons.org/licenses/by/4.0/
 *
 * Unless required by applicable law or agreed to in writing, documentation
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============LICENSE_END============================================
 *
 * ECOMP is a trademark and service mark of AT&T Intellectual Property.
 */

package org.onap.portalapp.login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onap.portalsdk.core.auth.LoginStrategy;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.onboarding.exception.CipherUtilException;
import org.onap.portalsdk.core.onboarding.exception.PortalAPIException;
import org.onap.portalsdk.core.onboarding.util.CipherUtil;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.web.servlet.ModelAndView;

/**
 * Implements basic single-signon login strategy for open-source applications
 * when users start at Portal. Extracts an encrypted user ID sent by Portal.
 */
public class LoginStrategyImpl extends LoginStrategy {

	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(LoginStrategyImpl.class);

	/**
	 * login for open source is same as external login in the non-open-source
	 * version.
	 */
	@Override
	public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return doExternalLogin(request, response);
	}

	@Override
	public String getUserId(HttpServletRequest request) throws PortalAPIException {
		// Check ECOMP Portal cookie
		Cookie ep = getCookie(request, EP_SERVICE);
		if (ep == null) {
			logger.debug(EELFLoggerDelegate.debugLogger, "getUserId: no EP_SERVICE cookie, returning null");
			return null;
		}

		String userid = null;
		try {
			userid = getUserIdFromCookie(request);
		} catch (Exception e) {
			logger.error(EELFLoggerDelegate.errorLogger, "getUserId failed", e);
		}
		return userid;
	}

	/**
	 * Searches the request for the user-ID cookie and decrypts the value using a
	 * key configured in properties
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return User ID
	 * @throws CipherUtilException
	 *             On any failure to decrypt
	 */
	private String getUserIdFromCookie(HttpServletRequest request) throws CipherUtilException {
		String userId = "";
		Cookie userIdCookie = getCookie(request, USER_ID);
		if (userIdCookie != null) {
			final String cookieValue = userIdCookie.getValue();
			if (!SystemProperties.containsProperty(SystemProperties.Decryption_Key))
				throw new IllegalStateException("Failed to find property " + SystemProperties.Decryption_Key);
			final String decryptionKey = SystemProperties.getProperty(SystemProperties.Decryption_Key);
			userId = CipherUtil.decrypt(cookieValue, decryptionKey);
			logger.debug(EELFLoggerDelegate.debugLogger, "getUserIdFromCookie: decrypted as {}", userId);
		}
		return userId;
	}

	/**
	 * Searches the request for the named cookie.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param cookieName
	 *            Name of desired cookie
	 * @return Cookie if found; otherwise null.
	 */
	private Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(cookieName))
					return cookie;
		return null;
	}

}
