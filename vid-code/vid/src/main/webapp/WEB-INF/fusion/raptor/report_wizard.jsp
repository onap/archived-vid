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
<%--
 Name: report_wizard.jsp
 Use : Master JSP which navigates to specific JSP when different tab is selected. Default it navigates to the wizard_definition.jsp 
 
 Change Log
 ==========
 
 22-Jun-2009 : Version 8.4 (Sundar); 
 				
 				<UL> 
 				<LI> Save button is suppressed from showing when wizard is in the last page (Run page).</LI>
 				<LI> width of the content_iframe is changed back to default one when navigated from >100% report's run page.</LI>
 				</UL>
--%>
<%@ page import="java.util.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.base.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.runtime.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.controller.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.*" %>

<%@ page errorPage="error_page.jsp" %>
<html>
 <body>
<script language="Javascript" src="<%= AppUtils.getBaseFolderURL() %>js/form-field-tooltip.js"></script>
<script language="Javascript" src="<%= AppUtils.getBaseFolderURL() %>js/other_scripts.js"></script>
<style>
td, th {
    padding: 0px;
}
</style>
<script>
	<%
		String paramString = "";
		Enumeration en = request.getParameterNames();
		if (en != null) {
		while (en.hasMoreElements()) {
			String name = (String) en.nextElement();
			String values[] = request.getParameterValues(name);
			if (name.equals(AppConstants.RI_ACTION) == false 
				&& name.equals("c_master") == false && name.equals("action") == false
				&& values != null) {
				for (int i = 0; i < values.length; i++) {
					//values[i] = values[i].replaceAll("=", "%3d");
					//values[i] = values[i].replaceAll("\\?", "%3f");
					//values[i] = values[i].replaceAll("&", "%26");	
					//System.out.println(name + " (" + i + "): [" + values[i] + "]");
					paramString = paramString + "&" + name + "=" + java.net.URLEncoder.encode(values[i],"UTF8");
				}
			}
		}
		}

	%>	
	if (window.parent.document.getElementById('content_Iframe') || window.document.getElementById('content_Iframe')){
	}else{
		//window.location="<%= AppUtils.getRaptorActionURL() %>report.create.container&c_master=<%=request.getParameter("c_master")%><%=paramString%>";
	}
</script>
<%	ReportDefinition rdef = (ReportDefinition) request.getSession().getAttribute(AppConstants.SI_REPORT_DEFINITION);
	
	String reportID   = rdef.getReportID();
	WizardSequence ws = rdef.getWizardSequence();
	
	String curStep    = ws.getCurrentStep();
	String curSubStep = ws.getCurrentSubStep();
    
	String dbInfo = null;
	dbInfo = rdef.getDBInfo();
	int sessionflag = 0;
	if(dbInfo == null  || dbInfo.length() == 0) {
	   dbInfo = (String) session.getAttribute("remoteDB");
       sessionflag = 1; 
	}
       session.setAttribute("remoteDB", dbInfo);
	if((dbInfo == null) && (request.getParameter("dataSource")!=null)) 
	   session.setAttribute("remoteDB", request.getParameter("dataSource"));

	String title = (reportID.equals("-1")?"Create Report":"Edit Report");
	String navTitle = Globals.getBaseTitle()+" > " + title;
	
	boolean isCrossTab = rdef.getReportType().equals(AppConstants.RT_CROSSTAB);
	boolean isSQLBased = rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED); 
%>
<!--  Set All variable to request, to enable included jsp to access it -->	
<%
	request.setAttribute(AppConstants.SI_REPORT_DEFINITION,rdef);
%>	

 <link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/raptor.css">
 <link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/ral.css">
 
 
<script language="JavaScript" src="<%= AppUtils.getBaseFolderURL() %>js/raptor.js"></script>
 
<jsp:include page="error_include.jsp" flush="true" />

<table width="100%" >
<tr><td id="folderTreeContainer" width="1%" height='300' valign='top' ><table>
		<!--<jsp:include page="tree/folderNav.jsp" flush="true" >	
        	<jsp:param name="doCollapseTree" value="Y" />
       	</jsp:include>-->
	</table>
