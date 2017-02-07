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
 Name: wizard_definition.jsp
 Use : Shows edit page of the meta information of the report. 
 
 Change Log
 ==========
 
 14-Jul-2009 : Version 8.4 (Sundar); 
 				
 				<UL> 
 				<LI> Schedule functionality is available for Dashboard.</LI>
 				</UL>
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
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.Reports"%>
<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    WizardSequence ws = rdef.getWizardSequence();
    String curSubStep = ws.getCurrentSubStep();
   	String reportID   = rdef.getReportID();
	String dbInfo = null;
	dbInfo = rdef.getDBInfo();
	HashMap hashMap = ReportLoader.loadReportsToAddInDashboard(request);
	Set mapSet = hashMap.entrySet();
	Map.Entry me;
	
	HashMap pdfImgMap = ReportLoader.loadPDFImgLookUp();
%>
<% /*boolean displayAdditionalFields = AppUtils.getRequestNvlValue(request, "showAdditionalFields").equals("Y")||
										AppUtils.getRequestNvlValue(request, "additionalFieldsShown").equals("Y")||
										rdef.isRuntimeColSortDisabled()||
										rdef.isDisplayOptionHideForm()||
										rdef.isDisplayOptionHideChart()||
										rdef.isDisplayOptionHideData()||
										rdef.isDisplayOptionHideBtns()||
										(rdef.getNumFormColsAsInt()>1)||
										(nvl(rdef.getReportTitle()).length()>0)||
										(nvl(rdef.getReportSubTitle()).length()>0)||
										(nvl(rdef.getReportHeader()).length()>0)||
										(nvl(rdef.getReportFooter()).length()>0); */%>
										
										
<% 
   boolean displayDashboard =  false;
   /*displayDashboard = 	AppUtils.getRequestNvlValue(request, "showDashboardOptions").equals("Y")||
					  	AppUtils.getRequestNvlValue(request, "dashboardOptionsShown").equals("Y")||
					  	rdef.isDashboardOptionHideBtns()||
						rdef.isDashboardOptionHideChart()||
						rdef.isDashboardOptionHideData();
   */
   //displayDashboard = 	AppUtils.getRequestNvlValue(request, "showDashboardOptions").equals("Y");
   boolean dashboard = rdef.isDashboardType();
   //if(AppUtils.getRequestNvlValue(request, "showDashboardOptions").length()>0) dashboard=displayDashboard;
%>	
<script language="JavaScript" src="<%= AppUtils.getBaseFolderURL() %>js/script.js"></script>
<script language="JavaScript" src="<%= AppUtils.getBaseFolderURL() %>js/raptor.js"></script>
<script language="JavaScript" src="<%= AppUtils.getBaseFolderURL() %>js/editabledropdown.js"></script>

<script type="text/javascript" language="JavaScript">
<!-- Copyright 2006,2007 Bontrager Connection, LLC
// http://bontragerconnection.com/ and http://willmaster.com/
// Version: July 28, 2007
var cX = 0; var cY = 0; var rX = 0; var rY = 0;
function UpdateCursorPosition(e){ cX = e.pageX; cY = e.pageY;}
function UpdateCursorPositionDocAll(e){ cX = event.clientX; cY = event.clientY;}
if(document.all) { document.onmousemove = UpdateCursorPositionDocAll; }
else { document.onmousemove = UpdateCursorPosition; }
function AssignPosition(d) {
if(self.pageYOffset) {
	rX = self.pageXOffset;
	rY = self.pageYOffset;
	}
else if(document.documentElement && document.documentElement.scrollTop) {
	rX = document.documentElement.scrollLeft;
	rY = document.documentElement.scrollTop;
	}
else if(document.body) {
	rX = document.body.scrollLeft;
	rY = document.body.scrollTop;
	}
if(document.all) {
	cX += rX; 
	cY += rY;
	}
d.style.left = (cX+10) + "px";
d.style.top = (cY+10) + "px";
}
function HideContent(d) {
if(d.length < 1) { return; }
document.getElementById(d).style.display = "none";
}
function HideAllContent() {
	var uniquearrays = new Array();
	uniquearrays[0] = "uniquename1";
	uniquearrays[1] = "uniquename2";
	uniquearrays[2] = "uniquename3";
	uniquearrays[3] = "uniquename4";

	for (i=0;i<uniquearrays.length;i++)	{
 	    document.getElementById(uniquearrays[i]).style.display = "none";
	}
}
function ShowContent(d) {
	var uniquearrays = new Array();
	uniquearrays[0] = "uniquename1";
	uniquearrays[1] = "uniquename2";
	uniquearrays[2] = "uniquename3";
	uniquearrays[3] = "uniquename4";
	
	for (i=0;i<uniquearrays.length;i++)	{
	     //if(uniquearrays[i]==d) {
 	      document.getElementById(uniquearrays[i]).style.display = "none";
 	     //}
	}
if(d.length < 1) { return; }
var dd = document.getElementById(d);
AssignPosition(dd);
dd.style.display = "block";
}
function ShowContentWAssign(d) {
if(d.length < 1) { return; }
var dd = document.getElementById(d);
dd.style.display = "block";
}
function ReverseContentDisplay(d) {
if(d.length < 1) { return; }
var dd = document.getElementById(d);
AssignPosition(dd);
if(dd.style.display == "none") { dd.style.display = "block"; }
else { dd.style.display = "none"; }
}
//var popupwin ;
function showPreview(src)
{
	try{
		
		//if(popupwin != null){popupwin.focus();popupwin.close();}
		/*
	    var ww = 300, hh = 300;
		var LeftPosition = (screen.width) ? (screen.width-ww)/2 : 0;
		var TopPosition = (screen.height) ? (screen.height-hh)/2 : 0;
		var popupwin = window.open ('about:blank',"fusionPopupWindow","menubar=0,resizable=0,height="+hh+",width="+ww+",top="+TopPosition+",left="+LeftPosition+",scrollbars=yes");
	
		
		popupwin.document.write('<html><body align=center>');
		popupwin.document.write('<style>table{border-width:thin; border-color:black}');
		popupwin.document.write('</style>');
		
		popupwin.document.write($('dashboardPreview').value);
		popupwin.document.write('</body></html>');
		
		popupwin.document.title = 'Preview';
		popupwin.focus();
                */

		if($('PreviewButton').value == 'Preview')
		{
		$('tempHidden').innerHTML = $('editingArea').innerHTML;

		$('editingArea').innerHTML = $('dashboardPreview').value;

		$('PreviewButton').value = 'Edit';
		}else if($('PreviewButton').value == 'Edit')
		{
			$('editingArea').innerHTML = $('tempHidden').innerHTML;
			$('PreviewButton').value = 'Preview';

		}
	}catch(e){alert(e.message);}
    return false;

}

function insertAtCursor(myField, myValue) {
	  //IE support
	  if (document.selection) {
	    myField.focus();
	    sel = document.selection.createRange();
	    sel.text = myValue;
	  }
	  //MOZILLA/NETSCAPE support
	  else if (myField.selectionStart || myField.selectionStart == '0') {
	    var startPos = myField.selectionStart;
	    var endPos = myField.selectionEnd;
	    myField.value = myField.value.substring(0, startPos)
	                  + myValue
	                  + myField.value.substring(endPos, myField.value.length);
	  } else {
	    myField.value += myValue;
	  }
	}


