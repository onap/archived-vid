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
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.SecurityEntry" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.Utils" %>

<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
    String curSubStep = ws.getCurrentSubStep();
%>  
<table width="100%"  class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=4 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %></b></td>
	</tr>
	<tr>
		<td class=rbg2 align="right" width="16%" height="30"><font class=rtabletext>Created By: </font></td> 
		<td class=rbg3 align="left" width="34%" valign="middle" nowrap><font class=rtabletext><%= AppUtils.getUserName(rdef.getCreateID()) %></font></td>
		<td class=rbg2 align="right" width="16%"><font class=rtabletext>Created Date: </font></td> 
		<td class=rbg3 align="left" width="34%" valign="middle" nowrap><font class=rtabletext><%= rdef.getCreateDate() %></font></td>
	</tr> 
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Last Updated By: </font></td> 
		<td class=rbg3 align="left" valign="middle" nowrap><font class=rtabletext><%= AppUtils.getUserName(rdef.getUpdateID()) %></font></td>
		<td class=rbg2 align="right"><font class=rtabletext>Last Updated: </font></td> 
		<td class=rbg3 align="left" valign="middle" nowrap><font class=rtabletext><%= rdef.getUpdateDate() %></font></td>
	</tr> 
	<tr>
		<td class=rbg2 align="right" height="30"><font class=rtabletext>Report Owner: </font></td> 
		<td class=rbg3 align="left"><font class=rtabletext>
			<select name="reportOwner">
<% 			Vector allUsers = Utils.getUsersNotInList(new java.util.ArrayList(),request);
			for(int i=0; i<allUsers.size(); i++) {
				IdNameValue user = (IdNameValue) allUsers.get(i); %>
			    <option value="<%= user.getId() %>"<%= user.getId().equals(rdef.getOwnerID())?" selected":"" %>><%= user.getName() %>
<% 			}	// for 
%>
			</select></font>
		</td>
		<td class=rbg2 align="right"><font class=rtabletext>Public? (All users can run the report)</font></td> 
		<td class=rbg3 align="left">
			<select name="public" >
				<option value="Y"<%= rdef.isPublic()?" selected":"" %>>Yes
				<option value="N"<%= rdef.isPublic()?"":" selected" %>>No
			</select>
		</td>
	</tr>
</table>
<br>

<table class="mTAB" width="100%" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=5 valign="Middle"><b class=rtableheader>Report Users</b></td>
	</tr>
	<tr class=rbg1>
		<td align="center" valign="Middle" width="7%" height="30"><b class=rtableheader>&nbsp;&nbsp;No&nbsp;&nbsp;</b></td>
		<td align="center" valign="Middle" width="59%"><b class=rtableheader>User Name</b></td>
		<td align="center" valign="Middle" width="10%"><b class=rtableheader>Run Access</b></td>
		<td align="center" valign="Middle" width="10%"><b class=rtableheader>Edit Access</b></td>
		<td align="center" valign="Middle" width="14%"><b class=rtableheader>Remove</b></td>
	</tr>
