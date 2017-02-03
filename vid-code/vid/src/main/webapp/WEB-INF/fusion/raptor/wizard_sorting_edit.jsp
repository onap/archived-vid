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
	boolean isEdit = curSubStep.equals(AppConstants.WSS_EDIT);
	DataColumnType currColumn = null;
	if(isEdit)
		currColumn = rdef.getColumnById(AppUtils.getRequestNvlValue(request, AppConstants.RI_DETAIL_ID)); %>

<table width="100%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=2 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %><%= curSubStep.equals(AppConstants.WSS_EDIT)?"Edit Sorting":(curSubStep.equals(AppConstants.WSS_ADD)?"Add Sorting":"") %></b></td>
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30" style="background-image:url(<%= AppUtils.getImgFolderURL() %>required.gif); background-position:top right; background-repeat:no-repeat;">
			<font class=rtabletext>Sort By Column: </font>
		</td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
		<% if(isEdit) { %>
			<%= currColumn.getDisplayName() %>
		<% } else { %>
			<select name="sortColId">
			<% 	int iCount = 0;
				List reportCols = rdef.getAllColumns();
				for(Iterator iter=reportCols.iterator(); iter.hasNext(); ) { 
					DataColumnType dct = (DataColumnType) iter.next();
					if(dct.getOrderBySeq()<=0) { %>
						<option value="<%= dct.getColId() %>"<%= ((iCount++)==0)?" selected":"" %>><%= dct.getDisplayName() %>
			<% 		}   // if
				}   // for 
			%>
			</select>
		<% }	// else 
		%>
		</font></td>
	</tr> 
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Sort Type: </font></td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
			<select name="sortAscDesc">
				<option value="<%= AppConstants.SO_ASC %>" <%= (isEdit&&nvl(currColumn.getOrderByAscDesc(), AppConstants.SO_ASC).equals(AppConstants.SO_DESC))?"":" selected" %>>Ascending
				<option value="<%= AppConstants.SO_DESC %>"<%= (isEdit&&nvl(currColumn.getOrderByAscDesc(), AppConstants.SO_ASC).equals(AppConstants.SO_DESC))?" selected":"" %>>Descending
			</select>
		</font></td>
	</tr>
</table>
<br>

<script language="JavaScript">
<!--
function dataValidate() {
	return true;
}   // dataValidate
//-->
</script>

<%!
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } 
%>