function udpateTemplate(val)
{
	$('dashboardPreview').value = layoutTemplates[val];
}
function addType(type)
{
	try{

	//alert($('dashboardTemplateReports').selectedIndex);
	
	if($('dashboardTemplateReports').selectedIndex == 0){ return; }

	insertAtCursor($('dashboardPreview'),'['+type+'#'+$('dashboardTemplateReports').value+']');
		
	}catch(e){alert(e.message);}
	return false;
}
var layoutTemplates = {
		"empty": " ",
		"2x2": "<table border=1 width='100%' height='100%'><tr><td>[Report]</td><td>[Report]</td></tr><tr><td>[Report]</td><td>[Report]</td></tr></table>",
		"2x1": "<table border=1 width='100%' height='100%'><tr><td>[Report]</td></tr><tr><td>[Report]</td></tr></table>",
		"1x2": "<table border=1 width='100%' height='100%'><tr><td>[Report]</td><td>[Report]</td></tr></table>",
		"3x1": "<table border=1  width='100%' height='100%'><tr><td>[Report]</td></tr><tr><td>[Report]</td></tr><tr><td>[Report]</td></tr></table>",
		"custom1": "<table border=1  width='100%' height='100%'><tr><td colspan='2'>[Report]</td></tr><tr><td>[Report]</td><td>[Report]</td></tr></table>",
		"custom2": "<table border=1  width='100%' height='100%'><tr rowspan='2'><td>[Report]</td></tr><tr><td>[Report]</td><td>[Report]</td></tr></table>",
		"custom3": "<table border=1  width='100%' height='100%'><tr><td>[Report]</td></tr><tr><td>[Report]</td></tr></table>"
}
//-->
</script>
<script type="text/javascript">
	function show(object,val) {
		document.getElementById(object).style.visibility = val;
	}
