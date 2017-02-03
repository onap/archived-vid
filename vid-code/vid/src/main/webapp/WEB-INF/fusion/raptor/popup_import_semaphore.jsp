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

<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.base.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.*" %>

<%	ArrayList importedList = (ArrayList) request.getAttribute(AppConstants.RI_DATA_SET); %>

<html>
<head>
	<title>Advanced Display Formatting</title>
	<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/raptor.css">
	
<script language=JavaScript>
<!--
	function updateOpenerList() {
<%	if(importedList!=null&&importedList.size()>0) 
		for(Iterator iter=importedList.iterator(); iter.hasNext(); ) { 
			IdNameValue value = (IdNameValue) iter.next(); %>
			window.opener.updateSemaphoreList("<%= value.getId() %>", "<%= value.getName() %>");
<%		} %>
	}	// updateOpenerList
//-->
</script>
	
</head>
<body onLoad="updateOpenerList()">
<br>

<table width=94% class="tableBorder" border=0 cellspacing=1 align=center>
	<tr class=rbg1>
		<td valign="middle" height="24"><b class=rtableheader>&nbsp;Advanced Display Formatting Import</b></td>
	</tr>
	<tr>
		<td class=rbg3 align="center" valign="middle" height="100"><font class=rtabletext>
<%	if(importedList!=null&&importedList.size()>0) { %>
			<%= importedList.size() %> Advanced Display Formattings successfully imported.
<%	} else { %>
			The selected report does not have Advanced Display Formattings <br>
			defined. No Advanced Display Formattings were imported.
<%	} %>
			</font></td>
	</tr>
	<tr>
		<td colspan="10" align="center">
			<br>
			<input type="Button" class=button value="Close" onClick="window.close();">
		</td>
	</tr>
</table>

</form>

</body>
</html>

<%!	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } %>

