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
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.ColFilterType" %>
<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
    String curSubStep = ws.getCurrentSubStep();
   	String reportID   = rdef.getReportID();
%>
<table class="mTAB" width="100%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=8 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %></b></td>
	</tr>
	<tr class=rbg1>
		<td align="center" valign="Middle" width="7%" height="30"><b class=rtableheader>&nbsp;&nbsp;No&nbsp;&nbsp;</b></td>
		<td align="center" valign="Middle" width="10%"><b class=rtableheader>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
		<td align="center" valign="Middle" width="5%"><b class=rtableheader>&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
		<td align="center" valign="Middle" width="33%"><b class=rtableheader>Column</b></td>
		<td align="center" valign="Middle" width="26%"><b class=rtableheader>Filter</b></td>
		<td align="center" valign="Middle" width="5%"><b class=rtableheader>&nbsp;&nbsp;&nbsp;&nbsp;</b></td>
		<td align="center" valign="Middle" width="14%" colspan=2><input type="button" class=button value="Add" border="0" width="71" height="28" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ADD %>';document.forma.submit()"></td>
	</tr>
<%	int iCount = 0;
	int iPos   = 0;
	int nOpenBrackets  = 0;
	int nCloseBrackets = 0;
	String colId = "";
	List reportFilters = rdef.getAllFilters();
	for(Iterator iter=reportFilters.iterator(); iter.hasNext(); iCount++, iPos++) { 
		ColFilterType cft = (ColFilterType) iter.next();
		
		nOpenBrackets  += nvl(cft.getOpenBrackets()).length();
		nCloseBrackets += nvl(cft.getCloseBrackets()).length();
		
		if(! colId.equals(cft.getColId()))
			iPos = 0;
		colId = cft.getColId(); %>
		<tr class=<%=(iCount % 2 == 0)?"rowalt1":"rowalt2"%>>
		<td align="center" height="30"><font class=rtabletext><%= iCount+1 %></font></td>
		<td align="center"><font class=rtabletext><%= iCount>0?cft.getJoinCondition():"&nbsp;" %></font></td>
		<td align="center"><font class=rtabletext><%= nvl(cft.getOpenBrackets(), "&nbsp;") %></font></td>
		<td><font class=rtabletext><%= rdef.getColumnById(colId).getDisplayName() %></font></td>
		<td><font class=rtabletext><%= HTMLEncode(rdef.getFilterLabel(cft)) %></font></td>
		<td align="center"><font class=rtabletext><%= nvl(cft.getCloseBrackets(), "&nbsp;") %></font></td>
		<td align="center"><input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>pen_paper.gif" alt="Edit" width="12" height="12" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_EDIT %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= colId+"|"+iPos %>';"></td>
		<td align="center"><input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>deleteicon.gif" alt="Remove" width="12" height="12" onClick="if(! confirm('Are you sure you want to remove this filter?')) {return false;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= colId+"|"+iPos %>'; }"></td>
	</tr>
<%	}   // for
	if(iCount==0) { %>
	<tr class=rbg2>
		<td colspan=8 align="center" height="30"><font class=rtabletext>No filters defined</font></td>
	</tr>
<%	} %>
</table>
<br>

<script language="JavaScript">
<!--
function dataValidate() {
<%	if(nOpenBrackets!=nCloseBrackets) { %>
	alert("The number of opening brackets (<%= nOpenBrackets %>) must be equal to the number of closing brackets (<%= nCloseBrackets %>).");
	return false;
<%	} %>
	return true;
}   // dataValidate
//-->
</script>

<%!
   private String HTMLEncode(String value) {
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
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } 
%>