</script>
<table width="100%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 colspan=2 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %></b></td>
	</tr>
	<% if(nvl(rdef.getReportID()).length()>0 && (! rdef.getReportID().equals("-1"))) { %>
	<tr>
		<td class=rbg2 height="30" align="right" width="35%">
			<font class=rtabletext>Report ID: </font>
		</td> 
		<td align="left" width="65%" class=rbg3>
			<font class=rtabletext><%= rdef.getReportID() %></font>
		</td>
	</tr> 
		<% } %>
	<tr>
		<td class=rbg2 height="30" align="right" width="35%" style="background-image:url(<%= AppUtils.getImgFolderURL() %>required.gif); background-position:top right; background-repeat:no-repeat;">
			<font class=rtabletext>Report Name: </font>
		</td> 
		<td align="left" width="65%" class=rbg3>
 			<input type="text" class="rtabletext" size="40" style="width: 200px;" maxlength="100" id="reportName" name="reportName" value="<%= (nvl(AppUtils.getRequestNvlValue(request, "reportName")).length() > 0)? 
			(!(AppUtils.getRequestNvlValue(request, "reportName").equals(rdef.getReportName()))?
                            AppUtils.getRequestNvlValue(request, "reportName"):rdef.getReportName()):
                                 rdef.getReportName() %>">
		</td>
	</tr> 
	<input type="hidden" name="folder_id" 
			   value="<%= (nvl(AppUtils.getRequestNvlValue(request, "folder_id")).length() > 0)? 
					     (!(AppUtils.getRequestNvlValue(request, "folder_id").equals(rdef.getFolderId()))?
					    		  AppUtils.getRequestNvlValue(request, "folder_id"):rdef.getFolderId()):
					    			  rdef.getFolderId() %>">
	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Report Description: </font>
		</td> 
		<td class=rbg3 align="left">
			<textarea name="reportDescr" class="rtabletext" style="width: 200px;" cols="40" rows="3"><%= (nvl(AppUtils.getRequestNvlValue(request, "reportDescr")).length() > 0)? 
					     (!(AppUtils.getRequestNvlValue(request, "reportDescr").equals(rdef.getReportDescr()))?
					    		  AppUtils.getRequestNvlValue(request, "reportDescr"):rdef.getReportDescr()):
                                                              rdef.getReportDescr() %></textarea>
		</td>
	</tr>
	
	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Report Type </font>
		</td> 
		<td class=rbg3 align="left">
		<% if(nvl(rdef.getReportType()).length()>0) { %>
			<font class=rtabletext><%= rdef.getReportType().equals(AppConstants.RT_LINEAR)?"Linear":(rdef.getReportType().equals(AppConstants.RT_CROSSTAB)?"Cross-tab":(rdef.getReportType().equals(AppConstants.RT_DASHBOARD)?"Dashboard":rdef.getReportType())) %></font>
			<input type="hidden" id="reportType"  name="reportType" value="<%= rdef.getReportType() %>">
		<% } else { %>
			<select id="reportType"  name="reportType" style="width: 200px;" onChange="document.forma.<%= AppConstants.RI_GO_TO_STEP %>.value='<%= rdef.getWizardSequence().getCurrentStep() %>'; document.forma.submit();">
			    <option value="-1"> Select Type </option>
				<option value="<%= AppConstants.RT_LINEAR %>" <%= AppUtils.getRequestNvlValue(request, "reportType").equals(AppConstants.RT_LINEAR) ? " selected":"selected" %>> Linear </option>
<!-- 				<option value="<%= AppConstants.RT_CROSSTAB %>"<%= AppUtils.getRequestNvlValue(request, "reportType").equals(AppConstants.RT_CROSSTAB) ? " selected":"" %>> Cross-tab </option>
				<option value="<%= AppConstants.RT_DASHBOARD %>"<%= AppUtils.getRequestNvlValue(request, "reportType").equals(AppConstants.RT_DASHBOARD) ? " selected":"" %>> Dashboard </option>
				<option value="<%= AppConstants.RT_HIVE %>"<%= AppUtils.getRequestNvlValue(request, "reportType").equals(AppConstants.RT_HIVE) ? " selected":"" %>> Hive Based Report </option>				
 -->								
			</select>
		<% } %>
		</td>
	</tr>
	<% if((AppUtils.getRequestNvlValue(request, "reportType").length() > 0) || (rdef.getReportType().length() > 0)) { %>
	<!-- Dashboard Begin -->
    <% if (AppUtils.getRequestNvlValue(request, "reportType").equals(AppConstants.RT_DASHBOARD) || rdef.getReportType().equals(AppConstants.RT_DASHBOARD))  { %>
	<tr>
		<td class=rbg2 height="30" align="right"><font class=rtabletext>Select HTML Template:</font></td>
		<td><select id="dashboardTemplate" style="width: 200px;" name="dashboardTemplate" onchange="return udpateTemplate(this.value);">
			<option value="empty">-- select --</option>
			<option value="2x2">2 Rows x 2 Columns</option>
			<option value="2x1">2 Rows x 1 Column</option>
			<option value="1x2">1 Row x 2 Columns</option>
			<option value="3x1">3 Rows x 1 Column</option>
			<option value="custom1">2 Rows with 1st Row with 1 column and 2nd Row with 2 Columns</option>
			<option value="custom2">2 Rows with 1st Column expanded to 2 rows</option>
			<option value="custom3">Hybrid Layout</option>
		</select></td>
	</tr>       
       <tr>
		<td class=rbg2 height="30" align="right"><font class=rtabletext>Dashboard HTML:</font></td>

		<td>
		<div id='editingArea' style="width: 600px; height: 200px; overflow: none">
		<textarea id='dashboardPreview' name="dashboardLayoutHTML" style="width: 200px; height: 100%">
                 <% if(rdef != null && nvl(rdef.getDashboardLayoutHTML()).length()>0 ) {%><%= rdef.getDashboardLayoutHTML().trim() %><%} %>
                 </textarea></div>
		<div id='tempHidden' style="display: none;"></div>
		<br/><select name="dashboardTemplateReports">
			<option value="-1" selected>-->select report <--</option>
			<%
				for (Iterator iter = mapSet.iterator(); iter.hasNext();) {
							me = (Map.Entry) iter.next();
			%>
			<%
				if (rdef != null && rdef.getDashBoardReports() != null && (rdef.getDashBoardReports().getReportsList().get(0) != null)
									&& (((Reports) rdef.getDashBoardReports().getReportsList().get(0)).getReportId().equals((String) me.getKey()))) {
			%>
			<option value="<%=(String) me.getKey()%>" selected><%=(String) me.getValue()%></option>
			<%
				} else {
			%>
			<option value="<%=(String) me.getKey()%>"><%=(String) me.getValue()%></option>
			<%
				}
			%>
			<%
				}
			%>
		</select>
		&nbsp;<input type="button" class='button' onclick="return addType();" value='Insert Report Id' />
		&nbsp;<input id='PreviewButton' style="width: 100px" type='button' class='button' value="Preview" onclick="return showPreview(this);" />
		<br />
		</td>
	</tr>
    
 	   
  	   <!-- Dashboard Ends -->
  <% } else {  %>		  

	
		<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Data Source:</font>
		</td> 
		<td align="left" class=rbg3>
		  <% 
			 org.openecomp.portalsdk.analytics.util.RemDbInfo remDbInfo = new org.openecomp.portalsdk.analytics.util.RemDbInfo();
			 HashMap remDbMap = remDbInfo.getDbHash();
		  
		  %>
		  <% if (dbInfo != null && dbInfo.length()>0 && !dbInfo.equalsIgnoreCase("null") && !remDbMap.isEmpty()) { %>
			         <font class=rtabletext><%= (AppUtils.nvl(remDbInfo.getDesc(dbInfo)).length()>0)?remDbInfo.getDesc(dbInfo):" No Desc " %></font>
          <%
		  }
		  else { 
		  %>
		  <%-- dbInfo.equals(AppConstants.DB_PROD)? "selected":""  --%>
		  <select id = "selectDS" name="dataSource" style="width: 200px;">
		  <%
		        if(!remDbMap.isEmpty()) {
				for( Iterator itr=remDbMap.entrySet().iterator(); itr.hasNext(); ) {
					Map.Entry e = (Map.Entry)itr.next();
					String prefix = (String)e.getKey();
					String desc = (String)e.getValue();
           %>
  				    <option value='<%= prefix %>' 
  				     <% if(prefix.equals(nvl(AppUtils.getRequestNvlValue(request, "dataSource")))) { %>
  				      <%=(nvl(AppUtils.getRequestNvlValue(request, "dataSource")).length()>0?
  				    		  (
  				    		   prefix.equals(nvl(AppUtils.getRequestNvlValue(request, "dataSource")))?"selected ":
  				    				(prefix.equals(AppConstants.DB_LOCAL)?"selected ":"")
  				    		   )
  				    			:prefix.equals(AppConstants.DB_LOCAL)?"selected ":"")%>
  				    <% } %>		
	    				><%= desc%>
		   <%
				}
		        
		  %>
           <% } else { %>
		       <option value='<%= AppConstants.DB_LOCAL%>' selected>Default
		   <% } %>
		   
			</select>
		   <% if(!remDbMap.isEmpty() && nvl(AppUtils.getRequestNvlValue(request, "dataSource")).length() <= 0){ %>
		         <script language="Javascript">
		           var selectDS = document.getElementById("selectDS");
		           var flag = 0;
		           for (i = selectDS.length - 1; i>=0; i--) {
                     if (selectDS.options[i].selected) {
                        flag = 2;
                        break;
                     }
                   }
                   if(flag==2) {
                    for (i = selectDS.length - 1; i>=0; i--) {
                      if(selectDS.options[i].value == '<%= AppConstants.DB_LOCAL%>' ){
                        selectDS.options[i].selected = true;
                      }
                    } 
                   }
		         </script>
		       
		   <% } %>
			
       <% } %>
		</td>
	</tr> 
	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Form Help Text: </font>
		</td> 
		<td class=rbg3 align="left">
			<textarea name="formHelp" style="width: 200px;" cols="40" rows="3"><%=rdef.getFormHelpText()%></textarea>
		</td>
	</tr>

	<% if(Globals.getAllowSQLBasedReports()||AppUtils.isAdminUser(request)) { %>
	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Report Definition: </font>
		</td> 
		<td class=rbg3 align="left">
			<font class=rtabletext>
			<% if(rdef.getReportDefType().length()>0) { %>
				<%= rdef.getReportDefType().equals(AppConstants.RD_VISUAL)?"Visual":(rdef.getReportDefType().equals(AppConstants.RD_SQL_BASED)?"SQL-based":rdef.getReportDefType()) %>
			<% } else { %>
				<!-- <input type=radio name="reportDefType" value="<%= AppConstants.RD_VISUAL %>" <%= AppUtils.getRequestNvlValue(request, "reportDefType").equals(AppConstants.RD_VISUAL)?" checked": ((AppUtils.getRequestNvlValue(request, "reportDefType").length()<=0)?" checked":"") %>>Visual -->
				<input type=radio name="reportDefType" checked value="<%= AppConstants.RD_SQL_BASED %>" <%= AppUtils.getRequestNvlValue(request, "reportDefType").equals(AppConstants.RD_SQL_BASED) ? " checked":"" %>>SQL-based
				<!-- <input type=radio name="reportDefType" value="<%= AppConstants.RD_SQL_BASED_DATAMIN %>" <%= AppUtils.getRequestNvlValue(request, "reportDefType").equals(AppConstants.RD_SQL_BASED_DATAMIN) ? " checked":"" %>>Data Forecasting -->
			<% } %>
			</font>
		</td>
	</tr>
	<% }	// if 
	%>
	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Page Size: </font>
		</td> 
		<td class=rbg3 align="left">
			<select name="pageSize" style="width: 200px;" onKeyDown="fnKeyDownHandler(this, event);" onKeyUp="fnKeyUpHandler_A(this, event); return false;" onKeyPress = "return fnKeyPressHandler_A(this, event);"  onChange="fnChangeHandler_A(this, event);">
				<option value="" style="COLOR:#ff0000;BACKGROUND-COLOR:#ffff00;">Custom</option> <!-- This is the Editable Option -->
				<option value="10"<%=  (rdef.getPageSize()==10 )?" selected":"" %>>10
				<option value="25"<%=  (rdef.getPageSize()==25 )?" selected":"" %>>25
				<option value="50"<%=  (rdef.getPageSize()==50 )?" selected":"" %>>50
				<option value="100"<%= (rdef.getPageSize()==100)?" selected":"" %>>100
				<option value="500"<%= (rdef.getPageSize()==500)?" selected":"" %>>500
				<% if(rdef.getPageSize()!=10 && rdef.getPageSize()!=20 && rdef.getPageSize()!=50
				     && rdef.getPageSize()!=100 && rdef.getPageSize()!=500) {
				%>
				<option value="<%=rdef.getPageSize()%>" selected><%=rdef.getPageSize()%></option> <!-- This is the Editable Option -->
                <%				
					 } 
                %>						
			</select>
		</td>
	</tr>
	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Display Area: </font>
		</td> 
		<td class=rbg3 align="left" valign="top" nowrap>
			<select name="menuID" size="3" multiple style="width: 100px;">
			<!--  need one more blank to deselect since approval checkbox is taken out -->
			<option value="">
			<% for(int i=0; i<AppUtils.getQuickLinksMenuIDs().size(); i++) {
					String qMenu = (String) AppUtils.getQuickLinksMenuIDs().get(i); %>
				<option value="<%= qMenu %>"<%= rdef.checkMenuIDSelected(qMenu)?" selected":"" %>><%= AppUtils.getMenuLabel(qMenu) %>
			<% }	// for 
			%>
			</select>
<%-- 			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="checkbox" name="menuApproved" value="Y"<%= rdef.isMenuApproved()?" checked":"" %><%= AppUtils.isAdminUser(request)?"":" disabled onClick='checked="+(rdef.isMenuApproved()?"true":"false")+"';" %>>
			<font class=rtabletext>Approved?</font> --%>
		</td>
	</tr>
<%-- 	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Generate report in a New Window?</font>
		</td> 
		<td class=rbg3 align="left" nowrap>
			<input type="checkbox" name="reportInNewWindow" value="Y" 
			<%= rdef.isReportInNewWindow()? " checked " : "" %>>	
		</td>
	</tr> --%>

	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Hide Form fields after run?</font>
		</td> 
		<td class=rbg3 align="left" nowrap>
			<input type="checkbox" name="hideFormFieldsAfterRun" value="Y" 
			<%= rdef.isHideFormFieldAfterRun()? " checked " : "" %>>	
		</td>
	</tr>

<%-- 	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Display Folder Tree?</font>
		</td> 
		<td class=rbg3 align="left" nowrap>
			<input type="checkbox" name="displayFolderTree" value="Y" 
            <%= rdef.isDisplayFolderTree()? " checked " : "" %>			
			> 
		</td>
	</tr> --%>

	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext> Max Rows in Excel/CSV Download  </font>
		</td> 
		<td class=rbg3 align="left">
			<select name="excelDownloadSize" style="width: 80px;">
			    <option value="500"<%=  (rdef.getMaxRowsInExcelDownload()==500 )?" selected":"" %>>500
				<option value="1000"<%=  (rdef.getMaxRowsInExcelDownload()==1000 )?" selected":"" %>>1000
				<option value="2000"<%=  (rdef.getMaxRowsInExcelDownload()==2000 )?" selected":"" %>>2000
				<option value="3000"<%=  (rdef.getMaxRowsInExcelDownload()==3000 )?" selected":"" %>>3000
				<option value="4000"<%=  (rdef.getMaxRowsInExcelDownload()==4000 )?" selected":"" %>>4000
				<option value="5000"<%=  (rdef.getMaxRowsInExcelDownload()==5000 )?" selected":"" %>>5000
				<option value="10000"<%=  (rdef.getMaxRowsInExcelDownload()==10000 )?" selected":"" %>>10000
				<option value="15000"<%=  (rdef.getMaxRowsInExcelDownload()==15000 )?" selected":"" %>>15000
				<option value="20000"<%=  (rdef.getMaxRowsInExcelDownload()==20000 )?" selected":"" %>>20000
				<option value="25000"<%=  (rdef.getMaxRowsInExcelDownload()==25000 )?" selected":"" %>>25000
				<option value="30000"<%=  (rdef.getMaxRowsInExcelDownload()==30000 )?" selected":"" %>>30000
				<option value="35000"<%=  (rdef.getMaxRowsInExcelDownload()==35000 )?" selected":"" %>>35000
				<option value="40000"<%=  (rdef.getMaxRowsInExcelDownload()==40000)?" selected":"" %>>40000
				<option value="45000"<%=  (rdef.getMaxRowsInExcelDownload()==45000 )?" selected":"" %>>45000
				<option value="50000"<%=  (rdef.getMaxRowsInExcelDownload()==50000)?" selected":"" %>>50000
				<option value="65000"<%=  (rdef.getMaxRowsInExcelDownload()==65000)?" selected":"" %>>65000
			</select>
		</td>
	</tr>
	
	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Columns to be Frozen: </font>
		</td> 
		<td class=rbg3 align="left">
			<select name="frozenColumns" style="width: 50px;" onKeyDown="fnKeyDownHandler(this, event);" onKeyUp="fnKeyUpHandler_A(this, event); return false;" onKeyPress = "return fnKeyPressHandler_A(this, event);"  onChange="fnChangeHandler_A(this, event);">
				<option value="" style="COLOR:#ff0000;BACKGROUND-COLOR:#ffff00;">Custom</option> <!-- This is the Editable Option -->
				<option value="0"<%=  (rdef.getFrozenColumns()==0 )?" selected":"" %>>0
				<option value="1"<%=  (rdef.getFrozenColumns()==1 )?" selected":"" %>>1
				<option value="2"<%=  (rdef.getFrozenColumns()==2 )?" selected":"" %>>2
				<option value="3"<%= (rdef.getFrozenColumns()==3)?" selected":"" %>>3
				<option value="4"<%= (rdef.getFrozenColumns()==4)?" selected":"" %>>4
				<% if(rdef.getFrozenColumns()!=0 && rdef.getFrozenColumns()!=1 && rdef.getFrozenColumns()!=2
				     && rdef.getFrozenColumns()!=3 && rdef.getFrozenColumns()!=4) {
				%>
				<option value="<%=rdef.getFrozenColumns()%>" selected><%=rdef.getFrozenColumns()%></option> <!-- This is the Editable Option -->
                <%				
					 } 
                %>						
			</select>
		</td>
	</tr>
	
	<% if(rdef.getReportType().equals(AppConstants.RT_CROSSTAB)) { %>
	
				<tr>
					<td class=rbg2 height="30" align="right">
						<font class=rtabletext>Record # column width: </font>
					</td> 
					<td class=rbg3 align="left">
						<input type="text" class="rtabletext" style="width: 100px;" size="40" maxlength="100" id="widthNo" name="widthNo" value="<%= (nvl(AppUtils.getRequestNvlValue(request, "widthNo")).length() > 0)? 
			(!(AppUtils.getRequestNvlValue(request, "widthNo").equals(rdef.getWidthNoColumn()))?
                            AppUtils.getRequestNvlValue(request, "widthNo"):rdef.getWidthNoColumn()):
                            	rdef.getWidthNoColumn() %>">
					</td>
				</tr>
				
		
	
	<% } %>
	
	
				<tr>
					<td class=rbg2 height="30" align="right">
						<font class=rtabletext>Data Grid Align: </font>
					</td> 
					<td class=rbg3 align="left">
						<select name="dataGridAlign" style="width: 100px;">
							<option value="left"<%=  (nvl(rdef.getDataGridAlign()).length()>0 ? (rdef.getDataGridAlign().equals("left")  ? " selected": ""):" selected ")%>> Left
							<option value="right"<%=  (nvl(rdef.getDataGridAlign()).length()>0 ? (rdef.getDataGridAlign().equals("right")  ? " selected": ""):"")%>> Right
							<option value="center"<%=  (nvl(rdef.getDataGridAlign()).length()>0 ? (rdef.getDataGridAlign().equals("center")  ? " selected": ""):"")%>> Center
						
						</select>
					</td>
				</tr>

	<% 
	  if(pdfImgMap.size() > 0) {
	%>
	
		  <%-- dbInfo.equals(AppConstants.DB_PROD)? "selected":""  --%>
		  <tr> 
			<td class=rbg2 height="30" align="right">
				<font class=rtabletext>Select logo for PDF download: </font>
			</td> 
			<td>		  
		  <select id = "pdfImg" name="pdfImg" style="width: 100px;">
		    <option value=''> <!-- SELECT --></option>
		  <%
		        if(!pdfImgMap.isEmpty()) {
				for( Iterator itr=pdfImgMap.entrySet().iterator(); itr.hasNext(); ) {
					Map.Entry e = (Map.Entry)itr.next();
					String image_id = (String)e.getKey();
					String image_loc = (String)e.getValue();
           %>
           <%
			if (nvl(AppUtils.getRequestNvlValue(request, "pdfImg")).length()>0 && !(AppUtils.getRequestNvlValue(request, "pdfImg").equals(rdef.getPdfImg())) ) {
				if(image_loc .equals (AppUtils.getRequestNvlValue(request, "pdfImg"))) {
			%>
			
			<option value='<%= image_loc %>' selected> <%=image_id %></option>
			<%	
				} else {
            %> 					
					<option value='<%= image_loc %>'> <%=image_id %></option>
			<%		
           		} 
			} else {
				if(image_loc .equals (rdef.getPdfImg())) {
				%>
				
				<option value='<%= image_loc %>' selected> <%=image_id %> </option>
				<%	
				
			    } else {
		            %> 					
					<option value='<%= image_loc %>'> <%=image_id %></option>
			<%		
			    }
			}
           %>
           </select>
           </td>
		   <% }
		     }
		   %>
		  </tr>
		 <% 
		}
%>
 
	<tr>
		<td class=rbg2 height="30" align="right" width="35%">
			<font class=rtabletext>Empty message: </font>
		</td> 
		<td align="left" width="65%" class=rbg3>
 			<input type="text" class="rtabletext" style="width: 200px;" size="40" maxlength="100" id="emptyMessage" name="emptyMessage" value="<%= (nvl(AppUtils.getRequestNvlValue(request, "emptyMessage")).length() > 0)? 
			(!(AppUtils.getRequestNvlValue(request, "emptyMessage").equals(rdef.getEmptyMessage()))?
                            AppUtils.getRequestNvlValue(request, "emptyMessage"):rdef.getEmptyMessage()):
                                 rdef.getEmptyMessage() %>">
		</td>
	</tr> 
 
 <% } %>
	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext> Height of the Data Container  </font>
		</td> 
		<td class=rbg3 align="left">
  		    <% if (nvl(rdef.getDataContainerHeight(), "100").equals ("auto") || nvl(rdef.getDataContainerHeight(), "100").equals ("100")) { rdef.setDataContainerHeight("100"); } %> 
			<select name="heightContainer" style="width: 70px;" onKeyDown="fnKeyDownHandler(this, event);" onKeyUp="fnKeyUpHandler_A(this, event); return false;" onKeyPress = "return fnKeyPressHandler_A(this, event);"  onChange="fnChangeHandler_A(this, event);">
				<option value="" style="COLOR:#ff0000;BACKGROUND-COLOR:#ffff00;">Custom</option> <!-- This is the Editable Option -->
				<option value="200" <%=  (nvl(rdef.getDataContainerHeight(),"200").equals("200"))?" selected":"" %>>200%</option> 
				<option value="190" <%=  (nvl(rdef.getDataContainerHeight(),"190").equals("190"))?" selected":"" %>>190%</option> 
				<option value="180" <%=  (nvl(rdef.getDataContainerHeight(),"180").equals("180"))?" selected":"" %>>180%</option> 
				<option value="170" <%=  (nvl(rdef.getDataContainerHeight(),"170").equals("170"))?" selected":"" %>>170%</option> 
				<option value="160" <%=  (nvl(rdef.getDataContainerHeight(),"160").equals("160"))?" selected":"" %>>160%</option> 
				<option value="150" <%=  (nvl(rdef.getDataContainerHeight(),"150").equals("150"))?" selected":"" %>>150%</option> 
				<option value="140" <%=  (nvl(rdef.getDataContainerHeight(),"140").equals("140"))?" selected":"" %>>140%</option> 
				<option value="130" <%=  (nvl(rdef.getDataContainerHeight(),"130").equals("130"))?" selected":"" %>>130%</option> 
				<option value="120" <%=  (nvl(rdef.getDataContainerHeight(),"120").equals("120"))?" selected":"" %>>120%</option> 
				<option value="110" <%=  (nvl(rdef.getDataContainerHeight(),"110").equals("110"))?" selected":"" %>>110%</option> 
				<option value="100" <%=  (nvl(rdef.getDataContainerHeight(),"100").equals("100"))?" selected":"" %>>100%</option> 
				<option value="90" <%=  (nvl(rdef.getDataContainerHeight(),"90").equals("90"))?" selected":"" %>>90%</option> 
				<option value="80" <%=  (nvl(rdef.getDataContainerHeight(),"80").equals("80"))?" selected":"" %>>80%</option> 
				<option value="70" <%=  (nvl(rdef.getDataContainerHeight(),"70").equals("70"))?" selected":"" %>>70%</option> 
				<option value="60" <%=  (nvl(rdef.getDataContainerHeight(),"60").equals("60"))?" selected":"" %>>60%</option> 
				<option value="50" <%=  (nvl(rdef.getDataContainerHeight(),"50").equals("50"))?" selected":"" %>>50%</option> 
				<option value="40" <%=  (nvl(rdef.getDataContainerHeight(),"40").equals("40"))?" selected":"" %>>40%</option> 
				<option value="30" <%=  (nvl(rdef.getDataContainerHeight(),"30").equals("30"))?" selected":"" %>>30%</option> 
				<option value="20" <%=  (nvl(rdef.getDataContainerHeight(),"20").equals("20"))?" selected":"" %>>20%</option> 
				<option value="10" <%=  (nvl(rdef.getDataContainerHeight(),"10").equals("10"))?" selected":"" %>>10%</option>
				
				 
				<% if(!((new Integer(nvl(rdef.getDataContainerHeight(), "100")).intValue()%10 == 0) && (new Integer(nvl(rdef.getDataContainerHeight(), "100")).intValue() <= 200)) ) {
				%>
				<option value="<%=rdef.getDataContainerHeight()%>" selected><%=rdef.getDataContainerHeight()%>%</option> 
                <%				
					 } 
                %>						
		</td>
	</tr>	

	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext> Width of the Data Container  </font>
		</td> 
		<td class=rbg3 align="left">
  		    <% if (nvl(rdef.getDataContainerWidth(), "100").equals ("auto") || nvl(rdef.getDataContainerWidth(), "100").equals ("100")) { rdef.setDataContainerWidth("100"); } %> 
			<select name="widthContainer" style="width: 70px;" onKeyDown="fnKeyDownHandler(this, event);" onKeyUp="fnKeyUpHandler_A(this, event); return false;" onKeyPress = "return fnKeyPressHandler_A(this, event);"  onChange="fnChangeHandler_A(this, event);">
				<option value="" style="COLOR:#ff0000;BACKGROUND-COLOR:#ffff00;">Custom</option> <!-- This is the Editable Option -->
				<option value="200" <%=  (nvl(rdef.getDataContainerWidth(),"200").equals("200"))?" selected":"" %>>200%</option> 
				<option value="190" <%=  (nvl(rdef.getDataContainerWidth(),"190").equals("190"))?" selected":"" %>>190%</option> 
				<option value="180" <%=  (nvl(rdef.getDataContainerWidth(),"180").equals("180"))?" selected":"" %>>180%</option> 
				<option value="170" <%=  (nvl(rdef.getDataContainerWidth(),"170").equals("170"))?" selected":"" %>>170%</option> 
				<option value="160" <%=  (nvl(rdef.getDataContainerWidth(),"160").equals("160"))?" selected":"" %>>160%</option> 
				<option value="150" <%=  (nvl(rdef.getDataContainerWidth(),"150").equals("150"))?" selected":"" %>>150%</option> 
				<option value="140" <%=  (nvl(rdef.getDataContainerWidth(),"140").equals("140"))?" selected":"" %>>140%</option> 
				<option value="130" <%=  (nvl(rdef.getDataContainerWidth(),"130").equals("130"))?" selected":"" %>>130%</option> 
				<option value="120" <%=  (nvl(rdef.getDataContainerWidth(),"120").equals("120"))?" selected":"" %>>120%</option> 
				<option value="110" <%=  (nvl(rdef.getDataContainerWidth(),"110").equals("110"))?" selected":"" %>>110%</option> 
				<option value="100" <%=  (nvl(rdef.getDataContainerWidth(),"100").equals("100"))?" selected":"" %>>100%</option> 
				<option value="90" <%=  (nvl(rdef.getDataContainerWidth(),"90").equals("90"))?" selected":"" %>>90%</option> 
				<option value="80" <%=  (nvl(rdef.getDataContainerWidth(),"80").equals("80"))?" selected":"" %>>80%</option> 
				<option value="70" <%=  (nvl(rdef.getDataContainerWidth(),"70").equals("70"))?" selected":"" %>>70%</option> 
				<option value="60" <%=  (nvl(rdef.getDataContainerWidth(),"60").equals("60"))?" selected":"" %>>60%</option> 
				<option value="50" <%=  (nvl(rdef.getDataContainerWidth(),"50").equals("50"))?" selected":"" %>>50%</option> 
				<option value="40" <%=  (nvl(rdef.getDataContainerWidth(),"40").equals("40"))?" selected":"" %>>40%</option> 
				<option value="30" <%=  (nvl(rdef.getDataContainerWidth(),"30").equals("30"))?" selected":"" %>>30%</option> 
				<option value="20" <%=  (nvl(rdef.getDataContainerWidth(),"20").equals("20"))?" selected":"" %>>20%</option> 
				<option value="10" <%=  (nvl(rdef.getDataContainerWidth(),"10").equals("10"))?" selected":"" %>>10%</option>
				
				 
				<% if(!((new Integer(nvl(rdef.getDataContainerWidth(), "100")).intValue()%10 == 0) && (new Integer(nvl(rdef.getDataContainerWidth(), "100")).intValue() <= 200)) ) {
				%>
				<option value="<%=rdef.getDataContainerWidth()%>" selected><%=rdef.getDataContainerWidth()%>%</option> 
                <%				
					 } 
                %>						
		</td>
	</tr>	

 
	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext> Allow Schedule?  </font>
		</td> 
		<td class=rbg3 align="left">
           <input type="checkbox" class ="rtabletext" size="4" name="allowSchedule" value="Y" <%=((rdef.getAllowSchedule()!=null)?(rdef.getAllowSchedule().toUpperCase().charAt(0)== 'Y' ?" checked":""): (ReportLoader.isReportsAlreadyScheduled(rdef.getReportID())?" checked":"")) %>/>
		</td>
	</tr>	

<%--    <tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext> Column Multi Group?  </font>
		</td> 
		<td class=rbg3 align="left">
           <input type="checkbox" class ="rtabletext" size="4" name="multiGroupColumn" value="Y" <%=(rdef.getMultiGroupColumn()!=null)?(rdef.getMultiGroupColumn().toUpperCase().charAt(0)== 'Y' ?" checked":""): "" %>/>
		</td>
	</tr> --%>	
   </tr>

  <% if (!(AppUtils.getRequestNvlValue(request, "reportType").equals(AppConstants.RT_DASHBOARD) || rdef.getReportType().equals(AppConstants.RT_DASHBOARD)))  { %>	

<%-- 	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext> Render Top - Down  </font>
		</td> 
		<td class=rbg3 align="left">
           <input type="checkbox" class ="rtabletext" size="4" name="topDown" value="Y" <%=(rdef.getTopDownOption()!=null)?(rdef.getTopDownOption().toUpperCase().charAt(0)== 'Y' ?" checked":""):(AppUtils.getRequestNvlValue(request, "topDown").equals("Y")?" checked":"") %>/>
		</td>
	</tr>	 --%>

	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext> Sized By Content  </font>
		</td> 
		<td class=rbg3 align="left">
           <input type="checkbox" class ="rtabletext" size="4" name="sizedByContent" value="Y" <%=(rdef.getSizedByContentOption()!=null)?(rdef.getSizedByContentOption().toUpperCase().charAt(0)== 'Y' ?" checked":""):(AppUtils.getRequestNvlValue(request, "sizedByContent").equals("Y")?" checked":"") %>/>
		</td>
	</tr>	

	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Display Options: </font>
		</td> 
		<td class=rbg3 align="left" nowrap>
			<input type="checkbox" name="hideForm" value="Y"<%= rdef.isDisplayOptionHideForm()?" checked":"" %>>
			<font class=rtabletext>Hide Form Fields</font>
			<input type="checkbox" name="hideChart" value="Y"<%= rdef.isDisplayOptionHideChart()?" checked":"" %>>
			<font class=rtabletext>Hide Chart</font>
			<input type="checkbox" name="hideData" value="Y"<%= rdef.isDisplayOptionHideData()?" checked":"" %>>
			<font class=rtabletext>Hide Report Data</font>
			<input type="checkbox" name="hideBtns" value="Y"<%= rdef.isDisplayOptionHideBtns()?" checked":"" %>>
			<font class=rtabletext>Hide Download Buttons</font>
			<input type="checkbox" name="hideMap" value="Y"<%= rdef.isDisplayOptionHideMap()?" checked":"" %>>
			<font class=rtabletext>Hide Map</font>
			<input type="checkbox" name="hideExcelIcons" value="Y"<%= rdef.isDisplayOptionHideExcelIcons()?" checked":"" %>>
			<font class=rtabletext>Hide Excel Icons</font>
			<input type="checkbox" name="hidePDFIcons" value="Y"<%= rdef.isDisplayOptionHidePDFIcons()?" checked":"" %>>
			<font class=rtabletext>Hide PDF Icons</font>
		</td>
	</tr>

	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>&nbsp; </font>
		</td> 
		<td class=rbg3 align="left" nowrap>
			<input type="checkbox" name="runtimeColSortDisabled" value="Y"<%= rdef.isRuntimeColSortDisabled()?" checked":"" %>>
			<font class=rtabletext>Disable column sort at runtime?</font>
		</td>
	</tr>
	
	
	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Run-time Form Number Columns: </font>
		</td> 
		<td class=rbg3 align="left">
			<select name="numFormCols" style="width: 70px;">
				<option value="1"<%= (rdef.getNumFormColsAsInt()==1)?" selected":"" %>>1
				<option value="2"<%= (rdef.getNumFormColsAsInt()==2)?" selected":"" %>>2
				<option value="3"<%= (rdef.getNumFormColsAsInt()==3)?" selected":"" %>>3
				<option value="4"<%= (rdef.getNumFormColsAsInt()==4)?" selected":"" %>>4
			</select>
		</td>
	</tr>
	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Report Title<br>(if blank, the Report Name will be displayed): </font>
		</td> 
		<td class=rbg3 align="left">
			<textarea name="reportTitle" style="width: 200px;" cols="40" rows="3"><%= nvl(rdef.getReportTitle()) %></textarea>
		</td>
	</tr>
	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Report Sub-Title: </font>
		</td> 
		<td class=rbg3 align="left">
			<textarea name="reportSubTitle" style="width: 200px;" cols="40" rows="3"><%= nvl(rdef.getReportSubTitle()) %></textarea>
		</td>
	</tr>
<%-- 	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Page Header (HTML): </font>
		</td> 
		<td class=rbg3 align="left">
			<textarea name="reportHeader" style="width: 200px;" cols="40" rows="3"><%= Utils.htmlEncode(nvl(rdef.getReportHeader())) %></textarea>
		</td>
	</tr>
	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Page Footer (HTML): </font>
		</td> 
		<td class=rbg3 align="left">
			<textarea name="reportFooter" style="width: 200px;" cols="40" rows="3"><%= Utils.htmlEncode(nvl(rdef.getReportFooter())) %></textarea>
		</td>
	</tr> --%>
	<tr>
		<td class=rbg2 height="30" align="right">
			<font class=rtabletext>Allowed Recurrance in Schedule: </font>
		</td> 
		<td class=rbg3 align="left">
			<input type="checkbox" name="isOneTimeScheduleAllowed" value="Y" <%if (rdef.getIsOneTimeScheduleAllowed() == null || rdef.getIsOneTimeScheduleAllowed().equals("Y")){ %>checked <%} %>>One Time<br>
			<input type="checkbox" name="isHourlyScheduleAllowed" value="Y" <%if (rdef.getIsHourlyScheduleAllowed() == null || rdef.getIsHourlyScheduleAllowed().equals("Y")){ %>checked<%} %> >Hourly<br>
			<input type="checkbox" name="isDailyScheduleAllowed" value="Y" <%if (rdef.getIsDailyScheduleAllowed() == null || rdef.getIsDailyScheduleAllowed().equals("Y")){ %>checked<%} %> >Daily<br>
			<input type="checkbox" name="isDailyMFScheduleAllowed" value="Y" <%if (rdef.getIsDailyMFScheduleAllowed() == null || rdef.getIsDailyMFScheduleAllowed().equals("Y")){ %>checked<%} %> >Daily Monday - Friday<br>
			<input type="checkbox" name="isWeeklyScheduleAllowed" value="Y" <%if (rdef.getIsWeeklyScheduleAllowed() == null || rdef.getIsWeeklyScheduleAllowed().equals("Y")){ %>checked<%} %> >Weekly<br>
			<input type="checkbox" name="isMonthlyScheduleAllowed" value="Y" <%if (rdef.getIsMonthlyScheduleAllowed() == null || rdef.getIsMonthlyScheduleAllowed().equals("Y")){ %>checked<%} %> >Monthly<br>			
		</td>
	</tr>
	<input type="hidden" name="additionalFieldsShown" value="Y">
<% } 	// else 
%>

<% } // if report type is empty %>

