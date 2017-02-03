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
    String curSubStep = ws.getCurrentSubStep();
   	boolean isCrossTab = rdef.getReportType().equals(AppConstants.RT_CROSSTAB);
	boolean isSQLBased = rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED);    
%>

<table width="100%"  class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=4 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %> - <%= curSubStep %></b></td>
	</tr>
	<tr class=rbg1>
		<td align="center" valign="Middle" width="7%" height="30"><b class=rtableheader>&nbsp;&nbsp;No&nbsp;&nbsp;</b></td>
		<td align="center" valign="Middle" width="65%"><b class=rtableheader>Column</b></td>
		<td align="center" valign="Middle" width="14%"><b class=rtableheader>Re-order To<br>Position</b></td>
	</tr>
<%  int icnt = 0;
	for(Iterator iter=rdef.getAllColumns().iterator(); iter.hasNext(); icnt++) { 
		DataColumnType dct = (DataColumnType) iter.next(); %>
	<tr<%= (icnt%2==0)?" class=rbg8":"" %>>
		<td align="center" height="30"><font class=rtabletext><%= icnt+1 %></font></td>
		<td><font class=rtabletext><%= dct.getDisplayName() %></font></td>
		<td align="center" valign="middle">
			<input type="hidden" name="colId" value="<%= dct.getColId() %>">
			<input type="text" size="5" maxlength="5" name="colOrder" value="<%= dct.getOrderSeq() %>">
		</td>
	</tr>
<%	}   // for 
%>
</table>
			<input type="hidden" name="colOrder" value="">
			<input type="hidden" name="colOrder" value="">
<br>

<script language="JavaScript">
<!--
var colNames = new Array(<%= rdef.getAllColumns().size() %>);
<%	for(int i=0; i<rdef.getAllColumns().size(); i++) { 
		DataColumnType dct = (DataColumnType) rdef.getAllColumns().get(i); %>
colNames[<%= i %>] = "<%= dct.getDisplayName() %>";
<%	} %>

function dataValidate() {
	var val = "";
	for(var i=0; i<document.forma.colOrder.length; i++) {
		val = document.forma.colOrder[i].value;
		if(val!="")
			if(! checkPositiveInteger(val)) {
				alert("Order Position for column "+colNames[i]+" should be a positive integer.\nPlease enter a valid value.");
				document.forma.colOrder[i].focus();
				document.forma.colOrder[i].select();
				
				return false;
			}	// if
	}	// for
	
	return true;
}   // dataValidate
//-->
</script>