</td>
<td id="searchContentContainer" valign='top' >
<div id="contentDiv" style="position:absoulte;overflow:auto;">
<jsp:include page="custom_js_include.jsp" flush="true" />
<form id="forma" name="forma" action="<%= AppUtils.getBaseURL() %>" method="post">
	<input type="hidden" name="action" value="raptor">
	<input type="hidden" name="<%= AppConstants.RI_ACTION %>" value="report.wizard">
	<input type="hidden" name="<%= AppConstants.RI_REPORT_ID %>" value="<%= reportID %>">
	<input type="hidden" name="<%= AppConstants.RI_DETAIL_ID %>" id="<%= AppConstants.RI_DETAIL_ID %>" value="<%= AppUtils.getRequestNvlValue(request, AppConstants.RI_DETAIL_ID) %>">
	<input type="hidden" id="<%= AppConstants.RI_GO_TO_STEP %>" name="<%= AppConstants.RI_GO_TO_STEP %>" value="">
	<input type="hidden" id="<%= AppConstants.RI_WIZARD_ACTION %>" name="<%= AppConstants.RI_WIZARD_ACTION %>" value="<%= AppConstants.WA_BACK %>">
	<input type="hidden" name="<%= AppConstants.RI_JAVASCRIPT_ITEM_ID %>" value="">

<table width="94%" border="0" cellspacing="0" cellpadding="0" align=center>
<tr><td>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin: 0px;">
		<tr>
<%		for(ws.resetNext(); ws.hasNext(); ) { 
			String sTab = ws.getNext(); %>
			<td width="9" align="right"><img border="0" src="<%= AppUtils.getImgFolderURL() %><%= sTab.equals(curStep)?"tab_left_sel.gif":"tab_left.gif" %>" width="9" height="24"></td>
			<td class=<%= sTab.equals(curStep)?"rbg1":"rbg1d" %> align="center" valign="middle">
			<%	if(sTab.equals(curStep)) { %>
				&nbsp;<a href="javascript:document.forma.submit()" class=rtabselected onClick="document.getElementById('<%= AppConstants.RI_GO_TO_STEP %>').value='<%= sTab %>';"><%= clearSpaces(sTab) %></a>&nbsp;
			<%	} else if(reportID.equals("-1")) { %>	
				&nbsp;<b class=rtabtext><%= clearSpaces(sTab) %></b>&nbsp;
			<%	} else { %>	
				&nbsp;<a href="javascript:document.forma.submit()" class=rtabtext onClick="document.getElementById('<%= AppConstants.RI_GO_TO_STEP %>').value='<%= sTab %>';"><%= clearSpaces(sTab) %></a>&nbsp;
			<%	} %>
			</td>
			<td width="9" align="left"><img border="0" src="<%= AppUtils.getImgFolderURL() %><%= sTab.equals(curStep)?"tab_right_sel.gif":"tab_right.gif" %>" width="9" height="24"></td>
<%		}	// for 
%>
			<td width="80%">&nbsp;</td>
		</tr>
	</table>
</td></tr><tr><td>
	<table width="100%" border="0" cellspacing="0" cellpadding="3" style="margin: 0px;">
		<tr class=rbg1>
			<td valign="Middle"><b class=rtableheader><%= navTitle %></b></td>
		</tr>
	</table>
</td></tr><tr><td><%	if(curStep.equals(AppConstants.WS_DEFINITION)) { %>
        <% if(sessionflag == 1) dbInfo = ""; %>
		   <jsp:include page="wizard_definition.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_SQL)) { %>
		 <jsp:include page="wizard_sql_def.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_TABLES)&&curSubStep.equals("")) { %>
		 <jsp:include page="wizard_tables_list.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_TABLES)&&(curSubStep.equals(AppConstants.WSS_ADD)||curSubStep.equals(AppConstants.WSS_EDIT))) { %>
		 <jsp:include page="wizard_tables_edit.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_COLUMNS)&&curSubStep.equals("")) { %>
		 <jsp:include page="wizard_columns_list.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_COLUMNS)&&curSubStep.equals(AppConstants.WSS_ADD_MULTI)) { %>
		 <jsp:include page="wizard_columns_add_multi.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_COLUMNS)&&curSubStep.equals(AppConstants.WSS_ORDER_ALL)) { %>
		 <jsp:include page="wizard_columns_order_all.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_COLUMNS)&&(curSubStep.equals(AppConstants.WSS_ADD)||curSubStep.equals(AppConstants.WSS_EDIT) ||curSubStep.equals(AppConstants.WA_MODIFY))) { %>
		 <jsp:include page="wizard_columns_edit.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_FORM_FIELDS)&&curSubStep.equals("")||curSubStep.equals(AppConstants.WSS_ADD_BLANK)) { %>
		 <jsp:include page="wizard_form_fields_list.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_FORM_FIELDS)&&(curSubStep.equals(AppConstants.WSS_ADD)||curSubStep.equals(AppConstants.WSS_EDIT))) { %>
		 <jsp:include page="wizard_form_fields_edit.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_FILTERS)&&curSubStep.equals("")) { %>
		<jsp:include page="wizard_filters_list.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_FILTERS)&&(curSubStep.equals(AppConstants.WSS_ADD)||curSubStep.equals(AppConstants.WSS_EDIT))) { %>
		  <jsp:include page="wizard_filters_edit.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_SORTING)&&curSubStep.equals("")) { %>
		 <jsp:include page="wizard_sorting_list.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_SORTING)&&curSubStep.equals(AppConstants.WSS_ORDER_ALL)) { %>
		<jsp:include page="wizard_sorting_order_all.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_SORTING)&&(curSubStep.equals(AppConstants.WSS_ADD)||curSubStep.equals(AppConstants.WSS_EDIT))) { %>
		 <jsp:include page="wizard_sorting_edit.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_JAVASCRIPT)) { %>
		 <jsp:include page="wizard_javascript.jsp"/>		 
<%	} else if(curStep.equals(AppConstants.WS_CHART)) { %>
		 <jsp:include page="wizard_chart.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_USER_ACCESS)) { %>
		 <jsp:include page="wizard_user_access.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_SCHEDULE)) { %>
		 <jsp:include page="wizard_schedule.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_REPORT_LOG)) { %>
		 <jsp:include page="wizard_log.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_MAP)) { %>
		 <jsp:include page="wizard_map.jsp"/>
<%	} else if(curStep.equals(AppConstants.WS_DATA_FORECASTING)) { %>
		 <jsp:include page="wizard_data_forecasting.jsp"/>
<%	} else { %>	
		 <jsp:include page="wizard_run.jsp"/>
<%	} %>
	
