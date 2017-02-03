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
<%@ page import="org.openecomp.portalsdk.analytics.util.AppConstants" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.ReportDefinition" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.AppUtils" %>
<%@ page import="org.openecomp.portalsdk.analytics.controller.WizardSequence" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.FormFieldType" %>
<%@page import="org.openecomp.portalsdk.analytics.model.runtime.FormField"%>
<%@page import="org.openecomp.portalsdk.analytics.system.Globals"%>
<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
   	
%>

<table class=mTAB width="100%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=5 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %></b></td>
	</tr>
	<tr class=rbg1>
		<td align="center" valign="Middle" width="7%" height="30"><b class=rtableheader>&nbsp;&nbsp;No&nbsp;&nbsp;</b></td>
		<td align="center" valign="Middle" width="72%"><b class=rtableheader>Field Name</b></td>
		<td align="center" valign="Middle" width="7%"><b class=rtableheader>Re-order</b></td>
		<td align="center" valign="Middle" width="14%" colspan=2>
		  <button type="submit" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ADD %>';document.forma.submit()" border="0" width="40" height="28"  att-button btn-type="primary" size="small" title='Add'>Add</button>
		 <button type="submit" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WSS_ADD_BLANK %>';document.forma.submit()" border="0" width="40" height="28"  att-button btn-type="primary" size="small" title='Add Blank'>Add Blank</button>
		</td>
	</tr>
<%	int iCount = 0;
	if(rdef.getFormFieldList()!=null)
		for(Iterator iter=rdef.getFormFieldList().getFormField().iterator(); iter.hasNext(); iCount++) { 
			FormFieldType fft = (FormFieldType) iter.next(); %>
	<tr class=<%=(iCount % 2 == 0)?"rowalt1":"rowalt2"%>>
		<td align="center" height="30"><font class=rtabletext><%= iCount+1 %><!--fft.getOrderBySeq(): <%= fft.getOrderBySeq() %>--></font></td>
		<td><font class=rtabletext><%= fft.getFieldName() %>&nbsp;[<%= fft.getFieldId()%>]</font></td>
		<td align="center" nowrap>
<% 			if(iCount==0) { %>
				<img border="0" src="<%= AppUtils.getImgFolderURL() %>columnblankdown.gif" width="25" height="7">
<% 			} else { %>
				<input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>columnup.gif" width="25" height="7" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_MOVE_UP %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= fft.getFieldId() %>';">
<% 			} %>
<% 			if(iCount==rdef.getFormFieldList().getFormField().size()-1) { %>
				<img border="0" src="<%= AppUtils.getImgFolderURL() %>columnblankup.gif" width="25" height="7">
<% 			} else { %>
				<input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>columndown.gif" width="25" height="7" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_MOVE_DOWN %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= fft.getFieldId() %>';">
<% 			} %>
		</td>
		
		<td align="center">
		  <%if(!fft.getFieldType().equals(FormField.FFT_BLANK)) { %>
		  <input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>pen_paper.gif" alt="Edit" width="12" height="12" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_EDIT %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= fft.getFieldId() %>';">
		  <% } else { %> &nbsp;
		  <% } %>
		 </td>
		<td align="center"><input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>deleteicon.gif" alt="Remove" width="12" height="12" onClick="if(! confirm('Are you sure you want to delete form field <%= fft.getFieldName() %> from the report?')) {return false;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= fft.getFieldId() %>'; }"></td>
	</tr>
<%		}	// for
	if(iCount==0) { %>
	<tr class=rbg2>
		<td colspan=5 align="center" height="30"><font class=rtabletext>No form fields defined</font></td>
	</tr>
<%	} %>
</table>
<% if(Globals.customizeFormFieldInfo()) { %>
<% if(rdef.getFormFieldList()!=null) { %>
<table class=mTAB width="100%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
  <tr><td class=rbg1 valign="middle"><b class=rtableheader> Defining the Information Bar </b></td></tr>
  <tr><td align="center">
    <textarea name="blueBarField" id="blueBarField" rows="20" cols="200" style="width: 200px"><%= nvl(rdef.getFormFieldList().getComment()).length()>0?rdef.getFormFieldList().getComment():""%></textarea>
  </td></tr>
  <tr><td align="center">
    <button type="submit" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WSS_INFO_BAR %>';document.forma.submit()" att-button btn-type="primary" size="small" title='Submit'>Submit</button> 
  </td></tr>
  </table>
<% } %>
<% } %>
<br>

<script language="JavaScript">
<!--
function dataValidate() {
	return true;
}   // dataValidate
//-->
</script>

<%!	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } %>

