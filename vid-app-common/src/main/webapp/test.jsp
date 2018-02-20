<html>
<head><title>Test VID Properties related to MSO </title></head>
<!-- This is a temporary test page. It will be removed from source control -->
<body>
<%@ page import="org.onap.portalsdk.core.util.SystemProperties" %>
<%@ page import="org.onap.vid.controller.MsoController" %>
<%@ page import="org.onap.vid.mso.rest.Request" %>
<%@ page import="org.onap.vid.mso.rest.RequestDetails" %>
<%@ page import="org.onap.vid.mso.rest.RelatedModel" %>
<%@ page import="org.onap.vid.domain.mso.SubscriberInfo" %>
<%@ page import="org.onap.vid.domain.mso.Response" %>
<%@ page import="org.onap.vid.domain.mso.ModelInfo" %>
<%@ page import="org.onap.vid.domain.mso.RequestInfo" %>
<%@ page import="org.onap.vid.domain.mso.CloudConfiguration" %>
<%@ page import="org.onap.vid.mso.MsoProperties" %>
<%@ page import="java.net.URI" %>
<%@ page import="com.sun.jersey.api.client.ClientResponse" %>
  <%
  String url = SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL);
  String max_polls = SystemProperties.getProperty(MsoProperties.MSO_MAX_POLLS);
  String max_polling_interval_msecs = SystemProperties.getProperty(MsoProperties.MSO_POLLING_INTERVAL_MSECS);
  %>
  	<h2>VID properties related to MSO:</h2>
      <h3>MSO server URL:</h3><p>"<%= url %>"</p>
      <h3>MSO max number of polls:</h3><p>"<%= max_polls %>"</p>
      <h3>MSO polling interval (msecs):</h3><p>"<%= max_polling_interval_msecs %>"</p>
  <a href="<%= request.getRequestURI() %>"><h3>Try Again</h3></a>
</body>
</html>
