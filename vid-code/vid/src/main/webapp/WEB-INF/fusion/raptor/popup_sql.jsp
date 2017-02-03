<%--
  ================================================================================
  eCOMP Portal SDK
  ================================================================================
  Copyright (C) 2017 AT&T Intellectual Property
  ================================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ================================================================================
  --%>
<%@ page import="java.util.*" %>

<%@ page import="org.openecomp.portalsdk.analytics.system.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.*" %>

<html>
<head>
	<title><%= nvl((String) request.getAttribute(AppConstants.RI_PAGE_TITLE)) %></title>
	<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/raptor.css">
</head>
<body>
<table width="94%" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 width="100%" valign="Middle">
			<b class=rtableheader><%= nvl((String) request.getAttribute(AppConstants.RI_PAGE_SUBTITLE), nvl((String) request.getAttribute(AppConstants.RI_PAGE_TITLE))) %></b>
		</td>
	</tr>
	<tr>
		<td class=rbg2<%= nvl((String) request.getAttribute("msg_align")) %>>
			<font class=rtabletext><%= nvl((String) request.getAttribute(AppConstants.RI_FORMATTED_SQL)) %></font>
		</td>
	</tr>
	<tr>
		<td align="center">
			<br>
			<input type="Submit" class=rsmallbutton value="Close" onClick="window.close();">
		</td>
	</tr>
</table>
<br>
</body>
</html>

<%!	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } %>

