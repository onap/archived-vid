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
<%@ page import="org.openecomp.portalsdk.analytics.system.Globals" %>
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
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.ReportSchedule" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.Utils" %>

<% 
  ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
  WizardSequence ws = rdef.getWizardSequence();
  ReportSchedule reportSchedule = rdef.getReportSchedule(); 
  String remoteDbPrefix = (String) session.getAttribute("remoteDB");
  boolean isSQLAllowed = Globals.getAllowSQLBasedReports();
  
%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="java.util.Date"%>
<%@page import="org.openecomp.portalsdk.analytics.model.ReportLoader"%>
<script language="JavaScript">
<!--
function showTestConditionPopup() {
	var w = window.open("<%= AppUtils.getRaptorActionURL() %>report.popup.test.cond&remoteDbPrefix=<%=remoteDbPrefix%>&<%= AppConstants.RI_FORMATTED_SQL %>="+escape(""+document.forma.conditionSQL.value), "testCondSQLPopup", "width=450,height=180,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
}   // showTestConditionPopup
	
function exclusiveCheckBox(which) {
	  var size = 2;
	  for (i=0; i<size; i++) {
	    if(document.getElementById("notify" + i) != which) {
	    	document.getElementById("notify" + i).checked = false;
	    } 
	  }
	  //alert("exclusive " + which.form.notify_type.value);
}	
function initFormFields() {

	var requestStr = "<%=reportSchedule.getFormFields()%>";
	//alert(requestStr);
       if(requestStr.length > 0) {
	SplitName = requestStr.substring(1,requestStr.length).split("&");
 	for (num = 0; num < SplitName.length; num++) {
 			keyValue = SplitName[num].split("=");
			//alert(keyValue[0]+ " " + keyValue[1]);
			var obj = eval("document.forma."+keyValue[0]);
			if(obj) {
			    if(obj.tagName == "SELECT") {
					//var opt = document.getElementsByName(keyValue[0]);
					var selString = "";
					for (var intLoop=0; intLoop < obj.length; intLoop++) {
					         if (obj[intLoop].value == keyValue[1]) {
                                  obj[intLoop].selected=true; 
                                  break;					            
					         }
					}
								    	
			    } else 
			  document.getElementsByName(keyValue[0])[0].value = unescape(keyValue[1]);
	}
       }
}
}	
//-->
</script>
<script language="javascript" src="<%= AppUtils.getBaseFolderURL() %>js/other_scripts.js"></script>
<script type="text/javascript" src="<%= AppUtils.getBaseFolderURL() %>js/CalendarPopup.js"></script> 
<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/calendar.css">
 <iframe id="calendarFrame" class="nav" z-index:199; scrolling="no"  frameborder="0"  width=165px height=165px src="" style="position:absolute; display:none;">
</iframe>
 <div id="calendarDiv" name="calendarDiv" style="position:absolute; z-index:20000; visibility:hidden; background-color:white;layer-background-color:white;"></div>
 <%
 Calendar startCalendarDate = Calendar.getInstance();
 startCalendarDate.add(Calendar.DAY_OF_MONTH, - 540); 
 Calendar endCalendarDate = Calendar.getInstance();
 endCalendarDate.add(Calendar.DAY_OF_MONTH, 540);
 SimpleDateFormat dtf = new SimpleDateFormat("MM/dd/yyyy");
 SimpleDateFormat oracleDateFormat = new SimpleDateFormat("MM/dd/yyyy kk:mm:ss");
 Date sysdate = oracleDateFormat.parse(ReportLoader.getSystemDateTime());
 SimpleDateFormat dtimestamp = new SimpleDateFormat(Globals.getScheduleDatePattern());
 //dtimestamp.setTimeZone(TimeZone.getTimeZone(Globals.getTimeZone())); 
 

 
	 
 %>

	<SCRIPT LANGUAGE="JavaScript">
 	var oCalendar = new CalendarPopup("calendarDiv");
    	
    	oCalendar.addDisabledDates(null, "<%=dtf.format(startCalendarDate.getTime())%>");
		oCalendar.addDisabledDates("<%=dtf.format(endCalendarDate.getTime())%>", null);


    	oCalendar.setCssPrefix("raptor");
	</SCRIPT>
<table class=mSDL width="100%"  class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
   <% if(request.getAttribute("schedule_only")!=null) { %>
	<tr>
		<td class=rbg1 colspan=2 valign="Middle"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - Report <%= ws.getCurrentStep() %></b></td>
	</tr>
   <% } %>	
	<tr>
		<td class=rbg1 colspan=2 valign="Middle"><b class=rtableheader><a href="javascript:uitmpl_qh('schedule_help')" class="qh-link"></a>Please enter Time in <%= Globals.getTimeZone()%>. The Current System Time is <%=dtimestamp.format(sysdate)%>&nbsp;<%=Globals.getTimeZone()%></b></td>
	</tr>
	<tr>
			<td colspan=2 class="nopad"><!-- quick help text -->
				<div id="schedule_help" class="mQH">
					<p><span class="label">Quick Help:</span>
						</p>
				</div>
			<!-- /quick help text --></td>
	</tr>	
	
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Schedule Emails: </font></td> 
		<td class=rbg3 align="left" width="50%" valign="middle" nowrap><font class=rtabletext>
			<input type='radio' name='schedEnabled' id='schedEnabled' value='Y' <%= reportSchedule.getSchedEnabled().equals("Y")?" checked":"" %> />Yes
			&nbsp;
			<input type='radio' name='schedEnabled' id='schedEnabled' value='N' <%= reportSchedule.getSchedEnabled().equals("N")?" checked":"" %> />No
			</font></td>
	</tr> 
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Email Attachment:</font></td> 
		<td class=rbg3 align="left" width="50%" valign="middle" nowrap>
			<font class=rtabletext>
			<input type='radio' name='notify' id='notify' value='2' <%= reportSchedule.getNotify_type().equals("2")?" checked":(reportSchedule.getNotify_type().length()<=0)?" checked":(!reportSchedule.getNotify_type().equals("4"))? " checked " :"" %> />PDF Attachment
			&nbsp;&nbsp;
			<input type='radio' name='notify' id='notify' value='4' <%= reportSchedule.getNotify_type().equals("4")?" checked":"" %> />Excel Attachment
			<input type='hidden' name='notify_type' value='1'/>
			</font>
	    </td>
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Recurrence: </font></td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
			<select name="schedRecurrence">
				<option value=""                                  <%= reportSchedule.getRecurrence().equals(""                         )?" selected":"" %>>One Time
				<option value="<%= AppConstants.SR_HOURLY      %>"<%= reportSchedule.getRecurrence().equals(AppConstants.SR_HOURLY     )?" selected":"" %>>Hourly
				<option value="<%= AppConstants.SR_DAILY       %>"<%= reportSchedule.getRecurrence().equals(AppConstants.SR_DAILY      )?" selected":"" %>>Daily
				<option value="<%= AppConstants.SR_DAILY_MO_FR %>"<%= reportSchedule.getRecurrence().equals(AppConstants.SR_DAILY_MO_FR)?" selected":"" %>>Daily Mo-Fr
				<option value="<%= AppConstants.SR_WEEKLY      %>"<%= reportSchedule.getRecurrence().equals(AppConstants.SR_WEEKLY     )?" selected":"" %>>Weekly
				<option value="<%= AppConstants.SR_MONTHLY     %>"<%= reportSchedule.getRecurrence().equals(AppConstants.SR_MONTHLY    )?" selected":"" %>>Monthly
			</select></font></td>
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Start Date: </font></td> 
		<td class=rbg3 align="left" width="50%" nowrap><font class=rtabletext>
			<input type="text" size="10" maxlength="10" name="schedStartDate" value="<%= reportSchedule.getStartDate() %>">
			<!--<a href="#" onClick="window.dateField = document.forma.schedStartDate;calendar = window.open('<%= AppUtils.getRaptorActionURL() %>popup.calendar','cal','WIDTH=200,HEIGHT=250');return false;">
				<img src="<%= AppUtils.getImgFolderURL() %>calender_icon.gif" align=absmiddle border=0 width="20" height="20">
			</a>-->
			<img src="<%= AppUtils.getImgFolderURL() %>calender_icon.gif" align=absmiddle border=0 width="20" height="20" onClick="oCalendar.select(document.getElementById('schedStartDate'),event,'MM/dd/yyyy'); return false;"  style="cursor:hand">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<select name="schedHour">
				<option value="01"<%= reportSchedule.getRunHour().equals("01")?" selected":"" %>>1
				<option value="02"<%= reportSchedule.getRunHour().equals("02")?" selected":"" %>>2
				<option value="03"<%= reportSchedule.getRunHour().equals("03")?" selected":"" %>>3
				<option value="04"<%= reportSchedule.getRunHour().equals("04")?" selected":"" %>>4
				<option value="05"<%= reportSchedule.getRunHour().equals("05")?" selected":"" %>>5
				<option value="06"<%= reportSchedule.getRunHour().equals("06")?" selected":"" %>>6
				<option value="07"<%= reportSchedule.getRunHour().equals("07")?" selected":"" %>>7
				<option value="08"<%= reportSchedule.getRunHour().equals("08")?" selected":"" %>>8
				<option value="09"<%= reportSchedule.getRunHour().equals("09")?" selected":"" %>>9
				<option value="10"<%= reportSchedule.getRunHour().equals("10")?" selected":"" %>>10
				<option value="11"<%= reportSchedule.getRunHour().equals("11")?" selected":"" %>>11
				<option value="12"<%= reportSchedule.getRunHour().equals("12")?" selected":"" %>>12
			</select>
			<select name="schedMin">
				<option value="00"<%= reportSchedule.getRunMin().equals("00")?" selected":"" %>>00
				<option value="15"<%= reportSchedule.getRunMin().equals("15")?" selected":"" %>>15
				<option value="30"<%= reportSchedule.getRunMin().equals("30")?" selected":"" %>>30
				<option value="45"<%= reportSchedule.getRunMin().equals("45")?" selected":"" %>>45
			</select>
			<select name="schedAMPM">
				<option value="AM"<%= reportSchedule.getRunAMPM().equals("AM")?" selected":"" %>>AM
				<option value="PM"<%= reportSchedule.getRunAMPM().equals("PM")?" selected":"" %>>PM
			</select>
			
			</font></td>
	</tr> 
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>End Date: </font></td> 
		<td class=rbg3 align="left" width="50%" nowrap><font class=rtabletext>
			<input type="text" size="10" maxlength="10" name="schedEndDate" value="<%= reportSchedule.getEndDate() %>">
			<!--<a href="#" onClick="window.dateField = document.forma.schedEndDate;calendar = window.open('<%= AppUtils.getRaptorActionURL() %>popup.calendar','cal','WIDTH=200,HEIGHT=250');return false;">
				<img src="<%= AppUtils.getImgFolderURL() %>calender_icon.gif" align=absmiddle border=0 width="20" height="20">
			</a>-->
			<img src="<%= AppUtils.getImgFolderURL() %>calender_icon.gif" align=absmiddle border=0 width="20" height="20" onClick="oCalendar.select(document.getElementById('schedEndDate'),event,'MM/dd/yyyy'); return false;"  style="cursor:hand">
			</font></td>
	</tr>

        <% if(AppUtils.isAdminUser(request) || isSQLAllowed ) { %>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Use Condition:</font></td> 
		<td class=rbg3 align="left" width="50%" valign="middle" nowrap><font class=rtabletext>
			<input type="checkbox" name="conditional" value="Y"<%= reportSchedule.getConditional().equals("Y")?" checked":"" %>> Send Emails Only When Condition Is Met
			</font></td>
	</tr> 
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Condition SQL:</font></td> 
		<td class=rbg3 align="left" width="50%" valign="middle" nowrap><font class=rtabletext>SELECT 1 FROM DUAL WHERE EXISTS (<br>
			<textarea name="conditionSQL" cols="40" rows="3"><%= nvl(reportSchedule.getConditionSQL()) %></textarea>
			)
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="Button" class=button value="Test" onClick="showTestConditionPopup()">
			&nbsp;
			</font></td>
	</tr>
        <% } %>	 	

	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Max rows in attachment:</font></td> 
		<td class=rbg3 align="left" width="50%" valign="middle" nowrap>
			<input type='text' name='downloadLimit' value="<%=reportSchedule.getDownloadLimit()%>" size='4'/>
	   </td>
	</tr> 			
	
	<jsp:include page="wizard_schedule_formfield_include.jsp" flush="true" />
	<script type="text/javascript">initFormFields();</script>	
	
<% 	List emailToUsers = reportSchedule.getEmailToUsers();
	for(int i=0; i<emailToUsers.size(); i++) { 
		IdNameValue userValue = (IdNameValue) emailToUsers.get(i); %>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext><%= (i==0)?"Email To: ":"&nbsp;" %></font></td> 
		<td class=rbg3 align="left" width="50%" nowrap><font class=rtabletext>
			<%= userValue.getName() %>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>deleteicon.gif" alt="Remove" width="12" height="12" onClick="if(! dataValidate()) {return false;} else if(! confirm('Are you sure?')) {return false;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE_USER %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= userValue.getId() %>'; }">
			</font></td>
	</tr>
<%	}	// for 
	List emailToRoles = reportSchedule.getEmailToRoles();
	for(int i=0; i<emailToRoles.size(); i++) { 
		IdNameValue roleValue = (IdNameValue) emailToRoles.get(i); %>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext><%= (emailToUsers.size()==0&&i==0)?"Email To: ":"&nbsp;" %></font></td> 
		<td class=rbg3 align="left" width="50%" nowrap><font class=rtabletext>Everyone With Role:&nbsp;
			<%= roleValue.getName() %>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>deleteicon.gif" alt="Remove" width="12" height="12" onClick="if(! dataValidate()) {return false;} else if(! confirm('Are you sure?')) {return false;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE_ROLE %>'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= roleValue.getId() %>'; }">
			</font></td>
	</tr>
<%	}	// for 
	
	Vector remainingUsers = Utils.getUsersNotInList(emailToUsers,request);
	Vector remainingRoles = Utils.getRolesNotInList(emailToRoles,request);
	if((emailToUsers.size()+emailToRoles.size()==0)||(remainingUsers.size()>0)||(remainingRoles.size()>0)) { %>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext><%= (emailToUsers.size()+emailToRoles.size()==0)?"Email To: ":"&nbsp;" %></font></td> 
		<td class=rbg3 align="left" width="50%" nowrap><font class=rtabletext>
<%		if(remainingUsers.size()>0) { %>
			<select name="schedEmailAdd" onChange="if(! dataValidate()) {selectedIndex=0;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ADD_USER %>'; document.forma.submit();}">
			    <option value="" selected>--- Select User ---
<% 			for(int i=0; i<remainingUsers.size(); i++) {
				IdNameValue userValue = (IdNameValue) remainingUsers.get(i); %>
			    <option value="<%= userValue.getId() %>"><%= userValue.getName() %>
<% 			}	// for %>
			</select>
<%		} else { %>
			No user emails available
<%		} %>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<%		if(remainingRoles.size()>0) { %>
			<select name="schedEmailAddRole" onChange="if(! dataValidate()) {selectedIndex=0;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ADD_ROLE %>'; document.forma.submit();}">
			    <option value="" selected>--- Select Role ---
<% 			for(int i=0; i<remainingRoles.size(); i++) {
				IdNameValue roleValue = (IdNameValue) remainingRoles.get(i); %>
			    <option value="<%= roleValue.getId() %>"><%= roleValue.getName() %>
<% 			}	// for %>
			</select>
<%		} else { %>
			No roles available
<%		} %>
		</font></td>
	</tr>
<%	}	// if 
%>
</table>
<br>

<script language="JavaScript">
<!--
function dataValidate() {
	if(! checkDate(document.forma.schedStartDate.value, true)) {
		alert("Invalid Start Date. The date format should be MM/DD/YYYY");
		document.forma.schedStartDate.focus();
		document.forma.schedStartDate.select();
		return false;
	}
	if(! checkDate(document.forma.schedEndDate.value, true)) {
		alert("Invalid End Date. The date format should be MM/DD/YYYY");
		document.forma.schedEndDate.focus();
		document.forma.schedEndDate.select();
		return false;
	}
	/*if(! checkDate(document.forma.schedRunDate.value, true)) {
		alert("Invalid Execution Date. The date format should be MM/DD/YYYY");
		document.forma.schedRunDate.focus();
		document.forma.schedRunDate.select();
		return false;
	}*/
<% if (AppUtils.isAdminUser(request) || isSQLAllowed){ %>
	if(document.forma.conditional.checked&&document.forma.conditionSQL.value=="") {
		alert("Please provide Condition SQL");
		document.forma.conditionSQL.focus();
		document.forma.conditionSQL.select();
		return false;
	}
  <% } %>
	if(! checkNonNegativeInteger(document.forma.downloadLimit.value)) {
		alert("Max row in attachment is not a valid integer.\nPlease enter a valid value.");
		document.forma.downloadLimit.focus();
		document.forma.downloadLimit.select();
		return false;
	}
	else 
	{
		if((document.forma.downloadLimit.value) > <%=Globals.getDownloadLimit()%>) {
			alert("Max row you can download should not be more than <%=Globals.getDownloadLimit()%> rows.\nPlease enter a valid value.");
			document.forma.downloadLimit.focus();
			document.forma.downloadLimit.select();
			return false;			
		}
		
	}
	for (var i=0; i < document.forma.notify.length; i++) {
   		if (document.forma.notify[i].checked) {
      		  document.forma.notify_type.value = document.forma.notify[i].value;
       	        }
	    } 
	return validateForm();
		
	return true;
}   // dataValidate
//-->
</script>

<%!
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } 
%>
