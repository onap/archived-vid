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
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.runtime.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.error.UserAccessException"%>
<%@ page import="org.openecomp.portalsdk.analytics.error.RaptorException"%>
<%@ page import="org.openecomp.portalsdk.analytics.error.UserDefinedException"%>
<%@ page isErrorPage="true" %>


<% java.lang.Exception ex = (Exception) request.getAttribute(AppConstants.RI_EXCEPTION); %>
<%	boolean showEditLink = false;
	if(AppUtils.getRequestNvlValue(request, "r_action").equals("report.run")) {
		ReportRuntime rr = (ReportRuntime) request.getSession().getAttribute(AppConstants.SI_REPORT_RUNTIME);
		if(rr!=null)
			try {
				rr.checkUserWriteAccess(request);
				showEditLink = true;
			} catch(Exception e) {}
	}	// if
%>



<html>

<head>
	<meta http-equiv="Content-Language" content="en-us">
	<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
	<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/raptor.css">
	<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/ral.css">
	<title>Application Error</title>
	</head>

<body bgcolor="#FFFFFF">
<%-- jsp:include page="custom_header_include.jsp" flush="true" /--%>

<form name="forma" action="raptor.htm" method="post">
	<input type="hidden" name="action" value="raptor">
	<input type="hidden" name="r_action" value="report.edit">
	<input type="hidden" name="c_master" value="<%= AppUtils.getRequestNvlValue(request, "c_master") %>">
	<input type="hidden" name="source_page" value="report_run">

<br>
<table class="mTAB" width="94%"  class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr class=rbg1>
		<td width="90%" class=rtext2 nowrap>
<% if(showEditLink) { %>
			<input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>pen_paper.gif" alt="Edit report" width="12" height="12">
<% } %>
			<b class=rtableheader>Error/User-Alert Message:</b>
		</td>
	</tr>
