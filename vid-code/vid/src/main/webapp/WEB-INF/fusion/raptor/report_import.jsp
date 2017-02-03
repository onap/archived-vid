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
	<title>Import</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/raptor.css">


<br><br>

<form name="forma" action="<%= AppUtils.getBaseURL() %>" method="post">
	<input type="hidden" name="action" value="raptor">
	<input type="hidden" name="<%= AppConstants.RI_ACTION %>" value="report.import.save">

<table width=94% class="tableBorder" border=0 cellspacing=1 cellpadding=3 align=center>
	<tr class=rbg1>
		<td>
			<b class=rtableheader><%= Globals.getBaseTitle() %> > IMPORT REPORT XML</b>
		</td>
	</tr>
	<tr class=rbg3>
		<td align="center">
			<font class=rtabletext>
			<textarea name="reportXML" cols="62" rows="16"></textarea>
			</font>
		</td>
	</tr>
	<tr>
		<td align="center">
			<br>
			<input type="Submit" class=button value="Import" onClick="if(document.forma.reportXML.value=='') { alert('Please provide report XML.'); return false; }">
		</td>
	</tr>
</table>	
	
	

</form>

<jsp:include page="disclaimer.jsp" flush="true" />

</body>
</html>

<%!	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } %>

