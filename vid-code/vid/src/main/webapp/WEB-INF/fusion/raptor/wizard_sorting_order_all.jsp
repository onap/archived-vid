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
<%@ page import="org.openecomp.portalsdk.analytics.system.Globals" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.Utils" %>
<%@ page import="java.util.Vector" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.ReportLoader" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.base.IdNameValue" %>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.FormFieldType" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.base.ReportWrapper" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.DataCache" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.runtime.FormField" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.base.OrderBySeqComparator" %>
<%@ page import="java.util.Collections" %>
<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
    String curSubStep = ws.getCurrentSubStep();
   	String reportID   = rdef.getReportID();
	boolean isCrossTab = rdef.getReportType().equals(AppConstants.RT_CROSSTAB);
	boolean isSQLBased = rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED); 
   	
%>
<table class="mTAB" width="100%" class="tableBorder" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=4 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %> - <%= curSubStep %></b></td>
	</tr>
	<tr class=rbg1>
		<td align="center" valign="Middle" width="7%" height="30"><b class=rtableheader>&nbsp;&nbsp;No&nbsp;&nbsp;</b></td>
		<td align="center" valign="Middle" width="65%"><b class=rtableheader>Column</b></td>
		<td align="center" valign="Middle" width="14%"><b class=rtableheader>Sort Order</b></td>
		<td align="center" valign="Middle" width="14%"><b class=rtableheader>Sort Type</b></td>
	</tr>
<%  int icnt = 0;
	for(Iterator iter=rdef.getAllColumns().iterator(); iter.hasNext(); icnt++) { 
		DataColumnType dct = (DataColumnType) iter.next(); %>
	<tr<%= (icnt%2==0)?" class=rowalt1":" class=rowalt2" %>>
		<td align="center" height="30"><font class=rtabletext><%= icnt+1 %></font></td>
		<td><font class=rtabletext><%= dct.getDisplayName() %></font></td>
		<td align="center" valign="middle">
			<input type="hidden" name="colId" value="<%= dct.getColId() %>">
			<input type="text" size="5" maxlength="5" name="sortOrder" value="<%= (dct.getOrderBySeq()>0)?(""+dct.getOrderBySeq()):"" %>">
		</td>
		<td align="left" valign="middle">
			<select name="sortAscDesc">
				<option value="<%= AppConstants.SO_ASC %>" <%= nvl(dct.getOrderByAscDesc(), AppConstants.SO_ASC).equals(AppConstants.SO_DESC)?"":" selected" %>>Ascending
				<option value="<%= AppConstants.SO_DESC %>"<%= nvl(dct.getOrderByAscDesc(), AppConstants.SO_ASC).equals(AppConstants.SO_DESC)?" selected":"" %>>Descending
			</select>
		</td>
	</tr>
<%	}   // for 
%>
</table>
			<input type="hidden" name="sortOrder" value="">
			<input type="hidden" name="sortOrder" value="">
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
	for(var i=0; i<document.forma.sortOrder.length; i++) {
		val = document.forma.sortOrder[i].value;
		if(val!="")
			if(! checkPositiveInteger(val)) {
				alert("Sort Order for column "+colNames[i]+" should be a positive integer.\nPlease enter valid Sort Order value.");
				document.forma.sortOrder[i].focus();
				document.forma.sortOrder[i].select();
				
				return false;
			}	// if
	}	// for
	
	return true;
}   // dataValidate
//-->
</script>

<%!
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } 
%>

