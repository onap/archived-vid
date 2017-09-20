<%@ page import="java.net.URLEncoder"%>
<%@ page import="org.openecomp.portalsdk.core.util.SystemProperties"%>
<%
	// Requests are handled by class ProcessCspController in the EP-SDK-Core library.
	// On login error, that controller returns a model that is a String->String map.
	
	// CSP property is defined in fusion.properties file
	final String cspLoginUrl = SystemProperties.getProperty(SystemProperties.CSP_LOGIN_URL);
	// Name is defined by app; do not throw if missing
	final String appDisplayName = SystemProperties.containsProperty(SystemProperties.APP_DISPLAY_NAME)
			? SystemProperties.getProperty(SystemProperties.APP_DISPLAY_NAME)
			: SystemProperties.APP_DISPLAY_NAME;
	// Build login-link URL using parameters and/or system properties 
	String returnUrl = request.getParameter("returnUrl");
	if (returnUrl == null) {
		final String cspPage = "doLogin";
		if (SystemProperties.containsProperty(SystemProperties.APP_BASE_URL)) {
			// Use property with the application URL; e.g., WebJunction
			String appUrl = SystemProperties.getProperty(SystemProperties.APP_BASE_URL);
			returnUrl = appUrl + (appUrl.endsWith("/") ? "" : "/") + cspPage;
		} else {
			// Use server info; incorrect for sites behind WebJunction.
			returnUrl = (request.isSecure() ? "https://" : "http://") + request.getServerName() + ":"
					+ request.getServerPort() + request.getContextPath() + "/" + cspPage;
		}
	} else {
		// Request has a parameter with the return URL
		returnUrl = URLEncoder.encode(returnUrl, "UTF-8");
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<style>
html {
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

body {
	padding-top: 15px;
}

.logo {
	position: fixed;
	left: 15px;
	top: 15px;
	z-index: -1;
}

.loginError {
	font-size: 18px;
	color: red;
	text-align: center;
}

.login {
	font-size: 16px;
	display: block;
	margin-left: auto;
	margin-right: auto;
	text-align: center;
	width: 100%;
}

.login a {
	font-size: 16px;
}

.terms {
	font-size: 10px;
	text-align: center;
	margin-left: auto;
	margin-right: auto;
}

.terms a {
	font-size: 10px;
	text-align: center;
	margin-left: auto;
	margin-right: auto;
}
</style>
</head>
<body>
	<!-- AT&T Logo -->
	<div class="logo">
		<img src="static/fusion/images/logo_att_header.jpg" alt="AT&T" />
	</div>
	<div class="login">
		<img src="static/fusion/images/ecomp-login-550x360.jpg" />
		<h2>
			<%=appDisplayName%>
		</h2>
		<a href="<%=cspLoginUrl%><%=returnUrl%>">Click here to login</a>
	</div>
	<br />
	<br />
	<br />
	<div class="loginError">${model.error}</div>
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<div id="footer">
		<div class="terms">
			Warning: This system is restricted to AT&amp;T authorized users for
			business purposes. Unauthorized access is a violation of the law. <br />
			This service may be monitored for administrative and security
			reasons. By proceeding, you consent to this monitoring.
		</div>
		<p>
		<div class="terms">
			<a target="_top" href="http://www.att.com/terms/">Terms and
				Conditions</a> | <a target="_top" href="http://www.att.com/privacy/">Privacy
				Policy</a> <br> &copy; 2017 AT&amp;T. All rights reserved.
		</div>
	</div>
</body>
</html>
