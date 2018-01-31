package org.onap.portalapp.login;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openecomp.portalsdk.core.auth.LoginStrategy;
import org.openecomp.portalsdk.core.onboarding.exception.PortalAPIException;
import org.openecomp.portalsdk.core.onboarding.util.CipherUtil;
import org.openecomp.portalsdk.core.onboarding.util.PortalApiConstants;
import org.openecomp.portalsdk.core.onboarding.util.PortalApiProperties;
import org.springframework.web.servlet.ModelAndView;

public class LoginStrategyImpl extends LoginStrategy {
	
	@Override
	public ModelAndView doLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//'login' for opensource is same as 'external' login.
		return doExternalLogin(request, response);
	}
	
	@Override
	public String getUserId(HttpServletRequest request) throws PortalAPIException {
		// Check ECOMP Portal cookie
		if (!isLoginCookieExist(request))
			return null;

		String userid = null;
		try {
			userid = getUserIdFromCookie(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userid;	
	}

	private static String getUserIdFromCookie(HttpServletRequest request) throws Exception {
		String userId = "";
		Cookie[] cookies = request.getCookies();
		Cookie userIdcookie = null;
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(USER_ID))
					userIdcookie = cookie;
		if(userIdcookie!=null){
			userId = CipherUtil.decrypt(userIdcookie.getValue());
		}
		return userId;
	
	}
	
	private static boolean isLoginCookieExist(HttpServletRequest request) {
		Cookie ep = getCookie(request, EP_SERVICE);
		return (ep != null);
	}
	
	private static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(cookieName))
					return cookie;

		return null;
	}

	
}
