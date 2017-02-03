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
<%@ page import="java.util.Collections" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.base.OrderSeqComparator" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.base.OrderBySeqComparator" %>

<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
    String curSubStep = ws.getCurrentSubStep();
   	String reportID   = rdef.getReportID();
	boolean isCrossTab = rdef.getReportType().equals(AppConstants.RT_CROSSTAB);
	boolean isSQLBased = rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED); 
	List reportCols = rdef.getAllColumns();
	Collections.sort(reportCols, new OrderSeqComparator());
	int numSortCols = rdef.getNumSortColumns(); %>
<table class="mTAB" width="100%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=6 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %></b></td>
	</tr>
	<tr class=rbg1>
		<td align="center" valign="Middle" width="7%" height="30"><b class=rtableheader>Sort Order</b></td>
		<td align="center" valign="Middle" width="65%"><b class=rtableheader>Sort By Column</b></td>
		<td align="center" valign="Middle" width="7%"><b class=rtableheader>Sort Type</b></td>
		<td align="center" valign="Middle" width="7%" nowrap><b class=rtableheader>Re-order</b></td>
		<td align="center" valign="Middle" width="14%" colspan=2>
		<% if(numSortCols<reportCols.size()) { %>
			<table border="0" cellspacing="0" cellpadding="0"><tr>
				<td height="28"><input type="button" class=button value="Add One" border="0" width="104" height="28" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ADD %>';document.forma.submit();"></td>
			</tr><tr><td height="28">
		<% } %>
				<input type="button" class=button value="Re-order All" border="0" width="104" height="28" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ORDER_ALL %>';document.forma.submit();">
		<% if(numSortCols<reportCols.size()) { %>
			</td></tr></table>
		<% } %>
		</td>
	</tr>
<%	int iCount = 0;
        Collections.sort(reportCols,new OrderBySeqComparator());
	for(Iterator iter=reportCols.iterator(); iter.hasNext(); ) { 
		DataColumnType dct = (DataColumnType) iter.next();
		if(dct.getOrderBySeq()>0) { %>
         <tr class=<%=(iCount % 2 == 0)?"rowalt1":"rowalt2"%>>
		<td align="center" height="30"><font class=rtabletext><%= iCount+1 %><!--dct.getOrderBySeq(): <%= dct.getOrderBySeq() %>--></font></td>
		<td><font class=rtabletext><%= dct.getDisplayName() %></font></td>
		<td align="center"><font class=rtabletext><%= dct.getOrderByAscDesc().equals(AppConstants.SO_ASC)?"Ascending":"Descending" %></font></td>
		<td align="center" nowrap>
<% 			if(iCount==0) { %>
				<img border="0" src="<%= AppUtils.getImgFolderURL() %>columnblankdown.gif" width="25" height="7">
<% 			} else { %>
				<input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>columnup.gif" width="25" height="7" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_MOVE_UP %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= dct.getColId() %>';">
<% 			} %>
<% 			if(iCount==numSortCols-1) { %>
				<img border="0" src="<%= AppUtils.getImgFolderURL() %>columnblankup.gif" width="25" height="7">
<% 			} else { %>
				<input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>columndown.gif" width="25" height="7" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_MOVE_DOWN %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= dct.getColId() %>';">
<% 			} %>
		</td>
		<td align="center"><input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>pen_paper.gif" alt="Edit" width="12" height="12" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_EDIT %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= dct.getColId() %>';"></td>
		<td align="center"><input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>deleteicon.gif" alt="Remove" width="12" height="12" onClick="if(! confirm('Are you sure you want to remove sort on column <%= dct.getDisplayName() %>?')) {return false;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= dct.getColId() %>'; }"></td>
	</tr>
<%
			iCount++;
		}   // if
	}   // for
	Collections.sort(reportCols, new OrderSeqComparator());
%>
<%	if(numSortCols==0) { %>
	<tr class=rbg2>
		<td colspan=6 align="center" height="30"><font class=rtabletext>No sorting defined</font></td>
	</tr>
<%	} %>
</table>
<br>

<script language="JavaScript">
<!--
function dataValidate() {
	return true;
}   // dataValidate
//-->
</script>