</table>
<br>

	<script language="JavaScript">
	<!--
	<% 
		Vector reportIdNames = ReportLoader.getUserReportNames(request); %>
		var reportIds   = new Array(<%= reportIdNames.size() %>);
		var reportNames = new Array(<%= reportIdNames.size() %>);
		<% for(int i=0; i<reportIdNames.size(); i++) { 
			IdNameValue value = (IdNameValue) reportIdNames.get(i); %>
			reportIds[<%= i %>]   = "<%= value.getId() %>";
			reportNames[<%= i %>] = "<%= Utils.javaSafe(value.getName()) %>";
		<% } %>
	
		var dashboardReports = new Array ('reports1', 'reports2', 'reports3', 'reports4');
		function dataValidate() {
			if(document.getElementById('reportType').value=="-1") {
				alert("Please enter Report Type");
				document.getElementById('reportType').focus();
				document.getElementById('reportType').select();
				return false;
			}
			

			if(document.getElementById('reportName').value=="") {
				alert("Please enter Report Name");
				document.forma.reportName.focus();
				document.forma.reportName.select();
				return false;
			}   // if

			if(document.forma.pageSize!=null && document.forma.pageSize.options.selectedIndex == 0) {
				if(!checkNonNegativeInteger(document.forma.pageSize.options[document.forma.pageSize.options.selectedIndex].text)) {
					alert("Please enter number in pageSize. No Characters are allowed.");
					return false;
				} else {
					document.forma.pageSize.options[document.forma.pageSize.options.selectedIndex].value=document.forma.pageSize.options[document.forma.pageSize.options.selectedIndex].text;
				}
			}
		
			if(document.forma.frozenColumns!=null && document.forma.frozenColumns.options.selectedIndex == 0) {
				if(!checkNonNegativeInteger(document.forma.frozenColumns.options[document.forma.frozenColumns.options.selectedIndex].text)) {
					alert("Please enter number in frozenColumns. No Characters are allowed.");
					return false;
				} else {
					document.forma.frozenColumns.options[document.forma.frozenColumns.options.selectedIndex].value=document.forma.frozenColumns.options[document.forma.frozenColumns.options.selectedIndex].text;
				}
			}

			if(document.forma.heightContainer!=null && document.forma.heightContainer.options.selectedIndex == 0) {
				if(!checkNonNegativeInteger(document.forma.heightContainer.options[document.forma.heightContainer.options.selectedIndex].text)) {
					alert("Please enter positive number greater than 10% in \"Desired Container Height\". No Characters are allowed.");
					return false;
				} else {
					if(eval(document.forma.heightContainer.options[document.forma.heightContainer.options.selectedIndex].text) < 10) {
						alert("Please enter positive number less than 10% in \"Desired Container Height\". No Characters are allowed.");
						return false;
					}
					if(eval(document.forma.heightContainer.options[document.forma.heightContainer.options.selectedIndex].text) > 300) {
						alert("Please enter positive number greater than 300% in \"Desired Container Height\". No Characters are allowed.");
						return false;
					}
					document.forma.heightContainer.options[document.forma.heightContainer.options.selectedIndex].value=document.forma.heightContainer.options[document.forma.heightContainer.options.selectedIndex].text;
				}
			}

			if(document.forma.widthContainer!=null && document.forma.widthContainer.options.selectedIndex == 0) {
				if(!checkNonNegativeInteger(document.forma.widthContainer.options[document.forma.widthContainer.options.selectedIndex].text)) {
					alert("Please enter positive number greater than 10% in \"Desired Container Width\". No Characters are allowed.");
					return false;
				} else {
					if(eval(document.forma.widthContainer.options[document.forma.widthContainer.options.selectedIndex].text) < 10) {
						alert("Please enter positive number less than 10% in \"Desired Container Width\". No Characters are allowed.");
						return false;
					}
					if(eval(document.forma.widthContainer.options[document.forma.widthContainer.options.selectedIndex].text) > 300) {
						alert("Please enter positive number greater than 300% in \"Desired Container Width\". No Characters are allowed.");
						return false;
					}
					document.forma.widthContainer.options[document.forma.widthContainer.options.selectedIndex].value=document.forma.widthContainer.options[document.forma.widthContainer.options.selectedIndex].text;
				}
			}

			for(var i=0; i<reportIds.length; i++)
				if((document.getElementById('reportName').value==reportNames[i])&&(reportIds[i]!="<%= reportID %>")) {
					alert("Report with that name created by you already exists.\nPlease select another name");
					document.forma.reportName.focus();
					document.forma.reportName.select();
					return false;
				}   // if
			return true;
		}   // dataValidate
	//-->
	</script>
