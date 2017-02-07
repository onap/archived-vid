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
   	boolean isCrossTab = rdef.getReportType().equals(AppConstants.RT_CROSSTAB);
	boolean isSQLBased = rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED);    
%>
<table class=mTAB width="100%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=<%= isSQLBased?"5":"7" %> valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %></b></td>
	</tr>
	<tr class=rbg1>
		<td align="center" valign="Middle" width="7%" height="30"><b class=rtableheader>&nbsp;&nbsp;No&nbsp;&nbsp;</b></td>
		<td align="center" valign="Middle" width="58%"><b class=rtableheader>Column</b></td>
<% if(isSQLBased) { %>
		<td align="center" valign="Middle" width="7%"><b class=rtableheader>ID</b></td>
	<% if(isCrossTab) { %>
		<td align="center" valign="Middle" width="14%"><b class=rtableheader>Cross-Tab Usage</b></td>
	<% } %>
		<td align="center" valign="Middle" width="7%"<%= isCrossTab?"":" colspan=2" %>><b class=rtableheader>Edit</b></td>
<% } else { %>
	<% if(isCrossTab) { %>
		<td align="center" valign="Middle" width="14%" colspan=2><b class=rtableheader>Cross-Tab Usage</b></td>
	<% } else { %>
		<td align="center" valign="Middle" width="7%"><b class=rtableheader>Group By</b></td>
		<td align="center" valign="Middle" width="7%"><b class=rtableheader>Visible</b></td>
	<% } %>
		<td align="center" valign="Middle" width="7%"><b class=rtableheader>Re-order</b></td>
		<td align="center" valign="Middle" width="14%" colspan=2 nowrap>
	<% if(isCrossTab) { %>
			<input type="button" class=button value="Add" border="0" width="104" height="28" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ADD %>';document.forma.submit();">
	<% } else { %>
			<table border="0" cellspacing="0" cellpadding="0"><tr>
				<td height="28"><input type="button" class=button value="Add One" border="0" width="104" height="28" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ADD %>';document.forma.submit()"></td>
			</tr><tr>
				<td height="28"><input type="button" class=button value="Add Multiple" border="0" width="104" height="28" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ADD_MULTI %>';document.forma.submit()"></td>
			</tr><tr>
				<td height="28"><input type="button" class=button value="Re-order All" border="0" width="104" height="28" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ORDER_ALL %>';document.forma.submit()"></td>
			</tr></table>
	<% } %>
		</td>
<% } %>
	</tr>
<%	int iCount = 0;
	boolean visibleColExist = false;
	boolean rowColExist = false;
	boolean colColExist = false;
	boolean valColExist = false;
	List reportCols = rdef.getAllColumns();
	for(Iterator iter=reportCols.iterator(); iter.hasNext(); iCount++) { 
		DataColumnType dct = (DataColumnType) iter.next();
		if(dct.isVisible())
			visibleColExist = true;
		if(nvl(dct.getCrossTabValue()).equals(AppConstants.CV_ROW))
			rowColExist = true;
		if(nvl(dct.getCrossTabValue()).equals(AppConstants.CV_COLUMN))
			colColExist = true;
		if(nvl(dct.getCrossTabValue()).equals(AppConstants.CV_VALUE))
			valColExist = true; %>
	<tr class=<%=(iCount % 2 == 0)?"rowalt1":"rowalt2"%>>
		<td align="center" height="30"><font class=rtabletext><%= iCount+1 %><!--dct.getOrderSeq(): <%= dct.getOrderSeq() %>--></font></td>
		<td><font class=rtabletext><%= dct.getDisplayName() %></font></td>
<% if(isSQLBased) { %>
		<td align="center"><font class=rtabletext><%= dct.getColId() %></font></td>
	<% if(isCrossTab) { %>
		<td align="center"><font class=rtabletext><%= nvl(rdef.getCrossTabDisplayValue(dct), "&nbsp;") %></font></td>
	<% } %>
		<td align="center"<%= isCrossTab?"":" colspan=2" %>><input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>pen_paper.gif" alt="Edit" width="12" height="12" onClick="document.getElementById('<%= AppConstants.RI_WIZARD_ACTION %>').value='<%= AppConstants.WA_MODIFY %>'; document.getElementById('<%= AppConstants.RI_DETAIL_ID %>').value='<%= dct.getColId() %>'; document.forma.submit();"></td>
<% } else { %>
	<% if(isCrossTab) { %>
		<td align="center" colspan=2><font class=rtabletext><%= nvl(rdef.getCrossTabDisplayValue(dct), "&nbsp;") %></font></td>
	<% } else { %>
		<td align="center"><font class=rtabletext><%= dct.isGroupBreak()?"Yes":"&nbsp;" %></font></td>
		<td align="center"><font class=rtabletext><%= dct.isVisible()?"Yes":"No" %></font></td>
	<% } %>
		<td align="center" nowrap>
	<%		if(iCount==0) { %>
				<img border="0" src="<%= AppUtils.getImgFolderURL() %>columnblankdown.gif" width="25" height="7">
	<%		} else { %>
				<input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>columnup.gif" width="25" height="7" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_MOVE_UP %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= dct.getColId() %>';">
	<%		} %>
	<%		if(iCount==reportCols.size()-1) { %>
				<img border="0" src="<%= AppUtils.getImgFolderURL() %>columnblankup.gif" width="25" height="7">
	<%		} else { %>
				<input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>columndown.gif" width="25" height="7" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_MOVE_DOWN %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= dct.getColId() %>';">
	<%		} %>
		</td>
		<td align="center"><input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>pen_paper.gif" alt="Edit" width="12" height="12" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_EDIT %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= dct.getColId() %>'; document.forma.submit();"></td>
		<td align="center"><input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>deleteicon.gif" alt="Remove" width="12" height="12" onClick="if(! confirm('Are you sure you want to remove column <%= dct.getDisplayName() %>?')) {return false;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= dct.getColId() %>'; document.forma.submit();}"></td>
<% } %>
	</tr>
<%	} %>
<%	if(iCount==0) { %>
	<tr class=rbg2>
		<td colspan=<%= isSQLBased?"5":"7" %> align="center" height="30"><font class=rtabletext>No columns defined</font></td>
	</tr>
<%	} %>
</table>
<br>

<script language="JavaScript">
<!--
function dataValidate() {
<% if(isCrossTab) { 
		if(! rowColExist) { %>
		alert("You must have at least one column used for cross-tab row headings in the report.\nPlease add a column.");
		return false;
	<% } else if(! colColExist) { %>
		alert("You must have at least one column used for cross-tab column headings in the report.\nPlease add a column.");
		return false;
	<% } else if(! valColExist) { %>
		alert("You must have at least one column used for cross-tab report values in the report.\nPlease add a column.");
		return false;
	<% } else { %>
		return true;
	<% } 
	} else { 
		if(visibleColExist) { %>
		return true;
	<% } else { %>
		alert("You must have at least one visible column in the report.\nPlease add a column.");
		return false;
	<% } 
	} %>
}   // dataValidate
//-->
</script>

<%!
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } 
%>
