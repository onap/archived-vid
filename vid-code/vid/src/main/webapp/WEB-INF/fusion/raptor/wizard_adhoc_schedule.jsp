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
 Name: wizard_adhoc_schedule.jsp
 Use : This JSP is used for accepting user parameters for scheduling the report.
 
 Change Log
 ==========
 
 28-Aug-2009 : Version 8.4 (Sundar);  initFormFields function is removed as it is handled in back end.
 23-Jun-2009 : Version 8.4 (Sundar); 
 				
 				<UL> 
 				<LI> Bug related to creating startDate variable (in Javascript) for the Validation purpose is fixed.</LI>
 				</UL>
 				         

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
  ReportSchedule reportSchedule = (ReportSchedule) request.getSession().getAttribute(AppConstants.SI_REPORT_SCHEDULE);
  ReportDefinition rdefRecurrance = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
  session.setAttribute("login_id", AppUtils.getUserBackdoorLoginId(request));
  if(reportSchedule==null) reportSchedule = (ReportSchedule) request.getAttribute(AppConstants.SI_REPORT_SCHEDULE);
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
//-->
</script>
<script language="javascript" src="<%= AppUtils.getBaseFolderURL() %>js/other_scripts.js"></script>
<script type="text/javascript" src="<%= AppUtils.getBaseFolderURL() %>js/CalendarPopup.js"></script> 
<script language="JavaScript" src="<%= AppUtils.getBaseFolderURL() %>js/rounded-corners.js"></script>
<script language="JavaScript" src="<%= AppUtils.getBaseFolderURL() %>js/form-field-tooltip.js"></script>
<link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/calendar.css">

	<script type="text/javascript">
var stat;
var delay=0;//delay in milliseconds
function show(){


var oIfr = document.getElementById('calendarFrame');
var oCal = document.getElementById('calendarDiv');
oIfr.style.display=(oCal.style.display=='block')?'none':'block';
oIfr.style.top=findPosY()
oIfr.style.left=findPosX()-5

}
function hide(){

// added to show Iframe behind calender
var oIfr = document.getElementById('calendarFrame');
var oCal = document.getElementById('calendarDiv');
oIfr.style.display=(oCal.style.display=='none')?'block':'none';
}

  function findPosX()
  {
 var obj= oCalendar
    var curleft = 0;
    if(obj.offsetParent)
        while(1) 
        {
          curleft += obj.offsetLeft;
          if(!obj.offsetParent)
            break;
          obj = obj.offsetParent;
        }
    else if(obj.x)
        curleft += obj.x;
    return curleft;
    
  }

  function findPosY()
  {
  
   var obj= oCalendar
    var curtop = 0;
    if(obj.offsetParent)
        while(1)
        {
          curtop += obj.offsetTop;
          if(!obj.offsetParent)
            break;
          obj = obj.offsetParent;
        }
    else if(obj.y)
        curtop += obj.y;
        //alert(curtop)
    return curtop;   
  }
  
  //frameborder="0" 
	</SCRIPT>

<iframe id="calendarFrame" class="nav" z-index:199; scrolling="no"  frameborder="0"  width=165px height=165px src="" style="position:absolute; display:none;">
</iframe>

 <div id="calendarDiv" name="calendarDiv" style="position:absolute; z-index:200; visibility:none; background-color:white;layer-background-color:white;"></div>
 <%
 Calendar startCalendarDate = Calendar.getInstance();
 startCalendarDate.add(Calendar.DAY_OF_MONTH, - 540); 
 Calendar endCalendarDate = Calendar.getInstance();
 endCalendarDate.add(Calendar.DAY_OF_MONTH, 540);
 SimpleDateFormat dtf = new SimpleDateFormat("MM/dd/yyyy");
 SimpleDateFormat oracleDateFormat = new SimpleDateFormat("MM/dd/yyyy kk:mm:ss");
 Date sysdate = oracleDateFormat.parse(ReportLoader.getSystemDateTime());
 SimpleDateFormat dtimestamp = new SimpleDateFormat(Globals.getScheduleDatePattern());
 Calendar systemCalendar = Calendar.getInstance();
 systemCalendar.setTime(sysdate);
 Date sysNext15date = oracleDateFormat.parse(ReportLoader.getNext15MinutesOfSystemDateTime());
 //dtimestamp = new SimpleDateFormat(Globals.getScheduleDatePattern());
 Calendar systemNext15Calendar = Calendar.getInstance();
 systemNext15Calendar.setTime(sysNext15date);
 Date sysNext30date = oracleDateFormat.parse(ReportLoader.getNext30MinutesOfSystemDateTime());
 //dtimestamp = new SimpleDateFormat(Globals.getScheduleDatePattern());
 Calendar systemNext30Calendar = Calendar.getInstance();
 systemNext30Calendar.setTime(sysNext30date);

 System.out.println(" systemNext15Calendar " + systemNext15Calendar);
 System.out.println(" systemNext30Calendar " + systemNext30Calendar);

 //dtimestamp.setTimeZone(TimeZone.getTimeZone(Globals.getTimeZone()));
 
