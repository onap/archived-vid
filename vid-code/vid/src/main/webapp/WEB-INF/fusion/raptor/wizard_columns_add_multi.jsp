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
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.DataSourceType" %>
<%@ page import="java.util.Vector" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.DBColumnInfo" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.DataCache" %>
<%@ page errorPage="error_page.jsp" %>
<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
    String curSubStep = ws.getCurrentSubStep();
%>  
<table class="mTAB" width="100%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=3 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %> - <%= curSubStep %></b></td>
	</tr>
	<tr class=rbg1>
		<td align="center" valign="Middle" width="7%" height="30"><b class=rtableheader>&nbsp;&nbsp;No&nbsp;&nbsp;</b></td>
		<td align="center" valign="Middle" width="79%"><b class=rtableheader>Column</b></td>
		<td align="center" valign="Middle" width="14%">
			<b class=rtableheader>Add To Report?</b>
			<br>
			<input type=checkbox name="selectAll" value="Y" onClick="setAllChk(this.checked);">
			</td>
	</tr>
<%  int icnt = 0;
	String remoteDbPrefix = (String) session.getAttribute("remoteDB");
	for(Iterator iter=rdef.getDataSourceList().getDataSource().iterator(); iter.hasNext(); ) { 
		DataSourceType dst = (DataSourceType) iter.next();
		
		Vector dbColumns = DataCache.getReportTableDbColumns(dst.getTableName().toUpperCase(),remoteDbPrefix);
		if(dbColumns!=null)
			for(int i=0; i<dbColumns.size(); i++) {
				DBColumnInfo dbCol = (DBColumnInfo) dbColumns.get(i);
				icnt++; %>
	<tr<%= (icnt%2==0)?" class=rowalt1":" class=rowalt2" %>>
		<td align="center" height="30"><font class=rtabletext><%= icnt %></font></td>
		<td><font class=rtabletext>[<%= dst.getDisplayName() %>].<%= dbCol.getLabel() %></font></td>
		<td align="center" valign="middle">
			<input type="checkbox" name="dataChk" onClick="document.forma.addColumn[<%= icnt-1 %>].value=(this.checked?'Y':'');">
			<input type="hidden" name="addColumn" value="">
			<input type="hidden" name="tableId" value="<%= dst.getTableId() %>">
			<input type="hidden" name="columnName" value="<%= dbCol.getColName() %>">
			<input type="hidden" name="columnType" value="<%= dbCol.getColType() %>">
			<input type="hidden" name="displayName" value="<%= dbCol.getLabel() %>">
		</td>
	</tr>
<% 			}   // for i 
	}   // for 
%>
</table>
			<input type="hidden" name="dataChk" value="">
			<input type="hidden" name="dataChk" value="">
			<input type="hidden" name="addColumn" value="">
			<input type="hidden" name="addColumn" value="">
<br>

<script language="JavaScript">
<!--
function setAllChk(toSelected) {
	for(var i=0; i<document.forma.addColumn.length-2; i++) {
		document.forma.addColumn[i].value = (toSelected?"Y":"");
		document.forma.dataChk[i].checked = toSelected;
	}	// for
}	// setAllChk

function dataValidate() {
	return true;
}   // dataValidate
//-->
</script>