<!-- Start Floating Layer -->
<div 
   id="uniquename1" 
   style="display:none; 
      position:absolute; 
      border-style: solid; 
      background-color: white; 
      padding: 5px;">
              <!-- place your HTML content here-->
		               <h3>Background color selector</h3>
				<% if (rdef !=null && rdef.getDashBoardReports()!=null && (rdef.getDashBoardReports().getReportsList().get(0)!=null) && (((Reports)rdef.getDashBoardReports().getReportsList().get(0)).getBgcolor()!=null) &&(((Reports)rdef.getDashBoardReports().getReportsList().get(0)).getBgcolor().length()>0)) {%>		     
 					<input id="hashCode1" style="position: absolute; right: 15px; top: 239px;" size="8" class="color" value="<%=((Reports)rdef.getDashBoardReports().getReportsList().get(0)).getBgcolor()%>" type="text"	/>
 				<%} else { %>
 					<input id="hashCode1" style="position: absolute; right: 15px; top: 239px;" size="8" class="color" value="#FFFFFF" type="text"	/> 				
 				<% } %>	
					<input type="button" value="Submit & Close" onClick="document.forma.repBgColor1.value=document.getElementById('hashCode1').value; document.forma.repBgColor1.style.backgroundColor = document.getElementById('hashCode1').value; HideContent('uniquename1');"/>


              <!-- End of content area -->