%>

 
	 

	<SCRIPT LANGUAGE="JavaScript">
 	var oCalendar = new CalendarPopup("calendarDiv", "calendarFrame");
    	
    	oCalendar.addDisabledDates(null, "<%=dtf.format(startCalendarDate.getTime())%>");
		oCalendar.addDisabledDates("<%=dtf.format(endCalendarDate.getTime())%>", null);


    	oCalendar.setCssPrefix("raptor");

    	function fillStartEndDate(varSelect) {
        	var flag = 0;
        	<% if(!(nvl(reportSchedule.getStartDate()).length() > 0)) { %>
        	a:
			 for (var i = 0; i < varSelect.options.length; i++) {
				 	if (varSelect.options[ i ].selected) {
			 		    if(!(varSelect.options[i].value.length > 0)) {
				 		    var d = new Date();
				 		    //alert(d.getMonth() + " " + d.getDate() + " " + d.getYear());
				 		    //alert(d.getMonth()+1+"/"+d.getDate()+"/"+d.getYear());
			 		    	document.forma.schedStartDate.value = addZero(d.getMonth()+1)+"/"+addZero(d.getDate())+"/"+addZero(d.getYear());
			 		    	document.forma.schedEndDate.value = addZero(d.getMonth()+1)+"/"+addZero(d.getDate())+"/"+addZero(d.getYear());
			 		    	//alert(document.forma.schedStartDate.value);
			 		    	flag = 1;
			 		    	break a;
			 		    } 
				 	}
			 } 
			 if(flag == 0) {
				//alert("Here");
				document.forma.schedStartDate.value = "";
				document.forma.schedEndDate.value = "";
			 }
			 <% } %>
    	}

    	function initStartEndDate() { 
        	var flag = 0;
        	var varSelect = document.forma.schedRecurrence;
        	var d = new Date();
        	var d_end = null;
            	d.setFullYear(<%=systemCalendar.get(Calendar.YEAR)%>, <%=systemCalendar.get(Calendar.MONTH)%>,<%=systemCalendar.get(Calendar.DAY_OF_MONTH)%>);
            	d.setHours(<%=systemCalendar.get(Calendar.HOUR_OF_DAY)%>);
            	d.setMinutes(<%=systemCalendar.get(Calendar.MINUTE)%>);
            	d.setSeconds(<%=systemCalendar.get(Calendar.SECOND)%>);
       		d_end = new Date();
            	d_end.setFullYear(<%=systemNext15Calendar.get(Calendar.YEAR)%>, <%=systemNext15Calendar.get(Calendar.MONTH)%>,<%=systemNext15Calendar.get(Calendar.DAY_OF_MONTH)%>);
            	d_end.setHours(<%=23%>);
            	d_end.setMinutes(<%=45%>);
            	d_end.setSeconds(<%=systemNext15Calendar.get(Calendar.SECOND)%>);

        	if(d.getHours() == 23 && d.getMinutes()>=30) {
            	d_end.setFullYear(<%=systemNext30Calendar.get(Calendar.YEAR)%>, <%=systemNext30Calendar.get(Calendar.MONTH)%>,<%=systemNext30Calendar.get(Calendar.DAY_OF_MONTH)%>);
            	d_end.setHours(<%=23%>);
            	d_end.setMinutes(<%=45%>);
            	d_end.setSeconds(<%=systemNext30Calendar.get(Calendar.SECOND)%>);
 		    	document.forma.schedEndDate.value = addZero(d_end.getMonth()+1)+"/"+addZero(d_end.getDate())+"/"+addZero(d_end.getYear());
 		    	var obj = document.forma.schedEndHour;
 		    	var hr = d_end.getHours();
				//alert("end hr " + hr);
 		    	//if(d_end.getMinutes()>=45) 
	 		    //	hr = hr + 1;
 		    	if(hr > 12) hr = hr - 12; 
 		    	if(hr == 0) hr = 12;
 		   		for (var i=0; i<obj.options.length; i++) {
 					if (eval(obj.options[i].value) == hr) {
 						obj.options[i].selected = true;
 						break;
 					} 
 				}
 				var obj1 = document.forma.schedEndMin;
 		   		for (var i=0; i<obj1.options.length; i++) {
 					if (d_end.getMinutes() <= eval(obj1.options[i].value)) {
 						obj1.options[i].selected = true;
 						break;
 					} 
 				}
 				var obj2 = document.forma.schedEndAMPM;
 				
 		   		for (var i=0; i<obj2.options.length; i++) {
 					if (d_end.getHours() < 12 && obj2.options[i].value == 'AM') {
 						obj2.options[i].selected = true;
 					} else if (d_end.getHours() >= 12 && obj2.options[i].value == 'PM')
 						obj2.options[i].selected = true;
 				}
 		    	
        	}
            	
            	
		      <% if(!(nvl(reportSchedule.getStartDate()).length() > 0)) { %>
	        	
				 		    //alert(d.getMonth() + " " + d.getDate() + " " + d.getYear());
				 		    //alert(d.getMonth()+1+"/"+d.getDate()+"/"+d.getYear());
							//alert(d.getHours());
				 		    <%-- System.out.println(reportSchedule.getStartDate());
				 		       if(!(nvl(reportSchedule.getStartDate()).length() > 0)) { --%>
							    if(d.getHours() == 23 && d.getMinutes() > 45) {
									//d.setDate(d.getDate() + 1);
					 		    	document.forma.schedStartDate.value = addZero(d.getMonth()+1)+"/"+addZero(d.getDate()+1)+"/"+addZero(d.getYear());
								}
								else
				 		    	document.forma.schedStartDate.value = addZero(d.getMonth()+1)+"/"+addZero(d.getDate())+"/"+addZero(d.getYear());
				 		    	//document.forma.schedEndDate.value = addZero(d.getMonth()+1)+"/"+addZero(d.getDate())+"/"+addZero(d.getYear());
				 		    	document.forma.schedEndDate.value = addZero(d_end.getMonth()+1)+"/"+addZero(d_end.getDate())+"/"+addZero(d_end.getYear());

				 		    	var obj = document.forma.schedHour;
				 		    	var hr = d.getHours();
				 		    	if(d.getMinutes()>=45) 
					 		    	hr = hr + 1;
				 		    	if(hr > 12) hr = hr - 12; 
				 		    	if(hr == 0) hr = 12;
				 		   		for (var i=0; i<obj.options.length; i++) {
				 					if (eval(obj.options[i].value) == hr) {
				 						obj.options[i].selected = true;
				 						break;
				 					} 
				 				}
				 				var obj1 = document.forma.schedMin;
				 		   		for (var i=0; i<obj1.options.length; i++) {
				 					if (d.getMinutes() <= eval(obj1.options[i].value)) {
				 						obj1.options[i].selected = true;
				 						break;
				 					} 
				 				}
				 				var obj2 = document.forma.schedAMPM;
				 				
				 		   		for (var i=0; i<obj2.options.length; i++) {
								 //alert(d.getHours() + " " + d.getMinutes());
								if(d.getHours() == 23 && d.getMinutes() >= 45) { 
								    if(obj2.options[i].value == 'AM') {
										//alert("hello");
										obj2.options[i].selected = true;
									}
								} else {
								   //alert("hello2");
				 					if ((d.getHours() < 12) && obj2.options[i].value == 'AM') {
				 						obj2.options[i].selected = true;
				 					} else if ((d.getHours() >= 12)  && obj2.options[i].value == 'PM') {
				 						obj2.options[i].selected = true;
				 				}
				 				}
				 			}
				 				
				 		
				 		    	
			 <% }  %>
    	}     	
 	
        function addZero(num) {
        	var numInt = 0;
        	numInt = num;
        	if(numInt < 10) {
            	return "0"+numInt;
        	}
        	else return ""+numInt;
        }	     	
	</SCRIPT>
<table width="100%" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr class="rbg1">
		<td class=rbg1 colspan="2" align="left">
	      	<%if(nvl(Globals.getScheduleHelpMessage()).trim().length()>0) {	%>
			<a href="javascript:uitmpl_qh('sch_help_text'); if(typeof window.parent.resizeWindow == 'function') window.parent.resizeWindow();" class="qh-link" style="position:fixed;"></a>
		<% } %>
	      	<b class=rtableheader>Please enter Time in <%= Globals.getTimeZone()%>. The Current System Time is <%=dtimestamp.format(sysdate)%>&nbsp;<%=Globals.getTimeZone()%></b>
	      </td>
	      
	</tr>	
	
	<%if(nvl(Globals.getScheduleHelpMessage()).length()>0) {	%>
	   <tr>
	   	<td colspan="2" class="nopad"><!-- quick help text -->
	   	<div id="sch_help_text" class="mQH">
	   		<p><span class="label">Report Desc:</span>
	   		<%= Globals.getScheduleHelpMessage() %></p>
	   	</div>
	   	<!-- /quick help text --></td>	   	
	   </tr>
	<% } %>	
	
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Schedule Emails: </font></td> 
		<td class=rbg3 align="left" width="50%" valign="middle" nowrap><font class=rtabletext>
			<input type='radio' name='schedEnabled' id='schedEnabled' value='Y' <%= reportSchedule.getSchedEnabled().equals("Y")?" checked":"" %> toolTipText="This is used for the enabling or disabling the scheduling feature for this report."/>Yes
			&nbsp;
			<input type='radio' name='schedEnabled' id='schedEnabled' value='N' <%= reportSchedule.getSchedEnabled().equals("N")?" checked":"" %> toolTipText="This is used for the enabling or disabling the scheduling feature for this report."/>No
			</font>
		</td>
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Email Attachment:</font></td> 
		<td class=rbg3 align="left" width="50%" valign="middle" nowrap>
			<font class=rtabletext>
			<%if(!rdefRecurrance.getReportType().equals(AppConstants.RT_HIVE)) {%>
			<input type='radio' name='notify' id='notify' value='2' <%= reportSchedule.getNotify_type().equals("2")?" checked":(reportSchedule.getNotify_type().length()<=0)?" checked":(!reportSchedule.getNotify_type().equals("4"))? " checked " :"" %> toolTipText="Provides the capability to attach reports as PDF format to the email."/>PDF Attachment
			&nbsp;&nbsp;
			<input type='radio' name='notify' id='notify' value='4' <%= reportSchedule.getNotify_type().equals("4")?" checked":"" %> toolTipText="Provides the capability to attach reports as Excel format to the email."/>Excel Attachment
			&nbsp;&nbsp;
			<% } %>
			<input type='radio' name='notify' id='notify' value='5' <%= reportSchedule.getNotify_type().equals("5")?" checked":"" %> toolTipText="Provides the capability to attach reports as Excel format to the email."/>Excelx Attachment
			&nbsp;&nbsp;
			<input type='radio' name='notify' id='notify' value='3' <%= reportSchedule.getNotify_type().equals("3")?" checked":"" %> toolTipText="Provides the capability to attach reports as CSV format to the email."/>CSV Attachment
			<%if(!rdefRecurrance.getReportType().equals(AppConstants.RT_HIVE)) {%>
  	         <% if(nvl(Globals.getShellScriptDir()).length()>1) { %>
  	        	 <input type='radio' name='notify' id='notify' value='6' <%= reportSchedule.getNotify_type().equals("6")?" checked":"" %> toolTipText="Provides the capability to send only links to the generated report in the email."/>Link to Generated report
  	         <% }  %>	 
			 <% } %>
			<input type='hidden' name='notify_type' value='1'/>
			</font>
	    </td>
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Recurrence: </font></td> 
		<td class=rbg3 align="left" width="50%"><font class=rtabletext>
			<select name="schedRecurrence" toolTipText="This is used to select recurrence frequency.">
				<%if (rdefRecurrance.getIsOneTimeScheduleAllowed() == null || rdefRecurrance.getIsOneTimeScheduleAllowed().equals("Y")){ %><option value=""                                  <%= reportSchedule.getRecurrence().equals(""                         )?" selected":"" %>>One Time<%} %>
				<%if (rdefRecurrance.getIsHourlyScheduleAllowed() == null || rdefRecurrance.getIsHourlyScheduleAllowed().equals("Y")){ %><option value="<%= AppConstants.SR_HOURLY      %>"<%= reportSchedule.getRecurrence().equals(AppConstants.SR_HOURLY     )?" selected":"" %>>Hourly<%} %>
				<%if (rdefRecurrance.getIsDailyScheduleAllowed() == null || rdefRecurrance.getIsDailyScheduleAllowed().equals("Y")){ %><option value="<%= AppConstants.SR_DAILY       %>"<%= reportSchedule.getRecurrence().equals(AppConstants.SR_DAILY      )?" selected":"" %>>Daily<%} %>
				<%if (rdefRecurrance.getIsDailyMFScheduleAllowed() == null || rdefRecurrance.getIsDailyMFScheduleAllowed().equals("Y")){ %><option value="<%= AppConstants.SR_DAILY_MO_FR %>"<%= reportSchedule.getRecurrence().equals(AppConstants.SR_DAILY_MO_FR)?" selected":"" %>>Daily Mo-Fr<%} %>
				<%if (rdefRecurrance.getIsWeeklyScheduleAllowed() == null || rdefRecurrance.getIsWeeklyScheduleAllowed().equals("Y")){ %><option value="<%= AppConstants.SR_WEEKLY      %>"<%= reportSchedule.getRecurrence().equals(AppConstants.SR_WEEKLY     )?" selected":"" %>>Weekly<%} %>
				<%if (rdefRecurrance.getIsMonthlyScheduleAllowed() == null || rdefRecurrance.getIsMonthlyScheduleAllowed().equals("Y")){ %><option value="<%= AppConstants.SR_MONTHLY     %>"<%= reportSchedule.getRecurrence().equals(AppConstants.SR_MONTHLY    )?" selected":"" %>>Monthly<%} %>
			</select></font>
	  </td>
	</tr>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>First Schedule Date: </font></td> 
		<td class=rbg3 align="left" width="50%" nowrap><font class=rtabletext>
			<input type="text" size="10" maxlength="10" name="schedStartDate" value="<%= reportSchedule.getStartDate() %>" toolTipText="Enter the date and time scheduling is to start.">
			<!--<a href="#" onClick="window.dateField = document.forma.schedStartDate;calendar = window.open('<%= AppUtils.getRaptorActionURL() %>popup.calendar','cal','WIDTH=200,HEIGHT=250');return false;">
				<img src="<%= AppUtils.getImgFolderURL() %>calender_icon.gif" align=absmiddle border=0 width="20" height="20">
			</a>-->
			<img src="<%= AppUtils.getImgFolderURL() %>calender_icon.gif" align=absmiddle border=0 width="20" height="20" onClick="oCalendar = new CalendarPopup('calendarDiv', 'calendarFrame');oCalendar.addDisabledDates(null, '<%=dtf.format(startCalendarDate.getTime())%>');oCalendar.addDisabledDates('<%=dtf.format(endCalendarDate.getTime())%>', null);oCalendar.setCssPrefix('raptor');oCalendar.select(document.getElementById('schedStartDate'),event,'MM/dd/yyyy'); return false;"  style="cursor:hand">
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
            <%= Globals.getTimeZone()%>

			</font>
		</td>
	</tr>
<!-- /quick help text -->             			
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Last Schedule Date: </font></td> 
		<td class=rbg3 align="left" width="50%" nowrap><font class=rtabletext>
			<input type="text" size="10" maxlength="10" name="schedEndDate" value="<%= reportSchedule.getEndDate() %>" toolTipText="Enter the date scheduling is to end. This is mandatory for recurring reports.">
			<!--<a href="#" onClick="window.dateField = document.forma.schedEndDate;calendar = window.open('<%= AppUtils.getRaptorActionURL() %>popup.calendar','cal','WIDTH=200,HEIGHT=250');return false;">
				<img src="<%= AppUtils.getImgFolderURL() %>calender_icon.gif" align=absmiddle border=0 width="20" height="20">
			</a>-->
			<img src="<%= AppUtils.getImgFolderURL() %>calender_icon.gif" align=absmiddle border=0 width="20" height="20" onClick="oCalendar = new CalendarPopup('calendarDiv', 'calendarFrame');oCalendar.addDisabledDates(null, '<%=dtf.format(startCalendarDate.getTime())%>');oCalendar.addDisabledDates('<%=dtf.format(endCalendarDate.getTime())%>', null);oCalendar.setCssPrefix('raptor');oCalendar.select(document.getElementById('schedEndDate'),event,'MM/dd/yyyy'); return false;"  style="cursor:hand">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<select name="schedEndHour">
				<option value="01"<%= reportSchedule.getEndHour().equals("01")?" selected":"" %>>1
				<option value="02"<%= reportSchedule.getEndHour().equals("02")?" selected":"" %>>2
				<option value="03"<%= reportSchedule.getEndHour().equals("03")?" selected":"" %>>3
				<option value="04"<%= reportSchedule.getEndHour().equals("04")?" selected":"" %>>4
				<option value="05"<%= reportSchedule.getEndHour().equals("05")?" selected":"" %>>5
				<option value="06"<%= reportSchedule.getEndHour().equals("06")?" selected":"" %>>6
				<option value="07"<%= reportSchedule.getEndHour().equals("07")?" selected":"" %>>7
				<option value="08"<%= reportSchedule.getEndHour().equals("08")?" selected":"" %>>8
				<option value="09"<%= reportSchedule.getEndHour().equals("09")?" selected":"" %>>9
				<option value="10"<%= reportSchedule.getEndHour().equals("10")?" selected":"" %>>10
				<option value="11"<%= reportSchedule.getEndHour().equals("11")?" selected":"" %>>11
				<option value="12"<%= reportSchedule.getEndHour().equals("12")?" selected":"" %>>12
			</select>
			<select name="schedEndMin">
				<option value="00"<%= reportSchedule.getEndMin().equals("00")?" selected":"" %>>00
				<option value="15"<%= reportSchedule.getEndMin().equals("15")?" selected":"" %>>15
				<option value="30"<%= reportSchedule.getEndMin().equals("30")?" selected":"" %>>30
				<option value="45"<%= reportSchedule.getEndMin().equals("45")?" selected":"" %>>45
			</select>
			<select name="schedEndAMPM">
				<option value="AM"<%= reportSchedule.getEndAMPM().equals("AM")?" selected":"" %>>AM
				<option value="PM"<%= reportSchedule.getEndAMPM().equals("PM")?" selected":"" %>>PM
			</select>
            <%= Globals.getTimeZone()%>

			</font>
		</td>
	</tr>

	<script type="text/javascript">initStartEndDate();</script>	

	<% if(AppUtils.isAdminUser(request) || isSQLAllowed ) { %>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Use Condition:</font></td> 
		<td class=rbg3 align="left" width="50%" valign="middle" nowrap><font class=rtabletext>
			<input type="checkbox" name="conditional" value="Y"<%= reportSchedule.getConditional().equals("Y")?" checked":"" %>> Send Emails Only When Condition Is Met
			</font></td>
	</tr> 
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Condition SQL:</font></td> 
		<td class=rbg3 align="left" width="50%" valign="middle" nowrap>
			<textarea name="conditionSQL" cols="40" rows="3"><%= nvl(reportSchedule.getConditionSQL()) %></textarea>
			<input type="Button" class=button value="Test" onClick="showTestConditionPopup()">
			&nbsp;
			</font></td>
	</tr> 
	<% } %>	

	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext>Max rows in attachment:</font></td> 
		<td class=rbg3 align="left" width="50%" valign="middle" nowrap>
			<input type='text' name='downloadLimit' value="<%=Integer.parseInt(reportSchedule.getDownloadLimit())>0?reportSchedule.getDownloadLimit(): Integer.toString(rdefRecurrance.getMaxRowsInExcelDownload())%>" size='4' toolTipText="Specify the maximum number of rows that can be sent in an attachment."/>
			</font>
		</td>
	</tr>
	<tr class="rbg1">
		<td class=rbg1 colspan="2" align="left">
		<b class=rtableheader>Form Fields</b>
		</td>
		      
	</tr>
	<jsp:include page="wizard_schedule_formfield_include.jsp" flush="true" />
	<tr>
		<td colspan=4><hr></hr></td>
	</tr>
	<input type="hidden" name="toListUpdated" value="false" />
	
<% 	List emailToUsers = reportSchedule.getEmailToUsers();
	for(int i=0; i<emailToUsers.size(); i++) { 
		IdNameValue userValue = (IdNameValue) emailToUsers.get(i); %>
	<tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext><%= (i==0)?"Email To: ":"&nbsp;" %></font></td> 
		<td class=rbg3 align="left" width="50%" nowrap><font class=rtabletext>
			<%= userValue.getName() %>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>deleteicon.gif" alt="Remove" width="12" height="12" onClick="if(! dataValidate()) {return false;} else if(! confirm('Are you sure?')) {return false;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE_USER %>'; document.all.toListUpdated.value='true'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= userValue.getId() %>'; }">
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
			<input type=image border="0" src="<%= AppUtils.getImgFolderURL() %>deleteicon.gif" alt="Remove" width="12" height="12" onClick="if(! dataValidate()) {return false;} else if(! confirm('Are you sure?')) {return false;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE_ROLE %>'; document.all.toListUpdated.value='true'; document.forma.<%= AppConstants.RI_DETAIL_ID %>.value='<%= roleValue.getId() %>'; }">
			</font></td>
	</tr>
<%	}	// for 
	
	Vector remainingUsers = Utils.getUsersNotInList(emailToUsers,request);
	Vector remainingRoles = Utils.getRolesNotInList(emailToRoles,request);
	if((emailToUsers.size()+emailToRoles.size()==0)||(remainingUsers.size()>0)||(remainingRoles.size()>0)) { %>
	<tr>
	  <td colspan="2" align="center">
		<table width="100%" cellspacing="1" cellpadding="3" align="center" border="0"> <tr>
		<td class=rbg2 align="right" width="25%" height="30"><font class=rtabletext><%= (emailToUsers.size()+emailToRoles.size()==0)?"Email To: ":"&nbsp;" %></font></td> 
		<td class=rbg3 align="left" width="50%" nowrap><font class=rtabletext>
<%		if(remainingUsers.size()>0) { %>
			<select name="schedEmailAdd" onChange="if(! dataValidate()) {selectedIndex=0;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ADD_USER %>'; document.all.toListUpdated.value='true'; document.forma.submit();}" toolTipText="Select users and/or roles the report should be sent to">
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
			<select name="schedEmailAddRole" onChange="if(! dataValidate()) {selectedIndex=0;} else {document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_ADD_ROLE %>'; document.all.toListUpdated.value='true'; document.forma.submit();}" toolTipText="Select users and/or roles the report should be sent to">
			    <option value="" selected>--- Select Role ---
<% 			for(int i=0; i<remainingRoles.size(); i++) {
				IdNameValue roleValue = (IdNameValue) remainingRoles.get(i); %>
			    <option value="<%= roleValue.getId() %>"><%= roleValue.getName() %>
<% 			}	// for %>
			</select>
<%		} else { %>
			No roles available
<%		} %>
			</font>
	  </td>
	  <% if ( nvl(Globals.getEncryptedSMTPServer(),"").length() > 0 ) { %>
	  <td align="right" width="27%"><font class=rtabletext>Encrypt Attachment</font>
			<input type='radio' name='encryptMode' id='encryptMode' value='Y' <%= reportSchedule.getEncryptMode().equals("Y")?"":" checked" %> toolTipText="Choose the encryption mode."/>Yes
			&nbsp;&nbsp;
			<input type='radio' name='encryptMode' id='encryptMode' value='N' <%= reportSchedule.getEncryptMode().equals("N")?"":(reportSchedule.getEncryptMode().length()<=0)?" checked":(!reportSchedule.getEncryptMode().equals("N"))? " " :" checked " %> toolTipText="Choose the encryption mode."/>No
	  </td>
	  <% } %>
	  <% if (Globals.generateSchedReportsInFileSystem()) { %>
	  <td align="right" width="27%"><font class=rtabletext>Send as Attachment</font>
			<input type='radio' name='sendAttachment' id='sendAttachment' value='Y' <%= reportSchedule.isAttachmentMode()?" checked ":""%> toolTipText="Send As Attachment"/>Yes
			&nbsp;&nbsp;
			<input type='radio' name='sendAttachment' id='sendAttachment' value='N' <%= !reportSchedule.isAttachmentMode()?" checked":"" %> toolTipText="Store it in file system."/>No
	  </td>
	  <% } %>
	   </tr>
		</table>
		</td>	  
	</tr>
<%	}	// if 
%>
</table>


<br>

<script language="JavaScript">
<!--
function dataValidate() {
	//alert("dataValidate called");
	var startDate = new Date(document.forma.schedStartDate.value);
	var startHour = eval(document.forma.schedHour.value);
	if(document.forma.schedAMPM.value == 'PM') {
		if (startHour != 12) startHour = startHour + 12; 
	} else {
		if (startHour == 12) startHour = startHour - 12;
	}
	startDate.setHours(startHour);
	startDate.setMinutes(eval(document.forma.schedMin.value));

	var endDate = new Date(document.forma.schedEndDate.value);
	var endHour = eval(document.forma.schedEndHour.value);
	if(document.forma.schedEndAMPM.value == 'PM') {
		if (endHour != 12) endHour = endHour + 12; 
	} else {
		if (endHour == 12) endHour = endHour - 12;
	}
	endDate.setHours(endHour);
	endDate.setMinutes(eval(document.forma.schedEndMin.value));
	
    //alert ("System Calendar " + "<%=systemCalendar.get(Calendar.YEAR)+ " " + (systemCalendar.get(Calendar.MONTH)+1)+ " " +  systemCalendar.get(Calendar.DAY_OF_MONTH) + " " +  systemCalendar.get(Calendar.HOUR_OF_DAY) + " " + systemCalendar.get(Calendar.MINUTE) + " " + systemCalendar.get(Calendar.SECOND)%>");
	var currDate = new Date();
	var curr1Date = new Date();
	currDate.setFullYear(<%=systemCalendar.get(Calendar.YEAR)%>, <%=systemCalendar.get(Calendar.MONTH)%>,<%=systemCalendar.get(Calendar.DAY_OF_MONTH)%>);
	currDate.setHours(<%=systemCalendar.get(Calendar.HOUR_OF_DAY)%>);
	currDate.setMinutes(<%=systemCalendar.get(Calendar.MINUTE)%>);
	currDate.setSeconds(<%=systemCalendar.get(Calendar.SECOND)%>);

	//alert(startDate+ " " + currDate + " " + (startDate-currDate) + " " + (startDate.toString()==currDate.toString()));
 	

	if(! checkDate(document.forma.schedStartDate.value, false)) {
		alert("Invalid Start Date. The date format should be MM/DD/YYYY");
		document.forma.schedStartDate.focus();
		document.forma.schedStartDate.select();
		return false;
	}
	if(! checkDate(document.forma.schedEndDate.value, false)) {
		alert("Invalid End Date. The date format should be MM/DD/YYYY");
		document.forma.schedEndDate.focus();
		document.forma.schedEndDate.select();
		return false;
	}
	if (startDate >= endDate){
			alert("Start Date has to be less than the End Date.");
		return false;
	}

	if (startDate - currDate < 0){
		if(startDate.toString()!=currDate.toString()) {
			var m_names = new Array("January", "February", "March", 
					"April", "May", "June", "July", "August", "September", 
					"October", "November", "December");
            var curr_min = currDate.getMinutes();
			curr_min = curr_min + "";
			if (curr_min.length == 1) {
				curr_min = "0" + curr_min;
			}  
			alert("Start Date/Time has to be greater or equal to the System Date/Time ("+m_names[currDate.getMonth()]+" "+ currDate.getDate() + ", " + currDate.getYear() + " " + currDate.getHours()+ ":" + curr_min+ ").");
			return false;
		}
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

<script type="text/javascript">
var tooltipObj = new DHTMLgoodies_formTooltip();
tooltipObj.setTooltipPosition('right');
tooltipObj.setPageBgColor('#EEEEEE');
//tooltipObj.setPageBgColor('#FFFFFF');
tooltipObj.setTooltipCornerSize(15);
tooltipObj.setTooltipBgColor("#99CCFF");
tooltipObj.initFormFieldTooltip();
</script>


<%!
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } 
%>
