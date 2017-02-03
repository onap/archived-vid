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
<%@ page import="org.openecomp.portalsdk.analytics.model.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.base.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.runtime.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.controller.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.*" %>

<%@ page errorPage="error_page.jsp" %>



<%	ReportDefinition rdef = (ReportDefinition) request.getSession().getAttribute(AppConstants.SI_REPORT_DEFINITION);
	
	String reportID   = rdef.getReportID();
	

    
	String dbInfo = null;
	dbInfo = rdef.getDBInfo();
	int sessionflag = 0;
	if(dbInfo == null  || dbInfo.length() == 0) {
	   dbInfo = (String) session.getAttribute("remoteDB");
       sessionflag = 1; 
	}
       session.setAttribute("remoteDB", dbInfo);
	if((dbInfo == null) && (request.getParameter("dataSource")!=null)) 
	   session.setAttribute("remoteDB", request.getParameter("dataSource"));

	StringBuffer title = new StringBuffer("");
	title.append(Globals.getBaseTitle()+" > "+(reportID.equals("-1")?"Create Report":"Schedule Report"));
	title.append(" > "+rdef.getReportName());
	
	boolean isCrossTab = rdef.getReportType().equals(AppConstants.RT_CROSSTAB);
	boolean isSQLBased = rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED); 
%>
<!--  Set All variable to request, to enable included jsp to access it -->	
<%
	request.setAttribute(AppConstants.SI_REPORT_DEFINITION,rdef);
%>	

 <jsp:include page="header.jsp" flush="true" />
 <!-- <link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/raptor.css">-->
<script language="JavaScript" src="<%= AppUtils.getBaseFolderURL() %>js/raptor.js"></script>
 

<jsp:include page="error_include.jsp" flush="true" />

<table width="100%" >
<tr><td id="folderTreeContainer" width="1%" height='300' valign='top' ><table>
		<!--<jsp:include page="tree/folderNav.jsp" flush="true" >	
        	<jsp:param name="doCollapseTree" value="Y" />
       	</jsp:include>-->
	</table>
</td>
<td id="searchContentContainer" valign='top' >
<div id="contentDiv" style="position:relative;overflow:auto;">
<form name="forma" action="<%= AppUtils.getBaseURL() %>" method="post">
	<input type="hidden" name="action" value="raptor">
	<input type="hidden" name="<%= AppConstants.RI_ACTION %>" value="report.schedule.submit_from_search">
	<input type="hidden" name="<%= AppConstants.RI_REPORT_ID %>" value="<%= reportID %>">
	<input type="hidden" name="<%= AppConstants.RI_DETAIL_ID %>" value="<%= AppUtils.getRequestNvlValue(request, AppConstants.RI_DETAIL_ID) %>">
	<input type="hidden" name="<%= AppConstants.RI_GO_TO_STEP %>" value="">
	<input type="hidden" name="<%= AppConstants.RI_WIZARD_ACTION %>" value="<%= AppConstants.WA_BACK %>">
	<input type="hidden" name="<%= AppConstants.RI_SCHEDULE_ID %>" value="<%= AppUtils.getRequestNvlValue(request, AppConstants.RI_SCHEDULE_ID) %>">


<table width="94%" class="tableBorder" cellspacing="0" cellpadding="0" align=center>
<tr><td>
	<table width="100%" border="0" cellspacing="0" cellpadding="3">
		<% if(request.getAttribute("message")!=null) { %>
		<tr >
			<td valign="Middle"><b class=rerrortext><%= (String) request.getAttribute("message") %></b></td>
		</tr>
		<% } %>
		<tr class=rbg1>
			<td valign="Middle"><b class=rtableheader><%= title.toString() %></b></td>
		</tr>
		
	</table>
</td></tr><tr><td>
		 <jsp:include page="wizard_adhoc_schedule.jsp"/>
</td></tr><tr><td>
	<table width="100%" border="0" cellspacing="1" cellpadding="3">
		<tr>
			<td width="10%" align="right">
				&nbsp;
			</td>
			<td align=center>
					<input type=submit class="button" border="0" value="Submit" width="71" height="28" onClick="if(! dataValidate()) return false;  document.forma.<%= AppConstants.RI_ACTION %>.value='report.schedule.submit_from_search';">
					<%--<input type=submit class="button" border="0" value="Report Search Page" width="71" height="28" onClick="document.forma.<%= AppConstants.RI_ACTION %>.value='report.search.public.container';">--%>
					
			</td>
		</tr>
	</table>	
</td></tr>
</table>	
</form>

 <jsp:include page="footer.jsp" flush="true" />
</div>
</td>
</tr>
</table>
<%--<jsp:include page="disclaimer.jsp" flush="true" />--%>
<script>
	//document.getElementById('loadingMessageDiv').style.display='none';
    //adding the bread crumb on the folder tree
	if (window.parent && window.parent.addBreadCrumb)
		window.parent.addBreadCrumb(true, "000<%=rdef.getReportID()%>");
	//displayTree("000<%=rdef.getReportID()%>");
	<%
		String isTreeHidden = "true";
		if(request.getSession().getAttribute("isTreeHidden") != null && ((String)request.getSession().getAttribute("isTreeHidden")).equals("true")){
			isTreeHidden = "true";
		}else{
			isTreeHidden = "false";
		}
	%>
	//if ("<%=isTreeHidden%>" == "true"){
	//	hideTree();
	//}
	//document.getElementById('contentDiv').style.height=document.body.offsetHeight - 50;
</script>
<%!	private String HTMLEncode(String value) {
		StringBuffer sb = new StringBuffer(value);
		
		for(int i=0; i<sb.length(); i++)
			if(sb.charAt(i)=='<')
				sb.replace(i, i+1, "&lt;");
			else if(sb.charAt(i)=='>')
				sb.replace(i, i+1, "&gt;");
			else if(sb.charAt(i)=='"')
				sb.replace(i, i+1, "&quot;");
   		
		return sb.toString();
	}   // HTMLEncode
	
	private String clearSpaces(String value) {
		StringBuffer sb = new StringBuffer(value);
		
		for(int i=0; i<sb.length(); i++)
			if(sb.charAt(i)==' ')
				sb.replace(i, i+1, "&nbsp;");
   		
		return sb.toString();
	}   // clearSpaces
	
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } %>

 