<%	int iCount = 0;
	Vector reportUsers = rdef.getReportUsers(request);
	for(Iterator iter=reportUsers.iterator(); iter.hasNext(); iCount++) { 
		SecurityEntry rUser = (SecurityEntry) iter.next(); %>
	<tr class="<%=((iCount % 2 == 0)?"rowalt1":"rowalt2")%>">
		<td align="center" height="30"><font class=rtabletext><%= iCount+1 %></font></td>
		<td><font class=rtabletext><%= rUser.getName() %></font></td>
		<td align="center" valign="Middle"><img src="<%= AppUtils.getImgFolderURL() %>active.gif" width="16" height="16" border="0"></td>
		<td align="center" valign="Middle"><input type="image" src="<%= AppUtils.getImgFolderURL() %><%= rUser.isReadOnly()?"inactive.gif":"active.gif" %>" alt="<%= rUser.isReadOnly()?"Grant":"Revoke" %> edit access" width="16" height="16" border="0" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= rUser.isReadOnly()?AppConstants.WA_GRANT_USER:AppConstants.WA_REVOKE_USER %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= rUser.getId() %>';"></td>
		<td align="center"><input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>deleteicon.gif" alt="Remove" width="12" height="12" onClick="if(! confirm('Are you sure you want to remove user <%= rUser.getName() %>?')) {return false;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE_USER %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= rUser.getId() %>'; }"></td>
	</tr>
<%	}   // for
//	if(iCount==0) { %>
	<!--tr class=rbg2>
		<td colspan=5 align="center" height="30"><font class=rtabletext>No user access defined</font></td>
	</tr-->
<%	//}
	Vector remainingUsers = Utils.getUsersNotInList(reportUsers,request);
	if(remainingUsers.size()>0) { %>
	<tr>
		<td class=rbg1 colspan=5 valign="Middle" nowrap><b class=rtableheader>Grant Access To&nbsp;
			<select name="newUserId" onChange="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ADD_USER %>'; document.forma.submit();">
			    <option value="" selected>--- Select User ---
<% 			for(int i=0; i<remainingUsers.size(); i++) {
				IdNameValue user = (IdNameValue) remainingUsers.get(i); %>
			    <option value="<%= user.getId() %>"><%= user.getName() %>
<% 			}	// for 
%>
			</select></b>
		</td>
<%	}	// if 
%>
	</tr>
</table>
<br>

<table class="mTAB" width="100%" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=5 valign="Middle"><b class=rtableheader>Report Roles</b></td>
	</tr>
	<tr class=rbg1>
		<td align="center" valign="Middle" width="7%" height="30"><b class=rtableheader>&nbsp;&nbsp;No&nbsp;&nbsp;</b></td>
		<td align="center" valign="Middle" width="59%"><b class=rtableheader>Role Name</b></td>
		<td align="center" valign="Middle" width="10%"><b class=rtableheader>Run Access</b></td>
		<td align="center" valign="Middle" width="10%"><b class=rtableheader>Edit Access</b></td>
		<td align="center" valign="Middle" width="14%"><b class=rtableheader>Remove</b></td>
	</tr>
<%	iCount = 0;
	Vector reportRoles = rdef.getReportRoles(request);
	for(Iterator iter=reportRoles.iterator(); iter.hasNext(); iCount++) { 
		SecurityEntry rRole = (SecurityEntry) iter.next(); %>
	<tr class="<%=((iCount % 2 == 0)?"rowalt1":"rowalt2")%>">
		<td align="center" height="30"><font class=rtabletext><%= iCount+1 %></font></td>
		<td><font class=rtabletext><%= rRole.getName() %></font></td>
		<td align="center" valign="Middle"><img src="<%= AppUtils.getImgFolderURL() %>active.gif" width="16" height="16" border="0"></td>
		<td align="center" valign="Middle"><input type="image" src="<%= AppUtils.getImgFolderURL() %><%= rRole.isReadOnly()?"inactive.gif":"active.gif" %>" alt="<%= rRole.isReadOnly()?"Grant":"Revoke" %> edit access" width="16" height="16" border="0" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= rRole.isReadOnly()?AppConstants.WA_GRANT_ROLE:AppConstants.WA_REVOKE_ROLE %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= rRole.getId() %>';"></td>
		<td align="center"><input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>deleteicon.gif" alt="Remove" width="12" height="12" onClick="if(! confirm('Are you sure you want to remove role <%= rRole.getName() %>?')) {return false;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE_ROLE %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= rRole.getId() %>'; }"></td>
	</tr>
<%	}   // for
//	if(iCount==0) { %>
	<!--tr class=rbg2>
		<td colspan=5 align="center" height="30"><font class=rtabletext>No role access defined</font></td>
	</tr-->
<%	//}
	Vector remainingRoles = Utils.getRolesNotInList(reportRoles,request); 
	if(remainingRoles.size()>0) { %>
	<tr>
		<td class=rbg1 colspan=5 valign="Middle" nowrap><b class=rtableheader>Grant Access To&nbsp;
			<select name="newRoleId" onChange="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ADD_ROLE %>'; document.forma.submit();">
			    <option value="" selected>--- Select Role ---
<% 			for(int i=0; i<remainingRoles.size(); i++) {
				IdNameValue role = (IdNameValue) remainingRoles.get(i); %>
			    <option value="<%= role.getId() %>"><%= role.getName() %>
<% 			}	// for 
%>
			</select></b>
		</td>
<%	}	// if 
%>
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