</div>
<div 
   id="uniquename2" 
   style="display:none; 
      position:absolute; 
      border-style: solid; 
      background-color: white; 
      padding: 5px;">
              <!-- place your HTML content here-->
		               <h3>Background color selector</h3>
				<% if (rdef !=null && rdef.getDashBoardReports()!=null && (rdef.getDashBoardReports().getReportsList().get(1)!=null) && (((Reports)rdef.getDashBoardReports().getReportsList().get(1)).getBgcolor()!=null) && (((Reports)rdef.getDashBoardReports().getReportsList().get(1)).getBgcolor().length()>0)) {%>		     
 					<input id="hashCode2" style="position: absolute; right: 15px; top: 239px;" size="8" class="color" value="<%=((Reports)rdef.getDashBoardReports().getReportsList().get(1)).getBgcolor()%>" type="text"	/>
 				<%} else { %>
 					<input id="hashCode2" style="position: absolute; right: 15px; top: 239px;" size="8" class="color" value="#FFFFFF" type="text"	/> 				
 				<% } %>	
					<input type="button" value="Submit & Close" onClick="document.forma.repBgColor2.value=document.getElementById('hashCode2').value; document.forma.repBgColor2.style.backgroundColor = document.getElementById('hashCode2').value;HideContent('uniquename2');"/>


              <!-- End of content area -->