<% if(ex!=null) { %>
    <% if(ex instanceof org.openecomp.portalsdk.analytics.error.RaptorSchedularException)  { %>
     <% if(AppUtils.isAdminUser(request)) { %>
	<tr class=rowalt1>
		<td width="90%" class=rtext2 nowrap>
			<font class=rtabletext><b>Exception Class: </b><%= (ex!=null && ex instanceof org.openecomp.portalsdk.analytics.error.RaptorSchedularException)?ex.getClass().toString():"" %></font>
		</td>
	</tr>
	<% } %>
	<tr class=rowalt2>
		<td width="90%" class=rtext2 nowrap><font class=rtabletext><b>Message:</b>&nbsp;<%= (ex!=null && ex instanceof org.openecomp.portalsdk.analytics.error.RaptorSchedularException)?ex.getMessage():"" %></font>
		</td>
	</tr>
	<% }  %>
		<% if(ex!=null) ex.printStackTrace(); %>
			<%		if(AppUtils.isAdminUser(request)) {
					if ((ex instanceof org.openecomp.portalsdk.analytics.error.ReportSQLException)||
						(request.getAttribute("c_error_sql")!=null && !((String) request.getAttribute("c_error_sql")).trim().equals(""))) { 
						String sql = "";
						if(ex instanceof org.openecomp.portalsdk.analytics.error.ReportSQLException)
							sql = ((org.openecomp.portalsdk.analytics.error.ReportSQLException) ex).getReportSQL(); 
						else
							sql = (String) request.getAttribute("c_error_sql"); %>
						<% if (sql!=null && sql.length() > 0) { %>	
						<tr class=rowalt1>
							<td width="90%">
								<b class=rtabletext>SQL Execution Error:</b>
							</td>
						</tr>
						<tr class=rowalt2>
							<td width="90%">
								<font class=rtabletext><%= sql %></font>
							</td>
						</tr>
			<% 			request.setAttribute("c_error_sql", sql);
				%>
				        <% }  %>
				<tr class=rowalt1>
					<td width="90%">
						<b class=rtabletext>Error Message:</b><br>
						<font class=rtabletext><%= AppUtils.getRequestNvlValue(request, "error_extra_msg") %><%= ex.getMessage() %></font>
					</td>
				</tr>
			<%		if(request.getAttribute("c_error_url")!=null && !((String) request.getAttribute("c_error_url")).trim().equals("")) { %>
				<tr class=rowalt1>
					<td width="90%">
						<font class=rtabletext>Please <a href="<%= (String) request.getAttribute("c_error_url") %>">click here</a> to edit report definition.</font>
					</td>
				</tr>
			<%		} // if %>
			<% } else { // reportSQLException 
				if (ex instanceof RaptorException) { %>
					<tr class=rowalt1>
					<td width="90%">
						<b class=rtabletext>Error Message:</b><br>
						<font class=rtabletext><%= AppUtils.getRequestNvlValue(request, "error_extra_msg") %><%= ex.getMessage() %></font>
					</td>
				</tr>	                 
			<%} %>
			<% } %>
			<% } else { 
				if (ex instanceof UserAccessException) { %>
					<tr class=rowalt1>
					<td width="90%">
						<b class=rtabletext>Error Message:</b><br>
						<font class=rtabletext><%= AppUtils.getRequestNvlValue(request, "error_extra_msg") %><%= ex.getMessage() %></font>
					</td>
				</tr>					
			<%	} else if (ex instanceof UserDefinedException) { %>
				<tr class=rowalt1>
					<td width="90%">
						<b class=rtabletext>Error Message:</b><br>
						<font class=rtabletext><%= AppUtils.getRequestNvlValue(request, "error_extra_msg") %><%= ex.getMessage() %></font>
					</td>
				</tr>	
			<%	}
			 } %>
				<tr class=rowalt2>
					<td width="90%">
						<font class=rtabletext>** The system administrator has been notified for this error.</font>
					</td>
				</tr>		
<% } else { %>
<% if(exception instanceof org.openecomp.portalsdk.analytics.error.RaptorSchedularException)  { %>
     <% if(AppUtils.isAdminUser(request)) { %>
	<tr class=rowalt1>
	<td width="90%" class=rtext2 nowrap>
		<font class=rtabletext><b>Exception Class: </b><%= (exception!=null && exception instanceof org.openecomp.portalsdk.analytics.error.RaptorSchedularException)?exception.getClass().toString():"" %></font>
	</td>
</tr>
   <% } %>
<tr class=rowalt2>
	<td width="90%" class=rtext2 nowrap><font class=rtabletext><b>Message:</b>&nbsp;<%= (exception!=null && exception instanceof org.openecomp.portalsdk.analytics.error.RaptorSchedularException)?exception.getMessage():"" %></font>
		</td>
		<% if(exception!=null) exception.printStackTrace(); %>
	</tr>
<% } %>
<%		if(AppUtils.isAdminUser(request)) {
		if ((exception instanceof org.openecomp.portalsdk.analytics.error.ReportSQLException)||
			(request.getAttribute("c_error_sql")!=null && !((String) request.getAttribute("c_error_sql")).trim().equals(""))) { 
			String sql = "";
			if(exception instanceof org.openecomp.portalsdk.analytics.error.ReportSQLException)
				sql = ((org.openecomp.portalsdk.analytics.error.ReportSQLException) ex).getReportSQL(); 
			else
				sql = (String) request.getAttribute("c_error_sql"); %>
				       <% if (sql!=null && sql.length() > 0) { %>					
	<tr class=rowalt1>
		<td width="90%">
										<b class=rtabletext>SQL Execution Error:</b>
		</td>
	</tr>
	<tr class=rowalt2>
		<td width="90%">
			<font class=rtabletext><%= sql %></font>
		</td>
	</tr>
<% 			request.setAttribute("c_error_sql", sql);
	%>
				       <% } %> 
	<tr class=rowalt1>
		<td width="90%">
			<b class=rtabletext>Error Message:</b><br>
			<font class=rtabletext><%= AppUtils.getRequestNvlValue(request, "error_extra_msg") %><%= ex.getMessage() %></font>
		</td>
	</tr>
<%		if(request.getAttribute("c_error_url")!=null && !((String) request.getAttribute("c_error_url")).trim().equals("")) { %>
	<tr class=rowalt1>
		<td width="90%">
			<font class=rtabletext>Please <a href="<%= (String) request.getAttribute("c_error_url") %>">click here</a> to edit report definition.</font>
		</td>
	</tr>
<%		} %>
<% } %>
<% } %>
	<tr class=rowalt2>
		<td width="90%">
			<font class=rtabletext>** The system administrator has been notified for this error.</font>
		</td>
	</tr>
<% if(AppUtils.isAdminUser(request))  { %>	
<!-------------------------------------------------------
EXCEPTION [<%= ex!=null? ex.getMessage():"" %>]
<%	if(ex!=null) ex.printStackTrace(new PrintWriter(out)); %>
-------------------------------------------------------->
<% } %>
<% if(exception!=null) exception.printStackTrace(); %>

<%	}	// else 
%>
</table>

</form>

</body>
</html>

