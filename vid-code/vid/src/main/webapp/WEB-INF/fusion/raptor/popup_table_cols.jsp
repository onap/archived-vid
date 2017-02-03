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
<%@ page import="org.openecomp.portalsdk.analytics.system.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.*" %>

<%	String tableName = AppUtils.getRequestValue(request, AppConstants.RI_TABLE_NAME);
	String remoteDbPrefix = (String) session.getAttribute("remoteDB");
	Vector tableSources = null;
	Vector dbColumns = null;
	if(tableName==null) {
		tableSources = DataCache.getReportTableSources(remoteDbPrefix);
		if(tableSources.size()>0)
		tableName = ((TableSource) DataCache.getReportTableSources(remoteDbPrefix).get(0)).getTableName();
	}
	    if(tableName!=null)  
			dbColumns = DataCache.getReportTableDbColumns(tableName.toUpperCase(),remoteDbPrefix); 
	
	
	boolean isSingleValueChoice      = AppUtils.getRequestFlag(request, "single_value");
	boolean includeTableNameInResult = AppUtils.getRequestFlag(request, "return_table_name");
	boolean includeColTypeInResult   = AppUtils.getRequestFlag(request, "return_col_type"); %>

<html>
<head>
	<title>Table Columns</title>
	<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/raptor.css">
	<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/ral.css">

<script language=JavaScript>
<!--
	function setValue(newValue) {
		window.opener.addText(newValue);
		window.close();
	}   // setValue

	function clearValue() {
		window.opener.clearText();
		window.close();
	}   // clearValue	
//-->
</script>

</head>
<body>

<% if(! isSingleValueChoice) { %>
<script language=JavaScript>
<!--
	function getSelection() {
		var selList = "";
		<% if(dbColumns!=null) { %>
		for(var i=0; i<<%= dbColumns.size() %>; i++)
			if(document.dataform.dataChk[i].checked)
				selList += ((selList=="")?"":", ")+document.dataform.dataChk[i].value;
		<% } %>
		
		setValue(selList);
		
		return false;
	}   // getSelection
//-->
</script>
<% }	// if 
%>


<form name="dataform" action="<%= AppUtils.getBaseURL() %>" method="post">
	<input type="hidden" name="action" value="raptor">
	<input type="hidden" name="<%= AppConstants.RI_ACTION %>" value="report.popup.table.cols">
<% if(isSingleValueChoice) { %>
	<input type="hidden" name="single_value" value="Y">
<% } %>
<% if(includeTableNameInResult) { %>
	<input type="hidden" name="return_table_name" value="Y">
<% } %>
<% if(includeColTypeInResult) { %>
	<input type="hidden" name="return_col_type" value="Y">
<% } %>

<table class="mTAB" width=94% border=0 cellspacing=1 align=center>
	<tr class=rbg1>
		<td align="center" valign="middle" height="30">&nbsp;
		<% if(! isSingleValueChoice) { %>
			<input type=image src='<%= AppUtils.getImgFolderURL() %>downloadicon.gif' alt='Store selection' width=12 height=12 border=0 onClick="return getSelection()">&nbsp;
		<% } %>
		</td>
		<td colspan="2" valign="middle" nowrap>
			<select name="<%= AppConstants.RI_TABLE_NAME %>" onChange="document.dataform.submit()">
			<%	for(Iterator iter=DataCache.getReportTableSources(remoteDbPrefix).iterator(); iter.hasNext(); ) { 
					String iTableName = ((TableSource) iter.next()).getTableName(); %>
			    <option value="<%= iTableName %>"<%= iTableName.equals(tableName)?" selected":"" %>><%= iTableName %>
			<%	}	// for 
			%>
			</select>
			<b class=rtableheader> DB Table Columns</b>
		</td>
	</tr>
	<%	int rNum = 0;
		if(dbColumns!=null)
			for(rNum=0; rNum<dbColumns.size(); rNum++) {
				DBColumnInfo dbCol = (DBColumnInfo) dbColumns.get(rNum); 
				String sValue   = (includeTableNameInResult?(tableName+"."):"")+dbCol.getColName()+(includeColTypeInResult?("|"+dbCol.getColType()):""); 
				String sDisplay = "["+tableName+"]."+dbCol.getColName(); %>
	<tr<%= (rNum%2==0)?" class=rowalt1":" class=rowalt2" %>>
		<td align="center" height="30"><font class=rtabletext><%= (rNum+1) %></font></td>
	<%			if(isSingleValueChoice) { %>
		<td colspan="2"><font class=rtabletext><a href="javascript:setValue('<%= sValue %>')"><%= sDisplay %></a></font></td>
	<%			} else { %>
		<td align="center" valign="middle">
			<input type="checkbox" name="dataChk" value="<%= sValue %>">
		</td>
		<td><font class=rtabletext><%= sDisplay %></font></td>
	<%			}	// else 
	%>
	</tr>
	<%		}	// for 
		if(rNum==0) { %>
	<tr class=rbg3>
		<td align="center" height="30" colspan="3"><font class=rtabletext>No columns found for table [<%= tableName %>]</font></td>
	</tr>
	<%	} else {	// if
	%>
	<tr class=rbg3>
		<td colspan="3"><font class=rtabletext><a href="javascript:clearValue()"><%= "CLEAR VALUE" %></a></font></td>
	</tr>	
	<%	
	}
	%>
	<tr class=rbg1>
		<td align="center" valign="middle" height="30">&nbsp;
		<% if(! isSingleValueChoice) { %>
			<input type=image src='<%= AppUtils.getImgFolderURL() %>downloadicon.gif' alt='Store selection' width=12 height=12 border=0 onClick="return getSelection()">&nbsp;
		<% } %>
		</td>
		<td colspan="2">&nbsp;</td>
	</tr>
</table>

	<input type="hidden" name="dataChk" value="">
	<input type="hidden" name="dataChk" value="">
</form>

</body>
</html>

<%!	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } %>