</div>
<div 
   id="uniquename3" 
   style="display:none; 
      position:absolute; 
      border-style: solid; 
      background-color: white; 
      padding: 5px;">
              <!-- place your HTML content here-->
		               <h3>Background color selector</h3>
				<% if (rdef !=null && rdef.getDashBoardReports()!=null && (rdef.getDashBoardReports().getReportsList().get(2)!=null) && (((Reports)rdef.getDashBoardReports().getReportsList().get(2)).getBgcolor()!=null) && (((Reports)rdef.getDashBoardReports().getReportsList().get(2)).getBgcolor().length()>0)) {%>		     
 					<input id="hashCode3" style="position: absolute; right: 15px; top: 239px;" size="8" class="color" value="<%=((Reports)rdef.getDashBoardReports().getReportsList().get(2)).getBgcolor()%>" type="text"	/>
 				<%} else { %>
 					<input id="hashCode3" style="position: absolute; right: 15px; top: 239px;" size="8" class="color" value="#FFFFFF" type="text"	/> 				
 				<% } %>	
					<input type="button" value="Submit & Close" onClick="document.forma.repBgColor3.value=document.getElementById('hashCode3').value; document.forma.repBgColor3.style.backgroundColor = document.getElementById('hashCode3').value; HideContent('uniquename3');"/>


              <!-- End of content area -->

