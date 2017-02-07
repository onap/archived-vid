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
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.DataColumnType" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.AppConstants" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.ReportDefinition" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.AppUtils" %>
<%@ page import="org.openecomp.portalsdk.analytics.controller.WizardSequence" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.List" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.DataCache" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.DataSourceType" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.DBColumnInfo" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.TableSource" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.base.IdNameValue" %>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.SemaphoreType" %>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.FormFieldType" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.TableJoin" %>

<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
    String curSubStep = ws.getCurrentSubStep();
%>
<table class="mTAB" width="100%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=4 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %></b></td>
	</tr>
	<tr class=rbg1>
		<td align="center" valign="Middle" width="7%" height="30"><b class=rtableheader>&nbsp;&nbsp;No&nbsp;&nbsp;</b></td>
		<td align="center" valign="Middle" width="79%"><b class=rtableheader>Table</b></td>
		<td align="center" valign="Middle" width="14%" colspan=2><input type="button" class=button border="0"  value="Add" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ADD %>';document.forma.submit();"></td>
	</tr>
<% 	int iCount = 0;
	for(Iterator iter=rdef.getDataSourceList().getDataSource().iterator(); iter.hasNext(); iCount++) { 
		DataSourceType dst = (DataSourceType) iter.next(); %>
	<tr class=<%=(iCount % 2 == 0)?"rowalt1":"rowalt2"%>>
		<td align="center" height="30"><font class=rtabletext><%= iCount+1 %></font></td>
		<td><font class=rtabletext><%= nvl(dst.getDisplayName()).length()>0?dst.getDisplayName():dst.getTableName()%></font></td>
		<td align="center"><input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>pen_paper.gif" alt="Edit" width="12" height="12" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_EDIT %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= dst.getTableId() %>';"></td>
		<td align="center"><input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>deleteicon.gif" alt="Remove" width="12" height="12" onClick="if(! confirm('Are you sure you want to remove the <%= dst.getDisplayName() %> table\nalong with all its columns from the report?')) {return false;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= dst.getTableId() %>'; }"></td>
	</tr>
<%	} %>
<%	if(iCount==0) { %>
	<tr class=rbg3>
		<td colspan=4 align="center" height="30"><font class=rtabletext>No tables defined</font></td>
	</tr>
<%	} %>
</table>
<br>

<script language="JavaScript">
<!--
function dataValidate() {
<% if(rdef.getDataSourceList().getDataSource().size()>0) { %>
	return true;
<% } else { %>
	alert("You must have at least one table in the report.\nPlease add a table.");
	return false;
<% } %>
}   // dataValidate
//-->
</script>

<%!	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } 
%>    

