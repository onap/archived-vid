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

<%@ page import="org.openecomp.portalsdk.analytics.model.definition.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.*" %>

<%	Exception ex = (Exception) request.getAttribute(AppConstants.RI_EXCEPTION);
	DataSet   ds = (DataSet)   request.getAttribute(AppConstants.RI_DATA_SET); %>

<html>
<head>
<title>SQL Statement Test Run</title>
<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/raptor.css">
<script language="JavaScript">
<!--
function setOpenerConfirm() {
<%	if(AppUtils.getRequestNvlValue(request, AppConstants.RI_CHK_FIELD_SQL).equals("Y")&&ex==null) { %>
	window.opener.setVerifyFlag();
<%	} %>
}	// setOpenerConfirm
//-->
</script>
</head>
<body onLoad="setOpenerConfirm()">
<table class="mTAB" width="94%"  class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 width="100%" valign="Middle"<%= (ds==null)?"":(" colspan="+(ds.getColumnCount()+1)) %>>
			<b class=rtableheader>SQL Execution <%= (ex==null)?"Result - Success":"Generated Exception" %></b>
		</td>
	</tr>
<% if(ex!=null) { %>
	<tr>
		<td class=rbg2>
			<br>
			<font class=rtabletext><%= nvl(ex.getMessage(), "&nbsp;") %></font>
			<br><br>
<!-------------------------------------------------------
EXCEPTION [<%= ex.getMessage() %>]
<%		ex.printStackTrace(new PrintWriter(out)); %>
-------------------------------------------------------->
		</td>
	</tr>
<% } else if(ds!=null) { %>
	<tr class=rbg1>
		<td>&nbsp;</td>
	<%	for(int c=0; c<ds.getColumnCount(); c++) { %>
		<td align="center" valign="Middle"><b class=rtabletext><%= ds.getColumnName(c) %></b></td>
	<%	} %>
	</tr>
	<%	for(int r=0; r<Math.min(ds.getRowCount(), Globals.getDefaultPageSize()); r++) { %>
	<tr<%= (r%2==0)?" class=rowalt1":" class=rowalt2" %>>
		<td align="center" valign="Middle"><font class=rtabletext><%= (r+1) %></font></td>
		<%	for(int c=0; c<ds.getColumnCount(); c++) { %>
		<td valign="Middle"><font class=rtabletext><%= nvl(ds.getString(r, c), "&nbsp;") %></font></td>
		<%	} %>
	</tr>
	<%	}	// for r 
		if(ds.getRowCount()>Globals.getDefaultPageSize()) {	%>
	<tr<%= (Globals.getDefaultPageSize()%2==0)?" class=rowalt1":" class=rowalt2" %>>
		<td align="center" valign="Middle"><font class=rtabletext><%= (Globals.getDefaultPageSize()+1) %></font></td>
		<td align="left" valign="Middle" colspan="<%= ds.getColumnCount() %>"><font class=rtabletext>...</font></td>
	</tr>
	<%	} else if(ds.getRowCount()==0) { %>
	<tr class=rbg3>
		<td align="center" valign="Middle" colspan="<%= ds.getColumnCount()+1 %>"><font class=rtabletext>No data found</font></td>
	</tr>
	<%	}	// else if
	}	// else if 
%>
	<tr>
		<td align="center"<%= (ds==null)?"":(" colspan="+(ds.getColumnCount()+1)) %>>
			<br>
			<input type="Submit" class=button value="Close" onClick="window.close();">
		</td>
	</tr>
</table>
<br>
</body>
</html>

<%!	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } %>