</td></tr><tr><td>
	<table width="100%" border="0" cellspacing="1" cellpadding="3">
		<tr>
			<td width="10%" align="right">
				&nbsp;
			</td>
			<td align=center>
				<% if(! ws.isInitialStep()) { %>
				<button type="submit" onClick="document.getElementById('<%= AppConstants.RI_WIZARD_ACTION %>').value='<%= AppConstants.WA_BACK %>';document.forma.submit();" att-button btn-type="primary" size="small" title='Back'>Back</button>
				<% } %>
				
				<% if(! ws.isFinalStep()) { %>
				<button type="submit" onClick="if(! dataValidate()) return false;  document.getElementById('<%= AppConstants.RI_WIZARD_ACTION %>').value='<%= AppConstants.WA_SAVE %>';document.forma.submit();" att-button btn-type="primary" size="small" title='Save'>Save</button>
				<button type="submit" onClick="if(! dataValidate()) return false;  document.getElementById('<%= AppConstants.RI_WIZARD_ACTION %>').value='<%= AppConstants.WA_NEXT %>';document.forma.submit();" att-button btn-type="primary" size="small" title='Next'>Next</button>
				<% } %>
			</td>
		</tr>
	</table>	
</td></tr>
</table>	

</form>

<script type="text/javascript">
  document.title += ' <%=title%>';
</script>

 <jsp:include page="footer.jsp" flush="true" />

</div>
</td>
</tr>
</table>
<%--<jsp:include page="disclaimer.jsp" flush="true" />--%>
<script>
	//document.getElementById('loadingMessageDiv').style.display='none';
    //adding the bread crumb on the folder tree
	if (window.parent && window.parent.addBreadCrumb)
		window.parent.addBreadCrumb(true, "000<%=rdef.getReportID()%>");
	//displayTree("000<%=rdef.getReportID()%>");
	<%
		String isTreeHidden = "true";
		if(request.getSession().getAttribute("isTreeHidden") != null && ((String)request.getSession().getAttribute("isTreeHidden")).equals("true")){
			isTreeHidden = "true";
		}else{
			isTreeHidden = "false";
		}
	%>
	//if ("<%=isTreeHidden%>" == "true"){
	//	hideTree();
	//}
	//document.getElementById('contentDiv').style.height=document.body.offsetHeight - 210;
	function resizeDivScrollbar(){
		var parentBody = window.parent.document.body;
		var parentMenu = window.parent.document.getElementById("application"); 
		var searchTD = document.getElementById("contentDiv"); 
		var contentIframe = window.parent.document.getElementById("content_Iframe");
		//parentBody.style.width = window.screen.size; 
		//searchTD.style.width  = parentBody.clientWidth - 200;
		//parentMenu.style.width = 1263;
		//javascript:resizeTo(screen.availWidth,screen.availHeight);
		if(parentMenu) parentMenu.style.width = screen.availWidth - 150;
		if(contentIframe) contentIframe.style.width = screen.availWidth - 30;
	}
	resizeDivScrollbar();
</script>
<%!	private String HTMLEncode(String value) {
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
	
	private String clearSpaces(String value) {
		StringBuffer sb = new StringBuffer(value);
		
		for(int i=0; i<sb.length(); i++)
			if(sb.charAt(i)==' ')
				sb.replace(i, i+1, "&nbsp;");
   		
		return sb.toString();
	}   // clearSpaces
	
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } %>

 
