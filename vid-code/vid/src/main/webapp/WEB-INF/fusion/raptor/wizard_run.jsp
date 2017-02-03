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
<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
%>
<script language="JavaScript">
<!--
function showSQLPopup() {
	var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.popup.sql", "showSQLPopup", "width=450,height=330,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
}   // showSQLPopup
//-->
</script>

<table width="100%"  class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=2 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %></b></td>
	</tr>
	<tr>
		<td class=rbg2>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="right" valign="middle" height="20" width="100%" nowrap>
						<b class=rtabletext>
						    <a href="javascript:showSQLPopup()">SQL&nbsp;<img border="0" src="<%= AppUtils.getImgFolderURL() %>txt_icon.gif" alt="Click here to view the generated SQL" width="12" height="12"></a>&nbsp;
						</b>
					</td>
				</tr>
				<tr>
					<td align="center" valign="middle" height="60" width="100%">
						<font class=rtabletext>
							<b>Report definition successfully completed.</b><br>
							<br>
							<input type="hidden" name="<%= AppConstants.RI_RESET_PARAMS %>" value="Y">
							<input type="hidden" name="<%= AppConstants.RI_REFRESH %>" value="Y">
							<a att-button btn-type="primary" size="small"  href="report.htm#/report_run/c_master=<%=rdef.getReportID()%>&refresh=Y">Run</a>
						</font>
					</td>
				</tr>
				<tr>
					<td height="20">&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<br>