</div>
<div 
   id="uniquename4" 
   style="display:none; 
      position:absolute; 
      border-style: solid; 
      background-color: white; 
      padding: 5px;">
              <!-- place your HTML content here-->
		               <h3>Background color selector</h3>
				<% if (rdef !=null && rdef.getDashBoardReports()!=null && (rdef.getDashBoardReports().getReportsList().get(3)!=null) && (((Reports)rdef.getDashBoardReports().getReportsList().get(3)).getBgcolor()!=null) && (((Reports)rdef.getDashBoardReports().getReportsList().get(3)).getBgcolor().length()>0)) {%>		     
 					<input id="hashCode4" style="position: absolute; right: 15px; top: 239px;" size="8" class="color" value="<%=((Reports)rdef.getDashBoardReports().getReportsList().get(3)).getBgcolor()%>" type="text"	/>
 				<%} else { %>
 					<input id="hashCode4" style="position: absolute; right: 15px; top: 239px;" size="8" class="color" value="#FFFFFF" type="text"	/> 				
 				<% } %>	
					<input type="button" value="Submit & Close" onClick="document.forma.repBgColor4.value=document.getElementById('hashCode4').value; document.forma.repBgColor4.style.backgroundColor = document.getElementById('hashCode4').value; HideContent('uniquename4');"/>


              <!-- End of content area -->

</div>

<div id="FloatingLayer" style="position:absolute;width:250px;left:100;top:100;visibility:hidden"> 
  <table border="0" width="250" bgcolor="#FF6600" cellspacing="0" cellpadding="5">
    <tr> 
      <td width="100%"> <table border="0" width="100%" cellspacing="0" cellpadding="0" height="36">
          <tr> 
            <td id="titleBar" style="cursor:move" width="100%"> <ilayer width="100%" onSelectStart="return false"> 
              <layer width="100%" onMouseover="isActive=true;if (isN4) MoveN4(FloatingLayer)" onMouseout="isActive=false"> 
              <font face="Arial" color="#FFFFFF">Layer Title</font></layer>
              </ilayer></td>
            <td style="cursor:hand" valign="top"> <a href="#" onClick="ToggleFloatingLayer('FloatingLayer',0);return false"><font color="#ffffff" size="2" face="arial"  style="text-decoration:none">X</font></a> 
            </td>
          </tr>
          <tr> 
            <td width="100%" bgcolor="#FFFFFF" style="padding:4px" colspan="2"> 
              <!-- place your HTML content here-->
		               <h3>Background color selector</h3>
					<input id="hashCode" style="position: absolute; right: 15px; top: 239px;" size="8" class="color" value="#FF0000" type="text"	/>
					<input type="button" value="Submit & Close" onClick="document.forma.dashBgColor1.value=document.getElementById('hashCode').value; javascript:ToggleFloatingLayer('FloatingLayer',0);"/>


              <!-- End of content area -->
            </td>
          </tr>
        </table></td>
    </tr>
  </table>
</div>
<!-- End Floating layer -->
<%!
	private String nvl(String s)                   { return (s==null)?"":s; }
	private String nvl(String s, String sDefault)  { return nvl(s).equals("")?sDefault:s; } 
%>
